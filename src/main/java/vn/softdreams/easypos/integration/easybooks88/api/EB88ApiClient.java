package vn.softdreams.easypos.integration.easybooks88.api;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import vn.softdreams.easypos.constants.ConfigCode;
import vn.softdreams.easypos.constants.EasyInvoiceConstants;
import vn.softdreams.easypos.constants.ResultConstants;
import vn.softdreams.easypos.domain.Config;
import vn.softdreams.easypos.dto.epPackage.EBPackageSaveRequest;
import vn.softdreams.easypos.dto.rsinoutward.RsInOutWardDeleteRequest;
import vn.softdreams.easypos.dto.user.ChangePasswordRequest;
import vn.softdreams.easypos.integration.easybooks88.api.dto.*;
import vn.softdreams.easypos.integration.easybooks88.queue.dto.EBChangePasswordRequest;
import vn.softdreams.easypos.integration.easybooks88.util.Utils;
import vn.softdreams.easypos.repository.ConfigRepository;
import vn.softdreams.easypos.service.UserService;
import vn.softdreams.easypos.util.Util;
import vn.softdreams.easypos.web.rest.errors.BadRequestAlertException;
import vn.softdreams.easypos.web.rest.errors.IntegrationException;
import vn.softdreams.easypos.web.rest.errors.InternalServerException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.validation.Validator;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EB88ApiClient {
    static {
        try {
            TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {}

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                },
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, sslSession) -> true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final Logger log = LoggerFactory.getLogger(EB88ApiClient.class);
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final ConfigRepository configRepository;
    private final UserService userService;
    //    private final String EB88_BASE_URL = "https://app88.easybooks.vn";
    private final String EB88_BASE_URL = "http://test88.easybooks.vn";
    //    private final String EB88_BASE_URL = "http://localhost:8082";
    private final Validator validator;
    private final String CLIENT_ID = "EasyPOSClient";
    private final String CLIENT_SECRET = "EasyPOS@2o23#";

    public EB88ApiClient(
        RestTemplate restTemplate,
        ObjectMapper objectMapper,
        ConfigRepository configRepository,
        UserService userService,
        Validator validator
    ) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.configRepository = configRepository;
        this.userService = userService;
        this.validator = validator;
    }

    public volatile Map<Integer, String> eb88GlobalJWT = new ConcurrentHashMap<>();

    public HttpHeaders initTestHeaders() {
        HttpHeaders headers = initCommonHeaders();
        String testToken =
            "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkZW1vIiwib3JnIjoiNzM5Iiwib3JnR2V0RGF0YSI6IjczOSIsInllYXJXb3JrIjoiMjAyMyIsImlzRGVwZW5kZW50IjoiIiwiYXV0aCI6IiIsImV4cCI6MTY3ODQzMzk1NiwiaWF0IjoxNjc4MzQ3NTU2fQ.jln-ESToaOAgzo5d2d58igAp3s2ElMNpSmu44MkkeMjVIqblVfOdDSN1XtNGLVU1LoBJjNgGvcXr14vLvDJC6w";
        headers.setBearerAuth(testToken);
        return headers;
    }

    public HttpHeaders initCommonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("User-Agent", "Mozilla/5.0 Firefox/26.0");
        return headers;
    }

    // Token coi như hết hạn nếu chỉ hơn thời điểm hiện tại 10min, tránh lỗi hết hạn ngay khi đang gọi
    private boolean isJWTExpired(DecodedJWT decodedJWT) {
        Date expiresAt = decodedJWT.getExpiresAt();
        long now = (new Date()).getTime();
        return expiresAt.before(new Date(now + (10 * 60 * 1000)));
    }

    private void validateInput(Object o) throws Exception {
        Set<String> violations = Utils.validateInput(validator, o);
        if (violations.isEmpty()) return;
        String errorMessage = String.join(",", violations);
        log.error("EB88 Api Client validate input failed: {}", errorMessage);
        throw new Exception("EB88 Api Client validate input failed: " + errorMessage);
    }

    // Với mỗi user đang gọi request từ mobile, sẽ dùng thông tin user đó để tạo JWT tương ứng ở EB88
    public HttpHeaders initAuthHeadersForUser(int comId) throws Exception {
        // Check có jwt cached hay chưa, hoặc nếu có thì jwt đã hết hạn hay chưa
        boolean doAuth =
            !eb88GlobalJWT.containsKey(comId) ||
            Strings.isNullOrEmpty(eb88GlobalJWT.get(comId)) ||
            isJWTExpired(JWT.decode(eb88GlobalJWT.get(comId)));

        HttpHeaders headers = initCommonHeaders();

        if (doAuth) {
            // Thực hiện authenticate lại để lấy JWT
            List<String> findingCodes = List.of(ConfigCode.EB88_DEFAULT_USER.getCode(), ConfigCode.EB88_COM_ID.getCode());
            List<Config> configList = configRepository.getAllByCompanyID(comId, findingCodes);
            String ebUsername = "";
            long ebOrgId = 0L;
            for (Config config : configList) {
                if (config.getCode().equals(ConfigCode.EB88_DEFAULT_USER.getCode())) ebUsername = config.getValue();
                if (config.getCode().equals(ConfigCode.EB88_COM_ID.getCode())) ebOrgId = Long.parseLong(config.getValue());
            }
            if (Strings.isNullOrEmpty(ebUsername)) throw new Exception("Cannot found default username for eb88 api with comId = " + comId);
            String url = EB88_BASE_URL + "/api/authenticate/third-party";
            if (ebOrgId == 0L) throw new Exception("Can not found eb88_com_id with pos comId = " + comId);

            AuthenticateRequest request = new AuthenticateRequest();
            request.setOrgId(ebOrgId);
            request.setUsername(ebUsername);
            request.setClientId(CLIENT_ID);
            request.setClientSecret(CLIENT_SECRET);
            validateInput(request);

            HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(request), headers);
            ResponseEntity<AuthenticateResponse> response;
            try {
                response = restTemplate.postForEntity(url, requestEntity, AuthenticateResponse.class);
            } catch (Exception e) {
                log.error("Authenticate to EB88 failed: {}", e.getMessage());
                throw new Exception("Authenticate to EB88 failed: " + e.getMessage());
            }
            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("Authenticate to EB88 failed, response code: {}", response.getStatusCode());
                throw new Exception("Authenticate to EB88 failed, response code: " + response.getStatusCode());
            }
            AuthenticateResponse authResponse = response.getBody();
            if (authResponse == null) {
                log.error("Not found eb88 authenticated token, null auth response from EBn");
                throw new Exception("Not found eb88 authenticated token, null auth response from EB");
            }

            eb88GlobalJWT.put(comId, authResponse.getToken());
            // Set bearer token, the value does not include the prefix 'Bearer '
            headers.setBearerAuth(authResponse.getToken());
        } else {
            // Nếu không cần auth lại, lấy từ cache
            headers.setBearerAuth(eb88GlobalJWT.get(comId));
        }

        return headers;
    }

    /**
     * Tạo mới công ty, người dùng admin bên EB88, cùng với gói dịch vụ mặc định
     * POST /api/easypos-integration/register
     * Không yêu cầu authe
     */
    public RegisterCompanyResponse registerCompany(RegisterCompanyRequest request) {
        HttpHeaders commonHeaders = initCommonHeaders();
        String url = EB88_BASE_URL + "/api/easypos-integration/register";
        ResponseEntity<RegisterCompanyResponse> response = null;
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(request), commonHeaders);
            log.debug("Request createCompanyEB: " + requestEntity.getBody());
            response = restTemplate.postForEntity(url, requestEntity, RegisterCompanyResponse.class);
        } catch (Exception e) {
            log.error("registerCompany failed: {}", e.getMessage());
            String value = "";
            if (e instanceof HttpStatusCodeException) {
                HttpStatusCodeException ex = (HttpStatusCodeException) e;
                value =
                    ex.getResponseBodyAsString().contains("message")
                        ? new JSONObject(ex.getResponseBodyAsString()).get("message").toString()
                        : ResultConstants.ERROR;
                if (value.contains("MST đã tồn tại")) {
                    throw new BadRequestAlertException(value, "registerCompany failed: " + value, "EB88_ERROR_TAX_CODE");
                }
            } else {
                value = e.getMessage();
            }
            throw new InternalServerException(value, "registerCompany failed: " + value, "EB88_ERROR");
        }
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("registerCompany failed, response status: {}", response.getStatusCode());
            throw new InternalServerException(
                response.getStatusCode().toString(),
                "registerCompany failed, response status: " + response.getStatusCode(),
                "EB88_EROR"
            );
        }
        return response.getBody();
    }

    /**
     * Lấy về danh mục đơn vị tính
     * Luôn fix lấy 1000 item, như ở tài liệu mô tả
     */
    public List<UnitEb88Response> getUnits(int comId) throws Exception {
        HttpHeaders authHeaders = initAuthHeadersForUser(comId);
        String url = EB88_BASE_URL + "/api/units?page=0&size=1000&searchCategory={}";
        HttpEntity<Void> requestEntity = new HttpEntity<>(authHeaders);
        ResponseEntity<UnitEb88Response[]> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, UnitEb88Response[].class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new Exception("getUnits failed");
        }
        return List.of(response.getBody());
    }

    /**
     * Tạo mới đơn hàng ở EasyBooks 88
     * Tương ứng với chức năng Hoàn thành đơn hàng ở EasyPOS: đơn hàng có thể thanh toán rồi, hoặc ghi vào công nợ.
     * Tạo mới đơn hàng, có thể kèm theo phiếu thu, phiếu xuất kho
     * POST /api/easypos-integration/sa-invoice
     */
    public CommonResponse createSAInvoice(int comId, CreateSAInvoiceRequest request) throws Exception {
        validateInput(request);
        HttpHeaders authHeaders = initAuthHeadersForUser(comId);
        String url = EB88_BASE_URL + "/api/easypos-integration/sa-invoice";
        HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(request), authHeaders);
        ResponseEntity<CommonResponse> response;
        try {
            response = restTemplate.postForEntity(url, requestEntity, CommonResponse.class);
        } catch (Exception e) {
            log.error("createSAInvoice failed: {}", e.getMessage());
            throw new Exception("createSAInvoice failed: " + e.getMessage());
        }
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("createSAInvoice failed, response status: {}", response.getStatusCode());
            throw new Exception("createSAInvoice failed, response status: " + response.getStatusCode());
        }
        return response.getBody();
    }

    /**
     * Hủy đơn hàng đã hoàn thành ở EasyPOS, tức là đã đồng bộ sang EB88;
     * Bỏ ghi sổ đơn hàng, phiếu thu, phiếu xuất kho tương ứng ở EB88
     * PUT /api/easypos-integration/sa-invoice/un-record
     */
    public CommonResponse cancelSAInvoice(int comId, CancelSAInvoiceRequest request) throws Exception {
        validateInput(request);
        HttpHeaders authHeaders = initAuthHeadersForUser(comId);
        String url = EB88_BASE_URL + "/api/easypos-integration/sa-invoice/un-record";
        HttpEntity<String> requestEntity;
        try {
            requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(request), authHeaders);
        } catch (Exception e) {
            log.error("cancelSAInvoice failed: {}", e.getMessage());
            throw new Exception("cancelSAInvoice failed: " + e.getMessage());
        }
        ResponseEntity<CommonResponse> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, CommonResponse.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("cancelSAInvoice failed, response status: {}", response.getStatusCode());
            throw new Exception("cancelSAInvoice failed, response status: " + response.getStatusCode());
        }
        return response.getBody();
    }

    /**
     * Tạo mới một hoặc nhiều sản phẩm, có thể bao gồm thông tin giá mua, và thông tin tồn kho
     * POST /api/easypos-integration/material-goods/
     */
    public CreateMaterialGoodResponse createMaterialGoods(int comId, CreateMaterialGoodsRequest request) throws Exception {
        validateInput(request);
        HttpHeaders authHeaders = initAuthHeadersForUser(comId);
        String url = EB88_BASE_URL + "/api/easypos-integration/material-goods";
        HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(request), authHeaders);
        ResponseEntity<CreateMaterialGoodResponse> response;
        try {
            response = restTemplate.postForEntity(url, requestEntity, CreateMaterialGoodResponse.class);
        } catch (Exception e) {
            log.error("createMaterialGoods failed: {}", e.getMessage());
            throw new Exception("createMaterialGoods failed: " + e.getMessage());
        }
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("createMaterialGoods failed, response status: {}", response.getStatusCode());
            throw new Exception("createMaterialGoods failed, response status: " + response.getStatusCode());
        }
        return response.getBody();
    }

    /**
     * Cập nhật thông tin cho một hoặc nhiều sản phẩm đã tạo từ trước theo mã sản phẩm; có thể cập nhật điều chỉnh giá mua và số lượng tồn kho.
     * PUT /api/easypos-integration/material-goods
     */
    public CommonResponse updateMaterialGoods(int comId, UpdateMaterialGoodsRequest request) throws Exception {
        validateInput(request);
        HttpHeaders authHeaders = initAuthHeadersForUser(comId);
        String url = EB88_BASE_URL + "/api/easypos-integration/material-goods";
        HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(request), authHeaders);
        ResponseEntity<CommonResponse> response;
        try {
            response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, CommonResponse.class);
        } catch (Exception e) {
            log.error("updateMaterialGoods failed: {}", e.getMessage());
            throw new Exception("updateMaterialGoods failed: " + e.getMessage());
        }
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("updateMaterialGoods failed, response status: {}", response.getStatusCode());
            throw new Exception("updateMaterialGoods failed, response status: " + response.getStatusCode());
        }
        return response.getBody();
    }

    /**
     * Tạo mới cho một hoặc nhiều khách hàng, nhà cung cấp; có thể tạo kèm công nợ nếu có.
     * PUT /api/easypos-integration/accounting-object
     */
    public CommonResponse createAccountingObject(int comId, List<CreateAccountingObjectRequest> request) throws Exception {
        validateInput(request);
        HttpHeaders authHeaders = initAuthHeadersForUser(comId);
        String url = EB88_BASE_URL + "/api/easypos-integration/accounting-object/";
        HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(request), authHeaders);
        ResponseEntity<CommonResponse> response;
        try {
            response = restTemplate.postForEntity(url, requestEntity, CommonResponse.class);
        } catch (Exception e) {
            log.error("createCustomer failed: {}", e.getMessage());
            throw new Exception("createCustomer failed: " + e.getMessage());
        }
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("createCustomer failed, response status: {}", response.getStatusCode());
            throw new Exception("createCustomer failed, response status: " + response.getStatusCode());
        }
        return response.getBody();
    }

    /**
     * Tạo mới cho một hoặc nhiều khách hàng, nhà cung cấp; có thể tạo kèm công nợ nếu có.
     * PUT /api/easypos-integration/accounting-object
     */
    public CommonResponse updateAccountingObject(int comId, List<UpdateAccountingObjectRequest> request) throws Exception {
        validateInput(request);
        HttpHeaders authHeaders = initAuthHeadersForUser(comId);
        String url = EB88_BASE_URL + "/api/easypos-integration/accounting-object/";
        HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(request), authHeaders);
        ResponseEntity<CommonResponse> response;
        try {
            response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, CommonResponse.class);
        } catch (Exception e) {
            log.error("updateCustomer failed: {}", e.getMessage());
            throw new Exception("updateCustomer failed: " + e.getMessage());
        }
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("updateCustomer failed, response status: {}", response.getStatusCode());
            throw new Exception("updateCustomer failed, response status: " + response.getStatusCode());
        }
        return response.getBody();
    }

    /**
     * Đăng nhập
     * POST /api/authenticate/third-party
     */
    public LoginResponse login(LoginRequest request) throws Exception {
        validateInput(request);
        HttpHeaders authHeaders = initCommonHeaders();
        String url = EB88_BASE_URL + "/api/authenticate/third-party";
        HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(request), authHeaders);
        ResponseEntity<LoginResponse> response = restTemplate.postForEntity(url, requestEntity, LoginResponse.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("Login failed");
            throw new Exception("Login failed, response status: " + response.getStatusCode());
        }
        return response.getBody();
    }

    /**
     * Báo cáo - Thống kê dòng tiền
     * GET /api/trang-chu/bieu-do-dong-tien?fromDate=2023-02-01&toDate=2023-02-28
     */
    public GetCashFlowReportResponse getCashFlowReport(int comId, String fromDate, String toDate) throws Exception {
        HttpHeaders authHeaders = initAuthHeadersForUser(comId);
        if (Utils.isValid(fromDate) && Utils.isValid(toDate)) {
            HttpEntity<String> requestEntity = new HttpEntity<>(authHeaders);
            ResponseEntity<GetCashFlowReportResponse> response = restTemplate.exchange(
                EB88_BASE_URL + "/api/trang-chu/bieu-do-dong-tien?fromDate={fromDate}&toDate={toDate}",
                HttpMethod.GET,
                requestEntity,
                GetCashFlowReportResponse.class,
                fromDate,
                toDate
            );
            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("GetCashFlow failed");
                throw new Exception("GetCashFlow failed");
            }
            return response.getBody();
        } else {
            log.error("Invalid FromDate or ToDate");
            throw new Exception("Invalid FromDate or ToDate");
        }
    }

    /**
     * Báo cáo - Thống kê doanh thu
     * GET /api/trang-chu/bieu-do-doanh-thu?fromDate=2023-01-01&toDate=2023-03-31
     *
     * @param fromDate
     * @param toDate
     */
    public GetSalesReportResponse[] getSalesReport(String fromDate, String toDate) throws Exception {
        HttpHeaders authHeaders = initTestHeaders();
        if (Utils.isValid(fromDate) && Utils.isValid(toDate)) {
            HttpEntity<String> requestEntity = new HttpEntity<>(authHeaders);
            ResponseEntity<GetSalesReportResponse[]> response = restTemplate.exchange(
                EB88_BASE_URL + "/api/trang-chu/bieu-do-doanh-thu?fromDate={fromDate}&toDate={toDate}",
                HttpMethod.GET,
                requestEntity,
                GetSalesReportResponse[].class,
                fromDate,
                toDate
            );
            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("GetSalesReport failed");
                throw new Exception("GetSalesReport failed");
            }
            return response.getBody();
        } else {
            log.error("Invalid FromDate or ToDate");
            throw new Exception("Invalid FromDate or ToDate");
        }
    }

    /**
     * Báo cáo - Thống kê lợi nhuận
     * GET /api/trang-chu/bieu-do-loi-nhuan?fromDate=2023-01-01&toDate=2023-03-31
     *
     * @param fromDate
     * @param toDate
     */
    public GetProfitReportResponse[] getProfitReport(String fromDate, String toDate) throws Exception {
        HttpHeaders authHeaders = initTestHeaders();
        if (Utils.isValid(fromDate) && Utils.isValid(toDate)) {
            HttpEntity<String> requestEntity = new HttpEntity<>(authHeaders);
            ResponseEntity<GetProfitReportResponse[]> response = restTemplate.exchange(
                EB88_BASE_URL + "/api/trang-chu/bieu-do-loi-nhuan?fromDate={fromDate}&toDate={toDate}",
                HttpMethod.GET,
                requestEntity,
                GetProfitReportResponse[].class,
                fromDate,
                toDate
            );
            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("GetProfit failed");
                throw new Exception("GetProfit failed");
            }
            return response.getBody();
        } else {
            log.error("Invalid FromDate or ToDate");
            throw new Exception("Invalid FromDate or ToDate");
        }
    }

    /**
     * Báo cáo - Thống kê hàng tồn kho
     * GET /api/trang-chu/bieu-do-hang-ton-kho?fromDate=2023-01-01&toDate=2023-03-31
     *
     * @param fromDate
     * @param toDate
     */
    public GetInventoryReportResponse[] getInventoryReport(int comId, String fromDate, String toDate) throws Exception {
        HttpHeaders authHeaders = initAuthHeadersForUser(comId);
        if (Utils.isValid(fromDate) && Utils.isValid(toDate)) {
            HttpEntity<String> requestEntity = new HttpEntity<>(authHeaders);
            ResponseEntity<GetInventoryReportResponse[]> response = restTemplate.exchange(
                EB88_BASE_URL + "/api/trang-chu/bieu-do-hang-ton-kho?fromDate={fromDate}&toDate={toDate}",
                HttpMethod.GET,
                requestEntity,
                GetInventoryReportResponse[].class,
                fromDate,
                toDate
            );
            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("GetInventory failed");
                throw new IntegrationException(IntegrationException.Party.Easybooks88, "GetInventory failed");
            }
            return response.getBody();
        } else {
            log.error("Invalid FromDate or ToDate");
            throw new IntegrationException(IntegrationException.Party.Easybooks88, "Invalid FromDate or ToDate");
        }
    }

    /**
     * Báo cáo chi tiết doanh thu nguồn vốn theo sản phẩm
     * POST /api/business/report/get-data-preview
     *
     * @param request
     * @return
     * @throws Exception
     */
    public GetSalesDetailReportResponse getSalesDetailReport(GetSalesDetailReportRequest request) throws Exception {
        validateInput(request);
        HttpHeaders authHeaders = initTestHeaders();
        String url = EB88_BASE_URL + "/api/business/report/get-data-preview";
        HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(request), authHeaders);
        ResponseEntity<GetSalesDetailReportResponse> response = restTemplate.postForEntity(
            url,
            requestEntity,
            GetSalesDetailReportResponse.class
        );
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("GetSalesReportDetail failed");
            throw new Exception("GetSalesReportDetail failed");
        }
        return response.getBody();
    }

    /**
     * Báo cáo tổng hợp tồn kho
     * POST /api/business/report/get-data-preview
     *
     * @param request
     * @return GetInventoryDetailReportResponse
     * @throws Exception
     */
    public GetInventoryDetailReportResponse getInventoryDetailReport(GetInventoryDetailReportRequest request) throws Exception {
        validateInput(request);
        HttpHeaders authHeaders = initTestHeaders();
        String url = EB88_BASE_URL + "/api/business/report/get-data-preview";
        HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(request), authHeaders);
        ResponseEntity<GetInventoryDetailReportResponse> response = restTemplate.postForEntity(
            url,
            requestEntity,
            GetInventoryDetailReportResponse.class
        );
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("GetSalesReportDetail failed");
            throw new Exception("GetSalesReportDetail failed");
        }
        return response.getBody();
    }

    public List<GetSalesReportResponse[]> getRevenueStats(int comId, String fromDate, String toDate) throws Exception {
        HttpHeaders authHeaders = initAuthHeadersForUser(comId);
        HttpEntity<String> requestEntity = new HttpEntity<>(authHeaders);
        List<GetSalesReportResponse[]> responses = new ArrayList<>();
        ResponseEntity<GetSalesReportResponse[]> salesResponse;
        ResponseEntity<GetSalesReportResponse[]> profitResponse;
        try {
            salesResponse =
                restTemplate.exchange(
                    EB88_BASE_URL + "/api/trang-chu/bieu-do-doanh-thu?fromDate={fromDate}&toDate={toDate}",
                    HttpMethod.GET,
                    requestEntity,
                    GetSalesReportResponse[].class,
                    fromDate,
                    toDate
                );
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new IntegrationException(IntegrationException.Party.Easybooks88, "Không thống kê được doanh thu");
        }
        if (!salesResponse.getStatusCode().is2xxSuccessful()) {
            log.error("GetSalesReport failed");
            throw new IntegrationException(IntegrationException.Party.Easybooks88, "Không thống kê được doanh thu");
        }
        responses.add(salesResponse.getBody());
        try {
            profitResponse =
                restTemplate.exchange(
                    EB88_BASE_URL + "/api/trang-chu/bieu-do-loi-nhuan?fromDate={fromDate}&toDate={toDate}",
                    HttpMethod.GET,
                    requestEntity,
                    GetSalesReportResponse[].class,
                    fromDate,
                    toDate
                );
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new IntegrationException(IntegrationException.Party.Easybooks88, "Không thống kê được lợi nhuận");
        }
        if (!profitResponse.getStatusCode().is2xxSuccessful()) {
            log.error("GetProfitReport failed");
            throw new IntegrationException(IntegrationException.Party.Easybooks88, "Không thống kê được lợi nhuận");
        }
        responses.add(profitResponse.getBody());

        return responses;
    }

    public GetSalesDetailReportResponse getRevenueCostPrice(int comId, GetSalesDetailReportRequest request) throws Exception {
        HttpHeaders authHeaders = initAuthHeadersForUser(comId);
        HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(request), authHeaders);
        ResponseEntity<GetSalesDetailReportResponse> response;
        try {
            response =
                restTemplate.postForEntity(
                    EB88_BASE_URL + "/api/business/report/get-data-preview",
                    requestEntity,
                    GetSalesDetailReportResponse.class
                );
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new IntegrationException(IntegrationException.Party.Easybooks88, "Không thống kê được doanh thu nguồn vốn theo sản phẩm");
        }
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("getRevenueCostPrice failed");
            throw new IntegrationException(IntegrationException.Party.Easybooks88, "Không thống kê được doanh thu nguồn vốn theo sản phẩm");
        }
        return response.getBody();
    }

    public GetInventoryDetailReportResponse getGeneralInventory(int comId, GetInventoryDetailReportRequest request) throws Exception {
        HttpHeaders authHeaders = initAuthHeadersForUser(comId);
        HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(request), authHeaders);
        ResponseEntity<GetInventoryDetailReportResponse> response;
        try {
            response =
                restTemplate.postForEntity(
                    EB88_BASE_URL + "/api/business/report/get-data-preview",
                    requestEntity,
                    GetInventoryDetailReportResponse.class
                );
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new IntegrationException(IntegrationException.Party.Easybooks88, "Không thống kê được tổng hợp tồn kho");
        }
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("getGeneralInventory failed");
            throw new IntegrationException(IntegrationException.Party.Easybooks88, "Không thống kê được tổng hợp tồn kho");
        }
        return response.getBody();
    }

    public Long createNewUnit(int comId, CreateUnitRequest request) throws Exception {
        HttpHeaders authHeaders = initAuthHeadersForUser(comId);
        HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(request), authHeaders);
        ResponseEntity<CreateUnitResponse> response;
        try {
            response = restTemplate.postForEntity(EB88_BASE_URL + "/api/units", requestEntity, CreateUnitResponse.class);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new IntegrationException(IntegrationException.Party.Easybooks88, "Không thể tạo mới đơn vị tính");
        }
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null || response.getBody().getUnit() == null) {
            log.error("createNewUnit failed");
            throw new IntegrationException(IntegrationException.Party.Easybooks88, "Không thể tạo mới đơn vị tính");
        }
        return response.getBody().getUnit().getId();
    }

    public CommonResponse changePassword(Integer comId, ChangePasswordRequest request) throws Exception {
        HttpHeaders authHeaders = initAuthHeadersForUser(comId);
        String url = EB88_BASE_URL + "/api/account/change-password";
        EBChangePasswordRequest changePasswordSendRequest = new EBChangePasswordRequest();
        changePasswordSendRequest.setCurrentPassword(request.getOldPassword());
        changePasswordSendRequest.setNewPassword(request.getNewPassword());
        changePasswordSendRequest.setConfirmNewPassword(request.getConfirmPassword());
        changePasswordSendRequest.setLogoutAll(null);
        HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(changePasswordSendRequest), authHeaders);
        ResponseEntity<CommonResponse> response;
        try {
            response = restTemplate.postForEntity(url, requestEntity, CommonResponse.class);
        } catch (Exception e) {
            log.error("Change Password failed: {}", e.getMessage());
            throw new Exception("Change Password failed: " + e.getMessage());
        }
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("Change Password failed, response status: {}", response.getStatusCode());
            throw new Exception("Change Password failed, response status: " + response.getStatusCode());
        }
        return response.getBody();
    }

    /**
     * Tạo mới chứng từ nhập kho
     * POST /api/easypos-integration/rs-inward-outward
     */
    public RsInOutWardResponse createRsInWard(int comId, RsInWardRequest request) throws Exception {
        validateInput(request);
        HttpHeaders authHeaders = initAuthHeadersForUser(comId);
        String url = EB88_BASE_URL + "/api/easypos-integration/rs-inward-outward";
        ResponseEntity<RsInOutWardResponse> response = null;
        try {
            String data = objectMapper.writeValueAsString(request);
            log.debug("Request create rsInOutWard to EB: " + data);
            HttpEntity<String> requestEntity = new HttpEntity<>(data, authHeaders);
            response = restTemplate.postForEntity(url, requestEntity, RsInOutWardResponse.class);
        } catch (Exception e) {
            log.error("createRsInWard failed: {}", e.getMessage());
            String value = "";
            if (e instanceof HttpStatusCodeException) {
                HttpStatusCodeException ex = (HttpStatusCodeException) e;
                value =
                    ex.getResponseBodyAsString().contains("message")
                        ? new JSONObject(ex.getResponseBodyAsString()).get("message").toString()
                        : ResultConstants.ERROR;
            } else {
                value = e.getMessage();
            }
            throw new BadRequestAlertException(value, "createRsInWard failed: " + value, "EB88_ERROR");
        }
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("createRsInWard failed, response status: {}", response.getStatusCode());
            throw new BadRequestAlertException(
                response.getStatusCode().toString(),
                "createRsInWard failed, response status: " + response.getStatusCode(),
                "EB88_ERROR"
            );
        }
        return response.getBody();
    }

    public CommonResponse deleteRsInWard(Integer comId, RsInOutWardDeleteRequest request) throws Exception {
        validateInput(request);
        HttpHeaders authHeaders = initAuthHeadersForUser(comId);
        String url = EB88_BASE_URL + "/api/page/rs-inoutward/delete";
        ResponseEntity<CommonResponse> response = null;
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(request), authHeaders);
            response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, CommonResponse.class);
        } catch (Exception e) {
            log.error("deleteRsInWard failed: {}", e.getMessage());
            String value = "";
            if (e instanceof HttpStatusCodeException) {
                HttpStatusCodeException ex = (HttpStatusCodeException) e;
                value =
                    ex.getResponseBodyAsString().contains("message")
                        ? new JSONObject(ex.getResponseBodyAsString()).get("message").toString()
                        : ResultConstants.ERROR;
            } else {
                value = e.getMessage();
            }
            throw new BadRequestAlertException(value, "deleteRsInWard failed: " + value, "EB88_ERROR");
        }
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("deleteRsInWard failed, response status: {}", response.getStatusCode());
            throw new BadRequestAlertException(
                response.getStatusCode().toString(),
                "deleteRsInWard failed, response status: " + response.getStatusCode(),
                "EB88_ERROR"
            );
        }
        return response.getBody();
    }

    /**
     * Tạo mới gói ở EasyBooks 88
     * Tương ứng với chức năng Tạo mới gói ở CRM và EasyPOS
     * POST /api/eb-c-r-m/new-eb-package/
     */
    public PackageCreateResponse createPackageFromCRM(EBPackageSaveRequest request) throws Exception {
        validateInput(request);
        HttpHeaders authHeaders = initCommonHeaders();
        String url = EB88_BASE_URL + "/api/eb-c-r-m/new-eb-package";
        HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(request), authHeaders);
        ResponseEntity<PackageCreateResponse> response;
        try {
            response = restTemplate.postForEntity(url, requestEntity, PackageCreateResponse.class);
        } catch (Exception e) {
            log.error("createPackageFromCRM failed: {}", e.getMessage());
            throw new Exception("createPackageFromCRM failed: " + e.getMessage());
        }
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("createPackageFromCRM failed, response status: {}", response.getStatusCode());
            throw new Exception("createPackageFromCRM failed, response status: " + response.getStatusCode());
        }
        if (Objects.requireNonNull(response.getBody()).getSystemCode() > 1) {
            log.error("createPackageFromCRM failed with system code: {}", response.getBody().getSystemCode());
            throw new Exception("createPackageFromCRM failed with system code: " + response.getBody().getSystemCode());
        }
        return response.getBody();
    }

    /**
     * Tạo mới công ty con ở EB
     */
    public Object saveCompany(CompanySaveRequest request, Integer comId, Boolean isNew) throws Exception {
        HttpHeaders authHeaders = initAuthHeadersForUser(comId);
        String url = EB88_BASE_URL + "/api/organization-units";
        ResponseEntity<CompanySaveResponse> response = null;
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(request), authHeaders);
            if (isNew) {
                response = restTemplate.postForEntity(url, requestEntity, CompanySaveResponse.class);
            } else {
                response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, CompanySaveResponse.class);
            }
        } catch (Exception e) {
            String value = "";
            if (e instanceof HttpStatusCodeException) {
                HttpStatusCodeException ex = (HttpStatusCodeException) e;
                value =
                    ex.getResponseBodyAsString().contains("title")
                        ? new JSONObject(ex.getResponseBodyAsString()).get("title").toString()
                        : ResultConstants.ERROR;
            } else {
                value = e.getMessage();
            }
            log.error("EB88_ERROR: save Company failed: " + value);
            return value;
        }
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("EB88_ERROR: save Company failed, response status: {}", response.getStatusCode());
            return response.getStatusCode();
        }
        return response.getBody();
    }

    /**
     * Lấy thông tin của account từ token
     */
    public AccountResponse getDataFromTokenEB(Integer comId) throws Exception {
        HttpHeaders commonHeaders = initAuthHeadersForUser(comId);
        String url = EB88_BASE_URL + "/api/account";
        ResponseEntity<AccountResponse> response = null;
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(commonHeaders);
            response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, AccountResponse.class);
        } catch (Exception e) {
            log.error("createSubCompany failed: {}", e.getMessage());
            String value = "";
            if (e instanceof HttpStatusCodeException) {
                HttpStatusCodeException ex = (HttpStatusCodeException) e;
                value =
                    ex.getResponseBodyAsString().contains("message")
                        ? new JSONObject(ex.getResponseBodyAsString()).get("message").toString()
                        : ResultConstants.ERROR;
                throw new BadRequestAlertException(value, "get info account failed: " + value, "EB88_ERROR");
            } else {
                value = e.getMessage();
            }
            throw new InternalServerException(value, "get info account failed: " + value, "EB88_ERROR");
        }
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("get info account failed, response status: {}", response.getStatusCode());
            throw new InternalServerException(
                response.getStatusCode().toString(),
                "get info account failed, response status: " + response.getStatusCode(),
                "EB88_ERROR"
            );
        }
        return response.getBody();
    }

    public CommonResponse forgotPassword(String username, String password) throws Exception {
        HttpHeaders authHeaders = initCommonHeaders();
        String url = EB88_BASE_URL + "/api/easypos-integration/reset-password";
        ForgotPasswordEBRequest eb88Request = new ForgotPasswordEBRequest();
        eb88Request.setNewPassword(password);
        eb88Request.setConfirmPassword(password);
        eb88Request.setUsername(username);
        eb88Request.setHash(Util.createMd5(username + "easyPosEb88" + EasyInvoiceConstants.KEY_HASH_MD5));
        HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(eb88Request), authHeaders);
        ResponseEntity<CommonResponse> response;
        try {
            response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, CommonResponse.class);
        } catch (Exception e) {
            log.error("Forgot Password failed: {}", e.getMessage());
            return objectMapper.readValue(e.getMessage().substring(e.getMessage().indexOf("{")), CommonResponse.class);
        }
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("Forgot Password failed: {}", (Object) null);
            return response.getBody();
        }
        return response.getBody();
    }
}
