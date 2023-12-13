package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.VersionUpdate;

import java.util.List;

/**
 * Spring Data JPA repository for the VersionUpdate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VersionUpdateRepository extends JpaRepository<VersionUpdate, Integer>, VersionUpdateCustom {
    @Query(
        value = "select * from version_update where (com_id is null or com_id = ?1) and CAST(start_date as DATE) <= ?2 and CAST(end_date as DATE) >= ?2 and system in ?3",
        nativeQuery = true
    )
    List<VersionUpdate> findAllForToday(Integer comId, String date, List<Integer> system);
}
