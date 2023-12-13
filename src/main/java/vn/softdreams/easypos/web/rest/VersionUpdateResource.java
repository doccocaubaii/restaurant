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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;
import vn.softdreams.easypos.aop.CheckAuthorize;
import vn.softdreams.easypos.constants.ResultConstants;
import vn.softdreams.easypos.domain.VersionUpdate;
import vn.softdreams.easypos.dto.versionUpdate.SaveVersionUpdateRequest;
import vn.softdreams.easypos.repository.VersionUpdateRepository;
import vn.softdreams.easypos.security.AuthoritiesConstants;
import vn.softdreams.easypos.service.VersionUpdateService;
import vn.softdreams.easypos.service.dto.ResultDTO;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * REST controller for managing {@link vn.softdreams.easypos.domain.VersionUpdate}.
 */
@RestController
@RequestMapping("/api")
public class VersionUpdateResource {

    private final Logger log = LoggerFactory.getLogger(VersionUpdateResource.class);

    private static final String ENTITY_NAME = "versionUpdate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VersionUpdateService versionUpdateService;

    private final VersionUpdateRepository versionUpdateRepository;

    public VersionUpdateResource(VersionUpdateService versionUpdateService, VersionUpdateRepository versionUpdateRepository) {
        this.versionUpdateService = versionUpdateService;
        this.versionUpdateRepository = versionUpdateRepository;
    }

    @PostMapping("/admin/client/page/version-update/create")
    public ResponseEntity<ResultDTO> createVersionUpdate(
        MultipartFile images,
        @RequestParam(required = false) Integer comId,
        @RequestParam(required = false) String version,
        @RequestParam(required = false) String date,
        @RequestParam(required = false) String description,
        @RequestParam(required = false) String link,
        @RequestParam(required = false) String startDate,
        @RequestParam(required = false) String endDate,
        @RequestParam(required = false) Integer system,
        HttpServletRequest request
    ) {
        SaveVersionUpdateRequest versionUpdateRequest = new SaveVersionUpdateRequest(
            comId,
            version,
            date,
            description,
            link,
            startDate,
            endDate,
            system
        );
        ResultDTO resultDTO = versionUpdateService.save(images, versionUpdateRequest, request);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PostMapping("/admin/client/page/version-update/update")
    public ResponseEntity<ResultDTO> updateVersionUpdate(
        MultipartFile images,
        @RequestParam(required = false) Integer id,
        @RequestParam(required = false) Integer comId,
        @RequestParam(required = false) String version,
        @RequestParam(required = false) String date,
        @RequestParam(required = false) String description,
        @RequestParam(required = false) String link,
        @RequestParam(required = false) String startDate,
        @RequestParam(required = false) String endDate,
        @RequestParam(required = false) Integer system,
        HttpServletRequest request
    ) {
        SaveVersionUpdateRequest versionUpdateRequest = new SaveVersionUpdateRequest(
            id,
            comId,
            version,
            date,
            description,
            link,
            startDate,
            endDate,
            system
        );
        ResultDTO resultDTO = versionUpdateService.save(images, versionUpdateRequest, request);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @DeleteMapping("/admin/client/page/version-update/delete/{id}")
    @CheckAuthorize(value = AuthoritiesConstants.AdminConfig.DELETE)
    public ResponseEntity<ResultDTO> deleteVersionUpdate(@NotNull @PathVariable("id") Integer id) {
        log.debug("REST request to delete config by id: {}", id);
        ResultDTO result = versionUpdateService.deleteResult(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/client/page/version-update/get-today")
    public ResponseEntity<ResultDTO> getAllVersionUpdatesToday(@RequestParam(required = false) Integer system) {
        log.debug("REST request to get a page of VersionUpdates");
        return ResponseEntity.ok().body(versionUpdateService.getNotificationToday(system));
    }

    @GetMapping("/admin/client/page/version-update/get-with-paging")
    public ResponseEntity<ResultDTO> getVersionUpdateWithPaging(
        Pageable pageable,
        @RequestParam(required = false) Integer comId,
        @RequestParam(required = false) String fromDate,
        @RequestParam(required = false) String toDate
    ) {
        log.debug("REST request to get a page of VersionUpdates");
        Page<VersionUpdate> page = versionUpdateService.getWithPaging(pageable, comId, fromDate, toDate);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity
            .ok()
            .headers(headers)
            .body(
                new ResultDTO(
                    ResultConstants.SUCCESS,
                    ResultConstants.SUCCESS_GET_LIST,
                    true,
                    page.getContent(),
                    (int) page.getTotalElements()
                )
            );
    }

    @GetMapping("/admin/client/page/version-update/by-id/{id}")
    public ResponseEntity<VersionUpdate> getVersionUpdate(@PathVariable Integer id) {
        log.debug("REST request to get VersionUpdate : {}", id);
        Optional<VersionUpdate> versionUpdate = versionUpdateService.findOne(id);
        return ResponseEntity.ok().body(versionUpdate.get());
    }
}
