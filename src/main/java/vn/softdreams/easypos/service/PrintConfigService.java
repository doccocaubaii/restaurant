package vn.softdreams.easypos.service;

import vn.softdreams.easypos.domain.PrintConfig;
import vn.softdreams.easypos.dto.config.PrintConfigRequest;
import vn.softdreams.easypos.service.dto.ResultDTO;

import java.util.Optional;

/**
 * Service Interface for managing {@link PrintConfig}.
 */
public interface PrintConfigService {
    /**
     * Save a printConfig.
     *
     * @param printConfig the entity to save.
     * @return the persisted entity.
     */
    ResultDTO save(PrintConfigRequest printConfig);

    /**
     * Updates a printConfig.
     *
     * @param printConfig the entity to update.
     * @return the persisted entity.
     */
    PrintConfig update(PrintConfig printConfig);

    /**
     * Partially updates a printConfig.
     *
     * @param printConfig the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PrintConfig> partialUpdate(PrintConfig printConfig);

    /**
     * Get all the printConfigs.
     *
     * @return the list of entities.
     */
    ResultDTO findAll();

    /**
     * Get the "id" printConfig.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    ResultDTO findOne(Integer id);

    /**
     * Delete the "id" printConfig.
     *
     * @param id the id of the entity.
     */
    void delete(Integer id);
}
