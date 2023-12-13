package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.Company;
import vn.softdreams.easypos.dto.company.CompanyItemsResult;
import vn.softdreams.easypos.dto.printTemplate.PrintDataInfoCompany;
import vn.softdreams.easypos.dto.user.GetByUserNameResult;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Company entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer>, CompanyRepositoryCustom {
    @Query(value = "select c.* from company_user cu join company c on cu.company_id = c.id where cu.user_id = ?1", nativeQuery = true)
    List<Company> findAllByUserID(Integer id);

    Optional<Company> findById(Integer id);

    @Query(value = "select count(*) from company c where c.name like ?1 and c.com_owner_id = ?2", nativeQuery = true)
    Integer countByNameAndOwnerId(String name, Integer ownerId);

    @Query(value = "select count(*) from company c where c.com_owner_id = ?1", nativeQuery = true)
    Integer countByOwnerId(Integer ownerId);

    @Query(value = "select c.id from company c where c.com_owner_id = ?1 and c.is_parent = 0", nativeQuery = true)
    List<Integer> findAllSubCompanyByOwnerId(Integer id);

    @Query(value = "select c.id from company c where c.com_owner_id = ?1 ", nativeQuery = true)
    List<Integer> findAllByOwnerId(Integer id);

    @Query(
        value = "select top(1) c.id from company c join company_owner co on c.com_owner_id  = co.id where tax_code = ?1",
        nativeQuery = true
    )
    Integer findCompanyIdByTaxCode(String taxCode);

    @Query(value = "select c.name from company c where c.id = ?1", nativeQuery = true)
    String getNameById(Integer companyId);

    @Query(value = "select top(1) * from company where com_owner_id = ?1 order by update_time desc", nativeQuery = true)
    Optional<Company> findCompanyLatest(Integer id);

    @Query(value = "select co.owner_id from company c join company_owner co on c.com_owner_id = co.id where c.id = ?1", nativeQuery = true)
    Integer getOwnerByComId(Integer companyId);

    @Query(
        value = "select count(*) from company c join company_owner co on c.com_owner_id = co.id where c.id = ?1 and co.owner_id = ?2",
        nativeQuery = true
    )
    Integer countOwnerByComIdAndOwnerId(Integer companyId, Integer id);

    @Query(value = "select c.* from company c where c.com_owner_id = ?1", nativeQuery = true)
    List<Company> getAllByOwnerId(Integer ownerId);

    @Query(value = "select c.id from company c where c.com_owner_id = ?1 and c.is_parent = 1 ", nativeQuery = true)
    Integer getIdByOwner(Integer ownerId);

    @Query(value = "select c.id id, c.name name from company c where c.normalized_name like %?1%", nativeQuery = true)
    List<CompanyItemsResult> getAllItems(String keyword);

    Integer countById(Integer id);

    @Query(value = "select c.* from company c where c.id = ?1 and c.com_owner_id = ?2", nativeQuery = true)
    Optional<Company> findByIdAndOwnerId(Integer id, Integer ownerId);

    @Query(value = "select c.com_owner_id from company c where c.id = ?1 ", nativeQuery = true)
    Integer getCompanyOwnerIdById(Integer id);

    @Query(
        value = "select count(c.id) from company_owner co join company c on c.com_owner_id = co.id where co.id = ?1 group by co.id",
        nativeQuery = true
    )
    Integer countAllByOwner(Integer ownerId);

    @Query(value = "select id from company", nativeQuery = true)
    List<Integer> getAllCompanyId();

    @Query(
        value = "select c.id comId, c.name comName, co.tax_code taxCode, co.id comOwnerId " +
        "from company c join company_owner co on c.com_owner_id = co.id where c.id in ?1",
        nativeQuery = true
    )
    List<GetByUserNameResult> getAllCompanyByIds(List<Integer> ids);

    @Query(value = "select id, name, address, phone phoneNumber from company where id = ?1", nativeQuery = true)
    Optional<PrintDataInfoCompany> findInfoPrintById(Integer companyId);
}
