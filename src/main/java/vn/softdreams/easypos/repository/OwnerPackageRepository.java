package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.OwnerPackage;

import java.util.Optional;

/**
 * Spring Data JPA repository for the OwnerPackage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OwnerPackageRepository extends JpaRepository<OwnerPackage, Integer> {
    @Query(value = "select op.* from owner_package op where op.owned_id = ?1", nativeQuery = true)
    Optional<OwnerPackage> findByOwnerId(Integer ownerId);
}
