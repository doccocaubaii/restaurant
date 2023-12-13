package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.TransactionDetail;

/**
 * Spring Data JPA repository for the Transactiondetail entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransactiondetailRepository extends JpaRepository<TransactionDetail, Integer> {}
