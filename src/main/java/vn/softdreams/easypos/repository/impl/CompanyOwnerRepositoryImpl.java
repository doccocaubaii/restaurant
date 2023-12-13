package vn.softdreams.easypos.repository.impl;

import com.google.common.base.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.dto.companyOwner.OwnerResult;
import vn.softdreams.easypos.repository.CompanyOwnerRepositoryCustom;
import vn.softdreams.easypos.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompanyOwnerRepositoryImpl implements CompanyOwnerRepositoryCustom {

    private final EntityManager entityManager;

    public CompanyOwnerRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Page<OwnerResult> getAllForAdmin(Pageable pageable, String keyword, String fromDate, String toDate) {
        List<OwnerResult> responses = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" from company_owner co where 1=1 ");

        if (!Strings.isNullOrEmpty(keyword)) {
            strQuery.append(" and (co.name like :keyword or co.address like :keyword or co.tax_code like :keyword) ");
            params.put("keyword", "%" + keyword + "%");
        }

        if (!Strings.isNullOrEmpty(fromDate) || !Strings.isNullOrEmpty(toDate)) {
            Common.addDateSearchCustom(fromDate, toDate, params, strQuery, "co.create_time", "company_owner");
        }
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(*) " + strQuery);
        Common.setParams(countQuery, params);
        Number count = (Number) countQuery.getSingleResult();
        if (count.intValue() > 0) {
            Query query = entityManager.createNativeQuery(
                "select co.id id, " +
                "co.name name, " +
                "co.address address, " +
                "co.tax_code taxCode, " +
                "co.owner_name ownerName, " +
                "co.create_time createTime, " +
                "co.update_time updateTime " +
                strQuery +
                Common.addSort(pageable.getSort(), params),
                "OwnerResult"
            );
            Common.setParamsWithPageable(query, params, pageable);
            responses = query.getResultList();
        }
        return new PageImpl<>(responses, pageable, count.longValue());
    }
}
