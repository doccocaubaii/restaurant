package vn.softdreams.easypos.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.dto.processingArea.ProcessingAreaItemResponse;
import vn.softdreams.easypos.dto.processingArea.ProductProcessingAreaResponse;

public interface ProcessingAreaRepositoryCustom {
    Page<ProcessingAreaItemResponse> filter(
        Integer comId,
        String name,
        Integer setting,
        Integer active,
        String fromDate,
        String toDate,
        Pageable pageable
    );

    Page<ProductProcessingAreaResponse> filterProduct(Integer comId, Integer processingAreaId, String keyword, Pageable pageable);
}
