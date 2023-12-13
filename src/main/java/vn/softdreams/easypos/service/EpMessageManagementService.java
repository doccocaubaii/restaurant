package vn.softdreams.easypos.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.domain.EpMessage;
import vn.softdreams.easypos.dto.message.IntegrationSendMessageRequest;
import vn.softdreams.easypos.service.dto.ResultDTO;

import java.util.Optional;

/**
 * Service Interface for managing {@link EpMessage}.
 */
public interface EpMessageManagementService {
    /**
     * Save a epMessage.
     *
     * @param epMessage the entity to save.
     * @return the persisted entity.
     */
    EpMessage save(EpMessage epMessage);

    /**
     * Updates a epMessage.
     *
     * @param epMessage the entity to update.
     * @return the persisted entity.
     */
    EpMessage update(EpMessage epMessage);

    /**
     * Partially updates a epMessage.
     *
     * @param epMessage the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EpMessage> partialUpdate(EpMessage epMessage);

    /**
     * Get all the epMessages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EpMessage> findAll(Pageable pageable);

    /**
     * Get the "id" epMessage.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EpMessage> findOne(Integer id);

    /**
     * Delete the "id" epMessage.
     *
     * @param id the id of the entity.
     */
    void delete(Integer id);
    ResultDTO sendMessage(EpMessage message);
    ResultDTO sendIntegrationMessage(IntegrationSendMessageRequest request);
}
