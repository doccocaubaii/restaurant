package vn.softdreams.easypos.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.softdreams.easypos.dto.product.DeleteProductList;
import vn.softdreams.easypos.dto.productGroup.*;
import vn.softdreams.easypos.dto.productProductUnit.GetByIdsRequest;
import vn.softdreams.easypos.repository.ProductGroupRepository;
import vn.softdreams.easypos.service.CategoryManagementService;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.util.Common;

import javax.validation.Validator;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryManagementResource {

    private final Logger log = LoggerFactory.getLogger(CategoryManagementResource.class);

    private final CategoryManagementService categoryManagementService;

    private static final String ENTITY_NAME = "Product_Group";
    private final Validator customValidator;
    private final ProductGroupRepository productGroupRepository;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public CategoryManagementResource(
        CategoryManagementService categoryManagementService,
        ProductGroupRepository productGroupRepository,
        Validator customValidator
    ) {
        this.categoryManagementService = categoryManagementService;
        this.productGroupRepository = productGroupRepository;
        this.customValidator = customValidator;
    }

    @GetMapping("/client/page/product-group/by-id/{id}")
    public ResponseEntity<ResultDTO> getProductGroup(@PathVariable Integer id) {
        log.debug("REST request to get ProductGroup : {}", id);
        ResultDTO result = categoryManagementService.getProductGroupDetail(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/client/page/product-group/create")
    public ResponseEntity<ResultDTO> createProductGroup(@RequestBody SaveProductGroupRequest request) throws URISyntaxException {
        Common.validateInput(customValidator, ENTITY_NAME, request);
        log.debug("REST request to save ProductGroup : {}", request);
        ResultDTO resultDTO = categoryManagementService.save(request);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PutMapping("/client/page/product-group/update")
    public ResponseEntity<ResultDTO> updateProductGroup(@RequestBody UpdateProductGroupRequest updateProductGroupRequest) {
        log.debug("REST request to update productGroup : {}", updateProductGroupRequest);
        Common.validateInput(customValidator, ENTITY_NAME, updateProductGroupRequest);
        ResultDTO resultDTO = categoryManagementService.update(updateProductGroupRequest);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PostMapping("/client/page/product-group/delete")
    public ResponseEntity<ResultDTO> deleteProductGroup(@RequestBody ProductGroupDeleteRequest request) {
        log.debug("REST request to delete ProductGroup : {}", request.getId());
        ResultDTO resultDTO = categoryManagementService.delete(request);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/client/page/product-group/get-with-paging")
    public ResponseEntity<ResultDTO> getSearchProductGroup(
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) Boolean isCountAll,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of search result for productGroup");
        ResultDTO result = categoryManagementService.searchProductGroup(keyword, pageable, isCountAll);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/client/page/product-group/get-all-for-offline")
    public ResponseEntity<ResultDTO> getAllProductGroupsForOffline() {
        log.debug("REST request to return a product groups of a product");
        ResultDTO resultDTO = categoryManagementService.getAllProductGroupsForOffline();
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/client/page/product-group/by-product/{id}")
    public ResponseEntity<ResultDTO> getProductGroupsByProductId(@PathVariable Integer id) {
        log.debug("REST request to return a product groups of a product");
        ResultDTO result = categoryManagementService.getAllByProductId(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/client/page/product-group/import-excel")
    public ResponseEntity<ResultDTO> importExcel(@RequestParam("file") MultipartFile file, @RequestParam Integer comId) {
        ResultDTO resultDTO = categoryManagementService.importExel(file, comId);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PostMapping("/client/page/product-group/import-excel/validate")
    public ResponseEntity<ResultDTO> validateImportExcel(
        @RequestParam("file") MultipartFile file,
        @RequestParam Integer comId,
        @RequestParam Integer indexSheet
    ) {
        ResultDTO resultDTO = categoryManagementService.validateImportExcel(file, comId, indexSheet);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PostMapping("/client/page/product-group/import-excel/save-data")
    public ResponseEntity<ResultDTO> saveDataImportExcel(@RequestBody ProductGroupExcelRequest request) {
        ResultDTO resultDTO = categoryManagementService.saveDataImportExcel(request);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PostMapping("/client/page/product-group/export-excel/error-data")
    public ResponseEntity<byte[]> exportErrorData(@RequestBody List<ProductGroupValidateResponse> request) {
        ResultDTO resultDTO = categoryManagementService.exportErrorData(request);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        // Set the content disposition header to specify the file name
        headers.setContentDispositionFormData("attachment", "filename.extension");

        return new ResponseEntity<>((byte[]) resultDTO.getData(), headers, HttpStatus.OK);
    }

    @PostMapping("/client/page/product-group/delete-list")
    public ResponseEntity<ResultDTO> deleteListProductGroup(@RequestBody DeleteProductList request) {
        ResultDTO resultDTO = categoryManagementService.deleteList(request);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PostMapping("/client/page/product-group/get-by-ids")
    public ResponseEntity<ResultDTO> getByIds(@RequestBody GetByIdsRequest request) {
        Common.validateInput(customValidator, ENTITY_NAME, request);
        return new ResponseEntity<>(categoryManagementService.getByIds(request), HttpStatus.OK);
    }
}
