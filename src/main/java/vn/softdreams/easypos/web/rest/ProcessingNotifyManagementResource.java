package vn.softdreams.easypos.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.softdreams.easypos.dto.bill.DeleteDishRequest;
import vn.softdreams.easypos.dto.processingRequest.ChangeProcessingStatus;
import vn.softdreams.easypos.service.ProcessingRequestManagementService;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.util.Common;

import javax.validation.Validator;

@RestController
@RequestMapping("/api")
public class ProcessingNotifyManagementResource {

    private final Logger log = LoggerFactory.getLogger(ProcessingAreaManagementResource.class);

    private static final String ENTITY_NAME = "processingArea";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProcessingRequestManagementService processingRequestManagementService;
    private final Validator customValidator;

    public ProcessingNotifyManagementResource(
        ProcessingRequestManagementService processingRequestManagementService,
        Validator customValidator
    ) {
        this.processingRequestManagementService = processingRequestManagementService;
        this.customValidator = customValidator;
    }

    @PostMapping("/client/page/processing-area/update-status")
    public ResponseEntity<ResultDTO> changeProcessingStatus(@RequestBody ChangeProcessingStatus req) {
        Common.validateInput(customValidator, ENTITY_NAME, req);
        return new ResponseEntity<>(processingRequestManagementService.changeProcessingStatus(req), HttpStatus.OK);
    }

    @PutMapping("/client/page/processing-area/delete-dish")
    public ResponseEntity<ResultDTO> deleteDish(@RequestBody DeleteDishRequest req) {
        Common.validateInput(customValidator, ENTITY_NAME, req);
        return new ResponseEntity<>(processingRequestManagementService.deleteDish(req), HttpStatus.OK);
    }
}
