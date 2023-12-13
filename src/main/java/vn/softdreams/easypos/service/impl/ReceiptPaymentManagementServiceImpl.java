package vn.softdreams.easypos.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.easypos.config.Constants;
import vn.softdreams.easypos.constants.BusinessTypeConstants;
import vn.softdreams.easypos.constants.ResultConstants;
import vn.softdreams.easypos.domain.*;
import vn.softdreams.easypos.dto.receiptpayment.*;
import vn.softdreams.easypos.repository.*;
import vn.softdreams.easypos.service.ReceiptPaymentManagementService;
import vn.softdreams.easypos.service.UserService;
import vn.softdreams.easypos.service.dto.DataResponse;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.util.Common;
import vn.softdreams.easypos.web.rest.errors.BadRequestAlertException;
import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;
import vn.softdreams.easypos.web.rest.errors.InternalServerException;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Transactional
public class ReceiptPaymentManagementServiceImpl implements ReceiptPaymentManagementService {

    private final Logger log = LoggerFactory.getLogger(ReportManagementServiceImpl.class);
    private static final String ENTITY_NAME = "receiptPayment";
    private final UserService userService;
    private final McReceiptRepository mcReceiptRepository;
    private final McPaymentRepository mcPaymentRepository;
    private final BusinessTypeRepository businessTypeRepository;
    private final ReceiptPaymentRepositoryCustom receiptPaymentRepositoryCustom;
    private final BillRepository billRepository;
    private final RsInoutWardRepository rsInoutWardRepository;
    private final RsInoutWardManagementServiceImpl rsInoutWardManagementService;
    private final BillManagementServiceImpl billManagementService;

    public ReceiptPaymentManagementServiceImpl(
        UserService userService,
        McReceiptRepository mcReceiptRepository,
        McPaymentRepository mcPaymentRepository,
        BusinessTypeRepository businessTypeRepository,
        ReceiptPaymentRepositoryCustom receiptPaymentRepositoryCustom,
        BillRepository billRepository,
        RsInoutWardRepository rsInoutWardRepository,
        RsInoutWardManagementServiceImpl rsInoutWardManagementService,
        BillManagementServiceImpl billManagementService
    ) {
        this.userService = userService;
        this.mcReceiptRepository = mcReceiptRepository;
        this.mcPaymentRepository = mcPaymentRepository;
        this.businessTypeRepository = businessTypeRepository;
        this.receiptPaymentRepositoryCustom = receiptPaymentRepositoryCustom;
        this.billRepository = billRepository;
        this.rsInoutWardRepository = rsInoutWardRepository;
        this.rsInoutWardManagementService = rsInoutWardManagementService;
        this.billManagementService = billManagementService;
    }

    public ResultDTO getAllTransactions(
        Pageable pageable,
        Integer comId,
        String fromDate,
        String toDate,
        Integer type,
        String keyword,
        Boolean isCountAll
    ) {
        log.info(ENTITY_NAME + " request to get all receipt/payment transactions during a particular period of time");
        User user = userService.getUserWithAuthorities(comId);
        if (type != null && !type.equals(BusinessTypeConstants.Type.PAYMENT) && !type.equals(BusinessTypeConstants.Type.RECEIPT)) {
            throw new BadRequestAlertException(ExceptionConstants.TYPE_INVALID_VI, ENTITY_NAME, ExceptionConstants.TYPE_INVALID);
        }
        GetAllTransactionsAlternative transactions = receiptPaymentRepositoryCustom.getAllTransactions(
            pageable,
            comId,
            fromDate,
            toDate,
            type,
            keyword,
            isCountAll,
            false,
            null
        );
        ReceiptPaymentGetAllTransactionsResponse response = new ReceiptPaymentGetAllTransactionsResponse();
        BeanUtils.copyProperties(transactions, response);
        response.setFromDate(fromDate);
        response.setToDate(toDate);
        response.setComId(comId);
        response.setReceiptPaymentList(transactions.getReceiptPaymentList().getContent());
        return new ResultDTO(
            ResultConstants.SUCCESS,
            ResultConstants.SUCCESS,
            true,
            response,
            (int) transactions.getReceiptPaymentList().getTotalElements()
        );
    }

    public ResultDTO getById(Integer comId, Integer type, Integer id) {
        log.info(ENTITY_NAME + " request to get a receipt/payment by id: {}", id);
        ReceiptPaymentGetByIdResponse response = new ReceiptPaymentGetByIdResponse();
        userService.getUserWithAuthorities(comId);
        if (type == 0) {
            Optional<McReceipt> mcReceiptOptional = mcReceiptRepository.findById(id);
            if (mcReceiptOptional.isPresent()) {
                McReceipt mcReceipt = mcReceiptOptional.get();
                response.setId(mcReceipt.getId());
                response.setComId(mcReceipt.getComId());
                response.setBillId(mcReceipt.getBillId());
                response.setType(0);
                response.setBusinessType(mcReceipt.getBusinessTypeId());
                response.setTypeDesc(mcReceipt.getTypeDesc());
                response.setDate(mcReceipt.getDate().format(DateTimeFormatter.ofPattern(Constants.ZONED_DATE_TIME_FORMAT)));
                response.setCustomerId(mcReceipt.getCustomerId());
                response.setCustomerName(mcReceipt.getCustomerName());
                response.setAmount(mcReceipt.getAmount());
                response.setNote(mcReceipt.getDescription());
                response.setNo(mcReceipt.getNo());
                response.setRsInOutWardId(mcReceipt.getRsInoutWardId());
            } else {
                log.error(ExceptionConstants.RECEIPT_NOT_EXISTS);
                throw new InternalServerException(
                    ExceptionConstants.RECEIPT_NOT_EXISTS,
                    ENTITY_NAME,
                    ExceptionConstants.RECEIPT_NOT_EXISTS_CODE
                );
            }
        }
        if (type == 1) {
            Optional<McPayment> mcPaymentOptional = mcPaymentRepository.findById(id);
            if (mcPaymentOptional.isPresent()) {
                McPayment mcPayment = mcPaymentOptional.get();
                response.setId(mcPayment.getId());
                response.setComId(mcPayment.getComId());
                response.setType(1);
                response.setBusinessType(mcPayment.getBusinessTypeId());
                response.setTypeDesc(mcPayment.getTypeDesc());
                response.setDate(mcPayment.getDate().format(DateTimeFormatter.ofPattern(Constants.ZONED_DATE_TIME_FORMAT)));
                response.setCustomerId(mcPayment.getCustomerId());
                response.setCustomerName(mcPayment.getCustomerName());
                response.setAmount(mcPayment.getAmount());
                response.setNote(mcPayment.getDescription());
                response.setNo(mcPayment.getNo());
                response.setRsInOutWardId(mcPayment.getRsInoutWardId());
                response.setBillId(mcPayment.getBillId());
            } else {
                log.error(ExceptionConstants.PAYMENT_NOT_EXISTS);
                throw new InternalServerException(
                    ExceptionConstants.PAYMENT_NOT_EXISTS,
                    ENTITY_NAME,
                    ExceptionConstants.PAYMENT_NOT_EXISTS_CODE
                );
            }
        }

        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS, true, response);
    }

    public ResultDTO create(ReceiptPaymentCreateRequest request) {
        log.info(ENTITY_NAME + " request to create");
        User user = userService.getUserWithAuthorities(request.getComId());
        if (
            !Objects.equals(request.getType(), BusinessTypeConstants.Type.RECEIPT) &&
            !Objects.equals(request.getType(), BusinessTypeConstants.Type.PAYMENT)
        ) {
            throw new BadRequestAlertException(ExceptionConstants.TYPE_INVALID_VI, ENTITY_NAME, ExceptionConstants.TYPE_INVALID);
        }
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestAlertException(ExceptionConstants.AMOUNT_INVALID_VI, ENTITY_NAME, ExceptionConstants.AMOUNT_INVALID_CODE);
        }
        if (request.getBusinessType() != null) {
            Optional<BusinessType> businessTypeOptional = businessTypeRepository.findByIdAndComId(
                request.getBusinessType(),
                user.getCompanyId()
            );
            if (businessTypeOptional.isEmpty()) {
                throw new BadRequestAlertException(
                    ExceptionConstants.BUSINESS_TYPE_NOT_FOUND_VI,
                    ENTITY_NAME,
                    ExceptionConstants.BUSINESS_TYPE_NOT_FOUND
                );
            }
            String type = businessTypeOptional.get().getType();
            if (
                Objects.equals(request.getType(), BusinessTypeConstants.Type.RECEIPT) &&
                !type.equals(BusinessTypeConstants.TypeName.RECEIPT)
            ) {
                throw new BadRequestAlertException(
                    ExceptionConstants.BUSINESS_TYPE_NOT_FOUND_VI,
                    ENTITY_NAME,
                    ExceptionConstants.BUSINESS_TYPE_NOT_FOUND
                );
            }
            if (
                Objects.equals(request.getType(), BusinessTypeConstants.Type.PAYMENT) &&
                !type.equals(BusinessTypeConstants.TypeName.PAYMENT)
            ) {
                throw new BadRequestAlertException(
                    ExceptionConstants.BUSINESS_TYPE_NOT_FOUND_VI,
                    ENTITY_NAME,
                    ExceptionConstants.BUSINESS_TYPE_NOT_FOUND
                );
            }
        } else {
            if (Objects.equals(request.getType(), BusinessTypeConstants.Type.PAYMENT)) {
                request.setBusinessType(
                    businessTypeRepository.getIdByComIdAndCode(user.getCompanyId(), BusinessTypeConstants.Code.PAYMENT_UNKNOWN)
                );
            } else if (Objects.equals(request.getType(), BusinessTypeConstants.Type.RECEIPT)) {
                request.setBusinessType(
                    businessTypeRepository.getIdByComIdAndCode(user.getCompanyId(), BusinessTypeConstants.Code.RECEIPT_UNKNOWN)
                );
            }
        }
        McReceipt mcReceipt = new McReceipt();
        McPayment mcPayment = new McPayment();
        String result = "";

        if (request.getType() == 0) {
            mcReceipt.setBillId(request.getBillId());
            mcReceipt.setComId(request.getComId());
            mcReceipt.setBusinessTypeId(
                request.getBusinessType() == null
                    ? (businessTypeRepository.getIdByComIdAndCode(request.getComId(), BusinessTypeConstants.Code.RECEIPT_UNKNOWN))
                    : request.getBusinessType()
            );
            mcReceipt.setTypeDesc(request.getTypeDesc());
            mcReceipt.setDate(request.getDate());
            mcReceipt.setCustomerName(request.getCustomerName());
            mcReceipt.setCustomerId(request.getCustomerId());
            mcReceipt.setAmount(request.getAmount());
            mcReceipt.setDescription(request.getNote());
            mcReceipt.setNo(userService.genCode(user.getCompanyId(), Constants.PHIEU_THU));
            mcReceiptRepository.save(mcReceipt);
            log.info(ENTITY_NAME + "_create: " + ResultConstants.RECEIPT_CREATE_SUCCESS_VI);
            result = ResultConstants.RECEIPT_CREATE_SUCCESS_VI;
        }
        if (request.getType() == 1) {
            mcPayment.setComId(request.getComId());
            mcPayment.setTypeDesc(request.getTypeDesc());
            mcPayment.setDate(request.getDate());
            mcPayment.setTypeDesc(request.getTypeDesc());
            mcPayment.setBusinessTypeId(
                request.getBusinessType() == null
                    ? (businessTypeRepository.getIdByComIdAndCode(request.getComId(), BusinessTypeConstants.Code.PAYMENT_UNKNOWN))
                    : request.getBusinessType()
            );
            mcPayment.setCustomerName(request.getCustomerName());
            mcPayment.setCustomerId(request.getCustomerId());
            mcPayment.setAmount(request.getAmount());
            mcPayment.setDescription(request.getNote());
            mcPayment.setNo(userService.genCode(user.getCompanyId(), Constants.PHIEU_CHI));
            mcPaymentRepository.save(mcPayment);
            log.info(ENTITY_NAME + "_create: " + ResultConstants.PAYMENT_CREATE_SUCCESS_VI);
            result = ResultConstants.PAYMENT_CREATE_SUCCESS_VI;
        }

        return new ResultDTO(ResultConstants.SUCCESS, result, true);
    }

    public ResultDTO update(ReceiptPaymentUpdateRequest request) {
        log.info(ENTITY_NAME + " request to update by id: {}", request.getId());
        User user = userService.getUserWithAuthorities(request.getComId());
        String result = "";
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestAlertException(ExceptionConstants.AMOUNT_INVALID_VI, ENTITY_NAME, ExceptionConstants.AMOUNT_INVALID_CODE);
        }
        if (Objects.equals(request.getType(), BusinessTypeConstants.Type.RECEIPT)) {
            Optional<McReceipt> mcReceiptOptional = mcReceiptRepository.findById(request.getId());
            if (mcReceiptOptional.isEmpty()) {
                log.error(ExceptionConstants.RECEIPT_NOT_EXISTS);
                throw new InternalServerException(
                    ExceptionConstants.RECEIPT_NOT_EXISTS,
                    ENTITY_NAME,
                    ExceptionConstants.RECEIPT_NOT_EXISTS_CODE
                );
            }
            McReceipt mcReceipt = mcReceiptOptional.get();
            if (request.getBusinessType() != null) {
                Optional<BusinessType> businessTypeOptional = businessTypeRepository.findByIdAndComId(
                    request.getBusinessType(),
                    user.getCompanyId()
                );
                if (businessTypeOptional.isEmpty()) {
                    throw new BadRequestAlertException(
                        ExceptionConstants.BUSINESS_TYPE_NOT_FOUND_VI,
                        ENTITY_NAME,
                        ExceptionConstants.BUSINESS_TYPE_NOT_FOUND
                    );
                }
            }
            if (mcReceipt.getBillId() != null || mcReceipt.getRsInoutWardId() != null) {
                log.error(ExceptionConstants.RECEIPT_UPDATE_PROHIBITED);
                throw new InternalServerException(
                    ExceptionConstants.RECEIPT_UPDATE_PROHIBITED,
                    ENTITY_NAME,
                    ExceptionConstants.RECEIPT_UPDATE_PROHIBITED_CODE
                );
            } else {
                mcReceipt.setComId(request.getComId());
                mcReceipt.setBusinessTypeId(
                    request.getBusinessType() == null
                        ? (businessTypeRepository.getIdByComIdAndCode(request.getComId(), BusinessTypeConstants.Code.RECEIPT_UNKNOWN))
                        : request.getBusinessType()
                );
                mcReceipt.setTypeDesc(request.getTypeDesc());
                mcReceipt.setDate(request.getDate());
                mcReceipt.setCustomerName(request.getCustomerName());
                mcReceipt.setCustomerId(request.getCustomerId());
                mcReceipt.setAmount(request.getAmount());
                mcReceipt.setDescription(request.getNote());
                mcReceiptRepository.save(mcReceipt);
                log.info(ENTITY_NAME + "_update: " + ResultConstants.RECEIPT_UPDATE_SUCCESS_VI);
                result = ResultConstants.RECEIPT_UPDATE_SUCCESS_VI;
            }
        } else if (Objects.equals(request.getType(), BusinessTypeConstants.Type.PAYMENT)) {
            Optional<McPayment> mcPaymentOptional = mcPaymentRepository.findById(request.getId());
            if (mcPaymentOptional.isEmpty()) {
                log.error(ExceptionConstants.PAYMENT_NOT_EXISTS);
                throw new InternalServerException(
                    ExceptionConstants.PAYMENT_NOT_EXISTS,
                    ENTITY_NAME,
                    ExceptionConstants.PAYMENT_NOT_EXISTS_CODE
                );
            }
            McPayment mcPayment = mcPaymentOptional.get();
            if (request.getBusinessType() != null) {
                Optional<BusinessType> businessTypeOptional = businessTypeRepository.findByIdAndComId(
                    request.getBusinessType(),
                    user.getCompanyId()
                );
                if (businessTypeOptional.isEmpty()) {
                    throw new BadRequestAlertException(
                        ExceptionConstants.BUSINESS_TYPE_NOT_FOUND_VI,
                        ENTITY_NAME,
                        ExceptionConstants.BUSINESS_TYPE_NOT_FOUND
                    );
                }
            }
            if (mcPayment.getRsInoutWardId() != null || mcPayment.getBillId() != null) {
                log.error(ExceptionConstants.RECEIPT_UPDATE_PROHIBITED);
                throw new InternalServerException(
                    ExceptionConstants.PAYMENT_UPDATE_PROHIBITED,
                    ENTITY_NAME,
                    ExceptionConstants.PAYMENT_UPDATE_PROHIBITED_CODE
                );
            } else {
                mcPayment.setComId(request.getComId());
                mcPayment.setTypeDesc(request.getTypeDesc());
                mcPayment.setDate(request.getDate());
                mcPayment.setTypeDesc(request.getTypeDesc());
                mcPayment.setBusinessTypeId(
                    request.getBusinessType() == null
                        ? (businessTypeRepository.getIdByComIdAndCode(request.getComId(), BusinessTypeConstants.Code.PAYMENT_UNKNOWN))
                        : request.getBusinessType()
                );
                mcPayment.setCustomerName(request.getCustomerName());
                mcPayment.setCustomerId(request.getCustomerId());
                mcPayment.setAmount(request.getAmount());
                mcPayment.setDescription(request.getNote());
                mcPaymentRepository.save(mcPayment);
                log.info(ENTITY_NAME + "_update: " + ResultConstants.PAYMENT_UPDATE_SUCCESS_VI);
                result = ResultConstants.PAYMENT_UPDATE_SUCCESS_VI;
            }
        } else {
            throw new BadRequestAlertException(ExceptionConstants.TYPE_INVALID_VI, ENTITY_NAME, ExceptionConstants.TYPE_INVALID);
        }

        return new ResultDTO(ResultConstants.SUCCESS, result, true);
    }

    public ResultDTO delete(ReceiptPaymentDeleteRequest request) {
        log.info(ENTITY_NAME + " request to delete by id: {}", request.getId());
        userService.getUserWithAuthorities(request.getComId());

        String result = "";
        if (request.getType() == 0) {
            Optional<McReceipt> mcReceiptOptional = mcReceiptRepository.findById(request.getId());
            if (mcReceiptOptional.isEmpty()) {
                log.error(ExceptionConstants.RECEIPT_NOT_EXISTS);
                throw new InternalServerException(
                    ExceptionConstants.RECEIPT_NOT_EXISTS,
                    ENTITY_NAME,
                    ExceptionConstants.RECEIPT_NOT_EXISTS_CODE
                );
            } else {
                McReceipt mcReceipt = mcReceiptOptional.get();
                String no = request.getNo().trim();
                if (!isNumeric(no)) {
                    if (!no.equals(mcReceipt.getNo())) {
                        log.error(ExceptionConstants.RECEIPT_INVALID);
                        throw new InternalServerException(
                            ExceptionConstants.RECEIPT_INVALID,
                            ENTITY_NAME,
                            ExceptionConstants.RECEIPT_INVALID_CODE
                        );
                    }
                } else {
                    if (!no.equals(mcReceipt.getNo()) || BigDecimal.valueOf(Double.parseDouble(no)).compareTo(mcReceipt.getAmount()) != 0) {
                        log.error(ExceptionConstants.RECEIPT_INVALID);
                        throw new InternalServerException(
                            ExceptionConstants.RECEIPT_INVALID,
                            ENTITY_NAME,
                            ExceptionConstants.RECEIPT_INVALID_CODE
                        );
                    }
                }

                if (mcReceipt.getRsInoutWardId() != null || mcReceipt.getBillId() != null) {
                    log.error(ExceptionConstants.RECEIPT_DELETE_PROHIBITED_CODE_VI);
                    throw new InternalServerException(
                        ExceptionConstants.RECEIPT_DELETE_PROHIBITED_CODE_VI,
                        ENTITY_NAME,
                        ExceptionConstants.RECEIPT_DELETE_PROHIBITED_CODE
                    );
                }
                mcReceiptRepository.delete(mcReceipt);
                log.info(ENTITY_NAME + "_delete: " + ResultConstants.RECEIPT_DELETE_SUCCESS_VI);
                result = ResultConstants.RECEIPT_DELETE_SUCCESS_VI;
            }
        }
        if (request.getType() == 1) {
            Optional<McPayment> mcPaymentOptional = mcPaymentRepository.findById(request.getId());
            if (mcPaymentOptional.isEmpty()) {
                log.error(ExceptionConstants.PAYMENT_NOT_EXISTS);
                throw new InternalServerException(
                    ExceptionConstants.PAYMENT_NOT_EXISTS,
                    ENTITY_NAME,
                    ExceptionConstants.PAYMENT_NOT_EXISTS_CODE
                );
            } else {
                McPayment mcPayment = mcPaymentOptional.get();
                String no = request.getNo().trim();
                if (!isNumeric(no)) {
                    if (!no.equals(mcPayment.getNo())) {
                        log.error(ExceptionConstants.RECEIPT_INVALID);
                        throw new InternalServerException(
                            ExceptionConstants.RECEIPT_INVALID,
                            ENTITY_NAME,
                            ExceptionConstants.RECEIPT_INVALID_CODE
                        );
                    }
                } else {
                    if (!no.equals(mcPayment.getNo()) || BigDecimal.valueOf(Double.parseDouble(no)).compareTo(mcPayment.getAmount()) != 0) {
                        log.error(ExceptionConstants.RECEIPT_INVALID);
                        throw new InternalServerException(
                            ExceptionConstants.RECEIPT_INVALID,
                            ENTITY_NAME,
                            ExceptionConstants.RECEIPT_INVALID_CODE
                        );
                    }
                }

                if (mcPayment.getRsInoutWardId() != null || mcPayment.getBillId() != null) {
                    log.error(ExceptionConstants.PAYMENT_DELETE_PROHIBITED_CODE_VI);
                    throw new InternalServerException(
                        ExceptionConstants.PAYMENT_DELETE_PROHIBITED_CODE_VI,
                        ENTITY_NAME,
                        ExceptionConstants.PAYMENT_DELETE_PROHIBITED_CODE
                    );
                }
                mcPaymentRepository.delete(mcPayment);
                log.info(ENTITY_NAME + "_delete: " + ResultConstants.PAYMENT_DELETE_SUCCESS_VI);
                result = ResultConstants.PAYMENT_DELETE_SUCCESS_VI;
            }
        }
        return new ResultDTO(ResultConstants.SUCCESS, result, true);
    }

    @Override
    public ResultDTO insertMissingData() {
        List<Bill> bills = billRepository.getBillNotInMcReceipt();
        List<McReceipt> mcReceipts = new ArrayList<>();
        List<BusinessType> businessTypes = businessTypeRepository.findByBusinessTypeCode(BusinessTypeConstants.Code.OUT_WARD);
        Map<Integer, Integer> businessTypeMap = new HashMap<>();
        for (BusinessType businessType : businessTypes) {
            businessTypeMap.put(businessType.getComId(), businessType.getId());
        }
        for (Bill bill : bills) {
            BillPayment billPayment = bill.getPayment();
            if (billPayment.getAmount() != null && billPayment.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                McReceipt mcReceipt = new McReceipt();
                mcReceipt.setBillId(bill.getId());
                mcReceipt.setComId(bill.getComId());
                mcReceipt.setTypeDesc(Constants.PHIEU_THU_BAN_HANG);
                mcReceipt.setDate(bill.getBillDate());
                mcReceipt.setDescription(Constants.MC_RECEIPT_REASON + bill.getCode());
                try {
                    mcReceipt.setNo(userService.genCode(bill.getComId(), Constants.PHIEU_THU));
                } catch (Exception e) {
                    mcReceipt.setNo(null);
                }
                if (bill.getCustomerId() != null) {
                    mcReceipt.setCustomerId(bill.getCustomerId());
                }
                if (bill.getCustomerName() != null) {
                    mcReceipt.setCustomerName(bill.getCustomerName());
                }
                mcReceipt.setAmount(billPayment.getAmount());
                if (businessTypeMap.containsKey(bill.getComId())) {
                    mcReceipt.setBusinessTypeId(businessTypeMap.get(bill.getComId()));
                }
                mcReceipt.setCustomerNormalizedName(Common.normalizedName(Arrays.asList(mcReceipt.getCustomerName())));
                mcReceipts.add(mcReceipt);
            }
        }
        mcReceiptRepository.saveAll(mcReceipts);
        List<RsInoutWard> rsInoutWards = rsInoutWardRepository.getAllMissing();
        for (RsInoutWard rsInoutWard : rsInoutWards) {
            try {
                rsInoutWardManagementService.insertToMcPayment(rsInoutWard, "Tiền mặt");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS, true);
    }

    @Override
    public ResultDTO deleteList(DeleteReceiptPaymentList request) {
        User user = userService.getUserWithAuthorities(request.getComId());
        Integer comId = user.getCompanyId();
        GetAllTransactionsAlternative data = receiptPaymentRepositoryCustom.getAllTransactions(
            null,
            comId,
            request.getFromDate(),
            request.getToDate(),
            request.getType(),
            request.getKeyword(),
            true,
            request.getParamCheckAll(),
            request.getIds()
        );
        List<Object> listError = new ArrayList<>();
        List<McReceipt> receiptDelete = new ArrayList<>();
        List<McPayment> paymentDelete = new ArrayList<>();

        for (ReceiptPayment receiptPayment : data.getReceiptPaymentList().getContent()) {
            if (receiptPayment.getType().equals(BusinessTypeConstants.Type.RECEIPT)) {
                Optional<McReceipt> mcReceiptOptional = mcReceiptRepository.findById(receiptPayment.getId());
                if (mcReceiptOptional.isEmpty()) {
                    receiptPayment.setErrorMessage(ExceptionConstants.RECEIPT_NOT_EXISTS);
                    listError.add(receiptPayment);
                } else {
                    McReceipt mcReceipt = mcReceiptOptional.get();
                    if (mcReceipt.getRsInoutWardId() != null || mcReceipt.getBillId() != null) {
                        receiptPayment.setErrorMessage(ExceptionConstants.RECEIPT_DELETE_PROHIBITED_CODE_VI);
                        listError.add(receiptPayment);
                    } else {
                        receiptDelete.add(mcReceipt);
                    }
                }
            }
            if (receiptPayment.getType().equals(BusinessTypeConstants.Type.PAYMENT)) {
                Optional<McPayment> mcPaymentOptional = mcPaymentRepository.findById(receiptPayment.getId());
                if (mcPaymentOptional.isEmpty()) {
                    receiptPayment.setErrorMessage(ExceptionConstants.PAYMENT_NOT_EXISTS);
                    listError.add(receiptPayment);
                } else {
                    McPayment mcPayment = mcPaymentOptional.get();
                    if (mcPayment.getRsInoutWardId() != null || mcPayment.getBillId() != null) {
                        receiptPayment.setErrorMessage(ExceptionConstants.PAYMENT_DELETE_PROHIBITED_CODE_VI);
                        listError.add(receiptPayment);
                    } else {
                        paymentDelete.add(mcPayment);
                    }
                }
            }
        }

        mcReceiptRepository.deleteAll(receiptDelete);
        mcPaymentRepository.deleteAll(paymentDelete);

        DataResponse response = new DataResponse();
        response.setCountAll(data.getReceiptPaymentList().getContent().size());
        response.setCountFalse(listError.size());
        response.setDataFalse(listError);
        response.setCountSuccess(response.getCountAll() - response.getCountFalse());
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.DELETE_LIST_PRODUCT_UNIT_SUCCESS, true, response);
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
