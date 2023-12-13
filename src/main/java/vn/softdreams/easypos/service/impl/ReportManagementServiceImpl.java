package vn.softdreams.easypos.service.impl;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.easypos.config.Constants;
import vn.softdreams.easypos.constants.*;
import vn.softdreams.easypos.domain.User;
import vn.softdreams.easypos.dto.audit.ActivityHistoryResponse;
import vn.softdreams.easypos.dto.audit.ActivityHistoryResult;
import vn.softdreams.easypos.dto.bill.BillStatItem;
import vn.softdreams.easypos.dto.bill.RevenueByMonth;
import vn.softdreams.easypos.dto.inventory.InventoryCommonStats;
import vn.softdreams.easypos.dto.product.*;
import vn.softdreams.easypos.dto.productProductUnit.ProductAndUnitItem;
import vn.softdreams.easypos.dto.report.ReportCommonResponse;
import vn.softdreams.easypos.integration.easybooks88.api.EB88ApiClient;
import vn.softdreams.easypos.integration.easybooks88.api.dto.*;
import vn.softdreams.easypos.repository.*;
import vn.softdreams.easypos.service.ReportManagementService;
import vn.softdreams.easypos.service.UserService;
import vn.softdreams.easypos.service.dto.*;
import vn.softdreams.easypos.util.Common;
import vn.softdreams.easypos.util.Util;
import vn.softdreams.easypos.web.rest.errors.BadRequestAlertException;
import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;
import vn.softdreams.easypos.web.rest.errors.IntegrationException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReportManagementServiceImpl implements ReportManagementService {

    private final Logger log = LoggerFactory.getLogger(ReportManagementServiceImpl.class);
    private final UserService userService;
    private final EB88ApiClient eb88ApiClient;
    private final ConfigRepository configRepository;
    private final ProductRepository productRepository;
    private final BillRepository billRepository;
    private final InvoiceRepository invoiceRepository;
    private final BusinessTypeRepository businessTypeRepository;
    private final ProductProductUnitRepository productProductUnitRepository;
    private final String DATE_REGEX = "^\\d{4}-\\d{2}-\\d{2}$";
    final String BY_MONTH = "yyyy/MM";
    final String BY_DAY = "yyyy/MM/dd";
    final String BY_HOUR = "yyyy/MM/dd HH";

    public ReportManagementServiceImpl(
        UserService userService,
        EB88ApiClient eb88ApiClient,
        ConfigRepository configRepository,
        ProductRepository productRepository,
        BillRepository billRepository,
        InvoiceRepository invoiceRepository,
        BusinessTypeRepository businessTypeRepository,
        ProductProductUnitRepository productProductUnitRepository
    ) {
        this.userService = userService;
        this.eb88ApiClient = eb88ApiClient;
        this.configRepository = configRepository;
        this.productRepository = productRepository;
        this.billRepository = billRepository;
        this.invoiceRepository = invoiceRepository;
        this.businessTypeRepository = businessTypeRepository;
        this.productProductUnitRepository = productProductUnitRepository;
    }

    public ResultDTO getRevenueStats(Integer comId, String fromDate, String toDate) throws Exception {
        ValidateInputData validateData = validateInput(comId, fromDate, toDate);
        User user = userService.getUserWithAuthorities(comId);
        checkStatusAndUser(validateData, user);
        List<GetSalesReportResponse[]> eb88Responses = eb88ApiClient.getRevenueStats(comId, fromDate, toDate);
        RevenueCommonStatsResponse response = new RevenueCommonStatsResponse();
        response.setFromDate(fromDate);
        response.setToDate(toDate);
        response.setComId(comId);
        BigDecimal sumRevenue = BigDecimal.valueOf(0);
        BigDecimal sumProfit = BigDecimal.valueOf(0);
        List<RevenueCommonStatsResponse.Detail> list = new ArrayList<>();
        GetSalesReportResponse[] revenueList = eb88Responses.get(0);
        GetSalesReportResponse[] profitList = eb88Responses.get(1);
        for (int i = 0; i < eb88Responses.get(0).length; i++) {
            RevenueCommonStatsResponse.Detail detail = new RevenueCommonStatsResponse.Detail();
            sumRevenue = sumRevenue.add(revenueList[i].getAmount());
            sumProfit = sumProfit.add(profitList[i].getAmount());
            detail.setFromDate(revenueList[i].getFromDate());
            detail.setToDate(revenueList[i].getToDate());
            detail.setRevenue(revenueList[i].getAmount());
            detail.setProfit(profitList[i].getAmount());
            list.add(detail);
        }
        response.setDetail(list);
        response.setRevenue(sumRevenue);
        response.setProfit(sumProfit);
        response.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_SEND_DETAIL, true, response);
    }

    public ResultDTO getInventoryStats(Integer comId, String fromDate, String toDate) throws Exception {
        ValidateInputData validateData = validateInput(comId, fromDate, toDate);
        User user = userService.getUserWithAuthorities(comId);
        checkStatusAndUser(validateData, user);
        GetInventoryReportResponse[] eb88Responses = eb88ApiClient.getInventoryReport(comId, fromDate, toDate);
        List<InventoryCommonStatsResponse> responses = new ArrayList<>();
        for (GetInventoryReportResponse item : eb88Responses) {
            InventoryCommonStatsResponse statsResponse = new InventoryCommonStatsResponse();
            statsResponse.setComId(comId);
            statsResponse.setFromDate(fromDate);
            statsResponse.setToDate(toDate);
            statsResponse.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            statsResponse.setProductCode(item.getMaterialGoodsCode());
            statsResponse.setProductName(item.getMaterialGoodsName());
            statsResponse.setOpeningQuantity(item.getOpeningQuantity());
            statsResponse.setOpeningAmount(item.getOpeningAmount());
            responses.add(statsResponse);
        }
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_SEND_DETAIL, true, responses);
    }

    public ResultDTO getRevenueCostPrice(Integer comId, String fromDate, String toDate) throws Exception {
        ValidateInputData validateData = validateInput(comId, fromDate, toDate);
        User user = userService.getUserWithAuthorities(comId);
        checkStatusAndUser(validateData, user);
        //get eb company
        Optional<String> configOpt = configRepository.getValueByComIdAndCode(comId, ConfigCode.EB88_COM_ID.getCode());
        if (configOpt.isEmpty() || Strings.isNullOrEmpty(configOpt.get())) throw new IntegrationException(
            IntegrationException.Party.Easybooks88,
            "Thiếu cấu hình của hệ thống (eb88_com_id)"
        );
        GetSalesDetailReportRequest request = new GetSalesDetailReportRequest();
        request.setCheckAll(true);
        request.setCompanyID(Integer.parseInt(configOpt.get()));
        request.setFromDate(fromDate);
        request.setToDate(toDate);
        GetSalesDetailReportResponse eb88Response = eb88ApiClient.getRevenueCostPrice(comId, request);
        RevenueCostPriceByProductResponse response = new RevenueCostPriceByProductResponse();
        response.setFromDate(fromDate);
        response.setToDate(toDate);
        response.setComId(comId);
        List<RevenueCostPriceByProductResponse.Detail> details = new ArrayList<>();
        response.setDetails(new ArrayList<>());
        if (eb88Response.getData() == null) eb88Response.setData(new ArrayList<>());
        for (GetSalesDetailReportResponse.Data data : eb88Response.getData()) {
            RevenueCostPriceByProductResponse.Detail detail = new RevenueCostPriceByProductResponse.Detail();
            detail.setProductCode(data.getMaterialGoodsCode());
            detail.setProductName(data.getMaterialGoodsName());
            detail.setUnitName(data.getUnitName());
            detail.setQuantity(data.getMainQuantityToString());
            detail.setRevenue(data.getAmountToString());
            detail.setInPrice(data.getCostPriceToString());
            detail.setDiscountAmount(data.getDiscountAmountToString());
            detail.setProfit(data.getProfitAndLossToString());
            if (detail.getProductCode().equals("Tổng cộng")) {
                response.setSumQuantity(data.getMainQuantityToString());
                response.setSumRevenue(data.getAmountToString());
                response.setSumProfit(data.getProfitAndLossToString());
                response.setSumDiscountAmount(data.getDiscountAmountToString());
            }
            details.add(detail);
        }
        response.setDetails(details);

        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_SEND_DETAIL, true, response);
    }

    public ResultDTO getGeneralInventory(Integer comId, String fromDate, String toDate) throws Exception {
        ValidateInputData validateData = validateInput(comId, fromDate, toDate);
        User user = userService.getUserWithAuthorities(comId);
        checkStatusAndUser(validateData, user);
        Optional<String> configOpt = configRepository.getValueByComIdAndCode(comId, ConfigCode.EB88_COM_ID.getCode());
        if (configOpt.isEmpty() || Strings.isNullOrEmpty(configOpt.get())) throw new IntegrationException(
            IntegrationException.Party.Easybooks88,
            "Thiếu cấu hình của hệ thống (eb88_com_id)"
        );
        GetInventoryDetailReportRequest request = new GetInventoryDetailReportRequest();
        request.setCheckAll(true);
        request.setCompanyID(Integer.parseInt(configOpt.get()));
        request.setFromDate(fromDate);
        request.setToDate(toDate);

        GetInventoryDetailReportResponse eb88Response = eb88ApiClient.getGeneralInventory(comId, request);
        GeneralInputOutputInventoryResponse response = new GeneralInputOutputInventoryResponse();
        response.setFromDate(fromDate);
        response.setToDate(toDate);
        response.setComId(comId);
        List<GeneralInputOutputInventoryResponse.Detail> details = new ArrayList<>();
        response.setDetails(new ArrayList<>());
        if (eb88Response.getData() == null) eb88Response.setData(new ArrayList<>());
        for (GetInventoryDetailReportResponse.Data data : eb88Response.getData()) {
            GeneralInputOutputInventoryResponse.Detail detail = new GeneralInputOutputInventoryResponse.Detail();
            detail.setProductCode(data.getMaterialGoodsCode());
            detail.setProductName(data.getMaterialGoodsName());
            detail.setUnitName(data.getUnitName());
            detail.setOpeningQuantity(data.getOpeningQuantityToString());
            detail.setOpeningAmount(data.getOpeningAmountToString());
            detail.setIwQuantity(data.getIwQuantityToString());
            detail.setIwAmount(data.getIwAmountToString());
            detail.setOwQuantity(data.getOwQuantityToString());
            detail.setOwAmount(data.getOwAmountToString());
            detail.setClosingQuantity(data.getClosingQuantityToString());
            detail.setClosingAmount(data.getClosingAmountToString());
            if (detail.getProductCode().equals("Tổng cộng")) {
                response.setSumOpeningQuantity(data.getOpeningQuantityToString());
                response.setSumOpeningAmount(data.getOpeningAmountToString());
                response.setSumIWQuantity(data.getIwQuantityToString());
                response.setSumIWAmount(data.getIwAmountToString());
                response.setSumOWQuantity(data.getOwQuantityToString());
                response.setSumOWAmount(data.getOwAmountToString());
                response.setSumClosingQuantity(data.getClosingQuantityToString());
                response.setSumClosingAmount(data.getClosingAmountToString());
            }
            details.add(detail);
        }
        response.setDetails(details);

        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_SEND_DETAIL, true, response);
    }

    @Override
    @Transactional(readOnly = true)
    public ResultDTO inventoryCommonStatsV2(Integer comId, Integer groupId, String keyword) {
        userService.getUserWithAuthorities(comId);
        InventoryCommonStats inventoryCommonStats = new InventoryCommonStats();
        inventoryCommonStats.setComId(comId);
        inventoryCommonStats.setKeyword(keyword);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(Constants.ZONED_DATE_TIME_FORMAT);
        inventoryCommonStats.setCreateTime(dateTimeFormatter.format(ZonedDateTime.now()));
        BigDecimal totalOnHand = BigDecimal.ZERO;
        BigDecimal totalValue = BigDecimal.ZERO;
        List<InventoryCommonStats.InventoryCommonStatsDetail> details = productRepository.inventoryCommonStatsV2(comId, groupId, keyword);
        for (InventoryCommonStats.InventoryCommonStatsDetail detail : details) {
            totalOnHand = totalOnHand.add(detail.getOnHand());
            totalValue = totalValue.add(detail.getValue());
        }
        inventoryCommonStats.setTotalOnHand(totalOnHand);
        inventoryCommonStats.setTotalValue(totalValue);
        inventoryCommonStats.setDetail(details);
        return new ResultDTO(
            ResultConstants.SUCCESS,
            ResultConstants.INVENTORY_COMMON_STATS_V2_SUCCESS_VI,
            true,
            inventoryCommonStats,
            details.size()
        );
    }

    /**
     * trường hợp có productProductUnitIdsStr
     * vẫn giữ luồng cũ, truyền thêm productIds cho câu query lấy từ productProductUnitIds
     * out put: map với bộ key item.getProductId() + item.getUnitName() + item.getUnitId() để lấy theo yêu cầu
     * tức là quy ước productProductUnitId = item.getProductId() + item.getUnitName() + item.getUnitId()
     */
    // sau giai đoạn 31/8 nên viết store procedure để đảm bảo hiệu năng
    @Override
    @Transactional(readOnly = true)
    public ResultDTO productProfitStats(
        String productProductUnitIdsStr,
        Integer comId,
        String fromDate,
        String toDate,
        Integer type,
        Pageable pageable,
        Boolean isPaging,
        Integer sortType
    ) {
        userService.getUserWithAuthorities(comId);
        Common.checkDateTime(fromDate, Constants.ZONED_DATE_FORMAT);
        Common.checkToDate(toDate, Constants.ZONED_DATE_FORMAT);

        ProductProfitResponse response = new ProductProfitResponse();
        BigDecimal totalQuantity = BigDecimal.ZERO, totalRevenue = BigDecimal.ZERO, totalProfit = BigDecimal.ZERO;
        List<ProductProfitResult> profitResult = new ArrayList<>();
        List<ProductAndUnitItem> productAndUnitItems;
        List<Integer> productIds = new ArrayList<>();
        Set<String> prodAndUnitMap = new HashSet<>();
        List<Integer> productProductUnitIds = new ArrayList<>();
        if (!Strings.isNullOrEmpty(productProductUnitIdsStr) && !productProductUnitIdsStr.equals("[]")) {
            String trimmedInput = productProductUnitIdsStr.substring(1, productProductUnitIdsStr.length() - 1);
            String[] elements = trimmedInput.split(",");
            for (String element : elements) {
                Integer intValue = Integer.parseInt(element.trim());
                productProductUnitIds.add(intValue);
            }
            productAndUnitItems = productProductUnitRepository.getProductIdAndUnitIdByIds(productProductUnitIds);
            productAndUnitItems.forEach(item -> {
                productIds.add(item.getProductId());
                if (productProductUnitIds.contains(item.getId())) {
                    prodAndUnitMap.add(item.getProductId() + item.getUnitName() + item.getUnitId());
                }
            });
        }
        Integer businessTypeId = businessTypeRepository.getIdByComIdAndCode(comId, BusinessTypeConstants.RsOutWard.OUT_WARD);
        List<ProductProfitResult> revenueResult = productRepository.getProductRevenueStats(comId, fromDate, toDate, type, productIds);
        List<ProductProfitResult> costResult = productRepository.getProductCostStats(
            comId,
            fromDate,
            toDate,
            type,
            businessTypeId,
            productIds
        );

        Map<String, ProductProfitResult> revenueResultMap = new HashMap<>();
        revenueResult.forEach(result -> revenueResultMap.put(result.getId() + result.getUnitName() + result.getUnitId(), result));
        for (ProductProfitResult cost : costResult) {
            String key = cost.getId() + cost.getUnitName() + cost.getUnitId();
            if (!productProductUnitIds.isEmpty() && !prodAndUnitMap.contains(key)) {
                continue;
            }
            ProductProfitResult result = new ProductProfitResult();
            BigDecimal profit;
            BigDecimal quantity = BigDecimal.ZERO;
            BigDecimal revenue = BigDecimal.ZERO;
            // sản phẩm đều có doanh thu và giá vốn & sản phẩm chỉ có giá vốn
            if (revenueResultMap.containsKey(key)) {
                revenue = revenueResultMap.get(key).getRevenue();
                quantity = revenueResultMap.get(key).getQuantity();
                revenueResultMap.remove(key);
            }
            result.setName(cost.getName());
            result.setUnitName(cost.getUnitName());
            profit = revenue.subtract(cost.getCostPrice());
            result.setId(cost.getId());
            result.setRevenue(revenue);
            result.setProfit(profit);
            result.setQuantity(quantity);
            totalQuantity = totalQuantity.add(quantity);
            totalRevenue = totalRevenue.add(revenue);
            totalProfit = totalProfit.add(profit);
            profitResult.add(result);
        }

        // sản phẩm còn lại chỉ có doanh thu
        List<ProductProfitResult> revenues = revenueResult
            .stream()
            .filter(result -> revenueResultMap.containsKey(result.getId() + result.getUnitName() + result.getUnitId()))
            .collect(Collectors.toList());
        for (ProductProfitResult revenue : revenues) {
            String key = revenue.getId() + revenue.getUnitName() + revenue.getUnitId();
            if (!productProductUnitIds.isEmpty() && !prodAndUnitMap.contains(key)) {
                continue;
            }
            ProductProfitResult result = new ProductProfitResult();
            result.setId(revenue.getId());
            result.setName(revenue.getName());
            result.setUnitName(revenue.getUnitName());
            result.setQuantity(revenue.getQuantity());
            result.setRevenue(revenue.getRevenue());
            result.setProfit(revenue.getRevenue());
            totalRevenue = totalRevenue.add(revenue.getRevenue());
            totalProfit = totalProfit.add(revenue.getRevenue());
            profitResult.add(result);
        }

        response.setComId(comId);
        response.setFromDate(fromDate);
        response.setToDate(toDate);
        response.setTotalQuantity(totalQuantity);
        response.setTotalRevenue(totalRevenue);
        response.setTotalProfit(totalProfit);
        Integer count = profitResult.size();

        if (isPaging != null && isPaging) {
            // Use Stream API to handle pagination
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), profitResult.size());
            List<ProductProfitResult> sortedAndPagedResult = profitResult
                .stream()
                .sorted((item1, item2) -> {
                    if (sortType == null) {
                        return 0;
                    }
                    int compareResult;
                    switch (sortType) {
                        case 1:
                            compareResult = item1.getQuantity().compareTo(item2.getQuantity());
                            return compareResult;
                        case 2:
                            compareResult = item1.getQuantity().compareTo(item2.getQuantity());
                            return -compareResult;
                        case 3:
                            compareResult = item1.getProfit().compareTo(item2.getProfit());
                            return compareResult;
                        case 4:
                            compareResult = item1.getProfit().compareTo(item2.getProfit());
                            return -compareResult;
                        case 35:
                            compareResult = item1.getRevenue().compareTo(item2.getRevenue());
                            return compareResult;
                        case 6:
                            compareResult = item1.getRevenue().compareTo(item2.getRevenue());
                            return -compareResult;
                        default:
                            return 0;
                    }
                })
                .skip(start)
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());
            response.setDetail(sortedAndPagedResult);
        } else {
            response.setDetail(profitResult);
        }
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.GET_PRODUCT_PROFIT_STATS, true, response, count);
    }

    @Override
    @Transactional(readOnly = true)
    public ResultDTO hotSaleProductStats(Integer comId, String fromDate, String toDate, Integer type) {
        if (type != null && (!type.equals(1) && !type.equals(0))) {
            throw new BadRequestAlertException(
                ExceptionConstants.REPORT_HOT_SALES_TYPE_INVALID_VI,
                "Report",
                ExceptionConstants.REPORT_HOT_SALES_TYPE_INVALID
            );
        }
        Common.checkDateTime(fromDate, Constants.ZONED_DATE_FORMAT);
        HotSaleProductResponse response = new HotSaleProductResponse();
        List<HotSaleProductResult> results = billRepository.getHotSaleProductStats(
            comId,
            fromDate,
            Common.checkToDate(toDate, Constants.ZONED_DATE_FORMAT),
            type
        );
        response.setComId(comId);
        response.setFromDate(fromDate);
        response.setToDate(toDate);
        response.setDetail(results);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.GET_HOT_SALE_PRODUCT_STATS, true, response, results.size());
    }

    @Override
    @Transactional(readOnly = true)
    public ResultDTO saleProductStats(SaleProductStatsRequest request) {
        userService.getUserWithAuthorities(request.getComId());
        Common.checkDateTime(request.getFromDate(), Constants.ZONED_DATE_FORMAT);
        Common.checkToDate(request.getToDate(), Constants.ZONED_DATE_FORMAT);
        List<SaleProductStatsResult> results = invoiceRepository.getProductSaleStats(request);

        BigDecimal sumTotalPreTax = BigDecimal.ZERO;
        BigDecimal sumTotalTaxable = BigDecimal.ZERO;
        BigDecimal sumVatAmount = BigDecimal.ZERO;
        for (SaleProductStatsResult item : results) {
            BigDecimal totalPreTax = item.getTotalPreTax();
            if (totalPreTax != null) {
                sumTotalPreTax = sumTotalPreTax.add(item.getTotalPreTax());
            }
            if (item.getVatRate() != null && !InvoiceConstants.VatRate.KCT.equals(item.getVatRate())) {
                sumTotalTaxable = sumTotalTaxable.add(item.getTotalPreTax());
            }
            if (item.getVatAmount() != null) {
                sumVatAmount = sumVatAmount.add(item.getVatAmount());
            }
            setDescriptionForSaleProductStats(item);
        }

        SaleProductStatsResponse response = new SaleProductStatsResponse(
            request.getComId(),
            request.getFromDate(),
            request.getToDate(),
            results,
            sumTotalPreTax,
            sumTotalTaxable,
            sumVatAmount
        );
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.GET_SALE_PRODUCT_STATS, true, response, results.size());
    }

    private void setDescriptionForSaleProductStats(SaleProductStatsResult item) {
        if (item.getStatus() != null) {
            switch (item.getStatus()) {
                case 3:
                    item.setDescription(InvoiceConstants.Status.THAY_THE_STRING);
                    break;
                //                case 4:
                //                    item.setDescription(InvoiceConstants.Status.DIEU_CHINH_STRING);
                //                    break;
                case 5:
                    item.setDescription(InvoiceConstants.Status.BI_HUY_STRING);
                    break;
                default:
                    item.setDescription("");
                    break;
            }
        }
    }

    /**
     * idea
     * - đầu tiên lấy List<ActivityHistoryResult> với các thuộc tính cơ bản của 2 bảng bill_aud và invoice_aud theo phân sang
     * - sau đó lọc bill_id, invoice_id -> query ngược lại 2 bảng đó, để đảm bảo việc ko bị thiếu bản ghi
     *     và đánh rowNumber giảm dần theo từng bill, invoice
     *      ví dụ: nếu 1 bill có 3 bản ghi, trang 0 có 2 bản ghi thì sẽ ko so sánh theo các điều kiện của nghiệp vụ đc
     *      vậy nên bắt buộc phải query thêm 1 lần ngược lại như thế để lấy đc ALL các bản ghi của bill, invoice
     * - khi query ngược lại, phải đảm bảo rằng sẽ loại bỏ các bản ghi chưa có sự thay đổi của status, vat_amount, total_amount
     *      đồng thời lấy min của rev và min của update_time
     * - sau khi query, xử lý code java:
     *      lọc theo type
     *      so sánh từng bản ghi thay đổi theo rowNumber giảm dần
     *      cuối cùng sẽ map với List<ActivityHistoryResult> theo key: id (billId/invoiceId) + typeName (DH/HD) + updateTime
     */
    @Override
    @Transactional(readOnly = true)
    public ResultDTO activityHistoryStats(Integer comId, String fromDate, String toDate, Pageable pageable) {
        userService.getUserWithAuthorities(comId);
        Common.checkDateTime(fromDate, Constants.ZONED_DATE_FORMAT);
        Common.checkToDate(toDate, Constants.ZONED_DATE_FORMAT);
        // lấy ra danh sách hoạt động gần đây theo phân trang
        List<String> resultSet = new ArrayList<>();
        Page<ActivityHistoryResult> resultsPage = billRepository.getActivityHistory(comId, fromDate, toDate, pageable);
        List<ActivityHistoryResult> results = resultsPage.getContent();
        ReportCommonResponse commonResponse = new ReportCommonResponse();
        List<ActivityHistoryResponse> response = new ArrayList<>();
        if (!results.isEmpty()) {
            List<Integer> billIdsResult = new ArrayList<>(), invoiceIdsResult = new ArrayList<>();
            List<Integer> revBillId = new ArrayList<>(), revInvoiceId = new ArrayList<>();
            results.forEach(result -> {
                resultSet.add(result.getId() + getTypeString(result.getType()));

                if (result.getType() == 1) {
                    billIdsResult.add(result.getId());
                    revBillId.add(result.getRev());
                } else {
                    invoiceIdsResult.add(result.getId());
                    revInvoiceId.add(result.getRev());
                }
            });
            List<ActivityHistoryResponse> allResponse = billRepository.getAllActivityHistory(billIdsResult, invoiceIdsResult);
            Map<String, ActivityHistoryResponse> responseMap = new HashMap<>();
            //            System.out.println(revBillId);
            //            System.out.println(revInvoiceId);
            Integer idCheck = null;
            ActivityHistoryResponse pre = null;
            for (ActivityHistoryResponse item : allResponse) {
                ActivityHistoryResponse itemResponse = null;
                if (!item.getId().equals(idCheck)) {
                    if (resultSet.contains(item.getId() + getTypeString(item.getType()))) {
                        if ((item.getStatus() != null && item.getType() == 1) || item.getType() == 2) {
                            itemResponse = getResponseDetail(null, item);
                        }
                        if (itemResponse != null) {
                            responseMap.put(item.getId() + getTypeString(item.getType()) + item.getUpdateTime(), itemResponse);
                        }
                    }
                    idCheck = item.getId();
                } else {
                    if (resultSet.contains(item.getId() + getTypeString(item.getType()))) {
                        if (pre.getStatus() == null) {
                            pre.setStatus(-1);
                        }
                        if (item.getStatus() == null) {
                            item.setStatus(-1);
                        }
                        itemResponse = getResponseDetail(pre, item);
                        if (itemResponse != null) {
                            responseMap.put(item.getId() + getTypeString(item.getType()) + item.getUpdateTime(), itemResponse);
                        }
                    }
                }
                pre = item;
            }
            for (ActivityHistoryResult result : results) {
                ActivityHistoryResponse itemResponse = responseMap.get(
                    result.getId() + getTypeString(result.getType()) + result.getUpdateTime()
                );
                if (itemResponse != null) {
                    response.add(itemResponse);
                }
            }

            commonResponse.setComId(comId);
            commonResponse.setFromDate(fromDate);
            commonResponse.setToDate(toDate);
            commonResponse.setDetail(List.of(response));
        }
        return new ResultDTO(
            ResultConstants.SUCCESS,
            ResultConstants.GET_ACTIVITY_HISTORY_STATS,
            true,
            commonResponse,
            (int) resultsPage.getTotalElements()
        );
    }

    private String getTypeString(Integer type) {
        String value = "";
        switch (type) {
            case 1:
                {
                    value = "DH";
                    break;
                }
            case 2:
                {
                    value = "HD";
                    break;
                }
        }
        return value;
    }

    private ActivityHistoryResponse getResponseDetail(ActivityHistoryResponse prev, ActivityHistoryResponse next) {
        if (next.getType() == 1) {
            return getHistoryBillAudResponse(prev, next);
        } else {
            if (next.getStatus() == null) {
                next.setStatus(-1);
            }
            if (prev != null && prev.getStatus() == null) {
                prev.setStatus(-1);
            }
            return getHistoryInvoiceAudResponse(prev, next);
        }
    }

    private ActivityHistoryResponse getHistoryBillAudResponse(ActivityHistoryResponse prev, ActivityHistoryResponse next) {
        ActivityHistoryResponse response;
        String subDescription = "";
        BigDecimal totalAmount = next.getTotalAmount();
        // bán hàng thanh toán luôn hoặc lưu đơn chưa thanh toán
        if (prev == null && next.getRevtype() == 0) {
            if (next.getStatus() == 1) {
                subDescription = " vừa bán đơn hàng ";
            } else if (next.getStatus() == 0) {
                subDescription = " vừa tạo đơn hàng chưa thanh toán ";
            }
        } else if (prev != null && next.getRevtype() == 1) {
            // sửa đơn hàng
            if (!Objects.equals(prev.getStatus(), next.getStatus()) && prev.getStatus() == 0 && next.getStatus() == 1) {
                subDescription = " hoàn thành đơn hàng ";
            } else if (prev.getStatus() == 0 && next.getStatus() == 0 && prev.getTotalAmount().compareTo(totalAmount) != 0) {
                BigDecimal amountChange = totalAmount.subtract(prev.getTotalAmount());
                if (amountChange.compareTo(BigDecimal.ZERO) < 0) {
                    subDescription = "giảm";
                    totalAmount = amountChange.multiply(BigDecimal.valueOf(-1));
                } else {
                    subDescription = "tăng";
                    totalAmount = amountChange;
                }
                subDescription = " vừa cập nhật " + subDescription + " giá trị đơn hàng ";
            } else if (next.getStatus() == 2) {
                subDescription = " đã hủy đơn hàng ";
            }
        } else if (prev == null && next.getStatus() == 2 && next.getRevtype() == 1) {
            subDescription = " đã hủy đơn hàng ";
        }
        if (Strings.isNullOrEmpty(subDescription)) {
            log.error("Null Bill: " + next.getId() + "-" + next.getRevtype() + "-" + next.getRowNumber());
            return null;
        }
        String totalAmountStr = Common.formatMoney(totalAmount);
        String description = next.getFullName() + subDescription + next.getCode() + " với giá trị " + totalAmountStr;
        response =
            new ActivityHistoryResponse(
                next.getRev(),
                next.getId(),
                next.getType(),
                description,
                next.getUpdateTime(),
                next.getCustomerId()
            );
        return response;
    }

    private ActivityHistoryResponse getHistoryInvoiceAudResponse(ActivityHistoryResponse prev, ActivityHistoryResponse next) {
        // đã set status = -1 ở trên để đỡ bị exception -> tương ứng với null trong db.audit.invoice_aud
        ActivityHistoryResponse response;
        String subDescription = "";
        String description = "";
        BigDecimal totalAmount = next.getTotalAmount();
        // phát hành tự động
        if (prev == null && next.getRevtype() == 1) {
            if (next.getStatus() == 1) {
                subDescription = " vừa phát hành thành công hóa đơn ";
                description = subDescription + next.getCode() + " với giá trị " + Common.formatMoney(totalAmount);
            }
            //            else if (next.getStatus() == 0) {
            //                subDescription = " vừa tạo một hóa đơn chưa phát hành cho đơn hàng ";
            //                description = subDescription + next.getBillCode();
            //            }
        } else if (prev == null && next.getRevtype() == 0) {
            subDescription = " vừa tạo một hóa đơn chưa phát hành cho đơn hàng ";
            description = subDescription + next.getBillCode();
        } else if (prev != null && next.getRevtype() == 1) {
            //  status chuyển từ null sang 1
            if (prev.getStatus() == -1 && next.getStatus() == 1) {
                subDescription = " vừa phát hành thành công hóa đơn ";
                description = subDescription + next.getCode() + " với giá trị " + Common.formatMoney(totalAmount);
            } else if ((prev.getStatus() == -1 || next.getStatus() == -1) && prev.getTotalAmount().compareTo(totalAmount) != 0) {
                //  status = null và trường total_amount thay đổi
                BigDecimal amountChange = totalAmount.subtract(prev.getTotalAmount());
                if (amountChange.compareTo(BigDecimal.ZERO) < 0) {
                    subDescription = "giảm";
                    totalAmount = amountChange.multiply(BigDecimal.valueOf(-1));
                } else {
                    subDescription = "tăng";
                    totalAmount = amountChange;
                }
                subDescription = " vừa cập nhật giá trị hóa đơn tương ứng với đơn hàng " + subDescription + " giá trị đơn hàng ";
                description = subDescription + next.getBillCode() + " với giá trị " + Common.formatMoney(totalAmount);
            } else if ((prev.getStatus() == -1 || next.getStatus() == -1) && prev.getVatAmount().compareTo(next.getVatAmount()) != 0) {
                BigDecimal vatAmountChange = next.getVatAmount().subtract(prev.getVatAmount());
                if (vatAmountChange.compareTo(BigDecimal.ZERO) < 0) {
                    subDescription = "giảm";
                    totalAmount = vatAmountChange.multiply(BigDecimal.valueOf(-1));
                } else {
                    subDescription = "tăng";
                    totalAmount = vatAmountChange;
                }
                subDescription = " vừa cập nhật giá trị thuế hóa đơn tương ứng với đơn hàng " + subDescription + " giá trị đơn hàng ";
                description = subDescription + next.getBillCode() + " với giá trị " + Common.formatMoney(totalAmount);
            }
        }
        if (Strings.isNullOrEmpty(description)) {
            log.error("Null Invoice: " + next.getId() + "-" + next.getRevtype() + "-" + next.getRowNumber());
            return null;
        }
        response =
            new ActivityHistoryResponse(
                next.getRev(),
                next.getId(),
                next.getType(),
                next.getFullName() + description,
                next.getUpdateTime(),
                next.getCustomerId()
            );
        return response;
    }

    public void checkStatusAndUser(ValidateInputData validateInputData, User user) throws IntegrationException {
        if (!validateInputData.isStatus()) {
            log.error(validateInputData.getMessage());
            throw new IntegrationException(IntegrationException.Party.Easybooks88, validateInputData.getMessage());
        }

        userService.checkUser(Optional.ofNullable(user), null);
    }

    public ValidateInputData validateInput(Integer comId, String fromDate, String toDate) throws IntegrationException {
        ValidateInputData validateInputData = new ValidateInputData();
        validateInputData.setStatus(false);

        if (comId == null) {
            validateInputData.setMessage("Mã công ty đang bỏ trống");
            return validateInputData;
        }

        if (fromDate == null || fromDate.isEmpty()) {
            validateInputData.setMessage("Trường dữ liệu 'Từ ngày' đang bỏ trống");
            return validateInputData;
        }

        if (!fromDate.matches(DATE_REGEX)) {
            validateInputData.setMessage("Trường dữ liệu 'Từ ngày' sai định dạng");
            return validateInputData;
        }

        if (toDate == null || toDate.isEmpty()) {
            validateInputData.setMessage("Trường dữ liệu 'Đến ngày' đang bỏ trống");
            return validateInputData;
        }

        if (!toDate.matches(DATE_REGEX)) {
            validateInputData.setMessage("Trường dữ liệu 'Đến ngày' sai định dạng");
            return validateInputData;
        }

        LocalDate fromDateDT = LocalDate.parse(fromDate);
        LocalDate toDateDT = LocalDate.parse(toDate);

        if (fromDateDT.isBefore(LocalDate.now().minusMonths(18))) {
            validateInputData.setMessage("Trường dữ liệu 'Từ ngày' không được vượt quá 18 tháng so với ngày hiện tại");
            return validateInputData;
        }

        //        if (fromDateDT.isAfter(LocalDate.now())) {
        //            validateInputData.setMessage("Trường dữ liệu 'Đến ngày' vượt quá ngày hiện tại");
        //            return validateInputData;
        //        }

        if (toDateDT.isBefore(fromDateDT)) {
            validateInputData.setMessage("Trường dữ liệu 'Đến ngày' không được nhỏ hơn 'Từ ngày'");
            return validateInputData;
        }
        validateInputData.setStatus(true);

        return validateInputData;
    }

    @Override
    public ResultDTO getBillRevenue(Integer comId, String fromDate, String toDate, Integer type) throws ParseException {
        User user = userService.getUserWithAuthorities(comId);
        if (type == null) {
            type = StatisticConstants.BY_MONTH;
        }
        String format = "";
        if (Objects.equals(type, StatisticConstants.BY_MONTH)) {
            format = BY_MONTH;
        } else if (Objects.equals(type, StatisticConstants.BY_DAY)) {
            format = BY_DAY;
        } else if (Objects.equals(type, StatisticConstants.BY_HOUR)) {
            format = BY_HOUR;
        }
        List<BillStatItem> revenues = billRepository.getBillMoney(user.getCompanyId(), fromDate, toDate, format);
        List<BillStatItem> expenses = billRepository.getBillExpense(user.getCompanyId(), fromDate, toDate, format);
        List<RevenueByMonth> bills = processData(revenues, expenses);

        List<RevenueByMonth> revenueByMonthList = getAllRevenueEvenZero(bills, type, user, fromDate, toDate);
        RevenueCommonStatsResponse response = new RevenueCommonStatsResponse();
        response.setFromDate(fromDate);
        response.setToDate(toDate);
        response.setComId(comId);
        BigDecimal totalRevenue = BigDecimal.ZERO;
        BigDecimal totalProfit = BigDecimal.ZERO;
        List<RevenueCommonStatsResponse.Detail> list = new ArrayList<>();
        for (RevenueByMonth revenue : revenueByMonthList) {
            totalRevenue = totalRevenue.add(revenue.getRevenue());
            totalProfit = totalProfit.add(revenue.getProfit());
            RevenueCommonStatsResponse.Detail detail = new RevenueCommonStatsResponse.Detail();
            setDetail(detail, type, revenue);
            list.add(detail);
        }

        response.setDetail(list);
        response.setRevenue(totalRevenue);
        response.setProfit(totalProfit);
        response.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(Constants.ZONED_DATE_TIME_FORMAT)));
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_GET_BILL_REVENUE, true, response);
    }

    private void setDetail(RevenueCommonStatsResponse.Detail detail, Integer type, RevenueByMonth revenue) throws ParseException {
        if (type.equals(StatisticConstants.BY_MONTH)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.ZONED_DATE_TIME_FORMAT);
            String[] parts = revenue.getMonth().split("/");
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);

            YearMonth yearMonth = YearMonth.of(year, month);
            LocalDateTime startDate = yearMonth.atDay(1).atTime(LocalTime.MIN);
            LocalDateTime endDate = yearMonth.atEndOfMonth().atTime(LocalTime.MAX);
            detail.setFromDate(startDate.format(formatter));
            detail.setToDate(endDate.format(formatter));
        } else if (type.equals(StatisticConstants.BY_DAY) || type.equals(StatisticConstants.BY_HOUR)) {
            SimpleDateFormat inputFormat;
            if (type.equals(StatisticConstants.BY_DAY)) {
                inputFormat = new SimpleDateFormat(Util.DATE_PATTERN_2);
            } else {
                inputFormat = new SimpleDateFormat("yyyy/MM/dd HH");
            }
            SimpleDateFormat outputFormat = new SimpleDateFormat(Constants.ZONED_DATE_TIME_FORMAT);
            Date date = inputFormat.parse(revenue.getMonth());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            if (type.equals(StatisticConstants.BY_DAY)) {
                calendar.set(Calendar.HOUR, 0);
            }
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            detail.setFromDate(outputFormat.format(calendar.getTime()));

            if (type.equals(StatisticConstants.BY_DAY)) {
                calendar.set(Calendar.HOUR, 23);
            }
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            detail.setToDate(outputFormat.format(calendar.getTime()));
        }
        detail.setRevenue(revenue.getRevenue().setScale(6, RoundingMode.UNNECESSARY));
        detail.setProfit(revenue.getProfit().setScale(6, RoundingMode.UNNECESSARY));
    }

    private List<RevenueByMonth> processData(List<BillStatItem> revenues, List<BillStatItem> expenses) {
        List<RevenueByMonth> list = new ArrayList<>();
        int index = 0;
        for (int i = 0; i < revenues.size(); i++) {
            try {
                BillStatItem revenue = revenues.get(i);
                BillStatItem expense = expenses.get(index);
                if (expense.getTime().equals(revenue.getTime())) {
                    list.add(new RevenueByMonth(revenue.getTime(), revenue.getMoney(), revenue.getMoney().subtract(expense.getMoney())));
                    index++;
                } else {
                    throw new Exception();
                }
            } catch (Exception e) {
                list.add(new RevenueByMonth(revenues.get(i).getTime(), revenues.get(i).getMoney(), revenues.get(i).getMoney()));
            }
        }
        return list;
    }

    private List<RevenueByMonth> getAllRevenueEvenZero(List<RevenueByMonth> bills, Integer type, User user, String fromDate, String toDate)
        throws ParseException {
        List<RevenueByMonth> revenueByMonthList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        Calendar calendarToDate = Calendar.getInstance();
        List<String> monthList = new ArrayList<>();
        SimpleDateFormat dateFormat = null;
        if (type.equals(StatisticConstants.BY_MONTH)) {
            dateFormat = new SimpleDateFormat(BY_MONTH);
        } else if (type.equals(StatisticConstants.BY_DAY)) {
            dateFormat = new SimpleDateFormat(BY_DAY);
        } else if (type.equals(StatisticConstants.BY_HOUR)) {
            dateFormat = new SimpleDateFormat(BY_HOUR);
        }
        calendar.setTime(new SimpleDateFormat(Constants.ZONED_DATE_FORMAT).parse(fromDate));
        calendarToDate.setTime(new SimpleDateFormat(Constants.ZONED_DATE_FORMAT).parse(toDate));
        while (calendar.getTime().before(new SimpleDateFormat(Constants.ZONED_DATE_TIME_FORMAT).parse(toDate + " 23:59:59"))) {
            String monthString = dateFormat.format(calendar.getTime());
            monthList.add(monthString);
            if (type.equals(StatisticConstants.BY_MONTH)) {
                calendar.add(Calendar.MONTH, 1);
            } else if (type.equals(StatisticConstants.BY_DAY)) {
                calendar.add(Calendar.DATE, 1);
            } else if (type.equals(StatisticConstants.BY_HOUR)) {
                calendar.add(Calendar.HOUR, 1);
            }
        }
        List<String> monthRevenue = new ArrayList<>();
        for (RevenueByMonth revenue : bills) {
            monthRevenue.add(revenue.getMonth());
            revenueByMonthList.add(revenue);
        }
        for (String month : monthList) {
            if (!monthRevenue.contains(month)) {
                revenueByMonthList.add(
                    new RevenueByMonth(
                        month,
                        BigDecimal.ZERO.setScale(6, RoundingMode.UNNECESSARY),
                        BigDecimal.ZERO.setScale(6, RoundingMode.UNNECESSARY)
                    )
                );
            }
        }
        Collections.sort(revenueByMonthList);
        return revenueByMonthList;
    }

    static class ValidateInputData {

        public boolean status;
        public String message;

        public ValidateInputData() {}

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
