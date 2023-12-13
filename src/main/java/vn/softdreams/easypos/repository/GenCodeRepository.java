package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.GenCode;

/**
 * Spring Data JPA repository for the GenCode entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GenCodeRepository extends JpaRepository<GenCode, Integer> {}
