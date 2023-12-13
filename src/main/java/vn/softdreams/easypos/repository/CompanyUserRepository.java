package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.CompanyUser;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the CompanyUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompanyUserRepository extends JpaRepository<CompanyUser, Integer>, CompanyUserRepositoryCustom {
    Integer countAllByCompanyIdAndUserId(Integer companyId, Integer userId);

    Optional<CompanyUser> findByCompanyIdAndUserId(Integer companyId, Integer userId);

    @Modifying
    @Query(value = "delete from company_user where company_id = ?1 and user_id = ?2", nativeQuery = true)
    void deleteByComIdAndUserId(Integer companyId, Integer id);

    @Modifying
    @Query(value = "delete from company_user where company_id = ?1 and user_id in ?2", nativeQuery = true)
    void deleteListByComIdAndUserId(Integer companyId, List<Integer> ids);

    @Query(value = "select count(*) from company_user where company_id = ?1 and user_id = ?2", nativeQuery = true)
    Integer countByComIdAndUserId(Integer companyId, Integer id);

    Optional<CompanyUser> findOneByCompanyIdAndUserId(Integer companyId, Integer userId);

    List<CompanyUser> findAllByUserId(Integer userId);

    @Query(
        value = "select c.com_owner_id from company_user cu join company c on cu.company_id = c.id where cu.user_id = ?1 ",
        nativeQuery = true
    )
    List<Integer> getCompanyOwnerIdByUserId(Integer userId);

    Integer countAllByUserId(Integer userId);
}
