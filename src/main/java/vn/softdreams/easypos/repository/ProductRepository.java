package vn.softdreams.easypos.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.Product;
import vn.softdreams.easypos.dto.product.ProductImagesResult;
import vn.softdreams.easypos.dto.queue.ObjectAsyncResponse;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Spring Data JPA repository for the Product entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>, ProductRepositoryCustom {
    Integer countAllByIdAndComId(Integer id, Integer comId);

    Optional<Product> findByIdAndComIdAndActive(Integer id, Integer comId, Boolean active);

    Optional<Product> findByIdAndComId(Integer id, Integer comId);

    List<Product> findByComIdAndIdIn(Integer comId, Set<Integer> ids);
    Integer countByComIdAndCodeAndActive(Integer comId, String code, Boolean active);

    Integer countByComIdAndCode2AndActiveTrue(Integer comId, String code2);

    Integer countByComIdAndBarCodeAndActiveTrue(Integer companyId, String barcode);

    @Query(
        value = "select count(*) " +
        "from product_product_unit ppu join product p on p.id = ppu.product_id " +
        "where p.com_id = ?1 and active = 1 and ppu.bar_code in ?2",
        nativeQuery = true
    )
    Integer countByComIdAndBarCodeInAndActiveTrue(Integer companyId, List<String> barcodes);

    @Query(nativeQuery = true, value = "select in_price from product where id = ?1")
    Optional<BigDecimal> findInPriceByProductId(Integer id);

    @Query(value = "select top(1) * from product where com_id = ?1 and active = 1 order by update_time desc", nativeQuery = true)
    Optional<Product> findOneByComId(Integer comId);

    @Query(
        value = "select distinct p.id, p.image from bill_product bp left join product p on bp.product_id = p.id where bp.bill_id = ?1 and p.image is not null",
        nativeQuery = true
    )
    List<ProductImagesResult> findImagesByBillId(Integer id);

    List<Product> findAllByComId(Integer comId);

    @Query(value = "select id, name, com_id comId, null type from product where com_id in ?1 and active = 1", nativeQuery = true)
    List<ObjectAsyncResponse> findAllByComIds(List<Integer> ids);

    @Query(value = "select p.* from product p where p.com_id = ?1 and p.code in ?2 and active = 1 ", nativeQuery = true)
    LinkedList<Product> findAllIdsByProductCodes(Integer comId, List<String> productCode);

    @Query(value = "select p.unit_id from product p where p.com_id = ?1 and p.id in ?2 and active = 1 ", nativeQuery = true)
    List<Integer> findAllUnitIdsByProductIds(Integer comId, List<Integer> productIds);

    List<Product> findAllByComIdAndIdInAndIsToppingTrueAndActiveTrue(Integer comId, Set<Integer> ids);
    List<Product> findAllByComIdAndIsToppingTrueAndActiveTrue(Integer comId, Pageable pageable);

    @Query(value = "select count(*) from product where active = 1 and id in ?1", nativeQuery = true)
    Integer countAllByStatusAndIdIn(Set<Integer> ids);

    @Query(nativeQuery = true, value = "select bar_code from product where com_id = ?1 and bar_code is not null and active = 1")
    List<String> getAllBarCodeByComId(Integer companyId);

    @Query(nativeQuery = true, value = "select lower(code2) from product where com_id = ?1 and code2 is not null and active = 1")
    List<String> getAllCode2ByComId(Integer companyId);

    @Query(
        value = "select count(*) " +
        "from bill b join bill_product bp on b.id = bp.bill_id " +
        "join product p on bp.product_id = p.id " +
        "where b.status = 0 and product_id = ?1",
        nativeQuery = true
    )
    Integer countByStatusAndProductId(Integer productId);

    List<Product> findAllByComIdAndIdInAndActiveAndInventoryTracking(
        Integer comId,
        List<Integer> ids,
        Boolean active,
        Boolean inventoryTracking
    );

    List<Product> findAllByComIdAndCodeInAndActiveAndInventoryTracking(
        Integer comId,
        List<String> codes,
        Boolean active,
        Boolean inventoryTracking
    );

    List<Product> findAllByComIdAndIdIn(Integer comId, List<Integer> ids);

    @Query(value = "select image from product where image like 'http://%' or image like 'https://%' ", nativeQuery = true)
    List<String> getAllOldImage();
}
