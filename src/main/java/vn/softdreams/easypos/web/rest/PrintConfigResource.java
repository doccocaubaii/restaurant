package vn.softdreams.easypos.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import vn.softdreams.easypos.constants.ResultConstants;
import vn.softdreams.easypos.dto.config.PrintConfigRequest;
import vn.softdreams.easypos.repository.PrintConfigRepository;
import vn.softdreams.easypos.service.PrintConfigService;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.util.Common;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

/**
 * REST controller for managing {@link vn.softdreams.easypos.domain.PrintConfig}.
 */
@RestController
@RequestMapping("/api")
public class PrintConfigResource {

    private final Logger log = LoggerFactory.getLogger(PrintConfigResource.class);

    private static final String ENTITY_NAME = "printConfig";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PrintConfigService printConfigService;
    private final Validator customValidator;
    private final PrintConfigRepository printConfigRepository;

    public PrintConfigResource(
        PrintConfigService printConfigService,
        Validator customValidator,
        PrintConfigRepository printConfigRepository
    ) {
        this.printConfigService = printConfigService;
        this.customValidator = customValidator;
        this.printConfigRepository = printConfigRepository;
    }

    /**
     * {@code POST  /client/common/print-configs} : Create a new printConfig.
     *
     * @param printConfig the printConfig to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new printConfig, or with status {@code 400 (Bad Request)} if the printConfig has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/client/common/print-configs/create")
    public ResponseEntity<ResultDTO> createPrintConfig(@RequestBody PrintConfigRequest printConfig) throws URISyntaxException {
        Common.validateInput(customValidator, ENTITY_NAME, printConfig);
        log.debug("REST request to save PrintConfig : {}", printConfig);
        if (printConfig.getId() != null) {
            return new ResponseEntity<>(
                new ResultDTO(ResultConstants.ERROR_CREATE_ID, ResultConstants.ERROR_CREATE_ID_VI, false),
                HttpStatus.OK
            );
        }
        ResultDTO result = printConfigService.save(printConfig);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * {@code PUT  /client/common/print-configs/:id} : Updates an existing printConfig.
     *
     * @param id the id of the printConfig to save.
     * @param printConfig the printConfig to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated printConfig,
     * or with status {@code 400 (Bad Request)} if the printConfig is not valid,
     * or with status {@code 500 (Internal Server Error)} if the printConfig couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/client/common/print-configs/update")
    public ResponseEntity<ResultDTO> updatePrintConfig(@RequestBody PrintConfigRequest printConfig) throws URISyntaxException {
        log.error("updatePrintConfig_request {}", printConfig);
        if (printConfig.getId() == null) {
            return new ResponseEntity<>(
                new ResultDTO(ResultConstants.ERROR_UPDATE_ID, ResultConstants.ERROR_UPDATE_ID_VI, false),
                HttpStatus.OK
            );
        }
        if (!printConfigRepository.existsById(printConfig.getId())) {
            return new ResponseEntity<>(
                new ResultDTO(ResultConstants.ERROR_UPDATE_ID, ResultConstants.ERROR_UPDATE_ID_VI, false),
                HttpStatus.OK
            );
        }
        ResultDTO result = printConfigService.save(printConfig);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * {@code GET  /client/common/print-configs} : get all the printConfigs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of printConfigs in body.
     */
    @GetMapping("/client/common/print-configs/get-all")
    public ResponseEntity<ResultDTO> getAllPrintConfigs() {
        log.debug("REST request to get a page of PrintConfigs");
        ResultDTO result = printConfigService.findAll();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * {@code GET  /client/common/print-configs/:id} : get the "id" printConfig.
     *
     * @param id the id of the printConfig to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the printConfig, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/client/common/print-configs/{id}")
    public ResponseEntity<ResultDTO> getPrintConfig(@PathVariable Integer id) {
        log.debug("REST request to get PrintConfig : {}", id);
        ResultDTO printConfig = printConfigService.findOne(id);
        return new ResponseEntity<>(printConfig, HttpStatus.OK);
    }

    /**
     * {@code DELETE  /client/common/print-configs/:id} : delete the "id" printConfig.
     *
     * @param id the id of the printConfig to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/client/common/print-configs/{id}")
    public ResponseEntity<Void> deletePrintConfig(@PathVariable Integer id) {
        log.debug("REST request to delete PrintConfig : {}", id);
        printConfigService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    private Set<String> validateInput(Object... objects) {
        Set<String> violationSet = new HashSet<>();
        for (Object o : objects) {
            Set<ConstraintViolation<Object>> violations = customValidator.validate(o);
            if (violations.isEmpty()) continue;
            String messages = violations.stream().reduce("", (acc, ele) -> acc + ", " + ele.getMessage(), String::concat);
            violationSet.add(messages);
        }
        return violationSet;
    }
}
