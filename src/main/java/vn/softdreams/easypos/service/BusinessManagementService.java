package vn.softdreams.easypos.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.domain.Business;
import vn.softdreams.easypos.service.dto.ResultDTO;

import java.util.Optional;

/**
 * Service Interface for managing {@link Business}.
 */
public interface BusinessManagementService {
    /**
     * Save a business.
     *
     * @param business the entity to save.
     * @return the persisted entity.
     */
    Business save(Business business);

    /**
     * Updates a business.
     *
     * @param business the entity to update.
     * @return the persisted entity.
     */
    Business update(Business business);

    /**
     * Partially updates a business.
     *
     * @param business the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Business> partialUpdate(Business business);

    /**
     * Get all the businesses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Business> findAll(Pageable pageable);

    ResultDTO getWithPaging(Pageable pageable, String keyword);

    /**
     * Get the "id" business.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Business> findOne(Integer id);

    /**
     * Delete the "id" business.
     *
     * @param id the id of the entity.
     */
    void delete(Integer id);
}
