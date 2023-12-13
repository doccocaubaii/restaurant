package vn.softdreams.easypos.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.domain.CustomerCustomerGroup;

import java.util.Optional;

/**
 * Service Interface for managing {@link CustomerCustomerGroup}.
 */
public interface CustomerCustomerGroupService {
    /**
     * Save a customerCustomerGroup.
     *
     * @param customerCustomerGroup the entity to save.
     * @return the persisted entity.
     */
    CustomerCustomerGroup save(CustomerCustomerGroup customerCustomerGroup);

    /**
     * Updates a customerCustomerGroup.
     *
     * @param customerCustomerGroup the entity to update.
     * @return the persisted entity.
     */
    CustomerCustomerGroup update(CustomerCustomerGroup customerCustomerGroup);

    /**
     * Partially updates a customerCustomerGroup.
     *
     * @param customerCustomerGroup the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CustomerCustomerGroup> partialUpdate(CustomerCustomerGroup customerCustomerGroup);

    /**
     * Get all the customerCustomerGroups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CustomerCustomerGroup> findAll(Pageable pageable);

    /**
     * Get the "id" customerCustomerGroup.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CustomerCustomerGroup> findOne(Integer id);

    /**
     * Delete the "id" customerCustomerGroup.
     *
     * @param id the id of the entity.
     */
    void delete(Integer id);
}
