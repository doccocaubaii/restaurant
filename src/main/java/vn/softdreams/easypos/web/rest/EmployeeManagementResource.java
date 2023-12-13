package vn.softdreams.easypos.web.rest;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.softdreams.easypos.aop.CheckAuthorize;
import vn.softdreams.easypos.dto.employee.EmployeeCreateRequest;
import vn.softdreams.easypos.dto.employee.EmployeeUpdateRequest;
import vn.softdreams.easypos.dto.product.DeleteProductList;
import vn.softdreams.easypos.security.AuthoritiesConstants;
import vn.softdreams.easypos.service.EmployerManagementService;
import vn.softdreams.easypos.service.UserService;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.util.Common;
import vn.softdreams.easypos.web.rest.errors.BadRequestAlertException;
import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.Validator;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api")
public class EmployeeManagementResource {

    private final Logger log = LoggerFactory.getLogger(EmployeeManagementResource.class);

    private static final String ENTITY_NAME = "Employee";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmployerManagementService employerManagementService;
    private final UserService userService;
    private final Validator validator;

    public EmployeeManagementResource(EmployerManagementService employerManagementService, UserService userService, Validator validator) {
        this.employerManagementService = employerManagementService;
        this.userService = userService;
        this.validator = validator;
    }

    @GetMapping("/client/page/employee/get-with-paging")
    @CheckAuthorize(value = AuthoritiesConstants.Employee.VIEW)
    public ResponseEntity<ResultDTO> getAllEmployeeWithPaging(
        Pageable pageable,
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) Boolean isCountAll
    ) {
        ResultDTO resultDTO = employerManagementService.getAllEmployeeWithPaging(keyword, pageable, isCountAll);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @GetMapping("/client/page/employee/by-id/{id}")
    @CheckAuthorize(value = AuthoritiesConstants.Employee.VIEW)
    public ResponseEntity<ResultDTO> getUserById(@PathVariable(value = "id") Integer id) {
        ResultDTO resultDTO = employerManagementService.getEmployeeById(id);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @GetMapping("/client/page/employee/get-all-roles/{comId}")
    @CheckAuthorize(value = AuthoritiesConstants.Employee.VIEW)
    public ResponseEntity<ResultDTO> getAllRole(@PathVariable(value = "comId") Integer comId) {
        ResultDTO resultDTO = employerManagementService.getAllRoles(comId);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PostMapping("/client/page/employee/create")
    @CheckAuthorize(value = AuthoritiesConstants.Employee.ADD)
    public ResponseEntity<ResultDTO> create(@RequestBody EmployeeCreateRequest request) {
        if (Strings.isNullOrEmpty(request.getUsername())) {
            throw new BadRequestAlertException(
                ExceptionConstants.USERNAME_NOT_NULL_VI,
                ENTITY_NAME,
                ExceptionConstants.USERNAME_NOT_NULL_CODE
            );
        }
        Common.validateInput(validator, ENTITY_NAME, request);
        return new ResponseEntity<>(employerManagementService.create(request), HttpStatus.OK);
    }

    @PutMapping("/client/page/employee/update")
    @CheckAuthorize(value = AuthoritiesConstants.Employee.UPDATE)
    public ResponseEntity<ResultDTO> update(@RequestBody EmployeeUpdateRequest request) {
        Common.validateInput(validator, ENTITY_NAME, request);
        return new ResponseEntity<>(employerManagementService.update(request), HttpStatus.OK);
    }

    @PutMapping("/client/page/employee/delete/{id}")
    @CheckAuthorize(value = AuthoritiesConstants.Employee.DELETE)
    public ResponseEntity<ResultDTO> delete(@NotNull @PathVariable(value = "id") Integer id) {
        return new ResponseEntity<>(employerManagementService.delete(id), HttpStatus.OK);
    }

    @PutMapping("/client/page/employee/delete-list")
    @CheckAuthorize(value = AuthoritiesConstants.Employee.DELETE)
    public ResponseEntity<ResultDTO> deleteList(@RequestBody DeleteProductList req) {
        return new ResponseEntity<>(employerManagementService.deleteList(req), HttpStatus.OK);
    }
}
