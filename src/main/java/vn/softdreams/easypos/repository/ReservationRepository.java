package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.Reservation;

import java.util.Optional;

/**
 * Spring Data JPA repository for the Reservation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer>, ReservationRepositoryCustom {
    Optional<Reservation> findByIdAndComId(Integer id, Integer comId);

    Integer countByIdAndComId(Integer id, Integer comId);
}
