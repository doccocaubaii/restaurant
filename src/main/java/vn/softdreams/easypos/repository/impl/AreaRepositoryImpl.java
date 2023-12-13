package vn.softdreams.easypos.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import vn.softdreams.easypos.dto.area.AreaDetailResponse;
import vn.softdreams.easypos.repository.AreaRepositoryCustom;
import vn.softdreams.easypos.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;

public class AreaRepositoryImpl implements AreaRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<AreaDetailResponse> getAllWithPaging(Integer comId, Integer size, String keyword, Integer areaId) {
        List<AreaDetailResponse> areaResponses = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("  FROM area a WHERE a.com_id = :companyId ");
        params.put("companyId", comId);
        //        if (areaId != null) {
        //            strQuery.append(" and a.id = :areaId ");
        //            params.put("areaId", areaId);
        //        } else
        if (!Strings.isNullOrEmpty(keyword)) {
            strQuery.append(" and a.normalized_name like :keyword ");
            params.put("keyword", "%" + Common.normalizedName(Arrays.asList(keyword)) + "%");
        }
        strQuery.append(" order by a.name asc ");

        String selectQuery = "SELECT TOP(:size) a.id, a.com_id comId, a.name name, a.create_time createTime, a.update_time updateTime ";
        params.put("size", size);
        Query query = entityManager.createNativeQuery(selectQuery + strQuery, "AreaResponse");
        Common.setParams(query, params);
        areaResponses = query.getResultList();
        return areaResponses;
    }

    @Override
    public AreaDetailResponse getById(Integer id, Integer comId) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("  FROM area a WHERE a.id = :id ");
        //        params.put("companyId", comId);
        params.put("id", id);
        Query query = entityManager.createNativeQuery(
            "SELECT a.id id, a.com_id comId, a.name name, a.create_time createTime, a.update_time updateTime " + strQuery,
            "AreaResponse"
        );
        Common.setParams(query, params);
        List<AreaDetailResponse> areaResponses = query.getResultList();
        if (!areaResponses.isEmpty()) {
            return areaResponses.get(0);
        }
        return null;
    }
}
