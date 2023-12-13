package vn.softdreams.easypos.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import vn.softdreams.easypos.dto.productUnit.ProductProductUnitResponse;
import vn.softdreams.easypos.repository.ProductProductUnitCustom;
import vn.softdreams.easypos.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductProductUnitRepositoryImpl implements ProductProductUnitCustom {

    private final Logger log = LoggerFactory.getLogger(ProductGroupRepositoryImpl.class);

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<ProductProductUnitResponse> getAllForOffline(Integer comId) {
        List<ProductProductUnitResponse> units = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" from product_product_unit ppu where ppu.com_id = :comId and direct_sale = :directSale ");
        params.put("comId", comId);
        params.put("directSale", Boolean.TRUE);
        Query query = entityManager.createNativeQuery(
            "select " +
            "ppu.id id, " +
            "ppu.com_id comId, " +
            "ppu.product_id productId, " +
            "ppu.product_unit_id productUnitId, " +
            "ppu.unit_name unitName, " +
            "ppu.is_primary isPrimary, " +
            "ppu.convert_rate convertRate, " +
            "ppu.formula formula, " +
            "ppu.sale_price salePrice, " +
            "ppu.purchase_price purchasePrice, " +
            "ppu.description description, " +
            "ppu.direct_sale directSale, " +
            "ppu.on_hand onHand, " +
            "ppu.bar_code barcode " +
            strQuery,
            "ProductProductUnitResponseDTO"
        );
        Common.setParams(query, params);
        units = query.getResultList();

        return units;
    }

    @Override
    public List<ProductProductUnitResponse> getByProductId(Integer comId, Integer productId) {
        List<ProductProductUnitResponse> units = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" from product_product_unit ppu where ppu.com_id = :comId and product_id = :productId ");
        params.put("comId", comId);
        params.put("productId", productId);
        Query query = entityManager.createNativeQuery(
            "select " +
            "ppu.id id, " +
            "ppu.com_id comId, " +
            "ppu.product_id productId, " +
            "ppu.product_unit_id productUnitId, " +
            "ppu.unit_name unitName, " +
            "ppu.is_primary isPrimary, " +
            "ppu.convert_rate convertRate, " +
            "ppu.formula formula, " +
            "ppu.sale_price salePrice, " +
            "IIF(ppu.purchase_price IS NULL, 0, ppu.purchase_price) purchasePrice, " +
            "ppu.description description, " +
            "ppu.direct_sale directSale, " +
            "ppu.on_hand onHand, " +
            "ppu.bar_code barcode " +
            strQuery,
            "ProductProductUnitResponseDTO"
        );
        Common.setParams(query, params);
        units = query.getResultList();

        return units;
    }
}
