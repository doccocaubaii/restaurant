package vn.softdreams.easypos.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.softdreams.easypos.aop.CheckAuthorize;
import vn.softdreams.easypos.dto.area.AreaCreateRequest;
import vn.softdreams.easypos.dto.area.AreaDeleteRequest;
import vn.softdreams.easypos.dto.area.AreaUpdateRequest;
import vn.softdreams.easypos.security.AuthoritiesConstants;
import vn.softdreams.easypos.service.AreaManagementService;
import vn.softdreams.easypos.service.UserService;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.util.Common;

import javax.validation.Validator;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api")
public class AreaManagementResource {

    private final Logger log = LoggerFactory.getLogger(AreaManagementResource.class);

    private static final String ENTITY_NAME = "areaManagement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AreaManagementService areaManagementService;
    private final Validator customValidator;
    private final UserService userService;

    public AreaManagementResource(AreaManagementService areaManagementService, Validator customValidator, UserService userService) {
        this.areaManagementService = areaManagementService;
        this.customValidator = customValidator;
        this.userService = userService;
    }

    @GetMapping("/client/page/area/get-all-for-offline")
    //    @CheckAuthorize(value = AuthoritiesConstants.Area.VIEW)
    public ResponseEntity<ResultDTO> getAllForOffline() {
        userService.getUserWithAuthorities();
        return ResponseEntity.ok().body(areaManagementService.getAllForOffline());
    }

    @GetMapping("/client/page/area/get-all-with-paging")
    //    @CheckAuthorize(value = AuthoritiesConstants.Area.VIEW)
    public ResponseEntity<ResultDTO> getAllWithPaging(
        @NotNull @RequestParam Integer areaSize,
        @NotNull @RequestParam Integer areaUnitSize,
        @RequestParam(required = false) Integer reservationId,
        @RequestParam(required = false) String areaKeyword,
        @RequestParam(required = false) Integer areaId,
        @RequestParam(required = false) String areaUnitKeyword
    ) {
        return ResponseEntity
            .ok()
            .body(areaManagementService.getAllWithPaging(areaSize, areaUnitSize, reservationId, areaKeyword, areaUnitKeyword, areaId));
    }

    @PostMapping("/client/page/area/create")
    @CheckAuthorize(value = AuthoritiesConstants.Area.ADD)
    public ResponseEntity<ResultDTO> createArea(@RequestBody AreaCreateRequest request) {
        userService.getUserWithAuthorities(request.getComId());
        Common.validateInput(customValidator, ENTITY_NAME, request);
        return ResponseEntity.ok().body(areaManagementService.createArea(request));
    }

    @PutMapping("/client/page/area/update")
    @CheckAuthorize(value = AuthoritiesConstants.Area.UPDATE)
    public ResponseEntity<ResultDTO> updateArea(@RequestBody AreaUpdateRequest request) {
        userService.getUserWithAuthorities(request.getComId());
        Common.validateInput(customValidator, ENTITY_NAME, request);
        return new ResponseEntity<>(areaManagementService.updateArea(request), HttpStatus.OK);
    }

    @PostMapping("/client/page/area/delete")
    @CheckAuthorize(value = AuthoritiesConstants.Area.DELETE)
    public ResponseEntity<ResultDTO> deleteArea(@RequestBody AreaDeleteRequest request) {
        userService.getUserWithAuthorities(request.getComId());
        Common.validateInput(customValidator, ENTITY_NAME, request);
        return new ResponseEntity<>(areaManagementService.deleteArea(request), HttpStatus.OK);
    }

    @GetMapping("/client/page/area/by-id/{id}")
    @CheckAuthorize(value = AuthoritiesConstants.Area.VIEW)
    public ResponseEntity<ResultDTO> findOneArea(@PathVariable @NotNull Integer id) {
        //        userService.getUserWithAuthorities();
        return new ResponseEntity<>(areaManagementService.findOneArea(id), HttpStatus.OK);
    }
}
