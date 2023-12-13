package vn.softdreams.easypos.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.easypos.config.Constants;
import vn.softdreams.easypos.constants.EasyInvoiceConstants;
import vn.softdreams.easypos.constants.PrintSettingConstant;
import vn.softdreams.easypos.constants.ProcessingAreaConstants;
import vn.softdreams.easypos.constants.ResultConstants;
import vn.softdreams.easypos.domain.*;
import vn.softdreams.easypos.dto.bill.BillDetailResponse;
import vn.softdreams.easypos.dto.bill.BillExtraConfig;
import vn.softdreams.easypos.dto.config.ExtraConfig;
import vn.softdreams.easypos.dto.printSetting.PrintSettingItemResponse;
import vn.softdreams.easypos.dto.printSetting.SavePrintSettingRequest;
import vn.softdreams.easypos.dto.printTemplate.*;
import vn.softdreams.easypos.dto.processingArea.ProductSetting;
import vn.softdreams.easypos.repository.*;
import vn.softdreams.easypos.service.PrintSettingManagementService;
import vn.softdreams.easypos.service.UserService;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.util.Common;
import vn.softdreams.easypos.web.rest.errors.BadRequestAlertException;
import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;
import vn.softdreams.easypos.web.rest.errors.InternalServerException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class PrintSettingManagementServiceImpl implements PrintSettingManagementService {

    private final UserService userService;
    private final CompanyRepository companyRepository;
    private final BillRepository billRepository;
    private final ConfigRepository configRepository;
    private final ProcessingAreaRepository processingAreaRepository;
    private final ProcessingAreaProductRepository processingAreaProductRepository;
    private final PrintTemplateRepository printTemplateRepository;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;
    private final PrintSettingRepository printSettingRepository;
    private final ProcessingRequestRepository processingRequestRepository;
    private final ProductProductUnitRepository productProductUnitRepository;
    private final String ENTITY_NAME = "print_setting";

    public PrintSettingManagementServiceImpl(
        UserService userService,
        CompanyRepository companyRepository,
        BillRepository billRepository,
        ConfigRepository configRepository,
        ProcessingAreaRepository processingAreaRepository,
        ProcessingAreaProductRepository processingAreaProductRepository,
        PrintTemplateRepository printTemplateRepository,
        ObjectMapper objectMapper,
        ModelMapper modelMapper,
        PrintSettingRepository printSettingRepository,
        ProcessingRequestRepository processingRequestRepository,
        ProductProductUnitRepository productProductUnitRepository
    ) {
        this.userService = userService;
        this.companyRepository = companyRepository;
        this.billRepository = billRepository;
        this.configRepository = configRepository;
        this.processingAreaRepository = processingAreaRepository;
        this.processingAreaProductRepository = processingAreaProductRepository;
        this.printTemplateRepository = printTemplateRepository;
        this.objectMapper = objectMapper;
        this.modelMapper = modelMapper;
        this.printSettingRepository = printSettingRepository;
        this.processingRequestRepository = processingRequestRepository;
        this.productProductUnitRepository = productProductUnitRepository;
    }

    @Override
    public ResultDTO savePrintTemplate(PrintTemplateRequest printTemplate) {
        User user = userService.getUserWithAuthorities();
        PrintTemplate print = new PrintTemplate();
        boolean isUpdate = printTemplate.getId() != null;
        Integer countCode = printTemplateRepository.countAllByCode(user.getCompanyId(), printTemplate.getId());
        if ((isUpdate && countCode > 1) || (!isUpdate && countCode > 0)) {
            throw new InternalServerException(
                ExceptionConstants.PRINT_TEMPLATE_CODE_DUPLICATE_VI,
                ExceptionConstants.PRINT_TEMPLATE_CODE_DUPLICATE,
                ExceptionConstants.PRINT_TEMPLATE_CODE_DUPLICATE
            );
        }
        BeanUtils.copyProperties(printTemplate, print);
        print.setComId(user.getCompanyId());
        printTemplateRepository.save(print);
        return new ResultDTO(ResultConstants.SUCCESS, isUpdate ? ResultConstants.SUCCESS_UPDATE : ResultConstants.SUCCESS_CREATE, true);
    }

    @Override
    public ResultDTO savePrintProcessing(List<ProcessingPrintSettingRequest> requests) {
        User user = userService.getUserWithAuthorities();
        if (requests == null || requests.isEmpty()) {
            return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_UPDATE, true);
        }
        Set<Integer> ids = new HashSet<>();
        Set<Integer> printTemplateIds = new HashSet<>();
        Map<Integer, ProcessingPrintSettingRequest> updateMap = new HashMap<>();
        for (ProcessingPrintSettingRequest request : requests) {
            updateMap.put(request.getProcessingAreaId(), request);
            ids.add(request.getProcessingAreaId());
            if (request.getPrintTemplateId() != null) {
                printTemplateIds.addAll(request.getPrintTemplateId());
            }
        }
        List<ProcessingArea> processingAreaList = processingAreaRepository.findByComIdAndIdIn(user.getCompanyId(), ids);
        if (processingAreaList.size() != ids.size()) {
            throw new BadRequestAlertException(ExceptionConstants.BAD_REQUEST_VI, ENTITY_NAME, ExceptionConstants.BAD_REQUEST);
        }
        List<PrintTemplate> printTemplates = printTemplateRepository.findByComIdAndIdIn(user.getCompanyId(), printTemplateIds);
        if (printTemplates.size() != printTemplateIds.size()) {
            throw new BadRequestAlertException(ExceptionConstants.BAD_REQUEST_VI, ENTITY_NAME, ExceptionConstants.BAD_REQUEST);
        }
        Map<Integer, PrintTemplate> printTemplateMap = new HashMap<>();
        for (PrintTemplate printTemplate : printTemplates) {
            printTemplateMap.put(printTemplate.getId(), printTemplate);
        }
        Map<Integer, String> pageSizeMap = printTemplates
            .stream()
            .collect(Collectors.toMap(PrintTemplate::getId, PrintTemplate::getPageSize));
        for (ProcessingPrintSettingRequest request : requests) {
            if (request.getPrintTemplateId() != null && !request.getPrintTemplateId().isEmpty()) {
                String pageSize = pageSizeMap.get(request.getPrintTemplateId().get(0));
                for (Integer printTemplateId : request.getPrintTemplateId()) {
                    if (pageSizeMap.containsKey(printTemplateId) && !pageSize.equals(pageSizeMap.get(printTemplateId))) {
                        Map<Integer, String> areaMap = processingAreaList
                            .stream()
                            .collect(Collectors.toMap(ProcessingArea::getId, ProcessingArea::getName));
                        throw new BadRequestAlertException(
                            ExceptionConstants.PRINT_SETTING_INVALID_VI.replace("@@name", areaMap.get(request.getProcessingAreaId())),
                            ENTITY_NAME,
                            ExceptionConstants.PRINT_SETTING_INVALID
                        );
                    }
                }
            }
        }
        printSettingRepository.deleteByComIdAndProcessingAreaId(user.getCompanyId(), ids);
        List<PrintSetting> printSettings = new ArrayList<>();
        for (Map.Entry<Integer, ProcessingPrintSettingRequest> entry : updateMap.entrySet()) {
            ProcessingPrintSettingRequest request = entry.getValue();
            if (request.getPrinterInfo() != null && !request.getPrinterInfo().isEmpty()) {
                List<String> printerNames = request.getPrinterInfo();
                for (String printerName : printerNames) {
                    if (request.getPrintTemplateId() != null && !request.getPrintTemplateId().isEmpty()) {
                        for (Integer id : request.getPrintTemplateId()) {
                            PrintSetting setting = new PrintSetting();
                            setting.setComId(user.getCompanyId());
                            setting.setPrintName(printerName);
                            setting.setUserId(user.getId());
                            setting.setProcessingAreaId(entry.getKey());
                            setting.setNormalizedName(Common.normalizedName(List.of(printerName)));
                            if (printTemplateMap.containsKey(id)) {
                                PrintTemplate printTemplate = printTemplateMap.get(id);
                                setting.setPageSize(printTemplate.getPageSize());
                                setting.setTypeTemplate(printTemplate.getTypeTemplate());
                                setting.setPrintTemplateId(printTemplate.getId());
                            }
                            printSettings.add(setting);
                        }
                    } else {
                        PrintSetting setting = new PrintSetting();
                        setting.setComId(user.getCompanyId());
                        setting.setPrintName(printerName);
                        setting.setUserId(user.getId());
                        setting.setProcessingAreaId(entry.getKey());
                        setting.setNormalizedName(Common.normalizedName(List.of(printerName)));
                        printSettings.add(setting);
                    }
                }
            }
        }
        printSettingRepository.saveAll(printSettings);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_UPDATE, true);
    }

    @Override
    public ResultDTO getAllPrintTemplate() {
        User user = userService.getUserWithAuthorities();
        List<PrintTemplate> printConfigList = printTemplateRepository.findAllByComId(user.getCompanyId());
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_LOAD_DATA, true, printConfigList, printConfigList.size());
    }

    @Override
    public ResultDTO getPrintTemplateById(Integer id) {
        User user = userService.getUserWithAuthorities();
        Optional<PrintTemplate> printTemplate = printTemplateRepository.findByIdAndComId(id, user.getCompanyId());
        if (printTemplate.isPresent()) {
            return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_LOAD_DATA, true, printTemplate.get(), 1);
        }
        throw new InternalServerException(ResultConstants.ERROR_LOAD_DATA_VI, "findOnePrintConfig", ResultConstants.ERROR_LOAD_DATA);
    }

    private ExtraConfig getExtraConfig(Integer comId) {
        Optional<Config> configOptional = configRepository.findByCompanyIdAndCode(comId, Constants.EXTRA_CONFIG_CODE);
        if (configOptional.isPresent()) {
            Config config = configOptional.get();
            if (!Strings.isNullOrEmpty(config.getValue())) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    return objectMapper.readValue(config.getValue(), ExtraConfig.class);
                } catch (Exception exception) {
                    throw new BadRequestAlertException(
                        ExceptionConstants.CONFIG_EXT_BILL_INVALID_VI,
                        "print_setting",
                        ExceptionConstants.CONFIG_EXT_BILL_INVALID
                    );
                }
            }
        }
        return null;
    }

    @Override
    public ResultDTO getDataPrintTemplate(Integer comId, String code, Integer billId) throws JsonProcessingException {
        User user = userService.getUserWithAuthorities();
        Optional<PrintDataInfoCompany> CompanyOption = companyRepository.findInfoPrintById(user.getCompanyId());
        PrintDataResponse printDataResponse = new PrintDataResponse();
        if (CompanyOption.isPresent()) {
            printDataResponse.setCompanyName(CompanyOption.get().getName());
            printDataResponse.setAddress(CompanyOption.get().getAddress());
        }
        printDataResponse.setPhoneNumber(user.getPhoneNumber());
        Optional<Bill> billOptional = billRepository.findByIdAndComId(billId, user.getCompanyId());

        if (billOptional.isPresent()) {
            printDataResponse = getBillData(printDataResponse, billOptional.get(), billOptional.get().getProducts());
            return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_LOAD_DATA, true, printDataResponse);
        }
        return new ResultDTO(ResultConstants.ERROR, ResultConstants.ERROR, false);
    }

    private PrintDataResponse getDataDefault(PrintDataResponse printDataResponse) throws JsonProcessingException {
        List<String> defaultCodes = List.of(
            Constants.DEFAULT_PRODUCT_CODE,
            Constants.PRODUCT_CODE_NOTE_DEFAULT,
            Constants.PRODUCT_CODE_PROMOTION_DEFAULT,
            Constants.PRODUCT_CODE_SERVICE_CHARGE_DEFAULT
        );
        BillDetailResponse bill = objectMapper.readValue(Constants.BILL_DATA_DEFAULT, BillDetailResponse.class);
        BeanUtils.copyProperties(bill, printDataResponse);
        printDataResponse.setBillCode(bill.getCode());
        printDataResponse.setCreatedDate(ZonedDateTime.now());
        printDataResponse.setAreaUnitName(
            (bill.getAreaUnitName() != null ? bill.getAreaUnitName() : "") +
            (bill.getAreaUnitName() != null ? " - " : "") +
            (bill.getAreaName() != null ? bill.getAreaName() : "")
        );
        printDataResponse.setQrCode(bill.getTaxAuthorityCode());
        printDataResponse.setLookupCode(bill.getTaxAuthorityCode());
        PrintDataBillPayment billPayment = new PrintDataBillPayment();
        BeanUtils.copyProperties(bill.getPayment(), billPayment);
        printDataResponse.setBillPayment(billPayment);
        List<PrintDataBillProduct> billProducts = new ArrayList<>();
        for (BillDetailResponse.BillProductResponse productResponse : bill.getProducts()) {
            if (defaultCodes.contains(productResponse.getProductCode())) {
                continue;
            }
            BigDecimal displayTotalDiscount = BigDecimal.ZERO;
            BigDecimal displayVatAmount = BigDecimal.ZERO;
            BigDecimal displayDiscountAmount = BigDecimal.ZERO;
            PrintDataBillProduct billProduct = new PrintDataBillProduct();
            BeanUtils.copyProperties(productResponse, billProduct);
            if (productResponse.getTotalDiscount() != null) {
                displayTotalDiscount = displayTotalDiscount.add(productResponse.getTotalDiscount());
            }
            if (productResponse.getVatAmount() != null) {
                displayVatAmount = displayVatAmount.add(productResponse.getVatAmount());
            }
            if (productResponse.getDiscountAmount() != null) {
                displayDiscountAmount = displayDiscountAmount.add(productResponse.getDiscountAmount());
            }
            if (productResponse.getToppings() != null && !productResponse.getToppings().isEmpty()) {
                List<PrintDataTopping> toppingResponses = Arrays.asList(
                    modelMapper.map(productResponse.getToppings(), PrintDataTopping[].class)
                );
                billProduct.setToppings(toppingResponses);
                for (BillDetailResponse.BillProductToppingResponse itemTopping : productResponse.getToppings()) {
                    if (itemTopping.getTotalDiscount() != null) {
                        displayTotalDiscount = displayTotalDiscount.add(itemTopping.getTotalDiscount());
                    }
                    if (itemTopping.getVatAmount() != null) {
                        displayVatAmount = displayVatAmount.add(itemTopping.getVatAmount());
                    }
                    if (itemTopping.getDiscountAmount() != null) {
                        displayDiscountAmount = displayDiscountAmount.add(itemTopping.getDiscountAmount());
                    }
                }
            }
            billProduct.setDisplayTotalDiscount(displayTotalDiscount);
            billProduct.setDisplayVatAmount(displayVatAmount);
            billProduct.setDisplayDiscountAmount(displayDiscountAmount);
            billProduct.setProductAmount(
                billProduct
                    .getDisplayAmount()
                    .subtract(billProduct.getDisplayDiscountAmount())
                    .add(billProduct.getDisplayVatAmount())
                    .subtract(billProduct.getDisplayTotalDiscount())
            );
            billProducts.add(billProduct);
        }
        printDataResponse.setBillProducts(billProducts);
        return printDataResponse;
    }

    private PrintDataResponse getPrintDataResponse(
        PrintDataResponse printDataResponse,
        Bill bill,
        List<ProcessingRequestDetail> details,
        User user,
        Integer type
    ) {
        printDataResponse.setBillCode(bill.getCode());
        printDataResponse.setTaxAuthorityCode(bill.getTaxAuthorityCode());
        printDataResponse.setCreatedDate(bill.getBillDate());
        printDataResponse.setAreaUnitName(bill.getAreaUnitName());
        printDataResponse.setCustomerName(bill.getCustomerName());

        Map<Integer, List<PrintDataTopping>> toppingMap = new HashMap<>();
        Map<Integer, PrintDataTopping> deleteToppingMap = new HashMap<>();
        Map<Integer, PrintDataBillProduct> parentMap = new HashMap<>();
        List<PrintDataBillProduct> printDataBillProducts = new ArrayList<>();
        List<Integer> ids = details.stream().map(ProcessingRequestDetail::getProductProductUnitId).collect(Collectors.toList());
        List<ProductProductUnit> productProductUnits = productProductUnitRepository.findAllByComIdAndIdIn(user.getCompanyId(), ids);
        Map<Integer, ProductProductUnit> unitMap = new HashMap<>();
        for (ProductProductUnit unit : productProductUnits) {
            unitMap.put(unit.getId(), unit);
        }
        for (ProcessingRequestDetail item : details) {
            if (item.getRefId() == null || !item.getIsTopping()) {
                //                    Lấy danh sách sản phẩm không phải topping
                PrintDataBillProduct billProduct1 = new PrintDataBillProduct();
                if (parentMap.containsKey(item.getPosition())) {
                    billProduct1 = parentMap.get(item.getPosition());
                }
                billProduct1.setProductName(item.getProductName());
                BigDecimal quantity = item.getQuantity().compareTo(BigDecimal.ZERO) < 0
                    ? BigDecimal.ZERO.subtract(item.getQuantity())
                    : item.getQuantity();
                billProduct1.setQuantity((billProduct1.getQuantity() == null ? BigDecimal.ZERO : billProduct1.getQuantity()).add(quantity));
                if (unitMap.containsKey(item.getProductProductUnitId())) {
                    ProductProductUnit unit = unitMap.get(item.getProductProductUnitId());
                    billProduct1.setUnit(unit.getUnitName());
                    billProduct1.setUnitPrice(unit.getSalePrice());
                    billProduct1.setAmount(
                        billProduct1
                            .getQuantity()
                            .multiply(unit.getSalePrice() == null ? BigDecimal.ZERO : unit.getSalePrice())
                            .setScale(6, RoundingMode.HALF_UP)
                    );
                }
                billProduct1.setId(item.getId());
                billProduct1.setPosition(item.getPosition());
                printDataBillProducts.add(billProduct1);
                parentMap.put(billProduct1.getPosition(), billProduct1);
            } else {
                List<PrintDataTopping> toppingList = new ArrayList<>();
                if (toppingMap.containsKey(item.getRefId())) {
                    toppingList = toppingMap.get(item.getRefId());
                }
                PrintDataTopping printDataTopping = new PrintDataTopping();
                printDataTopping.setProductName(item.getProductName());
                printDataTopping.setProductProductUnitId(item.getProductProductUnitId());
                printDataTopping.setPosition(item.getPosition());
                printDataTopping.setQuantity(
                    item.getQuantity().compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO.subtract(item.getQuantity()) : item.getQuantity()
                );
                if (unitMap.containsKey(item.getProductProductUnitId())) {
                    ProductProductUnit unit = unitMap.get(item.getProductProductUnitId());
                    printDataTopping.setUnit(unit.getUnitName());
                    printDataTopping.setUnitPrice(unit.getSalePrice());
                    printDataTopping.setAmount(
                        printDataTopping.getQuantity().multiply(unit.getSalePrice()).setScale(6, RoundingMode.HALF_UP)
                    );
                }

                toppingList.add(printDataTopping);
                toppingMap.put(item.getRefId(), toppingList);
                if (type.equals(ProcessingAreaConstants.PrintType.HUY_MON)) {
                    if (deleteToppingMap.containsKey(printDataTopping.getPosition())) {
                        printDataTopping.setQuantity(
                            printDataTopping.getQuantity().add(deleteToppingMap.get(printDataTopping.getPosition()).getQuantity())
                        );
                        printDataTopping.setAmount(
                            printDataTopping.getQuantity().multiply(printDataTopping.getUnitPrice()).setScale(6, RoundingMode.HALF_UP)
                        );
                    }
                    deleteToppingMap.put(printDataTopping.getPosition(), printDataTopping);
                }
            }
        }
        if (type.equals(ProcessingAreaConstants.PrintType.HUY_MON) && !deleteToppingMap.isEmpty()) {
            List<ProductSetting> settingList = processingRequestRepository.getParentPosition(bill.getId());
            for (ProductSetting setting : settingList) {
                if (parentMap.containsKey(setting.getSetting())) {
                    PrintDataBillProduct parent = parentMap.get(setting.getSetting());
                    PrintDataTopping topping = deleteToppingMap.get(setting.getId());
                    List<PrintDataTopping> toppings = parent.getToppings();
                    if (toppings == null) {
                        toppings = new ArrayList<>();
                    }
                    toppings.add(topping);
                    parent.setToppings(toppings);
                    parentMap.put(parent.getPosition(), parent);
                }
            }
            printDataBillProducts = new ArrayList<>(parentMap.values());
        } else {
            for (PrintDataBillProduct item : printDataBillProducts) {
                if (toppingMap.containsKey(item.getId())) {
                    item.setToppings(toppingMap.get(item.getId()));
                }
            }
        }
        if (type.equals(ProcessingAreaConstants.PrintType.HUY_MON) && !printDataBillProducts.isEmpty()) {
            Map<Integer, PrintDataBillProduct> resultMap = new HashMap<>();
            for (PrintDataBillProduct product : printDataBillProducts) {
                PrintDataBillProduct result = product;
                if (resultMap.containsKey(result.getPosition())) {
                    result = resultMap.get(result.getPosition());
                    result.setQuantity(result.getQuantity().add(product.getQuantity()));
                    result.setAmount(result.getQuantity().multiply(result.getUnitPrice()).setScale(6, RoundingMode.HALF_UP));
                }
                resultMap.put(result.getPosition(), result);
            }
            printDataBillProducts = new ArrayList<>(resultMap.values());
        }
        for (PrintDataBillProduct item : printDataBillProducts) {
            if (item.getToppings() != null && !item.getToppings().isEmpty()) {
                for (PrintDataTopping topping : item.getToppings()) {
                    topping.setDisplayQuantity(topping.getQuantity().divide(item.getQuantity(), 6, RoundingMode.HALF_UP));
                }
            }
        }
        printDataResponse.setBillProducts(printDataBillProducts);
        return printDataResponse;
    }

    private PrintDataResponse getBillData(PrintDataResponse printDataResponse, Bill bill, List<BillProduct> billProduct)
        throws JsonProcessingException {
        User user = userService.getUserWithAuthorities();
        printDataResponse.setBillCode(bill.getCode());
        printDataResponse.setTaxAuthorityCode(bill.getTaxAuthorityCode());
        printDataResponse.setCreatedDate(bill.getBillDate());
        printDataResponse.setAreaUnitName(
            (bill.getAreaUnitName() != null ? bill.getAreaUnitName() : "") +
            (bill.getAreaUnitName() != null ? " - " : "") +
            (bill.getAreaName() != null ? bill.getAreaName() : "")
        );
        printDataResponse.setCustomerName(bill.getCustomerName());
        printDataResponse.setAmount(bill.getAmount());
        printDataResponse.setDiscountAmount(bill.getDiscountAmount());
        printDataResponse.setTotalPreTax(bill.getTotalPreTax());
        printDataResponse.setVatAmount(bill.getVatAmount());
        printDataResponse.setDiscountVatAmount(bill.getDiscountVatAmount());
        printDataResponse.setTotalAmount(bill.getTotalAmount());
        printDataResponse.setQrCode(bill.getTaxAuthorityCode());
        printDataResponse.setLookupCode(bill.getTaxAuthorityCode());
        printDataResponse.setDescription(bill.getDescription());
        if (printDataResponse.getDiscountVatAmount() != null && printDataResponse.getDiscountVatAmount().compareTo(BigDecimal.ZERO) > 0) {
            printDataResponse.setHaveDiscountVat(Boolean.TRUE);
        } else {
            printDataResponse.setHaveDiscountVat(Boolean.FALSE);
        }

        PrintDataBillPayment billPaymentData = new PrintDataBillPayment();
        BillPayment payment = bill.getPayment();
        billPaymentData.setAmount(payment.getAmount());
        billPaymentData.setPaymentMethod(payment.getPaymentMethod());
        billPaymentData.setRefund(payment.getRefund());
        printDataResponse.setBillPayment(billPaymentData);

        Map<Integer, List<PrintDataTopping>> toppingMap = new HashMap<>();
        List<PrintDataBillProduct> printDataBillProducts = new ArrayList<>();
        BillExtraConfig extraConfig = new BillExtraConfig();
        for (BillProduct item : billProduct) {
            if (
                item.getParentId() == null &&
                !item.getProductCode().equals("SPDV") &&
                !item.getProductCode().equals("SPGC") &&
                !item.getProductCode().equals("SPKM") &&
                !item.getProductCode().equals("SP1")
            ) {
                //                    Lấy danh sách sản phẩm không phải topping
                PrintDataBillProduct billProduct1 = new PrintDataBillProduct();
                billProduct1.setProductName(item.getProductName());
                billProduct1.setUnit(item.getUnit());
                billProduct1.setUnitPrice(item.getUnitPrice());
                billProduct1.setDiscountAmount(item.getDiscountAmount());
                billProduct1.setVatAmount(item.getVatAmount());
                billProduct1.setTotalAmount(item.getTotalAmount());
                billProduct1.setId(item.getId());
                billProduct1.setVatRate(item.getVatRate());
                billProduct1.setQuantity(item.getQuantity());
                billProduct1.setPosition(item.getPosition());
                ObjectMapper objectMapper = new ObjectMapper();
                Object extra = new Object();
                if (item.getExtra() != null && !item.getExtra().isEmpty()) {
                    extra = objectMapper.readValue(item.getExtra(), Object.class);
                }
                if (extra instanceof Map) {
                    Map<String, Object> extraMap = (Map<String, Object>) extra;
                    String discountVatRate = (String) extraMap.get("DiscountVatRate");
                    billProduct1.setDiscountVatRate(Integer.parseInt(discountVatRate));

                    String totalDiscount = (String) extraMap.get("TotalDiscount");
                    billProduct1.setTotalDiscount(new BigDecimal(totalDiscount));
                }
                billProduct1.setAmount(item.getAmount());
                printDataBillProducts.add(billProduct1);
            } else {
                List<PrintDataTopping> toppingList = new ArrayList<>();
                if (toppingMap.containsKey(item.getParentId())) {
                    toppingList = toppingMap.get(item.getParentId());
                }

                PrintDataTopping printDataTopping = new PrintDataTopping();
                printDataTopping.setParentId(item.getParentId());
                printDataTopping.setQuantity(item.getQuantity());
                printDataTopping.setAmount(item.getAmount());
                printDataTopping.setDiscountAmount(item.getDiscountAmount());
                printDataTopping.setUnit(item.getUnit());
                printDataTopping.setUnitPrice(item.getUnitPrice());
                printDataTopping.setTotalAmount(item.getTotalAmount());
                printDataTopping.setProductName(item.getProductName());
                printDataTopping.setVatRate(item.getVatRate());
                printDataTopping.setVatAmount(item.getVatAmount());
                printDataTopping.setPosition(item.getPosition());

                ObjectMapper objectMapper = new ObjectMapper();
                Object extra = new Object();
                if (item.getExtra() != null && !item.getExtra().isEmpty()) {
                    extra = objectMapper.readValue(item.getExtra(), Object.class);
                }
                if (extra instanceof Map) {
                    Map<String, Object> extraMap = (Map<String, Object>) extra;
                    String discountVatRate = (String) extraMap.get("DiscountVatRate");
                    printDataTopping.setDiscountVatRate(Integer.parseInt(discountVatRate));

                    String totalDiscount = (String) extraMap.get("TotalDiscount");
                    printDataTopping.setTotalDiscount(new BigDecimal(totalDiscount));
                }
                toppingList.add(printDataTopping);
                toppingMap.put(item.getParentId(), toppingList);
            }
            if (item.getVatRate() == 10) {
                if (extraConfig.getAmountVat10() == null) {
                    extraConfig.setAmountVat10(BigDecimal.ZERO);
                }
                extraConfig.setAmountVat10(extraConfig.getAmountVat10().add(item.getVatAmount()));
            }
            if (item.getVatRate() == 8) {
                if (extraConfig.getAmountVat8() == null) {
                    extraConfig.setAmountVat8(BigDecimal.ZERO);
                }
                extraConfig.setAmountVat8(extraConfig.getAmountVat8().add(item.getVatAmount()));
            }
            if (item.getProductCode().equals("SPDV")) {
                if (extraConfig.getSvc5() == null) {
                    extraConfig.setSvc5(BigDecimal.ZERO);
                }
                extraConfig.setSvc5(extraConfig.getSvc5().add(item.getTotalPreTax()));
            }
        }
        if (extraConfig.getTotalAmount() == null) {
            extraConfig.setTotalAmount(BigDecimal.ZERO);
        }
        extraConfig.setTotalAmount(printDataResponse.getTotalAmount());
        List<Config> printConfig = configRepository.getValueExtraConfig(user.getCompanyId());
        for (Config config : printConfig) {
            if (config.getCode().equals("extra_config") && !config.getValue().isEmpty()) {
                if (extraConfig.getAmountVat10() != null || extraConfig.getAmountVat8() != null || extraConfig.getSvc5() != null) {
                    printDataResponse.setExtraConfig(extraConfig);
                }
            }
            if (config.getCode().equals("easyinvoice_lookup") && !config.getValue().isEmpty()) {
                printDataResponse.setLookupLink(config.getValue());
            }
        }
        for (PrintDataBillProduct item : printDataBillProducts) {
            BigDecimal displayTotalDiscount = BigDecimal.ZERO;
            BigDecimal displayAmount = BigDecimal.ZERO;
            BigDecimal displayVatAmount = BigDecimal.ZERO;
            BigDecimal displayDiscountAmount = BigDecimal.ZERO;
            if (toppingMap.containsKey(item.getId())) {
                List<PrintDataTopping> toppings = toppingMap.get(item.getId());
                for (PrintDataTopping printDataTopping : toppings) {
                    printDataTopping.setDisplayQuantity(printDataTopping.getQuantity().divide(item.getQuantity(), 6, RoundingMode.HALF_UP));
                }
                toppings.sort(Comparator.comparing(PrintDataTopping::getPosition));
                item.setToppings(toppings);
            }
            if (item.getTotalDiscount() != null) {
                displayTotalDiscount = displayTotalDiscount.add(item.getTotalDiscount());
            }
            if (item.getAmount() != null) {
                displayAmount = displayAmount.add(item.getAmount());
            }
            if (item.getVatAmount() != null) {
                displayVatAmount = displayVatAmount.add(item.getVatAmount());
            }
            if (item.getDiscountAmount() != null) {
                displayDiscountAmount = displayDiscountAmount.add(item.getDiscountAmount());
            }
            if (item.getToppings() != null) {
                for (PrintDataTopping itemTopping : item.getToppings()) {
                    if (itemTopping.getTotalDiscount() != null) {
                        displayTotalDiscount = displayTotalDiscount.add(itemTopping.getTotalDiscount());
                    }
                    if (itemTopping.getAmount() != null) {
                        displayAmount = displayAmount.add(itemTopping.getAmount());
                    }
                    if (itemTopping.getVatAmount() != null) {
                        displayVatAmount = displayVatAmount.add(itemTopping.getVatAmount());
                    }
                    if (itemTopping.getDiscountAmount() != null) {
                        displayDiscountAmount = displayDiscountAmount.add(itemTopping.getDiscountAmount());
                    }
                }
            }
            item.setDisplayTotalDiscount(displayTotalDiscount);
            item.setDisplayAmount(displayAmount);
            item.setDisplayVatAmount(displayVatAmount);
            item.setDisplayDiscountAmount(displayDiscountAmount);
            item.setProductAmount(
                item
                    .getDisplayAmount()
                    .subtract(item.getDisplayDiscountAmount())
                    .add(item.getDisplayVatAmount())
                    .subtract(item.getDisplayTotalDiscount())
            );
        }
        printDataBillProducts.sort(Comparator.comparing(PrintDataBillProduct::getPosition));
        printDataResponse.setBillProducts(printDataBillProducts);
        return printDataResponse;
    }

    @Override
    public ResultDTO getDataPrintTemplateDefault(String code) throws JsonProcessingException {
        User user = userService.getUserWithAuthorities();
        PrintDataResponse printDataResponse = new PrintDataResponse();
        Optional<PrintDataInfoCompany> CompanyOption = companyRepository.findInfoPrintById(user.getCompanyId());
        if (CompanyOption.isPresent()) {
            printDataResponse.setCompanyName(CompanyOption.get().getName());
            printDataResponse.setAddress(CompanyOption.get().getAddress());
        }
        printDataResponse.setPhoneNumber(user.getPhoneNumber());
        Optional<String> valueOptional = configRepository.getValueByComIdAndCode(
            user.getCompanyId(),
            EasyInvoiceConstants.EASYINVOICE_LOOKUP
        );
        if (valueOptional.isPresent()) {
            printDataResponse.setLookupLink(valueOptional.get());
        }

        printDataResponse = getDataDefault(printDataResponse);
        PrintDataProcessingResponse response = new PrintDataProcessingResponse();
        response.setDataResponse(printDataResponse);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_LOAD_DATA, true, List.of(response));
    }

    @Override
    public ResultDTO getDefaultTemplate(Integer type) {
        User user = userService.getUserWithAuthorities();
        PrintTemplate printConfig = printTemplateRepository.findDefaultByCode(type);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_LOAD_DATA, true, printConfig, null);
    }

    @Override
    public ResultDTO getDataPrintByProcessingArea(Integer billId, Integer type) throws JsonProcessingException {
        User user = userService.getUserWithAuthorities();
        Optional<Bill> billOptional = billRepository.findByIdAndComId(billId, user.getCompanyId());
        if (billOptional.isEmpty()) {
            throw new BadRequestAlertException(ExceptionConstants.BILL_NOT_FOUND_VI, ENTITY_NAME, ExceptionConstants.BILL_NOT_FOUND);
        }
        Bill bill = billOptional.get();
        List<PrintDataProcessingResponse> responses = new ArrayList<>();
        if (type == null) {
            type = ProcessingAreaConstants.PrintType.TAO_DON;
        }
        if (type.equals(ProcessingAreaConstants.PrintType.TAO_DON)) {
            List<Integer> ids = bill
                .getProducts()
                .stream()
                .filter(billProduct -> billProduct.getParentId() == null)
                .map(BillProduct::getProductProductUnitId)
                .collect(Collectors.toList());
            Map<Integer, List<BillProduct>> areaMap = new HashMap<>();
            List<ProcessingAreaProduct> processingAreaProducts = processingAreaProductRepository.findByComIdAndProductProductUnitIdIn(
                user.getCompanyId(),
                ids
            );
            Map<Integer, Integer> processingAreaMap = processingAreaProducts
                .stream()
                .collect(Collectors.toMap(ProcessingAreaProduct::getProductProductUnitId, ProcessingAreaProduct::getProcessingAreaId));
            Map<Integer, List<BillProduct>> toppingMap = new HashMap<>();
            for (BillProduct billProduct : bill.getProducts()) {
                if (billProduct.getParentId() != null) {
                    List<BillProduct> billProducts = new ArrayList<>();
                    if (toppingMap.containsKey(billProduct.getParentId())) {
                        billProducts = toppingMap.get(billProduct.getParentId());
                    }
                    billProducts.add(billProduct);
                    toppingMap.put(billProduct.getParentId(), billProducts);
                }
            }
            for (BillProduct billProduct : bill.getProducts()) {
                if (billProduct.getParentId() == null && processingAreaMap.containsKey(billProduct.getProductProductUnitId())) {
                    List<BillProduct> billProducts = new ArrayList<>();
                    billProducts.add(billProduct);
                    if (toppingMap.containsKey(billProduct.getId())) {
                        billProducts.addAll(toppingMap.get(billProduct.getId()));
                    }
                    if (areaMap.containsKey(processingAreaMap.get(billProduct.getProductProductUnitId()))) {
                        billProducts.addAll(areaMap.get(processingAreaMap.get(billProduct.getProductProductUnitId())));
                    }
                    areaMap.put(processingAreaMap.get(billProduct.getProductProductUnitId()), billProducts);
                }
            }
            List<ProcessingArea> processingAreaList = processingAreaRepository.findByComIdAndIdIn(user.getCompanyId(), areaMap.keySet());
            Map<Integer, ProcessingArea> processingMap = new HashMap<>();
            for (ProcessingArea processingArea : processingAreaList) {
                processingMap.put(processingArea.getId(), processingArea);
            }
            List<PrintSetting> printSettings = printSettingRepository.findByComIdAndProcessingAreaIdInAndTypeTemplate(
                user.getCompanyId(),
                areaMap.keySet(),
                type
            );
            Map<Integer, List<String>> printSettingMap = new HashMap<>();
            List<Integer> printTemplateIds = new ArrayList<>();
            for (PrintSetting printSetting : printSettings) {
                List<String> printerInfos = new ArrayList<>();
                if (printSettingMap.containsKey(printSetting.getProcessingAreaId())) {
                    printerInfos = printSettingMap.get(printSetting.getProcessingAreaId());
                }
                printerInfos.add(printSetting.getPrintName());
                printSettingMap.put(printSetting.getProcessingAreaId(), printerInfos);
                printTemplateIds.add(printSetting.getPrintTemplateId());
            }
            List<PrintTemplate> printTemplates = printTemplateRepository.findByComIdAndTypeTemplateAndIdIn(
                user.getCompanyId(),
                type,
                printTemplateIds
            );
            for (Map.Entry<Integer, List<BillProduct>> entry : areaMap.entrySet()) {
                PrintDataProcessingResponse response = new PrintDataProcessingResponse();
                ProcessingArea processingArea = processingMap.get(entry.getKey());
                response.setProcessingAreaId(processingArea.getId());
                response.setProcessingAreaName(processingArea.getName());
                if (printSettingMap.containsKey(processingArea.getId())) {
                    response.setPrinterInfo(printSettingMap.get(processingArea.getId()));
                }
                if (!printTemplates.isEmpty()) {
                    List<PrintDataProcessingResponse.PrintTemplateResponse> printTemplateResponses = new ArrayList<>();
                    for (PrintTemplate printTemplate : printTemplates) {
                        PrintDataProcessingResponse.PrintTemplateResponse printTemplateResponse = new PrintDataProcessingResponse.PrintTemplateResponse();
                        BeanUtils.copyProperties(printTemplate, printTemplateResponse);
                        printTemplateResponses.add(printTemplateResponse);
                    }
                    response.setPrintTemplate(printTemplateResponses);
                }
                PrintDataResponse printDataResponse = new PrintDataResponse();
                Optional<PrintDataInfoCompany> CompanyOption = companyRepository.findInfoPrintById(user.getCompanyId());
                if (CompanyOption.isPresent()) {
                    printDataResponse.setCompanyName(CompanyOption.get().getName());
                    printDataResponse.setAddress(CompanyOption.get().getAddress());
                }
                printDataResponse.setPhoneNumber(user.getPhoneNumber());
                printDataResponse = getBillData(printDataResponse, bill, entry.getValue());
                response.setDataResponse(printDataResponse);
                responses.add(response);
            }
        } else {
            responses = getDataNotCreateBill(billId, bill, type);
        }
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_GET_LIST, true, responses);
    }

    private List<PrintDataProcessingResponse> getDataNotCreateBill(Integer billId, Bill bill, Integer type) {
        User user = userService.getUserWithAuthorities();
        Optional<ProcessingRequest> processingRequestOptional = processingRequestRepository.findLastByBillId(billId);
        if (processingRequestOptional.isEmpty()) {
            throw new BadRequestAlertException(ExceptionConstants.BILL_NOT_FOUND_VI, ENTITY_NAME, ExceptionConstants.BILL_NOT_FOUND);
        }
        ProcessingRequest processingRequest = processingRequestOptional.get();
        List<ProcessingRequestDetail> requestDetails = processingRequest.getDetails();
        List<ProcessingRequestDetail> details = new ArrayList<>();
        for (ProcessingRequestDetail detail : requestDetails) {
            if (type.equals(ProcessingAreaConstants.PrintType.HUY_MON) && detail.getQuantity().compareTo(BigDecimal.ZERO) < 0) {
                details.add(detail);
            } else if (type.equals(ProcessingAreaConstants.PrintType.CHE_BIEN) && detail.getQuantity().compareTo(BigDecimal.ZERO) > 0) {
                details.add(detail);
            }
        }
        List<Integer> ids = details
            .stream()
            //            .filter(detail -> !detail.getIsTopping())
            .map(ProcessingRequestDetail::getProductProductUnitId)
            .collect(Collectors.toList());
        Map<Integer, List<ProcessingRequestDetail>> areaMap = new HashMap<>();
        List<ProcessingAreaProduct> processingAreaProducts = processingAreaProductRepository.findByComIdAndProductProductUnitIdIn(
            user.getCompanyId(),
            ids
        );
        Map<Integer, Integer> processingAreaMap = processingAreaProducts
            .stream()
            .collect(Collectors.toMap(ProcessingAreaProduct::getProductProductUnitId, ProcessingAreaProduct::getProcessingAreaId));
        for (ProcessingRequestDetail detail : details) {
            if (processingAreaMap.containsKey(detail.getProductProductUnitId())) {
                List<ProcessingRequestDetail> billProducts = new ArrayList<>();
                billProducts.add(detail);
                if (areaMap.containsKey(processingAreaMap.get(detail.getProductProductUnitId()))) {
                    billProducts.addAll(areaMap.get(processingAreaMap.get(detail.getProductProductUnitId())));
                }
                areaMap.put(processingAreaMap.get(detail.getProductProductUnitId()), billProducts);
            }
        }
        if (areaMap.isEmpty() && !details.isEmpty()) {
            throw new BadRequestAlertException(
                ExceptionConstants.BILL_NOT_PROCESSING_AREA_VI,
                ENTITY_NAME,
                ExceptionConstants.BILL_NOT_PROCESSING_AREA
            );
        }
        List<PrintDataProcessingResponse> responses = new ArrayList<>();
        List<ProcessingArea> processingAreaList = processingAreaRepository.findByComIdAndIdIn(user.getCompanyId(), areaMap.keySet());
        Map<Integer, ProcessingArea> processingMap = new HashMap<>();
        for (ProcessingArea processingArea : processingAreaList) {
            processingMap.put(processingArea.getId(), processingArea);
        }
        List<PrintSetting> printSettings = printSettingRepository.findByComIdAndProcessingAreaIdInAndTypeTemplate(
            user.getCompanyId(),
            areaMap.keySet(),
            type
        );
        Map<Integer, Set<String>> printSettingMap = new HashMap<>();
        Map<Integer, List<Integer>> printTemplateMap = new HashMap<>();
        List<Integer> printIds = new ArrayList<>();
        for (PrintSetting printSetting : printSettings) {
            Set<String> printerInfos = new HashSet<>();
            if (printSettingMap.containsKey(printSetting.getProcessingAreaId())) {
                printerInfos = printSettingMap.get(printSetting.getProcessingAreaId());
            }
            printerInfos.add(printSetting.getPrintName());
            printSettingMap.put(printSetting.getProcessingAreaId(), printerInfos);

            List<Integer> printTemplateIds = new ArrayList<>();
            if (printTemplateMap.containsKey(printSetting.getProcessingAreaId())) {
                printTemplateIds = printTemplateMap.get(printSetting.getProcessingAreaId());
            }
            printTemplateIds.add(printSetting.getPrintTemplateId());
            printTemplateMap.put(printSetting.getProcessingAreaId(), printTemplateIds);
            printIds.add(printSetting.getPrintTemplateId());
        }
        List<PrintTemplate> printTemplates = printTemplateRepository.findByComIdAndTypeTemplateAndIdIn(user.getCompanyId(), type, printIds);
        Map<Integer, PrintTemplate> tmpMap = new HashMap<>();
        Map<Integer, List<PrintTemplate>> dataMap = new HashMap<>();
        for (PrintTemplate printTemplate : printTemplates) {
            tmpMap.put(printTemplate.getId(), printTemplate);
        }
        for (Map.Entry<Integer, List<Integer>> entry : printTemplateMap.entrySet()) {
            for (Integer id : entry.getValue()) {
                if (tmpMap.containsKey(id)) {
                    List<PrintTemplate> templates = dataMap.getOrDefault(entry.getKey(), new ArrayList<>());
                    templates.add(tmpMap.get(id));
                    dataMap.put(entry.getKey(), templates);
                }
            }
        }
        for (Map.Entry<Integer, List<ProcessingRequestDetail>> entry : areaMap.entrySet()) {
            PrintDataProcessingResponse response = new PrintDataProcessingResponse();
            ProcessingArea processingArea = processingMap.get(entry.getKey());
            response.setProcessingAreaId(processingArea.getId());
            response.setProcessingAreaName(processingArea.getName());
            if (printSettingMap.containsKey(processingArea.getId())) {
                response.setPrinterInfo(new ArrayList<>(printSettingMap.get(processingArea.getId())));
            }
            if (dataMap.containsKey(entry.getKey())) {
                List<PrintDataProcessingResponse.PrintTemplateResponse> printTemplateResponses = new ArrayList<>();
                for (PrintTemplate printTemplate : dataMap.get(entry.getKey())) {
                    PrintDataProcessingResponse.PrintTemplateResponse printTemplateResponse = new PrintDataProcessingResponse.PrintTemplateResponse();
                    BeanUtils.copyProperties(printTemplate, printTemplateResponse);
                    printTemplateResponses.add(printTemplateResponse);
                }
                response.setPrintTemplate(printTemplateResponses);
            }
            PrintDataResponse printDataResponse = new PrintDataResponse();
            BeanUtils.copyProperties(bill, printDataResponse);
            Optional<PrintDataInfoCompany> CompanyOption = companyRepository.findInfoPrintById(user.getCompanyId());
            if (CompanyOption.isPresent()) {
                printDataResponse.setCompanyName(CompanyOption.get().getName());
                printDataResponse.setAddress(CompanyOption.get().getAddress());
            }
            printDataResponse.setPhoneNumber(user.getPhoneNumber());
            printDataResponse = getPrintDataResponse(printDataResponse, bill, entry.getValue(), user, type);
            if (printDataResponse.getBillProducts() == null || printDataResponse.getBillProducts().isEmpty()) {
                continue;
            }
            response.setDataResponse(printDataResponse);
            responses.add(response);
        }
        return responses;
    }

    @Override
    public ResultDTO createPrintSetting(SavePrintSettingRequest request) {
        User user = userService.getUserWithAuthorities(request.getComId());
        boolean isNew = request.getId() == null;
        if (Objects.equals(request.getType(), PrintSettingConstant.Type.WIFI)) {
            if (Strings.isNullOrEmpty(request.getIpAddress())) {
                throw new BadRequestAlertException(
                    ExceptionConstants.PRINT_IP_ADDRESS_NOT_NULL_VI,
                    ENTITY_NAME,
                    ExceptionConstants.PRINT_IP_ADDRESS_NOT_NULL
                );
            }
            if (
                (isNew && printSettingRepository.countAllByComIdAndIpAddress(user.getCompanyId(), request.getIpAddress()) > 0) ||
                (
                    !isNew &&
                    printSettingRepository.countAllByComIdAndIpAddressAndIdNot(
                        user.getCompanyId(),
                        request.getIpAddress(),
                        request.getId()
                    ) >
                    0
                )
            ) {
                throw new BadRequestAlertException(
                    ExceptionConstants.PRINT_IP_ADDRESS_DUPLICATE_VI,
                    ENTITY_NAME,
                    ExceptionConstants.PRINT_IP_ADDRESS_DUPLICATE
                );
            }
        }
        request.setPrintName(request.getPrintName().trim());
        if (isNew) {
            if (printSettingRepository.countByComIdAndPrintNameAndTypeIsNotNull(user.getCompanyId(), request.getPrintName()) > 0) {
                throw new BadRequestAlertException(
                    ExceptionConstants.DUPLICATE_PRINT_NAME_VI,
                    ENTITY_NAME,
                    ExceptionConstants.DUPLICATE_PRINT_NAME
                );
            }
            PrintSetting setting = new PrintSetting();
            BeanUtils.copyProperties(request, setting);
            setting.setUserId(user.getId());
            setting.setNormalizedName(Common.normalizedName(List.of(request.getPrintName())));
            printSettingRepository.save(setting);
        } else {
            Optional<PrintSetting> optionalPrintSetting = printSettingRepository.findByIdAndComId(request.getId(), user.getCompanyId());
            if (optionalPrintSetting.isEmpty()) {
                throw new BadRequestAlertException(
                    ExceptionConstants.PRINT_SETTING_NOT_FOUND_VI,
                    ENTITY_NAME,
                    ExceptionConstants.PRINT_SETTING_NOT_FOUND
                );
            }
            if (
                printSettingRepository.countByComIdAndPrintNameAndIdNotAndTypeIsNotNull(
                    user.getCompanyId(),
                    request.getPrintName(),
                    request.getId()
                ) >
                0
            ) {
                throw new BadRequestAlertException(
                    ExceptionConstants.DUPLICATE_PRINT_NAME_VI,
                    ENTITY_NAME,
                    ExceptionConstants.DUPLICATE_PRINT_NAME
                );
            }
            PrintSetting setting = optionalPrintSetting.get();
            BeanUtils.copyProperties(request, setting);
            setting.setUserId(user.getId());
            setting.setNormalizedName(Common.normalizedName(List.of(request.getPrintName())));
            printSettingRepository.save(setting);
        }
        return new ResultDTO(
            ResultConstants.SUCCESS,
            isNew ? ResultConstants.CREATE_PRINT_SETTING_SUCCESS : ResultConstants.UPDATE_PRINT_SETTING_SUCCESS,
            true
        );
    }

    @Override
    public ResultDTO getWithPaging(Pageable pageable, String keyword) {
        User user = userService.getUserWithAuthorities();
        Page<PrintSettingItemResponse> page = printSettingRepository.getWithPaging(pageable, user.getCompanyId(), user.getId(), keyword);
        return new ResultDTO(
            ResultConstants.SUCCESS,
            ResultConstants.SUCCESS_GET_LIST,
            true,
            page.getContent(),
            (int) page.getTotalElements()
        );
    }

    @Override
    public ResultDTO getById(Integer id) {
        User user = userService.getUserWithAuthorities();
        Optional<PrintSetting> optionalPrintSetting = printSettingRepository.findByIdAndComId(id, user.getCompanyId());
        if (optionalPrintSetting.isEmpty()) {
            throw new BadRequestAlertException(
                ExceptionConstants.PRINT_SETTING_NOT_FOUND_VI,
                ENTITY_NAME,
                ExceptionConstants.PRINT_SETTING_NOT_FOUND
            );
        }
        PrintSettingItemResponse response = new PrintSettingItemResponse();
        BeanUtils.copyProperties(optionalPrintSetting.get(), response);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_GET_LIST, true, response);
    }

    @Override
    public ResultDTO delete(Integer id) {
        User user = userService.getUserWithAuthorities();
        Optional<PrintSetting> optionalPrintSetting = printSettingRepository.findByIdAndComId(id, user.getCompanyId());
        if (optionalPrintSetting.isEmpty()) {
            throw new BadRequestAlertException(
                ExceptionConstants.PRINT_SETTING_NOT_FOUND_VI,
                ENTITY_NAME,
                ExceptionConstants.PRINT_SETTING_NOT_FOUND
            );
        }
        printSettingRepository.delete(optionalPrintSetting.get());
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.DELETE_PRINT_SETTING_SUCCESS, true);
    }

    @Override
    public ResultDTO deletePrintTemplate(PrintTemplateDeleteRequest printTemplate) {
        User user = userService.getUserWithAuthorities();
        Optional<PrintTemplate> tempateById = printTemplateRepository.findByIdAndComId(printTemplate.getId(), printTemplate.getComId());
        if (tempateById.isPresent()) {
            PrintTemplate delete = tempateById.get();
            printTemplateRepository.delete(delete);
            return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.DELETE_PRINT_TEMPLATE_SUCCESS, true);
        }
        throw new InternalServerException(ResultConstants.ERROR_LOAD_DATA_VI, "findIdPrintConfig", ResultConstants.ERROR_FIND_DATA);
    }
}
