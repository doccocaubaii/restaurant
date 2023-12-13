package vn.softdreams.easypos.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.domain.CompanyUser;
import vn.softdreams.easypos.repository.CompanyUserRepositoryCustom;
import vn.softdreams.easypos.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompanyUserRepositoryImpl implements CompanyUserRepositoryCustom {

    private final Logger log = LoggerFactory.getLogger(UserRepositoryImpl.class);

    @Autowired
    private EntityManager entityManager;

    @Override
    public Page<CompanyUser> getAllCompanyUsers(Pageable pageable) {
        List<CompanyUser> companyUsers = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" from company_user cs join company c on cs.company_id = c.id " + "join ep_user eu on cs.user_id = eu.id  ");
        Query query = entityManager.createNativeQuery(
            "SELECT cs.id," +
            "cs.company_id as companyId, " +
            "c.name        as companyName, " +
            "cs.user_id    as userId, " +
            "eu.username  as username " +
            strQuery,
            "CompanyUserResponse"
        );
        Common.setParams(query, params);
        companyUsers = query.getResultList();

        return new PageImpl<>(companyUsers, pageable, companyUsers.size());
    }
}
