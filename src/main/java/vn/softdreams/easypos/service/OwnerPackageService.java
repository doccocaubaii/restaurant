package vn.softdreams.easypos.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.domain.OwnerPackage;

import java.util.Optional;

/**
 * Service Interface for managing {@link OwnerPackage}.
 */
public interface OwnerPackageService {
    /**
     * Save a ownerPackage.
     *
     * @param ownerPackage the entity to save.
     * @return the persisted entity.
     */
    OwnerPackage save(OwnerPackage ownerPackage);

    /**
     * Updates a ownerPackage.
     *
     * @param ownerPackage the entity to update.
     * @return the persisted entity.
     */
    OwnerPackage update(OwnerPackage ownerPackage);

    /**
     * Partially updates a ownerPackage.
     *
     * @param ownerPackage the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OwnerPackage> partialUpdate(OwnerPackage ownerPackage);

    /**
     * Get all the ownerPackages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OwnerPackage> findAll(Pageable pageable);

    /**
     * Get the "id" ownerPackage.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OwnerPackage> findOne(Integer id);

    /**
     * Delete the "id" ownerPackage.
     *
     * @param id the id of the entity.
     */
    void delete(Integer id);
}
