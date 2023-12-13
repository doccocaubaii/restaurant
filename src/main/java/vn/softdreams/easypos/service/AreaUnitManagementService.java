package vn.softdreams.easypos.service;

import org.springframework.stereotype.Service;
import vn.softdreams.easypos.dto.areaUnit.AreaUnitCreateRequest;
import vn.softdreams.easypos.dto.areaUnit.AreaUnitDeleteRequest;
import vn.softdreams.easypos.dto.areaUnit.AreaUnitUpdateRequest;
import vn.softdreams.easypos.service.dto.ResultDTO;

@Service
public interface AreaUnitManagementService {
    ResultDTO createAreaUnit(AreaUnitCreateRequest request);

    ResultDTO updateAreaUnit(AreaUnitUpdateRequest request);

    ResultDTO findOneAreaUnit(Integer id);

    ResultDTO deleteAreaUnit(AreaUnitDeleteRequest request);
}
