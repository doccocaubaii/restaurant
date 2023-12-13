package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.Receivable;

import java.util.Optional;

/**
 * Spring Data JPA repository for the Receivable entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReceivableRepository extends JpaRepository<Receivable, Integer> {
    Integer countByIdAndComId(Integer id, Integer comID);
    Optional<Receivable> findOneByBillId(Integer billId);
}
