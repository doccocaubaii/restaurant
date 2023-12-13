package vn.softdreams.easypos.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContext;
import vn.softdreams.easypos.domain.Bill;
import vn.softdreams.easypos.dto.bill.*;
import vn.softdreams.easypos.service.dto.ResultDTO;

import java.util.List;

public interface BillManagementService {
    ResultDTO searchBills(Pageable pageable, Integer status, String fromDate, String toDate, String keyWord, Boolean isCountAll);

    ResultDTO getBillByIdAndCompanyId(Integer id);
    ResultDTO getBillById(Integer id);

    ResultDTO updateBillActivate(BillCompleteRequest statusBillDTO);

    ResultDTO saveBill(BillCreateRequest billDTO) throws JsonProcessingException;

    void billCompletionAsync(Bill bill, SecurityContext comId);

    ResultDTO cancelBillByID(BillCancelRequest billCancelRequest);

    ResultDTO getBillByCode(String code);

    ResultDTO payOffDebt(PayOffDebtRequest payOffDebtRequest);

    ResultBillAsync createBillSync(List<BillCreateRequest> billDTOs) throws JsonProcessingException;

    void listBillCompletionAsync(List<Bill> bills, SecurityContext context);

    ResultDTO updateBill(BillCreateRequest billDTO);
    ResultDTO checkUpdateBill(Integer comId, Integer billId);
    ResultDTO cancelBillCompleted(BillCompletedCancelRequest request);
    ResultDTO returnBillCompleted(BillCompletedReturnRequest request);
    ResultDTO getForProcessing(Pageable pageable, Integer type, Integer status);
}
