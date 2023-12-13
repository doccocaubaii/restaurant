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
import vn.softdreams.easypos.aop.CheckAuthorize;
import vn.softdreams.easypos.domain.Company;
import vn.softdreams.easypos.domain.CompanyOwner;
import vn.softdreams.easypos.dto.company.CompanyCreateRequest;
import vn.softdreams.easypos.dto.company.CompanyUpdateRequest;
import vn.softdreams.easypos.security.AuthoritiesConstants;
import vn.softdreams.easypos.service.CompanyManagementService;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.util.Common;
import vn.softdreams.easypos.web.rest.errors.BadRequestAlertException;

import javax.validation.Validator;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class CompanyManagementResource {

    private final Logger log = LoggerFactory.getLogger(CompanyManagementResource.class);
    private static final String ENTITY_NAME = "CompanyOwner";
    private final CompanyManagementService companyManagementService;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final Validator validator;

    public CompanyManagementResource(CompanyManagementService companyManagementService, Validator validator) {
        this.companyManagementService = companyManagementService;
        this.validator = validator;
    }

    @PostMapping("/companies")
    public ResponseEntity<Company> createCompany(@RequestBody Company company) throws URISyntaxException {
        log.debug("REST request to save Company : {}", company);
        if (company.getId() != null) {
            throw new BadRequestAlertException("A new company cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Company result = companyManagementService.save(company);
        return ResponseEntity
            .created(new URI("/api/companies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/companies/companies-owner")
    public ResponseEntity<CompanyOwner> createCompanyOwner(@RequestBody CompanyOwner company) throws URISyntaxException {
        log.debug("REST request to save Company : {}", company);
        if (company.getId() != null) {
            throw new BadRequestAlertException("A new company cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CompanyOwner result = companyManagementService.saveOwner(company);
        return ResponseEntity
            .created(new URI("/api/companies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @GetMapping("/companies/company-owners/get-all")
    public ResponseEntity<List<CompanyOwner>> getAllCompanyOwners(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of CompanyOwners");
        Page<CompanyOwner> page = companyManagementService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PutMapping("/companies/{id}")
    public ResponseEntity<Company> updateCompany(
        @PathVariable(value = "id", required = false) final Integer id,
        @RequestBody Company company
    ) throws URISyntaxException {
        log.debug("REST request to update Company : {}, {}", id, company);
        if (company.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, company.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        Company result = companyManagementService.update(company);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, company.getId().toString()))
            .body(result);
    }

    @GetMapping("/admin/client/companies/company/get-with-paging")
    @CheckAuthorize(value = AuthoritiesConstants.AdminCompany.VIEW)
    public ResponseEntity<ResultDTO> getAllCompaniesWithPaging(
        Pageable pageable,
        @RequestParam(required = false) Integer ownerId,
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) String fromDate,
        @RequestParam(required = false) String toDate
    ) {
        log.debug("REST request to get a page of CompanyOwners");
        ResultDTO response = companyManagementService.getAllWithPaging(pageable, ownerId, keyword, fromDate, toDate);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/admin/client/companies/company/get-by-ownerId/{ownerId}")
    @CheckAuthorize(value = AuthoritiesConstants.AdminCompany.VIEW)
    public ResponseEntity<ResultDTO> getAllByOwnerId(@PathVariable("ownerId") Integer ownerId) {
        log.debug("REST request to get all by owner id: {}", ownerId);
        ResultDTO response = companyManagementService.getAllByOwnerId(ownerId);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/admin/page/company/create")
    @CheckAuthorize(value = AuthoritiesConstants.AdminCompany.ADD)
    public ResponseEntity<ResultDTO> createCompany(@RequestBody CompanyCreateRequest request) throws URISyntaxException {
        log.debug("REST request to create Company by Name: {}", request.getName());
        Common.validateInput(validator, ENTITY_NAME, request);
        ResultDTO response = companyManagementService.create(request);
        return ResponseEntity.created(new URI("/api/client/companies/company/get-with-paging")).body(response);
    }

    @PutMapping("/admin/page/company/update")
    @CheckAuthorize(value = AuthoritiesConstants.AdminCompany.EDIT)
    public ResponseEntity<ResultDTO> updateCompany(@RequestBody CompanyUpdateRequest request) throws URISyntaxException {
        log.debug("REST request to update Company by id: {}", request.getComId());
        Common.validateInput(validator, ENTITY_NAME, request);
        ResultDTO response = companyManagementService.update(request);
        return ResponseEntity.created(new URI("/admin/page/company/get-with-paging")).body(response);
    }

    @GetMapping("/admin/client/companies/company/get-by-id/{id}")
    @CheckAuthorize(value = AuthoritiesConstants.AdminCompany.VIEW)
    public ResponseEntity<ResultDTO> getCompanyOwnerById(@PathVariable Integer id) {
        log.debug("REST request to get CompanyOwner by Id: {}", id);
        ResultDTO response = companyManagementService.getById(id);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/page/company/create")
    public ResponseEntity<ResultDTO> createCompanyForClient(@RequestBody CompanyCreateRequest request) throws URISyntaxException {
        log.debug("REST request to create Company by Name: {}", request.getName());
        Common.validateInput(validator, ENTITY_NAME, request);
        ResultDTO response = companyManagementService.create(request);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/page/company/update")
    public ResponseEntity<ResultDTO> updateCompanyForClient(@RequestBody CompanyUpdateRequest request) {
        log.debug("REST request to update Company by id: {}", request.getComId());
        Common.validateInput(validator, ENTITY_NAME, request);
        ResultDTO response = companyManagementService.update(request);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/admin/client/companies/company/get-all-items")
    @CheckAuthorize(value = AuthoritiesConstants.AdminCompany.VIEW)
    public ResponseEntity<ResultDTO> getAllCompanyItems(@RequestParam(required = false) String keyword) {
        log.debug("REST request to get all company items");
        ResultDTO response = companyManagementService.getAllItems(keyword);
        return ResponseEntity.ok().body(response);
    }
}
