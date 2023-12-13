package vn.softdreams.easypos.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.softdreams.easypos.constants.PackageConstants;
import vn.softdreams.easypos.dto.epPackage.CRMPackageResponse;
import vn.softdreams.easypos.dto.epPackage.CRMPackageSaveRequest;
import vn.softdreams.easypos.service.PackageManagementService;
import vn.softdreams.easypos.util.Common;
import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.Validator;

@RestController
@RequestMapping("/api")
public class PackageManagementResource {

    private final Logger log = LoggerFactory.getLogger(PackageManagementResource.class);
    private static final String ENTITY_NAME = "package";
    private final Validator validator;
    private final PackageManagementService packageManagementService;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public PackageManagementResource(Validator validator, PackageManagementService packageManagementService) {
        this.validator = validator;
        this.packageManagementService = packageManagementService;
    }

    @PostMapping("/client/page/crm/package/create")
    public ResponseEntity<CRMPackageResponse> createPackageFromCRM(@RequestBody CRMPackageSaveRequest request) {
        log.debug("REST request to create package with name : {}", request.getName());

        Common.validateInput(validator, ENTITY_NAME, request);
        CRMPackageResponse response = packageManagementService.saveFromCRM(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/client/page/crm/package/update")
    public ResponseEntity<CRMPackageResponse> updatePackageFromCRM(@RequestBody CRMPackageSaveRequest request) {
        if (request.getPackage_id() == null) {
            return new ResponseEntity<>(
                new CRMPackageResponse(PackageConstants.StatusResponse.FAIL, ExceptionConstants.PACKAGE_ID_NOT_NULL_VI),
                HttpStatus.OK
            );
        }
        log.debug("REST request to update package with id : {}", request.getPackage_id());
        Common.validateInput(validator, ENTITY_NAME, request);

        CRMPackageResponse response = packageManagementService.saveFromCRM(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
