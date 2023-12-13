package vn.softdreams.easypos.repository;

import vn.softdreams.easypos.dto.area.AreaDetailResponse;

import java.util.List;

public interface AreaRepositoryCustom {
    List<AreaDetailResponse> getAllWithPaging(Integer comId, Integer size, String keyword, Integer areaId);
    AreaDetailResponse getById(Integer id, Integer comId);
}
