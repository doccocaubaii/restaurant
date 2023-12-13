package vn.softdreams.easypos.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.dto.toppingGroup.ToppingGroupResponse;
import vn.softdreams.easypos.dto.toppingGroup.ToppingItem;
import vn.softdreams.easypos.dto.toppingGroup.ToppingItemOffline;
import vn.softdreams.easypos.repository.ToppingGroupCustomRepository;
import vn.softdreams.easypos.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToppingGroupRepositoryImpl implements ToppingGroupCustomRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Page<ToppingGroupResponse> getALlWithPaging(Pageable pageable, Integer comId, String keyword, Boolean isCountAll) {
        List<ToppingGroupResponse> toppingGroupResponses = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" from topping_group tp ");
        strQuery.append(" where tp.com_id = :comId ");
        params.put("comId", comId);
        if (!Strings.isNullOrEmpty(keyword)) {
            String keywordReplace = Common.normalizedName(List.of(keyword));
            strQuery.append(" and tp.normalized_name like :keyword ");
            params.put("keyword", "%" + keywordReplace + "%");
        }
        Number count = 0;
        if (Boolean.TRUE.equals(isCountAll)) {
            Query countQuery = entityManager.createNativeQuery("SELECT COUNT(*) " + strQuery);
            Common.setParams(countQuery, params);
            count = (Number) countQuery.getSingleResult();
        }
        if (pageable.getSort().isEmpty()) {
            strQuery.append(" order by tp.name asc ");
        } else {
            strQuery.append(Common.addSort(pageable.getSort(), params));
        }
        Query query = entityManager.createNativeQuery(
            "select tp.id id, " + "tp.name name, " + "tp.required_optional requiredOptional " + strQuery,
            "ProductToppingDTO"
        );
        Common.setParamsWithPageable(query, params, pageable, 0);
        toppingGroupResponses = query.getResultList();
        return new PageImpl<>(
            toppingGroupResponses,
            pageable,
            Boolean.TRUE.equals(isCountAll) ? count.longValue() : toppingGroupResponses.size()
        );
    }

    @Override
    public List<ToppingItem> getForProductDetail(Integer productId) {
        List<ToppingItem> toppingItems = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" select p.id , p.name, 1 isTopping, p.image imageUrl, p.out_price salePrice from product_topping pt ");
        strQuery.append(" join product p on p.id = pt.topping_id ");
        strQuery.append(" where product_id = :productId and topping_group_id is null ");
        strQuery.append(" union ");
        strQuery.append(" select distinct tg.id, tg.name, 0 isTopping, null imageUrl, null salePrice from product_topping pt ");
        strQuery.append(" join topping_group tg on pt.topping_group_id = tg.id ");
        strQuery.append(" where product_id = :productId and topping_group_id is not null ");
        params.put("productId", productId);
        Query query = entityManager.createNativeQuery(strQuery.toString(), "ToppingItemResponse");
        Common.setParams(query, params);
        toppingItems = query.getResultList();
        toppingItems.sort((o1, o2) -> Boolean.compare(o2.getIsTopping(), o1.getIsTopping()));
        return toppingItems;
    }

    @Override
    public List<ToppingItemOffline> getForProductOffline(List<Integer> productIds) {
        List<ToppingItemOffline> toppingItems = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(
            " select p.id , p.name, 1 isTopping, p.image imageUrl, p.out_price salePrice, product_id productId from product_topping pt "
        );
        strQuery.append(" join product p on p.id = pt.topping_id ");
        strQuery.append(" where product_id in :productId and topping_group_id is null ");
        strQuery.append(" union ");
        strQuery.append(
            " select distinct tg.id, tg.name, 0 isTopping, null imageUrl, null salePrice, product_id productId from product_topping pt "
        );
        strQuery.append(" join topping_group tg on pt.topping_group_id = tg.id ");
        strQuery.append(" where product_id in :productId and topping_group_id is not null ");
        params.put("productId", productIds);
        Query query = entityManager.createNativeQuery(strQuery.toString(), "ToppingItemOfflineResponse");
        Common.setParams(query, params);
        toppingItems = query.getResultList();
        toppingItems.sort((o1, o2) -> Boolean.compare(o2.getIsTopping(), o1.getIsTopping()));
        return toppingItems;
    }
}
