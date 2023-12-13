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
import vn.softdreams.easypos.domain.ToppingToppingGroup;
import vn.softdreams.easypos.repository.ToppingToppingGroupRepository;
import vn.softdreams.easypos.service.auto_service.ToppingToppingGroupService;
import vn.softdreams.easypos.web.rest.errors.BadRequestAlertException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link vn.softdreams.easypos.domain.ToppingToppingGroup}.
 */
@RestController
@RequestMapping("/api")
public class ToppingToppingGroupResource {

    private final Logger log = LoggerFactory.getLogger(ToppingToppingGroupResource.class);

    private static final String ENTITY_NAME = "toppingToppingGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ToppingToppingGroupService toppingToppingGroupService;

    private final ToppingToppingGroupRepository toppingToppingGroupRepository;

    public ToppingToppingGroupResource(
        ToppingToppingGroupService toppingToppingGroupService,
        ToppingToppingGroupRepository toppingToppingGroupRepository
    ) {
        this.toppingToppingGroupService = toppingToppingGroupService;
        this.toppingToppingGroupRepository = toppingToppingGroupRepository;
    }

    /**
     * {@code POST  /topping-topping-groups} : Create a new toppingToppingGroup.
     *
     * @param toppingToppingGroup the toppingToppingGroup to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new toppingToppingGroup, or with status {@code 400 (Bad Request)} if the toppingToppingGroup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/topping-topping-groups")
    public ResponseEntity<ToppingToppingGroup> createToppingToppingGroup(@RequestBody ToppingToppingGroup toppingToppingGroup)
        throws URISyntaxException {
        log.debug("REST request to save ToppingToppingGroup : {}", toppingToppingGroup);
        if (toppingToppingGroup.getId() != null) {
            throw new BadRequestAlertException("A new toppingToppingGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ToppingToppingGroup result = toppingToppingGroupService.save(toppingToppingGroup);
        return ResponseEntity
            .created(new URI("/api/topping-topping-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /topping-topping-groups/:id} : Updates an existing toppingToppingGroup.
     *
     * @param id the id of the toppingToppingGroup to save.
     * @param toppingToppingGroup the toppingToppingGroup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated toppingToppingGroup,
     * or with status {@code 400 (Bad Request)} if the toppingToppingGroup is not valid,
     * or with status {@code 500 (Internal Server Error)} if the toppingToppingGroup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/topping-topping-groups/{id}")
    public ResponseEntity<ToppingToppingGroup> updateToppingToppingGroup(
        @PathVariable(value = "id", required = false) final Integer id,
        @RequestBody ToppingToppingGroup toppingToppingGroup
    ) throws URISyntaxException {
        log.debug("REST request to update ToppingToppingGroup : {}, {}", id, toppingToppingGroup);
        if (toppingToppingGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, toppingToppingGroup.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!toppingToppingGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ToppingToppingGroup result = toppingToppingGroupService.update(toppingToppingGroup);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, toppingToppingGroup.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /topping-topping-groups/:id} : Partial updates given fields of an existing toppingToppingGroup, field will ignore if it is null
     *
     * @param id the id of the toppingToppingGroup to save.
     * @param toppingToppingGroup the toppingToppingGroup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated toppingToppingGroup,
     * or with status {@code 400 (Bad Request)} if the toppingToppingGroup is not valid,
     * or with status {@code 404 (Not Found)} if the toppingToppingGroup is not found,
     * or with status {@code 500 (Internal Server Error)} if the toppingToppingGroup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/topping-topping-groups/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ToppingToppingGroup> partialUpdateToppingToppingGroup(
        @PathVariable(value = "id", required = false) final Integer id,
        @RequestBody ToppingToppingGroup toppingToppingGroup
    ) throws URISyntaxException {
        log.debug("REST request to partial update ToppingToppingGroup partially : {}, {}", id, toppingToppingGroup);
        if (toppingToppingGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, toppingToppingGroup.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!toppingToppingGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ToppingToppingGroup> result = toppingToppingGroupService.partialUpdate(toppingToppingGroup);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, toppingToppingGroup.getId().toString())
        );
    }

    /**
     * {@code GET  /topping-topping-groups} : get all the toppingToppingGroups.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of toppingToppingGroups in body.
     */
    @GetMapping("/topping-topping-groups")
    public ResponseEntity<List<ToppingToppingGroup>> getAllToppingToppingGroups(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of ToppingToppingGroups");
        Page<ToppingToppingGroup> page = toppingToppingGroupService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /topping-topping-groups/:id} : get the "id" toppingToppingGroup.
     *
     * @param id the id of the toppingToppingGroup to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the toppingToppingGroup, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/topping-topping-groups/{id}")
    public ResponseEntity<ToppingToppingGroup> getToppingToppingGroup(@PathVariable Integer id) {
        log.debug("REST request to get ToppingToppingGroup : {}", id);
        Optional<ToppingToppingGroup> toppingToppingGroup = toppingToppingGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(toppingToppingGroup);
    }

    /**
     * {@code DELETE  /topping-topping-groups/:id} : delete the "id" toppingToppingGroup.
     *
     * @param id the id of the toppingToppingGroup to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/topping-topping-groups/{id}")
    public ResponseEntity<Void> deleteToppingToppingGroup(@PathVariable Integer id) {
        log.debug("REST request to delete ToppingToppingGroup : {}", id);
        toppingToppingGroupService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
