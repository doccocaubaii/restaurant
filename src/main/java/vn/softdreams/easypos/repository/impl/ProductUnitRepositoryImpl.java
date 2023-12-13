package vn.softdreams.easypos.repository.impl;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.dto.product.ProductUnitResponse;
import vn.softdreams.easypos.repository.ProductUnitRepositoryCustom;
import vn.softdreams.easypos.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductUnitRepositoryImpl implements ProductUnitRepositoryCustom {

    private final Logger log = LoggerFactory.getLogger(ProductUnitRepositoryImpl.class);

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<ProductUnitResponse> findByComId(Integer comId, String keyword) {
        List<ProductUnitResponse> productList = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("  from PRODUCT_UNIT where com_id = :comId and active = 'true' ");
        params.put("comId", comId);
        if (!Strings.isNullOrEmpty(keyword)) {
            strQuery.append(" AND (name like :keyword) ");
            params.put("keyword", "%" + keyword + "%");
        }
        //        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(*) " + strQuery);
        //        Common.setParams(countQuery, params);
        //        Number count = (Number) countQuery.getSingleResult();
        //        if (count.intValue() > 0) {
        Query query = entityManager.createNativeQuery(
            "SELECT id, com_id comId, name, description " + strQuery + " ORDER BY name ASC ",
            "ProductUnitResponse"
        );
        Common.setParams(query, params);
        productList = query.getResultList();
        //        }
        return productList;
    }

    @Override
    public Page<ProductUnitResponse> searchProductUnit(
        Pageable pageable,
        Integer comId,
        String keyword,
        boolean paramCheckAll,
        List<Integer> ids
    ) {
        List<ProductUnitResponse> productList = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("  from PRODUCT_UNIT where com_id = :comId and active = 1 ");
        params.put("comId", comId);
        if (!Strings.isNullOrEmpty(keyword)) {
            strQuery.append(" AND (name like :keyword) ");
            params.put("keyword", "%" + keyword + "%");
        }
        if (paramCheckAll) {
            if (ids != null && ids.size() > 0) {
                strQuery.append(" AND id not in :ids ");
                params.put("ids", ids);
            }
        } else {
            if (ids != null && ids.size() > 0) {
                strQuery.append(" AND id in :ids ");
                params.put("ids", ids);
            }
        }
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(*) " + strQuery);
        Common.setParams(countQuery, params);
        Number count = (Number) countQuery.getSingleResult();
        if (count.intValue() > 0) {
            Query query = entityManager.createNativeQuery(
                "SELECT id, com_id comId, name, description, active, create_time createTime " + strQuery + " ORDER BY name ASC ",
                "ProductUnitItemResponse"
            );
            if (pageable == null) {
                Common.setParams(query, params);
                productList = query.getResultList();
                return new PageImpl<>(productList);
            } else {
                Common.setParamsWithPageable(query, params, pageable);
                productList = query.getResultList();
                return new PageImpl<>(productList, pageable, count.longValue());
            }
        }
        return new PageImpl<>(new ArrayList<>(), pageable, count.longValue());
    }
}
