package vn.softdreams.easypos.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import vn.softdreams.easypos.dto.areaUnit.AreaUnitDetailResponse;
import vn.softdreams.easypos.repository.AreaUnitRepositoryCustom;
import vn.softdreams.easypos.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;

public class AreaUnitRepositoryImpl implements AreaUnitRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    @Override
    public AreaUnitDetailResponse getById(Integer id, Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("  from area_unit au where au.id = :id and au.com_id = :comId");
        params.put("id", id);
        params.put("comId", companyId);
        Query query = entityManager.createNativeQuery(
            "select " +
            "au.id, au.com_id comId, au.area_id areaId, au.name, " +
            "au.create_time createTime, au.update_time updateTime " +
            strQuery,
            "AreaUnitResponse"
        );
        Common.setParams(query, params);
        List<AreaUnitDetailResponse> unitResponses = query.getResultList();

        if (unitResponses.size() == 1) {
            return unitResponses.get(0);
        }

        return null;
    }

    @Override
    public List<AreaUnitDetailResponse> getAllWithPaging(Integer comId, List<Integer> areaIds, Integer size, String areaUnitKeyword) {
        List<AreaUnitDetailResponse> unitResponses = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(
            "  from (select " +
            "au.id, au.com_id comId, au.area_id areaId, au.name, " +
            "au.create_time createTime, au.update_time updateTime, " +
            "row_number() over (partition by a.id order by a.id) rowNumber " +
            "from area_unit au " +
            "join area a on au.area_id = a.id " +
            "where a.id in :areaIds and a.com_id = :comId and au.normalized_name like :keyword ) " +
            "tblUnit where tblUnit.rowNumber <= :size order by id asc"
        );
        params.put("comId", comId);
        params.put("areaIds", areaIds);
        params.put("size", size);
        params.put("keyword", "%" + Common.normalizedName(Arrays.asList(areaUnitKeyword)) + "%");
        Query query = entityManager.createNativeQuery(
            "select id, comId, areaId, name, createTime, updateTime " + strQuery,
            "AreaUnitResponse"
        );
        Common.setParams(query, params);
        unitResponses = query.getResultList();
        return unitResponses;
    }
}
