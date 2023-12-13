package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.BusinessType;

import java.util.List;
import java.util.Optional;

@Repository
public interface BusinessTypeRepository extends JpaRepository<BusinessType, Integer>, BusinessTypeRepositoryCustom {
    Integer countByIdAndComId(Integer id, Integer comId);

    @Query(value = "select count(*) from business_type where com_id = ?1 and business_type_code LIKE ?2", nativeQuery = true)
    Integer countByComIdAndType(Integer comId, String type);

    @Query(value = "select id from business_type where com_id = ?1 and business_type_code = ?2", nativeQuery = true)
    Integer getIdByComIdAndCode(Integer comId, String n2);

    Optional<BusinessType> findByIdAndComId(Integer id, Integer comId);

    List<BusinessType> findByBusinessTypeCode(String code);

    List<BusinessType> findByBusinessTypeNameAndComIdAndType(String name, Integer comId, String type);

    List<BusinessType> findByBusinessTypeCodeAndComIdIn(String code, List<Integer> comIds);

    @Query(value = "select id from company", nativeQuery = true)
    List<Integer> getAllCompanyId();
}
