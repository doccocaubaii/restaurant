package vn.softdreams.easypos.service.impl;

import com.google.common.base.Strings;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.easypos.config.Constants;
import vn.softdreams.easypos.constants.CommonConstants;
import vn.softdreams.easypos.constants.ResultConstants;
import vn.softdreams.easypos.constants.UserConstants;
import vn.softdreams.easypos.domain.CompanyUser;
import vn.softdreams.easypos.domain.Role;
import vn.softdreams.easypos.domain.User;
import vn.softdreams.easypos.domain.UserRole;
import vn.softdreams.easypos.dto.employee.EmployeeCreateRequest;
import vn.softdreams.easypos.dto.employee.EmployeeResponse;
import vn.softdreams.easypos.dto.employee.EmployeeUpdateRequest;
import vn.softdreams.easypos.dto.employee.GetRoleResponse;
import vn.softdreams.easypos.dto.product.DeleteProductList;
import vn.softdreams.easypos.repository.*;
import vn.softdreams.easypos.service.EmployerManagementService;
import vn.softdreams.easypos.service.UserService;
import vn.softdreams.easypos.service.dto.DataResponse;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.util.Common;
import vn.softdreams.easypos.web.rest.errors.BadRequestAlertException;
import vn.softdreams.easypos.web.rest.errors.InternalServerException;

import java.util.*;

import static vn.softdreams.easypos.constants.ResultConstants.*;
import static vn.softdreams.easypos.web.rest.errors.ExceptionConstants.*;

@Service
@Transactional
public class EmployerManagementServiceImpl implements EmployerManagementService {

    private static final String ENTITY_NAME = "EmployerManagementServiceImpl";
    private final Logger log = LoggerFactory.getLogger(EmployerManagementServiceImpl.class);

    private final UserService userService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleRepository userRoleRepository;
    private final CompanyUserRepository companyUserRepository;
    private final CompanyRepository companyRepository;
    private final EmailServiceImpl emailService;
    private final ModelMapper mapper;

    public EmployerManagementServiceImpl(
        UserService userService,
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        RoleRepository roleRepository,
        UserRoleRepository userRoleRepository,
        CompanyUserRepository companyUserRepository,
        CompanyRepository companyRepository,
        EmailServiceImpl emailService,
        ModelMapper mapper
    ) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.companyUserRepository = companyUserRepository;
        this.companyRepository = companyRepository;
        this.emailService = emailService;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public ResultDTO getAllEmployeeWithPaging(String keyword, Pageable pageable, Boolean isCountAll) {
        User user = userService.getUserWithAuthorities();
        Page<EmployeeResponse> employeeResponses = userRepository.findAllEmployeeByCompanyId(
            user.getCompanyId(),
            null,
            keyword,
            pageable,
            isCountAll,
            true,
            null
        );
        List<EmployeeResponse> employees = employeeResponses.getContent();
        return new ResultDTO(SUCCESS, GET_EMPLOYEES_SUCCESS_VI, true, employees, (int) employeeResponses.getTotalElements());
    }

    @Transactional(readOnly = true)
    public ResultDTO getEmployeeById(Integer id) {
        log.info(ENTITY_NAME + "_getEmployeeById: {}", id);
        User user = userService.getUserWithAuthorities();
        EmployeeResponse employeeResponse = userRepository.findOneByIdAndCompanyId(id, user.getCompanyId());
        return new ResultDTO(SUCCESS, GET_EMPLOYEE_DETAIL_SUCCESS_VI, true, employeeResponse);
    }

    public ResultDTO create(EmployeeCreateRequest request) {
        log.info(ENTITY_NAME + "_create: {}", request.getName());
        userService.getUserWithAuthorities(request.getComId());
        if (!Objects.equals(request.getUsername(), request.getPhoneNumber())) {
            throw new BadRequestAlertException(
                EMPLOYEE_USER_NAME_AND_PHONE_NOT_MATCH_VI,
                ENTITY_NAME,
                EMPLOYEE_USER_NAME_AND_PHONE_NOT_MATCH
            );
        }
        Common.checkEmail(request.getEmail());
        //Validate roleId & roleName from request
        validateRoleIdAndRoleName(request.getRoleId(), request.getRoleName());
        if (userRepository.countByUsername(request.getUsername()) > 0) {
            throw new BadRequestAlertException(USER_NAME_EXISTED_VI, ENTITY_NAME, USER_NAME_EXISTED_CODE);
        }
        User user = new User();
        BeanUtils.copyProperties(request, user);
        user.setFullName(request.getName());
        user.setStatus(UserConstants.Status.ACTIVATE);
        user.setManager(UserConstants.Manager.IS_MANAGER_FALSE);
        String password = RandomStringUtils.random(CommonConstants.REGISTER_PASSWORD_LENGTH, true, true);
        // send account to sms employee
        String content = Constants.SEND_MESSAGE_REGISTER_CONTENT + user.getUsername() + "/" + password;
        emailService.sendMessagePhoneNumber(request.getUsername(), "EasyPOS - Thông tin khởi tạo tài khoản", content);
        user.setPassword(passwordEncoder.encode(password));
        user.setNormalizedName(Common.normalizedName(Arrays.asList(user.getFullName())));
        Integer userId = userRepository.save(user).getId();
        companyUserRepository.save(new CompanyUser(request.getComId(), userId));
        userRoleRepository.save(new UserRole(userId, request.getRoleId(), request.getComId()));
        return new ResultDTO(SUCCESS, CREATE_EMPLOYEE_SUCCESS_VI, true);
    }

    private void validateRoleIdAndRoleName(Integer roleId, String roleName) {
        Optional<Role> roleOptional = roleRepository.findRoleById(roleId);
        if (roleOptional.isEmpty()) {
            throw new InternalServerException(ROLE_NOT_FOUND_VI, ENTITY_NAME, ROLE_NOT_FOUND);
        } else {
            if (!Strings.isNullOrEmpty(roleName) && !Objects.equals(roleOptional.get().getName(), roleName)) {
                throw new BadRequestAlertException(ROLE_NAME_INVALID_VI, ENTITY_NAME, ROLE_NAME_INVALID);
            }
        }
    }

    public ResultDTO update(EmployeeUpdateRequest request) {
        log.info(ENTITY_NAME + "_update: {}", request.getId());
        userService.getUserWithAuthorities(request.getComId());
        Optional<User> userOptional = userRepository.findUserByIdAndComId(request.getId(), request.getComId());
        if (userOptional.isEmpty()) {
            throw new InternalServerException(USER_NOT_FOUND_VI, ENTITY_NAME, USER_NOT_FOUND);
        }
        User user = userOptional.get();
        Optional<Role> roleOptional = roleRepository.findRoleById(request.getRoleId());
        if (request.getRoleId() != null) {
            if (roleOptional.isEmpty()) {
                throw new InternalServerException(ROLE_NOT_FOUND_VI, ENTITY_NAME, ROLE_NOT_FOUND);
            }
        }
        if (!Strings.isNullOrEmpty(request.getName())) {
            user.setFullName(request.getName());
        }
        if (request.getPhoneNumber() != null) {
            user.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getEmail() != null) {
            Common.checkEmail(request.getEmail());
            user.setEmail(request.getEmail());
        }

        if (roleOptional.isPresent()) {
            Optional<UserRole> userRoleOptional = userRoleRepository.findByUserIdAndComId(request.getId(), request.getComId());
            if (userRoleOptional.isPresent()) {
                UserRole userRole = userRoleOptional.get();
                userRole.setRoleId(request.getRoleId());
                userRoleRepository.save(userRole);
            } else {
                throw new BadRequestAlertException(USER_ROLE_NOT_EXISTS_VI, ENTITY_NAME, USER_ROLE_NOT_EXISTS_CODE);
            }
        }
        user.setNormalizedName(Common.normalizedName(Arrays.asList(user.getFullName())));
        userRepository.save(user);
        return new ResultDTO(SUCCESS, UPDATE_EMPLOYEE_SUCCESS_VI, true);
    }

    public ResultDTO delete(Integer id) {
        log.debug(ENTITY_NAME + "employer_delete: {}", id);

        User user = userService.getUserWithAuthorities();
        if (user.getId().equals(id)) {
            throw new BadRequestAlertException(DELETE_EMPLOYEE_ERROR_VI, ENTITY_NAME, DELETE_EMPLOYEE_ERROR);
        }
        //        Kiểm tra xem user có phải owner không
        //        Optional<User> employeeOptional = userRepository.findByCreator(user.getCreator());
        //        if (employeeOptional.isEmpty()) {
        //            throw new BadRequestAlertException(EMPLOYEE_DELETE_FAIL_VI, ENTITY_NAME, EMPLOYEE_DELETE_FAIL_CODE);
        //        }
        //        Optional<CompanyUser> companyUserOptional = companyUserRepository.findByCompanyIdAndUserId(
        //            user.getCompanyId(),
        //            employeeOptional.get().getId()
        //        );
        //        Kiểm tra xem có phải chủ cửa hàng hay không
        Integer count = companyRepository.countOwnerByComIdAndOwnerId(user.getCompanyId(), id);
        if (count > 0) {
            throw new InternalServerException(DELETE_OWNER_ERROR_VI, DELETE_OWNER_ERROR_VI, DELETE_OWNER_ERROR);
        }
        //        Kiểm tra xem nhân viên có thuộc công ty không
        Integer countCompanyUser = companyUserRepository.countByComIdAndUserId(user.getCompanyId(), id);
        if (countCompanyUser < 1) {
            throw new InternalServerException(EMPLOYEE_NOT_FOUND_VI, EMPLOYEE_NOT_FOUND_VI, EMPLOYEE_NOT_FOUND);
        }
        companyUserRepository.deleteByComIdAndUserId(user.getCompanyId(), id);
        return new ResultDTO(SUCCESS, DELETE_EMPLOYEE_SUCCESS_VI, true);
    }

    @Override
    @Transactional(readOnly = true)
    public ResultDTO getAllRoles(Integer comId) {
        log.info(ENTITY_NAME + "_getAllRoles: comId: {}", comId);
        //Kiem tra dang nhap
        userService.getUserWithAuthorities(comId);

        List<Role> roles = roleRepository.findAll();
        List<GetRoleResponse> getRoleResponses = new ArrayList<>();
        for (Role role : roles) {
            GetRoleResponse getRoleResponse = mapper.map(role, GetRoleResponse.class);
            getRoleResponse.setComId(comId);
            getRoleResponses.add(getRoleResponse);
        }
        return new ResultDTO(SUCCESS, GET_ROLES_SUCCESS_VI, true, getRoleResponses, getRoleResponses.size());
    }

    @Override
    public ResultDTO deleteList(DeleteProductList req) {
        log.debug(ENTITY_NAME + "employer_delete_list: ", req.toString());
        User user = userService.getUserWithAuthorities();
        List<Object> listError = new ArrayList<>();

        List<Integer> idDelete = new ArrayList<>();

        Integer idOwner = companyRepository.getOwnerByComId(user.getCompanyId());
        List<EmployeeResponse> employeeResponses = userRepository
            .findAllEmployeeByCompanyId(user.getCompanyId(), null, req.getKeyword(), null, false, req.getParamCheckAll(), req.getIds())
            .getContent();

        for (EmployeeResponse response : employeeResponses) {
            //        Kiểm tra xem có phải bản than
            if (user.getId().equals(response.getId())) {
                response.setNote(DELETE_EMPLOYEE_ERROR_VI);
                listError.add(response);
                continue;
            }
            //        Kiểm tra xem có phải chủ cửa hàng hay không
            if (idOwner != null && idOwner.equals(response.getId())) {
                response.setNote(DELETE_OWNER_ERROR_VI);
                listError.add(response);
                continue;
            }
            //        Kiểm tra xem nhân viên có thuộc công ty không
            if (!user.getCompanyId().equals(response.getComId())) {
                response.setNote(EMPLOYEE_NOT_FOUND_VI);
                listError.add(response);
                continue;
            }
            idDelete.add(response.getId());
        }
        companyUserRepository.deleteListByComIdAndUserId(user.getCompanyId(), idDelete);
        DataResponse response = new DataResponse();
        response.setCountAll(employeeResponses.size());
        response.setCountFalse(listError.size());
        response.setDataFalse(listError);
        response.setCountSuccess(response.getCountAll() - response.getCountFalse());
        return new ResultDTO(ResultConstants.SUCCESS, DELETE_EMPLOYEE_SUCCESS_VI, true, response);
        //        return new ResultDTO(SUCCESS, DELETE_EMPLOYEE_SUCCESS_VI, true);
    }
}
