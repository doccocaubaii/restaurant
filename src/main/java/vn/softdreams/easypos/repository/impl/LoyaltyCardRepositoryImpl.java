package vn.softdreams.easypos.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.constants.LoyaltyCardConstants;
import vn.softdreams.easypos.dto.loyaltyCard.LoyaltyCardResultItem;
import vn.softdreams.easypos.repository.LoyaltyCardCustom;
import vn.softdreams.easypos.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;

public class LoyaltyCardRepositoryImpl implements LoyaltyCardCustom {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Page<LoyaltyCardResultItem> getAllCustomerCard(
        Pageable pageable,
        Integer comId,
        String keyword,
        Integer id,
        Boolean isCountAll,
        Boolean paramCheckAll,
        List<Integer> ids
    ) {
        List<LoyaltyCardResultItem> customers = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(
            " from loyalty_card lc left join customer_card cc on lc.id = cc.card_id left join customer c on cc.customer_id = c.id and c.active = 1 "
        );
        strQuery.append(" where lc.com_id = :comId and lc.status in :status ");
        params.put("comId", comId);
        params.put("status", List.of(LoyaltyCardConstants.Status.NGUNG_HOAT_DONG, LoyaltyCardConstants.Status.HOAT_DONG));
        if (id != null) {
            strQuery.append(" AND lc.id = :id ");
            params.put("id", id);
        }
        if (!Strings.isNullOrEmpty(keyword)) {
            strQuery.append(" AND lc.normalized_name LIKE :keyword ");
            params.put("keyword", "%" + Common.normalizedName(Arrays.asList(keyword)) + "%");
        }
        if (paramCheckAll) {
            if (ids != null && ids.size() > 0) {
                strQuery.append(" AND lc.id not in :ids ");
                params.put("ids", ids);
            }
        } else {
            if (ids != null && ids.size() > 0) {
                strQuery.append(" AND lc.id in :ids ");
                params.put("ids", ids);
            }
        }

        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(*) " + strQuery);
        Common.setParams(countQuery, params);
        Number count = (Number) countQuery.getSingleResult();
        if (count.intValue() > 0) {
            strQuery.append(" order by lc.rank ");
            Query query = entityManager.createNativeQuery(
                "SELECT lc.id id," +
                " lc.name name," +
                " lc.is_default isDefault, " +
                " lc.rank rank, " +
                " lc.status status, " +
                "c.id customerId, " +
                "c.name customerName, " +
                "c.code code, " +
                "c.code2 code2, " +
                "c.address address, " +
                "c.phone_number phoneNumber, " +
                "c.email email, " +
                "c.tax_code taxCode, " +
                "c.id_number idNumber," +
                "c.type type, " +
                "c.description description, " +
                "c.city city, " +
                "c.district district, " +
                "c.create_time createTime, " +
                "c.update_time updateTime " +
                strQuery,
                "LoyaltyCardItem"
            );
            if (pageable != null) {
                Common.setParamsWithPageable(query, params, pageable);
                customers = query.getResultList();
                return new PageImpl<>(customers, pageable, Boolean.TRUE.equals(isCountAll) ? count.longValue() : customers.size());
            } else {
                Common.setParams(query, params);
                customers = query.getResultList();
                return new PageImpl<>(customers);
            }
        }
        return new PageImpl<>(new ArrayList<>());
    }
}
