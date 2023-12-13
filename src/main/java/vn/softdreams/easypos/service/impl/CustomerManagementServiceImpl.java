package vn.softdreams.easypos.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Strings;
import org.apache.poi.ss.usermodel.*;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import vn.softdreams.easypos.config.Constants;
import vn.softdreams.easypos.constants.*;
import vn.softdreams.easypos.domain.*;
import vn.softdreams.easypos.dto.TaskLogSendQueue;
import vn.softdreams.easypos.dto.card.CardPolicyConditions;
import vn.softdreams.easypos.dto.card.CardRankItem;
import vn.softdreams.easypos.dto.customer.*;
import vn.softdreams.easypos.dto.customerCard.CustomerCardInformation;
import vn.softdreams.easypos.dto.importExcel.ValidateImportResponse;
import vn.softdreams.easypos.dto.product.DeleteProductList;
import vn.softdreams.easypos.integration.easybooks88.queue.dto.AccountingObjectTask;
import vn.softdreams.easypos.repository.*;
import vn.softdreams.easypos.service.CustomerManagementService;
import vn.softdreams.easypos.service.UserService;
import vn.softdreams.easypos.service.dto.DataResponse;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.util.Common;
import vn.softdreams.easypos.web.rest.errors.BadRequestAlertException;
import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;
import vn.softdreams.easypos.web.rest.errors.InternalServerException;

import javax.validation.Validator;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static vn.softdreams.easypos.constants.ResultConstants.*;
import static vn.softdreams.easypos.web.rest.errors.ExceptionConstants.*;

@Service
@Transactional
public class CustomerManagementServiceImpl implements CustomerManagementService {

    private final Logger log = LoggerFactory.getLogger(CustomerManagementServiceImpl.class);
    private final String ENTITY_NAME = "customer";

    private final CustomerRepository customerRepository;
    private final CustomerCardRepository customerCardRepository;
    private final LoyaltyCardUsageRepository loyaltyCardUsageRepository;
    private final TransactionTemplate transactionTemplate;
    private final UserService userService;
    private final TaskLogRepository taskLogRepository;
    private final Validator validator;
    private final ModelMapper modelMapper;
    private final RestTemplate restTemplate;
    private final BillRepository billRepository;
    private final LoyaltyCardRepository loyaltyCardRepository;
    private final CardPolicyRepository cardPolicyRepository;

    public CustomerManagementServiceImpl(
        CustomerRepository customerRepository,
        CustomerCardRepository customerCardRepository,
        LoyaltyCardUsageRepository loyaltyCardUsageRepository,
        TransactionTemplate transactionTemplate,
        UserService userService,
        TaskLogRepository taskLogRepository,
        Validator validator,
        ModelMapper modelMapper,
        RestTemplate restTemplate,
        BillRepository billRepository,
        LoyaltyCardRepository loyaltyCardRepository,
        CardPolicyRepository cardPolicyRepository
    ) {
        this.customerRepository = customerRepository;
        this.customerCardRepository = customerCardRepository;
        this.loyaltyCardUsageRepository = loyaltyCardUsageRepository;
        this.transactionTemplate = transactionTemplate;
        this.userService = userService;
        this.taskLogRepository = taskLogRepository;
        this.validator = validator;
        this.modelMapper = modelMapper;
        this.restTemplate = restTemplate;
        this.billRepository = billRepository;
        this.loyaltyCardRepository = loyaltyCardRepository;
        this.cardPolicyRepository = cardPolicyRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public ResultDTO getAllWithPaging(
        Pageable pageable,
        String keyword,
        Integer type,
        boolean isCountAll,
        Boolean isHiddenDefault,
        boolean paramCheckAll,
        List<Integer> ids
    ) {
        Integer comId = userService.getCompanyId();
        Page<CustomerResponse> responseDTOS = customerRepository.getAllWithPaging(
            pageable,
            keyword,
            comId,
            CustomerConstants.Active.ACTIVE,
            type,
            isCountAll,
            isHiddenDefault,
            paramCheckAll,
            ids
        );
        List<CustomerResponse> responses = getCustomerCard(comId, responseDTOS.getContent());
        getCardBalance(comId, responseDTOS.getContent());
        return new ResultDTO(
            ResultConstants.SUCCESS,
            ResultConstants.CUSTOMER_GET_ALL_SUCCESS_VI,
            true,
            responses,
            (int) responseDTOS.getTotalElements()
        );
    }

    public List<CustomerResponse> getCustomerCard(Integer comId, List<CustomerResponse> responses) {
        if (!responses.isEmpty()) {
            List<Integer> customerIds = responses.stream().map(CustomerResponse::getId).collect(Collectors.toList());
            List<CustomerCardInformation> cardInformation = customerCardRepository.getAllCardByCustomerIds(comId, customerIds);
            List<CardPolicy> cardPolicyOptional = cardPolicyRepository.findByComIdAndStatus(comId, CardPolicyConstants.Status.RUNNING);
            Map<Integer, CardPolicyConditions> conditionsMap = new HashMap<>();
            for (CardPolicy cardPolicy : cardPolicyOptional) {
                ObjectMapper objectMapper = new ObjectMapper();
                if (cardPolicy.getConditions() != null) {
                    try {
                        CardPolicyConditions[] conditions = objectMapper.readValue(
                            cardPolicy.getConditions(),
                            CardPolicyConditions[].class
                        );
                        for (CardPolicyConditions condition : conditions) {
                            conditionsMap.put(condition.getCardId(), condition);
                        }
                    } catch (JsonProcessingException e) {
                        log.error("Error when convert card policy condition " + e.getMessage());
                    }
                }
            }
            for (CustomerCardInformation information : cardInformation) {
                if (conditionsMap.containsKey(information.getCardId())) {
                    CardPolicyConditions condition = conditionsMap.get(information.getCardId());
                    information.setAccumValue(condition.getAccumValue());
                    information.setRedeemValue(condition.getRedeemValue());
                }
            }
            Map<Integer, CustomerCardInformation> cardInformationMap = new HashMap<>();
            cardInformation.forEach(c -> {
                cardInformationMap.put(c.getCustomerId(), c);
                c.setCustomerId(null);
            });
            responses.forEach(customerResponse -> {
                customerResponse.setCardInformation(cardInformationMap.get(customerResponse.getId()));
            });
            log.debug(ENTITY_NAME + "_getAllCustomerPaging: " + ResultConstants.CUSTOMER_GET_ALL_SUCCESS_VI);
        }
        return responses;
    }

    public List<CustomerResponse> getCardBalance(Integer comId, List<CustomerResponse> responses) {
        if (!responses.isEmpty()) {
            List<Integer> ids = responses.stream().map(CustomerResponse::getId).collect(Collectors.toList());
            List<CustomerCard> customerCards = customerCardRepository.getAllByComIdAndCustomerIdIn(comId, ids);
            List<LoyaltyCardUsage> loyaltyCardUsages = loyaltyCardUsageRepository.findAllByComIdAndCustomerIdIn(comId, ids);
            List<LoyaltyCard> loyaltyCards = loyaltyCardRepository.findAllByComIdAndIdIn(
                comId,
                customerCards.stream().map(CustomerCard::getCardId).collect(Collectors.toList())
            );
            Map<Integer, Integer> statusMap = loyaltyCards.stream().collect(Collectors.toMap(LoyaltyCard::getId, LoyaltyCard::getStatus));
            Map<Integer, BigDecimal> moneyMap = new HashMap<>();
            Map<Integer, Integer> pointMap = new HashMap<>();
            for (CustomerCard card : customerCards) {
                if (
                    statusMap.containsKey(card.getCardId()) &&
                    Objects.equals(statusMap.get(card.getCardId()), LoyaltyCardConstants.Status.HOAT_DONG)
                ) {
                    moneyMap.put(card.getCustomerId(), card.getAmount());
                    pointMap.put(card.getCustomerId(), card.getPoint());
                }
            }
            for (LoyaltyCardUsage loyaltyCardUsage : loyaltyCardUsages) {
                if (pointMap.containsKey(loyaltyCardUsage.getCustomerId())) {
                    if (
                        Objects.equals(loyaltyCardUsage.getType(), LoyaltyCardConstants.Type.CONG_DIEM) ||
                        Objects.equals(loyaltyCardUsage.getType(), LoyaltyCardConstants.Type.NAP_DIEM)
                    ) {
                        pointMap.put(
                            loyaltyCardUsage.getCustomerId(),
                            pointMap.get(loyaltyCardUsage.getCustomerId()) + loyaltyCardUsage.getPoint()
                        );
                    } else if (
                        Objects.equals(loyaltyCardUsage.getType(), LoyaltyCardConstants.Type.QUY_DOI) ||
                        Objects.equals(loyaltyCardUsage.getType(), LoyaltyCardConstants.Type.TRU_DIEM)
                    ) {
                        pointMap.put(
                            loyaltyCardUsage.getCustomerId(),
                            pointMap.get(loyaltyCardUsage.getCustomerId()) - loyaltyCardUsage.getPoint()
                        );
                    }
                }

                if (moneyMap.containsKey(loyaltyCardUsage.getCustomerId())) {
                    if (
                        Objects.equals(loyaltyCardUsage.getType(), LoyaltyCardConstants.Type.NAP_TIEN) ||
                        Objects.equals(loyaltyCardUsage.getType(), LoyaltyCardConstants.Type.QUY_DOI) ||
                        Objects.equals(loyaltyCardUsage.getType(), LoyaltyCardConstants.Type.CONG_TIEN)
                    ) {
                        moneyMap.put(
                            loyaltyCardUsage.getCustomerId(),
                            moneyMap.get(loyaltyCardUsage.getCustomerId()).add(loyaltyCardUsage.getAmount())
                        );
                    } else if (Objects.equals(loyaltyCardUsage.getType(), LoyaltyCardConstants.Type.CHI_TIEN)) {
                        moneyMap.put(
                            loyaltyCardUsage.getCustomerId(),
                            moneyMap.get(loyaltyCardUsage.getCustomerId()).subtract(loyaltyCardUsage.getAmount())
                        );
                    }
                }
            }
            for (CustomerResponse response : responses) {
                if (moneyMap.containsKey(response.getId())) {
                    response.setMoneyBalance(moneyMap.get(response.getId()));
                }
                if (pointMap.containsKey(response.getId())) {
                    response.setPointBalance(pointMap.get(response.getId()));
                }
            }
        }
        return responses;
    }

    @Override
    @Transactional(readOnly = true)
    public ResultDTO getAllForOffline(Integer type) {
        Integer comId = userService.getCompanyId();
        List<CustomerResponse> responses = customerRepository.getAllForOffline(comId, CustomerConstants.Active.ACTIVE, type);
        getCustomerCard(comId, responses);
        getCardBalance(comId, responses);
        log.debug(ENTITY_NAME + "_getAllCustomer: " + ResultConstants.CUSTOMER_GET_ALL_SUCCESS_VI);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.CUSTOMER_GET_ALL_SUCCESS_VI, true, responses, responses.size());
    }

    private String getTypeCode(Integer type) {
        if (Objects.equals(type, CustomerConstants.Type.SUPPLIER)) {
            return Constants.SUPPLIER_CODE;
        } else if (Objects.equals(type, CustomerConstants.Type.CUSTOMER_AND_SUPPLIER)) {
            return Constants.CUSTOMER_SUPPLIER_CODE;
        }
        // default code
        return Constants.CUSTOMER_CODE;
    }

    @Override
    public ResultDTO create(CustomerCreateRequest customerRequest) {
        Integer comId = customerRequest.getComId();
        Integer type = customerRequest.getType();
        log.info(
            ENTITY_NAME + "_createCustomer: REST request to create Customer(name, code) : {}, {}",
            customerRequest.getName(),
            customerRequest.getCode2()
        );
        userService.getUserWithAuthorities(comId);

        if (!Strings.isNullOrEmpty(customerRequest.getCode2())) {
            checkDuplicateCode2(customerRequest.getCode2());
        }

        if (!Strings.isNullOrEmpty(customerRequest.getTaxCode())) {
            Common.checkCustomerTaxCode(customerRequest.getTaxCode());
        }

        if (type == null) {
            customerRequest.setType(CustomerConstants.Type.CUSTOMER);
        }

        if (customerRequest.getBirthday() != null) {
            Common.checkBirthdate(customerRequest.getBirthday(), Constants.ZONED_DATE_FORMAT);
        }

        Customer customer = new Customer();
        BeanUtils.copyProperties(customerRequest, customer);
        customer.setCode(userService.genCode(comId, getTypeCode(type)));

        List<String> normalizedName = new ArrayList<>();
        normalizedName.add(customerRequest.getName());
        String phoneNumber = customerRequest.getPhoneNumber();
        if (!Strings.isNullOrEmpty(phoneNumber)) {
            normalizedName.add(phoneNumber);
        }
        customer.setNormalizedName(Common.normalizedName(normalizedName));
        customer.setActive(CommonConstants.Customer.CUSTOMER_ACTIVE_TRUE);

        log.info(ENTITY_NAME + "_create: " + ResultConstants.CUSTOMER_CREATE_SUCCESS_CODE_VI);
        customerRepository.save(customer);
        if (
            customerRequest.getCardInformation() != null &&
            List.of(CustomerConstants.Type.CUSTOMER, CustomerConstants.Type.CUSTOMER_AND_SUPPLIER).contains(type)
        ) {
            createCustomerCardDefault(customerRequest.getCardInformation(), comId, customer.getId());
        }

        CustomerResponse response = new CustomerResponse();
        BeanUtils.copyProperties(customer, response);
        return new ResultDTO(
            ResultConstants.SUCCESS,
            Objects.equals(customerRequest.getType(), CustomerConstants.Type.CUSTOMER)
                ? ResultConstants.CUSTOMER_CREATE_SUCCESS_CODE_VI
                : Objects.equals(customerRequest.getType(), CustomerConstants.Type.CUSTOMER_AND_SUPPLIER)
                    ? ResultConstants.SUPPLIER_CUSTOMER_CREATE_SUCCESS_CODE_VI
                    : ResultConstants.SUPPLIER_CREATE_SUCCESS_CODE_VI,
            true,
            response
        );
    }

    private void checkCustomerCardCode(Integer comId, Integer code) {
        Integer checkDuplicate = customerCardRepository.checkCodeDuplicate(comId, code);
        if (Objects.equals(checkDuplicate, 1)) {
            throw new BadRequestAlertException(CUSTOMER_CARD_DUPLICATE_CODE_INVALID_VI, ENTITY_NAME, CUSTOMER_CARD_DUPLICATE_CODE_INVALID);
        }
    }

    /**
     * Tạo mới TaskLog để queue-service tự động xử lý, tạo Vật tư hàng hóa tương ứng ở phần mềm kế toán
     */
    private TaskLogSendQueue createAndPublishQueueTask(int comId, int customerId, int customerType, String taskType) throws Exception {
        AccountingObjectTask task = new AccountingObjectTask();
        task.setComId("" + comId);
        task.setAccountingObjectId("" + customerId);
        if (customerType == CustomerConstants.Type.CUSTOMER) {
            task.setType(CustomerConstants.TypeName.CUSTOMER);
        } else if (customerType == CustomerConstants.Type.SUPPLIER) {
            task.setType(CustomerConstants.TypeName.SUPPLIER);
        } else if (customerType == CustomerConstants.Type.CUSTOMER_AND_SUPPLIER) {
            task.setType(CustomerConstants.TypeName.CUSTOMER_AND_SUPPLIER);
        } else if (customerType == CustomerConstants.Type.OTHER) {
            task.setType(CustomerConstants.TypeName.OTHER);
        }
        TaskLog taskLog = new TaskLog();
        taskLog.setComId(comId);
        taskLog.setStatus(TaskLogConstants.Status.PROCESSING);
        //New objectMapper because we need disable a feature
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.INDENT_OUTPUT);
        taskLog.setContent(objectMapper.writeValueAsString(task));
        taskLog.setType(taskType);
        taskLog.setRefId(customerId);
        taskLog = taskLogRepository.save(taskLog);
        return new TaskLogSendQueue(taskLog.getId().toString(), taskLog.getType());
        //publish to queue
        //        eb88Producer.send(new TaskLogIdEnqueueMessage(taskLog.getId()));
    }

    @Override
    public ResultDTO update(CustomerUpdateRequest customerRequest) {
        log.info(ENTITY_NAME + "_updateCustomer: REST request to update Customer Id : {}", customerRequest.getId());
        userService.getUserWithAuthorities(customerRequest.getComId());
        Optional<Customer> customerOptional = customerRepository.findByIdAndComIdAndActive(
            customerRequest.getId(),
            userService.getCompanyId(),
            CustomerConstants.Active.TRUE
        );
        userService.getUserWithAuthorities(customerRequest.getComId());
        if (customerOptional.isEmpty()) {
            throw new InternalServerException(ExceptionConstants.CUSTOMER_NOT_FOUND_VI, ENTITY_NAME, ExceptionConstants.CUSTOMER_NOT_FOUND);
        } else {
            checkCustomerDefault(customerOptional.get().getCode());
            Customer existingCustomer = customerOptional.get();
            Integer typeOld = existingCustomer.getType();
            if (customerRequest.getName() != null) {
                if (customerRequest.getName().isEmpty()) {
                    throw new BadRequestAlertException(
                        ExceptionConstants.CUSTOMER_NAME_NOT_EMPTY_VI,
                        ENTITY_NAME,
                        ExceptionConstants.CUSTOMER_NAME_NOT_EMPTY_CODE
                    );
                }
                existingCustomer.setName(customerRequest.getName());
            }
            if (customerRequest.getCode2() != null && !customerRequest.getCode2().equals(existingCustomer.getCode2())) {
                if (!customerRequest.getCode2().isEmpty()) {
                    checkDuplicateCode2(customerRequest.getCode2());
                }
                existingCustomer.setCode2(customerRequest.getCode2());
            }
            existingCustomer.setAddress(customerRequest.getAddress());
            existingCustomer.setPhoneNumber(customerRequest.getPhoneNumber());
            existingCustomer.setEmail(customerRequest.getEmail());
            if (customerRequest.getTaxCode() != null) {
                if (!customerRequest.getTaxCode().isEmpty()) {
                    Common.checkCustomerTaxCode(customerRequest.getTaxCode());
                }
                existingCustomer.setTaxCode(customerRequest.getTaxCode());
            }
            existingCustomer.setIdNumber(customerRequest.getIdNumber());
            existingCustomer.setDescription(customerRequest.getDescription());
            if (customerRequest.getType() != null) {
                existingCustomer.setType(customerRequest.getType());
            }
            if (customerRequest.getCity() != null) {
                existingCustomer.setCity(customerRequest.getCity());
            }
            if (customerRequest.getDistrict() != null) {
                existingCustomer.setDistrict(customerRequest.getDistrict());
            }
            if (customerRequest.getGender() != null) {
                existingCustomer.setGender(customerRequest.getGender());
            }
            if (customerRequest.getBirthday() != null) {
                Common.checkBirthdate(customerRequest.getBirthday(), Constants.ZONED_DATE_FORMAT);
                existingCustomer.setBirthday(customerRequest.getBirthday());
            }
            existingCustomer.setNormalizedName(
                Common.normalizedName(Arrays.asList(existingCustomer.getName(), existingCustomer.getPhoneNumber()))
            );
            customerRepository.save(existingCustomer);
            saveCardInformation(
                customerRequest.getComId(),
                customerRequest.getCardInformation(),
                customerRequest.getId(),
                customerRequest.getType(),
                typeOld,
                existingCustomer.getName()
            );
            return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.CUSTOMER_UPDATE_SUCCESS_VI, true);
        }
    }

    private void saveCardInformation(
        Integer comId,
        CustomerCardInformation cardInformation,
        Integer customerId,
        Integer type,
        Integer typeOld,
        String customerName
    ) {
        if (
            Objects.equals(type, CustomerConstants.Type.SUPPLIER) &&
            List.of(CustomerConstants.Type.CUSTOMER, CustomerConstants.Type.CUSTOMER_AND_SUPPLIER).contains(typeOld)
        ) {
            Optional<CustomerCard> customerCardOptional = customerCardRepository.findOneByCustomerId(customerId);
            if (customerCardOptional.isPresent()) {
                CustomerCard customerCard = customerCardOptional.get();
                if (Objects.equals(customerCard.getCardId(), null)) {
                    customerCardRepository.delete(customerCard);
                } else {
                    throw new BadRequestAlertException(
                        String.format(ExceptionConstants.CUSTOMER_UPDATE_TYPE_INVALID_VI, customerName),
                        ENTITY_NAME,
                        ExceptionConstants.CUSTOMER_UPDATE_TYPE_INVALID
                    );
                }
            }
        } else {
            if (cardInformation != null && !Objects.equals(type, CustomerConstants.Type.SUPPLIER)) {
                CustomerCard customerCard;
                Optional<CustomerCard> customerCardOptional;
                Integer cardId = null;
                if (cardInformation.getCardId() != null) {
                    customerCardOptional =
                        customerCardRepository.getCustomerCardByCustomerIdAndCardId(customerId, cardInformation.getCardId());
                } else {
                    customerCardOptional = customerCardRepository.findOneByCustomerId(customerId);
                    cardId = loyaltyCardRepository.getCardIdDefault(comId);
                }
                if (typeOld.equals(CustomerConstants.Type.SUPPLIER)) {
                    createCustomerCardDefault(cardInformation, comId, customerId);
                } else {
                    if (customerCardOptional.isPresent()) {
                        customerCard = customerCardOptional.get();
                        if (
                            cardInformation.getCardCode() != null && !Objects.equals(cardInformation.getCardCode(), customerCard.getCode())
                        ) {
                            checkCustomerCardCode(customerCard.getComId(), cardInformation.getCardCode());
                        }
                        customerCard.setCode(cardInformation.getCardCode());
                        customerCard.setAmount(cardInformation.getAmount());
                        customerCard.setPoint(cardInformation.getPoint());
                        if (cardInformation.getCardId() == null && cardId != null) {
                            customerCard.setCardId(cardId);
                        }
                        customerCardRepository.save(customerCard);
                    } else if (cardId != null) {
                        throw new InternalServerException(CUSTOMER_CARD_NOT_FOUND_VI, ENTITY_NAME, CUSTOMER_CARD_NOT_FOUND);
                    }
                }
            }
        }
    }

    private void createCustomerCardDefault(CustomerCardInformation cardInformation, Integer comId, Integer customerId) {
        CustomerCard customerCard = new CustomerCard();
        Integer cardIdDefault = loyaltyCardRepository.getCardIdDefault(comId);
        customerCard.setComId(comId);
        customerCard.setCustomerId(customerId);
        if (cardIdDefault != null) {
            customerCard.setCardId(cardIdDefault);
        }
        if (cardInformation.getCardCode() != null) {
            checkCustomerCardCode(comId, cardInformation.getCardCode());
        }
        customerCard.setCode(cardInformation.getCardCode());
        customerCard.setAmount(cardInformation.getAmount());
        customerCard.setPoint(cardInformation.getPoint());
        customerCard.setStartDate(ZonedDateTime.now());
        customerCard.setExpiredDate(null);

        customerCardRepository.save(customerCard);
    }

    private void checkDuplicateCode2(String code2) {
        if (customerRepository.countByComIdAndCode2(userService.getCompanyId(), code2) > 0) {
            throw new BadRequestAlertException(
                ExceptionConstants.CUSTOMER_CODE2_EXISTS_VI,
                ENTITY_NAME,
                ExceptionConstants.CUSTOMER_CODE2_EXISTS_CODE
            );
        }
    }

    private void checkCustomerDefault(String code) {
        if (code.equals(CommonConstants.CUSTOMER_CODE_DEFAULT)) {
            throw new BadRequestAlertException(
                ExceptionConstants.CUSTOMER_DEFAULT_ERROR_VI,
                ENTITY_NAME,
                ExceptionConstants.CUSTOMER_DEFAULT_ERROR_CODE
            );
        }
    }

    @Override
    public ResultDTO deleteCustomer(Integer id) {
        log.info(ENTITY_NAME + "_deleteCustomer: REST request to delete CustomerId : {}", id);
        User user = userService.getUserWithAuthorities();
        Optional<Customer> customerOptional = customerRepository.findByIdAndComIdAndActive(
            id,
            userService.getCompanyId(),
            CustomerConstants.Active.TRUE
        );
        if (customerOptional.isEmpty()) {
            throw new InternalServerException(ExceptionConstants.CUSTOMER_NOT_FOUND_VI, ENTITY_NAME, ExceptionConstants.CUSTOMER_NOT_FOUND);
        }
        checkCustomerDefault(customerOptional.get().getCode());
        Customer customer = customerOptional.get();
        if (billRepository.countAllByComIdAndStatusAndCustomerId(user.getCompanyId(), BillConstants.Status.BILL_DONT_COMPLETE, id) > 0) {
            throw new BadRequestAlertException(
                ExceptionConstants.CUSTOMER_CANNOT_DELETE_VI.replace("@@name", customer.getName()),
                ENTITY_NAME,
                CUSTOMER_CANNOT_DELETE
            );
        }
        customer.setActive(false);
        Optional<CustomerCard> customerCardOptional = customerCardRepository.findByCustomerIdAndComId(id, customer.getComId());
        customerCardOptional.ifPresent(customerCardRepository::delete);
        TaskLogSendQueue taskLogSendQueue = transactionTemplate.execute(status -> {
            customerRepository.save(customer);
            // Send message to queue
            //            try {
            //                return createAndPublishQueueTask(
            //                    customerOptional.get().getComId(),
            //                    customerOptional.get().getId(),
            //                    customerOptional.get().getType(),
            //                    TaskLogConstants.Type.EB_UPDATE_ACC_OBJECT
            //                );
            //            } catch (Exception e) {
            //                log.error("Can not create queue task for eb88 deleting accountingObject (customer): {}", e.getMessage());
            //            }
            return null;
        });
        //        if (taskLogSendQueue != null) {
        //            userService.sendTaskLog(taskLogSendQueue);
        //        }
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.CUSTOMER_DELETE_SUCCESS_VI, true);
    }

    @Override
    public ResultDTO deleteListCustomer(DeleteProductList req) {
        User user = userService.getUserWithAuthorities();
        Integer comId = user.getCompanyId();
        List<CustomerResponse> customers = customerRepository
            .getAllWithPaging(
                null,
                req.getKeyword(),
                user.getCompanyId(),
                CustomerConstants.Active.ACTIVE,
                null,
                Boolean.TRUE,
                Boolean.TRUE,
                req.getParamCheckAll(),
                req.getIds()
            )
            .getContent();
        //        List<TaskLog> taskLogs = new ArrayList<>();
        List<Object> listError = new ArrayList<>();
        List<Integer> idDelete = new ArrayList<>();

        for (CustomerResponse response : customers) {
            Optional<Customer> customerOptional = customerRepository.findByIdAndComIdAndActive(
                response.getId(),
                userService.getCompanyId(),
                CustomerConstants.Active.TRUE
            );
            if (customerOptional.isEmpty()) {
                response.setNote(ExceptionConstants.CUSTOMER_NOT_FOUND_VI);
                listError.add(response);
            } else if (response.getCode().equals(CommonConstants.CUSTOMER_CODE_DEFAULT)) {
                response.setNote(ExceptionConstants.CUSTOMER_DEFAULT_ERROR_VI);
                listError.add(response);
            } else {
                Customer customer = customerOptional.get();
                if (
                    billRepository.countAllByComIdAndStatusAndCustomerId(
                        user.getCompanyId(),
                        BillConstants.Status.BILL_DONT_COMPLETE,
                        response.getId()
                    ) >
                    0
                ) {
                    response.setNote(ExceptionConstants.CUSTOMER_CANNOT_DELETE_VI.replace("@@name", customer.getName()));
                    listError.add(response);
                } else {
                    idDelete.add(response.getId());
                    // create taskLog
                    //                    AccountingObjectTask task = new AccountingObjectTask();
                    //                    Integer customerType = response.getType();
                    //                    task.setComId("" + comId);
                    //                    task.setAccountingObjectId("" + response.getId());
                    //                    if (Objects.equals(customerType, CustomerConstants.Type.CUSTOMER)) {
                    //                        task.setType(CustomerConstants.TypeName.CUSTOMER);
                    //                    } else if (Objects.equals(customerType, CustomerConstants.Type.SUPPLIER)) {
                    //                        task.setType(CustomerConstants.TypeName.SUPPLIER);
                    //                    } else if (Objects.equals(customerType, CustomerConstants.Type.CUSTOMER_AND_SUPPLIER)) {
                    //                        task.setType(CustomerConstants.TypeName.CUSTOMER_AND_SUPPLIER);
                    //                    } else if (Objects.equals(customerType, CustomerConstants.Type.OTHER)) {
                    //                        task.setType(CustomerConstants.TypeName.OTHER);
                    //                    }
                    //                    TaskLog taskLog = new TaskLog();
                    //                    taskLog.setComId(comId);
                    //                    taskLog.setStatus(TaskLogConstants.Status.PROCESSING);
                    //                    ObjectMapper objectMapper = new ObjectMapper();
                    //                    objectMapper.disable(SerializationFeature.INDENT_OUTPUT);
                    //                    try {
                    //                        taskLog.setContent(objectMapper.writeValueAsString(task));
                    //                    } catch (JsonProcessingException e) {
                    //                        throw new RuntimeException(e);
                    //                    }
                    //                    taskLog.setType(TaskLogConstants.Type.EB_UPDATE_ACC_OBJECT);
                    //                    taskLogs.add(taskLog);
                }
            }
        }

        List<Customer> customerList = customerRepository.findByComIdAndIdInAndActiveTrue(comId, idDelete);
        for (Customer customer : customerList) {
            customer.setActive(Boolean.FALSE);
        }
        customerRepository.saveAll(customerList);
        //        if (taskLogs.size() > 0) {
        //            transactionTemplate.execute(status -> {
        //                taskLogRepository.saveAll(taskLogs);
        //                return taskLogs;
        //            });
        //            for (TaskLog task : taskLogs) {
        //                userService.sendTaskLog(new TaskLogSendQueue(task.getId().toString(), task.getType()));
        //            }
        //        }
        DataResponse response = new DataResponse();
        response.setCountAll(customers.size());
        response.setCountFalse(listError.size());
        response.setDataFalse(listError);
        response.setCountSuccess(response.getCountAll() - response.getCountFalse());
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.DELETE_LIST_PRODUCT_UNIT_SUCCESS, true, response);
    }

    @Override
    @Transactional(readOnly = true)
    public ResultDTO findById(Integer id) {
        log.info(ENTITY_NAME + "_findById: REST request to get Customer by Id : {}", id);
        Integer comId = userService.getCompanyId();
        Optional<Customer> customerOptional = customerRepository.findByIdAndComIdAndActive(id, comId, CustomerConstants.Active.TRUE);
        if (customerOptional.isEmpty()) {
            throw new InternalServerException(ExceptionConstants.CUSTOMER_NOT_FOUND_VI, ENTITY_NAME, ExceptionConstants.CUSTOMER_NOT_FOUND);
        }
        CustomerResponse response = new CustomerResponse();
        BeanUtils.copyProperties(customerOptional.get(), response);
        response = getCustomerCard(comId, List.of(response)).get(0);
        response = getCardBalance(comId, List.of(response)).get(0);

        log.info(ENTITY_NAME + "_findById: " + ResultConstants.CUSTOMER_DETAIL_SUCCESS_VI);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.CUSTOMER_DETAIL_SUCCESS_VI, true, response);
    }

    @Override
    public ResultDTO importExel(MultipartFile file, Integer comId) {
        User user = userService.getUserWithAuthorities();
        if (!user.getManager()) {
            if (!user.getCompanyId().equals(comId)) {
                throw new InternalServerException(COMPANY_NOT_EXISTS_CODE_VI, ENTITY_NAME, COMPANY_NOT_EXISTS_CODE);
            }
        }
        List<String> code2List = customerRepository.findCode2ByComId(comId);
        List<CustomerCreateRequest> requests = new ArrayList<>();

        Workbook workbook;
        int countItem = 0;
        try {
            workbook = WorkbookFactory.create(file.getInputStream());
        } catch (Exception e) {
            throw new InternalServerException(EXCEL_FILE_ERROR_VI, ENTITY_NAME, EXCEL_FILE_ERROR);
        }
        Sheet sheet = workbook.getSheetAt(0);
        for (int i = 1; i <= Common.getMaxRowNumberImportExcel(sheet.getLastRowNum()); i++) {
            Row row = sheet.getRow(i);
            CustomerCreateRequest request = new CustomerCreateRequest();
            for (int j = 0; j < 11; j++) {
                Cell cell = row.getCell(j);
                if (cell != null && !cell.toString().isBlank()) {
                    String cellString = cell.toString().trim();
                    if (cellString.endsWith(".0")) {
                        cellString = cellString.substring(0, cellString.length() - 2);
                    }
                    switch (j) {
                        case 0:
                            {
                                request.setName(cellString);
                                break;
                            }
                        case 1:
                            {
                                Integer type = CustomerConstants.Type.CUSTOMER;
                                if (cellString.equalsIgnoreCase("Nhà cung cấp")) {
                                    type = CustomerConstants.Type.SUPPLIER;
                                } else if (cellString.equalsIgnoreCase("Khách hàng và Nhà cung cấp")) {
                                    type = CustomerConstants.Type.CUSTOMER_AND_SUPPLIER;
                                }
                                request.setType(type);
                                break;
                            }
                        case 2:
                            {
                                if (code2List.contains(cellString)) {
                                    throw new BadRequestAlertException(
                                        ExceptionConstants.CUSTOMER_CODE2_EXISTS_VI,
                                        ENTITY_NAME,
                                        ExceptionConstants.CUSTOMER_CODE2_EXISTS_CODE
                                    );
                                }
                                request.setCode2(cellString);
                                code2List.add(cellString);
                                break;
                            }
                        case 3:
                            {
                                request.setAddress(cellString);
                                break;
                            }
                        case 4:
                            {
                                request.setDistrict(cellString);
                                break;
                            }
                        case 5:
                            {
                                request.setCity(cellString);
                                break;
                            }
                        case 6:
                            {
                                request.setPhoneNumber(cellString);
                                break;
                            }
                        case 7:
                            {
                                request.setEmail(cellString);
                                break;
                            }
                        case 8:
                            {
                                if (!cellString.matches(RegexConstants.CUSTOMER_TAX_CODE_REGEX)) {
                                    throw new BadRequestAlertException(
                                        ExceptionConstants.CUSTOMER_TAX_CODE_INVALID_VI,
                                        ENTITY_NAME,
                                        ExceptionConstants.CUSTOMER_TAX_CODE_INVALID_CODE
                                    );
                                }
                                request.setTaxCode(cellString);
                                break;
                            }
                        case 9:
                            {
                                request.setIdNumber(cellString);
                                break;
                            }
                        case 10:
                            {
                                request.setDescription(cellString);
                                break;
                            }
                    }
                }
            }
            request.setComId(comId);
            Common.validateInput(validator, ENTITY_NAME, request);
            requests.add(request);
        }
        return saveDataExelCommon(requests, comId);
    }

    private ResultDTO saveDataExelCommon(List<CustomerCreateRequest> requests, Integer comId) {
        List<Customer> customers;
        if (!requests.isEmpty()) {
            customers = Arrays.asList(modelMapper.map(requests, Customer[].class));
            for (Customer customer : customers) {
                customer.setCode(userService.genCode(comId, getTypeCode(customer.getType())));
                customer.setNormalizedName(Common.normalizedName(Arrays.asList(customer.getName(), customer.getPhoneNumber())));
                customer.setActive(CommonConstants.Customer.CUSTOMER_ACTIVE_TRUE);
            }
            customerRepository.saveAll(customers);
            return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.CUSTOMER_CREATE_SUCCESS_CODE_VI, true, customers);
        }
        return new ResultDTO(ResultConstants.FAIL, ResultConstants.CUSTOMER_CREATE_FAIL_CODE_VI, false);
    }

    @Override
    public ResultDTO getDataByTaxCode(String taxCode) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "easyinvoice@sds@123");

        try {
            RequestEntity<?> requestEntity = new RequestEntity<>(
                headers,
                HttpMethod.GET,
                new URI("http://utilsrv.easyinvoice.com.vn/api/company/info?taxCode=" + taxCode)
            );
            ResponseEntity<Object> response = restTemplate.exchange(requestEntity, Object.class);
            return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_GET_LIST, true, response.getBody());
        } catch (Exception e) {
            return new ResultDTO(ResultConstants.FAIL, ResultConstants.CUSTOMER_NOT_FOUND_VI, false);
        }
    }

    @Override
    public ResultDTO validateImportExcel(MultipartFile file, Integer comId, Integer indexSheet) {
        userService.getUserWithAuthorities(comId);
        List<String> code2Exists = customerRepository.findCode2ByComId(comId);
        List<CustomerValidateResponse> response = new ArrayList<>();
        Workbook workbook;
        try {
            workbook = WorkbookFactory.create(file.getInputStream());
        } catch (Exception e) {
            throw new InternalServerException(EXCEL_FILE_ERROR_VI, ENTITY_NAME, EXCEL_FILE_ERROR);
        }
        Sheet sheet = workbook.getSheetAt(indexSheet);
        int countValid = 0;
        int countInValid = 0;
        int countTotal = 0;
        for (int i = 1; i <= Common.getMaxRowNumberImportExcel(sheet.getLastRowNum()); i++) {
            Row row = sheet.getRow(i);
            CustomerValidateResponse request = new CustomerValidateResponse();
            boolean check = false;
            Map<Integer, String> message = new HashMap<>();
            for (int j = 0; j < 11; j++) {
                Cell cell;
                try {
                    cell = row.getCell(j);
                } catch (Exception ex) {
                    break;
                }
                FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                CellValue cellValue = evaluator.evaluate(cell);
                String cellString = Common.getRawValueExcel(cellValue);
                if (cell != null && !cell.toString().isBlank() && !Strings.isNullOrEmpty(cellString)) {
                    check = true;
                    if (cellString.endsWith(".0")) {
                        cellString = cellString.substring(0, cellString.length() - 2);
                    }
                    switch (j) {
                        case 0:
                            {
                                request.setName(cellString);
                                break;
                            }
                        case 1:
                            {
                                Integer type = CustomerConstants.Type.CUSTOMER;
                                if (cellString.equalsIgnoreCase("Nhà cung cấp")) {
                                    type = CustomerConstants.Type.SUPPLIER;
                                } else if (cellString.equalsIgnoreCase("Khách hàng và Nhà cung cấp")) {
                                    type = CustomerConstants.Type.CUSTOMER_AND_SUPPLIER;
                                }
                                request.setType(type);
                                break;
                            }
                        case 2:
                            {
                                if (code2Exists.contains(cellString)) {
                                    message.put(CustomerConstants.IMPORT_EXCEL.CODE2, ExceptionConstants.CUSTOMER_CODE2_EXISTS_VI);
                                }
                                request.setCode2(cellString);
                                code2Exists.add(cellString);
                                break;
                            }
                        case 3:
                            {
                                request.setAddress(cellString);
                                break;
                            }
                        case 4:
                            {
                                request.setDistrict(cellString);
                                break;
                            }
                        case 5:
                            {
                                request.setCity(cellString);
                                break;
                            }
                        case 6:
                            {
                                if (
                                    cellString.length() < 10 ||
                                    cellString.length() > 20 ||
                                    !cellString.matches(RegexConstants.PHONE_NUMBER_REGEX)
                                ) {
                                    message.put(CustomerConstants.IMPORT_EXCEL.PHONE, CUSTOMER_PHONE_INVALID_VI);
                                }
                                request.setPhoneNumber(cellString);
                                break;
                            }
                        case 7:
                            {
                                if (!cellString.matches(RegexConstants.EMAIL_REGEX) || cellString.contains("..")) {
                                    message.put(CustomerConstants.IMPORT_EXCEL.EMAIL, ExceptionConstants.CUSTOMER_EMAIL_INVALID_VI);
                                }
                                request.setEmail(cellString);
                                break;
                            }
                        case 8:
                            {
                                if (!cellString.matches(RegexConstants.CUSTOMER_TAX_CODE_REGEX)) {
                                    message.put(CustomerConstants.IMPORT_EXCEL.TAX_CODE, ExceptionConstants.CUSTOMER_TAX_CODE_INVALID_VI);
                                }
                                request.setTaxCode(cellString);
                                break;
                            }
                        case 9:
                            {
                                if (cellString.length() < 9 || cellString.length() > 12) {
                                    message.put(CustomerConstants.IMPORT_EXCEL.ID_NUMBER, CUSTOMER_ID_NUMBER_INVALID_VI);
                                }
                                request.setIdNumber(cellString);
                                break;
                            }
                        case 10:
                            {
                                request.setDescription(cellString);
                                break;
                            }
                    }
                }
            }
            if (!check) {
                break;
            }
            request.setComId(comId);
            if (Strings.isNullOrEmpty(request.getName())) {
                message.put(CustomerConstants.IMPORT_EXCEL.NAME, CUSTOMER_NAME_NOT_EMPTY_VI);
            } else if (request.getType() == null) {
                message.put(CustomerConstants.IMPORT_EXCEL.TYPE, CUSTOMER_TYPE_NOT_NULL_VI);
            }
            if (!message.isEmpty()) {
                request.setMessageErrorMap(message);
                countInValid++;
            } else {
                request.setStatus(true);
                countValid++;
            }
            response.add(request);
            countTotal++;
        }
        if (response.isEmpty()) {
            return new ResultDTO(FAIL, PRODUCT_IMPORT_EXCEL_ERROR, false);
        }
        return new ResultDTO(
            SUCCESS,
            PRODUCT_IMPORT_VALIDATE_SUCCESS,
            true,
            new ValidateImportResponse(countValid, countInValid, response),
            countTotal
        );
    }

    @Override
    public ResultDTO saveDataImportExcel(List<CustomerCreateRequest> request) {
        request.forEach(customerCreateRequest -> Common.validateInput(validator, ENTITY_NAME, customerCreateRequest));
        User user = userService.getUserWithAuthorities(request.get(0).getComId());
        return saveDataExelCommon(request, user.getCompanyId());
    }

    @Override
    @Async
    public void syncDataAfterImport(List<Customer> customers) {
        //        TaskLogSendQueue taskLogSendQueue = transactionTemplate.execute(status -> {
        //            customers.forEach(customer -> {
        //                try {
        //                    createAndPublishQueueTask(
        //                        customer.getComId(),
        //                        customer.getId(),
        //                        customer.getType(),
        //                        TaskLogConstants.Type.EB_CREATE_ACC_OBJECT
        //                    );
        //                } catch (Exception e) {
        //                    log.error("Can not create queue task for eb88 creating accountingObject (customer): {}", e.getMessage());
        //                }
        //            });
        //            return null;
        //        });
        //        if (taskLogSendQueue != null) {
        //            userService.sendTaskLog(taskLogSendQueue);
        //        }
    }

    @Override
    public ResultDTO exportErrorData(List<CustomerValidateResponse> request) {
        Workbook workbook = Common.readFileExcelTemplate(ImportExcelConstants.CUSTOMER_FILE_URL);

        if (workbook != null) {
            Sheet sheet = workbook.getSheetAt(0);
            int firstRowNum = 0;
            for (CustomerValidateResponse cus : request) {
                Row newRow = sheet.createRow(firstRowNum + 1);
                for (int i = 0; i < 11; i++) {
                    Cell cell = newRow.createCell(i);
                    Object value = null;
                    switch (i) {
                        case 0:
                            {
                                value = cus.getName();
                                break;
                            }
                        case 1:
                            {
                                if (cus.getType() != null) {
                                    String type = cus.getType().toString();
                                    if (Objects.equals(type, CustomerConstants.Type.CUSTOMER.toString())) {
                                        value = CustomerConstants.TypeNameVi.CUSTOMER;
                                    } else if (Objects.equals(type, CustomerConstants.Type.SUPPLIER.toString())) {
                                        value = CustomerConstants.TypeNameVi.SUPPLIER;
                                    } else if (Objects.equals(type, CustomerConstants.Type.CUSTOMER_AND_SUPPLIER.toString())) {
                                        value = CustomerConstants.TypeNameVi.CUSTOMER_AND_SUPPLIER;
                                    }
                                }
                                break;
                            }
                        case 2:
                            {
                                value = cus.getCode2();
                                break;
                            }
                        case 3:
                            {
                                value = cus.getAddress();
                                break;
                            }
                        case 4:
                            {
                                value = cus.getDistrict();
                                break;
                            }
                        case 5:
                            {
                                value = cus.getCity();
                                break;
                            }
                        case 6:
                            {
                                value = cus.getPhoneNumber();
                                break;
                            }
                        case 7:
                            {
                                value = cus.getEmail();
                                break;
                            }
                        case 8:
                            {
                                value = cus.getTaxCode();
                                break;
                            }
                        case 9:
                            {
                                value = cus.getIdNumber();
                                break;
                            }
                        case 10:
                            {
                                value = cus.getDescription();
                                break;
                            }
                    }
                    if (value != null) {
                        cell.setCellValue(value.toString());
                    }
                    if (cus.getMessageErrorMap().containsKey(i)) {
                        cell.setCellStyle(Common.highLightErrorCell(workbook));
                        cell.setCellComment(Common.setCommentErrorCell(workbook, sheet, cell, cus.getMessageErrorMap().get(i)));
                    }
                }
                firstRowNum++;
            }
            byte[] response = Common.writeWorkbookToByte(workbook);
            try {
                workbook.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return new ResultDTO(SUCCESS, EXPORT_FILE_SUCCESS, true, response);
        }

        return new ResultDTO(FAIL, EXCEL_FILE_ERROR_VI, false);
    }

    @Override
    public ResultDTO updateCard(CustomerUpdateCardRequest request) {
        checkCustomerUpdateCardRequest(request);
        Integer comId = request.getComId();
        Integer typeResult = null;
        Integer type = request.getType();
        if (!LoyaltyCardUsageConstants.TypeCustomerCardUpdateRequest.values.contains(type)) {
            throw new BadRequestAlertException(
                ExceptionConstants.CUSTOMER_CARD_UPDATE_TYPE_INVALID_VI,
                "customer_card",
                ExceptionConstants.CUSTOMER_CARD_UPDATE_TYPE_INVALID
            );
        } else {
            if (Objects.equals(type, LoyaltyCardUsageConstants.TypeCustomerCardUpdateRequest.NAP_TIEN)) {
                typeResult = LoyaltyCardUsageConstants.Type.NAP_TIEN;
            } else if (Objects.equals(type, LoyaltyCardUsageConstants.TypeCustomerCardUpdateRequest.CONG_DIEM)) {
                typeResult = LoyaltyCardUsageConstants.Type.CONG_DIEM;
            } else if (Objects.equals(type, LoyaltyCardUsageConstants.TypeCustomerCardUpdateRequest.TRU_DIEM)) {
                typeResult = LoyaltyCardUsageConstants.Type.TRU_DIEM;
            } else if (Objects.equals(type, LoyaltyCardUsageConstants.TypeCustomerCardUpdateRequest.QUY_DOI)) {
                typeResult = LoyaltyCardUsageConstants.Type.QUY_DOI;
            }
        }

        userService.getUserWithAuthorities(comId);
        List<Customer> customers = customerRepository.findAllByComIdAndIds(comId, request.getCustomerIds());
        if (customers.isEmpty() || request.getCustomerIds().size() != customers.size()) {
            throw new InternalServerException(
                ExceptionConstants.CUSTOMER_LIST_INVALID_VI,
                ENTITY_NAME,
                ExceptionConstants.CUSTOMER_LIST_INVALID
            );
        }
        List<CustomerCard> customerCards = customerCardRepository.getAllByCustomerIds(comId, request.getCustomerIds());
        Map<Integer, CustomerCard> customerAndCardMap = new HashMap<>();
        customerCards.forEach(c -> customerAndCardMap.put(c.getCustomerId(), c));
        List<LoyaltyCardUsage> loyaltyCardUsages = new ArrayList<>();
        // hiện tại chỉ làm với 1 KH, để lại vòng for nếu sau này phát triển với nhiều KH
        for (Integer customerId : request.getCustomerIds()) {
            if (!customerAndCardMap.containsKey(customerId)) {
                throw new BadRequestAlertException(
                    ExceptionConstants.CUSTOMER_CARD_USED_NOT_FOUND_VI,
                    ENTITY_NAME,
                    ExceptionConstants.CUSTOMER_CARD_USED_NOT_FOUND
                );
            }
            LoyaltyCardUsage item = new LoyaltyCardUsage();
            item.setComId(comId);
            item.setCardId(customerAndCardMap.get(customerId).getCardId());
            item.setCustomerId(customerId);
            item.setUsageDate(ZonedDateTime.now());
            item.setAmount(request.getAmount());
            item.setPoint(request.getPoint());
            item.setDescription(request.getDescription());
            item.setType(typeResult);
            loyaltyCardUsages.add(item);
        }
        if (!loyaltyCardUsages.isEmpty()) {
            Integer customerIdFirst = customers.get(0).getId();
            // check số điểm đã có với số điểm quy đổi hoặc số tiền bị trừ
            if (type == 3 || type == 2) {
                Integer pointBegan = customerAndCardMap.get(customerIdFirst).getPoint();
                Integer pointAccum = loyaltyCardUsageRepository.getPointAccumValueByCustomerId(
                    comId,
                    customerIdFirst,
                    LoyaltyCardUsageConstants.Type.CONG_DIEM,
                    LoyaltyCardUsageConstants.Type.TRU_DIEM,
                    LoyaltyCardUsageConstants.Type.QUY_DOI
                );
                if (pointAccum == null) {
                    pointAccum = 0;
                } else if (pointBegan == null) {
                    pointBegan = 0;
                }
                Integer pointTotal = pointBegan + pointAccum;
                boolean compare = request.getPoint().compareTo(pointTotal) > 0;
                if (type == 3 && compare) {
                    throw new BadRequestAlertException(
                        ExceptionConstants.CUSTOMER_CARD_UPDATE_POINT_INVALID_VI,
                        ENTITY_NAME,
                        ExceptionConstants.CUSTOMER_CARD_UPDATE_POINT_INVALID
                    );
                } else if (type == 2 && compare) {
                    throw new BadRequestAlertException(
                        ExceptionConstants.CUSTOMER_CARD_UPDATE_POINT_MINUS_INVALID_VI,
                        ENTITY_NAME,
                        ExceptionConstants.CUSTOMER_CARD_UPDATE_POINT_MINUS_INVALID
                    );
                }
            }

            loyaltyCardUsageRepository.saveAll(loyaltyCardUsages);
            resetRankAfterDeposited(comId, type, customerIdFirst);
        }

        return new ResultDTO(SUCCESS, CUSTOMER_UPDATE_CARD_SUCCESS_VI, true);
    }

    private void resetRankAfterDeposited(Integer comId, Integer type, Integer customerId) {
        Optional<CustomerCard> customerCardOptional = customerCardRepository.getCustomerCardByCustomerId(customerId);
        if (LoyaltyCardUsageConstants.TypeCustomerCardUpdateRequest.values.contains(type) && type == 0) {
            Optional<CardPolicy> cardPolicyOptional = cardPolicyRepository.findOneByComIdAndUpgradeTypeAndStatusIn(
                comId,
                CardPolicyConstants.UpgradeType.TOTAL_DEPOSIT,
                getStatusAvailable()
            );
            if (cardPolicyOptional.isPresent() && customerCardOptional.isPresent()) {
                Integer cardIdChange = null;
                CustomerCard customerCard = customerCardOptional.get();
                Integer cardId = customerCard.getCardId();
                BigDecimal totalAccumAmount = loyaltyCardUsageRepository.getTotalAmountWithCustomerIdAndType(
                    comId,
                    customerId,
                    LoyaltyCardUsageConstants.Type.NAP_TIEN
                );
                if (totalAccumAmount == null) {
                    totalAccumAmount = BigDecimal.ZERO;
                }
                ObjectMapper objectMapper = new ObjectMapper();
                CardPolicyConditions[] data;
                CardPolicy cardPolicy = cardPolicyOptional.get();
                try {
                    data = objectMapper.readValue(cardPolicy.getConditions(), CardPolicyConditions[].class);
                    for (CardPolicyConditions condition : data) {
                        BigDecimal upgradeValue = condition.getUpgradeValue();
                        if (totalAccumAmount.compareTo(upgradeValue) >= 0) {
                            cardIdChange = condition.getCardId();
                        }
                    }

                    if (cardIdChange != null && !Objects.equals(cardId, cardIdChange)) {
                        // check if card downgrade -> not reset card rank
                        List<CardRankItem> rankItems = loyaltyCardRepository.findAllByIdInOrderByRank(List.of(cardId, cardIdChange));
                        if (!rankItems.isEmpty() && Objects.equals(rankItems.get(0).getId(), cardId)) {
                            cardId = cardIdChange;
                        }
                    }

                    customerCard.setCardId(cardId);
                    customerCardRepository.save(customerCard);
                } catch (JsonProcessingException e) {
                    throw new BadRequestAlertException(
                        ExceptionConstants.CARD_POLICY_CONDITION_INVALID_VI,
                        ENTITY_NAME,
                        ExceptionConstants.CARD_POLICY_CONDITION_INVALID
                    );
                }
            }
        }
    }

    private static List<Integer> getStatusAvailable() {
        return List.of(CardPolicyConstants.Status.NOT_RUN, CardPolicyConstants.Status.RUNNING);
    }

    private void checkCustomerUpdateCardRequest(CustomerUpdateCardRequest request) {
        if (
            Objects.equals(request.getType(), LoyaltyCardUsageConstants.TypeCustomerCardUpdateRequest.NAP_TIEN) ||
            Objects.equals(request.getType(), LoyaltyCardUsageConstants.TypeCustomerCardUpdateRequest.QUY_DOI)
        ) {
            if (request.getAmount() == null) {
                throw new BadRequestAlertException(
                    ExceptionConstants.CUSTOMER_UPDATE_CARD_AMOUNT_EMPTY_VI,
                    ENTITY_NAME,
                    ExceptionConstants.CUSTOMER_UPDATE_CARD_AMOUNT_EMPTY
                );
            }
            if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                throw new BadRequestAlertException(
                    CUSTOMER_UPDATE_CARD_AMOUNT_INVALID_VI,
                    ENTITY_NAME,
                    CUSTOMER_UPDATE_CARD_AMOUNT_INVALID
                );
            }
        }
        if (
            Objects.equals(request.getType(), LoyaltyCardUsageConstants.TypeCustomerCardUpdateRequest.CONG_DIEM) ||
            Objects.equals(request.getType(), LoyaltyCardUsageConstants.TypeCustomerCardUpdateRequest.TRU_DIEM) ||
            Objects.equals(request.getType(), LoyaltyCardUsageConstants.TypeCustomerCardUpdateRequest.QUY_DOI)
        ) {
            if (request.getPoint() == null) {
                throw new BadRequestAlertException(
                    ExceptionConstants.CUSTOMER_UPDATE_CARD_POINT_EMPTY_VI,
                    ENTITY_NAME,
                    ExceptionConstants.CUSTOMER_UPDATE_CARD_POINT_EMPTY
                );
            }
            if (request.getPoint() <= 0) {
                throw new BadRequestAlertException(CUSTOMER_UPDATE_CARD_POINT_INVALID_VI, ENTITY_NAME, CUSTOMER_UPDATE_CARD_POINT_INVALID);
            }
            if (
                Objects.equals(request.getType(), LoyaltyCardUsageConstants.TypeCustomerCardUpdateRequest.QUY_DOI) &&
                request.getAmount().compareTo(BigDecimal.ZERO) <= 0
            ) {
                throw new BadRequestAlertException(
                    CUSTOMER_UPDATE_CARD_AMOUNT_INVALID_VI,
                    ENTITY_NAME,
                    CUSTOMER_UPDATE_CARD_AMOUNT_INVALID
                );
            }
        }
    }
}
