package vn.softdreams.easypos.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Strings;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import tech.jhipster.security.RandomUtil;
import vn.softdreams.easypos.config.Constants;
import vn.softdreams.easypos.constants.*;
import vn.softdreams.easypos.domain.*;
import vn.softdreams.easypos.dto.TaskLogSendQueue;
import vn.softdreams.easypos.dto.TaskLogSendQueueUser;
import vn.softdreams.easypos.dto.authorities.AuthenticationDTO;
import vn.softdreams.easypos.dto.authorities.AuthoritiesResponse;
import vn.softdreams.easypos.dto.authorities.JwtDTO;
import vn.softdreams.easypos.dto.authorities.UserRequest;
import vn.softdreams.easypos.dto.company.CompanyResult;
import vn.softdreams.easypos.dto.invoice.TaskCheckInvoice;
import vn.softdreams.easypos.dto.user.*;
import vn.softdreams.easypos.integration.TaskLogIdEnqueueMessage;
import vn.softdreams.easypos.integration.easybooks88.api.EB88ApiClient;
import vn.softdreams.easypos.integration.easybooks88.api.dto.RegisterCompanyRequest;
import vn.softdreams.easypos.integration.easybooks88.api.dto.RegisterCompanyResponse;
import vn.softdreams.easypos.integration.easybooks88.queue.EB88Producer;
import vn.softdreams.easypos.integration.easybooks88.queue.dto.ChangePasswordTask;
import vn.softdreams.easypos.integration.easybooks88.queue.dto.ForgotPasswordTask;
import vn.softdreams.easypos.integration.easybooks88.queue.dto.RegisterUserTask;
import vn.softdreams.easypos.integration.easyinvoice.api.EasyInvoiceApiClient;
import vn.softdreams.easypos.integration.easyinvoice.queue.EasyInvoiceProducer;
import vn.softdreams.easypos.integration.easyinvoice.queue.NgoGiaPhatInvoiceProducer;
import vn.softdreams.easypos.repository.*;
import vn.softdreams.easypos.security.AuthoritiesConstants;
import vn.softdreams.easypos.security.SecurityUtils;
import vn.softdreams.easypos.security.jwt.TokenProvider;
import vn.softdreams.easypos.service.dto.AdminUserDTO;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.service.dto.UserDTO;
import vn.softdreams.easypos.service.impl.EmailServiceImpl;
import vn.softdreams.easypos.util.Common;
import vn.softdreams.easypos.util.Util;
import vn.softdreams.easypos.web.rest.errors.*;
import vn.softdreams.easypos.web.rest.vm.LoginVM;

import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static vn.softdreams.easypos.web.rest.errors.ExceptionConstants.*;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private static final String ENTITY_NAME = "user";
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(Constants.ZONED_DATE_FORMAT);
    private final UserRepository userRepository;
    private final TaxAuthorityCodeRepository taxAuthorityCodeRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;

    private final CompanyRepository companyRepository;
    private final OwnerDeviceRepository ownerDeviceRepository;
    private final ConfigRepository configRepository;
    private final PrintConfigRepository printConfigRepository;
    private final ProductUnitRepository productUnitRepository;
    private final CompanyUserRepository companyUserRepository;
    private final CompanyOwnerRepository companyOwnerRepository;
    private final AreaRepository areaRepository;
    private final EPPackageRepository epPackageRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final EasyInvoiceApiClient easyInvoiceApiClient;
    private final OwnerPackageRepository ownerPackageRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final EmailServiceImpl emailService;
    private final EpMessageRepository epMessageRepository;
    private final EasyInvoiceProducer easyInvoiceProducer;
    private final EB88Producer eb88Producer;
    private final TransactionTemplate transactionTemplate;
    private final TaskLogRepository taskLogRepository;
    private final BusinessTypeRepository businessTypeRepository;
    private final ProductProductUnitRepository productProductUnitRepository;
    private final PrintTemplateRepository printTemplateRepository;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final NgoGiaPhatInvoiceProducer ngoGiaPhatInvoiceProducer;
    private final OtpRepository otpRepository;

    public UserService(
        UserRepository userRepository,
        TaxAuthorityCodeRepository taxAuthorityCodeRepository,
        PasswordEncoder passwordEncoder,
        AuthorityRepository authorityRepository,
        CompanyRepository companyRepository,
        OwnerDeviceRepository ownerDeviceRepository,
        ConfigRepository configRepository,
        PrintConfigRepository printConfigRepository,
        ProductUnitRepository productUnitRepository,
        CompanyUserRepository companyUserRepository,
        CompanyOwnerRepository companyOwnerRepository,
        AreaRepository areaRepository,
        EPPackageRepository epPackageRepository,
        UserRoleRepository userRoleRepository,
        RoleRepository roleRepository,
        EasyInvoiceApiClient easyInvoiceApiClient,
        OwnerPackageRepository ownerPackageRepository,
        CustomerRepository customerRepository,
        ProductRepository productRepository,
        ModelMapper modelMapper,
        EmailServiceImpl emailService,
        EpMessageRepository epMessageRepository,
        EasyInvoiceProducer easyInvoiceProducer,
        EB88Producer eb88Producer,
        TransactionTemplate transactionTemplate,
        TaskLogRepository taskLogRepository,
        BusinessTypeRepository businessTypeRepository,
        ProductProductUnitRepository productProductUnitRepository,
        PrintTemplateRepository printTemplateRepository,
        TokenProvider tokenProvider,
        AuthenticationManagerBuilder authenticationManagerBuilder,
        NgoGiaPhatInvoiceProducer ngoGiaPhatInvoiceProducer,
        OtpRepository otpRepository
    ) {
        this.userRepository = userRepository;
        this.taxAuthorityCodeRepository = taxAuthorityCodeRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.companyRepository = companyRepository;
        this.ownerDeviceRepository = ownerDeviceRepository;
        this.configRepository = configRepository;
        this.printConfigRepository = printConfigRepository;
        this.productUnitRepository = productUnitRepository;
        this.companyUserRepository = companyUserRepository;
        this.companyOwnerRepository = companyOwnerRepository;
        this.areaRepository = areaRepository;
        this.epPackageRepository = epPackageRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
        this.easyInvoiceApiClient = easyInvoiceApiClient;
        this.ownerPackageRepository = ownerPackageRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.emailService = emailService;
        this.epMessageRepository = epMessageRepository;
        this.easyInvoiceProducer = easyInvoiceProducer;
        this.eb88Producer = eb88Producer;
        this.transactionTemplate = transactionTemplate;
        this.taskLogRepository = taskLogRepository;
        this.businessTypeRepository = businessTypeRepository;
        this.productProductUnitRepository = productProductUnitRepository;
        this.printTemplateRepository = printTemplateRepository;
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.otpRepository = otpRepository;
        this.ngoGiaPhatInvoiceProducer = ngoGiaPhatInvoiceProducer;
    }

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        //        return userRepository
        //            .findOneByActivationKey(key)
        //            .map(user -> {
        //                // activate given user for the registration key.
        ////                user.setStatus(true);
        //                log.debug("Activated user: {}", user);
        //                return user;
        //            });
        return null;
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);
        //        return userRepository
        //            .findOneByResetKey(key)
        //             .map(user -> {
        //                user.setPassword(passwordEncoder.encode(newPassword));
        //                return user;
        //            });
        return null;
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository
            .findOneByEmailIgnoreCase(mail)
            .map(user -> {
                return user;
            });
    }

    public ResultDTO registerSendOtp(RegisterSendOtpRequest request) {
        String phoneNumber = request.getPhoneNumber();
        String email = request.getEmail();
        String taxCode = request.getCompanyTaxCode();
        String userName = "";
        boolean sendMail = false;
        if (!Strings.isNullOrEmpty(taxCode)) {
            if (!taxCode.matches(RegexConstants.CUSTOMER_TAX_CODE_REGEX)) {
                throw new InternalServerException(
                    ExceptionConstants.TAX_AUTHORITY_CODE_INVALID_VI,
                    ENTITY_NAME,
                    ExceptionConstants.TAX_AUTHORITY_CODE_INVALID
                );
            }
            if (companyOwnerRepository.countByTaxCode(taxCode) > 0) {
                throw new InternalServerException(
                    ExceptionConstants.TAX_AUTHORITY_CODE_ALREADY_VI,
                    ENTITY_NAME,
                    ExceptionConstants.TAX_AUTHORITY_CODE_ALREADY
                );
            }
        }
        if (!phoneNumber.matches(RegexConstants.PHONE_NUMBER_REGEX)) {
            throw new BadRequestAlertException(CUSTOMER_PHONE_INVALID_VI, ENTITY_NAME, PHONE_NUMBER_INVALID);
        }
        if (phoneNumber.matches(RegexConstants.PHONE_NUMBER_MOBI_PONE_REGEX)) {
            if (Strings.isNullOrEmpty(email)) {
                throw new BadRequestAlertException(EMAIL_NULL_WITH_MOBI_PONE_VI, ENTITY_NAME, EMAIL_NULL_WITH_MOBI_PONE);
            } else if (!email.matches(RegexConstants.EMAIL_REGEX) || email.contains("..")) {
                throw new BadRequestAlertException(CUSTOMER_EMAIL_INVALID_VI, ENTITY_NAME, CUSTOMER_EMAIL_INVALID);
            }
            sendMail = true;
        }
        String otpCode = RandomStringUtils.random(CommonConstants.REGISTER_SEND_OTP_LENGTH, false, true);

        List<Otp> saves = new ArrayList<>();
        Otp otp = new Otp();
        otp.setOtp(otpCode);
        userName = sendMail ? email : phoneNumber;
        Long countUserName = userRepository.countByUsername(userName);
        if (countUserName > 0) {
            throw new BadRequestAlertException(
                String.format(USER_NAME_TRIAL_DUPLICATE_VI, sendMail ? "Email" : "Số điện thoại"),
                ENTITY_NAME,
                USER_NAME_TRIAL_DUPLICATE
            );
        }
        Integer count = otpRepository.countNumberRequest(userName);
        if (count >= CommonConstants.MAX_TIME_REQUEST_FORGOT_PASS) {
            throw new BadRequestAlertException(
                ExceptionConstants.MAX_TIME_REQUEST_FORGOT_VI,
                ENTITY_NAME,
                ExceptionConstants.MAX_TIME_REQUEST_FORGOT
            );
        }
        // xoá otp cũ
        Optional<Otp> otpOldOptional = otpRepository.getLastUserOtp(userName, OTPConstants.Type.REGISTER);
        if (otpOldOptional.isPresent()) {
            Otp oldOtp = otpOldOptional.get();
            oldOtp.setStatus(OTPConstants.Status.USED);
            saves.add(oldOtp);
        }
        otp.setUsername(userName);
        otp.setType(OTPConstants.Type.REGISTER);
        otp.setStatus(OTPConstants.Status.DEFAULT);
        otp.setExpiredTime(ZonedDateTime.now());
        saves.add(otp);
        otpRepository.saveAll(saves);
        //        int delayInMillis = 5000; // Độ trễ 5 giây (5000 mili giây)
        //
        //        try {
        //            Thread.sleep(delayInMillis);
        //            // Thực hiện hành động sau khi đã chờ trong 5 giây
        //            System.out.println("Hành động sau đợi 5 giây.");
        //        } catch (InterruptedException e) {
        //            e.printStackTrace();
        //        }
        if (sendMail) {
            emailService.sendOTPRegister(email, otpCode);
        } else {
            String content = String.format(Constants.SEND_OTP_REGISTER, otpCode);
            emailService.sendMessagePhoneNumber(phoneNumber, Constants.SEND_MESSAGE_REGISTER_SUBJECT, content);
        }
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_SEND_OTP_REGISTER, true);
    }

    public ResultDTO registerCheckOtp(RegisterCheckOtpRequest request) {
        if (request.getOtp().length() != CommonConstants.REGISTER_SEND_OTP_LENGTH) {
            throw new BadRequestAlertException(
                ExceptionConstants.REGISTER_OTP_INVALID_VI,
                ENTITY_NAME,
                ExceptionConstants.REGISTER_OTP_INVALID
            );
        }
        Optional<Otp> otpOptional = otpRepository.findByUsernameAndOtpAndType(
            request.getUsername(),
            request.getOtp(),
            OTPConstants.Type.REGISTER
        );
        if (otpOptional.isEmpty()) {
            throw new BadRequestAlertException(
                ExceptionConstants.REGISTER_OTP_INCORRECT_VI,
                ENTITY_NAME,
                ExceptionConstants.REGISTER_OTP_INVALID
            );
        }
        Otp otp = otpOptional.get();
        Duration duration = Duration.between(otp.getExpiredTime(), ZonedDateTime.now());
        long seconds = duration.getSeconds();
        if (seconds > CommonConstants.TIME_OTP_AVAILABLE_SECOND || otp.getStatus().equals(OTPConstants.Status.USED)) {
            throw new BadRequestAlertException(
                ExceptionConstants.REGISTER_OTP_INCORRECT_VI,
                ENTITY_NAME,
                ExceptionConstants.REGISTER_OTP_INVALID
            );
        }
        otpRepository.save(otp);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.REGISTER_CHECK_OTP_VALID, true);
    }

    public ResultDTO registerUser(RegisterRequest request, EB88ApiClient eb88ApiClient) throws Exception {
        // log info request
        log.info("user_registerUser_value:" + request.toString());
        Integer comIdReq = request.getComId();
        Integer packageId = request.getPackageId();
        if (packageId != null) {
            Optional<EPPackage> packageOptional = epPackageRepository.findByIdAndStatus(packageId, PackageConstants.Status.ACTIVE);
            if (packageOptional.isEmpty()) {
                throw new InternalServerException(PACKAGE_CODE_NOT_FOUND_VI, ENTITY_NAME, PACKAGE_CODE_NOT_FOUND_CODE);
            }
        } else {
            Optional<EPPackage> packageOptional = epPackageRepository.findByStatusAndType(
                PackageConstants.Status.ACTIVE,
                PackageConstants.Type.TRIAL
            );
            if (packageOptional.isEmpty()) {
                throw new InternalServerException(PACKAGE_DEFAULT_NOT_FOUND_VI, ENTITY_NAME, PACKAGE_DEFAULT_NOT_FOUND_CODE);
            }
            packageId = packageOptional.get().getId();
        }

        ZonedDateTime startDate = Common.convertStringToDateTime(request.getStartDate(), Constants.ZONED_DATE_FORMAT);
        ZonedDateTime endDate = Common.convertStringToDateTime(request.getEndDate(), Constants.ZONED_DATE_FORMAT);
        if (startDate != null && endDate != null) {
            Common.checkStartAndEndDate(startDate, endDate);
        }

        Company company;
        CompanyOwner companyOwner;
        OwnerPackage ownerPackage = new OwnerPackage();
        CompanyUser companyUser = new CompanyUser();
        String taxCodeReq = request.getCompanyTaxCode();
        Integer userIdOld = null;
        if (comIdReq != null) {
            Optional<Company> companyOptional = companyRepository.findById(comIdReq);
            if (companyOptional.isEmpty()) {
                throw new BadRequestAlertException(COMPANY_NOT_EXISTS_CODE_VI, ENTITY_NAME, COMPANY_NOT_EXISTS_CODE);
            }
            company = companyOptional.get();
            if (!company.getParent()) {
                throw new BadRequestAlertException(REGISTER_UPDATE_COMPANY_INVALID_VI, ENTITY_NAME, REGISTER_UPDATE_COMPANY_INVALID);
            }
            companyOwner = company.getCompanyOwner();
            Optional<OwnerPackage> ownerPackageOptional = ownerPackageRepository.findByOwnerId(companyOwner.getId());
            if (ownerPackageOptional.isPresent()) {
                ownerPackage = ownerPackageOptional.get();
            }
            if (!companyOwner.getTaxCode().equalsIgnoreCase(taxCodeReq) && companyOwnerRepository.countByTaxCode(taxCodeReq) > 0) {
                throw new InternalServerException(TAX_AUTHORITY_CODE_ALREADY_VI, ENTITY_NAME, TAX_AUTHORITY_CODE_ALREADY);
            }
            Optional<CompanyUser> companyUserOptional = companyUserRepository.findOneByCompanyIdAndUserId(
                comIdReq,
                companyOwner.getOwnerId()
            );
            if (companyUserOptional.isPresent()) {
                companyUser = companyUserOptional.get();
                userIdOld = companyUser.getUserId();
            }
        } else {
            companyOwner = new CompanyOwner();
            company = new Company();
            ownerPackage = new OwnerPackage();
            if (Strings.isNullOrEmpty(request.getHashCode())) {
                throw new BadRequestAlertException(HASH_CODE_NOT_NULL_VI, ENTITY_NAME, HASH_CODE_NOT_NULL_CODE);
            }
            String hashMD5 = Util.createMd5(request.getCompanyTaxCode() + request.getUsername() + Constants.HASH_EASY_POS_MD5);
            if (!Objects.equals(hashMD5, request.getHashCode())) {
                throw new BadRequestAlertException(HASH_CODE_INVALID_VI, ENTITY_NAME, HASH_CODE_INVALID);
            }

            if (!Strings.isNullOrEmpty(taxCodeReq) && companyOwnerRepository.countByTaxCode(taxCodeReq) > 0) {
                throw new InternalServerException(TAX_AUTHORITY_CODE_ALREADY_VI, ENTITY_NAME, TAX_AUTHORITY_CODE_ALREADY);
            }
        }
        Optional<User> userOptional = userRepository.findByUsername(request.getUsername());
        // task log send Queue
        //        TaskLogSendQueueUser sendQueue = transactionTemplate.execute(status -> {
        User user;
        String password = null;
        //            try {
        if (userOptional.isEmpty()) {
            user = new User();
            user.setStatus(UserConstants.Status.ACTIVATE);
            user.setManager(UserConstants.Manager.IS_MANAGER_FALSE);
            // random password
            //            generatePassword = RandomStringUtils.random(CommonConstants.REGISTER_PASSWORD_LENGTH, false, true);
            password = "Epos@123";
            log.error("createUser :" + user.getUsername() + "pass:" + password);
            user.setPassword(passwordEncoder.encode(password));
            user.setPasswordVersion(0);
            user.setNormalizedName(Common.normalizedName(List.of(request.getFullName())));
        } else {
            user = userOptional.get();
            List<Integer> comOwnerIdsOld = companyUserRepository.getCompanyOwnerIdByUserId(user.getId());
            if (companyOwner.getId() != null && !comOwnerIdsOld.contains(companyOwner.getId())) {
                throw new BadRequestAlertException(USER_NAME_DUPLICATE_VI, ENTITY_NAME, USER_NAME_DUPLICATE);
            }
        }

        if (userOptional.isEmpty() || request.getComId() != null) {
            user.setUsername(request.getUsername());
            user.setFullName(request.getFullName());
            user.setPhoneNumber(request.getPhoneNumber());
            user.setEmail(request.getEmail());
            user.setNormalizedName(Common.normalizedName(List.of(user.getFullName())));
            user = userRepository.save(user);
        }

        // nếu là api cập nhật, user cũ khác user mới thì update lại vào bảng user_role
        if (comIdReq != null && !Objects.equals(user.getId(), companyUser.getUserId())) {
            Optional<UserRole> userRoleOptional = userRoleRepository.findByUserIdAndComId(companyUser.getUserId(), comIdReq);
            if (userRoleOptional.isPresent()) {
                UserRole userRole = userRoleOptional.get();
                userRole.setUserId(user.getId());
                userRoleRepository.save(userRole);
            }
        }

        companyOwner.setName(request.getCompanyName());
        companyOwner.setAddress(request.getCompanyAddress());
        companyOwner.setOwnerName(request.getFullName());
        companyOwner.setOwnerId(user.getId());
        companyOwner.setTaxCode(request.getCompanyTaxCode());
        Integer ownerId = companyOwnerRepository.save(companyOwner).getId();

        company.setCompanyOwner(companyOwner);
        company.setName(request.getCompanyName());
        company.setAddress(request.getCompanyAddress());
        company.setParent(true);
        company.setNormalizedName(Common.normalizedName(List.of(request.getCompanyName())));
        Integer companyId = companyRepository.save(company).getId();
        user.setCompanyId(companyId);

        ownerPackage.setPackageId(packageId);
        ownerPackage.setOwnedId(ownerId);
        ownerPackage.setStatus(OwnerPackageConstants.Status.DEFAULT);
        ownerPackage.setStartDate(startDate);
        ownerPackage.setEndDate(endDate);
        ownerPackage.setPackCount(request.getPackCount());
        ownerPackageRepository.save(ownerPackage);

        companyUser.setCompanyId(companyId);
        companyUser.setUserId(user.getId());
        companyUserRepository.save(companyUser);
        if (comIdReq == null) {
            // TH: userName not duplicate
            if (password != null) {
                //        kiểm tra nếu là mail thì gửi mail, nếu là sms thì gửi sms
                if (user.getUsername().matches(Constants.PATTERN_MAIL)) {
                    emailService.sendAccountCredentials(user.getUsername(), user.getUsername(), password);
                } else {
                    String content = Constants.SEND_MESSAGE_REGISTER_CONTENT + user.getUsername() + "/" + password;
                    emailService.sendMessagePhoneNumber(user.getUsername(), Constants.SEND_MESSAGE_REGISTER_SUBJECT, content);
                }
            }
            // TH: userName duplicated
            else {
                emailService.sendMessagePhoneNumber(
                    user.getUsername(),
                    Constants.SEND_MESSAGE_REGISTER_SUBJECT,
                    Constants.SEND_MESSAGE_REGISTER_COMPANY_CONTENT
                );
            }
        }

        //                if (password != null) {
        //                    return new TaskLogSendQueueUser(
        //                        createAndPublishQueueTask(companyId, user.getId(), password, TaskLogConstants.Type.EB_CREATE_ACCOUNT),
        //                        user,
        //                        ownerId
        //                    );
        //                }
        //                return new TaskLogSendQueueUser(null, user, ownerId);
        //            } catch (Exception e) {
        //                log.error("Can not create queue task for eb88 creating account/companyOwner : {}", e.getMessage());
        //            }
        //            return null;
        //        });
        //        if (sendQueue != null && sendQueue.getTaskLogSendQueue() != null) {
        //            sendTaskLog(sendQueue.getTaskLogSendQueue());
        //        }

        // xoá user cũ nếu ko gắn cho công ty nào cả
        if (userIdOld != null && companyUserRepository.countAllByUserId(userIdOld) < 1) {
            log.debug("Delete user old with id= {}", userIdOld);
            Optional<UserRole> userRoleOptional = userRoleRepository.findByUserId(userIdOld);
            userRoleOptional.ifPresent(userRoleRepository::delete);
            userRepository.deleteById(userIdOld);
        }
        // xoá otp cũ
        Optional<Otp> otpOldOptional = otpRepository.getLastUserOtp(request.getUsername(), OTPConstants.Type.REGISTER);
        if (otpOldOptional.isPresent()) {
            Otp oldOtp = otpOldOptional.get();
            oldOtp.setStatus(OTPConstants.Status.USED);
            otpRepository.save(oldOtp);
        }
        return new ResultDTO(
            ResultConstants.SUCCESS,
            ResultConstants.SUCCESS_REGISTER_CREATE,
            true,
            new TaskLogSendQueueUser(null, user, companyId)
        );
    }

    private void insertToConfigFromEB(Integer companyId, RegisterCompanyResponse ebCompanyResponse, Map<String, Integer> configMap) {
        List<Config> configs = new ArrayList<>();
        configs.add(
            new Config(
                companyId,
                EasyInvoiceConstants.EB88_COM_ID,
                ebCompanyResponse.getData().getEbCompanyId() != null ? ebCompanyResponse.getData().getEbCompanyId().toString() : null,
                EasyInvoiceConstants.EB88_COM_ID_VI
            )
        );
        configs.add(
            new Config(
                companyId,
                EasyInvoiceConstants.EB88_DEFAULT_USER,
                ebCompanyResponse.getData().getEbUsername(),
                EasyInvoiceConstants.EB88_DEFAULT_USER_VI
            )
        );
        configs.add(
            new Config(
                companyId,
                EasyInvoiceConstants.EB88_REPOSITORY_ID,
                ebCompanyResponse.getData().getEbRepositoryId() != null ? ebCompanyResponse.getData().getEbRepositoryId().toString() : null,
                EasyInvoiceConstants.EB88_REPOSITORY_ID_VI
            )
        );
        if (configMap != null) {
            for (Config config : configs) {
                if (configMap.containsKey(config.getCode())) {
                    config.setId(configMap.get(config.getCode()));
                }
            }
        }
        configRepository.saveAll(configs);
    }

    private RegisterCompanyResponse createUserEB88(RegisterRequest request, String email, EB88ApiClient eb88ApiClient, String password)
        throws Exception {
        RegisterCompanyRequest registerCompanyRequest = modelMapper.map(request, RegisterCompanyRequest.class);
        registerCompanyRequest.setUserName(request.getUsername());
        String hashMD5 = Util.createMd5(
            registerCompanyRequest.getCompanyTaxCode() +
            registerCompanyRequest.getStartDate() +
            registerCompanyRequest.getEndDate() +
            EasyInvoiceConstants.KEY_HASH_MD5
        );
        registerCompanyRequest.setHash(hashMD5);
        registerCompanyRequest.setType(0);
        registerCompanyRequest.setPassword(password);
        RegisterCompanyResponse response = null;
        registerCompanyRequest.setServicePackage(registerCompanyRequest.getServicePackage());
        try {
            response = eb88ApiClient.registerCompany(registerCompanyRequest);
        } catch (BadRequestAlertException exception) {
            if (exception.getErrorKey().equalsIgnoreCase("EB88_ERROR_TAX_CODE")) {
                registerCompanyRequest.setCompanyTaxCode(registerCompanyRequest.getCompanyTaxCode() + "_pos");
                registerCompanyRequest.setHash(
                    Util.createMd5(
                        registerCompanyRequest.getCompanyTaxCode() +
                        registerCompanyRequest.getStartDate() +
                        registerCompanyRequest.getEndDate() +
                        EasyInvoiceConstants.KEY_HASH_MD5
                    )
                );
                response = eb88ApiClient.registerCompany(registerCompanyRequest);
            }
        }
        return response;
    }

    private TaskLogSendQueue createAndPublishQueueTask(Integer comId, Integer userId, String password, String taskType)
        throws JsonProcessingException {
        RegisterUserTask task = new RegisterUserTask();
        task.setComId(comId);
        task.setUserId(userId);
        task.setPassword(password);

        TaskLog taskLog = new TaskLog();
        taskLog.setComId(comId);
        taskLog.setStatus(TaskLogConstants.Status.PROCESSING);
        //New objectMapper because we need disable a feature
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.INDENT_OUTPUT);
        taskLog.setContent(objectMapper.writeValueAsString(task));
        taskLog.setType(taskType);
        taskLog = taskLogRepository.save(taskLog);
        return new TaskLogSendQueue(taskLog.getId().toString(), taskLog.getType());
    }

    private void createPrintConfigDefault(User user, Integer comId) {
        List<PrintConfig> printConfigs = Common.createPrintConfig(user, comId, null);
        printConfigs.forEach(printConfig -> {
            if (Objects.equals(printConfig.getCode(), "StoreName")) {
                printConfig.setName(companyRepository.getNameById(user.getCompanyId()));
            }
        });
        printConfigRepository.saveAll(printConfigs);
    }

    private void createPrintTemplateDefault(Integer comId) {
        List<PrintTemplate> printTemplates = printTemplateRepository.getAllDefaultTemplate();
        List<PrintTemplate> result = Common.createPrintTemplateDefault(printTemplates, comId);
        printTemplateRepository.saveAll(result);
    }

    private void createBusinessTypeDefault(Integer companyId) {
        businessTypeRepository.saveAll(Common.createBusinessTypeDefault(companyId));
    }

    private void createUnitDefault(Integer companyId) {
        TaskLogSendQueue taskLogSendQueue = transactionTemplate.execute(status -> {
            productUnitRepository.saveAll(Common.createUnitDefault(companyId));
            TaskLog taskLog = new TaskLog();
            taskLog.setComId(companyId);
            taskLog.setStatus(TaskLogConstants.Status.PROCESSING);
            taskLog.setContent(companyId.toString());
            taskLog.setType(TaskLogConstants.Type.EB_ASYNC_PRODUCT_UNIT);
            taskLogRepository.save(taskLog);
            return new TaskLogSendQueue(taskLog.getId().toString(), taskLog.getType());
        });
        sendTaskLog(taskLogSendQueue);
    }

    private void createConfigDefault(Integer companyId) {
        ObjectMapper objectMapper = new ObjectMapper();
        Config[] data = null;
        try {
            data = objectMapper.readValue(Constants.CONFIG_VALUE_JSON, Config[].class);
            List<Config> configs = new ArrayList<>();
            for (Config config : data) {
                config.setCompanyId(companyId);
                configs.add(config);
            }
            Config config = new Config();
            config.setCompanyId(companyId);
            config.setCode(EasyInvoiceConstants.DISPLAY_CONFIG);
            // Đặt config mặc định theo com_id = 1
            Optional<String> valueOptional = configRepository.getValueByComIdAndCode(1, EasyInvoiceConstants.DISPLAY_CONFIG);
            config.setValue(valueOptional.orElse(""));
            config.setDescription(Constants.DISPLAY_CONFIG_DESCRIPTION);
            configs.add(config);
            configRepository.saveAll(configs);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new InternalServerException(
                ExceptionConstants.CONFIG_SAVE_DEFAULT_ERROR_VI,
                ENTITY_NAME,
                ExceptionConstants.CONFIG_SAVE_DEFAULT_ERROR_CODE
            );
        }
    }

    private void createRoleDefault(User user) {
        UserRole userRole = new UserRole();
        Optional<Role> roleOptional = roleRepository.findByCode(RoleConstants.ROLE_DEFAULT);
        if (roleOptional.isEmpty()) {
            throw new InternalServerException(
                ExceptionConstants.ROLE_DEFAULT_NOT_FOUND_VI,
                ENTITY_NAME,
                ExceptionConstants.ROLE_NOT_FOUND_CODE
            );
        }
        userRole.setRoleId(roleOptional.get().getId());
        userRole.setUserId(user.getId());
        userRole.setComId(user.getCompanyId());
        userRoleRepository.save(userRole);
    }

    private void createAreaDefault(Integer companyId) {
        areaRepository.save(Common.createAreaDefault(companyId));
    }

    private void createCustomerDefault(Integer companyId) {
        Customer customer = Common.createCustomerDefault(companyId);
        customer.setCode(genCode(companyId, Constants.CUSTOMER_CODE));
        customerRepository.save(customer);
    }

    private void createProductDefault(Integer companyId) {
        List<Product> products = new ArrayList<>();
        List<ProductProductUnit> productUnits = new ArrayList<>();
        Product product = Common.createProductDefault(companyId, CommonConstants.PRODUCT_NAME_CREATE_DEFAULT);
        product.setCode(genCode(companyId, Constants.PRODUCT_CODE));
        products.add(product);

        Product productNote = Common.createProductDefault(companyId, CommonConstants.PRODUCT_NAME_NOTE_CREATE_DEFAULT);
        productNote.setCode(Constants.PRODUCT_CODE_NOTE_DEFAULT);
        products.add(productNote);

        Product productPromotion = Common.createProductDefault(companyId, CommonConstants.PRODUCT_NAME_PROMOTION_CREATE_DEFAULT);
        productPromotion.setCode(Constants.PRODUCT_CODE_PROMOTION_DEFAULT);
        products.add(productPromotion);

        productRepository.saveAll(products);
        for (Product p : products) {
            ProductProductUnit productProductUnit = Common.createProductProductUnitDefault(companyId, p.getId());
            productUnits.add(productProductUnit);
        }
        productProductUnitRepository.saveAll(productUnits);
    }

    private boolean removeNonActivatedUser(User existingUser) {
        if (existingUser.getStatus() == UserConstants.Status.ACTIVATE) {
            return false;
        }
        userRepository.delete(existingUser);
        userRepository.flush();
        return true;
    }

    public User createUser(AdminUserDTO userDTO) {
        User user = new User();
        user.setFullName(userDTO.getFullName());
        user.setUsername(userDTO.getUserName());
        user.setManager(userDTO.isManager());
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail().toLowerCase());
        }
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setPassword(encryptedPassword);
        user.setStatus(UserConstants.Status.ACTIVATE);
        if (userDTO.getAuthorities() != null) {
            //            Set<Authority> authorities = userDTO
            //                .getAuthorities()
            //                .stream()
            //                .map(authorityRepository::findById)
            //                .filter(Optional::isPresent)
            //                .map(Optional::get)
            //                .collect(Collectors.toSet());
            //            user.setAuthorities(authorities);
        }
        user.setNormalizedName(Common.normalizedName(Arrays.asList(user.getFullName())));
        userRepository.save(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update.
     * @return updated user.
     */
    public Optional<AdminUserDTO> updateUser(AdminUserDTO userDTO) {
        return Optional
            .of(userRepository.findById(userDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(user -> {
                user.setUsername(userDTO.getUserName().toLowerCase());
                user.setFullName(userDTO.getFullName());
                if (userDTO.getEmail() != null) {
                    user.setEmail(userDTO.getEmail().toLowerCase());
                }
                user.setPhoneNumber(userDTO.getPhoneNumber());
                user.setAddress(userDTO.getAddress());
                //                Set<String> managedAuthorities = user.getAuthorities();
                //                managedAuthorities.clear();
                //                userDTO
                //                    .getAuthorities()
                //                    .stream()
                //                    .map(authorityRepository::findById)
                //                    .filter(Optional::isPresent)
                //                    .map(Optional::get)
                //                    .forEach(managedAuthorities::add);
                log.debug("Changed Information for User: {}", user);
                return user;
            })
            .map(AdminUserDTO::new);
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param firstName first name of user.
     */
    public void updateUser(String firstName, String email) {
        SecurityUtils
            .getCurrentUserLogin()
            .flatMap(userRepository::findOneByUsernameActive)
            .ifPresent(user -> {
                user.setFullName(firstName);

                if (email != null) {
                    user.setEmail(email.toLowerCase());
                }
                log.debug("Changed Information for User: {}", user);
            });
    }

    @Transactional
    public void changePassword(String currentClearTextPassword, String newPassword) {
        SecurityUtils
            .getCurrentUserLogin()
            .flatMap(userRepository::findOneByUsernameActive)
            .ifPresent(user -> {
                String currentEncryptedPassword = user.getPassword();
                if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                    throw new InvalidPasswordException();
                }
                String encryptedPassword = passwordEncoder.encode(newPassword);
                user.setPassword(encryptedPassword);
                log.debug("Changed password for User: {}", user);
            });
    }

    @Transactional(readOnly = true)
    public Page<AdminUserDTO> getAllManagedUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(AdminUserDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllPublicUsers(Pageable pageable) {
        return userRepository.findAllByIdNotNull(pageable).map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneWithAuthoritiesByUsername(login);
    }

    @Transactional(readOnly = true)
    public User getUserWithAuthorities() {
        Optional<User> userOptional = SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByUsernameActive);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            JwtDTO jwtDTO = getInfoJwt();
            if (jwtDTO == null || !Objects.equals(jwtDTO.getPasswordVersion(), user.getPasswordVersion())) {
                throw new BadRequestAlertException(ExceptionConstants.TOKEN_EXPIRED_VI, ENTITY_NAME, ExceptionConstants.TOKEN_EXPIRED);
            }
            user.setCompanyId(jwtDTO.getCompanyId());
            user.setTaxCode(jwtDTO.getTaxCode());
            Set<String> authorities = new HashSet<>();
            authorities = authorityRepository.findAllAuthorityByUserIDAndCompanyId(user.getId(), jwtDTO.getCompanyId());
            user.setAuthorities(authorities);
            return user;
        }
        throw new InternalServerException(
            ExceptionConstants.COMPANY_NOT_EXISTS_CODE_VI,
            ENTITY_NAME,
            ExceptionConstants.COMPANY_NOT_EXISTS_CODE
        );
    }

    public ResultDTO changeSession(Integer comId, EB88ApiClient eb88ApiClient) {
        Optional<User> userOptional = SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByUsernameActive);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            JwtDTO jwtDTO = getInfoJwt();
            if (jwtDTO == null || !Objects.equals(jwtDTO.getPasswordVersion(), user.getPasswordVersion())) {
                throw new BadRequestAlertException(ExceptionConstants.TOKEN_EXPIRED_VI, ENTITY_NAME, ExceptionConstants.TOKEN_EXPIRED);
            }
            jwtDTO.setCompanyId(comId);
            user.setCompanyId(jwtDTO.getCompanyId());

            String jwt = "";
            LoginVM loginVM = new LoginVM();
            loginVM.setUsername(user.getUsername());
            loginVM.setCompanyId(comId);
            AuthenticationDTO authenticationDTO = getAuthoritiesAndInfo(loginVM, eb88ApiClient);
            AuthoritiesResponse authoritiesResponseDTO = new AuthoritiesResponse();

            authoritiesResponseDTO.setActivate(authenticationDTO.isActivate());
            authoritiesResponseDTO.setCompanies(authenticationDTO.getCompanies());
            authoritiesResponseDTO.setFullName(authenticationDTO.getFullName());
            authoritiesResponseDTO.setUserName(authenticationDTO.getUsername());
            authoritiesResponseDTO.setRole(authenticationDTO.getRole());
            authoritiesResponseDTO.setTaxCode(authenticationDTO.getTaxCode());
            authoritiesResponseDTO.setCompanyId(authenticationDTO.getCompanyId());
            authoritiesResponseDTO.setPermissions(authenticationDTO.getPermissions());
            authoritiesResponseDTO.setCompanyName(authenticationDTO.getCompanyName());
            authoritiesResponseDTO.setId(authenticationDTO.getId());
            authoritiesResponseDTO.setService(authenticationDTO.getService());

            if (authenticationDTO.isActivate()) {
                jwt = tokenProvider.createToken(authenticationDTO, loginVM);
                authoritiesResponseDTO.setId_token(jwt);
            } else {
                return new ResultDTO(ResultConstants.LOGIN_ERROR, ResultConstants.LOGIN_ERROR_VI);
            }
            return new ResultDTO(
                ResultConstants.SUCCESS,
                ResultConstants.LOGIN_SUCCESS_VI,
                authenticationDTO.isActivate(),
                authoritiesResponseDTO
            );
        }
        throw new InternalServerException(ExceptionConstants.USER_NOT_FOUND_VI, ENTITY_NAME, ExceptionConstants.USER_NOT_FOUND);
    }

    @Transactional(readOnly = true)
    public User getUserWithAuthorities(Integer companyId) {
        Optional<User> userOptional = SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByUsernameActive);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            JwtDTO jwtDTO = getInfoJwt();
            if (jwtDTO != null) {
                if (!Objects.equals(jwtDTO.getPasswordVersion(), user.getPasswordVersion())) {
                    throw new BadRequestAlertException(ExceptionConstants.TOKEN_EXPIRED_VI, ENTITY_NAME, ExceptionConstants.TOKEN_EXPIRED);
                }
                user.setCompanyId(jwtDTO.getCompanyId());
                user.setTaxCode(jwtDTO.getTaxCode());
                if (!companyId.equals(jwtDTO.getCompanyId())) {
                    throw new InternalServerException(
                        ExceptionConstants.COMPANY_NOT_EXISTS_CODE_VI,
                        ENTITY_NAME,
                        ExceptionConstants.COMPANY_NOT_EXISTS_CODE
                    );
                }
                user.setCompanyId(jwtDTO.getCompanyId());
                Set<String> authorities = new HashSet<>();
                if (user.getManager()) {
                    authorities.add(AuthoritiesConstants.SYSTEM_ADMIN);
                } else {
                    authorities = authorityRepository.findAllAuthorityByUserIDAndCompanyId(user.getId(), jwtDTO.getCompanyId());
                }
                user.setAuthorities(authorities);
            } else {
                throw new BadRequestAlertException(ExceptionConstants.TOKEN_EXPIRED_VI, ENTITY_NAME, ExceptionConstants.TOKEN_EXPIRED);
            }
            return user;
        }
        throw new InternalServerException(ExceptionConstants.USER_NOT_FOUND_VI, ENTITY_NAME, ExceptionConstants.USER_NOT_FOUND);
    }

    public JwtDTO getInfoJwt() {
        if (!Strings.isNullOrEmpty(SecurityContextHolder.getContext().getAuthentication().getCredentials().toString())) {
            String[] chunks = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString().split("\\.");
            Base64.Decoder decoder = Base64.getUrlDecoder();
            String payload = new String(decoder.decode(chunks[1]));
            try {
                ObjectMapper mapper = new ObjectMapper();
                JwtDTO jwtDTO = mapper.readValue(payload, JwtDTO.class);
                return jwtDTO;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //    public void checkUser(Optional<User> user) {
    //        if (!user.isPresent()) throw new AuthenticateException(
    //            ExceptionConstants.USER_NOT_FOUND_VI,
    //            "User",
    //            ExceptionConstants.USER_NOT_FOUND
    //        );
    //    }

    public void checkUser(Optional<User> user, Integer comId) {
        if (!user.isPresent()) throw new AuthenticateException(
            ExceptionConstants.USER_NOT_FOUND_VI,
            "User",
            ExceptionConstants.USER_NOT_FOUND
        );
        if (comId != null) {
            if (!Objects.equals(comId, user.get().getCompanyId())) {
                throw new InternalServerException(
                    ExceptionConstants.COMPANY_NOT_EXISTS_CODE_VI,
                    ENTITY_NAME,
                    ExceptionConstants.COMPANY_NOT_EXISTS_CODE
                );
            }
        }
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    //    @Scheduled(cron = "0 0 1 * * ?")
    //    public void removeNotActivatedUsers() {
    //        userRepository
    //            .findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS))
    //            .forEach(user -> {
    //                log.debug("Deleting not activated user {}", user.getUsername());
    //                userRepository.delete(user);
    //            });
    //    }
    //    @Scheduled(cron = "0 0/30 * * * ?")
    //    public void removeNotActivatedUsers() {
    //        log.error("Gửi lại taskLog: ");
    //        List<TaskLog> taskLogs = taskLogRepository.findAllTaskLogErrorIO();
    //        String ids = "";
    //        if (!taskLogs.isEmpty()) {
    //            for (TaskLog taskLog : taskLogs) {
    //                sendTaskLog(new TaskLogSendQueue(taskLog.getId().toString(), taskLog.getType()));
    //                ids += taskLog.getId() + ",";
    //            }
    //        } else {
    //            log.error("Danh sách taskLog rỗng ");
    //        }
    //        log.error("Gửi lại taskLog: " + ids);
    //    }

    /**
     * Gets a list of all the authorities.
     *
     * @return a list of all the authorities.
     */
    @Transactional(readOnly = true)
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }

    public AuthenticationDTO getAuthoritiesAndInfo(LoginVM loginVM, EB88ApiClient eb88ApiClient) {
        Optional<User> userOptional = userRepository.findOneByUsernameActive(loginVM.getUsername());
        AuthenticationDTO authenticationDTO = new AuthenticationDTO();
        Integer comId = loginVM.getCompanyId();
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<CompanyResult> companyUsers = companyRepository.findAllCompanyCustomByUserID(user.getId());
            authenticationDTO.setActivate(false);
            //            if (user.getManager()) {
            //                authenticationDTO.setActivate(true);
            //            } else {
            for (CompanyResult com : companyUsers) {
                if (companyUsers.size() == 1 || (com.getId() != null && Objects.equals(com.getId(), comId))) {
                    checkAccountExpired(com.getCompanyOwnerId());
                    authenticationDTO.setActivate(true);
                    authenticationDTO.setCompanyName(com.getName());
                    authenticationDTO.setCompanyId(com.getId());
                    authenticationDTO.setTaxCode(com.getTaxCode());
                    authenticationDTO.setService(com.getService());
                    break;
                }
            }
            if (loginVM.getCompanyId() != null && !Objects.equals(loginVM.getCompanyId(), authenticationDTO.getCompanyId())) {
                throw new BadRequestAlertException(COMPANY_NOT_EXISTS_CODE_VI, ENTITY_NAME, COMPANY_NOT_EXISTS_CODE);
            }

            authenticationDTO.setCompanies(companyUsers);
            //            }
            Set<String> authorities = new HashSet<>();
            if (user.getManager()) {
                //                authorities.add(AuthoritiesConstants.SYSTEM_ADMIN);
                authenticationDTO.setActivate(true);
                //                authenticationDTO.setRole(AuthoritiesConstants.SYSTEM_ADMIN);
            }
            if (authenticationDTO.isActivate()) {
                // check information Config EB88
                //                if (!Strings.isNullOrEmpty(loginVM.getPassword())) {
                //                    try {
                //                        registerConfig(
                //                            new RegisterConfigRequest(authenticationDTO.getCompanyId(), loginVM.getPassword()),
                //                            user,
                //                            eb88ApiClient
                //                        );
                //                    } catch (Exception exception) {
                //                        log.error(ExceptionConstants.REGISTER_CONFIG_ERROR_VI);
                //                    }
                //                }
                authorities = authorityRepository.findAllAuthorityByUserIDAndCompanyId(user.getId(), authenticationDTO.getCompanyId());
                String role = authorityRepository.getRoleNameByUserIdAndCompanyId(user.getId(), authenticationDTO.getCompanyId());
                if (!Strings.isNullOrEmpty(role)) {
                    authenticationDTO.setRole(role);
                }
            }

            authenticationDTO.setPermissions(authorities.toString());
            authenticationDTO.setPermissionList(authorities);
            authenticationDTO.setAuthentication(
                new UsernamePasswordAuthenticationToken(
                    loginVM.getUsername(),
                    null,
                    authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
                )
            );
            authenticationDTO.setId(user.getId());
            authenticationDTO.setFullName(user.getFullName());
            authenticationDTO.setUsername(user.getUsername());
            authenticationDTO.setPasswordVersion(user.getPasswordVersion());
            if (authenticationDTO.isActivate() && authenticationDTO.getCompanyId() != null) {
                //              Gửi Tasklog check all invoice theo com
                TaskLog taskLog = new TaskLog();
                taskLog.setComId(authenticationDTO.getCompanyId());
                taskLog.setType(TaskLogConstants.Type.CHECK_ALL_INVOICE);
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.disable(SerializationFeature.INDENT_OUTPUT);
                    taskLog.setContent(
                        objectMapper.writeValueAsString(new TaskCheckInvoice(authenticationDTO.getCompanyId().toString(), null))
                    );
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                transactionTemplate.execute(status -> {
                    taskLogRepository.save(taskLog);
                    return taskLog;
                });
                sendTaskLog(new TaskLogSendQueue(taskLog.getId().toString(), taskLog.getType(), authenticationDTO.getService()));
            }
            return authenticationDTO;
        }
        return null;
    }

    public void checkAccountExpired(Integer comOwnerId) {
        // check owner_package
        Optional<OwnerPackage> ownerPackageOptional = ownerPackageRepository.findByOwnerId(comOwnerId);
        if (ownerPackageOptional.isEmpty()) {
            throw new BadRequestAlertException(OWNER_PACKAGE_NOT_FOUND_ID, ENTITY_NAME, OWNER_PACKAGE_NOT_FOUND);
        }

        OwnerPackage ownerPackage = ownerPackageOptional.get();
        if (
            ZonedDateTime.now().compareTo(ownerPackage.getEndDate()) > 0 ||
            Objects.equals(ownerPackage.getStatus(), OwnerPackageConstants.Status.LOCKED)
        ) {
            Optional<EPPackage> packageOptional = epPackageRepository.findByIdAndStatus(
                ownerPackage.getPackageId(),
                PackageConstants.Status.ACTIVE
            );
            if (packageOptional.isEmpty()) {
                throw new BadRequestAlertException(PACKAGE_NOT_FOUND_VI, ENTITY_NAME, PACKAGE_NOT_FOUND_CODE);
            }
            EPPackage epPackage = packageOptional.get();
            if (Objects.equals(epPackage.getType(), PackageConstants.Type.TRIAL)) {
                throw new BadRequestAlertException(TRIAL_EXPIRED_VI, ENTITY_NAME, TRIAL_EXPIRED);
            }
            throw new BadRequestAlertException(ACCOUNT_EXPIRED_VI, ENTITY_NAME, ACCOUNT_EXPIRED);
        }
    }

    public User save(UserRequest userRequestDTO) {
        userRepository
            .findOneByUsernameActive(userRequestDTO.getUsername().toLowerCase())
            .ifPresent(existingUser -> {
                boolean removed = removeNonActivatedUser(existingUser);
                if (!removed) {
                    throw new UsernameAlreadyUsedException();
                }
            });
        User user = new User();
        BeanUtils.copyProperties(userRequestDTO, user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setManager(false);
        user.setNormalizedName(Common.normalizedName(Arrays.asList(user.getFullName())));
        userRepository.save(user);
        CompanyUser companyUser = new CompanyUser();
        companyUser.setUserId(user.getId());
        companyUser.setCompanyId(userRequestDTO.getCompanyId());
        companyUserRepository.save(companyUser);
        return user;
    }

    public String genCode(Integer comId, String code) {
        String seqName = getSeqName(comId, code);
        String value = genCodeByValue(comId, code);
        seqName = code + value;
        return seqName;
    }

    public String genCodeByValue(Integer comId, String code) {
        String seqName = getSeqName(comId, code);
        String value = "";
        List<String> codes = Arrays.asList(
            Constants.PHIEU_THU,
            Constants.PHIEU_CHI,
            Constants.DON_HANG,
            Constants.NHAP_KHO,
            Constants.XUAT_KHO,
            Constants.NO_PHAI_THU,
            Constants.NO_PHAI_TRA
        );
        //        Nếu trong danh sách trên thì lấy dữ liệu từ SEQ ngược lại thì count
        if (codes.contains(code)) {
            value = userRepository.getSeqByCompanyAndCode(seqName);
        } else {
            String tableName = genTableNameByCode(code);
            if (code.equals(Constants.DEVICE_CODE)) {
                Integer count = ownerDeviceRepository.countAllByCompanyId(comId);
                value = String.valueOf(count + 1);
                int number = 3;
                StringBuilder codeFirst = new StringBuilder();
                for (int i = 0; i < number - value.length(); i++) {
                    codeFirst.append("0");
                }
                value = codeFirst + value;
            } else if (code.equals(Constants.SUPPLIER_CODE)) {
                Integer count = customerRepository.countAllByComIdAndType(comId, CustomerConstants.Type.SUPPLIER);
                value = String.valueOf(count + 1);
            } else if (code.equals(Constants.CUSTOMER_SUPPLIER_CODE)) {
                Integer count = customerRepository.countAllByComIdAndType(comId, CustomerConstants.Type.CUSTOMER_AND_SUPPLIER);
                value = String.valueOf(count + 1);
            } else if (code.equals(Constants.THU)) {
                Integer count = businessTypeRepository.countByComIdAndType(
                    comId,
                    "%" + Common.normalizedName(Arrays.asList(Constants.THU)) + "%"
                );
                value = String.valueOf(count + 1);
            } else if (code.equals(Constants.CHI)) {
                Integer count = businessTypeRepository.countByComIdAndType(
                    comId,
                    "%" + Common.normalizedName(Arrays.asList(Constants.CHI)) + "%"
                );
                value = String.valueOf(count + 1);
            } else {
                value = userRepository.genCodeByTableName(comId, tableName);
            }
        }
        return value;
    }

    private String genTableNameByCode(String code) {
        String value = "";
        switch (code) {
            case "KH":
                {
                    value = Constants.TableName.CUSTOMER;
                    break;
                }
            case "SP":
                {
                    value = Constants.TableName.PRODUCT;
                    break;
                }
            case Constants.DEVICE_CODE:
                {
                    value = Constants.TableName.DEVICE;
                    break;
                }
            case Constants.PRODUCT_GROUP_CODE:
                {
                    value = Constants.TableName.PRODUCT_GROUP;
                    break;
                }
        }
        return value;
    }

    public ResultDTO autoGenSeqName(Integer comId) {
        List<String> codes = Arrays.asList(
            Constants.PHIEU_THU,
            Constants.PHIEU_CHI,
            Constants.DON_HANG,
            Constants.NHAP_KHO,
            Constants.XUAT_KHO,
            Constants.NO_PHAI_THU,
            Constants.NO_PHAI_TRA
        );
        for (String item : codes) {
            String seqName = getSeqName(comId, item);
            userRepository.autoGenSeqName(seqName);
        }
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_SEQ_CREATE, true);
    }

    public String getSeqName(Integer comId, String code) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(code);
        stringBuilder.append("_");
        stringBuilder.append(comId);
        stringBuilder.append("_");
        stringBuilder.append(Constants.SEQ);
        return stringBuilder.toString();
    }

    public Page<User> findAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public ResultDTO registerDeviceForCode(String deviceId) {
        TaxAuthorityCode taxAuthorityCode = new TaxAuthorityCode();
        taxAuthorityCode.setDeviceId(deviceId);
        User user = getUserWithAuthorities();
        taxAuthorityCode.setComId(user.getCompanyId());
        //        taxAuthorityCode.setTaxcode(dto.getTaxcode());
        List<TaxAuthorityCode> taxAuthorityCodeOptional = taxAuthorityCodeRepository.findByComIdAndDeviceId(user.getCompanyId(), deviceId);
        if (!taxAuthorityCodeOptional.isEmpty()) {
            return new ResultDTO(
                ResultConstants.SUCCESS,
                ResultConstants.SUCCESS_DEVICE_CODE,
                true,
                taxAuthorityCodeOptional.get(0).getDeviceCode()
            );
            //            return new TaxAuthorityCodeDTO(taxAuthorityCodeOptional.get(0));
        }
        //        List<TaxAuthorityCode> byCompanyId = taxAuthorityCodeRepository.findAllByCompanyId(user.getCompanyId());
        //        // get the current highest device_code, by ordering desc
        //        byCompanyId =
        //            byCompanyId.stream().sorted(((o1, o2) -> o2.getDeviceCode().compareTo(o1.getDeviceCode()))).collect(Collectors.toList());
        //        int currentCode = 0;
        //        if (!byCompanyId.isEmpty()) {
        //
        //        }
        String seqName = getSeqName(user.getCompanyId(), Constants.DEVICE_ID);
        String value = userRepository.getSeqByCompanyAndCode(seqName);
        taxAuthorityCode.setDeviceCode(value);
        taxAuthorityCode = taxAuthorityCodeRepository.save(taxAuthorityCode);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_DEVICE_CODE, true, taxAuthorityCode.getDeviceCode());
    }

    public ResultDTO autoGenSeqNameCode() {
        User user = getUserWithAuthorities();
        autoGenSeqName(user.getCompanyId());
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS, true);
    }

    @Async
    public void userCompletionAsync(User user) {
        //        Tạo thông tin thêm sau khi tạo user
        Integer companyId = user.getCompanyId();
        autoGenSeqName(user.getCompanyId());
        createRoleDefault(user);
        createAreaDefault(companyId);
        createPrintConfigDefault(user, companyId);
        createPrintTemplateDefault(companyId);
        createUnitDefault(companyId);
        createConfigDefault(companyId);
        createCustomerDefault(companyId);
        createProductDefault(companyId);
        createBusinessTypeDefault(companyId);
    }

    public UserResponse findUserById(Integer userId) {
        log.error(ENTITY_NAME + "_findUserById: {}", userId);
        return userRepository.getUserResponse(userId);
    }

    public User updateUser(UserRequest userRequestDTO) {
        User user = getUserWithAuthorities();
        if (companyUserRepository.countAllByCompanyIdAndUserId(user.getCompanyId(), userRequestDTO.getId()) != 1) {
            throw new BadRequestAlertException("UserId", ENTITY_NAME, "is not valid");
        }
        BeanUtils.copyProperties(userRequestDTO, user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setNormalizedName(Common.normalizedName(Arrays.asList(user.getFullName())));
        return userRepository.save(user);
    }

    public Integer getCompanyId() {
        User user = getUserWithAuthorities();
        return user.getCompanyId();
    }

    public ResultDTO changePassword(ChangePasswordRequest request, EB88ApiClient eb88ApiClient) {
        log.info("change_password:" + request.toString());
        User userCurrent = getUserWithAuthorities();

        Optional<User> userOptional = userRepository.findOneByUsernameActive(userCurrent.getUsername());
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            String currentEncryptedPassword = user.getPassword();
            if (!passwordEncoder.matches(request.getOldPassword(), currentEncryptedPassword)) {
                throw new InternalServerException(ExceptionConstants.PASSWORD_VALID_VI, ENTITY_NAME, ExceptionConstants.PASSWORD_VALID);
            }
            if (!request.getNewPassword().equals(request.getConfirmPassword())) {
                throw new InternalServerException(
                    ExceptionConstants.CONFIRM_PASSWORD_CODE_VI,
                    ENTITY_NAME,
                    ExceptionConstants.CONFIRM_PASSWORD_CODE
                );
            }
            if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
                throw new InternalServerException(
                    ExceptionConstants.PASSWORD_DUPLICATE_VI,
                    ENTITY_NAME,
                    ExceptionConstants.PASSWORD_DUPLICATE_CODE
                );
            }
            TaskLogSendQueue taskLogSendQueue = transactionTemplate.execute(status -> {
                try {
                    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
                    if (request.getIsLogoutAll()) {
                        user.setPasswordVersion(user.getPasswordVersion() + 1);
                    }
                    userRepository.save(user);
                    return createAndPublishQueueChangePassword(user.getCompanyId(), user.getUsername(), request);
                } catch (Exception e) {
                    log.error("Can not create queue task for eb88 change password: {}", e.getMessage());
                }
                return null;
            });
            if (taskLogSendQueue != null) {
                sendTaskLog(taskLogSendQueue);

                LoginVM loginVM = new LoginVM();
                loginVM.setUsername(user.getUsername());
                loginVM.setPassword(request.getNewPassword());
                loginVM.setCompanyId(user.getCompanyId());

                String token = tokenProvider.createToken(getAuthoritiesAndInfo(loginVM, eb88ApiClient), loginVM);
                AuthoritiesResponse authoritiesResponseDTO = new AuthoritiesResponse();
                authoritiesResponseDTO.setId_token(token);
                return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_CHANGE_PASSWORD, true, authoritiesResponseDTO);
            } else {
                return new ResultDTO(ResultConstants.FAIL, ResultConstants.FAIL_RESET_PASSWORD, false);
            }
        } else {
            throw new InternalServerException(ExceptionConstants.USER_NOT_FOUND, ENTITY_NAME, ExceptionConstants.USER_NOT_FOUND_VI);
        }
    }

    public ResultDTO getUserItems(String keyword) {
        List<UserItemsResult> results = userRepository.getUserItems(keyword == null ? "" : Common.normalizedName(List.of(keyword)));
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.USER_GET_ALL_SUCCESS_VI, true, results);
    }

    public void registerConfig(RegisterConfigRequest registerConfigRequest, User user, EB88ApiClient eb88ApiClient) throws Exception {
        Integer comId = registerConfigRequest.getComId();
        List<String> codes = Common.getEBConfigCodes();
        List<Config> configs = configRepository.getAllByCompanyID(comId, codes);
        Map<String, Integer> configMap = new HashMap<>();
        configs.forEach(config -> configMap.put(config.getCode(), config.getId()));
        configs = configs.stream().filter(config -> !Strings.isNullOrEmpty(config.getValue())).collect(Collectors.toList());
        if (configs.size() != codes.size()) {
            // chưa có thông tin config || chưa đầy đủ value theo code
            Optional<Company> companyOptional = companyRepository.findById(comId);
            Company company = companyOptional.get();
            CompanyOwner companyOwner = company.getCompanyOwner();
            if (companyOwner != null) {
                Optional<OwnerPackage> packageOptional = ownerPackageRepository.findByOwnerId(companyOwner.getId());
                if (packageOptional.isPresent()) {
                    Optional<EPPackage> epPackageOptional = epPackageRepository.findById(packageOptional.get().getPackageId());
                    if (epPackageOptional.isPresent()) {
                        RegisterRequest registerRequest = new RegisterRequest();

                        registerRequest.setCompanyName(company.getName());
                        registerRequest.setCompanyTaxCode(companyOwner.getTaxCode());
                        registerRequest.setCompanyAddress(company.getAddress());
                        registerRequest.setFullName(user.getFullName());
                        registerRequest.setUsername(user.getUsername());
                        registerRequest.setEmail(user.getEmail());
                        registerRequest.setPhoneNumber(user.getPhoneNumber());
                        registerRequest.setPackageId(epPackageOptional.get().getId());
                        registerRequest.setStartDate(dateTimeFormatter.format(packageOptional.get().getStartDate()));
                        registerRequest.setEndDate(dateTimeFormatter.format(packageOptional.get().getEndDate()));
                        registerRequest.setPackCount(packageOptional.get().getPackCount());
                        RegisterCompanyResponse ebCompanyResponse = createUserEB88(
                            registerRequest,
                            user.getEmail(),
                            eb88ApiClient,
                            registerConfigRequest.getPassword()
                        );
                        insertToConfigFromEB(comId, ebCompanyResponse, configMap);
                        log.debug("RegisterConfig: " + ResultConstants.SUCCESS_CONFIG_STORE);
                    }
                } else {
                    throw new InternalServerException(
                        ExceptionConstants.PACKAGE_CODE_NOT_FOUND_VI,
                        ENTITY_NAME,
                        ExceptionConstants.PACKAGE_NOT_FOUND_CODE
                    );
                }
            } else {
                throw new InternalServerException(
                    ExceptionConstants.COMPANY_NOT_EXISTS_CODE_VI,
                    ENTITY_NAME,
                    ExceptionConstants.COMPANY_NOT_EXISTS_CODE
                );
            }
        }
    }

    public void sendTaskLog(TaskLogSendQueue task) {
        JwtDTO jwtDTO = null;
        String service = "";
        try {
            if (!Strings.isNullOrEmpty(task.getService())) {
                service = task.getService();
            } else {
                jwtDTO = getInfoJwt();
                service = jwtDTO.getService();
                if (!Strings.isNullOrEmpty(jwtDTO.getService()) && service.equals(UserConstants.Service.ADMIN)) {
                    service = taskLogRepository.getServiceById(task.getId());
                }
            }
        } catch (NullPointerException exception) {
            service = "";
        }
        log.error("send tassklog: " + service);
        switch (task.getType()) {
            case TaskLogConstants.Type.PUBLISH_INVOICE:
                {
                    if (!Strings.isNullOrEmpty(service) && service.equals(UserConstants.Service.NGP)) {
                        ngoGiaPhatInvoiceProducer.issueInvoice(new TaskLogIdEnqueueMessage(Integer.valueOf(task.getId())));
                    } else {
                        easyInvoiceProducer.issueInvoice(new TaskLogIdEnqueueMessage(Integer.valueOf(task.getId())));
                    }
                    break;
                }
            case TaskLogConstants.Type.IMPORT_INVOICE:
                {
                    if (!Strings.isNullOrEmpty(service) && service.equals(UserConstants.Service.NGP)) {
                        ngoGiaPhatInvoiceProducer.importInvoice(new TaskLogIdEnqueueMessage(Integer.valueOf(task.getId())));
                    } else {
                        easyInvoiceProducer.importInvoice(new TaskLogIdEnqueueMessage(Integer.valueOf(task.getId())));
                    }
                    break;
                }
            case TaskLogConstants.Type.CHECK_INVOICE:
            case TaskLogConstants.Type.CHECK_ALL_INVOICE:
                {
                    if (!Strings.isNullOrEmpty(service) && service.equals(UserConstants.Service.NGP)) {
                        ngoGiaPhatInvoiceProducer.checkInvoice(new TaskLogIdEnqueueMessage(Integer.valueOf(task.getId())));
                    } else {
                        easyInvoiceProducer.checkInvoice(new TaskLogIdEnqueueMessage(Integer.valueOf(task.getId())));
                    }
                    break;
                }
            case TaskLogConstants.Type.CANCEL_INVOICE:
            case TaskLogConstants.Type.REMOVE_UNSIGNED_INVOICE:
                {
                    cancelOrRemoveUnsignedInvoice(Integer.valueOf(task.getId()), service);
                    break;
                }
            case TaskLogConstants.Type.EB_CREATE_MATERIAL_GOODS:
            case TaskLogConstants.Type.EB_UPDATE_MATERIAL_GOODS:
            case TaskLogConstants.Type.EB_CREATE_ACC_OBJECT:
            case TaskLogConstants.Type.EB_UPDATE_ACC_OBJECT:
            case TaskLogConstants.Type.EB_CREATE_SAINVOICE:
            case TaskLogConstants.Type.EB_CANCEL_SAINVOICE:
            case TaskLogConstants.Type.EB_CREATE_PRODUCT_UNIT:
            case TaskLogConstants.Type.EB_ASYNC_PRODUCT_UNIT:
            case TaskLogConstants.Type.EB_CREATE_RS_IN_OUT_WARD:
            case TaskLogConstants.Type.EB_DELETE_RS_IN_OUT_WARD:
            case TaskLogConstants.Type.EB_CREATE_ACCOUNT:
            case TaskLogConstants.Type.EB_SAVE_COMPANY_UNIT:
            case TaskLogConstants.Type.EB_FORGOT_PASSWORD:
            case TaskLogConstants.Type.EB_CHANGE_PASSWORD:
                //                eb88Producer.send(new TaskLogIdEnqueueMessage(Integer.valueOf(task.getId())));
                break;
        }
    }

    private void cancelOrRemoveUnsignedInvoice(Integer taskId, String service) {
        if (!Strings.isNullOrEmpty(service) && service.equals(UserConstants.Service.NGP)) {
            ngoGiaPhatInvoiceProducer.cancelInvoice(new TaskLogIdEnqueueMessage(taskId));
        } else {
            easyInvoiceProducer.cancelInvoice(new TaskLogIdEnqueueMessage(taskId));
        }
    }

    public ResultDTO forgotPassword(ForgotPasswordRequest request, EB88ApiClient eb88ApiClient) throws Exception {
        log.info("forgot_password:" + request.toString());
        Optional<User> userOptional = userRepository.findOneByUsernameActive(request.getUsername());
        if (userOptional.isPresent()) {
            Integer count = otpRepository.countNumberRequest(request.getUsername());
            if (count >= CommonConstants.MAX_TIME_REQUEST_FORGOT_PASS) {
                throw new BadRequestAlertException(
                    ExceptionConstants.MAX_TIME_REQUEST_FORGOT_VI,
                    ENTITY_NAME,
                    ExceptionConstants.MAX_TIME_REQUEST_FORGOT
                );
            }
            List<Otp> otps = otpRepository.findByUsernameAndTypeAndStatus(
                request.getUsername(),
                OTPConstants.Type.FORGOT_PASS,
                OTPConstants.Status.DEFAULT
            );
            for (Otp otp : otps) {
                otp.setStatus(OTPConstants.Status.USED);
            }
            String otp = RandomStringUtils.random(CommonConstants.REGISTER_PASSWORD_LENGTH, false, true);
            if (request.getUsername().matches(Constants.PATTERN_MAIL)) {
                emailService.sendResetPassword(request.getUsername(), otp);
            } else if (request.getUsername().matches(Constants.PHONE_NUMBER_REGEX)) {
                String content = Constants.SEND_MESSAGE_OTP_CONTENT.replace("@@otp", otp);
                emailService.sendMessagePhoneNumber(request.getUsername(), Constants.SEND_MESSAGE_FORGOT_PASSWORD_SUBJECT, content);
            }
            Otp otpSave = new Otp();
            otpSave.setOtp(otp);
            otpSave.setUsername(request.getUsername());
            otpSave.setExpiredTime(ZonedDateTime.now().plusSeconds(CommonConstants.EXPIRED_TIME_FORGOT_PASS));
            otpSave.setType(OTPConstants.Type.FORGOT_PASS);
            otpSave.setStatus(OTPConstants.Status.DEFAULT);
            otpRepository.save(otpSave);
            return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_SEND_OTP, Boolean.TRUE);
        }
        throw new InternalServerException(ExceptionConstants.USER_NOT_FOUND_VI, ENTITY_NAME, ExceptionConstants.USER_NOT_FOUND);
    }

    public ResultDTO changePasswordForAdmin(AdminChangePasswordRequest request, EB88ApiClient eb88ApiClient, Boolean isActionAdmin)
        throws Exception {
        Optional<User> userOptional = userRepository.findOneByUsernameActive(request.getUsername());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<String> hash = createHashForgot(request);
            if (!hash.contains(request.getHash())) {
                throw new BadRequestAlertException(
                    ExceptionConstants.HASH_INCORRECT_VI,
                    ENTITY_NAME,
                    ExceptionConstants.HASH_INCORRECT_CODE
                );
            }
            Optional<Otp> otpOptional = otpRepository.getLastUserOtp(request.getUsername(), OTPConstants.Type.FORGOT_PASS);
            if (
                otpOptional.isEmpty() ||
                !otpOptional.get().getStatus().equals(OTPConstants.Status.USED) ||
                !otpOptional.get().getOtp().equals(request.getOtp())
            ) {
                throw new BadRequestAlertException(ExceptionConstants.OTP_INCORRECT_VI, ENTITY_NAME, ExceptionConstants.OTP_INCORRECT);
            }
            if (!request.getNewPassword().equals(request.getConfirmPassword())) {
                throw new InternalServerException(
                    ExceptionConstants.CONFIRM_PASSWORD_CODE_VI,
                    ENTITY_NAME,
                    ExceptionConstants.CONFIRM_PASSWORD_CODE
                );
            }
            if (
                request.getNewPassword().trim().length() < CommonConstants.REGISTER_PASSWORD_LENGTH ||
                request.getConfirmPassword().trim().length() < CommonConstants.REGISTER_PASSWORD_LENGTH
            ) {
                throw new BadRequestAlertException(ExceptionConstants.PASSWORD_INVALID_VI, ENTITY_NAME, ExceptionConstants.PASSWORD_VALID);
            }
            if (request.getNewPassword().contains(" ")) {
                throw new BadRequestAlertException(
                    ExceptionConstants.PASSWORD_INVALID_SPACE_VI,
                    ENTITY_NAME,
                    ExceptionConstants.PASSWORD_VALID
                );
            }
            if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
                throw new InternalServerException(
                    ExceptionConstants.PASSWORD_DUPLICATE_VI,
                    ENTITY_NAME,
                    ExceptionConstants.PASSWORD_DUPLICATE_CODE
                );
            }
            TaskLogSendQueue taskLogSendQueue = transactionTemplate.execute(status -> {
                try {
                    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
                    user.setPasswordVersion(user.getPasswordVersion() + 1);
                    userRepository.save(user);
                    if (isActionAdmin) {
                        if (request.getUsername().matches(Constants.PATTERN_MAIL)) {
                            emailService.sendResetPassword(request.getUsername(), request.getNewPassword());
                        } else if (request.getUsername().matches(Constants.PHONE_NUMBER_REGEX)) {
                            String content = Constants.SEND_MESSAGE_FORGOT_PASSWORD_CONTENT.replace("@@password", request.getNewPassword());
                            emailService.sendMessagePhoneNumber(
                                request.getUsername(),
                                Constants.SEND_MESSAGE_FORGOT_PASSWORD_SUBJECT,
                                content
                            );
                        }
                    }
                    return createAndPublishQueuePassword(
                        null,
                        request.getUsername(),
                        request.getNewPassword(),
                        TaskLogConstants.Type.EB_FORGOT_PASSWORD
                    );
                } catch (Exception e) {
                    log.error("Can not create queue task for eb88 creating rsInOutWard: {}", e.getMessage());
                }
                return null;
            });
            if (taskLogSendQueue != null) {
                sendTaskLog(taskLogSendQueue);
                return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_RESET_PASSWORD, true);
            } else {
                return new ResultDTO(ResultConstants.FAIL, ResultConstants.FAIL_RESET_PASSWORD, false);
            }
        }
        throw new InternalServerException(ExceptionConstants.USER_NOT_FOUND_VI, ENTITY_NAME, ExceptionConstants.USER_NOT_FOUND);
    }

    private TaskLogSendQueue createAndPublishQueuePassword(Integer comId, String username, String password, String type) throws Exception {
        ForgotPasswordTask task = new ForgotPasswordTask();
        task.setComId(comId);
        task.setUsername(username);
        task.setPassword(password);

        TaskLog taskLog = new TaskLog();
        taskLog.setComId(comId);
        taskLog.setStatus(TaskLogConstants.Status.PROCESSING);
        //New objectMapper because we need disable a feature
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.INDENT_OUTPUT);
        taskLog.setContent(objectMapper.writeValueAsString(task));
        taskLog.setType(type);
        taskLog = taskLogRepository.save(taskLog);
        //publish to queue
        //        eb88Producer.send(new TaskLogIdEnqueueMessage(taskLog.getId()));
        return new TaskLogSendQueue(taskLog.getId().toString(), taskLog.getType());
    }

    private TaskLogSendQueue createAndPublishQueueChangePassword(Integer comId, String username, ChangePasswordRequest request)
        throws Exception {
        ChangePasswordTask task = new ChangePasswordTask();
        task.setComId(comId);
        task.setUsername(username);
        task.setOldPassword(request.getOldPassword());
        task.setNewPassword(request.getNewPassword());

        TaskLog taskLog = new TaskLog();
        taskLog.setComId(comId);
        taskLog.setStatus(TaskLogConstants.Status.PROCESSING);
        //New objectMapper because we need disable a feature
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.INDENT_OUTPUT);
        taskLog.setContent(objectMapper.writeValueAsString(task));
        taskLog.setType(TaskLogConstants.Type.EB_CHANGE_PASSWORD);
        taskLog = taskLogRepository.save(taskLog);
        //publish to queue
        //        eb88Producer.send(new TaskLogIdEnqueueMessage(taskLog.getId()));
        return new TaskLogSendQueue(taskLog.getId().toString(), taskLog.getType());
    }

    private List<String> createHashForgot(AdminChangePasswordRequest request) {
        List<String> result = new ArrayList<>();
        long dateTime = Instant.now().toEpochMilli() / (1000 * 300);
        int count = 0;
        while (count < 2) {
            result.add(Util.createMd5(request.getUsername() + request.getNewPassword() + dateTime + Constants.HASH_EASY_POS_MD5));
            dateTime--;
            count++;
        }
        return result;
    }

    public ResultDTO getByUserName(String userName) {
        Optional<User> userOptional = userRepository.findOneByUsernameActive(userName.trim().toLowerCase());
        if (userOptional.isEmpty()) {
            return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.USER_NOT_EXISTS, true);
        }
        User user = userOptional.get();
        List<CompanyUser> companyUsers = companyUserRepository.findAllByUserId(user.getId());
        if (companyUsers.isEmpty()) {
            return new ResultDTO(ResultConstants.USER_DONT_USED_CODE, ResultConstants.USER_DONT_USED_CODE_VI, true);
        } else {
            List<Integer> comIds = companyUsers.stream().map(CompanyUser::getCompanyId).collect(Collectors.toList());
            List<GetByUserNameResult> companies = companyRepository.getAllCompanyByIds(comIds);

            return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.USER_BY_USER_NAME_SUCCESS, true, companies, companies.size());
        }
    }
}
