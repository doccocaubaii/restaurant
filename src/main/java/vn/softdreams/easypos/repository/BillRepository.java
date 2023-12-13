package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.Bill;
import vn.softdreams.easypos.dto.bill.BillCodeResult;
import vn.softdreams.easypos.dto.bill.BillProductCheckQuantity;
import vn.softdreams.easypos.dto.bill.BillStatsResult;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Bill entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BillRepository extends JpaRepository<Bill, Integer>, BillRepositoryCustom {
    @Query(
        value = "select count (*) from bill " +
        "join area_unit au on bill.area_unit_id = au.id " +
        "where au.com_id = ?1 and status = ?2 and delivery_type = ?3 and au.area_id = ?4  ",
        nativeQuery = true
    )
    Integer countByStatusAndDeliveryTypeAndAreaId(Integer comId, Integer status, Integer deliveryType, Integer areaId);

    @Query(
        value = "select count (*) from bill where com_id = ?1 and status = ?2 and delivery_type = ?3 and area_unit_id = ?4 ",
        nativeQuery = true
    )
    Integer countByStatusAndDeliveryTypeAndAreaUnitId(Integer comId, Integer status, Integer deliveryType, Integer areaUnitId);

    Optional<Bill> findByComIdAndCode(Integer id, String code);

    Optional<Bill> findByIdAndComId(Integer id, Integer companyId);

    Optional<Bill> findByIdAndComIdAndStatus(Integer id, Integer companyId, Integer status);

    @Query(
        value = "select sum(iif(b.status = 0, 1, 0)) processingCount, " +
        "count(b.id) allCount from bill b " +
        "where b.com_id = ?1 and b.bill_date between ?2 and ?3",
        nativeQuery = true
    )
    BillStatsResult countByComIdAndStatus(Integer comId, String fromDate, String toDate);

    Integer countAllByComIdAndTaxAuthorityCode(Integer companyId, String taxAuthorityCode);
    Integer countAllByComIdAndTaxAuthorityCodeAndIdNot(Integer companyId, String taxAuthorityCode, Integer id);

    @Query(value = "select count (*) from bill where com_id = ?1 and tax_authority_code in ?2 ", nativeQuery = true)
    Integer countAllByComIdAndTaxAuthorityCodes(Integer companyId, List<String> taxAuthorityCodes);

    @Query(value = "select id, code billCode, type_inv typeInv from bill where id = ?1 and com_id = ?2 ", nativeQuery = true)
    BillCodeResult findBillCodeByIdAndComId(Integer billId, Integer comId);

    List<Bill> findByComIdAndAreaUnitIdIn(Integer comId, List<Integer> areaUnitIds);

    Integer countByIdAndComId(Integer id, Integer comId);

    Integer countAllByComIdAndStatusAndCustomerId(Integer comId, Integer status, Integer customerId);

    @Query(
        value = "select * from bill b where id not in (select bill_id from mc_receipt where bill_id is not null) and status = 1",
        nativeQuery = true
    )
    List<Bill> getBillNotInMcReceipt();

    @Query(value = "select count(*) from bill where id in ?1 and status = ?2", nativeQuery = true)
    Integer countStatusBillNotCompleted(List<Integer> billIds, Integer status);

    @Query(
        value = "select bp.product_product_unit_id productProductUnitId, sum(bp.quantity) totalQuantity " +
        "from bill b join bill_product bp on b.id = bp.bill_id " +
        "where b.id in ?1 group by bp.product_product_unit_id ",
        nativeQuery = true
    )
    List<BillProductCheckQuantity> checkQuantityOldReturn(List<String> ids);

    Integer countAllByComIdAndStatusAndDiscountVatRateIsNotNull(Integer comId, Integer status);

    @Query(
        value = "select count(*) " +
        "from bill b join bill_product bp on b.id = bp.bill_id " +
        "join product_product_unit ppu on bp.product_product_unit_id = ppu.id " +
        "where b.com_id = ?1 and b.status = 0 and ppu.product_unit_id = ?2 and ppu.unit_name = ?3",
        nativeQuery = true
    )
    Integer countAllForDeleteProductUnit(Integer comId, Integer unitId, String unitName);
}
