package vn.softdreams.easypos.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.dto.processingArea.ProcessingAreaItemResponse;
import vn.softdreams.easypos.dto.processingArea.ProductProcessingAreaResponse;
import vn.softdreams.easypos.repository.ProcessingAreaProductRepository;
import vn.softdreams.easypos.repository.ProcessingAreaRepositoryCustom;
import vn.softdreams.easypos.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;

public class ProcessingAreaRepositoryImpl implements ProcessingAreaRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ProcessingAreaProductRepository processingAreaProductRepository;

    @Override
    public Page<ProcessingAreaItemResponse> filter(
        Integer comId,
        String name,
        Integer setting,
        Integer active,
        String fromDate,
        String toDate,
        Pageable pageable
    ) {
        List<ProcessingAreaItemResponse> responseList = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" FROM processing_area a ");
        strQuery.append(" WHERE 1 = 1");
        if (comId != null) {
            strQuery.append(" AND a.com_id = :comId ");
            params.put("comId", comId);
        }
        if (!Strings.isNullOrEmpty(name)) {
            String keywordReplace = Common.normalizedName(Arrays.asList(name));
            strQuery.append(" AND a.normalized_name like :name ");
            params.put("name", "%" + keywordReplace + "%");
        }
        if (setting != null) {
            strQuery.append(" AND a.setting = :setting ");
            params.put("setting", setting);
        }
        if (active == null) {
            strQuery.append(" AND (a.active = 0 OR a.active =1) ");
        } else {
            strQuery.append(" AND a.active = :active ");
            params.put("active", active);
        }
        Common.addDateSearchCustom(fromDate, toDate, params, strQuery, "a.create_time", "processing_area");
        Common.replaceWhere(strQuery);
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(*) " + strQuery);
        Common.setParams(countQuery, params);
        Number count = (Number) countQuery.getSingleResult();
        strQuery.append(" ORDER BY a.name ");
        if (count.intValue() > 0) {
            Query query = entityManager.createNativeQuery(
                "select " +
                "a.id id, " +
                "a.com_id comId, " +
                "a.name name, " +
                "a.setting setting, " +
                "a.active active, " +
                "a.create_time createTime " +
                strQuery,
                "ProcessingAreaItemResponseDTO"
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
    public Page<ProductProcessingAreaResponse> filterProduct(Integer comId, Integer processingAreaId, String keyword, Pageable pageable) {
        List<ProductProcessingAreaResponse> responseList = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" FROM product_product_unit ppu ");
        strQuery.append("join product p on p.id = ppu.product_id and p.active = 1 ");
        //        if (processingAreaId != null) {
        //            List<Integer> list = processingAreaProductRepository.getAllProcessingAreaProductByPaId(processingAreaId);
        //            if (list.size() > 0) {
        //                strQuery.append("and ppu.id not in :list ");
        //                params.put("list", list);
        //            }
        //        }
        strQuery.append("left join processing_area_product pap on pap.product_product_unit_id = ppu.id ");
        strQuery.append("WHERE p.code <>'SP1' and p.code<>'SPKM' and p.code<>'SPGC' ");
        //        strQuery.append(" WHERE pap.id is null");
        if (comId != null) {
            strQuery.append(" AND p.com_id = :comId ");
            params.put("comId", comId);
        }
        if (!Strings.isNullOrEmpty(keyword)) {
            String keywordReplace = Common.normalizedName(Arrays.asList(keyword));
            strQuery.append(" AND p.normalized_name like :name ");
            params.put("name", "%" + keywordReplace + "%");
        }

        String fromDate = null;
        String toDate = null;
        Common.addDateSearchCustom(fromDate, toDate, params, strQuery, "ppu.create_time", "product_product_unit");
        Common.replaceWhere(strQuery);
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(*) " + strQuery);
        Common.setParams(countQuery, params);
        Number count = (Number) countQuery.getSingleResult();
        strQuery.append(" ORDER BY p.name ");
        if (count.intValue() > 0) {
            Query query = entityManager.createNativeQuery(
                "select " +
                "p.id id, " +
                "p.com_id comId, " +
                "ppu.id productProductUnitId, " +
                "p.name name, " +
                "p.code2 code2, " +
                "ppu.unit_name unit " +
                strQuery,
                "ProductProcessingAreaResponseDTO"
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
