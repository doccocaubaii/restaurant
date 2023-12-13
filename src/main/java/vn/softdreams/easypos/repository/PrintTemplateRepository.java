package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.PrintTemplate;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Spring Data JPA repository for the PrintConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PrintTemplateRepository extends JpaRepository<PrintTemplate, Integer>, PrintTemplateRepositoryCustom {
    @Query(value = "select count(*) from print_template where com_id = ?1 and id = ?2", nativeQuery = true)
    Integer countAllByCode(Integer comId, Integer id);

    @Query(value = "select * from print_template where com_id = ?1", nativeQuery = true)
    List<PrintTemplate> findAllByComId(Integer companyId);

    @Query(value = "select * from print_template where id = ?1 and com_id = ?2", nativeQuery = true)
    Optional<PrintTemplate> findByIdAndComId(Integer id, Integer companyId);

    List<PrintTemplate> findByComIdAndIdIn(Integer companyId, Set<Integer> ids);
    List<PrintTemplate> findByComIdAndTypeTemplate(Integer comId, Integer typeTemplate);
    List<PrintTemplate> findByComIdAndTypeTemplateAndIdIn(Integer comId, Integer typeTemplate, List<Integer> ids);

    @Query(value = "select * from print_template where type_template = ?1 and is_default = 1", nativeQuery = true)
    PrintTemplate findDefaultByCode(Integer type);

    @Query(value = "select * from print_template where com_id is null and is_default = 1", nativeQuery = true)
    List<PrintTemplate> getAllDefaultTemplate();

    @Query(value = "select * from print_template where code = ?1 and is_default = 1", nativeQuery = true)
    PrintTemplate findDefaultByCode(String code);
}
