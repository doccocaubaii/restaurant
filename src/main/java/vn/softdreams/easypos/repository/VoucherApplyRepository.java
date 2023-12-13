package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.VoucherApply;
import vn.softdreams.easypos.dto.voucher.VoucherApplyCardItem;
import vn.softdreams.easypos.dto.voucher.VoucherApplyCustomerItem;
import vn.softdreams.easypos.dto.voucher.VoucherApplyTypeItem;

import java.util.List;

/**
 * Spring Data JPA repository for the VoucherApply entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VoucherApplyRepository extends JpaRepository<VoucherApply, Integer> {
    @Query(
        value = "select distinct va.voucher_id voucherId, va.apply_type applyType from voucher_apply va where va.voucher_id in ?1 and va.apply_type is not null",
        nativeQuery = true
    )
    List<VoucherApplyTypeItem> getAllApplyTypeByVoucherId(List<Integer> voucherIds);

    @Query(value = "select voucher_id from voucher_apply where com_id = ?1 and apply_type = 1 and apply_id = ?2", nativeQuery = true)
    List<Integer> getVoucherIdByComIdAndApplyId(Integer comId, Integer applyId);

    List<VoucherApply> findAllByComIdAndApplyIdAndApplyType(Integer comId, Integer applyId, Integer applyType);

    List<VoucherApply> getAllByComIdAndApplyType(Integer comId, Integer applyType);

    @Query(
        value = "select va.id id, va.voucher_id voucherId, va.customer_id customerId " +
        "from voucher_apply va join voucher v on v.id = va.voucher_id " +
        "where va.com_id = ?1 and va.apply_type = ?2 and v.status != -1 and va.voucher_id = ?3 and va.customer_id in ?4 ",
        nativeQuery = true
    )
    List<VoucherApplyCustomerItem> getAllVoucherApplyByVoucherIdAndCustomer(
        Integer comId,
        Integer applyType,
        Integer voucherId,
        List<Integer> customerId
    );

    @Query(
        value = "select va.* from voucher_apply va where va.com_id = ?1 and va.voucher_id = ?4 and va.apply_type = ?2 and va.customer_id in ?3 ",
        nativeQuery = true
    )
    List<VoucherApply> getAllVoucherApplyByCustomer(Integer comId, Integer applyType, List<Integer> customerIds, Integer voucherId);

    @Query(
        value = "select va.* from voucher_apply va where va.com_id = ?1 and va.voucher_id = ?4 and va.apply_type = ?2 and va.apply_id in ?3 ",
        nativeQuery = true
    )
    List<VoucherApply> getAllVoucherApplyByCard(Integer comId, Integer applyType, List<Integer> cardIds, Integer voucherId);

    @Query(
        value = "select va.id id, va.voucher_id voucherId, va.apply_id cardId " +
        "from voucher_apply va join voucher v on v.id = va.voucher_id " +
        "where va.com_id = ?1 and va.apply_type = ?2 and v.status != -1 and va.voucher_id = ?3 and va.apply_id in ?4 ",
        nativeQuery = true
    )
    List<VoucherApplyCardItem> getAllVoucherApplyByVoucherIdAndCard(
        Integer comId,
        Integer applyType,
        Integer voucherId,
        List<Integer> cardIds
    );

    List<VoucherApply> getAllByComIdAndIdInAndApplyType(Integer comId, List<Integer> ids, Integer applyType);
    List<VoucherApply> getAllByComIdAndApplyIdInAndApplyType(Integer comId, List<Integer> applyIds, Integer applyType);

    List<VoucherApply> getAllByComIdAndIdIn(Integer comId, List<Integer> applyIds);

    @Query(
        value = "select va.* from voucher_apply va where va.com_id = ?1 and va.apply_type = ?2 and va.customer_id not in ?3 and va.apply_id is null",
        nativeQuery = true
    )
    List<VoucherApply> getAllVoucherApplyCustomerAndIdNotIn(Integer comId, Integer applyType, List<Integer> customerIds);

    @Query(
        value = "select va.* from voucher_apply va where va.com_id = ?1 and va.apply_type = ?2 and va.apply_id not in ?3 ",
        nativeQuery = true
    )
    List<VoucherApply> getAllVoucherApplyCardAndIdNotIn(Integer comId, Integer applyType, List<Integer> cardIds);

    @Query(
        value = "select va.apply_id from voucher_apply va where va.com_id = ?1 and va.apply_type = ?3 and va.apply_id in ?2 ",
        nativeQuery = true
    )
    List<Integer> getAllIdsByComIdAndApplyIdInAndApplyTypeCard(Integer comId, List<Integer> applyIds, Integer applyType);

    @Query(
        value = "select va.apply_id from voucher_apply va where va.com_id = ?1 and va.apply_type = ?3 and va.customer_id in ?2 and va.apply_id is null ",
        nativeQuery = true
    )
    List<Integer> getAllIdsByComIdAndApplyIdInAndApplyTypeCustomer(Integer comId, List<Integer> applyIds, Integer applyType);
}
