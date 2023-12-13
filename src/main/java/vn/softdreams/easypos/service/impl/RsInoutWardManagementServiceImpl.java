package vn.softdreams.easypos.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Strings;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import vn.softdreams.easypos.config.Constants;
import vn.softdreams.easypos.constants.*;
import vn.softdreams.easypos.domain.*;
import vn.softdreams.easypos.dto.TaskLogSendQueue;
import vn.softdreams.easypos.dto.productProductUnit.ProductProductUnitItems;
import vn.softdreams.easypos.dto.rsInoutWardDetail.GetOneByIdDetailResponse;
import vn.softdreams.easypos.dto.rsinoutward.*;
import vn.softdreams.easypos.integration.TaskLogIdEnqueueMessage;
import vn.softdreams.easypos.integration.easybooks88.queue.EB88Producer;
import vn.softdreams.easypos.integration.easybooks88.queue.dto.RsInOutWardTask;
import vn.softdreams.easypos.repository.*;
import vn.softdreams.easypos.service.RsInoutWardManagementService;
import vn.softdreams.easypos.service.UserService;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.util.Common;
import vn.softdreams.easypos.web.rest.errors.BadRequestAlertException;
import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;
import vn.softdreams.easypos.web.rest.errors.InternalServerException;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Service Implementation for managing {@link RsInoutWard}.
 */
@Service
@Transactional
public class RsInoutWardManagementServiceImpl implements RsInoutWardManagementService {

    private final Logger log = LoggerFactory.getLogger(RsInoutWardManagementServiceImpl.class);
    private final String ENTITY_NAME = "rs_inoutWard";

    private final RsInoutWardRepository rsInoutWardRepository;

    private final McPaymentRepository mcPaymentRepository;

    private final McReceiptRepository mcReceiptRepository;

    private final UserService userService;

    private final CustomerRepository customerRepository;
    private final BillRepository billRepository;
    private final EB88Producer eb88Producer;
    private final TaskLogRepository taskLogRepository;
    private final ProductProductUnitRepository productProductUnitRepository;
    private final ProductRepository productRepository;
    private final BusinessTypeRepository businessTypeRepository;
    private final ConfigRepository configRepository;
    private final TransactionTemplate transactionTemplate;

    public RsInoutWardManagementServiceImpl(
        RsInoutWardRepository rsInoutWardRepository,
        McPaymentRepository mcPaymentRepository,
        McReceiptRepository mcReceiptRepository,
        UserService userService,
        CustomerRepository customerRepository,
        BillRepository billRepository,
        EB88Producer eb88Producer,
        TaskLogRepository taskLogRepository,
        ProductProductUnitRepository productProductUnitRepository,
        ProductRepository productRepository,
        BusinessTypeRepository businessTypeRepository,
        ConfigRepository configRepository,
        TransactionTemplate transactionTemplate
    ) {
        this.rsInoutWardRepository = rsInoutWardRepository;
        this.mcPaymentRepository = mcPaymentRepository;
        this.mcReceiptRepository = mcReceiptRepository;
        this.userService = userService;
        this.customerRepository = customerRepository;
        this.billRepository = billRepository;
        this.eb88Producer = eb88Producer;
        this.taskLogRepository = taskLogRepository;
        this.productProductUnitRepository = productProductUnitRepository;
        this.productRepository = productRepository;
        this.businessTypeRepository = businessTypeRepository;
        this.configRepository = configRepository;
        this.transactionTemplate = transactionTemplate;
    }

    @Override
    public ResultDTO getAllTransactions(
        Integer comId,
        String fromDate,
        String toDate,
        Integer type,
        String keyword,
        Boolean getWithPaging,
        Pageable pageable
    ) {
        userService.getUserWithAuthorities(comId);
        Common.checkDateTime(fromDate, Constants.ZONED_DATE_FORMAT);
        Common.checkDateTime(toDate, Constants.ZONED_DATE_FORMAT);
        String messageResponse;
        GetAllTransactionResponse response = new GetAllTransactionResponse();
        if (Objects.equals(type, Constants.RS_INWARD_TYPE)) {
            messageResponse = ResultConstants.RS_IN_WARD_TRANSACTION_SUCCESS;
        } else if (Objects.equals(type, Constants.RS_OUTWARD_TYPE)) {
            messageResponse = ResultConstants.RS_OUT_WARD_TRANSACTION_SUCCESS;
        } else {
            messageResponse = ResultConstants.RS_INOUT_WARD_TRANSACTION_SUCCESS;
        }

        int size = 0;
        Object result = rsInoutWardRepository.getAllTransactions(comId, fromDate, toDate, type, keyword, getWithPaging, pageable);
        List<RsInoutWardResponse> rsInoutWardList;
        if (getWithPaging != null && getWithPaging) {
            Page<RsInoutWardResponse> resultPage = (Page<RsInoutWardResponse>) result;
            rsInoutWardList = resultPage.getContent();
            size = (int) resultPage.getTotalElements();
        } else {
            rsInoutWardList = (List<RsInoutWardResponse>) result;
            size = rsInoutWardList.size();
        }
        if (rsInoutWardList.size() > 0) {
            RsInOutWardStatusResult rsInOutWardStatusResult = rsInoutWardRepository.getTotalAmountStatus(
                comId,
                fromDate,
                toDate,
                type,
                keyword
            );
            if (rsInOutWardStatusResult != null) {
                response.setInWardAmount(rsInOutWardStatusResult.getTotalAmountInWard());
                response.setOutWardAmount(rsInOutWardStatusResult.getTotalAmountOutWard());
            }
            response.setFromDate(fromDate);
            response.setToDate(toDate);
            response.setComId(comId);
            response.setInOutWardList(rsInoutWardList);
        }

        log.debug(ENTITY_NAME + "_getAllTransactions: " + messageResponse);
        return new ResultDTO(ResultConstants.SUCCESS, messageResponse, true, response, size);
    }

    @Override
    public ResultDTO getOneById(Integer id) {
        User user = userService.getUserWithAuthorities();
        GetOneByIdResponse response = rsInoutWardRepository.getOneById(id, user.getCompanyId());
        if (response != null) {
            List<GetOneByIdDetailResponse> detail = rsInoutWardRepository.getDetailByRsInoutWardId(id);
            if (!detail.isEmpty()) {
                response.setDetail(detail);
            }
        } else {
            throw new InternalServerException(
                ExceptionConstants.RS_INOUT_WARD_ID_NOT_FOUND_VI,
                ENTITY_NAME,
                ExceptionConstants.RS_INOUT_WARD_ID_NOT_FOUND
            );
        }
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.RS_INOUT_WARD_DETAIL_SUCCESS, true, response);
    }

    @Override
    public ResultDTO create(RsInOutWardCreateRequest request) throws Exception {
        log.debug(ENTITY_NAME + ": request to create");

        Integer comId = request.getComId();
        userService.getUserWithAuthorities(comId);
        Integer type = request.getType();
        RsInoutWard rsInoutWard = new RsInoutWard();

        rsInoutWard.setComId(comId);
        rsInoutWard.setDate(getDateTimeFromRequest(request.getDate()));
        if (Objects.equals(type, Constants.RS_INWARD_TYPE)) {
            rsInoutWard.setNo(userService.genCode(comId, Constants.NHAP_KHO));
            checkRequestCustomer(comId, request.getCustomerId(), CustomerConstants.Type.SUPPLIER);
            rsInoutWard.setSupplierId(request.getCustomerId());
            rsInoutWard.setSupplierName(request.getCustomerName());
            Integer businessTypeId = businessTypeRepository.getIdByComIdAndCode(comId, BusinessTypeConstants.RsInWard.IN_WARD);
            if (businessTypeId != null) {
                rsInoutWard.setBusinessTypeId(businessTypeId);
            }
        } else {
            rsInoutWard.setNo(userService.genCode(comId, Constants.XUAT_KHO));

            if (request.getBillId() != null && billRepository.countByIdAndComId(request.getBillId(), comId) != 1) {
                throw new InternalServerException(ExceptionConstants.BILL_NOT_EXIST_VI, ENTITY_NAME, ExceptionConstants.BILL_NOT_EXIST);
            }
            checkRequestCustomer(comId, request.getCustomerId(), CustomerConstants.Type.CUSTOMER);
            rsInoutWard.setCustomerId(request.getCustomerId());
            rsInoutWard.setCustomerName(request.getCustomerName());
        }
        rsInoutWard.setType(type);
        rsInoutWard.setTypeDesc(request.getTypeDesc());
        rsInoutWard.setBillId(request.getBillId());
        rsInoutWard.setQuantity(request.getQuantity());
        rsInoutWard.setAmount(request.getAmount());
        rsInoutWard.setDiscountAmount(request.getDiscountAmount());
        rsInoutWard.setCostAmount(request.getCostAmount());
        rsInoutWard.setTotalAmount(request.getTotalAmount());
        rsInoutWard.setDescription(request.getDescription());
        rsInoutWard.setPaymentMethod(request.getPaymentMethod());
        if (request.getBusinessType() != null) {
            Integer countBusinessType = businessTypeRepository.countByIdAndComId(request.getBusinessType(), comId);
            if (countBusinessType == null || countBusinessType == 0) {
                throw new InternalServerException(
                    ExceptionConstants.BUSINESS_TYPE_NOT_FOUND_VI,
                    ENTITY_NAME,
                    ExceptionConstants.BUSINESS_TYPE_NOT_FOUND
                );
            }
            rsInoutWard.setBusinessTypeId(request.getBusinessType());
        }

        List<RsInoutWardDetail> details = new ArrayList<>();
        List<RsInOutWardCreateRequest.DetailsRequest> requestsDetail = request.getDetails();
        // map theo detail request. 1 productCode sẽ có 1 list unitId
        Map<Integer, RsInOutWardCreateRequest.DetailsRequest> productProductUnitIdRequest = new HashMap<>();
        List<String> productCodesRequest = new ArrayList<>();
        requestsDetail.forEach(requestDetail -> {
            productCodesRequest.add(requestDetail.getProductCode());
            productProductUnitIdRequest.put(requestDetail.getProductProductUnitId(), requestDetail);
        });

        List<ProductProductUnitItems> productsResult = productProductUnitRepository.getProductProductUnitItems(
            comId,
            productCodesRequest,
            Constants.OVER_STOCK_CODE
        );
        Map<Integer, ProductProductUnitItems> productCheckOverStockMap = new HashMap<>();
        if (productsResult.isEmpty()) {
            throw new BadRequestAlertException(
                ExceptionConstants.RS_INOUT_WARD_PRODUCT_LIST_NOT_EXISTS_VI,
                ENTITY_NAME,
                ExceptionConstants.RS_INOUT_WARD_PRODUCT_LIST_NOT_EXISTS
            );
        }
        productsResult.forEach(product -> {
            productCheckOverStockMap.put(product.getProductProductUnitId(), product);
        });

        // kiểm tra danh sách sản phẩm
        for (Integer ppuIdRequest : productProductUnitIdRequest.keySet()) {
            String codeRequest = productProductUnitIdRequest.get(ppuIdRequest).getProductCode();
            Integer position = productProductUnitIdRequest.get(ppuIdRequest).getPosition();
            if (!productCheckOverStockMap.containsKey(ppuIdRequest)) {
                throw new InternalServerException(
                    ExceptionConstants.RS_INOUT_WARD_PRODUCT_LIST_NOT_EXISTS_VI +
                    ", mã sản phẩm: " +
                    codeRequest +
                    ", số thứ tự: " +
                    position,
                    ENTITY_NAME,
                    ExceptionConstants.RS_INOUT_WARD_PRODUCT_LIST_NOT_EXISTS
                );
            }
            Integer unitIdRequest = productProductUnitIdRequest.get(ppuIdRequest).getUnitId();

            ProductProductUnitItems result = productCheckOverStockMap.get(ppuIdRequest);
            String codeResult = result.getProductCode();
            Integer unitIdResult = result.getProductUnitId();

            if (!Objects.equals(unitIdRequest, unitIdResult)) {
                throw new InternalServerException(
                    ExceptionConstants.RS_INOUT_WARD_PRODUCT_UNIT_LIST_INVALID_VI +
                    ", mã sản phẩm: " +
                    codeRequest +
                    ", số thứ tự: " +
                    position,
                    ENTITY_NAME,
                    ExceptionConstants.RS_INOUT_WARD_PRODUCT_UNIT_LIST_INVALID
                );
            } else if (!Objects.equals(codeRequest, codeResult)) {
                throw new InternalServerException(
                    ExceptionConstants.RS_INOUT_WARD_PRODUCT_LIST_NOT_EXISTS_VI +
                    ", mã sản phẩm: " +
                    codeRequest +
                    ", số thứ tự: " +
                    position,
                    ENTITY_NAME,
                    ExceptionConstants.RS_INOUT_WARD_PRODUCT_LIST_NOT_EXISTS
                );
            }
        }

        List<Integer> unitIds = new ArrayList<>(productCheckOverStockMap.keySet());

        List<ProductProductUnit> productProductUnits = productProductUnitRepository.findAllByComIdAndIds(comId, unitIds);
        List<ProductProductUnit> unitsSave = new ArrayList<>();
        Map<Integer, BigDecimal> unitIdAndQuantityMap = new HashMap<>();
        for (RsInOutWardCreateRequest.DetailsRequest item : request.getDetails()) {
            Integer pid = productCheckOverStockMap.get(item.getProductProductUnitId()).getProductId();
            RsInoutWardDetail detail = new RsInoutWardDetail();
            BeanUtils.copyProperties(item, detail);
            detail.setProductId(pid);
            detail.setRsInoutWard(rsInoutWard);
            detail.setProductNormalizedName(Common.normalizedName(Arrays.asList(item.getProductName())));
            details.add(detail);
            unitIdAndQuantityMap.put(item.getProductProductUnitId(), item.getQuantity());
        }
        rsInoutWard.setRsInoutWardDetails(details);
        Map<Integer, BigDecimal> productPrimaryIdAndQuantityMap = new HashMap<>();
        // Cập nhật số lượng tồn kho ở bảng product_product_unit & inventory_count ở product trường hợp có đơn vị tính
        if (!unitIdAndQuantityMap.isEmpty()) {
            productProductUnits = productProductUnitRepository.findAllByComIdAndIds(comId, unitIds);
            MultiValuedMap<Integer, ProductProductUnit> unitIdAndProductUnitMap = new HashSetValuedHashMap<>();
            for (ProductProductUnit unit : productProductUnits) {
                unitIdAndProductUnitMap.put(unit.getProductId(), unit);
            }
            Set<Integer> unitIdAndProductUnitMapKeys = unitIdAndProductUnitMap.keySet();
            for (Integer productId : unitIdAndProductUnitMapKeys) {
                Collection<ProductProductUnit> units = unitIdAndProductUnitMap.get(productId);
                BigDecimal quantity;
                BigDecimal quantityTotal = BigDecimal.ZERO;
                for (ProductProductUnit unit : units) {
                    if (unit.getProductUnitId() != null && unitIdAndQuantityMap.containsKey(unit.getId())) {
                        if (unit.getIsPrimary()) {
                            quantity = unitIdAndQuantityMap.get(unit.getId());
                        } else {
                            if (!unit.getFormula()) {
                                quantity = unitIdAndQuantityMap.get(unit.getId()).multiply(unit.getConvertRate());
                            } else {
                                quantity = unitIdAndQuantityMap.get(unit.getId()).divide(unit.getConvertRate(), new MathContext(6));
                            }
                        }
                        // đã tính được tổng tồn kho mới theo ĐVT chính
                        quantityTotal = quantityTotal.add(quantity);
                    }
                }
                for (ProductProductUnit unit : units) {
                    ProductProductUnitItems productCheck = productCheckOverStockMap.get(unit.getId());
                    // sản phẩm không có đơn vị tính
                    if (unit.getProductUnitId() == null) {
                        BigDecimal onHandProductNotUnit;
                        if (Objects.equals(type, Constants.RS_INWARD_TYPE)) {
                            onHandProductNotUnit = unit.getOnHand().add(unitIdAndQuantityMap.get(unit.getId()));
                        } else {
                            checkOverStock(productCheck, unitIdAndQuantityMap.get(unit.getId()), type);
                            onHandProductNotUnit = unit.getOnHand().subtract(unitIdAndQuantityMap.get(unit.getId()));
                        }
                        unit.setOnHand(onHandProductNotUnit);
                        productPrimaryIdAndQuantityMap.put(unit.getProductId(), onHandProductNotUnit);
                        unitsSave.add(unit);
                        continue;
                    }
                    // sản phẩm có đơn vị tính
                    BigDecimal finalQuantity;
                    if (unit.getIsPrimary()) {
                        finalQuantity = quantityTotal;
                    } else {
                        if (unit.getFormula()) {
                            finalQuantity = quantityTotal.multiply(unit.getConvertRate());
                        } else {
                            finalQuantity = quantityTotal.divide(unit.getConvertRate(), new MathContext(6));
                        }
                        // check quá số lượng từng đvt
                        checkOverStock(productCheck, finalQuantity, type);
                    }
                    if (Objects.equals(type, Constants.RS_INWARD_TYPE)) {
                        unit.setOnHand(unit.getOnHand().add(finalQuantity));
                    } else {
                        unit.setOnHand(unit.getOnHand().subtract(finalQuantity));
                    }
                    if (unit.getIsPrimary()) {
                        // add sản phẩm có đvt chính vào map để set lại inventoryCount
                        checkOverStock(productCheck, finalQuantity, type);
                        productPrimaryIdAndQuantityMap.put(unit.getProductId(), unit.getOnHand());
                    }
                    unitsSave.add(unit);
                }
            }
        }

        List<Product> products = productRepository.findAllById(productPrimaryIdAndQuantityMap.keySet());
        products.forEach(product -> product.setInventoryCount(productPrimaryIdAndQuantityMap.get(product.getId())));
        // Send message to queue
        TaskLogSendQueue taskLogSendQueue = transactionTemplate.execute(status -> {
            try {
                String businessTypeCreateEb = null;
                rsInoutWard.setCustomerNormalizedName(
                    Common.normalizedName(
                        Arrays.asList(rsInoutWard.getNo(), request.getCustomerName(), String.valueOf(rsInoutWard.getTotalAmount()))
                    )
                );
                rsInoutWardRepository.save(rsInoutWard);
                productProductUnitRepository.saveAll(unitsSave);
                productRepository.saveAll(products);

                if (Objects.equals(type, Constants.RS_INWARD_TYPE)) {
                    businessTypeCreateEb = BusinessTypeConstants.RsInWard.IN_WARD;
                    insertToMcPayment(rsInoutWard, request.getPaymentMethod());
                } else if (Objects.equals(type, Constants.RS_OUTWARD_TYPE)) {
                    businessTypeCreateEb = BusinessTypeConstants.RsOutWard.OUT_WARD;
                    insertToMcReceipt(rsInoutWard, request.getPaymentMethod());
                }
                return createAndPublishQueueRsInWardTask(comId, rsInoutWard.getId(), businessTypeCreateEb);
            } catch (Exception e) {
                log.error("Can not create queue task for eb88 creating rsInOutWard: {}", e.getMessage());
            }
            return null;
        });
        if (taskLogSendQueue != null) {
            userService.sendTaskLog(taskLogSendQueue);
            return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.RS_IN_OUT_WARD_CREATE_SUCCESS, true);
        } else {
            return new ResultDTO(ResultConstants.FAIL, ResultConstants.RS_IN_OUT_WARD_CREATE_FAIL, false);
        }
    }

    private void checkOverStock(ProductProductUnitItems productCheck, BigDecimal onHand, Integer type) {
        if (
            type.equals(Constants.RS_OUTWARD_TYPE) &&
            productCheck.getInventoryTracking() &&
            productCheck.getOverStock().equals(Constants.OVER_STOCK_DEFAULT) &&
            productCheck.getOnHand().compareTo(onHand) < 0
        ) {
            throw new BadRequestAlertException(
                "Sản phẩm " +
                productCheck.getProductName() +
                " vượt quá số lượng tồn kho (tổng tồn kho hiện tại: " +
                Common.formatBigDecimal(productCheck.getOnHand()) +
                productCheck.getProductUnitName() +
                ")",
                ENTITY_NAME,
                ExceptionConstants.PRODUCT_IN_VALID
            );
        }
    }

    private ZonedDateTime getDateTimeFromRequest(String datetime) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.ZONED_DATE_TIME_FORMAT);
            if (Strings.isNullOrEmpty(datetime)) {
                return ZonedDateTime.now();
            } else {
                LocalDateTime localDateTime = LocalDateTime.parse(datetime, formatter);
                return ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
            }
        } catch (Exception ex) {
            throw new BadRequestAlertException(ExceptionConstants.DATE_INVALID_VI, ENTITY_NAME, ExceptionConstants.DATE_TIME_INVALID);
        }
    }

    /**
     * Lấy thông tin mã Code của khách hàng/nhà cung cấp
     * nếu type = 1 -> khách hàng; type = 2 -> nhà cung cấp
     */
    private void checkRequestCustomer(Integer comId, Integer id, Integer type) {
        List<Integer> customerType = new ArrayList<>(List.of(CustomerConstants.Type.CUSTOMER_AND_SUPPLIER));
        if (type.equals(CustomerConstants.Type.CUSTOMER)) {
            customerType.add(CustomerConstants.Type.CUSTOMER);
        } else {
            customerType.add(CustomerConstants.Type.SUPPLIER);
        }
        String code = customerRepository.getCodeByIdAndComIdAndType(id, comId, customerType);
        if (Strings.isNullOrEmpty(code)) {
            if (type.equals(CustomerConstants.Type.CUSTOMER)) {
                throw new InternalServerException(
                    ExceptionConstants.CUSTOMER_NOT_FOUND_VI,
                    ENTITY_NAME,
                    ExceptionConstants.CUSTOMER_NOT_FOUND
                );
            }
            if (type.equals(CustomerConstants.Type.SUPPLIER)) {
                throw new InternalServerException(
                    ExceptionConstants.SUPPLIER_NOT_FOUND_VI,
                    ENTITY_NAME,
                    ExceptionConstants.CUSTOMER_NOT_FOUND
                );
            }
        }
    }

    public void insertToMcPayment(RsInoutWard rsInoutWard, String paymentMethod) {
        McPayment payment = new McPayment();
        BeanUtils.copyProperties(rsInoutWard, payment);
        Integer businessTypeId = businessTypeRepository.getIdByComIdAndCode(
            rsInoutWard.getComId(),
            BusinessTypeConstants.RsInWard.MC_PAYMENT
        );
        if (businessTypeId != null) {
            payment.setBusinessTypeId(businessTypeId);
        }
        payment.setAmount(rsInoutWard.getTotalAmount());
        payment.setRsInoutWardId(rsInoutWard.getId());
        payment.setNo(userService.genCode(rsInoutWard.getComId(), Constants.PHIEU_CHI));
        payment.setId(null);
        payment.setTypeDesc(Constants.PHIEU_CHI_NHAP_HANG);
        payment.setCustomerId(rsInoutWard.getSupplierId());
        payment.setCustomerName(rsInoutWard.getSupplierName());
        payment.setPaymentMethod(paymentMethod);
        payment.setCustomerNormalizedName(Common.normalizedName(Arrays.asList(payment.getCustomerName())));
        payment.setDescription("Nhập hàng " + rsInoutWard.getNo());
        mcPaymentRepository.save(payment);
        log.info(ENTITY_NAME + "_create_Mc_Payment: " + ResultConstants.SUCCESS_CREATE);
    }

    public void insertToMcReceipt(RsInoutWard rsInoutWard, String paymentMethod) {
        McReceipt receipt = new McReceipt();
        BeanUtils.copyProperties(rsInoutWard, receipt);
        receipt.setRsInoutWardId(rsInoutWard.getId());
        receipt.setNo(userService.genCode(rsInoutWard.getComId(), Constants.PHIEU_THU));
        receipt.setId(null);
        receipt.setTypeDesc(Constants.PHIEU_THU_BAN_HANG);
        receipt.setPaymentMethod(paymentMethod);
        receipt.setCustomerNormalizedName(Common.normalizedName(Arrays.asList(receipt.getCustomerName())));
        receipt.setDescription("Xuất hàng " + rsInoutWard.getNo());
        mcReceiptRepository.save(receipt);
        log.info(ENTITY_NAME + "_create_Mc_Receipt: " + ResultConstants.SUCCESS_CREATE);
    }

    private TaskLogSendQueue createAndPublishQueueRsInWardTask(Integer comId, Integer rsId, String businessType) throws Exception {
        RsInOutWardTask task = new RsInOutWardTask();
        task.setComId(comId);
        task.setRsInOutWardId(rsId);
        task.setBusinessType(businessType);

        TaskLog taskLog = new TaskLog();
        taskLog.setComId(comId);
        taskLog.setStatus(TaskLogConstants.Status.PROCESSING);
        //New objectMapper because we need disable a feature
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.INDENT_OUTPUT);
        taskLog.setContent(objectMapper.writeValueAsString(task));
        taskLog.setType(TaskLogConstants.Type.EB_CREATE_RS_IN_OUT_WARD);
        taskLog = taskLogRepository.save(taskLog);
        //publish to queue
        eb88Producer.send(new TaskLogIdEnqueueMessage(taskLog.getId()));
        return new TaskLogSendQueue(taskLog.getId().toString(), taskLog.getType());
    }

    @Override
    public ResultDTO deleteByIdAndCode(RsInOutWardDeleteRequest request) {
        log.debug(ENTITY_NAME + ": Request to delete by id and code");
        Integer comId = request.getComId();
        userService.getUserWithAuthorities(comId);
        Optional<RsInoutWard> rsInoutWardOptional = rsInoutWardRepository.findByIdAndComId(request.getRsInoutwardId(), comId);
        if (rsInoutWardOptional.isEmpty()) {
            throw new InternalServerException(
                ExceptionConstants.RS_INOUT_WARD_INVALID_VI,
                ENTITY_NAME,
                ExceptionConstants.RS_INOUT_WARD_INVALID_CODE
            );
        }
        RsInoutWard rsInoutWard = rsInoutWardOptional.get();
        // Send message to queue
        TaskLogSendQueue taskLogSendQueue = transactionTemplate.execute(status -> {
            if (!Objects.equals(rsInoutWard.getNo(), request.getRsInoutwardCode())) {
                throw new InternalServerException(
                    ExceptionConstants.RS_INOUT_WARD_CODE_NOT_FOUND_VI,
                    ENTITY_NAME,
                    ExceptionConstants.RS_INOUT_WARD_CODE_NOT_FOUND
                );
            }

            if (Objects.equals(rsInoutWard.getType(), Constants.RS_INWARD_TYPE)) {
                Integer mcPaymentId = mcPaymentRepository.findIdByComIdAndRsInoutWardId(comId, rsInoutWard.getId());
                if (mcPaymentId == null) {
                    throw new InternalServerException(
                        ExceptionConstants.MC_PAYMENT_NOT_FOUND_VI,
                        ENTITY_NAME,
                        ExceptionConstants.MC_PAYMENT_NOT_FOUND
                    );
                }
                mcPaymentRepository.deleteById(mcPaymentId);
            } else if (Objects.equals(rsInoutWard.getType(), Constants.RS_OUTWARD_TYPE)) {
                Integer mcReceiptId = mcReceiptRepository.findIdByComIdAndRsInoutWardId(comId, rsInoutWard.getId());
                if (mcReceiptId == null) {
                    throw new InternalServerException(
                        ExceptionConstants.MC_RECEIPT_NOT_FOUND_VI,
                        ENTITY_NAME,
                        ExceptionConstants.MC_RECEIPT_NOT_FOUND
                    );
                }
                mcReceiptRepository.deleteById(mcReceiptId);
            }
            Integer rsEbId = rsInoutWard.getEbId();
            rsInoutWardRepository.delete(rsInoutWard);
            return deleteToEB(comId, rsEbId, request.getRsInoutwardCode());
        });
        if (taskLogSendQueue != null) {
            userService.sendTaskLog(taskLogSendQueue);
        }
        log.debug(ENTITY_NAME + "_delete: " + ResultConstants.RS_OUT_WARD_DELETE_SUCCESS);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.RS_OUT_WARD_DELETE_SUCCESS, true);
    }

    private TaskLogSendQueue deleteToEB(Integer comId, Integer id, String rsCode) {
        TaskLog taskLog = new TaskLog();
        Optional<String> valuepOptional = configRepository.getValueByComIdAndCode(comId, EasyInvoiceConstants.EB88_COM_ID);
        if (valuepOptional.isEmpty()) {
            throw new InternalServerException(
                ExceptionConstants.COMPANY_NOT_EXISTS_CODE_VI + " bên hệ thống EB88",
                ENTITY_NAME,
                ExceptionConstants.COMPANY_NOT_EXISTS_CODE + "_EB88"
            );
        }
        int comIdEB;
        try {
            comIdEB = Integer.parseInt(valuepOptional.get());
        } catch (NumberFormatException e) {
            throw new BadRequestAlertException(
                ExceptionConstants.COMPANY_ID_INVALID_VI + " bên hệ thống EB88",
                ENTITY_NAME,
                ExceptionConstants.COMPANY_ID_INVALID_CODE + "_EB88"
            );
        }
        taskLog.setComId(comId);
        taskLog.setStatus(TaskLogConstants.Status.PROCESSING);
        //New objectMapper because we need disable a feature
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.INDENT_OUTPUT);
        try {
            RsInOutWardDeleteRequest request = new RsInOutWardDeleteRequest();
            request.setComId(comIdEB);
            request.setRsInoutwardId(id);
            request.setRsInoutwardCode(rsCode);
            taskLog.setContent(objectMapper.writeValueAsString(request));
        } catch (JsonProcessingException e) {
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "TASK_LOG_CONTENT_INVALID");
        }
        taskLog.setType(TaskLogConstants.Type.EB_DELETE_RS_IN_OUT_WARD);
        taskLog = taskLogRepository.save(taskLog);
        //  publish to queue
        eb88Producer.send(new TaskLogIdEnqueueMessage(taskLog.getId()));
        return new TaskLogSendQueue(taskLog.getId().toString(), taskLog.getType());
    }
}
