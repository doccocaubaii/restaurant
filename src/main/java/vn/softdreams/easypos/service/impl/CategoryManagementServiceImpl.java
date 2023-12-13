package vn.softdreams.easypos.service.impl;

import com.google.common.base.Strings;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import org.apache.poi.ss.usermodel.*;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vn.softdreams.easypos.constants.ImportExcelConstants;
import vn.softdreams.easypos.constants.ProductGroupConstants;
import vn.softdreams.easypos.constants.ResultConstants;
import vn.softdreams.easypos.domain.ProductGroup;
import vn.softdreams.easypos.domain.User;
import vn.softdreams.easypos.dto.importExcel.ValidateImportResponse;
import vn.softdreams.easypos.dto.product.DeleteProductList;
import vn.softdreams.easypos.dto.product.ProductItemResponse;
import vn.softdreams.easypos.dto.productGroup.*;
import vn.softdreams.easypos.dto.productProductUnit.GetByIdsRequest;
import vn.softdreams.easypos.repository.ProductGroupRepository;
import vn.softdreams.easypos.repository.ProductRepository;
import vn.softdreams.easypos.repository.UserRepository;
import vn.softdreams.easypos.service.CategoryManagementService;
import vn.softdreams.easypos.service.UserService;
import vn.softdreams.easypos.service.dto.DataResponse;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.util.Common;
import vn.softdreams.easypos.web.rest.errors.BadRequestAlertException;
import vn.softdreams.easypos.web.rest.errors.InternalServerException;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static vn.softdreams.easypos.constants.ResultConstants.*;
import static vn.softdreams.easypos.web.rest.errors.ExceptionConstants.*;

@Service
@Transactional
public class CategoryManagementServiceImpl implements CategoryManagementService {

    private final Logger log = LoggerFactory.getLogger(CategoryManagementServiceImpl.class);

    private final ProductGroupRepository productGroupRepository;
    private final ProductRepository productRepository;

    private final UserService userService;
    private final UserRepository userRepository;
    private static final String ENTITY_NAME = "productGroup";
    private final ModelMapper mapper = new ModelMapper();

    public CategoryManagementServiceImpl(
        ProductGroupRepository productGroupRepository,
        ProductRepository productRepository,
        UserService userService,
        UserRepository userRepository
    ) {
        this.productGroupRepository = productGroupRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    public ResultDTO save(SaveProductGroupRequest request) {
        User user = userService.getUserWithAuthorities(request.getComId());
        String name = request.getName();
        ProductGroup productGroup = new ProductGroup();
        Integer count = productGroupRepository.countAllByComIdAndName(user.getCompanyId(), name);
        if (count != null && count > 0) {
            throw new BadRequestAlertException(
                PRODUCT_GROUP_DUPLICATE_NAME_VI,
                PRODUCT_GROUP_DUPLICATE_NAME_VI,
                PRODUCT_GROUP_DUPLICATE_NAME
            );
        }
        productGroup.setName(name);
        productGroup.setDescription(request.getDescription());
        //Tu dong get CompanyId theo UserId
        productGroup.setComId(user.getCompanyId());
        productGroup.setNormalizedName(Common.normalizedName(Arrays.asList(name)));
        productGroupRepository.save(productGroup);
        ProductGroupOnlineResponse response = new ProductGroupOnlineResponse();
        BeanUtils.copyProperties(productGroup, response);
        response.setComId(user.getCompanyId());
        return new ResultDTO(SUCCESS, CREATE_PRODUCT_GROUP_SUCCESS_VI, true, response);
    }

    @Override
    public ResultDTO update(UpdateProductGroupRequest updateProductGroupRequest) {
        log.debug("Request to update Product Group : {}", updateProductGroupRequest);
        String name = updateProductGroupRequest.getName();
        User user = userService.getUserWithAuthorities(updateProductGroupRequest.getComId());
        //Kiem tra nguoi dung da dang nhap chua
        //Kiem tra user cung comId voi productGroup khong
        Optional<ProductGroup> productGroupOptional = productGroupRepository.getProductGroupByIdAndComId(
            updateProductGroupRequest.getId(),
            user.getCompanyId()
        );
        if (productGroupOptional.isEmpty()) {
            throw new InternalServerException(INVALID_PRODUCT_GROUP_VI, INVALID_PRODUCT_GROUP_VI, INVALID_PRODUCT_GROUP);
        }
        ProductGroup productGroup = productGroupOptional.get();
        if (!Strings.isNullOrEmpty(name)) {
            Integer count = productGroupRepository.countAllByComIdAndIDAndName(
                updateProductGroupRequest.getId(),
                user.getCompanyId(),
                name
            );
            if (count != null && count > 0) {
                throw new BadRequestAlertException(
                    PRODUCT_GROUP_DUPLICATE_NAME_VI,
                    PRODUCT_GROUP_DUPLICATE_NAME_VI,
                    PRODUCT_GROUP_DUPLICATE_NAME
                );
            }
            productGroup.setName(updateProductGroupRequest.getName());
        }
        productGroup.setDescription(updateProductGroupRequest.getDescription());
        productGroup.setNormalizedName(Common.normalizedName(Arrays.asList(name)));
        productGroupRepository.save(productGroup);
        return new ResultDTO(SUCCESS, UPDATE_PRODUCT_GROUP_SUCCESS_VI, true);
    }

    @Override
    public ResultDTO delete(ProductGroupDeleteRequest request) {
        log.debug("Request to delete Product Group : {}", request.getId());
        ResultDTO resultDTO = new ResultDTO();
        User user = userService.getUserWithAuthorities(request.getComId());
        Optional<ProductGroup> productGroupOptional = productGroupRepository.getProductGroupByIdAndComId(
            request.getId(),
            user.getCompanyId()
        );
        // Kiem tra comId productGroup voi user co giong nhau khong
        if (productGroupOptional.isEmpty()) {
            throw new InternalServerException(INVALID_PRODUCT_GROUP_VI, INVALID_PRODUCT_GROUP_VI, INVALID_PRODUCT_GROUP);
        }
        productGroupRepository.deleteByIdFromProductProductGroup(request.getId());
        productGroupRepository.deleteByIdFromProductGroup(request.getId());
        log.error(ENTITY_NAME + "_update: " + DELETE_PRODUCT_GROUP_SUCCESS_VI);
        return new ResultDTO(SUCCESS, DELETE_PRODUCT_GROUP_SUCCESS_VI, true);
    }

    @Override
    public ResultDTO deleteList(DeleteProductList request) {
        User user = userService.getUserWithAuthorities(request.getComId());
        Integer comId = user.getCompanyId();
        List<ProductGroupOnlineResponse> productGroups = productGroupRepository
            .searchProductGroupByKeywordForPage(null, request.getKeyword(), comId, false, request.getParamCheckAll(), request.getIds())
            .getContent();
        List<Object> listError = new ArrayList<>();

        List<Integer> ids = productGroups.stream().map(ProductGroupOnlineResponse::getId).collect(Collectors.toList());
        productGroupRepository.deleteByIdsFromProductProductGroup(ids);
        productGroupRepository.deleteByIdsFromProductGroup(ids);

        DataResponse response = new DataResponse();
        response.setCountAll(productGroups.size());
        response.setCountFalse(0);
        response.setDataFalse(listError);
        response.setCountSuccess(response.getCountAll() - response.getCountFalse());
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.DELETE_LIST_PRODUCT_UNIT_SUCCESS, true, response);
    }

    @Transactional(readOnly = true)
    public ResultDTO searchProductGroup(String keyword, Pageable pageable, Boolean isCountAll) {
        log.debug("Request to get productGroup by keyword");
        User user = userService.getUserWithAuthorities();
        Integer company_id = user.getCompanyId();
        //ProductGroupTotalResponse thuc chat chi la ket qua query + total (gia tri de su dung PageImpl)
        Page<ProductGroupOnlineResponse> productGroups = productGroupRepository.searchProductGroupByKeywordForPage(
            pageable,
            keyword,
            company_id,
            isCountAll,
            false,
            null
        );
        log.debug("productGroup_getAllWithPaging: " + GET_PRODUCT_GROUPS_SUCCESS_VI);
        return new ResultDTO(
            SUCCESS,
            GET_PRODUCT_GROUPS_SUCCESS_VI,
            true,
            productGroups.getContent(),
            (int) productGroups.getTotalElements()
        );
    }

    @Transactional(readOnly = true)
    public ResultDTO getAllProductGroupsForOffline() {
        log.debug("Request to get productGroup by product with keyword");
        User user = userService.getUserWithAuthorities();
        List<ProductGroupOfflineResponse> productGroups = productGroupRepository.getAllProductGroupsForOffline(user.getCompanyId());
        return new ResultDTO(SUCCESS, GET_PRODUCT_GROUPS_SUCCESS_VI, true, productGroups);
    }

    @Override
    public ResultDTO importExel(MultipartFile file, Integer comId) {
        User user = userService.getUserWithAuthorities();
        if (!user.getManager()) {
            if (!user.getCompanyId().equals(comId)) {
                throw new InternalServerException(COMPANY_NOT_EXISTS_CODE_VI, ENTITY_NAME, COMPANY_NOT_EXISTS_CODE);
            }
        }
        List<String> productsGroupName = productGroupRepository.findAllNameByComId(comId);

        Workbook workbook;
        try {
            workbook = WorkbookFactory.create(file.getInputStream());
        } catch (Exception e) {
            throw new InternalServerException(EXCEL_FILE_ERROR_VI, ENTITY_NAME, EXCEL_FILE_ERROR);
        }
        List<ProductGroup> request = new ArrayList<>();
        Sheet sheet = workbook.getSheetAt(0);
        int countItem = 0;
        for (int i = 1; i <= Common.getMaxRowNumberImportExcel(sheet.getLastRowNum()); i++) {
            Row row = sheet.getRow(i);
            CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
            ProductGroup productGroup = new ProductGroup();
            for (int j = 0; j < 3; j++) {
                Cell cell;
                try {
                    cell = row.getCell(j);
                } catch (Exception ex) {
                    break;
                }
                if (cell != null && !String.valueOf(cell).isBlank()) {
                    String cellString = String.valueOf(cell).trim();
                    if (cellString.endsWith(".0")) {
                        cellString = cellString.substring(0, cellString.length() - 2);
                    }
                    switch (j) {
                        case 0:
                            {
                                if (productsGroupName.contains(cellString.toLowerCase())) {
                                    throw new BadRequestAlertException(
                                        PRODUCT_GROUP_DUPLICATE_NAME_VI,
                                        ENTITY_NAME,
                                        PRODUCT_GROUP_DUPLICATE_NAME
                                    );
                                }
                                productGroup.setName(cellString.replaceAll("\\s{2,}", " ").trim());
                                productGroup.setNormalizedName(Common.normalizedName(List.of(cellString)));
                                productsGroupName.add(cellString);
                                break;
                            }
                        case 1:
                            {
                                productGroup.setDescription(cellString);
                                break;
                            }
                    }
                }
            }
            if (!Strings.isNullOrEmpty(productGroup.getName())) {
                productGroup.setComId(comId);
                request.add(productGroup);
                cellStyle.setFillBackgroundColor(IndexedColors.GREY_50_PERCENT.index);
                row.setRowStyle(cellStyle);
            } else {
                countItem++;
            }
            if (countItem == 2) {
                break;
            }
        }
        if (!request.isEmpty()) {
            productGroupRepository.saveAll(request);
            return new ResultDTO(SUCCESS, CREATE_PRODUCT_GROUP_SUCCESS_VI, true);
        }
        return new ResultDTO(FAIL, CREATE_PRODUCT_GROUP_FAIL_VI, false);
    }

    @Transactional(readOnly = true)
    public ResultDTO getProductGroupDetail(Integer id) {
        log.debug("Request to get Product Group detail by id");
        ResultDTO resultDTO = new ResultDTO();
        User user = userService.getUserWithAuthorities();
        Optional<ProductGroup> productGroup = productGroupRepository.findByIdAndComId(id, user.getCompanyId());
        //Kiem tra neu danh muc ton tai
        if (productGroup.isEmpty()) {
            throw new InternalServerException(PRODUCT_GROUP_NOT_FOUND_VI, PRODUCT_GROUP_NOT_FOUND_VI, PRODUCT_GROUP_NOT_FOUND);
        }

        ProductGroupOfflineResponse productGroupResponse = mapper.map(productGroup.get(), ProductGroupOfflineResponse.class);
        log.debug("productGroup_getDetail: " + GET_PRODUCT_GROUP_DETAIL_SUCCESS_VI);
        return new ResultDTO(SUCCESS, GET_PRODUCT_GROUP_DETAIL_SUCCESS_VI, true, productGroupResponse);
    }

    @Transactional(readOnly = true)
    public ResultDTO getAllByProductId(Integer productId) {
        ResultDTO resultDTO = new ResultDTO();
        log.debug("Request to get Product Groups by productId: {}", productId);
        User user = userService.getUserWithAuthorities();
        Integer countProduct = productRepository.countAllByIdAndComId(productId, user.getCompanyId());
        //Kiem tra neu san pham ton tai
        if (countProduct != null && countProduct != 1) {
            throw new InternalServerException(PRODUCT_IN_VALID_VI, PRODUCT_IN_VALID_VI, PRODUCT_IN_VALID);
        }
        List<ProductGroup> productGroups;
        productGroups = productGroupRepository.findAllByProductId(productId, user.getCompanyId());

        resultDTO.setData(productGroups);
        resultDTO.setStatus(true);
        resultDTO.setMessage(SUCCESS);
        resultDTO.setReason(GET_PRODUCT_GROUPS_SUCCESS_VI);
        return resultDTO;
    }

    @Override
    public ResultDTO validateImportExcel(MultipartFile file, Integer comId, Integer indexSheet) {
        userService.getUserWithAuthorities(comId);
        List<String> productsGroupName = productGroupRepository.findAllNameByComId(comId);

        Workbook workbook;
        try {
            workbook = WorkbookFactory.create(file.getInputStream());
        } catch (Exception e) {
            throw new InternalServerException(EXCEL_FILE_ERROR_VI, ENTITY_NAME, EXCEL_FILE_ERROR);
        }
        List<ProductGroupValidateResponse> dataResponse = new ArrayList<>();
        Sheet sheet = workbook.getSheetAt(indexSheet);
        int countValid = 0;
        int countInValid = 0;
        int countTotal = 0;
        for (int i = 1; i <= Common.getMaxRowNumberImportExcel(sheet.getLastRowNum()); i++) {
            Row row = sheet.getRow(i);
            ProductGroupValidateResponse validateResponse = new ProductGroupValidateResponse();
            boolean check = false;
            Map<Integer, String> message = new HashMap<>();
            for (int j = 0; j < 3; j++) {
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
                    cellString.replaceAll("\\s{2,}", " ").trim();
                    switch (j) {
                        case 0:
                            {
                                if (productsGroupName.contains(cellString.toLowerCase())) {
                                    message.put(ProductGroupConstants.IMPORT_EXCEL.NAME, PRODUCT_GROUP_DUPLICATE_NAME_VI);
                                }
                                validateResponse.setName(cellString);
                                productsGroupName.add(cellString.toLowerCase());
                                break;
                            }
                        case 1:
                            {
                                validateResponse.setDescription(cellString);
                                break;
                            }
                    }
                }
            }
            if (!check) {
                break;
            }
            if (Strings.isNullOrEmpty(validateResponse.getName())) {
                message.put(ProductGroupConstants.IMPORT_EXCEL.NAME, PRODUCT_GROUP_NOT_BLANK_VI);
            }
            if (!message.isEmpty()) {
                validateResponse.setMessageErrorMap(message);
                countInValid++;
            } else {
                validateResponse.setStatus(true);
                countValid++;
            }
            dataResponse.add(validateResponse);
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

    @Override
    public ResultDTO saveDataImportExcel(ProductGroupExcelRequest request) {
        Integer comId = request.getComId();
        userService.getUserWithAuthorities(comId);
        List<ProductGroupExcelRequest.ProductGroupExcelDetail> dataResponse = request.getData();
        List<String> groupNameResult = productGroupRepository.findAllNameByComId(comId);
        List<ProductGroup> saveProductGroups = new ArrayList<>();
        for (ProductGroupExcelRequest.ProductGroupExcelDetail item : dataResponse) {
            if (!groupNameResult.contains(item.getName().toLowerCase())) {
                String name = item.getName().replaceAll("\\s{2,}", " ").trim();
                groupNameResult.add(name.toLowerCase());
                ProductGroup productGroup = new ProductGroup();
                productGroup.setComId(comId);
                productGroup.setName(name);
                productGroup.setNormalizedName(Common.normalizedName(List.of(name)));
                productGroup.setDescription(item.getDescription());
                saveProductGroups.add(productGroup);
            } else {
                throw new BadRequestAlertException(PRODUCT_GROUP_DUPLICATE_NAME_VI, ENTITY_NAME, PRODUCT_GROUP_DUPLICATE_NAME);
            }
        }
        productGroupRepository.saveAll(saveProductGroups);
        return new ResultDTO(SUCCESS, SUCCESS_CREATE, true, dataResponse.size());
    }

    @Override
    public ResultDTO exportErrorData(List<ProductGroupValidateResponse> request) {
        Workbook workbook = Common.readFileExcelTemplate(ImportExcelConstants.PRODUCT_GROUP_FILE_URL);

        if (workbook != null) {
            Sheet sheet = workbook.getSheetAt(0);
            int firstRowNum = 0;
            for (ProductGroupValidateResponse prod : request) {
                Row newRow = sheet.createRow(firstRowNum + 1);
                for (int i = 0; i < 11; i++) {
                    Cell cell = newRow.createCell(i);
                    Object value = null;
                    switch (i) {
                        case 0:
                            {
                                value = prod.getName();
                                break;
                            }
                        case 2:
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
    public ResultDTO getByIds(GetByIdsRequest request) {
        Integer comId = request.getComId();
        List<Integer> requestIds = request.getIds();
        userService.getUserWithAuthorities(comId);
        List<ProductGroupItem> result = productGroupRepository.getAllByIds(comId, requestIds);
        List<ProductItemResponse> productItemResponses = productRepository.getDetailsByIdsOrGroupIds(comId, null, requestIds);
        MultiValuedMap<Integer, ProductItemResponse> prodMap = new HashSetValuedHashMap<>();
        productItemResponses.forEach(item -> prodMap.put(item.getGroupId(), item));
        List<ProductGroupForVoucherResponse> response = new ArrayList<>();

        for (ProductGroupItem item : result) {
            ProductGroupForVoucherResponse itemRes = new ProductGroupForVoucherResponse();
            itemRes.setId(item.getId());
            itemRes.setName(item.getName());
            itemRes.setProducts(prodMap.get(item.getId()));
            response.add(itemRes);
        }
        if (result.isEmpty() || result.size() != requestIds.size()) {
            requestIds.forEach(id -> {
                if (!prodMap.containsKey(id)) {
                    throw new BadRequestAlertException(INVALID_PRODUCT_GROUP_VI, ENTITY_NAME, INVALID_PRODUCT_GROUP);
                }
            });
        }
        return new ResultDTO(SUCCESS, SUCCESS_GET_LIST, true, response);
    }
}
