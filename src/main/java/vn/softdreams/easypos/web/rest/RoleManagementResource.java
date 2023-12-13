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
import vn.softdreams.easypos.domain.Authority;
import vn.softdreams.easypos.domain.Role;
import vn.softdreams.easypos.dto.authorities.TreeView;
import vn.softdreams.easypos.service.RoleManagementService;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.service.dto.RolePermissionRequestDTO;
import vn.softdreams.easypos.web.rest.errors.BadRequestAlertException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class RoleManagementResource {

    private final Logger log = LoggerFactory.getLogger(RoleManagementResource.class);

    private static final String ENTITY_NAME = "productProductGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RoleManagementService roleManagentService;

    public RoleManagementResource(RoleManagementService roleManagentService) {
        this.roleManagentService = roleManagentService;
    }

    @PostMapping("role/authority/create")
    public ResponseEntity<Authority> createAuthority(@RequestBody Authority authority) throws URISyntaxException {
        log.debug("REST request to save ProductProductGroup : {}", authority);
        if (authority.getId() != null) {
            throw new BadRequestAlertException("A new productProductGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Authority result = roleManagentService.saveAuthority(authority);
        return (ResponseEntity<Authority>) ResponseEntity
            .created(new URI("/api/product-product-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("role/authority/update")
    public ResponseEntity<Authority> updateAuthority(@RequestBody Authority authority) throws URISyntaxException {
        log.debug("REST request to save ProductProductGroup : {}", authority);
        if (authority.getId() == null) {
            throw new BadRequestAlertException("A new productProductGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Authority result = roleManagentService.saveAuthority(authority);
        return (ResponseEntity<Authority>) ResponseEntity
            .created(new URI("/api/product-product-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @GetMapping("role/authority/find-one/{id}")
    public ResponseEntity<Authority> getArea(@PathVariable Integer id) {
        log.debug("REST request to get Area : {}", id);
        Optional<Authority> area = roleManagentService.findOneAuthority(id);
        return ResponseUtil.wrapOrNotFound(area);
    }

    @DeleteMapping("role/authority/delete/{id}")
    public ResponseEntity<ResultDTO> deleteAuthority(@PathVariable Integer id) {
        log.debug("REST request to get Area : {}", id);
        ResultDTO resultDTO = roleManagentService.deleteAuthority(id);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @GetMapping("role/authority/get-all")
    public ResponseEntity<List<Authority>> getAllAuthorityParent(@RequestParam(required = false) Boolean isParent)
        throws URISyntaxException {
        log.debug("REST request to save ProductProductGroup : {}");
        List<Authority> result = roleManagentService.getAllAuthorityParent(isParent);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("role/authority/get-all/tree")
    public ResponseEntity<ArrayList<TreeView>> getAllRole() throws URISyntaxException {
        log.debug("REST request to save ProductProductGroup : {}");
        ArrayList<TreeView> result = roleManagentService.getAllRole(false);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("role/role-permission/save")
    public ResponseEntity<ResultDTO> saveRolePermission(@RequestBody(required = false) RolePermissionRequestDTO rolePermissionRequestDTO) {
        log.debug("REST request to save ProductProductGroup : {}");
        ResultDTO result = roleManagentService.saveRolePermission(rolePermissionRequestDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("role/get-all")
    public ResponseEntity<List<Role>> getAllRoles(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Roles");
        Page<Role> page = roleManagentService.findAllRole(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @DeleteMapping("role/delete/{id}")
    public ResponseEntity<ResultDTO> deleteRole(@PathVariable Integer id) {
        log.debug("REST request to delete roleId : {}", id);
        ResultDTO resultDTO = roleManagentService.deleteRole(id);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @GetMapping("/client/page/permission/get-all")
    public ResponseEntity<ResultDTO> getAllPermission() {
        log.debug("REST request to get all permission");
        ResultDTO result = roleManagentService.getAllPermission();
        return ResponseEntity.ok(result);
    }
}
