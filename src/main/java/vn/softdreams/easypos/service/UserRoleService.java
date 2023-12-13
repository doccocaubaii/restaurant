package vn.softdreams.easypos.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.domain.UserRole;

import java.util.Optional;

/**
 * Service Interface for managing {@link UserRole}.
 */
public interface UserRoleService {
    /**
     * Save a userRole.
     *
     * @param userRole the entity to save.
     * @return the persisted entity.
     */
    UserRole save(UserRole userRole);

    /**
     * Updates a userRole.
     *
     * @param userRole the entity to update.
     * @return the persisted entity.
     */
    UserRole update(UserRole userRole);

    /**
     * Partially updates a userRole.
     *
     * @param userRole the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UserRole> partialUpdate(UserRole userRole);

    /**
     * Get all the userRoles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserRole> findAll(Pageable pageable);

    /**
     * Get the "id" userRole.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserRole> findOne(Integer id);

    /**
     * Delete the "id" userRole.
     *
     * @param id the id of the entity.
     */
    void delete(Integer id);
}
