package vn.softdreams.easypos.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.softdreams.easypos.dto.processingArea.ProcessingAreaRequest;
import vn.softdreams.easypos.repository.ProcessingAreaRepository;
import vn.softdreams.easypos.service.BillManagementService;
import vn.softdreams.easypos.service.ProcessingAreaManagementService;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.util.Common;

import javax.validation.Validator;

/**
 * REST controller for managing {@link vn.softdreams.easypos.domain.ProcessingArea}.
 */
@RestController
@RequestMapping("/api")
public class ProcessingAreaManagementResource {

    private final Logger log = LoggerFactory.getLogger(ProcessingAreaManagementResource.class);

    private static final String ENTITY_NAME = "processingArea";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProcessingAreaManagementService processingAreaService;
    private final BillManagementService billManagementService;

    private final ProcessingAreaRepository processingAreaRepository;
    private final Validator customValidator;

    public ProcessingAreaManagementResource(
        ProcessingAreaManagementService processingAreaService,
        BillManagementService billManagementService,
        ProcessingAreaRepository processingAreaRepository,
        Validator customValidator
    ) {
        this.processingAreaService = processingAreaService;
        this.billManagementService = billManagementService;
        this.processingAreaRepository = processingAreaRepository;
        this.customValidator = customValidator;
    }

    @GetMapping("/client/page/processing-area/get-with-paging")
    public ResponseEntity<ResultDTO> filterProcessingArea(
        @RequestParam(required = false) Integer comId,
        @RequestParam(required = false) String name,
        @RequestParam(required = false) Integer setting,
        @RequestParam(required = false) Integer active,
        @RequestParam(required = false) String fromDate,
        @RequestParam(required = false) String toDate,
        @RequestParam(required = false) Integer size,
        @RequestParam(required = false) Integer page
    ) {
        PageRequest pageable = null;
        if (size != null && page != null) {
            pageable = PageRequest.of(page, size);
        }
        log.debug("REST request to filter a page of ProcessingArea");
        return new ResponseEntity<>(processingAreaService.filter(comId, name, setting, active, fromDate, toDate, pageable), HttpStatus.OK);
    }

    @PostMapping("/client/page/processing-area/create")
    public ResponseEntity<ResultDTO> create(@RequestBody ProcessingAreaRequest request) {
        Common.validateInput(customValidator, ENTITY_NAME, request);
        ResultDTO resultDTO = processingAreaService.save(request);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PostMapping("/client/page/processing-area/delete/{id}")
    public ResponseEntity<ResultDTO> delete(@PathVariable Integer id) {
        ResultDTO resultDTO = processingAreaService.delete(id);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @GetMapping("/client/page/processing-area/find/{id}")
    public ResponseEntity<ResultDTO> find(@PathVariable Integer id) {
        ResultDTO resultDTO = processingAreaService.getProcessingArea(id);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @GetMapping("/client/page/product-processing-area/get-with-paging")
    public ResponseEntity<ResultDTO> filterProductProcessingArea(
        @RequestParam(required = false) Integer comId,
        @RequestParam(required = false) Integer processingAreaId,
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) Integer size,
        @RequestParam(required = false) Integer page
    ) {
        PageRequest pageable = null;
        if (size != null && page != null) {
            pageable = PageRequest.of(page, size);
        }
        log.debug("REST request to filter a page of ProcessingArea");
        return new ResponseEntity<>(processingAreaService.filterProduct(comId, processingAreaId, keyword, pageable), HttpStatus.OK);
    }

    @PostMapping("/client/page/processing-area/update")
    public ResponseEntity<ResultDTO> update(@RequestBody ProcessingAreaRequest request) {
        Common.validateInput(customValidator, ENTITY_NAME, request);
        ResultDTO resultDTO = processingAreaService.update(request);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @GetMapping("/client/page/processing-area/get-for-processing")
    public ResponseEntity<ResultDTO> getForProcessing(Pageable pageable, @RequestParam Integer type, @RequestParam Integer status) {
        ResultDTO resultDTO = billManagementService.getForProcessing(pageable, type, status);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }
}
