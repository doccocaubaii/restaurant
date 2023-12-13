package vn.softdreams.easypos.service;

import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.dto.voucher.VoucherApplyAllRequest;
import vn.softdreams.easypos.dto.voucher.VoucherApplyRequest;
import vn.softdreams.easypos.dto.voucher.VoucherWebSaveRequest;
import vn.softdreams.easypos.service.dto.ResultDTO;

import java.util.List;

public interface VoucherWebManagementService {
    ResultDTO saveVoucher(VoucherWebSaveRequest request);

    ResultDTO applyVoucher(VoucherApplyRequest request);

    ResultDTO getWithPaging(Integer comId, String keyword, Pageable pageable, String fromDate, String toDate);

    ResultDTO deleteVoucher(Integer id);

    ResultDTO getAllVoucherForBill(Integer customerId, String keyword, Pageable pageable);
    ResultDTO checkValidVoucher(List<Integer> ids);

    ResultDTO getVoucherApplyDetail(Integer comId, Integer id, Integer type, Pageable pageable, String keyword, Boolean getDefault);

    ResultDTO applyAllVoucher(VoucherApplyAllRequest request);
}
