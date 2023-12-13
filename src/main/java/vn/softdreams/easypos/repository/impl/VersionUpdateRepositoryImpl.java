package vn.softdreams.easypos.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.domain.VersionUpdate;
import vn.softdreams.easypos.repository.VersionUpdateCustom;
import vn.softdreams.easypos.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VersionUpdateRepositoryImpl implements VersionUpdateCustom {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Page<VersionUpdate> getWithPaging(Pageable pageable, Integer comId, String fromDate, String toDate) {
        List<VersionUpdate> versionUpdates = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" from version_update ");
        strQuery.append(" WHERE 1 = 1");
        if (comId != null) {
            strQuery.append(" AND com_id = :comId ");
            params.put("comId", comId);
        }
        Common.addDateSearchCustom(fromDate, toDate, params, strQuery, "date", "v");
        Common.replaceWhere(strQuery);
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(*) " + strQuery);
        Common.setParams(countQuery, params);
        Number count = (Number) countQuery.getSingleResult();
        if (count.intValue() > 0) {
            if (!pageable.getSort().isEmpty()) {
                strQuery.append(" ORDER BY ").append(pageable.getSort().toString().replace(":", ""));
            }
            Query query = entityManager.createNativeQuery(
                "select " +
                "    id, " +
                "    com_id, " +
                "    version, " +
                "    description, " +
                "    link, " +
                "    date, " +
                "    start_date, " +
                "    end_date, " +
                "    image, " +
                "    system" +
                strQuery,
                "VersionUpdateDTO"
            );
            Common.setParamsWithPageable(query, params, pageable, count);
            versionUpdates = query.getResultList();
        }
        return new PageImpl<>(versionUpdates, pageable, count.longValue());
    }
}
