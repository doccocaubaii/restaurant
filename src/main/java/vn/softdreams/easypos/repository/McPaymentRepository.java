package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.McPayment;

import java.util.Optional;

/**
 * Spring Data JPA repository for the McPayment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface McPaymentRepository extends JpaRepository<McPayment, Integer> {
    @Query(value = "select id from mc_payment where com_id = ?1 and rs_inoutward_id = ?2 ", nativeQuery = true)
    Integer findIdByComIdAndRsInoutWardId(Integer comId, Integer rsInoutWardId);

    @Query(value = "select no from mc_payment where com_id = ?1 and rs_inoutward_id = ?2 ", nativeQuery = true)
    String findNoByComIdAndRsInoutWardId(Integer comId, Integer rsInoutWardId);

    Optional<McPayment> findOneByComIdAndRsInoutWardId(Integer comId, Integer rsInoutWardId);

    Optional<McPayment> findById(Integer id);
}
