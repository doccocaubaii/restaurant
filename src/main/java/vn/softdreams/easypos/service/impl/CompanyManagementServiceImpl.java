package vn.softdreams.easypos.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import vn.softdreams.easypos.config.Constants;
import vn.softdreams.easypos.constants.CommonConstants;
import vn.softdreams.easypos.constants.ResultConstants;
import vn.softdreams.easypos.constants.TaskLogConstants;
import vn.softdreams.easypos.domain.*;
import vn.softdreams.easypos.dto.TaskLogSendQueue;
import vn.softdreams.easypos.dto.company.*;
import vn.softdreams.easypos.integration.easybooks88.api.EB88ApiClient;
import vn.softdreams.easypos.integration.easybooks88.queue.dto.CompanyUnitTask;
import vn.softdreams.easypos.repository.*;
import vn.softdreams.easypos.security.AuthoritiesConstants;
import vn.softdreams.easypos.service.CompanyManagementService;
import vn.softdreams.easypos.service.UserService;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.util.Common;
import vn.softdreams.easypos.web.rest.errors.BadRequestAlertException;
import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;
import vn.softdreams.easypos.web.rest.errors.InternalServerException;

import java.util.*;

@Service
@Transactional
public class CompanyManagementServiceImpl implements CompanyManagementService {

    private final Logger log = LoggerFactory.getLogger(CategoryManagementServiceImpl.class);
    private final String ENTITY_NAME = "Company";
    private final CompanyRepository companyRepository;
    private final CompanyOwnerRepository companyOwnerRepository;
    private final UserService userService;
    private final CompanyUserRepository companyUserRepository;
    private final EB88ApiClient eb88ApiClient;
    private final ConfigRepository configRepository;
    private final ModelMapper modelMapper;
    private final BusinessRepository businessRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final TransactionTemplate transactionTemplate;
    private final TaskLogRepository taskLogRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final ProductProductUnitRepository productProductUnitRepository;
    private final BusinessTypeRepository businessTypeRepository;
    private final ProductUnitRepository productUnitRepository;
    private final PrintConfigRepository printConfigRepository;
    private final AreaRepository areaRepository;
    private final PrintTemplateRepository printTemplateRepository;

    public CompanyManagementServiceImpl(
        CompanyRepository companyRepository,
        CompanyOwnerRepository companyOwnerRepository,
        UserService userService,
        CompanyUserRepository companyUserRepository,
        EB88ApiClient eb88ApiClient,
        ConfigRepository configRepository,
        ModelMapper modelMapper,
        BusinessRepository businessRepository,
        RoleRepository roleRepository,
        UserRoleRepository userRoleRepository,
        TransactionTemplate transactionTemplate,
        TaskLogRepository taskLogRepository,
        CustomerRepository customerRepository,
        ProductRepository productRepository,
        ProductProductUnitRepository productProductUnitRepository,
        BusinessTypeRepository businessTypeRepository,
        ProductUnitRepository productUnitRepository,
        PrintConfigRepository printConfigRepository,
        AreaRepository areaRepository,
        PrintTemplateRepository printTemplateRepository
    ) {
        this.companyRepository = companyRepository;
        this.companyOwnerRepository = companyOwnerRepository;
        this.userService = userService;
        this.companyUserRepository = companyUserRepository;
        this.eb88ApiClient = eb88ApiClient;
        this.configRepository = configRepository;
        this.modelMapper = modelMapper;
        this.businessRepository = businessRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.transactionTemplate = transactionTemplate;
        this.taskLogRepository = taskLogRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.productProductUnitRepository = productProductUnitRepository;
        this.businessTypeRepository = businessTypeRepository;
        this.productUnitRepository = productUnitRepository;
        this.printConfigRepository = printConfigRepository;
        this.areaRepository = areaRepository;
        this.printTemplateRepository = printTemplateRepository;
    }

    @Override
    public Company save(Company company) {
        return companyRepository.save(company);
    }

    @Override
    public Company update(Company company) {
        return null;
    }

    @Override
    public CompanyOwner saveOwner(CompanyOwner company) {
        return companyOwnerRepository.save(company);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CompanyOwner> findAll(Pageable pageable) {
        return companyOwnerRepository.findAll(pageable);
    }

    @Override
    public ResultDTO getAllWithPaging(Pageable pageable, Integer ownerId, String keyword, String fromDate, String toDate) {
        Page<CompanyResult> results = companyRepository.getAllWithPaging(
            pageable,
            Common.getEBConfigCodes(),
            ownerId,
            keyword,
            fromDate,
            toDate
        );
        log.debug(ENTITY_NAME + "_getAllWithPaging: " + ResultConstants.SUCCESS_GET_COMPANY);
        return new ResultDTO(
            ResultConstants.SUCCESS,
            ResultConstants.SUCCESS_GET_COMPANY,
            true,
            results.getContent(),
            (int) results.getTotalElements()
        );
    }

    @Override
    public ResultDTO update(CompanyUpdateRequest request) {
        User user = userService.getUserWithAuthorities();
        Integer comOwnerId = request.getComOwnerId();
        CompanyOwner companyOwner = checkOwnerIdExists(comOwnerId);
        Optional<Company> companyOptional = companyRepository.findByIdAndOwnerId(request.getComId(), comOwnerId);

        if (companyOptional.isEmpty()) {
            throw new InternalServerException(
                ExceptionConstants.COMPANY_NOT_EXISTS_CODE_VI,
                ENTITY_NAME,
                ExceptionConstants.COMPANY_NOT_EXISTS_CODE
            );
        }
        Company company = companyOptional.get();
        // nếu không phải là system_admin: kiểm tra comId có hợp lệ với user không
        if (!user.getManager()) {
            if (!user.getCompanyId().equals(company.getId()) && !companyOwner.getOwnerId().equals(user.getId())) {
                throw new InternalServerException(
                    ExceptionConstants.COMPANY_ID_NOT_EXISTS_VI,
                    ENTITY_NAME,
                    ExceptionConstants.COMPANY_ID_NOT_EXISTS_CODE
                );
            }
        }

        if (request.getName() != null) {
            if (request.getName().isBlank()) {
                throw new BadRequestAlertException(
                    ExceptionConstants.COMPANY_NAME_NOT_EMPTY_VI,
                    ENTITY_NAME,
                    ExceptionConstants.COMPANY_NAME_NOT_EMPTY_CODE
                );
            }
            if (
                !request.getName().equals(company.getName()) && companyRepository.countByNameAndOwnerId(request.getName(), comOwnerId) > 0
            ) {
                throw new BadRequestAlertException(
                    ExceptionConstants.COMPANY_NAME_DUPLICATE_VI,
                    ENTITY_NAME,
                    ExceptionConstants.COMPANY_NAME_DUPLICATE_CODE
                );
            }
        }
        if (request.getBusinessId() != null) {
            checkBusinessId(request.getBusinessId());
        }
        BeanUtils.copyProperties(request, company);
        company.setCompanyOwner(companyOwner);
        Integer requestComId = companyRepository.getIdByOwner(comOwnerId);
        if (requestComId == null) {
            throw new InternalServerException(
                ExceptionConstants.OWNER_ID_NOT_EXISTS_VI + " bên hệ thống EasyPos",
                ENTITY_NAME,
                ExceptionConstants.OWNER_ID_NOT_EXISTS_CODE + "_EasyPos"
            );
        }
        //        Integer ownerIdEB = getOwnerIdEB(comOwnerId);
        // update to EB & EP
        TaskLogSendQueue taskLogSendQueue = transactionTemplate.execute(status -> {
            try {
                company.setNormalizedName(Common.normalizedName(List.of(company.getName())));
                companyRepository.save(company);

                return createAndPublishQueueTask(company.getId(), false, TaskLogConstants.Type.EB_SAVE_COMPANY_UNIT);
            } catch (Exception e) {
                log.error("Can not create queue task for eb88 update CompanyUnit: {}", e.getMessage());
            }
            return null;
        });
        if (taskLogSendQueue != null) {
            userService.sendTaskLog(taskLogSendQueue);
        }
        log.debug(ENTITY_NAME + "_updateEP: " + ResultConstants.COMPANY_OWNER_SAVE_SUCCESS_VI);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.COMPANY_OWNER_SAVE_SUCCESS_VI, true);
        //            log.debug(ENTITY_NAME + "_updateToEB88 failed: " + updateToEBResult.getReason());
        //            return new ResultDTO(ResultConstants.FAIL, updateToEBResult.getReason(), false);
    }

    @Override
    public ResultDTO create(CompanyCreateRequest request) {
        User user = userService.getUserWithAuthorities();
        Integer requestOwnerId = request.getComOwnerId();
        // lấy id company từ companyOwnerId
        CompanyOwner companyOwner = checkOwnerIdExists(requestOwnerId);
        Integer requestComId = companyRepository.getIdByOwner(requestOwnerId);
        if (requestComId == null) {
            throw new InternalServerException(
                ExceptionConstants.OWNER_ID_NOT_EXISTS_VI + " bên hệ thống EasyPos",
                ENTITY_NAME,
                ExceptionConstants.OWNER_ID_NOT_EXISTS_CODE + "_EasyPos"
            );
        }

        Company company = new Company();
        Integer ownerId = companyOwner.getOwnerId(); // id chủ công ty
        // nếu không phải là system_admin
        if (!user.getManager()) {
            // Người tạo phải là owner_id của company_owner
            if (!Objects.equals(ownerId, user.getId())) {
                throw new BadRequestAlertException(
                    ExceptionConstants.COMPANY_SAVE_NOT_VALID_VI,
                    ENTITY_NAME,
                    ExceptionConstants.COMPANY_SAVE_NOT_VALID_CODE
                );
            }
        }
        checkCompanyNameDuplicate(request.getName(), requestOwnerId);
        if (request.getBusinessId() != null) {
            checkBusinessId(request.getBusinessId());
        }
        BeanUtils.copyProperties(request, company);
        company.setCompanyOwner(companyOwner);
        company.setParent(false);
        company.setNormalizedName(Common.normalizedName(List.of(company.getName())));

        // Send message to queue
        TaskLogSendQueue taskLogSendQueue = transactionTemplate.execute(status -> {
            try {
                companyRepository.save(company);
                Integer comId = company.getId();
                userService.autoGenSeqName(comId);

                // set UserRole & CompanyOwner
                CompanyUser companyUser = new CompanyUser();
                UserRole userRole = new UserRole();

                userRole.setComId(comId);
                companyUser.setCompanyId(comId);
                if (!user.getManager()) {
                    companyUser.setUserId(user.getId());
                    userRole.setUserId(user.getId());
                } else {
                    companyUser.setUserId(ownerId);
                    userRole.setUserId(ownerId);
                }
                Integer roleId = roleRepository.findIdByCode(AuthoritiesConstants.ADMIN);
                if (roleId == null) {
                    throw new InternalServerException(ExceptionConstants.ROLE_NOT_FOUND_VI, ENTITY_NAME, ExceptionConstants.ROLE_NOT_FOUND);
                }
                userRole.setRoleId(roleId);
                userRoleRepository.save(userRole);
                companyUserRepository.save(companyUser);

                // set config chi nhánh công ty theo công ty cha
                List<Config> configsFromCompanyOwner = configRepository.getAllByCompanyID(requestComId, Common.getConfigDefaultCodes());
                List<Config> configsSave = new ArrayList<>();
                configsFromCompanyOwner.forEach(config -> {
                    Config configSave = new Config();
                    configSave.setId(null);
                    configSave.setCompanyId(comId);
                    if (config.getCode().equalsIgnoreCase(Constants.EB88_COM_ID)) {
                        configSave.setValue(String.valueOf(company.getEbId()));
                    } else {
                        configSave.setValue(config.getValue());
                    }
                    configSave.setCode(config.getCode());
                    configSave.setCreator(ownerId);
                    configSave.setUpdater(ownerId);
                    configSave.setDescription(config.getDescription());
                    configsSave.add(configSave);
                });
                configRepository.saveAll(configsSave);
                syncAfterCreateCompanyUnit(company.getId(), user, company.getName());

                return createAndPublishQueueTask(company.getId(), true, TaskLogConstants.Type.EB_SAVE_COMPANY_UNIT);
            } catch (Exception e) {
                log.error("Can not create queue task for eb88 creating CompanyUnit: {}", e.getMessage());
            }
            return null;
        });
        if (taskLogSendQueue != null) {
            userService.sendTaskLog(taskLogSendQueue);
        }

        log.debug(ENTITY_NAME + "_createEP: " + ResultConstants.COMPANY_OWNER_CREATE_SUCCESS_VI);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.COMPANY_OWNER_CREATE_SUCCESS_VI, true);
    }

    private void syncAfterCreateCompanyUnit(Integer companyId, User user, String companyName) {
        List<Product> products = new ArrayList<>();
        List<ProductProductUnit> productUnits = new ArrayList<>();
        Product product = Common.createProductDefault(companyId, CommonConstants.PRODUCT_NAME_CREATE_DEFAULT);
        product.setCode(userService.genCode(companyId, Constants.PRODUCT_CODE));
        products.add(product);

        Product productNote = Common.createProductDefault(companyId, CommonConstants.PRODUCT_NAME_NOTE_CREATE_DEFAULT);
        productNote.setCode(Constants.PRODUCT_CODE_NOTE_DEFAULT);
        products.add(productNote);

        Product productPromotion = Common.createProductDefault(companyId, CommonConstants.PRODUCT_NAME_PROMOTION_CREATE_DEFAULT);
        productPromotion.setCode(Constants.PRODUCT_CODE_PROMOTION_DEFAULT);
        products.add(productPromotion);

        productRepository.saveAll(products);
        for (Product p : products) {
            ProductProductUnit productProductUnit = Common.createProductProductUnitDefault(companyId, p.getId());
            productUnits.add(productProductUnit);
        }
        productProductUnitRepository.saveAll(productUnits);

        Customer customer = Common.createCustomerDefault(companyId);
        customer.setCode(userService.genCode(companyId, Constants.CUSTOMER_CODE));
        customerRepository.save(customer);

        businessTypeRepository.saveAll(Common.createBusinessTypeDefault(companyId));

        areaRepository.save(Common.createAreaDefault(companyId));

        List<PrintTemplate> printTemplates = printTemplateRepository.getAllDefaultTemplate();
        List<PrintTemplate> result = Common.createPrintTemplateDefault(printTemplates, companyId);
        printTemplateRepository.saveAll(result);

        printConfigRepository.saveAll(Common.createPrintConfig(user, companyId, companyName));

        TaskLogSendQueue taskLogSendQueue = transactionTemplate.execute(status -> {
            productUnitRepository.saveAll(Common.createUnitDefault(companyId));
            TaskLog taskLog = new TaskLog();
            taskLog.setComId(companyId);
            taskLog.setStatus(TaskLogConstants.Status.PROCESSING);
            taskLog.setContent(companyId.toString());
            taskLog.setType(TaskLogConstants.Type.EB_ASYNC_PRODUCT_UNIT);
            taskLogRepository.save(taskLog);
            return new TaskLogSendQueue(taskLog.getId().toString(), taskLog.getType());
        });
        if (taskLogSendQueue != null) {
            userService.sendTaskLog(taskLogSendQueue);
        }
    }

    private void checkBusinessId(Integer id) {
        if (businessRepository.countById(id) != 1) {
            throw new InternalServerException(
                ExceptionConstants.BUSINESS_NOT_EXISTS_VI,
                ENTITY_NAME,
                ExceptionConstants.BUSINESS_NOT_EXISTS_CODE
            );
        }
    }

    private TaskLogSendQueue createAndPublishQueueTask(int comId, boolean isNew, String taskType) throws Exception {
        CompanyUnitTask task = new CompanyUnitTask();
        task.setComId(comId);
        task.setIsNew(isNew);

        TaskLog taskLog = new TaskLog();
        taskLog.setComId(comId);
        taskLog.setStatus(TaskLogConstants.Status.PROCESSING);
        //New objectMapper because we need disable a feature
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.INDENT_OUTPUT);
        taskLog.setContent(objectMapper.writeValueAsString(task));
        taskLog.setType(taskType);
        taskLog = taskLogRepository.save(taskLog);
        return new TaskLogSendQueue(taskLog.getId().toString(), taskLog.getType());
    }

    private void checkCompanyNameDuplicate(String name, Integer comId) {
        if (companyRepository.countByNameAndOwnerId(name, comId) > 0) {
            throw new BadRequestAlertException(
                ExceptionConstants.COMPANY_NAME_DUPLICATE_VI,
                ENTITY_NAME,
                ExceptionConstants.COMPANY_NAME_DUPLICATE_CODE
            );
        }
    }

    private CompanyOwner checkOwnerIdExists(Integer id) {
        Optional<CompanyOwner> ownerOptional = companyOwnerRepository.findById(id);
        if (ownerOptional.isEmpty()) {
            throw new InternalServerException(
                ExceptionConstants.OWNER_ID_NOT_EXISTS_VI,
                ENTITY_NAME,
                ExceptionConstants.OWNER_ID_NOT_EXISTS_CODE
            );
        }
        return ownerOptional.get();
    }

    @Override
    public ResultDTO getById(Integer id) {
        CompanyResult result = companyRepository.getCompanyById(id);
        if (result == null) {
            throw new InternalServerException(
                ExceptionConstants.COMPANY_NOT_EXISTS_CODE_VI,
                ENTITY_NAME,
                ExceptionConstants.COMPANY_NOT_EXISTS_CODE
            );
        }
        log.info(ENTITY_NAME + "_getById: " + ResultConstants.SUCCESS_GET_COMPANY);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_GET_COMPANY, true, result);
    }

    @Override
    public ResultDTO getAllByOwnerId(Integer ownerId) {
        List<Company> companies = companyRepository.getAllByOwnerId(ownerId);
        List<CompanyResponse> companyResponses = Arrays.asList(modelMapper.map(companies, CompanyResponse[].class));

        log.debug(ENTITY_NAME + "_getAllByOwnerId: " + ResultConstants.SUCCESS_GET_COMPANY);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_GET_COMPANY, true, companyResponses);
    }

    @Override
    public ResultDTO getAllItems(String keyword) {
        List<CompanyItemsResult> companies = companyRepository.getAllItems(keyword == null ? "" : Common.normalizedName(List.of(keyword)));
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_GET_COMPANY, true, companies);
    }
}
