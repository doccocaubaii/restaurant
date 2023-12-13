package vn.softdreams.easypos.web.rest;

import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import vn.softdreams.easypos.constants.ResultConstants;
import vn.softdreams.easypos.domain.User;
import vn.softdreams.easypos.domain.UserRole;
import vn.softdreams.easypos.dto.TaskLogSendQueueUser;
import vn.softdreams.easypos.dto.authorities.UserRequest;
import vn.softdreams.easypos.dto.user.*;
import vn.softdreams.easypos.integration.easybooks88.api.EB88ApiClient;
import vn.softdreams.easypos.repository.UserRepository;
import vn.softdreams.easypos.security.AuthoritiesConstants;
import vn.softdreams.easypos.service.MailService;
import vn.softdreams.easypos.service.UserService;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.util.Common;
import vn.softdreams.easypos.web.rest.errors.BadRequestAlertException;
import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;
import vn.softdreams.easypos.web.rest.vm.ManagedUserVM;

import javax.validation.Validator;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * REST controller for managing {@link UserRole}.
 */
@RestController
@RequestMapping("/api")
public class UserManagementResource {

    private final Logger log = LoggerFactory.getLogger(UserManagementResource.class);

    private static final String ENTITY_NAME = "userRole";

    private final MailService mailService;
    private final Validator customValidator;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserService userService;
    private final EB88ApiClient eb88ApiClient;
    private final ModelMapper modelMapper;

    public UserManagementResource(
        MailService mailService,
        Validator customValidator,
        UserService userService,
        EB88ApiClient eb88ApiClient,
        ModelMapper modelMapper,
        UserRepository userRepository
    ) {
        this.mailService = mailService;
        this.customValidator = customValidator;
        this.userService = userService;
        this.eb88ApiClient = eb88ApiClient;
        this.modelMapper = modelMapper;
    }

    /**
     * {@code POST  /user-roles} : Create a new userRole.
     *
     * @param userRole the userRole to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userRole, or with status {@code 400 (Bad Request)} if the userRole has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user/create")
    public ResponseEntity<User> createUserRole(@RequestBody UserRequest userRequestDTO) throws URISyntaxException {
        log.debug("REST request to save UserRole : {}", userRequestDTO);
        if (userRequestDTO.getId() != null) {
            throw new BadRequestAlertException("A new userRole cannot already have an ID", ENTITY_NAME, "idexists");
        }
        User result = userService.save(userRequestDTO);
        return ResponseEntity
            .created(new URI("/api/user-roles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/client/common/register/update")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ResultDTO> updateRegisterAccount(@RequestBody RegisterRequest registerRequestDTO) throws Exception {
        Common.validateInput(customValidator, ENTITY_NAME, registerRequestDTO);
        if (registerRequestDTO.getComId() == null) {
            throw new BadRequestAlertException(
                ExceptionConstants.COMPANY_ID_NOT_NULL_VI,
                ENTITY_NAME,
                ExceptionConstants.COMPANY_ID_NOT_NULL_CODE
            );
        }
        ResultDTO result = handleResultRegister(registerRequestDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/integration/crm/register/update")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ResultDTO> updateRegisterIntegrationCrm(@RequestBody RegisterRequest registerRequestDTO) throws Exception {
        Common.validateInput(customValidator, ENTITY_NAME, registerRequestDTO);
        if (registerRequestDTO.getComId() == null) {
            throw new BadRequestAlertException(
                ExceptionConstants.COMPANY_ID_NOT_NULL_VI,
                ENTITY_NAME,
                ExceptionConstants.COMPANY_ID_NOT_NULL_CODE
            );
        }
        ResultDTO result = handleResultRegister(registerRequestDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/client/common/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ResultDTO> registerAccount(@RequestBody RegisterRequest registerRequestDTO) throws Exception {
        Common.validateInput(customValidator, ENTITY_NAME, registerRequestDTO);
        ResultDTO result = handleResultRegister(registerRequestDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/integration/crm/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ResultDTO> registerIntegrationCrm(@RequestBody RegisterRequest request) throws Exception {
        Common.validateInput(customValidator, ENTITY_NAME, request);
        if (Strings.isNullOrEmpty(request.getCompanyTaxCode())) {
            throw new BadRequestAlertException(
                ExceptionConstants.COMPANY_TAX_CODE_NOT_EMPTY_VI,
                ENTITY_NAME,
                ExceptionConstants.COMPANY_TAX_CODE_NOT_EMPTY_CODE
            );
        }
        if (request.getPackageId() == null) {
            throw new BadRequestAlertException(
                ExceptionConstants.COMPANY_USER_PACKAGE_ID_NOT_NULL_VI,
                ENTITY_NAME,
                ExceptionConstants.COMPANY_USER_PACKAGE_ID_NOT_NULL_CODE
            );
        }
        ResultDTO result = handleResultRegister(request);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    private ResultDTO handleResultRegister(RegisterRequest registerRequestDTO) throws Exception {
        ResultDTO result = userService.registerUser(registerRequestDTO, eb88ApiClient);
        if (result.isStatus()) {
            TaskLogSendQueueUser queueUser = (TaskLogSendQueueUser) result.getData();
            if (registerRequestDTO.getComId() == null && queueUser.getUser() != null) {
                userService.userCompletionAsync(queueUser.getUser());
            }
            result.setData(queueUser.getComId());
        }
        return result;
    }

    @PostMapping("/client/common/register/otp/send")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ResultDTO> registerSendOtp(@RequestBody RegisterSendOtpRequest request) {
        Common.validateInput(customValidator, ENTITY_NAME, request);
        ResultDTO result = userService.registerSendOtp(request);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/client/common/register/otp/check")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ResultDTO> registerCheckOtp(@RequestBody RegisterCheckOtpRequest request) {
        Common.validateInput(customValidator, ENTITY_NAME, request);
        ResultDTO result = userService.registerCheckOtp(request);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/client/user/by-username/{username}")
    public ResponseEntity<ResultDTO> getByUserName(@PathVariable("username") @NotBlank String userName) {
        ResultDTO result = userService.getByUserName(userName);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/client/common/auto-gen-code")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ResultDTO> autoGenCode() {
        ResultDTO result = userService.autoGenSeqNameCode();
        mailService.sendActivationEmail((User) result.getData());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/user/get-all")
    public ResponseEntity<List<User>> getAll(@ParameterObject Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get all users");
        Page<User> users = userService.findAllUsers(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), users);
        return ResponseEntity.ok().headers(headers).body(users.getContent());
    }

    @GetMapping("/user/find-one/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<UserResponse> getOneUser(@NotNull @PathVariable Integer id) {
        log.debug("REST request to get user : {}", id);
        UserResponse user = userService.findUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/user/update")
    public ResponseEntity<User> updateUser(@RequestBody UserRequest userRequestDTO) throws URISyntaxException {
        log.debug("REST request to update user : {}", userRequestDTO);
        if (userRequestDTO.getId() == null) {
            throw new BadRequestAlertException("Can not update User with", ENTITY_NAME, "id null");
        }

        User result = userService.updateUser(userRequestDTO);
        return ResponseEntity
            .created(new URI("/api/user/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    private static boolean isPasswordLengthInvalid(String password) {
        return (
            StringUtils.isEmpty(password) ||
            password.length() < ManagedUserVM.PASSWORD_MIN_LENGTH ||
            password.length() > ManagedUserVM.PASSWORD_MAX_LENGTH
        );
    }

    @GetMapping("/client/common/get-device-code")
    public ResponseEntity<ResultDTO> registerDeviceForCode(@RequestParam String deviceId) {
        log.debug("REST request to register and get authority code back : {}", deviceId);
        if (deviceId == null) {
            return ResponseEntity.ok(new ResultDTO(ResultConstants.ERROR_DEVICE_CODE, ResultConstants.ERROR_DEVICE_CODE_VI, false));
        }
        ResultDTO result = userService.registerDeviceForCode(deviceId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/client/user/change-password")
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.SYSTEM_ADMIN + "\")")
    public ResponseEntity<ResultDTO> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        Common.validateInput(customValidator, ENTITY_NAME, changePasswordRequest);
        ResultDTO result = userService.changePassword(changePasswordRequest, eb88ApiClient);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get userItems for dropdown list
     */
    @GetMapping("/admin/page/common/get-user-items")
    public ResponseEntity<ResultDTO> getUserItems(@RequestParam(required = false) String keyword) {
        log.debug("REST request to get user items");
        ResultDTO result = userService.getUserItems(keyword);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/client/common/forgot-password")
    public ResponseEntity<ResultDTO> forgotPassword(@RequestBody ForgotPasswordRequest request) throws Exception {
        Common.validateInput(customValidator, ENTITY_NAME, request);
        ResultDTO result = userService.forgotPassword(request, eb88ApiClient);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/admin/common/reset-password")
    public ResponseEntity<ResultDTO> changePasswordForAdmin(@RequestBody AdminChangePasswordRequest request) throws Exception {
        Common.validateInput(customValidator, ENTITY_NAME, request);
        ResultDTO result = userService.changePasswordForAdmin(request, eb88ApiClient, Boolean.TRUE);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/p/client/user/change-password-otp")
    public ResponseEntity<ResultDTO> changePasswordForOtp(@RequestBody AdminChangePasswordRequest request) throws Exception {
        Common.validateInput(customValidator, ENTITY_NAME, request);
        ResultDTO result = userService.changePasswordForAdmin(request, eb88ApiClient, Boolean.FALSE);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
