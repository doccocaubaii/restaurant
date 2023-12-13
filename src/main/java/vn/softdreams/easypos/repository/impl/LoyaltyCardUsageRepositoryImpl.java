package vn.softdreams.easypos.repository.impl;

import com.google.common.base.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.dto.card.CardHistoryResult;
import vn.softdreams.easypos.repository.LoyaltyCardUsageRepositoryCustom;
import vn.softdreams.easypos.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoyaltyCardUsageRepositoryImpl implements LoyaltyCardUsageRepositoryCustom {

    private final EntityManager entityManager;

    public LoyaltyCardUsageRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Page<CardHistoryResult> getAllHistory(
        Integer comId,
        Integer customerId,
        List<Integer> type,
        Pageable pageable,
        String fromDate,
        String toDate
    ) {
        List<CardHistoryResult> response = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" from loyalty_card_usage lcu join loyalty_card lc on lc.id = lcu.card_id left join bill b on b.id = lcu.ref_id ");
        strQuery.append(
            " where lcu.com_id = :comId and lcu.customer_id = :customerId and lcu.type in :type and (lcu.amount <> 0 or lcu.point <> 0) "
        );
        params.put("comId", comId);
        params.put("customerId", customerId);
        params.put("type", type);
        if (!Strings.isNullOrEmpty(fromDate) || !Strings.isNullOrEmpty(toDate)) {
            Common.addDateSearchCustom(fromDate, toDate, params, strQuery, "lcu.usage_date", "loyalty_card_usage");
        }
        Query countQuery = entityManager.createNativeQuery("select count(*) " + strQuery);
        Common.setParams(countQuery, params);
        Number count = (Number) countQuery.getSingleResult();
        if (count.intValue() > 0) {
            strQuery.append(" order by lcu.usage_date desc ");
            Query query = entityManager.createNativeQuery(
                "select lcu.id id, lc.name cardName, lcu.usage_date usageDate, " +
                "lcu.type type, b.code billCode, lcu.amount amount, lcu.point point, lcu.description description " +
                strQuery,
                "CardHistoryResult"
            );
            Common.setParamsWithPageable(query, params, pageable);
            response = query.getResultList();
        }
        return new PageImpl<>(response, pageable, count.longValue());
    }
}
