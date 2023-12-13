package vn.softdreams.easypos.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import vn.softdreams.easypos.domain.BillPayment;
import vn.softdreams.easypos.repository.BillPaymentRepository;
import vn.softdreams.easypos.service.BillPaymentService;
import vn.softdreams.easypos.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link vn.softdreams.easypos.domain.BillPayment}.
 */
@RestController
@RequestMapping("/api")
public class BillPaymentResource {

    private final Logger log = LoggerFactory.getLogger(BillPaymentResource.class);

    private static final String ENTITY_NAME = "billPayment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BillPaymentService billPaymentService;

    private final BillPaymentRepository billPaymentRepository;

    public BillPaymentResource(BillPaymentService billPaymentService, BillPaymentRepository billPaymentRepository) {
        this.billPaymentService = billPaymentService;
        this.billPaymentRepository = billPaymentRepository;
    }

    /**
     * {@code POST  /bill-payments} : Create a new billPayment.
     *
     * @param billPayment the billPayment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new billPayment, or with status {@code 400 (Bad Request)} if the billPayment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bill-payments")
    public ResponseEntity<BillPayment> createBillPayment(@RequestBody BillPayment billPayment) throws URISyntaxException {
        log.debug("REST request to save BillPayment : {}", billPayment);
        if (billPayment.getId() != null) {
            throw new BadRequestAlertException("A new billPayment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BillPayment result = billPaymentService.save(billPayment);
        return ResponseEntity
            .created(new URI("/api/bill-payments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bill-payments/:id} : Updates an existing billPayment.
     *
     * @param id the id of the billPayment to save.
     * @param billPayment the billPayment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated billPayment,
     * or with status {@code 400 (Bad Request)} if the billPayment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the billPayment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bill-payments/{id}")
    public ResponseEntity<BillPayment> updateBillPayment(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BillPayment billPayment
    ) throws URISyntaxException {
        log.debug("REST request to update BillPayment : {}, {}", id, billPayment);
        if (billPayment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, billPayment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!billPaymentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BillPayment result = billPaymentService.update(billPayment);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, billPayment.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /bill-payments/:id} : Partial updates given fields of an existing billPayment, field will ignore if it is null
     *
     * @param id the id of the billPayment to save.
     * @param billPayment the billPayment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated billPayment,
     * or with status {@code 400 (Bad Request)} if the billPayment is not valid,
     * or with status {@code 404 (Not Found)} if the billPayment is not found,
     * or with status {@code 500 (Internal Server Error)} if the billPayment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/bill-payments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BillPayment> partialUpdateBillPayment(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BillPayment billPayment
    ) throws URISyntaxException {
        log.debug("REST request to partial update BillPayment partially : {}, {}", id, billPayment);
        if (billPayment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, billPayment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!billPaymentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BillPayment> result = billPaymentService.partialUpdate(billPayment);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, billPayment.getId().toString())
        );
    }

    /**
     * {@code GET  /bill-payments} : get all the billPayments.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of billPayments in body.
     */
    @GetMapping("/bill-payments")
    public List<BillPayment> getAllBillPayments() {
        log.debug("REST request to get all BillPayments");
        return billPaymentService.findAll();
    }

    /**
     * {@code GET  /bill-payments/:id} : get the "id" billPayment.
     *
     * @param id the id of the billPayment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the billPayment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bill-payments/{id}")
    public ResponseEntity<BillPayment> getBillPayment(@PathVariable Long id) {
        log.debug("REST request to get BillPayment : {}", id);
        Optional<BillPayment> billPayment = billPaymentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(billPayment);
    }

    /**
     * {@code DELETE  /bill-payments/:id} : delete the "id" billPayment.
     *
     * @param id the id of the billPayment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bill-payments/{id}")
    public ResponseEntity<Void> deleteBillPayment(@PathVariable Long id) {
        log.debug("REST request to delete BillPayment : {}", id);
        billPaymentService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
