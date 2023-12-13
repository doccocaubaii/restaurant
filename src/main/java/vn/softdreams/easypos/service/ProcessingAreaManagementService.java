package vn.softdreams.easypos.service;

import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.domain.ProcessingArea;
import vn.softdreams.easypos.dto.processingArea.ProcessingAreaRequest;
import vn.softdreams.easypos.service.dto.ResultDTO;

/**
 * Service Interface for managing {@link ProcessingArea}.
 */
public interface ProcessingAreaManagementService {
    ResultDTO filter(Integer comId, String name, Integer setting, Integer active, String fromDate, String toDate, Pageable pageable);

    ResultDTO save(ProcessingAreaRequest request);

    ResultDTO delete(Integer id);

    ResultDTO getProcessingArea(Integer id);

    ResultDTO filterProduct(Integer comId, Integer processingAreaId, String keyword, Pageable pageable);

    ResultDTO update(ProcessingAreaRequest request);
}
