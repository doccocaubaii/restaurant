package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.Voucher;
import vn.softdreams.easypos.dto.product.ProductImagesResult;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Voucher entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Integer>, VoucherRepositoryCustom {
    @Query(
        value = "select v.id from voucher v join voucher_company vc on vc.voucher_id = v.id where vc.com_id = ?1 and v.id = ?2 and v.status in ?3",
        nativeQuery = true
    )
    Integer checkVoucherById(Integer comId, Integer id, List<Integer> status);

    @Query(
        value = "select v.* from voucher v join voucher_company vc on vc.voucher_id = v.id where v.id = ?1 and vc.com_id = ?2 and v.status != ?3",
        nativeQuery = true
    )
    Optional<Voucher> findOneByIdIgnoreStatus(Integer id, Integer companyId, Integer status);

    @Query(
        value = "select v.* from voucher v join voucher_company vc on v.id = vc.voucher_id where com_id = ?1 and v.id in ?2",
        nativeQuery = true
    )
    List<Voucher> findAllByComIdAndIdIn(Integer comId, List<Integer> ids);

    @Query(
        value = "select distinct v.id, v.code as image from voucher v join voucher_company vc on v.id = vc.voucher_id join voucher_apply va on v.id = va.voucher_id where va.com_id = ?1 and v.id in ?2 and customer_id = ?3",
        nativeQuery = true
    )
    List<ProductImagesResult> findAllByComIdAndIdInAndCustomerId(Integer comId, List<Integer> ids, Integer customerId);

    @Query(
        value = "select v.* from voucher v join voucher_company vc on vc.voucher_id = v.id where vc.com_id = ?1 order by v.update_time desc",
        nativeQuery = true
    )
    Optional<Voucher> getTopByByComId(Integer companyId);
}
