package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.ProcessingRequest;
import vn.softdreams.easypos.dto.processingArea.ProductSetting;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
@Repository
public interface ProcessingRequestRepository extends JpaRepository<ProcessingRequest, Integer> {
    List<ProcessingRequest> findByBillId(Integer billId);

    @Query(
        value = "select distinct pr.* from processing_product pp join processing_product pp2 on pp.ref_id = pp2.id join processing_request_detail prd on pp2.request_detail_id = prd.id join processing_request pr on prd.processing_request_id = pr.id where pp.bill_id = ?1  and pp2.product_product_unit_id = ?2 and pp2.ref_id is null",
        nativeQuery = true
    )
    Optional<ProcessingRequest> findForAddTopping(Integer billId, Integer productProductUnitId);

    @Query(value = "select top(1) * from processing_request where bill_id = ?1 order by create_time desc", nativeQuery = true)
    Optional<ProcessingRequest> findLastByBillId(Integer billId);

    @Query(
        value = "select bp1.position id, bp2.position setting from bill_product bp1 join bill_product bp2 on bp1.parent_id = bp2.id where bp1.bill_id = ?1",
        nativeQuery = true
    )
    List<ProductSetting> getParentPosition(Integer billId);
}
