package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.VoucherCompany;

import java.util.Optional;

/**
 * Spring Data JPA repository for the VoucherCompany entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VoucherCompanyRepository extends JpaRepository<VoucherCompany, Integer> {
    @Query(
        value = "select count(*) from voucher_company vc join voucher v on v.id = vc.voucher_id where vc.com_id = ?1 and lower(v.code) = ?2 and v.status != ?3",
        nativeQuery = true
    )
    Integer checkCodeDuplicate(Integer comId, String code, Integer status);

    Optional<VoucherCompany> findOneByVoucherIdAndComId(Integer voucherId, Integer comId);

    Optional<VoucherCompany> findFirstByVoucherIdAndComIdOrderByUpdateTime(Integer id, Integer comId);
}
