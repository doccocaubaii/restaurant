package vn.softdreams.easypos.repository;

import vn.softdreams.easypos.dto.areaUnit.AreaUnitDetailResponse;

import java.util.List;

public interface AreaUnitRepositoryCustom {
    AreaUnitDetailResponse getById(Integer id, Integer companyId);
    List<AreaUnitDetailResponse> getAllWithPaging(Integer comId, List<Integer> areaIds, Integer size, String areaUnitKeyword);
}
