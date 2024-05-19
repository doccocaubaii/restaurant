package vn.hust.easypos.repository.impl;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import vn.hust.easypos.repository.CompanyRepositoryCustom;
import vn.hust.easypos.service.dto.company.CompanyResult;

import java.util.List;

@Repository
public class CompanyRepositoryImpl implements CompanyRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<CompanyResult> findAllCompanyByCompanyID(Integer id) {
        return null;
//        CompanyResult companyResult = new CompanyResult();
//
//        List<CompanyResult> companyResultDTOS = new ArrayList<>();
//        Map<String, Object> params = new HashMap<>();
//        StringBuilder strQuery = new StringBuilder();
//        strQuery.append(" from company c ");
////        strQuery.append(" join company_user cu on c.id = cu.company_id ");
//        strQuery.append(" where c.id = :id ");
//        params.put("id", id);
//        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(*) " + strQuery);
//        Common.setParams(countQuery, params);
//        Number count = (Number) countQuery.getSingleResult();
//        if (count.intValue() > 0) {
//            Query query = entityManager.createNativeQuery("select c.id,  " + "       c.name  " + strQuery, "CompanyResult");
//            Common.setParams(query, params);
//            companyResultDTOS = query.getResultList();
//        }
//        return companyResultDTOS;
    }
}
