package vn.softdreams.easypos.integration.easybooks88.queue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.easypos.config.Constants;
import vn.softdreams.easypos.constants.*;
import vn.softdreams.easypos.domain.*;
import vn.softdreams.easypos.dto.productUnit.GetUnitEBId;
import vn.softdreams.easypos.dto.productUnit.ProductUnitResponse;
import vn.softdreams.easypos.dto.rsinoutward.RsInOutWardDeleteRequest;
import vn.softdreams.easypos.dto.user.ChangePasswordRequest;
import vn.softdreams.easypos.dto.user.RegisterUserRequest;
import vn.softdreams.easypos.integration.easybooks88.api.EB88ApiClient;
import vn.softdreams.easypos.integration.easybooks88.api.dto.*;
import vn.softdreams.easypos.integration.easybooks88.queue.dto.*;
import vn.softdreams.easypos.repository.*;
import vn.softdreams.easypos.util.Common;
import vn.softdreams.easypos.util.Util;
import vn.softdreams.easypos.web.rest.errors.BadRequestAlertException;
import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;
import vn.softdreams.easypos.web.rest.errors.InternalServerException;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class EB88MessageHandler {

    private final Logger log = LoggerFactory.getLogger(EB88MessageHandler.class);

    private final TaskLogRepository taskLogRepository;
    private final ObjectMapper objectMapper;
    private final ProductRepository productRepository;
    private final ConfigRepository configRepository;
    private final EB88ApiClient eb88ApiClient;
    private final CustomerRepository customerRepository;
    private final BillRepository billRepository;
    private final McReceiptRepository mcReceiptRepository;
    private final McPaymentRepository mcPaymentRepository;
    private final RsInoutWardRepository rsInoutWardRepository;
    private final ProductUnitRepository productUnitRepository;
    private final ProductProductUnitRepository productProductUnitRepository;
    private final CompanyOwnerRepository companyOwnerRepository;
    private final ModelMapper modelMapper;
    private final CompanyRepository companyRepository;
    private final int OK_API_RESPONSE_STATUS = 1;
    private final String ENTITY_NAME = "EB88MessageHandler";

    public EB88MessageHandler(
        TaskLogRepository taskLogRepository,
        ObjectMapper objectMapper,
        ProductRepository productRepository,
        ConfigRepository configRepository,
        EB88ApiClient eb88ApiClient,
        CustomerRepository customerRepository,
        BillRepository billRepository,
        McReceiptRepository mcReceiptRepository,
        McPaymentRepository mcPaymentRepository,
        RsInoutWardRepository rsInoutWardRepository,
        ProductUnitRepository productUnitRepository,
        ProductProductUnitRepository productProductUnitRepository,
        CompanyRepository companyRepository,
        CompanyOwnerRepository companyOwnerRepository,
        ModelMapper modelMapper
    ) {
        this.taskLogRepository = taskLogRepository;
        this.objectMapper = objectMapper;
        this.productRepository = productRepository;
        this.configRepository = configRepository;
        this.eb88ApiClient = eb88ApiClient;
        this.customerRepository = customerRepository;
        this.billRepository = billRepository;
        this.mcReceiptRepository = mcReceiptRepository;
        this.mcPaymentRepository = mcPaymentRepository;
        this.rsInoutWardRepository = rsInoutWardRepository;
        this.productUnitRepository = productUnitRepository;
        this.productProductUnitRepository = productProductUnitRepository;
        this.companyOwnerRepository = companyOwnerRepository;
        this.modelMapper = modelMapper;
        this.companyRepository = companyRepository;
    }

    public void process(TaskLog taskLog) throws Exception {
        log.info("Processing taskLogId = {}, type = {}", taskLog.getId(), taskLog.getType());
        String type = taskLog.getType();
        //        TimeUnit.SECONDS.sleep(2);
        try {
            switch (type) {
                case TaskLogConstants.Type.EB_CREATE_MATERIAL_GOODS:
                    createMaterialGoods(taskLog);
                    break;
                case TaskLogConstants.Type.EB_UPDATE_MATERIAL_GOODS:
                    updateMaterialGoods(taskLog);
                    break;
                case TaskLogConstants.Type.EB_CREATE_ACC_OBJECT:
                    createAccountObject(taskLog);
                    break;
                case TaskLogConstants.Type.EB_UPDATE_ACC_OBJECT:
                    updateAccountingObject(taskLog);
                    break;
                case TaskLogConstants.Type.EB_CREATE_SAINVOICE:
                    createSAInvoice(taskLog);
                    break;
                case TaskLogConstants.Type.EB_CANCEL_SAINVOICE:
                    cancelSAInvoice(taskLog);
                    break;
                case TaskLogConstants.Type.EB_CREATE_PRODUCT_UNIT:
                    createProductUnit(taskLog);
                    break;
                case TaskLogConstants.Type.EB_ASYNC_PRODUCT_UNIT:
                    asyncProductUnit(taskLog);
                    break;
                case TaskLogConstants.Type.EB_CREATE_RS_IN_OUT_WARD:
                    createRsInOutWard(taskLog);
                    break;
                case TaskLogConstants.Type.EB_DELETE_RS_IN_OUT_WARD:
                    deleteRsInOutWard(taskLog);
                    break;
                case TaskLogConstants.Type.EB_SAVE_COMPANY_UNIT:
                    saveCompanyUnit(taskLog);
                    break;
                case TaskLogConstants.Type.EB_CREATE_ACCOUNT:
                    registerUser(taskLog);
                    break;
                case TaskLogConstants.Type.EB_FORGOT_PASSWORD:
                    forgotPassword(taskLog);
                    break;
                case TaskLogConstants.Type.EB_CHANGE_PASSWORD:
                    changePassword(taskLog);
                    break;
                default:
                    logIncorrectTask(taskLog, "Type is not valid");
                    break;
            }
        } catch (Exception e) {
            // Ghi log và lưu trạng thái, message xử lý lỗi xuống DB
            logIncorrectTask(taskLog, e.getMessage());
        }
    }

    private void asyncProductUnit(TaskLog taskLog) {
        int comId = taskLog.getComId();
        List<UnitEb88Response> unitEb88Responses = null;
        try {
            unitEb88Responses = eb88ApiClient.getUnits(comId);
            Map<String, Integer> mapUnit = new HashMap<>();
            for (UnitEb88Response unit : unitEb88Responses) {
                if (!Strings.isNullOrEmpty(unit.getUnitName())) {
                    mapUnit.put(unit.getUnitName().toLowerCase(), unit.getId());
                }
            }
            List<ProductUnit> productUnits = productUnitRepository.findAllProductUnitByComId(comId);
            int count = 0;
            for (ProductUnit itemProduct : productUnits) {
                if (!Strings.isNullOrEmpty(itemProduct.getName()) && mapUnit.containsKey(itemProduct.getName().toLowerCase())) {
                    count++;
                    itemProduct.setEbId(mapUnit.get(itemProduct.getName().toLowerCase()));
                }
            }
            if (count > 0) {
                productUnitRepository.saveAll(productUnits);
            }
            taskLog.setStatus(TaskLogConstants.Status.OK);
            taskLog.setErrorMessage("OK");
        } catch (Exception e) {
            e.printStackTrace();
            taskLog.setStatus(TaskLogConstants.Status.FAILED);
            taskLog.setErrorMessage(e.getMessage());
        }
        taskLogRepository.save(taskLog);
    }

    private void createProductUnit(TaskLog taskLog) throws Exception {
        ProductUnitTask task = objectMapper.readValue(taskLog.getContent(), ProductUnitTask.class);
        Integer productUnitId = Integer.parseInt(task.getProductUnitId());
        int comId = Integer.parseInt(task.getComId());
        Optional<ProductUnit> productUnitOptional = productUnitRepository.findByIdAndComId(productUnitId, comId);
        if (productUnitOptional.isEmpty()) throw new Exception("ProductUnit not found with id = " + productUnitId + ", comId = " + comId);
        ProductUnit productUnit = productUnitOptional.get();
        CreateUnitRequest request = new CreateUnitRequest();
        request.setUnitName(productUnit.getName());
        request.setUnitDescription(productUnit.getDescription());
        request.setActive(true);
        Long ebUnitId = eb88ApiClient.createNewUnit(comId, request);
        if (ebUnitId == null) {
            log.error("Create productUnit at EB88 failed");
            taskLog.setStatus(TaskLogConstants.Status.FAILED);
            taskLog.setErrorMessage("response unitId is null");
        } else {
            taskLog.setStatus(TaskLogConstants.Status.OK);
            taskLog.setErrorMessage("OK");
            //Update lai productUnit truong eb_id
            productUnit.setEbId(ebUnitId.intValue());
            productUnitRepository.save(productUnit);
        }
        taskLogRepository.save(taskLog);
    }

    private void logIncorrectTask(TaskLog taskLog, String message) {
        log.error("Process taskLog failed, taskLogId = {}, error = {}", taskLog.getId(), message);
        taskLog.setStatus(TaskLogConstants.Status.FAILED);
        taskLog.setErrorMessage(message);
        taskLogRepository.save(taskLog);
    }

    private void cancelSAInvoice(TaskLog taskLog) throws Exception {
        String content = taskLog.getContent();
        SAInvoiceTask task = objectMapper.readValue(content, SAInvoiceTask.class);
        Integer saInvoiceId = Integer.parseInt(task.getSaInvoiceId());
        int comId = Integer.parseInt(task.getComId());
        Optional<Bill> billOptional = billRepository.findByIdAndComId(saInvoiceId, comId);
        if (billOptional.isEmpty()) throw new Exception("Bill not found with id = " + saInvoiceId + ", comId = " + comId);
        Bill bill = billOptional.get();

        CancelSAInvoiceRequest request = new CancelSAInvoiceRequest();
        request.setCode(bill.getCode());
        Optional<Customer> customerOptional = customerRepository.findById(bill.getCustomerId());
        if (customerOptional.isEmpty()) throw new Exception("Not found customer with id = " + bill.getCustomerId());
        Customer customer = customerOptional.get();
        String customerCode = Strings.isNullOrEmpty(customer.getCode2()) ? customer.getCode() : customer.getCode2();
        request.setCustomerCode(customerCode);
        request.setDate(bill.getBillDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        CommonResponse response = eb88ApiClient.cancelSAInvoice(comId, request);
        handleResponse(taskLog, response);
    }

    private void createSAInvoice(TaskLog taskLog) throws Exception {
        String content = taskLog.getContent();
        SAInvoiceTask task = objectMapper.readValue(content, SAInvoiceTask.class);
        Integer saInvoiceId = Integer.parseInt(task.getSaInvoiceId());
        int comId = Integer.parseInt(task.getComId());
        Optional<Bill> billOptional = billRepository.findByIdAndComId(saInvoiceId, comId);
        if (billOptional.isEmpty()) throw new Exception("Bill not found with id = " + saInvoiceId + ", comId = " + comId);
        Bill bill = billOptional.get();
        CreateSAInvoiceRequest request = new CreateSAInvoiceRequest();
        request.setCode(bill.getCode());
        String billDate = bill.getBillDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        request.setDate(billDate);
        Optional<Customer> customerOptional = customerRepository.findById(bill.getCustomerId());
        if (customerOptional.isEmpty()) throw new Exception("Not found customer with id = " + bill.getCustomerId());
        Customer customer = customerOptional.get();
        String customerCode = Strings.isNullOrEmpty(customer.getCode2()) ? customer.getCode() : customer.getCode2();
        request.setCustomerCode(customerCode);
        int creator = bill.getCreator();
        //TODO: tương ứng khi tạo mới nhân viên cũng cần tạo code như thế này
        String employeeCode = comId + "_" + creator;
        request.setEmployeeCode(employeeCode);
        request.setTotalAmount(bill.getAmount());
        request.setTotalDiscountAmount(bill.getDiscountAmount());
        request.setTotalVAT(bill.getVatAmount());
        request.setTotalAll(bill.getTotalAmount());
        //check related MCReceipt
        Optional<McReceipt> mcReceiptOptional = mcReceiptRepository.findOneByBillId(saInvoiceId);
        mcReceiptOptional.ifPresent(mcReceipt -> request.setMcReceiptCode(mcReceipt.getNo()));
        // check related RSInwardOutward
        Optional<RsInoutWard> rsInoutWardOptional = rsInoutWardRepository.findOneByBillId(saInvoiceId);
        rsInoutWardOptional.ifPresent(rsInoutWard -> request.setRsInwardOutwardCode(rsInoutWard.getNo()));

        List<CreateSAInvoiceRequest.Detail> details = new ArrayList<>();
        List<BillProduct> billProducts = bill.getProducts();
        if (billProducts.size() == 0) throw new Exception("No product found with billId = " + saInvoiceId);
        List<ProductUnitResponse> units = productUnitRepository.findEbIdByBillId(bill.getId());
        Map<Integer, Integer> result = units.stream().collect(Collectors.toMap(ProductUnitResponse::getId, ProductUnitResponse::getEBId));
        for (BillProduct product : billProducts) {
            CreateSAInvoiceRequest.Detail detail = new CreateSAInvoiceRequest.Detail();
            detail.setCode(product.getProductCode());
            detail.setPosition(product.getPosition());
            detail.isPromotion(product.getFeature() == 2);
            //TODO: (hoangtd) unit_id tạm thời chưa truyền sang EB, chờ EB88 có tính năng thay đổi unit_id ngon nghẻ
            if (product.getUnitId() != null && result.containsKey(product.getUnitId())) {
                detail.setUnitId(result.get(product.getUnitId()).longValue());
            }
            detail.setQuantity(product.getQuantity());
            detail.setUnitPrice(product.getUnitPrice());
            detail.setAmount(product.getAmount());
            //TODO: tạm thời vẫn query trong for để tìm ra giá vốn, giá nhập tương ứng với Product
            Optional<BigDecimal> inPriceOpt = productRepository.findInPriceByProductId(product.getProductId());
            inPriceOpt.ifPresent(inPrice -> {
                detail.setoWPrice(inPrice);
                BigDecimal oWAmount = calculateAmount(inPrice.multiply(product.getQuantity()), comId);
                detail.setoWAmount(oWAmount);
            });
            detail.setDiscountAmount(product.getDiscountAmount());
            details.add(detail);
        }
        request.setDetail(details);

        CommonResponse response = eb88ApiClient.createSAInvoice(comId, request);
        handleResponse(taskLog, response);
    }

    private void updateAccountingObject(TaskLog taskLog) throws Exception {
        String content = taskLog.getContent();
        AccountingObjectTask task = objectMapper.readValue(content, AccountingObjectTask.class);
        Integer accObjectId = Integer.parseInt(task.getAccountingObjectId());
        int comId = Integer.parseInt(task.getComId());
        Optional<Customer> customerOptional = customerRepository.findOneByIdAndComId(accObjectId, comId);
        if (customerOptional.isEmpty()) throw new Exception("Customer not found with id = " + accObjectId + " and comId = " + comId);
        Customer customer = customerOptional.get();

        UpdateAccountingObjectRequest request = new UpdateAccountingObjectRequest();
        //        TODO: Tạm thời chưa dùng code2 ở giai đoạn 1
        String code = customer.getCode();
        //        String code = Strings.isNullOrEmpty(customer.getCode2()) ? customer.getCode() : customer.getCode2();
        if (Strings.isNullOrEmpty(code)) throw new Exception("Customer code is empty, customerId=" + accObjectId);
        request.setCode(code);
        request.setName(customer.getName());
        //        if (!Strings.isNullOrEmpty(customer.getPhoneNumber()))
        request.setPhone(customer.getPhoneNumber());
        //        if (!Strings.isNullOrEmpty(customer.getAddress()))
        request.setAddress(customer.getAddress());
        if (!customer.getActive()) request.setActive(false);

        List<UpdateAccountingObjectRequest> requestList = new ArrayList<>();
        requestList.add(request);
        CommonResponse response = eb88ApiClient.updateAccountingObject(comId, requestList);
        handleResponse(taskLog, response);
    }

    private void createAccountObject(TaskLog taskLog) throws Exception {
        String content = taskLog.getContent();
        AccountingObjectTask task = objectMapper.readValue(content, AccountingObjectTask.class);
        Integer accObjectId = Integer.parseInt(task.getAccountingObjectId());
        int comId = Integer.parseInt(task.getComId());
        Optional<Customer> customerOptional = customerRepository.findOneByIdAndComId(accObjectId, comId);
        if (customerOptional.isEmpty()) throw new Exception("Customer not found with id = " + accObjectId + " and comId = " + comId);
        Customer customer = customerOptional.get();

        CreateAccountingObjectRequest request = new CreateAccountingObjectRequest();
        switch (task.getType()) {
            case CustomerConstants.TypeName.CUSTOMER:
                request.setType(CustomerConstants.Type.CUSTOMER);
                break;
            case CustomerConstants.TypeName.SUPPLIER:
                request.setType(CustomerConstants.Type.SUPPLIER);
                break;
            case CustomerConstants.TypeName.CUSTOMER_AND_SUPPLIER:
                request.setType(CustomerConstants.Type.CUSTOMER_AND_SUPPLIER);
                break;
        }
        //        TODO: Tạm thời chưa dùng code2 ở giai đoạn 1
        String code = customer.getCode();
        //        String code = Strings.isNullOrEmpty(customer.getCode2()) ? customer.getCode() : customer.getCode2();
        if (Strings.isNullOrEmpty(code)) throw new Exception("Customer code is empty, customerId=" + accObjectId);
        request.setCode(code);
        request.setName(customer.getName());
        if (!Strings.isNullOrEmpty(customer.getPhoneNumber())) request.setPhone(customer.getPhoneNumber());
        if (!Strings.isNullOrEmpty(customer.getAddress())) request.setAddress(customer.getAddress());

        List<CreateAccountingObjectRequest> requestList = new ArrayList<>();
        requestList.add(request);
        CommonResponse response = eb88ApiClient.createAccountingObject(comId, requestList);
        handleResponse(taskLog, response);
    }

    private void updateMaterialGoods(TaskLog taskLog) throws Exception {
        String content = taskLog.getContent();
        MaterialGoodsTask task = objectMapper.readValue(content, MaterialGoodsTask.class);
        Integer productId = Integer.parseInt(task.getProductId());
        int comId = Integer.parseInt(task.getComId());
        Optional<Product> productOpt = productRepository.findByIdAndComId(productId, comId);
        if (productOpt.isEmpty()) throw new Exception("Product not found with productId = " + productId + " and comId = " + comId);
        Product product = productOpt.get();
        Integer unitEbId = null;
        if (product.getUnitId() != null) {
            unitEbId = productUnitRepository.findEbIdByIdAndComId(product.getUnitId(), comId);
        }
        UpdateMaterialGoodsRequest request = new UpdateMaterialGoodsRequest();
        //        TODO: Tạm thời chưa dùng code2 ở giai đoạn 1
        String code = product.getCode();
        //        String code = Strings.isNullOrEmpty(product.getCode2()) ? product.getCode() : product.getCode2();
        request.setCode(code);
        request.setName(product.getName());
        request.setSalePrice(product.getSalePrice());
        if (unitEbId != null && unitEbId > 0) {
            request.setUnitId(unitEbId.longValue());
            if (product.getPurchasePrice() != null) request.setPurchasePrice(product.getPurchasePrice());
        }
        // Số dư đầu kỳ cho EB88
        if (product.getInventoryTracking()) {
            request.setOpnQuantity(product.getInventoryCount());
            request.setOpnUnitPrice(product.getPurchasePrice());
            BigDecimal opnAmount = calculateAmount(product.getInventoryCount().multiply(product.getPurchasePrice()), comId);
            request.setOpnAmount(opnAmount);
        }
        if (!product.getActive()) request.setActive(false);
        request.setConvertUnit(processConversionUnit(product, comId));

        CommonResponse response = eb88ApiClient.updateMaterialGoods(comId, request);
        handleResponse(taskLog, response);
    }

    private void createMaterialGoods(TaskLog taskLog) throws Exception {
        //        TimeUnit.SECONDS.sleep(2);
        String content = taskLog.getContent();
        MaterialGoodsTask task = objectMapper.readValue(content, MaterialGoodsTask.class);
        Integer productId = Integer.parseInt(task.getProductId());
        int comId = Integer.parseInt(task.getComId());
        Optional<Product> productOpt = productRepository.findByIdAndComId(productId, comId);

        if (productOpt.isEmpty()) throw new Exception("Product not found with productId = " + productId + " and comId = " + comId);
        Product product = productOpt.get();

        Integer unitEbId = null;
        if (product.getUnitId() != null) {
            unitEbId = productUnitRepository.findEbIdByIdAndComId(product.getUnitId(), comId);
        }
        CreateMaterialGoodsRequest request = new CreateMaterialGoodsRequest();
        //        TODO: Tạm thời chưa dùng code2 ở giai đoạn 1
        String code = product.getCode();
        //        String code = Strings.isNullOrEmpty(product.getCode2()) ? product.getCode() : product.getCode2();
        request.setCode(code);
        request.setName(product.getName());
        request.setSalePrice(product.getSalePrice());

        //get repository tương ứng với công ty
        Optional<String> configOptional = configRepository.getValueByComIdAndCode(comId, ConfigCode.EB88_REPOSITORY_ID.getCode());
        if (configOptional.isEmpty()) throw new Exception("Not found EB88 repositoryId with comId = " + comId);
        request.setRepositoryId(Integer.parseInt(configOptional.get()));

        if (unitEbId != null && unitEbId > 0) request.setUnitId(unitEbId.longValue());
        if (product.getPurchasePrice() != null) request.setPurchasePrice(product.getPurchasePrice());
        if (product.getInventoryTracking() != null && product.getInventoryTracking()) {
            request.setOpnQuantity(product.getInventoryCount());

            request.setOpnUnitPrice(product.getPurchasePrice());
            BigDecimal opnAmount = product.getInventoryCount().multiply(product.getPurchasePrice());
            request.setOpnAmount(calculateAmount(opnAmount, comId));
        }
        request.setConvertUnit(processConversionUnit(product, comId));

        // Gọi API đến EB88 tạo VTHH
        CreateMaterialGoodResponse response = eb88ApiClient.createMaterialGoods(comId, request);
        // add eb_id
        log.error("idEb88" + response.getData());
        if (response.getData() != null) {
            product.setEbId(response.getData());
            productRepository.save(product);
        }
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setMessage(response.getMessage());
        commonResponse.setStatus(response.getStatus());
        handleResponse(taskLog, commonResponse);
    }

    private List<ConversionUnitRequest> processConversionUnit(Product product, Integer comId) {
        int position = 0;
        List<ConversionUnitRequest> units = new ArrayList<>();
        List<ProductProductUnit> productUnits = productProductUnitRepository.findByProductIdAndComIdAndIsPrimaryFalse(
            product.getId(),
            comId
        );
        for (ProductProductUnit unit : productUnits) {
            ConversionUnitRequest unitRequest = new ConversionUnitRequest();
            unitRequest.setCode(product.getCode());
            Integer ebUnitId = productUnitRepository.findEbIdByIdAndComId(product.getUnitId(), comId);
            if (ebUnitId == null || ebUnitId == 0) {
                continue;
            }
            unitRequest.setUnitId(ebUnitId);
            unitRequest.setConvertRate(unit.getConvertRate());
            unitRequest.setFormula(unit.getFormula() ? "/" : "*");
            unitRequest.setDescription(unit.getDescription());
            unitRequest.setSalePrice(unit.getSalePrice());
            unitRequest.setPosition(String.valueOf(position++));
            units.add(unitRequest);
        }
        return units;
    }

    private void handleResponse(TaskLog taskLog, CommonResponse response) {
        if (response.getStatus() != OK_API_RESPONSE_STATUS) {
            log.error("Create accountingObject (customer) at EB88 failed with message = {}", response.getMessage());
            taskLog.setStatus(TaskLogConstants.Status.FAILED);
            taskLog.setErrorMessage(response.getMessage());
        } else {
            taskLog.setStatus(TaskLogConstants.Status.OK);
            taskLog.setErrorMessage("OK");
        }
        taskLogRepository.save(taskLog);
    }

    /*
     * Làm tròn cho thành tiền theo cấu hình của công ty
     */
    private BigDecimal calculateAmount(BigDecimal rawAmount, Integer comId) {
        Optional<String> configOptional = configRepository.getValueByComIdAndCode(comId, ConfigCode.ROUND_SCALE_AMOUNT.getCode());
        int scale = 0;
        if (configOptional.isPresent()) scale = Integer.parseInt(configOptional.get());
        return Common.roundMoney(rawAmount, scale);
    }

    private void createRsInOutWard(TaskLog taskLog) throws Exception {
        RsInOutWardTask task = objectMapper.readValue(taskLog.getContent(), RsInOutWardTask.class);
        Integer rsId = task.getRsInOutWardId();
        Integer comId = task.getComId();
        Optional<RsInoutWard> rsInoutWardOptional = rsInoutWardRepository.findByIdAndComId(rsId, comId);
        if (rsInoutWardOptional.isEmpty()) {
            throw new InternalServerException(
                ExceptionConstants.RS_INOUT_WARD_CODE_NOT_FOUND_VI,
                "EB88MessageHandler",
                ExceptionConstants.RS_INOUT_WARD_CODE_NOT_FOUND
            );
        }
        RsInoutWard rsInoutWardEP = rsInoutWardOptional.get();

        RsInWardRequest rsInWardRequest = new RsInWardRequest();
        rsInWardRequest.setCode(rsInoutWardEP.getNo());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.ZONED_DATE_TIME_FORMAT);
        rsInWardRequest.setDate(rsInoutWardEP.getDate().format(formatter));
        Integer customerId = null;
        String mcPaymentNo;
        if (rsInoutWardEP.getType().equals(Constants.RS_INWARD_TYPE)) {
            customerId = rsInoutWardEP.getSupplierId();
            mcPaymentNo = mcPaymentRepository.findNoByComIdAndRsInoutWardId(comId, rsId);
        } else {
            customerId = rsInoutWardEP.getCustomerId();
            mcPaymentNo = mcReceiptRepository.findNoByComIdAndRsInoutWardId(comId, rsId);
        }
        if (
            !Strings.isNullOrEmpty(task.getBusinessType()) &&
            (
                task.getBusinessType().equals(BusinessTypeConstants.RsInWard.IN_WARD) ||
                task.getBusinessType().equals(BusinessTypeConstants.RsOutWard.OUT_WARD)
            )
        ) {
            if (Strings.isNullOrEmpty(mcPaymentNo)) {
                throw new InternalServerException(
                    ExceptionConstants.MC_PAYMENT_RECEIPT_NOT_FOUND_VI,
                    "EB88MessageHandler",
                    ExceptionConstants.MC_PAYMENT_RECEIPT_NOT_FOUND
                );
            }
            rsInWardRequest.setMcPaymentCode(mcPaymentNo);

            Optional<Customer> customerOptional = customerRepository.findOneByIdAndComId(customerId, comId);
            if (customerOptional.isEmpty()) {
                throw new InternalServerException(
                    ExceptionConstants.CUSTOMER_SUPPLIER_NOT_FOUND_VI,
                    "EB88MessageHandler",
                    ExceptionConstants.CUSTOMER_SUPPLIER_NOT_FOUND
                );
            }
            rsInWardRequest.setCustomerCode(customerOptional.get().getCode());
        }
        rsInWardRequest.setTotalAmount(rsInoutWardEP.getAmount());
        rsInWardRequest.setTotalDiscountAmount(rsInoutWardEP.getDiscountAmount());
        rsInWardRequest.setTotalInwardAmount(rsInoutWardEP.getTotalAmount());
        rsInWardRequest.setRsInwardOutwardCode(rsInoutWardEP.getNo());

        Map<Long, Long> unitMap = new HashMap<>();
        List<Integer> unitIds = rsInoutWardEP
            .getRsInoutWardDetails()
            .stream()
            .map(RsInoutWardDetail::getUnitId)
            .collect(Collectors.toList());
        List<GetUnitEBId> unitIdsResult = productUnitRepository.getUnitEBIds(comId, unitIds);
        unitIdsResult.forEach(unitEBId -> {
            unitMap.put(unitEBId.getId(), unitEBId.getUnitEBId());
        });

        List<String> productCodeErrors = new ArrayList<>();
        List<RsInWardRequest.RsInWardDetailRequest> rsInWardDetailRequests = new ArrayList<>();
        for (RsInoutWardDetail detail : rsInoutWardEP.getRsInoutWardDetails()) {
            RsInWardRequest.RsInWardDetailRequest rsInWardDetailRequest = new RsInWardRequest.RsInWardDetailRequest();
            rsInWardDetailRequest.setCode(detail.getProductCode());
            rsInWardDetailRequest.setPosition(detail.getPosition());
            rsInWardDetailRequest.setPromotion(false);
            rsInWardDetailRequest.setUnitId(unitMap.get(detail.getUnitId().longValue()));
            rsInWardDetailRequest.setQuantity(detail.getQuantity());
            rsInWardDetailRequest.setUnitPrice(detail.getUnitPrice());
            rsInWardDetailRequest.setAmount(detail.getAmount());
            rsInWardDetailRequest.setDiscountAmount(detail.getDiscountAmount());
            rsInWardDetailRequests.add(rsInWardDetailRequest);
        }
        rsInWardRequest.setDetail(rsInWardDetailRequests);
        RsInOutWardResponse response = eb88ApiClient.createRsInWard(comId, rsInWardRequest);
        if (response.getStatus() == 0) {
            log.error("Create RsInOutWard at EB88 failed");
            taskLog.setStatus(TaskLogConstants.Status.FAILED);
            taskLog.setErrorMessage("Error message: " + response.getMessage());
        } else if (response.getStatus() == 1) {
            taskLog.setStatus(TaskLogConstants.Status.OK);
            taskLog.setErrorMessage("OK");
            try {
                rsInoutWardEP.setEbId(Integer.parseInt(response.getData()));
                log.debug("Save RsInOutWard with EbId: " + response.getData());
                rsInoutWardRepository.save(rsInoutWardEP);
            } catch (NumberFormatException e) {
                throw new BadRequestAlertException(
                    "EB88 ERROR: " + ExceptionConstants.RS_INOUT_WARD_INVALID_ID_VI,
                    "EB88MessageHandler",
                    "EB88 ERROR: " + ExceptionConstants.RS_INOUT_WARD_INVALID_ID
                );
            }
        }
        taskLogRepository.save(taskLog);
    }

    private void deleteRsInOutWard(TaskLog taskLog) throws Exception {
        RsInOutWardDeleteRequest task = objectMapper.readValue(taskLog.getContent(), RsInOutWardDeleteRequest.class);
        task.setRsInoutwardCode(Constants.CODE_PREFIX_EP + task.getRsInoutwardCode());
        Integer rsEBId = task.getRsInoutwardId();
        Integer comId = taskLog.getComId();
        log.debug("DELETE RsInOutWard by rsEBId: {}, comId = {}", rsEBId, comId);
        // xóa bên EP trước nên bỏ qua bước check
        //        Optional<RsInoutWard> rsInoutWardOptional = rsInoutWardRepository.findByEbIdAndComId(rsEBId, comId);
        //        if (rsInoutWardOptional.isEmpty()) {
        //            throw new InternalServerException(
        //                "EB88MessageHandler: " + ExceptionConstants.RS_INOUT_WARD_CODE_NOT_FOUND_VI,
        //                "EB88MessageHandler",
        //                ExceptionConstants.RS_INOUT_WARD_CODE_NOT_FOUND
        //            );
        //        }
        CommonResponse response = eb88ApiClient.deleteRsInWard(comId, task);
        if (response.getStatus() == 0) {
            log.error("Delete RsInOutWard for EB88 failed");
            taskLog.setStatus(TaskLogConstants.Status.FAILED);
            taskLog.setErrorMessage("response deleteRsInOutWard: " + response.getMessage());
        } else if (response.getStatus() == 1) {
            taskLog.setStatus(TaskLogConstants.Status.OK);
            taskLog.setErrorMessage("OK");
        }
        taskLogRepository.save(taskLog);
    }

    private void saveCompanyUnit(TaskLog taskLog) throws Exception {
        CompanyUnitTask task = objectMapper.readValue(taskLog.getContent(), CompanyUnitTask.class);
        Integer comId = task.getComId();
        Boolean isNew = task.getIsNew();
        log.debug("Save CompanyUnit to EB88: comId = {}, isNew: {}", comId, isNew);

        CompanySaveRequest createEBRequest = new CompanySaveRequest();
        CompanySaveRequest.OrganizationUnitRequest organizationUnit = new CompanySaveRequest.OrganizationUnitRequest();

        Optional<Company> companyOptional = companyRepository.findById(comId);
        Integer comOwnerId = companyRepository.getCompanyOwnerIdById(comId);
        if (companyOptional.isEmpty() || Objects.equals(comOwnerId, null)) {
            throw new InternalServerException(
                ExceptionConstants.OWNER_ID_NOT_EXISTS_VI + " bên hệ thống EasyPos",
                "EB88MessageHandler",
                ExceptionConstants.OWNER_ID_NOT_EXISTS_CODE + "_EasyPos"
            );
        }
        Company company = companyOptional.get();

        Integer comOwnerIdEB = getOwnerIdEB(comOwnerId);
        organizationUnit.setParentID(comOwnerIdEB);
        organizationUnit.setId(company.getEbId());
        Integer count = companyRepository.countAllByOwner(comOwnerId);
        Integer comIdLoginEB = companyRepository.getIdByOwner(comOwnerId);
        if (isNew) {
            organizationUnit.setOrganizationUnitCode(Constants.CODE_PREFIX_EP + (count + 1));
        } else {
            organizationUnit.setOrganizationUnitCode(Constants.CODE_PREFIX_EP);
        }
        organizationUnit.setOrganizationUnitName(company.getName());
        organizationUnit.setAddress(company.getAddress());
        organizationUnit.setPhoneNumber(company.getPhone());
        String errorMessage = "";
        try {
            AccountResponse accountResponse = eb88ApiClient.getDataFromTokenEB(comIdLoginEB);
            organizationUnit.setUserID(accountResponse.getId());
            createEBRequest.setOrganizationUnit(organizationUnit);
            Object result = eb88ApiClient.saveCompany(createEBRequest, comIdLoginEB, isNew);
            if (result instanceof CompanySaveResponse) {
                CompanySaveResponse response = (CompanySaveResponse) result;
                Integer comIdEb = response.getOrganizationUnit().getId();
                if (
                    response.getOrganizationUnit().getId().equals(response.getOrganizationUnitOptionReport().getOrganizationUnitID()) &&
                    response.getOrganizationUnit().getParentID().equals(comOwnerIdEB)
                ) {
                    log.debug(ENTITY_NAME + "_createToEB88: " + ResultConstants.COMPANY_OWNER_CREATE_SUCCESS_VI);
                    company.setEbId(comIdEb);
                    companyRepository.save(company);
                    configRepository.save(
                        new Config(comId, EasyBookConstants.EB88_COM_ID, comIdEb.toString(), EasyBookConstants.EB88_COM_ID_VI)
                    );
                    taskLog.setStatus(TaskLogConstants.Status.OK);
                    taskLog.setErrorMessage("OK");
                } else {
                    errorMessage = result.toString();
                }
            } else {
                errorMessage = result.toString();
            }
        } catch (Exception exception) {
            errorMessage = exception.getMessage();
        }

        if (!Strings.isNullOrEmpty(errorMessage)) {
            log.error(ENTITY_NAME + errorMessage);
            taskLog.setStatus(TaskLogConstants.Status.FAILED);
            taskLog.setErrorMessage(ENTITY_NAME + errorMessage);
        }
        taskLogRepository.save(taskLog);
        // sync again EB_ASYNC_PRODUCT_UNIT
        Optional<TaskLog> taskLogAsyncProductUnit = taskLogRepository.findIdByComIdAndType(
            comId,
            TaskLogConstants.Type.EB_ASYNC_PRODUCT_UNIT
        );
        taskLogAsyncProductUnit.ifPresent(this::asyncProductUnit);
    }

    // get CompanyOwnerIdEB from Config and OwnerId
    private Integer getOwnerIdEB(Integer ownerId) {
        Integer requestComId = companyRepository.getIdByOwner(ownerId);
        if (requestComId == null) {
            throw new InternalServerException(
                ExceptionConstants.OWNER_ID_NOT_EXISTS_VI + " bên hệ thống EasyPos",
                ENTITY_NAME,
                ExceptionConstants.OWNER_ID_NOT_EXISTS_CODE + "_EasyPos"
            );
        }

        Optional<Config> optionalConfig = configRepository.findByCompanyIdAndCode(requestComId, Constants.EB88_COM_ID);
        if (optionalConfig.isEmpty() || Strings.isNullOrEmpty(optionalConfig.get().getValue())) {
            throw new InternalServerException(
                ExceptionConstants.OWNER_ID_NOT_EXISTS_VI + " bên hệ thống EB88",
                ENTITY_NAME,
                ExceptionConstants.OWNER_ID_NOT_EXISTS_CODE + "_EB88"
            );
        }
        try {
            return Integer.parseInt(optionalConfig.get().getValue());
        } catch (Exception e) {
            throw new InternalServerException(
                ExceptionConstants.COMPANY_ID_NOT_EXISTS_VI + " bên hệ thống EB88",
                ENTITY_NAME,
                ExceptionConstants.COMPANY_ID_NOT_EXISTS_CODE + "_EB88"
            );
        }
    }

    private void registerUser(TaskLog taskLog) throws Exception {
        RegisterUserTask task = objectMapper.readValue(taskLog.getContent(), RegisterUserTask.class);
        Integer comId = task.getComId();
        Integer userId = task.getUserId();
        log.debug("Register UserEB by ep_user_id: {}, comId: {}", userId, comId);

        Optional<RegisterUserRequest> requestFromDBOptional = companyOwnerRepository.getOwnerInfoByUserIdAndComId(userId, comId);

        if (requestFromDBOptional.isEmpty()) {
            log.error("Register User EB88 failed: thông tin đăng kí không tồn tại ở hệ thống EasyPos");
            taskLog.setStatus(TaskLogConstants.Status.FAILED);
            taskLog.setErrorMessage("Thông tin đăng kí không tồn tại ở hệ thống EasyPos");
        } else {
            RegisterUserRequest request = requestFromDBOptional.get();
            RegisterCompanyRequest registerCompanyRequest = modelMapper.map(request, RegisterCompanyRequest.class);
            String hashMD5 = Util.createMd5(
                registerCompanyRequest.getCompanyTaxCode() +
                registerCompanyRequest.getStartDate() +
                registerCompanyRequest.getEndDate() +
                EasyInvoiceConstants.KEY_HASH_MD5
            );
            registerCompanyRequest.setHash(hashMD5);
            registerCompanyRequest.setPassword(task.getPassword());
            registerCompanyRequest.setType(0);

            RegisterCompanyResponse response = registerUserHandler(registerCompanyRequest, eb88ApiClient);
            //        Sau khi tạo tài khaonr thành công thì insert data EB88 vào bảng config
            insertToConfigFromEB(comId, response);
            if (response.getStatus() == 0) {
                log.error("Register User EB88 failed");
                taskLog.setStatus(TaskLogConstants.Status.FAILED);
                taskLog.setErrorMessage("Response RegisterUserEB88: " + response.getMessage());
            } else if (response.getStatus() == 1) {
                taskLog.setStatus(TaskLogConstants.Status.OK);
                taskLog.setErrorMessage("OK");
            }
        }
        taskLogRepository.save(taskLog);
    }

    private void forgotPassword(TaskLog taskLog) throws Exception {
        ForgotPasswordTask task = objectMapper.readValue(taskLog.getContent(), ForgotPasswordTask.class);
        CommonResponse response = eb88ApiClient.forgotPassword(task.getUsername(), task.getPassword());
        if (response.getStatus() == 0) {
            log.error("Forgot Password EB88 failed");
            taskLog.setStatus(TaskLogConstants.Status.FAILED);
            taskLog.setErrorMessage("Response ForgotPasswordEB88: " + response.getMessage());
        } else if (response.getStatus() == 1) {
            taskLog.setStatus(TaskLogConstants.Status.OK);
            taskLog.setErrorMessage("OK");
        }
        taskLogRepository.save(taskLog);
    }

    private void changePassword(TaskLog taskLog) throws Exception {
        ChangePasswordTask task = objectMapper.readValue(taskLog.getContent(), ChangePasswordTask.class);
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setOldPassword(task.getOldPassword());
        request.setNewPassword(task.getNewPassword());
        request.setConfirmPassword(task.getNewPassword());
        request.setIsLogoutAll(Boolean.FALSE);
        CommonResponse response = eb88ApiClient.changePassword(task.getComId(), request);
        if (response.getStatus() == 0) {
            log.error("Change Password EB88 failed");
            taskLog.setStatus(TaskLogConstants.Status.FAILED);
            taskLog.setErrorMessage("Response ChangePasswordEB88: " + response.getMessage());
        } else if (response.getStatus() == 1) {
            taskLog.setStatus(TaskLogConstants.Status.OK);
            taskLog.setErrorMessage("OK");
        }
        taskLogRepository.save(taskLog);
    }

    private RegisterCompanyResponse registerUserHandler(RegisterCompanyRequest registerCompanyRequest, EB88ApiClient eb88ApiClient) {
        RegisterCompanyResponse response = null;
        try {
            response = eb88ApiClient.registerCompany(registerCompanyRequest);
        } catch (BadRequestAlertException exception) {
            if (exception.getErrorKey().equalsIgnoreCase("EB88_ERROR_TAX_CODE")) {
                registerCompanyRequest.setCompanyTaxCode(registerCompanyRequest.getCompanyTaxCode() + "_pos");
                registerCompanyRequest.setHash(
                    Util.createMd5(
                        registerCompanyRequest.getCompanyTaxCode() +
                        registerCompanyRequest.getStartDate() +
                        registerCompanyRequest.getEndDate() +
                        EasyInvoiceConstants.KEY_HASH_MD5
                    )
                );
                response = eb88ApiClient.registerCompany(registerCompanyRequest);
            }
        }
        return response;
    }

    private void insertToConfigFromEB(Integer companyId, RegisterCompanyResponse ebCompanyResponse) {
        List<Config> configs = new ArrayList<>();
        configs.add(
            new Config(
                companyId,
                EasyInvoiceConstants.EB88_COM_ID,
                ebCompanyResponse.getData().getEbCompanyId() != null ? ebCompanyResponse.getData().getEbCompanyId().toString() : null,
                EasyInvoiceConstants.EB88_COM_ID_VI
            )
        );
        configs.add(
            new Config(
                companyId,
                EasyInvoiceConstants.EB88_DEFAULT_USER,
                ebCompanyResponse.getData().getEbUsername(),
                EasyInvoiceConstants.EB88_DEFAULT_USER_VI
            )
        );
        configs.add(
            new Config(
                companyId,
                EasyInvoiceConstants.EB88_REPOSITORY_ID,
                ebCompanyResponse.getData().getEbRepositoryId() != null ? ebCompanyResponse.getData().getEbRepositoryId().toString() : null,
                EasyInvoiceConstants.EB88_REPOSITORY_ID_VI
            )
        );
        configRepository.saveAll(configs);
    }
}
