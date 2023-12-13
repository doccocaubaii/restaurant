package vn.softdreams.easypos.web.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import vn.softdreams.easypos.constants.ResultConstants;
import vn.softdreams.easypos.dto.authorities.AuthenticationDTO;
import vn.softdreams.easypos.dto.authorities.AuthoritiesResponse;
import vn.softdreams.easypos.integration.easybooks88.api.EB88ApiClient;
import vn.softdreams.easypos.security.EPUserAuthenticationToken;
import vn.softdreams.easypos.security.UserNameNotFoundExceptionCustom;
import vn.softdreams.easypos.security.jwt.JWTFilter;
import vn.softdreams.easypos.security.jwt.TokenProvider;
import vn.softdreams.easypos.service.UserService;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.util.Common;
import vn.softdreams.easypos.web.rest.errors.BadRequestAlertException;
import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;
import vn.softdreams.easypos.web.rest.errors.InternalServerException;
import vn.softdreams.easypos.web.rest.vm.LoginVM;

import javax.validation.Validator;
import java.io.IOException;
import java.net.InetAddress;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
public class UserJWTController {

    private final Logger log = LoggerFactory.getLogger(UserJWTController.class);
    private static final String ENTITY_NAME = "authenticate";

    @Autowired
    public UserService userService;

    private final TokenProvider tokenProvider;
    private final Validator customValidator;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final EB88ApiClient eb88ApiClient;

    public UserJWTController(
        TokenProvider tokenProvider,
        Validator customValidator,
        AuthenticationManagerBuilder authenticationManagerBuilder,
        EB88ApiClient eb88ApiClient
    ) {
        this.tokenProvider = tokenProvider;
        this.customValidator = customValidator;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.eb88ApiClient = eb88ApiClient;
    }

    @GetMapping("/p/client/ping")
    public ResponseEntity<ResultDTO> ping() {
        try {
            InetAddress address = InetAddress.getByName("8.8.8.8");
            if (address.isReachable(5000)) {
                return ResponseEntity.ok(new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS, true));
            } else {
                return new ResponseEntity<>(new ResultDTO(ResultConstants.FALSE, ResultConstants.FALSE), HttpStatus.BAD_GATEWAY);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(new ResultDTO(ResultConstants.FALSE, ResultConstants.FALSE), HttpStatus.BAD_GATEWAY);
        }
    }

    @PostMapping("/client/common/authenticate")
    public ResponseEntity<ResultDTO> authorize(@RequestBody LoginVM loginVM) {
        Common.validateInput(customValidator, ENTITY_NAME, loginVM);
        return new ResponseEntity<>(login(loginVM), HttpStatus.OK);
    }

    @PostMapping("/client/common/change-session")
    public ResponseEntity<Object> changeSession(@RequestBody Integer comId) {
        Object response = userService.changeSession(comId, eb88ApiClient);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

        private String idToken;

        JWTToken(String idToken) {
            this.idToken = idToken;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }

    public ResultDTO login(LoginVM loginVM) {
        EPUserAuthenticationToken authenticationToken = new EPUserAuthenticationToken(
            loginVM.getUsername(),
            loginVM.getPassword(),
            loginVM.getCompanyId()
        );
        Authentication authentication;
        AuthenticationDTO authenticationDTO;
        ResultDTO resultDTO = new ResultDTO();
        try {
            authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            authenticationDTO = userService.getAuthoritiesAndInfo(loginVM, eb88ApiClient);
        } catch (Exception ex) {
            if (ex instanceof BadCredentialsException) {
                resultDTO = new ResultDTO(ExceptionConstants.BAD_CREDENTIALS, ExceptionConstants.BAD_CREDENTIALS_VI);
            } else if (ex instanceof BadRequestAlertException) {
                resultDTO = new ResultDTO(((BadRequestAlertException) ex).getErrorKey(), ex.getMessage());
            } else if (ex instanceof InternalServerException) {
                resultDTO = new ResultDTO(((InternalServerException) ex).getErrorKey(), ex.getMessage());
            } else if (ex instanceof UserNameNotFoundExceptionCustom || ex instanceof InternalAuthenticationServiceException) {
                resultDTO = new ResultDTO(ExceptionConstants.USER_NOT_FOUND, ex.getMessage());
            } else {
                resultDTO = new ResultDTO(ExceptionConstants.EXCEPTION_ERROR, ExceptionConstants.EXCEPTION_ERROR_VI);
            }
            return resultDTO;
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = "";

        AuthoritiesResponse authoritiesResponseDTO = new AuthoritiesResponse();

        authoritiesResponseDTO.setActivate(authenticationDTO.isActivate());
        authoritiesResponseDTO.setCompanies(authenticationDTO.getCompanies());

        authoritiesResponseDTO.setFullName(authenticationDTO.getFullName());
        authoritiesResponseDTO.setUserName(authenticationDTO.getUsername());
        authoritiesResponseDTO.setRole(authenticationDTO.getRole());
        authoritiesResponseDTO.setTaxCode(authenticationDTO.getTaxCode());
        authoritiesResponseDTO.setCompanyId(authenticationDTO.getCompanyId());
        authoritiesResponseDTO.setPermissions(authenticationDTO.getPermissions());
        authoritiesResponseDTO.setPermissionList(authenticationDTO.getPermissionList());
        authoritiesResponseDTO.setCompanyName(authenticationDTO.getCompanyName());
        authoritiesResponseDTO.setId(authenticationDTO.getId());
        authoritiesResponseDTO.setService(authenticationDTO.getService());
        HttpHeaders httpHeaders = new HttpHeaders();

        if (authenticationDTO.isActivate()) {
            jwt = tokenProvider.createToken(authenticationDTO, loginVM);
            authoritiesResponseDTO.setId_token(jwt);
            resultDTO.setMessage(ResultConstants.SUCCESS);
            resultDTO.setReason(ResultConstants.LOGIN_SUCCESS_VI);
        } else {
            resultDTO.setMessage(ResultConstants.LOGIN_ERROR);
            resultDTO.setReason(ResultConstants.LOGIN_ERROR_VI);
        }
        resultDTO.setData(authoritiesResponseDTO);
        resultDTO.setStatus(authenticationDTO.isActivate());
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return resultDTO;
    }
}
