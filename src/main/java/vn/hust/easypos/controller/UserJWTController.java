package vn.hust.easypos.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.Validator;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.hust.easypos.constants.ResultConstants;
import vn.hust.easypos.security.EPUserAuthenticationToken;
import vn.hust.easypos.security.UserNameNotFoundExceptionCustom;
import vn.hust.easypos.security.jwt.JWTFilter;
import vn.hust.easypos.security.jwt.TokenProvider;
import vn.hust.easypos.service.dto.ResultDTO;
import vn.hust.easypos.service.dto.authorities.AuthenticationDTO;
import vn.hust.easypos.service.dto.authorities.AuthoritiesResponse;
import vn.hust.easypos.service.impl.UserService;
import vn.hust.easypos.service.util.Common;
import vn.hust.easypos.web.rest.errors.ExceptionConstants;
import vn.hust.easypos.web.rest.errors.InternalServerException;
import vn.hust.easypos.web.rest.vm.LoginVM;

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

    public UserJWTController(
        TokenProvider tokenProvider,
        Validator customValidator,
        AuthenticationManagerBuilder authenticationManagerBuilder
    ) {
        this.tokenProvider = tokenProvider;
        this.customValidator = customValidator;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @PostMapping("/client/common/authenticate")
    public ResponseEntity<ResultDTO> authorize(@RequestBody LoginVM loginVM) {
        Common.validateInput(customValidator, ENTITY_NAME, loginVM);
        return new ResponseEntity<>(login(loginVM), HttpStatus.OK);
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
            authenticationDTO = userService.getAuthoritiesAndInfo(loginVM);
        } catch (Exception ex) {
            if (ex instanceof BadCredentialsException) {
                resultDTO = new ResultDTO(ExceptionConstants.BAD_CREDENTIALS, ExceptionConstants.BAD_CREDENTIALS_VI);
            } else {
                if (
                    ex instanceof InternalServerException ||
                    ex instanceof InternalAuthenticationServiceException ||
                    ex instanceof UserNameNotFoundExceptionCustom
                ) {
                    resultDTO = new ResultDTO(ex.getMessage(), ex.getMessage());
                } else {
                    resultDTO = new ResultDTO(ExceptionConstants.EXCEPTION_ERROR, ExceptionConstants.EXCEPTION_ERROR_VI);
                }
            }
            return resultDTO;
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = "";
        AuthoritiesResponse authoritiesResponseDTO = new AuthoritiesResponse();
        authoritiesResponseDTO.setActivate(authenticationDTO.isActivate());
        authoritiesResponseDTO.setCompanies(authenticationDTO.getCompanies());
        //        new JWTToken(jwt);
        authoritiesResponseDTO.setFullName(authenticationDTO.getFullName());
        authoritiesResponseDTO.setUserName(authenticationDTO.getUsername());
        authoritiesResponseDTO.setCompanyId(authenticationDTO.getCompanyId());
        authoritiesResponseDTO.setCompanyName(authenticationDTO.getCompanyName());
        authoritiesResponseDTO.setId(authenticationDTO.getId());
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
