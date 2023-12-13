package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.BillPayment;

/**
 * Spring Data JPA repository for the BillPayment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BillPaymentRepository extends JpaRepository<BillPayment, Long> {}
