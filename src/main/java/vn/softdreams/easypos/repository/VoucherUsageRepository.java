package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.VoucherUsage;
import vn.softdreams.easypos.dto.voucher.VoucherUsageResult;

import java.util.List;

/**
 * Spring Data JPA repository for the VoucherUsage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VoucherUsageRepository extends JpaRepository<VoucherUsage, Integer> {
    @Query(value = "select bill_id from voucher_usage vu where com_id = ?1 and voucher_id = ?2 ", nativeQuery = true)
    List<Integer> getBillIdByVoucherId(Integer comId, Integer voucherId);

    List<VoucherUsage> findAllByBillIdAndCustomerId(Integer billId, Integer customerId);

    List<VoucherUsageResult> findAllByVoucherIdIn(List<Integer> voucherIds);
}
