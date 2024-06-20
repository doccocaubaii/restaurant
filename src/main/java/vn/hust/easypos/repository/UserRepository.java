package vn.hust.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.hust.easypos.domain.User;

import java.util.Optional;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer>, UserRepositoryCustom {
    @Query(value = "SELECT * FROM ep_user u where u.username = ?1", nativeQuery = true)
    Optional<User> findOneByUsername(String username);
}
