package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.softdreams.easypos.domain.Authority;

import java.util.List;
import java.util.Set;

/**
 * Spring Data JPA repository for the {@link } entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, Integer> {
    @Query(value = "SELECT * from permission where parent_id is null ", nativeQuery = true)
    List<Authority> getAllAuthorityParent();

    @Query(
        value = "select r.permission_code from role_permission r join user_role ur on r.role_id = ur.role_id where ur.com_id = ?2 and ur.user_id = ?1",
        nativeQuery = true
    )
    Set<String> findAllAuthorityByUserIDAndCompanyId(Integer id, Integer companyID);

    @Query(
        value = "select r.permission_code from role_permission r join user_role ur on r.role_id = ur.role_id where  ur.user_id = ?1 and ur.com_id = ?2 and r.permission_code in (?3)",
        nativeQuery = true
    )
    Set<String> findAllAuthorityByUserIDAndCompanyId(Integer id, Integer companyID, List<String> codes);

    @Query(
        value = "select r.permission_code from role_permission r join user_role ur on r.role_id = ur.role_id where ur.user_id = ?1",
        nativeQuery = true
    )
    Set<String> findAllAuthorityByUserID(Integer id);

    @Query(value = "SELECT * from permission where code in ?1 ", nativeQuery = true)
    List<Authority> findAllByCode(List<String> rolePermissions);

    @Query(
        value = "select " +
        "top 1 " +
        "r.name " +
        "from user_role ur " +
        "         join role r on r.id = ur.role_id " +
        "where ur.user_id = ?1 " +
        "  and ur.com_id = ?2 ",
        nativeQuery = true
    )
    String getRoleNameByUserIdAndCompanyId(Integer userId, Integer companyId);
}
