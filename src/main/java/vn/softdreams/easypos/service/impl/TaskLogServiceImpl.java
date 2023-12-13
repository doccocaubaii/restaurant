package vn.softdreams.easypos.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import vn.softdreams.easypos.config.Constants;
import vn.softdreams.easypos.constants.CommonConstants;
import vn.softdreams.easypos.constants.ResultConstants;
import vn.softdreams.easypos.constants.TaskLogConstants;
import vn.softdreams.easypos.domain.TaskLog;
import vn.softdreams.easypos.domain.User;
import vn.softdreams.easypos.dto.TaskLogSendQueue;
import vn.softdreams.easypos.dto.company.CompanyResultItem;
import vn.softdreams.easypos.dto.invoice.CheckInvoiceTaskLogResponse;
import vn.softdreams.easypos.dto.invoice.TaskCheckInvoice;
import vn.softdreams.easypos.dto.queue.ObjectAsyncResponse;
import vn.softdreams.easypos.dto.queue.TaskLogAsyncCheckInvoiceRequest;
import vn.softdreams.easypos.dto.queue.TaskLogAsyncRequest;
import vn.softdreams.easypos.dto.queue.TaskLogAsyncResponse;
import vn.softdreams.easypos.dto.taskLog.TaskLogItemResponse;
import vn.softdreams.easypos.dto.taskLog.TaskLogResultItem;
import vn.softdreams.easypos.dto.taskLog.TaskLogSendQueueRequest;
import vn.softdreams.easypos.integration.TaskLogIdEnqueueMessage;
import vn.softdreams.easypos.integration.easybooks88.queue.EB88Producer;
import vn.softdreams.easypos.integration.easybooks88.queue.dto.AccountingObjectTask;
import vn.softdreams.easypos.integration.easybooks88.queue.dto.MaterialGoodsTask;
import vn.softdreams.easypos.integration.easybooks88.queue.dto.ProductUnitTask;
import vn.softdreams.easypos.integration.easyinvoice.queue.EasyInvoiceProducer;
import vn.softdreams.easypos.repository.*;
import vn.softdreams.easypos.service.TaskLogService;
import vn.softdreams.easypos.service.UserService;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;
import vn.softdreams.easypos.web.rest.errors.InternalServerException;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link TaskLog}.
 */
@Service
@Transactional
public class TaskLogServiceImpl implements TaskLogService, TaskLogConstants {

    private final Logger log = LoggerFactory.getLogger(TaskLogServiceImpl.class);

    private final TaskLogRepository taskLogRepository;
    private final TaskLogRepositoryCustom taskLogRepositoryCustom;
    private final CompanyRepository companyRepository;
    private final UserService userService;
    private final EasyInvoiceProducer easyInvoiceProducer;
    private final EB88Producer eb88Producer;
    private final String ENTITY_NAME = "taskLog";
    private final ProductUnitRepository productUnitRepository;
    private final ProductRepository productRepository;
    private final BillRepository billRepository;
    private final TransactionTemplate transactionTemplate;
    private final InvoiceRepository invoiceRepository;
    private final CustomerRepository customerRepository;

    public TaskLogServiceImpl(
        TaskLogRepository taskLogRepository,
        TaskLogRepositoryCustom taskLogRepositoryCustom,
        CompanyRepository companyRepository,
        UserService userService,
        EasyInvoiceProducer easyInvoiceProducer,
        EB88Producer eb88Producer,
        ProductUnitRepository productUnitRepository,
        ProductRepository productRepository,
        BillRepository billRepository,
        TransactionTemplate transactionTemplate,
        InvoiceRepository invoiceRepository,
        CustomerRepository customerRepository
    ) {
        this.taskLogRepository = taskLogRepository;
        this.taskLogRepositoryCustom = taskLogRepositoryCustom;
        this.companyRepository = companyRepository;
        this.userService = userService;
        this.easyInvoiceProducer = easyInvoiceProducer;
        this.eb88Producer = eb88Producer;
        this.productUnitRepository = productUnitRepository;
        this.productRepository = productRepository;
        this.billRepository = billRepository;
        this.transactionTemplate = transactionTemplate;
        this.invoiceRepository = invoiceRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public TaskLog save(TaskLog taskLog) {
        log.debug("Request to save TaskLog : {}", taskLog);
        return taskLogRepository.save(taskLog);
    }

    @Override
    public TaskLog update(TaskLog taskLog) {
        log.debug("Request to update TaskLog : {}", taskLog);
        return taskLogRepository.save(taskLog);
    }

    @Override
    public Optional<TaskLog> partialUpdate(TaskLog taskLog) {
        log.debug("Request to partially update TaskLog : {}", taskLog);

        return taskLogRepository
            .findById(taskLog.getId())
            .map(existingTaskLog -> {
                if (taskLog.getComId() != null) {
                    existingTaskLog.setComId(taskLog.getComId());
                }
                if (taskLog.getType() != null) {
                    existingTaskLog.setType(taskLog.getType());
                }
                if (taskLog.getContent() != null) {
                    existingTaskLog.setContent(taskLog.getContent());
                }
                if (taskLog.getStatus() != null) {
                    existingTaskLog.setStatus(taskLog.getStatus());
                }
                if (taskLog.getErrorMessage() != null) {
                    existingTaskLog.setErrorMessage(taskLog.getErrorMessage());
                }

                return existingTaskLog;
            })
            .map(taskLogRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskLog> findAll(Pageable pageable) {
        log.debug("Request to get all TaskLogs");
        return taskLogRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public ResultDTO findTaskLogById(Integer id) {
        log.debug("Request to get TaskLog : {}", id);
        User user = userService.getUserWithAuthorities();
        Optional<TaskLog> taskLogResponseOptional = taskLogRepository.findOneById(id);

        if (taskLogResponseOptional.isPresent()) {
            TaskLogItemResponse taskLogResponse = new TaskLogItemResponse();
            BeanUtils.copyProperties(taskLogResponseOptional.get(), taskLogResponse);
            taskLogResponse.setCreateTime(
                taskLogResponseOptional.get().getCreateTime().format(DateTimeFormatter.ofPattern(Constants.ZONED_DATE_TIME_FORMAT))
            );
            return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_GET_DETAIL, true, taskLogResponse, 1);
        }
        throw new InternalServerException(
            ExceptionConstants.TASK_LOG_NOT_FOUND,
            ExceptionConstants.TASK_LOG_NOT_FOUND + id,
            ExceptionConstants.TASK_LOG_NOT_FOUND_VI
        );
    }

    @Override
    public void delete(Integer id) {
        log.debug("Request to delete TaskLog : {}", id);
        taskLogRepository.deleteById(id);
    }

    @Override
    public Page<TaskLogItemResponse> filter(
        Pageable pageable,
        Integer status,
        String taxCode,
        Integer companyOwnerName,
        String fromDate,
        String toDate,
        String type
    ) {
        //        ResultDTO resultDTO = new ResultDTO();
        log.debug("Request to filter taskLog");
        User user = userService.getUserWithAuthorities();

        return taskLogRepositoryCustom.filter(pageable, status, taxCode, companyOwnerName, fromDate, toDate, type);
    }

    @Override
    public List<CompanyResultItem> initDataCompanyCombobox(String input) {
        return companyRepository.findAllCompany(input);
    }

    @Override
    public List<TaskLogResultItem> initDataStatusCombobox(String input) {
        List<TaskLogResultItem> list = new ArrayList<>();
        list.add(new TaskLogResultItem(Status.PROCESSING, Constants.PROCESSING_STATUS));
        list.add(new TaskLogResultItem(Status.OK, Constants.OK_STATUS));
        list.add(new TaskLogResultItem(Status.FAILED, Constants.FAILED_STATUS));
        if (!Strings.isNullOrEmpty(input)) {
            list =
                list
                    .stream()
                    .filter(taskLogCombobox -> taskLogCombobox.getName().toLowerCase().contains(input.toLowerCase()))
                    .collect(Collectors.toList());
        }
        return list;
    }

    @Override
    public List<TaskLogResultItem> initDataTypeCombobox(String input) {
        List<TaskLogResultItem> listType = new ArrayList<>();
        listType.add(new TaskLogResultItem(Status.PROCESSING, Type.PUBLISH_INVOICE));
        listType.add(new TaskLogResultItem(Status.PROCESSING, Type.CHECK_INVOICE_STATUS));
        listType.add(new TaskLogResultItem(Status.PROCESSING, Type.EB_CREATE_ACC_OBJECT));
        listType.add(new TaskLogResultItem(Status.PROCESSING, Type.EB_UPDATE_ACC_OBJECT));
        listType.add(new TaskLogResultItem(Status.PROCESSING, Type.EB_CREATE_MATERIAL_GOODS));
        listType.add(new TaskLogResultItem(Status.PROCESSING, Type.EB_UPDATE_MATERIAL_GOODS));
        listType.add(new TaskLogResultItem(Status.PROCESSING, Type.EB_CREATE_SAINVOICE));
        listType.add(new TaskLogResultItem(Status.PROCESSING, Type.EB_CANCEL_SAINVOICE));
        listType.add(new TaskLogResultItem(Status.PROCESSING, Type.CREATE_ACTIVITY_LOG));
        listType.add(new TaskLogResultItem(Status.PROCESSING, Type.EB_CREATE_PRODUCT_UNIT));
        listType.add(new TaskLogResultItem(Status.PROCESSING, Type.EB_ASYNC_PRODUCT_UNIT));
        listType.add(new TaskLogResultItem(Status.PROCESSING, Type.EB_CREATE_RS_IN_OUT_WARD));
        listType.add(new TaskLogResultItem(Status.PROCESSING, Type.EB_DELETE_RS_IN_OUT_WARD));
        listType.add(new TaskLogResultItem(Status.PROCESSING, Type.EB_FORGOT_PASSWORD));
        if (!Strings.isNullOrEmpty(input)) {
            listType =
                listType
                    .stream()
                    .filter(taskLogCombobox -> taskLogCombobox.getName().toLowerCase().contains(input.toLowerCase()))
                    .collect(Collectors.toList());
        }
        return listType;
    }

    @Override
    public ResultDTO sendQueueAgain(TaskLogSendQueueRequest request) {
        String type = request.getType();
        Integer taskLogId = request.getId();

        Optional<TaskLog> taskLogOptional = taskLogRepository.findOneById(taskLogId);
        if (taskLogOptional.isPresent()) {
            TaskLog taskLog = taskLogOptional.get();
            taskLog.setStatus(Status.PROCESSING);
            taskLog.setErrorMessage("");
            taskLogRepository.save(taskLog);
            if (Type.PUBLISH_INVOICE.equals(type)) {
                easyInvoiceProducer.issueInvoice(new TaskLogIdEnqueueMessage(taskLogId));
            } else if (Type.CHECK_INVOICE.equals(type) || Type.CHECK_INVOICE_STATUS.equals(type)) {
                easyInvoiceProducer.checkInvoice(new TaskLogIdEnqueueMessage(taskLogId));
            } else if (Type.IMPORT_INVOICE.equals(type)) {
                easyInvoiceProducer.importInvoice(new TaskLogIdEnqueueMessage(taskLogId));
            } else if (
                Type.EB_CREATE_MATERIAL_GOODS.equals(type) ||
                Type.EB_UPDATE_MATERIAL_GOODS.equals(type) ||
                Type.EB_CREATE_ACC_OBJECT.equals(type) ||
                Type.EB_UPDATE_ACC_OBJECT.equals(type) ||
                Type.EB_CREATE_SAINVOICE.equals(type) ||
                Type.EB_CANCEL_SAINVOICE.equals(type) ||
                Type.CREATE_ACTIVITY_LOG.equals(type) ||
                Type.EB_CREATE_PRODUCT_UNIT.equals(type) ||
                Type.EB_ASYNC_PRODUCT_UNIT.equals(type) ||
                Type.EB_CREATE_RS_IN_OUT_WARD.equals(type) ||
                Type.EB_DELETE_RS_IN_OUT_WARD.equals(type) ||
                Type.EB_CHANGE_PASSWORD.equals(type) ||
                Type.EB_FORGOT_PASSWORD.equals(type)
            ) {
                eb88Producer.send(new TaskLogIdEnqueueMessage(taskLogId));
            }
            return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_SEND_DETAIL, true);
        }
        throw new InternalServerException(ExceptionConstants.TASK_LOG_NOT_FOUND, ENTITY_NAME, ExceptionConstants.TASK_LOG_NOT_FOUND_VI);
    }

    @Override
    public ResultDTO unitCreateAsync(TaskLogAsyncRequest taskLogAsyncRequest) {
        // Lấy tất cả danh sách unit và tạo lại bên EB88
        List<ObjectAsyncResponse> listUnits = productUnitRepository.findAllByComIds(taskLogAsyncRequest.getIds());
        return asyncTaskLogByType(listUnits, TaskLogConstants.Type.EB_CREATE_PRODUCT_UNIT);
    }

    @Override
    public ResultDTO productCreateAsync(TaskLogAsyncRequest taskLogAsyncRequest) {
        List<ObjectAsyncResponse> listUnits = productRepository.findAllByComIds(taskLogAsyncRequest.getIds());
        return asyncTaskLogByType(listUnits, TaskLogConstants.Type.EB_CREATE_MATERIAL_GOODS);
    }

    @Override
    public ResultDTO customerCreateAsync(TaskLogAsyncRequest taskLogAsyncRequest) {
        List<ObjectAsyncResponse> customers = customerRepository.findAllByComIds(
            taskLogAsyncRequest.getIds(),
            CommonConstants.CUSTOMER_CODE_DEFAULT
        );
        return asyncTaskLogByType(customers, TaskLogConstants.Type.EB_CREATE_ACC_OBJECT);
    }

    @Override
    public ResultDTO checkInvoiceErrorAsync(TaskLogAsyncCheckInvoiceRequest taskLogAsyncRequest) {
        List<CheckInvoiceTaskLogResponse> list = new ArrayList<>();
        if (taskLogAsyncRequest.getIkeys() != null && !taskLogAsyncRequest.getIkeys().isEmpty()) {
            list = invoiceRepository.findComAndIkeyByIkey(taskLogAsyncRequest.getIkeys());
        }
        Map<Integer, List<String>> map = new HashMap<>();
        for (CheckInvoiceTaskLogResponse item : list) {
            if (item.getIkey() != null && item.getId() != null) {
                List<String> listIkey = new ArrayList<>();
                if (map.containsKey(item.getId())) {
                    listIkey = map.get(item.getId());
                }
                listIkey.add(item.getIkey());
                if (!listIkey.isEmpty()) {
                    map.put(item.getId(), listIkey);
                }
            }
        }
        List<TaskLog> taskLogs = new ArrayList<>();
        List<TaskLog> finalTaskLogs = taskLogs;
        taskLogs =
            transactionTemplate.execute(status -> {
                for (Map.Entry<Integer, List<String>> value : map.entrySet()) {
                    // Xử lý value tại đây
                    TaskLog taskLog = new TaskLog();
                    taskLog.setComId(value.getKey());
                    taskLog.setType(Type.CHECK_INVOICE);
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        objectMapper.disable(SerializationFeature.INDENT_OUTPUT);
                        taskLog.setContent(
                            objectMapper.writeValueAsString(new TaskCheckInvoice(value.getKey().toString(), value.getValue()))
                        );
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    finalTaskLogs.add(taskLog);
                    taskLogRepository.saveAll(finalTaskLogs);
                }
                return finalTaskLogs;
            });
        for (TaskLog item : taskLogs) {
            userService.sendTaskLog(new TaskLogSendQueue(item.getId().toString(), item.getType()));
        }
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS);
    }

    @Override
    public ResultDTO productUpdateAsync(TaskLogAsyncRequest taskLogAsyncRequest) {
        List<ObjectAsyncResponse> listUnits = productRepository.findAllByComIds(taskLogAsyncRequest.getIds());
        return asyncTaskLogByType(listUnits, TaskLogConstants.Type.EB_UPDATE_MATERIAL_GOODS);
    }

    @Override
    public ResultDTO createBillAsync(TaskLogAsyncRequest taskLogAsyncRequest) {
        List<TaskLog> listUnits = taskLogRepository.findAllErrorAsyncByComIdsAndTypes(
            taskLogAsyncRequest.getIds(),
            Arrays.asList(TaskLogConstants.Type.EB_CREATE_SAINVOICE)
        );
        return reSendQueueByTaskLogs(listUnits);
    }

    @Override
    public ResultDTO invoiceErrorAsync(TaskLogAsyncRequest taskLogAsyncRequest) {
        List<TaskLog> listUnits = taskLogRepository.findAllErrorAsyncByComIdsAndTypes(
            taskLogAsyncRequest.getIds(),
            Arrays.asList(TaskLogConstants.Type.PUBLISH_INVOICE, TaskLogConstants.Type.IMPORT_INVOICE)
        );
        return reSendQueueByTaskLogs(listUnits);
    }

    public ResultDTO reSendQueueByTaskLogs(List<TaskLog> listUnits) {
        if (listUnits.size() > 0) {
            List<TaskLog> finalTaskLogSaves = new ArrayList<>();

            finalTaskLogSaves =
                transactionTemplate.execute(status -> {
                    List<TaskLog> taskLogSaves = new ArrayList<>();
                    for (TaskLog item : listUnits) {
                        TaskLog taskLog = new TaskLog();
                        BeanUtils.copyProperties(item, taskLog);
                        taskLog.setId(null);
                        taskLogSaves.add(taskLog);
                    }
                    taskLogRepository.saveAll(taskLogSaves);
                    return taskLogSaves;
                });

            Map<String, TaskLogAsyncResponse> mapCompany = new HashMap<>();
            for (TaskLog itemTask : finalTaskLogSaves) {
                userService.sendTaskLog(new TaskLogSendQueue(itemTask.getId().toString(), itemTask.getType()));
                TaskLogAsyncResponse task = new TaskLogAsyncResponse();
                if (mapCompany.containsKey(itemTask.getComId().toString())) {
                    task = mapCompany.get(itemTask.getComId().toString());
                    task.setCount(task.getCount() + 1);
                } else {
                    task = new TaskLogAsyncResponse(itemTask.getComId(), 1);
                }
                mapCompany.put(itemTask.getComId().toString(), task);
            }
            return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_SEND_DETAIL, true, new ArrayList<>(mapCompany.values()));
        } else {
            return new ResultDTO(ResultConstants.FALSE, ResultConstants.SEND_TASK_LOG_NOT_FOUND);
        }
    }

    public ResultDTO asyncTaskLogByType(List<ObjectAsyncResponse> listUnits, String type) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<TaskLog> taskLogs = new ArrayList<>();
        Map<Integer, String> customerIdAndTypeMap = new HashMap<>();
        if (TaskLogConstants.Type.EB_CREATE_MATERIAL_GOODS.equals(type)) {
            listUnits.forEach(objectAsyncResponse ->
                customerIdAndTypeMap.put(objectAsyncResponse.getId(), objectAsyncResponse.getType().toString())
            );
        }
        for (ObjectAsyncResponse item : listUnits) {
            TaskLog taskLog = new TaskLog();
            taskLog.setComId(item.getComId());
            taskLog.setType(type);
            String data = null;
            try {
                switch (type) {
                    case TaskLogConstants.Type.EB_CREATE_PRODUCT_UNIT:
                        {
                            data =
                                objectMapper.writeValueAsString(new ProductUnitTask(item.getComId().toString(), item.getId().toString()));
                            break;
                        }
                    case TaskLogConstants.Type.EB_CREATE_MATERIAL_GOODS:
                    case TaskLogConstants.Type.EB_UPDATE_MATERIAL_GOODS:
                        {
                            data =
                                objectMapper.writeValueAsString(new MaterialGoodsTask(item.getComId().toString(), item.getId().toString()));
                            break;
                        }
                    case Type.EB_CREATE_ACC_OBJECT:
                        {
                            data =
                                objectMapper.writeValueAsString(
                                    new AccountingObjectTask(
                                        item.getComId().toString(),
                                        item.getId().toString(),
                                        customerIdAndTypeMap.get(item.getId())
                                    )
                                );
                            break;
                        }
                }

                taskLog.setStatus(TaskLogConstants.Status.PROCESSING);
                taskLog.setContent(data);
                taskLogs.add(taskLog);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        if (taskLogs.size() > 0) {
            List<TaskLog> finalTaskLogs = taskLogs;
            taskLogs =
                transactionTemplate.execute(status -> {
                    taskLogRepository.saveAll(finalTaskLogs);
                    return finalTaskLogs;
                });
            Map<String, TaskLogAsyncResponse> mapCompany = new HashMap<>();
            for (TaskLog itemTask : taskLogs) {
                userService.sendTaskLog(new TaskLogSendQueue(itemTask.getId().toString(), itemTask.getType()));
                TaskLogAsyncResponse task = new TaskLogAsyncResponse();
                if (mapCompany.containsKey(itemTask.getComId().toString())) {
                    task = mapCompany.get(itemTask.getComId().toString());
                    task.setCount(task.getCount() + 1);
                } else {
                    task = new TaskLogAsyncResponse(itemTask.getComId(), 1);
                }
                mapCompany.put(itemTask.getComId().toString(), task);
            }
            return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_SEND_DETAIL, true, new ArrayList<>(mapCompany.values()));
        } else {
            return new ResultDTO(ResultConstants.FALSE, ResultConstants.SEND_TASK_LOG_NOT_FOUND);
        }
    }

    @Scheduled(cron = CommonConstants.Schedule.DELETE_TASK_LOG)
    public void scheduleDeleteTaskLog() {
        taskLogRepository.deleteTaskLogBeforeFourMonths();
        //        taskLogRepository.deleteAll(taskLogs);
    }
}
