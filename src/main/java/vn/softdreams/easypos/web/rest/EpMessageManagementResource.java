package vn.softdreams.easypos.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import vn.softdreams.easypos.domain.EpMessage;
import vn.softdreams.easypos.dto.message.IntegrationSendMessageRequest;
import vn.softdreams.easypos.dto.message.SendMessageReponse;
import vn.softdreams.easypos.repository.EpMessageRepository;
import vn.softdreams.easypos.service.EpMessageManagementService;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.util.Common;
import vn.softdreams.easypos.util.CommonIntegrated;
import vn.softdreams.easypos.web.rest.errors.BadRequestAlertException;

import javax.validation.Validator;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link vn.softdreams.easypos.domain.EpMessage}.
 */
@RestController
@RequestMapping("/api")
public class EpMessageManagementResource {

    private final Logger log = LoggerFactory.getLogger(EpMessageManagementResource.class);

    private static final String ENTITY_NAME = "epMessage";
    private final Validator customValidator;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EpMessageManagementService epMessageService;

    private final EpMessageRepository epMessageRepository;

    public EpMessageManagementResource(
        Validator customValidator,
        EpMessageManagementService epMessageService,
        EpMessageRepository epMessageRepository
    ) {
        this.customValidator = customValidator;
        this.epMessageService = epMessageService;
        this.epMessageRepository = epMessageRepository;
    }

    /**
     * {@code POST  /ep-messages} : Create a new epMessage.
     *
     * @param epMessage the epMessage to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new epMessage, or with status {@code 400 (Bad Request)} if the epMessage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/messages/send-message")
    public ResponseEntity<EpMessage> createEpMessage(@RequestBody EpMessage epMessage) throws URISyntaxException {
        log.debug("REST request to save EpMessage : {}", epMessage);
        if (epMessage.getId() != null) {
            throw new BadRequestAlertException("A new epMessage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EpMessage result = epMessageService.save(epMessage);
        return ResponseEntity
            .created(new URI("/api/ep-messages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/p/messages/send-message/test")
    public ResponseEntity<EpMessage> createEpMessageTEst(@RequestBody EpMessage epMessage) throws URISyntaxException {
        log.error("epMessage" + epMessage.toString());
        SendMessageReponse sendMessageReponse = CommonIntegrated.sendMessage(epMessage, new RestTemplate());
        log.error("epMessage reponse" + sendMessageReponse.toString());
        return ResponseEntity.ok().body(null);
    }

    @PostMapping("/p/messages/send-message/otp")
    public ResponseEntity<EpMessage> createEpMessageOtp(@RequestBody EpMessage epMessage) throws URISyntaxException {
        log.error("epMessage" + epMessage.toString());
        SendMessageReponse sendMessageReponse = CommonIntegrated.sendMessage(epMessage, new RestTemplate());
        log.error("epMessage reponse" + sendMessageReponse.toString());
        return ResponseEntity.ok().body(null);
    }

    /**
     * {@code PUT  /ep-messages/:id} : Updates an existing epMessage.
     *
     * @param id the id of the epMessage to save.
     * @param epMessage the epMessage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated epMessage,
     * or with status {@code 400 (Bad Request)} if the epMessage is not valid,
     * or with status {@code 500 (Internal Server Error)} if the epMessage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ep-messages/{id}")
    public ResponseEntity<EpMessage> updateEpMessage(
        @PathVariable(value = "id", required = false) final Integer id,
        @RequestBody EpMessage epMessage
    ) throws URISyntaxException {
        log.debug("REST request to update EpMessage : {}, {}", id, epMessage);
        if (epMessage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, epMessage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!epMessageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EpMessage result = epMessageService.update(epMessage);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, epMessage.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ep-messages/:id} : Partial updates given fields of an existing epMessage, field will ignore if it is null
     *
     * @param id the id of the epMessage to save.
     * @param epMessage the epMessage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated epMessage,
     * or with status {@code 400 (Bad Request)} if the epMessage is not valid,
     * or with status {@code 404 (Not Found)} if the epMessage is not found,
     * or with status {@code 500 (Internal Server Error)} if the epMessage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ep-messages/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EpMessage> partialUpdateEpMessage(
        @PathVariable(value = "id", required = false) final Integer id,
        @RequestBody EpMessage epMessage
    ) throws URISyntaxException {
        log.debug("REST request to partial update EpMessage partially : {}, {}", id, epMessage);
        if (epMessage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, epMessage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!epMessageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EpMessage> result = epMessageService.partialUpdate(epMessage);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, epMessage.getId().toString())
        );
    }

    /**
     * {@code GET  /ep-messages} : get all the epMessages.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of epMessages in body.
     */
    @GetMapping("/ep-messages")
    public ResponseEntity<List<EpMessage>> getAllEpMessages(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of EpMessages");
        Page<EpMessage> page = epMessageService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ep-messages/:id} : get the "id" epMessage.
     *
     * @param id the id of the epMessage to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the epMessage, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ep-messages/{id}")
    public ResponseEntity<EpMessage> getEpMessage(@PathVariable Integer id) {
        log.debug("REST request to get EpMessage : {}", id);
        Optional<EpMessage> epMessage = epMessageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(epMessage);
    }

    /**
     * {@code DELETE  /ep-messages/:id} : delete the "id" epMessage.
     *
     * @param id the id of the epMessage to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ep-messages/{id}")
    public ResponseEntity<Void> deleteEpMessage(@PathVariable Integer id) {
        log.debug("REST request to delete EpMessage : {}", id);
        epMessageService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/integration/send-sms")
    public ResponseEntity<ResultDTO> sendMessageIntegration(@RequestBody IntegrationSendMessageRequest request) {
        Common.validateInput(customValidator, ENTITY_NAME, request);
        ResultDTO result = epMessageService.sendIntegrationMessage(request);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
