package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.PrintConfig;

import java.util.Optional;

/**
 * Spring Data JPA repository for the PrintConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PrintConfigRepository extends JpaRepository<PrintConfig, Integer>, PrintConfigRepositoryCustom {
    Optional<PrintConfig> findByIdAndComId(Integer id, Integer comId);
    Optional<PrintConfig> findByComIdAndCode(Integer comId, String Code);
}
