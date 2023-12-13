package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.BillPayment;

import java.util.Optional;

/**
 * Spring Data JPA repository for the BillPayment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BillPaymentRepository extends JpaRepository<BillPayment, Integer> {
    @Query(value = "select * from bill_payment where bill_id = ?", nativeQuery = true)
    Optional<BillPayment> findByBillId(Integer billId);
}
