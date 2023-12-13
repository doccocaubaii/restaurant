package vn.softdreams.easypos.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.domain.SupplierGroup;

import java.util.Optional;

/**
 * Service Interface for managing {@link SupplierGroup}.
 */
public interface SupplierGroupService {
    /**
     * Save a supplierGroup.
     *
     * @param supplierGroup the entity to save.
     * @return the persisted entity.
     */
    SupplierGroup save(SupplierGroup supplierGroup);

    /**
     * Updates a supplierGroup.
     *
     * @param supplierGroup the entity to update.
     * @return the persisted entity.
     */
    SupplierGroup update(SupplierGroup supplierGroup);

    /**
     * Partially updates a supplierGroup.
     *
     * @param supplierGroup the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SupplierGroup> partialUpdate(SupplierGroup supplierGroup);

    /**
     * Get all the supplierGroups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SupplierGroup> findAll(Pageable pageable);

    /**
     * Get the "id" supplierGroup.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SupplierGroup> findOne(Integer id);

    /**
     * Delete the "id" supplierGroup.
     *
     * @param id the id of the entity.
     */
    void delete(Integer id);
}
