package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.softdreams.easypos.domain.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
