package vn.softdreams.easypos.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import vn.softdreams.easypos.constants.BillConstants;
import vn.softdreams.easypos.constants.ResultConstants;
import vn.softdreams.easypos.domain.Bill;
import vn.softdreams.easypos.dto.bill.*;
import vn.softdreams.easypos.repository.BillPaymentRepository;
import vn.softdreams.easypos.repository.BillProductRepository;
import vn.softdreams.easypos.repository.BillRepository;
import vn.softdreams.easypos.service.BillManagementService;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.util.Common;
import vn.softdreams.easypos.web.rest.errors.BadRequestAlertException;
import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.Validator;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BillManagementResource {

    private final Logger log = LoggerFactory.getLogger(BillManagementResource.class);

    private final BillManagementService billManagementService;
    private final BillRepository billRepository;
    private final BillPaymentRepository billPaymentRepository;
    private final BillProductRepository billProductRepository;
    private final Validator customValidator;

    private static final String ENTITY_NAME = "Bill";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public BillManagementResource(
        BillManagementService billManagementService,
        BillRepository billRepository,
        BillPaymentRepository billPaymentRepository,
        BillProductRepository billProductRepository,
        Validator customValidator
    ) {
        this.billManagementService = billManagementService;
        this.billRepository = billRepository;
        this.billPaymentRepository = billPaymentRepository;
        this.billProductRepository = billProductRepository;
        this.customValidator = customValidator;
    }

    @PostMapping("/client/page/bill/create")
    public ResponseEntity<ResultDTO> createBill(@RequestBody BillCreateRequest billDTO) throws URISyntaxException, JsonProcessingException {
        Common.validateInput(customValidator, ENTITY_NAME, billDTO);
        log.debug("REST request to save Bill : {}", billDTO);
        ResultDTO result = billManagementService.saveBill(billDTO);
        Bill bill = (Bill) result.getData();
        if (bill.getStatus().equals(BillConstants.Status.BILL_COMPLETE)) {
            SecurityContext context = SecurityContextHolder.getContext();
            billManagementService.billCompletionAsync(bill, context);
        }
        BillCancelRequest data = new BillCancelRequest(bill.getId(), bill.getCode());
        BillDetailResponse detail = (BillDetailResponse) billManagementService.getBillByIdAndCompanyId(bill.getId()).getData();
        BeanUtils.copyProperties(detail, data);
        result.setData(data);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/client/page/bill/update")
    public ResponseEntity<ResultDTO> updateBill(@RequestBody BillCreateRequest billDTO) throws URISyntaxException, JsonProcessingException {
        if (billDTO.getId() == null) {
            throw new BadRequestAlertException(
                ExceptionConstants.ID_MUST_NOT_NULL_VI,
                ExceptionConstants.ID_MUST_NOT_NULL_VI,
                ExceptionConstants.ID_MUST_BE_NULL
            );
        }
        Common.validateInput(customValidator, ENTITY_NAME, billDTO);
        log.debug("REST request to save Bill : {}", billDTO);
        ResultDTO result = billManagementService.saveBill(billDTO);
        Bill bill = (Bill) result.getData();
        if (bill.getStatus().equals(BillConstants.Status.BILL_COMPLETE)) {
            SecurityContext context = SecurityContextHolder.getContext();
            billManagementService.billCompletionAsync(bill, context);
        }
        BillCancelRequest data = new BillCancelRequest(bill.getId(), bill.getCode());
        BillDetailResponse detail = (BillDetailResponse) billManagementService.getBillByIdAndCompanyId(bill.getId()).getData();
        BeanUtils.copyProperties(detail, data);
        result.setData(data);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/client/page/offline-bill/sync")
    public ResponseEntity<ResultDTO> createBillSync(@RequestBody List<BillCreateRequest> billDTOs)
        throws URISyntaxException, JsonProcessingException {
        Common.validateInput(customValidator, ENTITY_NAME, billDTOs);
        log.debug("REST request to save Bill : {}", billDTOs);
        ResultBillAsync result = billManagementService.createBillSync(billDTOs);
        List<Bill> bills = result.getBills();
        if (!bills.isEmpty()) {
            SecurityContext context = SecurityContextHolder.getContext();
            billManagementService.listBillCompletionAsync(bills, context);
        }
        ResultDTO resultDTO = new ResultDTO();
        if (!result.isStatus()) {
            resultDTO.setMessage(ExceptionConstants.BILLS_INVALID);
            resultDTO.setReason(ExceptionConstants.BILLS_INVALID_VI);
            resultDTO.setStatus(false);
        } else {
            resultDTO.setMessage(ResultConstants.SUCCESS);
            resultDTO.setReason(ResultConstants.CREATE_BILL_SUCCESS);
            resultDTO.setStatus(true);
        }
        resultDTO.setData(result.getResultBillErrors());
        //        result.setData(new BillCancelRequest(bill.getId(), bill.getCode()));
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PutMapping("/client/page/bill/done-by-id")
    public ResultDTO updateBillActivate(@RequestBody BillCompleteRequest statusBillDTO) throws URISyntaxException {
        Common.validateInput(customValidator, ENTITY_NAME, statusBillDTO);
        log.debug("REST request to update status Bill : {}, {}", statusBillDTO.getBillId(), statusBillDTO);
        if (statusBillDTO.getBillId() == null) {
            return new ResultDTO(ExceptionConstants.BILL_ID_NOT_FOUND_VI, ExceptionConstants.BILL_ID_NOT_FOUND, false);
        }
        ResultDTO result = billManagementService.updateBillActivate(statusBillDTO);
        return result;
    }

    @PostMapping("/client/page/bill/pay-off-debt")
    public ResultDTO payOffDebt(@RequestBody PayOffDebtRequest payOffDebtRequest) throws URISyntaxException {
        Common.validateInput(customValidator, ENTITY_NAME, payOffDebtRequest);
        ResultDTO result = billManagementService.payOffDebt(payOffDebtRequest);
        return result;
    }

    @PutMapping("/client/page/bill/cancel-by-id")
    public ResponseEntity<ResultDTO> cancelBillByID(@RequestBody BillCancelRequest billCancelRequest) throws URISyntaxException {
        Common.validateInput(customValidator, ENTITY_NAME, billCancelRequest);
        log.debug("REST request to update status Bill : {}", billCancelRequest);
        ResultDTO result = billManagementService.cancelBillByID(billCancelRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/client/page/bill/complete/cancel-by-id")
    public ResponseEntity<ResultDTO> cancelBillByIdCompleted(@RequestBody BillCompletedCancelRequest request) {
        Common.validateInput(customValidator, ENTITY_NAME, request);
        log.debug("REST request to cancel Bill completed with billId: {}", request.getBillId());
        ResultDTO result = billManagementService.cancelBillCompleted(request);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/client/page/bill/complete/return-by-id")
    public ResponseEntity<ResultDTO> returnBillByIdCompleted(@RequestBody BillCompletedReturnRequest request) {
        Common.validateInput(customValidator, ENTITY_NAME, request);
        log.debug("REST request to return Bill completed with billId: {}", request.getBillId());
        ResultDTO result = billManagementService.returnBillCompleted(request);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/client/page/bill/get-with-paging")
    public ResponseEntity<ResultDTO> getAllBills(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) Integer status,
        @RequestParam(required = false) String fromDate,
        @RequestParam(required = false) String toDate,
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) Boolean isCountAll
    ) {
        log.debug("REST request to get a page of Bills");
        ResultDTO resultDTO = billManagementService.searchBills(pageable, status, fromDate, toDate, keyword, isCountAll);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @GetMapping("/client/page/bill/by-id/{id}")
    public ResponseEntity<ResultDTO> getBillById(
        @PathVariable(value = "id") @NotNull(message = ExceptionConstants.BILL_ID_NOT_NULL) Integer id
    ) {
        log.debug("REST request to get Bill detail with id: {}", id);
        ResultDTO resultDTO = billManagementService.getBillByIdAndCompanyId(id);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @GetMapping("/client/page/bill/check-bill-update")
    public ResponseEntity<ResultDTO> getBillByCode(@RequestParam Integer comId, @RequestParam Integer billId) {
        ResultDTO resultDTO = billManagementService.checkUpdateBill(comId, billId);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @GetMapping("/client/page/bill/by-code/{code}")
    public ResponseEntity<ResultDTO> getBillByCode(
        @PathVariable(value = "code") @NotBlank(message = ExceptionConstants.BILL_CODE_NOT_NULL) String code
    ) {
        log.debug("REST request to get Bill detail with id: {}", code);
        ResultDTO resultDTO = billManagementService.getBillByCode(code);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }
}
