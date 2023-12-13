package vn.softdreams.easypos.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.softdreams.easypos.aop.CheckAuthorize;
import vn.softdreams.easypos.dto.areaUnit.AreaUnitCreateRequest;
import vn.softdreams.easypos.dto.areaUnit.AreaUnitDeleteRequest;
import vn.softdreams.easypos.dto.areaUnit.AreaUnitUpdateRequest;
import vn.softdreams.easypos.security.AuthoritiesConstants;
import vn.softdreams.easypos.service.AreaUnitManagementService;
import vn.softdreams.easypos.service.UserService;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.util.Common;

import javax.validation.Validator;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api")
public class AreaUnitManagementResource {

    private final Logger log = LoggerFactory.getLogger(AreaUnitManagementResource.class);
    private static final String ENTITY_NAME = "areaUnitManagement";
    private final AreaUnitManagementService areaUnitManagementService;
    private final Validator customValidator;
    private final UserService userService;

    public AreaUnitManagementResource(
        AreaUnitManagementService areaUnitManagementService,
        Validator customValidator,
        UserService userService
    ) {
        this.areaUnitManagementService = areaUnitManagementService;
        this.customValidator = customValidator;
        this.userService = userService;
    }

    @PostMapping("/client/page/area-unit/create")
    @CheckAuthorize(value = AuthoritiesConstants.AreaUnit.ADD)
    public ResponseEntity<ResultDTO> createAreaUnit(@RequestBody AreaUnitCreateRequest request) {
        userService.getUserWithAuthorities(request.getComId());
        Common.validateInput(customValidator, ENTITY_NAME, request);
        return new ResponseEntity<>(areaUnitManagementService.createAreaUnit(request), HttpStatus.OK);
    }

    @PutMapping("/client/page/area-unit/update")
    @CheckAuthorize(value = AuthoritiesConstants.AreaUnit.UPDATE)
    public ResponseEntity<ResultDTO> updateAreaUnit(@RequestBody AreaUnitUpdateRequest request) {
        userService.getUserWithAuthorities(request.getComId());
        Common.validateInput(customValidator, ENTITY_NAME, request);
        return new ResponseEntity<>(areaUnitManagementService.updateAreaUnit(request), HttpStatus.OK);
    }

    @PostMapping("/client/page/area-unit/delete")
    @CheckAuthorize(value = AuthoritiesConstants.AreaUnit.DELETE)
    public ResponseEntity<ResultDTO> deleteAreaUnit(@RequestBody AreaUnitDeleteRequest request) {
        userService.getUserWithAuthorities(request.getComId());
        Common.validateInput(customValidator, ENTITY_NAME, request);
        return new ResponseEntity<>(areaUnitManagementService.deleteAreaUnit(request), HttpStatus.OK);
    }

    @GetMapping("/client/page/area-unit/by-id/{id}")
    @CheckAuthorize(value = AuthoritiesConstants.AreaUnit.VIEW)
    public ResponseEntity<ResultDTO> findOneAreaUnit(@PathVariable @NotNull Integer id) {
        userService.getUserWithAuthorities();
        return new ResponseEntity<>(areaUnitManagementService.findOneAreaUnit(id), HttpStatus.OK);
    }
}
