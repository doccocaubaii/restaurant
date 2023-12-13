package vn.softdreams.easypos.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.dto.invoice.InvoiceListResponse;
import vn.softdreams.easypos.dto.invoice.InvoiceSearchResponse;
import vn.softdreams.easypos.dto.invoice.InvoiceStatsResult;
import vn.softdreams.easypos.dto.product.SaleProductStatsRequest;
import vn.softdreams.easypos.dto.product.SaleProductStatsResult;
import vn.softdreams.easypos.service.dto.PublishListRequest;

import java.util.List;

public interface InvoiceRepositoryCustom {
    Page<InvoiceListResponse> getAllInvoices(
        Integer taxCheckStatus,
        String fromDate,
        String toDate,
        String pattern,
        String customerName,
        String no,
        Integer companyId,
        Pageable pageable,
        boolean isCountAll,
        boolean paramCheckAll,
        List<Integer> listId
    );

    List<InvoiceSearchResponse> findAllByCompanyIdAndIds(List<Integer> ids, Integer companyId);

    InvoiceStatsResult getInvoiceStats(Integer comId, String fromDate, String toDate);

    List<SaleProductStatsResult> getProductSaleStats(SaleProductStatsRequest request);

    List<Integer> getAllIdInvoices(PublishListRequest publishListRequest, Integer comId);
}
