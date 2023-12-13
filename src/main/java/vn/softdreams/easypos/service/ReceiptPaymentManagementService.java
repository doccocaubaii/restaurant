package vn.softdreams.easypos.service;

import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.dto.receiptpayment.DeleteReceiptPaymentList;
import vn.softdreams.easypos.dto.receiptpayment.ReceiptPaymentCreateRequest;
import vn.softdreams.easypos.dto.receiptpayment.ReceiptPaymentDeleteRequest;
import vn.softdreams.easypos.dto.receiptpayment.ReceiptPaymentUpdateRequest;
import vn.softdreams.easypos.service.dto.ResultDTO;

public interface ReceiptPaymentManagementService {
    ResultDTO getAllTransactions(
        Pageable pageable,
        Integer comId,
        String fromDate,
        String toDate,
        Integer type,
        String keyword,
        Boolean isCountAll
    );

    ResultDTO getById(Integer comId, Integer type, Integer id);

    ResultDTO create(ReceiptPaymentCreateRequest request);

    ResultDTO update(ReceiptPaymentUpdateRequest request);

    ResultDTO delete(ReceiptPaymentDeleteRequest request);

    ResultDTO insertMissingData();
    ResultDTO deleteList(DeleteReceiptPaymentList request);
}
