package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.Payable;

/**
 * Spring Data JPA repository for the Payable entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PayableRepository extends JpaRepository<Payable, Integer> {}
