package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.ProcessingAreaProduct;
import vn.softdreams.easypos.dto.productGroup.ProductProcessingAreaDetail;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the ProcessingAreaProduct entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProcessingAreaProductRepository extends JpaRepository<ProcessingAreaProduct, Integer> {
    Optional<ProcessingAreaProduct> findByComIdAndProductProductUnitId(Integer comId, Integer productProductUnitId);
    List<ProcessingAreaProduct> findAllByComIdIsNull();

    @Query(
        value = "select pa.id, pa.name, pap.product_product_unit_id productProductUnitId from processing_area_product pap join processing_area pa on pap.processing_area_id = pa.id where pap.com_id = ?1 and product_product_unit_id in ?2",
        nativeQuery = true
    )
    List<ProductProcessingAreaDetail> findForProductDetail(Integer comId, List<Integer> ids);

    @Modifying
    @Query(
        value = "delete pap from processing_area_product pap join product_product_unit ppu on pap.product_product_unit_id = ppu.id where pap.com_id = ?1 and product_id = ?2 and ppu.is_primary = 0",
        nativeQuery = true
    )
    void deleteBeforeUpdateProduct(Integer comId, Integer productId);

    @Query(
        nativeQuery = true,
        value = "SELECT * FROM processing_area_product " +
        "WHERE processing_area_product.com_id = :processingAreaProductComId " +
        "AND processing_area_product.product_product_unit_id = :processingAreaProductProductProductUnitId "
    )
    ProcessingAreaProduct getProcessingAreaProductId(
        @Param("processingAreaProductComId") Integer processingAreaProductComId,
        @Param("processingAreaProductProductProductUnitId") Integer processingAreaProductProductProductUnitId
    );

    @Query(
        nativeQuery = true,
        value = "SELECT * FROM processing_area_product " +
        "WHERE processing_area_product.com_id = :comId " +
        "AND processing_area_product.processing_area_id = :processingAreaId "
    )
    List<ProcessingAreaProduct> getProcessingAreaProductByProcessingAreaId(
        @Param("comId") Integer comId,
        @Param("processingAreaId") Integer processingAreaId
    );

    Optional<ProcessingAreaProduct> findByIdAndComId(Integer id, Integer comId);

    List<ProcessingAreaProduct> findByProcessingAreaIdAndComIdIsNull(Integer id);

    List<ProcessingAreaProduct> findByComIdAndProductProductUnitIdIn(Integer comId, List<Integer> ids);
}
