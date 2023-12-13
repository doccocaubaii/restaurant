package vn.softdreams.easypos.service;

import java.util.List;
import java.util.Optional;
import vn.softdreams.easypos.domain.BillPayment;

/**
 * Service Interface for managing {@link BillPayment}.
 */
public interface BillPaymentService {
    /**
     * Save a billPayment.
     *
     * @param billPayment the entity to save.
     * @return the persisted entity.
     */
    BillPayment save(BillPayment billPayment);

    /**
     * Updates a billPayment.
     *
     * @param billPayment the entity to update.
     * @return the persisted entity.
     */
    BillPayment update(BillPayment billPayment);

    /**
     * Partially updates a billPayment.
     *
     * @param billPayment the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BillPayment> partialUpdate(BillPayment billPayment);

    /**
     * Get all the billPayments.
     *
     * @return the list of entities.
     */
    List<BillPayment> findAll();

    /**
     * Get the "id" billPayment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BillPayment> findOne(Long id);

    /**
     * Delete the "id" billPayment.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
