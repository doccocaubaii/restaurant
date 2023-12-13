package vn.softdreams.easypos.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.dto.company.CompanyResult;
import vn.softdreams.easypos.dto.company.CompanyResultItem;
import vn.softdreams.easypos.dto.companyUser.CompanyOwnerResponse;
import vn.softdreams.easypos.repository.CompanyRepositoryCustom;
import vn.softdreams.easypos.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompanyRepositoryImpl implements CompanyRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    @Override
    public CompanyResult getCompanyById(Integer companyId) {
        List<CompanyResult> results = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(
            "  from company c join company_owner co on c.com_owner_id = co.id left join business b on b.id = c.business_id where c.id = :companyId"
        );
        params.put("companyId", companyId);

        Query query = entityManager.createNativeQuery(
            "select c.id id, " +
            "c.name name, " +
            "c.phone phone, " +
            "c.address address, " +
            "c.business_id businessId, " +
            "b.name businessName, " +
            "co.id companyOwnerId, " +
            "co.name companyOwnerName,  " +
            "co.address companyOwnerAddress,  " +
            "co.tax_machine_code companyOwnerTaxMachineCode, " +
            "co.tax_code taxCode, " +
            "c.description description " +
            strQuery,
            "CompanyDetailResult"
        );
        Common.setParams(query, params);
        results = query.getResultList();
        if (results.isEmpty()) {
            return null;
        }
        return results.get(0);
    }

    @Override
    public List<CompanyResult> findAllCompanyCustomByUserID(Integer id) {
        List<CompanyResult> companyResultDTOS = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" from company c ");
        strQuery.append(" join company_owner co on c.com_owner_id = co.id ");
        strQuery.append(" join company_user cu on c.id = cu.company_id ");
        strQuery.append(" where cu.user_id = :id ");
        params.put("id", id);

        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(*) " + strQuery);
        Common.setParams(countQuery, params);
        Number count = (Number) countQuery.getSingleResult();
        if (count.intValue() > 0) {
            Query query = entityManager.createNativeQuery(
                "select c.id,  " +
                "       c.name,  " +
                "       c.phone,  " +
                "       c.address,  " +
                "       co.id               companyOwnerId,  " +
                "       co.name             companyOwnerName,  " +
                "       co.address          companyOwnerAddress,  " +
                "       co.tax_machine_code companyOwnerTaxMachineCode, " +
                "       co.tax_code taxCode, " +
                "       c.service service " +
                strQuery,
                "CompanyResult"
            );
            Common.setParams(query, params);
            companyResultDTOS = query.getResultList();
        }
        return companyResultDTOS;
    }

    @Override
    public CompanyOwnerResponse findCompanyOwnerByCompanyId(Integer comId) {
        CompanyOwnerResponse companyResultDTOS = new CompanyOwnerResponse();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("  from company_owner co left join company c on co.id = c.com_owner_id where c.id = :comId ");
        params.put("comId", comId);
        Query query = entityManager.createNativeQuery(
            "select c.id                    comId, " +
            "       co.name                 name, " +
            "       co.tax_code             taxCode, " +
            "       co.tax_machine_code     taxRegisterCode, " +
            "       co.tax_register_time    taxRegisterTime, " +
            "       co.tax_register_message taxRegisterMessage " +
            strQuery,
            "CompanyOwnerResponse"
        );
        Common.setParams(query, params);
        companyResultDTOS = (CompanyOwnerResponse) query.getSingleResult();
        return companyResultDTOS;
    }

    @Override
    public List<CompanyResultItem> findAllCompany(String input) {
        List<CompanyResultItem> list = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("  from company c WHERE 1 = 1 ");
        if (!Strings.isNullOrEmpty(input)) {
            strQuery.append(" AND c.name like :input ");
            params.put("input", "%" + input + "%");
        }

        Query query = entityManager.createNativeQuery("select c.id value, " + "       c.name name " + strQuery, "CompanyResultItemDTO");
        Common.setParams(query, params);
        list = query.getResultList();

        return list;
    }

    @Override
    public Page<CompanyResult> getAllWithPaging(
        Pageable pageable,
        List<String> ownerIds,
        Integer ownerId,
        String keyword,
        String fromDate,
        String toDate
    ) {
        List<CompanyResult> responses = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" from company c join company_owner co on c.com_owner_id = co.id ");
        strQuery.append(
            " left join config c2 on c.id = c2.company_id and c2.code in :codes and (c2.value != '' and c2.value is not null) where 1=1 "
        );
        params.put("codes", ownerIds);

        if (ownerId != null) {
            strQuery.append(" and c.com_owner_id = :ownerId ");
            params.put("ownerId", ownerId);
        }

        if (!Strings.isNullOrEmpty(keyword)) {
            strQuery.append(" and (c.normalized_name like :keyword or co.name like :keywordOwner or co.tax_code like :keywordOwner) ");
            params.put("keyword", "%" + Common.normalizedName(List.of(keyword)) + "%");
            params.put("keywordOwner", "%" + keyword + "%");
        }

        if (!Strings.isNullOrEmpty(fromDate) || !Strings.isNullOrEmpty(toDate)) {
            Common.addDateSearchCustom(fromDate, toDate, params, strQuery, "c.create_time", "company");
        }
        strQuery.append(" group by c.id, c.name, c.phone, c.address, co.id, co.name, co.address, co.tax_machine_code, co.tax_code ");
        Query countQuery = entityManager.createNativeQuery("SELECT top 1 COUNT(*) OVER () " + strQuery);
        Common.setParams(countQuery, params);
        Number count = (Number) countQuery.getSingleResult();
        if (count.intValue() > 0) {
            Query query = entityManager.createNativeQuery(
                "select c.id id, " +
                "c.name name, " +
                "c.phone phone, " +
                "c.address address, " +
                "co.id companyOwnerId, " +
                "co.name companyOwnerName, " +
                "co.address companyOwnerAddress, " +
                "co.tax_machine_code companyOwnerTaxMachineCode, " +
                "co.tax_code taxCode, " +
                "count(c2.id) countConfig " +
                strQuery +
                Common.addSort(pageable.getSort(), params),
                "CompanyForAdminResult"
            );
            Common.setParamsWithPageable(query, params, pageable);
            responses = query.getResultList();
        }
        return new PageImpl<>(responses, pageable, count.longValue());
    }
}
