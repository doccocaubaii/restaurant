package vn.softdreams.easypos.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import vn.softdreams.easypos.constants.ProcessingAreaConstants;
import vn.softdreams.easypos.constants.ResultConstants;
import vn.softdreams.easypos.domain.*;
import vn.softdreams.easypos.dto.processingArea.ProcessingAreaItemResponse;
import vn.softdreams.easypos.dto.processingArea.ProcessingAreaRequest;
import vn.softdreams.easypos.dto.processingArea.ProcessingAreaResponse;
import vn.softdreams.easypos.dto.processingArea.ProductProcessingAreaResponse;
import vn.softdreams.easypos.dto.processingAreaProduct.ProcessingAreaProductItemResponse;
import vn.softdreams.easypos.repository.*;
import vn.softdreams.easypos.service.ProcessingAreaManagementService;
import vn.softdreams.easypos.service.UserService;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.util.Common;
import vn.softdreams.easypos.web.rest.errors.BadRequestAlertException;
import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link ProcessingArea}.
 */
@Service
@Transactional
public class ProcessingAreaManagementServiceImpl implements ProcessingAreaManagementService {

    private final Logger log = LoggerFactory.getLogger(ProcessingAreaManagementServiceImpl.class);
    private final String ENTITY_NAME = "processing-area";

    private final ProcessingAreaRepository processingAreaRepository;
    private final ProcessingAreaProductRepository processingAreaProductRepository;
    private final ProductProductUnitRepository productProductUnitRepository;
    private final ProcessingAreaRepositoryCustom processingAreaRepositoryCustom;
    private final ProcessingAreaProductRepositoryCustom processingAreaProductRepositoryCustom;
    private final ConfigRepository configRepository;
    private final PrintSettingRepository printSettingRepository;
    private final PrintTemplateRepository printTemplateRepository;
    private final UserService userService;
    private final TransactionTemplate transactionTemplate;

    public ProcessingAreaManagementServiceImpl(
        ProcessingAreaRepository processingAreaRepository,
        ProcessingAreaProductRepository processingAreaProductRepository,
        ProductProductUnitRepository productProductUnitRepository,
        ProcessingAreaRepositoryCustom processingAreaRepositoryCustom,
        ProcessingAreaProductRepositoryCustom processingAreaProductRepositoryCustom,
        ConfigRepository configRepository,
        PrintSettingRepository printSettingRepository,
        PrintTemplateRepository printTemplateRepository,
        UserService userService,
        TransactionTemplate transactionTemplate
    ) {
        this.processingAreaRepository = processingAreaRepository;
        this.processingAreaProductRepository = processingAreaProductRepository;
        this.productProductUnitRepository = productProductUnitRepository;
        this.processingAreaRepositoryCustom = processingAreaRepositoryCustom;
        this.processingAreaProductRepositoryCustom = processingAreaProductRepositoryCustom;
        this.configRepository = configRepository;
        this.printSettingRepository = printSettingRepository;
        this.printTemplateRepository = printTemplateRepository;
        this.userService = userService;
        this.transactionTemplate = transactionTemplate;
    }

    @Override
    public ResultDTO filter(
        Integer comId,
        String name,
        Integer setting,
        Integer active,
        String fromDate,
        String toDate,
        Pageable pageable
    ) {
        User user = userService.getUserWithAuthorities();
        Optional<String> typeStr = configRepository.getValueByComIdAndCode(user.getCompanyId(), "business_type");
        Integer type = null;
        if (typeStr.isPresent()) {
            type = Integer.parseInt(typeStr.get());
        } else {
            throw new BadRequestAlertException(
                ExceptionConstants.PROCESSING_AREA_BUSINESS_TYPE_NOT_F_AND_B_VI,
                ENTITY_NAME,
                ExceptionConstants.PROCESSING_AREA_BUSINESS_TYPE_NOT_F_AND_B
            );
        }
        if (type == 0) {
            log.debug("Request to filter processingArea");
            Page<ProcessingAreaItemResponse> page = processingAreaRepositoryCustom.filter(
                user.getCompanyId(),
                name,
                setting,
                active,
                fromDate,
                toDate,
                pageable
            );
            List<ProcessingAreaItemResponse> responses = page.getContent();
            Map<Integer, Map<String, List<Integer>>> printMap = new HashMap<>();
            List<Integer> ids = responses.stream().map(ProcessingAreaItemResponse::getId).collect(Collectors.toList());
            List<PrintSetting> printSettings = printSettingRepository.findByComIdAndProcessingAreaIdIn(user.getCompanyId(), ids);
            List<PrintTemplate> printTemplates = printTemplateRepository.findAllByComId(user.getCompanyId());
            Map<Integer, List<Integer>> typeTemplateMap = new HashMap<>();
            for (PrintTemplate printTemplate : printTemplates) {
                List<Integer> printIds = new ArrayList<>();
                if (typeTemplateMap.containsKey(printTemplate.getTypeTemplate())) {
                    printIds = typeTemplateMap.get(printTemplate.getTypeTemplate());
                }
                printIds.add(printTemplate.getId());
                typeTemplateMap.put(printTemplate.getTypeTemplate(), printIds);
            }
            for (PrintSetting printSetting : printSettings) {
                Map<String, List<Integer>> childMap = new HashMap<>();
                if (printMap.containsKey(printSetting.getProcessingAreaId())) {
                    childMap = printMap.get(printSetting.getProcessingAreaId());
                }
                List<Integer> typeTemplates = new ArrayList<>();
                if (childMap.containsKey(printSetting.getPrintName())) {
                    typeTemplates = childMap.get(printSetting.getPrintName());
                }
                typeTemplates.add(printSetting.getPrintTemplateId());
                childMap.put(printSetting.getPrintName(), typeTemplates);
                printMap.put(printSetting.getProcessingAreaId(), childMap);
            }
            for (ProcessingAreaItemResponse response : responses) {
                if (printMap.containsKey(response.getId())) {
                    Map<String, List<Integer>> childMap = printMap.get(response.getId());
                    response.setPrinterInfo(new ArrayList<>(childMap.keySet()));
                    for (Map.Entry<String, List<Integer>> entry : childMap.entrySet()) {
                        response.setPrintTemplateId(entry.getValue());
                    }
                }
            }
            return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_GET_LIST, true, responses, (int) page.getTotalElements());
        }
        throw new BadRequestAlertException(
            ExceptionConstants.PROCESSING_AREA_BUSINESS_TYPE_NOT_F_AND_B_VI,
            ENTITY_NAME,
            ExceptionConstants.PROCESSING_AREA_BUSINESS_TYPE_NOT_F_AND_B
        );
    }

    @Override
    public ResultDTO save(ProcessingAreaRequest request) {
        User user = userService.getUserWithAuthorities();
        ProcessingArea processingArea = new ProcessingArea();
        if (user.getCompanyId() == null || !request.getComId().equals(user.getCompanyId())) {
            throw new BadRequestAlertException(ExceptionConstants.COM_ID_NOT_VALID_VI, ENTITY_NAME, ExceptionConstants.COM_ID_NOT_VALID);
        }
        if (request.getName() == null || request.getName() == "") {
            throw new BadRequestAlertException(ExceptionConstants.NAME_NOT_VALID_VI, ENTITY_NAME, ExceptionConstants.NAME_NOT_VALID);
        }
        String name = request.getName().trim();
        Integer count = processingAreaRepository.countAllByComIdAndNameAndActiveIsNot(
            user.getCompanyId(),
            name,
            ProcessingAreaConstants.ProcessingAreaActive.DELETE
        );
        if (count > 0) {
            throw new BadRequestAlertException(ExceptionConstants.DUPLICATE_NAME_VI, ENTITY_NAME, ExceptionConstants.DUPLICATE_NAME);
        }
        processingArea.setComId(user.getCompanyId());
        processingArea.setName(name);
        processingArea.setSetting(ProcessingAreaConstants.ProcessingAreaSetting.BOTH);
        processingArea.setActive(ProcessingAreaConstants.ProcessingAreaActive.ACTIVE);
        processingArea.setNormalizedName(Common.normalizedName(Arrays.asList(request.getName())));
        ProcessingArea processingAreaResponse = processingAreaRepository.save(processingArea);
        ProcessingAreaResponse response = new ProcessingAreaResponse();
        response.setProcessingArea(processingAreaResponse);
        List<ProcessingAreaProduct> list = new ArrayList<>();

        List<Integer> listProduct = new ArrayList<>();
        List<ProductProductUnit> productUnitList = productProductUnitRepository.findAllByComIdAndIdIn(
            user.getCompanyId(),
            request.getListProduct()
        );
        if (
            request.getListProduct().size() > productUnitList.size() ||
            request.getListProduct().size() > productUnitList.size() &&
            productUnitList.size() == 0
        ) {
            throw new BadRequestAlertException(ExceptionConstants.INVALID_PRODUCT_VI, ENTITY_NAME, ExceptionConstants.INVALID_PRODUCT);
        }
        if (productUnitList.size() > 0) {
            for (ProductProductUnit item : productUnitList) {
                item.setProcessingAreas(List.of(processingArea));
                listProduct.add(item.getId());
            }
        }

        transactionTemplate.execute(status -> processingAreaRepository.save(processingArea));
        List<ProcessingAreaProduct> processingAreaProductList = processingAreaProductRepository.findByProcessingAreaIdAndComIdIsNull(
            processingArea.getId()
        );
        for (ProcessingAreaProduct processingAreaProduct : processingAreaProductList) {
            processingAreaProduct.setComId(user.getCompanyId());
            processingAreaProduct.setCreator(user.getId());
            processingAreaProduct.setUpdater(user.getId());
            processingAreaProduct.setCreateTime(ZonedDateTime.now());
            processingAreaProduct.setUpdateTime(ZonedDateTime.now());
            list.add(processingAreaProduct);
        }
        response.setProcessingAreaProductList(list);
        //        if (listProduct.size() > 0) {
        //            for (Integer productProductUnitId : listProduct) {
        //                ProcessingAreaProduct processingAreaProductNew = new ProcessingAreaProduct();
        //                processingAreaProductNew.setComId(user.getCompanyId());
        //                processingAreaProductNew.setProcessingAreaId(processingAreaResponse.getId());
        //                processingAreaProductNew.setProductProductUnitId(productProductUnitId);
        //                ProcessingAreaProduct processingAreaProduct2 = processingAreaProductRepository.save(processingAreaProductNew);
        //                list.add(processingAreaProduct2);
        //            }
        //
        //            response.setProcessingAreaProductList(list);
        //        }
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.CREATE_PROCESSING_AREA_SUCCESS, true, response);
    }

    @Override
    public ResultDTO delete(Integer id) {
        User user = userService.getUserWithAuthorities();
        Optional<ProcessingArea> result = processingAreaRepository.findByIdAndComId(id, user.getCompanyId());
        ProcessingArea response;
        if (result.isPresent()) {
            response = result.get();
        } else {
            throw new BadRequestAlertException(
                ExceptionConstants.PROCESSING_AREA_NOT_EXIT_VI,
                ENTITY_NAME,
                ExceptionConstants.PROCESSING_AREA_NOT_EXIT
            );
        }
        List<ProcessingAreaProduct> productList = processingAreaProductRepository.getProcessingAreaProductByProcessingAreaId(
            user.getCompanyId(),
            response.getId()
        );
        if (productList.size() > 0) {
            for (ProcessingAreaProduct item : productList) {
                processingAreaProductRepository.delete(item);
            }
        }
        List<ProcessingAreaProduct> productList2 = processingAreaProductRepository.getProcessingAreaProductByProcessingAreaId(
            user.getCompanyId(),
            response.getId()
        );
        if (productList2.size() == 0) {
            processingAreaRepository.delete(response);
        }
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.DELETE_PROCESSING_AREA_SUCCESS, true);
    }

    @Override
    public ResultDTO getProcessingArea(Integer id) {
        ProcessingAreaResponse response = new ProcessingAreaResponse();
        User user = userService.getUserWithAuthorities();
        Optional<ProcessingArea> result = processingAreaRepository.findByIdAndComId(id, user.getCompanyId());
        if (result.isPresent()) {
            response.setProcessingArea(result.get());
        } else {
            throw new BadRequestAlertException(
                ExceptionConstants.PROCESSING_AREA_NOT_EXIT_VI,
                ENTITY_NAME,
                ExceptionConstants.PROCESSING_AREA_NOT_EXIT
            );
        }
        Pageable pageable = null;
        Page<ProcessingAreaProductItemResponse> page = processingAreaProductRepositoryCustom.getProductByProcessingAreaId(
            response.getProcessingArea().getId(),
            pageable
        );
        response.setProcessingAreaProductItemResponses(page.getContent());
        Integer count = 0;
        for (int i = 0; i < page.getContent().size(); i++) {
            count++;
        }
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.FIND_PROCESSING_AREA_SUCCESS, true, response, count);
    }

    @Override
    public ResultDTO filterProduct(Integer comId, Integer processingAreaId, String keyword, Pageable pageable) {
        User user = userService.getUserWithAuthorities();
        Page<ProductProcessingAreaResponse> page = processingAreaRepositoryCustom.filterProduct(
            user.getCompanyId(),
            processingAreaId,
            keyword,
            pageable
        );
        return new ResultDTO(
            ResultConstants.SUCCESS,
            ResultConstants.SUCCESS_GET_LIST,
            true,
            page.getContent(),
            (int) page.getTotalElements()
        );
    }

    @Override
    public ResultDTO update(ProcessingAreaRequest request) {
        User user = userService.getUserWithAuthorities();
        ProcessingArea processingArea = new ProcessingArea();
        if (user.getCompanyId() == null || !request.getComId().equals(user.getCompanyId())) {
            throw new BadRequestAlertException(ExceptionConstants.COM_ID_NOT_VALID_VI, ENTITY_NAME, ExceptionConstants.COM_ID_NOT_VALID);
        }
        if (request.getName() == null || request.getName() == "") {
            throw new BadRequestAlertException(ExceptionConstants.NAME_NOT_VALID_VI, ENTITY_NAME, ExceptionConstants.NAME_NOT_VALID);
        }
        String name = request.getName().trim();
        Integer count;
        if (request.getId() == null) {
            throw new BadRequestAlertException(
                ExceptionConstants.PROCESSING_AREA_NOT_EXIT_VI,
                ENTITY_NAME,
                ExceptionConstants.PROCESSING_AREA_NOT_EXIT
            );
        } else {
            processingArea.setId(request.getId());
            count =
                processingAreaRepository.countAllByComIdAndNameAndActiveIsNotAndIdNot(
                    user.getCompanyId(),
                    name,
                    ProcessingAreaConstants.ProcessingAreaActive.DELETE,
                    request.getId()
                );
        }
        if (count > 0) {
            throw new BadRequestAlertException(ExceptionConstants.DUPLICATE_NAME_VI, ENTITY_NAME, ExceptionConstants.DUPLICATE_NAME);
        }
        Optional<ProcessingArea> result = processingAreaRepository.findByIdAndComId(request.getId(), user.getCompanyId());
        ProcessingArea processingAreaCheck;
        if (result.isPresent()) {
            processingAreaCheck = result.get();
            if (processingAreaCheck.getActive() == ProcessingAreaConstants.ProcessingAreaActive.DELETE) {
                throw new BadRequestAlertException(
                    ExceptionConstants.PROCESSING_AREA_DELETE_VI,
                    ENTITY_NAME,
                    ExceptionConstants.PROCESSING_AREA_DELETE
                );
            }
        } else {
            throw new BadRequestAlertException(
                ExceptionConstants.PROCESSING_AREA_NOT_EXIT_VI,
                ENTITY_NAME,
                ExceptionConstants.PROCESSING_AREA_NOT_EXIT
            );
        }
        processingArea.setComId(user.getCompanyId());
        processingArea.setName(name);
        processingArea.setSetting(ProcessingAreaConstants.ProcessingAreaSetting.BOTH);
        processingArea.setActive(ProcessingAreaConstants.ProcessingAreaActive.ACTIVE);
        processingArea.setNormalizedName(Common.normalizedName(Arrays.asList(request.getName())));
        ProcessingArea processingAreaResponse = processingAreaRepository.save(processingArea);
        ProcessingAreaResponse response = new ProcessingAreaResponse();
        response.setProcessingArea(processingAreaResponse);
        List<ProcessingAreaProduct> list = new ArrayList<>();

        List<Integer> listProduct = new ArrayList<>();
        List<ProductProductUnit> productUnitList = productProductUnitRepository.findAllByComIdAndIdIn(
            user.getCompanyId(),
            request.getListProduct()
        );
        if (
            request.getListProduct().size() > productUnitList.size() ||
            request.getListProduct().size() > productUnitList.size() &&
            productUnitList.size() == 0
        ) {
            throw new BadRequestAlertException(ExceptionConstants.INVALID_PRODUCT_VI, ENTITY_NAME, ExceptionConstants.INVALID_PRODUCT);
        }
        if (productUnitList.size() > 0) {
            for (ProductProductUnit item : productUnitList) {
                item.setProcessingAreas(List.of(processingArea));
                listProduct.add(item.getId());
            }
        }

        transactionTemplate.execute(status -> processingAreaRepository.save(processingArea));
        List<ProcessingAreaProduct> processingAreaProductList = processingAreaProductRepository.findByProcessingAreaIdAndComIdIsNull(
            processingArea.getId()
        );
        for (ProcessingAreaProduct processingAreaProduct : processingAreaProductList) {
            processingAreaProduct.setComId(user.getCompanyId());
            processingAreaProduct.setCreator(user.getId());
            processingAreaProduct.setUpdater(user.getId());
            processingAreaProduct.setCreateTime(ZonedDateTime.now());
            processingAreaProduct.setUpdateTime(ZonedDateTime.now());
            list.add(processingAreaProduct);
        }
        response.setProcessingAreaProductList(list);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.UPDATE_PROCESSING_AREA_SUCCESS, true, response);
    }
}
