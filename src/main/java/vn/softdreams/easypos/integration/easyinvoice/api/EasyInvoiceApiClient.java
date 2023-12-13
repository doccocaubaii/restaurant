package vn.softdreams.easypos.integration.easyinvoice.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import vn.softdreams.easypos.dto.config.LoginEasyInvoiceRequest;
import vn.softdreams.easypos.dto.config.LoginEasyInvoiceResponse;
import vn.softdreams.easypos.integration.easyinvoice.dto.*;
import vn.softdreams.easypos.integration.easyinvoice.utils.EasyInvoiceUtils;
import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;
import vn.softdreams.easypos.web.rest.errors.IntegrationException;
import vn.softdreams.easypos.web.rest.errors.InternalServerException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Component
public class EasyInvoiceApiClient {
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

    private final Logger log = LoggerFactory.getLogger(EasyInvoiceApiClient.class);
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private final String RESPONSE_CLIENT_ERR = "4";
    private final String RESPONSE_SERVER_ERR = "5";

    public EasyInvoiceApiClient(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public HttpHeaders initHeaders(String username, String password, HttpMethod method) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        String authString = EasyInvoiceUtils.generateAuthToken(method, username, password);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authentication", authString);
        headers.add("User-Agent", "Mozilla/5.0 Firefox/26.0");
        return headers;
    }

    public Object checkLogin(String baseUrl, LoginEasyInvoiceRequest request, HttpHeaders headers) {
        String loginUrl = baseUrl + "/api/account/verify";
        ResponseEntity<LoginEasyInvoiceResponse> response;
        ResponseEntity<EasyInvoiceLoginResponse> response2;
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(request), headers);
            response = restTemplate.postForEntity(loginUrl, requestEntity, LoginEasyInvoiceResponse.class);
            response2 = restTemplate.postForEntity(loginUrl, requestEntity, EasyInvoiceLoginResponse.class);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new InternalServerException(
                ExceptionConstants.GET_URL_EASY_INVOICE_ERROR_VI,
                "login easy-invoice",
                ExceptionConstants.GET_URL_EASY_INVOICE_ERROR
            );
        }
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("loginEasyInvoice failed, response status: {}", response.getStatusCode());
            throw new InternalServerException(
                ExceptionConstants.LOGIN_EASY_INVOICE_ERROR_VI,
                "login easy-invoice",
                ExceptionConstants.LOGIN_EASY_INVOICE_ERROR
            );
        }
        return Strings.isNullOrEmpty(response.getBody().getStatus()) ? response2.getBody() : response.getBody();
    }

    public List<GetInvoicePatternsEasyInvoiceResponse> getInvoicePatterns(String baseUrl, HttpHeaders headers) throws Exception {
        String publishUrl = baseUrl + "/api/invoice-strip/search";
        headers.remove("User-Agent");
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<GetInvoicePatternsEasyInvoiceResponse[]> response = null;
        try {
            response = restTemplate.postForEntity(publishUrl, requestEntity, GetInvoicePatternsEasyInvoiceResponse[].class);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new Exception("Không lấy được danh sách mẫu số hóa đơn");
        }
        GetInvoicePatternsEasyInvoiceResponse[] result = response.getBody();
        if (result == null) {
            log.error("Response is null");
            throw new Exception("Kết quả trả về đang bỏ trống");
        }
        return Arrays.asList(result);
    }

    public String getInvoicePdf(String baseUrl, GetInvoicePdfEasyInvoiceRequest request, HttpHeaders headers) throws Exception {
        String publishUrl = baseUrl + "/api/publish/getInvoicePdf";
        HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(request), headers);
        ResponseEntity<byte[]> response = null;
        try {
            response = restTemplate.postForEntity(publishUrl, requestEntity, byte[].class);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new IntegrationException(
                IntegrationException.Party.EasyInvoice,
                "Không thể xem do kết nối đến hệ thống hóa đơn thất bại"
            );
        }
        log.info("EasyInvoice API response status code = {} ", response.getStatusCode());
        if (response.getStatusCode() != HttpStatus.OK) {
            log.error("EasyInvoice GetDocumentPDF API response is not ok, response status = {}", response.getStatusCode().value());
            throw new Exception("Trạng thái trả về không thành công");
        }
        if (response.getBody() == null) {
            log.error("Response is null");
            throw new Exception("Kết quả trả về đang bỏ trống");
        }

        return Base64.getEncoder().encodeToString(response.getBody());
    }

    public SendIssuanceNoticeEasyInvoiceResponse sendIssuanceNotice(
        String baseUrl,
        SendIssuanceNoticeEasyInvoiceRequest request,
        HttpHeaders headers
    ) throws Exception {
        String publishUrl = baseUrl + "/api/business/sendIssuanceNotice";
        HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(request), headers);
        ResponseEntity<SendIssuanceNoticeEasyInvoiceResponse> response = null;
        try {
            response = restTemplate.postForEntity(publishUrl, requestEntity, SendIssuanceNoticeEasyInvoiceResponse.class);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new IntegrationException(IntegrationException.Party.EasyInvoice, "Gửi mail đến hệ thống hóa đơn thất bại");
        }

        log.info("EasyInvoice API response status code = {} ", response.getStatusCode());
        if (response.getStatusCode() != HttpStatus.OK) {
            log.error("EasyInvoice SendIssuanceNotice API response is not ok, response status = {}", response.getStatusCode().value());
            throw new IntegrationException(IntegrationException.Party.EasyInvoice, "Trạng thái trả về không thành công");
        }
        SendIssuanceNoticeEasyInvoiceResponse sendIssuanceNoticeEasyInvoiceResponse = response.getBody();
        if (sendIssuanceNoticeEasyInvoiceResponse == null) {
            log.error("Response is null");
            throw new IntegrationException(IntegrationException.Party.EasyInvoice, "Kết quả trả về đang bỏ trống");
        }
        if (sendIssuanceNoticeEasyInvoiceResponse.getStatus().equals(RESPONSE_CLIENT_ERR)) {
            log.error("Client error " + sendIssuanceNoticeEasyInvoiceResponse.getMessage());
            throw new IntegrationException(IntegrationException.Party.EasyInvoice, "Lỗi từ máy trạm");
        }
        if (sendIssuanceNoticeEasyInvoiceResponse.getStatus().equals(RESPONSE_SERVER_ERR)) {
            log.error("Server error " + sendIssuanceNoticeEasyInvoiceResponse.getMessage());
            throw new IntegrationException(IntegrationException.Party.EasyInvoice, "Lỗi hệ thống");
        }

        return sendIssuanceNoticeEasyInvoiceResponse;
    }
}
