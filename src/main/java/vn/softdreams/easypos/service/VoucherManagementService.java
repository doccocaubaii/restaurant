package vn.softdreams.easypos.service;

import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.domain.Voucher;
import vn.softdreams.easypos.dto.voucher.VoucherApplyAllRequest;
import vn.softdreams.easypos.dto.voucher.VoucherApplyRequest;
import vn.softdreams.easypos.dto.voucher.VoucherSaveRequest;
import vn.softdreams.easypos.service.dto.ResultDTO;

import java.util.List;

/**
 * Service Interface for managing {@link Voucher}.
 */
public interface VoucherManagementService {
    ResultDTO saveVoucher(VoucherSaveRequest request);

    ResultDTO applyVoucher(VoucherApplyRequest request);

    ResultDTO getWithPaging(Integer comId, String keyword, Pageable pageable, String fromDate, String toDate, Integer type);

    ResultDTO deleteVoucher(Integer id);

    ResultDTO getAllVoucherForBill(Integer customerId, String keyword, Pageable pageable);
    ResultDTO checkValidVoucher(List<Integer> ids);

    ResultDTO getVoucherApplyDetail(Integer comId, Integer id, Integer type, Pageable pageable, String keyword, Boolean getDefault);

    ResultDTO applyAllVoucher(VoucherApplyAllRequest request);
}
