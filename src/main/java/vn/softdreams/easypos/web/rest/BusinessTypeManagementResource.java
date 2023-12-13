package vn.softdreams.easypos.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.softdreams.easypos.service.BusinessTypeManagementService;
import vn.softdreams.easypos.service.dto.ResultDTO;

import javax.validation.Validator;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BusinessTypeManagementResource {

    private final Logger log = LoggerFactory.getLogger(ConfigManagementResource.class);

    private static final String ENTITY_NAME = "business_type";

    private final Validator customValidator;

    private final BusinessTypeManagementService businessTypeManagementService;

    public BusinessTypeManagementResource(Validator customValidator, BusinessTypeManagementService businessTypeManagementService) {
        this.customValidator = customValidator;
        this.businessTypeManagementService = businessTypeManagementService;
    }

    @GetMapping("/page/business-type/get-all-transactions")
    public ResponseEntity<ResultDTO> getAllTransaction(
        @RequestParam Integer comId,
        @RequestParam Integer type,
        @RequestParam(required = false) String keyword
    ) {
        log.debug("REST request to get all business types");
        ResultDTO result = businessTypeManagementService.getAllTransactions(comId, type, keyword);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/page/business-type/create")
    public ResponseEntity<ResultDTO> create(@RequestParam Integer comId, @RequestParam String name, @RequestParam Integer type) {
        log.debug("REST request to create a business type");
        ResultDTO result = businessTypeManagementService.create(comId, name, type);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/page/business-type/update")
    public ResponseEntity<ResultDTO> update(
        @RequestParam Integer comId,
        @RequestParam Integer id,
        @RequestParam Integer type,
        @RequestParam String name
    ) {
        log.debug("REST request to update a business type");
        ResultDTO result = businessTypeManagementService.update(comId, id, name, type);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/p/page/business-type/insert-default")
    public ResponseEntity<ResultDTO> getAllTransaction(@RequestParam List<String> codes, @RequestParam Integer type) {
        ResultDTO result = businessTypeManagementService.insertDefault(codes, type);
        return ResponseEntity.ok(result);
    }
}
