package vn.softdreams.easypos.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import vn.softdreams.easypos.config.Constants;
import vn.softdreams.easypos.constants.*;
import vn.softdreams.easypos.domain.*;
import vn.softdreams.easypos.dto.authorities.ConfigInvoice;
import vn.softdreams.easypos.dto.authorities.JwtDTO;
import vn.softdreams.easypos.dto.authorities.LoginEasyInvoice;
import vn.softdreams.easypos.dto.config.*;
import vn.softdreams.easypos.dto.invoice.*;
import vn.softdreams.easypos.dto.invoice.ngp.PatternNGPResponse;
import vn.softdreams.easypos.integration.easyinvoice.api.EasyInvoiceApiClient;
import vn.softdreams.easypos.integration.easyinvoice.dto.EasyInvoiceLoginResponse;
import vn.softdreams.easypos.repository.*;
import vn.softdreams.easypos.service.ConfigManagementService;
import vn.softdreams.easypos.service.UserService;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.util.Common;
import vn.softdreams.easypos.util.CommonIntegrated;
import vn.softdreams.easypos.web.rest.errors.BadRequestAlertException;
import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;
import vn.softdreams.easypos.web.rest.errors.InternalServerException;

import java.util.*;

/**
 * Service Implementation for managing {@link Config}.
 */
@Service
@Transactional
public class ConfigManagementServiceImpl implements ConfigManagementService {

    private final Logger log = LoggerFactory.getLogger(ConfigManagementServiceImpl.class);
    private final String ENTITY_NAME = "config";
    private final ConfigRepository configRepository;
    private final CompanyOwnerRepository companyOwnerRepository;
    private final UserService userService;
    private final PrintConfigRepository printConfigRepository;
    private final OwnerDeviceRepository ownerDeviceRepository;
    private final RestTemplate restTemplate;
    private final EasyInvoiceApiClient easyInvoiceApiClient;
    private final CompanyRepository companyRepository;
    private final BillRepository billRepository;

    public ConfigManagementServiceImpl(
        ConfigRepository configRepository,
        CompanyOwnerRepository companyOwnerRepository,
        UserService userService,
        PrintConfigRepository printConfigRepository,
        OwnerDeviceRepository ownerDeviceRepository,
        RestTemplate restTemplate,
        EasyInvoiceApiClient easyInvoiceApiClient,
        CompanyRepository companyRepository,
        BillRepository billRepository
    ) {
        this.configRepository = configRepository;
        this.companyOwnerRepository = companyOwnerRepository;
        this.userService = userService;
        this.printConfigRepository = printConfigRepository;
        this.ownerDeviceRepository = ownerDeviceRepository;
        this.restTemplate = restTemplate;
        this.easyInvoiceApiClient = easyInvoiceApiClient;
        this.companyRepository = companyRepository;
        this.billRepository = billRepository;
    }

    @Override
    public Config update(Config config) {
        log.debug("Request to update Config : {}", config);
        return configRepository.save(config);
    }

    @Override
    public Optional<Config> partialUpdate(Config config) {
        log.debug("Request to partially update Config : {}", config);

        return configRepository
            .findById(config.getId())
            .map(existingConfig -> {
                if (config.getCompanyId() != null) {
                    existingConfig.setCompanyId(config.getCompanyId());
                }
                if (config.getCode() != null) {
                    existingConfig.setCode(config.getCode());
                }
                if (config.getValue() != null) {
                    existingConfig.setValue(config.getValue());
                }
                if (config.getDescription() != null) {
                    existingConfig.setDescription(config.getDescription());
                }

                return existingConfig;
            })
            .map(configRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Config> findOne(Integer id) {
        log.debug("Request to get Config : {}", id);
        return configRepository.findById(id);
    }

    @Override
    public ResultDTO loginEasyInvoice(LoginEasyInvoice loginEasyInvoice) {
        User user = userService.getUserWithAuthorities();
        Integer ownerId = loginEasyInvoice.getCompanyOwnerId();
        Integer companyId;
        String taxCodeOwner;
        // TH: system_admin connect to EasyInvoice
        if (ownerId != null) {
            Optional<CompanyOwner> companyOwner = companyOwnerRepository.findById(ownerId);
            if (companyOwner.isEmpty()) {
                throw new BadRequestAlertException(
                    ExceptionConstants.OWNER_ID_NOT_EXISTS_VI,
                    ENTITY_NAME,
                    ExceptionConstants.OWNER_ID_NOT_EXISTS_CODE
                );
            }
            taxCodeOwner = companyOwner.get().getTaxCode();

            companyId = companyRepository.getIdByOwner(ownerId);
            if (companyId == null) {
                throw new BadRequestAlertException(
                    ExceptionConstants.OWNER_ID_NOT_EXISTS_VI,
                    ENTITY_NAME,
                    ExceptionConstants.OWNER_ID_NOT_EXISTS_CODE
                );
            }
        }
        // TH: client connect to EasyInvoice
        else {
            companyId = userService.getCompanyId();
            taxCodeOwner = user.getTaxCode();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Optional<String> configOptional = configRepository.getValueByComIdAndCode(companyId, EasyInvoiceConstants.EASYINVOICE_URL);
        LoginEasyInvoiceRequest request = new LoginEasyInvoiceRequest();
        request.setUsername(loginEasyInvoice.getUsername());
        request.setPassword(loginEasyInvoice.getPassword());
        request.setTaxcode(taxCodeOwner);
        String baseUrl;
        String lookUpUrl = "";
        JwtDTO jwtDTO = userService.getInfoJwt();
        if ((configOptional.isEmpty() || Strings.isNullOrEmpty(configOptional.get())) && Strings.isNullOrEmpty(loginEasyInvoice.getUrl())) {
            // Call api lay url
            CompanyUrlResponse companyUrlResponse = CommonIntegrated.getUrlByUserNameAndTaxCode(taxCodeOwner, restTemplate);
            baseUrl = formatBaseUrl(companyUrlResponse.getPublishDomain());
            lookUpUrl = companyUrlResponse.getPortalLink();
        } else {
            if (!Strings.isNullOrEmpty(loginEasyInvoice.getUrl())) {
                baseUrl = formatBaseUrl(loginEasyInvoice.getUrl());
                if (Strings.isNullOrEmpty(jwtDTO.getService())) {
                    CompanyUrlResponse companyUrlResponse = CommonIntegrated.getUrlByUserNameAndTaxCode(taxCodeOwner, restTemplate);
                    lookUpUrl = companyUrlResponse.getPortalLink();
                }
            } else {
                baseUrl = configOptional.get();
            }
        }

        if (Strings.isNullOrEmpty(jwtDTO.getService()) || jwtDTO.getService().equals(UserConstants.Service.NGP)) {
            //            if (Strings.isNullOrEmpty(jwtDTO.getService())) {
            //                if (!baseUrl.contains("softdreams") && !baseUrl.contains("easyinvoice")) {
            //                    throw new InternalServerException(
            //                        ExceptionConstants.SYSTEM_INVALID_ERROR_VI,
            //                        ENTITY_NAME,
            //                        ExceptionConstants.SYSTEM_INVALID_ERROR
            //                    );
            //                }
            //            } else if (jwtDTO.getService().equals(UserConstants.Service.NGP) && !baseUrl.contains("ngogiaphat")) {
            //                throw new InternalServerException(
            //                    ExceptionConstants.SYSTEM_INVALID_ERROR_VI,
            //                    ENTITY_NAME,
            //                    ExceptionConstants.SYSTEM_INVALID_ERROR
            //                );
            //            }
            Object loginResponse = easyInvoiceApiClient.checkLogin(baseUrl, request, headers);
            if (loginResponse instanceof LoginEasyInvoiceResponse) {
                if (!Objects.equals(((LoginEasyInvoiceResponse) loginResponse).getStatus(), "2")) throw new InternalServerException(
                    "Không thể kết nối đến hệ thống Hóa đơn điện tử. Thông tin đăng nhập không chính xác.",
                    "Không thể kết nối đến hệ thống Hóa đơn điện tử. Thông tin đăng nhập không chính xác.",
                    "LOGIN_EASY_INVOICE"
                );
            } else if (loginResponse instanceof EasyInvoiceLoginResponse) if (
                ((EasyInvoiceLoginResponse) loginResponse).getCode() != 200
            ) throw new InternalServerException(
                "Không thể kết nối đến hệ thống Hóa đơn điện tử. Thông tin đăng nhập không chính xác.",
                "Không thể kết nối đến hệ thống Hóa đơn điện tử. Thông tin đăng nhập không chính xác.",
                "LOGIN_EASY_INVOICE"
            );
        }
        List<String> invoiceCodes = new ArrayList<>();
        invoiceCodes.add(EasyInvoiceConstants.EASYINVOICE_ACCOUNT);
        invoiceCodes.add(EasyInvoiceConstants.EASYINVOICE_PASS);
        //        if (isConfigUrl) {
        invoiceCodes.add(EasyInvoiceConstants.EASYINVOICE_URL);
        invoiceCodes.add(EasyInvoiceConstants.EASYINVOICE_LOOKUP);
        //        }
        List<Config> configs = configRepository.getAllByCompanyID(companyId, invoiceCodes);

        Map<String, Config> mapConfig = new HashMap<>();
        for (Config config : configs) {
            mapConfig.put(config.getCode(), config);
        }
        configs = new ArrayList<>();
        if (mapConfig.containsKey(EasyInvoiceConstants.EASYINVOICE_ACCOUNT)) {
            Config configItem = mapConfig.get(EasyInvoiceConstants.EASYINVOICE_ACCOUNT);
            configItem.setValue(loginEasyInvoice.getUsername());
            configs.add(configItem);
        } else {
            configs.add(
                new Config(
                    companyId,
                    EasyInvoiceConstants.EASYINVOICE_ACCOUNT,
                    loginEasyInvoice.getUsername(),
                    EasyInvoiceConstants.EASYINVOICE_ACCOUNT_VI
                )
            );
        }

        if (mapConfig.containsKey(EasyInvoiceConstants.EASYINVOICE_PASS)) {
            Config configItem = mapConfig.get(EasyInvoiceConstants.EASYINVOICE_PASS);
            configItem.setValue(loginEasyInvoice.getPassword());
            configs.add(configItem);
        } else {
            configs.add(
                new Config(
                    companyId,
                    EasyInvoiceConstants.EASYINVOICE_PASS,
                    loginEasyInvoice.getPassword(),
                    EasyInvoiceConstants.EASYINVOICE_PASS_VI
                )
            );
        }

        if (mapConfig.containsKey(EasyInvoiceConstants.EASYINVOICE_URL)) {
            Config configItem = mapConfig.get(EasyInvoiceConstants.EASYINVOICE_URL);
            configItem.setValue(baseUrl);
            configs.add(configItem);
        } else {
            configs.add(new Config(companyId, EasyInvoiceConstants.EASYINVOICE_URL, baseUrl, EasyInvoiceConstants.EASYINVOICE_URL_VI));
        }
        if (mapConfig.containsKey(EasyInvoiceConstants.EASYINVOICE_LOOKUP)) {
            Config configItem = mapConfig.get(EasyInvoiceConstants.EASYINVOICE_LOOKUP);
            if (!Strings.isNullOrEmpty(lookUpUrl)) {
                configItem.setValue(lookUpUrl);
            }
            configs.add(configItem);
        } else {
            configs.add(
                new Config(companyId, EasyInvoiceConstants.EASYINVOICE_LOOKUP, lookUpUrl, EasyInvoiceConstants.EASYINVOICE_LOOKUP_VI)
            );
        }
        configRepository.saveAll(configs);
        if (user.getManager()) {
            addAllConfigOwnerToSubCompany(ownerId, companyId);
        }
        //        Sau khi login xong. Kiểm tra xem company đã có mã cơ quan thuế chưa. Nếu chưa thì call tiếp. Tự động lấy mã cơ quan thuế từ hddt
        String taxCode = companyOwnerRepository.getTaxMachineCodeCompanyIdAndTaxCode(user.getCompanyId());
        //        if (Strings.isNullOrEmpty(taxCode)) {
        if (Strings.isNullOrEmpty(jwtDTO.getService()) || !jwtDTO.getService().equals(UserConstants.Service.NGP)) {
            try {
                declarationSearch(new DeclarationRequest(1, ""));
            } catch (Exception ex) {
                log.error("Lay ma so thue khong thanh cong");
            }
        }
        //        }
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.CREATE_CONFIG_EI_SUCCESS_VI, true);
    }

    private void addAllConfigOwnerToSubCompany(Integer ownerId, Integer id) {
        List<Config> configs = configRepository.getAllByCompanyID(id, Common.getInvoiceConfigCodes());
        List<Config> configSave = new ArrayList<>();
        List<Integer> companyIds = companyRepository.findAllSubCompanyByOwnerId(ownerId);
        List<Config> configExists = configRepository.findByOwnerIdAndCodes(ownerId, Common.getInvoiceConfigCodes());
        for (Integer comId : companyIds) {
            for (Config config : configs) {
                configSave.add(new Config(comId, config.getCode(), config.getValue(), config.getDescription()));
            }
        }
        configRepository.deleteAllInBatch(configExists);
        configRepository.saveAll(configSave);
    }

    @Override
    @Transactional(readOnly = true)
    public ResultDTO getAllConfigs() {
        User user = userService.getUserWithAuthorities();
        ConfigInvoice configs = getConfigInvoiceByCompanyID(user);
        if (configs == null) {
            return new ResultDTO(ResultConstants.ERROR_CONFIG, ResultConstants.ERROR_CONFIG_VI, false);
        }
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.UPDATE_CONFIG_SUCCESS_VI, true, configs, 1);
    }

    @Override
    public ResultDTO registerAndPublish(RegisterEasyInvoiceRequest registerEasyInvoiceDTO) {
        User user = userService.getUserWithAuthorities();
        ConfigInvoice configInvoiceDTO = getConfigInvoiceByCompanyID(user);
        ResultDTO resultDTO = CommonIntegrated.registerAndPublish(
            registerEasyInvoiceDTO,
            user.getTaxCode(),
            configInvoiceDTO.getEasyInvoiceAccount(),
            configInvoiceDTO.getEasyInvoicePass(),
            restTemplate
        );
        return resultDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public ResultDTO declarationSearch(DeclarationRequest declarationDTO) {
        User user = userService.getUserWithAuthorities();
        ConfigInvoice configInvoiceDTO = getConfigInvoiceByCompanyID(user);
        String value = "";
        JwtDTO jwtDTO = userService.getInfoJwt();
        if (!Strings.isNullOrEmpty(jwtDTO.getService()) && jwtDTO.getService().equals(UserConstants.Service.NGP)) {
            List<DeclarationGiaPhatResponse> declarationSearch = CommonIntegrated.declarationSearchNgoGiaPhat(
                new DeclarationGiaPhatRequest(user.getTaxCode()),
                configInvoiceDTO.getEasyInvoiceAccount(),
                configInvoiceDTO.getEasyInvoicePass(),
                configInvoiceDTO.getEasyInvoiceUrl(),
                restTemplate
            );
            if (!declarationSearch.isEmpty()) {
                PatternNGPResponse data = CommonIntegrated.getPatternNgoGiaPhat(
                    new DeclarationGiaPhatRequest(user.getTaxCode()),
                    configInvoiceDTO.getEasyInvoiceAccount(),
                    configInvoiceDTO.getEasyInvoicePass(),
                    configInvoiceDTO.getEasyInvoiceUrl(),
                    restTemplate
                );
                if (data != null && data.getResponse() != null && !Strings.isNullOrEmpty(data.getResponse().getCode())) {
                    value = data.getResponse().getCode();
                }
            }
        } else {
            List<DeclarationResponse> declarationSearch = CommonIntegrated.declarationSearch(
                declarationDTO,
                user.getTaxCode(),
                configInvoiceDTO.getEasyInvoiceAccount(),
                configInvoiceDTO.getEasyInvoicePass(),
                configInvoiceDTO.getEasyInvoiceUrl(),
                restTemplate
            );
            if (!declarationSearch.isEmpty()) {
                for (DeclarationResponse item : declarationSearch) {
                    if (item.getCheckStatus().equals(5) && item.getCashRegister() && !Strings.isNullOrEmpty(item.getTaxAuthorityCode())) {
                        value = item.getTaxAuthorityCode();
                        break;
                    }
                }
            }
        }
        Integer id = companyOwnerRepository.findIdByCompanyID(user.getCompanyId());
        if (id != null && !Strings.isNullOrEmpty(value)) {
            companyOwnerRepository.taxMachineCode(id, value);
            return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_GET_LIST, true);
        }

        return new ResultDTO(ExceptionConstants.EXCEPTION_ERROR, ResultConstants.ERROR_LOAD_DATA_VI);
    }

    @Override
    @Transactional(readOnly = true)
    public ResultDTO getAllPrintConfigs(Integer comId, Integer type) {
        log.debug("Request to get all PrintConfigs");
        User user = userService.getUserWithAuthorities(comId);
        if (type == null) {
            type = 1;
        } else if (type == -1) {
            type = null;
        }
        List<PrintConfigCompany> printConfigList = printConfigRepository.findAllByCompanyID(user.getCompanyId(), type);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_LOAD_DATA, true, printConfigList, printConfigList.size());
    }

    @Override
    @Transactional(readOnly = true)
    public ResultDTO getRegisterInvoicePatterns(Integer comId) {
        User user = userService.getUserWithAuthorities(comId);
        InvoiceConfig configStoreDTO = getConfigStoreByCompanyID(user);
        if (configStoreDTO == null) {
            return new ResultDTO(ResultConstants.ERROR_CONFIG, ResultConstants.ERROR_CONFIG_VI, false);
        }
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_CONFIG_STORE, true, configStoreDTO);
    }

    @Override
    public ResultDTO updateInvoiceConfig(InvoiceConfig invoiceConfig) {
        User user = userService.getUserWithAuthorities(invoiceConfig.getComId());
        List<String> codes = Arrays.asList(
            EasyInvoiceConstants.TYPE_DISCOUNT,
            EasyInvoiceConstants.INVOICE_PATTERN,
            EasyInvoiceConstants.INVOICE_TYPE,
            EasyInvoiceConstants.INVOICE_METHOD,
            Constants.VOUCHER_APPLY_CODE,
            Constants.COMBINE_VOUCHER_APPLY_CODE,
            Constants.PUSH_VOUCHER_DISCOUNT_INVOICE_CODE,
            EasyInvoiceConstants.DISCOUNT_VAT,
            Constants.OVER_STOCK_CODE,
            Constants.TAX_REDUCTION_CODE,
            Constants.IS_BUYER_CODE,
            Constants.INV_DYNAMIC_DISCOUNT_NAME_CODE
        );
        List<Config> configs = configRepository.getAllByCompanyID(user.getCompanyId(), codes);
        List<Config> configList = new ArrayList<>();
        for (Config config : configs) {
            if (invoiceConfig.getTypeDiscount() != null && config.getCode().equals(EasyInvoiceConstants.TYPE_DISCOUNT)) {
                config.setValue(invoiceConfig.getTypeDiscount().toString());
                configList.add(config);
            } else if (
                !Strings.isNullOrEmpty(invoiceConfig.getInvoicePattern()) && config.getCode().equals(EasyInvoiceConstants.INVOICE_PATTERN)
            ) {
                config.setValue(invoiceConfig.getInvoicePattern());
                configList.add(config);
            } else if (invoiceConfig.getInvoiceType() != null && config.getCode().equals(EasyInvoiceConstants.INVOICE_TYPE)) {
                config.setValue(invoiceConfig.getInvoiceType().toString());
                configList.add(config);
            } else if (invoiceConfig.getInvoiceMethod() != null && config.getCode().equals(EasyInvoiceConstants.INVOICE_METHOD)) {
                config.setValue(invoiceConfig.getInvoiceMethod().toString());
                configList.add(config);
            } else if (invoiceConfig.getOverStock() != null && config.getCode().equals(Constants.OVER_STOCK_CODE)) {
                config.setValue(invoiceConfig.getOverStock().toString());
                configList.add(config);
            } else if (invoiceConfig.getTaxReductionType() != null && config.getCode().equals(Constants.TAX_REDUCTION_CODE)) {
                if (
                    invoiceConfig.getTaxReductionType() != Integer.parseInt(config.getValue()) &&
                    config.getValue().equals(ProductConstant.TAX_REDUCTION_TYPE.GIAM_TRU_CHUNG) &&
                    invoiceConfig.getTaxReductionType() == 1
                ) {
                    if (
                        billRepository.countAllByComIdAndStatusAndDiscountVatRateIsNotNull(
                            user.getCompanyId(),
                            BillConstants.Status.BILL_DONT_COMPLETE
                        ) >
                        0
                    ) {
                        throw new BadRequestAlertException(
                            ExceptionConstants.TAX_REDUCTION_BILL_VI,
                            ENTITY_NAME,
                            ExceptionConstants.TAX_REDUCTION_BILL_CODE
                        );
                    }
                }
                config.setValue(invoiceConfig.getTaxReductionType().toString());
                configList.add(config);
            } else if (invoiceConfig.getDiscountVat() != null && config.getCode().equals(EasyInvoiceConstants.DISCOUNT_VAT)) {
                config.setValue(invoiceConfig.getDiscountVat().toString());
                configList.add(config);
            } else if (invoiceConfig.getIsBuyer() != null && config.getCode().equals(Constants.IS_BUYER_CODE)) {
                config.setValue(invoiceConfig.getIsBuyer().toString());
                configList.add(config);
            } else if (
                invoiceConfig.getInvDynamicDiscountName() != null && config.getCode().equals(Constants.INV_DYNAMIC_DISCOUNT_NAME_CODE)
            ) {
                config.setValue(invoiceConfig.getInvDynamicDiscountName().toString());
                configList.add(config);
            } else if (invoiceConfig.getVoucherApply() != null && config.getCode().equals(Constants.VOUCHER_APPLY_CODE)) {
                config.setValue(invoiceConfig.getVoucherApply().toString());
                configList.add(config);
            } else if (invoiceConfig.getCombineVoucherApply() != null && config.getCode().equals(Constants.COMBINE_VOUCHER_APPLY_CODE)) {
                config.setValue(invoiceConfig.getCombineVoucherApply().toString());
                configList.add(config);
            } else if (
                invoiceConfig.getPushVoucherDiscountInvoice() != null &&
                config.getCode().equals(Constants.PUSH_VOUCHER_DISCOUNT_INVOICE_CODE)
            ) {
                config.setValue(invoiceConfig.getPushVoucherDiscountInvoice().toString());
                configList.add(config);
            }
        }
        configRepository.saveAll(configList);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_UPDATE, true);
    }

    @Override
    public ResultDTO updatePrintConfig(PrintConfigCompany printConfigCompany) {
        User user = userService.getUserWithAuthorities(printConfigCompany.getComId());
        PrintConfig printConfig = getPrintfConfig(printConfigCompany, user);
        if (printConfig == null) {
            return new ResultDTO(ResultConstants.ERROR, ResultConstants.ERROR_CONFIG_VI);
        }
        printConfigRepository.save(printConfig);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_UPDATE, true);
    }

    @Override
    public ResultDTO updateListPrintConfig(List<PrintConfigCompany> requests) {
        User user = userService.getUserWithAuthorities();
        List<PrintConfig> printConfigs = new ArrayList<>();
        for (PrintConfigCompany printConfigCompany : requests) {
            PrintConfig printConfig = getPrintfConfig(printConfigCompany, user);
            if (printConfig == null) {
                throw new BadRequestAlertException(
                    ExceptionConstants.PRINT_CONFIG_NOT_FOUND_VI,
                    ENTITY_NAME,
                    ExceptionConstants.PRINT_CONFIG_NOT_FOUND
                );
            }
            printConfigs.add(printConfig);
        }
        printConfigRepository.saveAll(printConfigs);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_UPDATE, true);
    }

    private PrintConfig getPrintfConfig(PrintConfigCompany printConfigCompany, User user) {
        Optional<PrintConfig> printConfigOptional = printConfigRepository.findByIdAndComId(printConfigCompany.getId(), user.getCompanyId());
        if (printConfigOptional.isPresent()) {
            PrintConfig printConfig = printConfigOptional.get();
            if (printConfigCompany.getValue().getIsPrint() != null) {
                printConfig.setIsPrint(printConfigCompany.getValue().getIsPrint() == 1);
            }
            if (printConfigCompany.getValue().getIsBold() != null) {
                printConfig.setIsBold(printConfigCompany.getValue().getIsBold() == 1);
            }
            if (printConfigCompany.getValue().getIsHeader() != null) {
                printConfig.setIsHeader(printConfigCompany.getValue().getIsHeader() == 1);
            }
            if (printConfigCompany.getValue().getIsEditable() != null) {
                printConfig.setIsEditable(printConfigCompany.getValue().getIsEditable() == 1);
            }
            if (!Strings.isNullOrEmpty(printConfigCompany.getValue().getContent())) {
                printConfig.setName(printConfigCompany.getValue().getContent());
            }
            if (printConfigCompany.getValue().getFontSize() != null) {
                printConfig.setFontSize(printConfigCompany.getValue().getFontSize());
            }
            if (printConfigCompany.getValue().getAlignText() != null) {
                printConfig.setAlignText(printConfigCompany.getValue().getAlignText());
            }
            return printConfig;
        } else {
            return null;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResultDTO getDeviceCodeByName(String taxCode, String name) {
        User user = userService.getUserWithAuthorities();
        DeviceCode deviceCodeResponse = new DeviceCode(taxCode, name);
        Optional<String> deviceCode = ownerDeviceRepository.getDeviceCodeByTaxCodeAndName(
            deviceCodeResponse.getTaxCode(),
            deviceCodeResponse.getName()
        );
        if (deviceCode.isPresent()) {
            deviceCodeResponse.setDeviceCode(deviceCode.get());
            return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_GET_LIST, true, deviceCodeResponse);
        }
        return new ResultDTO(
            ResultConstants.DEVICE_CODE_GET_LIST_ERROR,
            ResultConstants.DEVICE_CODE_GET_LIST_ERROR_VI,
            false,
            deviceCodeResponse
        );
    }

    @Override
    public ResultDTO registerOwnerDevice(DeviceCode deviceCodeRequest) {
        User user = userService.getUserWithAuthorities();

        OwnerDevice ownerDevice = new OwnerDevice();
        Optional<CompanyOwner> companyOwner = companyOwnerRepository.findByCompanyIdAndTaxCode(
            user.getCompanyId(),
            deviceCodeRequest.getTaxCode()
        );
        if (companyOwner.isPresent()) {
            Optional<String> deviceCode = ownerDeviceRepository.getDeviceCodeByOwnerIDAndCode(
                companyOwner.get().getId(),
                deviceCodeRequest.getName()
            );
            if (deviceCode.isPresent()) {
                deviceCodeRequest.setDeviceCode(deviceCode.get());
                return new ResultDTO(ResultConstants.DEVICE_CODE_ALREADY, ResultConstants.DEVICE_CODE_ALREADY_VI, true, deviceCodeRequest);
            }
            ownerDevice.setOwnerId(companyOwner.get().getId());
            ownerDevice.setDeviceCode(userService.genCodeByValue(user.getCompanyId(), Constants.DEVICE_CODE));
            ownerDevice.setName(deviceCodeRequest.getName());
            ownerDeviceRepository.save(ownerDevice);
        } else {
            throw new BadRequestAlertException(
                ExceptionConstants.CUSTOMER_TAX_CODE_INVALID_VI,
                ExceptionConstants.CUSTOMER_TAX_CODE_INVALID_VI,
                ExceptionConstants.CUSTOMER_TAX_CODE_INVALID_CODE
            );
        }

        deviceCodeRequest.setDeviceCode(ownerDevice.getDeviceCode());
        return new ResultDTO(
            ResultConstants.DEVICE_CODE_REGISTER_SUCCESS,
            ResultConstants.DEVICE_CODE_REGISTER_SUCCESS_VI,
            true,
            deviceCodeRequest
        );
    }

    @Override
    public ResultDTO getWithPaging(Pageable pageable, Integer companyId, String keyword, String fromDate, String toDate) {
        Page<ConfigResult> resultPage = configRepository.getWithPaging(companyId, pageable, keyword, fromDate, toDate);
        log.debug(ENTITY_NAME + "_getWithPaging: " + ResultConstants.CONFIG_GET_ALL_SUCCESS_VI);
        return new ResultDTO(
            ResultConstants.SUCCESS,
            ResultConstants.CONFIG_GET_ALL_SUCCESS_VI,
            true,
            resultPage.getContent(),
            (int) resultPage.getTotalElements()
        );
    }

    @Override
    public ResultDTO getConfigDetail(Integer id) {
        Optional<Config> configOptional = configRepository.findById(id);
        if (configOptional.isEmpty()) {
            throw new InternalServerException(
                ExceptionConstants.CONFIG_NOT_EXISTS_VI,
                ENTITY_NAME,
                ExceptionConstants.CONFIG_NOT_EXISTS_CODE
            );
        }
        ConfigResult configResult = new ConfigResult();
        BeanUtils.copyProperties(configOptional.get(), configResult);
        log.debug(ENTITY_NAME + "_getConfigDetail: " + ResultConstants.CONFIG_GET_DETAIL_SUCCESS_VI);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.CONFIG_GET_DETAIL_SUCCESS_VI, true, configResult);
    }

    @Override
    public ResultDTO save(ConfigSaveRequest request) {
        User user = userService.getUserWithAuthorities();
        Integer comId = null;
        Optional<Config> configOptional;
        if (user.getManager()) {
            configOptional = configRepository.findByIdAndCompanyId(request.getId(), request.getCompanyId());
        } else {
            configOptional = configRepository.findById(request.getId());
        }
        Config config;
        if (request.getId() == null) {
            config = new Config();
        } else {
            if (configOptional.isEmpty()) {
                throw new InternalServerException(
                    ExceptionConstants.CONFIG_NOT_EXISTS_VI,
                    ENTITY_NAME,
                    ExceptionConstants.CONFIG_NOT_EXISTS_CODE
                );
            }
            config = configOptional.get();
        }

        if (
            !request.getCode().equals(config.getCode()) &&
            configRepository.countByCompanyIdAndCode(request.getCompanyId(), request.getCode()) > 0
        ) {
            throw new BadRequestAlertException(
                ExceptionConstants.CONFIG_CODE_EXISTS_VI,
                ENTITY_NAME,
                ExceptionConstants.CONFIG_CODE_EXISTS_CODE
            );
        }
        BeanUtils.copyProperties(request, config);
        configRepository.save(config);
        log.debug(ENTITY_NAME + "_saveConfig: " + ResultConstants.CONFIG_SAVE_SUCCESS_VI);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.CONFIG_SAVE_SUCCESS_VI, true);
    }

    @Override
    public ResultDTO getWithOwnerId(Integer ownerId) {
        List<String> codes = Common.getInvoiceConfigCodes();
        List<ConfigOwnerResult> results = configRepository.getConfigsByCompanyOwnerOrCompany(ownerId, codes, null);
        Map<String, Object> resultMap = new HashMap<>();
        if (!results.isEmpty()) {
            for (ConfigOwnerResult result : results) {
                resultMap.put(result.getCode(), result);
            }
        }
        for (String code : codes) {
            if (!resultMap.containsKey(code)) {
                results.add(new ConfigOwnerResult(code, "", ""));
            }
        }
        log.debug(ENTITY_NAME + "_getWithOwnerId: " + ResultConstants.CONFIG_GET_ALL_SUCCESS_VI);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.CONFIG_GET_ALL_SUCCESS_VI, true, results);
    }

    @Override
    public ResultDTO getWithComId(Integer comId) {
        List<String> codes = Common.getEBConfigCodes();
        List<ConfigOwnerResult> results = configRepository.getConfigsByCompanyOwnerOrCompany(null, codes, comId);
        Map<String, Object> resultMap = new HashMap<>();
        if (!results.isEmpty()) {
            for (ConfigOwnerResult result : results) {
                resultMap.put(result.getCode(), result);
            }
        }
        for (String code : codes) {
            if (!resultMap.containsKey(code)) {
                results.add(new ConfigOwnerResult(code, "", ""));
            }
        }
        log.debug(ENTITY_NAME + "_getWithComId: " + ResultConstants.CONFIG_GET_ALL_SUCCESS_VI);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.CONFIG_GET_ALL_SUCCESS_VI, true, results);
    }

    @Override
    public ResultDTO saveConfigByOwnerId(ConfigOwnerSaveRequest request) {
        if (companyOwnerRepository.countById(request.getOwnerId()) < 1) {
            throw new BadRequestAlertException(
                ExceptionConstants.COMPANY_NOT_EXISTS_CODE_VI,
                ENTITY_NAME,
                ExceptionConstants.COMPANY_NOT_EXISTS_CODE
            );
        }
        List<Integer> comIds = companyRepository.findAllByOwnerId(request.getOwnerId());
        if (comIds.size() >= 1) {
            List<Config> results = configRepository.getByCompanyIdAndCode(request.getOwnerId(), request.getCode());

            Map<Integer, Config> comIdAndConfigMap = new HashMap<>();
            if (!results.isEmpty()) {
                for (Config config : results) {
                    comIdAndConfigMap.put(config.getCompanyId(), config);
                }
            }
            results = new ArrayList<>();
            for (Integer comId : comIds) {
                Config config;
                if (comIdAndConfigMap.containsKey(comId)) {
                    config = comIdAndConfigMap.get(comId);
                } else {
                    config = new Config();
                    config.setCompanyId(comId);
                }
                config.setCode(request.getCode());
                config.setValue(request.getValue());
                config.setDescription(request.getDescription());
                results.add(config);
            }

            configRepository.saveAll(results);
            return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.CONFIG_SAVE_SUCCESS_VI, true);
        } else {
            return new ResultDTO(ResultConstants.FAIL, ResultConstants.CONFIG_SAVE_FAIL_VI, false);
        }
    }

    @Override
    public ResultDTO saveConfigByComId(ConfigOwnerSaveRequest request) {
        Integer comId = request.getOwnerId();
        String code = request.getCode();
        if (companyRepository.countById(comId) != 1) {
            throw new BadRequestAlertException(
                ExceptionConstants.COMPANY_NOT_EXISTS_CODE_VI,
                ENTITY_NAME,
                ExceptionConstants.COMPANY_NOT_EXISTS_CODE
            );
        }
        Optional<Config> optionalConfig = configRepository.findByCompanyIdAndCode(comId, code);
        Config config;
        if (optionalConfig.isEmpty()) {
            config = new Config();
            config.setCompanyId(comId);
        } else {
            config = optionalConfig.get();
        }
        if (!code.equals(config.getCode()) && configRepository.countByCompanyIdAndCode(comId, code) == 1) {
            throw new BadRequestAlertException(
                ExceptionConstants.CONFIG_CODE_EXISTS_VI,
                ENTITY_NAME,
                ExceptionConstants.CONFIG_CODE_EXISTS_CODE
            );
        }
        BeanUtils.copyProperties(request, config);
        configRepository.save(config);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.CONFIG_SAVE_SUCCESS_VI, true);
    }

    public InvoiceConfig getConfigStoreByCompanyID(User user) {
        List<String> codes = Arrays.asList(
            EasyInvoiceConstants.TYPE_DISCOUNT,
            EasyInvoiceConstants.INVOICE_PATTERN,
            EasyInvoiceConstants.INVOICE_TYPE,
            EasyInvoiceConstants.INVOICE_METHOD,
            Constants.OVER_STOCK_CODE,
            Constants.VOUCHER_APPLY_CODE,
            Constants.COMBINE_VOUCHER_APPLY_CODE,
            Constants.PUSH_VOUCHER_DISCOUNT_INVOICE_CODE,
            EasyInvoiceConstants.DISCOUNT_VAT,
            Constants.INV_DYNAMIC_DISCOUNT_NAME_CODE,
            EasyInvoiceConstants.DISCOUNT_VAT,
            Constants.IS_BUYER_CODE,
            Constants.TAX_REDUCTION_CODE,
            Constants.EXTRA_CONFIG_CODE,
            Constants.BUSINESS_TYPE,
            Constants.DISPLAY_CONFIG
        );
        InvoiceConfig configStore = new InvoiceConfig();
        List<Config> configs = configRepository.getAllByCompanyID(user.getCompanyId(), codes);
        if (configs == null || configs.size() == 0) {
            return null;
        }
        for (Config item : configs) {
            if (!Strings.isNullOrEmpty(item.getValue())) {
                if (item.getCode() != null && item.getCode().equals(EasyInvoiceConstants.TYPE_DISCOUNT)) {
                    configStore.setTypeDiscount(Integer.parseInt(item.getValue()));
                }
                if (item.getCode() != null && item.getCode().equals(EasyInvoiceConstants.INVOICE_PATTERN)) {
                    configStore.setInvoicePattern(item.getValue());
                }
                if (item.getCode() != null && item.getCode().equals(EasyInvoiceConstants.INVOICE_TYPE)) {
                    configStore.setInvoiceType(Integer.parseInt(item.getValue()));
                }
                if (item.getCode() != null && item.getCode().equals(EasyInvoiceConstants.INVOICE_METHOD)) {
                    configStore.setInvoiceMethod(Integer.parseInt(item.getValue()));
                }
                if (item.getCode() != null && item.getCode().equals(Constants.OVER_STOCK_CODE)) {
                    configStore.setOverStock(Integer.parseInt(item.getValue()));
                }
                if (item.getCode() != null && item.getCode().equals(EasyInvoiceConstants.DISCOUNT_VAT)) {
                    configStore.setDiscountVat(Integer.parseInt(item.getValue()));
                }
                if (item.getCode() != null && item.getCode().equals(Constants.VOUCHER_APPLY_CODE)) {
                    configStore.setVoucherApply(Integer.parseInt(item.getValue()));
                }
                if (item.getCode() != null && item.getCode().equals(Constants.COMBINE_VOUCHER_APPLY_CODE)) {
                    configStore.setCombineVoucherApply(Integer.parseInt(item.getValue()));
                }
                if (item.getCode() != null && item.getCode().equals(Constants.PUSH_VOUCHER_DISCOUNT_INVOICE_CODE)) {
                    configStore.setPushVoucherDiscountInvoice(Integer.parseInt(item.getValue()));
                }
                if (item.getCode() != null && item.getCode().equals(Constants.TAX_REDUCTION_CODE)) {
                    configStore.setTaxReductionType(Integer.parseInt(item.getValue()));
                }
                if (item.getCode() != null && item.getCode().equals(Constants.IS_BUYER_CODE)) {
                    configStore.setIsBuyer(Integer.parseInt(item.getValue()));
                }
                if (item.getCode() != null && item.getCode().equals(Constants.INV_DYNAMIC_DISCOUNT_NAME_CODE)) {
                    configStore.setInvDynamicDiscountName(Integer.parseInt(item.getValue()));
                }
                if (
                    item.getCode() != null && item.getCode().equals(Constants.EXTRA_CONFIG_CODE) && !Strings.isNullOrEmpty(item.getValue())
                ) {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        ExtraConfig extraConfig = objectMapper.readValue(item.getValue(), ExtraConfig.class);
                        if (extraConfig.getServiceCharge() != null) {
                            extraConfig
                                .getServiceCharge()
                                .forEach(svc -> {
                                    svc.setCode(null);
                                    svc.setVariable(null);
                                    svc.setTable(null);
                                });
                            configStore.setServiceChargeConfig(extraConfig.getServiceCharge());
                        }
                        if (extraConfig.getTotalAmount() != null) {
                            extraConfig
                                .getTotalAmount()
                                .forEach(svc -> {
                                    svc.setCode(null);
                                    svc.setTable(null);
                                });
                            configStore.setTotalAmount(extraConfig.getTotalAmount());
                        }
                    } catch (Exception exception) {
                        throw new BadRequestAlertException(
                            ExceptionConstants.CONFIG_EXT_BILL_INVALID_VI,
                            ENTITY_NAME,
                            ExceptionConstants.CONFIG_EXT_BILL_INVALID
                        );
                    }
                }
                if (item.getCode() != null && item.getCode().equals(Constants.BUSINESS_TYPE)) {
                    configStore.setBusinessType(Integer.parseInt(item.getValue()));
                }
                if (item.getCode() != null && item.getCode().equals(Constants.DISPLAY_CONFIG)) {
                    configStore.setDisplayConfig(item.getValue());
                }
            }
        }
        configStore.setComId(user.getCompanyId());
        configStore.setTaxCode(user.getTaxCode());
        return configStore;
    }

    @Override
    public ResultDTO delete(Integer id) {
        Optional<Config> configOptional = configRepository.findById(id);
        if (configOptional.isEmpty()) {
            throw new InternalServerException(
                ExceptionConstants.CONFIG_NOT_EXISTS_VI,
                ENTITY_NAME,
                ExceptionConstants.CONFIG_NOT_EXISTS_CODE
            );
        }
        configRepository.deleteById(id);
        log.debug(ENTITY_NAME + "_getConfigDetail: " + ResultConstants.CONFIG_DELETE_SUCCESS_VI);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.CONFIG_DELETE_SUCCESS_VI, true);
    }

    @Override
    public ConfigInvoice getConfigInvoiceByCompanyID(User user) {
        List<String> codes = new ArrayList<>();
        codes =
            Arrays.asList(
                EasyInvoiceConstants.EASYINVOICE_URL,
                EasyInvoiceConstants.EASYINVOICE_LOOKUP,
                EasyInvoiceConstants.EASYINVOICE_ACCOUNT,
                EasyInvoiceConstants.EASYINVOICE_PASS
            );
        ConfigInvoice configStoreDTO = new ConfigInvoice();
        List<Config> configs = configRepository.getAllByCompanyID(user.getCompanyId(), codes);
        if (configs == null || configs.size() == 0) {
            return null;
        }
        for (Config item : configs) {
            if (item.getCode().equals(EasyInvoiceConstants.EASYINVOICE_URL)) {
                configStoreDTO.setEasyInvoiceUrl(item.getValue());
            }
            if (item.getCode().equals(EasyInvoiceConstants.EASYINVOICE_LOOKUP)) {
                configStoreDTO.setEasyInvoiceLookup(item.getValue());
            }
            if (item.getCode().equals(EasyInvoiceConstants.EASYINVOICE_ACCOUNT)) {
                configStoreDTO.setEasyInvoiceAccount(item.getValue());
            }
            if (item.getCode().equals(EasyInvoiceConstants.EASYINVOICE_PASS)) {
                configStoreDTO.setEasyInvoicePass(item.getValue());
            }
        }
        return configStoreDTO;
    }

    @Override
    public ResultDTO updateSellConfig(SellConfig sellConfig) {
        User user = userService.getUserWithAuthorities(sellConfig.getComId());
        List<String> codes = Arrays.asList(
            EasyInvoiceConstants.DISCOUNT_VAT,
            EasyInvoiceConstants.TYPE_DISCOUNT,
            Constants.OVER_STOCK_CODE,
            Constants.TAX_REDUCTION_CODE,
            Constants.IS_BUYER_CODE,
            Constants.VOUCHER_APPLY_CODE,
            Constants.COMBINE_VOUCHER_APPLY_CODE,
            Constants.PUSH_VOUCHER_DISCOUNT_INVOICE_CODE,
            Constants.INV_DYNAMIC_DISCOUNT_NAME_CODE
        );
        List<Config> configs = configRepository.getAllByCompanyID(user.getCompanyId(), codes);
        List<Config> configList = new ArrayList<>();
        for (Config config : configs) {
            if (sellConfig.getOverStock() != null && config.getCode().equals(Constants.OVER_STOCK_CODE)) {
                config.setValue(sellConfig.getOverStock().toString());
                configList.add(config);
            } else if (sellConfig.getTaxReductionType() != null && config.getCode().equals(Constants.TAX_REDUCTION_CODE)) {
                config.setValue(sellConfig.getTaxReductionType().toString());
                configList.add(config);
            } else if (sellConfig.getDiscountVat() != null && config.getCode().equals(EasyInvoiceConstants.DISCOUNT_VAT)) {
                config.setValue(sellConfig.getDiscountVat().toString());
                configList.add(config);
            } else if (sellConfig.getIsBuyer() != null && config.getCode().equals(Constants.IS_BUYER_CODE)) {
                config.setValue(sellConfig.getIsBuyer().toString());
                configList.add(config);
            } else if (
                sellConfig.getInvDynamicDiscountName() != null && config.getCode().equals(Constants.INV_DYNAMIC_DISCOUNT_NAME_CODE)
            ) {
                config.setValue(sellConfig.getInvDynamicDiscountName().toString());
                configList.add(config);
            } else if (sellConfig.getTypeDiscount() != null && config.getCode().equals(EasyInvoiceConstants.TYPE_DISCOUNT)) {
                config.setValue(sellConfig.getTypeDiscount().toString());
                configList.add(config);
            } else if (sellConfig.getVoucherApply() != null && config.getCode().equals(Constants.VOUCHER_APPLY_CODE)) {
                config.setValue(sellConfig.getVoucherApply().toString());
                configList.add(config);
            } else if (sellConfig.getCombineVoucherApply() != null && config.getCode().equals(Constants.COMBINE_VOUCHER_APPLY_CODE)) {
                config.setValue(sellConfig.getCombineVoucherApply().toString());
                configList.add(config);
            } else if (
                sellConfig.getPushVoucherDiscountInvoice() != null && config.getCode().equals(Constants.PUSH_VOUCHER_DISCOUNT_INVOICE_CODE)
            ) {
                config.setValue(sellConfig.getPushVoucherDiscountInvoice().toString());
                configList.add(config);
            }
        }
        configRepository.saveAll(configList);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_UPDATE, true);
    }

    @Override
    public ResultDTO updateEasyInvoiceConfig(EasyInvoiceConfig easyInvoiceConfig) {
        Integer comId = easyInvoiceConfig.getComId();
        User user = userService.getUserWithAuthorities(comId);
        LoginEasyInvoice loginEasyInvoice = new LoginEasyInvoice();
        loginEasyInvoice.setUsername(easyInvoiceConfig.getUsername());
        loginEasyInvoice.setPassword(easyInvoiceConfig.getPassword());
        loginEasyInvoice.setUrl(easyInvoiceConfig.getUrl());
        loginEasyInvoice.setService(easyInvoiceConfig.getService());

        ResultDTO resultDTO = updateEasyInvoice(loginEasyInvoice);
        if (!resultDTO.isStatus()) {
            return resultDTO;
        }
        if (
            !Strings.isNullOrEmpty(easyInvoiceConfig.getService()) &&
            easyInvoiceConfig.getService().equals(UserConstants.Service.NGP) &&
            Strings.isNullOrEmpty(easyInvoiceConfig.getTaxRegisterCode())
        ) {
            resultDTO = getTaxMachineCodeForNGP(easyInvoiceConfig, user);
            if (!resultDTO.isStatus()) {
                return resultDTO;
            }
        }
        Optional<Company> companyOptional = companyRepository.findById(comId);
        if (companyOptional.isEmpty()) {
            throw new BadRequestAlertException(
                ExceptionConstants.COMPANY_NOT_EXISTS_CODE_VI,
                ENTITY_NAME,
                ExceptionConstants.COMPANY_NOT_EXISTS_CODE
            );
        }
        Company company = companyOptional.get();
        company.setService(easyInvoiceConfig.getService());
        //        CompanyOwner companyOwner = company.getCompanyOwner();
        //        if (!Strings.isNullOrEmpty(easyInvoiceConfig.getTaxRegisterCode())) {
        //            companyOwner.setTaxMachineCode(easyInvoiceConfig.getTaxRegisterCode());
        //        }

        List<String> codes = Arrays.asList(
            EasyInvoiceConstants.INVOICE_TYPE,
            EasyInvoiceConstants.INVOICE_METHOD,
            EasyInvoiceConstants.INVOICE_PATTERN
        );
        List<Config> configs = configRepository.getAllByCompanyID(comId, codes);
        List<Config> configList = new ArrayList<>();
        for (Config config : configs) {
            if (
                !Strings.isNullOrEmpty(easyInvoiceConfig.getInvoicePattern()) &&
                config.getCode().equals(EasyInvoiceConstants.INVOICE_PATTERN)
            ) {
                config.setValue(easyInvoiceConfig.getInvoicePattern());
                configList.add(config);
            } else if (easyInvoiceConfig.getInvoiceType() != null && config.getCode().equals(EasyInvoiceConstants.INVOICE_TYPE)) {
                config.setValue(easyInvoiceConfig.getInvoiceType().toString());
                configList.add(config);
            } else if (easyInvoiceConfig.getInvoiceMethod() != null && config.getCode().equals(EasyInvoiceConstants.INVOICE_METHOD)) {
                config.setValue(easyInvoiceConfig.getInvoiceMethod().toString());
                configList.add(config);
            }
        }
        configRepository.saveAll(configList);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_UPDATE, true);
    }

    @Override
    public ResultDTO updateDisplayConfig(ConfigSaveRequest config) {
        User user = userService.getUserWithAuthorities(config.getCompanyId());
        Optional<Config> configOptional = configRepository.findByIdAndCompanyId(config.getId(), config.getCompanyId());
        if (configOptional.isPresent()) {
            Config configSave = configOptional.get();
            configSave.setValue(config.getValue());
            configRepository.save(configSave);
            return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_UPDATE, true);
        }
        return new ResultDTO(ResultConstants.ERROR_CONFIG, ResultConstants.ERROR_CONFIG_VI, false);
    }

    @Override
    public ResultDTO getDisplayConfig() {
        User user = userService.getUserWithAuthorities();
        Optional<Config> configOptional = configRepository.findByCompanyIdAndCode(user.getCompanyId(), EasyInvoiceConstants.DISPLAY_CONFIG);
        if (configOptional.isPresent()) {
            return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_UPDATE, true, configOptional.get());
        }
        return new ResultDTO(ResultConstants.ERROR, ResultConstants.ERROR_CONFIG_VI, false);
    }

    public ResultDTO getTaxMachineCodeForNGP(EasyInvoiceConfig easyInvoiceConfig, User user) {
        if (
            easyInvoiceConfig.getService().equals(UserConstants.Service.NGP) &&
            !Strings.isNullOrEmpty(easyInvoiceConfig.getUrl()) &&
            !easyInvoiceConfig.getUrl().contains("ngogiaphat")
        ) {
            throw new InternalServerException(
                ExceptionConstants.SYSTEM_INVALID_ERROR_VI,
                ENTITY_NAME,
                ExceptionConstants.SYSTEM_INVALID_ERROR
            );
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ObjectMapper objectMapper = new ObjectMapper();
        LoginEasyInvoiceRequest request = new LoginEasyInvoiceRequest();
        request.setUsername(easyInvoiceConfig.getUsername());
        request.setPassword(easyInvoiceConfig.getPassword());
        request.setTaxcode(user.getTaxCode());
        Optional<String> configOptional = configRepository.getValueByComIdAndCode(
            user.getCompanyId(),
            EasyInvoiceConstants.EASYINVOICE_URL
        );
        if (configOptional.isPresent()) {
            String loginUrl = configOptional.get() + "/api/account/verify";
            ResponseEntity<LoginEasyInvoiceResponse> response;
            try {
                HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(request), headers);
                response = restTemplate.postForEntity(loginUrl, requestEntity, LoginEasyInvoiceResponse.class);
            } catch (Exception ex) {
                log.error(ex.getMessage());
                throw new InternalServerException(
                    ExceptionConstants.GET_URL_EASY_INVOICE_ERROR_VI,
                    ENTITY_NAME,
                    ExceptionConstants.GET_URL_EASY_INVOICE_ERROR
                );
            }
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new InternalServerException(
                    ExceptionConstants.LOGIN_EASY_INVOICE_ERROR_VI,
                    ENTITY_NAME,
                    ExceptionConstants.LOGIN_EASY_INVOICE_ERROR
                );
            }
            if (!Strings.isNullOrEmpty(Objects.requireNonNull(response.getBody()).getData().getCode())) {
                Optional<CompanyOwner> companyOwnerOptional = companyOwnerRepository.findByCompanyIdAndTaxCode(
                    user.getCompanyId(),
                    user.getTaxCode()
                );
                if (companyOwnerOptional.isPresent()) {
                    CompanyOwner companyOwner = companyOwnerOptional.get();
                    companyOwner.setTaxMachineCode(response.getBody().getData().getCode());
                }
            }
            return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_GET_LIST, true);
        }
        return new ResultDTO(ResultConstants.FAIL, ExceptionConstants.LOGIN_EASY_INVOICE_ERROR_VI, false);
    }

    public ResultDTO updateEasyInvoice(LoginEasyInvoice loginEasyInvoice) {
        User user = userService.getUserWithAuthorities();
        Integer ownerId = loginEasyInvoice.getCompanyOwnerId();
        Integer companyId;
        String taxCodeOwner;
        companyId = userService.getCompanyId();
        taxCodeOwner = user.getTaxCode();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Optional<String> configOptional = configRepository.getValueByComIdAndCode(companyId, EasyInvoiceConstants.EASYINVOICE_URL);
        LoginEasyInvoiceRequest request = new LoginEasyInvoiceRequest();
        request.setUsername(loginEasyInvoice.getUsername());
        request.setPassword(loginEasyInvoice.getPassword());
        request.setTaxcode(user.getTaxCode());
        String baseUrl = "";
        String lookUpUrl = "";
        String service = loginEasyInvoice.getService();
        if ((configOptional.isEmpty() || Strings.isNullOrEmpty(configOptional.get())) && Strings.isNullOrEmpty(loginEasyInvoice.getUrl())) {
            // Call api lay url
            CompanyUrlResponse companyUrlResponse = CommonIntegrated.getUrlByUserNameAndTaxCode(taxCodeOwner, restTemplate);
            baseUrl = formatBaseUrl(companyUrlResponse.getPublishDomain());
            lookUpUrl = companyUrlResponse.getPortalLink();
        } else {
            if (!Strings.isNullOrEmpty(loginEasyInvoice.getUrl())) {
                baseUrl = formatBaseUrl(loginEasyInvoice.getUrl());
                if (Strings.isNullOrEmpty(service) && Strings.isNullOrEmpty(loginEasyInvoice.getUrl())) {
                    CompanyUrlResponse companyUrlResponse = CommonIntegrated.getUrlByUserNameAndTaxCode(taxCodeOwner, restTemplate);
                    lookUpUrl = companyUrlResponse.getPortalLink();
                }
            } else {
                baseUrl = configOptional.get();
            }
        }
        if (Strings.isNullOrEmpty(service) || service.equals(UserConstants.Service.NGP)) {
            if (Strings.isNullOrEmpty(service)) {
                if (!baseUrl.contains("softdreams") && !baseUrl.contains("easyinvoice")) {
                    throw new InternalServerException(
                        ExceptionConstants.SYSTEM_INVALID_ERROR_VI,
                        ENTITY_NAME,
                        ExceptionConstants.SYSTEM_INVALID_ERROR
                    );
                }
            } else if (service.equals(UserConstants.Service.NGP) && !baseUrl.contains("ngogiaphat")) {
                throw new InternalServerException(
                    ExceptionConstants.SYSTEM_INVALID_ERROR_VI,
                    ENTITY_NAME,
                    ExceptionConstants.SYSTEM_INVALID_ERROR
                );
            }

            Object loginResponse = easyInvoiceApiClient.checkLogin(baseUrl, request, headers);
            if (loginResponse instanceof LoginEasyInvoiceResponse) {
                if (!Objects.equals(((LoginEasyInvoiceResponse) loginResponse).getStatus(), "2")) throw new InternalServerException(
                    "Không thể kết nối đến hệ thống Hóa đơn điện tử. Thông tin đăng nhập không chính xác.",
                    "Không thể kết nối đến hệ thống Hóa đơn điện tử. Thông tin đăng nhập không chính xác.",
                    "LOGIN_EASY_INVOICE"
                );
            } else if (loginResponse instanceof EasyInvoiceLoginResponse) if (
                ((EasyInvoiceLoginResponse) loginResponse).getCode() != 200
            ) throw new InternalServerException(
                "Không thể kết nối đến hệ thống Hóa đơn điện tử. Thông tin đăng nhập không chính xác.",
                "Không thể kết nối đến hệ thống Hóa đơn điện tử. Thông tin đăng nhập không chính xác.",
                "LOGIN_EASY_INVOICE"
            );
        }
        List<String> invoiceCodes = new ArrayList<>();
        invoiceCodes.add(EasyInvoiceConstants.EASYINVOICE_ACCOUNT);
        invoiceCodes.add(EasyInvoiceConstants.EASYINVOICE_PASS);
        invoiceCodes.add(EasyInvoiceConstants.EASYINVOICE_URL);
        invoiceCodes.add(EasyInvoiceConstants.EASYINVOICE_LOOKUP);
        List<Config> configs = configRepository.getAllByCompanyID(companyId, invoiceCodes);

        Map<String, Config> mapConfig = new HashMap<>();
        for (Config config : configs) {
            mapConfig.put(config.getCode(), config);
        }
        configs = new ArrayList<>();
        if (mapConfig.containsKey(EasyInvoiceConstants.EASYINVOICE_ACCOUNT)) {
            Config configItem = mapConfig.get(EasyInvoiceConstants.EASYINVOICE_ACCOUNT);
            configItem.setValue(loginEasyInvoice.getUsername());
            configs.add(configItem);
        } else {
            configs.add(
                new Config(
                    companyId,
                    EasyInvoiceConstants.EASYINVOICE_ACCOUNT,
                    loginEasyInvoice.getUsername(),
                    EasyInvoiceConstants.EASYINVOICE_ACCOUNT_VI
                )
            );
        }

        if (mapConfig.containsKey(EasyInvoiceConstants.EASYINVOICE_PASS)) {
            Config configItem = mapConfig.get(EasyInvoiceConstants.EASYINVOICE_PASS);
            configItem.setValue(loginEasyInvoice.getPassword());
            configs.add(configItem);
        } else {
            configs.add(
                new Config(
                    companyId,
                    EasyInvoiceConstants.EASYINVOICE_PASS,
                    loginEasyInvoice.getPassword(),
                    EasyInvoiceConstants.EASYINVOICE_PASS_VI
                )
            );
        }

        if (mapConfig.containsKey(EasyInvoiceConstants.EASYINVOICE_URL)) {
            Config configItem = mapConfig.get(EasyInvoiceConstants.EASYINVOICE_URL);
            configItem.setValue(baseUrl);
            configs.add(configItem);
        } else {
            configs.add(new Config(companyId, EasyInvoiceConstants.EASYINVOICE_URL, baseUrl, EasyInvoiceConstants.EASYINVOICE_URL_VI));
        }
        if (mapConfig.containsKey(EasyInvoiceConstants.EASYINVOICE_LOOKUP)) {
            Config configItem = mapConfig.get(EasyInvoiceConstants.EASYINVOICE_LOOKUP);
            if (!Strings.isNullOrEmpty(lookUpUrl)) {
                configItem.setValue(lookUpUrl);
            }
            configs.add(configItem);
        } else {
            configs.add(
                new Config(companyId, EasyInvoiceConstants.EASYINVOICE_LOOKUP, lookUpUrl, EasyInvoiceConstants.EASYINVOICE_LOOKUP_VI)
            );
        }
        configRepository.saveAll(configs);
        if (user.getManager()) {
            addAllConfigOwnerToSubCompany(ownerId, companyId);
        }
        if (Strings.isNullOrEmpty(service) || !service.equals(UserConstants.Service.NGP) || !service.equals(UserConstants.Service.VTE)) {
            try {
                declarationSearch(new DeclarationRequest(1, ""));
            } catch (Exception ex) {
                log.error("Lay ma so thue khong thanh cong");
            }
        }
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.CREATE_CONFIG_EI_SUCCESS_VI, true);
    }

    private String formatBaseUrl(String baseUrl) {
        return baseUrl.replaceAll(":7878", "").replaceAll("http://", "https://").replaceAll(".vn/", ".vn");
    }

    @Override
    public ResultDTO getInfoConfigByCompanyId(Integer companyId) {
        List<String> codes = Arrays.asList(
            EasyInvoiceConstants.EB88_COM_ID,
            EasyInvoiceConstants.EB88_DEFAULT_USER,
            EasyInvoiceConstants.EB88_REPOSITORY_ID
        );
        List<Config> configs = configRepository.getAllByCompanyID(companyId, codes);
        if (configs.size() != codes.size()) {
            return new ResultDTO(ResultConstants.FAIL, ResultConstants.CONFIG_STORE_EMPTY, false);
        }
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.CONFIG_STORE_AVAILABLE, true);
    }
}
