package vn.softdreams.easypos.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.dto.receiptpayment.GetAllTransactionsAlternative;

import java.util.List;

@Repository
public interface ReceiptPaymentRepositoryCustom {
    GetAllTransactionsAlternative getAllTransactions(
        Pageable pageable,
        Integer comId,
        String fromDate,
        String toDate,
        Integer type,
        String keyword,
        Boolean isCountAll,
        boolean paramCheckAll,
        List<Integer> ids
    );
}
