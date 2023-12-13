package vn.softdreams.easypos.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.dto.processingAreaProduct.ProcessingAreaProductItemResponse;
import vn.softdreams.easypos.dto.processingAreaProduct.ProductProcessingArea;

public interface ProcessingAreaProductRepositoryCustom {
    Page<ProcessingAreaProductItemResponse> getProductByProcessingAreaId(Integer processingAreaId, Pageable pageable);
    Page<ProductProcessingArea> getAllProductProductUnitIdNotPaId(Integer comId, Integer processingAreaId);
}
