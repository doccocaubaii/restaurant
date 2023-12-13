package vn.softdreams.easypos.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import vn.softdreams.easypos.aop.CheckAuthorize;
import vn.softdreams.easypos.constants.ResultConstants;
import vn.softdreams.easypos.domain.TaskLog;
import vn.softdreams.easypos.dto.company.CompanyResultItem;
import vn.softdreams.easypos.dto.queue.TaskLogAsyncCheckInvoiceRequest;
import vn.softdreams.easypos.dto.queue.TaskLogAsyncRequest;
import vn.softdreams.easypos.dto.taskLog.TaskLogItemResponse;
import vn.softdreams.easypos.dto.taskLog.TaskLogResultItem;
import vn.softdreams.easypos.dto.taskLog.TaskLogSendQueueRequest;
import vn.softdreams.easypos.repository.TaskLogRepository;
import vn.softdreams.easypos.security.AuthoritiesConstants;
import vn.softdreams.easypos.service.TaskLogService;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.util.Common;
import vn.softdreams.easypos.web.rest.errors.BadRequestAlertException;

import javax.validation.Validator;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link vn.softdreams.easypos.domain.TaskLog}.
 */
@RestController
@RequestMapping("/api")
public class TaskLogResource {

    private final Logger log = LoggerFactory.getLogger(TaskLogResource.class);

    private static final String ENTITY_NAME = "taskLog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TaskLogService taskLogService;

    private final TaskLogRepository taskLogRepository;
    private final Validator customValidator;

    public TaskLogResource(TaskLogService taskLogService, TaskLogRepository taskLogRepository, Validator customValidator) {
        this.taskLogService = taskLogService;
        this.taskLogRepository = taskLogRepository;
        this.customValidator = customValidator;
    }

    @PostMapping("/task-logs")
    public ResponseEntity<TaskLog> createTaskLog(@RequestBody TaskLog taskLog) throws URISyntaxException {
        log.debug("REST request to save TaskLog : {}", taskLog);
        if (taskLog.getId() != null) {
            throw new BadRequestAlertException("A new taskLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TaskLog result = taskLogService.save(taskLog);
        return ResponseEntity
            .created(new URI("/api/task-logs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/task-logs/{id}")
    public ResponseEntity<TaskLog> updateTaskLog(
        @PathVariable(value = "id", required = false) final Integer id,
        @RequestBody TaskLog taskLog
    ) throws URISyntaxException {
        log.debug("REST request to update TaskLog : {}, {}", id, taskLog);
        if (taskLog.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taskLog.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taskLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TaskLog result = taskLogService.update(taskLog);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, taskLog.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/task-logs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TaskLog> partialUpdateTaskLog(
        @PathVariable(value = "id", required = false) final Integer id,
        @RequestBody TaskLog taskLog
    ) throws URISyntaxException {
        log.debug("REST request to partial update TaskLog partially : {}, {}", id, taskLog);
        if (taskLog.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taskLog.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taskLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TaskLog> result = taskLogService.partialUpdate(taskLog);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, taskLog.getId().toString())
        );
    }

    @GetMapping("/task-logs")
    public ResponseEntity<List<TaskLog>> getAllTaskLogs(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of TaskLogs");
        Page<TaskLog> page = taskLogService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/task-logs/{id}")
    public ResponseEntity<ResultDTO> getTaskLog(@PathVariable Integer id) {
        log.debug("REST request to get TaskLog : {}", id);
        ResultDTO resultDTO = taskLogService.findTaskLogById(id);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @DeleteMapping("/task-logs/{id}")
    public ResponseEntity<Void> deleteTaskLog(@PathVariable Integer id) {
        log.debug("REST request to delete TaskLog : {}", id);
        taskLogService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/admin/client/task-logs/filter")
    public ResponseEntity<ResultDTO> filterTaskLogs(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) Integer status,
        @RequestParam(required = false) String taxCode,
        @RequestParam(required = false) Integer companyOwnerName,
        @RequestParam(required = false) String fromDate,
        @RequestParam(required = false) String toDate,
        @RequestParam(required = false) String type
    ) {
        log.debug("REST request to filter a page of TaskLogs");
        Page<TaskLogItemResponse> page = taskLogService.filter(pageable, status, taxCode, companyOwnerName, fromDate, toDate, type);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity
            .ok()
            .headers(headers)
            .body(new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_GET_LIST, true, page.getContent()));
    }

    @GetMapping("/admin/client/task-logs/init-company-cbb")
    public ResponseEntity<List<CompanyResultItem>> initDataCompanyCombobox(@RequestParam(required = false) String input) {
        log.debug("REST request to get init data for combobox TaskLog");
        List<CompanyResultItem> result = taskLogService.initDataCompanyCombobox(input);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/admin/client/task-logs/init-status-cbb")
    @CheckAuthorize(value = AuthoritiesConstants.AdminTaskLog.VIEW)
    public ResponseEntity<List<TaskLogResultItem>> initDataStatusCombobox(@RequestParam(required = false) String input) {
        log.debug("REST request to get init data for combobox TaskLog");
        List<TaskLogResultItem> result = taskLogService.initDataStatusCombobox(input);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/admin/client/task-logs/init-type-cbb")
    @CheckAuthorize(value = AuthoritiesConstants.AdminTaskLog.VIEW)
    public ResponseEntity<List<TaskLogResultItem>> initDataTypeCombobox(@RequestParam(required = false) String input) {
        log.debug("REST request to get init data for combobox TaskLog");
        List<TaskLogResultItem> result = taskLogService.initDataTypeCombobox(input);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/admin/client/task-logs/send-queue-again")
    @CheckAuthorize(value = AuthoritiesConstants.AdminTaskLog.RESEND_TASK_LOG)
    public ResponseEntity<ResultDTO> sendQueueAgain(@RequestBody TaskLogSendQueueRequest request) {
        Common.validateInput(customValidator, ENTITY_NAME, request);
        log.debug("REST request to send queue TaskLog again");
        ResultDTO result = taskLogService.sendQueueAgain(request);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/client/page/task-log/unit/create")
    public ResponseEntity<ResultDTO> createUnitAsync(@RequestBody TaskLogAsyncRequest taskLogAsyncRequest) {
        ResultDTO resultDTO = taskLogService.unitCreateAsync(taskLogAsyncRequest);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PostMapping("/client/page/task-log/product/create")
    public ResponseEntity<ResultDTO> createProductAsync(@RequestBody TaskLogAsyncRequest taskLogAsyncRequest) {
        ResultDTO resultDTO = taskLogService.productCreateAsync(taskLogAsyncRequest);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PostMapping("/client/page/task-log/customer/create")
    public ResponseEntity<ResultDTO> createCustomerAsync(@RequestBody TaskLogAsyncRequest taskLogAsyncRequest) {
        ResultDTO resultDTO = taskLogService.customerCreateAsync(taskLogAsyncRequest);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PostMapping("/client/page/task-log/product/update")
    public ResponseEntity<ResultDTO> productUpdateAsync(@RequestBody TaskLogAsyncRequest taskLogAsyncRequest) {
        ResultDTO resultDTO = taskLogService.productUpdateAsync(taskLogAsyncRequest);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PostMapping("/client/page/task-log/bill/update")
    public ResponseEntity<ResultDTO> createBillAsync(@RequestBody TaskLogAsyncRequest taskLogAsyncRequest) {
        ResultDTO resultDTO = taskLogService.createBillAsync(taskLogAsyncRequest);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PostMapping("/client/page/task-log/invoice/async")
    public ResponseEntity<ResultDTO> invoiceErrorAsync(@RequestBody TaskLogAsyncRequest taskLogAsyncRequest) {
        ResultDTO resultDTO = taskLogService.invoiceErrorAsync(taskLogAsyncRequest);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PostMapping("/client/page/task-log/check-invoice/async")
    public ResponseEntity<ResultDTO> checkInvoiceErrorAsync(@RequestBody TaskLogAsyncCheckInvoiceRequest taskLogAsyncRequest) {
        ResultDTO resultDTO = taskLogService.checkInvoiceErrorAsync(taskLogAsyncRequest);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }
}
