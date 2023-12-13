package vn.softdreams.easypos.service;

import org.springframework.stereotype.Service;
import vn.softdreams.easypos.dto.area.AreaCreateRequest;
import vn.softdreams.easypos.dto.area.AreaDeleteRequest;
import vn.softdreams.easypos.dto.area.AreaUpdateRequest;
import vn.softdreams.easypos.service.dto.ResultDTO;

@Service
public interface AreaManagementService {
    ResultDTO getAllForOffline();

    ResultDTO getAllWithPaging(
        Integer areaSize,
        Integer areaUnitSize,
        Integer reservationId,
        String keyword,
        String areaUnitKeyword,
        Integer areaId
    );

    ResultDTO createArea(AreaCreateRequest request);

    ResultDTO updateArea(AreaUpdateRequest request);

    ResultDTO deleteArea(AreaDeleteRequest request);

    ResultDTO findOneArea(Integer id);
}
