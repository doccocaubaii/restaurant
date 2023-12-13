package vn.softdreams.easypos.service;

import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.service.dto.ResultDTO;

public interface ProcessingAreaProductManagementService {
    ResultDTO getProductByProcessingAreaId(Integer processingAreaId, Pageable pageable);
    ResultDTO delete(Integer id);
    ResultDTO getAllProductProductUnitIdNotPaId(Integer comId, Integer processingAreaId);
}
