package vn.softdreams.easypos.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import vn.softdreams.easypos.dto.product.SaleProductStatsRequest;
import vn.softdreams.easypos.service.ReportManagementService;
import vn.softdreams.easypos.service.dto.CustomPdf;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.util.Common;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api")
public class ReportManagementResource {

    private final Logger log = LoggerFactory.getLogger(ReportManagementService.class);

    private final ReportManagementService reportManagementService;

    public ReportManagementResource(ReportManagementService reportManagementService) {
        this.reportManagementService = reportManagementService;
    }

    @GetMapping("/client/page/home/revenue-common-stats")
    public ResultDTO revenueCommonStats(
        @RequestParam Integer comId,
        @RequestParam String fromDate,
        @RequestParam String toDate,
        @RequestParam(required = false) Integer type
    ) throws Exception {
        return reportManagementService.getBillRevenue(comId, fromDate, toDate, type);
    }

    @GetMapping("/client/page/report/inventory-common-stats")
    public ResultDTO inventoryCommonStats(@RequestParam Integer comId, @RequestParam String fromDate, @RequestParam String toDate)
        throws Exception {
        return reportManagementService.getInventoryStats(comId, fromDate, toDate);
    }

    @GetMapping("/client/page/report/revenue-cost-price-by-product")
    public ResultDTO revenueCostPriceByProduct(@RequestParam Integer comId, @RequestParam String fromDate, @RequestParam String toDate)
        throws Exception {
        return reportManagementService.getRevenueCostPrice(comId, fromDate, toDate);
    }

    @GetMapping("/client/page/report/general-input-output-inventory")
    public ResultDTO generalInventory(@RequestParam Integer comId, @RequestParam String fromDate, @RequestParam String toDate)
        throws Exception {
        return reportManagementService.getGeneralInventory(comId, fromDate, toDate);
    }

    @GetMapping("/client/page/report/inventory-common-stats-v2")
    public ResultDTO inventoryCommonStatsV2(
        @NotNull @RequestParam Integer comId,
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) Integer groupId
    ) {
        return reportManagementService.inventoryCommonStatsV2(comId, groupId, keyword);
    }

    @GetMapping("/client/page/report/product-profit-stats")
    public ResultDTO productProfitStats(
        @RequestParam(required = false) String productProductUnitIds,
        @NotNull @RequestParam Integer comId,
        @NotBlank @RequestParam String fromDate,
        @NotBlank @RequestParam String toDate,
        @RequestParam(required = false) Integer type,
        @RequestParam(required = false) Boolean isPaging,
        @RequestParam(required = false) Integer sortType,
        Pageable pageable
    ) {
        return reportManagementService.productProfitStats(
            productProductUnitIds,
            comId,
            fromDate,
            toDate,
            type,
            pageable,
            isPaging,
            sortType
        );
    }

    @GetMapping("/client/page/report/product-hot-sales-stats")
    public ResultDTO hotSaleProductStats(
        @NotNull @RequestParam Integer comId,
        @NotBlank @RequestParam String fromDate,
        @NotBlank @RequestParam String toDate,
        @RequestParam(required = false) Integer type
    ) {
        return reportManagementService.hotSaleProductStats(comId, fromDate, toDate, type);
    }

    @GetMapping("/client/page/report/product-sales-stats")
    public ResultDTO hotSaleProductStats(
        @NotNull @RequestParam Integer comId,
        @NotBlank @RequestParam String fromDate,
        @NotBlank @RequestParam String toDate,
        @RequestParam(required = false) String pattern,
        @RequestParam(required = false) Integer status,
        @RequestParam(required = false) Integer taxCheckStatus
    ) {
        SaleProductStatsRequest request = new SaleProductStatsRequest(comId, fromDate, toDate, status, taxCheckStatus, pattern);
        return reportManagementService.saleProductStats(request);
    }

    @GetMapping("/client/page/report/activity-history-stats")
    public ResultDTO activityHistoryStats(
        @NotNull @RequestParam Integer comId,
        @NotBlank @RequestParam String fromDate,
        @NotBlank @RequestParam String toDate,
        Pageable pageable
    ) {
        return reportManagementService.activityHistoryStats(comId, fromDate, toDate, pageable);
    }

    @PostMapping("/p/client/page/report/custom-pdf")
    public byte[] convertHtmlToPdf(@RequestBody CustomPdf customPdf) {
        return Common.convertHtmlToPdf(customPdf);
    }
}
