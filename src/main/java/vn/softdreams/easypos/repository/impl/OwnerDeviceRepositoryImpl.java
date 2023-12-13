package vn.softdreams.easypos.repository.impl;

import com.google.common.base.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.dto.ownerDevice.OwnerDeviceResult;
import vn.softdreams.easypos.repository.OwnerDeviceRepositoryCustom;
import vn.softdreams.easypos.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OwnerDeviceRepositoryImpl implements OwnerDeviceRepositoryCustom {

    private final EntityManager entityManager;

    public OwnerDeviceRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Page<OwnerDeviceResult> getWithPaging(Pageable pageable, Integer ownerId, String keyword, String fromDate, String toDate) {
        List<OwnerDeviceResult> ownerDeviceResult = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();

        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" from owner_device od join company_owner co on co.id = od.owner_id where 1=1 ");
        if (ownerId != null) {
            strQuery.append(" and od.owner_id = :ownerId ");
            params.put("ownerId", ownerId);
        }

        if (!Strings.isNullOrEmpty(keyword)) {
            strQuery.append(" and (od.normalized_name like :keyword or co.name like :keywordOwner) ");
            params.put("keyword", "%" + Common.normalizedName(List.of(keyword)) + "%");
            params.put("keywordOwner", "%" + keyword + "%");
        }

        if (!Strings.isNullOrEmpty(fromDate) || !Strings.isNullOrEmpty(toDate)) {
            Common.addDateSearchCustom(fromDate, toDate, params, strQuery, "od.create_time", "owner_device");
        }

        Query countQuery = entityManager.createNativeQuery("select count(*) " + strQuery);
        Common.setParams(countQuery, params);
        Number count = (Number) countQuery.getSingleResult();
        if (count.intValue() > 0) {
            Query query = entityManager.createNativeQuery(
                "select od.id id, od.name name, od.owner_id ownerId, co.name ownerName, " +
                "od.device_code deviceCode, od.create_time createTime, od.update_time updateTime " +
                strQuery +
                Common.addSort(pageable.getSort(), params),
                "OwnerDeviceResult"
            );
            Common.setParamsWithPageable(query, params, pageable);
            ownerDeviceResult = query.getResultList();
        }
        return new PageImpl<>(ownerDeviceResult, pageable, count.longValue());
    }
}
