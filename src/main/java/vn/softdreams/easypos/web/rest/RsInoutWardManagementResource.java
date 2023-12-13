package vn.softdreams.easypos.web.rest;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.softdreams.easypos.dto.rsinoutward.RsInOutWardCreateRequest;
import vn.softdreams.easypos.dto.rsinoutward.RsInOutWardDeleteRequest;
import vn.softdreams.easypos.service.RsInoutWardManagementService;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.util.Common;
import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.Validator;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api")
public class RsInoutWardManagementResource {

    private static final String ENTITY_NAME = "rsInoutWard";
    private final RsInoutWardManagementService rsInoutWardManagementService;
    private final Validator customValidator;

    public RsInoutWardManagementResource(RsInoutWardManagementService rsInoutWardManagementService, Validator customValidator) {
        this.rsInoutWardManagementService = rsInoutWardManagementService;
        this.customValidator = customValidator;
    }

    @GetMapping("/page/rs-inoutward/get-all-transactions")
    public ResponseEntity<ResultDTO> getAllTransactions(
        @NotNull(message = ExceptionConstants.COMPANY_ID_NOT_NULL) @RequestParam Integer comId,
        @RequestParam(required = false) String fromDate,
        @RequestParam(required = false) String toDate,
        @RequestParam(required = false) Integer type,
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) Boolean getWithPaging,
        Pageable pageable
    ) {
        ResultDTO result = rsInoutWardManagementService.getAllTransactions(comId, fromDate, toDate, type, keyword, getWithPaging, pageable);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/page/rs-inoutward/by-id/{id}")
    public ResponseEntity<ResultDTO> getOneById(@PathVariable Integer id) {
        ResultDTO result = rsInoutWardManagementService.getOneById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/page/rs-inoutward/create")
    public ResponseEntity<ResultDTO> create(@RequestBody RsInOutWardCreateRequest request) throws Exception {
        Common.validateInput(customValidator, ENTITY_NAME, request);
        ResultDTO resultDTO = rsInoutWardManagementService.create(request);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PutMapping("/page/rs-inoutward/delete")
    public ResponseEntity<ResultDTO> deleteByIdAndCode(@RequestBody RsInOutWardDeleteRequest request) {
        Common.validateInput(customValidator, ENTITY_NAME, request);
        ResultDTO result = rsInoutWardManagementService.deleteByIdAndCode(request);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
