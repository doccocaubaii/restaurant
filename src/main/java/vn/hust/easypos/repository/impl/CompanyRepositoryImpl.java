package vn.hust.easypos.repository.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import vn.hust.easypos.repository.CompanyRepositoryCustom;
import vn.hust.easypos.service.dto.company.CompanyResult;
import vn.hust.easypos.service.util.Common;

public class CompanyRepositoryImpl implements CompanyRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<CompanyResult> findAllCompanyCustomByUserID(Integer id) {
        List<CompanyResult> companyResultDTOS = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" from company c ");
        strQuery.append(" join company_user cu on c.id = cu.company_id ");
        strQuery.append(" where cu.user_id = :id ");
        params.put("id", id);
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(*) " + strQuery);
        Common.setParams(countQuery, params);
        Number count = (Number) countQuery.getSingleResult();
        if (count.intValue() > 0) {
            Query query = entityManager.createNativeQuery("select c.id,  " + "       c.name  " + strQuery, "CompanyResult");
            Common.setParams(query, params);
            companyResultDTOS = query.getResultList();
        }
        return companyResultDTOS;
    }
}
