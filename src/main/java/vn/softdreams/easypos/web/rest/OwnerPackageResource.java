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
import vn.softdreams.easypos.domain.OwnerPackage;
import vn.softdreams.easypos.repository.OwnerPackageRepository;
import vn.softdreams.easypos.service.OwnerPackageService;
import vn.softdreams.easypos.web.rest.errors.BadRequestAlertException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link vn.softdreams.easypos.domain.OwnerPackage}.
 */
@RestController
@RequestMapping("/api")
public class OwnerPackageResource {

    private final Logger log = LoggerFactory.getLogger(OwnerPackageResource.class);

    private static final String ENTITY_NAME = "ownerPackage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OwnerPackageService ownerPackageService;

    private final OwnerPackageRepository ownerPackageRepository;

    public OwnerPackageResource(OwnerPackageService ownerPackageService, OwnerPackageRepository ownerPackageRepository) {
        this.ownerPackageService = ownerPackageService;
        this.ownerPackageRepository = ownerPackageRepository;
    }

    /**
     * {@code POST  /owner-packages} : Create a new ownerPackage.
     *
     * @param ownerPackage the ownerPackage to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ownerPackage, or with status {@code 400 (Bad Request)} if the ownerPackage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/owner-packages")
    public ResponseEntity<OwnerPackage> createOwnerPackage(@RequestBody OwnerPackage ownerPackage) throws URISyntaxException {
        log.debug("REST request to save OwnerPackage : {}", ownerPackage);
        if (ownerPackage.getId() != null) {
            throw new BadRequestAlertException("A new ownerPackage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OwnerPackage result = ownerPackageService.save(ownerPackage);
        return ResponseEntity
            .created(new URI("/api/owner-packages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /owner-packages/:id} : Updates an existing ownerPackage.
     *
     * @param id the id of the ownerPackage to save.
     * @param ownerPackage the ownerPackage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ownerPackage,
     * or with status {@code 400 (Bad Request)} if the ownerPackage is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ownerPackage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/owner-packages/{id}")
    public ResponseEntity<OwnerPackage> updateOwnerPackage(
        @PathVariable(value = "id", required = false) final Integer id,
        @RequestBody OwnerPackage ownerPackage
    ) throws URISyntaxException {
        log.debug("REST request to update OwnerPackage : {}, {}", id, ownerPackage);
        if (ownerPackage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ownerPackage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ownerPackageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OwnerPackage result = ownerPackageService.update(ownerPackage);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ownerPackage.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /owner-packages/:id} : Partial updates given fields of an existing ownerPackage, field will ignore if it is null
     *
     * @param id the id of the ownerPackage to save.
     * @param ownerPackage the ownerPackage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ownerPackage,
     * or with status {@code 400 (Bad Request)} if the ownerPackage is not valid,
     * or with status {@code 404 (Not Found)} if the ownerPackage is not found,
     * or with status {@code 500 (Internal Server Error)} if the ownerPackage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/owner-packages/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OwnerPackage> partialUpdateOwnerPackage(
        @PathVariable(value = "id", required = false) final Integer id,
        @RequestBody OwnerPackage ownerPackage
    ) throws URISyntaxException {
        log.debug("REST request to partial update OwnerPackage partially : {}, {}", id, ownerPackage);
        if (ownerPackage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ownerPackage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ownerPackageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OwnerPackage> result = ownerPackageService.partialUpdate(ownerPackage);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ownerPackage.getId().toString())
        );
    }

    /**
     * {@code GET  /owner-packages} : get all the ownerPackages.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ownerPackages in body.
     */
    @GetMapping("/owner-packages")
    public ResponseEntity<List<OwnerPackage>> getAllOwnerPackages(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of OwnerPackages");
        Page<OwnerPackage> page = ownerPackageService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /owner-packages/:id} : get the "id" ownerPackage.
     *
     * @param id the id of the ownerPackage to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ownerPackage, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/owner-packages/{id}")
    public ResponseEntity<OwnerPackage> getOwnerPackage(@PathVariable Integer id) {
        log.debug("REST request to get OwnerPackage : {}", id);
        Optional<OwnerPackage> ownerPackage = ownerPackageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ownerPackage);
    }

    /**
     * {@code DELETE  /owner-packages/:id} : delete the "id" ownerPackage.
     *
     * @param id the id of the ownerPackage to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/owner-packages/{id}")
    public ResponseEntity<Void> deleteOwnerPackage(@PathVariable Integer id) {
        log.debug("REST request to delete OwnerPackage : {}", id);
        ownerPackageService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
