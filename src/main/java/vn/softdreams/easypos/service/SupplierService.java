package vn.softdreams.easypos.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.domain.Supplier;

import java.util.Optional;

/**
 * Service Interface for managing {@link Supplier}.
 */
public interface SupplierService {
    /**
     * Save a supplier.
     *
     * @param supplier the entity to save.
     * @return the persisted entity.
     */
    Supplier save(Supplier supplier);

    /**
     * Updates a supplier.
     *
     * @param supplier the entity to update.
     * @return the persisted entity.
     */
    Supplier update(Supplier supplier);

    /**
     * Partially updates a supplier.
     *
     * @param supplier the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Supplier> partialUpdate(Supplier supplier);

    /**
     * Get all the suppliers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Supplier> findAll(Pageable pageable);

    /**
     * Get the "id" supplier.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Supplier> findOne(Integer id);

    /**
     * Delete the "id" supplier.
     *
     * @param id the id of the entity.
     */
    void delete(Integer id);
}
