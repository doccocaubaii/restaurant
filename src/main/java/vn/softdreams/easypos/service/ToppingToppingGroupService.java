package vn.softdreams.easypos.service.auto_service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.domain.ToppingToppingGroup;

import java.util.Optional;

/**
 * Service Interface for managing {@link ToppingToppingGroup}.
 */
public interface ToppingToppingGroupService {
    /**
     * Save a toppingToppingGroup.
     *
     * @param toppingToppingGroup the entity to save.
     * @return the persisted entity.
     */
    ToppingToppingGroup save(ToppingToppingGroup toppingToppingGroup);

    /**
     * Updates a toppingToppingGroup.
     *
     * @param toppingToppingGroup the entity to update.
     * @return the persisted entity.
     */
    ToppingToppingGroup update(ToppingToppingGroup toppingToppingGroup);

    /**
     * Partially updates a toppingToppingGroup.
     *
     * @param toppingToppingGroup the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ToppingToppingGroup> partialUpdate(ToppingToppingGroup toppingToppingGroup);

    /**
     * Get all the toppingToppingGroups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ToppingToppingGroup> findAll(Pageable pageable);

    /**
     * Get the "id" toppingToppingGroup.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ToppingToppingGroup> findOne(Integer id);

    /**
     * Delete the "id" toppingToppingGroup.
     *
     * @param id the id of the entity.
     */
    void delete(Integer id);
}
