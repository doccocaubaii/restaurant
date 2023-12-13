package vn.softdreams.easypos.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import vn.softdreams.easypos.domain.ProductUnit;
import vn.softdreams.easypos.dto.productUnit.SaveConversionUnitRequest;
import vn.softdreams.easypos.repository.ProductUnitRepository;
import vn.softdreams.easypos.service.ProductProductUnitService;
import vn.softdreams.easypos.service.ProductUnitService;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.util.Common;
import vn.softdreams.easypos.web.rest.errors.BadRequestAlertException;

import javax.validation.Validator;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link vn.softdreams.easypos.domain.ProductUnit}.
 */
@RestController
@RequestMapping("/api")
public class ProductUnitResource {

    private final Logger log = LoggerFactory.getLogger(ProductUnitResource.class);

    private static final String ENTITY_NAME = "productUnit";
    private final Validator customValidator;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductUnitService productUnitService;
    private final ProductProductUnitService productProductUnitService;

    private final ProductUnitRepository productUnitRepository;

    public ProductUnitResource(
        Validator customValidator,
        ProductUnitService productUnitService,
        ProductProductUnitService productProductUnitService,
        ProductUnitRepository productUnitRepository
    ) {
        this.customValidator = customValidator;
        this.productUnitService = productUnitService;
        this.productProductUnitService = productProductUnitService;
        this.productUnitRepository = productUnitRepository;
    }

    @PostMapping("/product-units")
    public ResponseEntity<ProductUnit> createProductUnit(@RequestBody ProductUnit productUnit) throws URISyntaxException {
        log.debug("REST request to save ProductUnit : {}", productUnit);
        if (productUnit.getId() != null) {
            throw new BadRequestAlertException("A new productUnit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductUnit result = productUnitService.save(productUnit);
        return ResponseEntity
            .created(new URI("/api/product-units/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/product-units/{id}")
    public ResponseEntity<ProductUnit> updateProductUnit(
        @PathVariable(value = "id", required = false) final Integer id,
        @RequestBody ProductUnit productUnit
    ) throws URISyntaxException {
        log.debug("REST request to update ProductUnit : {}, {}", id, productUnit);
        if (productUnit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productUnit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productUnitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProductUnit result = productUnitService.update(productUnit);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productUnit.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/product-units/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductUnit> partialUpdateProductUnit(
        @PathVariable(value = "id", required = false) final Integer id,
        @RequestBody ProductUnit productUnit
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProductUnit partially : {}, {}", id, productUnit);
        if (productUnit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productUnit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productUnitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductUnit> result = productUnitService.partialUpdate(productUnit);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productUnit.getId().toString())
        );
    }

    @GetMapping("/product-units")
    public ResponseEntity<List<ProductUnit>> getAllProductUnits(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of ProductUnits");
        Page<ProductUnit> page = productUnitService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/product-units/{id}")
    public ResponseEntity<ProductUnit> getProductUnit(@PathVariable Integer id) {
        log.debug("REST request to get ProductUnit : {}", id);
        Optional<ProductUnit> productUnit = productUnitService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productUnit);
    }

    @DeleteMapping("/product-units/{id}")
    public ResponseEntity<Void> deleteProductUnit(@PathVariable Integer id) {
        log.debug("REST request to delete ProductUnit : {}", id);
        productUnitService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/page/product-product-unit/create")
    public ResponseEntity<ResultDTO> createProductConversionUnit(@RequestBody SaveConversionUnitRequest request) {
        Common.validateInput(customValidator, "CreateConversionUnitRequest", request);
        ResultDTO resultDTO = productProductUnitService.saveConversionUnit(request);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PostMapping("/page/product-product-unit/update")
    public ResponseEntity<ResultDTO> updateProductConversionUnit(@RequestBody SaveConversionUnitRequest request) {
        Common.validateInput(customValidator, "UpdateConversionUnitRequest", request);
        ResultDTO resultDTO = productProductUnitService.updateConversionUnit(request);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @GetMapping("/page/product-product-unit/by-product/{productId}")
    public ResponseEntity<ResultDTO> getProductConversionUnitByProductId(@PathVariable Integer productId) {
        ResultDTO resultDTO = productProductUnitService.getByProductId(productId);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PostMapping("/page/product-product-unit/delete/{id}")
    public ResponseEntity<ResultDTO> deleteProductConversionUnit(@PathVariable Integer id) {
        ResultDTO resultDTO = productProductUnitService.deleteConversionUnit(id);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PutMapping("/p/client/page/product-unit/update-prod")
    public ResponseEntity<ResultDTO> updateProductUnit(@RequestBody List<Integer> comIds) {
        ResultDTO resultDTO = productUnitService.updateUnitNameProd(comIds);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }
}
