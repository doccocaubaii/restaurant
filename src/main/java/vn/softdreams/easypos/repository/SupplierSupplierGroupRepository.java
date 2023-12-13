package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.SupplierSupplierGroup;

/**
 * Spring Data JPA repository for the SupplierSupplierGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SupplierSupplierGroupRepository extends JpaRepository<SupplierSupplierGroup, Integer> {}
