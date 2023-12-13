package vn.softdreams.easypos.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.domain.GenCode;

import java.util.Optional;

/**
 * Service Interface for managing {@link GenCode}.
 */
public interface GenCodeService {
    /**
     * Save a genCode.
     *
     * @param genCode the entity to save.
     * @return the persisted entity.
     */
    GenCode save(GenCode genCode);

    /**
     * Updates a genCode.
     *
     * @param genCode the entity to update.
     * @return the persisted entity.
     */
    GenCode update(GenCode genCode);

    /**
     * Partially updates a genCode.
     *
     * @param genCode the entity to update partially.
     * @return the persisted entity.
     */
    Optional<GenCode> partialUpdate(GenCode genCode);

    /**
     * Get all the genCodes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<GenCode> findAll(Pageable pageable);

    /**
     * Get the "id" genCode.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<GenCode> findOne(Integer id);

    /**
     * Delete the "id" genCode.
     *
     * @param id the id of the entity.
     */
    void delete(Integer id);
}
