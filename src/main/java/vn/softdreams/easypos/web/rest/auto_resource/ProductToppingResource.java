package vn.softdreams.easypos.web.rest.auto_resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import vn.softdreams.easypos.domain.ProductTopping;
import vn.softdreams.easypos.repository.ProductToppingRepository;
import vn.softdreams.easypos.service.auto_service.ProductToppingService;
import vn.softdreams.easypos.web.rest.errors.BadRequestAlertException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link vn.softdreams.easypos.domain.ProductTopping}.
 */
@RestController
@RequestMapping("/api")
public class ProductToppingResource {

    private final Logger log = LoggerFactory.getLogger(ProductToppingResource.class);

    private static final String ENTITY_NAME = "productTopping";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductToppingService productToppingService;

    private final ProductToppingRepository productToppingRepository;

    public ProductToppingResource(ProductToppingService productToppingService, ProductToppingRepository productToppingRepository) {
        this.productToppingService = productToppingService;
        this.productToppingRepository = productToppingRepository;
    }

    /**
     * {@code POST  /product-toppings} : Create a new productTopping.
     *
     * @param productTopping the productTopping to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productTopping, or with status {@code 400 (Bad Request)} if the productTopping has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/product-toppings")
    public ResponseEntity<ProductTopping> createProductTopping(@RequestBody ProductTopping productTopping) throws URISyntaxException {
        log.debug("REST request to save ProductTopping : {}", productTopping);
        if (productTopping.getId() != null) {
            throw new BadRequestAlertException("A new productTopping cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductTopping result = productToppingService.save(productTopping);
        return ResponseEntity
            .created(new URI("/api/product-toppings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /product-toppings/:id} : Updates an existing productTopping.
     *
     * @param id the id of the productTopping to save.
     * @param productTopping the productTopping to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productTopping,
     * or with status {@code 400 (Bad Request)} if the productTopping is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productTopping couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/product-toppings/{id}")
    public ResponseEntity<ProductTopping> updateProductTopping(
        @PathVariable(value = "id", required = false) final Integer id,
        @RequestBody ProductTopping productTopping
    ) throws URISyntaxException {
        log.debug("REST request to update ProductTopping : {}, {}", id, productTopping);
        if (productTopping.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productTopping.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productToppingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProductTopping result = productToppingService.update(productTopping);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productTopping.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /product-toppings/:id} : Partial updates given fields of an existing productTopping, field will ignore if it is null
     *
     * @param id the id of the productTopping to save.
     * @param productTopping the productTopping to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productTopping,
     * or with status {@code 400 (Bad Request)} if the productTopping is not valid,
     * or with status {@code 404 (Not Found)} if the productTopping is not found,
     * or with status {@code 500 (Internal Server Error)} if the productTopping couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/product-toppings/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductTopping> partialUpdateProductTopping(
        @PathVariable(value = "id", required = false) final Integer id,
        @RequestBody ProductTopping productTopping
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProductTopping partially : {}, {}", id, productTopping);
        if (productTopping.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productTopping.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productToppingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductTopping> result = productToppingService.partialUpdate(productTopping);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productTopping.getId().toString())
        );
    }

    /**
     * {@code GET  /product-toppings} : get all the productToppings.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productToppings in body.
     */
    @GetMapping("/product-toppings")
    public ResponseEntity<List<ProductTopping>> getAllProductToppings(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of ProductToppings");
        Page<ProductTopping> page = productToppingService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-toppings/:id} : get the "id" productTopping.
     *
     * @param id the id of the productTopping to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productTopping, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/product-toppings/{id}")
    public ResponseEntity<ProductTopping> getProductTopping(@PathVariable Integer id) {
        log.debug("REST request to get ProductTopping : {}", id);
        Optional<ProductTopping> productTopping = productToppingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productTopping);
    }

    /**
     * {@code DELETE  /product-toppings/:id} : delete the "id" productTopping.
     *
     * @param id the id of the productTopping to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/product-toppings/{id}")
    public ResponseEntity<Void> deleteProductTopping(@PathVariable Integer id) {
        log.debug("REST request to delete ProductTopping : {}", id);
        productToppingService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
