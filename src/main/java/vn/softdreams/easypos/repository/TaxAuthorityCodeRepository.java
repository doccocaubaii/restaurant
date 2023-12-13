package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.TaxAuthorityCode;

import java.util.List;

/**
 * Spring Data JPA repository for the TaxAuthorityCode entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TaxAuthorityCodeRepository extends JpaRepository<TaxAuthorityCode, Integer> {
    List<TaxAuthorityCode> findByComIdAndDeviceId(Integer companyId, String deviceId);
    List<TaxAuthorityCode> findAllByComId(Integer companyId);
}
