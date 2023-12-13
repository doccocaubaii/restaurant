package vn.softdreams.easypos.repository.impl;

import com.google.common.base.Strings;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.config.Constants;
import vn.softdreams.easypos.dto.inventory.InventoryCommonStats;
import vn.softdreams.easypos.dto.product.*;
import vn.softdreams.easypos.dto.productGroup.ProductOfflineResponse;
import vn.softdreams.easypos.repository.ProductRepositoryCustom;
import vn.softdreams.easypos.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;

public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final Logger log = LoggerFactory.getLogger(ProductRepositoryImpl.class);

    @Autowired
    private EntityManager entityManager;

    @Override
    public boolean countByIdAndComId(Integer id, Integer companyId, String code2) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("  from PRODUCT  WHERE com_id = :companyId AND active = 1 ");
        params.put("companyId", companyId);
        if (!Strings.isNullOrEmpty(code2)) {
            strQuery.append(" AND code2 = :code2 ");
            params.put("code2", code2);
        }
        if (id != null) {
            strQuery.append(" AND id != :id ");
            params.put("id", id);
        }
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(*) " + strQuery);
        Common.setParams(countQuery, params);
        Number count = (Number) countQuery.getSingleResult();
        return count.longValue() == 0;
    }

    public boolean checkDuplicateBarcode(Integer id, Integer companyId, Set<String> barcode) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(
            "  from product_product_unit ppu join product p on p.id = ppu.product_id " + " WHERE p.com_id = :companyId AND active = 1 "
        );
        params.put("companyId", companyId);
        if (id != null) {
            strQuery.append(" AND p.id != :id ");
            params.put("id", id);
        }
        if (!barcode.isEmpty()) {
            strQuery.append(" AND ppu.bar_code in :barcode ");
            params.put("barcode", barcode);
        }
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(*) " + strQuery);
        Common.setParams(countQuery, params);
        Number count = (Number) countQuery.getSingleResult();
        return count.longValue() == 0;
    }

    @Override
    public List<ProductOfflineResponse> findAllByProductGroupIds(List<Integer> ids, Integer companyId) {
        List<ProductOfflineResponse> productList = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(
            "  from product p " +
            "  left join product_product_group ppg on p.id = ppg.product_id " +
            "  left join product_product_unit ppu on p.id = ppu.product_id and ppu.direct_sale = 1 " +
            "where p.com_id = :companyId and  ppg.product_group_id in :ids "
        );
        params.put("companyId", companyId);
        params.put("ids", ids);

        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(*) " + strQuery);
        Common.setParams(countQuery, params);
        Number count = (Number) countQuery.getSingleResult();
        strQuery.append(" ORDER BY p.id DESC");
        if (count.intValue() > 0) {
            Query query = entityManager.createNativeQuery(
                "SELECT p.id              id, " +
                "       p.com_id          comId, " +
                "       p.code            code, " +
                "       p.code2           code2, " +
                "       p.name            name, " +
                "       ppu.unit_name unit, " +
                "       p.in_price        inPrice, " +
                "       p.out_price       outPrice, " +
                "       p.bar_code        barCode, " +
                "       p.bar_code_2      barCode2, " +
                "       p.vat_rate        vatRate, " +
                "       p.inventory_id    inventoryId, " +
                "       p.inventory_count inventoryCount, " +
                "       p.description     description, " +
                "       p.active          active, " +
                "       p.image           image, " +
                "       p.inventory_tracking    isInventory, " +
                "       p.create_time     createTime, " +
                "       p.update_time     updateTime, " +
                "       ppg.product_group_id productGroupId " +
                strQuery,
                "ProductGroupSearchResponse"
            );
            Common.setParams(query, params);
            productList = query.getResultList();
        }
        return productList;
    }

    @Override
    public List<ProductCheckBill> productCheckBill(Integer comId, Set<Integer> ids) {
        List<ProductCheckBill> productList;
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(
            "  from product p join config c on c.company_id = p.com_id join product_product_unit ppu on p.id = ppu.product_id "
        );
        strQuery.append("  where p.com_id = :comId and p.active = 1 and p.id in :ids and c.code = :code order by ppu.is_primary desc ");
        params.put("comId", comId);
        params.put("ids", ids);
        params.put("code", Constants.OVER_STOCK_CODE);
        Query query = entityManager.createNativeQuery(
            "select p.id, ppu.id productProductUnitId, p.code, p.name, ppu.product_unit_id unitId, iif(ppu.unit_name is null, '', ' ' + ppu.unit_name) unitName, ppu.is_primary isPrimary, ppu.convert_rate convertRate, ppu.formula, ppu.on_hand onHand, p.inventory_tracking inventoryTracking, c.value overStock " +
            strQuery,
            "ProductCheckBill"
        );
        Common.setParams(query, params);
        productList = query.getResultList();
        return productList;
    }

    @Override
    public List<ProductItemResponse> getAllForOffline(Integer companyId) {
        List<ProductItemResponse> productResponses = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(
            " from product p " +
            " left join product_product_unit ppu on p.id = ppu.product_id and ppu.direct_sale = 1 " +
            " where p.com_id = :comId and p.active = 1 order by p.code2 asc, p.name asc "
        );
        params.put("comId", companyId);
        Query query = entityManager.createNativeQuery(
            "select ppu.id productProductUnitId, " +
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
            "p.inventory_tracking inventoryTracking, " +
            "IIF(ppu.on_hand IS NULL, 0, ppu.on_hand) inventoryCount, " +
            "p.description description, " +
            "p.create_time createTime, " +
            "p.update_time updateTime,  " +
            "ppu.bar_code barCode,  " +
            "p.image imageUrl,  " +
            "p.is_topping isTopping,  " +
            "ppu.is_primary isPrimary,  " +
            "IIF(ppu.other_prices IS NULL, 0, 1) haveOtherPrice, " +
            "p.discount_vat_rate discountVatRate  " +
            strQuery,
            "ProductBillResponse"
        );
        Common.setParams(query, params);
        productResponses = query.getResultList();
        return productResponses;
    }

    @Override
    public List<ProductDetailResponse> getAllForOfflineForProduct(Integer companyId) {
        List<ProductDetailResponse> productResponses = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(
            " from product p " +
            " left join product_product_unit ppu on p.id = ppu.product_id and ppu.direct_sale = 1 and ppu.is_primary = 1 " +
            " where p.com_id = :comId and p.code != 'SPDV' and p.active = 1 order by p.name asc "
        );
        params.put("comId", companyId);
        Query query = entityManager.createNativeQuery(
            "select p.id id, " +
            "p.com_id comId, " +
            "p.code code, " +
            "p.code2 code2, " +
            "p.name name, " +
            "ppu.unit_name unit, " +
            "ppu.product_unit_id unitId, " +
            "IIF(ppu.purchase_price IS NULL, 0, ppu.purchase_price) inPrice, " +
            "IIF(ppu.sale_price IS NULL, 0, ppu.sale_price) outPrice, " +
            "p.vat_rate vatRate, " +
            "p.inventory_tracking inventoryTracking, " +
            "IIF(ppu.on_hand IS NULL, 0, ppu.on_hand) inventoryCount, " +
            "p.description description, " +
            "p.create_time createTime, " +
            "p.update_time updateTime,  " +
            "ppu.bar_code barCode,  " +
            "p.image imageUrl,  " +
            "p.is_topping isTopping  " +
            strQuery,
            "ProductItemResult"
        );
        Common.setParams(query, params);
        productResponses = query.getResultList();
        ProductDetailResponse item = new ProductDetailResponse();

        ModelMapper modelMapper = new ModelMapper();
        int index = productResponses
            .stream()
            .filter(product -> product.getCode().equals(Constants.PRODUCT_CODE_DEFAULT))
            .findFirst()
            .map(productResponses::indexOf)
            .orElse(-1);
        if (index != -1) {
            modelMapper.map(productResponses.get(index), item);
            productResponses.remove(index);
            productResponses.add(0, item);
        }
        return productResponses;
    }

    @Override
    public Page<ProductItemResponse> getWithPaging(
        Pageable pageable,
        Integer companyId,
        Integer groupId,
        String keyword,
        Boolean isTopping,
        Boolean isCountAll
    ) {
        List<ProductItemResponse> productResponses = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" from product p " + " left join product_product_unit ppu on p.id = ppu.product_id and ppu.direct_sale = 1 ");
        if (groupId != null && groupId != 0) {
            strQuery.append(" join product_product_group ppg on p.id = ppg.product_id ");
        }
        strQuery.append(" where p.com_id = :comId and p.active = 1 ");
        params.put("comId", companyId);

        if (groupId != null && groupId != 0) {
            strQuery.append(" and ppg.product_group_id = :groupId ");
            params.put("groupId", groupId);
        }
        String keywordReplace = Common.normalizedName(Arrays.asList(keyword));
        if (!Strings.isNullOrEmpty(keyword)) {
            strQuery.append(
                " and (p.bar_code = :searchBarcode or ppu.bar_code = :searchBarcode or p.normalized_name like :keyword or p.code2 like :keyword ) "
            );
            params.put("searchBarcode", keywordReplace);
            params.put("keyword", "%" + keywordReplace + "%");
        }
        //        if (!Strings.isNullOrEmpty(keyword)) {
        //            strQuery.append(" and (p.name like :keyword or p.code2 like :keyword or p.bar_code like :keyword )");
        //            params.put("keyword", "%" + keyword + "%");
        //        }
        if (isTopping != null) {
            strQuery.append(" and p.is_topping = :isTopping");
            params.put("isTopping", isTopping);
        }
        Number count = 0;
        if (Boolean.TRUE.equals(isCountAll)) {
            Query countQuery = entityManager.createNativeQuery("SELECT COUNT(*) " + strQuery);
            Common.setParams(countQuery, params);
            count = (Number) countQuery.getSingleResult();
        }
        strQuery.append(
            " order by case when p.code = :codeDefault then 0 when p.code = :codeNoteDefault then 1 when p.code = :codePromotionDefault then 2 else 3 end, p.name asc "
        );
        params.put("codeDefault", Constants.PRODUCT_CODE_DEFAULT);
        params.put("codeNoteDefault", Constants.PRODUCT_CODE_NOTE_DEFAULT);
        params.put("codePromotionDefault", Constants.PRODUCT_CODE_PROMOTION_DEFAULT);
        Query query = entityManager.createNativeQuery(
            "select ppu.id productProductUnitId, " +
            "p.id productId, " +
            "p.com_id comId, " +
            "p.code code, " +
            "p.code2 code2, " +
            "p.name name, " +
            "ppu.unit_name unit, " +
            "ppu.product_unit_id            unitId, " +
            "IIF(ppu.purchase_price IS NULL, 0, ppu.purchase_price) inPrice, " +
            "IIF(ppu.sale_price IS NULL, 0, ppu.sale_price) outPrice, " +
            "p.vat_rate vatRate, " +
            "p.inventory_tracking inventoryTracking, " +
            "IIF(ppu.on_hand IS NULL, 0, ppu.on_hand) inventoryCount, " +
            "p.description description, " +
            "p.create_time createTime, " +
            "p.update_time updateTime,  " +
            "ppu.bar_code barCode,  " +
            "p.image imageUrl, " +
            "p.is_topping isTopping, " +
            "ppu.is_primary isPrimary,  " +
            "IIF(ppu.other_prices IS NULL, 0, 1) haveOtherPrice, " +
            "p.discount_vat_rate discountVatRate  " +
            strQuery,
            "ProductBillResponse"
        );
        Common.setParamsWithPageable(query, params, pageable, 0);
        productResponses = query.getResultList();
        return new PageImpl<>(productResponses, pageable, Boolean.TRUE.equals(isCountAll) ? count.longValue() : productResponses.size());
    }

    @Override
    public List<ProductItemResponse> getWithPaging2(Pageable pageable, Integer companyId, List<Integer> groupIds, String keyword) {
        List<ProductItemResponse> productResponses = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" from product p " + " left join product_product_unit ppu on p.id = ppu.product_id and ppu.direct_sale = 1 ");
        if (groupIds != null && !groupIds.isEmpty()) {
            strQuery.append(" join product_product_group ppg on p.id = ppg.product_id ");
        }
        strQuery.append(" where p.com_id = :comId and p.active = 1 ");
        params.put("comId", companyId);

        if (groupIds != null && !groupIds.isEmpty()) {
            strQuery.append(" and ppg.product_group_id in :groupId ");
            params.put("groupId", groupIds);
        }
        String keywordReplace = Common.normalizedName(Arrays.asList(keyword));
        if (!Strings.isNullOrEmpty(keyword)) {
            strQuery.append(" and (p.bar_code = :searchBarcode or ppu.bar_code = :searchBarcode or p.normalized_name like :keyword ) ");
            params.put("searchBarcode", keywordReplace);
            params.put("keyword", "%" + keywordReplace + "%");
        }
        strQuery.append(
            " order by case when p.code = :codeDefault then 0 when p.code = :codeNoteDefault then 1 when p.code = :codePromotionDefault then 2 when p.code = :codeServiceChargeDefault then 3 else 4 end, p.name asc "
        );
        params.put("codeDefault", Constants.PRODUCT_CODE_DEFAULT);
        params.put("codeNoteDefault", Constants.PRODUCT_CODE_NOTE_DEFAULT);
        params.put("codePromotionDefault", Constants.PRODUCT_CODE_PROMOTION_DEFAULT);
        params.put("codeServiceChargeDefault", Constants.PRODUCT_CODE_SERVICE_CHARGE_DEFAULT);
        Query query = entityManager.createNativeQuery(
            "select ppu.id productProductUnitId, " +
            "p.id productId, " +
            "p.com_id comId, " +
            "p.code code, " +
            "p.code2 code2, " +
            "p.feature feature, " +
            "p.name name, " +
            "ppu.unit_name unit, " +
            "ppu.product_unit_id            unitId, " +
            "IIF(ppu.purchase_price IS NULL, 0, ppu.purchase_price) inPrice, " +
            "IIF(ppu.sale_price IS NULL, 0, ppu.sale_price) outPrice, " +
            "p.vat_rate vatRate, " +
            "p.inventory_tracking inventoryTracking, " +
            "IIF(ppu.on_hand IS NULL, 0, ppu.on_hand) inventoryCount, " +
            "p.description description, " +
            "p.create_time createTime, " +
            "p.update_time updateTime,  " +
            "p.bar_code barCode,  " +
            "p.image imageUrl, " +
            "p.is_topping isTopping, " +
            "ppu.is_primary isPrimary , " +
            "IIF(ppu.other_prices IS NULL, 0, 1) haveOtherPrice, " +
            "p.discount_vat_rate discountVatRate  " +
            strQuery,
            "ProductBillResponse"
        );
        Common.setParamsWithPageable(query, params, pageable, 0);
        productResponses = query.getResultList();
        return new PageImpl<>(productResponses, pageable, productResponses.size()).getContent();
    }

    @Override
    public Page<ProductDetailResponse> getWithPagingForProduct(
        Pageable pageable,
        Integer companyId,
        Integer groupId,
        String keyword,
        Boolean isTopping,
        Boolean isCountAll,
        List<Integer> productIds,
        boolean paramCheckAll,
        List<Integer> ids
    ) {
        List<ProductDetailResponse> productResponses = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" from product p " + " left join product_product_unit ppu on p.id = ppu.product_id and (ppu.is_primary = 1 ");
        String keywordReplace = Common.normalizedName(Arrays.asList(keyword));
        if (!Strings.isNullOrEmpty(keyword)) {
            strQuery.append(" or ppu.bar_code = :searchBarcode ");
            params.put("searchBarcode", keywordReplace);
        }
        strQuery.append(") ");
        if (groupId != null && groupId != 0) {
            strQuery.append(" join product_product_group ppg on p.id = ppg.product_id ");
        }
        strQuery.append(" where p.com_id = :comId and p.active = 1 and p.code not in (:codeDefault) ");
        params.put("comId", companyId);
        params.put(
            "codeDefault",
            List.of(
                Constants.PRODUCT_CODE_DEFAULT,
                Constants.PRODUCT_CODE_NOTE_DEFAULT,
                Constants.PRODUCT_CODE_PROMOTION_DEFAULT,
                Constants.PRODUCT_CODE_SERVICE_CHARGE_DEFAULT
            )
        );

        if (groupId != null && groupId != 0) {
            strQuery.append(" and ppg.product_group_id = :groupId ");
            params.put("groupId", groupId);
        }
        if (!Strings.isNullOrEmpty(keyword)) {
            strQuery.append(" and (p.bar_code = :searchBarcode or ppu.bar_code = :searchBarcode or p.normalized_name like :keyword ) ");
            params.put("searchBarcode", keywordReplace);
            params.put("keyword", "%" + keywordReplace + "%");
        }
        if (isTopping != null) {
            strQuery.append(" and p.is_topping = :isTopping");
            params.put("isTopping", isTopping);
        }
        if (paramCheckAll) {
            if (ids != null && ids.size() > 0) {
                strQuery.append(" AND p.id not in :ids ");
                params.put("ids", ids);
            }
        } else {
            if (ids != null && ids.size() > 0) {
                strQuery.append(" AND p.id in :ids ");
                params.put("ids", ids);
            }
        }
        Number count = 0;
        if (Boolean.TRUE.equals(isCountAll)) {
            Query countQuery = entityManager.createNativeQuery("SELECT COUNT(*) " + strQuery);
            Common.setParams(countQuery, params);
            count = (Number) countQuery.getSingleResult();
        }
        if (pageable != null && pageable.getSort().isEmpty()) {
            strQuery.append(
                " order by case when p.code = :codeProDefault then 0 when p.code = :codeNoteDefault then 1 when p.code = :codePromotionDefault then 2 when p.id in :productIds then 3 else 4 end, p.name asc "
            );
        } else if (pageable != null) {
            strQuery.append(
                " order by case when p.code = :codeProDefault then 0 when p.code = :codeNoteDefault then 1 when p.code = :codePromotionDefault then 2 when p.id in :productIds then 3 else 4 end, "
            );
            strQuery.append(Objects.requireNonNull(Common.addSort(pageable.getSort(), params)).replace("ORDER BY", ""));
        }
        if (pageable != null) {
            params.put("codeProDefault", Constants.PRODUCT_CODE_DEFAULT);
            params.put("codeNoteDefault", Constants.PRODUCT_CODE_NOTE_DEFAULT);
            params.put("codePromotionDefault", Constants.PRODUCT_CODE_PROMOTION_DEFAULT);
            params.put(
                "productIds",
                (productIds == null || productIds.isEmpty())
                    ? new ArrayList<>() {
                        {
                            add(-1);
                        }
                    }
                    : productIds
            );
        }
        Query query = entityManager.createNativeQuery(
            "select p.id id, " +
            "p.com_id comId, " +
            "p.code code, " +
            "p.code2 code2, " +
            "p.name name, " +
            "ppu.unit_name unit, " +
            "ppu.product_unit_id            unitId, " +
            "IIF(ppu.purchase_price IS NULL, 0, ppu.purchase_price) inPrice, " +
            "IIF(ppu.sale_price IS NULL, 0, ppu.sale_price) outPrice, " +
            "p.vat_rate vatRate, " +
            "p.inventory_tracking inventoryTracking, " +
            "IIF(ppu.on_hand IS NULL, 0, ppu.on_hand) inventoryCount, " +
            "p.description description, " +
            "p.create_time createTime, " +
            "p.update_time updateTime,  " +
            "ppu.bar_code barCode,  " +
            "p.image imageUrl, " +
            "p.is_topping isTopping  " +
            strQuery,
            "ProductItemResult"
        );
        if (pageable == null) {
            Common.setParams(query, params);
            productResponses = query.getResultList();
            return new PageImpl<>(productResponses);
        } else {
            Common.setParamsWithPageable(query, params, pageable, 0);
            productResponses = query.getResultList();
            return new PageImpl<>(
                productResponses,
                pageable,
                Boolean.TRUE.equals(isCountAll) ? count.longValue() : productResponses.size()
            );
        }
    }

    public ProductDetailResponse findByBarcode(Integer companyId, String barcode) {
        List<ProductDetailResponse> products = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(
            "FROM product p left join product_product_unit ppu on p.id = ppu.product_id WHERE p.com_id = :companyId AND ppu.bar_code = :barcode AND p.active = 1 "
        );
        params.put("companyId", companyId);
        params.put("barcode", barcode.trim());

        Query query = entityManager.createNativeQuery(
            "select p.id id, " +
            "p.com_id comId, " +
            "p.code code, " +
            "p.code2 code2, " +
            "p.name name, " +
            "ppu.unit_name unit, " +
            "ppu.product_unit_id            unitId, " +
            "IIF(ppu.purchase_price IS NULL, 0, ppu.purchase_price) inPrice, " +
            "IIF(ppu.sale_price IS NULL, 0, ppu.sale_price) outPrice, " +
            "p.vat_rate vatRate, " +
            "p.inventory_tracking inventoryTracking, " +
            "IIF(ppu.on_hand IS NULL, 0, ppu.on_hand) inventoryCount, " +
            "p.description description, " +
            "p.create_time createTime, " +
            "p.update_time updateTime,  " +
            "ppu.bar_code barCode,  " +
            "p.image imageUrl,  " +
            "p.is_topping isTopping  " +
            strQuery,
            "ProductItemResult"
        );
        Common.setParams(query, params);
        products = query.getResultList();
        return products.isEmpty() ? null : products.get(0);
    }

    @Override
    public ProductItemResponse findByBarcodeForBill(Integer companyId, String barcode) {
        List<ProductItemResponse> productResponses = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" from product p " + " left join product_product_unit ppu on p.id = ppu.product_id and ppu.direct_sale = 1 ");
        strQuery.append(" where p.com_id = :comId AND ppu.bar_code = :barcode AND p.active = 1 ");
        params.put("comId", companyId);
        params.put("barcode", barcode);

        Query query = entityManager.createNativeQuery(
            "select ppu.id productProductUnitId, " +
            "p.id productId, " +
            "p.com_id comId, " +
            "p.code code, " +
            "p.code2 code2, " +
            "p.name name, " +
            "ppu.unit_name unit, " +
            "ppu.product_unit_id            unitId, " +
            "IIF(ppu.purchase_price IS NULL, 0, ppu.purchase_price) inPrice, " +
            "IIF(ppu.sale_price IS NULL, 0, ppu.sale_price) outPrice, " +
            "p.vat_rate vatRate, " +
            "p.inventory_tracking inventoryTracking, " +
            "IIF(ppu.on_hand IS NULL, 0, ppu.on_hand) inventoryCount, " +
            "p.description description, " +
            "p.create_time createTime, " +
            "p.update_time updateTime,  " +
            "ppu.bar_code barCode,  " +
            "p.image imageUrl,  " +
            "p.is_topping isTopping,  " +
            "ppu.is_primary isPrimary,  " +
            "IIF(ppu.other_prices IS NULL, 0, 1) haveOtherPrice, " +
            "p.discount_vat_rate discountVatRate  " +
            strQuery,
            "ProductBillResponse"
        );
        Common.setParams(query, params);
        productResponses = query.getResultList();
        return productResponses.isEmpty() ? null : productResponses.get(0);
    }

    @Override
    public ProductItem findByProductId(Integer companyId, Integer productId) {
        List<ProductItem> products = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(
            "FROM product p left join product_product_unit ppu on p.id = ppu.product_id WHERE p.com_id = :companyId AND p.id = :productId AND p.active = 1 AND ppu.is_primary = 1 "
        );
        params.put("companyId", companyId);
        params.put("productId", productId);

        Query query = entityManager.createNativeQuery(
            "select p.id id, " +
            "p.com_id comId, " +
            "p.code code, " +
            "p.code2 code2, " +
            "p.name name, " +
            "ppu.unit_name unit, " +
            "ppu.product_unit_id            unitId, " +
            "IIF(ppu.purchase_price IS NULL, 0, ppu.purchase_price) inPrice, " +
            "IIF(ppu.sale_price IS NULL, 0, ppu.sale_price) outPrice, " +
            "p.vat_rate vatRate, " +
            "p.inventory_tracking inventoryTracking, " +
            "IIF(ppu.on_hand IS NULL, 0, ppu.on_hand) inventoryCount, " +
            "p.description description, " +
            "p.create_time createTime, " +
            "p.update_time updateTime,  " +
            "ppu.bar_code barCode,  " +
            "p.image imageUrl, " +
            "p.is_topping isTopping, " +
            "p.discount_vat_rate discountVatRate " +
            strQuery,
            "ProductItemResponse"
        );
        Common.setParams(query, params);
        products = query.getResultList();
        return products.isEmpty() ? null : products.get(0);
    }

    @Override
    public ProductItem findByProductIdAdmin(Integer productId) {
        List<ProductItem> products = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(
            "FROM product p left join product_product_unit ppu on p.id = ppu.product_id WHERE p.id = :productId AND ppu.is_primary = 1 "
        );
        params.put("productId", productId);

        Query query = entityManager.createNativeQuery(
            "select p.id id, " +
            "p.com_id comId, " +
            "p.code code, " +
            "p.code2 code2, " +
            "p.name name, " +
            "ppu.unit_name unit, " +
            "ppu.product_unit_id            unitId, " +
            "IIF(ppu.purchase_price IS NULL, 0, ppu.purchase_price) inPrice, " +
            "IIF(ppu.sale_price IS NULL, 0, ppu.sale_price) outPrice, " +
            "p.vat_rate vatRate, " +
            "p.inventory_tracking inventoryTracking, " +
            "IIF(ppu.on_hand IS NULL, 0, ppu.on_hand) inventoryCount, " +
            "p.description description, " +
            "p.create_time createTime, " +
            "p.update_time updateTime,  " +
            "p.bar_code barCode,  " +
            "p.image imageUrl, " +
            "p.is_topping isTopping, " +
            "p.discount_vat_rate discountVatRate " +
            strQuery,
            "ProductItemResponse"
        );
        Common.setParams(query, params);
        products = query.getResultList();
        return products.isEmpty() ? null : products.get(0);
    }

    @Override
    public boolean checkProductExistInInvoice(Integer productId, Integer comId) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(
            "  from bill b join bill_product bp on b.id = bp.bill_id " +
            "join product p on bp.product_id = p.id " +
            "where p.id = :productId and b.com_id = :comId "
        );
        params.put("comId", comId);
        params.put("productId", productId);
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(*) " + strQuery);
        Common.setParams(countQuery, params);
        Number count = (Number) countQuery.getSingleResult();
        return count.longValue() == 0;
    }

    @Override
    public List<InventoryCommonStats.InventoryCommonStatsDetail> inventoryCommonStatsV2(Integer comId, Integer groupId, String keyword) {
        List<InventoryCommonStats.InventoryCommonStatsDetail> inventoryResponses = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" from product p join product_product_unit ppu on p.id = ppu.product_id ");
        if (groupId != null) {
            strQuery.append(" join product_product_group ppg on p.id = ppg.product_id and ppg.product_group_id = :groupId ");
            params.put("groupId", groupId);
        }
        strQuery.append(" where p.com_id = :comId and p.active = 1 and p.inventory_tracking = 1 and ppu.is_primary = 1 ");
        params.put("comId", comId);
        if (!Strings.isNullOrEmpty(keyword)) {
            strQuery.append(" and (p.normalized_name like :keyword or p.code like :keyword) ");
            params.put("keyword", "%" + keyword + "%");
        }
        strQuery.append(" group by p.id, p.code, p.name ");
        Query query = entityManager.createNativeQuery(
            "select " +
            "p.id, " +
            "p.code, " +
            "p.name," +
            "sum(iif(ppu.on_hand is null, 0, ppu.on_hand)) onHand, " +
            "sum(iif(ppu.purchase_price is null, 0, ppu.purchase_price)) purchasePrice, " +
            "sum(iif(ppu.on_hand is null, 0, ppu.on_hand)) * sum(iif(ppu.purchase_price is null, 0, ppu.purchase_price)) value " +
            strQuery,
            "InventoryStatsResponses"
        );
        Common.setParams(query, params);
        inventoryResponses = query.getResultList();
        return inventoryResponses;
    }

    @Override
    public List<ProductToppingItem> findForToppingGroupDetail(Integer comId, List<Integer> ids, Boolean isGetTopping) {
        List<ProductToppingItem> products = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" from product p join product_product_unit ppu on p.id = ppu.product_id and is_primary = 1 ");
        strQuery.append(" where p.com_id = :companyId and active = 1 and p.id in :ids ");
        params.put("companyId", comId);
        params.put("ids", ids);
        if (isGetTopping != null && isGetTopping) {
            strQuery.append(" AND is_topping = 1 ");
        }

        Query query = entityManager.createNativeQuery(
            "select p.id id, " +
            "p.com_id comId, " +
            "p.image image,  " +
            "p.code code, " +
            "p.code2 code2, " +
            "p.name name, " +
            "ppu.unit_name unit, " +
            "ppu.product_unit_id unitId, " +
            "IIF(ppu.purchase_price IS NULL, 0, ppu.purchase_price) purchasePrice, " +
            "IIF(ppu.sale_price IS NULL, 0, ppu.sale_price) salePrice, " +
            "p.vat_rate vatRate, " +
            "p.inventory_tracking inventoryTracking, " +
            "IIF(ppu.on_hand IS NULL, 0, ppu.on_hand) inventoryCount, " +
            "p.description description " +
            strQuery,
            "ProductToppingItem"
        );
        Common.setParams(query, params);
        products = query.getResultList();
        return products;
    }

    @Override
    public List<ProductProfitResult> getProductProfit(Integer comId, String fromDate, String toDate, Pageable pageable, Integer type) {
        List<ProductProfitResult> productProfitResult = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(
            " from rs_inoutward ri " +
            "join rs_inoutward_detail rid on rid.rs_inoutward_id = ri.id and ri.type = :rsType " +
            "right join product p on p.id = rid.product_id " +
            "left join bill_product bp on p.id = bp.product_id " +
            "left join bill b on b.id = bp.bill_id " +
            "where p.com_id = :comId "
        );

        params.put("rsType", Constants.RS_OUTWARD_TYPE);
        params.put("comId", comId);

        if (!Strings.isNullOrEmpty(fromDate) || !Strings.isNullOrEmpty(toDate)) {
            Common.addDateSearchCustom(fromDate, toDate, params, strQuery, "date", "rs_inoutward");
            Common.addDateSearchCustom(fromDate, toDate, params, strQuery, "bill_date", "bill");
        }
        strQuery.append(" group by p.id, p.name ");
        Query query = entityManager.createNativeQuery(
            "select " +
            "p.id id, " +
            "p.name name, " +
            "sum(iif(rid.quantity is null, 0, rid.quantity)) quantity, " +
            "sum(iif(bp.total_amount is null, 0, bp.total_amount)) revenue, " +
            "sum(iif(bp.total_amount is null, 0, bp.total_amount)) - sum(iif(rid.total_amount is null, 0, rid.total_amount)) profit " +
            strQuery,
            "ProductProfitResult"
        );
        Common.setParamsWithPageable(query, params, pageable);
        productProfitResult = query.getResultList();
        return productProfitResult;
    }

    @Override
    public List<ProductProfitResult> getProductRevenueStats(
        Integer comId,
        String fromDate,
        String toDate,
        Integer type,
        List<Integer> productIds
    ) {
        List<ProductProfitResult> productProfitResult;
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        List<String> productCodeDefault = List.of(
            Constants.PRODUCT_CODE_DEFAULT,
            Constants.PRODUCT_CODE_NOTE_DEFAULT,
            Constants.PRODUCT_CODE_PROMOTION_DEFAULT,
            Constants.PRODUCT_CODE_SERVICE_CHARGE_DEFAULT
        );
        strQuery.append(
            " from product p " +
            "join bill_product bp on p.id = bp.product_id " +
            "join bill b on bp.bill_id = b.id " +
            "where p.com_id = :comId and p.active = 1 and p.code not in :productCodeDefault and b.status = 1 "
        );
        params.put("comId", comId);
        params.put("productCodeDefault", productCodeDefault);
        if (!productIds.isEmpty()) {
            strQuery.append(" and p.id in :productIds ");
            params.put("productIds", productIds);
        }
        if (type != null) {
            strQuery.append(" and p.is_topping = :type ");
            params.put("type", type);
        }

        if (!Strings.isNullOrEmpty(fromDate) || !Strings.isNullOrEmpty(toDate)) {
            Common.addDateSearchCustom(fromDate, toDate, params, strQuery, "b.update_time", "bill");
        }
        // vì tên đvt là duy nhất, nên có thể groupby theo unitName
        strQuery.append(" group by p.id, p.name, bp.unit, bp.unit_id ");
        Query query = entityManager.createNativeQuery(
            "select p.id id, sum(iif(bp.total_amount is null, 0, bp.total_amount)) revenue, " +
            "sum(iif(bp.quantity is null, 0, bp.quantity)) quantity, p.name name, bp.unit unitName, bp.unit_id unitId " +
            strQuery,
            "ProductRevenueResult"
        );
        Common.setParams(query, params);
        productProfitResult = query.getResultList();
        return productProfitResult;
    }

    @Override
    public List<ProductProfitResult> getProductCostStats(
        Integer comId,
        String fromDate,
        String toDate,
        Integer type,
        Integer businessTypeId,
        List<Integer> productIds
    ) {
        List<String> productCodeDefault = List.of(
            Constants.PRODUCT_CODE_DEFAULT,
            Constants.PRODUCT_CODE_NOTE_DEFAULT,
            Constants.PRODUCT_CODE_PROMOTION_DEFAULT,
            Constants.PRODUCT_CODE_SERVICE_CHARGE_DEFAULT
        );
        List<ProductProfitResult> productProfitResult = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(
            " from rs_inoutward ri join rs_inoutward_detail rid on rid.rs_inoutward_id = ri.id and ri.type = :rsType " +
            "join product p on p.id = rid.product_id where p.com_id = :comId and p.code not in :productCodeDefault and p.active = 1 and ri.business_type_id = :businessTypeId "
        );
        params.put("comId", comId);
        params.put("productCodeDefault", productCodeDefault);
        params.put("businessTypeId", businessTypeId);
        params.put("rsType", Constants.RS_OUTWARD_TYPE);

        if (!productIds.isEmpty()) {
            strQuery.append(" and p.id in :productIds ");
            params.put("productIds", productIds);
        }
        if (type != null) {
            strQuery.append(" and p.is_topping = :type ");
            params.put("type", type);
        }
        if (!Strings.isNullOrEmpty(fromDate) || !Strings.isNullOrEmpty(toDate)) {
            Common.addDateSearchCustom(fromDate, toDate, params, strQuery, "date", "rs_inoutward");
        }
        strQuery.append(" group by p.id, p.name, rid.unit_name, rid.unit_id ");
        Query query = entityManager.createNativeQuery(
            "select p.id id, p.name name, rid.unit_name unitName, sum(iif(rid.quantity is null, 0, rid.quantity)) quantity, sum(iif(rid.total_amount is null, 0, rid.total_amount)) costPrice, rid.unit_id unitId " +
            strQuery,
            "ProductCostResult"
        );
        Common.setParams(query, params);
        productProfitResult = query.getResultList();
        return productProfitResult;
    }

    @Override
    public List<ProductItemResponse> getDetailsByIdsOrGroupIds(Integer comId, List<Integer> ids, List<Integer> groupIds) {
        List<ProductItemResponse> productResponses;
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        String sqlSelect =
            "select ppu.id productProductUnitId, " +
            "p.id productId, " +
            "p.com_id comId, " +
            "p.code code, " +
            "p.code2 code2, " +
            "p.feature feature, " +
            "p.name name, " +
            "ppu.unit_name unit, " +
            "ppu.product_unit_id            unitId, " +
            "IIF(ppu.purchase_price IS NULL, 0, ppu.purchase_price) inPrice, " +
            "IIF(ppu.sale_price IS NULL, 0, ppu.sale_price) outPrice, " +
            "p.vat_rate vatRate, " +
            "p.inventory_tracking inventoryTracking, " +
            "IIF(ppu.on_hand IS NULL, 0, ppu.on_hand) inventoryCount, " +
            "p.description description, " +
            "p.create_time createTime, " +
            "p.update_time updateTime,  " +
            "p.bar_code barCode,  " +
            "p.image imageUrl, " +
            "p.is_topping isTopping, " +
            "ppu.is_primary isPrimary , " +
            "IIF(ppu.other_prices IS NULL, 0, 1) haveOtherPrice, " +
            "p.discount_vat_rate discountVatRate, ";
        strQuery.append(" from product p left join product_product_unit ppu on p.id = ppu.product_id ");

        if (ids != null) {
            strQuery.append(" where p.com_id = :comId and p.active = 1 and ppu.id in :ids ");
            params.put("ids", ids);
            sqlSelect += " null groupId ";
        }
        if (groupIds != null) {
            strQuery.append(" left join product_product_group ppg on ppg.product_id = p.id where p.com_id = :comId and p.active = 1 ");
            strQuery.append(" and ppg.product_group_id in :groupIds ");
            params.put("groupIds", groupIds);
            sqlSelect += " ppg.product_group_id groupId ";
        }

        params.put("comId", comId);
        Query query = entityManager.createNativeQuery(sqlSelect + strQuery, "ProductVoucherResponse");
        Common.setParams(query, params);
        productResponses = query.getResultList();
        return productResponses;
    }
}
