package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.ProcessingArea;
import vn.softdreams.easypos.dto.processingArea.ProductSetting;
import vn.softdreams.easypos.dto.productGroup.ProductProcessingAreaResult;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Spring Data JPA repository for the ProcessingArea entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProcessingAreaRepository extends JpaRepository<ProcessingArea, Integer> {
    List<ProcessingArea> findByComIdAndIdIn(Integer comId, List<Integer> ids);
    Optional<ProcessingArea> findByComIdAndId(Integer comId, Integer id);

    @Query(
        value = "select pa.id, pa.name " +
        "from processing_area_product pap join product_product_unit ppu on pap.product_product_unit_id = ppu.id " +
        "join processing_area pa on pap.processing_area_id = pa.id " +
        "join product p on ppu.product_id = p.id " +
        "where pa.com_id = ?1 and p.active = 1 and ppu.is_primary = 1 and p.id = ?2",
        nativeQuery = true
    )
    List<ProductProcessingAreaResult> findForProductDetail(Integer comId, Integer productId);

    @Query(
        value = "select pa.id, pa.name, ppu.id productProductUnitId " +
        "from processing_area_product pap join product_product_unit ppu on pap.product_product_unit_id = ppu.id " +
        "join processing_area pa on pap.processing_area_id = pa.id " +
        "where pa.com_id = ?1 and ppu.id in ?2 ",
        nativeQuery = true
    )
    List<ProductProcessingAreaResult> findForProductResult(Integer comId, Set<Integer> productId);

    @Query(
        value = "select ppu.id, pa.setting " +
        "from processing_area_product pap join product_product_unit ppu on pap.product_product_unit_id = ppu.id " +
        "join product p on ppu.product_id = p.id " +
        "join processing_area pa  on pap.processing_area_id = pa.id " +
        "where p.com_id = ?1 and ppu.id in ?2 and p.active = 1",
        nativeQuery = true
    )
    List<ProductSetting> getProductSetting(Integer comId, List<Integer> productIds);

    Integer countAllByComIdAndNameAndActiveIsNot(Integer comId, String name, Integer active);
    Integer countAllByComIdAndNameAndActiveIsNotAndIdNot(Integer comId, String name, Integer active, Integer id);
    Optional<ProcessingArea> findByIdAndComId(Integer id, Integer comId);
    List<ProcessingArea> findByComIdAndIdIn(Integer comId, Set<Integer> ids);
}
