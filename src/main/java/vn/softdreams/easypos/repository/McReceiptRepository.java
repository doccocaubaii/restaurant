package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.McReceipt;

import java.util.Optional;

/**
 * Spring Data JPA repository for the McReceipt entity.
 */
@SuppressWarnings("unused")
@Repository
public interface McReceiptRepository extends JpaRepository<McReceipt, Integer> {
    Optional<McReceipt> findOneByBillId(Integer billId);

    Optional<McReceipt> findById(Integer id);

    @Query(value = "select id from mc_receipt where com_id = ?1 and rs_inoutward_id = ?2 ", nativeQuery = true)
    Integer findIdByComIdAndRsInoutWardId(Integer comId, Integer rsInoutWardId);

    @Query(value = "select no from mc_receipt where com_id = ?1 and rs_inoutward_id = ?2 ", nativeQuery = true)
    String findNoByComIdAndRsInoutWardId(Integer comId, Integer rsInoutWardId);

    Optional<McReceipt> findOneByComIdAndRsInoutWardId(Integer comId, Integer rsInoutWardId);
}
