package vn.softdreams.easypos.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.dto.voucher.VoucherResponse;
import vn.softdreams.easypos.dto.voucher.VoucherWebResponse;

import java.util.List;

public interface VoucherRepositoryCustom {
    Page<VoucherResponse> getWithPaging(
        Integer comId,
        Pageable pageable,
        String keyword,
        List<Integer> status,
        String fromDate,
        String toDate,
        Integer type
    );

    Page<VoucherResponse> getVoucherForBill(Integer comId, Pageable pageable, String keyword, Integer customerId);

    Page<VoucherWebResponse> getWithPagingWeb(
        Integer comId,
        Pageable pageable,
        String keyword,
        List<Integer> status,
        String fromDate,
        String toDate
    );

    Page<VoucherWebResponse> getVoucherForBillWeb(Integer comId, Pageable pageable, String keyword, Integer customerId);
}
