package vn.softdreams.easypos.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.softdreams.easypos.service.OwnerDeviceManagementService;
import vn.softdreams.easypos.service.dto.ResultDTO;

@RestController
@RequestMapping("/api")
public class OwnerDeviceManagementResource {

    private final Logger log = LoggerFactory.getLogger(OwnerDeviceManagementResource.class);

    private static final String ENTITY_NAME = "owner-device";

    private final OwnerDeviceManagementService ownerDeviceManagementService;

    public OwnerDeviceManagementResource(OwnerDeviceManagementService ownerDeviceManagementService) {
        this.ownerDeviceManagementService = ownerDeviceManagementService;
    }

    @GetMapping("/admin/client/page/owner-device/get-with-paging")
    public ResponseEntity<ResultDTO> getWithPaging(
        Pageable pageable,
        @RequestParam(required = false) Integer ownerId,
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) String fromDate,
        @RequestParam(required = false) String toDate
    ) {
        log.debug("REST request to get Owner Device with paging");
        ResultDTO result = ownerDeviceManagementService.getWithPaging(pageable, ownerId, keyword, fromDate, toDate);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
