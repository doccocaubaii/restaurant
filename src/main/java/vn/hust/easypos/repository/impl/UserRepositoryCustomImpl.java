package vn.hust.easypos.repository.impl;

import com.google.common.base.Strings;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import vn.hust.easypos.repository.UserRepositoryCustom;
import vn.hust.easypos.service.dto.StaffResponse;
import vn.hust.easypos.service.util.Common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Page<StaffResponse> searchStaffs(Pageable pageable, String keyword, Integer companyId) {
        List<StaffResponse> staffList = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" from EP_USER u ");
        strQuery.append(" WHERE company_id = :comId ");
        params.put("comId", companyId);
        if (!Strings.isNullOrEmpty(keyword)) {
            strQuery.append(" AND (u.normalized_name like :keyword) ");
            params.put("keyword", "%" + keyword.toLowerCase() + "%");
        }
        strQuery.append(" AND u.company_id <> u.id ");
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(*) " + strQuery);
        Common.setParams(countQuery, params);
        Number count = (Number) countQuery.getSingleResult();
        if (count.intValue() > 0) {
            String sort = " order by username desc ";
            Query query = entityManager.createNativeQuery(
                "select id,email,full_name,username,phone_number,status " +
                    strQuery +
                    sort,
                "StaffResponse"
            );
            Common.setParamsWithPageable(query, params, pageable);
            staffList = query.getResultList();
        }

        return new PageImpl<>(staffList, pageable, count.intValue());
    }
}
