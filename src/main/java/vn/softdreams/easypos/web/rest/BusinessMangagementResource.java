package vn.softdreams.easypos.web.rest;

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
import vn.softdreams.easypos.domain.Business;
import vn.softdreams.easypos.repository.BusinessRepository;
import vn.softdreams.easypos.service.BusinessManagementService;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.web.rest.errors.BadRequestAlertException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link vn.softdreams.easypos.domain.Business}.
 */
@RestController
@RequestMapping("/api")
public class BusinessMangagementResource {

    private final Logger log = LoggerFactory.getLogger(BusinessMangagementResource.class);

    private static final String ENTITY_NAME = "business";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BusinessManagementService businessManagementService;

    private final BusinessRepository businessRepository;

    public BusinessMangagementResource(BusinessManagementService businessManagementService, BusinessRepository businessRepository) {
        this.businessManagementService = businessManagementService;
        this.businessRepository = businessRepository;
    }

    @GetMapping("/client/page/business/get-with-paging")
    public ResponseEntity<ResultDTO> getAllBusinesses(Pageable pageable, @RequestParam(required = false) String keyword) {
        log.debug("REST request to get a page of Businesses");
        ResultDTO response = businessManagementService.getWithPaging(pageable, keyword);
        return ResponseEntity.ok().body(response);
    }

    /**
     * {@code POST  /businesses} : Create a new business.
     *
     * @param business the business to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new business, or with status {@code 400 (Bad Request)} if the business has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/businesses")
    public ResponseEntity<Business> createBusiness(@RequestBody Business business) throws URISyntaxException {
        log.debug("REST request to save Business : {}", business);
        if (business.getId() != null) {
            throw new BadRequestAlertException("A new business cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Business result = businessManagementService.save(business);
        return ResponseEntity
            .created(new URI("/api/businesses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /businesses/:id} : Updates an existing business.
     *
     * @param id       the id of the business to save.
     * @param business the business to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated business,
     * or with status {@code 400 (Bad Request)} if the business is not valid,
     * or with status {@code 500 (Internal Server Error)} if the business couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/businesses/{id}")
    public ResponseEntity<Business> updateBusiness(
        @PathVariable(value = "id", required = false) final Integer id,
        @RequestBody Business business
    ) throws URISyntaxException {
        log.debug("REST request to update Business : {}, {}", id, business);
        if (business.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, business.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!businessRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Business result = businessManagementService.update(business);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, business.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /businesses/:id} : Partial updates given fields of an existing business, field will ignore if it is null
     *
     * @param id       the id of the business to save.
     * @param business the business to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated business,
     * or with status {@code 400 (Bad Request)} if the business is not valid,
     * or with status {@code 404 (Not Found)} if the business is not found,
     * or with status {@code 500 (Internal Server Error)} if the business couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/businesses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Business> partialUpdateBusiness(
        @PathVariable(value = "id", required = false) final Integer id,
        @RequestBody Business business
    ) throws URISyntaxException {
        log.debug("REST request to partial update Business partially : {}, {}", id, business);
        if (business.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, business.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!businessRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Business> result = businessManagementService.partialUpdate(business);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, business.getId().toString())
        );
    }

    /**
     * {@code GET  /businesses} : get all the businesses.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of businesses in body.
     */
    @GetMapping("/businesses")
    public ResponseEntity<List<Business>> getAllBusinesses(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Businesses");
        Page<Business> page = businessManagementService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /businesses/:id} : get the "id" business.
     *
     * @param id the id of the business to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the business, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/businesses/{id}")
    public ResponseEntity<Business> getBusiness(@PathVariable Integer id) {
        log.debug("REST request to get Business : {}", id);
        Optional<Business> business = businessManagementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(business);
    }

    /**
     * {@code DELETE  /businesses/:id} : delete the "id" business.
     *
     * @param id the id of the business to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/businesses/{id}")
    public ResponseEntity<Void> deleteBusiness(@PathVariable Integer id) {
        log.debug("REST request to delete Business : {}", id);
        businessManagementService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
