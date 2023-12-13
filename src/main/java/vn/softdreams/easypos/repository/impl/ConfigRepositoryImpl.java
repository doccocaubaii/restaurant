package vn.softdreams.easypos.repository.impl;

import com.google.common.base.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.dto.config.ConfigOwnerResult;
import vn.softdreams.easypos.dto.config.ConfigResult;
import vn.softdreams.easypos.dto.config.ConfigStatusResult;
import vn.softdreams.easypos.repository.ConfigRepositoryCustom;
import vn.softdreams.easypos.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigRepositoryImpl implements ConfigRepositoryCustom {

    private final EntityManager entityManager;

    public ConfigRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Page<ConfigResult> getWithPaging(Integer comId, Pageable pageable, String keyword, String fromDate, String toDate) {
        List<ConfigResult> configsResult = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();

        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" from config c join company c1 on c1.id = c.company_id where 1 = 1 ");
        if (comId != null) {
            strQuery.append(" and c.company_id = :comId ");
            params.put("comId", comId);
        }

        if (!Strings.isNullOrEmpty(keyword)) {
            strQuery.append(" and (c.code like :keyword or c.description like :keyword) ");
            params.put("keyword", "%" + keyword + "%");
        }

        if (!Strings.isNullOrEmpty(fromDate) || !Strings.isNullOrEmpty(toDate)) {
            Common.addDateSearchCustom(fromDate, toDate, params, strQuery, "c.create_time", "config");
        }

        Query countQuery = entityManager.createNativeQuery("select count(*) " + strQuery);
        Common.setParams(countQuery, params);
        Number count = (Number) countQuery.getSingleResult();
        if (count.intValue() > 0) {
            Query query = entityManager.createNativeQuery(
                "select c.id id, c.code code, c.value value, c.description description, c1.name companyName, c.create_time createTime, c.update_time updateTime " +
                strQuery +
                Common.addSort(pageable.getSort(), params),
                "ConfigResult"
            );
            Common.setParamsWithPageable(query, params, pageable);
            configsResult = query.getResultList();
        }
        return new PageImpl<>(configsResult, pageable, count.longValue());
    }

    @Override
    public List<ConfigOwnerResult> getConfigsByCompanyOwnerOrCompany(Integer ownerId, List<String> codes, Integer companyId) {
        List<ConfigOwnerResult> configsResult;
        Map<String, Object> params = new HashMap<>();

        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" from config c join company c2 on c.company_id = c2.id where code in :codes ");
        if (ownerId != null) {
            strQuery.append(" and c2.is_parent = 1 and c2.com_owner_id = :ownerId ");
            params.put("ownerId", ownerId);
        } else if (companyId != null) {
            strQuery.append(" and c2.id = :companyId ");
            params.put("companyId", companyId);
        }

        params.put("codes", codes);
        Query query = entityManager.createNativeQuery(
            "select c.code code, c.value value, c.description description " + strQuery,
            "ConfigOwnerResult"
        );
        Common.setParams(query, params);
        configsResult = query.getResultList();
        return configsResult;
    }

    @Override
    public List<ConfigStatusResult> getConfigsStatusByOwner(List<Integer> ownerIds, List<String> codes) {
        List<ConfigStatusResult> results;
        Map<String, Object> params = new HashMap<>();

        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" from tblCountCompany t1 left join tblCountConfig t2 on t1.id = t2.com_owner_id ");

        Query query = entityManager.createNativeQuery(
            "with tblCountCompany as ( " +
            "select co.id, count(c.id) countCompany " +
            "from company_owner co " +
            "join company c on c.com_owner_id = co.id " +
            "where co.id in :ownerIds " +
            "group by co.id " +
            "), " +
            "tblCountConfig as ( " +
            "select c.com_owner_id, count(cf.id) countConfig " +
            "from company c " +
            "join config cf on c.id = cf.company_id " +
            "where c.com_owner_id in :ownerIds " +
            "and cf.code in :codes and cf.value != '' and cf.value is not null " +
            "group by c.com_owner_id " +
            ") " +
            "select  " +
            "t1.id companyId, " +
            "iif(t1.countCompany * :sizeCode = t2.countConfig, 1, 0) status " +
            strQuery,
            "ConfigStatusResult"
        );
        params.put("ownerIds", ownerIds);
        params.put("codes", codes);
        params.put("sizeCode", codes.size());
        Common.setParams(query, params);
        results = query.getResultList();
        return results;
    }

    @Override
    public List<ConfigStatusResult> getConfigsStatusByCompanies(List<Integer> companies, List<String> codes) {
        return null;
    }
}
