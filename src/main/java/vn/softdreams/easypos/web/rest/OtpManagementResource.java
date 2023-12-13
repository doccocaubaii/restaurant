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
import vn.softdreams.easypos.domain.Otp;
import vn.softdreams.easypos.dto.user.OTPCheckRequest;
import vn.softdreams.easypos.repository.OtpRepository;
import vn.softdreams.easypos.service.OtpManagementService;
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
 * REST controller for managing {@link vn.softdreams.easypos.domain.Otp}.
 */
@RestController
@RequestMapping("/api")
public class OtpManagementResource {

    private final Logger log = LoggerFactory.getLogger(OtpManagementResource.class);

    private static final String ENTITY_NAME = "otp";

    private final Validator customValidator;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OtpManagementService otpService;

    private final OtpRepository otpRepository;

    public OtpManagementResource(Validator customValidator, OtpManagementService otpService, OtpRepository otpRepository) {
        this.customValidator = customValidator;
        this.otpService = otpService;
        this.otpRepository = otpRepository;
    }

    /**
     * {@code POST  /otps} : Create a new otp.
     *
     * @param otp the otp to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new otp, or with status {@code 400 (Bad Request)} if the otp has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/otps")
    public ResponseEntity<Otp> createOtp(@RequestBody Otp otp) throws URISyntaxException {
        log.debug("REST request to save Otp : {}", otp);
        if (otp.getId() != null) {
            throw new BadRequestAlertException("A new otp cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Otp result = otpService.save(otp);
        return ResponseEntity
            .created(new URI("/api/otps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /otps/:id} : Updates an existing otp.
     *
     * @param id the id of the otp to save.
     * @param otp the otp to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated otp,
     * or with status {@code 400 (Bad Request)} if the otp is not valid,
     * or with status {@code 500 (Internal Server Error)} if the otp couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/otps/{id}")
    public ResponseEntity<Otp> updateOtp(@PathVariable(value = "id", required = false) final Integer id, @RequestBody Otp otp)
        throws URISyntaxException {
        log.debug("REST request to update Otp : {}, {}", id, otp);
        if (otp.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, otp.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!otpRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Otp result = otpService.update(otp);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, otp.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /otps/:id} : Partial updates given fields of an existing otp, field will ignore if it is null
     *
     * @param id the id of the otp to save.
     * @param otp the otp to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated otp,
     * or with status {@code 400 (Bad Request)} if the otp is not valid,
     * or with status {@code 404 (Not Found)} if the otp is not found,
     * or with status {@code 500 (Internal Server Error)} if the otp couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/otps/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Otp> partialUpdateOtp(@PathVariable(value = "id", required = false) final Integer id, @RequestBody Otp otp)
        throws URISyntaxException {
        log.debug("REST request to partial update Otp partially : {}, {}", id, otp);
        if (otp.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, otp.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!otpRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Otp> result = otpService.partialUpdate(otp);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, otp.getId().toString())
        );
    }

    /**
     * {@code GET  /otps} : get all the otps.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of otps in body.
     */
    @GetMapping("/otps")
    public ResponseEntity<List<Otp>> getAllOtps(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Otps");
        Page<Otp> page = otpService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /otps/:id} : get the "id" otp.
     *
     * @param id the id of the otp to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the otp, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/otps/{id}")
    public ResponseEntity<Otp> getOtp(@PathVariable Integer id) {
        log.debug("REST request to get Otp : {}", id);
        Optional<Otp> otp = otpService.findOne(id);
        return ResponseUtil.wrapOrNotFound(otp);
    }

    /**
     * {@code DELETE  /otps/:id} : delete the "id" otp.
     *
     * @param id the id of the otp to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/otps/{id}")
    public ResponseEntity<Void> deleteOtp(@PathVariable Integer id) {
        log.debug("REST request to delete Otp : {}", id);
        otpService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/p/client/page/otp/check-otp")
    public ResponseEntity<ResultDTO> forgotPassword(@RequestBody OTPCheckRequest request) throws Exception {
        Common.validateInput(customValidator, ENTITY_NAME, request);
        ResultDTO result = otpService.checkOtp(request);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
