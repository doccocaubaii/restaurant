package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.BillProduct;

import java.util.List;

/**
 * Spring Data JPA repository for the BillProduct entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BillProductRepository extends JpaRepository<BillProduct, Integer> {
    @Query(
        value = "select *  " +
        "from bill_product bp  " +
        "         left join product p on p.id = bp.product_id  " +
        "where p.com_id = ?1 and bp.bill_id = ?2 and bp.product_id in ?3  and p.inventory_tracking = 1 ",
        nativeQuery = true
    )
    List<BillProduct> findAllByIdsAndInventory(Integer comId, Integer billId, List<Integer> ids);

    @Query(value = "select * from bill_product bp where bp.bill_id = ?", nativeQuery = true)
    List<BillProduct> findAllByBillId(Integer billId);

    Integer countAllByProductIdAndUnitIdAndUnit(Integer productId, Integer unitId, String unit);

    Integer countAllByProductProductUnitId(Integer productId);
    Integer countAllByProductIdAndUnitId(Integer productId, Integer unitId);
}
