package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.ProductProductGroup;

/**
 * Spring Data JPA repository for the ProductProductGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductProductGroupRepository extends JpaRepository<ProductProductGroup, Integer> {}
