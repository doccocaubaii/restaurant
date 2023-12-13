package vn.softdreams.easypos.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.dto.printSetting.PrintSettingItemResponse;
import vn.softdreams.easypos.repository.PrintSettingRepositoryCustom;
import vn.softdreams.easypos.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;

public class PrintSettingRepositoryImpl implements PrintSettingRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Page<PrintSettingItemResponse> getWithPaging(Pageable pageable, Integer comId, Integer userId, String keyword) {
        List<PrintSettingItemResponse> configList = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" from print_setting where com_id = :comId and user_id = :userId and type is not null ");
        params.put("comId", comId);
        params.put("userId", userId);
        if (!Strings.isNullOrEmpty(keyword)) {
            String keywordReplace = Common.normalizedName(Arrays.asList(keyword));
            strQuery.append(" AND normalized_name like :keyword ");
            params.put("keyword", "%" + keywordReplace + "%");
        }
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(*) " + strQuery);
        Common.setParams(countQuery, params);
        Number count = (Number) countQuery.getSingleResult();
        if (count.intValue() > 0) {
            strQuery.append(" order by print_name asc ");
            Query query = entityManager.createNativeQuery(
                "select id, " +
                "       com_id comId, " +
                "       print_name printName, " +
                "       ip_address ipAddress, " +
                "       type, " +
                "       page_size pageSize, " +
                "       processing_area_id processingAreaId, " +
                "       type_template typeTemplate " +
                strQuery,
                "PrintSettingItemResponse"
            );
            Common.setParamsWithPageable(query, params, pageable);
            configList = query.getResultList();
        }
        return new PageImpl<>(configList, pageable, count.longValue());
    }
}
