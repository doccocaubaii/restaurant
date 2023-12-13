package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.Role;
import vn.softdreams.easypos.dto.permission.PermissionItemResponse;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Role entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByCode(String code);

    Optional<Role> findRoleById(Integer id);

    @Query(value = "select id from role where code = ?1", nativeQuery = true)
    Integer findIdByCode(String code);

    @Query(
        value = "select id id, code code, parent_code parentCode, name name, description description from permission",
        nativeQuery = true
    )
    List<PermissionItemResponse> getAllPermission();
}
