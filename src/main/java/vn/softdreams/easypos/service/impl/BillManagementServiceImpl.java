package vn.softdreams.easypos.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Strings;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import vn.softdreams.easypos.config.Constants;
import vn.softdreams.easypos.constants.*;
import vn.softdreams.easypos.domain.*;
import vn.softdreams.easypos.dto.TaskLogSendQueue;
import vn.softdreams.easypos.dto.bill.*;
import vn.softdreams.easypos.dto.card.CardPolicyConditions;
import vn.softdreams.easypos.dto.config.InvoiceConfig;
import vn.softdreams.easypos.dto.customer.CustomerResponse;
import vn.softdreams.easypos.dto.invoice.DiscountAmountInvoice;
import vn.softdreams.easypos.dto.invoice.PublishInvoiceQueue;
import vn.softdreams.easypos.dto.invoice.TaskCancelInvoice;
import vn.softdreams.easypos.dto.processingRequest.BillProductRelation;
import vn.softdreams.easypos.dto.product.ProductCheckBill;
import vn.softdreams.easypos.dto.product.ProductImagesResult;
import vn.softdreams.easypos.dto.productGroup.ProductProcessingAreaResult;
import vn.softdreams.easypos.dto.productProductUnit.OnHandItem;
import vn.softdreams.easypos.dto.toppingGroup.ToppingItem;
import vn.softdreams.easypos.dto.toppingGroup.ToppingRequiredItem;
import vn.softdreams.easypos.dto.voucher.*;
import vn.softdreams.easypos.integration.easybooks88.queue.dto.RsInOutWardTask;
import vn.softdreams.easypos.integration.easybooks88.queue.dto.SAInvoiceTask;
import vn.softdreams.easypos.repository.*;
import vn.softdreams.easypos.service.BillManagementService;
import vn.softdreams.easypos.service.UserService;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.util.Common;
import vn.softdreams.easypos.web.rest.errors.BadRequestAlertException;
import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;
import vn.softdreams.easypos.web.rest.errors.InternalServerException;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static vn.softdreams.easypos.web.rest.errors.ExceptionConstants.*;

@Service
@Transactional
public class BillManagementServiceImpl implements BillManagementService {

    private static final String ENTITY_NAME = "BillManagementServiceImpl";
    private final Logger log = LoggerFactory.getLogger(ProductManagementServiceImpl.class);

    private final UserRepository userRepository;
    private final UserService userService;
    private final BillRepository billRepository;
    private final CustomerRepository customerRepository;
    private final ConfigRepository configRepository;
    private final InvoiceRepository invoiceRepository;
    private final BillProductRepository billProductRepository;
    private final McReceiptRepository mcReceiptRepository;
    private final InvoiceProductRepository invoiceProductRepository;
    private final RsInoutWardRepository rsInoutWardRepository;
    private final RsInoutWardDetailRepository rsInoutWardDetailRepository;
    private final BillPaymentRepository billPaymentRepository;
    private final ProductRepository productRepository;
    private final ProductUnitRepository productUnitRepository;
    private final ProductProductUnitRepository productProductUnitRepository;
    private final ReceivableRepository receivableRepository;
    private final ReservationRepository reservationRepository;
    private final TaskLogRepository taskLogRepository;
    private final UserRoleRepository userRoleRepository;
    private final ObjectMapper objectMapper;
    private final PayableRepository payableRepository;
    private final ModelMapper modelMapper;
    private final AreaUnitRepository areaUnitRepository;
    private final ProductToppingRepository productToppingRepository;
    private final ToppingGroupRepository toppingGroupRepository;
    private final TransactionTemplate transactionTemplate;
    private final BusinessTypeRepository businessTypeRepository;
    private final ConfigManagementServiceImpl configManagementService;
    private final CustomerCardRepository customerCardRepository;
    private final LoyaltyCardRepository loyaltyCardRepository;
    private final LoyaltyCardUsageRepository loyaltyCardUsageRepository;
    private final CustomerManagementServiceImpl customerManagementService;
    private final CardPolicyRepository cardPolicyRepository;
    private final McPaymentRepository mcPaymentRepository;
    private final VoucherRepository voucherRepository;
    private final CompanyRepository companyRepository;
    private final VoucherUsageRepository voucherUsageRepository;
    private final VoucherApplyRepository voucherApplyRepository;
    private final VoucherManagementServiceImpl voucherManagementService;
    private final BillConfigRepository billConfigRepository;
    private final ProcessingAreaRepository processingAreaRepository;
    private final ProcessingRequestRepository processingRequestRepository;
    private final ProcessingRequestDetailRepository processingRequestDetailRepository;
    private final ProcessingProductRepository processingProductRepository;
    private final NotificationRepository notificationRepository;

    public BillManagementServiceImpl(
        UserRepository userRepository,
        UserService userService,
        BillRepository billRepository,
        CustomerRepository customerRepository,
        ConfigRepository configRepository,
        InvoiceRepository invoiceRepository,
        TaskLogRepository taskLogRepository,
        McReceiptRepository mcReceiptRepository,
        InvoiceProductRepository invoiceProductRepository,
        RsInoutWardRepository rsInoutWardRepository,
        RsInoutWardDetailRepository rsInoutWardDetailRepository,
        BillProductRepository billProductRepository,
        BillPaymentRepository billPaymentRepository,
        ProductRepository productRepository,
        ProductUnitRepository productUnitRepository,
        ProductProductUnitRepository productProductUnitRepository,
        ReceivableRepository receivableRepository,
        ReservationRepository reservationRepository,
        UserRoleRepository userRoleRepository,
        RsInoutWardManagementServiceImpl rsInoutWardServiceImpl,
        ObjectMapper objectMapper,
        PayableRepository payableRepository,
        ModelMapper modelMapper,
        AreaUnitRepository areaUnitRepository,
        ProductToppingRepository productToppingRepository,
        ToppingGroupRepository toppingGroupRepository,
        TransactionTemplate transactionTemplate,
        BusinessTypeRepository businessTypeRepository,
        ConfigManagementServiceImpl configManagementService,
        EntityManager entityManager,
        McPaymentRepository mcPaymentRepository,
        CustomerCardRepository customerCardRepository,
        LoyaltyCardRepository loyaltyCardRepository,
        LoyaltyCardUsageRepository loyaltyCardUsageRepository,
        CustomerManagementServiceImpl customerManagementService,
        CardPolicyRepository cardPolicyRepository,
        VoucherRepository voucherRepository,
        CompanyRepository companyRepository,
        VoucherUsageRepository voucherUsageRepository,
        VoucherApplyRepository voucherApplyRepository,
        VoucherManagementServiceImpl voucherManagementService,
        BillConfigRepository billConfigRepository,
        ProcessingAreaRepository processingAreaRepository,
        ProcessingRequestRepository processingRequestRepository,
        ProcessingRequestDetailRepository processingRequestDetailRepository,
        ProcessingProductRepository processingProductRepository,
        NotificationRepository notificationRepository
    ) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.billRepository = billRepository;
        this.customerRepository = customerRepository;
        this.configRepository = configRepository;
        this.invoiceRepository = invoiceRepository;
        this.billProductRepository = billProductRepository;
        this.taskLogRepository = taskLogRepository;
        this.mcReceiptRepository = mcReceiptRepository;
        this.invoiceProductRepository = invoiceProductRepository;
        this.rsInoutWardRepository = rsInoutWardRepository;
        this.rsInoutWardDetailRepository = rsInoutWardDetailRepository;
        this.billPaymentRepository = billPaymentRepository;
        this.productRepository = productRepository;
        this.productUnitRepository = productUnitRepository;
        this.productProductUnitRepository = productProductUnitRepository;
        this.receivableRepository = receivableRepository;
        this.reservationRepository = reservationRepository;
        this.userRoleRepository = userRoleRepository;
        this.payableRepository = payableRepository;
        this.areaUnitRepository = areaUnitRepository;
        this.productToppingRepository = productToppingRepository;
        this.toppingGroupRepository = toppingGroupRepository;
        this.objectMapper = objectMapper;
        this.modelMapper = modelMapper;
        this.transactionTemplate = transactionTemplate;
        this.businessTypeRepository = businessTypeRepository;
        this.configManagementService = configManagementService;
        this.customerCardRepository = customerCardRepository;
        this.loyaltyCardRepository = loyaltyCardRepository;
        this.loyaltyCardUsageRepository = loyaltyCardUsageRepository;
        this.customerManagementService = customerManagementService;
        this.cardPolicyRepository = cardPolicyRepository;
        this.mcPaymentRepository = mcPaymentRepository;
        this.voucherRepository = voucherRepository;
        this.companyRepository = companyRepository;
        this.voucherUsageRepository = voucherUsageRepository;
        this.voucherApplyRepository = voucherApplyRepository;
        this.voucherManagementService = voucherManagementService;
        this.billConfigRepository = billConfigRepository;
        this.processingAreaRepository = processingAreaRepository;
        this.processingRequestRepository = processingRequestRepository;
        this.processingRequestDetailRepository = processingRequestDetailRepository;
        this.processingProductRepository = processingProductRepository;
        this.notificationRepository = notificationRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public ResultDTO searchBills(Pageable pageable, Integer status, String fromDate, String toDate, String keyword, Boolean isCountAll) {
        ResultDTO resultDTO = new ResultDTO();
        log.debug("Request to get Bills by status");
        User user = userService.getUserWithAuthorities();

        Page<BillItemResponse> page = billRepository.searchBills(
            pageable,
            status,
            fromDate,
            toDate,
            keyword,
            user.getCompanyId(),
            isCountAll
        );
        List<BillItemResponse> itemResponses = page.getContent();
        itemResponses = getCustomerCardResponse(itemResponses, user);
        resultDTO.setMessage(ResultConstants.SUCCESS);
        resultDTO.setReason(ResultConstants.SUCCESS_GET_LIST);
        resultDTO.setStatus(true);
        resultDTO.setData(page.getContent());
        resultDTO.setCount((int) page.getTotalElements());
        return resultDTO;
    }

    @Override
    public ResultDTO getBillByIdAndCompanyId(Integer id) {
        User user = userService.getUserWithAuthorities();
        Optional<Bill> bill = billRepository.findByIdAndComId(id, user.getCompanyId());
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_GET_DETAIL, true, transferBillDetail(bill, user), 1);
    }

    @Override
    @Transactional(readOnly = true)
    public ResultDTO getBillById(Integer id) {
        User user = userService.getUserWithAuthorities();
        Optional<Bill> bill = billRepository.findById(id);
        if (bill.isPresent()) {
            return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_GET_DETAIL, true, bill, 1);
        }
        throw new InternalServerException(
            ExceptionConstants.BILL_NOT_FOUND,
            ExceptionConstants.BILL_NOT_FOUND + id,
            ExceptionConstants.BILL_NOT_FOUND_VI
        );
    }

    @Override
    public ResultDTO updateBillActivate(BillCompleteRequest billRequest) {
        ResultDTO resultDTO = new ResultDTO();
        log.error("updateStatusBill : {}", billRequest);
        User user = userService.getUserWithAuthorities();
        Optional<Bill> billOptional = billRepository.findByIdAndComId(billRequest.getBillId(), user.getCompanyId());
        if (billOptional.isEmpty() || !billOptional.get().getCode().equals(billRequest.getBillCode())) {
            throw new InternalServerException(
                ExceptionConstants.BILL_ID_NOT_FOUND_VI,
                Constants.ID + billRequest.getBillId(),
                ExceptionConstants.BILL_ID_NOT_FOUND
            );
        }
        Bill bill = billOptional.get();
        if (
            bill.getCustomerName().equals(Constants.CUSTOMER_NAME) &&
            billRequest.getCardAmount() != null &&
            billRequest.getCardAmount().compareTo(BigDecimal.ZERO) > 0
        ) {
            throw new BadRequestAlertException(
                ExceptionConstants.PAYMENT_BY_CARD_INVALID_VI,
                ENTITY_NAME,
                ExceptionConstants.PAYMENT_BY_CARD_INVALID
            );
        }
        Set<Integer> ids = new HashSet<>();
        List<Integer> productIds = new ArrayList<>();
        List<Integer> productUnitIds = new ArrayList<>();
        List<String> unitNames = new ArrayList<>();
        for (BillProduct billProduct : bill.getProducts()) {
            if (billProduct.getProductId() != null) {
                ids.add(billProduct.getProductId());
            }
            productIds.add(billProduct.getProductId());
            productUnitIds.add(billProduct.getUnitId());
            unitNames.add(billProduct.getUnit());
        }
        List<BillProductChangeUnit> changeUnits = billRepository.checkChangeUnit(
            user.getCompanyId(),
            productIds,
            productUnitIds,
            unitNames
        );
        List<Integer> units = changeUnits.stream().map(BillProductChangeUnit::getProductId).collect(Collectors.toList());
        for (BillProduct billProduct : bill.getProducts()) {
            if (!units.contains(billProduct.getProductId())) {
                throw new BadRequestAlertException(
                    ExceptionConstants.BILL_PRODUCT_NOT_VALID_VI.replace("@@", billProduct.getProductName()),
                    ENTITY_NAME,
                    ExceptionConstants.BILL_PRODUCT_NOT_VALID
                );
            }
        }
        List<ProductCheckBill> products = productRepository.productCheckBill(user.getCompanyId(), ids);
        if (!products.stream().map(ProductCheckBill::getId).collect(Collectors.toSet()).containsAll(ids)) {
            throw new BadRequestAlertException(
                ExceptionConstants.BILL_NOT_DONE_PRODUCT_VI,
                ENTITY_NAME,
                ExceptionConstants.BILL_CANNOT_DONE
            );
        }
        Optional<Customer> customerOptional = customerRepository.findOneByIdAndComId(bill.getCustomerId(), user.getCompanyId());
        if (
            customerOptional.isEmpty() ||
            customerOptional.get().getActive() == null ||
            (customerOptional.get().getActive() != null && !customerOptional.get().getActive())
        ) {
            throw new BadRequestAlertException(ExceptionConstants.BILL_CANNOT_DONE_VI, ENTITY_NAME, ExceptionConstants.BILL_CANNOT_DONE);
        }
        if (bill.getStatus() == 1 || bill.getStatus() == 2) {
            throw new InternalServerException(
                ExceptionConstants.BILL_STATUS_INVALID_VI,
                ENTITY_NAME,
                ExceptionConstants.BILL_STATUS_INVALID
            );
        }
        BillPayment billPayment = bill.getPayment();
        billPayment.setPaymentMethod(billRequest.getPaymentMethod());
        if (billRequest.getAmount() != null) {
            billPayment.setAmount(billRequest.getAmount());
        }
        if (billRequest.getCardAmount() != null) {
            billPayment.setAmount(billPayment.getAmount().add(billRequest.getCardAmount()));
        }
        if (billRequest.getRefund() != null) {
            billPayment.setRefund(billRequest.getRefund());
            billPayment.setAmount(billPayment.getAmount().subtract(billRequest.getRefund()));
        }
        if (billRequest.getDebt() != null) {
            billPayment.setDebt(billRequest.getDebt());
        }
        bill.setStatus(BillConstants.Status.BILL_COMPLETE);
        bill.setPayment(billPayment);
        if (!bill.getCustomerName().equals(Constants.CUSTOMER_NAME)) {
            calculateRankCard(bill, billRequest.getCardAmount(), null, null);
        }
        if (bill.getStatus().equals(BillConstants.Status.BILL_COMPLETE)) {
            Integer countInvoice = invoiceRepository.countInvoiceByBillId(bill.getId());
            if (countInvoice > 0) {
                throw new InternalServerException(
                    ExceptionConstants.INVOICE_ALREADY_VI,
                    Constants.ID + bill.getId(),
                    ExceptionConstants.INVOICE_ALREADY
                );
            }
            Optional<String> patternOptional = configRepository.getValueByComIdAndCode(
                user.getCompanyId(),
                EasyInvoiceConstants.INVOICE_PATTERN
            );
            String pattern = "";
            if (patternOptional.isPresent()) {
                pattern = patternOptional.get();
            }
            Optional<String> methodOptional = configRepository.getValueByComIdAndCode(
                user.getCompanyId(),
                EasyInvoiceConstants.INVOICE_METHOD
            );
            int method = 0;
            if (methodOptional.isPresent()) {
                method = Integer.parseInt(methodOptional.get());
            }
            billCompletion(user.getCompanyId(), bill, pattern, method, false);
        }
        billRepository.save(bill);
        resultDTO.setStatus(true);
        resultDTO.setMessage(ResultConstants.SUCCESS);
        resultDTO.setReason(ResultConstants.SUCCESS_BILL_STATUS);
        return resultDTO;
    }

    public Bill convertBill(User user, BillCreateRequest billDTO, Integer invoiceType, String pattern) {
        Bill bill = new Bill();
        BillConfig billConfigOld = null;
        bill = modelMapper.map(billDTO, Bill.class);
        InvoiceConfig invoiceConfig = configManagementService.getConfigStoreByCompanyID(user);
        Bill finalBill = bill;
        String taxReduction;
        Optional<String> taxReductionOptional = configRepository.getValueByComIdAndCode(user.getCompanyId(), Constants.TAX_REDUCTION_CODE);
        taxReduction = taxReductionOptional.orElse("");
        billDTO
            .getProducts()
            .forEach(billProductRequest -> {
                if (billProductRequest.getProductProductUnitId() == 0) {
                    throw new BadRequestAlertException(
                        ExceptionConstants.BILL_PRODUCT_NOT_VALID_VI.replace("@@", billProductRequest.getProductName()),
                        ENTITY_NAME,
                        ExceptionConstants.BILL_PRODUCT_NOT_VALID
                    );
                }
                for (BillProduct billProduct : finalBill.getProducts()) {
                    if (
                        billProduct.getVatAmount() != null &&
                        billProduct.getVatAmount().compareTo(BigDecimal.ZERO) < 0 &&
                        !taxReduction.equals(ProductConstant.TAX_REDUCTION_TYPE.GIAM_TRU_RIENG)
                    ) {
                        throw new BadRequestAlertException(
                            ExceptionConstants.VAT_AMOUNT_NOT_VALID_VI.replace("@@", billProduct.getProductName()),
                            ENTITY_NAME,
                            ExceptionConstants.VAT_AMOUNT_NOT_VALID
                        );
                    }
                    if (billProduct.getProductCode().equals(Constants.PRODUCT_CODE_DEFAULT)) {
                        Optional<String> discountNameOption = configRepository.getValueByComIdAndCode(
                            user.getCompanyId(),
                            Constants.INV_DYNAMIC_DISCOUNT_NAME_CODE
                        );
                        String discountName = "";
                        if (discountNameOption.isPresent()) {
                            discountName = discountNameOption.get();
                        }
                        //                        if (
                        //                            !discountName.equals(BillConstants.InvDynamicDiscountName.SHOW) &&
                        //                            !billProduct.getProductName().equals(CommonConstants.PRODUCT_NAME_CREATE_DEFAULT) &&
                        //                            !billProduct.getProductName().equals(CommonConstants.BILL_PRODUCT_NAME_CREATE_DEFAULT)
                        //                        ) {
                        //                            throw new BadRequestAlertException(
                        //                                ExceptionConstants.CONFIG_INV_DYNAMIC_INVALID_VI,
                        //                                ENTITY_NAME,
                        //                                ExceptionConstants.CONFIG_INV_DYNAMIC_INVALID_CODE
                        //                            );
                        //                        }
                    }
                    if (billProduct.getProductCode().equals(billProductRequest.getProductCode())) {
                        if (
                            billProductRequest.getUnit() == null ||
                            (billProductRequest.getUnit() != null && billProduct.getUnit().equals(billProductRequest.getUnit()))
                        ) {
                            billProduct.setProductId(billProductRequest.getProductProductUnitId());
                        }
                    }
                    if (billProduct.getProductCode().equals(Constants.PRODUCT_CODE_DEFAULT)) {
                        Optional<String> discountNameOption = configRepository.getValueByComIdAndCode(
                            user.getCompanyId(),
                            Constants.INV_DYNAMIC_DISCOUNT_NAME_CODE
                        );
                        String discountName = "";
                        if (discountNameOption.isPresent()) {
                            discountName = discountNameOption.get();
                        }
                        if (
                            !discountName.equals(BillConstants.InvDynamicDiscountName.SHOW) &&
                            !billProduct.getProductName().equals(CommonConstants.PRODUCT_NAME_CREATE_DEFAULT) &&
                            !billProduct.getProductName().equals(CommonConstants.BILL_PRODUCT_NAME_CREATE_DEFAULT) &&
                            invoiceConfig.getPushVoucherDiscountInvoice() == null
                        ) {
                            throw new BadRequestAlertException(
                                ExceptionConstants.CONFIG_INV_DYNAMIC_INVALID_VI,
                                ENTITY_NAME,
                                ExceptionConstants.CONFIG_INV_DYNAMIC_INVALID_CODE
                            );
                        }
                    }
                }
            });
        Bill billOld = new Bill();
        boolean isNew = bill.getId() == null;
        if (!isNew) {
            Optional<Bill> billOptional = billRepository.findByIdAndComId(bill.getId(), user.getCompanyId());
            if (billOptional.isEmpty()) {
                throw new BadRequestAlertException(
                    ExceptionConstants.BILL_NOT_FOUND_VI,
                    ExceptionConstants.BILL_NOT_FOUND_VI,
                    ExceptionConstants.BILL_NOT_FOUND
                );
            } else {
                billOld = billOptional.get();
                if (!Objects.equals(billOld.getStatus(), BillConstants.Status.BILL_DONT_COMPLETE)) {
                    throw new BadRequestAlertException(
                        ExceptionConstants.BILL_NOT_UPDATE_VI,
                        ExceptionConstants.BILL_NOT_UPDATE_VI,
                        ExceptionConstants.BILL_NOT_UPDATE
                    );
                }
                if (billOld.getCustomerId() != null && billOld.getCustomerId() != 1) {
                    Optional<Customer> customerOptional = customerRepository.findOneByIdAndComId(
                        billOld.getCustomerId(),
                        user.getCompanyId()
                    );
                    if (
                        customerOptional.isEmpty() ||
                        customerOptional.get().getActive() == null ||
                        (customerOptional.get().getActive() != null && !customerOptional.get().getActive())
                    ) {
                        throw new BadRequestAlertException(
                            ExceptionConstants.BILL_CANNOT_UPDATE_VI,
                            ExceptionConstants.BILL_NOT_UPDATE_VI,
                            ExceptionConstants.BILL_CANNOT_UPDATE
                        );
                    }
                }
                if (billOld.getAreaUnitId() != null && billOld.getAreaUnitId() != 0) {
                    Optional<AreaUnit> areaUnitOptional = areaUnitRepository.findByIdAndComId(billOld.getAreaUnitId(), user.getCompanyId());
                    if (areaUnitOptional.isEmpty()) {
                        throw new BadRequestAlertException(
                            ExceptionConstants.BILL_NOT_UPDATE_VI,
                            ExceptionConstants.BILL_NOT_UPDATE_VI,
                            ExceptionConstants.BILL_NOT_UPDATE
                        );
                    }
                }
                Set<Integer> ids = new HashSet<>();
                for (BillProduct billProduct : billOld.getProducts()) {
                    if (billProduct.getProductId() != null) {
                        //                        Integer productId = productProductUnitRepository.findProductIdById(billProduct.getProductId());
                        ids.add(billProduct.getProductId());
                    }
                }
                if (productRepository.countAllByStatusAndIdIn(ids) != ids.size()) {
                    throw new BadRequestAlertException(
                        ExceptionConstants.BILL_CANNOT_DONE_PRODUCT_VI,
                        ExceptionConstants.BILL_NOT_UPDATE_VI,
                        ExceptionConstants.BILL_CANNOT_DONE_PRODUCT
                    );
                }
                bill.setCode(billOld.getCode());
            }
        }
        //        if (bill.getQuantity().stripTrailingZeros().scale() > 0) {
        //            throw new BadRequestAlertException(
        //                ExceptionConstants.QUANTITY_IS_INVALID_VI,
        //                ENTITY_NAME,
        //                ExceptionConstants.QUANTITY_IS_INVALID
        //            );
        //        }
        if (bill.getCustomerId() != null && bill.getCustomerId() != 1) {
            Optional<Customer> customerOptional = customerRepository.findByIdAndComIdAndActive(
                bill.getCustomerId(),
                bill.getComId(),
                CustomerConstants.Active.TRUE
            );
            if (customerOptional.isEmpty()) {
                throw new BadRequestAlertException(
                    ExceptionConstants.CUSTOMER_ID_NOT_FOUND_VI,
                    ENTITY_NAME,
                    ExceptionConstants.CUSTOMER_ID_NOT_FOUND
                );
            }
        }
        if (bill.getBuyerName() != null && !Strings.isNullOrEmpty(bill.getBuyerName())) {
            Optional<String> buyerOption = configRepository.getValueByComIdAndCode(user.getCompanyId(), Constants.IS_BUYER_CODE);
            String buyer = "";
            if (buyerOption.isPresent()) {
                buyer = buyerOption.get();
            }
            if (!buyer.equals(BillConstants.Buyer.SHOW)) {
                throw new BadRequestAlertException(
                    ExceptionConstants.CONFIG_IS_BUYER_INVALID_VI,
                    ENTITY_NAME,
                    ExceptionConstants.CONFIG_IS_BUYER_INVALID_CODE
                );
            }
        }
        if (billDTO.getHaveDiscountVat() != null && billDTO.getHaveDiscountVat()) {
            if (billDTO.getDiscountVatRate() == null) {
                throw new BadRequestAlertException(
                    ExceptionConstants.DISCOUNT_VAT_RATE_NULL_VI,
                    ENTITY_NAME,
                    ExceptionConstants.DISCOUNT_VAT_RATE_NULL
                );
            }
            if (billDTO.getDiscountVatAmount() == null) {
                throw new BadRequestAlertException(
                    ExceptionConstants.DISCOUNT_VAT_AMOUNT_NULL_VI,
                    ENTITY_NAME,
                    ExceptionConstants.DISCOUNT_VAT_AMOUNT_NULL
                );
            }
            boolean check = false;
            for (BillProductRequest productRequest : billDTO.getProducts()) {
                if (Objects.equals(productRequest.getProductCode(), Constants.PRODUCT_CODE_NOTE_DEFAULT)) {
                    check = true;
                    break;
                }
            }
            if (!check) {
                throw new BadRequestAlertException(
                    ExceptionConstants.DISCOUNT_PRODUCT_NULL_VI,
                    ENTITY_NAME,
                    ExceptionConstants.DISCOUNT_PRODUCT_NULL
                );
            }
        } else if (billDTO.getHaveDiscountVat() != null && !billDTO.getHaveDiscountVat()) {
            if (billDTO.getDiscountVatAmount() != null && billDTO.getDiscountVatAmount().compareTo(BigDecimal.ZERO) > 0) {
                throw new BadRequestAlertException(
                    ExceptionConstants.DISCOUNT_VAT_RATE_IS_NULL_VI,
                    ENTITY_NAME,
                    ExceptionConstants.DISCOUNT_VAT_RATE_NULL
                );
            }
        }
        if (
            billDTO.getVoucherAmount() != null &&
            billDTO.getVoucherAmount().compareTo(BigDecimal.ZERO) > 0 &&
            invoiceConfig.getVoucherApply() != 1
        ) {
            throw new BadRequestAlertException(
                ExceptionConstants.CONFIG_VOUCHER_INVALID_VI,
                ENTITY_NAME,
                ExceptionConstants.CONFIG_VOUCHER_INVALID
            );
        }
        if (
            Objects.equals(bill.getDeliveryType(), BillConstants.DeliveryType.TAI_CHO) &&
            (bill.getAreaUnitId() == null || bill.getAreaUnitId() == 0)
        ) {
            throw new BadRequestAlertException(
                ExceptionConstants.AREA_UNIT_NAME_NOT_EMPTY_VI,
                ENTITY_NAME,
                ExceptionConstants.AREA_UNIT_NAME_NOT_EMPTY_CODE
            );
        }
        BillPayment billPayment;
        if (isNew) {
            if (bill.getPayment() == null) {
                throw new BadRequestAlertException(
                    ExceptionConstants.BILL_PAYMENTS_IS_EMPTY_VI,
                    ENTITY_NAME,
                    ExceptionConstants.BILL_PAYMENTS_IS_EMPTY
                );
            }
            billPayment = bill.getPayment();
        } else {
            billPayment = billOld.getPayment();
            bill.setStatus(BillConstants.Status.BILL_DONT_COMPLETE);
        }
        if (billDTO.getPayment().getCardAmount() != null) {
            if (
                bill.getCustomerName().equals(Constants.CUSTOMER_NAME) &&
                billDTO.getPayment().getCardAmount().compareTo(BigDecimal.ZERO) > 0
            ) {
                throw new BadRequestAlertException(
                    ExceptionConstants.PAYMENT_BY_CARD_INVALID_VI,
                    ENTITY_NAME,
                    ExceptionConstants.PAYMENT_BY_CARD_INVALID
                );
            }
            CustomerResponse response = (CustomerResponse) customerManagementService.findById(bill.getCustomerId()).getData();
            if (response.getMoneyBalance() == null && billDTO.getPayment().getCardAmount().compareTo(BigDecimal.ZERO) > 0) {
                throw new BadRequestAlertException(ExceptionConstants.CARD_NOT_EXIST_VI, ENTITY_NAME, ExceptionConstants.CARD_NOT_EXIST);
            }
            if (response.getMoneyBalance() != null && billDTO.getPayment().getCardAmount().compareTo(response.getMoneyBalance()) > 0) {
                throw new BadRequestAlertException(
                    ExceptionConstants.CARD_BALANCE_INVALID_VI,
                    ENTITY_NAME,
                    ExceptionConstants.CARD_BALANCE_INVALID
                );
            }
        }
        if (bill.getQuantity().compareTo(BigDecimal.ZERO) == 0) {
            throw new BadRequestAlertException(
                ExceptionConstants.QUANTITY_IS_VALID_VI,
                ExceptionConstants.QUANTITY_IS_VALID_VI,
                ExceptionConstants.QUANTITY_INVALID
            );
        }
        if (billPayment.getAmount() == null) {
            billPayment.setAmount(BigDecimal.ZERO);
        }
        if (billDTO.getPayment().getCardAmount() == null) {
            billDTO.getPayment().setCardAmount(BigDecimal.ZERO);
        }
        billPayment.setAmount(billPayment.getAmount().add(billDTO.getPayment().getCardAmount()));
        if (Objects.equals(bill.getTypeInv(), BillConstants.TypeInv.BAN_HANG) && (bill.getVatRate() != null && bill.getVatRate() > 0)) {
            throw new BadRequestAlertException(ExceptionConstants.VAT_RATE_INVALID_VI, ENTITY_NAME, ExceptionConstants.VAT_RATE_INVALID);
        }
        Optional<String> configOptional = configRepository.getValueByComIdAndCode(
            user.getCompanyId(),
            ConfigCode.ROUND_SCALE_AMOUNT.getCode()
        );
        int scale = 0;
        if (configOptional.isPresent()) scale = Integer.parseInt(configOptional.get());
        bill.setAmount(Common.roundMoney(bill.getAmount(), scale));
        if (bill.getReservationId() != null && bill.getReservationId() != 0) {
            Integer countReceivable = reservationRepository.countByIdAndComId(bill.getReservationId(), user.getCompanyId());
            if (countReceivable == 0) {
                throw new BadRequestAlertException(
                    ExceptionConstants.RESERVATION_NOT_EXISTS,
                    ExceptionConstants.RESERVATION_NOT_EXISTS,
                    ExceptionConstants.RESERVATION_NOT_EXISTS_CODE
                );
            }
        }
        ZonedDateTime billDate = Common.convertStringToZoneDateTime(billDTO.getBillDate(), Constants.ZONED_DATE_TIME_FORMAT);
        if (billDate != null) {
            bill.setBillDate(billDate);
        }
        bill.setTypeInv(invoiceType);
        //        if (bill.getTypeInv().equals(BillConstants.TypeInv.BAN_HANG) && bill.getVatRate() != null && bill.getVatRate() > 0) {
        //            throw new BadRequestAlertException(ExceptionConstants.BILL_TYPE_SELL_VI, ENTITY_NAME, ExceptionConstants.BILL_TYPE_SELL);
        //        }
        if (billPayment.getDebt() == null) {
            billPayment.setDebt(BigDecimal.ZERO);
        }
        if (billPayment.getDebt().compareTo(BigDecimal.ZERO) > 0) {
            billPayment.setDebtType(BillConstants.DebtType.NO_PHAI_THU);
        } else {
            billPayment.setDebt(BigDecimal.ZERO);
            billPayment.setDebtType(BillConstants.DebtType.MAC_DINH);
        }
        if (!Strings.isNullOrEmpty(pattern)) {
            if (!Common.checkTaxAuthorityCode(bill.getTaxAuthorityCode())) {
                throw new BadRequestAlertException(
                    ExceptionConstants.TAX_AUTHORITY_CODE_INVALID_VI,
                    ENTITY_NAME,
                    ExceptionConstants.TAX_AUTHORITY_CODE_INVALID
                );
            } else {
                Integer countTaxCode;
                if (isNew) {
                    countTaxCode = billRepository.countAllByComIdAndTaxAuthorityCode(user.getCompanyId(), bill.getTaxAuthorityCode());
                } else {
                    countTaxCode =
                        billRepository.countAllByComIdAndTaxAuthorityCodeAndIdNot(
                            user.getCompanyId(),
                            bill.getTaxAuthorityCode(),
                            bill.getId()
                        );
                }
                if (countTaxCode != null && countTaxCode > 0) {
                    throw new BadRequestAlertException(
                        ExceptionConstants.TAX_AUTHORITY_CODE_ALREADY_VI,
                        ENTITY_NAME,
                        ExceptionConstants.TAX_AUTHORITY_CODE_ALREADY
                    );
                }
            }
        } else {
            if (!bill.getTaxAuthorityCode().equals(Constants.TAX_AUTHORITY_CODE_DEFAULT)) {
                throw new BadRequestAlertException(
                    ExceptionConstants.TAX_AUTHORITY_CODE_INVALID_VI,
                    ENTITY_NAME,
                    ExceptionConstants.TAX_AUTHORITY_CODE_INVALID
                );
            }
        }
        if (bill.getStatus().equals(BillConstants.Status.BILL_COMPLETE)) {
            if (Strings.isNullOrEmpty(billDTO.getPayment().getPaymentMethod())) {
                throw new BadRequestAlertException(
                    ExceptionConstants.PAYMENT_METHOD_IS_VALID_VI,
                    ENTITY_NAME,
                    ExceptionConstants.PAYMENT_METHOD_IS_VALID
                );
            }
        }
        if (
            bill.getAreaUnitId() != null &&
            bill.getAreaUnitId() != 0 &&
            Objects.equals(bill.getDeliveryType(), BillConstants.DeliveryType.TAI_CHO)
        ) {
            Optional<AreaUnit> areaUnitOptional = areaUnitRepository.findByIdAndComId(bill.getAreaUnitId(), user.getCompanyId());
            if (areaUnitOptional.isEmpty()) {
                throw new BadRequestAlertException(
                    ExceptionConstants.AREA_UNIT_NOT_EXISTS,
                    ExceptionConstants.AREA_UNIT_NOT_EXISTS,
                    ExceptionConstants.AREA_UNIT_NOT_EXISTS_CODE
                );
            }
            AreaUnit areaUnit = areaUnitOptional.get();
            bill.setAreaUnitName(areaUnit.getName());
            bill.setAreaName(areaUnit.getArea().getName());
        }
        bill.setComId(user.getCompanyId());
        if (bill.getCustomerId() != 1) {
            //        Check thông tin khách hàng
            Integer countUser = customerRepository.countAllByIdAndComId(bill.getCustomerId(), user.getCompanyId());
            if (countUser == 0) {
                throw new BadRequestAlertException(
                    ExceptionConstants.CUSTOMER_NOT_FOUND_VI,
                    Constants.ID + bill.getCustomerId(),
                    ExceptionConstants.CUSTOMER_NOT_FOUND
                );
            }
        } else {
            if (Strings.isNullOrEmpty(bill.getCustomerName())) {
                bill.setCustomerName(Constants.CUSTOMER_NAME);
            }
        }
        checkListVoucher(billDTO, user);
        Set<Integer> ids = new HashSet<>();
        Set<Integer> toppingIds = new HashSet<>();
        List<Integer> unitIds = new ArrayList<>();
        unitIds = bill.getProducts().stream().map(BillProduct::getProductId).collect(Collectors.toList());
        for (BillProductRequest productRequest : billDTO.getProducts()) {
            if (productRequest.getToppings() != null && !productRequest.getToppings().isEmpty()) {
                for (BillProductToppingRequest request : productRequest.getToppings()) {
                    unitIds.add(request.getProductProductUnitId());
                }
            }
        }
        Map<Integer, ProductCheckBill> mapProduct = new HashMap<>();
        Map<Integer, List<BillProduct>> mapUnit = new HashMap<>();
        Map<Integer, Integer> unitIdAndProductIdMap = new HashMap<>();
        // lấy lại productIds tương ứng với unitIds
        List<ProductProductUnit> productProductUnits = productProductUnitRepository.findAllByComIdAndIdIn(user.getCompanyId(), unitIds);
        unitIds = new ArrayList<>();
        productProductUnits.forEach(unit -> unitIdAndProductIdMap.put(unit.getId(), unit.getProductId()));
        boolean checkDiscountVatRate = true;
        boolean checkDiscountVatProduct = false;
        Integer vatRate = bill.getProducts().get(0).getVatRate();
        List<Integer> productIds = new ArrayList<>();
        List<Integer> parentIds = new ArrayList<>();
        // Không tính lại giá tiền nên hiện đang không check tiền
        for (BillProduct billProductOb : bill.getProducts()) {
            Integer productId = unitIdAndProductIdMap.get(billProductOb.getProductId());
            if (billProductOb.getIsTopping() == null) {
                billProductOb.setIsTopping(Boolean.FALSE);
            }
            //            if (!billProductOb.getIsTopping() && billProductOb.getParentProductId() != null) {
            //                throw new BadRequestAlertException(
            //                    ExceptionConstants.PRODUCT_IN_VALID_VI,
            //                    ExceptionConstants.PRODUCT_IN_VALID_VI,
            //                    ExceptionConstants.PRODUCT_IN_VALID
            //                );
            //            }
            productIds.add(unitIdAndProductIdMap.get(billProductOb.getProductId()));
            parentIds.add(unitIdAndProductIdMap.get(billProductOb.getParentId()));
            if (billProductOb.getFeature() == null) {
                billProductOb.setFeature(BillConstants.BillProductFeature.HANG_HOA_DICH_VU);
            }
            if (
                (
                    billProductOb.getProductCode().equals(Constants.PRODUCT_CODE_NOTE_DEFAULT) &&
                    !Objects.equals(billProductOb.getFeature(), BillConstants.BillProductFeature.GHI_CHU)
                ) ||
                (
                    billProductOb.getProductCode().equals(Constants.PRODUCT_CODE_DEFAULT) &&
                    !Objects.equals(billProductOb.getFeature(), BillConstants.BillProductFeature.CHIET_KHAU)
                ) ||
                (
                    billProductOb.getProductCode().equals(Constants.PRODUCT_CODE_PROMOTION_DEFAULT) &&
                    !Objects.equals(billProductOb.getFeature(), BillConstants.BillProductFeature.HANG_HOA_KHUYEN_MAI)
                )
            ) {
                throw new BadRequestAlertException(ExceptionConstants.FEATURE_INVALID_VI, ENTITY_NAME, ExceptionConstants.FEATURE_INVALID);
            }
            if (billProductOb.getProductCode().equals(Constants.PRODUCT_CODE_NOTE_DEFAULT)) {
                billProductOb.setUnitPrice(BigDecimal.ZERO);
                billProductOb.setAmount(BigDecimal.ZERO);
                billProductOb.setDiscountAmount(BigDecimal.ZERO);
                billProductOb.setTotalPreTax(BigDecimal.ZERO);
                billProductOb.setVatRate(0);
                billProductOb.setVatAmount(BigDecimal.ZERO);
                billProductOb.setTotalAmount(BigDecimal.ZERO);
                if (
                    !Objects.equals(invoiceConfig.getInvoiceType(), BillConstants.TypeInv.BAN_HANG) &&
                    billProductOb.getProductName().contains("Đã giảm")
                ) {
                    throw new BadRequestAlertException(
                        ExceptionConstants.SPGC_EXIST_IN_GTGT_VI,
                        ENTITY_NAME,
                        ExceptionConstants.SPGC_EXIST_IN_GTGT
                    );
                }
            }
            if (billProductOb.getProductId() != null) {
                ids.add(productId);
                if (billProductOb.getIsTopping()) {
                    toppingIds.add(productId);
                }
            }
            if (Strings.isNullOrEmpty(billProductOb.getUnit())) {
                billProductOb.setUnit(null);
            }
            if (
                !Objects.equals(vatRate, billProductOb.getVatRate()) &&
                Objects.equals(billProductOb.getFeature(), BillConstants.BillProductFeature.HANG_HOA_DICH_VU)
            ) {
                checkDiscountVatRate = false;
            }
            billProductOb.setBill(bill);
            billProductOb.setProductNormalizedName(Common.normalizedName(Arrays.asList(billProductOb.getProductName())));
            billProductOb.setProductProductUnitId(billProductOb.getProductProductUnitId());
            if (billProductOb.getUnit() != null) {
                unitIds.add(billProductOb.getProductId());
                List<BillProduct> billProducts;
                if (mapUnit.containsKey(billProductOb.getUnitId())) {
                    billProducts = mapUnit.get(billProductOb.getUnitId());
                } else {
                    billProducts = new ArrayList<>();
                }
                billProducts.add(billProductOb);
                mapUnit.put(billProductOb.getUnitId(), billProducts);
            }
        }
        if (
            invoiceConfig != null &&
            invoiceConfig.getDiscountVat() == 1 &&
            invoiceConfig.getInvoiceType() == 0 &&
            invoiceConfig.getTaxReductionType() == 1
        ) {
            checkDiscountVatProduct = Boolean.TRUE;
            //            if (checkDiscountVatRate) {
            //                bill.setDiscountVatRate(vatRate);
            //            } else {
            //                bill.setDiscountVatRate(ProductConstant.VatRate.VAT_RATE_OTHER);
            //            }
        }
        for (BillProductRequest request : billDTO.getProducts()) {
            if (request.getToppings() != null && !request.getToppings().isEmpty()) {
                for (BillProductToppingRequest toppingRequest : request.getToppings()) {
                    if (unitIdAndProductIdMap.containsKey(toppingRequest.getProductProductUnitId())) {
                        Integer productId = unitIdAndProductIdMap.get(toppingRequest.getProductProductUnitId());
                        ProductCheckBill productCheckBill;
                        if (mapProduct.containsKey(toppingRequest.getProductProductUnitId())) {
                            productCheckBill = mapProduct.get(toppingRequest.getProductProductUnitId());
                            productCheckBill.setOnHand(productCheckBill.getOnHand().add(toppingRequest.getQuantity()));
                        } else {
                            productCheckBill =
                                new ProductCheckBill(
                                    productId,
                                    toppingRequest.getProductProductUnitId(),
                                    toppingRequest.getProductCode(),
                                    toppingRequest.getQuantity()
                                );
                        }
                        mapProduct.put(toppingRequest.getProductProductUnitId(), productCheckBill);
                        ids.add(productId);
                    }
                }
            }
        }
        // Check danh sách sản phẩm
        List<ProductCheckBill> productResponses = productRepository.productCheckBill(user.getCompanyId(), ids);
        checkTopping(billDTO.getProducts(), unitIdAndProductIdMap, productIds, parentIds);
        checkListUnits(user.getCompanyId(), mapUnit, unitIds);
        // TỰ động insert mã đơn hàng trường code
        if (isNew) {
            bill.setCode(userService.genCode(user.getCompanyId(), Constants.DON_HANG));
        }
        billPayment.setBill(bill);
        bill.setPayment(billPayment);
        // Xóa bill_product trước khi update
        if (!isNew) {
            //            List<BillProduct> billProducts = billProductRepository.findAllByBillId(billOld.getId());
            for (BillProduct billProduct : billOld.getProducts()) {
                billProduct.setBill(null);
                billProductRepository.delete(billProduct);
            }
            billConfigOld = billOld.getConfig();
        }
        for (BillProduct billProduct : bill.getProducts()) {
            Integer productProductUnitId = billProduct.getProductId();
            // set lại productId chuẩn cho productId trong billProduct
            if (unitIdAndProductIdMap.containsKey(productProductUnitId)) {
                Integer productId = unitIdAndProductIdMap.get(productProductUnitId);
                ProductCheckBill productCheckBill;
                if (mapProduct.containsKey(productProductUnitId)) {
                    productCheckBill = mapProduct.get(productProductUnitId);
                    productCheckBill.setOnHand(productCheckBill.getOnHand().add(billProduct.getQuantity()));
                } else {
                    productCheckBill =
                        new ProductCheckBill(
                            productId,
                            billProduct.getProductProductUnitId(),
                            billProduct.getProductCode(),
                            billProduct.getQuantity()
                        );
                }
                mapProduct.put(productProductUnitId, productCheckBill);
                ids.add(productId);
                // set lại
                billProduct.setProductId(productId);
                billProduct.setProductNormalizedName(Common.normalizedName(List.of(billProduct.getProductName())));
            } else {
                throw new BadRequestAlertException(
                    ExceptionConstants.PRODUCT_IN_VALID_VI + ": " + billProduct.getProductCode(),
                    ENTITY_NAME,
                    ExceptionConstants.PRODUCT_IN_VALID
                );
            }
        }
        // Check danh sách sản phẩm
        checkListProduct(productResponses, mapProduct, Constants.OVER_STOCK_DEFAULT, false);
        bill.setCustomerNormalizedName(Common.normalizedName(Arrays.asList(bill.getCustomerName())));
        List<BillProduct> billProducts = new ArrayList<>();
        Map<Integer, List<BigDecimal>> vatRateAndAmountMap = new HashMap<>();
        BigDecimal totalServiceCharge = BigDecimal.ZERO;
        for (BillProductRequest productRequest : billDTO.getProducts()) {
            BillProduct billProduct = modelMapper.map(productRequest, BillProduct.class);
            billProduct.setIsTopping(Boolean.FALSE);
            billProduct.setParentId(null);
            if (
                checkDiscountVatProduct &&
                Objects.equals(bill.getTypeInv(), BillConstants.TypeInv.BAN_HANG) &&
                productRequest.getDiscountVatRate() != null &&
                productRequest.getTotalDiscount() != null
            ) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.disable(SerializationFeature.INDENT_OUTPUT);
                try {
                    DiscountAmountInvoice request = new DiscountAmountInvoice();
                    request.setDiscountVatRate(String.valueOf(productRequest.getDiscountVatRate()));
                    request.setTotalDiscount(String.valueOf(productRequest.getTotalDiscount()));
                    billProduct.setExtra(objectMapper.writeValueAsString(request));
                } catch (JsonProcessingException e) {
                    throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "DISCOUNT_INVOICE_CONTENT_INVALID");
                }
            }
            if (unitIdAndProductIdMap.containsKey(productRequest.getProductProductUnitId())) {
                Integer productId = unitIdAndProductIdMap.get(productRequest.getProductProductUnitId());
                billProduct.setProductId(productId);
                billProduct.setProductNormalizedName(Common.normalizedName(List.of(billProduct.getProductName())));
                billProduct.setBill(bill);
            } else {
                throw new BadRequestAlertException(
                    ExceptionConstants.PRODUCT_IN_VALID_VI + ": " + billProduct.getProductCode(),
                    ENTITY_NAME,
                    ExceptionConstants.PRODUCT_IN_VALID
                );
            }
            billProducts.add(billProduct);
            if (ConfigConstants.BillConfig.VALUES.contains(productRequest.getVatRate())) {
                BigDecimal vatAmount = productRequest.getVatAmount();
                if (productRequest.getFeature().equals(BillConstants.Feature.CHIET_KHAU)) {
                    vatAmount = vatAmount.multiply(BigDecimal.valueOf(-1));
                }
                List<BigDecimal> addValues = vatRateAndAmountMap.get(productRequest.getVatRate());
                if (addValues == null) {
                    addValues = new ArrayList<>();
                }
                addValues.add(vatAmount);
                vatRateAndAmountMap.put(productRequest.getVatRate(), addValues);
            }
            if (productRequest.getProductCode().equalsIgnoreCase(Constants.PRODUCT_CODE_SERVICE_CHARGE_DEFAULT)) {
                totalServiceCharge = totalServiceCharge.add(productRequest.getTotalPreTax());
            }
            if (productRequest.getToppings() != null && !productRequest.getToppings().isEmpty()) {
                for (BillProductToppingRequest request : productRequest.getToppings()) {
                    BillProduct product = modelMapper.map(request, BillProduct.class);
                    if (
                        checkDiscountVatProduct &&
                        Objects.equals(bill.getTypeInv(), BillConstants.TypeInv.BAN_HANG) &&
                        request.getDiscountVatRate() != null &&
                        request.getTotalDiscount() != null
                    ) {
                        ObjectMapper objectMapper = new ObjectMapper();
                        objectMapper.disable(SerializationFeature.INDENT_OUTPUT);
                        try {
                            DiscountAmountInvoice extra = new DiscountAmountInvoice();
                            extra.setDiscountVatRate(String.valueOf(request.getDiscountVatRate()));
                            extra.setTotalDiscount(String.valueOf(request.getTotalDiscount()));
                            product.setExtra(objectMapper.writeValueAsString(extra));
                        } catch (JsonProcessingException e) {
                            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "DISCOUNT_INVOICE_CONTENT_INVALID");
                        }
                    }
                    if (unitIdAndProductIdMap.containsKey(request.getProductProductUnitId())) {
                        product.setProductId(unitIdAndProductIdMap.get(request.getProductProductUnitId()));
                        product.setProductNormalizedName(Common.normalizedName(List.of(product.getProductName())));
                        product.setParentId(1);
                        product.setBill(bill);
                    } else {
                        throw new BadRequestAlertException(
                            ExceptionConstants.PRODUCT_IN_VALID_VI + ": " + product.getProductCode(),
                            ENTITY_NAME,
                            ExceptionConstants.PRODUCT_IN_VALID
                        );
                    }
                    billProducts.add(product);
                    if (ConfigConstants.BillConfig.VALUES.contains(product.getVatRate())) {
                        List<BigDecimal> addValues = vatRateAndAmountMap.get(product.getVatRate());
                        if (addValues == null) {
                            addValues = new ArrayList<>();
                        }
                        addValues.add(product.getVatAmount());
                        vatRateAndAmountMap.put(product.getVatRate(), addValues);
                    }
                }
            }
        }
        bill.setProducts(billProducts);
        if (invoiceConfig != null && invoiceConfig.getServiceChargeConfig() != null && invoiceConfig.getTotalAmount() != null) {
            BillConfig billConfig = saveBillConfig(
                billConfigOld,
                vatRateAndAmountMap,
                totalServiceCharge,
                billDTO.getComId(),
                bill.getTotalAmount()
            );
            billConfig.setBill(bill);
            bill.setConfig(billConfig);
        }
        return bill;
    }

    private BillConfig saveBillConfig(
        BillConfig billConfigOld,
        Map<Integer, List<BigDecimal>> vatRateAndAmountMap,
        BigDecimal totalServiceCharge,
        Integer comId,
        BigDecimal totalAmount
    ) {
        BillConfig billConfig = new BillConfig();
        if (billConfigOld != null) {
            billConfig = billConfigOld;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        BillExtraConfig extraConfig = new BillExtraConfig();
        for (Map.Entry<Integer, List<BigDecimal>> entry : vatRateAndAmountMap.entrySet()) {
            int key = entry.getKey();
            BigDecimal totalValue = BigDecimal.ZERO;
            for (BigDecimal amount : vatRateAndAmountMap.get(key)) {
                totalValue = totalValue.add(amount);
            }
            if (ConfigConstants.BillConfig.VAT_AMOUNT_8.equals(key)) {
                extraConfig.setAmountVat8(totalValue);
            } else if (ConfigConstants.BillConfig.VAT_AMOUNT_10.equals(key)) {
                extraConfig.setAmountVat10(totalValue);
            }
        }
        extraConfig.setSvc5(totalServiceCharge);
        extraConfig.setTotalAmount(totalAmount);
        try {
            billConfig.setExtra(objectMapper.writeValueAsString(extraConfig));
            billConfig.setComId(comId);
        } catch (JsonProcessingException e) {
            throw new BadRequestAlertException(
                ExceptionConstants.CONFIG_EXT_BILL_INVALID_VI,
                ENTITY_NAME,
                ExceptionConstants.CONFIG_EXT_BILL_INVALID
            );
        }
        return billConfig;
    }

    private void checkTopping(
        List<BillProductRequest> products,
        Map<Integer, Integer> unitIdAndProductIdMap,
        List<Integer> productIds,
        List<Integer> parentIds
    ) {
        User user = userService.getUserWithAuthorities();
        Map<Integer, List<Integer>> parentIdMap = new HashMap<>();
        for (BillProductRequest request : products) {
            if (request.getToppings() != null && !request.getToppings().isEmpty()) {
                Integer productId = unitIdAndProductIdMap.get(request.getProductProductUnitId());
                for (BillProductToppingRequest toppingRequest : request.getToppings()) {
                    if (!toppingRequest.getIsTopping()) {
                        throw new BadRequestAlertException(
                            ExceptionConstants.PRODUCT_TOPPING_IN_VALID_VI,
                            ENTITY_NAME,
                            ExceptionConstants.PRODUCT_TOPPING_IN_VALID
                        );
                    }
                    if (!Objects.equals(request.getProductProductUnitId(), toppingRequest.getParentProductId())) {
                        throw new BadRequestAlertException(
                            ExceptionConstants.PRODUCT_TOPPING_IN_VALID_VI,
                            ENTITY_NAME,
                            ExceptionConstants.PRODUCT_TOPPING_IN_VALID
                        );
                    }
                    List<Integer> ids = new ArrayList<>();
                    if (parentIdMap.containsKey(productId)) {
                        ids = parentIdMap.get(productId);
                    }
                    ids.add(unitIdAndProductIdMap.get(toppingRequest.getProductProductUnitId()));
                    parentIdMap.put(productId, ids);
                }
            }
        }
        List<ToppingItem> toppingItemIds = new ArrayList<>();
        if (!parentIdMap.isEmpty()) {
            toppingItemIds = productToppingRepository.getListToppingGroupForBill(parentIdMap);
        }
        List<ToppingRequiredItem> items = toppingGroupRepository.getAllRequiredGroupByProductId(productIds);
        List<Integer> toppingGroupIds = toppingItemIds.stream().map(ToppingItem::getId).collect(Collectors.toList());
        Map<Integer, List<Integer>> groupRequiredIdMap = new HashMap<>();
        for (ToppingRequiredItem item : items) {
            List<Integer> list = new ArrayList<>();
            if (groupRequiredIdMap.containsKey(item.getProductId())) {
                list = groupRequiredIdMap.get(item.getProductId());
            }
            list.add(item.getToppingGroupId());
            groupRequiredIdMap.put(item.getProductId(), list);
        }
        for (Integer productId : productIds) {
            if (groupRequiredIdMap.containsKey(productId)) {
                List<Integer> groupRequiredIds = groupRequiredIdMap.get(productId);
                if (!new HashSet<>(toppingGroupIds).containsAll(groupRequiredIds)) {
                    throw new BadRequestAlertException(
                        ExceptionConstants.MISSING_REQUIRED_GROUP_VI.replace(
                            "@@",
                            productRepository.findByIdAndComId(productId, user.getCompanyId()).get().getName()
                        ),
                        ENTITY_NAME,
                        ExceptionConstants.MISSING_REQUIRED_GROUP_CODE
                    );
                }
            }
        }
    }

    @Override
    public ResultDTO saveBill(BillCreateRequest billDTO) throws JsonProcessingException {
        Bill bill = new Bill();
        //       Copy data từ dto vào bill
        ResultDTO resultDTO = new ResultDTO();
        User user = userService.getUserWithAuthorities(billDTO.getComId());
        Integer comId = user.getCompanyId();
        Optional<String> invoiceTypeConfig = configRepository.getValueByComIdAndCode(comId, EasyInvoiceConstants.INVOICE_TYPE);
        Integer invoiceType = BillConstants.TypeInv.BAN_HANG;
        if (invoiceTypeConfig.isPresent()) {
            invoiceType = Integer.valueOf(invoiceTypeConfig.get());
        }
        Optional<String> patternOption = configRepository.getValueByComIdAndCode(user.getCompanyId(), EasyInvoiceConstants.INVOICE_PATTERN);
        String pattern = "";
        if (patternOption.isPresent()) {
            pattern = patternOption.get();
        }
        Bill oldBill = null;
        Map<Integer, BigDecimal> oldParentQuantityMap = new HashMap<>();

        if (billDTO.getId() != null) {
            Optional<Bill> billOptional = billRepository.findByIdAndComId(billDTO.getId(), billDTO.getComId());
            if (billOptional.isEmpty()) {
                throw new BadRequestAlertException(ExceptionConstants.BILL_NOT_FOUND_VI, ENTITY_NAME, ExceptionConstants.BILL_NOT_FOUND);
            }
            oldBill = billOptional.get();
            List<String> defaultProduct = List.of(
                Constants.PRODUCT_CODE_DEFAULT,
                Constants.PRODUCT_CODE_NOTE_DEFAULT,
                Constants.PRODUCT_CODE_PROMOTION_DEFAULT
            );
            for (BillProduct billProduct : oldBill.getProducts()) {
                if (defaultProduct.contains(billProduct.getProductCode())) {
                    continue;
                }
                if (billProduct.getParentId() == null) {
                    oldParentQuantityMap.put(billProduct.getId(), billProduct.getQuantity());
                }
            }
        }
        bill = convertBill(user, billDTO, invoiceType, pattern);
        Bill finalBill = bill;
        bill = transactionTemplate.execute(status -> billRepository.save(finalBill));
        saveToppingBill(bill, billDTO, billDTO.getId() == null);
        Optional<String> optionalConfig = configRepository.getValueByComIdAndCode(user.getCompanyId(), Constants.BUSINESS_TYPE);
        if (optionalConfig.isPresent() && Integer.parseInt(optionalConfig.get()) == 0) {
            saveProcessProduct(bill, billDTO, oldParentQuantityMap, user);
        }
        log.debug("Save Bill success : {}", bill);
        saveVoucherUsage(billDTO, user, bill);
        if (bill.getStatus().equals(BillConstants.Status.BILL_COMPLETE)) {
            if (!bill.getCustomerName().equals(Constants.CUSTOMER_NAME)) {
                calculateRankCard(bill, billDTO.getPayment().getCardAmount(), null, null);
            }
        }
        resultDTO.setStatus(true);
        if (billDTO.getId() == null) {
            resultDTO.setReason(ResultConstants.CREATE_BILL_SUCCESS);
        } else {
            resultDTO.setReason(ResultConstants.UPDATE_BILL_SUCCESS);
        }
        resultDTO.setMessage(ResultConstants.SUCCESS);
        resultDTO.setData(bill);
        return resultDTO;
    }

    @Async
    @Override
    @Transactional
    public void billCompletionAsync(Bill bill, SecurityContext context) {
        SecurityContextHolder.setContext(context);
        User user = userService.getUserWithAuthorities();
        Optional<String> patternOption = configRepository.getValueByComIdAndCode(user.getCompanyId(), EasyInvoiceConstants.INVOICE_PATTERN);
        String pattern = "";
        if (patternOption.isPresent()) {
            pattern = patternOption.get();
        }
        Optional<String> methodOptional = configRepository.getValueByComIdAndCode(user.getCompanyId(), EasyInvoiceConstants.INVOICE_METHOD);
        int method = 0;
        if (methodOptional.isPresent()) {
            method = Integer.parseInt(methodOptional.get());
        }
        billCompletion(user.getCompanyId(), bill, pattern, method, false);
    }

    @Override
    public ResultDTO cancelBillByID(BillCancelRequest billCancelRequest) {
        User user = userService.getUserWithAuthorities();
        Optional<Bill> billOptional = billRepository.findByIdAndComId(billCancelRequest.getBillId(), user.getCompanyId());
        if (billOptional.isPresent()) {
            Bill bill = billOptional.get();
            if (bill.getStatus().equals(BillConstants.Status.BILL_COMPLETE) || bill.getStatus().equals(BillConstants.Status.BILL_CANCEL)) {
                throw new InternalServerException(ExceptionConstants.BILL_NOT_CANCEL_VI, ENTITY_NAME, ExceptionConstants.BILL_NOT_CANCEL);
            }
            if (
                bill.getCode().equals(billCancelRequest.getBillCode()) ||
                (bill.getCode2() != null && bill.getCode2().equals(billCancelRequest.getBillCode()))
            ) {
                //                TaskLogSendQueue taskLogSendQueue = transactionTemplate.execute(status -> {
                TaskLogSendQueue taskLog = new TaskLogSendQueue();
                List<ProcessingProduct> processingProducts = processingProductRepository.findAllByBillId(bill.getId());
                ProcessingRequest request = new ProcessingRequest();
                request.setBillId(bill.getId());
                request.setAreaUnitId(bill.getAreaUnitId());
                List<ProcessingRequestDetail> details = new ArrayList<>();
                for (ProcessingProduct processingProduct : processingProducts) {
                    if (processingProduct.getDeliveredQuantity().compareTo(BigDecimal.ZERO) > 0) {
                        throw new BadRequestAlertException(
                            ExceptionConstants.BILL_NOT_CANCEL_VI,
                            ENTITY_NAME,
                            ExceptionConstants.BILL_NOT_CANCEL
                        );
                    }
                    BigDecimal cancelQuantity = processingProduct.getProcessingQuantity().add(processingProduct.getProcessedQuantity());
                    if (cancelQuantity.compareTo(BigDecimal.ZERO) > 0) {
                        details.add(createCancelBill(processingProduct, request));
                        processingProduct.setCanceledQuantity(processingProduct.getCanceledQuantity().add(cancelQuantity));
                        processingProduct.setProcessingQuantity(BigDecimal.ZERO);
                        processingProduct.setProcessedQuantity(BigDecimal.ZERO);
                    }
                }
                request.setDetails(details);
                processingRequestRepository.save(request);
                bill.setStatus(BillConstants.Status.BILL_CANCEL);
                billRepository.save(bill);

                // Đồng bộ sang EB88 để bỏ ghi sổ chứng từ bán hàng tương ứng với đơn hàng hủy
                // Tạo taskLog và enqueue, sau đó sẽ có các service xử lý
                //                    try {
                //                        taskLog = createAndPublishQueueTask(user.getCompanyId(), bill.getId(), TaskLogConstants.Type.EB_CANCEL_SAINVOICE);
                //                    } catch (Exception e) {
                //                        log.error("Can not create queue task for eb88 creating saInvoice: {}", e.getMessage());
                //                    }
                //                    return taskLog;
                //                });
                //                userService.sendTaskLog(taskLogSendQueue);
                return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_UPDATE, true);
            }
        }
        return new ResultDTO(ExceptionConstants.BILL_NOT_EXIST, ExceptionConstants.BILL_NOT_EXIST_VI, true);
    }

    private TaskLogSendQueue createAndPublishQueueTask(int comId, int billId, String type) throws Exception {
        SAInvoiceTask task = new SAInvoiceTask();
        task.setComId("" + comId);
        task.setSaInvoiceId("" + billId);
        TaskLog taskLog = new TaskLog();
        taskLog.setComId(comId);
        taskLog.setStatus(TaskLogConstants.Status.PROCESSING);
        taskLog.setType(type);
        //New objectMapper because we need disable a feature
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.INDENT_OUTPUT);
        taskLog.setContent(objectMapper.writeValueAsString(task));
        taskLog.setRefId(billId);
        taskLog = taskLogRepository.save(taskLog);
        return new TaskLogSendQueue(taskLog.getId().toString(), taskLog.getType());
    }

    private TaskLogSendQueue createAndPublishQueueTaskInvoice(Integer comId, Integer invoiceId, int method) {
        TaskLog taskLog = new TaskLog();
        taskLog.setComId(comId);
        taskLog.setStatus(TaskLogConstants.Status.PROCESSING);
        taskLog.setType(
            method == InvoiceConstants.InvoiceMethod.TU_DONG ? TaskLogConstants.Type.PUBLISH_INVOICE : TaskLogConstants.Type.IMPORT_INVOICE
        );
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.INDENT_OUTPUT);
        try {
            taskLog.setContent(objectMapper.writeValueAsString(new PublishInvoiceQueue(comId.toString(), invoiceId.toString())));
            taskLog.setRefId(invoiceId);
            taskLogRepository.save(taskLog);
            return new TaskLogSendQueue(taskLog.getId().toString(), taskLog.getType());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ResultDTO getBillByCode(String code) {
        User user = userService.getUserWithAuthorities();
        Optional<Bill> bill = billRepository.findByComIdAndCode(user.getCompanyId(), code);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_GET_DETAIL, true, transferBillDetail(bill, user), 1);
    }

    @Override
    public ResultDTO payOffDebt(PayOffDebtRequest payOffDebtRequest) {
        if (
            payOffDebtRequest.getAmount().compareTo(BigDecimal.ZERO) == 0 &&
            payOffDebtRequest.getCardAmount().compareTo(BigDecimal.ZERO) == 0
        ) {
            throw new BadRequestAlertException(
                ExceptionConstants.AMOUNT_IS_VALID_VI,
                ExceptionConstants.AMOUNT_IS_VALID_VI,
                ExceptionConstants.AMOUNT_IS_VALID
            );
        }
        User user = userService.getUserWithAuthorities();
        Optional<Bill> billOptional = billRepository.findByIdAndComId(payOffDebtRequest.getId(), user.getCompanyId());
        if (billOptional.isPresent()) {
            Bill bill = billOptional.get();
            if (!bill.getStatus().equals(BillConstants.Status.BILL_COMPLETE)) {
                throw new InternalServerException(
                    ExceptionConstants.BILL_STATUS_INVALID_VI,
                    ExceptionConstants.BILL_STATUS_INVALID_VI,
                    ExceptionConstants.BILL_STATUS_INVALID
                );
            }
            BillPayment billPayment = bill.getPayment();
            if (billPayment.getDebt().compareTo(BigDecimal.ZERO) == 0) {
                throw new InternalServerException(ExceptionConstants.BILL_NOT_DEBT_VI, ENTITY_NAME, ExceptionConstants.BILL_NOT_DEBT);
            }
            McReceipt mcReceipt = new McReceipt();
            //            if (billPayment.getDebtType() != 0 && !billPayment.getDebt().equals(BigDecimal.ZERO)) {
            mcReceipt.setBillId(payOffDebtRequest.getId());
            mcReceipt.setComId(user.getCompanyId());
            mcReceipt.setTypeDesc(Constants.PHIEU_THU_BAN_HANG);
            //                mcReceipt.setDate(bill.getBillDate());
            mcReceipt.setDate(ZonedDateTime.now());
            mcReceipt.setNo(userService.genCode(user.getCompanyId(), Constants.PHIEU_THU));
            if (bill.getCustomerId() != null) {
                mcReceipt.setCustomerId(bill.getCustomerId());
            }
            if (bill.getCustomerName() != null) {
                mcReceipt.setCustomerName(bill.getCustomerName());
            }
            mcReceipt.setDescription("Trả nợ đơn " + bill.getCode());
            mcReceipt.setBusinessTypeId(
                businessTypeRepository.getIdByComIdAndCode(user.getCompanyId(), BusinessTypeConstants.Code.DEBT_COLLECTION)
            );
            BigDecimal debt = BigDecimal.ZERO;
            if (payOffDebtRequest.getAmount() != null || payOffDebtRequest.getCardAmount() != null) {
                BigDecimal amount = billPayment.getAmount();
                amount = amount.add(payOffDebtRequest.getAmount()).add(payOffDebtRequest.getCardAmount());
                billPayment.setAmount(amount);
                debt = billPayment.getDebt().subtract(payOffDebtRequest.getAmount()).subtract(payOffDebtRequest.getCardAmount());
                billPayment.setDebt(debt);
                if (debt.compareTo(BigDecimal.ZERO) == 0) {
                    billPayment.setDebtType(BillConstants.DebtType.MAC_DINH);
                } else if (debt.compareTo(BigDecimal.ZERO) < 0) {
                    throw new InternalServerException(
                        ExceptionConstants.BILL_AMOUNT_GREATER_DEBT_VI,
                        ENTITY_NAME,
                        ExceptionConstants.BILL_AMOUNT_GREATER_DEBT
                    );
                }
            }
            calculateRankCard(
                bill,
                payOffDebtRequest.getCardAmount(),
                payOffDebtRequest.getAmount().add(payOffDebtRequest.getCardAmount()),
                null
            );
            mcReceipt.setAmount(payOffDebtRequest.getAmount().add(payOffDebtRequest.getCardAmount()));
            mcReceipt.setCustomerNormalizedName(Common.normalizedName(Arrays.asList(mcReceipt.getCustomerName())));
            mcReceiptRepository.save(mcReceipt);
            Payable payable = new Payable();
            payable.setTypeId(1);
            payable.setDate(ZonedDateTime.now());
            payable.setCustomerId(bill.getCustomerId());
            payable.setCustomerName(bill.getCustomerName());
            payable.setComId(user.getCompanyId());
            payable.setDescription("Trả nợ đơn " + bill.getCode());
            payable.setAmount(payOffDebtRequest.getAmount().add(payOffDebtRequest.getCardAmount()));
            payable.setCustomerNormalizedName(Common.normalizedName(Arrays.asList(payable.getCustomerName())));
            billPaymentRepository.save(billPayment);
            payableRepository.save(payable);
        } else {
            throw new BadRequestAlertException(ExceptionConstants.BILL_NOT_FOUND_VI, ENTITY_NAME, ExceptionConstants.BILL_NOT_FOUND);
        }
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_UPDATE, true);
    }

    @Override
    public ResultBillAsync createBillSync(List<BillCreateRequest> billDTOs) throws JsonProcessingException {
        List<ResultBillError> resultBills = new ArrayList<>();
        User user = userService.getUserWithAuthorities();
        List<Bill> billList = new ArrayList<>();
        Map<String, BillCreateRequest> toppingMap = new HashMap<>();
        List<Bill> billsActivate = new ArrayList<>();
        Optional<String> invoiceTypeConfig = configRepository.getValueByComIdAndCode(
            user.getCompanyId(),
            EasyInvoiceConstants.INVOICE_TYPE
        );
        Optional<String> patternOption = configRepository.getValueByComIdAndCode(user.getCompanyId(), EasyInvoiceConstants.INVOICE_PATTERN);
        Integer invoiceType = BillConstants.TypeInv.BAN_HANG;
        if (invoiceTypeConfig.isPresent()) {
            invoiceType = Integer.valueOf(invoiceTypeConfig.get());
        }
        String pattern = "";
        if (patternOption.isPresent()) {
            pattern = patternOption.get();
        }

        for (BillCreateRequest billItem : billDTOs) {
            Bill bill = new Bill();
            try {
                bill = convertBill(user, billItem, invoiceType, pattern);
                billList.add(bill);
                toppingMap.put(bill.getCode(), billItem);
            } catch (BadRequestAlertException ex) {
                resultBills.add(new ResultBillError(ex.getErrorKey(), ex.getTitle(), new ResultBillInfo(billItem.getCode2())));
            }
        }
        // Check danh sách sản phẩm
        billRepository.saveAll(billList);
        for (Bill bill : billList) {
            if (toppingMap.containsKey(bill.getCode())) {
                BillCreateRequest request = toppingMap.get(bill.getCode());
                saveToppingBill(bill, request, Boolean.TRUE);
            }
        }
        for (Bill bill : billList) {
            resultBills.add(
                new ResultBillError(
                    ResultConstants.SUCCESS,
                    ResultConstants.CREATE_SUCCESS_VI,
                    new ResultBillInfo(bill.getId(), bill.getCode()),
                    true
                )
            );
            if (bill.getStatus().equals(BillConstants.Status.BILL_COMPLETE)) {
                billsActivate.add(bill);
            }
        }
        return new ResultBillAsync(resultBills, billList, !billList.isEmpty());
    }

    public List<CheckCompleteBill> checkListProduct(
        List<ProductCheckBill> productResponses,
        Map<Integer, ProductCheckBill> mapProductRequest,
        Integer overStockRequest,
        Boolean showAll
    ) {
        //        Check danh sách sản phẩm
        //        List<ProductCheckBill> productResponses = productRepository.productCheckBill(companyId, ids);
        if (productResponses.isEmpty()) {
            throw new BadRequestAlertException(ExceptionConstants.PRODUCT_IN_VALID_VI, ENTITY_NAME, ExceptionConstants.PRODUCT_IN_VALID);
        }
        Map<Integer, ProductCheckBill> resultMapCheck = new HashMap<>();
        MultiValuedMap<Integer, ProductCheckBill> responseMap = new HashSetValuedHashMap<>();
        productResponses.forEach(result -> {
            responseMap.put(result.getId(), result);
            resultMapCheck.put(result.getProductProductUnitId(), result);
        });

        for (Integer ppuId : mapProductRequest.keySet()) {
            if (!resultMapCheck.containsKey(ppuId)) {
                throw new BadRequestAlertException(
                    ExceptionConstants.PRODUCT_IN_VALID_VI,
                    ENTITY_NAME,
                    ExceptionConstants.PRODUCT_IN_VALID
                );
            } else {
                ProductCheckBill request = mapProductRequest.get(ppuId);
                ProductCheckBill result = resultMapCheck.get(ppuId);
                if (!request.equals(result)) {
                    throw new BadRequestAlertException(
                        ExceptionConstants.PRODUCT_IN_VALID_VI + ": " + request.getCode(),
                        ENTITY_NAME,
                        ExceptionConstants.PRODUCT_IN_VALID
                    );
                }
            }
        }
        List<CheckCompleteBill> completeBillErrors = new ArrayList<>();

        if (overStockRequest.equals(Constants.OVER_STOCK_DEFAULT)) {
            for (Integer productId : responseMap.keySet()) {
                LinkedList<ProductCheckBill> checkBills = new LinkedList<>(responseMap.get(productId));
                ProductCheckBill productPrimary = null;
                BigDecimal totalQuantity = BigDecimal.ZERO; // tổng số lượng của 1 sản phẩm
                // sản phẩm không có đvt || chỉ có đvt chính
                if (checkBills.size() == 1) {
                    productPrimary = checkBills.getFirst();
                    totalQuantity = mapProductRequest.get(productPrimary.getProductProductUnitId()).getOnHand();
                } else {
                    // sản phẩm có đvt
                    for (ProductCheckBill item : checkBills) {
                        ProductCheckBill request = mapProductRequest.get(item.getProductProductUnitId());
                        if (request != null) {
                            BigDecimal quantity = request.getOnHand();
                            // kiểm tra số lượng của từng đvt
                            CheckCompleteBill check = checkOverStock(item, quantity, showAll);
                            if (showAll && check != null) {
                                completeBillErrors.add(check);
                            }
                            if (!item.getFormula()) {
                                quantity = quantity.multiply(item.getConvertRate());
                            } else {
                                quantity = quantity.divide(item.getConvertRate(), new MathContext(6));
                            }
                            totalQuantity = totalQuantity.add(quantity);
                        }
                        if (item.getIsPrimary()) {
                            productPrimary = item;
                        }
                    }
                }
                // kiểm tra tổng số lượng của sản phẩm
                if (productPrimary != null) {
                    CheckCompleteBill check = checkOverStock(productPrimary, totalQuantity, showAll);
                    if (showAll && check != null) {
                        completeBillErrors.add(check);
                    }
                }
            }
        }
        return completeBillErrors;
    }

    private CheckCompleteBill checkOverStock(ProductCheckBill productCheck, BigDecimal quantity, Boolean showAll) {
        if (
            productCheck.getInventoryTracking() != null &&
            productCheck.getInventoryTracking() &&
            productCheck.getOverStock().equals(Constants.OVER_STOCK_DEFAULT) &&
            productCheck.getOnHand().compareTo(quantity) < 0
        ) {
            if (showAll) {
                return new CheckCompleteBill(
                    productCheck.getName(),
                    productCheck.getUnitName(),
                    Common.formatBigDecimal(productCheck.getOnHand())
                );
            }
            throw new BadRequestAlertException(
                "Sản phẩm " +
                productCheck.getName() +
                " vượt quá số lượng tồn kho (tổng tồn kho hiện tại: " +
                Common.formatBigDecimal(productCheck.getOnHand()) +
                productCheck.getUnitName() +
                ")",
                ENTITY_NAME,
                ExceptionConstants.PRODUCT_IN_VALID
            );
        }
        return null;
    }

    public void checkListUnits(Integer companyId, Map<Integer, List<BillProduct>> mapUnit, List<Integer> unitIds) {
        //  Check danh sách sản phẩm
        if (!unitIds.isEmpty()) {
            List<ProductProductUnit> units = productProductUnitRepository.findAllByComIdAndIdIn(companyId, unitIds);
            if (units.isEmpty()) {
                throw new BadRequestAlertException(
                    ExceptionConstants.UNIT_IN_VALID_VI,
                    ExceptionConstants.UNIT_IN_VALID_VI,
                    ExceptionConstants.UNIT_IN_VALID
                );
            }
            for (ProductProductUnit item : units) {
                if (mapUnit.containsKey(item.getProductUnitId())) {
                    List<BillProduct> billProducts = mapUnit.get(item.getProductUnitId());
                    BillProduct billProduct = billProducts
                        .stream()
                        .filter(bp -> Objects.equals(bp.getProductId(), item.getId()))
                        .findFirst()
                        .orElse(new BillProduct());
                    if (item.getUnitName() != null && !item.getUnitName().equalsIgnoreCase(billProduct.getUnit())) {
                        throw new BadRequestAlertException(
                            ExceptionConstants.UNIT_IN_VALID_VI,
                            ExceptionConstants.UNIT_IN_VALID_VI,
                            ExceptionConstants.UNIT_IN_VALID
                        );
                    }
                    if (!Objects.equals(item.getId(), billProduct.getProductId())) {
                        throw new BadRequestAlertException(
                            ExceptionConstants.UNIT_IN_VALID_VI,
                            ExceptionConstants.UNIT_IN_VALID_VI,
                            ExceptionConstants.UNIT_IN_VALID
                        );
                    }
                } else {
                    throw new BadRequestAlertException(
                        ExceptionConstants.UNIT_IN_VALID_VI,
                        ExceptionConstants.UNIT_IN_VALID_VI,
                        ExceptionConstants.UNIT_IN_VALID
                    );
                }
            }
        }
    }

    public void checkListVoucher(BillCreateRequest request, User user) {
        if (request.getVouchers() == null || request.getVouchers().isEmpty()) {
            return;
        }
        if (request.getCustomerId() == null) {
            throw new BadRequestAlertException(
                ExceptionConstants.CUSTOMER_ID_NOT_NULL_VI,
                ENTITY_NAME,
                ExceptionConstants.CUSTOMER_ID_NOT_NULL_CODE
            );
        }
        List<Integer> ids = request.getVouchers().stream().map(BillVoucherRequest::getId).collect(Collectors.toList());
        List<ProductImagesResult> voucherList = voucherRepository.findAllByComIdAndIdInAndCustomerId(
            user.getCompanyId(),
            ids,
            request.getCustomerId()
        );
        if (voucherList.size() != ids.size()) {
            throw new BadRequestAlertException(
                ExceptionConstants.VOUCHER_LIST_ID_INVALID_VI,
                ENTITY_NAME,
                ExceptionConstants.VOUCHER_LIST_ID_INVALID
            );
        }
        Map<Integer, String> voucherMap = voucherList
            .stream()
            .collect(Collectors.toMap(ProductImagesResult::getId, ProductImagesResult::getImage));
        for (BillVoucherRequest voucherRequest : request.getVouchers()) {
            if (!voucherMap.containsKey(voucherRequest.getId())) {
                throw new BadRequestAlertException(
                    ExceptionConstants.VOUCHER_LIST_ID_INVALID_VI,
                    ENTITY_NAME,
                    ExceptionConstants.VOUCHER_LIST_ID_INVALID
                );
            }
            String code = voucherMap.get(voucherRequest.getId());
            if (!code.equals(voucherRequest.getCode())) {
                throw new BadRequestAlertException(
                    ExceptionConstants.VOUCHER_LIST_ID_INVALID_VI,
                    ENTITY_NAME,
                    ExceptionConstants.VOUCHER_LIST_ID_INVALID
                );
            }
        }
    }

    public void saveVoucherUsage(BillCreateRequest request, User user, Bill bill) {
        if (request.getVouchers() == null || request.getVouchers().isEmpty()) {
            return;
        }
        boolean isNew = request.getId() == null;
        List<VoucherUsage> voucherUsages = new ArrayList<>();
        if (isNew) {
            for (BillVoucherRequest voucherRequest : request.getVouchers()) {
                VoucherUsage voucherUsage = createVoucherUsage(voucherRequest, user, bill);
                voucherUsages.add(voucherUsage);
            }
        } else {
            List<VoucherUsage> voucherUsageList = voucherUsageRepository.findAllByBillIdAndCustomerId(
                request.getId(),
                request.getCustomerId()
            );
            Map<Integer, VoucherUsage> oldVoucherMap = new HashMap<>();
            for (VoucherUsage voucherUsage : voucherUsageList) {
                oldVoucherMap.put(voucherUsage.getId(), voucherUsage);
            }
            for (BillVoucherRequest voucherRequest : request.getVouchers()) {
                if (oldVoucherMap.containsKey(voucherRequest.getId())) {
                    VoucherUsage voucherUsage = oldVoucherMap.get(voucherRequest.getId());
                    voucherUsage.setVoucherValue(voucherRequest.getVoucherValue());
                    voucherUsages.add(voucherUsage);
                    oldVoucherMap.remove(voucherRequest.getId());
                } else {
                    voucherUsages.add(createVoucherUsage(voucherRequest, user, bill));
                }
            }
            if (!oldVoucherMap.isEmpty()) {
                voucherUsageRepository.deleteAll(new ArrayList<>(oldVoucherMap.values()));
            }
        }
        voucherUsageRepository.saveAll(voucherUsages);
    }

    private VoucherUsage createVoucherUsage(BillVoucherRequest voucherRequest, User user, Bill bill) {
        VoucherUsage voucherUsage = new VoucherUsage();
        voucherUsage.setComId(user.getCompanyId());
        voucherUsage.setCompanyName(companyRepository.getNameById(user.getCompanyId()));
        voucherUsage.setVoucherId(voucherRequest.getId());
        voucherUsage.setVoucherCode(voucherRequest.getCode());
        voucherUsage.setBillId(bill.getId());
        voucherUsage.setBillCode(bill.getCode());
        voucherUsage.setBillValue(bill.getTotalAmount());
        voucherUsage.setCustomerId(bill.getCustomerId());
        voucherUsage.setCustomerName(bill.getCustomerName());
        voucherUsage.setVoucherValue(voucherRequest.getVoucherValue());
        return voucherUsage;
    }

    @Async
    public void listBillCompletionAsync(List<Bill> listBill, SecurityContext context) {
        SecurityContextHolder.setContext(context);
        User user = userService.getUserWithAuthorities();
        Optional<String> patternOptional = configRepository.getValueByComIdAndCode(
            user.getCompanyId(),
            EasyInvoiceConstants.INVOICE_PATTERN
        );
        String pattern = "";
        if (patternOptional.isPresent()) {
            pattern = patternOptional.get();
        }
        Optional<String> methodOptional = configRepository.getValueByComIdAndCode(user.getCompanyId(), EasyInvoiceConstants.INVOICE_METHOD);
        int method = 0;
        if (methodOptional.isPresent()) {
            method = Integer.parseInt(methodOptional.get());
        }
        for (Bill bill : listBill) {
            //            Chỉ lấy những bill có status  = 1
            if (bill.getStatus().equals(BillConstants.Status.BILL_COMPLETE)) {
                billCompletion(user.getCompanyId(), bill, pattern, method, false);
            }
        }
    }

    @Override
    public ResultDTO updateBill(BillCreateRequest billDTO) {
        Bill bill = new Bill();
        // Copy data từ dto vào bill
        ResultDTO resultDTO = new ResultDTO();
        User user = userService.getUserWithAuthorities(billDTO.getComId());
        Integer comId = user.getCompanyId();
        Optional<String> invoiceTypeConfig = configRepository.getValueByComIdAndCode(comId, EasyInvoiceConstants.INVOICE_TYPE);
        Integer invoiceType = BillConstants.TypeInv.BAN_HANG;
        if (invoiceTypeConfig.isPresent()) {
            invoiceType = Integer.valueOf(invoiceTypeConfig.get());
        }
        Optional<String> patternOption = configRepository.getValueByComIdAndCode(user.getCompanyId(), EasyInvoiceConstants.INVOICE_PATTERN);
        String pattern = "";
        if (patternOption.isPresent()) {
            pattern = patternOption.get();
        }
        bill = convertBill(user, billDTO, invoiceType, pattern);
        billRepository.save(bill);
        resultDTO.setStatus(true);
        resultDTO.setReason(ResultConstants.CREATE_BILL_SUCCESS);
        resultDTO.setMessage(ResultConstants.SUCCESS);
        resultDTO.setData(bill);
        return resultDTO;
    }

    @Override
    public ResultDTO checkUpdateBill(Integer comId, Integer billId) {
        User user = userService.getUserWithAuthorities(comId);
        Optional<Bill> billOptional = billRepository.findByIdAndComId(billId, comId);
        if (billOptional.isEmpty()) {
            throw new BadRequestAlertException(ExceptionConstants.BILL_NOT_FOUND_VI, ENTITY_NAME, ExceptionConstants.BILL_NOT_FOUND);
        }
        Bill bill = billOptional.get();
        if (!Objects.equals(bill.getStatus(), BillConstants.Status.BILL_DONT_COMPLETE)) {
            throw new BadRequestAlertException(ExceptionConstants.BILL_NOT_UPDATE_VI, ENTITY_NAME, ExceptionConstants.BILL_NOT_UPDATE);
        }
        Optional<Customer> customerOptional = customerRepository.findOneByIdAndComId(bill.getCustomerId(), user.getCompanyId());
        if (
            customerOptional.isEmpty() ||
            customerOptional.get().getActive() == null ||
            (customerOptional.get().getActive() != null && !customerOptional.get().getActive())
        ) {
            throw new BadRequestAlertException(
                ExceptionConstants.BILL_CANNOT_DONE_CUSTOMER_VI,
                ExceptionConstants.BILL_NOT_UPDATE_VI,
                ExceptionConstants.BILL_CANNOT_DONE_CUSTOMER
            );
        }
        Set<Integer> ids = new HashSet<>();
        for (BillProduct billProduct : bill.getProducts()) {
            if (billProduct.getProductId() != null) {
                ids.add(billProduct.getProductId());
            }
        }
        List<ProductCheckBill> products = productRepository.productCheckBill(user.getCompanyId(), ids);
        if (!products.stream().map(ProductCheckBill::getId).collect(Collectors.toSet()).containsAll(ids)) {
            throw new BadRequestAlertException(
                ExceptionConstants.BILL_CANNOT_DONE_PRODUCT_VI,
                ExceptionConstants.BILL_NOT_UPDATE_VI,
                ExceptionConstants.BILL_CANNOT_DONE_PRODUCT
            );
        }
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_GET_LIST, true);
    }

    public Invoice createInvoice(Integer comId, Bill bill, String pattern) {
        Invoice invoice = new Invoice();
        if (!Strings.isNullOrEmpty(pattern) && pattern.matches(Constants.PATTERN_REGEX)) {
            invoice.setPattern(pattern);
        }
        if (
            bill.getPayment() != null &&
            !Strings.isNullOrEmpty(bill.getPayment().getPaymentMethod()) &&
            bill.getStatus().equals(BillConstants.Status.BILL_COMPLETE)
        ) {
            invoice.setPaymentMethod(bill.getPayment().getPaymentMethod());
        } else {
            throw new InternalServerException(
                ExceptionConstants.PAYMENT_METHOD_IS_VALID_VI,
                ENTITY_NAME,
                ExceptionConstants.PAYMENT_METHOD_IS_VALID
            );
        }
        invoice.setBillId(bill.getId());
        // Mặc định type và status = 0
        invoice.setType(0);
        invoice.setStatus(null);
        invoice.setCustomerId(bill.getCustomerId());
        invoice.setCustomerName(bill.getCustomerName());
        if (bill.getCustomerId() != null) {
            Optional<Customer> customerOptional = customerRepository.findById(bill.getCustomerId());
            if (customerOptional.isPresent()) {
                Customer customer = customerOptional.get();
                invoice.setCustomerTaxCode(customer.getTaxCode());
                invoice.setIdNumber(customer.getIdNumber());
                invoice.setCustomerPhone(customer.getPhoneNumber());
                invoice.setCustomerAddress(customer.getAddress());
            }
        }
        invoice.setArisingDate(bill.getBillDate());
        invoice.setRefikey(bill.getTaxAuthorityCode());
        invoice.setIkey(bill.getTaxAuthorityCode());
        invoice.setTaxAuthorityCode(bill.getTaxAuthorityCode());
        invoice.setCompanyId(comId);
        invoice.setAmount(bill.getAmount());
        //        ExchangeRate - tỷ giá mặc định bằng 1
        invoice.setExchangeRate(1);
        invoice.setDiscountAmount(bill.getDiscountAmount());
        invoice.setTotalPreTax(bill.getTotalPreTax());
        invoice.setCurrencyUnit(Constants.VND);
        invoice.setBuyerName(bill.getBuyerName());
        //            invoice.setCurrencyUnit(Constants.VND);
        if (bill.getVatRate() != null) {
            if (bill.getVatRate() == ProductConstant.VatRate.VAT_RATE_DEFAULT) {
                boolean isSaleInvoice =
                    Objects.equals(bill.getTypeInv(), BillConstants.TypeInv.BAN_HANG) && bill.getTaxAuthorityCode().startsWith("M2");
                if (isSaleInvoice) {
                    invoice.setVatRate(-1);
                } else {
                    invoice.setVatRate(0);
                }
            } else {
                invoice.setVatRate(bill.getVatRate());
            }
        } else {
            invoice.setVatRate(0);
        }
        if (bill.getVatAmount() != null) {
            invoice.setVatAmount(bill.getVatAmount());
        } else {
            invoice.setVatAmount(BigDecimal.ZERO);
        }
        if (bill.getDiscountVatRate() != null) {
            invoice.setDiscountVatRate(bill.getDiscountVatRate());
        }
        if (
            Objects.equals(bill.getTypeInv(), BillConstants.TypeInv.BAN_HANG) &&
            bill.getDiscountVatAmount() != null &&
            bill.getDiscountVatRate() != null &&
            bill.getDiscountVatAmount().compareTo(BigDecimal.ZERO) > 0
        ) {
            invoice.setDiscountVatAmount(bill.getDiscountVatAmount());
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(SerializationFeature.INDENT_OUTPUT);
            try {
                DiscountAmountInvoice request = new DiscountAmountInvoice();
                request.setCheckDiscount(1);
                request.setDiscountVatRate(String.valueOf(bill.getDiscountVatRate()));
                request.setTotalDiscount(String.valueOf(bill.getDiscountVatAmount()));
                invoice.setExtra(objectMapper.writeValueAsString(request));
            } catch (JsonProcessingException e) {
                throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "DISCOUNT_INVOICE_CONTENT_INVALID");
            }
        }
        invoice.setTotalAmount(bill.getTotalAmount());
        invoice.setBillId(bill.getId());
        return invoice;
    }

    public InvoiceProduct convertBillProductToInvoiceProduct(
        BillProduct billProduct,
        Bill bill,
        Invoice invoice,
        InvoiceConfig configStoreDTO
    ) {
        InvoiceProduct invoiceProduct = new InvoiceProduct();
        invoiceProduct.setInvoice(invoice);
        invoiceProduct.setBillId(bill.getId());
        invoiceProduct.setProductId(billProduct.getProductId());
        invoiceProduct.setCode(billProduct.getProductCode());
        invoiceProduct.setFeature(billProduct.getFeature());
        invoiceProduct.setName(billProduct.getProductName());
        invoiceProduct.setQuantity(billProduct.getQuantity());
        invoiceProduct.setUnit(billProduct.getUnit());
        invoiceProduct.setUnitPrice(billProduct.getUnitPrice());
        invoiceProduct.setDiscountAmount(billProduct.getDiscountAmount());
        invoiceProduct.setTotalPreTax(billProduct.getTotalPreTax());
        if (billProduct.getVatRate() != null) {
            if (billProduct.getVatRate() == ProductConstant.VatRate.VAT_RATE_DEFAULT) {
                boolean isSaleInvoice =
                    Objects.equals(bill.getTypeInv(), BillConstants.TypeInv.BAN_HANG) && bill.getTaxAuthorityCode().startsWith("M2");
                if (isSaleInvoice) {
                    invoiceProduct.setVatRate(-1);
                } else {
                    invoiceProduct.setVatRate(0);
                }
            } else {
                invoiceProduct.setVatRate(billProduct.getVatRate());
            }
        } else {
            invoiceProduct.setVatRate(0);
        }
        if (configStoreDTO.getDiscountVat() == 1 && configStoreDTO.getInvoiceType() == 0 && configStoreDTO.getTaxReductionType() == 1) {
            if (!Strings.isNullOrEmpty(billProduct.getExtra())) {
                invoiceProduct.setExtra(billProduct.getExtra());
            }
        }
        invoiceProduct.setVatAmount(billProduct.getVatAmount());
        invoiceProduct.setTotalAmount(billProduct.getTotalAmount());
        invoiceProduct.setPosition(billProduct.getPosition());
        invoiceProduct.setNormalizedName(Common.normalizedName(Arrays.asList(invoiceProduct.getName())));
        return invoiceProduct;
    }

    public Receivable convertReceivable(Integer comId, Bill bill) {
        BillPayment billPayment = bill.getPayment();
        if (billPayment.getDebt() != null && billPayment.getDebt().compareTo(BigDecimal.valueOf(0)) > 0) {
            Receivable receivable = new Receivable();
            receivable.setComId(comId);
            receivable.setBillId(bill.getId());
            receivable.setType(Constants.THU_TU_BAN_HANG);
            receivable.setNo(userService.genCode(comId, Constants.NO_PHAI_THU));
            if (bill.getCustomerId() != null) {
                receivable.setCustomerId(bill.getCustomerId());
            }
            if (bill.getCustomerName() != null) {
                receivable.setCustomerName(bill.getCustomerName());
            }
            receivable.setDate(ZonedDateTime.now());
            receivable.setDescription(Constants.RECEIVABLE_REASON + bill.getCode());
            receivable.setAmount(bill.getTotalAmount().subtract(billPayment.getDebt()));
            receivable.setCustomerNormalizedName(Common.normalizedName(Arrays.asList(bill.getCustomerName())));
            return receivable;
        }
        return null;
    }

    public McReceipt convertMcReceipt(Integer comId, Bill bill) {
        BillPayment billPayment = bill.getPayment();
        if (billPayment.getAmount() != null && billPayment.getAmount().compareTo(BigDecimal.ZERO) > 0) {
            McReceipt mcReceipt = new McReceipt();
            mcReceipt.setBillId(bill.getId());
            mcReceipt.setComId(comId);
            mcReceipt.setTypeDesc(Constants.PHIEU_THU_BAN_HANG);
            mcReceipt.setDate(ZonedDateTime.now());
            mcReceipt.setDescription(Constants.MC_RECEIPT_REASON + bill.getCode());
            mcReceipt.setNo(userService.genCode(comId, Constants.PHIEU_THU));
            if (bill.getCustomerId() != null) {
                mcReceipt.setCustomerId(bill.getCustomerId());
            }
            if (bill.getCustomerName() != null) {
                mcReceipt.setCustomerName(bill.getCustomerName());
            }
            mcReceipt.setAmount(billPayment.getAmount());
            Integer businessTypeId = businessTypeRepository.getIdByComIdAndCode(comId, BusinessTypeConstants.RsOutWard.MC_RECEIPT);
            if (businessTypeId != null) {
                mcReceipt.setBusinessTypeId(businessTypeId);
            }
            mcReceipt.setCustomerNormalizedName(Common.normalizedName(Arrays.asList(mcReceipt.getCustomerName())));
            return mcReceipt;
        }
        return null;
    }

    public void billCompletion(Integer comId, Bill bill, String pattern, int method, boolean isPublish) {
        Optional<String> configOverStock = configRepository.getValueByComIdAndCode(comId, Constants.OVER_STOCK_CODE);
        Integer overStock = Integer.parseInt(configOverStock.get());
        List<TaskLogSendQueue> taskLogs = transactionTemplate.execute(status -> {
            List<TaskLogSendQueue> taskLogSendQueues = new ArrayList<>();
            if (Objects.isNull(bill)) {
                throw new InternalServerException(ExceptionConstants.BILL_NOT_EXIST_VI, ENTITY_NAME, ExceptionConstants.BILL_NOT_EXIST);
            }
            //  lấy ký hiệu hóa đơn
            //      Tạo mới hóa đơn
            Invoice invoice = createInvoice(comId, bill, pattern);
            List<Integer> productIds = new ArrayList<>();
            Map<String, BigDecimal> productAndQuantityMap = new HashMap<>();
            Map<Integer, BigDecimal> productIdAndQuantityMap = new HashMap<>();
            Map<Integer, String> productIdAndNameMap = new HashMap<>();
            List<InvoiceProduct> invoiceProducts = new ArrayList<>();
            InvoiceConfig configStoreDTO = configManagementService.getConfigStoreByCompanyID(userService.getUserWithAuthorities());
            for (BillProduct billProduct : bill.getProducts()) {
                InvoiceProduct invoiceProduct = convertBillProductToInvoiceProduct(billProduct, bill, invoice, configStoreDTO);
                invoiceProducts.add(invoiceProduct);
                productIds.add(billProduct.getProductId());
                // TH sản phẩm có đơn vị tính
                if (billProduct.getUnitId() != null) {
                    BigDecimal quantity;
                    if (
                        productAndQuantityMap.containsKey(
                            billProduct.getProductId() + billProduct.getUnitId() + billProduct.getUnit().toLowerCase()
                        )
                    ) {
                        quantity =
                            productAndQuantityMap.get(
                                billProduct.getProductId() + billProduct.getUnitId() + billProduct.getUnit().toLowerCase()
                            );
                        quantity = quantity.add(billProduct.getQuantity());
                    } else {
                        quantity = billProduct.getQuantity();
                    }
                    productAndQuantityMap.put(
                        billProduct.getProductId() + billProduct.getUnitId() + billProduct.getUnit().toLowerCase(),
                        quantity
                    );
                } else {
                    // TH sản phẩm không có đơn vị tính
                    BigDecimal quantity;
                    if (productIdAndQuantityMap.containsKey(billProduct.getProductId())) {
                        quantity = productIdAndQuantityMap.get(billProduct.getProductId());
                        quantity = quantity.add(billProduct.getQuantity());
                    } else {
                        quantity = billProduct.getQuantity();
                    }
                    productIdAndQuantityMap.put(billProduct.getProductId(), quantity);
                    //                    productIdAndQuantityMap.put(billProduct.getProductId(), billProduct.getQuantity());
                }
                productIdAndNameMap.put(billProduct.getProductId(), billProduct.getProductName());
            }
            // Cập nhật số lượng tồn kho ở bảng product_product_unit & inventory_count ở product
            List<ProductProductUnit> productProductUnits = productProductUnitRepository.findAllByComIdAndProductIds(comId, productIds);
            MultiValuedMap<Integer, ProductProductUnit> unitIdAndProductUnitMap = new HashSetValuedHashMap<>();
            Map<Integer, BigDecimal> productPrimaryIdAndQuantityMap = new HashMap<>();
            List<ProductProductUnit> unitsSave = new ArrayList<>();
            for (ProductProductUnit unit : productProductUnits) {
                unitIdAndProductUnitMap.put(unit.getProductId(), unit);
            }
            Set<Integer> unitIdAndProductUnitMapKeys = unitIdAndProductUnitMap.keySet();
            for (Integer productId : unitIdAndProductUnitMapKeys) {
                Collection<ProductProductUnit> units = unitIdAndProductUnitMap.get(productId);
                BigDecimal quantity;
                BigDecimal finalQuantity = BigDecimal.ZERO;
                // tính lại onHand của dvt chính
                for (ProductProductUnit unit : units) {
                    String key = null;
                    if (unit.getProductUnitId() != null) {
                        key = unit.getProductId() + unit.getProductUnitId() + unit.getUnitName().toLowerCase();
                    }
                    if (unit.getProductUnitId() != null && productAndQuantityMap.containsKey(key)) {
                        BigDecimal quantityRequest = productAndQuantityMap.get(key);
                        if (unit.getIsPrimary()) {
                            quantity = quantityRequest;
                        } else {
                            if (!unit.getFormula()) {
                                quantity = quantityRequest.multiply(unit.getConvertRate());
                            } else {
                                quantity = quantityRequest.divide(unit.getConvertRate(), new MathContext(6));
                            }
                        }
                        finalQuantity = finalQuantity.add(quantity);
                    }
                }
                for (ProductProductUnit unit : units) {
                    BigDecimal onHand = unit.getOnHand();
                    String productName = productIdAndNameMap.get(unit.getProductId());
                    // sản phẩm không có đơn vị tính
                    if (productIdAndQuantityMap.containsKey(unit.getProductId())) {
                        BigDecimal quantityCheck = productIdAndQuantityMap.get(unit.getProductId());
                        //                        checkOverStock(new ProductCheckBill(onHand, true, overStock, productName, ""), quantityCheck, false);
                        BigDecimal onHandProductNotUnit = onHand.subtract(quantityCheck);
                        unit.setOnHand(onHandProductNotUnit);
                        productPrimaryIdAndQuantityMap.put(unit.getProductId(), onHandProductNotUnit);
                        unitsSave.add(unit);
                        continue;
                    }
                    if (unit.getIsPrimary()) {
                        //                        checkOverStock(new ProductCheckBill(onHand, true, overStock, productName, unit.getUnitName()), finalQuantity, false);
                        unit.setOnHand(onHand.subtract(finalQuantity));
                        productPrimaryIdAndQuantityMap.put(unit.getProductId(), unit.getOnHand());
                    } else {
                        if (unit.getFormula()) {
                            unit.setOnHand(onHand.subtract(finalQuantity.multiply(unit.getConvertRate())));
                        } else {
                            unit.setOnHand(onHand.subtract(finalQuantity.divide(unit.getConvertRate(), new MathContext(6))));
                        }
                        //                        checkOverStock(new ProductCheckBill(onHand, true, overStock, productName, unit.getUnitName()), unit.getOnHand(), false);
                    }
                    unitsSave.add(unit);
                }
            }
            productProductUnitRepository.saveAll(unitsSave);
            List<Product> products = productRepository.findAllById(productPrimaryIdAndQuantityMap.keySet());
            products.forEach(product -> {
                product.setInventoryCount(productPrimaryIdAndQuantityMap.get(product.getId()));
            });
            productRepository.saveAll(products);
            invoice.setInvoiceProducts(invoiceProducts);
            invoice.setCustomerNormalizedName(Common.normalizedName(Arrays.asList(invoice.getCustomerName())));
            invoice = invoiceRepository.save(invoice);
            // Lưu thông tin vào kho
            taskLogSendQueues.add(
                createAndPublishQueueRsInWardTask(comId, bill, productIds, null, BusinessTypeConstants.RsOutWard.OUT_WARD, null)
            );
            //        Tạo mới  MCReceipt
            McReceipt mcReceipt = convertMcReceipt(comId, bill);
            if (mcReceipt != null) {
                BillPayment billPayment = bill.getPayment();
                mcReceipt.setAmount(
                    billPayment.getAmount().subtract(billPayment.getRefund() == null ? BigDecimal.ZERO : billPayment.getRefund())
                );
                mcReceiptRepository.save(mcReceipt);
            }
            Receivable receivable = convertReceivable(comId, bill);
            if (receivable != null) {
                receivableRepository.save(receivable);
            }
            List<ProcessingProduct> processingProducts = processingProductRepository.findAllByBillId(bill.getId());
            for (ProcessingProduct processingProduct : processingProducts) {
                processingProduct.setDeliveredQuantity(
                    processingProduct
                        .getDeliveredQuantity()
                        .add(processingProduct.getProcessingQuantity())
                        .add(processingProduct.getProcessedQuantity())
                );
                processingProduct.setProcessingQuantity(BigDecimal.ZERO);
                processingProduct.setProcessedQuantity(BigDecimal.ZERO);
            }
            try {
                if (
                    !Strings.isNullOrEmpty(bill.getTaxAuthorityCode()) &&
                    !bill.getTaxAuthorityCode().equals(Constants.TAX_AUTHORITY_CODE_DEFAULT)
                ) {
                    if (
                        method == InvoiceConstants.InvoiceMethod.TU_DONG ||
                        method == InvoiceConstants.InvoiceMethod.HOA_DON_MOI_TAO_LAP ||
                        isPublish
                    ) {
                        TaskLogSendQueue taskLogInvoice = createAndPublishQueueTaskInvoice(comId, invoice.getId(), method);
                        if (taskLogInvoice != null) {
                            taskLogSendQueues.add(taskLogInvoice);
                        }
                    }
                    TaskLogSendQueue taskLogEB = createAndPublishQueueTask(comId, bill.getId(), TaskLogConstants.Type.EB_CREATE_SAINVOICE);
                    if (taskLogEB != null) {
                        taskLogSendQueues.add(taskLogEB);
                    }
                }
            } catch (Exception e) {
                log.error("Can not create queue task for eb88 creating saInvoice: {}", e.getMessage());
            }
            return taskLogSendQueues;
        });
        for (TaskLogSendQueue task : taskLogs) {
            if (task != null) {
                userService.sendTaskLog(task);
            }
        }
    }

    private TaskLogSendQueue createAndPublishQueueRsInWardTask(
        Integer comId,
        Bill bill,
        List<Integer> ids,
        List<BillProduct> billProductsInventory,
        String businessType,
        String paymentMethod
    ) {
        RsInoutWard rsInoutWard = convertRsInoutWard(comId, bill, billProductsInventory, ids, paymentMethod);
        if (rsInoutWard != null) {
            rsInoutWardRepository.save(rsInoutWard);
            RsInOutWardTask task = new RsInOutWardTask();
            task.setComId(comId);
            task.setBusinessType(businessType);
            task.setRsInOutWardId(rsInoutWard.getId());
            TaskLog taskLog = new TaskLog();
            taskLog.setComId(comId);
            taskLog.setStatus(TaskLogConstants.Status.PROCESSING);
            //New objectMapper because we need disable a feature
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(SerializationFeature.INDENT_OUTPUT);
            try {
                taskLog.setContent(objectMapper.writeValueAsString(task));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            taskLog.setType(TaskLogConstants.Type.EB_CREATE_RS_IN_OUT_WARD);
            taskLogRepository.save(taskLog);
            return new TaskLogSendQueue(taskLog.getId().toString(), taskLog.getType());
        }
        return null;
    }

    public RsInoutWard convertRsInoutWard(
        Integer comId,
        Bill bill,
        List<BillProduct> billProductsInventory,
        List<Integer> ids,
        String paymentMethod
    ) {
        RsInoutWard rsInoutWard = new RsInoutWard();
        if (billProductsInventory == null || billProductsInventory.isEmpty()) {
            if (ids == null || ids.isEmpty()) {
                return null;
            }
            billProductsInventory = billProductRepository.findAllByIdsAndInventory(comId, bill.getId(), ids);
            rsInoutWard.setTypeDesc(Constants.XUAT_KHO_BAN_HANG);
            rsInoutWard.setPaymentMethod(bill.getPayment().getPaymentMethod());
            rsInoutWard.setNo(userService.genCode(comId, Constants.XUAT_KHO));
            rsInoutWard.setType(Constants.RS_OUTWARD_TYPE);
        } else {
            if (Objects.equals(bill.getStatus(), BillConstants.Status.BILL_CANCEL)) {
                rsInoutWard.setTypeDesc(Constants.NHAP_KHO_HUY_HANG);
            } else if (Objects.equals(bill.getStatus(), BillConstants.Status.BILL_RETURN)) {
                rsInoutWard.setTypeDesc(Constants.NHAP_KHO_TRA_HANG);
            }
            rsInoutWard.setPaymentMethod(paymentMethod);
            rsInoutWard.setNo(userService.genCode(comId, Constants.NHAP_KHO));
            rsInoutWard.setType(Constants.RS_INWARD_TYPE);
        }

        if (billProductsInventory.isEmpty()) {
            return null;
        }
        rsInoutWard.setComId(comId);
        rsInoutWard.setDate(ZonedDateTime.now());
        rsInoutWard.setBillId(bill.getId());
        //        Tự động sinh mã xuất kho
        rsInoutWard.setCustomerId(bill.getCustomerId());
        rsInoutWard.setCustomerName(bill.getCustomerName());
        rsInoutWard.setCostAmount(BigDecimal.ZERO);
        BigDecimal quantity = BigDecimal.ZERO;
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal amount = BigDecimal.ZERO;
        //  Lấy danh sách bill_product có sản phẩm theo dõi tồn kho

        List<RsInoutWardDetail> rsInoutWardDetails = new ArrayList<>();
        int index = 1;
        List<Integer> productIds = billProductsInventory.stream().map(BillProduct::getProductId).collect(Collectors.toList());
        List<ProductProductUnit> productProductUnits = productProductUnitRepository.findAllByComIdAndProductIds(comId, productIds);
        MultiValuedMap<Integer, ProductProductUnit> productMap = new HashSetValuedHashMap<>();
        productProductUnits.forEach(ppu -> productMap.put(ppu.getProductId(), ppu));

        for (BillProduct billProduct : billProductsInventory) {
            RsInoutWardDetail rsInoutWardDetail = new RsInoutWardDetail();
            rsInoutWardDetail.setProductId(billProduct.getProductId());
            rsInoutWardDetail.setProductName(billProduct.getProductName());
            rsInoutWardDetail.setQuantity(billProduct.getQuantity());

            List<ProductProductUnit> productUnits = new ArrayList<>(productMap.get(billProduct.getProductId()));
            Map<Integer, BigDecimal> ppunitIdAndInPriceMap = new HashMap<>();

            BigDecimal inPrice;
            // nếu sản phẩm không có đơn vị tính | chỉ có đơn vị tính chính
            if (productUnits.size() == 1) {
                inPrice = productUnits.get(0).getPurchasePrice() == null ? BigDecimal.ZERO : productUnits.get(0).getPurchasePrice();
            } else {
                // sản phẩm có nhiều đvt
                productUnits.forEach(ppu -> ppunitIdAndInPriceMap.put(ppu.getId(), ppu.getPurchasePrice()));
                inPrice = ppunitIdAndInPriceMap.get(billProduct.getProductProductUnitId());
            }

            rsInoutWardDetail.setUnitPrice(inPrice);
            rsInoutWardDetail.setAmount(billProduct.getQuantity().multiply(inPrice));
            rsInoutWardDetail.setDiscountAmount(BigDecimal.ZERO);
            rsInoutWardDetail.setTotalAmount(rsInoutWardDetail.getAmount().subtract(rsInoutWardDetail.getDiscountAmount()));
            rsInoutWardDetail.setUnitName(billProduct.getUnit());
            rsInoutWardDetail.setUnitId(billProduct.getUnitId());
            rsInoutWardDetail.setProductCode(billProduct.getProductCode());
            rsInoutWardDetail.setPosition(index++);
            rsInoutWardDetail.setProductNormalizedName(Common.normalizedName(Arrays.asList(billProduct.getProductName())));
            rsInoutWardDetail.setRsInoutWard(rsInoutWard);
            rsInoutWardDetails.add(rsInoutWardDetail);
            quantity = quantity.add(billProduct.getQuantity());
            totalAmount = totalAmount.add(rsInoutWardDetail.getTotalAmount());
            amount = amount.add(rsInoutWardDetail.getAmount());
        }
        rsInoutWard.setTotalAmount(totalAmount);
        rsInoutWard.setQuantity(quantity);
        rsInoutWard.setAmount(amount);
        rsInoutWard.setDiscountAmount(BigDecimal.ZERO);
        rsInoutWard.setRsInoutWardDetails(rsInoutWardDetails);
        rsInoutWard.setDescription("Đơn hàng " + bill.getCode());
        Integer businessTypeId = businessTypeRepository.getIdByComIdAndCode(comId, BusinessTypeConstants.RsOutWard.OUT_WARD);
        if (businessTypeId != null) {
            rsInoutWard.setBusinessTypeId(businessTypeId);
        }
        rsInoutWard.setCustomerNormalizedName(
            Common.normalizedName(
                Arrays.asList(rsInoutWard.getNo(), rsInoutWard.getCustomerName(), String.valueOf(rsInoutWard.getTotalAmount()))
            )
        );
        return rsInoutWard;
    }

    private BillDetailResponse transferBillDetail(Optional<Bill> bill, User user) {
        List<Integer> parentProductIds = productToppingRepository.findAllProductId();
        InvoiceConfig invoiceConfig = configManagementService.getConfigStoreByCompanyID(user);
        boolean checkDiscountVatProduct =
            invoiceConfig != null &&
            invoiceConfig.getDiscountVat() == 1 &&
            invoiceConfig.getInvoiceType() == 0 &&
            invoiceConfig.getTaxReductionType() == 1;
        Bill billData = new Bill();
        BillDetailResponse response = new BillDetailResponse();
        if (bill.isPresent()) {
            billData = bill.get();
            Set<Integer> productProductUnitIds = bill
                .get()
                .getProducts()
                .stream()
                .map(BillProduct::getProductProductUnitId)
                .collect(Collectors.toSet());
            Set<Integer> productIds = bill.get().getProducts().stream().map(BillProduct::getProductId).collect(Collectors.toSet());
            List<OnHandItem> onHandItems = productProductUnitRepository.getOnHandById(productProductUnitIds);
            List<Product> products = productRepository.findByComIdAndIdIn(user.getCompanyId(), productIds);
            Map<Integer, BigDecimal> onHandMap = onHandItems.stream().collect(Collectors.toMap(OnHandItem::getId, OnHandItem::getOnHand));
            Map<Integer, Boolean> trackingMap = products.stream().collect(Collectors.toMap(Product::getId, Product::getInventoryTracking));
            List<ProductImagesResult> productImagesResults = productRepository.findImagesByBillId(billData.getId());
            if (!productImagesResults.isEmpty()) {
                Map<Integer, String> mapImages = productImagesResults
                    .stream()
                    .collect(Collectors.toMap(ProductImagesResult::getId, ProductImagesResult::getImage));
                for (BillProduct item : billData.getProducts()) {
                    if (mapImages.containsKey(item.getProductId())) {
                        item.setImageUrl(mapImages.get(item.getProductId()));
                    }
                }
            }
            modelMapper.map(billData, response);
            List<LoyaltyCardUsage> loyaltyCardUsage = loyaltyCardUsageRepository.findByComIdAndRefIdAndType(
                user.getCompanyId(),
                billData.getId(),
                LoyaltyCardConstants.Type.CHI_TIEN
            );
            for (LoyaltyCardUsage usage : loyaltyCardUsage) {
                BillDetailResponse.BillPaymentResponse billPaymentResponse = response.getPayment();
                if (billPaymentResponse.getCardAmount() == null) {
                    billPaymentResponse.setCardAmount(BigDecimal.ZERO);
                }
                billPaymentResponse.setCardAmount(billPaymentResponse.getCardAmount().add(usage.getAmount()));
                response.setPayment(billPaymentResponse);
            }
            if (response.getDiscountVatAmount() != null && response.getDiscountVatAmount().compareTo(BigDecimal.ZERO) > 0) {
                response.setHaveDiscountVat(Boolean.TRUE);
            } else {
                response.setHaveDiscountVat(Boolean.FALSE);
            }
            List<BillDetailResponse.BillProductResponse> billProductResponses = new ArrayList<>();
            BigDecimal displayTotalDiscount = BigDecimal.ZERO;
            List<ProductProcessingAreaResult> processingAreaList = processingAreaRepository.findForProductResult(
                user.getCompanyId(),
                productProductUnitIds
            );
            Map<Integer, ProductProcessingAreaResult> processingAreaMap = new LinkedHashMap<>();
            for (ProductProcessingAreaResult result : processingAreaList) {
                processingAreaMap.put(result.getProductProductUnitId(), result);
            }
            for (BillProduct billProduct : billData.getProducts()) {
                BillDetailResponse.BillProductResponse productResponse = modelMapper.map(
                    billProduct,
                    BillDetailResponse.BillProductResponse.class
                );
                //                if (productResponse.getProductCode().equals(Constants.PRODUCT_CODE_NOTE_DEFAULT)) {
                //                    productResponse.setAmount(billData.getDiscountVatAmount());
                //                    productResponse.setTotalPreTax(billData.getDiscountVatAmount());
                //                    productResponse.setTotalAmount(billData.getDiscountVatAmount());
                //                }
                if (!Strings.isNullOrEmpty(billProduct.getExtra())) {
                    try {
                        DiscountAmountInvoice discountAmountInvoice = objectMapper.readValue(
                            billProduct.getExtra(),
                            DiscountAmountInvoice.class
                        );
                        productResponse.setDiscountVatRate(Integer.parseInt(discountAmountInvoice.getDiscountVatRate()));
                        productResponse.setTotalDiscount(BigDecimal.valueOf(Double.parseDouble(discountAmountInvoice.getTotalDiscount())));
                    } catch (JsonProcessingException e) {
                        log.error("Error when convert discount vat rate for product: " + e.getMessage());
                    }
                }
                if (billProduct.getParentId() != null) {
                    productResponse.setParentProductId(billProduct.getParentId());
                }
                productResponse.setHaveTopping(parentProductIds.contains(productResponse.getProductId()));
                if (onHandMap.containsKey(productResponse.getProductProductUnitId())) {
                    productResponse.setInventoryCount(onHandMap.get(productResponse.getProductProductUnitId()));
                }
                if (trackingMap.containsKey(productResponse.getProductId())) {
                    productResponse.setInventoryTracking(trackingMap.get(productResponse.getProductId()));
                }
                if (processingAreaMap.containsKey(productResponse.getProductProductUnitId())) {
                    productResponse.setProcessingArea(processingAreaMap.get(productResponse.getProductProductUnitId()));
                }
                billProductResponses.add(productResponse);
            }
            billProductResponses.sort(Comparator.comparingInt(BillDetailResponse.BillProductResponse::getPosition));
            response.setProducts(billProductResponses);
            response.setCreatorId(billData.getCreator());
            response.setCreatorName(userRepository.findUserByIdAndComId(billData.getCreator(), user.getCompanyId()).get().getFullName());
            Map<Integer, BillDetailResponse.BillProductResponse> productResponseMap = new LinkedHashMap<>();

            List<ProcessingProduct> processingProducts = processingProductRepository.findAllByBillId(billData.getId());
            Map<Integer, ProcessingProduct> quantityMap = new HashMap<>();
            for (ProcessingProduct item : processingProducts) {
                ProcessingProduct processingProduct = new ProcessingProduct();
                processingProduct.setProcessingQuantity(BigDecimal.ZERO);
                processingProduct.setProcessedQuantity(BigDecimal.ZERO);
                processingProduct.setDeliveredQuantity(BigDecimal.ZERO);
                processingProduct.setCanceledQuantity(BigDecimal.ZERO);
                if (quantityMap.containsKey(item.getBillProductId())) {
                    processingProduct = quantityMap.get(item.getBillProductId());
                }
                processingProduct.setProcessingQuantity(processingProduct.getProcessingQuantity().add(item.getProcessingQuantity()));
                processingProduct.setProcessedQuantity(processingProduct.getProcessedQuantity().add(item.getProcessedQuantity()));
                processingProduct.setDeliveredQuantity(processingProduct.getDeliveredQuantity().add(item.getDeliveredQuantity()));
                processingProduct.setCanceledQuantity(processingProduct.getCanceledQuantity().add(item.getCanceledQuantity()));
                quantityMap.put(item.getBillProductId(), processingProduct);
            }

            for (BillDetailResponse.BillProductResponse productResponse : response.getProducts()) {
                if (productResponse.getParentProductId() == null) {
                    productResponse.setDisplayAmount(productResponse.getAmount());
                    productResponse.setDisplayTotalDiscount(productResponse.getTotalDiscount());
                    productResponseMap.put(productResponse.getId(), productResponse);
                    productResponse.setProcessingQuantity(
                        quantityMap.containsKey(productResponse.getId())
                            ? quantityMap.get(productResponse.getId()).getProcessingQuantity().setScale(6, RoundingMode.UNNECESSARY)
                            : BigDecimal.ZERO.setScale(6, RoundingMode.UNNECESSARY)
                    );
                    productResponse.setProcessedQuantity(
                        quantityMap.containsKey(productResponse.getId())
                            ? quantityMap.get(productResponse.getId()).getProcessedQuantity().setScale(6, RoundingMode.UNNECESSARY)
                            : BigDecimal.ZERO.setScale(6, RoundingMode.UNNECESSARY)
                    );
                    productResponse.setCanceledQuantity(
                        quantityMap.containsKey(productResponse.getId())
                            ? quantityMap.get(productResponse.getId()).getCanceledQuantity().setScale(6, RoundingMode.UNNECESSARY)
                            : BigDecimal.ZERO.setScale(6, RoundingMode.UNNECESSARY)
                    );
                    productResponse.setDeliveredQuantity(
                        quantityMap.containsKey(productResponse.getId())
                            ? quantityMap.get(productResponse.getId()).getDeliveredQuantity().setScale(6, RoundingMode.UNNECESSARY)
                            : BigDecimal.ZERO.setScale(6, RoundingMode.UNNECESSARY)
                    );
                }
                productResponse.setNotifiedQuantity(productResponse.getQuantity());
            }
            for (BillDetailResponse.BillProductResponse productResponse : response.getProducts()) {
                if (productResponse.getParentProductId() != null) {
                    if (productResponseMap.containsKey(productResponse.getParentProductId())) {
                        BillDetailResponse.BillProductResponse billProductResponse = productResponseMap.get(
                            productResponse.getParentProductId()
                        );
                        List<BillDetailResponse.BillProductToppingResponse> billProductToppingResponses = new ArrayList<>();
                        if (billProductResponse.getToppings() != null) {
                            billProductToppingResponses = billProductResponse.getToppings();
                        }
                        BillDetailResponse.BillProductToppingResponse billProductToppingResponse = modelMapper.map(
                            productResponse,
                            BillDetailResponse.BillProductToppingResponse.class
                        );
                        billProductToppingResponse.setParentProductId(billProductResponse.getProductProductUnitId());
                        billProductToppingResponse.setDisplayQuantity(
                            billProductToppingResponse
                                .getQuantity()
                                .divide(
                                    billProductResponse.getQuantity(),
                                    CommonConstants.REGISTER_PASSWORD_LENGTH,
                                    RoundingMode.UNNECESSARY
                                )
                        );
                        billProductResponse.setDisplayAmount(
                            billProductResponse.getDisplayAmount().add(billProductToppingResponse.getAmount())
                        );
                        if (billProductToppingResponse.getTotalDiscount() != null) {
                            if (billProductResponse.getDisplayTotalDiscount() == null) {
                                billProductResponse.setDisplayTotalDiscount(BigDecimal.ZERO);
                            }
                            billProductResponse.setDisplayTotalDiscount(
                                billProductResponse.getDisplayTotalDiscount().add(billProductToppingResponse.getTotalDiscount())
                            );
                        }
                        billProductToppingResponses.add(billProductToppingResponse);
                        billProductResponse.setToppings(billProductToppingResponses);
                    }
                }
            }
            response.setProducts(new ArrayList<>(productResponseMap.values()));
            response.setStatusInvoice(invoiceRepository.getStatusByBillId(response.getComId(), response.getId()));
            List<VoucherUsage> voucherUsages = voucherUsageRepository.findAllByBillIdAndCustomerId(
                response.getId(),
                response.getCustomerId()
            );
            List<Integer> voucherIds = voucherUsages.stream().map(VoucherUsage::getVoucherId).collect(Collectors.toList());
            List<Voucher> voucherList = voucherRepository.findAllByComIdAndIdIn(user.getCompanyId(), voucherIds);
            MultiValuedMap<Integer, String> applyTypeMap = voucherManagementService.getApplyTypeName(voucherIds);
            Map<Integer, Voucher> descMap = new HashMap<>();
            for (Voucher voucher : voucherList) {
                descMap.put(voucher.getId(), voucher);
            }
            if (!voucherUsages.isEmpty()) {
                List<VoucherResponse> billVoucherRequests = new ArrayList<>();
                for (VoucherUsage usage : voucherUsages) {
                    VoucherResponse request = new VoucherResponse();
                    request.setId(usage.getVoucherId());
                    request.setCode(usage.getVoucherCode());
                    if (descMap.containsKey(usage.getVoucherId())) {
                        Voucher voucher = descMap.get(usage.getVoucherId());
                        request.setName(voucher.getName());
                        request.setStartTime(voucher.getStartTime());
                        request.setEndTime(voucher.getEndTime());
                        request.setStatus(voucher.getStatus());
                        request.setVoucherValue(usage.getVoucherValue());
                        try {
                            if (applyTypeMap.containsKey(voucher.getId())) {
                                String applyTypeName = "";
                                List<String> applyTypeNameList = new ArrayList<>(applyTypeMap.get(voucher.getId()));
                                if (!applyTypeNameList.isEmpty()) {
                                    applyTypeName = applyTypeNameList.toString().substring(1, applyTypeNameList.toString().length() - 1);
                                }
                                request.setApplyType(applyTypeName);
                            }
                            List<Object> result = new ArrayList<>();
                            VoucherDiscountCondition[] conditions = objectMapper.readValue(
                                voucher.getDiscountConditions(),
                                VoucherDiscountCondition[].class
                            );
                            for (VoucherDiscountCondition condition : conditions) {
                                DiscountBillDetail[] conditionDetails;
                                if (Objects.equals(condition.getType(), VoucherConstants.Type.VOUCHER_DISCOUNT)) {
                                    conditionDetails = objectMapper.readValue(condition.getData(), DiscountBillDetail[].class);
                                    DiscountBillDetail detail = conditionDetails[0];
                                    if (detail.getDiscountPercent() != null) {
                                        request.setDiscountPercent(detail.getDiscountPercent());
                                    }
                                    if (detail.getDiscountValue() != null) {
                                        request.setDiscountValue(detail.getDiscountValue().setScale(6, RoundingMode.HALF_UP));
                                    }
                                    request.setDesc(detail.getDesc());
                                } else if (
                                    Objects.equals(condition.getType(), VoucherConstants.DiscountType.VOUCHER_DISCOUNT_BILL_TOTAL_AMOUNT)
                                ) {
                                    conditionDetails = objectMapper.readValue(condition.getData(), DiscountBillFromToDetail[].class);
                                } else if (
                                    Objects.equals(condition.getType(), VoucherConstants.DiscountType.VOUCHER_DISCOUNT_BILL_BONUS_PRODUCT)
                                ) {
                                    conditionDetails = objectMapper.readValue(condition.getData(), DiscountBillBonusDetail[].class);
                                } else {
                                    conditionDetails = objectMapper.readValue(condition.getData(), DiscountBillBuyBonusDetail[].class);
                                }
                                result.addAll(List.of(conditionDetails));
                            }
                            request.setConditions(result);
                            if (!Strings.isNullOrEmpty(request.getExtTimeCondition())) {
                                try {
                                    ExtTimeCondition[] timeConditions = objectMapper.readValue(
                                        request.getExtTimeCondition(),
                                        ExtTimeCondition[].class
                                    );
                                    request.setExtTimeConditions(List.of(timeConditions));
                                } catch (JsonProcessingException e) {
                                    throw new BadRequestAlertException(
                                        VOUCHER_CONDITION_INVALID_VI,
                                        ENTITY_NAME,
                                        VOUCHER_CONDITION_INVALID
                                    );
                                }
                            }
                            if (!Strings.isNullOrEmpty(request.getDifferentExtCondition())) {
                                try {
                                    DifferentExtConditions differentExtConditions = objectMapper.readValue(
                                        request.getDifferentExtCondition(),
                                        DifferentExtConditions.class
                                    );
                                    request.setDifferentExtConditions(differentExtConditions);
                                } catch (JsonProcessingException e) {
                                    log.error(e.getMessage());
                                    throw new BadRequestAlertException(
                                        VOUCHER_DIFF_CONDITION_INVALID_VI,
                                        ENTITY_NAME,
                                        VOUCHER_DIFF_CONDITION_INVALID
                                    );
                                }
                            }
                        } catch (JsonProcessingException e) {
                            log.error("Error when convert condition: " + e.getMessage());
                        }
                    }
                    billVoucherRequests.add(request);
                }
                response.setVouchers(billVoucherRequests);
            }
            if (response.getCustomerId() != null && !response.getCustomerName().equals(Constants.CUSTOMER_NAME)) {
                Optional<Customer> customerOptional = customerRepository.findByIdAndComIdAndActive(
                    response.getCustomerId(),
                    user.getCompanyId(),
                    Boolean.TRUE
                );
                if (customerOptional.isPresent()) {
                    CustomerResponse customerResponse = new CustomerResponse();
                    BeanUtils.copyProperties(customerOptional.get(), customerResponse);
                    List<CustomerResponse> responses = customerManagementService.getCustomerCard(
                        user.getCompanyId(),
                        List.of(customerResponse)
                    );
                    responses = customerManagementService.getCardBalance(user.getCompanyId(), responses);
                    customerResponse = responses.get(0);

                    BillDetailResponse.CustomerMoreInformation moreInformation = new BillDetailResponse.CustomerMoreInformation();
                    moreInformation.setPointBalance(customerResponse.getPointBalance());
                    moreInformation.setMoneyBalance(customerResponse.getMoneyBalance());
                    moreInformation.setCardInformation(customerResponse.getCardInformation());
                    response.setCustomerCard(moreInformation);
                }
            }
            // extra config
            if (billData.getConfig() != null) {
                try {
                    String extra = billData.getConfig().getExtra();
                    BillExtraConfig extraConfig = objectMapper.readValue(extra, BillExtraConfig.class);
                    response.setExtraConfig(extraConfig);
                } catch (Exception exception) {
                    throw new BadRequestAlertException(
                        ExceptionConstants.CONFIG_EXT_BILL_INVALID_VI,
                        ENTITY_NAME,
                        ExceptionConstants.CONFIG_EXT_BILL_INVALID
                    );
                }
            }
            return response;
        }
        throw new InternalServerException(ExceptionConstants.BILL_NOT_FOUND, ENTITY_NAME, ExceptionConstants.BILL_NOT_FOUND_VI);
    }

    private void calculateRankCard(Bill bill, BigDecimal cardAmount, BigDecimal amountPayoff, BigDecimal amountDone) {
        User user = userService.getUserWithAuthorities();
        //        boolean createPointPolicy = Boolean.FALSE;
        Optional<CustomerCard> customerCardOptional = customerCardRepository.findByCustomerIdAndComId(
            bill.getCustomerId(),
            user.getCompanyId()
        );
        if (customerCardOptional.isEmpty()) {
            return;
        }
        Integer cardId = customerCardOptional.get().getCardId();
        List<CardPolicy> cardPolicyOptional = cardPolicyRepository.findByComIdAndStatus(
            user.getCompanyId(),
            CardPolicyConstants.Status.RUNNING
        );
        CustomerCard customerCard = customerCardOptional.get();
        if (cardAmount != null && cardAmount.compareTo(BigDecimal.ZERO) > 0) {
            createPayCard(bill, cardAmount, customerCard.getCardId());
        }
        boolean havePolicy = false;
        boolean updateRank = false;
        Map<Integer, CardPolicyConditions> conditionsMap = new HashMap<>();
        CardPolicy cardPolicyApply = null;
        for (CardPolicy cardPolicy : cardPolicyOptional) {
            ObjectMapper objectMapper = new ObjectMapper();
            if (cardPolicy.getConditions() != null) {
                try {
                    CardPolicyConditions[] conditions = objectMapper.readValue(cardPolicy.getConditions(), CardPolicyConditions[].class);
                    for (CardPolicyConditions condition : conditions) {
                        conditionsMap.put(condition.getCardId(), condition);
                        if (Objects.equals(condition.getCardId(), cardId)) {
                            havePolicy = true;
                            cardPolicyApply = cardPolicy;
                        }
                    }
                } catch (JsonProcessingException e) {
                    log.error("Error when convert card policy condition " + e.getMessage());
                }
            }
        }
        if (havePolicy) {
            CardPolicyConditions policyCondition = null;
            boolean checkNextRank = Boolean.TRUE;
            if (conditionsMap.containsKey(customerCard.getCardId()) && cardPolicyApply.getStartTime().isBefore(ZonedDateTime.now())) {
                createPointPolicy(bill, customerCard.getCardId(), conditionsMap.get(customerCard.getCardId()), amountPayoff);
            }
            do {
                Integer nextRank = loyaltyCardRepository.getNextRank(bill.getComId(), customerCard.getCardId());
                if (conditionsMap.containsKey(nextRank)) {
                    policyCondition = conditionsMap.get(nextRank);
                    //                        createPointPolicy = Boolean.TRUE;
                } else {
                    break;
                }
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.ZONED_DATE_FORMAT);
                String fromDate = formatter.format(cardPolicyApply.getStartTime());
                if (policyCondition.getUpgradeTime() != null) {
                    ZonedDateTime checkTime = ZonedDateTime.now().plusDays((int) Math.floor(policyCondition.getUpgradeTime()) * -30L);
                    if (cardPolicyApply.getStartTime().isBefore(checkTime)) {
                        fromDate = formatter.format(checkTime);
                    }
                }
                String toDate = formatter.format(ZonedDateTime.now());
                BigDecimal totalMoney = billRepository.countTotalMoneyForUpdateCard(bill.getCustomerId(), fromDate, toDate);
                if (amountDone != null) {
                    totalMoney = totalMoney.add(amountDone);
                }
                if (policyCondition.getUpgradeValue() == null) {
                    break;
                }
                if (
                    totalMoney.compareTo(policyCondition.getUpgradeValue()) >= 0 &&
                    Objects.equals(cardPolicyApply.getUpgradeType(), CardPolicyConstants.UpgradeType.TOTAL_SPENDING)
                ) {
                    if (nextRank != null) {
                        customerCard.setCardId(nextRank);
                        updateRank = Boolean.TRUE;
                    } else {
                        checkNextRank = Boolean.FALSE;
                    }
                } else {
                    checkNextRank = Boolean.FALSE;
                }
            } while (checkNextRank);
            //                if (createPointPolicy) {
            //                    createPointPolicy(bill, customerCard.getCardId(), policyCondition.getAccumValue());
            //                }
            if (updateRank) {
                // áp dụng voucher cho hạng mới

                //                List<Integer> voucherIds = voucherApplyRepository.getVoucherIdByComIdAndApplyId(
                //                    user.getCompanyId(),
                //                    customerCard.getCardId()
                //                );
                //                if (voucherIds != null && !voucherIds.isEmpty()) {
                //                    List<VoucherApply> voucherApplies = new ArrayList<>();
                //                    for (Integer voucherId : voucherIds) {
                //                        VoucherApply voucherApply = new VoucherApply();
                //                        voucherApply.setApplyType(1);
                //                        voucherApply.setComId(user.getCompanyId());
                //                        voucherApply.setVoucherId(voucherId);
                //                        voucherApply.setApplyId(customerCard.getCardId());
                //                        voucherApply.setCustomerId(customerCard.getCustomerId());
                //                        voucherApplies.add(voucherApply);
                //                    }
                //                    voucherApplyRepository.saveAll(voucherApplies);
                //                }
            }
        }
    }

    private void createPointPolicy(Bill bill, Integer cardId, CardPolicyConditions policyCondition, BigDecimal amountPayoff) {
        if (policyCondition.getAccumValue() == null) {
            return;
        }
        BigDecimal moneyToCalculate = bill.getTotalAmount();
        if (amountPayoff != null) {
            moneyToCalculate = amountPayoff;
        } else if (
            Objects.equals(bill.getPayment().getDebtType(), BillConstants.DebtType.NO_PHAI_THU) ||
            (bill.getPayment().getDebt() != null && bill.getPayment().getDebt().compareTo(BigDecimal.ZERO) > 0)
        ) {
            moneyToCalculate =
                bill
                    .getPayment()
                    .getAmount()
                    .subtract(bill.getPayment().getRefund() != null ? bill.getPayment().getRefund() : BigDecimal.ZERO);
        }
        LoyaltyCardUsage loyaltyCardUsage = new LoyaltyCardUsage();
        loyaltyCardUsage.setComId(bill.getComId());
        loyaltyCardUsage.setCustomerId(bill.getCustomerId());
        loyaltyCardUsage.setCardId(cardId);
        loyaltyCardUsage.setType(LoyaltyCardConstants.Type.CONG_DIEM);
        loyaltyCardUsage.setUsageDate(bill.getBillDate());
        loyaltyCardUsage.setRefId(bill.getId());
        loyaltyCardUsage.setAmount(moneyToCalculate);
        if (policyCondition.getAccumValue().compareTo(BigDecimal.ZERO) == 0) {
            loyaltyCardUsage.setPoint(0);
        } else {
            loyaltyCardUsage.setPoint(
                moneyToCalculate.divide(policyCondition.getAccumValue(), RoundingMode.DOWN).setScale(0, RoundingMode.DOWN).intValue()
            );
        }
        loyaltyCardUsageRepository.save(loyaltyCardUsage);
    }

    private void createPayCard(Bill bill, BigDecimal cardAmount, Integer cardId) {
        LoyaltyCardUsage loyaltyCardUsage = new LoyaltyCardUsage();
        loyaltyCardUsage.setComId(bill.getComId());
        loyaltyCardUsage.setCustomerId(bill.getCustomerId());
        loyaltyCardUsage.setCardId(cardId);
        loyaltyCardUsage.setType(LoyaltyCardConstants.Type.CHI_TIEN);
        loyaltyCardUsage.setUsageDate(bill.getBillDate());
        loyaltyCardUsage.setRefId(bill.getId());
        loyaltyCardUsage.setAmount(cardAmount);
        loyaltyCardUsageRepository.save(loyaltyCardUsage);
    }

    private void saveProcessProduct(Bill bill, BillCreateRequest billDTO, Map<Integer, BigDecimal> oldProductMap, User user)
        throws JsonProcessingException {
        if (!Objects.equals(bill.getStatus(), BillConstants.Status.BILL_DONT_COMPLETE)) {
            return;
        }
        List<String> defaultProduct = List.of(
            Constants.PRODUCT_CODE_DEFAULT,
            Constants.PRODUCT_CODE_NOTE_DEFAULT,
            Constants.PRODUCT_CODE_PROMOTION_DEFAULT,
            Constants.PRODUCT_CODE_SERVICE_CHARGE_DEFAULT
        );
        boolean isNew = billDTO.getId() == null;
        int positionSub = 0;
        //        List<Integer> productProductUnitIds = bill
        //            .getProducts()
        //            .stream()
        //            .map(BillProduct::getProductProductUnitId)
        //            .collect(Collectors.toList());
        //        List<ProductSetting> settings = processingAreaRepository.getProductSetting(bill.getComId(), productProductUnitIds);
        //        Map<Integer, Integer> settingMap = settings.stream().collect(Collectors.toMap(ProductSetting::getId, ProductSetting::getSetting));
        if (isNew) {
            ProcessingRequest processingRequest = new ProcessingRequest();
            processingRequest.setBillId(bill.getId());
            processingRequest.setAreaUnitId(bill.getAreaUnitId());
            List<ProcessingRequestDetail> details = new ArrayList<>();
            for (BillProductRequest billProduct : billDTO.getProducts()) {
                if (defaultProduct.contains(billProduct.getProductCode())) {
                    continue;
                }
                details.add(createProcessDetail(billProduct, bill, processingRequest, Boolean.FALSE));
                if (billProduct.getToppings() != null && !billProduct.getToppings().isEmpty()) {
                    for (BillProductToppingRequest toppingRequest : billProduct.getToppings()) {
                        details.add(
                            createProcessDetail(
                                modelMapper.map(toppingRequest, BillProductRequest.class),
                                bill,
                                processingRequest,
                                Boolean.TRUE
                            )
                        );
                    }
                }
            }
            processingRequest.setDetails(details);
            processingRequestRepository.save(processingRequest);
            createNotification(billDTO, bill);
        } else {
            ProcessingRequest request = new ProcessingRequest();
            request.setBillId(bill.getId());
            request.setAreaUnitId(bill.getAreaUnitId());
            request.setDetails(new ArrayList<>());
            List<ProcessingRequestDetail> details = request.getDetails();
            List<BillProductRequest> addList = new ArrayList<>();
            List<String> list = new ArrayList<>();
            Map<Integer, BillProductRequest> newProductMap = new HashMap<>();
            for (BillProductRequest productRequest : billDTO.getProducts()) {
                if (defaultProduct.contains(productRequest.getProductCode())) {
                    continue;
                }
                if (productRequest.getId() == null) {
                    addList.add(productRequest);
                } else {
                    newProductMap.put(productRequest.getId(), productRequest);
                }
            }
            if (!addList.isEmpty()) {
                for (BillProductRequest billProduct : addList) {
                    if (defaultProduct.contains(billProduct.getProductCode())) {
                        continue;
                    }
                    list.add(billProduct.getProductName());
                    details.add(createProcessDetail(billProduct, bill, request, Boolean.FALSE));
                    if (billProduct.getToppings() != null && !billProduct.getToppings().isEmpty()) {
                        for (BillProductToppingRequest toppingRequest : billProduct.getToppings()) {
                            details.add(
                                createProcessDetail(modelMapper.map(toppingRequest, BillProductRequest.class), bill, request, Boolean.TRUE)
                            );
                        }
                    }
                }
            }
            List<ProcessingRequestDetail> requestDetails = processingRequestDetailRepository.findByBillIdAndIsTopping(
                bill.getId(),
                Boolean.TRUE
            );
            Map<Integer, BigDecimal> toppingQuantityMap = getToppingQuantityMap(requestDetails);
            List<Integer> deleteList = new ArrayList<>();
            for (Map.Entry<Integer, BigDecimal> entry : oldProductMap.entrySet()) {
                if (!newProductMap.containsKey(entry.getKey())) {
                    deleteList.add(entry.getKey());
                } else {
                    if (entry.getValue().compareTo(newProductMap.get(entry.getKey()).getQuantity()) < 0) {
                        BillProductRequest productRequest = newProductMap.get(entry.getKey());
                        list.add(productRequest.getProductName());
                        productRequest.setQuantity(productRequest.getQuantity().subtract(entry.getValue()));
                        details.addAll(updateProcessingProduct(productRequest, bill, request, toppingQuantityMap, user));
                    } else if (entry.getValue().compareTo(newProductMap.get(entry.getKey()).getQuantity()) > 0) {
                        BillProductRequest productRequest = newProductMap.get(entry.getKey());
                        productRequest.setQuantity(productRequest.getQuantity().subtract(entry.getValue()));
                        details.addAll(createCancelProcessingProduct(productRequest, bill, request));
                    }
                }
            }
            if (!deleteList.isEmpty()) {
                int oldSize = details.size();
                details.addAll(deleteProcessingProduct(deleteList, request, bill, user));
                positionSub = details.size() - oldSize;
            }
            if (!details.isEmpty()) {
                request.setDetails(details);
                processingRequestRepository.save(request);
                createUpdateProcessingNotification(list, bill.getAreaUnitName(), bill.getComId(), user, bill.getId());
            }
        }
        saveBillProductId(bill, positionSub);
    }

    private static Map<Integer, BigDecimal> getToppingQuantityMap(List<ProcessingRequestDetail> requestDetails) {
        Map<Integer, Integer> positiveToppingMap = new HashMap<>();
        for (ProcessingRequestDetail detail : requestDetails) {
            if (detail.getIsTopping() && detail.getQuantity().compareTo(BigDecimal.ZERO) > 0) {
                positiveToppingMap.put(detail.getId(), detail.getProcessingProducts().get(0).getBillProductId());
            }
        }
        Map<Integer, BigDecimal> toppingQuantityMap = new HashMap<>();
        for (ProcessingRequestDetail detail : requestDetails) {
            if (detail.getIsTopping()) {
                BigDecimal quantity;
                if (detail.getQuantity().compareTo(BigDecimal.ZERO) < 0) {
                    quantity = toppingQuantityMap.getOrDefault(positiveToppingMap.get(detail.getRefId()), BigDecimal.ZERO);
                    quantity = quantity.add(detail.getQuantity());
                    toppingQuantityMap.put(positiveToppingMap.get(detail.getRefId()), quantity);
                } else {
                    quantity = toppingQuantityMap.getOrDefault(detail.getProcessingProducts().get(0).getBillProductId(), BigDecimal.ZERO);
                    quantity = quantity.add(detail.getQuantity());
                    toppingQuantityMap.put(detail.getProcessingProducts().get(0).getBillProductId(), quantity);
                }
            }
        }
        return toppingQuantityMap;
    }

    private ProcessingRequestDetail createProcessDetail(
        BillProductRequest billProduct,
        Bill bill,
        ProcessingRequest processingRequest,
        Boolean isTopping
    ) {
        ProcessingRequestDetail detail = new ProcessingRequestDetail();
        BeanUtils.copyProperties(billProduct, detail);
        detail.setId(null);
        detail.setBillId(bill.getId());
        detail.setProcessingRequest(processingRequest);
        detail.setIsTopping(isTopping);
        detail.setAreaUnitId(bill.getAreaUnitId());
        detail.setProcessingProducts(List.of(createProcessingProduct(billProduct, detail, isTopping)));
        return detail;
    }

    private ProcessingProduct createProcessingProduct(BillProductRequest request, ProcessingRequestDetail detail, Boolean isTopping) {
        ProcessingProduct processingProduct = new ProcessingProduct();
        processingProduct.setBillId(detail.getBillId());
        processingProduct.setProductProductUnitId(request.getProductProductUnitId());
        processingProduct.setProcessingRequestDetail(detail);
        processingProduct.setProcessingQuantity(request.getQuantity());
        processingProduct.setNotifiedQuantity(request.getQuantity());
        processingProduct.setCanceledQuantity(BigDecimal.ZERO);
        processingProduct.setIsTopping(isTopping);
        processingProduct.setProcessedQuantity(BigDecimal.ZERO);
        processingProduct.setDeliveredQuantity(BigDecimal.ZERO);
        return processingProduct;
    }

    private List<ProcessingRequestDetail> updateProcessingProduct(
        BillProductRequest productRequest,
        Bill bill,
        ProcessingRequest request,
        Map<Integer, BigDecimal> toppingQuantityMap,
        User user
    ) {
        List<ProcessingRequestDetail> details = new ArrayList<>();
        ProcessingRequestDetail detail = new ProcessingRequestDetail();
        BeanUtils.copyProperties(productRequest, detail);
        detail.setId(null);
        detail.setBillId(bill.getId());
        detail.setProcessingRequest(request);
        detail.setAreaUnitId(bill.getAreaUnitId());
        detail.setIsTopping(Boolean.FALSE);
        detail.setProcessingProducts(List.of(createProcessingProduct(productRequest, detail, Boolean.FALSE)));
        details.add(detail);
        if (productRequest.getToppings() != null && !productRequest.getToppings().isEmpty()) {
            for (BillProductToppingRequest toppingRequest : productRequest.getToppings()) {
                toppingRequest.setQuantity(toppingRequest.getQuantity().subtract(toppingQuantityMap.get(toppingRequest.getId())));
                details.add(createProcessDetail(modelMapper.map(toppingRequest, BillProductRequest.class), bill, request, Boolean.TRUE));
            }
        }
        notificationRepository.save(
            getNotification(bill.getAreaUnitName(), bill.getComId(), user, productRequest.getProductName(), bill.getId())
        );
        return details;
    }

    private List<ProcessingRequestDetail> createCancelProcessingProduct(
        BillProductRequest request,
        Bill bill,
        ProcessingRequest processingRequest
    ) {
        User user = userService.getUserWithAuthorities();
        List<ProcessingProduct> processingProductList = processingProductRepository.findByBillProductIdAndRefIdIsNull(request.getId());
        int index = 0;
        BigDecimal cancelQuantity = BigDecimal.ZERO.subtract(request.getQuantity());
        if (cancelQuantity.compareTo(BigDecimal.ZERO) > 0) {
            notificationRepository.save(createCancelNotification(bill, user, request.getProductName(), cancelQuantity));
        }
        Map<Integer, ProcessingRequestDetail> deleteMap = new HashMap<>();
        while (cancelQuantity.compareTo(BigDecimal.ZERO) > 0 && index < processingProductList.size()) {
            ProcessingProduct product = processingProductList.get(index);
            if (product.getProcessingQuantity().compareTo(BigDecimal.ZERO) == 0) {
                index++;
                continue;
            }
            ProcessingRequestDetail copyDetail = genCopyRequestDetail(product, processingRequest);
            List<ProcessingProduct> products = processingProductRepository.findByRefIdAndIsTopping(product.getId(), Boolean.TRUE);
            for (ProcessingProduct processingProduct : products) {
                ProcessingRequestDetail copyToppingDetail = genCopyRequestDetail(processingProduct, processingRequest);
                BigDecimal quantity = processingProduct
                    .getProcessingQuantity()
                    .multiply(cancelQuantity)
                    .divide(product.getProcessingQuantity(), 6, RoundingMode.HALF_UP);
                if (processingProduct.getProcessingQuantity().compareTo(quantity) >= 0) {
                    copyToppingDetail.setQuantity(BigDecimal.ZERO.subtract(quantity));
                    processingProduct.setProcessingQuantity(processingProduct.getProcessingQuantity().subtract(quantity));
                    processingProduct.setCanceledQuantity(processingProduct.getCanceledQuantity().add(quantity));
                } else {
                    copyToppingDetail.setQuantity(BigDecimal.ZERO.subtract(processingProduct.getProcessingQuantity()));
                    processingProduct.setCanceledQuantity(
                        processingProduct.getCanceledQuantity().add(processingProduct.getProcessingQuantity())
                    );
                    processingProduct.setProcessingQuantity(BigDecimal.ZERO);
                }
                deleteMap.put(processingProduct.getId(), copyToppingDetail);
            }
            if (product.getProcessingQuantity().compareTo(cancelQuantity) >= 0) {
                copyDetail.setQuantity(BigDecimal.ZERO.subtract(cancelQuantity));
                product.setProcessingQuantity(product.getProcessingQuantity().subtract(cancelQuantity));
                product.setCanceledQuantity(product.getCanceledQuantity().add(cancelQuantity));
                cancelQuantity = BigDecimal.ZERO;
            } else {
                copyDetail.setQuantity(BigDecimal.ZERO.subtract(product.getProcessingQuantity()));
                product.setCanceledQuantity(product.getCanceledQuantity().add(product.getProcessingQuantity()));
                index++;
                cancelQuantity = cancelQuantity.subtract(product.getProcessingQuantity());
                product.setProcessingQuantity(BigDecimal.ZERO);
            }
            deleteMap.put(product.getId(), copyDetail);
        }
        index = 0;
        while (cancelQuantity.compareTo(BigDecimal.ZERO) > 0 && index < processingProductList.size()) {
            ProcessingProduct product = processingProductList.get(index);
            if (product.getProcessedQuantity().compareTo(BigDecimal.ZERO) == 0) {
                index++;
                continue;
            }
            ProcessingRequestDetail copyDetail = deleteMap.get(product.getId());
            if (copyDetail == null) {
                copyDetail = genCopyRequestDetail(product, processingRequest);
            }
            List<ProcessingProduct> products = processingProductRepository.findByRefIdAndIsTopping(product.getId(), Boolean.TRUE);
            for (ProcessingProduct processingProduct : products) {
                BigDecimal quantity = processingProduct
                    .getProcessedQuantity()
                    .multiply(cancelQuantity)
                    .divide(product.getProcessedQuantity(), 6, RoundingMode.HALF_UP);
                ProcessingRequestDetail copyToppingDetail = deleteMap.get(processingProduct.getId());
                if (copyToppingDetail == null) {
                    copyToppingDetail = genCopyRequestDetail(processingProduct, processingRequest);
                }
                if (processingProduct.getProcessedQuantity().compareTo(quantity) >= 0) {
                    copyToppingDetail.setQuantity(copyToppingDetail.getQuantity().add(BigDecimal.ZERO.subtract(quantity)));
                    processingProduct.setProcessedQuantity(processingProduct.getProcessedQuantity().subtract(quantity));
                    processingProduct.setCanceledQuantity(processingProduct.getCanceledQuantity().add(quantity));
                } else {
                    copyToppingDetail.setQuantity(
                        copyToppingDetail.getQuantity().add(BigDecimal.ZERO.subtract(processingProduct.getProcessedQuantity()))
                    );
                    processingProduct.setCanceledQuantity(
                        processingProduct.getCanceledQuantity().add(processingProduct.getProcessedQuantity())
                    );
                    processingProduct.setProcessedQuantity(BigDecimal.ZERO);
                }
                deleteMap.put(processingProduct.getId(), copyToppingDetail);
            }
            if (product.getProcessedQuantity().compareTo(BigDecimal.ZERO) > 0) {
                if (product.getProcessedQuantity().compareTo(cancelQuantity) >= 0) {
                    copyDetail.setQuantity(copyDetail.getQuantity().add(BigDecimal.ZERO.subtract(cancelQuantity)));
                    product.setProcessedQuantity(product.getProcessedQuantity().subtract(cancelQuantity));
                    product.setCanceledQuantity(product.getCanceledQuantity().add(cancelQuantity));
                    cancelQuantity = BigDecimal.ZERO;
                } else {
                    copyDetail.setQuantity(copyDetail.getQuantity().add(BigDecimal.ZERO.subtract(product.getProcessedQuantity())));
                    product.setCanceledQuantity(product.getCanceledQuantity().add(product.getProcessedQuantity()));
                    index++;
                    cancelQuantity = cancelQuantity.subtract(product.getProcessedQuantity());
                    product.setProcessedQuantity(BigDecimal.ZERO);
                }
                deleteMap.put(product.getId(), copyDetail);
            } else {
                index++;
            }
        }
        if (cancelQuantity.compareTo(BigDecimal.ZERO) > 0) {
            throw new BadRequestAlertException(
                ExceptionConstants.DELETE_LIST_INVALID_VI,
                ENTITY_NAME,
                ExceptionConstants.DELETE_LIST_INVALID
            );
        }
        return new ArrayList<>(deleteMap.values());
    }

    private ProcessingRequestDetail genCopyRequestDetail(ProcessingProduct product, ProcessingRequest processingRequest) {
        ProcessingRequestDetail copyDetail = new ProcessingRequestDetail();
        BeanUtils.copyProperties(product.getProcessingRequestDetail(), copyDetail);
        copyDetail.setId(null);
        copyDetail.setCreateTime(ZonedDateTime.now());
        copyDetail.setRefId(product.getProcessingRequestDetail().getId());
        copyDetail.setQuantity(BigDecimal.ZERO);
        copyDetail.setProcessingRequest(processingRequest);
        copyDetail.setProcessingProducts(new ArrayList<>());
        return copyDetail;
    }

    private List<ProcessingRequestDetail> deleteProcessingProduct(
        List<Integer> deleteList,
        ProcessingRequest request,
        Bill bill,
        User user
    ) {
        List<ProcessingRequestDetail> details = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();
        for (Integer id : deleteList) {
            List<ProcessingProduct> processingProducts = processingProductRepository.findByBillProductIdAndRefIdIsNull(id);
            for (ProcessingProduct processingProduct : processingProducts) {
                if (processingProduct.getDeliveredQuantity().compareTo(BigDecimal.ZERO) > 0) {
                    throw new BadRequestAlertException(
                        ExceptionConstants.DELETE_LIST_INVALID_VI,
                        ENTITY_NAME,
                        ExceptionConstants.DELETE_LIST_INVALID
                    );
                }
                ids.add(processingProduct.getId());
                processingProduct.setCanceledQuantity(
                    processingProduct
                        .getCanceledQuantity()
                        .add(processingProduct.getProcessingQuantity().add(processingProduct.getProcessedQuantity()))
                );
                ProcessingRequestDetail detail = processingProduct.getProcessingRequestDetail();
                ProcessingRequestDetail copyDetail = new ProcessingRequestDetail();
                BigDecimal quantity = processingProduct.getProcessingQuantity().add(processingProduct.getProcessedQuantity());
                BeanUtils.copyProperties(detail, copyDetail);
                copyDetail.setId(null);
                copyDetail.setRefId(detail.getId());
                copyDetail.setCreateTime(ZonedDateTime.now());
                copyDetail.setQuantity(BigDecimal.ZERO.subtract(quantity));
                copyDetail.setProcessingProducts(new ArrayList<>());
                copyDetail.setProcessingRequest(request);
                processingProduct.setProcessingQuantity(BigDecimal.ZERO);
                processingProduct.setProcessedQuantity(BigDecimal.ZERO);
                details.add(copyDetail);
                if (quantity.compareTo(BigDecimal.ZERO) > 0) {
                    notificationRepository.save(createCancelNotification(bill, user, copyDetail.getProductName(), quantity));
                }
            }
        }
        if (!ids.isEmpty()) {
            List<ProcessingProduct> products = processingProductRepository.findByRefIdInAndIsTopping(ids, Boolean.TRUE);
            for (ProcessingProduct processingProduct : products) {
                processingProduct.setCanceledQuantity(
                    processingProduct
                        .getCanceledQuantity()
                        .add(processingProduct.getProcessingQuantity().add(processingProduct.getProcessedQuantity()))
                );
                processingProduct.setProcessingQuantity(BigDecimal.ZERO);
                processingProduct.setProcessedQuantity(BigDecimal.ZERO);
                ProcessingRequestDetail detail = processingProduct.getProcessingRequestDetail();
                ProcessingRequestDetail copyDetail = new ProcessingRequestDetail();
                BeanUtils.copyProperties(detail, copyDetail);
                copyDetail.setId(null);
                copyDetail.setRefId(detail.getId());
                copyDetail.setCreateTime(ZonedDateTime.now());
                copyDetail.setQuantity(BigDecimal.ZERO.subtract(detail.getQuantity()));
                copyDetail.setProcessingProducts(new ArrayList<>());
                copyDetail.setProcessingRequest(request);
                details.add(copyDetail);
            }
        }
        return details;
    }

    private ProcessingRequestDetail createCancelBill(ProcessingProduct processingProduct, ProcessingRequest request) {
        ProcessingRequestDetail copyDetail = new ProcessingRequestDetail();
        BigDecimal cancelQuantity = processingProduct.getProcessingQuantity().add(processingProduct.getProcessedQuantity());
        BeanUtils.copyProperties(processingProduct.getProcessingRequestDetail(), copyDetail);
        copyDetail.setId(null);
        copyDetail.setCreateTime(ZonedDateTime.now());
        copyDetail.setRefId(processingProduct.getProcessingRequestDetail().getId());
        copyDetail.setQuantity(BigDecimal.ZERO.subtract(cancelQuantity));
        copyDetail.setProcessingProducts(new ArrayList<>());
        copyDetail.setProcessingRequest(request);
        return copyDetail;
    }

    private void saveBillProductId(Bill bill, int positionSub) {
        List<ProcessingRequest> requests = processingRequestRepository.findByBillId(bill.getId());
        List<BillProductRelation> list = processingRequestDetailRepository.getBillProductId(bill.getId(), positionSub);
        Map<Integer, Integer> positionMap = bill
            .getProducts()
            .stream()
            .collect(Collectors.toMap(BillProduct::getId, BillProduct::getPosition));
        Map<Integer, List<Integer>> billProductMap = new HashMap<>();
        for (BillProductRelation relation : list) {
            List<Integer> requestDetails = new ArrayList<>();
            if (billProductMap.containsKey(relation.getBillProductId())) {
                requestDetails = billProductMap.get(relation.getBillProductId());
            }
            requestDetails.add(relation.getRequestDetailId());
            billProductMap.put(relation.getBillProductId(), requestDetails);
        }
        Map<Integer, Integer> billProductMap2 = list
            .stream()
            .collect(Collectors.toMap(BillProductRelation::getRequestDetailId, BillProductRelation::getBillProductId));
        Map<Integer, Integer> processsingDetailMap = list
            .stream()
            .collect(Collectors.toMap(BillProductRelation::getRequestDetailId, BillProductRelation::getId));
        Map<Integer, List<Integer>> billProductParentMap = new HashMap<>();
        for (BillProduct billProduct : bill.getProducts()) {
            if (billProduct.getParentId() != null) {
                List<Integer> ids = new ArrayList<>();
                if (billProductParentMap.containsKey(billProduct.getParentId())) {
                    ids = billProductParentMap.get(billProduct.getParentId());
                }
                ids.add(billProduct.getId());
                billProductParentMap.put(billProduct.getParentId(), ids);
            }
        }
        Map<Integer, Integer> refIdMap = new HashMap<>();
        for (Map.Entry<Integer, List<Integer>> entry : billProductMap.entrySet()) {
            if (billProductParentMap.containsKey(entry.getKey())) {
                List<Integer> parentValue = entry.getValue();
                List<Integer> billProductParentValue = billProductParentMap.get(entry.getKey());
                for (Integer value : billProductParentValue) {
                    if (billProductMap.containsKey(value)) {
                        List<Integer> childValues = billProductMap.get(value);
                        for (int i = 0; i < childValues.size(); i++) {
                            refIdMap.put(childValues.get(i), parentValue.get(i));
                        }
                    }
                }
            }
        }
        for (ProcessingRequest request : requests) {
            for (ProcessingRequestDetail detail : request.getDetails()) {
                if (positionMap.containsKey(billProductMap2.get(detail.getId()))) {
                    detail.setPosition(positionMap.get(billProductMap2.get(detail.getId())));
                }
                if (refIdMap.containsKey(detail.getId())) {
                    detail.setRefId(refIdMap.get(detail.getId()));
                }
                for (ProcessingProduct processingProduct : detail.getProcessingProducts()) {
                    if (billProductMap2.containsKey(processingProduct.getProcessingRequestDetail().getId())) {
                        processingProduct.setBillProductId(billProductMap2.get(processingProduct.getProcessingRequestDetail().getId()));
                    }
                    if (refIdMap.containsKey(processingProduct.getProcessingRequestDetail().getId())) {
                        processingProduct.setRefId(
                            processsingDetailMap.get(refIdMap.get(processingProduct.getProcessingRequestDetail().getId()))
                        );
                    }
                }
            }
        }
    }

    private void saveToppingBill(Bill bill, BillCreateRequest billDTO, Boolean isNew) throws JsonProcessingException {
        List<BillProduct> parentSaveList = new ArrayList<>();
        List<BillProduct> toppingSaveList = new ArrayList<>();
        List<BillProduct> toppingResultList = new ArrayList<>();
        Map<Integer, List<BillProduct>> toppingMap = new HashMap<>();

        for (BillProduct billProduct : bill.getProducts()) {
            if (billProduct.getParentId() == null) {
                parentSaveList.add(billProduct);
                if (!toppingMap.containsKey(billProduct.getProductProductUnitId())) {
                    toppingMap.put(billProduct.getProductProductUnitId(), new ArrayList<>());
                }
            } else {
                toppingSaveList.add(billProduct);
            }
        }

        for (BillProductRequest request : billDTO.getProducts()) {
            if (request.getToppings() != null && !request.getToppings().isEmpty()) {
                BillProduct parentFind = findListSaveTopping(parentSaveList, modelMapper.map(request, BillProduct.class));
                if (parentFind != null) {
                    for (BillProductToppingRequest toppingRequest : request.getToppings()) {
                        BillProduct toppingFind = findListSaveTopping(toppingSaveList, modelMapper.map(toppingRequest, BillProduct.class));
                        if (toppingFind != null) {
                            toppingSaveList.remove(toppingFind);
                            toppingFind.setParentId(parentFind.getId());
                            toppingResultList.add(toppingFind);

                            if (toppingMap.containsKey(parentFind.getProductProductUnitId())) {
                                List<BillProduct> billProducts = toppingMap.get(parentFind.getProductProductUnitId());
                                billProducts.add(toppingFind);
                                toppingMap.put(parentFind.getProductProductUnitId(), billProducts);
                            }
                        }
                    }
                }
            }
        }
        parentSaveList.addAll(toppingResultList);
        bill.setProducts(parentSaveList);
        //        billRepository.save(bill);
    }

    private BillProduct findListSaveTopping(List<BillProduct> billProducts, BillProduct billProduct) {
        for (BillProduct product : billProducts) {
            if (
                Objects.equals(product.getProductProductUnitId(), billProduct.getProductProductUnitId()) &&
                Objects.equals(product.getPosition(), billProduct.getPosition())
            ) {
                return product;
            }
        }
        return null;
    }

    @Override
    public ResultDTO cancelBillCompleted(BillCompletedCancelRequest request) {
        Integer comId = request.getComId();
        //        userService.getUserWithAuthorities(comId);
        Integer billId = request.getBillId();
        Optional<Bill> billOptional = billRepository.findByIdAndComIdAndStatus(billId, comId, BillConstants.Status.BILL_COMPLETE);
        if (billOptional.isPresent()) {
            Bill bill = billOptional.get();
            Integer customerId = bill.getCustomerId();
            BigDecimal amountPayment = bill.getPayment().getAmount();

            Map<Integer, ProductProductUnit> prodProdUnitMap = new HashMap<>();
            Set<Integer> productIds = new HashSet<>();
            Set<Integer> productIdsTracking = new HashSet<>();
            List<Product> productSave = new ArrayList<>();
            List<BillProduct> billProducts = bill.getProducts();
            billProducts.forEach(bp -> {
                productIds.add(bp.getProductId());
            });
            productRepository
                .findAllByComIdAndIdInAndActiveAndInventoryTracking(comId, new ArrayList<>(productIds), true, true)
                .forEach(p -> {
                    productIdsTracking.add(p.getId());
                    productSave.add(p);
                });

            List<ProductProductUnit> productProductUnits = productProductUnitRepository.findAllByComIdAndProductIds(
                comId,
                new ArrayList<>(productIdsTracking)
            );
            Map<Integer, BigDecimal> prodIdAndInventoryCountMap = new HashMap<>();
            for (ProductProductUnit ppu : productProductUnits) {
                ppu.setConvertRate(ppu.getConvertRate() == null ? BigDecimal.ONE : ppu.getConvertRate());
                prodProdUnitMap.put(ppu.getId(), ppu);
                prodIdAndInventoryCountMap.put(ppu.getProductId(), BigDecimal.ZERO);
            }

            // tính tổng inventory_count theo productId
            billProducts.forEach(bp -> {
                Integer productId = bp.getProductId();
                Integer ppUnitId = bp.getProductProductUnitId();
                if (ppUnitId != null && prodIdAndInventoryCountMap.containsKey(productId) && prodProdUnitMap.containsKey(ppUnitId)) {
                    BigDecimal quantity = prodIdAndInventoryCountMap.get(productId);
                    BigDecimal convertRate = prodProdUnitMap.get(ppUnitId).getConvertRate();
                    prodIdAndInventoryCountMap.put(productId, quantity.add(bp.getQuantity().multiply(convertRate)));
                }
            });

            // set lại onHand, inventory_count
            calOnHandForCancelReturnBill(productProductUnits, prodIdAndInventoryCountMap, productSave);

            // mc_payment
            McPayment mcPayment;
            if (amountPayment.compareTo(BigDecimal.ZERO) > 0) {
                mcPayment = new McPayment();
                mcPayment.setBusinessTypeId(businessTypeRepository.getIdByComIdAndCode(comId, BusinessTypeConstants.Bill.CANCEL));
                mcPayment.setComId(comId);
                mcPayment.setNo(userService.genCode(comId, Constants.PHIEU_CHI));
                mcPayment.setTypeDesc(Constants.PHIEU_CHI_HUY_HANG);
                mcPayment.setCustomerId(bill.getCustomerId());
                mcPayment.setCustomerName(bill.getCustomerName());
                mcPayment.setDate(ZonedDateTime.now());
                mcPayment.setPaymentMethod(request.getPaymentMethod());
                mcPayment.setAmount(
                    amountPayment.subtract(bill.getPayment().getRefund() != null ? bill.getPayment().getRefund() : BigDecimal.ZERO)
                );
                mcPayment.setCustomerNormalizedName(Common.normalizedName(List.of(mcPayment.getCustomerName())));
                mcPayment.setDescription("Huỷ hàng " + bill.getCode());
            } else {
                mcPayment = null;
            }

            bill.setStatus(BillConstants.Status.BILL_CANCEL);

            List<TaskLogSendQueue> requestSendQueue = transactionTemplate.execute(status -> {
                List<TaskLogSendQueue> taskLogSendQueues = new ArrayList<>();
                try {
                    billRepository.save(bill);
                    productProductUnitRepository.saveAll(productProductUnits);
                    productRepository.saveAll(productSave);
                    if (mcPayment != null) {
                        mcPayment.setBillId(bill.getId());
                        mcPaymentRepository.save(mcPayment);
                    }

                    // invoice
                    Optional<Invoice> invoiceOptional = invoiceRepository.findOneByBillId(billId);
                    if (invoiceOptional.isPresent()) {
                        Invoice invoice = invoiceOptional.get();
                        if (invoice.getStatus() == null) {
                            invoiceRepository.delete(invoice);
                        } else {
                            if (
                                Objects.equals(invoice.getStatus(), InvoiceConstants.Status.DRAFT) ||
                                Objects.equals(invoice.getStatus(), InvoiceConstants.Status.PUBLISHED)
                            ) {
                                taskLogSendQueues.add(
                                    publishQueueCancelOrRemoveUnsignedInvoice(
                                        comId,
                                        invoice.getStatus(),
                                        invoice.getId(),
                                        invoice.getIkey(),
                                        invoice.getPattern()
                                    )
                                );
                            }
                        }
                    }

                    // rs_inout_ward
                    List<BillProduct> billProductsSaveRsInoutWard = new ArrayList<>();
                    billProducts.forEach(bp -> {
                        if (productIdsTracking.contains(bp.getProductId())) {
                            billProductsSaveRsInoutWard.add(bp);
                        }
                    });

                    // loyalty_card_usage && reset_rank
                    List<LoyaltyCardUsage> loyaltyCardUsages = loyaltyCardUsageRepository.findOneByComIdAndRefIdAndType(
                        comId,
                        billId,
                        LoyaltyCardUsageConstants.Type.CONG_DIEM
                    );
                    Optional<CustomerCard> customerCardOptional = customerCardRepository.getCustomerCardByCustomerId(customerId);
                    if (!loyaltyCardUsages.isEmpty() && customerId != null) {
                        BigDecimal totalAmount = BigDecimal.ZERO;
                        Integer totalPoint = 0;
                        for (LoyaltyCardUsage item : loyaltyCardUsages) {
                            totalAmount = totalAmount.add(item.getAmount() == null ? BigDecimal.ZERO : item.getAmount());
                            totalPoint += item.getPoint() == null ? 0 : item.getPoint();
                        }
                        LoyaltyCardUsage cardsUsageOld = loyaltyCardUsages.get(0);

                        LoyaltyCardUsage minusPoint = new LoyaltyCardUsage();
                        minusPoint.comId(comId);
                        minusPoint.setUsageDate(ZonedDateTime.now());
                        minusPoint.setCustomerId(customerId);
                        minusPoint.setCardId(cardsUsageOld.getCardId());
                        minusPoint.setRefId(billId);
                        minusPoint.setAmount(BigDecimal.ZERO);
                        minusPoint.setPoint(totalPoint);
                        if (Objects.equals(cardsUsageOld.getType(), LoyaltyCardUsageConstants.Type.CONG_DIEM)) {
                            minusPoint.setType(LoyaltyCardUsageConstants.Type.TRU_DIEM);
                        }

                        LoyaltyCardUsage plusAmount = new LoyaltyCardUsage();
                        BeanUtils.copyProperties(minusPoint, plusAmount);
                        plusAmount.setAmount(totalAmount);
                        plusAmount.setPoint(0);
                        plusAmount.setType(LoyaltyCardUsageConstants.Type.CONG_TIEN);
                        List<LoyaltyCardUsage> cardsUsageSave = List.of(minusPoint, plusAmount);

                        loyaltyCardUsageRepository.saveAll(cardsUsageSave);
                    } else {
                        LoyaltyCardUsage plusAmount = new LoyaltyCardUsage();
                        plusAmount.comId(comId);
                        plusAmount.setUsageDate(ZonedDateTime.now());
                        plusAmount.setCustomerId(customerId);
                        customerCardOptional.ifPresent(customerCard -> plusAmount.setCardId(customerCard.getCardId()));
                        plusAmount.setRefId(billId);
                        plusAmount.setAmount(
                            amountPayment.subtract(bill.getPayment().getRefund() != null ? bill.getPayment().getRefund() : BigDecimal.ZERO)
                        );
                        plusAmount.setPoint(0);
                        plusAmount.setType(LoyaltyCardUsageConstants.Type.CONG_TIEN);
                        loyaltyCardUsageRepository.save(plusAmount);
                    }
                    resetRankAfterCancelBill(comId, customerId, customerCardOptional);

                    //                    taskLogSendQueues.add(
                    createAndPublishQueueRsInWardTask(
                        comId,
                        bill,
                        null,
                        billProductsSaveRsInoutWard,
                        BusinessTypeConstants.Bill.CANCEL,
                        request.getPaymentMethod()
                    );
                    //                    );
                    return taskLogSendQueues;
                } catch (Exception e) {
                    log.error("Can not create queue task for eb88 create rsInoutWard: {}", e.getMessage());
                }
                return null;
            });

            if (requestSendQueue != null && !requestSendQueue.isEmpty()) {
                for (TaskLogSendQueue taskLogSendQueue : requestSendQueue) {
                    if (taskLogSendQueue != null) {
                        userService.sendTaskLog(taskLogSendQueue);
                    }
                }
            }
            return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.BILL_COMPLETED_CANCEL_SUCCESS, true);
        }

        return new ResultDTO(ExceptionConstants.BILL_NOT_EXIST, ExceptionConstants.BILL_NOT_EXIST_VI, true);
    }

    private void resetRankAfterCancelBill(Integer comId, Integer customerId, Optional<CustomerCard> customerCardOptional) {
        Optional<CardPolicy> cardPolicyOptional = cardPolicyRepository.findOneByComIdAndUpgradeTypeAndStatusIn(
            comId,
            CardPolicyConstants.UpgradeType.TOTAL_SPENDING,
            CardPolicyConstants.Status.STATUS_AVAILABLE
        );
        if (cardPolicyOptional.isPresent() && customerCardOptional.isPresent()) {
            Integer cardIdResult;
            CustomerCard customerCard = customerCardOptional.get();
            cardIdResult = customerCard.getCardId();
            BigDecimal totalAccumAmount = loyaltyCardUsageRepository.getTotalAmountBySpendingAfterCancelBill(
                comId,
                customerId,
                List.of(
                    LoyaltyCardUsageConstants.Type.TRU_DIEM,
                    LoyaltyCardUsageConstants.Type.CONG_DIEM,
                    LoyaltyCardUsageConstants.Type.CONG_TIEN
                )
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
                    if (upgradeValue.compareTo(BigDecimal.ZERO) == 0 && totalAccumAmount.compareTo(upgradeValue) <= 0) {
                        cardIdResult = condition.getCardId();
                        break;
                    }
                    if (totalAccumAmount.compareTo(upgradeValue) >= 0) {
                        cardIdResult = condition.getCardId();
                    }
                }
                customerCard.setCardId(cardIdResult);
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

    private TaskLogSendQueue publishQueueCancelOrRemoveUnsignedInvoice(
        Integer comId,
        Integer method,
        Integer id,
        String ikey,
        String pattern
    ) {
        TaskLog taskLog = new TaskLog();
        taskLog.setComId(comId);
        taskLog.setStatus(TaskLogConstants.Status.PROCESSING);
        taskLog.setType(
            Objects.equals(method, InvoiceConstants.Status.DRAFT)
                ? TaskLogConstants.Type.REMOVE_UNSIGNED_INVOICE
                : TaskLogConstants.Type.CANCEL_INVOICE
        );
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.INDENT_OUTPUT);
        try {
            taskLog.setContent(
                objectMapper.writeValueAsString(new TaskCancelInvoice(comId.toString(), id.toString(), ikey, pattern, method.toString()))
            );
            taskLogRepository.save(taskLog);
            return new TaskLogSendQueue(taskLog.getId().toString(), taskLog.getType());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ResultDTO returnBillCompleted(BillCompletedReturnRequest request) {
        Integer comId = request.getComId();
        userService.getUserWithAuthorities(comId);
        Integer billId = request.getBillId();
        String taxAuthorityCode = request.getTaxAuthorityCode();
        List<BillCompletedReturnRequest.ProductReturn> productsReturn = request.getProducts();
        Optional<Bill> billOptional = billRepository.findByIdAndComIdAndStatus(billId, comId, BillConstants.Status.BILL_COMPLETE);
        if (billOptional.isPresent()) {
            Bill oldBill = billOptional.get();
            Map<Integer, BigDecimal> prodProdUnitOldMap = new HashMap<>();
            Map<Integer, BigDecimal> totalQuantityReturnOldMap = new HashMap<>();
            oldBill
                .getProducts()
                .forEach(oldProduct -> prodProdUnitOldMap.put(oldProduct.getProductProductUnitId(), oldProduct.getQuantity()));
            // lấy tổng số lượng trả của những lần trước
            if (!Strings.isNullOrEmpty(oldBill.getBillIdReturns())) {
                List<String> oldBillReturnIds = Arrays.asList(oldBill.getBillIdReturns().split(","));
                List<BillProductCheckQuantity> quantityOldReturn = billRepository.checkQuantityOldReturn(oldBillReturnIds);
                quantityOldReturn.forEach(oldQuantity ->
                    totalQuantityReturnOldMap.put(oldQuantity.getProductProductUnitId(), oldQuantity.getTotalQuantity())
                );
            } else {
                oldBill
                    .getProducts()
                    .forEach(billProduct -> totalQuantityReturnOldMap.put(billProduct.getProductProductUnitId(), BigDecimal.ZERO));
            }
            Bill saveBill = new Bill();
            List<BillProduct> billProducts = new ArrayList<>();
            saveBill.setComId(comId);
            saveBill.setCode(userService.genCode(comId, Constants.DON_HANG));
            saveBill.setCustomerId(oldBill.getCustomerId());
            saveBill.setCustomerName(oldBill.getCustomerName());
            saveBill.setBillDate(ZonedDateTime.now());
            saveBill.setDeliveryType(null);
            saveBill.setDiscountAmount(BigDecimal.ZERO);
            saveBill.setTotalPreTax(BigDecimal.ZERO);
            saveBill.setVatRate(0);
            saveBill.setVatAmount(BigDecimal.ZERO);
            saveBill.setStatus(BillConstants.Status.BILL_RETURN);
            saveBill.setProductDiscountAmount(BigDecimal.ZERO);
            saveBill.setCustomerNormalizedName(Common.normalizedName(List.of(saveBill.getCustomerName())));
            saveBill.setDescription(request.getDescription());

            Optional<String> patternOption = configRepository.getValueByComIdAndCode(comId, EasyInvoiceConstants.INVOICE_PATTERN);
            String pattern = "";
            if (patternOption.isPresent()) {
                pattern = patternOption.get();
            }
            if (!Strings.isNullOrEmpty(pattern)) {
                if (!Common.checkTaxAuthorityCode(taxAuthorityCode)) {
                    throw new BadRequestAlertException(
                        ExceptionConstants.TAX_AUTHORITY_CODE_INVALID_VI,
                        ENTITY_NAME,
                        ExceptionConstants.TAX_AUTHORITY_CODE_INVALID
                    );
                } else {
                    Integer countTaxCode = billRepository.countAllByComIdAndTaxAuthorityCode(comId, taxAuthorityCode);
                    if (countTaxCode != null && countTaxCode > 0) {
                        throw new BadRequestAlertException(
                            ExceptionConstants.TAX_AUTHORITY_CODE_ALREADY_VI,
                            ENTITY_NAME,
                            ExceptionConstants.TAX_AUTHORITY_CODE_ALREADY
                        );
                    }
                }
            } else {
                if (!taxAuthorityCode.equals(Constants.TAX_AUTHORITY_CODE_DEFAULT)) {
                    throw new BadRequestAlertException(
                        ExceptionConstants.TAX_AUTHORITY_CODE_INVALID_VI,
                        ENTITY_NAME,
                        ExceptionConstants.TAX_AUTHORITY_CODE_INVALID
                    );
                }
            }
            saveBill.setTaxAuthorityCode(taxAuthorityCode);

            Set<Integer> prodProdIdRequest = new HashSet<>();
            Set<String> productCodesRequest = new HashSet<>();
            Set<Integer> productIdsTracking = new HashSet<>();
            List<Product> productSave = new ArrayList<>();
            Map<Integer, ProductProductUnit> productProductUnitMap = new HashMap<>();
            Map<Integer, BigDecimal> prodIdAndInventoryCountMap = new HashMap<>();
            BigDecimal totalQuantity = BigDecimal.ZERO;
            BigDecimal totalPrice = BigDecimal.ZERO;
            BigDecimal totalAmount;
            Map<Integer, ProductProductUnit> prodProdUnitResultMap = new HashMap<>();
            productsReturn.forEach(p -> {
                prodProdIdRequest.add(p.getProductProductUnitId());
                productCodesRequest.add(p.getProductCode());
            });

            productProductUnitRepository
                .findAllByComIdAndIdIn(comId, new ArrayList<>(prodProdIdRequest))
                .forEach(ppu -> productProductUnitMap.put(ppu.getId(), ppu));

            productRepository
                .findAllByComIdAndCodeInAndActiveAndInventoryTracking(comId, new ArrayList<>(productCodesRequest), true, true)
                .forEach(p -> {
                    productIdsTracking.add(p.getId());
                    productSave.add(p);
                });

            List<ProductProductUnit> prodProdUnitsResult = productProductUnitRepository.findAllByComIdAndProductIds(
                comId,
                new ArrayList<>(productIdsTracking)
            );
            prodProdUnitsResult.forEach(ppu -> {
                prodProdUnitResultMap.put(ppu.getId(), ppu);
                prodIdAndInventoryCountMap.put(ppu.getProductId(), BigDecimal.ZERO);
            });
            int position = 1;
            for (BillCompletedReturnRequest.ProductReturn prod : productsReturn) {
                Integer productId = productProductUnitMap.get(prod.getProductProductUnitId()).getProductId();
                Integer ppUnitId = prod.getProductProductUnitId();
                if (productId != null && productProductUnitMap.containsKey(ppUnitId)) {
                    // kiểm tra tổng số lần trước trả + hiện tại với đơn mua ban đầu
                    if (
                        totalQuantityReturnOldMap.containsKey(ppUnitId) &&
                        prodProdUnitOldMap.containsKey(ppUnitId) &&
                        prodProdUnitOldMap.get(ppUnitId).compareTo(totalQuantityReturnOldMap.get(ppUnitId).add(prod.getQuantity())) < 0
                    ) {
                        throw new BadRequestAlertException(
                            String.format(ExceptionConstants.RETURN_BILL_QUANTITY_INVALID_VI, prod.getProductName()),
                            ENTITY_NAME,
                            ExceptionConstants.RETURN_BILL_QUANTITY_INVALID
                        );
                    }
                    BillProduct billProduct = new BillProduct();
                    billProduct.setProductId(productId);
                    billProduct.setProductName(prod.getProductName());
                    billProduct.setQuantity(prod.getQuantity());
                    billProduct.setUnit(prod.getUnitName());
                    billProduct.setUnitPrice(prod.getUnitPrice());
                    billProduct.setDiscountAmount(BigDecimal.ZERO);
                    billProduct.setTotalPreTax(BigDecimal.ZERO);
                    billProduct.setVatRate(0);
                    billProduct.setVatAmount(BigDecimal.ZERO);
                    billProduct.setFeature(1);
                    billProduct.setAmount(prod.getQuantity().multiply(prod.getUnitPrice()));
                    billProduct.setProductCode(prod.getProductCode());
                    billProduct.setPosition(position);
                    billProduct.setUnitId(prod.getUnitId());
                    billProduct.setProductNormalizedName(Common.normalizedName(List.of(prod.getProductName())));
                    billProduct.setProductProductUnitId(prod.getProductProductUnitId());
                    billProduct.setBill(saveBill);
                    billProducts.add(billProduct);

                    totalQuantity = totalQuantity.add(prod.getQuantity());
                    totalPrice = totalPrice.add(prod.getUnitPrice());
                    position++;

                    if (
                        ppUnitId != null && prodIdAndInventoryCountMap.containsKey(productId) && prodProdUnitResultMap.containsKey(ppUnitId)
                    ) {
                        prodIdAndInventoryCountMap.put(
                            productId,
                            prodIdAndInventoryCountMap
                                .get(productId)
                                .add(prod.getQuantity().multiply(prodProdUnitResultMap.get(ppUnitId).getConvertRate()))
                        );
                    }
                    continue;
                }
                throw new InternalServerException(
                    ExceptionConstants.PRODUCT_NOT_FOUND_VI + " " + prod.getProductName(),
                    ENTITY_NAME,
                    ExceptionConstants.PRODUCT_NOT_FOUND
                );
            }
            totalAmount = totalQuantity.multiply(totalPrice);
            saveBill.setAmount(totalAmount);
            saveBill.setTotalAmount(totalAmount);
            saveBill.setQuantity(totalQuantity);
            saveBill.setProducts(billProducts);

            // set lại onHand, inventory_count
            calOnHandForCancelReturnBill(prodProdUnitsResult, prodIdAndInventoryCountMap, productSave);

            // bill_payment
            BillPayment billPayment = new BillPayment();
            billPayment.setBill(saveBill);
            billPayment.setPaymentMethod(request.getPaymentMethod());
            billPayment.setAmount(totalAmount);
            billPayment.setRefund(BigDecimal.ZERO);
            billPayment.setDebtType(0);
            billPayment.setDebt(BigDecimal.ZERO);

            // mc_payment
            McPayment mcPayment = new McPayment();
            mcPayment.setBusinessTypeId(businessTypeRepository.getIdByComIdAndCode(comId, BusinessTypeConstants.Bill.CANCEL));
            mcPayment.setComId(comId);
            mcPayment.setNo(userService.genCode(comId, Constants.PHIEU_CHI));
            mcPayment.setTypeDesc(Constants.PHIEU_CHI_TRA_HANG);
            mcPayment.setCustomerId(saveBill.getCustomerId());
            mcPayment.setCustomerName(saveBill.getCustomerName());
            mcPayment.setDate(ZonedDateTime.now());
            mcPayment.setPaymentMethod(request.getPaymentMethod());
            mcPayment.setAmount(totalAmount);
            mcPayment.setCustomerNormalizedName(Common.normalizedName(List.of(mcPayment.getCustomerName())));
            mcPayment.setDescription("Trả hàng " + saveBill.getCode());

            TaskLogSendQueue taskLogSendQueue = transactionTemplate.execute(status -> {
                try {
                    billRepository.save(saveBill);
                    oldBill.setBillIdReturns(
                        oldBill.getBillIdReturns() != null
                            ? oldBill.getBillIdReturns() + "," + saveBill.getId()
                            : saveBill.getId().toString()
                    );
                    billRepository.save(oldBill);
                    billPaymentRepository.save(billPayment);
                    productProductUnitRepository.saveAll(prodProdUnitsResult);
                    productRepository.saveAll(productSave);
                    mcPayment.setBillId(saveBill.getId());
                    mcPaymentRepository.save(mcPayment);

                    // rs_inout_ward
                    List<BillProduct> billProductsSaveRsInoutWard = new ArrayList<>();
                    billProducts.forEach(bp -> {
                        if (productIdsTracking.contains(bp.getProductId())) {
                            billProductsSaveRsInoutWard.add(bp);
                        }
                    });
                    return createAndPublishQueueRsInWardTask(
                        comId,
                        saveBill,
                        null,
                        billProductsSaveRsInoutWard,
                        BusinessTypeConstants.Bill.RETURN,
                        request.getPaymentMethod()
                    );
                } catch (Exception e) {
                    log.error("Can not create queue task for eb88 create rsInoutWard: {}", e.getMessage());
                }
                return null;
            });
            if (taskLogSendQueue != null) {
                userService.sendTaskLog(taskLogSendQueue);
            }
            return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.BILL_COMPLETED_RETURN_SUCCESS, true);
        }

        return new ResultDTO(ExceptionConstants.BILL_NOT_EXIST, ExceptionConstants.BILL_NOT_EXIST_VI, true);
    }

    private List<BillItemResponse> getCustomerCardResponse(List<BillItemResponse> itemResponses, User user) {
        List<Integer> customerIds = itemResponses
            .stream()
            .filter(item -> item.getCustomerId() != null && !item.getCustomerName().equals(Constants.CUSTOMER_NAME))
            .map(BillItemResponse::getCustomerId)
            .collect(Collectors.toList());
        List<Customer> customers = customerRepository.findAllByComIdAndIds(user.getCompanyId(), customerIds);
        List<CustomerResponse> customerResponses = new ArrayList<>();
        for (Customer customer : customers) {
            customerResponses.add(modelMapper.map(customer, CustomerResponse.class));
        }
        customerResponses = customerManagementService.getCustomerCard(user.getCompanyId(), customerResponses);
        customerManagementService.getCardBalance(user.getCompanyId(), customerResponses);
        Map<Integer, CustomerResponse> responseMap = new HashMap<>();
        for (CustomerResponse response : customerResponses) {
            responseMap.put(response.getId(), response);
        }
        for (BillItemResponse response : itemResponses) {
            if (responseMap.containsKey(response.getCustomerId())) {
                CustomerResponse customerResponse = responseMap.get(response.getCustomerId());
                BillDetailResponse.CustomerMoreInformation moreInformation = new BillDetailResponse.CustomerMoreInformation();
                moreInformation.setPointBalance(customerResponse.getPointBalance());
                moreInformation.setMoneyBalance(customerResponse.getMoneyBalance());
                moreInformation.setCardInformation(customerResponse.getCardInformation());
                response.setCustomerCard(moreInformation);
            }
        }
        return itemResponses;
    }

    @Override
    public ResultDTO getForProcessing(Pageable pageable, Integer type, Integer status) {
        User user = userService.getUserWithAuthorities();
        List<Integer> ids = processingProductRepository.findByRefIdIsNotNull(user.getCompanyId());
        Page<BillProductProcessing> page = billRepository.getForProcessing(user.getCompanyId(), type, status, ids);
        List<BillProductProcessing> processings = new ArrayList<>();
        if (Objects.equals(type, ProcessingAreaConstants.Type.UU_TIEN)) {
            processings = processForBillProduct(page.getContent(), status);
        } else if (Objects.equals(type, ProcessingAreaConstants.Type.THEO_MON)) {
            List<BillProductProcessing> data = processForBillProduct(page.getContent(), status);
            Map<Integer, BillProductProcessing> processingMap = new HashMap<>();
            Map<Integer, BillProductProcessing> noToppingMap = new HashMap<>();

            for (BillProductProcessing processing : data) {
                if (processing.getToppings() == null || processing.getToppings().isEmpty()) {
                    if (noToppingMap.containsKey(processing.getProductProductUnitId())) {
                        BillProductProcessing productProcessing = noToppingMap.get(processing.getProductProductUnitId());
                        productProcessing.setProcessingQuantity(
                            productProcessing.getProcessingQuantity().add(processing.getProcessingQuantity())
                        );
                        noToppingMap.put(productProcessing.getProductProductUnitId(), productProcessing);
                    } else {
                        noToppingMap.put(processing.getProductProductUnitId(), processing);
                    }
                } else {
                    processingMap.put(processing.getId(), processing);
                }
            }
            processings = new ArrayList<>(processingMap.values());
            processings.addAll(noToppingMap.values());
            processings =
                processings.stream().filter(r -> r.getProcessingQuantity().compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList());
            processings.sort(Comparator.comparing(o -> o.getProductName().toLowerCase()));
        } else if (Objects.equals(type, ProcessingAreaConstants.Type.THEO_BAN)) {
            List<BillProductProcessing> data = processForBillProduct(page.getContent(), status)
                .stream()
                .filter(r -> r.getProcessingQuantity().compareTo(BigDecimal.ZERO) > 0)
                .collect(Collectors.toList());
            Map<Integer, BillProductProcessing> noAreaMap = new HashMap<>();
            for (BillProductProcessing processing : data) {
                BillProductProcessing processingProduct;
                if (noAreaMap.containsKey(processing.getBillId())) {
                    processingProduct = noAreaMap.get(processing.getBillId());
                    List<BillProductProcessing.AreaProductProcessing> productProcessings = processingProduct.getAreaProcessing();
                    if (productProcessings == null) {
                        productProcessings = new ArrayList<>();
                    }
                    productProcessings.add(modelMapper.map(processing, BillProductProcessing.AreaProductProcessing.class));
                    processingProduct.setAreaProcessing(productProcessings);
                    processingProduct.setTotalQuantity(processingProduct.getTotalQuantity().add(processing.getProcessingQuantity()));
                } else {
                    processingProduct = new BillProductProcessing();
                    if (processing.getAreaUnitId() == null) {
                        processingProduct.setAreaUnitName(ProcessingAreaConstants.NO_AREA + " - " + processing.getBillCode());
                    } else {
                        processingProduct.setAreaUnitName(
                            processing.getAreaUnitName() + " - " + processing.getAreaName() + " - " + processing.getBillCode()
                        );
                    }
                    processingProduct.setTotalQuantity(processing.getProcessingQuantity());
                    processingProduct.setBillId(processing.getBillId());
                    List<BillProductProcessing.AreaProductProcessing> productProcessings = new ArrayList<>();
                    productProcessings.add(modelMapper.map(processing, BillProductProcessing.AreaProductProcessing.class));
                    processingProduct.setAreaProcessing(productProcessings);
                }
                noAreaMap.put(processingProduct.getBillId(), processingProduct);
            }
            processings = new ArrayList<>(noAreaMap.values());
            processings.sort(Comparator.comparing(b -> b.getAreaUnitName().toLowerCase()));
        }
        int count = processings.size();
        if (pageable != null) {
            processings =
                processings.subList(
                    Math.min(processings.size(), pageable.getPageNumber() * pageable.getPageSize()),
                    Math.min(processings.size(), (pageable.getPageNumber() + 1) * pageable.getPageSize())
                );
        }
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_GET_LIST, true, processings, count);
    }

    private List<BillProductProcessing> processForBillProduct(List<BillProductProcessing> processingList, Integer status) {
        User user = userService.getUserWithAuthorities();
        Map<Integer, BillProductProcessing> processingMap = new LinkedHashMap<>();
        Page<BillProductProcessing> deleteList = billRepository.getDeleteRequest(user.getCompanyId());
        Map<Integer, List<BillProductProcessing>> deleteMap = new HashMap<>();
        for (BillProductProcessing productProcessing : deleteList) {
            List<BillProductProcessing> processings = new ArrayList<>();
            if (deleteMap.containsKey(productProcessing.getId())) {
                processings = deleteMap.get(productProcessing.getId());
            }
            processings.add(productProcessing);
            deleteMap.put(productProcessing.getId(), processings);
        }
        for (BillProductProcessing processing : processingList) {
            if (!processing.getIsTopping() && processing.getRefId() == null) {
                processingMap.put(processing.getId(), processing);
            }
        }
        for (BillProductProcessing processing : processingList) {
            if (deleteMap.containsKey(processing.getId()) && processingMap.containsKey(processing.getId())) {
                BillProductProcessing productProcessing = processingMap.get(processing.getId());
                productProcessing.setDeletes(
                    List.of(
                        modelMapper.map(deleteMap.get(productProcessing.getId()), BillProductProcessing.ProcessingProductDelete[].class)
                    )
                );
            }
            if (processing.getIsTopping() && processing.getRefId() != null && processingMap.containsKey(processing.getRefId())) {
                BillProductProcessing parentProcessing = processingMap.get(processing.getRefId());
                List<BillProductProcessing.BillProductToppingProcessing> toppings = new ArrayList<>();
                if (parentProcessing.getToppings() != null && !parentProcessing.getToppings().isEmpty()) {
                    toppings = parentProcessing.getToppings();
                }
                BillProductProcessing.BillProductToppingProcessing topping = modelMapper.map(
                    processing,
                    BillProductProcessing.BillProductToppingProcessing.class
                );
                //                if (Objects.equals(status, ProcessingAreaConstants.Status.PROCESSING)) {
                //                    topping.setQuantity(processing.getProcessingQuantity());
                //                } else if (Objects.equals(status, ProcessingAreaConstants.Status.PROCESSED)) {
                //                    topping.setQuantity(processing.getProcessedQuantity());
                //                }
                topping.setQuantity(
                    processing.getNotifiedQuantity().divide(parentProcessing.getNotifiedQuantity(), 6, RoundingMode.HALF_UP)
                );
                toppings.add(topping);
                parentProcessing.setToppings(toppings);
                processingMap.put(parentProcessing.getId(), parentProcessing);
            }
        }
        List<BillProductProcessing> result = new ArrayList<>();
        for (BillProductProcessing productProcessing : processingMap.values()) {
            if (
                Objects.equals(status, ProcessingAreaConstants.Status.PROCESSING) &&
                (productProcessing.getProcessingQuantity().compareTo(BigDecimal.ZERO) != 0)
            ) {
                result.add(productProcessing);
            } else if (
                Objects.equals(status, ProcessingAreaConstants.Status.PROCESSED) &&
                (
                    productProcessing.getProcessedQuantity().compareTo(BigDecimal.ZERO) != 0
                    //                        || checkShowDelete(productProcessing)
                )
            ) {
                result.add(productProcessing);
            }
        }
        return result;
    }

    private boolean checkShowDelete(BillProductProcessing productProcessing) {
        if (productProcessing.getDeletes() != null && !productProcessing.getDeletes().isEmpty()) {
            List<BillProductProcessing.ProcessingProductDelete> deletes = productProcessing.getDeletes();
            BillProductProcessing.ProcessingProductDelete maxTime = deletes
                .stream()
                .filter(delete -> delete.getCreateTime() != null)
                .max(Comparator.comparing(BillProductProcessing.ProcessingProductDelete::getCreateTime))
                .orElse(null);
            if (maxTime == null) {
                return true;
            }
            return (
                productProcessing
                    .getUpdateTime()
                    .truncatedTo(ChronoUnit.SECONDS)
                    .compareTo(maxTime.getCreateTime().truncatedTo(ChronoUnit.SECONDS)) ==
                0
            );
        }
        return false;
    }

    private void calOnHandForCancelReturnBill(
        List<ProductProductUnit> prodProdUnitsResult,
        Map<Integer, BigDecimal> prodIdAndInventoryCountMap,
        List<Product> productSave
    ) {
        Map<Integer, BigDecimal> onHandFinalMap = new HashMap<>();
        BigDecimal onHandWithPrimary = BigDecimal.ZERO;
        for (ProductProductUnit ppu : prodProdUnitsResult) {
            if (ppu.getIsPrimary()) {
                onHandWithPrimary = ppu.getOnHand();
            }
            int prodId = ppu.getProductId();
            if (prodIdAndInventoryCountMap.containsKey(prodId)) {
                BigDecimal inventoryCount = prodIdAndInventoryCountMap.get(prodId);
                if (ppu.getIsPrimary()) {
                    onHandWithPrimary = onHandWithPrimary.add(inventoryCount);
                    ppu.setOnHand(onHandWithPrimary);
                    onHandFinalMap.put(ppu.getProductId(), onHandWithPrimary);
                } else {
                    ppu.setOnHand(onHandWithPrimary.divide(ppu.getConvertRate(), new MathContext(6)));
                }
            }
        }

        for (Product p : productSave) {
            if (prodIdAndInventoryCountMap.containsKey(p.getId())) {
                p.setInventoryCount(onHandFinalMap.get(p.getId()));
            }
        }
    }

    private static Notification createNotification(User user, String content, Integer billId) {
        Notification notification = new Notification();
        notification.setSubject(NotificationConstant.Subject.CHE_BIEN);
        notification.setContent(content);
        notification.setType(NotificationConstant.Type.TAO_DON);
        notification = createDefaultNotification(notification, billId, user);
        return notification;
    }

    private void createNotification(BillCreateRequest billDTO, Bill bill) {
        User user = userService.getUserWithAuthorities();
        String content = user.getFullName() + NotificationConstant.Content.CREATE_BILL + bill.getCode();
        Notification notification = createNotification(user, content, bill.getId());
        notificationRepository.save(notification);
    }

    private void createUpdateProcessingNotification(List<String> list, String areaUnitName, Integer comId, User user, Integer billId) {
        List<Notification> notifications = new ArrayList<>();
        if (list.size() > 2) {
            Notification notification = getNotification(areaUnitName, comId, user, null, billId);
            notifications.add(notification);
        } else {
            for (String billProduct : list) {
                Notification notification = getNotification(areaUnitName, comId, user, billProduct, billId);
                notifications.add(notification);
            }
        }
        notificationRepository.saveAll(notifications);
    }

    private static Notification getNotification(String areaUnitName, Integer comId, User user, String productName, Integer billId) {
        Notification notification = new Notification();
        notification.setSubject(NotificationConstant.Subject.CHE_BIEN);
        notification.setContent(
            (Strings.isNullOrEmpty(areaUnitName) ? ProcessingAreaConstants.NO_AREA : areaUnitName) +
            " " +
            (Strings.isNullOrEmpty(productName) ? "" : (productName + " ")) +
            NotificationConstant.Content.SEND_REQUEST
        );
        notification.setType(NotificationConstant.Type.TAO_DON);
        notification = createDefaultNotification(notification, billId, user);
        return notification;
    }

    private static Notification createCancelNotification(Bill bill, User user, String productName, BigDecimal cancelQuantity) {
        Notification notification = new Notification();
        notification.setSubject(NotificationConstant.Subject.HUY_MON);
        notification.setContent(
            (!Strings.isNullOrEmpty(bill.getAreaUnitName()) ? ("bàn " + bill.getAreaUnitName() + " ") : "") +
            bill.getCode() +
            " " +
            user.getFullName() +
            " vừa hủy " +
            cancelQuantity.stripTrailingZeros().toPlainString() +
            " " +
            productName +
            " đã báo bếp"
        );
        notification.setType(NotificationConstant.Type.TAO_DON);
        notification = createDefaultNotification(notification, bill.getId(), user);
        return notification;
    }

    private static Notification createDefaultNotification(Notification notification, Integer billId, User user) {
        notification.setBillId(billId);
        notification.setComId(user.getCompanyId());
        notification.setIsRead(Boolean.FALSE);
        NotificationUser notificationUser = new NotificationUser();
        notificationUser.setNotification(notification);
        notificationUser.setUserId(user.getId());
        notification.setNotificationUsers(List.of(notificationUser));
        return notification;
    }
}
