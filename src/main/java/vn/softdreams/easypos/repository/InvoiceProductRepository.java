package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.InvoiceProduct;

/**
 * Spring Data JPA repository for the InvoiceProduct entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceProductRepository extends JpaRepository<InvoiceProduct, Integer> {}
