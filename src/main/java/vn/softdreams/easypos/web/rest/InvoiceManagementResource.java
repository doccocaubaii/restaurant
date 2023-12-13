package vn.softdreams.easypos.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.softdreams.easypos.dto.invoice.InvoiceRequest;
import vn.softdreams.easypos.dto.invoice.PublishInvoiceRequest;
import vn.softdreams.easypos.dto.invoice.SendIssuanceNoticeRequest;
import vn.softdreams.easypos.service.InvoiceManagementService;
import vn.softdreams.easypos.service.dto.PublishListRequest;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.util.Common;
import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;
import vn.softdreams.easypos.web.rest.errors.IntegrationException;

import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class InvoiceManagementResource {

    private final Logger log = LoggerFactory.getLogger(InvoiceManagementResource.class);

    private final InvoiceManagementService invoiceManagementService;
    private final Validator validator;

    private static final String ENTITY_NAME = "Expenditure";

    public InvoiceManagementResource(InvoiceManagementService invoiceManagementService, Validator validator) {
        this.invoiceManagementService = invoiceManagementService;
        this.validator = validator;
    }

    @PostMapping("/client/page/invoice/publish")
    public ResultDTO issueInvoices(@RequestBody List<PublishInvoiceRequest> publishInvoiceRequests) throws URISyntaxException {
        ResultDTO result = invoiceManagementService.issueInvoices(publishInvoiceRequests);
        return result;
    }

    @GetMapping("/client/page/invoice/view-pdf")
    public ResponseEntity<ResultDTO> getInvoicePdf(@RequestParam int invoiceId, @RequestParam String ikey) throws Exception {
        if (ikey.isEmpty()) throw new IntegrationException(IntegrationException.Party.EasyInvoice, "Ikey không được bỏ trống");
        ResultDTO result = invoiceManagementService.getInvoicePdf(invoiceId, ikey);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/client/page/invoice/send-mail")
    public ResponseEntity<ResultDTO> sendIssuanceNotice(@RequestBody SendIssuanceNoticeRequest request) throws Exception {
        Common.validateInput(validator, "SendIssuanceNoticeRequest", request);
        ResultDTO result = invoiceManagementService.sendIssuanceNotice(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/client/page/invoice/get-with-paging")
    public ResponseEntity<ResultDTO> getAllInvoices(
        @RequestParam(required = false) Integer taxCheckStatus,
        @RequestParam(required = false) String fromDate,
        @RequestParam(required = false) String toDate,
        @RequestParam(required = false) String pattern,
        @RequestParam(required = false) String customerName,
        @RequestParam(required = false) String no,
        @RequestParam(required = false) Boolean isCountAll,
        Pageable pageable
    ) {
        ResultDTO resultDTO = invoiceManagementService.getAllInvoices(
            taxCheckStatus,
            fromDate,
            toDate,
            pattern,
            customerName,
            no,
            pageable,
            isCountAll != null && isCountAll
        );
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PostMapping("/client/page/invoice/publish-list")
    public ResponseEntity<ResultDTO> publishListInvoice(@RequestBody(required = false) PublishListRequest publishListRequest) {
        ResultDTO resultDTO = invoiceManagementService.publishListInvoice(publishListRequest);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @GetMapping("/client/page/invoice/by-id/{id}")
    public ResponseEntity<ResultDTO> getInvoiceById(@PathVariable Integer id) {
        log.debug("REST request to get Invoice with id : {}", id);
        ResultDTO resultDTO = invoiceManagementService.getInvoiceById(id);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PutMapping("/client/page/invoice/modify/{id}")
    public ResponseEntity<ResultDTO> updateInvoice(@PathVariable Integer id, @RequestBody InvoiceRequest invoiceRequest) {
        log.debug("REST request to get Invoice with id : {}", id);
        ResultDTO resultDTO = invoiceManagementService.updateInvoice(id, invoiceRequest);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @GetMapping("/client/common/get-owner-info/{comId}")
    public ResponseEntity<ResultDTO> getOwnerInfo(@PathVariable Integer comId) {
        ResultDTO result = invoiceManagementService.getOwnerInfo(comId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/client/common/get-register-invoice-patterns/{comId}")
    public ResponseEntity<ResultDTO> getInvoicePatterns(@PathVariable Integer comId) throws Exception {
        ResultDTO result = invoiceManagementService.getInvoicePatterns(comId);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/client/page/invoice/delete/{invoiceId}")
    public ResponseEntity<ResultDTO> deleteInvoiceNotPublish(
        @PathVariable @NotNull(message = ExceptionConstants.INVOICE_ID_NOT_NULL) Integer invoiceId
    ) throws Exception {
        ResultDTO result = invoiceManagementService.deleteInvoiceNotPublish(invoiceId);
        return ResponseEntity.ok(result);
    }
}
