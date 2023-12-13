package vn.softdreams.easypos.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.MultipartFile;
import vn.softdreams.easypos.config.Constants;
import vn.softdreams.easypos.constants.*;
import vn.softdreams.easypos.domain.*;
import vn.softdreams.easypos.dto.TaskLogSendQueue;
import vn.softdreams.easypos.dto.UnitResponse;
import vn.softdreams.easypos.dto.backup.*;
import vn.softdreams.easypos.dto.importExcel.ValidateImportResponse;
import vn.softdreams.easypos.dto.product.*;
import vn.softdreams.easypos.dto.productGroup.ProductGroupItemResult;
import vn.softdreams.easypos.dto.productGroup.ProductGroupResult;
import vn.softdreams.easypos.dto.productGroup.ProductProcessingAreaDetail;
import vn.softdreams.easypos.dto.productGroup.ProductProcessingAreaResult;
import vn.softdreams.easypos.dto.productProductUnit.GetByIdsRequest;
import vn.softdreams.easypos.dto.productUnit.ConversionUnitRequest;
import vn.softdreams.easypos.dto.productUnit.ProductProductUnitResponse;
import vn.softdreams.easypos.dto.toppingGroup.*;
import vn.softdreams.easypos.integration.easybooks88.api.EB88ApiClient;
import vn.softdreams.easypos.integration.easybooks88.api.dto.UnitEb88Response;
import vn.softdreams.easypos.integration.easybooks88.queue.EB88Producer;
import vn.softdreams.easypos.integration.easybooks88.queue.dto.AccountingObjectTask;
import vn.softdreams.easypos.integration.easybooks88.queue.dto.MaterialGoodsTask;
import vn.softdreams.easypos.integration.easybooks88.queue.dto.ProductUnitTask;
import vn.softdreams.easypos.integration.easybooks88.queue.dto.RsInOutWardTask;
import vn.softdreams.easypos.repository.*;
import vn.softdreams.easypos.service.ProductManagementService;
import vn.softdreams.easypos.service.UserService;
import vn.softdreams.easypos.service.dto.DataResponse;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.util.Common;
import vn.softdreams.easypos.util.ImagePathConstants;
import vn.softdreams.easypos.web.rest.errors.BadRequestAlertException;
import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;
import vn.softdreams.easypos.web.rest.errors.InternalServerException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static vn.softdreams.easypos.config.Constants.DEFAULT_PRODUCT_CODE;
import static vn.softdreams.easypos.constants.ResultConstants.*;
import static vn.softdreams.easypos.web.rest.errors.ExceptionConstants.*;

@Service
@Transactional
public class ProductManagementServiceImpl implements ProductManagementService {

    private final Logger log = LoggerFactory.getLogger(ProductManagementServiceImpl.class);
    private static final String ENTITY_NAME = "product";
    private final ProductRepository productRepository;
    private final McPaymentRepository mcPaymentRepository;
    private final ProductProductGroupRepository productProductGroupRepository;
    private final ProductGroupRepository productGroupRepository;
    private final BusinessTypeRepository businessTypeRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final ProductUnitRepository productUnitRepository;
    private final TaskLogRepository taskLogRepository;
    private final ObjectMapper objectMapper;
    private final EB88Producer eb88Producer;
    private final CompanyRepository companyRepository;
    private final CustomerRepository customerRepository;
    private final ProductProductUnitRepository productProductUnitRepository;
    private final RsInoutWardRepository rsInoutWardRepository;
    private final BillProductRepository billProductRepository;
    private final ProductProductUnitCustom productProductUnitCustom;
    private final ConfigRepository configRepository;
    private final ToppingGroupRepository toppingGroupRepository;
    private final ToppingToppingGroupRepository toppingToppingGroupRepository;
    private final ProductToppingRepository productToppingRepository;
    private final ProcessingAreaRepository processingAreaRepository;
    private final ProcessingAreaProductRepository processingAreaProductRepository;
    private final TransactionTemplate transactionTemplate;
    private final BillRepository billRepository;

    public ProductManagementServiceImpl(
        ProductRepository productRepository,
        McPaymentRepository mcPaymentRepository,
        ProductProductGroupRepository productProductGroupRepository,
        BusinessTypeRepository businessTypeRepository,
        UserService userService,
        UserRepository userRepository,
        ProductGroupRepository productGroupRepository,
        ModelMapper modelMapper,
        ProductUnitRepository productUnitRepository,
        TaskLogRepository taskLogRepository,
        ObjectMapper objectMapper,
        EB88Producer eb88Producer,
        CompanyRepository companyRepository,
        CustomerRepository customerRepository,
        ProductProductUnitRepository productProductUnitRepository,
        RsInoutWardRepository rsInoutWardRepository,
        BillProductRepository billProductRepository,
        ProductProductUnitCustom productProductUnitCustom,
        ConfigRepository configRepository,
        ToppingGroupRepository toppingGroupRepository,
        ToppingToppingGroupRepository toppingToppingGroupRepository,
        ProductToppingRepository productToppingRepository,
        ProcessingAreaRepository processingAreaRepository,
        ProcessingAreaProductRepository processingAreaProductRepository,
        TransactionTemplate transactionTemplate,
        BillRepository billRepository
    ) {
        this.productRepository = productRepository;
        this.mcPaymentRepository = mcPaymentRepository;
        this.productProductGroupRepository = productProductGroupRepository;
        this.businessTypeRepository = businessTypeRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.productGroupRepository = productGroupRepository;
        this.modelMapper = modelMapper;
        this.productUnitRepository = productUnitRepository;
        this.taskLogRepository = taskLogRepository;
        this.objectMapper = objectMapper;
        this.eb88Producer = eb88Producer;
        this.companyRepository = companyRepository;
        this.customerRepository = customerRepository;
        this.productProductUnitRepository = productProductUnitRepository;
        this.rsInoutWardRepository = rsInoutWardRepository;
        this.billProductRepository = billProductRepository;
        this.productProductUnitCustom = productProductUnitCustom;
        this.configRepository = configRepository;
        this.toppingGroupRepository = toppingGroupRepository;
        this.toppingToppingGroupRepository = toppingToppingGroupRepository;
        this.productToppingRepository = productToppingRepository;
        this.processingAreaRepository = processingAreaRepository;
        this.processingAreaProductRepository = processingAreaProductRepository;
        this.transactionTemplate = transactionTemplate;
        this.billRepository = billRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public ResultDTO getAllForOffline() {
        User user = userService.getUserWithAuthorities();
        List<ProductItemResponse> productResponses = productRepository.getAllForOffline(user.getCompanyId());
        productResponses = getProductResult(productResponses, user.getCompanyId(), Boolean.TRUE);
        return new ResultDTO(SUCCESS, GET_PRODUCTS_SUCCESS_VI, true, productResponses, productResponses.size());
    }

    @Override
    public ResultDTO getAllForOfflineForProduct() {
        User user = userService.getUserWithAuthorities();
        List<ProductDetailResponse> productResponses = productRepository.getAllForOfflineForProduct(user.getCompanyId());
        productResponses = getProductItemResult(productResponses, user.getCompanyId());
        return new ResultDTO(SUCCESS, GET_PRODUCTS_SUCCESS_VI, true, productResponses, productResponses.size());
    }

    @Override
    @Transactional(readOnly = true)
    public ResultDTO getWithPaging(Pageable pageable, String keyword, Integer productGroupId, Boolean isTopping, Boolean isCountAll) {
        User user = userService.getUserWithAuthorities();
        Page<ProductItemResponse> productPage = productRepository.getWithPaging(
            pageable,
            user.getCompanyId(),
            productGroupId,
            keyword,
            isTopping,
            isCountAll
        );
        List<ProductItemResponse> productResponses = productPage.getContent();
        productResponses = getProductResult(productResponses, user.getCompanyId(), Boolean.FALSE);
        return new ResultDTO(SUCCESS, GET_PRODUCTS_SUCCESS_VI, true, productResponses, (int) productPage.getTotalElements());
    }

    @Override
    public ResultDTO getWithPaging2(Pageable pageable, String keyword, List<Integer> groupIds) {
        User user = userService.getUserWithAuthorities();
        List<ProductItemResponse> productResponses = productRepository.getWithPaging2(pageable, user.getCompanyId(), groupIds, keyword);
        Map<Integer, ProductItemResponse> productMap = new LinkedHashMap<>();
        for (ProductItemResponse response : productResponses) {
            productMap.put(response.getProductProductUnitId(), response);
        }
        productResponses = getProductResult(new ArrayList<>(productMap.values()), user.getCompanyId(), Boolean.FALSE);
        return new ResultDTO(SUCCESS, GET_PRODUCTS_SUCCESS_VI, true, productResponses, productResponses.size());
    }

    @Override
    public ResultDTO getWithPagingForProduct(
        Pageable pageable,
        String keyword,
        Integer productGroupId,
        Boolean isTopping,
        Boolean isCountAll,
        List<Integer> ids
    ) {
        User user = userService.getUserWithAuthorities();
        Page<ProductDetailResponse> productPage = productRepository.getWithPagingForProduct(
            pageable,
            user.getCompanyId(),
            productGroupId,
            keyword,
            isTopping,
            isCountAll,
            ids,
            false,
            null
        );
        List<ProductDetailResponse> productResponses = productPage.getContent();
        if (!productResponses.isEmpty() && productResponses.get(0).getCode().equals(DEFAULT_PRODUCT_CODE)) {
            productResponses = productResponses.subList(1, productResponses.size());
        }
        productResponses = getProductItemResult(productResponses, user.getCompanyId());
        return new ResultDTO(SUCCESS, GET_PRODUCTS_SUCCESS_VI, true, productResponses, (int) productPage.getTotalElements());
    }

    public List<ProductResponse> moveDefaultProductToTop(List<ProductResponse> productResponses, ProductResponse defaultProduct) {
        productResponses.remove(defaultProduct);
        productResponses.add(0, defaultProduct);
        return productResponses;
    }

    public ProductResponse getDefaultProduct(List<ProductResponse> productResponses) {
        ProductResponse defaultProduct = null;
        for (ProductResponse productResponse : productResponses) {
            if (productResponse.getCode().equals(DEFAULT_PRODUCT_CODE)) {
                defaultProduct = productResponse;
                break;
            }
        }
        return defaultProduct;
    }

    private List<ProductItemResponse> getProductResult(List<ProductItemResponse> productResponses, Integer companyId, Boolean isOffline) {
        List<Integer> productIds = productResponses.stream().map(ProductItemResponse::getProductId).collect(Collectors.toList());
        Set<Integer> productProductUnitIds = productResponses
            .stream()
            .map(ProductItemResponse::getProductProductUnitId)
            .collect(Collectors.toSet());
        List<ProductGroupResult> productGroupResults = productGroupRepository.getAllForOffline(companyId, productIds);
        List<Integer> parentProductIds = productToppingRepository.findAllProductId();
        List<ProductProcessingAreaResult> processingAreaList = processingAreaRepository.findForProductResult(
            companyId,
            productProductUnitIds
        );
        Map<Integer, List<ToppingGroupItemResponse>> toppingItemOfflineMap = new HashMap<>();
        if (isOffline) {
            toppingItemOfflineMap = getListToppingForOffline();
        }
        Map<Integer, List<ProductGroupItemResult>> productGroupMap = new LinkedHashMap<>();
        Map<Integer, ProductProcessingAreaResult> processingAreaMap = new LinkedHashMap<>();

        for (ProductGroupResult result : productGroupResults) {
            List<ProductGroupItemResult> groupResults = new ArrayList<>();
            if (productGroupMap.containsKey(result.getProductId())) {
                groupResults = productGroupMap.get(result.getProductId());
            }
            ProductGroupItemResult item = new ProductGroupItemResult();
            BeanUtils.copyProperties(result, item);
            groupResults.add(item);
            productGroupMap.put(result.getProductId(), groupResults);
        }

        for (ProductProcessingAreaResult result : processingAreaList) {
            processingAreaMap.put(result.getProductProductUnitId(), result);
        }

        for (ProductItemResponse response : productResponses) {
            if (productGroupMap.containsKey(response.getProductId())) {
                response.setGroups(productGroupMap.get(response.getProductId()));
            }
            if (toppingItemOfflineMap.containsKey(response.getProductId())) {
                response.setGroupToppings(toppingItemOfflineMap.get(response.getProductId()));
            }
            if (processingAreaMap.containsKey(response.getProductProductUnitId())) {
                response.setProcessingArea(processingAreaMap.get(response.getProductProductUnitId()));
            }
            response.setIsHaveTopping(parentProductIds.contains(response.getProductId()));
        }

        return productResponses;
    }

    private List<ProductDetailResponse> getProductItemResult(List<ProductDetailResponse> productItems, Integer companyId) {
        List<Integer> productIds = productItems.stream().map(ProductDetailResponse::getId).collect(Collectors.toList());
        List<ProductGroupResult> productGroupResults = productGroupRepository.getAllForOffline(companyId, productIds);
        List<ProductProductUnit> units = productProductUnitRepository.findAllByComIdAndIsPrimaryIsNotNullOrderByIsPrimaryDesc(companyId);
        Map<Integer, List<ProductGroupResult>> productGroupMap = new LinkedHashMap<>();
        Map<Integer, List<ProductProductUnitResponse>> productUnitMap = new LinkedHashMap<>();

        for (ProductProductUnit unit : units) {
            List<ProductProductUnitResponse> productUnits = new ArrayList<>();
            if (productUnitMap.containsKey(unit.getProductId())) {
                productUnits = productUnitMap.get(unit.getProductId());
            }
            ProductProductUnitResponse response = new ProductProductUnitResponse();
            modelMapper.map(unit, response);
            productUnits.add(response);
            productUnitMap.put(unit.getProductId(), productUnits);
        }

        for (ProductGroupResult result : productGroupResults) {
            List<ProductGroupResult> groupResults = new ArrayList<>();
            if (productGroupMap.containsKey(result.getProductId())) {
                groupResults = productGroupMap.get(result.getProductId());
            }
            groupResults.add(result);
            productGroupMap.put(result.getProductId(), groupResults);
        }

        for (ProductDetailResponse response : productItems) {
            if (productGroupMap.containsKey(response.getId())) {
                response.setGroups(productGroupMap.get(response.getId()));
            }
            if (productUnitMap.containsKey(response.getId())) {
                response.setConversionUnits(productUnitMap.get(response.getId()));
            }
        }
        return productItems;
    }

    @Override
    public ResultDTO delete(Integer id) {
        //                Kiem tra dang nhap
        log.debug("Request to delete Product : {}", id);
        User user = userService.getUserWithAuthorities();
        //        Integer productId = productProductUnitRepository.findProductIdById(id);
        // Lay san pham
        Optional<Product> productOptional = productRepository.findByIdAndComId(id, user.getCompanyId());
        if (productOptional.isEmpty()) {
            throw new InternalServerException(PRODUCT_NOT_FOUND_VI, ENTITY_NAME, PRODUCT_NOT_FOUND);
        }
        //        boolean isExist = productRepository.checkProductExistInInvoice(id, user.getCompanyId());
        //        if (!isExist) {
        //            throw new BadRequestAlertException(PRODUCT_NOT_DELETE_VI, ENTITY_NAME, PRODUCT_NOT_DELETE);
        //        }
        Product product = productOptional.get();
        if (
            List
                .of(Constants.PRODUCT_CODE_DEFAULT, Constants.PRODUCT_CODE_NOTE_DEFAULT, Constants.PRODUCT_CODE_PROMOTION_DEFAULT)
                .contains(product.getCode())
        ) {
            throw new BadRequestAlertException(PRODUCT_DEFAULT_NOT_DELETE_VI, ENTITY_NAME, PRODUCT_NOT_DELETE);
        }
        if (productRepository.countByStatusAndProductId(id) > 0) {
            throw new BadRequestAlertException(PRODUCT_NOT_DELETE_VI.replace("@@name", product.getName()), ENTITY_NAME, PRODUCT_NOT_DELETE);
        }
        List<ToppingToppingGroup> groups = toppingToppingGroupRepository.findAllByProductId(product.getId());
        List<Integer> ids = groups.stream().map(ToppingToppingGroup::getToppingGroupId).collect(Collectors.toList());
        List<UnitResponse> toppingGroupList = toppingToppingGroupRepository.getEmptyToppingGroup(ids);
        if (toppingGroupList != null && !toppingGroupList.isEmpty()) {
            throw new BadRequestAlertException(
                ExceptionConstants.PRODUCT_IN_TOPPING_GROUP_CANNOT_DELETE_VI
                    .replace("@@1", product.getName())
                    .replace("@@2", toppingGroupList.get(0).getName()),
                ENTITY_NAME,
                PRODUCT_IN_TOPPING_GROUP_CANNOT_DELETE
            );
        }
        // Thay doi trang thai
        product.setActive(false);
        productRepository.save(product);

        //        List<ProductProductUnit> units = productProductUnitRepository.findAllByProductIdAndComId(id, user.getCompanyId());
        //        productProductUnitRepository.deleteAll(units);
        List<ProductTopping> productToppings = productToppingRepository.findAllByProductIdOrToppingId(id, id);
        productToppingRepository.deleteAll(productToppings);
        List<ToppingToppingGroup> toppingToppingGroups = toppingToppingGroupRepository.findAllByProductId(id);
        toppingToppingGroupRepository.deleteAll(toppingToppingGroups);
        List<ProductProductUnit> productProductUnits = productProductUnitRepository.findAllByProductIdAndComId(id, user.getCompanyId());
        if (!productProductUnits.isEmpty()) {
            List<Integer> unitIds = productProductUnits.stream().map(ProductProductUnit::getId).collect(Collectors.toList());
            List<ProcessingAreaProduct> processingAreaProducts = processingAreaProductRepository.findByComIdAndProductProductUnitIdIn(
                user.getCompanyId(),
                unitIds
            );
            processingAreaProductRepository.deleteAll(processingAreaProducts);
        }
        // Update vật tư hàng hóa ở PMKT
        try {
            createAndPublishQueueTask(user.getCompanyId(), product.getId(), TaskLogConstants.Type.EB_UPDATE_MATERIAL_GOODS);
        } catch (Exception e) {
            log.error("Can not create queue task for eb88 updating material goods: {}", e.getMessage());
        }
        return new ResultDTO(SUCCESS, DELETE_PRODUCT_SUCCESS_VI, true);
    }

    @Override
    public ResultDTO deleteList(DeleteProductList req) {
        User user = userService.getUserWithAuthorities(req.getComId());
        Integer comId = user.getCompanyId();
        List<ProductDetailResponse> products = productRepository
            .getWithPagingForProduct(
                null,
                comId,
                req.getGroupId(),
                req.getKeyword(),
                null,
                Boolean.TRUE,
                null,
                req.getParamCheckAll(),
                req.getIds()
            )
            .getContent();
        List<TaskLog> taskLogs = new ArrayList<>();
        List<Object> listError = new ArrayList<>();
        List<Integer> idDelete = new ArrayList<>();

        for (ProductDetailResponse product : products) {
            if (product.getCode().equals(DEFAULT_PRODUCT_CODE)) {
                product.setNote(PRODUCT_DEFAULT_NOT_DELETE_VI);
                listError.add(product);
            } else if (productRepository.countByStatusAndProductId(product.getId()) > 0) {
                product.setNote(PRODUCT_NOT_DELETE_VI.replace("@@name", product.getName()));
                listError.add(product);
            } else {
                idDelete.add(product.getId());
                // create taskLog
                MaterialGoodsTask task = new MaterialGoodsTask();
                task.setComId("" + comId);
                task.setProductId("" + product.getId());
                TaskLog taskLog = new TaskLog();
                taskLog.setComId(comId);
                taskLog.setStatus(TaskLogConstants.Status.PROCESSING);
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.disable(SerializationFeature.INDENT_OUTPUT);
                try {
                    taskLog.setContent(objectMapper.writeValueAsString(task));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                taskLog.setType(TaskLogConstants.Type.EB_UPDATE_MATERIAL_GOODS);
                taskLogs.add(taskLog);
            }
        }

        List<Product> productList = productRepository.findAllByComIdAndIdIn(comId, idDelete);
        for (Product billProduct : productList) {
            billProduct.setActive(Boolean.FALSE);
        }
        productRepository.saveAll(productList);

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
        response.setCountAll(products.size());
        response.setCountFalse(listError.size());
        response.setDataFalse(listError);
        response.setCountSuccess(response.getCountAll() - response.getCountFalse());
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.DELETE_LIST_PRODUCT_UNIT_SUCCESS, true, response);
    }

    @Override
    public ResultDTO searchProductUnit(Pageable pageable, String keyword) {
        User user = userService.getUserWithAuthorities();
        Page<ProductUnitResponse> units = productUnitRepository.searchProductUnit(pageable, user.getCompanyId(), keyword, false, null);
        return new ResultDTO(SUCCESS, GET_ALL_PRODUCT_UNIT_SUCCESS, true, units.getContent(), (int) units.getTotalElements());
    }

    @Override
    public ResultDTO getDetailProductUnit(Integer id) {
        User user = userService.getUserWithAuthorities();
        Optional<ProductUnit> productUnitOptional = productUnitRepository.findByIdAndComId(id, user.getCompanyId());
        if (productUnitOptional.isEmpty()) {
            throw new BadRequestAlertException(PRODUCT_UNIT_NOT_FOUND_VI, ENTITY_NAME, ExceptionConstants.PRODUCT_UNIT_NOT_FOUND);
        }
        ProductUnitResponse response = new ProductUnitResponse();
        BeanUtils.copyProperties(productUnitOptional.get(), response);
        return new ResultDTO(SUCCESS, GET_PRODUCT_UNIT_SUCCESS, true, response);
    }

    @Override
    public ResultDTO deleteProductUnit(Integer id) {
        User user = userService.getUserWithAuthorities();
        Optional<ProductUnit> productUnit = productUnitRepository.findByIdAndComId(id, user.getCompanyId());
        if (productUnit.isEmpty()) {
            throw new BadRequestAlertException(PRODUCT_UNIT_NOT_FOUND_VI, ENTITY_NAME, ExceptionConstants.PRODUCT_UNIT_NOT_FOUND);
        }
        ProductUnit unit = productUnit.get();
        if (!unit.getActive()) {
            throw new BadRequestAlertException(PRODUCT_UNIT_CANNOT_DELETE_VI, ENTITY_NAME, PRODUCT_UNIT_CANNOT_DELETE);
        }
        if (billRepository.countAllForDeleteProductUnit(user.getCompanyId(), id, unit.getName()) > 0) {
            throw new BadRequestAlertException(PRODUCT_UNIT_CANNOT_DELETE_VI2, ENTITY_NAME, PRODUCT_UNIT_CANNOT_DELETE);
        }
        List<ProductImagesResult> ids = productProductUnitRepository.checkForDeleteProductUnit(id);
        if (ids != null && !ids.isEmpty()) {
            throw new BadRequestAlertException(
                PRODUCT_UNIT_CANNOT_DELETE_VI3.replace("@@name", ids.get(0).getImage()),
                ENTITY_NAME,
                PRODUCT_UNIT_CANNOT_DELETE
            );
        }
        List<ProductProductUnit> productProductUnits = productProductUnitRepository.findAllByComIdAndProductUnitId(user.getCompanyId(), id);
        if (productProductUnits != null && !productProductUnits.isEmpty()) {
            List<DeleteConversionUnitRequest> deleteList = new ArrayList<>();
            for (ProductProductUnit productProductUnit : productProductUnits) {
                if (productProductUnit.getIsPrimary()) {
                    productProductUnit.setProductUnitId(null);
                    productProductUnit.setUnitName("");
                    productProductUnit.setDescription("");
                    productProductUnit.setUnitNormalizedName(null);
                } else {
                    deleteList.add(new DeleteConversionUnitRequest(productProductUnit.getId(), productProductUnit.getComId()));
                }
            }
            for (DeleteConversionUnitRequest request : deleteList) {
                deleteProductUnit(request, Boolean.FALSE);
            }
        }
        unit.setActive(Boolean.FALSE);
        productUnitRepository.save(unit);
        return new ResultDTO(SUCCESS, ResultConstants.DELETE_PRODUCT_UNIT_SUCCESS_VI, true);
    }

    @Override
    public ResultDTO updateProductUnit(UpdateProductUnitRequest request) {
        User user = userService.getUserWithAuthorities(request.getComId());
        int companyId = user.getCompanyId();
        Optional<ProductUnit> optionalProductUnit = productUnitRepository.findByIdAndComId(request.getId(), companyId);
        if (optionalProductUnit.isEmpty()) {
            throw new BadRequestAlertException(UNIT_NOT_FOUND_VI, ENTITY_NAME, UNIT_NOT_FOUND);
        }
        String checkName = request.getUnitName().trim().toUpperCase();
        Optional<ProductUnit> productUnitOptional = productUnitRepository.findByComIdAndUppercaseName(
            companyId,
            checkName,
            request.getId()
        );
        if (productUnitOptional.isPresent()) throw new BadRequestAlertException(UNIT_NAME_IS_EXISTED_VI, ENTITY_NAME, UNIT_NAME_IS_EXISTED);
        ProductUnit productUnit = optionalProductUnit.get();
        productUnit.setDescription(request.getUnitDescription());
        if (!productUnit.getName().equals(request.getUnitName())) {
            if (billRepository.countAllForDeleteProductUnit(user.getCompanyId(), request.getId(), productUnit.getName()) > 0) {
                throw new BadRequestAlertException(PRODUCT_UNIT_CANNOT_UPDATE_VI, ENTITY_NAME, PRODUCT_UNIT_CANNOT_DELETE_VI);
            }
            productUnit.setName(request.getUnitName());
            productUnitRepository.save(productUnit);
            List<ProductProductUnit> productProductUnits = productProductUnitRepository.findAllByComIdAndProductUnitId(
                user.getCompanyId(),
                request.getId()
            );
            List<Integer> productIds = new ArrayList<>();
            if (productProductUnits != null && !productProductUnits.isEmpty()) {
                for (ProductProductUnit unit : productProductUnits) {
                    if (unit.getIsPrimary()) {
                        unit.setUnitName(request.getUnitName());
                        productIds.add(unit.getProductId());
                    } else {
                        unit.setUnitName(request.getUnitName());
                        String primaryUnit = "";
                        if (!Strings.isNullOrEmpty(unit.getDescription())) {
                            String[] split = unit.getDescription().split(" ");
                            primaryUnit = split[split.length - 1];
                        }
                        unit.setDescription(
                            ProductConstant.VALIDATE.generateDescription(
                                request.getUnitName(),
                                unit.getConvertRate(),
                                primaryUnit,
                                ProductConstant.CONVERSION_UNIT.MUL_FORMULA
                            )
                        );
                    }
                }
                List<ProductProductUnit> units = productProductUnitRepository.findAllByComIdAndProductIdIn(user.getCompanyId(), productIds);
                for (ProductProductUnit unit : units) {
                    if (unit.getIsPrimary()) {
                        unit.setDescription(
                            ProductConstant.VALIDATE.generateDescription(
                                request.getUnitName(),
                                unit.getConvertRate(),
                                request.getUnitName(),
                                ProductConstant.CONVERSION_UNIT.MUL_FORMULA
                            )
                        );
                    } else {
                        unit.setDescription(
                            ProductConstant.VALIDATE.generateDescription(
                                unit.getUnitName(),
                                unit.getConvertRate(),
                                request.getUnitName(),
                                ProductConstant.CONVERSION_UNIT.MUL_FORMULA
                            )
                        );
                    }
                }
            }
        }
        ProductUnitResponse response = new ProductUnitResponse();
        BeanUtils.copyProperties(productUnit, response);
        return new ResultDTO(SUCCESS, UPDATE_PRODUCT_UNIT_SUCCESS, true, response);
    }

    @Override
    public ResultDTO deleteListProductUnit(DeleteProductList request) {
        User user = userService.getUserWithAuthorities(request.getComId());
        Integer comId = user.getCompanyId();

        List<ProductUnitResponse> units = productUnitRepository
            .searchProductUnit(null, user.getCompanyId(), request.getKeyword(), request.getParamCheckAll(), request.getIds())
            .getContent();
        List<Object> listError = new ArrayList<>();
        List<Integer> idDelete = new ArrayList<>();

        for (ProductUnitResponse unit : units) {
            if (!unit.getActive()) {
                unit.setNote(PRODUCT_UNIT_CANNOT_DELETE_VI);
                listError.add(unit);
            } else {
                idDelete.add(unit.getId());
            }
        }

        List<ProductUnit> productUnitList = productUnitRepository.findAllByComIdAndIdIn(comId, idDelete);
        for (ProductUnit productUnit : productUnitList) {
            productUnit.setActive(Boolean.FALSE);
        }
        productUnitRepository.saveAll(productUnitList);

        DataResponse response = new DataResponse();
        response.setCountAll(units.size());
        response.setCountFalse(listError.size());
        response.setDataFalse(listError);
        response.setCountSuccess(response.getCountAll() - response.getCountFalse());
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.DELETE_LIST_PRODUCT_UNIT_SUCCESS, true, response);
    }

    public ResultDTO save(MultipartFile images, SaveProductRequest product, HttpServletRequest httpRequest, Boolean isActionAdmin) {
        User user;
        if (isActionAdmin) {
            user = new User();
            user.setCompanyId(product.getComId());
        } else {
            user = userService.getUserWithAuthorities(product.getComId());
        }
        Integer comId = user.getCompanyId();
        //Check trùng code2 theo comId.
        if (!Strings.isNullOrEmpty(product.getCode2())) {
            Integer countCode2 = productRepository.countByComIdAndCode2AndActiveTrue(product.getComId(), product.getCode2());
            if (countCode2 > 0) throw new InternalServerException(
                PRODUCT_CODE_DUPLICATED_VI,
                PRODUCT_CODE_DUPLICATED_VI,
                PRODUCT_CODE_DUPLICATED
            );
        }
        if (product.getUnitId() == null || product.getUnitId() == 0) {
            product.setUnit(null);
        }
        checkProductUnit(product.getUnitId(), product.getUnit(), comId);
        if (product.getUnitId() != null && product.getUnitId() != 0) {
            ConversionUnitRequest mainUnit = new ConversionUnitRequest();
            mainUnit.setProductUnitId(product.getUnitId());
            mainUnit.setConvertRate(BigDecimal.ONE);
            mainUnit.setFormula(ProductConstant.CONVERSION_UNIT.MUL_FORMULA);
            mainUnit.setDirectSale(Boolean.TRUE);
            mainUnit.setPrimary(Boolean.TRUE);
            mainUnit.setSalePrice(product.getSalePrice());
            mainUnit.setPurchasePrice(product.getPurchasePrice());
            mainUnit.setUnitName(product.getUnit());
            mainUnit.setBarcode(product.getBarcode());

            List<ConversionUnitRequest> list = product.getConversionUnits();
            if (list == null) {
                list = new ArrayList<>();
            }
            list.add(mainUnit);
            product.setConversionUnits(list);
        }
        List<String> barcodes = product
            .getConversionUnits()
            .stream()
            .map(ConversionUnitRequest::getBarcode)
            .filter(barcode -> !Strings.isNullOrEmpty(barcode))
            .collect(Collectors.toList());
        if (!Strings.isNullOrEmpty(product.getBarcode())) {
            barcodes.add(product.getBarcode());
        }
        Integer count = productRepository.countByComIdAndBarCodeInAndActiveTrue(user.getCompanyId(), barcodes);
        if (count > 0) {
            throw new InternalServerException(PRODUCT_BARCODE_DUPLICATED_VI, PRODUCT_BARCODE_DUPLICATED_VI, PRODUCT_BARCODE_DUPLICATED);
        }
        checkProductUnitConversion(product.getConversionUnits(), product.getUnitId(), product.getUnit(), user, Boolean.TRUE);
        if (product.getIsTopping() == null) {
            product.setIsTopping(Boolean.FALSE);
        }
        checkTopping(product.getToppings(), user, product.getIsTopping(), null);
        // Kiểm tra inventoryTracking: nếu inventoryTracking = true, inventoryCount và inPrice k đc trống.
        checkValidIsInventory(product.getInventoryTracking(), product.getInventoryCount(), product.getPurchasePrice());
        if (product.getDiscountVatRate() != null) {
            Optional<String> configOptional = configRepository.getValueByComIdAndCode(comId, Constants.TAX_REDUCTION_CODE);
            if (configOptional.isEmpty() || !configOptional.get().equals(ProductConstant.TAX_REDUCTION_TYPE.GIAM_TRU_RIENG)) {
                throw new BadRequestAlertException(NOT_TAX_REDUCTION_VI.replace("@@", product.getName()), ENTITY_NAME, NOT_TAX_REDUCTION);
            }
        }
        //Lấy danh sách ProductGroup
        List<Integer> ids = product.getGroups();
        List<ProductGroup> productGroupList = productGroupRepository.findAllByIdAndComId(ids, comId);

        //Tạo sản phẩm mới
        Product productSave = new Product();
        String url = "";
        if (images != null && !Strings.isNullOrEmpty(images.getOriginalFilename())) {
            url = Common.saveFile(images, Constants.IMAGE_FORMAT, ImagePathConstants.PRODUCT + comId.toString(), httpRequest);
        }
        productSave.setImage(url);
        BeanUtils.copyProperties(product, productSave);
        productSave.setBarCode(product.getBarcode());
        productSave.setInventoryTracking(product.getInventoryTracking());
        productSave.setPurchasePrice(product.getPurchasePrice());
        productSave.setSalePrice(product.getSalePrice());
        productSave.setProductGroups(productGroupList);
        productSave.setCode(userService.genCode(comId, Constants.PRODUCT_CODE));
        productSave.setActive(true);

        // Tạo mới vật tư hàng hóa ở PMKT
        List<TaskLogSendQueue> taskLogSendQueueList = transactionTemplate.execute(status -> {
            productSave.setNormalizedName(Common.normalizedName(Arrays.asList(product.getName())));
            productRepository.save(productSave);
            List<ProductProductUnit> units = new ArrayList<>();
            if (product.getConversionUnits() != null && !product.getConversionUnits().isEmpty()) {
                for (ConversionUnitRequest unitRequest : product.getConversionUnits()) {
                    units.add(convertRequest(null, unitRequest, productSave, comId, unitRequest.getUnitName(), null));
                }
            } else {
                units.add(createNewProductProductUnit(productSave, null));
            }
            addToppings(product.getToppings(), productSave.getId(), user);
            productProductUnitRepository.saveAll(units);
            // Tạo danh sách ProcessingArea
            if (product.getProcessingArea() != null) {
                Optional<ProcessingArea> processingAreaOptional = processingAreaRepository.findByComIdAndId(
                    user.getCompanyId(),
                    product.getProcessingArea()
                );
                if (processingAreaOptional.isEmpty()) {
                    throw new BadRequestAlertException(PROCESSING_AREA_NOT_FOUND_VI, ENTITY_NAME, PROCESSING_AREA_NOT_FOUND);
                }
                Optional<ProductProductUnit> optionalProductProductUnit = productProductUnitRepository.findByProductIdAndComIdAndIsPrimaryTrue(
                    productSave.getId(),
                    comId
                );
                if (optionalProductProductUnit.isPresent()) {
                    List<ProcessingAreaProduct> processingAreaProducts = new ArrayList<>();
                    List<Integer> processingAreas = List.of(product.getProcessingArea());
                    List<ProcessingArea> processingAreaList = processingAreaRepository.findByComIdAndIdIn(comId, processingAreas);
                    for (ProcessingArea processingArea : processingAreaList) {
                        ProcessingAreaProduct areaProduct = new ProcessingAreaProduct();
                        areaProduct.setProductProductUnitId(optionalProductProductUnit.get().getId());
                        areaProduct.setComId(comId);
                        areaProduct.setProcessingAreaId(processingArea.getId());
                        processingAreaProducts.add(areaProduct);
                    }
                    processingAreaProductRepository.saveAll(processingAreaProducts);
                }
            }
            List<TaskLogSendQueue> taskLogSendQueues = new ArrayList<>();

            try {
                TaskLogSendQueue taskLogSendQueue = createAndPublishQueueTask(
                    comId,
                    productSave.getId(),
                    TaskLogConstants.Type.EB_CREATE_MATERIAL_GOODS
                );
                taskLogSendQueues.add(taskLogSendQueue);
                // save rsInWard
                if (product.getInventoryTracking()) {
                    List<RsInoutWard> rsInoutWard = convertRsInoutWard(product.getComId(), null, productSave, true);
                    if (rsInoutWard != null) {
                        rsInoutWardRepository.save(rsInoutWard.get(0));
                    }
                }
            } catch (Exception e) {
                log.error("Can not create queue task for eb88 creating material goods: {}", e.getMessage());
            }
            return taskLogSendQueues;
        });
        if (taskLogSendQueueList != null) {
            for (TaskLogSendQueue task : taskLogSendQueueList) {
                userService.sendTaskLog(task);
            }
        }
        List<ProcessingAreaProduct> processingAreaProducts = processingAreaProductRepository.findAllByComIdIsNull();
        for (ProcessingAreaProduct processingAreaProduct : processingAreaProducts) {
            processingAreaProduct.setComId(comId);
            processingAreaProduct.setCreateTime(ZonedDateTime.now());
            processingAreaProduct.setCreator(user.getId());
        }
        ProductDetailResponse response = new ProductDetailResponse();
        BeanUtils.copyProperties(productSave, response);
        response.setBarcode(productSave.getBarCode());
        response.setImageUrl(productSave.getImage());
        List<ProductDetailResponse> responses = List.of(response);
        responses = getProductItemResult(responses, comId);
        for (ProductProductUnitResponse unitResponse : responses.get(0).getConversionUnits()) {
            unitResponse.setOnHand(unitResponse.getOnHand().setScale(CommonConstants.REGISTER_PASSWORD_LENGTH, RoundingMode.UNNECESSARY));
        }
        return new ResultDTO(SUCCESS, CREATE_PRODUCT_SUCCESS_VI, true, responses.get(0));
    }

    // tạo mới bản ghi prod_prod_unit mặc định trong trường hợp không bật tồn kho
    private ProductProductUnit createNewProductProductUnit(Product productSave, List<ProcessingArea> processingAreas) {
        ProductProductUnit productProductUnit = new ProductProductUnit();
        productProductUnit.setComId(productSave.getComId());
        productProductUnit.setProductId(productSave.getId());
        productProductUnit.setDirectSale(Boolean.TRUE);
        productProductUnit.setSalePrice(productSave.getSalePrice());
        productProductUnit.setPurchasePrice(productSave.getPurchasePrice());
        if (productSave.getInventoryTracking()) {
            productProductUnit.setOnHand(productSave.getInventoryCount());
        } else {
            productProductUnit.setOnHand(BigDecimal.ZERO);
        }
        productProductUnit.setFormula(false);
        productProductUnit.setIsPrimary(true);
        productProductUnit.setBarcode(productSave.getBarCode());
        productProductUnit.setUnitNormalizedName(productSave.getUnit());
        //        productProductUnit.setProcessingAreas(processingAreas);
        return productProductUnit;
    }

    private void checkProductUnit(Integer unitID, String unit, Integer comId) {
        if (unitID == null) {
            return;
        }
        if (unitID != 0) {
            Optional<ProductUnit> productUnit = productUnitRepository.findByIdAndComId(unitID, comId);
            if (productUnit.isPresent()) {
                if (!productUnit.get().getName().toLowerCase().equals(unit.toLowerCase())) {
                    throw new BadRequestAlertException(UNIT_NAME_NOT_INVALID_VI, ENTITY_NAME, UNIT_NAME_NOT_INVALID);
                }
            } else {
                throw new BadRequestAlertException(UNIT_NOT_FOUND_VI, ENTITY_NAME, UNIT_NOT_FOUND);
            }
        } else if (!Strings.isNullOrEmpty(unit)) {
            throw new BadRequestAlertException(UNIT_MUST_NOT_EMPTY_VI, ENTITY_NAME, UNIT_MUST_NOT_EMPTY);
        }
    }

    private List<Integer> checkProductUnitConversion(
        List<ConversionUnitRequest> conversionUnits,
        Integer unitId,
        String unitName,
        User user,
        Boolean isNew
    ) {
        if (conversionUnits == null || conversionUnits.isEmpty()) {
            return new ArrayList<>();
        }
        if (unitId == null || unitId == 0) {
            throw new BadRequestAlertException(UNIT_NAME_IS_NOT_VALID_VI, ENTITY_NAME, UNIT_NAME_IS_NOT_VALID);
        }
        List<Integer> idChange = new ArrayList<>();
        Set<String> unitNameSet = new HashSet<>();
        Set<String> barcodeSet = new HashSet<>();
        for (ConversionUnitRequest conversionUnit : conversionUnits) {
            if (conversionUnit.getPrimary() != null && conversionUnit.getPrimary()) {
                if (Strings.isNullOrEmpty(conversionUnit.getUnitName()) && conversionUnits.size() > 1) {
                    throw new BadRequestAlertException(PRODUCT_UNIT_NOT_DELETE_VI, ENTITY_NAME, PRODUCT_UNIT_NOT_DELETE);
                }
                if (!Strings.isNullOrEmpty(conversionUnit.getBarcode())) {
                    if (barcodeSet.contains(conversionUnit.getBarcode())) {
                        throw new BadRequestAlertException(
                            DUPLICATE_CONVERSION_UNIT_BARCODE_VI,
                            ENTITY_NAME,
                            DUPLICATE_CONVERSION_UNIT_BARCODE
                        );
                    }
                    barcodeSet.add(conversionUnit.getBarcode());
                }
                continue;
            }
            if (conversionUnit.getId() != null) {
                Optional<ProductProductUnit> productUnitOptional = productProductUnitRepository.findByIdAndComId(
                    conversionUnit.getId(),
                    user.getCompanyId()
                );
                if (productUnitOptional.isEmpty()) {
                    throw new BadRequestAlertException(UNIT_NOT_FOUND_VI, ENTITY_NAME, UNIT_NOT_FOUND);
                }
                ProductProductUnit productProductUnit = productUnitOptional.get();
                Integer conversionUnitId = productProductUnit.getProductUnitId();
                if ((isNew && Objects.equals(unitId, conversionUnitId)) || (unitName.equalsIgnoreCase(conversionUnit.getUnitName()))) {
                    throw new BadRequestAlertException(DUPLICATE_WITH_UNIT_VI, ENTITY_NAME, DUPLICATE_WITH_UNIT);
                }
                if (!productProductUnit.getUnitName().equals(conversionUnit.getUnitName().trim())) {
                    idChange.add(conversionUnit.getId());
                }
            }
            if (unitNameSet.contains(conversionUnit.getUnitName().trim())) {
                throw new BadRequestAlertException(DUPLICATE_CONVERSION_UNIT_VI, ENTITY_NAME, DUPLICATE_CONVERSION_UNIT);
            }
            if (barcodeSet.contains(conversionUnit.getBarcode())) {
                throw new BadRequestAlertException(DUPLICATE_CONVERSION_UNIT_BARCODE_VI, ENTITY_NAME, DUPLICATE_CONVERSION_UNIT_BARCODE);
            }
            if ((isNew && Objects.equals(unitId, conversionUnit.getId())) || (unitName.equalsIgnoreCase(conversionUnit.getUnitName()))) {
                throw new BadRequestAlertException(DUPLICATE_WITH_UNIT_VI, ENTITY_NAME, DUPLICATE_WITH_UNIT);
            }
            if (conversionUnit.getConvertRate().compareTo(BigDecimal.ZERO) <= 0) {
                throw new BadRequestAlertException(CONVERT_RATE_INVALID_VI, ENTITY_NAME, CONVERT_RATE_INVALID);
            }
            if (!Strings.isNullOrEmpty(conversionUnit.getUnitName())) {
                unitNameSet.add(conversionUnit.getUnitName());
            }
            if (!Strings.isNullOrEmpty(conversionUnit.getBarcode())) {
                barcodeSet.add(conversionUnit.getBarcode());
            }
        }
        return idChange;
    }

    private void checkTopping(List<ToppingRequest> toppingRequests, User user, Boolean isTopping, Integer productId) {
        if (toppingRequests == null || toppingRequests.isEmpty()) {
            return;
        }
        //        if (isTopping) {
        //            throw new BadRequestAlertException(TOPPING_LIST_INVALID_VI, ENTITY_NAME, TOPPING_LIST_INVALID_CODE);
        //        }
        Set<Integer> toppingProducts = new HashSet<>();
        Set<Integer> toppingGroups = new HashSet<>();
        List<Integer> groupIds = toppingGroupRepository.findToppingGroupIdByProductId(productId);
        for (ToppingRequest request : toppingRequests) {
            if (!request.getIsTopping()) {
                if (toppingGroups.contains(request.getId())) {
                    throw new BadRequestAlertException(TOPPING_GROUP_LIST_INVALID_VI, ENTITY_NAME, TOPPING_GROUP_LIST_INVALID_CODE);
                }
                if (groupIds.contains(request.getId())) {
                    throw new BadRequestAlertException(TOPPING_ITSELF_GROUP_VI, ENTITY_NAME, TOPPING_ITSELF_GROUP_CODE);
                }
                toppingGroups.add(request.getId());
            } else {
                if (toppingProducts.contains(request.getId())) {
                    throw new BadRequestAlertException(TOPPING_LIST_INVALID_VI, ENTITY_NAME, TOPPING_LIST_INVALID_CODE);
                }
                if (Objects.equals(productId, request.getId())) {
                    throw new BadRequestAlertException(TOPPING_ITSELF_VI, ENTITY_NAME, TOPPING_ITSELF_CODE);
                }
                toppingProducts.add(request.getId());
            }
        }
        List<Product> products = productRepository.findAllByComIdAndIdInAndIsToppingTrueAndActiveTrue(user.getCompanyId(), toppingProducts);
        if (toppingProducts.size() != products.size()) {
            throw new BadRequestAlertException(TOPPING_LIST_INVALID_VI, ENTITY_NAME, TOPPING_LIST_INVALID_CODE);
        }
        List<ToppingGroup> groups = toppingGroupRepository.findAllByComIdAndIdIn(user.getCompanyId(), toppingGroups);
        if (groups.size() != toppingGroups.size()) {
            throw new BadRequestAlertException(TOPPING_GROUP_LIST_INVALID_VI, ENTITY_NAME, TOPPING_GROUP_LIST_INVALID_CODE);
        }
    }

    private void addToppings(List<ToppingRequest> toppingRequests, Integer productId, User user) {
        if (toppingRequests == null || toppingRequests.isEmpty()) {
            return;
        }
        List<Integer> toppingGroupIds = new ArrayList<>();
        List<ProductTopping> toppings = new ArrayList<>();
        for (ToppingRequest request : toppingRequests) {
            if (!request.getIsTopping()) {
                toppingGroupIds.add(request.getId());
            } else {
                ProductTopping topping = new ProductTopping();
                topping.setProductId(productId);
                topping.setToppingId(request.getId());
                topping.setToppingGroupId(null);
                toppings.add(topping);
            }
        }
        List<ToppingRequiredItem> items = toppingToppingGroupRepository.findProductIdByToppingGroupId(toppingGroupIds);
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (ToppingRequiredItem item : items) {
            List<Integer> ids = new ArrayList<>();
            if (map.containsKey(item.getToppingGroupId())) {
                ids = map.get(item.getToppingGroupId());
            }
            ids.add(item.getProductId());
            map.put(item.getToppingGroupId(), ids);
        }
        for (Map.Entry<Integer, List<Integer>> entry : map.entrySet()) {
            List<Integer> value = entry.getValue();
            for (Integer id : value) {
                ProductTopping topping = new ProductTopping();
                topping.setProductId(productId);
                topping.setToppingId(id);
                topping.setToppingGroupId(entry.getKey());
                toppings.add(topping);
            }
        }
        productToppingRepository.saveAll(toppings);
    }

    private void updateToppings(List<ToppingRequest> toppingRequests, Product product, Product productOld, User user) {
        Integer productId = product.getId();
        if (!product.getIsTopping() && productOld.getIsTopping()) {
            List<ToppingToppingGroup> groups = toppingToppingGroupRepository.findAllByProductId(productId);
            List<Integer> ids = groups.stream().map(ToppingToppingGroup::getToppingGroupId).collect(Collectors.toList());
            List<UnitResponse> toppingGroupList = toppingToppingGroupRepository.getEmptyToppingGroup(ids);
            if (toppingGroupList != null && !toppingGroupList.isEmpty()) {
                throw new BadRequestAlertException(
                    ExceptionConstants.PRODUCT_IN_TOPPING_GROUP_CANNOT_DELETE_VI
                        .replace("@@1", product.getName())
                        .replace("@@2", toppingGroupList.get(0).getName()),
                    ENTITY_NAME,
                    PRODUCT_IN_TOPPING_GROUP_CANNOT_DELETE
                );
            }
            toppingToppingGroupRepository.deleteAll(groups);

            List<ProductTopping> productToppings = productToppingRepository.findAllByToppingId(productId);
            productToppingRepository.deleteAll(productToppings);
        }
        if (toppingRequests == null || toppingRequests.isEmpty()) {
            List<ProductTopping> productToppings = productToppingRepository.findAllByProductId(productId);
            checkDeleteAllTopping(productToppings, productId);
            productToppingRepository.deleteAll(productToppings);
            return;
        }
        List<ToppingItem> toppingItems = toppingGroupRepository.getForProductDetail(productId);
        Map<Integer, ToppingItem> oldGroupTopping = new HashMap<>();
        Map<Integer, ToppingItem> oldProductTopping = new HashMap<>();
        for (ToppingItem item : toppingItems) {
            if (item.getIsTopping()) {
                oldProductTopping.put(item.getId(), item);
            } else {
                oldGroupTopping.put(item.getId(), item);
            }
        }
        List<ProductTopping> toppings = new ArrayList<>();
        List<Integer> newGroupIds = new ArrayList<>();
        for (ToppingRequest request : toppingRequests) {
            if (!request.getIsTopping()) {
                if (oldGroupTopping.containsKey(request.getId())) {
                    oldGroupTopping.remove(request.getId());
                } else {
                    newGroupIds.add(request.getId());
                }
            } else {
                if (oldProductTopping.containsKey(request.getId())) {
                    oldProductTopping.remove(request.getId());
                } else {
                    ProductTopping topping = new ProductTopping();
                    topping.setProductId(productId);
                    topping.setToppingId(request.getId());
                    topping.setToppingGroupId(null);
                    toppings.add(topping);
                }
            }
        }
        List<ToppingRequiredItem> items = toppingToppingGroupRepository.findProductIdByToppingGroupId(newGroupIds);
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (ToppingRequiredItem item : items) {
            List<Integer> ids = new ArrayList<>();
            if (map.containsKey(item.getToppingGroupId())) {
                ids = map.get(item.getToppingGroupId());
            }
            ids.add(item.getProductId());
            map.put(item.getToppingGroupId(), ids);
        }
        for (Map.Entry<Integer, List<Integer>> entry : map.entrySet()) {
            List<Integer> value = entry.getValue();
            for (Integer id : value) {
                ProductTopping topping = new ProductTopping();
                topping.setProductId(productId);
                topping.setToppingId(id);
                topping.setToppingGroupId(entry.getKey());
                toppings.add(topping);
            }
        }
        productToppingRepository.saveAll(toppings);
        List<ProductTopping> deleteList = new ArrayList<>();
        for (Map.Entry<Integer, ToppingItem> entry : oldGroupTopping.entrySet()) {
            ToppingItem value = entry.getValue();
            if (toppingGroupRepository.checkDeleteGroup(value.getId(), productId) > 0) {
                throw new BadRequestAlertException(
                    ExceptionConstants.PRODUCT_IN_GROUP_CANNOT_DELETE_VI.replace("@@", value.getName()),
                    ENTITY_NAME,
                    ExceptionConstants.PRODUCT_IN_GROUP_CANNOT_DELETE
                );
            }
            List<ProductTopping> list = productToppingRepository.findAllByToppingGroupId(value.getId());
            deleteList.addAll(list);
        }
        for (Map.Entry<Integer, ToppingItem> entry : oldProductTopping.entrySet()) {
            ToppingItem value = entry.getValue();
            if (toppingGroupRepository.checkDeleteProduct(value.getId()) > 0) {
                throw new BadRequestAlertException(
                    ExceptionConstants.PRODUCT_IN_TOPPING_CANNOT_DELETE_VI.replace("@@", value.getName()),
                    ENTITY_NAME,
                    ExceptionConstants.PRODUCT_IN_TOPPING_CANNOT_DELETE
                );
            }
            Optional<ProductTopping> topping = productToppingRepository.findOneByToppingIdAndProductIdAndToppingGroupIdIsNull(
                value.getId(),
                productId
            );
            topping.ifPresent(deleteList::add);
        }
        productToppingRepository.deleteAll(deleteList);
    }

    private void checkDeleteAllTopping(List<ProductTopping> productToppings, Integer productId) {
        Set<Integer> groups = productToppings
            .stream()
            .map(ProductTopping::getToppingGroupId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
        Set<Integer> products = productToppings
            .stream()
            .filter(productTopping -> productTopping.getToppingGroupId() == null)
            .map(ProductTopping::getToppingId)
            .collect(Collectors.toSet());
        for (Integer group : groups) {
            if (toppingGroupRepository.checkDeleteGroup(group, productId) > 0) {
                throw new BadRequestAlertException(
                    ExceptionConstants.PRODUCT_IN_GROUP_CANNOT_DELETE_VI.replace("[@@] ", ""),
                    ENTITY_NAME,
                    ExceptionConstants.PRODUCT_IN_GROUP_CANNOT_DELETE
                );
            }
        }
        for (Integer product : products) {
            if (toppingGroupRepository.checkDeleteProduct(product) > 0) {
                throw new BadRequestAlertException(
                    ExceptionConstants.PRODUCT_IN_TOPPING_CANNOT_DELETE_VI.replace("[@@] ", ""),
                    ENTITY_NAME,
                    ExceptionConstants.PRODUCT_IN_TOPPING_CANNOT_DELETE
                );
            }
        }
    }

    public ResultDTO update(MultipartFile images, UpdateProdRequest productRequest, HttpServletRequest httpRequest, Boolean isActionAdmin) {
        User user;
        if (isActionAdmin) {
            user = new User();
            user.setCompanyId(productRequest.getComId());
        } else {
            user = userService.getUserWithAuthorities(productRequest.getComId());
        }
        //        Integer productId = productProductUnitRepository.findProductIdById(productRequest.getId());
        Integer productId = productRequest.getId();
        Integer comId = user.getCompanyId();
        if (!Strings.isNullOrEmpty(productRequest.getCode2())) {
            //Check Duplicate Code2
            if (!productRepository.countByIdAndComId(productId, productRequest.getComId(), productRequest.getCode2())) {
                throw new InternalServerException(PRODUCT_CODE_DUPLICATED_VI, PRODUCT_CODE_DUPLICATED_VI, PRODUCT_CODE_DUPLICATED);
            }
        }
        //Kiểm tra UnitId
        //        checkProductUnit(productRequest.getUnitId(), productRequest.getUnit(), user);
        if (productRequest.getUnitId() != null && productRequest.getUnitId() != 0) {
            ConversionUnitRequest mainUnit = new ConversionUnitRequest();
            mainUnit.setId(productRequest.getId());
            mainUnit.setProductUnitId(productRequest.getUnitId());
            mainUnit.setConvertRate(BigDecimal.ONE);
            mainUnit.setFormula(ProductConstant.CONVERSION_UNIT.MUL_FORMULA);
            mainUnit.setDirectSale(Boolean.TRUE);
            mainUnit.setPrimary(Boolean.TRUE);
            mainUnit.setSalePrice(productRequest.getSalePrice());
            mainUnit.setPurchasePrice(productRequest.getPurchasePrice());
            mainUnit.setUnitName(productRequest.getUnit());
            mainUnit.setBarcode(productRequest.getBarcode());

            List<ConversionUnitRequest> list = productRequest.getConversionUnits();
            if (list == null) {
                list = new ArrayList<>();
            }
            list.add(mainUnit);
            productRequest.setConversionUnits(list);
        }
        // Kiểm tra barcode trùng
        Set<String> barcodes = productRequest
            .getConversionUnits()
            .stream()
            .map(ConversionUnitRequest::getBarcode)
            .filter(barcode -> !Strings.isNullOrEmpty(barcode))
            .collect(Collectors.toSet());
        if (!Strings.isNullOrEmpty(productRequest.getBarcode())) {
            barcodes.add(productRequest.getBarcode());
        }
        if (!barcodes.isEmpty() && !productRepository.checkDuplicateBarcode(productId, comId, barcodes)) {
            throw new InternalServerException(PRODUCT_BARCODE_DUPLICATED_VI, PRODUCT_BARCODE_DUPLICATED_VI, PRODUCT_BARCODE_DUPLICATED);
        }
        List<Integer> idChange = checkProductUnitConversion(
            productRequest.getConversionUnits(),
            productRequest.getUnitId(),
            productRequest.getUnit(),
            user,
            Boolean.FALSE
        );
        if (productRequest.getIsTopping() == null) {
            productRequest.setIsTopping(Boolean.FALSE);
        }
        checkTopping(productRequest.getToppings(), user, productRequest.getIsTopping(), productId);
        //Kiểm tra InventoryTracking
        if (productRequest.getInventoryTracking() != null) {
            checkValidIsInventory(
                productRequest.getInventoryTracking(),
                productRequest.getInventoryCount(),
                productRequest.getPurchasePrice()
            );
        }
        //        if (productRequest.getDiscountVatRate() != null) {
        //            Optional<String> configOptional = configRepository.getValueByComIdAndCode(comId, Constants.TAX_REDUCTION_CODE);
        //            if (configOptional.isEmpty() || !configOptional.get().equals(ProductConstant.TAX_REDUCTION_TYPE.GIAM_TRU_RIENG)) {
        //                throw new BadRequestAlertException(
        //                    NOT_TAX_REDUCTION_VI.replace("@@", productRequest.getName()),
        //                    ENTITY_NAME,
        //                    NOT_TAX_REDUCTION
        //                );
        //            }
        //        }
        //Lấy danh sách ProductGroup
        List<ProductGroup> productGroups = new ArrayList<>();
        if (productRequest.getGroups() != null && !productRequest.getGroups().isEmpty()) {
            productGroups = productGroupRepository.findAllByIdAndComId(productRequest.getGroups(), productRequest.getComId());
        }

        // Tạo danh sách ProcessingArea
        processingAreaProductRepository.deleteBeforeUpdateProduct(comId, productRequest.getId());
        if (productRequest.getProcessingArea() != null) {
            Optional<ProcessingArea> processingAreaOptional = processingAreaRepository.findByComIdAndId(
                user.getCompanyId(),
                productRequest.getProcessingArea()
            );
            if (processingAreaOptional.isEmpty()) {
                throw new BadRequestAlertException(PROCESSING_AREA_NOT_FOUND_VI, ENTITY_NAME, PROCESSING_AREA_NOT_FOUND);
            }
            Optional<ProductProductUnit> optionalProductProductUnit = productProductUnitRepository.findByProductIdAndComIdAndIsPrimaryTrue(
                productRequest.getId(),
                user.getCompanyId()
            );
            if (optionalProductProductUnit.isPresent()) {
                Optional<ProcessingAreaProduct> optionalProcessingAreaProduct = processingAreaProductRepository.findByComIdAndProductProductUnitId(
                    user.getCompanyId(),
                    optionalProductProductUnit.get().getId()
                );
                if (optionalProcessingAreaProduct.isPresent()) {
                    optionalProcessingAreaProduct.ifPresent(processingAreaProduct ->
                        processingAreaProduct.setProcessingAreaId(productRequest.getProcessingArea())
                    );
                } else {
                    ProcessingAreaProduct areaProduct = new ProcessingAreaProduct();
                    areaProduct.setProcessingAreaId(productRequest.getProcessingArea());
                    areaProduct.setProductProductUnitId(optionalProductProductUnit.get().getId());
                    areaProduct.setComId(productRequest.getComId());
                    processingAreaProductRepository.save(areaProduct);
                }
            }
        } else {
            Optional<ProductProductUnit> optionalProductProductUnit = productProductUnitRepository.findByProductIdAndComIdAndIsPrimaryTrue(
                productRequest.getId(),
                user.getCompanyId()
            );
            if (optionalProductProductUnit.isPresent()) {
                Optional<ProcessingAreaProduct> optionalProcessingAreaProduct = processingAreaProductRepository.findByComIdAndProductProductUnitId(
                    user.getCompanyId(),
                    optionalProductProductUnit.get().getId()
                );
                optionalProcessingAreaProduct.ifPresent(processingAreaProductRepository::delete);
            }
        }
        Optional<Product> productOptional = productRepository.findByIdAndComIdAndActive(
            productId,
            comId,
            ProductConstant.Active.ACTIVE_TRUE
        );
        if (productOptional.isEmpty()) {
            throw new InternalServerException(PRODUCT_NOT_FOUND_VI, PRODUCT_NOT_FOUND_VI, PRODUCT_NOT_FOUND);
        }
        List<ProductProductUnit> result = new ArrayList<>();
        Product product = productOptional.get();
        Product productOld = new Product();
        BeanUtils.copyProperties(product, productOld);
        if (images != null && !Strings.isNullOrEmpty(images.getOriginalFilename())) {
            String url = Common.saveFile(images, Constants.IMAGE_FORMAT, ImagePathConstants.PRODUCT + comId.toString(), httpRequest);
            product.setImage(url);
        }
        boolean check = Boolean.TRUE;
        Map<Integer, ProductProductUnit> unitMap = new HashMap<>();
        if (productRequest.getConversionUnits() != null) {
            if (productRequest.getConversionUnits().isEmpty()) {
                if (Strings.isNullOrEmpty(productRequest.getUnit()) && !Strings.isNullOrEmpty(productOld.getUnit())) {
                    Optional<ProductProductUnit> unitOptional = productProductUnitRepository.findByProductIdAndComIdAndIsPrimaryTrue(
                        productId,
                        comId
                    );
                    unitOptional.ifPresent(productProductUnit -> idChange.add(productProductUnit.getId()));
                }
            }
            Product p = new Product();
            BeanUtils.copyProperties(productRequest, p);
            for (ConversionUnitRequest unitRequest : productRequest.getConversionUnits()) {
                if (unitRequest.getPrimary() != null && unitRequest.getPrimary()) {
                    check = unitRequest.getUnitName().equals(productOld.getUnit());
                    if (!check) {
                        Optional<ProductProductUnit> unitOptional = productProductUnitRepository.findByProductIdAndComIdAndIsPrimaryTrue(
                            productId,
                            comId
                        );
                        unitOptional.ifPresent(productProductUnit -> idChange.add(productProductUnit.getId()));
                    }
                    if (check) {
                        check = Objects.equals(unitRequest.getBarcode(), productOld.getBarCode());
                    }
                } else {
                    if (unitRequest.getId() != null) {
                        unitMap.put(
                            unitRequest.getId(),
                            convertRequest(unitRequest.getId(), unitRequest, p, comId, unitRequest.getUnitName(), null)
                        );
                    } else {
                        result.add(convertRequest(null, unitRequest, p, comId, unitRequest.getUnitName(), null));
                    }
                }
            }
        }
        if (productProductUnitRepository.countByStatusAndIdIn(idChange) > 0) {
            throw new BadRequestAlertException(
                UNIT_CAN_NOT_CHANGE_VI.replace("@@name", productRequest.getName()),
                ENTITY_NAME,
                UNIT_CAN_NOT_CHANGE
            );
        }
        partialUpdate(product, productRequest, user);
        product.setUnit(Strings.isNullOrEmpty(productRequest.getUnit()) ? null : productRequest.getUnit());
        product.setProductGroups(productGroups);
        product.setNormalizedName(Common.normalizedName(Arrays.asList(product.getName())));
        List<ProductProductUnit> productUnits = productProductUnitRepository.findAllByProductIdAndComId(product.getId(), comId);
        for (ProductProductUnit unit : productUnits) {
            if (unit.getIsPrimary() != null && unit.getIsPrimary()) {
                result.add(unit);
            } else if (unitMap.containsKey(unit.getId())) {
                result.add(unitMap.get(unit.getId()));
                unitMap.remove(unit.getId());
            } else {
                DeleteConversionUnitRequest request = new DeleteConversionUnitRequest();
                request.setId(unit.getId());
                request.setComId(unit.getComId());
                ResultDTO resultDTO = deleteProductUnit(request, Boolean.FALSE);
                if (resultDTO.isStatus()) {
                    productProductUnitRepository.delete(unit);
                }
            }
        }
        for (Map.Entry<Integer, ProductProductUnit> entry : unitMap.entrySet()) {
            ProductProductUnit value = entry.getValue();
            result.add(value);
        }
        if (!check) {
            Optional<ProductProductUnit> unitOptional = productProductUnitRepository.findByProductIdAndComIdAndIsPrimaryTrue(
                productId,
                comId
            );
            if (unitOptional.isEmpty() && productOld.getUnitId() != null && productOld.getUnitId() != 0) {
                throw new InternalServerException(PRODUCT_UNIT_NOT_FOUND_VI, ENTITY_NAME, ExceptionConstants.PRODUCT_UNIT_NOT_FOUND);
            }
            ProductProductUnit unit = new ProductProductUnit();
            if (unitOptional.isEmpty() && productRequest.getUnitId() != null && productRequest.getUnitId() != 0) {
                unit =
                    convertRequest(
                        null,
                        productRequest.getConversionUnits().get(productRequest.getConversionUnits().size() - 1),
                        product,
                        comId,
                        productRequest.getUnit(),
                        null
                    );
            } else if (unitOptional.isPresent() && !Strings.isNullOrEmpty(productRequest.getUnit())) {
                unit = unitOptional.get();
                unit.setProductUnitId(productRequest.getUnitId());
                unit.setConvertRate(BigDecimal.ONE);
                unit.setUnitName(productRequest.getUnit());
                unit.setBarcode(productRequest.getBarcode());
                unit.setDescription(
                    ProductConstant.VALIDATE.generateDescription(
                        productRequest.getUnit(),
                        BigDecimal.ONE,
                        productRequest.getUnit(),
                        ProductConstant.CONVERSION_UNIT.MUL_FORMULA
                    )
                );
                if (!Strings.isNullOrEmpty(productRequest.getUnit())) {
                    unit.setUnitNormalizedName(Common.normalizedName(Arrays.asList(productRequest.getUnit())));
                }
            }
            if (unit.getComId() != null) {
                result.add(unit);
            }
        }
        // tính lại on_hand sản phẩm trong trường hợp không có đơn vị tính
        for (ProductProductUnit unit : result) {
            if (unit.getProductUnitId() == null) {
                unit.setPurchasePrice(product.getPurchasePrice());
                unit.setOnHand(product.getInventoryCount() == null ? BigDecimal.ZERO : product.getInventoryCount());
            }
        }
        productProductUnitRepository.saveAll(result);
        Product finalProduct = product;
        List<TaskLogSendQueue> taskLogSendQueueList = transactionTemplate.execute(status -> {
            productRepository.save(finalProduct);
            updateToppings(productRequest.getToppings(), finalProduct, productOld, user);
            List<TaskLogSendQueue> taskLogSendQueues = new ArrayList<>();
            // Update vật tư hàng hóa ở PMKT
            try {
                taskLogSendQueues.add(
                    createAndPublishQueueTask(user.getCompanyId(), finalProduct.getId(), TaskLogConstants.Type.EB_UPDATE_MATERIAL_GOODS)
                );
                List<RsInoutWard> rsInoutWards = new ArrayList<>();
                String businessType = null;
                // tạo mới chứng từ khởi tạo kho khi từ không tồn kho -> tồn kho
                if (productRequest.getInventoryTracking() != productOld.getInventoryTracking()) {
                    rsInoutWards = convertRsInoutWard(productRequest.getComId(), productOld, finalProduct, true);
                    businessType = BusinessTypeConstants.RsInWard.INITIAL;
                }
                // lưu rsInWard nếu sản phẩm có theo dõi tồn kho, request update có InventoryCount khác null
                else if (productRequest.getInventoryTracking() && productRequest.getInventoryCount() != null) {
                    rsInoutWards = convertRsInoutWard(productRequest.getComId(), productOld, finalProduct, false);
                }
                if (rsInoutWards != null && !rsInoutWards.isEmpty()) {
                    rsInoutWardRepository.saveAll(rsInoutWards);
                    for (RsInoutWard rsInoutWard : rsInoutWards) {
                        taskLogSendQueues.add(createAndPublishQueueRsInWardTask(comId, rsInoutWard.getId(), businessType));
                    }
                }
            } catch (Exception e) {
                log.error("Can not create queue task for eb88 creating material goods: {}", e.getMessage());
            }
            return taskLogSendQueues;
        });
        if (taskLogSendQueueList != null) {
            for (TaskLogSendQueue task : taskLogSendQueueList) {
                if (task != null) {
                    userService.sendTaskLog(task);
                }
            }
        }
        List<ProcessingAreaProduct> processingAreaProducts = processingAreaProductRepository.findAllByComIdIsNull();
        for (ProcessingAreaProduct processingAreaProduct : processingAreaProducts) {
            processingAreaProduct.setComId(comId);
            processingAreaProduct.setCreateTime(ZonedDateTime.now());
            processingAreaProduct.setCreator(user.getId());
        }
        return new ResultDTO(SUCCESS, UPDATE_PRODUCT_SUCCESS_VI, true);
    }

    private TaskLogSendQueue createAndPublishQueueTask(int comId, int productId, String taskType) throws Exception {
        MaterialGoodsTask task = new MaterialGoodsTask();
        task.setComId("" + comId);
        task.setProductId("" + productId);
        TaskLog taskLog = new TaskLog();
        taskLog.setComId(comId);
        taskLog.setStatus(TaskLogConstants.Status.PROCESSING);
        //New objectMapper because we need disable a feature
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.INDENT_OUTPUT);
        taskLog.setContent(objectMapper.writeValueAsString(task));
        taskLog.setType(taskType);
        taskLog.setRefId(productId);
        taskLog = taskLogRepository.save(taskLog);
        return new TaskLogSendQueue(taskLog.getId().toString(), taskLog.getType());
        //        eb88Producer.send(new TaskLogIdEnqueueMessage(taskLog.getId()));
    }

    private TaskLogSendQueue createAndPublishQueueProductUnitTask(int comId, int productUnitId, String taskType) throws Exception {
        ProductUnitTask task = new ProductUnitTask();
        task.setComId("" + comId);
        task.setProductUnitId("" + productUnitId);
        TaskLog taskLog = new TaskLog();
        taskLog.setComId(comId);
        taskLog.setStatus(TaskLogConstants.Status.PROCESSING);
        //New objectMapper because we need disable a feature
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.INDENT_OUTPUT);
        taskLog.setContent(objectMapper.writeValueAsString(task));
        taskLog.setType(taskType);
        taskLog.setRefId(productUnitId);
        taskLog = taskLogRepository.save(taskLog);
        return new TaskLogSendQueue(taskLog.getId().toString(), taskLog.getType());
        //        eb88Producer.send(new TaskLogIdEnqueueMessage(taskLog.getId()));
    }

    private TaskLogSendQueue createAndPublishQueueTask2(int comId, int productId, String taskType) throws Exception {
        MaterialGoodsTask task = new MaterialGoodsTask();
        task.setComId("" + comId);
        task.setProductId("" + productId);
        TaskLog taskLog = new TaskLog();
        taskLog.setComId(comId);
        taskLog.setStatus(TaskLogConstants.Status.PROCESSING);
        //New objectMapper because we need disable a feature
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.INDENT_OUTPUT);
        taskLog.setContent(objectMapper.writeValueAsString(task));
        taskLog.setType(taskType);
        taskLog.setRefId(productId);
        taskLog = taskLogRepository.save(taskLog);
        return new TaskLogSendQueue(taskLog.getId().toString(), taskLog.getType());
        //publish to queue

    }

    private TaskLogSendQueue createAndPublishQueueRsInWardTask(int comId, Integer rsId, String businessType) {
        RsInOutWardTask task = new RsInOutWardTask();
        task.setComId(comId);
        task.setBusinessType(businessType);
        task.setRsInOutWardId(rsId);
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

    public List<RsInoutWard> convertRsInoutWard(Integer comId, Product productOld, Product product, Boolean isCreate) {
        List<RsInoutWard> rsInoutWards = new ArrayList<>();

        String no;
        String businessTypeCode = null;
        BigDecimal inPriceNew = product.getPurchasePrice() == null ? BigDecimal.ZERO : product.getPurchasePrice();
        BigDecimal quantityNew = product.getInventoryCount() == null ? BigDecimal.ZERO : product.getInventoryCount();
        if (isCreate) {
            RsInoutWard rsInoutWard = new RsInoutWard();
            rsInoutWard.setType(Constants.RS_INWARD_TYPE);
            rsInoutWard.setTypeDesc(Constants.KHOI_TAO_KHO);
            no = userService.genCode(comId, Constants.NHAP_KHO);
            Integer businessTypeId = businessTypeRepository.getIdByComIdAndCode(comId, BusinessTypeConstants.RsInWard.INITIAL);
            if (businessTypeId != null) {
                rsInoutWard.setBusinessTypeId(businessTypeId);
            }
            rsInoutWard.setDescription("Tạo kho từ sản phẩm: " + product.getName());
            rsInoutWard.setNo(no);
            rsInoutWard.setQuantity(quantityNew);
            rsInoutWard.setAmount(inPriceNew.multiply(quantityNew));
            rsInoutWards.add(rsInoutWard);
        } else {
            BigDecimal quantityOld = productOld.getInventoryCount();
            BigDecimal inPriceOld = productOld.getPurchasePrice();
            int compareQuantity = quantityNew.compareTo(quantityOld);
            int compareInPrice = inPriceNew.compareTo(inPriceOld);
            if (compareQuantity == 0 && compareInPrice == 0) {
                return null;
            }
            if (compareInPrice != 0) {
                RsInoutWard rsInoutWard = new RsInoutWard();
                rsInoutWard.setTypeDesc(Constants.SUA_GIA_VON);
                BigDecimal inPrice = (inPriceNew.subtract(inPriceOld)).multiply(quantityOld);
                // tăng giá vốn
                if (compareInPrice > 0) {
                    no = userService.genCode(comId, Constants.NHAP_KHO);
                    rsInoutWard.setType(Constants.RS_INWARD_TYPE);
                    businessTypeCode = BusinessTypeConstants.RsInWard.EDIT_IN_PRICE;
                } else {
                    // giảm giá vốn
                    no = userService.genCode(comId, Constants.XUAT_KHO);
                    if (inPrice.compareTo(BigDecimal.valueOf(0)) < 0) {
                        inPrice = inPrice.multiply(BigDecimal.valueOf(-1));
                    }
                    rsInoutWard.setType(Constants.RS_OUTWARD_TYPE);
                    businessTypeCode = BusinessTypeConstants.RsOutWard.EDIT_IN_PRICE;
                }
                rsInoutWard.setDescription(
                    "Điều chỉnh giá vốn sản phẩm: " +
                    product.getName() +
                    " từ " +
                    Common.formatBigDecimal(inPriceOld) +
                    " thành " +
                    Common.formatBigDecimal(inPriceNew.setScale(6))
                );
                Integer businessTypeId = businessTypeRepository.getIdByComIdAndCode(comId, businessTypeCode);
                if (businessTypeId != null) {
                    rsInoutWard.setBusinessTypeId(businessTypeId);
                }
                rsInoutWard.setNo(no);
                rsInoutWard.setQuantity(quantityOld);
                rsInoutWard.setAmount(inPrice);
                rsInoutWards.add(rsInoutWard);
            }

            // tăng tồn kho'
            if (compareQuantity != 0) {
                RsInoutWard rsInoutWard = new RsInoutWard();
                rsInoutWard.setTypeDesc(Constants.SUA_TON_KHO);
                BigDecimal quantity = quantityNew.subtract(quantityOld);
                BigDecimal inPrice = quantity.multiply(inPriceNew);
                if (compareQuantity > 0) {
                    no = userService.genCode(comId, Constants.NHAP_KHO);
                    rsInoutWard.setType(Constants.RS_INWARD_TYPE);
                    businessTypeCode = BusinessTypeConstants.RsInWard.EDIT_QUANTITY;
                } else {
                    // giảm tồn kho
                    no = userService.genCode(comId, Constants.XUAT_KHO);
                    rsInoutWard.setType(Constants.RS_OUTWARD_TYPE);
                    if (quantity.compareTo(BigDecimal.valueOf(0)) < 0) {
                        quantity = quantity.multiply(BigDecimal.valueOf(-1));
                        inPrice = inPrice.multiply(BigDecimal.valueOf(-1));
                    }
                    businessTypeCode = BusinessTypeConstants.RsOutWard.EDIT_QUANTITY;
                }
                rsInoutWard.setDescription(
                    "Điều chỉnh tồn kho sản phẩm: " +
                    product.getName() +
                    " từ " +
                    Common.formatBigDecimal(quantityOld) +
                    " thành " +
                    Common.formatBigDecimal(quantityNew.setScale(6))
                );
                Integer businessTypeId = businessTypeRepository.getIdByComIdAndCode(comId, businessTypeCode);
                if (businessTypeId != null) {
                    rsInoutWard.setBusinessTypeId(businessTypeId);
                }
                rsInoutWard.setNo(no);
                rsInoutWard.setQuantity(quantity);
                rsInoutWard.setAmount(inPrice);
                rsInoutWards.add(rsInoutWard);
            }
        }
        for (RsInoutWard rsInoutWard : rsInoutWards) {
            rsInoutWard.setComId(comId);
            rsInoutWard.setCustomerNormalizedName(Common.normalizedName(List.of(rsInoutWard.getNo())));
            rsInoutWard.setDate(ZonedDateTime.now());
            rsInoutWard.setCostAmount(BigDecimal.ZERO);
            rsInoutWard.setTotalAmount(rsInoutWard.getAmount());
            rsInoutWard.setDiscountAmount(BigDecimal.ZERO);
            //  Lấy danh sách bill_product có sản phẩm theo dõi tồn kho

            List<RsInoutWardDetail> rsInoutWardDetails = new ArrayList<>();
            RsInoutWardDetail rsInoutWardDetail = new RsInoutWardDetail();
            rsInoutWardDetail.setProductId(product.getId());
            rsInoutWardDetail.setProductName(product.getName());
            rsInoutWardDetail.setQuantity(rsInoutWard.getQuantity());
            if (isCreate) {
                rsInoutWardDetail.setUnitPrice(inPriceNew);
            } else {
                if (rsInoutWard.getQuantity().compareTo(BigDecimal.ZERO) == 0) {
                    rsInoutWardDetail.setUnitPrice(rsInoutWard.getAmount());
                } else {
                    rsInoutWardDetail.setUnitPrice(rsInoutWard.getAmount().divide(rsInoutWard.getQuantity(), new MathContext(6)));
                }
            }
            rsInoutWardDetail.setAmount(rsInoutWard.getAmount());
            rsInoutWardDetail.setDiscountAmount(BigDecimal.ZERO);
            rsInoutWardDetail.setTotalAmount(rsInoutWard.getAmount());
            rsInoutWardDetail.setUnitName(product.getUnit());
            rsInoutWardDetail.setUnitId(product.getUnitId());
            rsInoutWardDetail.setProductCode(product.getCode());
            rsInoutWardDetail.setPosition(0);
            rsInoutWardDetail.setRsInoutWard(rsInoutWard);
            rsInoutWardDetails.add(rsInoutWardDetail);

            rsInoutWard.setRsInoutWardDetails(rsInoutWardDetails);
        }
        return rsInoutWards;
    }

    private void checkValidIsInventory(Boolean isInventory, BigDecimal inventoryCount, BigDecimal inPrice) {
        if (isInventory) { //Kiem tra neu isInventory == true, inventoryCount, inPrice khong duoc null
            if (inventoryCount == null) {
                throw new BadRequestAlertException(PRODUCT_INVENTORY_COUNT_NULL_VI, ENTITY_NAME, PRODUCT_INVENTORY_COUNT_NULL);
            }
            if (inPrice == null) {
                throw new BadRequestAlertException(PRODUCT_IN_PRICE_NULL_VI, ENTITY_NAME, PRODUCT_IN_PRICE_NULL);
            }
        }
    }

    public Product partialUpdate(Product product, UpdateProdRequest updateProdRequest, User user) {
        //        Integer productId = productProductUnitRepository.findProductIdById(updateProdRequest.getId());
        Integer productId = updateProdRequest.getId();
        if (updateProdRequest.getCode2() != null) {
            product.setCode2(updateProdRequest.getCode2());
        }
        if (!Strings.isNullOrEmpty(updateProdRequest.getName()) || product.getIsTopping()) {
            Integer count = toppingToppingGroupRepository.checkUpdateProduct(productId);
            if (!updateProdRequest.getName().equals(product.getName())) {
                if (count > 0) {
                    throw new BadRequestAlertException(
                        PRODUCT_TOPPING_CANNOT_UPDATE_VI.replace("@@", product.getName()),
                        ENTITY_NAME,
                        PRODUCT_TOPPING_CANNOT_UPDATE
                    );
                }
                List<ToppingToppingGroup> groups = toppingToppingGroupRepository.findAllByProductId(productId);
                groups.forEach(group -> group.setProductName(updateProdRequest.getName()));
                toppingToppingGroupRepository.saveAll(groups);
            }
            if (product.getIsTopping() && !updateProdRequest.getIsTopping() && count > 0) {
                throw new BadRequestAlertException(
                    PRODUCT_TOPPING_CANNOT_UPDATE_VI.replace("@@", product.getName()),
                    ENTITY_NAME,
                    PRODUCT_TOPPING_CANNOT_UPDATE
                );
            }
            product.setName(updateProdRequest.getName());
        }
        if (updateProdRequest.getUnitId() != 0 && updateProdRequest.getUnitId() != null) {
            product.setUnitId(updateProdRequest.getUnitId());
        } else {
            product.setUnitId(null);
        }
        if (updateProdRequest.getUnit() != null && !Strings.isNullOrEmpty(updateProdRequest.getUnit().trim())) {
            product.setUnit(updateProdRequest.getUnit());
            Optional<ProductProductUnit> productUnit = productProductUnitRepository.findByProductIdAndComIdAndIsPrimaryIsNull(
                product.getId(),
                user.getCompanyId()
            );
            productUnit.ifPresent(productProductUnitRepository::delete);
            Optional<ProductProductUnit> unitOptional = productProductUnitRepository.findByProductIdAndComIdAndIsPrimaryTrue(
                product.getId(),
                user.getCompanyId()
            );
            if (unitOptional.isPresent()) {
                ProductProductUnit unit = unitOptional.get();
                unit.setUnitName(updateProdRequest.getUnit());
                unit.setDescription(
                    ProductConstant.VALIDATE.generateDescription(
                        updateProdRequest.getUnit(),
                        BigDecimal.ONE,
                        updateProdRequest.getUnit(),
                        ProductConstant.CONVERSION_UNIT.MUL_FORMULA
                    )
                );
                if (!Strings.isNullOrEmpty(updateProdRequest.getUnit())) {
                    unit.setUnitNormalizedName(Common.normalizedName(List.of(updateProdRequest.getUnit())));
                }
                productProductUnitRepository.save(unit);
            }
            List<ProductProductUnit> units = productProductUnitRepository.findByProductIdAndComIdAndIsPrimaryFalse(
                productId,
                user.getCompanyId()
            );
            for (ProductProductUnit unit : units) {
                unit.setDescription(
                    ProductConstant.VALIDATE.generateDescription(
                        unit.getUnitName(),
                        unit.getConvertRate(),
                        updateProdRequest.getUnit(),
                        unit.getFormula() ? ProductConstant.CONVERSION_UNIT.DIV_FORMULA : ProductConstant.CONVERSION_UNIT.MUL_FORMULA
                    )
                );
            }
            productProductUnitRepository.saveAll(units);
        } else {
            if (
                (
                    !Strings.isNullOrEmpty(product.getUnit()) &&
                    rsInoutWardRepository.countAllByComIdAndProductIdAndUnitName(user.getCompanyId(), product.getId(), product.getUnit()) >
                    0
                ) ||
                (
                    product.getUnitId() != null &&
                    product.getUnitId() != 0 &&
                    billProductRepository.countAllByProductIdAndUnitIdAndUnit(product.getId(), product.getUnitId(), product.getUnit()) > 0
                )
            ) {
                throw new BadRequestAlertException(PRODUCT_PRODUCT_UNIT_NOT_DELETE_VI, ENTITY_NAME, PRODUCT_PRODUCT_UNIT_NOT_DELETE);
            }
            Optional<ProductProductUnit> unitOptional = productProductUnitRepository.findByProductIdAndComIdAndIsPrimaryTrue(
                product.getId(),
                user.getCompanyId()
            );
            if (unitOptional.isPresent()) {
                ProductProductUnit unit = unitOptional.get();
                unit.setProductUnitId(null);
                unit.setUnitName(null);
                unit.setIsPrimary(Boolean.TRUE);
                unit.setConvertRate(null);
                unit.setFormula(Boolean.FALSE);
                unit.setDescription(null);
                unit.setBarcode(updateProdRequest.getBarcode());
                productProductUnitRepository.save(unit);
            }
            product.setUnitId(null);
        }
        if (updateProdRequest.getPurchasePrice() != null) {
            if (!Objects.equals(product.getPurchasePrice(), updateProdRequest.getPurchasePrice())) {
                Optional<ProductProductUnit> unitOptional = productProductUnitRepository.findByProductIdAndComIdAndIsPrimaryTrue(
                    product.getId(),
                    user.getCompanyId()
                );
                if (unitOptional.isPresent()) {
                    ProductProductUnit unit = unitOptional.get();
                    unit.setPurchasePrice(updateProdRequest.getPurchasePrice());
                    productProductUnitRepository.save(unit);
                }
            }
            product.setPurchasePrice(updateProdRequest.getPurchasePrice());
        }
        if (updateProdRequest.getSalePrice() != null) {
            if (!Objects.equals(product.getSalePrice(), updateProdRequest.getSalePrice())) {
                Optional<ProductProductUnit> unitOptional = productProductUnitRepository.findByProductIdAndComIdAndIsPrimaryTrue(
                    product.getId(),
                    user.getCompanyId()
                );
                if (unitOptional.isPresent()) {
                    ProductProductUnit unit = unitOptional.get();
                    unit.setSalePrice(updateProdRequest.getSalePrice());
                    productProductUnitRepository.save(unit);
                }
            }
            product.setSalePrice(updateProdRequest.getSalePrice());
        }
        if (updateProdRequest.getVatRate() != null) {
            product.setVatRate(updateProdRequest.getVatRate());
        }
        if (updateProdRequest.getInventoryTracking() != null) {
            product.setInventoryTracking(updateProdRequest.getInventoryTracking());
        }
        if (updateProdRequest.getInventoryCount() != null) {
            if (!Objects.equals(product.getInventoryCount(), updateProdRequest.getInventoryCount())) {
                List<ProductProductUnit> units = new ArrayList<>();
                Optional<ProductProductUnit> productUnitOptional = productProductUnitRepository.findByProductIdAndComIdAndIsPrimaryIsNull(
                    product.getId(),
                    user.getCompanyId()
                );
                if (productUnitOptional.isPresent()) {
                    ProductProductUnit unit = productUnitOptional.get();
                    unit.setOnHand(updateProdRequest.getInventoryCount());
                    units.add(unit);
                } else {
                    Optional<ProductProductUnit> unitOptional = productProductUnitRepository.findByProductIdAndComIdAndIsPrimaryTrue(
                        product.getId(),
                        user.getCompanyId()
                    );
                    if (unitOptional.isPresent()) {
                        ProductProductUnit unit = unitOptional.get();
                        unit.setOnHand(updateProdRequest.getInventoryCount());
                        units.add(unit);
                    }
                }
                productProductUnitRepository.saveAll(units);
            }
            product.setInventoryCount(updateProdRequest.getInventoryCount());
        }
        if (updateProdRequest.getDescription() != null) {
            product.setDescription(updateProdRequest.getDescription());
        }
        //        if (updateProdRequest.getBarcode() != null) {
        product.setBarCode(updateProdRequest.getBarcode());
        //        }
        if (updateProdRequest.getIsTopping() != null) {
            product.setIsTopping(updateProdRequest.getIsTopping());
        }
        product.setDiscountVatRate(updateProdRequest.getDiscountVatRate());
        return product;
    }

    private boolean checkValidUnitAndUnitId(Integer requestUnitId, String requestUnit, Integer comId, Optional<ProductUnit> productUnit) {
        //Nếu có unitId khác 0 và truyền vào unit khác rỗng,
        if (requestUnitId != 0 && !Strings.isNullOrEmpty(requestUnit)) {
            // kiểm tra xem unitId truyền vào có tồn tại trong công ty không
            if (productUnit.isEmpty()) throw new BadRequestAlertException(UNIT_NOT_FOUND_VI, ENTITY_NAME, UNIT_NOT_FOUND);
            //so sánh chéo giữa unit từ UnitId và unit truyền vào
            if (!productUnit.get().getName().toLowerCase().equals(requestUnit.toLowerCase())) {
                throw new BadRequestAlertException(UNIT_NAME_NOT_INVALID_VI, ENTITY_NAME, UNIT_NAME_NOT_INVALID);
            }
            return true;
        }
        //Trả lỗi nếu truyền vào chỉ unitId nhưng không truyền unit để so sánh chéo
        else if ((requestUnitId != 0) && Strings.isNullOrEmpty(requestUnit)) throw new BadRequestAlertException(
            UNIT_NAME_MUST_NOT_EMPTY_VI,
            ENTITY_NAME,
            UNIT_NAME_MUST_NOT_EMPTY
        );
        //Trả lỗi nếu truyền vào chỉ unit nhưng không truyền unitId để so sánh chéo
        else if (requestUnitId == 0 && !Strings.isNullOrEmpty(requestUnit)) throw new BadRequestAlertException(
            UNIT_MUST_NOT_EMPTY_VI,
            ENTITY_NAME,
            UNIT_MUST_NOT_EMPTY
        );
        return false;
    }

    @Transactional(readOnly = true)
    public ResultDTO getProductDetail(Integer id) {
        User user = userService.getUserWithAuthorities();
        //        Integer productId = productProductUnitRepository.findProductIdById(id);
        ProductItem productItem = productRepository.findByProductId(userService.getCompanyId(), id);
        if (productItem == null) {
            throw new InternalServerException(ExceptionConstants.PRODUCT_NOT_FOUND_VI, ENTITY_NAME, ExceptionConstants.PRODUCT_NOT_FOUND);
        }
        ProductDetailResponse productResponse = new ProductDetailResponse();
        BeanUtils.copyProperties(productItem, productResponse);
        // get list ProductGroup by ProductId
        List<ProductGroupResult> groups = productGroupRepository.getAllForOffline(
            user.getCompanyId(),
            Arrays.asList(productResponse.getId())
        );

        List<ProductProcessingAreaResult> processingAreaList = processingAreaRepository.findForProductDetail(user.getCompanyId(), id);
        productResponse.setProcessingArea((processingAreaList == null || processingAreaList.isEmpty()) ? null : processingAreaList.get(0));

        List<ProductProductUnitResponse> units = productProductUnitCustom.getByProductId(user.getCompanyId(), id);
        productResponse.setGroups(groups);
        productResponse.setToppings(toppingGroupRepository.getForProductDetail(id));
        List<Integer> ids = units.stream().map(ProductProductUnitResponse::getId).collect(Collectors.toList());
        List<ProductProcessingAreaDetail> details = processingAreaProductRepository.findForProductDetail(user.getCompanyId(), ids);
        Map<Integer, ProductProcessingAreaDetail> areaResultMap = new HashMap<>();
        for (ProductProcessingAreaDetail detail : details) {
            areaResultMap.put(detail.getProductProductUnitId(), detail);
        }
        for (ProductProductUnitResponse unitResponse : units) {
            unitResponse.setProcessingArea(areaResultMap.getOrDefault(unitResponse.getId(), null));
        }
        productResponse.setConversionUnits(units);

        log.error(ENTITY_NAME + "_getProductDetail: " + GET_PRODUCT_DETAIL_SUCCESS_VI);
        return new ResultDTO(SUCCESS, GET_PRODUCT_DETAIL_SUCCESS_VI, true, productResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ResultDTO getAllProductUnit(String keyword) {
        //Kiem tra nguoi dung da dang nhap chua
        User user = userService.getUserWithAuthorities();
        List<ProductUnitResponse> units = productUnitRepository.findByComId(user.getCompanyId(), keyword);
        return new ResultDTO(SUCCESS, GET_ALL_PRODUCT_UNIT_SUCCESS, true, units);
    }

    @Override
    @Transactional(readOnly = true)
    public ResultDTO findByBarcode(String barcode) {
        User user = userService.getUserWithAuthorities();
        ProductDetailResponse product = productRepository.findByBarcode(user.getCompanyId(), barcode);
        if (product != null) {
            product.setGroups(productGroupRepository.getAllForOffline(user.getCompanyId(), Arrays.asList(product.getId())));
            List<ProductProductUnitResponse> units = productProductUnitCustom.getByProductId(user.getCompanyId(), product.getId());
            product.setConversionUnits(units);
        } else {
            throw new InternalServerException(PRODUCT_NOT_FOUND_VI, PRODUCT_NOT_FOUND_VI, PRODUCT_NOT_FOUND);
        }
        return new ResultDTO(SUCCESS, GET_PRODUCT_DETAIL_SUCCESS_VI, true, product);
    }

    @Override
    public ResultDTO findByBarcodeForBill(String barcode) {
        User user = userService.getUserWithAuthorities();
        ProductItemResponse item = productRepository.findByBarcodeForBill(user.getCompanyId(), barcode);
        List<ProductItemResponse> productResponses = new ArrayList<>();
        if (item != null) {
            productResponses.add(item);
            getProductResult(productResponses, user.getCompanyId(), Boolean.FALSE);
        } else {
            throw new InternalServerException(PRODUCT_NOT_FOUND_VI, PRODUCT_NOT_FOUND_VI, PRODUCT_NOT_FOUND);
        }
        return new ResultDTO(SUCCESS, GET_PRODUCT_DETAIL_SUCCESS_VI, true, productResponses.get(0), productResponses.size());
    }

    @Override
    public ResultDTO importUrlByTaxCode(List<ImportUrlRequest> importUrlRequests) {
        for (ImportUrlRequest item : importUrlRequests) {
            JsonNode data = Common.readBackupData(item.getUrl());
            try {
                Integer comId = companyRepository.findCompanyIdByTaxCode(item.getTaxCode());
                List<UnitResponse> productUnit = productUnitRepository.findAllByComId(comId);
                Map<String, Integer> mapUnit = new HashMap<>();
                for (UnitResponse unitItem : productUnit) {
                    mapUnit.put(Common.unAccent(unitItem.getName().toLowerCase()), unitItem.getId());
                }
                List<BackupProduct> backupProduct = objectMapper.readValue(
                    data.get("product").toString(),
                    new TypeReference<List<BackupProduct>>() {}
                );
                List<ProductGroupDb> productGroupDbs = objectMapper.readValue(
                    data.get("productGroupDb").toString(),
                    new TypeReference<List<ProductGroupDb>>() {}
                );
                List<CustomerBackUp> customers = objectMapper.readValue(
                    data.get("customer").toString(),
                    new TypeReference<List<CustomerBackUp>>() {}
                );
                List<ProductProductGroupBackup> groupGroup = objectMapper.readValue(
                    data.get("productProductGroupDb").toString(),
                    new TypeReference<List<ProductProductGroupBackup>>() {}
                );
                //                Lấy ra list lớn nhất để chạy 1 vòng for duy nhất cho 3 list
                int sizeMax = backupProduct.size();
                if (sizeMax < productGroupDbs.size()) {
                    sizeMax = productGroupDbs.size();
                }
                if (sizeMax < customers.size()) {
                    sizeMax = customers.size();
                }
                List<Product> products = new ArrayList<>();
                List<Customer> customerList = new ArrayList<>();
                List<ProductGroup> productGroupList = new ArrayList<>();
                Map<String, Product> mapProduct = new HashMap<>();
                Map<String, ProductGroup> mapGroup = new HashMap<>();
                for (int i = 0; i < sizeMax; i++) {
                    //                    prod
                    if (i < backupProduct.size()) {
                        Product product = new Product();
                        BeanUtils.copyProperties(backupProduct.get(i), product);
                        if (!Strings.isNullOrEmpty(product.getUnit())) {
                            if (mapUnit.containsKey(Common.unAccent(product.getUnit().toLowerCase()))) {
                                product.setUnitId(mapUnit.get(Common.unAccent(product.getUnit().toLowerCase())));
                            }
                        } else {
                            product.setUnit(null);
                        }
                        product.setComId(comId);
                        product.setActive(true);
                        product.setFeature(1);
                        mapProduct.put(backupProduct.get(i).getItem(), product);
                        if (!Strings.isNullOrEmpty(backupProduct.get(i).getInventoryIdItem())) {
                            product.setInventoryTracking(true);
                        }
                        if (Strings.isNullOrEmpty(product.getBarCode())) {
                            product.setBarCode(null);
                        }
                        if (Strings.isNullOrEmpty(product.getBarCode2())) {
                            product.setBarCode2(null);
                        }
                        if (i == 0) {
                            product.setCode(userService.genCode(comId, Constants.PRODUCT_CODE));
                        } else {
                            Integer value = Integer.valueOf(products.get(i - 1).getCode().replace("SP", ""));
                            product.setCode(Constants.PRODUCT_CODE + (value + 1));
                        }
                        products.add(product);
                    }
                    //                    customer
                    if (i < customers.size()) {
                        Customer customer = new Customer();
                        BeanUtils.copyProperties(customers.get(i), customer);
                        customer.setComId(comId);
                        customer.setActive(true);
                        if (Strings.isNullOrEmpty(customer.getEmail())) {
                            customer.setEmail(null);
                        }
                        if (Strings.isNullOrEmpty(customer.getTaxCode())) {
                            customer.setTaxCode(null);
                        }
                        if (Strings.isNullOrEmpty(customer.getPhoneNumber())) {
                            customer.setPhoneNumber(null);
                        }
                        if (Strings.isNullOrEmpty(customer.getDescription())) {
                            customer.setDescription(null);
                        }
                        if (Strings.isNullOrEmpty(customer.getAddress())) {
                            customer.setAddress(null);
                        }
                        if (i == 0) {
                            customer.setCode(userService.genCode(comId, Constants.CUSTOMER_CODE));
                        } else {
                            Integer value = Integer.valueOf(customerList.get(i - 1).getCode().replace(Constants.CUSTOMER_CODE, ""));
                            customer.setCode(Constants.CUSTOMER_CODE + (value + 1));
                        }
                        customerList.add(customer);
                    }
                    //                    productGroup
                    if (i < productGroupDbs.size()) {
                        ProductGroup productGroup = new ProductGroup();
                        BeanUtils.copyProperties(productGroupDbs.get(i), productGroup);
                        productGroup.setComId(comId);
                        mapGroup.put(productGroupDbs.get(i).getIditem(), productGroup);
                        productGroupList.add(productGroup);
                    }
                }
                //                lưu đối tượng
                if (customerList.size() > 0) {
                    customerRepository.saveAll(customerList);
                }
                if (productGroupList.size() > 0) {
                    productGroupRepository.saveAll(productGroupList);
                }
                if (products.size() > 0) {
                    productRepository.saveAll(products);
                }
                //                gán vào productproductGroup
                List<ProductProductGroup> productProductGroupList = new ArrayList<>();
                for (ProductProductGroupBackup groupGroupItem : groupGroup) {
                    if (mapProduct.containsKey(groupGroupItem.getProductId()) && mapGroup.containsKey(groupGroupItem.getProductGroupId())) {
                        ProductProductGroup productProductGroup = new ProductProductGroup();
                        productProductGroup.setProductId(mapProduct.get(groupGroupItem.getProductId()).getId());
                        productProductGroup.setProductGroupId(mapGroup.get(groupGroupItem.getProductGroupId()).getId());
                        productProductGroupList.add(productProductGroup);
                    }
                }
                productProductGroupRepository.saveAll(productProductGroupList);
                //                try {
                ////                    TimeUnit.SECONDS.sleep(2);
                //                } catch (InterruptedException e) {
                //                    e.printStackTrace();
                //                }
                List<TaskLogSendQueue> taskLogs = new ArrayList<>();
                transactionTemplate.execute(status -> {
                    //                    List<TaskLogSendQueue> taskLogs = new ArrayList<>();
                    try {
                        for (Product productItem : products) {
                            taskLogs.add(
                                createAndPublishQueueTask(comId, productItem.getId(), TaskLogConstants.Type.EB_CREATE_MATERIAL_GOODS)
                            );
                        }
                        for (Customer cus : customerList) {
                            taskLogs.add(createAndPublishQueueTaskCustomer(comId, cus.getId(), TaskLogConstants.Type.EB_CREATE_ACC_OBJECT));
                        }
                    } catch (Exception e) {
                        log.error("Can not create queue task for eb88 creating material goods: {}", e.getMessage());
                    }
                    return taskLogs;
                });
                for (TaskLogSendQueue task : taskLogs) {
                    userService.sendTaskLog(task);
                }
            } catch (JsonProcessingException e) {
                throw new InternalServerException(EXCEPTION_ERROR_VI, e.toString(), EXCEPTION_ERROR);
            }
        }
        return new ResultDTO(SUCCESS, SUCCESS_UPDATE, true);
    }

    @Override
    public ResultDTO createNewProductUnit(CreateProductUnitRequest request) {
        User user = userService.getUserWithAuthorities(request.getComId());
        int companyId = user.getCompanyId();
        //Kiểm tra tên unit mới đã tồn tại hay chưa
        // TODO: tạm thời query với UPPERCASE cast, cần fix lại sau này tên đơn vị tính ở DB đều là UPPERCASE
        String checkName = request.getUnitName().trim().toUpperCase();
        Optional<ProductUnit> productUnitOptional = productUnitRepository.findByComIdAndUppercaseName(companyId, checkName);
        if (productUnitOptional.isPresent()) throw new BadRequestAlertException(UNIT_NAME_IS_EXISTED_VI, ENTITY_NAME, UNIT_NAME_IS_EXISTED);

        ProductUnit productUnit = new ProductUnit();
        productUnit.setComId(companyId);
        productUnit.setName(request.getUnitName());
        productUnit.setDescription(request.getUnitDescription());
        productUnit.setActive(true);
        TaskLogSendQueue taskLogSendQueue = transactionTemplate.execute(status -> {
            productUnitRepository.save(productUnit);
            //Thêm vào taskLog để xử lý
            try {
                return createAndPublishQueueProductUnitTask(companyId, productUnit.getId(), TaskLogConstants.Type.EB_CREATE_PRODUCT_UNIT);
            } catch (Exception e) {
                log.error("Can not create queue task for eb88 creating product unit: {}", e.getMessage());
            }
            return null;
        });
        if (taskLogSendQueue != null) {
            userService.sendTaskLog(taskLogSendQueue);
        }
        ProductUnitResponse response = new ProductUnitResponse();
        BeanUtils.copyProperties(productUnit, response);
        return new ResultDTO(SUCCESS, CREATE_PRODUCT_UNIT_SUCCESS, true, response);
    }

    @Override
    public ResultDTO deleteProductUnit(DeleteConversionUnitRequest request, Boolean isActionAdmin) {
        User user;
        if (isActionAdmin) {
            user = new User();
            user.setCompanyId(request.getComId());
        } else {
            user = userService.getUserWithAuthorities(request.getComId());
        }
        Optional<ProductProductUnit> unitOptional = productProductUnitRepository.findByIdAndComIdAndIsPrimaryFalse(
            request.getId(),
            user.getCompanyId()
        );
        if (unitOptional.isEmpty()) {
            throw new BadRequestAlertException(UNIT_NOT_FOUND_VI, ENTITY_NAME, UNIT_NOT_FOUND);
        }
        ProductProductUnit unit = unitOptional.get();
        if (
            rsInoutWardRepository.countAllByComIdAndProductIdAndUnitName(user.getCompanyId(), unit.getProductId(), unit.getUnitName()) >
            0 ||
            billProductRepository.countAllByProductProductUnitId(unit.getId()) > 0
        ) {
            throw new BadRequestAlertException(PRODUCT_PRODUCT_UNIT_NOT_DELETE_VI, ENTITY_NAME, PRODUCT_PRODUCT_UNIT_NOT_DELETE);
        }
        //        productProductUnitRepository.delete(unitOptional.get());
        return new ResultDTO(SUCCESS, ResultConstants.DELETE_PRODUCT_CONVERSION_UNIT_SUCCESS_VI, Boolean.TRUE);
    }

    public ResultDTO updateProductAsync(List<Integer> companyIds, EB88ApiClient eb88ApiClient) {
        for (Integer item : companyIds) {
            try {
                List<UnitEb88Response> unitEb88Responses = eb88ApiClient.getUnits(item);
                Map<String, Integer> mapUnit = new HashMap<>();
                for (UnitEb88Response unit : unitEb88Responses) {
                    if (!Strings.isNullOrEmpty(unit.getUnitName())) {
                        mapUnit.put(unit.getUnitName().toLowerCase(), unit.getId());
                    }
                }
                List<ProductUnit> productUnits = productUnitRepository.findAllProductUnitByComId(item);
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
                return new ResultDTO(SUCCESS, SUCCESS_UPDATE, true);
            } catch (Exception e) {
                return new ResultDTO(FALSE, e.getMessage());
            }
        }

        return new ResultDTO(FALSE, UPDATE_ERROR);
    }

    @Override
    public ResultDTO importProductByExcel(MultipartFile file, Integer comId) {
        //        try {
        //            // Đọc dữ liệu từ tệp Excel
        //            //            User user = userService.getUserWithAuthorities();
        //            List<Map<String, String>> dataList = new ArrayList<>();
        //            Workbook workbook = WorkbookFactory.create(file.getInputStream());
        //            Sheet sheet = workbook.getSheetAt(0);
        //            Row headerRow = sheet.getRow(0);
        //
        //            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
        //                Row row = sheet.getRow(i);
        //                Map<String, String> data = new HashMap<>();
        //
        //                for (int j = 0; j < row.getLastCellNum(); j++) {
        //                    Cell cell = row.getCell(j);
        //                    if (headerRow.getCell(j) != null && cell != null) {
        //                        data.put(headerRow.getCell(j).getStringCellValue(), cell.toString());
        //                    }
        //                }
        //
        //                dataList.add(data);
        //            }
        //
        //            workbook.close();
        //
        //            // Chuyển đổi dữ liệu sang JSON
        //            String json = objectMapper.writeValueAsString(dataList);
        //            List<ProductExcelResponse> products = objectMapper.readValue(json, new TypeReference<List<ProductExcelResponse>>() {});
        //            Map<String, ProductGroup> mapGroup = new HashMap<>();
        //            List<Product> products1 = new ArrayList<>();
        //            int i = 0;
        //            for (ProductExcelResponse item : products) {
        //                Product productItem = new Product();
        //                ProductGroup productGroup = new ProductGroup();
        //                if (mapGroup.containsKey(item.getGroupName())) {
        //                    productGroup = mapGroup.get(item.getGroupName());
        //                } else {
        //                    productGroup.setName(item.getGroupName());
        //                    productGroup.setComId(comId);
        //                    productGroupRepository.save(productGroup);
        //                }
        //                mapGroup.put(item.getGroupName(), productGroup);
        //                modelMapper.map(item, productItem);
        //                productItem.setComId(comId);
        //                if (i == 0) {
        //                    productItem.setCode(userService.genCode(comId, Constants.PRODUCT_CODE));
        //                } else {
        //                    Integer value = Integer.valueOf(products1.get(i - 1).getCode().replace(Constants.PRODUCT_CODE, ""));
        //                    productItem.setCode(Constants.PRODUCT_CODE + (value + 1));
        //                }
        //                i++;
        //                productItem.setProductGroups(List.of(productGroup));
        //                productItem.setActive(true);
        //                if (productItem.getInventoryTracking() == null) {
        //                    productItem.setInventoryTracking(false);
        //                }
        //                products1.add(productItem);
        //            }
        //            productRepository.saveAll(products1);
        //            ResultDTO resultDTO = new ResultDTO();
        //            List<TaskLog> taskLogs = new ArrayList<>();
        //            for (Product product : products1) {
        //                try {
        //                    taskLogs.add(createAndPublishQueueTask2(comId, product.getId(), TaskLogConstants.Type.EB_CREATE_MATERIAL_GOODS));
        //                } catch (Exception e) {
        //                    log.error("Can not create queue task for eb88 updating material goods: {}", e.getMessage());
        //                }
        //            }
        //            return new ResultDTO(SUCCESS, SUCCESS_CREATE, true, taskLogs);
        //        } catch (Exception e) {
        //            return null;
        //        }
        return null;
    }

    @Override
    public ResultDTO importProductByExcel1(MultipartFile file, Integer comId) {
        List<ProductExcelResponse> products = new ArrayList<>();
        Map<String, ProductUnit> mapNewUnit = new HashMap<>();
        Map<String, Integer> mapUnit = new HashMap<>();
        List<ProductUnit> productUnits = productUnitRepository.findAllProductUnitByComId(comId);
        try {
            // Đọc dữ liệu từ tệp Excel
            // User user = userService.getUserWithAuthorities();
            Workbook workbook = WorkbookFactory.create(file.getInputStream());

            //          Đọc danh sách produc từ Excel Sheet 0
            Sheet sheet = workbook.getSheetAt(0);

            for (ProductUnit item : productUnits) {
                mapUnit.put(item.getName().trim().toLowerCase(), item.getId());
            }
            int countItem = 0;
            for (int i = 1; i <= Common.getMaxRowNumberImportExcel(sheet.getLastRowNum()); i++) {
                Row row = sheet.getRow(i);
                ProductExcelResponse productExcelResponse = new ProductExcelResponse();
                for (int j = 0; j < 11; j++) {
                    Cell cell = null;
                    try {
                        cell = row.getCell(j);
                    } catch (Exception ex) {
                        break;
                    }
                    FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                    CellValue cellValue = evaluator.evaluate(cell);
                    String cellString = Common.getRawValueExcel(cellValue);
                    if (cell != null && !cell.toString().isBlank() && !Strings.isNullOrEmpty(cellString)) {
                        cellString = cellString.replaceAll("\\s{2,}", " ").trim();
                        if (cellString.endsWith(".0")) {
                            cellString = cellString.substring(0, cellString.length() - 2);
                        }
                        switch (j) {
                            case 0:
                                {
                                    productExcelResponse.setCode2(cellString);
                                    break;
                                }
                            case 1:
                                {
                                    productExcelResponse.setName(cellString);
                                    break;
                                }
                            case 2:
                                {
                                    productExcelResponse.setBarCode(cellString);
                                    break;
                                }
                            case 3:
                                {
                                    productExcelResponse.setGroupName(cellString);
                                    break;
                                }
                            case 4:
                                {
                                    BigDecimal inPrice = null;
                                    inPrice = new BigDecimal(cellString);
                                    productExcelResponse.setInPrice(inPrice);
                                    break;
                                }
                            case 5:
                                {
                                    BigDecimal outPrice = null;
                                    outPrice = new BigDecimal(cellString);
                                    productExcelResponse.setOutPrice(outPrice);
                                    break;
                                }
                            case 6:
                                {
                                    productExcelResponse.setUnit(cellString);
                                    if (mapUnit.containsKey(cellString.toLowerCase())) {
                                        productExcelResponse.setUnitId(mapUnit.get(cellString.toLowerCase()));
                                    } else {
                                        ProductUnit productUnit = new ProductUnit();
                                        productUnit.setComId(comId);
                                        productUnit.setName(cellString);
                                        productUnit.setActive(true);
                                        mapNewUnit.put(cellString, productUnit);
                                    }
                                    break;
                                }
                            case 7:
                                {
                                    boolean inventoryChecking = cellString.equalsIgnoreCase("true") || cellString.equalsIgnoreCase("Có");
                                    productExcelResponse.setInventoryTracking(inventoryChecking);
                                    break;
                                }
                            case 8:
                                {
                                    BigDecimal inventoryCount = BigDecimal.valueOf(Double.parseDouble(cellString));
                                    productExcelResponse.setInventoryCount(inventoryCount);
                                    break;
                                }
                            case 9:
                                {
                                    productExcelResponse.setVatRate(getVatRate(cellString));
                                    break;
                                }
                            case 10:
                                {
                                    productExcelResponse.setDescription(cellString);
                                    break;
                                }
                        }
                    }
                }
                if (Strings.isNullOrEmpty(productExcelResponse.getName()) && Strings.isNullOrEmpty(productExcelResponse.getCode2())) {
                    countItem++;
                } else {
                    products.add(productExcelResponse);
                }
                if (countItem == 2) {
                    break;
                }
            }
            if (products.isEmpty()) {
                return new ResultDTO(FAIL, PRODUCT_IMPORT_EXCEL_ERROR, false);
            }

            workbook.close();
        } catch (Exception e) {
            return new ResultDTO(FAIL, PRODUCT_IMPORT_EXCEL_ERROR, false);
        }
        return importExcelCommon(comId, products, mapNewUnit);
    }

    private ResultDTO importExcelCommon(Integer comId, List<ProductExcelResponse> products, Map<String, ProductUnit> mapNewUnit) {
        List<Product> productsSave = new ArrayList<>();
        List<ProductGroup> productGroups = productGroupRepository.findAllByComId(comId);
        Map<String, ProductGroup> groupMap = new HashMap<>();
        productGroups.forEach(group -> groupMap.put(group.getName().toLowerCase(), group));
        List<String> barcodes = productRepository.getAllBarCodeByComId(comId);
        List<String> code2List = productRepository.getAllCode2ByComId(comId);
        ImportProductAsyncRequest request = new ImportProductAsyncRequest();
        request.setComId(comId);
        int i = 0;
        productUnitRepository.saveAll(mapNewUnit.values());
        request.setProductUnits(new ArrayList<>(mapNewUnit.values()));
        for (ProductExcelResponse item : products) {
            Product productItem = new Product();
            checkProductImportExcel(productItem, item, comId, groupMap, barcodes, code2List);
            item.setName(item.getName().replaceAll("\\s{2,}", " ").trim());
            modelMapper.map(item, productItem);
            productItem.setPurchasePrice(item.getInPrice() == null ? BigDecimal.ZERO : item.getInPrice());
            productItem.setSalePrice(item.getOutPrice() == null ? BigDecimal.ZERO : item.getOutPrice());
            if (!Strings.isNullOrEmpty(productItem.getUnit()) && mapNewUnit.containsKey(productItem.getUnit())) {
                productItem.setUnitId(mapNewUnit.get(productItem.getUnit()).getId());
            }
            if (item.getInPrice() != null) {
                productItem.setPurchasePrice(item.getInPrice());
            }
            if (item.getOutPrice() != null) {
                productItem.setSalePrice(item.getOutPrice());
            }
            productItem.setComId(comId);
            if (i == 0) {
                productItem.setCode(userService.genCode(comId, Constants.PRODUCT_CODE));
            } else {
                Integer value = Integer.valueOf(productsSave.get(i - 1).getCode().replace(Constants.PRODUCT_CODE, ""));
                productItem.setCode(Constants.PRODUCT_CODE + (value + 1));
            }
            i++;
            productItem.setActive(true);
            if (productItem.getInventoryTracking() == null) {
                productItem.setInventoryTracking(false);
            }
            if (productItem.getVatRate() == null) {
                productItem.setVatRate(ProductConstant.VatRate.VAT_RATE_DEFAULT);
            }
            productItem.setNormalizedName(Common.normalizedName(Arrays.asList(productItem.getName())));
            if (!Strings.isNullOrEmpty(productItem.getBarCode())) {
                barcodes.add(productItem.getBarCode());
            }
            if (!Strings.isNullOrEmpty(productItem.getCode2())) {
                code2List.add(productItem.getCode2());
            }
            productItem.setIsTopping(false);
            productsSave.add(productItem);
        }
        if (!productsSave.isEmpty()) {
            productRepository.saveAll(productsSave);
            request.setProducts(productsSave);

            List<ProductProductUnit> productProductUnits = new ArrayList<>();
            for (Product product : productsSave) {
                // tạo mới bản ghi prod_prod_unit và sinh chứng từ kho
                if (product.getUnitId() != null) {
                    ConversionUnitRequest conversionUnitRequest = new ConversionUnitRequest(
                        null,
                        product.getUnitId(),
                        BigDecimal.ONE,
                        0,
                        product.getPurchasePrice(),
                        product.getSalePrice(),
                        true,
                        product.getUnit(),
                        true
                    );
                    productProductUnits.add(
                        convertRequest(null, conversionUnitRequest, product, comId, product.getUnit(), new ArrayList<>())
                    );
                } else {
                    productProductUnits.add(createNewProductProductUnit(product, new ArrayList<>()));
                }
            }
            productProductUnitRepository.saveAll(productProductUnits);
            request.setCount(productsSave.size());
        }
        return new ResultDTO(SUCCESS, SUCCESS_CREATE, true, request);
    }

    @Async
    @Override
    @Transactional
    public void syncDataAfterImport(ImportProductAsyncRequest request) {
        List<TaskLogSendQueue> taskLogs = new ArrayList<>();
        transactionTemplate.execute(status -> {
            List<RsInoutWard> rsInoutWardSave = new ArrayList<>();
            List<ProductUnit> productUnits = request.getProductUnits();
            for (Product product : request.getProducts()) {
                try {
                    if (product.getInventoryTracking()) {
                        List<RsInoutWard> rsInoutWard = convertRsInoutWard(product.getComId(), null, product, true);
                        if (rsInoutWard != null) {
                            rsInoutWardSave.add(rsInoutWard.get(0));
                        }
                    }
                    taskLogs.add(
                        createAndPublishQueueTask2(request.getComId(), product.getId(), TaskLogConstants.Type.EB_CREATE_MATERIAL_GOODS)
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            for (ProductUnit productUnit : productUnits) {
                try {
                    taskLogs.add(
                        createAndPublishQueueProductUnitTask(
                            request.getComId(),
                            productUnit.getId(),
                            TaskLogConstants.Type.EB_CREATE_PRODUCT_UNIT
                        )
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            rsInoutWardRepository.saveAll(rsInoutWardSave);
            return taskLogs;
        });
        if (!request.getProducts().isEmpty() && !taskLogs.isEmpty()) {
            for (TaskLogSendQueue task : taskLogs) {
                userService.sendTaskLog(task);
            }
            log.debug("Sync after save product import Success!, count =" + request.getProducts().size());
        }
    }

    private void checkProductImportExcel(
        Product productItem,
        ProductExcelResponse item,
        Integer comId,
        Map<String, ProductGroup> groupMap,
        List<String> barcodes,
        List<String> code2List
    ) {
        if (Strings.isNullOrEmpty(item.getName())) {
            throw new BadRequestAlertException(PRODUCT_NAME_EMPTY_VI, ENTITY_NAME, PRODUCT_NAME_EMPTY_CODE);
        }

        if (Strings.isNullOrEmpty(item.getCode2())) {
            throw new BadRequestAlertException(PRODUCT_CODE2_EMPTY_VI + ": " + item.getName(), ENTITY_NAME, PRODUCT_CODE2_EMPTY);
        } else {
            if (!code2List.isEmpty() && code2List.contains(item.getCode2().toLowerCase())) {
                throw new BadRequestAlertException(PRODUCT_CODE2_EXISTS_VI + ": " + item.getName(), ENTITY_NAME, PRODUCT_CODE2_EXISTS);
            }
        }

        if (item.getOutPrice() == null) {
            throw new BadRequestAlertException(
                PRODUCT_OUT_PRICE_MUST_NOT_EMPTY_VI + ": " + item.getName(),
                ENTITY_NAME,
                PRODUCT_OUT_PRICE_MUST_NOT_EMPTY_CODE
            );
        }

        if (!Strings.isNullOrEmpty(item.getBarCode()) && !barcodes.isEmpty() && barcodes.contains(item.getBarCode())) {
            throw new BadRequestAlertException(
                PRODUCT_BARCODE_DUPLICATED_VI + ": " + item.getName(),
                ENTITY_NAME,
                PRODUCT_BARCODE_DUPLICATED
            );
        }

        if (!Strings.isNullOrEmpty(item.getGroupName())) {
            String groupName = item.getGroupName().toLowerCase();
            if (groupMap.containsKey(groupName)) {
                productItem.setProductGroups(List.of(groupMap.get(groupName)));
            } else {
                throw new InternalServerException(PRODUCT_GROUP_NOT_EXISTS_VI + ": " + groupName, ENTITY_NAME, PRODUCT_GROUP_NOT_EXISTS);
            }
        }

        if (item.getInventoryTracking()) {
            if (item.getInventoryCount() == null) {
                throw new BadRequestAlertException(
                    PRODUCT_INVENTORY_COUNT_NULL_VI + ": " + item.getName(),
                    ENTITY_NAME,
                    PRODUCT_INVENTORY_COUNT_NULL
                );
            }
            if (item.getInPrice() == null) {
                throw new BadRequestAlertException(PRODUCT_IN_PRICE_NULL_VI + ": " + item.getName(), ENTITY_NAME, PRODUCT_IN_PRICE_NULL);
            }
        }
    }

    @Override
    public ResultDTO reSendQueue(List<Integer> ids) {
        List<TaskLog> taskLogs = taskLogRepository.findAllErrorByAndIds(ids);
        for (TaskLog task : taskLogs) {
            userService.sendTaskLog(new TaskLogSendQueue(task.getId().toString(), task.getType()));
        }
        return new ResultDTO(SUCCESS, SUCCESS, true);
    }

    @Override
    public ResultDTO enableInventoryTracking(ProductStockUpdateRequest request) {
        userService.getUserWithAuthorities(request.getComId());
        Integer productProductUnitId = request.getProductProductUnitId();
        BigDecimal inventoryCountRequest = request.getInventoryCount();
        BigDecimal purchasePriceRequest = request.getPurchasePrice();
        Boolean inventoryTrackingRequest = request.getInventoryTracking();

        // check request
        if (!inventoryTrackingRequest) {
            throw new BadRequestAlertException(PRODUCT_ENABLE_TRACKING_FALSE_VI, ENTITY_NAME, PRODUCT_ENABLE_TRACKING_FALSE);
        }
        checkValidIsInventory(Boolean.TRUE, inventoryCountRequest, purchasePriceRequest);

        Integer comId = request.getComId();
        Optional<ProductProductUnit> productProductUnitOptional = productProductUnitRepository.findByIdAndComId(
            productProductUnitId,
            comId
        );
        if (productProductUnitOptional.isEmpty()) {
            throw new InternalServerException(PRODUCT_NOT_FOUND_VI, ENTITY_NAME, PRODUCT_NOT_FOUND);
        }
        ProductProductUnit productProductUnitRequest = productProductUnitOptional.get();
        Boolean isPrimaryRequest = productProductUnitRequest.getIsPrimary();
        if (!isPrimaryRequest) {
            throw new InternalServerException(PRODUCT_ENABLE_TRACKING_ERROR_VI, ENTITY_NAME, PRODUCT_ENABLE_TRACKING_ERROR);
        }
        Integer productId = productProductUnitRequest.getProductId();

        Optional<Product> productOptional = productRepository.findByIdAndComIdAndActive(
            productId,
            comId,
            ProductConstant.Active.ACTIVE_TRUE
        );
        if (productOptional.isEmpty()) {
            throw new InternalServerException(PRODUCT_NOT_FOUND_VI, ENTITY_NAME, PRODUCT_NOT_FOUND);
        }
        // update for product & product_product_unit
        Product product = productOptional.get();
        if (product.getInventoryTracking()) {
            throw new BadRequestAlertException(PRODUCT_ENABLE_TRACKING_INVALID_VI, ENTITY_NAME, PRODUCT_ENABLE_TRACKING_INVALID);
        }
        product.setInventoryTracking(Boolean.TRUE);
        product.setPurchasePrice(purchasePriceRequest);
        // danh sach pro_pro_unit orderby primary
        List<ProductProductUnit> productProductUnits = productProductUnitRepository.getAllByProductIdAndComId(productId, comId);
        if (inventoryCountRequest != null) {
            BigDecimal onHand = BigDecimal.ZERO;
            for (ProductProductUnit unit : productProductUnits) {
                BigDecimal convertRate = unit.getConvertRate();
                if (unit.getIsPrimary()) {
                    onHand = inventoryCountRequest;
                    unit.setOnHand(onHand);
                    unit.setPurchasePrice(purchasePriceRequest);
                } else {
                    // đã tính đc onHand
                    if (!unit.getFormula()) {
                        unit.setOnHand(onHand.divide(convertRate, new MathContext(6)));
                        unit.setPurchasePrice(purchasePriceRequest.multiply(convertRate));
                    } else {
                        unit.setOnHand(onHand.multiply(convertRate));
                        unit.setPurchasePrice(purchasePriceRequest.divide(convertRate, new MathContext(6)));
                    }
                }
            }
            product.setInventoryCount(request.getInventoryCount());
        }

        List<TaskLogSendQueue> taskLogSendQueuesResult = transactionTemplate.execute(status -> {
            try {
                List<TaskLogSendQueue> taskLogSendQueues = new ArrayList<>();

                // cập nhật sản phẩm, sinh chứng từ khởi tạo kho
                if (product.getInventoryTracking()) {
                    productProductUnitRepository.saveAll(productProductUnits);
                    productRepository.save(product);
                    taskLogSendQueues.add(
                        createAndPublishQueueTask(comId, product.getId(), TaskLogConstants.Type.EB_UPDATE_MATERIAL_GOODS)
                    );
                    List<RsInoutWard> rsInoutWard = convertRsInoutWard(comId, null, product, true);
                    if (rsInoutWard != null) {
                        rsInoutWardRepository.save(rsInoutWard.get(0));
                        taskLogSendQueues.add(
                            createAndPublishQueueRsInWardTask(comId, rsInoutWard.get(0).getId(), BusinessTypeConstants.RsInWard.INITIAL)
                        );
                    }
                }
                return taskLogSendQueues;
            } catch (Exception e) {
                log.error("Can not create queue task for eb88 update material goods & create rsInOutWard: {}", e.getMessage());
                return null;
            }
        });
        if (taskLogSendQueuesResult != null) {
            for (TaskLogSendQueue task : taskLogSendQueuesResult) {
                userService.sendTaskLog(task);
            }
        }
        return new ResultDTO(SUCCESS, UPDATE_PRODUCT_SUCCESS_VI, true);
    }

    @Override
    public ResultDTO checkBarCode(String barcode) {
        if (!Strings.isNullOrEmpty(barcode)) {
            User user = userService.getUserWithAuthorities();
            Integer count = productProductUnitRepository.getAllBarcode(user.getCompanyId(), barcode);
            if (count > 0) {
                throw new BadRequestAlertException(PRODUCT_BARCODE_DUPLICATED_VI, ENTITY_NAME, PRODUCT_BARCODE_DUPLICATED);
            }
        }
        return new ResultDTO(SUCCESS, SUCCESS_GET_LIST, true);
    }

    @Override
    public ResultDTO alterImage(List<String> images) {
        //        List<String> images = productRepository.getAllOldImage();
        List<String> error = new ArrayList<>();
        for (String image : images) {
            int index = image.lastIndexOf('/');
            if (index != -1) {
                String fileName = image.substring(index + 1);
                String remainUrl = image.substring(0, index);
                index = remainUrl.lastIndexOf('/');
                String comId = remainUrl.substring(index + 1);
                try {
                    String result = Common.saveFile(image, fileName, Constants.IMAGE_FORMAT, ImagePathConstants.PRODUCT + comId, null);
                    if (Strings.isNullOrEmpty(result)) {
                        error.add(image);
                    }
                } catch (Exception e) {
                    error.add(image);
                }
            }
        }
        if (!error.isEmpty()) {
            return new ResultDTO(SUCCESS, "Những ảnh bên dưới bị lỗi", true, error);
        }
        return new ResultDTO(SUCCESS, SUCCESS_UPDATE, true);
    }

    @Override
    public ResultDTO insertDefaultProduct() {
        List<Integer> ids = companyRepository.getAllCompanyId();
        List<ProductProductUnit> productUnits = new ArrayList<>();

        for (Integer companyId : ids) {
            List<Product> products = new ArrayList<>();
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
        }
        productProductUnitRepository.saveAll(productUnits);
        return new ResultDTO(SUCCESS, SUCCESS, true);
    }

    @Override
    public ResultDTO importUrl(List<ImportUrlRequest> importUrlRequests) {
        //        Chức năng dùng tạm để đẩy dữ liệu từ V1 sang
        for (ImportUrlRequest item : importUrlRequests) {
            JsonNode data = Common.readBackupData(item.getUrl());
            try {
                List<UnitResponse> productUnit = productUnitRepository.findAllByComId(item.getComId());
                Map<String, Integer> mapUnit = new HashMap<>();
                for (UnitResponse unitItem : productUnit) {
                    mapUnit.put(Common.unAccent(unitItem.getName().toLowerCase()), unitItem.getId());
                }
                List<BackupProduct> backupProduct = objectMapper.readValue(
                    data.get("product").toString(),
                    new TypeReference<List<BackupProduct>>() {}
                );
                List<ProductGroupDb> productGroupDbs = objectMapper.readValue(
                    data.get("productGroupDb").toString(),
                    new TypeReference<List<ProductGroupDb>>() {}
                );
                List<CustomerBackUp> customers = objectMapper.readValue(
                    data.get("customer").toString(),
                    new TypeReference<List<CustomerBackUp>>() {}
                );
                List<ProductProductGroupBackup> groupGroup = objectMapper.readValue(
                    data.get("productProductGroupDb").toString(),
                    new TypeReference<List<ProductProductGroupBackup>>() {}
                );
                //                Lấy ra list lớn nhất để chạy 1 vòng for duy nhất cho 3 list
                int sizeMax = backupProduct.size();
                if (sizeMax < productGroupDbs.size()) {
                    sizeMax = productGroupDbs.size();
                }
                if (sizeMax < customers.size()) {
                    sizeMax = customers.size();
                }
                List<Product> products = new ArrayList<>();
                List<Customer> customerList = new ArrayList<>();
                List<ProductGroup> productGroupList = new ArrayList<>();
                Map<String, Product> mapProduct = new HashMap<>();
                Map<String, ProductGroup> mapGroup = new HashMap<>();
                for (int i = 0; i < sizeMax; i++) {
                    //                    prod
                    if (i < backupProduct.size()) {
                        Product product = new Product();

                        BeanUtils.copyProperties(backupProduct.get(i), product);
                        if (!Strings.isNullOrEmpty(product.getUnit())) {
                            if (mapUnit.containsKey(Common.unAccent(product.getUnit().toLowerCase()))) {
                                product.setUnitId(mapUnit.get(Common.unAccent(product.getUnit().toLowerCase())));
                            }
                        } else {
                            product.setUnit(null);
                        }
                        product.setComId(item.getComId());
                        product.setActive(true);
                        product.setFeature(1);
                        mapProduct.put(backupProduct.get(i).getItem(), product);
                        if (!Strings.isNullOrEmpty(backupProduct.get(i).getInventoryIdItem())) {
                            product.setInventoryTracking(true);
                        }
                        if (Strings.isNullOrEmpty(product.getBarCode())) {
                            product.setBarCode(null);
                        }
                        if (Strings.isNullOrEmpty(product.getBarCode2())) {
                            product.setBarCode2(null);
                        }
                        if (i == 0) {
                            product.setCode(userService.genCode(item.getComId(), Constants.PRODUCT_CODE));
                        } else {
                            Integer value = Integer.valueOf(products.get(i - 1).getCode().replace("SP", ""));
                            product.setCode(Constants.PRODUCT_CODE + (value + 1));
                        }
                        products.add(product);
                    }
                    //                    customer
                    if (i < customers.size()) {
                        Customer customer = new Customer();
                        BeanUtils.copyProperties(customers.get(i), customer);
                        customer.setComId(item.getComId());
                        customer.setActive(true);
                        if (Strings.isNullOrEmpty(customer.getEmail())) {
                            customer.setEmail(null);
                        }
                        if (Strings.isNullOrEmpty(customer.getTaxCode())) {
                            customer.setTaxCode(null);
                        }
                        if (Strings.isNullOrEmpty(customer.getPhoneNumber())) {
                            customer.setPhoneNumber(null);
                        }
                        if (Strings.isNullOrEmpty(customer.getDescription())) {
                            customer.setDescription(null);
                        }
                        if (Strings.isNullOrEmpty(customer.getAddress())) {
                            customer.setAddress(null);
                        }
                        if (i == 0) {
                            customer.setCode(userService.genCode(item.getComId(), Constants.CUSTOMER_CODE));
                        } else {
                            Integer value = Integer.valueOf(customerList.get(i - 1).getCode().replace(Constants.CUSTOMER_CODE, ""));
                            customer.setCode(Constants.CUSTOMER_CODE + (value + 1));
                        }
                        customerList.add(customer);
                    }
                    //                    productGroup
                    if (i < productGroupDbs.size()) {
                        ProductGroup productGroup = new ProductGroup();
                        BeanUtils.copyProperties(productGroupDbs.get(i), productGroup);
                        productGroup.setComId(item.getComId());
                        mapGroup.put(productGroupDbs.get(i).getIditem(), productGroup);
                        productGroupList.add(productGroup);
                    }
                }
                //                lưu đối tượng
                if (customerList.size() > 0) {
                    customerRepository.saveAll(customerList);
                }
                if (productGroupList.size() > 0) {
                    productGroupRepository.saveAll(productGroupList);
                }
                if (products.size() > 0) {
                    productRepository.saveAll(products);
                }
                //                gán vào productproductGroup
                List<ProductProductGroup> productProductGroupList = new ArrayList<>();
                for (ProductProductGroupBackup groupGroupItem : groupGroup) {
                    if (mapProduct.containsKey(groupGroupItem.getProductId()) && mapGroup.containsKey(groupGroupItem.getProductGroupId())) {
                        ProductProductGroup productProductGroup = new ProductProductGroup();
                        productProductGroup.setProductId(mapProduct.get(groupGroupItem.getProductId()).getId());
                        productProductGroup.setProductGroupId(mapGroup.get(groupGroupItem.getProductGroupId()).getId());
                        productProductGroupList.add(productProductGroup);
                    }
                }
                List<TaskLogSendQueue> taskLogs = new ArrayList<>();
                transactionTemplate.execute(status -> {
                    productProductGroupRepository.saveAll(productProductGroupList);
                    try {
                        for (Product productItem : products) {
                            taskLogs.add(
                                createAndPublishQueueTask(
                                    item.getComId(),
                                    productItem.getId(),
                                    TaskLogConstants.Type.EB_CREATE_MATERIAL_GOODS
                                )
                            );
                        }
                        //                        for (Customer cus : customerList) {
                        //                            taskLogs.add(createAndPublishQueueTaskCustomer(item.getComId(), cus.getId(), TaskLogConstants.Type.EB_CREATE_ACC_OBJECT));
                        //                        }
                    } catch (Exception e) {
                        log.error("Can not create queue task for eb88 creating material goods: {}", e.getMessage());
                    }
                    return taskLogs;
                });
                for (TaskLogSendQueue task : taskLogs) {
                    userService.sendTaskLog(task);
                }
            } catch (JsonProcessingException e) {
                throw new InternalServerException(EXCEPTION_ERROR_VI, e.toString(), EXCEPTION_ERROR);
            }
        }
        return new ResultDTO(SUCCESS, SUCCESS_UPDATE, true);
    }

    private TaskLogSendQueue createAndPublishQueueTaskCustomer(int comId, int customerId, String taskType) throws Exception {
        AccountingObjectTask task = new AccountingObjectTask();
        task.setComId("" + comId);
        task.setAccountingObjectId("" + customerId);
        task.setType("CUSTOMER");
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

    private ProductProductUnit convertRequest(
        Integer id,
        ConversionUnitRequest unitRequest,
        Product product,
        Integer comId,
        String unitName,
        List<ProcessingArea> processingAreas
    ) {
        BigDecimal convertRate = unitRequest.getConvertRate();
        if (unitRequest.getSalePrice() == null) {
            BigDecimal price = product.getSalePrice();
            unitRequest.setSalePrice(
                Objects.equals(unitRequest.getFormula(), ProductConstant.CONVERSION_UNIT.MUL_FORMULA)
                    ? price.multiply(convertRate)
                    : price.divide(convertRate, new MathContext(7))
            );
        }
        if (convertRate.compareTo(BigDecimal.ZERO) == 0) {
            unitRequest.setConvertRate(BigDecimal.ONE);
        }
        if (unitRequest.getPrimary() == null) {
            unitRequest.setPrimary(Boolean.FALSE);
        }
        if (unitRequest.getFormula() == null) {
            unitRequest.setFormula(ProductConstant.CONVERSION_UNIT.MUL_FORMULA);
        }
        ProductProductUnit productProductUnit = new ProductProductUnit();
        BeanUtils.copyProperties(unitRequest, productProductUnit);
        productProductUnit.setId(id);
        productProductUnit.setComId(comId);
        productProductUnit.setConvertRate(convertRate);
        productProductUnit.setProductId(product.getId());
        productProductUnit.setProductUnitId(unitRequest.getProductUnitId());
        productProductUnit.setIsPrimary(unitRequest.getPrimary());
        productProductUnit.setFormula(
            Objects.equals(unitRequest.getFormula(), ProductConstant.CONVERSION_UNIT.MUL_FORMULA) ? Boolean.FALSE : Boolean.TRUE
        );
        Optional<ProductUnit> productUnitOptional = productUnitRepository.findByIdAndComId(unitRequest.getProductUnitId(), comId);
        if (productUnitOptional.isEmpty()) {
            throw new BadRequestAlertException(PRODUCT_UNIT_NOT_FOUND_VI, ENTITY_NAME, ExceptionConstants.PRODUCT_UNIT_NOT_FOUND);
        }
        if (unitRequest.getProcessingArea() != null) {
            Optional<ProcessingArea> processingAreaOptional = processingAreaRepository.findByComIdAndId(
                comId,
                unitRequest.getProcessingArea()
            );
            processingAreaOptional.ifPresent(processingArea -> productProductUnit.setProcessingAreas(List.of(processingArea)));
        } else {
            productProductUnit.setProcessingAreas(null);
        }
        productProductUnit.setDescription(
            ProductConstant.VALIDATE.generateDescription(
                Strings.isNullOrEmpty(unitName) ? productUnitOptional.get().getName() : unitName.trim(),
                convertRate,
                product.getUnit(),
                unitRequest.getFormula()
            )
        );
        productProductUnit.setUnitName(Strings.isNullOrEmpty(unitName) ? productUnitOptional.get().getName() : unitName.trim());
        BigDecimal inventoryCount = product.getInventoryCount();
        if (product.getInventoryTracking() && inventoryCount != null) {
            if (!unitRequest.getPrimary()) {
                if (!productProductUnit.getFormula()) {
                    inventoryCount = inventoryCount.divide(convertRate, new MathContext(6));
                } else {
                    inventoryCount = inventoryCount.multiply(convertRate);
                }
                productProductUnit.setOnHand(inventoryCount);
            } else {
                productProductUnit.setOnHand(inventoryCount);
            }
        } else {
            productProductUnit.setOnHand(BigDecimal.ZERO);
        }
        // tính giá nhập
        if (product.getPurchasePrice() != null) {
            if (productProductUnit.getFormula()) {
                productProductUnit.setPurchasePrice(product.getPurchasePrice().divide(convertRate, new MathContext(6)));
            } else {
                productProductUnit.setPurchasePrice(product.getPurchasePrice().multiply(convertRate));
            }
        }
        productProductUnit.setUnitNormalizedName(Common.normalizedName(Arrays.asList(productProductUnit.getUnitName())));
        return productProductUnit;
    }

    private Map<Integer, List<ToppingGroupItemResponse>> getListToppingForOffline() {
        User user = userService.getUserWithAuthorities();
        Map<Integer, List<ToppingGroupItemResponse>> responseMap = new HashMap<>();
        List<ToppingGroupItem> groupItems = productToppingRepository.findAllToppingGroup(user.getCompanyId());
        List<ProductToppingItemResponse> itemResponses = productToppingRepository.findToppingGroupForOffline(user.getCompanyId());
        Map<Integer, List<ProductItemResponse>> map = new HashMap<>();
        Map<Integer, Map<Integer, List<ProductItemResponse>>> mapMap = new HashMap<>();
        Map<Integer, List<ProductItemResponse>> noGroupMap = new HashMap<>();
        for (ProductToppingItemResponse item : itemResponses) {
            Map<Integer, List<ProductItemResponse>> childMap = new HashMap<>();
            List<ProductItemResponse> list = new ArrayList<>();
            if (mapMap.containsKey(item.getToppingGroupId())) {
                childMap = mapMap.get(item.getToppingGroupId());
                if (childMap.containsKey(item.getParentProductId())) {
                    list = childMap.get((item.getParentProductId()));
                }
            }
            ProductItemResponse response = new ProductItemResponse();
            BeanUtils.copyProperties(item, response);
            response.setProductCode(item.getCode());
            response.setProductName(item.getName());
            list.add(response);
            childMap.put(item.getParentProductId(), list);

            mapMap.put(item.getToppingGroupId(), childMap);
            if (item.getToppingGroupId() == null) {
                List<ProductItemResponse> noGroupList = new ArrayList<>();
                if (noGroupMap.containsKey(item.getParentProductId())) {
                    noGroupList = noGroupMap.get(item.getParentProductId());
                }
                noGroupList.add(response);
                noGroupMap.put(item.getParentProductId(), noGroupList);
            }
        }
        //        if (map.containsKey(null)) {
        //            ToppingGroupItemResponse result = new ToppingGroupItemResponse();
        //            result.setIsGroupTopping(Boolean.FALSE);
        //            result.setId(0);
        //            result.setRequiredOptional(Boolean.FALSE);
        //            result.setProducts(map.get(null));
        //            results.add(result);
        //        }
        for (ToppingGroupItem item : groupItems) {
            List<ToppingGroupItemResponse> results = new ArrayList<>();
            if (responseMap.containsKey(item.getProductId())) {
                results = responseMap.get(item.getProductId());
            }
            if (noGroupMap.containsKey(item.getProductId())) {
                ToppingGroupItemResponse result = new ToppingGroupItemResponse();
                result.setIsGroupTopping(Boolean.FALSE);
                result.setId(0);
                result.setRequiredOptional(Boolean.FALSE);
                result.setProducts(noGroupMap.get(item.getProductId()));
                results.add(result);
                noGroupMap.remove(item.getProductId());
            }
            ToppingGroupItemResponse result = new ToppingGroupItemResponse();
            BeanUtils.copyProperties(item, result);
            if (mapMap.containsKey(item.getId())) {
                Map<Integer, List<ProductItemResponse>> childMap = mapMap.get(item.getId());
                if (childMap.containsKey(item.getProductId())) {
                    result.setProducts(childMap.get(item.getProductId()));
                }
            }
            result.setIsGroupTopping(Boolean.TRUE);
            results.add(result);
            responseMap.put(item.getProductId(), results);
        }
        return responseMap;
    }

    @Override
    public ResultDTO validateImportExcel(MultipartFile file, Integer comId, Integer indexSheet) {
        Workbook workbook;
        try {
            workbook = WorkbookFactory.create(file.getInputStream());
            workbook.close();
        } catch (Exception e) {
            return new ResultDTO(FAIL, PRODUCT_IMPORT_INVALID_TYPE_ERROR, false);
        }
        //          Đọc danh sách produc từ Excel Sheet 0
        Sheet sheet = workbook.getSheetAt(indexSheet);
        List<ProductExcelResponse> dataResponse = new ArrayList<>();
        int countValid = 0;
        int countInValid = 0;
        int countTotal = 0;
        List<ProductGroup> productGroups = productGroupRepository.findAllByComId(comId);
        Map<String, ProductGroup> groupMap = new HashMap<>();
        productGroups.forEach(group -> groupMap.put(group.getName().toLowerCase(), group));
        List<String> barcodes = productRepository.getAllBarCodeByComId(comId);
        List<String> code2List = productRepository.getAllCode2ByComId(comId);
        for (int i = 1; i <= Common.getMaxRowNumberImportExcel(sheet.getLastRowNum()); i++) {
            Row row = sheet.getRow(i);
            ProductExcelResponse productExcelResponse = new ProductExcelResponse();
            boolean check = false;
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
                    cellString = cellString.replaceAll("\\s{2,}", " ").trim();
                    if (cellString.endsWith(".0")) {
                        cellString = cellString.substring(0, cellString.length() - 2);
                    }
                    switch (j) {
                        case 0:
                            {
                                productExcelResponse.setCode2(cellString);
                                break;
                            }
                        case 1:
                            {
                                productExcelResponse.setName(cellString);
                                break;
                            }
                        case 2:
                            {
                                productExcelResponse.setBarCode(cellString);
                                break;
                            }
                        case 3:
                            {
                                productExcelResponse.setGroupName(cellString);
                                break;
                            }
                        case 4:
                            {
                                BigDecimal inPrice = new BigDecimal(cellString);
                                productExcelResponse.setInPrice(inPrice);
                                break;
                            }
                        case 5:
                            {
                                BigDecimal outPrice = new BigDecimal(cellString);
                                productExcelResponse.setOutPrice(outPrice);
                                break;
                            }
                        case 6:
                            {
                                productExcelResponse.setUnit(cellString);
                                break;
                            }
                        case 7:
                            {
                                boolean inventoryChecking = cellString.equalsIgnoreCase("true") || cellString.equalsIgnoreCase("Có");
                                productExcelResponse.setInventoryTracking(inventoryChecking);
                                break;
                            }
                        case 8:
                            {
                                BigDecimal inventoryCount = BigDecimal.valueOf(Double.parseDouble(cellString));
                                productExcelResponse.setInventoryCount(inventoryCount);
                                break;
                            }
                        case 9:
                            {
                                productExcelResponse.setVatRate(getVatRate(cellString));
                                break;
                            }
                        case 10:
                            {
                                productExcelResponse.setDescription(cellString);
                                break;
                            }
                    }
                }
            }
            if (!check) {
                break;
            }
            Map<Integer, String> messageErrorMap = new HashMap<>();
            if (Strings.isNullOrEmpty(productExcelResponse.getCode2())) {
                messageErrorMap.put(ProductConstant.IMPORT_EXCEL.CODE2, EXCEL_FILE_PRODUCT_CODE2_BLANK);
            }
            if (Strings.isNullOrEmpty(productExcelResponse.getName())) {
                messageErrorMap.put(ProductConstant.IMPORT_EXCEL.NAME, EXCEL_FILE_PRODUCT_NAME_BLANK);
            }
            messageErrorMap.putAll(checkProductImportExcelMessage(productExcelResponse, groupMap, barcodes, code2List));
            if (!Strings.isNullOrEmpty(productExcelResponse.getBarCode())) {
                barcodes.add(productExcelResponse.getBarCode());
            }
            if (!Strings.isNullOrEmpty(productExcelResponse.getCode2())) {
                code2List.add(productExcelResponse.getCode2());
            }
            if (!messageErrorMap.isEmpty()) {
                productExcelResponse.setMessageErrorMap(messageErrorMap);
                countInValid++;
            } else {
                productExcelResponse.setStatus(true);
                countValid++;
            }
            dataResponse.add(productExcelResponse);
            countTotal++;
        }
        if (dataResponse.isEmpty()) {
            return new ResultDTO(FAIL, PRODUCT_IMPORT_EXCEL_ERROR, false);
        }
        return new ResultDTO(
            SUCCESS,
            PRODUCT_IMPORT_VALIDATE_SUCCESS,
            true,
            new ValidateImportResponse(countValid, countInValid, dataResponse),
            countTotal
        );
    }

    private int getVatRate(String cellString) {
        int vatRate = ProductConstant.VatRate.VAT_RATE_OTHER;
        if (cellString.equalsIgnoreCase("0.0") || cellString.equalsIgnoreCase("0%")) {
            vatRate = 0;
        } else if (cellString.equalsIgnoreCase("0.05") || cellString.equalsIgnoreCase("5%")) {
            vatRate = 5;
        } else if (cellString.equalsIgnoreCase("0.08") || cellString.equalsIgnoreCase("8%")) {
            vatRate = 8;
        } else if (cellString.equalsIgnoreCase("0.1") || cellString.equalsIgnoreCase("10%")) {
            vatRate = 10;
        } else if (cellString.equalsIgnoreCase("KCT")) {
            vatRate = ProductConstant.VatRate.VAT_RATE_KCT;
        } else if (cellString.equalsIgnoreCase("KTT")) {
            vatRate = ProductConstant.VatRate.VAT_RATE_KTT;
        }
        return vatRate;
    }

    @Override
    public ResultDTO saveDataImportExcel(ProductExcelRequest request) {
        Integer comId = request.getComId();
        userService.getUserWithAuthorities(comId);
        List<ProductExcelResponse> products = request.getData();
        Map<String, ProductUnit> mapNewUnit = new HashMap<>();
        Map<String, Integer> mapUnit = new HashMap<>();

        List<ProductUnit> productUnits = productUnitRepository.findAllProductUnitByComId(comId);
        productUnits.forEach(item -> mapUnit.put(item.getName().trim().toLowerCase(), item.getId()));
        for (ProductExcelResponse item : products) {
            String unit = item.getUnit();
            if (!Strings.isNullOrEmpty(unit)) {
                if (!mapUnit.containsKey(unit.toLowerCase())) {
                    ProductUnit productUnit = new ProductUnit();
                    productUnit.setComId(comId);
                    productUnit.setName(unit);
                    productUnit.setActive(true);
                    mapNewUnit.put(unit, productUnit);
                } else {
                    item.setUnitId(mapUnit.get(unit.toLowerCase()));
                }
            }
        }

        return importExcelCommon(comId, products, mapNewUnit);
    }

    private Map<Integer, String> checkProductImportExcelMessage(
        ProductExcelResponse item,
        Map<String, ProductGroup> groupMap,
        List<String> barcodes,
        List<String> code2List
    ) {
        Map<Integer, String> messageErrorMap = new HashMap<>();
        if (item.getOutPrice() == null) {
            messageErrorMap.put(ProductConstant.IMPORT_EXCEL.OUT_PRICE, EXCEL_FILE_PRODUCT_SALE_PRICE_NULL);
        }

        if (!Strings.isNullOrEmpty(item.getBarCode()) && !barcodes.isEmpty() && barcodes.contains(item.getBarCode())) {
            messageErrorMap.put(ProductConstant.IMPORT_EXCEL.BARCODE, EXCEL_FILE_PRODUCT_BAR_CODE_DUPLICATE);
        }

        if (!Strings.isNullOrEmpty(item.getCode2()) && !code2List.isEmpty() && code2List.contains(item.getCode2())) {
            messageErrorMap.put(ProductConstant.IMPORT_EXCEL.CODE2, EXCEL_FILE_PRODUCT_CODE2_DUPLICATE);
        }

        if (!Strings.isNullOrEmpty(item.getGroupName())) {
            String groupName = item.getGroupName().toLowerCase();
            if (!groupMap.containsKey(groupName)) {
                messageErrorMap.put(ProductConstant.IMPORT_EXCEL.GROUP, EXCEL_FILE_PRODUCT_GROUP_NOT_EXISTS);
            }
        }

        if (item.getInventoryTracking()) {
            if (item.getInventoryCount() == null) {
                messageErrorMap.put(ProductConstant.IMPORT_EXCEL.INVENTORY_COUNT, PRODUCT_INVENTORY_COUNT_NULL_VI);
            }
            if (item.getInPrice() == null) {
                messageErrorMap.put(ProductConstant.IMPORT_EXCEL.IN_PRICE, PRODUCT_IN_PRICE_NULL_VI);
            }
        }
        return messageErrorMap;
    }

    @Override
    public ResultDTO exportErrorData(List<ProductExcelResponse> request) {
        Workbook workbook = Common.readFileExcelTemplate(ImportExcelConstants.PRODUCT_FILE_URL);

        if (workbook != null) {
            Sheet sheet = workbook.getSheetAt(0);
            int firstRowNum = 0;
            for (ProductExcelResponse prod : request) {
                Row newRow = sheet.createRow(firstRowNum + 1);
                for (int i = 0; i < 11; i++) {
                    Cell cell = newRow.createCell(i);
                    Object value = null;
                    switch (i) {
                        case 0:
                            {
                                value = prod.getCode2();
                                break;
                            }
                        case 1:
                            {
                                value = prod.getName();
                                break;
                            }
                        case 2:
                            {
                                value = prod.getBarCode();
                                break;
                            }
                        case 3:
                            {
                                value = prod.getGroupName();
                                break;
                            }
                        case 4:
                            {
                                value = prod.getInPrice();
                                break;
                            }
                        case 5:
                            {
                                value = prod.getOutPrice();
                                break;
                            }
                        case 6:
                            {
                                value = prod.getUnit();
                                break;
                            }
                        case 7:
                            {
                                if (prod.getInventoryTracking() != null) {
                                    value = prod.getInventoryTracking() ? "Có" : "Không";
                                }
                                break;
                            }
                        case 8:
                            {
                                value = prod.getInventoryCount();
                                break;
                            }
                        case 9:
                            {
                                if (prod.getVatRate() != null) {
                                    String vatRate = prod.getVatRate().toString();
                                    if (vatRate.equals(String.valueOf(ProductConstant.VatRate.VAT_RATE_KCT))) {
                                        value = "KCT";
                                    } else if (vatRate.equals(String.valueOf(ProductConstant.VatRate.VAT_RATE_KTT))) {
                                        value = "KTT";
                                    } else if (vatRate.equals(String.valueOf(ProductConstant.VatRate.VAT_RATE_OTHER))) {
                                        value = "Khác";
                                    } else if (vatRate.equals("0") || vatRate.equals("5") || vatRate.equals("8") || vatRate.equals("10")) {
                                        value = vatRate + "%";
                                    } else {
                                        value = vatRate;
                                    }
                                }
                                break;
                            }
                        case 10:
                            {
                                value = prod.getDescription();
                                break;
                            }
                    }
                    if (value != null) {
                        cell.setCellValue(value.toString());
                    }
                    if (prod.getMessageErrorMap().containsKey(i)) {
                        cell.setCellStyle(Common.highLightErrorCell(workbook));
                        cell.setCellComment(Common.setCommentErrorCell(workbook, sheet, cell, prod.getMessageErrorMap().get(i)));
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
    public ResultDTO getByProductProductUnitIds(GetByIdsRequest request) {
        Integer comId = request.getComId();
        List<Integer> requestIds = request.getIds();
        userService.getUserWithAuthorities(comId);
        List<ProductItemResponse> detailForVouchers = productRepository.getDetailsByIdsOrGroupIds(comId, requestIds, null);
        if (detailForVouchers.isEmpty() || detailForVouchers.size() != requestIds.size()) {
            List<Integer> detailIds = detailForVouchers
                .stream()
                .map(ProductItemResponse::getProductProductUnitId)
                .collect(Collectors.toList());
            requestIds.forEach(id -> {
                if (!detailIds.contains(id)) {
                    throw new BadRequestAlertException(PRODUCT_IN_VALID_VI, ENTITY_NAME, PRODUCT_IN_VALID);
                }
            });
        }
        return new ResultDTO(SUCCESS, SUCCESS_GET_LIST, true, detailForVouchers);
    }
}
