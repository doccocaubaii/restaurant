package vn.softdreams.easypos.repository;

import vn.softdreams.easypos.dto.businessType.GetAllTransactionsResponse;

import java.util.List;

public interface BusinessTypeRepositoryCustom {
    List<GetAllTransactionsResponse> getAllTransactions(Integer comId, Integer type, String keyword);
}
