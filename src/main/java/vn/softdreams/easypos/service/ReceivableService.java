package vn.softdreams.easypos.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.domain.Bill;
import vn.softdreams.easypos.domain.Receivable;
import vn.softdreams.easypos.domain.User;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Service Interface for managing {@link Receivable}.
 */
public interface ReceivableService {
    /**
     * Save a receivable.
     *
     * @param receivable the entity to save.
     * @return the persisted entity.
     */
    Receivable save(Receivable receivable);

    /**
     * Updates a receivable.
     *
     * @param receivable the entity to update.
     * @return the persisted entity.
     */
    Receivable update(Receivable receivable);

    /**
     * Partially updates a receivable.
     *
     * @param receivable the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Receivable> partialUpdate(Receivable receivable);

    /**
     * Get all the receivables.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Receivable> findAll(Pageable pageable);

    /**
     * Get the "id" receivable.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Receivable> findOne(Integer id);

    /**
     * Delete the "id" receivable.
     *
     * @param id the id of the entity.
     */
    void delete(Integer id);
    void saveReceivable(Integer comId, Bill bill, BigDecimal amount, User user);
}
