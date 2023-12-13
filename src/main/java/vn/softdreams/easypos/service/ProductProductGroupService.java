package vn.softdreams.easypos.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.domain.ProductProductGroup;

import java.util.Optional;

/**
 * Service Interface for managing {@link ProductProductGroup}.
 */
public interface ProductProductGroupService {
    /**
     * Save a productProductGroup.
     *
     * @param productProductGroup the entity to save.
     * @return the persisted entity.
     */
    ProductProductGroup save(ProductProductGroup productProductGroup);

    /**
     * Updates a productProductGroup.
     *
     * @param productProductGroup the entity to update.
     * @return the persisted entity.
     */
    ProductProductGroup update(ProductProductGroup productProductGroup);

    /**
     * Partially updates a productProductGroup.
     *
     * @param productProductGroup the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProductProductGroup> partialUpdate(ProductProductGroup productProductGroup);

    /**
     * Get all the productProductGroups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProductProductGroup> findAll(Pageable pageable);

    /**
     * Get the "id" productProductGroup.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProductProductGroup> findOne(Integer id);

    /**
     * Delete the "id" productProductGroup.
     *
     * @param id the id of the entity.
     */
    void delete(Integer id);
}
