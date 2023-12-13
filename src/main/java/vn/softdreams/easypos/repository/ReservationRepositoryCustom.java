package vn.softdreams.easypos.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.dto.reservation.ReservationResponse;

public interface ReservationRepositoryCustom {
    Page<ReservationResponse> findAll(Pageable pageable, Integer comId, Integer status);
}
