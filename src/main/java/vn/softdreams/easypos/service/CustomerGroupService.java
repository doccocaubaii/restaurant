package vn.softdreams.easypos.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.domain.CustomerGroup;

import java.util.Optional;

/**
 * Service Interface for managing {@link CustomerGroup}.
 */
public interface CustomerGroupService {
    /**
     * Save a customerGroup.
     *
     * @param customerGroup the entity to save.
     * @return the persisted entity.
     */
    CustomerGroup save(CustomerGroup customerGroup);

    /**
     * Updates a customerGroup.
     *
     * @param customerGroup the entity to update.
     * @return the persisted entity.
     */
    CustomerGroup update(CustomerGroup customerGroup);

    /**
     * Partially updates a customerGroup.
     *
     * @param customerGroup the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CustomerGroup> partialUpdate(CustomerGroup customerGroup);

    /**
     * Get all the customerGroups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CustomerGroup> findAll(Pageable pageable);

    /**
     * Get the "id" customerGroup.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CustomerGroup> findOne(Integer id);

    /**
     * Delete the "id" customerGroup.
     *
     * @param id the id of the entity.
     */
    void delete(Integer id);
}
