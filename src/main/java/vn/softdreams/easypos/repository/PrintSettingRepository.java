package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import vn.softdreams.easypos.domain.PrintSetting;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PrintSettingRepository extends JpaRepository<PrintSetting, Integer>, PrintSettingRepositoryCustom {
    Optional<PrintSetting> findByIdAndComId(Integer id, Integer comId);
    Integer countByComIdAndPrintNameAndTypeIsNotNull(Integer comId, String name);
    Integer countByComIdAndPrintNameAndIdNotAndTypeIsNotNull(Integer comId, String name, Integer id);
    Integer countAllByComIdAndIpAddress(Integer comId, String ipAddress);
    Integer countAllByComIdAndIpAddressAndIdNot(Integer comId, String ipAddress, Integer id);

    @Modifying
    @Query(value = "delete from print_setting where com_id = ?1 and processing_area_id in ?2 and type is null", nativeQuery = true)
    void deleteByComIdAndProcessingAreaId(Integer comId, Set<Integer> ids);

    List<PrintSetting> findByComIdAndProcessingAreaIdInAndTypeTemplate(Integer comId, Set<Integer> processingAreaId, Integer typeTemplate);

    List<PrintSetting> findByComIdAndProcessingAreaIdIn(Integer comId, List<Integer> ids);
}
