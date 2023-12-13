package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.ProcessingProduct;
import vn.softdreams.easypos.dto.processingRequest.ToppingNumber;
import vn.softdreams.easypos.dto.product.ProcessingProductItem;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
@Repository
public interface ProcessingProductRepository extends JpaRepository<ProcessingProduct, Integer> {
    List<ProcessingProduct> findAllByBillId(Integer billId);

    @Query(
        value = "select pp.* from processing_request_detail prd join processing_product pp on prd.id = pp.request_detail_id where prd.area_unit_id = ?1 and pp.status = 'PROCESSING'",
        nativeQuery = true
    )
    List<ProcessingProduct> findAllByAreaUnitId(Integer areaUnitId);

    @Modifying
    @Query(value = "delete from processing_product where bill_id = ?1 and product_product_unit_id in ?2", nativeQuery = true)
    void deleteByBillIdAndProductProductUnitIdIn(Integer billId, List<Integer> ids);

    Optional<ProcessingProduct> findByBillIdAndProductProductUnitId(Integer billId, Integer productProductUnitId);

    @Query(
        value = "select top(1) * from processing_product pp where product_product_unit_id = ?1 and pp.processing_quantity is not null and pp.processing_quantity > 0 order by pp.create_time",
        nativeQuery = true
    )
    Optional<ProcessingProduct> getTop1ByProductProductUnitId(Integer id);

    @Query(
        value = "select * from processing_product where product_product_unit_id = ?1 and processing_quantity is not null and processing_quantity > 0",
        nativeQuery = true
    )
    List<ProcessingProduct> findAllByProductProductUnitId(Integer id);

    @Query(
        value = "select distinct pp2.id, pp.request_detail_id requestDetailId, prd.quantity / prd2.quantity number " +
        "from processing_product pp " +
        "    join processing_request_detail prd on pp.request_detail_id = prd.id " +
        "    join processing_request_detail prd2 on prd.processing_request_id = prd2.processing_request_id " +
        "    join processing_product pp2 on prd2.id = pp2.request_detail_id " +
        "where pp.bill_id = ?1 and pp.ref_id is not null and pp.ref_id = 1 and prd2.is_topping = 0 order by pp2.id",
        nativeQuery = true
    )
    List<ToppingNumber> getToppingNumber(Integer billId);

    @Query(value = "select * from processing_product where bill_id = ?1 and ref_id = 1 order by request_detail_id", nativeQuery = true)
    List<ProcessingProduct> findAllByBillIdAndRefId(Integer billId);

    @Query(
        value = "select bp.id, pp.status, count(pp.id) as quantity " +
        "from processing_product pp join bill_product bp on pp.bill_product_id = bp.id " +
        "where bp.bill_id = ?1 " +
        "group by bp.id, pp.status",
        nativeQuery = true
    )
    List<ProcessingProductItem> getForBillDetail(Integer billId);

    @Query(
        value = "select pp.* from processing_product pp join processing_request_detail prd on pp.request_detail_id = prd.id where prd.processing_request_id = ?1 and pp.product_product_unit_id = ?2 and status = ?3 and prd.is_topping = ?4",
        nativeQuery = true
    )
    List<ProcessingProduct> findByRequestDetailIdAndProductIdAndStatus(
        Integer detailId,
        Integer productId,
        String status,
        Boolean isTopping
    );

    @Query(
        value = "select pp.* from processing_product pp join processing_request_detail prd on pp.request_detail_id = prd.id where pp.product_product_unit_id = ?1 and status = ?2 and prd.is_topping = ?3 and prd.quantity is not null and prd.quantity > 0 order by prd.create_time",
        nativeQuery = true
    )
    List<ProcessingProduct> findByProductIdAndStatus(Integer productId, String status, Boolean isTopping);

    @Query(
        value = "select pp.* from processing_request_detail prd join processing_product pp on pp.request_detail_id = prd.id where prd.processing_request_id = ?1 and status = ?2",
        nativeQuery = true
    )
    List<ProcessingProduct> findForDoneWithPagingByTable(Integer detailId, String status);

    @Query(
        value = "select top(1) pp1.* from processing_product pp1 join processing_product pp2 on pp1.id = pp2.ref_id where pp1.product_product_unit_id = ?1 and pp1.status = ?2 order by pp1.create_time",
        nativeQuery = true
    )
    List<ProcessingProduct> findParentForDoneWithPagingByDish(Integer productProductUnitId, String status);

    @Query(
        value = "select pp2.* from processing_product pp1 join processing_product pp2 on pp1.id = pp2.ref_id where pp1.product_product_unit_id = ?1 and pp2.status = ?2 order by pp2.create_time",
        nativeQuery = true
    )
    List<ProcessingProduct> findChildForDoneWithPagingByDish(Integer productProductUnitId, String status);

    @Query(
        value = "with t as (select top (1) pp.*  " +
        "           from processing_product pp  " +
        "                    join processing_product pp2 on pp.id = pp2.ref_id  " +
        "           where pp.bill_id = ?1  " +
        "             and pp.bill_product_id = ?2 and pp.status = 'PROCESSING' order by pp.create_time desc)  " +
        "select pp.* from processing_product pp join t on pp.ref_id = t.id  " +
        "union  " +
        "select * from t",
        nativeQuery = true
    )
    List<ProcessingProduct> findForCancelWithTopping(Integer billId, Integer billProductId);

    Optional<ProcessingProduct> findByBillProductIdAndRefId(Integer billProductId, Integer refId);

    @Query(
        value = "select * from processing_product where bill_product_id = ?1 and ref_id is null order by create_time desc",
        nativeQuery = true
    )
    List<ProcessingProduct> findByBillProductIdAndRefIdIsNull(Integer billProductId);

    List<ProcessingProduct> findByBillProductId(Integer billProductId);

    @Query(
        value = "select distinct pp.ref_id from processing_product pp join bill b on pp.bill_id = b.id where b.com_id = ?1 and pp.ref_id is not null",
        nativeQuery = true
    )
    List<Integer> getAllRefId(Integer comId);

    @Modifying
    @Query(value = "delete pp from processing_product pp where pp.bill_id = ?1 and pp.is_topping = 1", nativeQuery = true)
    void deleteTopping(Integer billId);

    @Query(
        value = "select distinct pp.ref_id from processing_request_detail pp join bill b on pp.bill_id = b.id where b.com_id = ?1 and pp.ref_id is not null",
        nativeQuery = true
    )
    List<Integer> findByRefIdIsNotNull(Integer comId);

    Optional<ProcessingProduct> findByIdAndBillId(Integer id, Integer billId);
    List<ProcessingProduct> findByBillIdAndRefIdIsNull(Integer billId);

    @Query(
        value = "select * from processing_product where product_product_unit_id = ?1 and ref_id is null order by create_time",
        nativeQuery = true
    )
    List<ProcessingProduct> findByProductProductUnitIdAndRefIdOrder(Integer productProductUnitId);

    @Modifying
    @Query(
        value = "delete prd2 from processing_product pp join processing_request_detail prd on pp.request_detail_id = prd.id join processing_request_detail prd2 on prd.id = prd2.ref_id " +
        "where pp.bill_id = ?1 and pp.id = ?2 and prd2.is_topping = 0 and prd2.quantity < 0",
        nativeQuery = true
    )
    void findForDeleteDish(Integer billId, Integer id);

    @Query(value = "select * from processing_product pp where bill_id = ?1 and (id = ?2 or ref_id = ?2)", nativeQuery = true)
    List<ProcessingProduct> findForUpdateWithTopping(Integer comId, Integer id);

    List<ProcessingProduct> findByRefIdAndIsTopping(Integer refId, Boolean isTopping);
    List<ProcessingProduct> findByRefIdInAndIsTopping(List<Integer> refId, Boolean isTopping);
}
