package vn.softdreams.easypos.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.Cache;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.client.RestTemplate;
import vn.softdreams.easypos.config.Constants;
import vn.softdreams.easypos.constants.*;
import vn.softdreams.easypos.domain.*;
import vn.softdreams.easypos.dto.TaskLogSendQueue;
import vn.softdreams.easypos.dto.authorities.ConfigInvoice;
import vn.softdreams.easypos.dto.authorities.JwtDTO;
import vn.softdreams.easypos.dto.bill.BillCodeResult;
import vn.softdreams.easypos.dto.companyUser.CompanyOwnerResponse;
import vn.softdreams.easypos.dto.invoice.*;
import vn.softdreams.easypos.integration.easybooks88.util.Utils;
import vn.softdreams.easypos.integration.easyinvoice.api.EasyInvoiceApiClient;
import vn.softdreams.easypos.integration.easyinvoice.dto.GetInvoicePatternsEasyInvoiceResponse;
import vn.softdreams.easypos.integration.easyinvoice.dto.GetInvoicePdfEasyInvoiceRequest;
import vn.softdreams.easypos.integration.easyinvoice.dto.SendIssuanceNoticeEasyInvoiceRequest;
import vn.softdreams.easypos.integration.easyinvoice.queue.EasyInvoiceProducer;
import vn.softdreams.easypos.integration.easyinvoice.utils.EasyInvoiceUtils;
import vn.softdreams.easypos.repository.*;
import vn.softdreams.easypos.service.ConfigManagementService;
import vn.softdreams.easypos.service.InvoiceManagementService;
import vn.softdreams.easypos.service.UserService;
import vn.softdreams.easypos.service.dto.DataResponse;
import vn.softdreams.easypos.service.dto.PublishListRequest;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.util.Common;
import vn.softdreams.easypos.util.CommonIntegrated;
import vn.softdreams.easypos.web.rest.errors.BadRequestAlertException;
import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;
import vn.softdreams.easypos.web.rest.errors.IntegrationException;
import vn.softdreams.easypos.web.rest.errors.InternalServerException;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static vn.softdreams.easypos.config.Constants.*;
import static vn.softdreams.easypos.web.rest.errors.ExceptionConstants.*;

@Service
@Transactional
public class InvoiceManagementServiceImpl implements InvoiceManagementService {

    private final Logger log = LoggerFactory.getLogger(GenCodeServiceImpl.class);
    private final UserService userService;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceProductRepository invoiceProductRepository;
    private final TaskLogRepository taskLogRepository;
    private final ConfigRepository configRepository;
    private final EasyInvoiceProducer easyInvoiceProducer;
    private final EasyInvoiceApiClient easyInvoiceApiClient;
    private final CompanyRepository companyRepository;
    private final CustomerRepository customerRepository;
    private final BillRepository billRepository;
    private final TransactionTemplate transactionTemplate;
    private final ConfigManagementService configManagementService;
    private final RestTemplate restTemplate;
    private final Cache cache;
    private final String DB_URL_CONFIG = "easyinvoice_url";
    private final String DB_ACCOUNT_CONFIG = "easyinvoice_account";
    private final String DB_PASSWORD_CONFIG = "easyinvoice_pass";
    private final String ENTITY_NAME = "invoice";

    public InvoiceManagementServiceImpl(
        UserService userService,
        InvoiceRepository invoiceRepository,
        InvoiceProductRepository invoiceProductRepository,
        TaskLogRepository taskLogRepository,
        ConfigRepository configRepository,
        EasyInvoiceProducer easyInvoiceProducer,
        CompanyRepository companyRepository,
        EasyInvoiceApiClient easyInvoiceApiClient,
        CustomerRepository customerRepository,
        BillRepository billRepository,
        TransactionTemplate transactionTemplate,
        ConfigManagementService configManagementService,
        RestTemplate restTemplate,
        Cache cache
    ) {
        this.userService = userService;
        this.invoiceRepository = invoiceRepository;
        this.invoiceProductRepository = invoiceProductRepository;
        this.taskLogRepository = taskLogRepository;
        this.configRepository = configRepository;
        this.easyInvoiceProducer = easyInvoiceProducer;
        this.easyInvoiceApiClient = easyInvoiceApiClient;
        this.companyRepository = companyRepository;
        this.customerRepository = customerRepository;
        this.billRepository = billRepository;
        this.transactionTemplate = transactionTemplate;
        this.configManagementService = configManagementService;
        this.restTemplate = restTemplate;
        this.cache = cache;
    }

    @Override
    public ResultDTO issueInvoices(List<PublishInvoiceRequest> publishInvoiceRequests) {
        User user = userService.getUserWithAuthorities();
        Map<Integer, String> mapInvoice = new HashMap<>();
        List<Integer> ids = new ArrayList<>();
        for (PublishInvoiceRequest item : publishInvoiceRequests) {
            ids.add(item.getInvoiceId());
            mapInvoice.put(item.getInvoiceId(), item.getInvoiceIKey());
        }
        List<InvoiceSearchResponse> invoices = invoiceRepository.findAllByCompanyIdAndIds(ids, user.getCompanyId());
        //        Nếu tìm thấy invoice thì gửi dữ liệu lên rabbit
        if (invoices != null && invoices.size() == publishInvoiceRequests.size()) {
            ObjectMapper objectMapper = Common.getObjectMapper();
            try {
                Optional<String> invoiceMethodOptional = configRepository.getValueByComIdAndCode(
                    user.getCompanyId(),
                    EasyInvoiceConstants.INVOICE_METHOD
                );
                int invoiceMethod = invoiceMethodOptional.isPresent() ? Integer.parseInt(invoiceMethodOptional.get()) : 0;
                List<TaskLog> taskLogs = new ArrayList<>();
                String method = invoiceMethod == 2 ? TaskLogConstants.Type.IMPORT_INVOICE : TaskLogConstants.Type.PUBLISH_INVOICE;
                for (InvoiceSearchResponse item : invoices) {
                    if (!mapInvoice.containsKey(item.getId()) || !mapInvoice.get(item.getId()).equals(item.getIkey())) {
                        throw new InternalServerException(
                            ExceptionConstants.INVOICE_COUNT_FAIL_VI,
                            "issueInvoices",
                            ExceptionConstants.INVOICE_COUNT_FAIL
                        );
                    }
                    if (cache.get(item.getId() + method) == null) {
                        cache.put(item.getId() + method, user.getCompanyId());
                        TaskLog taskLog = new TaskLog();
                        taskLog.setComId(user.getCompanyId());
                        taskLog.setType(method);
                        String data = objectMapper.writeValueAsString(
                            new PublishInvoiceQueue(user.getCompanyId().toString(), item.getId().toString())
                        );
                        taskLog.setStatus(TaskLogConstants.Status.PROCESSING);
                        taskLog.setContent(data);
                        taskLogs.add(taskLog);
                    }
                }
                if (taskLogs.size() > 0) {
                    transactionTemplate.execute(status -> {
                        taskLogRepository.saveAll(taskLogs);
                        return taskLogs;
                    });
                    for (TaskLog task : taskLogs) {
                        userService.sendTaskLog(new TaskLogSendQueue(task.getId().toString(), task.getType()));
                    }
                }
                return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_SEND_DETAIL, true);
            } catch (JsonProcessingException e) {
                log.error("issueInvoices_saveError:");
                for (StackTraceElement item : e.getStackTrace()) {
                    log.error(item.toString());
                }
            }
        }
        throw new InternalServerException(ExceptionConstants.INVOICE_COUNT_FAIL_VI, "issueInvoices", ExceptionConstants.INVOICE_COUNT_FAIL);
    }

    @Override
    public ResultDTO getInvoicePatterns(Integer comId) throws Exception {
        // check null + check chéo với token
        User user = userService.getUserWithAuthorities(comId);
        // gọi api lấy pattern
        List<GetInvoicePatternsEasyInvoiceResponse> listFiltered = getPatternsInvoice(comId);
        if (listFiltered.size() == 1 && !Strings.isNullOrEmpty(listFiltered.get(0).getPattern())) {
            Optional<Config> configOptional = configRepository.findByCompanyIdAndCode(
                user.getCompanyId(),
                EasyInvoiceConstants.INVOICE_PATTERN
            );
            if (configOptional.isPresent()) {
                Config config = configOptional.get();
                config.setValue(listFiltered.get(0).getPattern());
                configRepository.save(config);
            }
        }
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_SEND_DETAIL, true, listFiltered);
    }

    @Override
    @Transactional(readOnly = true)
    public ResultDTO getOwnerInfo(Integer comId) {
        User user = userService.getUserWithAuthorities(comId);
        CompanyOwnerResponse companyOwner = companyRepository.findCompanyOwnerByCompanyId(comId);
        setConfigsForCompany(companyOwner, configRepository.getAllByCompanyID(comId, ROUND_SCALE));
        List<GetInvoicePatternsEasyInvoiceResponse> listFiltered = getPatternsInvoice(comId);
        companyOwner.setPatterns(listFiltered);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_GET_LIST, true, companyOwner);
    }

    public List<GetInvoicePatternsEasyInvoiceResponse> getPatternsInvoice(Integer comId) {
        User user = userService.getUserWithAuthorities();

        Optional<String> urlConfigOptional = configRepository.getValueByComIdAndCode(comId, DB_URL_CONFIG);
        HttpHeaders headers = null;
        try {
            JwtDTO jwtDTO = userService.getInfoJwt();
            if (!Strings.isNullOrEmpty(jwtDTO.getService()) && jwtDTO.getService().equals(UserConstants.Service.NGP)) {
                ConfigInvoice configInvoiceDTO = configManagementService.getConfigInvoiceByCompanyID(user);
                List<DeclarationGiaPhatResponse> declarationSearch = CommonIntegrated.declarationSearchNgoGiaPhat(
                    new DeclarationGiaPhatRequest(user.getTaxCode()),
                    configInvoiceDTO.getEasyInvoiceAccount(),
                    configInvoiceDTO.getEasyInvoicePass(),
                    configInvoiceDTO.getEasyInvoiceUrl(),
                    restTemplate
                );
                List<GetInvoicePatternsEasyInvoiceResponse> result = new ArrayList<>();
                for (DeclarationGiaPhatResponse item : declarationSearch) {
                    result.add(new GetInvoicePatternsEasyInvoiceResponse(item.getStartDate(), item.getPattern(), item.getStatus()));
                }
                return result;
            } else {
                headers = getHeaders(comId, urlConfigOptional);
                List<GetInvoicePatternsEasyInvoiceResponse> responses = easyInvoiceApiClient.getInvoicePatterns(
                    urlConfigOptional.get(),
                    headers
                );
                // lọc status 1 2
                List<GetInvoicePatternsEasyInvoiceResponse> listFiltered = responses
                    .stream()
                    .filter(i -> i.getStatus() == 1 || i.getStatus() == 2)
                    .collect(Collectors.toList());
                return listFiltered;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Boolean checkStatusInvoice(Integer taxCheckStatus, Integer id, String pattern) {
        return (
            cache.get(id + TaskLogConstants.Type.CHECK_INVOICE) == null &&
            pattern != null &&
            !pattern.matches(TAX_AUTHORITY_CODE_DEFAULT) &&
            (taxCheckStatus == null || !taxCheckStatus.equals(InvoiceConstants.TaxCheckStatus.HOP_LE))
        );
    }

    public ResultDTO getInvoicePdf(int invoiceId, String ikey) throws Exception {
        log.info("Process with invoiceId = {}, ikey = {}", invoiceId, ikey);
        Optional<Invoice> invoiceOptional = invoiceRepository.findById(invoiceId);
        if (invoiceOptional.isEmpty()) {
            log.error("Invoice is not found");
            throw new IntegrationException(IntegrationException.Party.EasyInvoice, "Không tìm thấy hóa đơn");
        }
        Invoice invoice = invoiceOptional.get();
        User user = userService.getUserWithAuthorities();

        int comId = user.getCompanyId();

        Optional<String> urlConfigOptional = configRepository.getValueByComIdAndCode(comId, DB_URL_CONFIG);
        HttpHeaders headers = getHeaders(comId, urlConfigOptional);
        var easyInvoiceRequest = new GetInvoicePdfEasyInvoiceRequest();
        easyInvoiceRequest.setIkey(ikey);
        easyInvoiceRequest.setOption("0");
        easyInvoiceRequest.setPattern(invoice.getPattern());

        return new ResultDTO(
            ResultConstants.SUCCESS,
            ResultConstants.SUCCESS_SEND_DETAIL,
            true,
            easyInvoiceApiClient.getInvoicePdf(urlConfigOptional.get(), easyInvoiceRequest, headers)
        );
    }

    @Override
    public ResultDTO sendIssuanceNotice(SendIssuanceNoticeRequest request) throws Exception {
        log.info("Process with invoiceId = {}, ikey = {}", request.getInvoiceId(), request.getIkey());
        Optional<Invoice> invoiceOptional = invoiceRepository.findById(request.getInvoiceId());
        if (invoiceOptional.isEmpty()) {
            log.error("Invoice is not found");
            throw new IntegrationException(IntegrationException.Party.EasyInvoice, "Không tìm thấy hóa đơn");
        }
        Invoice invoice = invoiceOptional.get();
        User user = userService.getUserWithAuthorities();

        int comId = user.getCompanyId();
        Optional<String> urlConfigOptional = configRepository.getValueByComIdAndCode(comId, DB_URL_CONFIG);
        HttpHeaders headers = getHeaders(comId, urlConfigOptional);
        SendIssuanceNoticeEasyInvoiceRequest easyInvoiceRequest = new SendIssuanceNoticeEasyInvoiceRequest();
        Map<String, String> ikeyEmails = new HashMap<>();
        if (Utils.isValidEmail(request.getEmails())) {
            if (request.getEmails().contains(",")) {
                String[] items = request.getEmails().split("\\s*,\\s*");
                for (String item : items) {
                    ikeyEmails.put(request.getIkey(), item);
                }
            } else ikeyEmails.put(request.getIkey(), request.getEmails());
        } else {
            log.error("Invalid email");
            throw new IntegrationException(IntegrationException.Party.EasyInvoice, "Email truyền vào không hợp lệ");
        }
        easyInvoiceRequest.setIkeyEmail(ikeyEmails);

        return new ResultDTO(
            ResultConstants.SUCCESS,
            ResultConstants.SUCCESS_SEND_DETAIL,
            true,
            easyInvoiceApiClient.sendIssuanceNotice(urlConfigOptional.get(), easyInvoiceRequest, headers)
        );
    }

    public HttpHeaders getHeaders(int comId, Optional<String> urlConfigOptional) throws Exception {
        Optional<String> accountConfigOptional = configRepository.getValueByComIdAndCode(comId, DB_ACCOUNT_CONFIG);
        Optional<String> passConfigOptional = configRepository.getValueByComIdAndCode(comId, DB_PASSWORD_CONFIG);

        if (urlConfigOptional.isEmpty()) {
            log.error("URL (easyinvoice_url) is empty for comId = {}", comId);
            throw new IntegrationException(IntegrationException.Party.EasyInvoice, "URL đang bỏ trống");
        } else {
            if (!EasyInvoiceUtils.isValidURL(urlConfigOptional.get())) {
                log.error("URL (easyinvoice_url) is invalid for comId = {}", comId);
                throw new IntegrationException(IntegrationException.Party.EasyInvoice, "URL không hợp lệ");
            }
        }
        if (accountConfigOptional.isEmpty()) {
            log.error("Account (easyinvoice_account) not found for comId = {}", comId);
            throw new Exception("Tài khoản không hợp lệ");
        }
        if (passConfigOptional.isEmpty()) {
            log.error("Password (easyinvoice_pass) is empty for comId = {}", comId);
            throw new Exception("Mật khẩu đang bỏ trống");
        }

        return easyInvoiceApiClient.initHeaders(accountConfigOptional.get(), passConfigOptional.get(), HttpMethod.POST);
    }

    //    @Transactional(readOnly = true)
    public ResultDTO getAllInvoices(
        Integer taxCheckStatus,
        String fromDate,
        String toDate,
        String pattern,
        String customerName,
        String no,
        Pageable pageable,
        boolean isCountAll
    ) {
        User user = userService.getUserWithAuthorities();
        Integer comId = user.getCompanyId();
        Page<InvoiceListResponse> invoices = invoiceRepository.getAllInvoices(
            taxCheckStatus,
            fromDate,
            toDate,
            pattern,
            customerName,
            no,
            comId,
            pageable,
            isCountAll,
            false,
            null
        );
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCount((int) invoices.getTotalElements());
        if (invoices.getContent().isEmpty()) {
            return new ResultDTO(ResultConstants.FAIL, INVOICE_NOT_FOUND_VI, false, invoices.getContent());
        } else {
            resultDTO.setData(invoices.getContent());
            resultDTO.setStatus(true);
            resultDTO.setMessage(ResultConstants.SUCCESS);
            resultDTO.setReason(ResultConstants.GET_ALL_INVOICES_SUCCESS);
            Optional<String> invoiceMethodOptional = configRepository.getValueByComIdAndCode(
                user.getCompanyId(),
                EasyInvoiceConstants.INVOICE_METHOD
            );
            if (invoiceMethodOptional.isPresent()) {
                List<String> iKeys = new ArrayList<>();
                int invoiceMethod = Integer.parseInt(invoiceMethodOptional.get());
                String taskLogType = TaskLogConstants.Type.CHECK_INVOICE;
                //                if (
                //                    invoiceMethod == InvoiceConstants.InvoiceMethod.TU_DONG ||
                //                    invoiceMethod == InvoiceConstants.InvoiceMethod.HOA_DON_MOI_TAO_LAP
                //                ) {
                //                    taskLogType =TaskLogConstants.Type.CHECK_INVOICE;
                //                        (invoiceMethod == InvoiceConstants.InvoiceMethod.TU_DONG)
                //                            ? TaskLogConstants.Type.CHECK_INVOICE
                //                            : TaskLogConstants.Type.IMPORT_INVOICE;
                for (InvoiceListResponse item : invoices) {
                    if (checkStatusInvoice(item.getTaxCheckStatus(), item.getId(), item.getTaxAuthorityCode())) {
                        cache.put(item.getId() + taskLogType, item.getIkey());
                        iKeys.add(item.getIkey());
                    }
                }
                //                }
                if (!iKeys.isEmpty()) {
                    TaskLog taskLog = new TaskLog();
                    taskLog.setComId(user.getCompanyId());
                    taskLog.setType(taskLogType);
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        objectMapper.disable(SerializationFeature.INDENT_OUTPUT);
                        taskLog.setContent(objectMapper.writeValueAsString(new TaskCheckInvoice(comId.toString(), iKeys)));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    sendTaskLog(taskLog);
                    //                    sendQueueInvoice(invoiceMethod, taskLog.getId());
                }
            }
        }
        return resultDTO;
    }

    //    @Transactional(readOnly = true)
    public ResultDTO getInvoiceById(Integer id) {
        log.debug("Request to get Invoice by id: {}", id);
        ResultDTO resultDTO = new ResultDTO();
        User user = userService.getUserWithAuthorities();
        Integer comId = user.getCompanyId();
        Optional<Invoice> invoiceOptional = invoiceRepository.findByIdAndCompanyId(id, comId, TAX_AUTHORITY_CODE_DEFAULT);
        if (invoiceOptional.isEmpty()) {
            throw new InternalServerException(INVOICE_NOT_FOUND_VI, INVOICE_NOT_FOUND_VI, INVOICE_NOT_FOUND);
        }
        Invoice invoice = invoiceOptional.get();
        BillCodeResult billCodeResult = billRepository.findBillCodeByIdAndComId(invoice.getBillId(), comId);
        //      thêm billcode và typeInv
        if (billCodeResult != null) {
            invoice.setBillCode(billCodeResult.getBillCode());
            invoice.setTypeInv(billCodeResult.getTypeInv());
        }
        List<String> iKeys = new ArrayList<>();
        Optional<String> invoiceMethodOptional = configRepository.getValueByComIdAndCode(
            user.getCompanyId(),
            EasyInvoiceConstants.INVOICE_METHOD
        );
        int invoiceMethod = Integer.parseInt(invoiceMethodOptional.get());
        String taskLogType = "";
        //        if (
        //            invoiceMethod == InvoiceConstants.InvoiceMethod.TU_DONG || invoiceMethod == InvoiceConstants.InvoiceMethod.HOA_DON_MOI_TAO_LAP
        //        ) {
        taskLogType = TaskLogConstants.Type.CHECK_INVOICE;
        if (checkStatusInvoice(invoice.getTaxCheckStatus(), invoice.getId(), invoice.getTaxAuthorityCode())) {
            cache.put(invoice.getId() + taskLogType, invoice.getIkey());
            iKeys.add(invoice.getIkey());
        }
        //        }
        if (!iKeys.isEmpty()) {
            TaskLog taskLog = new TaskLog();
            taskLog.setComId(user.getCompanyId());
            taskLog.setType(taskLogType);
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.disable(SerializationFeature.INDENT_OUTPUT);
                taskLog.setContent(objectMapper.writeValueAsString(new TaskCheckInvoice(comId.toString(), iKeys)));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            sendTaskLog(taskLog);
            //            sendQueueInvoice(invoiceMethod, taskLog.getId());
        }
        InvoiceDetailResponse response = new InvoiceDetailResponse();
        BeanUtils.copyProperties(invoice, response);
        response.setHaveDiscountVat(Boolean.FALSE);
        if (invoice.getDiscountVatRate() != null && invoice.getDiscountVatAmount() != null) {
            response.setHaveDiscountVat(Boolean.TRUE);
        }
        resultDTO.setData(response);
        resultDTO.setStatus(true);
        resultDTO.setMessage(ResultConstants.SUCCESS);
        resultDTO.setReason(ResultConstants.GET_INVOICE_DETAIL_SUCCESS);
        return resultDTO;
    }

    public void sendTaskLog(TaskLog taskLog) {
        transactionTemplate.execute(status -> {
            taskLogRepository.save(taskLog);
            return taskLog;
        });
        userService.sendTaskLog(new TaskLogSendQueue(taskLog.getId().toString(), taskLog.getType()));
    }

    //    @Async
    //    public void sendQueueInvoice(Integer method, Integer taskLogId) {
    //        easyInvoiceProducer.checkInvoice(new TaskLogIdEnqueueMessage(taskLogId));
    //    }
    //
    //    @Async
    //    public void sendQueueIssueInvoice(Integer method, Integer taskLogId) {
    //        easyInvoiceProducer.issueInvoice(new TaskLogIdEnqueueMessage(taskLogId));
    //    }

    @Override
    public ResultDTO updateInvoice(Integer id, InvoiceRequest invoiceRequest) {
        if (!id.equals(invoiceRequest.getId())) {
            throw new BadRequestAlertException(ID_INVALID_VI, ID_INVALID_VI, ID_INVALID);
        }
        User user = userService.getUserWithAuthorities();
        Optional<Invoice> invoiceOptional = invoiceRepository.findByIdAndCompanyId(id, user.getCompanyId(), TAX_AUTHORITY_CODE_DEFAULT);
        if (invoiceOptional.isPresent()) {
            Invoice invoice = invoiceOptional.get();
            if (invoiceRequest.getCustomerId() != null) {
                if (!Objects.equals(invoice.getCustomerId(), invoiceRequest.getCustomerId())) {
                    Optional<Customer> customerOptional = customerRepository.findByIdAndComIdAndActive(
                        invoiceRequest.getCustomerId(),
                        user.getCompanyId(),
                        true
                    );
                    if (customerOptional.isEmpty()) {
                        throw new BadRequestAlertException(
                            ExceptionConstants.CUSTOMER_NOT_FOUND_VI,
                            ExceptionConstants.CUSTOMER_NOT_FOUND_VI,
                            ExceptionConstants.CUSTOMER_NOT_FOUND
                        );
                    }
                    Customer customer = customerOptional.get();
                    invoice.setCustomerId(customer.getId());
                    invoice.setCustomerName(customer.getName());
                    invoice.setCustomerPhone(customer.getPhoneNumber());
                    invoice.setCustomerAddress(customer.getAddress());
                    invoice.setCustomerTaxCode(customer.getTaxCode());
                    invoice.setIdNumber(customer.getIdNumber());
                }
            }
            if (!Strings.isNullOrEmpty(invoiceRequest.getPaymentMethod())) {
                invoice.setPaymentMethod(invoiceRequest.getPaymentMethod());
            }
            if (invoiceRequest.getAmount() != null) {
                invoice.setAmount(invoiceRequest.getAmount());
            }
            if (invoiceRequest.getDiscountAmount() != null) {
                invoice.setDiscountAmount(invoiceRequest.getDiscountAmount());
            }
            if (invoiceRequest.getTotalPreTax() != null) {
                invoice.setTotalPreTax(invoiceRequest.getTotalPreTax());
            }
            if (invoiceRequest.getTotalAmount() != null) {
                invoice.setTotalAmount(invoiceRequest.getTotalAmount());
            }
            if (invoiceRequest.getVatRate() != null) {
                Optional<Config> configOptional = configRepository.findByCompanyIdAndCode(
                    user.getCompanyId(),
                    EasyInvoiceConstants.INVOICE_TYPE
                );
                List<String> invoiceTypes = List.of(
                    String.valueOf(BillConstants.TypeInv.MOT_THUE),
                    String.valueOf(BillConstants.TypeInv.NHIEU_THUE)
                );
                if (
                    configOptional.isPresent() &&
                    invoiceTypes.contains(configOptional.get().getValue()) &&
                    invoiceRequest.getVatRate() == ProductConstant.VatRate.VAT_RATE_DEFAULT
                ) {
                    invoice.setVatRate(0);
                } else {
                    invoice.setVatRate(invoiceRequest.getVatRate());
                }
            }
            if (invoiceRequest.getVatAmount() != null) {
                invoice.setVatAmount(invoiceRequest.getVatAmount());
            }
            if (!Strings.isNullOrEmpty(invoiceRequest.getArisingDate())) {
                ZonedDateTime arisingDate = Common.convertStringToDateTime(
                    invoiceRequest.getArisingDate(),
                    Constants.ZONED_DATE_TIME_INVOICE_FORMAT
                );
                invoice.setArisingDate(arisingDate);
                if (arisingDate.compareTo(ZonedDateTime.now()) > 0) {
                    throw new BadRequestAlertException(ARISING_DATE_IS_VALID_VI, ARISING_DATE_IS_VALID_VI, ARISING_DATE_IS_VALID);
                }
            }
            invoice.setCustomerNormalizedName(Common.normalizedName(Collections.singletonList(invoice.getCustomerName())));
            invoiceRepository.save(invoice);
        } else {
            throw new BadRequestAlertException(
                INVOICE_NOT_FOUND_VI,
                ExceptionConstants.INVOICE_NOT_FOUND_VI,
                ExceptionConstants.INVOICE_NOT_FOUND
            );
        }
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.UPDATE_INVOICE_SUCCESS_VI, true);
    }

    @Override
    public ResultDTO deleteInvoiceNotPublish(Integer invoiceId) {
        User user = userService.getUserWithAuthorities();
        Optional<Invoice> invoiceOptional = invoiceRepository.findOneByIdAndCompanyId(invoiceId, user.getCompanyId());
        if (invoiceOptional.isEmpty()) {
            throw new InternalServerException(INVOICE_NOT_FOUND_VI, ENTITY_NAME, INVOICE_NOT_FOUND);
        }
        Invoice invoice = invoiceOptional.get();
        if (
            invoice.getTaxCheckStatus() != null &&
            !Objects.equals(invoice.getTaxCheckStatus(), InvoiceConstants.TaxCheckStatus.CHUA_PHAT_HANH)
        ) {
            throw new InternalServerException(INVOICE_NOT_CANCEL_VI, ENTITY_NAME, INVOICE_NOT_CANCEL);
        }

        invoiceProductRepository.deleteAll(invoice.getInvoiceProducts());
        invoiceRepository.delete(invoice);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.DELETE_INVOICE_SUCCESS_VI, true);
    }

    @Override
    public ResultDTO publishListInvoice(PublishListRequest publishListRequest) {
        User user = userService.getUserWithAuthorities();
        Integer comId = user.getCompanyId();
        List<InvoiceListResponse> invoices = invoiceRepository
            .getAllInvoices(
                publishListRequest.getTaxCheckStatus(),
                publishListRequest.getFromDate(),
                publishListRequest.getToDate(),
                publishListRequest.getPattern(),
                publishListRequest.getCustomerName(),
                publishListRequest.getNo(),
                comId,
                null,
                false,
                publishListRequest.getParamCheckAll(),
                publishListRequest.getListID()
            )
            .getContent();
        List<TaskLog> taskLogs = new ArrayList<>();
        List<Object> listError = new ArrayList<>();
        for (InvoiceListResponse item : invoices) {
            if (item.getTaxCheckStatus() == null || item.getTaxCheckStatus() == 0) {
                TaskLog taskLog = new TaskLog();
                taskLog.setComId(user.getCompanyId());
                taskLog.setType(TaskLogConstants.Type.PUBLISH_INVOICE);
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.disable(SerializationFeature.INDENT_OUTPUT);
                String data = null;
                try {
                    data =
                        objectMapper.writeValueAsString(new PublishInvoiceQueue(user.getCompanyId().toString(), item.getId().toString()));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                taskLog.setStatus(TaskLogConstants.Status.PROCESSING);
                taskLog.setContent(data);
                taskLogs.add(taskLog);
            } else {
                item.setNote("Trạng thái hóa đơn không hợp lệ");
                listError.add(item);
            }
        }
        if (taskLogs.size() > 0) {
            transactionTemplate.execute(status -> {
                taskLogRepository.saveAll(taskLogs);
                return taskLogs;
            });
            for (TaskLog task : taskLogs) {
                userService.sendTaskLog(new TaskLogSendQueue(task.getId().toString(), task.getType()));
            }
        }
        DataResponse response = new DataResponse();
        response.setCountAll(invoices.size());
        response.setCountFalse(listError.size());
        response.setDataFalse(listError);
        response.setCountSuccess(response.getCountAll() - response.getCountFalse());
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS, true, response);
    }

    public void setConfigsForCompany(CompanyOwnerResponse companyOwner, List<Config> configs) {
        for (Config config : configs) {
            if (config.getCode().equals(ROUND_SCALE_AMOUNT)) {
                companyOwner.setRoundScaleAmount(Integer.parseInt(config.getValue()));
            } else if (config.getCode().equals(ROUND_SCALE_UNIT_PRICE)) {
                companyOwner.setRoundScaleUnitPrice(Integer.parseInt(config.getValue()));
            } else if (config.getCode().equals(ROUND_SCALE_QUANTITY)) {
                companyOwner.setRoundScaleQuantity(Integer.parseInt(config.getValue()));
            } else if (config.getCode().equals(EASYINVOICE_URL)) {
                companyOwner.setEasyInvoiceUrl(config.getValue());
            } else if (config.getCode().equals(EASYINVOICE_ACCOUNT)) {
                companyOwner.setEasyInvoiceAccount(config.getValue());
            } else if (config.getCode().equals(EASYINVOICE_PASS)) {
                companyOwner.setEasyInvoicePass(config.getValue());
            }
        }
    }
}
