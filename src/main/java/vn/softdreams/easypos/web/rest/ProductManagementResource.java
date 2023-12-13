package vn.softdreams.easypos.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.softdreams.easypos.dto.backup.ImportUrlRequest;
import vn.softdreams.easypos.dto.product.*;
import vn.softdreams.easypos.dto.productProductUnit.GetByIdsRequest;
import vn.softdreams.easypos.dto.productUnit.ConversionUnitRequest;
import vn.softdreams.easypos.dto.queue.ReSendTaskLog;
import vn.softdreams.easypos.dto.toppingGroup.ToppingRequest;
import vn.softdreams.easypos.integration.easybooks88.api.EB88ApiClient;
import vn.softdreams.easypos.integration.easybooks88.queue.EB88Producer;
import vn.softdreams.easypos.repository.ProductRepository;
import vn.softdreams.easypos.service.ProductManagementService;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.util.Common;
import vn.softdreams.easypos.web.rest.errors.BadRequestAlertException;
import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Validator;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductManagementResource {

    private final Logger log = LoggerFactory.getLogger(ProductManagementResource.class);

    private final ProductManagementService productManagementService;
    private final ProductRepository productRepository;
    private final Validator customValidator;
    private final EB88ApiClient eb88ApiClient;
    private final ObjectMapper objectMapper;
    private final EB88Producer eb88Producer;

    private static final String ENTITY_NAME = "Product";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public ProductManagementResource(
        ProductManagementService productManagementService,
        ProductRepository productRepository,
        Validator customValidator,
        ObjectMapper objectMapper,
        EB88ApiClient eb88ApiClient,
        EB88Producer eb88Producer
    ) {
        this.productManagementService = productManagementService;
        this.productRepository = productRepository;
        this.customValidator = customValidator;
        this.eb88ApiClient = eb88ApiClient;
        this.objectMapper = objectMapper;
        this.eb88Producer = eb88Producer;
    }

    @PostMapping("/client/page/product/create1")
    public ResponseEntity<ResultDTO> createProductFromData(
        @RequestParam(required = false) MultipartFile images,
        @NotNull(message = ExceptionConstants.COM_ID_MUST_NOT_NULL) Integer comId,
        @Size(max = 100, message = ExceptionConstants.PRODUCT_CODE_2_LENGTH_INVALID) @RequestParam(required = false) String code2,
        @RequestParam(required = false) @NotBlank(message = ExceptionConstants.PRODUCT_NAME_MUST_NOT_EMPTY) String name,
        @RequestParam(required = false) String unit,
        @RequestParam(required = false) Integer unitId,
        @RequestParam(required = false) @Size(max = 50, message = ExceptionConstants.PRODUCT_BARCODE_LENGTH_INVALID) String barcode,
        @RequestParam(required = false) BigDecimal purchasePrice,
        @RequestParam(required = false) @NotNull(message = ExceptionConstants.PRODUCT_OUT_PRICE_MUST_NOT_EMPTY) @Min(
            value = 1,
            message = ExceptionConstants.PRODUCT_OUT_PRICE_INVALID
        ) BigDecimal salePrice,
        @RequestParam(required = false) @Max(value = 100, message = ExceptionConstants.PRODUCT_VAT_RATE_INVALID) Integer vatRate,
        @RequestParam(required = false) @NotNull(
            message = ExceptionConstants.PRODUCT_IS_INVENTORY_MUST_NOT_EMPTY
        ) Boolean inventoryTracking,
        @RequestParam(required = false) BigDecimal inventoryCount,
        @RequestParam(required = false) String description,
        @RequestParam(required = false) List<Integer> groups,
        @RequestParam(required = false) String conversionUnits,
        @RequestParam(required = false) Integer discountVatRate,
        @RequestParam(required = false) Boolean isTopping,
        @RequestParam(required = false) String toppings,
        @RequestParam(required = false) Integer processingArea,
        HttpServletRequest request
    ) {
        List<ConversionUnitRequest> conversionUnitList = new ArrayList<>();
        if (!Strings.isNullOrEmpty(conversionUnits)) {
            try {
                conversionUnitList = objectMapper.readValue(conversionUnits, new TypeReference<List<ConversionUnitRequest>>() {});
            } catch (JsonProcessingException e) {
                throw new BadRequestAlertException(
                    ExceptionConstants.UNIT_IS_NOT_VALID_VI,
                    ENTITY_NAME,
                    ExceptionConstants.UNIT_IS_NOT_VALID
                );
            }
        }
        List<ToppingRequest> toppingRequestList = new ArrayList<>();
        if (!Strings.isNullOrEmpty(toppings)) {
            try {
                toppingRequestList = objectMapper.readValue(toppings, new TypeReference<List<ToppingRequest>>() {});
            } catch (JsonProcessingException e) {
                throw new BadRequestAlertException(
                    ExceptionConstants.TOPPING_LIST_INVALID_VI,
                    ENTITY_NAME,
                    ExceptionConstants.TOPPING_LIST_INVALID_CODE
                );
            }
        }
        SaveProductRequest saveProductRequest = new SaveProductRequest(
            comId,
            code2,
            name,
            unit,
            unitId,
            barcode,
            purchasePrice,
            salePrice,
            vatRate,
            inventoryTracking,
            inventoryCount,
            description,
            discountVatRate,
            isTopping,
            groups,
            conversionUnitList,
            toppingRequestList,
            processingArea
        );
        Common.validateInput(customValidator, "SaveProductRequest", saveProductRequest);
        ResultDTO resultDTO = productManagementService.save(images, saveProductRequest, request, false);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PostMapping("/client/page/product/create2")
    public ResponseEntity<ResultDTO> createProduct2(
        @RequestParam(required = false) MultipartFile images,
        @RequestParam(required = false) String saveProductRequest
    ) throws URISyntaxException, JsonProcessingException {
        SaveProductRequest saveProductRequestConvert = new SaveProductRequest();
        try {
            saveProductRequestConvert = objectMapper.readValue(saveProductRequest, SaveProductRequest.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Common.validateInput(customValidator, "SaveProductRequest", saveProductRequestConvert);
        //        SaveProductRequest saveProductRequest = new SaveProductRequest(comId, code2, name, unit, unitId, barcode, inPrice, outPrice, vatRate, inventoryTracking, inventoryCount, description, groups);
        Common.validateInput(customValidator, "SaveProductRequest", saveProductRequest);
        ResultDTO resultDTO = productManagementService.save(images, saveProductRequestConvert, null, false);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PutMapping("/client/page/product/update2")
    public ResponseEntity<ResultDTO> updateProduct2(
        @RequestParam(required = false) MultipartFile images,
        @RequestParam(required = false) String updateProdRequest
    ) throws URISyntaxException, JsonProcessingException {
        UpdateProdRequest updateProdRequestConvert = new UpdateProdRequest();
        try {
            updateProdRequestConvert = objectMapper.readValue(updateProdRequest, UpdateProdRequest.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Common.validateInput(customValidator, "UpdateProductRequest", updateProdRequestConvert);
        //        SaveProductRequest saveProductRequest = new SaveProductRequest(comId, code2, name, unit, unitId, barcode, inPrice, outPrice, vatRate, inventoryTracking, inventoryCount, description, groups);
        Common.validateInput(customValidator, "SaveProductRequest", updateProdRequest);
        ResultDTO resultDTO = productManagementService.update(images, updateProdRequestConvert, null, false);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PostMapping("/client/page/product/create")
    public ResponseEntity<ResultDTO> createProduct(@RequestBody SaveProductRequest saveProductRequest)
        throws URISyntaxException, JsonProcessingException {
        //        SaveProductRequest saveProductRequest = new SaveProductRequest(comId, code2, name, unit, unitId, barcode, inPrice, outPrice, vatRate, inventoryTracking, inventoryCount, description, groups);
        Common.validateInput(customValidator, "SaveProductRequest", saveProductRequest);
        ResultDTO resultDTO = productManagementService.save(null, saveProductRequest, null, false);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PutMapping("/client/page/product/update")
    public ResponseEntity<ResultDTO> updateProduct(@RequestBody UpdateProdRequest updateProdRequest) {
        log.debug("REST request to update Product by id : {}", updateProdRequest.getId());
        Common.validateInput(customValidator, "UpdateProductRequest", updateProdRequest);
        ResultDTO resultDTO = productManagementService.update(null, updateProdRequest, null, false);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    //    @PostMapping("/client/page/product/import-excel")
    //    public ResponseEntity<ResultDTO> convertExcelToJson(@RequestParam("file") MultipartFile file, @RequestParam Integer comId) {
    //        ResultDTO resultDTO = productManagementService.importProductByExcel(file, comId);
    //        if (resultDTO.getData() != null) {
    //            List<TaskLog> taskLogs = (List<TaskLog>) resultDTO.getData();
    //            for (TaskLog item : taskLogs) {
    //                if (item.getId() != null) {
    //                    eb88Producer.send(new TaskLogIdEnqueueMessage(item.getId()));
    //                }
    //            }
    //        }
    //        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    //    }
    @PostMapping("/client/page/product/import-excel")
    public ResponseEntity<ResultDTO> convertExcelToJson1(@RequestParam("file") MultipartFile file, @RequestParam Integer comId) {
        ResultDTO result = productManagementService.importProductByExcel1(file, comId);
        if (result.isStatus()) {
            ImportProductAsyncRequest resultData = (ImportProductAsyncRequest) result.getData();
            productManagementService.syncDataAfterImport(resultData);
            result.setData(resultData.getCount());
        } else {
            result.setData(null);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/client/page/product/import-excel/validate")
    public ResponseEntity<ResultDTO> validateImportExcel(
        @RequestParam("file") MultipartFile file,
        @RequestParam Integer comId,
        @RequestParam Integer indexSheet
    ) {
        ResultDTO resultDTO = productManagementService.validateImportExcel(file, comId, indexSheet);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PostMapping("/client/page/product/import-excel/save-data")
    public ResponseEntity<ResultDTO> saveDataImportExcel(@RequestBody ProductExcelRequest request) {
        ResultDTO result = productManagementService.saveDataImportExcel(request);
        if (result.isStatus()) {
            ImportProductAsyncRequest resultData = (ImportProductAsyncRequest) result.getData();
            productManagementService.syncDataAfterImport(resultData);
            result.setData(resultData.getCount());
        } else {
            result.setData(null);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/client/page/product/export-excel/error-data")
    public ResponseEntity<byte[]> exportErrorData(@RequestBody List<ProductExcelResponse> request) {
        ResultDTO resultDTO = productManagementService.exportErrorData(request);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        // Set the content disposition header to specify the file name
        headers.setContentDispositionFormData("attachment", "filename.extension");

        return new ResponseEntity<>((byte[]) resultDTO.getData(), headers, HttpStatus.OK);
    }

    @PostMapping("/client/page/re-send-queue")
    //    @CheckAuthorize(value = AuthoritiesConstants.SYSTEM_ADMIN)
    public ResponseEntity<ResultDTO> reSendQueue(@RequestBody(required = false) ReSendTaskLog reSendTaskLog) {
        ResultDTO resultDTO = productManagementService.reSendQueue(reSendTaskLog.getIds());
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PutMapping("/client/page/product-unit/async")
    public ResponseEntity<ResultDTO> updateProductAsync(@RequestBody List<Integer> companyIds) {
        ResultDTO resultDTO = productManagementService.updateProductAsync(companyIds, eb88ApiClient);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PutMapping("/client/page/product/update1")
    public ResponseEntity<ResultDTO> updateProductFormData(
        @RequestParam(required = false) MultipartFile images,
        @RequestParam(required = false) @NotNull(message = ExceptionConstants.PRODUCT_ID_NOT_NULL) Integer id,
        @RequestParam(required = false) @NotNull(message = ExceptionConstants.COM_ID_MUST_NOT_NULL) Integer comId,
        @RequestParam(required = false) @Size(max = 100, message = ExceptionConstants.PRODUCT_CODE_2_LENGTH_INVALID) String code2,
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String unit,
        @RequestParam(required = false) Integer unitId,
        @RequestParam(required = false) @Size(max = 50, message = ExceptionConstants.PRODUCT_BARCODE_LENGTH_INVALID) String barcode,
        @RequestParam(required = false) BigDecimal purchasePrice,
        @RequestParam(required = false) @Min(value = 1, message = ExceptionConstants.PRODUCT_OUT_PRICE_INVALID) BigDecimal salePrice,
        @RequestParam(required = false) @Max(value = 100, message = ExceptionConstants.PRODUCT_VAT_RATE_INVALID) Integer vatRate,
        @RequestParam(required = false) Boolean inventoryTracking,
        @RequestParam(required = false) BigDecimal inventoryCount,
        @RequestParam(required = false) String description,
        @RequestParam(required = false) Integer discountVatRate,
        @RequestParam(required = false) List<Integer> groups,
        @RequestParam(required = false) String conversionUnits,
        @RequestParam(required = false) String toppings,
        @RequestParam(required = false) Boolean isTopping,
        @RequestParam(required = false) Integer processingArea,
        HttpServletRequest request
    ) {
        log.debug("REST request to update Product by id : {}", id);
        List<ConversionUnitRequest> conversionUnitList = new ArrayList<>();
        if (!Strings.isNullOrEmpty(conversionUnits)) {
            try {
                conversionUnitList = objectMapper.readValue(conversionUnits, new TypeReference<List<ConversionUnitRequest>>() {});
            } catch (JsonProcessingException e) {
                throw new BadRequestAlertException(
                    ExceptionConstants.UNIT_IS_NOT_VALID_VI,
                    ENTITY_NAME,
                    ExceptionConstants.UNIT_IS_NOT_VALID
                );
            }
        }
        List<ToppingRequest> toppingRequestList = new ArrayList<>();
        if (!Strings.isNullOrEmpty(toppings)) {
            try {
                toppingRequestList = objectMapper.readValue(toppings, new TypeReference<List<ToppingRequest>>() {});
            } catch (JsonProcessingException e) {
                throw new BadRequestAlertException(
                    ExceptionConstants.TOPPING_LIST_INVALID_VI,
                    ENTITY_NAME,
                    ExceptionConstants.TOPPING_LIST_INVALID_CODE
                );
            }
        }
        UpdateProdRequest updateProdRequest = new UpdateProdRequest(
            id,
            comId,
            code2,
            name,
            unit,
            unitId,
            barcode,
            purchasePrice,
            salePrice,
            vatRate,
            inventoryTracking,
            inventoryCount,
            description,
            discountVatRate,
            isTopping,
            groups,
            conversionUnitList,
            toppingRequestList,
            processingArea
        );
        Common.validateInput(customValidator, "UpdateProductRequest", updateProdRequest);
        ResultDTO resultDTO = productManagementService.update(images, updateProdRequest, request, false);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PostMapping("/client/common/import-url")
    public ResponseEntity<ResultDTO> importUrl(@RequestBody List<ImportUrlRequest> importUrlRequests) {
        ResultDTO resultDTO = productManagementService.importUrl(importUrlRequests);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PostMapping("/client/common/import-url/by-tax-code")
    public ResponseEntity<ResultDTO> importUrlByTaxCode(@RequestBody List<ImportUrlRequest> importUrlRequests) {
        ResultDTO resultDTO = productManagementService.importUrlByTaxCode(importUrlRequests);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @GetMapping("/client/page/product/by-id/{id}")
    public ResponseEntity<ResultDTO> getProductDetailWithProductGroups(@PathVariable(value = "id") Integer id) {
        log.debug("REST request to get Product details with id: {}", id);
        return new ResponseEntity<>(productManagementService.getProductDetail(id), HttpStatus.OK);
    }

    @GetMapping("/client/page/product/get-all-for-offline")
    public ResponseEntity<ResultDTO> getAllForOffline() {
        return new ResponseEntity<>(productManagementService.getAllForOffline(), HttpStatus.OK);
    }

    @GetMapping("/client/page/product/get-with-paging")
    public ResponseEntity<ResultDTO> getWithPagingForBill(
        Pageable pageable,
        @RequestParam(required = false) Integer groupId,
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) Boolean isTopping,
        @RequestParam(required = false) Boolean isCountAll
    ) {
        ResultDTO result = productManagementService.getWithPaging(pageable, keyword, groupId, isTopping, isCountAll);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/client/page/product/get-with-paging2")
    public ResponseEntity<ResultDTO> getWithPagingForBill2(@RequestBody GetProductPagingRequest request) {
        ResultDTO result = productManagementService.getWithPaging2(
            PageRequest.of(request.getPage(), request.getSize()),
            request.getKeyword(),
            request.getGroupIds()
        );
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/client/page/product/category-get-with-offline")
    public ResponseEntity<ResultDTO> getAllForOfflineForProduct() {
        return new ResponseEntity<>(productManagementService.getAllForOfflineForProduct(), HttpStatus.OK);
    }

    @GetMapping("/client/page/product/category-get-with-paging")
    public ResponseEntity<ResultDTO> getWithPagingForProduct(
        Pageable pageable,
        @RequestParam(required = false) Integer groupId,
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) Boolean isTopping,
        @RequestParam(required = false) List<Integer> ids,
        @RequestParam(required = false) Boolean isCountAll
    ) {
        ResultDTO result = productManagementService.getWithPagingForProduct(pageable, keyword, groupId, isTopping, isCountAll, ids);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/client/page/product/delete/{id}")
    public ResponseEntity<ResultDTO> deleteProduct(@PathVariable Integer id) {
        log.debug("REST request to delete Product : {}", id);
        ResultDTO resultDTO = productManagementService.delete(id);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PutMapping("/client/page/product/delete-list")
    public ResponseEntity<ResultDTO> deleteProductList(@RequestBody DeleteProductList req) {
        log.debug("REST request to delete list Product : {}", req);
        ResultDTO resultDTO = productManagementService.deleteList(req);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @GetMapping("/client/page/product/get-all-product-unit")
    public ResponseEntity<ResultDTO> getAllProductUnits(@RequestParam(required = false) String keyword) {
        return new ResponseEntity<>(productManagementService.getAllProductUnit(keyword), HttpStatus.OK);
    }

    @GetMapping("/client/page/product/find-by-barcode/{barcode}")
    public ResponseEntity<ResultDTO> findByBarcode(@PathVariable String barcode) {
        return new ResponseEntity<>(productManagementService.findByBarcode(barcode), HttpStatus.OK);
    }

    @GetMapping("/client/page/product/bill/find-by-barcode/{barcode}")
    public ResponseEntity<ResultDTO> findByBarcodeForBill(@PathVariable String barcode) {
        return new ResponseEntity<>(productManagementService.findByBarcodeForBill(barcode), HttpStatus.OK);
    }

    @PostMapping("/client/page/product/create-product-unit")
    public ResponseEntity<ResultDTO> createNewProductUnit(@RequestBody CreateProductUnitRequest request) {
        ResultDTO result = productManagementService.createNewProductUnit(request);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/client/page/product/delete-product-conversion-unit")
    public ResponseEntity<ResultDTO> deleteProductConversionUnit(@RequestBody DeleteConversionUnitRequest request) {
        ResultDTO resultDTO = productManagementService.deleteProductUnit(request, Boolean.FALSE);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PutMapping("/client/page/product/enable-inventory-tracking")
    public ResponseEntity<ResultDTO> enableInventoryTracking(@RequestBody ProductStockUpdateRequest request) {
        Common.validateInput(customValidator, ENTITY_NAME, request);
        ResultDTO resultDTO = productManagementService.enableInventoryTracking(request);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PostMapping("/admin/page/product/create")
    public ResponseEntity<ResultDTO> createProductAdmin(
        @RequestParam(required = false) MultipartFile images,
        @RequestParam(required = false) String saveProductRequest
    ) throws URISyntaxException, JsonProcessingException {
        SaveProductRequest saveProductRequestConvert = new SaveProductRequest();
        try {
            saveProductRequestConvert = objectMapper.readValue(saveProductRequest, SaveProductRequest.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Common.validateInput(customValidator, "SaveProductRequest", saveProductRequestConvert);
        Common.validateInput(customValidator, "SaveProductRequest", saveProductRequest);
        ResultDTO resultDTO = productManagementService.save(images, saveProductRequestConvert, null, true);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PutMapping("/admin/page/product/update")
    public ResponseEntity<ResultDTO> updateProductFormDataForAdmin(
        @RequestParam(required = false) MultipartFile images,
        @RequestParam(required = false) String updateProdRequest
    ) throws URISyntaxException, JsonProcessingException {
        UpdateProdRequest updateProdRequestConvert = new UpdateProdRequest();
        try {
            updateProdRequestConvert = objectMapper.readValue(updateProdRequest, UpdateProdRequest.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Common.validateInput(customValidator, "UpdateProductRequest", updateProdRequestConvert);
        //        SaveProductRequest saveProductRequest = new SaveProductRequest(comId, code2, name, unit, unitId, barcode, inPrice, outPrice, vatRate, inventoryTracking, inventoryCount, description, groups);
        Common.validateInput(customValidator, "SaveProductRequest", updateProdRequest);
        ResultDTO resultDTO = productManagementService.update(images, updateProdRequestConvert, null, true);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PutMapping("/admin/page/product/delete-product-conversion-unit")
    public ResponseEntity<ResultDTO> deleteProductConversionUnitAdmin(@RequestBody DeleteConversionUnitRequest request) {
        ResultDTO resultDTO = productManagementService.deleteProductUnit(request, Boolean.TRUE);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @GetMapping("/p/client/page/product/insert-default")
    public ResponseEntity<ResultDTO> insertDefault() {
        ResultDTO resultDTO = productManagementService.insertDefaultProduct();
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @GetMapping("/client/page/product-unit/get-all")
    public ResponseEntity<ResultDTO> getAllProductUnit(Pageable pageable, @RequestParam(required = false) String keyword) {
        return new ResponseEntity<>(productManagementService.searchProductUnit(pageable, keyword), HttpStatus.OK);
    }

    @GetMapping("/client/page/product-unit/get-by-id/{id}")
    public ResponseEntity<ResultDTO> getAllProductUnit(@PathVariable Integer id) {
        return new ResponseEntity<>(productManagementService.getDetailProductUnit(id), HttpStatus.OK);
    }

    @PostMapping("/client/page/product-unit/update")
    public ResponseEntity<ResultDTO> updateProductUnit(@RequestBody UpdateProductUnitRequest request) {
        return new ResponseEntity<>(productManagementService.updateProductUnit(request), HttpStatus.OK);
    }

    @PutMapping("/client/page/product-unit/delete/{id}")
    public ResponseEntity<ResultDTO> deleteProductUnit(@PathVariable Integer id) {
        return new ResponseEntity<>(productManagementService.deleteProductUnit(id), HttpStatus.OK);
    }

    @PutMapping("/client/page/product-unit/delete-list")
    public ResponseEntity<ResultDTO> deleteListProductUnit(@RequestBody DeleteProductList req) {
        return new ResponseEntity<>(productManagementService.deleteListProductUnit(req), HttpStatus.OK);
    }

    @GetMapping("/client/page/product/check-bar-code")
    public ResponseEntity<ResultDTO> getAllBarCode(@RequestParam String barcode) {
        return new ResponseEntity<>(productManagementService.checkBarCode(barcode), HttpStatus.OK);
    }

    @PostMapping("/p/client/page/product/alter-image")
    public ResponseEntity<ResultDTO> alterProductImage(@RequestBody List<String> images) {
        return new ResponseEntity<>(productManagementService.alterImage(images), HttpStatus.OK);
    }

    @PostMapping("/client/page/product-unit/get-by-ids")
    public ResponseEntity<ResultDTO> getByProductProductUnitIds(@RequestBody GetByIdsRequest request) {
        Common.validateInput(customValidator, ENTITY_NAME, request);
        return new ResponseEntity<>(productManagementService.getByProductProductUnitIds(request), HttpStatus.OK);
    }
}
