package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.Area;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Area entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AreaRepository extends JpaRepository<Area, Integer>, AreaRepositoryCustom {
    Integer countByComIdAndName(Integer comId, String name);

    Integer countByComId(Integer comId);

    List<Area> findAllByComId(Integer comId);

    Optional<Area> findByIdAndComId(Integer id, Integer comId);
}
