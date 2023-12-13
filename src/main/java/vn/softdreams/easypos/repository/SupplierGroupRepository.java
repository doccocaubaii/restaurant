package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.SupplierGroup;

/**
 * Spring Data JPA repository for the SupplierGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SupplierGroupRepository extends JpaRepository<SupplierGroup, Integer> {}
