package vn.softdreams.easypos.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.dto.processingAreaProduct.ProcessingAreaProductItemResponse;
import vn.softdreams.easypos.dto.processingAreaProduct.ProductProcessingArea;
import vn.softdreams.easypos.repository.ProcessingAreaProductRepositoryCustom;
import vn.softdreams.easypos.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcessingAreaProductRepositoryImpl implements ProcessingAreaProductRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Page<ProcessingAreaProductItemResponse> getProductByProcessingAreaId(Integer processingAreaId, Pageable pageable) {
        List<ProcessingAreaProductItemResponse> responseList = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" FROM processing_area_product pp ");
        strQuery.append(" join processing_area a on pp.processing_area_id = a.id ");
        strQuery.append(" join product_product_unit u on pp.product_product_unit_id = u.id ");
        strQuery.append(" join product p on u.product_id = p.id ");
        strQuery.append(" WHERE pp.processing_area_id = :processingAreaId");
        params.put("processingAreaId", processingAreaId);
        Common.replaceWhere(strQuery);
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(*) " + strQuery);
        Common.setParams(countQuery, params);
        Number count = (Number) countQuery.getSingleResult();
        if (count.intValue() > 0) {
            Query query = entityManager.createNativeQuery(
                "SELECT " +
                "pp.id id, " +
                "pp.com_id comId, " +
                "pp.processing_area_id processingAreaId, " +
                "pp.product_product_unit_id productProductUnitId, " +
                "p.code2 code2, " +
                "p.name name, " +
                "u.unit_name unit " +
                strQuery,
                "ProcessingAreaProductItemResponseDTO"
            );
            if (pageable == null) {
                pageable = PageRequest.of(0, (Integer) count);
            }
            Common.setParamsWithPageable(query, params, pageable, count);
            responseList = query.getResultList();
        }
        if (pageable == null) {
            pageable = PageRequest.of(0, 1);
        }
        return new PageImpl<>(responseList, pageable, count.longValue());
    }

    @Override
    public Page<ProductProcessingArea> getAllProductProductUnitIdNotPaId(Integer comId, Integer processingAreaId) {
        Pageable pageable = null;
        List<ProductProcessingArea> responseList = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" from processing_area_product pap ");
        strQuery.append(" join processing_area pa on pap.processing_area_id = pa.id  ");
        strQuery.append(" where 1 = 1 ");
        if (comId != null) {
            strQuery.append(" and pap.com_id = :comId ");
            params.put("comId", comId);
        }
        if (processingAreaId != null) {
            strQuery.append(" and pap.processing_area_id <> :processingAreaId ");
            params.put("processingAreaId", processingAreaId);
        }
        Common.replaceWhere(strQuery);
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(*) " + strQuery);
        Common.setParams(countQuery, params);
        Number count = (Number) countQuery.getSingleResult();
        if (count.intValue() > 0) {
            Query query = entityManager.createNativeQuery(
                "SELECT " + "pap.product_product_unit_id productProductUnitId, " + "pa.name processingAreaName " + strQuery,
                "ProductProcessingAreaItemResponseDTO"
            );
            if (pageable == null) {
                pageable = PageRequest.of(0, (Integer) count);
            }
            Common.setParamsWithPageable(query, params, pageable, count);
            responseList = query.getResultList();
        }
        if (pageable == null) {
            pageable = PageRequest.of(0, 1);
        }
        return new PageImpl<>(responseList, pageable, count.longValue());
    }
}
