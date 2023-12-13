package vn.softdreams.easypos.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.softdreams.easypos.dto.voucher.VoucherApplyAllRequest;
import vn.softdreams.easypos.dto.voucher.VoucherApplyRequest;
import vn.softdreams.easypos.dto.voucher.VoucherSaveRequest;
import vn.softdreams.easypos.dto.voucher.VoucherWebSaveRequest;
import vn.softdreams.easypos.service.VoucherManagementService;
import vn.softdreams.easypos.service.VoucherWebManagementService;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.util.Common;
import vn.softdreams.easypos.web.rest.errors.BadRequestAlertException;
import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * REST controller for managing {@link vn.softdreams.easypos.domain.Voucher}.
 */
@RestController
@RequestMapping("/api")
public class VoucherManagementResource {

    private final Logger log = LoggerFactory.getLogger(VoucherManagementResource.class);

    private static final String ENTITY_NAME = "voucher";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VoucherManagementService voucherService;
    private final VoucherWebManagementService voucherWebService;
    private final Validator customValidator;

    public VoucherManagementResource(
        VoucherManagementService voucherService,
        VoucherWebManagementService voucherWebService,
        Validator customValidator
    ) {
        this.voucherService = voucherService;
        this.voucherWebService = voucherWebService;
        this.customValidator = customValidator;
    }

    @PostMapping("/client/page/voucher/web/create")
    public ResponseEntity<ResultDTO> createWebVoucher(@RequestBody VoucherWebSaveRequest request) {
        Common.validateInput(customValidator, ENTITY_NAME, request);
        ResultDTO resultDTO = voucherWebService.saveVoucher(request);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PostMapping("/client/page/voucher/create")
    public ResponseEntity<ResultDTO> createVoucher(@RequestBody VoucherSaveRequest request) {
        Common.validateInput(customValidator, ENTITY_NAME, request);
        ResultDTO resultDTO = voucherService.saveVoucher(request);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PutMapping("/client/page/voucher/update")
    public ResponseEntity<ResultDTO> updateVoucher(@RequestBody VoucherSaveRequest request) {
        Common.validateInput(customValidator, ENTITY_NAME, request);
        if (request.getId() == null) {
            throw new BadRequestAlertException(
                ExceptionConstants.VOUCHER_ID_NOT_NULL_VI,
                ENTITY_NAME,
                ExceptionConstants.VOUCHER_ID_NOT_NULL
            );
        }
        ResultDTO resultDTO = voucherService.saveVoucher(request);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PutMapping("/client/page/voucher/web/update")
    public ResponseEntity<ResultDTO> updateWebVoucher(@RequestBody VoucherWebSaveRequest request) {
        Common.validateInput(customValidator, ENTITY_NAME, request);
        if (request.getId() == null) {
            throw new BadRequestAlertException(
                ExceptionConstants.VOUCHER_ID_NOT_NULL_VI,
                ENTITY_NAME,
                ExceptionConstants.VOUCHER_ID_NOT_NULL
            );
        }
        ResultDTO resultDTO = voucherWebService.saveVoucher(request);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PostMapping("/client/page/voucher/apply")
    public ResponseEntity<ResultDTO> applyVoucher(@RequestBody VoucherApplyRequest request) {
        Common.validateInput(customValidator, ENTITY_NAME, request);
        ResultDTO resultDTO = voucherService.applyVoucher(request);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PostMapping("/client/page/voucher/apply-all")
    public ResponseEntity<ResultDTO> applyAllVoucher(@RequestBody VoucherApplyAllRequest request) {
        Common.validateInput(customValidator, ENTITY_NAME, request);
        ResultDTO resultDTO = voucherService.applyAllVoucher(request);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @GetMapping("/client/page/voucher/get-with-paging")
    public ResponseEntity<ResultDTO> getWithPaging(
        @RequestParam Integer comId,
        @RequestParam Integer type,
        Pageable pageable,
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) String fromDate,
        @RequestParam(required = false) String toDate
    ) {
        ResultDTO resultDTO = voucherService.getWithPaging(comId, keyword, pageable, fromDate, toDate, type);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @GetMapping("/client/page/voucher/web/get-with-paging")
    public ResponseEntity<ResultDTO> getWithPagingWeb(
        @RequestParam Integer comId,
        Pageable pageable,
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) String fromDate,
        @RequestParam(required = false) String toDate
    ) {
        ResultDTO resultDTO = voucherWebService.getWithPaging(comId, keyword, pageable, fromDate, toDate);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PutMapping("/client/page/voucher/delete/{id}")
    public ResponseEntity<ResultDTO> deleteVoucher(@PathVariable(value = "id") @NotNull Integer id) {
        ResultDTO resultDTO = voucherService.deleteVoucher(id);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @GetMapping("/client/page/voucher/get-for-bill")
    public ResponseEntity<ResultDTO> getAllForBill(
        @RequestParam Integer customerId,
        Pageable pageable,
        @RequestParam(required = false) String keyword
    ) {
        ResultDTO resultDTO = voucherService.getAllVoucherForBill(customerId, keyword, pageable);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @GetMapping("/client/page/voucher/web/get-for-bill")
    public ResponseEntity<ResultDTO> getAllForBillWeb(
        @RequestParam Integer customerId,
        Pageable pageable,
        @RequestParam(required = false) String keyword
    ) {
        ResultDTO resultDTO = voucherWebService.getAllVoucherForBill(customerId, keyword, pageable);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PostMapping("/client/page/voucher/check-valid")
    public ResponseEntity<ResultDTO> checkValidVoucher(@RequestBody List<Integer> ids) {
        ResultDTO resultDTO = voucherService.checkValidVoucher(ids);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PostMapping("/client/page/voucher/web/check-valid")
    public ResponseEntity<ResultDTO> checkValidVoucherWeb(@RequestBody List<Integer> ids) {
        ResultDTO resultDTO = voucherWebService.checkValidVoucher(ids);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @GetMapping("/client/page/voucher/apply/detail")
    public ResponseEntity<ResultDTO> getVoucherApplyDetail(
        @RequestParam Integer comId,
        @RequestParam Integer voucherId,
        @RequestParam Integer type,
        @RequestParam Boolean getDefault,
        @RequestParam(required = false) String keyword,
        Pageable pageable
    ) {
        ResultDTO resultDTO = voucherService.getVoucherApplyDetail(comId, voucherId, type, pageable, keyword, getDefault);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }
}
