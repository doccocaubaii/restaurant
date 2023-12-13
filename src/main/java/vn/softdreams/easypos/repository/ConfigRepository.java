package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.Config;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Config entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfigRepository extends JpaRepository<Config, Integer>, ConfigRepositoryCustom {
    @Query(value = "SELECT * from Config where company_id = ?1 and code in ?2", nativeQuery = true)
    List<Config> getAllByCompanyID(Integer companyId, List<String> codes);

    @Query(value = "SELECT c.value FROM Config c WHERE c.company_id = ?1 and c.code = ?2", nativeQuery = true)
    Optional<String> getValueByComIdAndCode(int comId, String code);

    Optional<Config> findByIdAndCompanyId(Integer id, Integer CompanyId);

    Integer countByCompanyIdAndCode(Integer companyId, String code);

    @Query(
        value = "select c.* from config c join company c2 on c.company_id = c2.id where c2.com_owner_id = ?1 and c.code = ?2 ",
        nativeQuery = true
    )
    List<Config> getByCompanyIdAndCode(Integer ownerId, String code);

    Optional<Config> findByCompanyIdAndCode(Integer companyId, String code);

    @Query(
        value = "select c.* from config c join company c2 on c.company_id = c2.id where c2.com_owner_id = ?1 and c.code in ?2 and is_parent = 0",
        nativeQuery = true
    )
    List<Config> findByOwnerIdAndCodes(Integer companyOwnerId, List<String> invoiceConfigCodes);

    List<Config> findByCompanyId(Integer companyId);

    @Query(value = "SELECT * from config WHERE company_id = ?1", nativeQuery = true)
    List<Config> getValueExtraConfig(Integer company_id);
}
