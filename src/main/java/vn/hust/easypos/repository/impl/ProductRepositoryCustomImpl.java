package vn.hust.easypos.repository.impl;

import com.google.common.base.Strings;
import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.hust.easypos.repository.ProductRepositoryCustom;
import vn.hust.easypos.service.dto.product.ProductDetailResponse;
import vn.hust.easypos.service.dto.product.ProductResponse;
import vn.hust.easypos.service.util.Common;

public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Page<ProductDetailResponse> getWithPagingForProduct(Pageable pageable, Integer companyId, String keyword) {
        List<ProductDetailResponse> productResponses = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" from product p ");
        strQuery.append(" where p.com_id = :comId and p.active = 1 ");
        params.put("comId", companyId);
        if (!Strings.isNullOrEmpty(keyword)) {
            String keywordReplace = Common.normalizedName(Arrays.asList(keyword));
            strQuery.append(" and (p.normalized_name like :keyword ) ");
            params.put("keyword", "%" + keywordReplace + "%");
        }
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(*) " + strQuery);
        Common.setParams(countQuery, params);
        Number count = (Number) countQuery.getSingleResult();
        if (count.intValue() > 0) {
            strQuery.append(" order by p.name desc ");
            Query query = entityManager.createNativeQuery(
                "select p.id id, " +
                "p.com_id comId, " +
                "p.code code, " +
                "p.name name, " +
                "p.unit unit, " +
                "p.in_price inPrice, " +
                "p.out_price outPrice, " +
                "p.description description, " +
                "p.created_date createTime, " +
                "p.last_modified_date updateTime,  " +
                "p.image imageUrl " +
                strQuery,
                "ProductItemResult"
            );
            Common.setParamsWithPageable(query, params, pageable, 0);
            productResponses = query.getResultList();
        }
        return new PageImpl<>(productResponses, pageable, count.intValue());
    }

    @Override
    public ProductDetailResponse findByProductId(Integer companyId, Integer productId) {
        List<ProductDetailResponse> products = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("FROM product p WHERE p.com_id = :companyId AND p.id = :productId AND p.active = 1 ");
        params.put("companyId", companyId);
        params.put("productId", productId);

        Query query = entityManager.createNativeQuery(
            "select p.id id, " +
            "p.com_id comId, " +
            "p.code code, " +
            "p.name name, " +
            "p.unit unit, " +
            "p.in_price inPrice, " +
            "p.out_price outPrice, " +
            "p.description description, " +
            "p.created_date createTime, " +
            "p.last_modified_date updateTime,  " +
            "p.image imageUrl " +
            strQuery,
            "ProductItemResult"
        );
        Common.setParams(query, params);
        products = query.getResultList();
        return products.isEmpty() ? null : products.get(0);
    }

    @Override
    public List<ProductResponse> searchAllByComIdAndIdAndStatusTrue(Integer comId, Set<Integer> ids) {
        List<ProductResponse> productList = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("  from PRODUCT ");
        strQuery.append("  where com_id = :comId and active = 1 and id in :ids ");
        params.put("comId", comId);
        params.put("ids", ids);
        Query query = entityManager.createNativeQuery("SELECT id, code, name " + strQuery, "ProductDTO");
        Common.setParams(query, params);
        productList = query.getResultList();
        return productList;
    }
}
