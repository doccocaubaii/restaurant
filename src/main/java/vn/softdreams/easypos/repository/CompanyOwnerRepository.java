package vn.softdreams.easypos.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.CompanyOwner;
import vn.softdreams.easypos.dto.company.CompanyItemsResult;
import vn.softdreams.easypos.dto.user.RegisterUserRequest;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the CompanyOwner entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompanyOwnerRepository extends JpaRepository<CompanyOwner, Integer>, CompanyOwnerRepositoryCustom {
    @Modifying
    @Query(value = "update company_owner set tax_machine_code = ?2 where id = ?1", nativeQuery = true)
    void taxMachineCode(Integer id, String value);

    @Query(value = "select com_owner_id from company where id = ?1", nativeQuery = true)
    Integer findIdByCompanyID(Integer companyId);

    @Query(value = "select * from company_owner where id = ?1", nativeQuery = true)
    Optional<CompanyOwner> findOneById(Integer id);

    @Query(
        value = "select co.* from company_owner co join company c on co.id = c.com_owner_id where c.id = ?1 and co.tax_code = ?2",
        nativeQuery = true
    )
    Optional<CompanyOwner> findByCompanyIdAndTaxCode(Integer companyId, String taxCode);

    @Query(
        value = "select co.tax_machine_code from company_owner co join company c on co.id = c.com_owner_id where c.id = ?1",
        nativeQuery = true
    )
    String getTaxMachineCodeCompanyIdAndTaxCode(Integer companyId);

    Integer countByTaxCode(String taxCode);

    @Query(value = "select top(1) * from company_owner where owner_id = ?1 order by update_time desc", nativeQuery = true)
    Optional<CompanyOwner> findCompanyOwnerLatest(Integer ownerId);

    @Query(
        value = "select co.name companyName, co.tax_code companyTaxCode, " +
        "eu.full_name fullName, eu.username userName, " +
        "p.package_code servicePackage, cast(op.start_date as DATE) startDate, " +
        "cast(op.end_date as DATE) endDate, op.pack_count packCount " +
        "from company_owner co " +
        "join company c on c.com_owner_id = co.id " +
        "join company_user cu on c.id = cu.company_id " +
        "join ep_user eu on cu.user_id = eu.id " +
        "join owner_package op on co.id = op.owned_id " +
        "join package p on op.package_id = p.id " +
        "where c.id = ?2 and eu.id = ?1 ",
        nativeQuery = true
    )
    Optional<RegisterUserRequest> getOwnerInfoByUserIdAndComId(Integer userId, Integer companyId);

    Integer countById(Integer id);

    @Query(value = "select * from company_owner where owner_id = ?1 and (name like %?2% or tax_code like %?2%) ", nativeQuery = true)
    Page<CompanyOwner> findAllByNameOrTaxCode(Pageable pageable, Integer ownerId, String keyword);

    List<CompanyOwner> findAllByOwnerId(Integer ownerId);

    @Query(
        value = "select cf.value from config cf join company c on cf.company_id = c.id where c.com_owner_id = :companyId and cf.code = :code ",
        nativeQuery = true
    )
    String getCompanyOwnerIdEB(Integer companyId, String code);

    @Query(value = "select c.id id, c.name name from company_owner c where c.name like %?1%", nativeQuery = true)
    List<CompanyItemsResult> getAllItems(Pageable pageable, String keyword);
}
