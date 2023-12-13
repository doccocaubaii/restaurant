package vn.softdreams.easypos.repository.impl;

import vn.softdreams.easypos.dto.customerCard.CustomerCardInformation;
import vn.softdreams.easypos.repository.CustomerCardRepositoryCustom;
import vn.softdreams.easypos.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerCardRepositoryImpl implements CustomerCardRepositoryCustom {

    private final EntityManager entityManager;

    public CustomerCardRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<CustomerCardInformation> getAllCardByCustomerIds(Integer comId, List<Integer> customerIds) {
        List<CustomerCardInformation> result;
        Map<String, Object> params = new HashMap<>();

        StringBuilder strQuery = new StringBuilder();
        strQuery.append(
            " from customer_card cc left join loyalty_card lc on lc.id = cc.card_id and lc.status = 1 " +
            " where cc.com_id = :comId and cc.customer_id in :customerIds "
        );
        params.put("comId", comId);
        params.put("customerIds", customerIds);
        Query query = entityManager.createNativeQuery(
            "select cc.card_id cardId, cc.customer_id customerId, cc.code cardCode," +
            "lc.name cardName, cc.point point, cc.amount amount, lc.rank rank " +
            strQuery,
            "CustomerCardDetail"
        );
        Common.setParams(query, params);
        result = query.getResultList();
        return result;
    }
}
