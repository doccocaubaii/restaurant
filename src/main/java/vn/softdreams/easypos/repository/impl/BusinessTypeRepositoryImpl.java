package vn.softdreams.easypos.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import vn.softdreams.easypos.dto.businessType.GetAllTransactionsResponse;
import vn.softdreams.easypos.repository.BusinessTypeRepositoryCustom;
import vn.softdreams.easypos.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BusinessTypeRepositoryImpl implements BusinessTypeRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<GetAllTransactionsResponse> getAllTransactions(Integer comId, Integer type, String keyword) {
        List<GetAllTransactionsResponse> responses;
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" FROM business_type b WHERE b.com_id = :comId ");
        params.put("comId", comId);

        if (type != null) {
            strQuery.append(" AND b.type = :type ");
            params.put("type", type);
        }

        if (!Strings.isNullOrEmpty(keyword)) {
            strQuery.append(" AND b.business_type_name LIKE :keyword ");
            params.put("keyword", "%" + keyword + "%");
        }

        StringBuilder strQuerySelect = new StringBuilder();
        strQuerySelect.append(
            " SELECT b.id id," +
            "b.type type," +
            "b.com_id comId," +
            "b.business_type_code businessTypeCode," +
            "b.business_type_name businessTypeName "
        );
        strQuery.append(" ORDER BY b.business_type_name ");
        Query query = entityManager.createNativeQuery(String.valueOf(strQuerySelect.append(strQuery)), "GetAllTransactionsResponse");
        Common.setParams(query, params);
        responses = query.getResultList();

        return responses;
    }
}
