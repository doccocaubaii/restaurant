package vn.softdreams.easypos.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.domain.OwnerDevice;

import java.util.Optional;

/**
 * Service Interface for managing {@link OwnerDevice}.
 */
public interface OwnerDeviceService {
    /**
     * Save a ownerDevice.
     *
     * @param ownerDevice the entity to save.
     * @return the persisted entity.
     */
    OwnerDevice save(OwnerDevice ownerDevice);

    /**
     * Updates a ownerDevice.
     *
     * @param ownerDevice the entity to update.
     * @return the persisted entity.
     */
    OwnerDevice update(OwnerDevice ownerDevice);

    /**
     * Partially updates a ownerDevice.
     *
     * @param ownerDevice the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OwnerDevice> partialUpdate(OwnerDevice ownerDevice);

    /**
     * Get all the ownerDevices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OwnerDevice> findAll(Pageable pageable);

    /**
     * Get the "id" ownerDevice.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OwnerDevice> findOne(Integer id);

    /**
     * Delete the "id" ownerDevice.
     *
     * @param id the id of the entity.
     */
    void delete(Integer id);
}
