package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.UserRole;

import java.util.Optional;

/**
 * Spring Data JPA repository for the UserRole entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {
    @Query(value = "select * from user_role where user_id = ?1 and com_id = ?2", nativeQuery = true)
    Optional<UserRole> findByUserIdAndComId(Integer id, Integer companyId);

    @Query(value = "select * from user_role where user_id = ?1 ", nativeQuery = true)
    Optional<UserRole> findByUserId(Integer id);
}
