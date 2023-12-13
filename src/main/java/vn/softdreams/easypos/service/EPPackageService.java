package vn.softdreams.easypos.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.domain.EPPackage;

import java.util.Optional;

/**
 * Service Interface for managing {@link EPPackage}.
 */
public interface EPPackageService {
    /**
     * Save a ePPackage.
     *
     * @param ePPackage the entity to save.
     * @return the persisted entity.
     */
    EPPackage save(EPPackage ePPackage);

    /**
     * Updates a ePPackage.
     *
     * @param ePPackage the entity to update.
     * @return the persisted entity.
     */
    EPPackage update(EPPackage ePPackage);

    /**
     * Partially updates a ePPackage.
     *
     * @param ePPackage the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EPPackage> partialUpdate(EPPackage ePPackage);

    /**
     * Get all the ePPackages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EPPackage> findAll(Pageable pageable);

    /**
     * Get the "id" ePPackage.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EPPackage> findOne(Integer id);

    /**
     * Delete the "id" ePPackage.
     *
     * @param id the id of the entity.
     */
    void delete(Integer id);
}
