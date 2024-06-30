package vn.hust.restaurant.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.hust.restaurant.service.dto.bill.BillItemResponse;
import vn.hust.restaurant.service.dto.bill.BillStatItem;
import vn.hust.restaurant.service.dto.bill.BillStatsResult;

public interface BillRepositoryCustom {
    Page<BillItemResponse> searchBills(
        Pageable pageable,
        Integer status,
        String fromDate,
        String toDate,
        String keyword,
        Integer companyId
    );

    List<BillStatItem> getBillMoney(Integer companyId, String fromDate, String toDate, String format);

    BillStatsResult getBillStats(Integer comId, String fromDate, String toDate);
}
