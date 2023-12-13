package vn.softdreams.easypos.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.Business;

/**
 * Spring Data JPA repository for the Business entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BusinessRepository extends JpaRepository<Business, Integer> {
    Integer countById(Integer id);

    @Query(value = "select * from business where normalized_name like %?1%", nativeQuery = true)
    Page<Business> getAll(Pageable pageable, String keyword);
}
