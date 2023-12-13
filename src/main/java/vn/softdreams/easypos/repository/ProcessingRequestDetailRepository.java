package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.ProcessingRequestDetail;
import vn.softdreams.easypos.dto.processingRequest.BillProductRelation;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
@Repository
public interface ProcessingRequestDetailRepository extends JpaRepository<ProcessingRequestDetail, Integer> {
    //    Optional<ProcessingRequestDetail> findByBillIdAndProductProductUnitIdAndIsTopping(
    //        Integer billId,
    //        Integer productProductUnitId,
    //        Boolean isTopping
    //    );
    //    List<ProcessingRequestDetail> findByBillIdAndProductProductUnitIdInAndIsTopping(
    //        Integer billId,
    //        List<Integer> productProductUnitId,
    //        Boolean isTopping
    //    );

    @Query(
        value = "select distinct prd.* from processing_product pp join processing_request_detail prd on pp.request_detail_id = prd.id where prd.bill_id = ?1 and pp.bill_product_id in ?2",
        nativeQuery = true
    )
    List<ProcessingRequestDetail> findForDeleteNoToppingProduct(Integer billId, List<Integer> ids);

    @Query(
        value = "select distinct prd.* from processing_product pp join processing_request_detail prd on pp.request_detail_id = prd.id where prd.bill_id = ?1 and pp.bill_product_id in ?2 order by prd.create_time desc",
        nativeQuery = true
    )
    List<ProcessingRequestDetail> findForDeleteNoToppingProductOrderBy(Integer billId, List<Integer> ids);

    @Query(
        value = "select distinct prd.* from processing_product pp join processing_product pp2 on pp.ref_id = pp2.id join processing_request_detail prd on pp2.request_detail_id = prd.id or pp.request_detail_id = prd.id where pp.bill_id = ?1  and pp2.product_product_unit_id in ?2 and pp2.ref_id is null",
        nativeQuery = true
    )
    List<ProcessingRequestDetail> findForDeleteToppingProduct(Integer billId, List<Integer> ids);

    @Query(
        value = "select distinct prd.* from processing_product pp join processing_product pp2 on pp.ref_id = pp2.id join processing_request_detail prd on pp.request_detail_id = prd.id where pp.bill_id = ?1 and pp2.product_product_unit_id = ?2 and pp2.ref_id is null",
        nativeQuery = true
    )
    Optional<ProcessingRequestDetail> findForChangeToppingProduct(Integer billId, Integer id);

    @Query(
        value = "select distinct prd.* from processing_product pp join processing_product pp2 on pp.ref_id = pp2.id join processing_request_detail prd on pp.request_detail_id = prd.id where pp.bill_id = ?1 and pp2.product_product_unit_id in ?2 and pp2.ref_id is null",
        nativeQuery = true
    )
    List<ProcessingRequestDetail> findForDeleteOnlyTopping(Integer billId, List<Integer> ids);

    @Query(
        value = "select prd.id requestDetailId, bp.id billProductId, pp.id " +
        "from processing_request_detail prd join processing_product pp on pp.request_detail_id = prd.id join bill_product bp on prd.product_product_unit_id = bp.product_product_unit_id and (prd.position = bp.position or prd.position - ?2 = bp.position) and prd.bill_id = bp.bill_id " +
        "where prd.bill_id = ?1",
        nativeQuery = true
    )
    List<BillProductRelation> getBillProductId(Integer billId, int positionSub);

    @Modifying
    @Query(value = "delete pp from processing_request_detail pp where pp.bill_id = ?1 and pp.is_topping = 1", nativeQuery = true)
    void deleteTopping(Integer billId);

    List<ProcessingRequestDetail> findByBillIdAndIsTopping(Integer billId, Boolean isTopping);
}
