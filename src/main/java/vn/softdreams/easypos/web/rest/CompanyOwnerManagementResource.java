package vn.softdreams.easypos.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.softdreams.easypos.aop.CheckAuthorize;
import vn.softdreams.easypos.dto.companyOwner.CompanyOwnerRequest;
import vn.softdreams.easypos.security.AuthoritiesConstants;
import vn.softdreams.easypos.service.CompanyOwnerManagementService;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.util.Common;

import javax.validation.Validator;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api")
public class CompanyOwnerManagementResource {

    private final Logger log = LoggerFactory.getLogger(CompanyOwnerManagementResource.class);
    private static final String ENTITY_NAME = "CompanyOwner";
    private final CompanyOwnerManagementService companyOwnerManagementService;
    private final Validator validator;

    public CompanyOwnerManagementResource(CompanyOwnerManagementService companyOwnerManagementService, Validator validator) {
        this.companyOwnerManagementService = companyOwnerManagementService;
        this.validator = validator;
    }

    @GetMapping("/page/company/get-with-paging")
    public ResponseEntity<ResultDTO> getAllCompanyOwnersWithPaging(Pageable pageable, String keyword) {
        log.debug("REST request to get a page of CompanyOwners");
        ResultDTO response = companyOwnerManagementService.getAllWithPaging(pageable, keyword);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/admin/page/company/get-with-paging")
    @CheckAuthorize(value = AuthoritiesConstants.AdminCompanyOwner.VIEW)
    public ResponseEntity<ResultDTO> getAllCompanyOwnersWithPagingForAdmin(
        Pageable pageable,
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) String fromDate,
        @RequestParam(required = false) String toDate
    ) {
        log.debug("REST request to get a page of CompanyOwners");
        ResultDTO response = companyOwnerManagementService.getAllForAdmin(pageable, keyword, fromDate, toDate);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/admin/client/companies/companies-owners/create")
    public ResponseEntity<ResultDTO> createCompanyOwner(@RequestBody CompanyOwnerRequest request) throws URISyntaxException {
        log.info("REST request to create Company by Name: {}", request.getName());
        Common.validateInput(validator, ENTITY_NAME, request);
        ResultDTO response = companyOwnerManagementService.save(request);
        return ResponseEntity.created(new URI("/api/client/companies/company-owners/get-with-paging")).body(response);
    }

    @PutMapping("/admin/client/companies/companies-owners/update")
    @CheckAuthorize(value = AuthoritiesConstants.AdminCompanyOwner.EDIT)
    public ResponseEntity<ResultDTO> updateCompanyOwner(@RequestBody CompanyOwnerRequest request) throws URISyntaxException {
        log.info("REST request to update Company by id: {}", request.getId());
        Common.validateInput(validator, ENTITY_NAME, request);
        ResultDTO response = companyOwnerManagementService.save(request);
        return ResponseEntity.created(new URI("/api/client/companies/company-owners/get-with-paging")).body(response);
    }

    @GetMapping("/admin/client/companies/company-owners/get-detail-by-id/{id}")
    @CheckAuthorize(value = AuthoritiesConstants.AdminCompanyOwner.VIEW)
    public ResponseEntity<ResultDTO> getOwnerDetailById(@PathVariable Integer id) {
        log.debug("REST request to get CompanyOwner by Id: {}", id);
        ResultDTO response = companyOwnerManagementService.getDetailById(id);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/admin/client/companies/company-owners/get-by-id/{id}")
    @CheckAuthorize(value = AuthoritiesConstants.AdminCompanyOwner.VIEW)
    public ResponseEntity<ResultDTO> getCompanyOwnerById(@PathVariable Integer id) {
        log.debug("REST request to get CompanyOwner by Id: {}", id);
        ResultDTO response = companyOwnerManagementService.getById(id);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/page/company/get-company-owner/{userId}")
    public ResponseEntity<ResultDTO> getCompaniesByUserId(@PathVariable Integer userId) {
        log.debug("REST request to get CompanyOwner by userId: {}", userId);
        ResultDTO response = companyOwnerManagementService.getByUserId(userId);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/admin/client/companies/company-owners/get-all-items")
    @CheckAuthorize(value = AuthoritiesConstants.AdminCompanyOwner.VIEW)
    public ResponseEntity<ResultDTO> getOwnerItems(Pageable pageable, @RequestParam(required = false) String keyword) {
        ResultDTO response = companyOwnerManagementService.getOwnerItems(pageable, keyword);
        return ResponseEntity.ok().body(response);
    }
}
