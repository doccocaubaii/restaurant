package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.EPPackage;

import java.util.Optional;

/**
 * Spring Data JPA repository for the EPPackage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EPPackageRepository extends JpaRepository<EPPackage, Integer> {
    Optional<EPPackage> findByPackageCode(String packageCode);
    Integer countByPackageName(String packageName);
    Optional<EPPackage> findByIdAndStatus(Integer id, Integer status);
    Optional<EPPackage> findByStatusAndType(Integer status, String type);
}
