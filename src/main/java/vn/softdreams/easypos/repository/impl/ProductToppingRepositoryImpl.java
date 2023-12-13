package vn.softdreams.easypos.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import vn.softdreams.easypos.dto.toppingGroup.ProductToppingItemResponse;
import vn.softdreams.easypos.dto.toppingGroup.ToppingItem;
import vn.softdreams.easypos.repository.ProductToppingRepositoryCustom;
import vn.softdreams.easypos.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductToppingRepositoryImpl implements ProductToppingRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<ToppingItem> getListToppingGroupForBill(Map<Integer, List<Integer>> parentIdMap) {
        List<ToppingItem> productList = new ArrayList<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("  from product_topping ");
        strQuery.append("  where topping_group_id is not null and (");
        for (Map.Entry<Integer, List<Integer>> entry : parentIdMap.entrySet()) {
            strQuery.append(
                "or product_id = " +
                entry.getKey() +
                " and topping_id in (" +
                String.join(",", entry.getValue().stream().map(String::valueOf).toArray(String[]::new)) +
                ") "
            );
        }
        //        for (int i = 0; i < productIds.size(); i++) {
        //            strQuery.append("or product_id = " + parentIds.get(i) + " and topping_id = " + productIds.get(i) + " ");
        //        }
        strQuery.append(")");
        Query query = entityManager.createNativeQuery(
            "SELECT topping_group_id id " + strQuery.toString().replace("(or", "("),
            "ToppingGroupId"
        );
        productList = query.getResultList();
        //        }
        return productList;
    }

    @Override
    public List<ProductToppingItemResponse> findToppingGroupForBill(Integer productId) {
        List<ProductToppingItemResponse> productResponses = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(
            " from product_topping pt " +
            " join product p on pt.topping_id = p.id " +
            " join product_product_unit ppu on pt.topping_id = ppu.product_id and is_primary = 1 " +
            " where pt.product_id = :productId "
        );
        params.put("productId", productId);
        Query query = entityManager.createNativeQuery(
            "select " +
            "pt.topping_group_id toppingGroupId," +
            "ppu.id productProductUnitId, " +
            "p.id productId, " +
            "p.com_id comId, " +
            "p.code code, " +
            "p.code2 code2, " +
            "p.name name, " +
            "ppu.unit_name unit, " +
            "ppu.product_unit_id unitId, " +
            "IIF(ppu.purchase_price IS NULL, 0, ppu.purchase_price) inPrice, " +
            "IIF(ppu.sale_price IS NULL, 0, ppu.sale_price) outPrice, " +
            "p.vat_rate vatRate, " +
            "p.discount_vat_rate discountVatRate, " +
            "p.inventory_tracking inventoryTracking, " +
            "IIF(ppu.on_hand IS NULL, 0, ppu.on_hand) inventoryCount, " +
            "p.description description, " +
            "p.create_time createTime, " +
            "p.update_time updateTime,  " +
            "p.bar_code barCode,  " +
            "p.image imageUrl,  " +
            "p.is_topping isTopping  " +
            strQuery,
            "ProductToppingItemResponse"
        );
        Common.setParams(query, params);
        productResponses = query.getResultList();
        return productResponses;
    }

    @Override
    public List<ProductToppingItemResponse> findToppingGroupForOffline(Integer comId) {
        List<ProductToppingItemResponse> productResponses = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(
            " from product_topping pt " +
            " join product p on pt.topping_id = p.id " +
            " join product_product_unit ppu on pt.topping_id = ppu.product_id and is_primary = 1 " +
            " where p.active = 1 and p.com_id = :comId "
        );
        params.put("comId", comId);
        Query query = entityManager.createNativeQuery(
            "select " +
            "pt.topping_group_id toppingGroupId," +
            "ppu.id productProductUnitId, " +
            "p.id productId, " +
            "p.com_id comId, " +
            "p.code code, " +
            "p.code2 code2, " +
            "p.name name, " +
            "ppu.unit_name unit, " +
            "ppu.product_unit_id unitId, " +
            "IIF(ppu.purchase_price IS NULL, 0, ppu.purchase_price) inPrice, " +
            "IIF(ppu.sale_price IS NULL, 0, ppu.sale_price) outPrice, " +
            "p.vat_rate vatRate, " +
            "p.discount_vat_rate discountVatRate, " +
            "p.inventory_tracking inventoryTracking, " +
            "IIF(ppu.on_hand IS NULL, 0, ppu.on_hand) inventoryCount, " +
            "p.description description, " +
            "p.create_time createTime, " +
            "p.update_time updateTime,  " +
            "p.bar_code barCode,  " +
            "p.image imageUrl,  " +
            "p.is_topping isTopping,  " +
            "pt.product_id parentProductId  " +
            strQuery,
            "ProductToppingItemOfflineResponse"
        );
        Common.setParams(query, params);
        productResponses = query.getResultList();
        return productResponses;
    }
}
