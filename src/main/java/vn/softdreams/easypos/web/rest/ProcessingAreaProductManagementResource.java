package vn.softdreams.easypos.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.softdreams.easypos.repository.ProcessingAreaProductRepository;
import vn.softdreams.easypos.service.ProcessingAreaProductManagementService;
import vn.softdreams.easypos.service.dto.ResultDTO;

@RestController
@RequestMapping("/api")
public class ProcessingAreaProductManagementResource {

    private final Logger log = LoggerFactory.getLogger(ProcessingAreaProductManagementResource.class);

    private static final String ENTITY_NAME = "processingAreaProduct";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProcessingAreaProductManagementService processingAreaProductManagementService;

    private final ProcessingAreaProductRepository processingAreaProductRepository;

    public ProcessingAreaProductManagementResource(
        ProcessingAreaProductManagementService processingAreaProductManagementService,
        ProcessingAreaProductRepository processingAreaProductRepository
    ) {
        this.processingAreaProductManagementService = processingAreaProductManagementService;
        this.processingAreaProductRepository = processingAreaProductRepository;
    }

    @GetMapping("/client/page/processing-area-product/get-with-processingAreaId")
    public ResponseEntity<ResultDTO> getProductByProcessingAreaId(
        @RequestParam(required = false) Integer processingAreaId,
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size
    ) {
        PageRequest pageable;
        if (page == null || size == null) {
            pageable = null;
        } else {
            pageable = PageRequest.of(page, size);
        }
        log.debug("REST request to filter a page of ProcessingAreaProduct");
        return new ResponseEntity<>(
            processingAreaProductManagementService.getProductByProcessingAreaId(processingAreaId, pageable),
            HttpStatus.OK
        );
    }

    @PostMapping("/client/page/processing-area-product/delete/{id}")
    public ResponseEntity<ResultDTO> delete(@PathVariable Integer id) {
        ResultDTO resultDTO = processingAreaProductManagementService.delete(id);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @GetMapping("/client/page/processing-area-product/get-all-productProductUnitId-with-not-PaId")
    public ResponseEntity<ResultDTO> getAllProcessingAreaProductWithNotProcessingAreaId(
        @RequestParam(required = false) Integer comId,
        @RequestParam(required = false) Integer processingAreaId
    ) {
        log.debug("REST request to filter a page of ProcessingAreaProduct");
        return new ResponseEntity<>(
            processingAreaProductManagementService.getAllProductProductUnitIdNotPaId(comId, processingAreaId),
            HttpStatus.OK
        );
    }
}
