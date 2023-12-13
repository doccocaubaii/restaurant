package vn.softdreams.easypos.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.domain.SupplierSupplierGroup;

import java.util.Optional;

/**
 * Service Interface for managing {@link SupplierSupplierGroup}.
 */
public interface SupplierSupplierGroupService {
    /**
     * Save a supplierSupplierGroup.
     *
     * @param supplierSupplierGroup the entity to save.
     * @return the persisted entity.
     */
    SupplierSupplierGroup save(SupplierSupplierGroup supplierSupplierGroup);

    /**
     * Updates a supplierSupplierGroup.
     *
     * @param supplierSupplierGroup the entity to update.
     * @return the persisted entity.
     */
    SupplierSupplierGroup update(SupplierSupplierGroup supplierSupplierGroup);

    /**
     * Partially updates a supplierSupplierGroup.
     *
     * @param supplierSupplierGroup the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SupplierSupplierGroup> partialUpdate(SupplierSupplierGroup supplierSupplierGroup);

    /**
     * Get all the supplierSupplierGroups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SupplierSupplierGroup> findAll(Pageable pageable);

    /**
     * Get the "id" supplierSupplierGroup.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SupplierSupplierGroup> findOne(Integer id);

    /**
     * Delete the "id" supplierSupplierGroup.
     *
     * @param id the id of the entity.
     */
    void delete(Integer id);
}
