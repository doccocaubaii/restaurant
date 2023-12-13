package vn.softdreams.easypos.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import vn.softdreams.easypos.dto.receiptpayment.DeleteReceiptPaymentList;
import vn.softdreams.easypos.dto.receiptpayment.ReceiptPaymentCreateRequest;
import vn.softdreams.easypos.dto.receiptpayment.ReceiptPaymentDeleteRequest;
import vn.softdreams.easypos.dto.receiptpayment.ReceiptPaymentUpdateRequest;
import vn.softdreams.easypos.service.ReceiptPaymentManagementService;
import vn.softdreams.easypos.service.ReportManagementService;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.util.Common;

import javax.validation.Validator;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api")
public class ReceiptPaymentManagementResource {

    private final Logger log = LoggerFactory.getLogger(ReportManagementService.class);

    private final ReceiptPaymentManagementService receiptPaymentManagementService;
    private final Validator validator;
    private final String ENTITY_NAME = "receipt payment";

    public ReceiptPaymentManagementResource(ReceiptPaymentManagementService receiptPaymentManagementService, Validator validator) {
        this.receiptPaymentManagementService = receiptPaymentManagementService;
        this.validator = validator;
    }

    @GetMapping("/page/receipt-payment/get-all-transactions")
    public ResultDTO getAllTransactions(
        Pageable pageable,
        @RequestParam @NotNull(message = "comId is required") Integer comId,
        @RequestParam(required = false) String fromDate,
        @RequestParam(required = false) String toDate,
        @RequestParam(required = false) Integer type,
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) Boolean isCountAll
    ) throws Exception {
        return receiptPaymentManagementService.getAllTransactions(pageable, comId, fromDate, toDate, type, keyword, isCountAll);
    }

    @GetMapping("/page/receipt-payment/by-id")
    public ResultDTO getById(
        @RequestParam @NotNull(message = "comId is required") Integer comId,
        @RequestParam @NotNull(message = "type is required") Integer type,
        @RequestParam @NotNull(message = "id is required") Integer id
    ) {
        return receiptPaymentManagementService.getById(comId, type, id);
    }

    @PostMapping("/page/receipt-payment/create")
    public ResultDTO create(@RequestBody ReceiptPaymentCreateRequest request) throws Exception {
        Common.validateInput(validator, ENTITY_NAME, request);
        return receiptPaymentManagementService.create(request);
    }

    @PutMapping("/page/receipt-payment/update")
    public ResultDTO update(@RequestBody ReceiptPaymentUpdateRequest request) throws Exception {
        Common.validateInput(validator, ENTITY_NAME, request);
        return receiptPaymentManagementService.update(request);
    }

    @PutMapping("/page/receipt-payment/delete")
    public ResultDTO delete(@RequestBody ReceiptPaymentDeleteRequest request) throws Exception {
        Common.validateInput(validator, ENTITY_NAME, request);
        return receiptPaymentManagementService.delete(request);
    }

    @GetMapping("/p/page/receipt-payment/insert-missing")
    public ResultDTO insertMissing() throws Exception {
        return receiptPaymentManagementService.insertMissingData();
    }

    @PutMapping("/page/receipt-payment/delete-list")
    public ResultDTO deleteList(@RequestBody DeleteReceiptPaymentList request) throws Exception {
        Common.validateInput(validator, ENTITY_NAME, request);
        return receiptPaymentManagementService.deleteList(request);
    }
}
