package vn.softdreams.easypos.service;

import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.dto.product.SaleProductStatsRequest;
import vn.softdreams.easypos.service.dto.ResultDTO;

import java.text.ParseException;

public interface ReportManagementService {
    ResultDTO getBillRevenue(Integer comId, String fromDate, String toDate, Integer type) throws ParseException;
    ResultDTO getInventoryStats(Integer comId, String fromDate, String toDate) throws Exception;

    ResultDTO getRevenueCostPrice(Integer comId, String fromDate, String toDate) throws Exception;

    ResultDTO getGeneralInventory(Integer comId, String fromDate, String toDate) throws Exception;

    ResultDTO inventoryCommonStatsV2(Integer comId, Integer groupId, String keyword);

    ResultDTO productProfitStats(
        String productProductUnitIds,
        Integer comId,
        String fromDate,
        String toDate,
        Integer type,
        Pageable pageable,
        Boolean isPaging,
        Integer sortType
    );

    ResultDTO hotSaleProductStats(Integer comId, String fromDate, String toDate, Integer type);

    ResultDTO saleProductStats(SaleProductStatsRequest request);
    ResultDTO activityHistoryStats(Integer comId, String fromDate, String toDate, Pageable pageable);
}
