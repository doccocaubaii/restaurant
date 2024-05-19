package vn.hust.easypos.controller;

import java.math.BigDecimal;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Validator;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.hust.easypos.service.dto.ResultDTO;
import vn.hust.easypos.service.dto.product.SaveProductRequest;
import vn.hust.easypos.service.impl.ProductService;
import vn.hust.easypos.service.util.Common;
import vn.hust.easypos.service.util.Util;
import vn.hust.easypos.web.rest.errors.ExceptionConstants;

@RestController
@RequestMapping("/api")
public class ProductController {

    private final Logger log = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    private final Validator customValidator;

    public ProductController(ProductService productService, Validator customValidator) {
        this.productService = productService;
        this.customValidator = customValidator;
    }

    @GetMapping("/client/page/product/product-get-with-paging")
    public ResponseEntity<ResultDTO> getWithPagingForProduct(Pageable pageable, @RequestParam(required = false) String keyword) {
        ResultDTO result = productService.getWithPagingForProduct(pageable, keyword);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/client/page/product/create")
    public ResponseEntity<ResultDTO> createProductFromData(
        @RequestParam(required = false) MultipartFile images,
        @NotNull(message = ExceptionConstants.COM_ID_MUST_NOT_NULL) Integer comId,
        @RequestParam(required = false) @NotBlank(message = ExceptionConstants.PRODUCT_NAME_MUST_NOT_EMPTY) String name,
        @RequestParam(required = false) String unit,
        @RequestParam(required = false) BigDecimal purchasePrice,
        @RequestParam(required = false) @NotNull(message = ExceptionConstants.PRODUCT_OUT_PRICE_MUST_NOT_EMPTY) @Min(
            value = 1,
            message = ExceptionConstants.PRODUCT_OUT_PRICE_INVALID
        ) BigDecimal salePrice,
        @RequestParam(required = false) String description,
        HttpServletRequest request
    ) {
        SaveProductRequest saveProductRequest = new SaveProductRequest(comId, name, unit, purchasePrice, salePrice, description);
        Common.validateInput(customValidator, "SaveProductRequest", saveProductRequest);
        ResultDTO resultDTO = productService.save(images, saveProductRequest, request);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PutMapping("/client/page/product/update")
    public ResponseEntity<ResultDTO> updateProductFormData(
        @RequestParam(required = false) MultipartFile images,
        @RequestParam(required = false) @NotNull(message = ExceptionConstants.PRODUCT_ID_NOT_NULL) Integer id,
        @RequestParam(required = false) @NotNull(message = ExceptionConstants.COM_ID_MUST_NOT_NULL) Integer comId,
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String unit,
        @RequestParam(required = false) BigDecimal purchasePrice,
        @RequestParam(required = false) @Min(value = 1, message = ExceptionConstants.PRODUCT_OUT_PRICE_INVALID) BigDecimal salePrice,
        @RequestParam(required = false) String description,
        HttpServletRequest request
    ) {
        log.debug("REST request to update Product by id : {}", id);
        SaveProductRequest saveProductRequest = new SaveProductRequest(id, comId, name, unit, purchasePrice, salePrice, description);
        Common.validateInput(customValidator, "UpdateProductRequest", saveProductRequest);
        ResultDTO resultDTO = productService.update(images, saveProductRequest, request);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @GetMapping("/client/page/product/by-id/{id}")
    public ResponseEntity<ResultDTO> getProductDetailWithProductGroups(@PathVariable(value = "id") Integer id) {
        log.debug("REST request to get Product details with id: {}", id);
        return new ResponseEntity<>(productService.getProductDetail(id), HttpStatus.OK);
    }

    @PutMapping("/client/page/product/delete/{id}")
    public ResponseEntity<ResultDTO> deleteProduct(@PathVariable Integer id) {
        log.debug("REST request to delete Product : {}", id);
        ResultDTO resultDTO = productService.delete(id);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:9000")
    @GetMapping("/client/file/product/{comId}/{path}")
    public ResponseEntity<byte[]> getWithOwnerId(
        @NotNull @PathVariable("comId") String comId,
        @NotNull @PathVariable("path") String path,
        @RequestParam(required = false) String locationPath
    ) {
        byte[] image = Util.getImageData(comId, path, locationPath);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        ByteArrayResource resource = new ByteArrayResource(image);

        return ResponseEntity.ok().headers(headers).contentLength(image.length).body(resource.getByteArray());
    }
}
