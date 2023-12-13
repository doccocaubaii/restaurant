package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.ProductProductUnit;
import vn.softdreams.easypos.dto.product.ProductImagesResult;
import vn.softdreams.easypos.dto.productProductUnit.GetDetailForVoucher;
import vn.softdreams.easypos.dto.productProductUnit.OnHandItem;
import vn.softdreams.easypos.dto.productProductUnit.ProductAndUnitItem;
import vn.softdreams.easypos.dto.productProductUnit.ProductProductUnitItems;
import vn.softdreams.easypos.dto.voucher.VoucherProductItem;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Spring Data JPA repository for the ProductProductUnit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductProductUnitRepository extends JpaRepository<ProductProductUnit, Integer> {
    Optional<ProductProductUnit> findByProductIdAndComIdAndIsPrimaryTrue(Integer productId, Integer comId);

    List<ProductProductUnit> findByProductIdAndComIdAndIsPrimaryFalse(Integer productId, Integer comId);

    Optional<ProductProductUnit> findByProductIdAndComIdAndProductUnitId(Integer productId, Integer comId, Integer unitId);
    Optional<ProductProductUnit> findByProductIdAndComIdAndProductUnitIdAndUnitNameAndIsPrimaryFalse(
        Integer productId,
        Integer comId,
        Integer unitId,
        String unitName
    );

    Optional<ProductProductUnit> findByProductIdAndComIdAndProductUnitIdAndIsPrimaryFalse(Integer productId, Integer comId, Integer unitId);

    Optional<ProductProductUnit> findByIdAndComId(Integer id, Integer comId);

    Optional<ProductProductUnit> findByIdAndComIdAndIsPrimaryFalse(Integer id, Integer comId);

    Optional<ProductProductUnit> findByProductIdAndComIdAndIsPrimaryIsNull(Integer id, Integer comId);
    List<ProductProductUnit> findAllByProductIdAndComId(Integer productId, Integer comId);

    @Query(value = "select product_id from product_product_unit where id = ?1", nativeQuery = true)
    Integer findProductIdById(Integer id);

    List<ProductProductUnit> findAllByComIdAndIsPrimaryIsNotNullOrderByIsPrimaryDesc(Integer comId);

    List<ProductProductUnit> findAllByComIdAndIsPrimaryIsNotNull(Integer comId);

    List<ProductProductUnit> findAllByComIdAndProductIdIn(Integer comId, List<Integer> productIds);

    List<ProductProductUnit> findAllByComIdAndIdIn(Integer comId, List<Integer> ids);

    @Query(
        value = "select ppu.* from product p " +
        "join product_product_unit ppu on p.id = ppu.product_id " +
        "where p.com_id = ?1 and p.inventory_tracking = 1 and p.id in ?2 " +
        "order by ppu.is_primary desc ",
        nativeQuery = true
    )
    List<ProductProductUnit> findAllByComIdAndProductIds(Integer comId, List<Integer> productIds);

    @Query(
        value = "select ppu.* from product_product_unit ppu where ppu.com_id = ?1 and ppu.id in ?2 order by ppu.is_primary desc ",
        nativeQuery = true
    )
    List<ProductProductUnit> findAllByComIdAndIds(Integer comId, List<Integer> ids);

    @Query(
        value = "select p.id productId, p.name productName, p.code productCode, ppu.on_hand onHand, " +
        " p.inventory_tracking inventoryTracking, c.value overStock, ppu.id productProductUnitId, " +
        " ppu.product_unit_id productUnitId, iif(ppu.unit_name is null, '', ' ' + ppu.unit_name) productUnitName " +
        " from product p left join product_product_unit ppu on p.id = ppu.product_id join config c on c.company_id = p.com_id " +
        " where p.com_id = ?1 and p.code in ?2 and active = 1 and c.code = ?3 ",
        nativeQuery = true
    )
    List<ProductProductUnitItems> getProductProductUnitItems(Integer comId, List<String> productCode, String configCode);

    @Query(
        value = "select ppu.* from product_product_unit ppu where ppu.com_id = ?2 and ppu.product_id = ?1 order by ppu.is_primary desc ",
        nativeQuery = true
    )
    List<ProductProductUnit> getAllByProductIdAndComId(Integer productId, Integer comId);

    @Query(
        value = "select ppu.* from product p " +
        "join product_product_unit ppu on p.id = ppu.product_id " +
        "where p.com_id = ?1 and p.id = ?2 and ppu.product_unit_id in ?3 ",
        nativeQuery = true
    )
    List<ProductProductUnit> findByProductIdAndUnitIds(Integer comId, Integer productId, List<Integer> unitIds);

    @Query(
        value = "select ppu.* from product p " +
        "join product_product_unit ppu on p.id = ppu.product_id " +
        "where p.com_id = ?1 and p.inventory_tracking = 1 and p.id in ?2 and ppu.is_primary = 1 ",
        nativeQuery = true
    )
    List<ProductProductUnit> findProductPrimaryByComIdAndProductIds(Integer comId, List<Integer> productIds);

    @Query(
        value = "select count(*) " +
        "from bill b join bill_product bp on b.id = bp.bill_id " +
        "join product p on bp.product_id = p.id " +
        "join product_product_unit ppu on p.id = ppu.product_id " +
        "where b.status = 0 and ppu.id in ?1 " +
        "and (bp.unit = ppu.unit_name or (bp.unit is null and ppu.unit_name is null)) ",
        nativeQuery = true
    )
    Integer countByStatusAndIdIn(List<Integer> ids);

    @Query(
        value = "select id, product_id productId, product_unit_id unitId, unit_name unitName from product_product_unit where id in ?1 ",
        nativeQuery = true
    )
    List<ProductAndUnitItem> getProductIdAndUnitIdByIds(List<Integer> ids);

    @Query(value = "select id id, on_hand onHand from product_product_unit where id in ?1", nativeQuery = true)
    List<OnHandItem> getOnHandById(Set<Integer> ids);

    @Query(
        value = "select ppu.id prodProdId, p.name prodName from product_product_unit ppu join product p on p.id = ppu.product_id where p.com_id = ?1 and p.active = 1",
        nativeQuery = true
    )
    List<VoucherProductItem> findAllIdsByComId(Integer comId);

    @Query(value = "select count(*) from product_product_unit where com_id = ?1 and bar_code = ?2", nativeQuery = true)
    Integer getAllBarcode(Integer comId, String barcode);

    @Query(
        value = "with t as (select product_id from product_product_unit ppu where product_unit_id = ?1 and is_primary = 1) " +
        "select ppu.product_id, name as image from product_product_unit ppu join product p on ppu.product_id = p.id where ppu.product_id in (select * from t) group by ppu.product_id, name having count(*) > 1",
        nativeQuery = true
    )
    List<ProductImagesResult> checkForDeleteProductUnit(Integer unitId);

    List<ProductProductUnit> findAllByComIdAndProductUnitId(Integer comId, Integer unitId);

    @Query(
        nativeQuery = true,
        value = "SELECT product_product_unit.id FROM product_product_unit " +
        "WHERE product_product_unit.com_id = :productProductUnitComId " +
        "AND product_product_unit.product_id = :productProductUnitProductId"
    )
    Integer getProductProductUnitId(
        @Param("productProductUnitComId") Integer productProductUnitComId,
        @Param("productProductUnitProductId") Integer productProductUnitProductId
    );

    @Query(
        value = "select ppu.id, p.name productName, ppu.unit_name unitName, ppu.on_hand onHand, ppu.sale_price salePrice, p.image from product_product_unit ppu join product p on ppu.product_id = p.id where ppu.com_id = ?1 and ppu.id in ?2",
        nativeQuery = true
    )
    List<GetDetailForVoucher> getDetailByIdsForVoucher(Integer comId, List<Integer> ids);
}
