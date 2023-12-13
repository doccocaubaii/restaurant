package vn.softdreams.easypos.repository.impl;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.dto.productGroup.ProductGroupOfflineResponse;
import vn.softdreams.easypos.dto.productGroup.ProductGroupOnlineResponse;
import vn.softdreams.easypos.dto.productGroup.ProductGroupResult;
import vn.softdreams.easypos.repository.ProductGroupRepositoryCustom;
import vn.softdreams.easypos.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;

public class ProductGroupRepositoryImpl implements ProductGroupRepositoryCustom {

    private final Logger log = LoggerFactory.getLogger(ProductGroupRepositoryImpl.class);

    @Autowired
    private EntityManager entityManager;

    @Override
    public Page<ProductGroupOnlineResponse> searchProductGroupByKeywordForPage(
        Pageable pageable,
        String keyword,
        Integer comId,
        Boolean isLoadAll,
        boolean paramCheckAll,
        List<Integer> ids
    ) {
        List<ProductGroupOnlineResponse> productGroupList = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        StringBuilder strCount = new StringBuilder();
        strQuery.append(
            "  from product_group pg " +
            "LEFT JOIN product_product_group ppg on pg.id = ppg.product_group_id " +
            "LEFT JOIN product p on p.id = ppg.product_id and p.active = 1 " +
            "WHERE pg.com_id = :companyId "
        );
        strCount.append(" from product_group pg" + " WHERE pg.com_id = :companyId ");
        params.put("companyId", comId);
        if (!Strings.isNullOrEmpty(keyword)) {
            strQuery.append(" and pg.normalized_name LIKE :keyword ");
            strCount.append(" and pg.normalized_name LIKE :keyword ");
            params.put("keyword", "%" + Common.normalizedName(Arrays.asList(keyword)) + "%");
        }
        if (paramCheckAll) {
            if (ids != null && ids.size() > 0) {
                strQuery.append(" AND pg.id not in :ids ");
                params.put("ids", ids);
            }
        } else {
            if (ids != null && ids.size() > 0) {
                strQuery.append(" AND pg.id in :ids ");
                params.put("ids", ids);
            }
        }
        Number count = 0;
        if (Boolean.TRUE.equals(isLoadAll)) {
            Query countQuery = entityManager.createNativeQuery("SELECT COUNT(*) " + strCount);
            Common.setParams(countQuery, params);
            count = (Number) countQuery.getSingleResult();
        }
        strQuery.append(" GROUP BY pg.id, pg.com_id, pg.name, pg.description, pg.create_time , pg.update_time ");
        strQuery.append(" order by pg.name ");
        Query query = entityManager.createNativeQuery(
            "SELECT pg.id id, " +
            " pg.com_id comId, " +
            "pg.name name, " +
            "pg.description description, " +
            "pg.create_time createTime," +
            "pg.update_time updateTime, " +
            "count(p.id) productCount " +
            strQuery,
            "ProductGroupOnlineResponse"
        );
        if (pageable == null) {
            Common.setParams(query, params);
            productGroupList = query.getResultList();
            return new PageImpl<>(productGroupList);
        } else {
            Common.setParamsWithPageable(query, params, pageable);
            productGroupList = query.getResultList();
            return new PageImpl<>(productGroupList, pageable, Boolean.TRUE.equals(isLoadAll) ? count.longValue() : productGroupList.size());
        }
    }

    public List<ProductGroupOfflineResponse> getAllProductGroupsForOffline(Integer comId) {
        List<ProductGroupOfflineResponse> productGroupList = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" from product_group pg where pg.com_id  = :companyId ");
        params.put("companyId", comId);
        Query query = entityManager.createNativeQuery(
            "select pg.id id, " +
            " pg.com_id comId, " +
            "pg.name name, " +
            "pg.description description, " +
            "pg.create_time createTime, " +
            "pg.update_time updateTime " +
            strQuery,
            "ProductGroupResponse"
        );
        Common.setParams(query, params);
        productGroupList = query.getResultList();
        //        }
        return productGroupList;
    }

    @Override
    public List<ProductGroupResult> getAllForOffline(Integer comId, List<Integer> productIds) {
        List<ProductGroupResult> productGroupResults = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        //TODO: Lấy offline thì sao cần điều kiện ppg.product_id in :productIds
        //TODO: order là tào tao
        strQuery.append(
            " from product_group pg " +
            " join product_product_group ppg on pg.id = ppg.product_group_id " +
            "where pg.com_id = :comId and ppg.product_id in :productIds "
        );
        params.put("comId", comId);
        params.put("productIds", productIds);
        Query query = entityManager.createNativeQuery(
            "select pg.id id, ppg.product_id productId, pg.name name, pg.description description " + strQuery,
            "ProductGroupResult"
        );
        Common.setParams(query, params);
        productGroupResults = query.getResultList();

        return productGroupResults;
    }
}
