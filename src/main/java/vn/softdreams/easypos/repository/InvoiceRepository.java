package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.Invoice;
import vn.softdreams.easypos.dto.invoice.CheckInvoiceTaskLogResponse;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Invoice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer>, InvoiceRepositoryCustom {
    @Query(value = "Select count(*) from invoice where bill_id = ?1", nativeQuery = true)
    Integer countInvoiceByBillId(Integer id);

    @Query(value = "Select count(*) from invoice where id = ?1 and company_id = ?2", nativeQuery = true)
    Integer countInvoiceByIdAndCompanyID(Integer id, Integer companyId);

    @Query(
        "SELECT i FROM Invoice i JOIN FETCH i.invoiceProducts p WHERE i.id = ?1 AND i.companyId = ?2 AND i.taxAuthorityCode != ?3 ORDER BY p.position ASC "
    )
    Optional<Invoice> findByIdAndCompanyId(Integer id, Integer comId, String taxAuthorityCode);

    Optional<Invoice> findOneByBillId(Integer billId);

    Optional<Invoice> findOneByIdAndCompanyId(Integer invoiceId, Integer comId);

    @Query(value = "select status from invoice where company_id = ?1 and bill_id = ?2", nativeQuery = true)
    Integer getStatusByBillId(Integer comId, Integer billId);

    @Query(value = "SELECT company_id id, ikey FROM Invoice where ikey in ?1", nativeQuery = true)
    List<CheckInvoiceTaskLogResponse> findComAndIkeyByIkey(List<String> ikeys);
}
