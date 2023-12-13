package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.AreaUnit;

import java.util.List;
import java.util.Optional;

@Repository
public interface AreaUnitRepository extends JpaRepository<AreaUnit, Integer>, AreaUnitRepositoryCustom {
    Integer countByAreaIdAndName(Integer areaId, String name);

    Optional<AreaUnit> findByIdAndComIdAndAreaId(Integer id, Integer comId, Integer areaId);
    Optional<AreaUnit> findByIdAndComId(Integer id, Integer comId);

    List<AreaUnit> findByAreaId(Integer areaId);
}
