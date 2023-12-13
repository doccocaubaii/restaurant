package vn.softdreams.easypos.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.domain.Bill;
import vn.softdreams.easypos.dto.audit.ActivityHistoryResponse;
import vn.softdreams.easypos.dto.audit.ActivityHistoryResult;
import vn.softdreams.easypos.dto.bill.*;
import vn.softdreams.easypos.dto.product.HotSaleProductResult;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface BillRepositoryCustom {
    List<BillUnitResponse> getBillUnit(Integer comId, Integer deliveryType, Integer status, List<Integer> areaUnitIds);

    BillStatsResult getBillStats(Integer comId, String fromDate, String toDate);
    Page<BillItemResponse> searchBills(
        Pageable pageable,
        Integer status,
        String fromDate,
        String toDate,
        String keyword,
        Integer comId,
        Boolean isCountAll
    );

    Optional<Bill> findByIdAndComIdCustom(Integer billId, Integer companyId);

    BigDecimal getBillRevenue(String formDate, String toDate, Integer comId);
    List<BillStatItem> getBillMoney(Integer comId, String fromDate, String toDate, String format);
    List<BillStatItem> getBillExpense(Integer comId, String fromDate, String toDate, String format);
    List<BillProductChangeUnit> checkChangeUnit(
        Integer comId,
        List<Integer> productIds,
        List<Integer> productUnitIds,
        List<String> unitNames
    );

    List<HotSaleProductResult> getHotSaleProductStats(Integer comId, String fromDate, String toDate, Integer type);
    Page<ActivityHistoryResult> getActivityHistory(Integer comId, String fromDate, String toDate, Pageable pageable);
    List<ActivityHistoryResponse> getAllActivityHistory(List<Integer> billIds, List<Integer> invoiceIds);
    BigDecimal countTotalMoneyForUpdateCard(Integer cusId, String fromDate, String toDate);
    Page<BillProductProcessing> getForProcessing(Integer comId, Integer type, Integer status, List<Integer> ids);
    Page<BillProductProcessing> getDeleteRequest(Integer comId);
}
