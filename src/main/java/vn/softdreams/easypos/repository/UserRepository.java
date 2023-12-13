package vn.softdreams.easypos.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.User;
import vn.softdreams.easypos.dto.user.UserItemsResult;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer>, UserRepositoryCustom {
    Optional<User> findOneByEmailIgnoreCase(String email);

    @Query(value = "SELECT * FROM ep_user u where lower(u.username) = ?1 and u.status = 1", nativeQuery = true)
    Optional<User> findOneByUsernameActive(String username);

    @Query(value = "SELECT count(*) FROM ep_user u where u.username = ?1", nativeQuery = true)
    Long countByUsername(String username);

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByUsername(String username);

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByEmailIgnoreCase(String email);

    Page<User> findAllByIdNotNull(Pageable pageable);

    Optional<User> findById(Integer id);

    @Query(
        value = "SELECT u.* FROM EP_USER u JOIN company_user cu ON u.id = cu.user_id WHERE u.id = ?1 AND company_id = ?2 ",
        nativeQuery = true
    )
    Optional<User> findUserByIdAndComId(Integer id, Integer companyId);

    @Query(value = "SELECT company_id FROM ep_user u JOIN company_user c ON u.id = c.user_id WHERE u.id = ?1", nativeQuery = true)
    Integer getCompanyIdById(Integer id);

    Optional<User> findByIdAndStatus(Integer id, Integer status);

    Optional<User> findByUsername(String username);

    @Query(value = "select top(1) * from ep_user where status = 1 order by update_time desc", nativeQuery = true)
    Optional<User> findUserLatest();

    Optional<User> findByCreator(Integer creator);

    Integer countByIdAndStatus(Integer id, Integer status);

    @Query(value = "select id id, full_name name from ep_user where status = 1 and normalized_name like %?1% ", nativeQuery = true)
    List<UserItemsResult> getUserItems(String keyword);
}
