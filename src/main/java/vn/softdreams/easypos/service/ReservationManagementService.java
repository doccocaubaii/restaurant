package vn.softdreams.easypos.service;

import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.dto.reservation.ReservationCreateRequest;
import vn.softdreams.easypos.dto.reservation.ReservationUpdateRequest;
import vn.softdreams.easypos.service.dto.ResultDTO;

public interface ReservationManagementService {
    ResultDTO create(ReservationCreateRequest request);

    ResultDTO update(ReservationUpdateRequest request);

    ResultDTO delete(Integer id, Integer comId);

    ResultDTO findAll(Pageable pageable, Integer status);

    ResultDTO findOne(Integer id);
}
