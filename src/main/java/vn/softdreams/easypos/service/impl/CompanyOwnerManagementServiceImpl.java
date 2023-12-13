package vn.softdreams.easypos.service.impl;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.easypos.config.Constants;
import vn.softdreams.easypos.constants.ResultConstants;
import vn.softdreams.easypos.constants.UserConstants;
import vn.softdreams.easypos.domain.Company;
import vn.softdreams.easypos.domain.CompanyOwner;
import vn.softdreams.easypos.domain.User;
import vn.softdreams.easypos.dto.company.CompanyItemsResult;
import vn.softdreams.easypos.dto.company.CompanyResponse;
import vn.softdreams.easypos.dto.companyOwner.CompanyOwnerRequest;
import vn.softdreams.easypos.dto.companyOwner.CompanyOwnerUserResult;
import vn.softdreams.easypos.dto.companyOwner.OwnerResponse;
import vn.softdreams.easypos.dto.companyOwner.OwnerResult;
import vn.softdreams.easypos.dto.config.ConfigStatusResult;
import vn.softdreams.easypos.repository.CompanyOwnerRepository;
import vn.softdreams.easypos.repository.CompanyRepository;
import vn.softdreams.easypos.repository.ConfigRepository;
import vn.softdreams.easypos.repository.UserRepository;
import vn.softdreams.easypos.service.CompanyOwnerManagementService;
import vn.softdreams.easypos.service.UserService;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.util.Common;
import vn.softdreams.easypos.web.rest.errors.BadRequestAlertException;
import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;
import vn.softdreams.easypos.web.rest.errors.InternalServerException;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class CompanyOwnerManagementServiceImpl implements CompanyOwnerManagementService {

    private final Logger log = LoggerFactory.getLogger(CategoryManagementServiceImpl.class);

    private final String ENTITY_NAME = "CompanyOwner";

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(Constants.ZONED_DATE_TIME_FORMAT);

    private final CompanyOwnerRepository companyOwnerRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final ModelMapper mapper;
    private final ConfigRepository configRepository;

    public CompanyOwnerManagementServiceImpl(
        CompanyOwnerRepository companyOwnerRepository,
        CompanyRepository companyRepository,
        UserRepository userRepository,
        UserService userService,
        ModelMapper mapper,
        ConfigRepository configRepository
    ) {
        this.companyOwnerRepository = companyOwnerRepository;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.mapper = mapper;
        this.configRepository = configRepository;
    }

    @Override
    public ResultDTO getAllWithPaging(Pageable pageable, String keyword) {
        User user = userService.getUserWithAuthorities();
        Pageable pageableCustom = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("update_time").descending());
        Page<CompanyOwner> companyOwners = companyOwnerRepository.findAllByNameOrTaxCode(
            pageableCustom,
            user.getId(),
            keyword == null ? "" : keyword
        );
        List<OwnerResponse> response = Arrays.asList(mapper.map(companyOwners.getContent(), OwnerResponse[].class));
        Integer currentOwnerId = companyOwnerRepository.findIdByCompanyID(userService.getCompanyId());
        if (currentOwnerId != null) {
            List<OwnerResponse> companyResponses = response
                .stream()
                .filter(owner -> owner.getId().equals(currentOwnerId))
                .collect(Collectors.toList());
            if (!companyResponses.isEmpty()) {
                for (CompanyResponse companyResponse : companyResponses.get(0).getCompanies()) {
                    if (companyResponse.getId().equals(userService.getCompanyId())) {
                        companyResponse.setIsCurrent(true);
                    }
                }
            }
        }

        log.debug(ENTITY_NAME + "_getAllWithPaging: " + ResultConstants.SUCCESS_GET_COMPANY);
        return new ResultDTO(
            ResultConstants.SUCCESS,
            ResultConstants.SUCCESS_GET_COMPANY,
            true,
            response,
            (int) companyOwners.getTotalElements()
        );
    }

    /**
     * lấy danh sách công ty cha
     * set status config cho công ty cha ( nếu các công ty con đều có thông tin config của E-Invoice ? true : false )
     */
    @Override
    public ResultDTO getAllForAdmin(Pageable pageable, String keyword, String fromDate, String toDate) {
        Page<OwnerResult> results = companyOwnerRepository.getAllForAdmin(pageable, keyword, fromDate, toDate);
        List<OwnerResult> response = results.getContent();
        List<Integer> ownerIds = response.stream().map(OwnerResult::getId).collect(Collectors.toList());

        // get status configs by list owner
        List<ConfigStatusResult> statusResults = configRepository.getConfigsStatusByOwner(ownerIds, Common.getInvoiceConfigCodes());
        Map<Integer, Integer> statusMap = new HashMap<>();
        statusResults.forEach(result -> statusMap.put(result.getCompanyId(), result.getStatus()));

        // set status Config for response
        for (OwnerResult result : response) {
            if (statusMap.containsKey(result.getId())) {
                result.setStatusConfig(statusMap.get(result.getId()).equals(1));
            }
        }

        log.debug(ENTITY_NAME + "_getAllForAdmin: " + ResultConstants.SUCCESS_GET_COMPANY);
        return new ResultDTO(
            ResultConstants.SUCCESS,
            ResultConstants.SUCCESS_GET_COMPANY,
            true,
            results.getContent(),
            (int) results.getTotalElements()
        );
    }

    @Override
    public ResultDTO save(CompanyOwnerRequest request) {
        CompanyOwner companyOwner;
        if (request.getId() == null) {
            companyOwner = new CompanyOwner();
        } else {
            Optional<CompanyOwner> ownerOptional = companyOwnerRepository.findById(request.getId());
            if (ownerOptional.isEmpty()) {
                throw new InternalServerException(
                    ExceptionConstants.COMPANY_NOT_EXISTS_CODE_VI,
                    ENTITY_NAME,
                    ExceptionConstants.COMPANY_NOT_EXISTS_CODE
                );
            }
            companyOwner = ownerOptional.get();
        }
        if (request.getOwnerId() != null) {
            Optional<User> userOptional = userRepository.findByIdAndStatus(request.getOwnerId(), UserConstants.Status.ACTIVATE);
            if (userOptional.isEmpty()) {
                throw new InternalServerException(
                    ExceptionConstants.OWNER_USER_NOT_FOUND_VI,
                    ENTITY_NAME,
                    ExceptionConstants.OWNER_USER_NOT_FOUND_CODE
                );
            }
            companyOwner.setOwnerName(userOptional.get().getFullName());
        }
        BeanUtils.copyProperties(request, companyOwner);
        if (request.getTaxRegisterTime() != null) {
            companyOwner.setTaxRegisterTime(formatDateTime(request.getTaxRegisterTime()));
        }
        if (request.getId() == null) {
            companyRepository.save(
                new Company(companyOwner, request.getName(), request.getAddress(), false, Common.normalizedName(List.of(request.getName())))
            );
        }
        companyOwnerRepository.save(companyOwner);
        log.info(ENTITY_NAME + "_saveCompanyOwner: " + ResultConstants.COMPANY_OWNER_CREATE_SUCCESS_VI);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.COMPANY_OWNER_CREATE_SUCCESS_VI, true);
    }

    @Override
    public ResultDTO getDetailById(Integer id) {
        Optional<CompanyOwner> ownerOptional = companyOwnerRepository.findById(id);
        if (ownerOptional.isEmpty()) {
            throw new InternalServerException(
                ExceptionConstants.COMPANY_NOT_EXISTS_CODE_VI,
                ENTITY_NAME,
                ExceptionConstants.COMPANY_NOT_EXISTS_CODE
            );
        }
        OwnerResponse response = new OwnerResponse();
        BeanUtils.copyProperties(ownerOptional.get(), response);
        log.debug(ENTITY_NAME + "_getById: " + ResultConstants.COMPANY_OWNER_GET_SUCCESS_VI);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.COMPANY_OWNER_GET_SUCCESS_VI, true, response);
    }

    @Override
    public ResultDTO getById(Integer id) {
        Optional<CompanyOwner> ownerOptional = companyOwnerRepository.findById(id);
        if (ownerOptional.isEmpty()) {
            throw new InternalServerException(
                ExceptionConstants.COMPANY_NOT_EXISTS_CODE_VI,
                ENTITY_NAME,
                ExceptionConstants.COMPANY_NOT_EXISTS_CODE
            );
        }
        CompanyOwnerRequest ownerRequest = new CompanyOwnerRequest();
        BeanUtils.copyProperties(ownerOptional.get(), ownerRequest);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.ZONED_TIME_ADMIN_WEB_FORMAT);
        if (ownerOptional.get().getTaxRegisterTime() != null) {
            ownerRequest.setTaxRegisterTime(ownerOptional.get().getTaxRegisterTime().format(formatter));
        }
        log.debug(ENTITY_NAME + "_getById: " + ResultConstants.COMPANY_OWNER_GET_SUCCESS_VI);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.COMPANY_OWNER_GET_SUCCESS_VI, true, ownerRequest);
    }

    @Override
    public ResultDTO getByUserId(Integer id) {
        userService.getUserWithAuthorities();

        List<CompanyOwner> companyOwners = companyOwnerRepository.findAllByOwnerId(id);
        List<CompanyOwnerUserResult> results = Arrays.asList(mapper.map(companyOwners, CompanyOwnerUserResult[].class));

        log.debug(ENTITY_NAME + "_getByUserId: " + ResultConstants.COMPANY_OWNER_GET_SUCCESS_VI);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.COMPANY_OWNER_GET_SUCCESS_VI, true, results);
    }

    @Override
    public ResultDTO getOwnerItems(Pageable pageable, String keyword) {
        List<CompanyItemsResult> companies = companyOwnerRepository.getAllItems(null, keyword == null ? "" : keyword);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_GET_COMPANY, true, companies);
    }

    private ZonedDateTime formatDateTime(String requestDateTime) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(Constants.ZONED_TIME_ADMIN_WEB_FORMAT);
            SimpleDateFormat output = new SimpleDateFormat(Constants.ZONED_DATE_TIME_FORMAT);
            String dateTime = output.format(sdf.parse(requestDateTime));

            DateTimeFormatter formatter = dateTimeFormatter.withZone(ZoneId.systemDefault());
            return ZonedDateTime.parse(dateTime, formatter);
        } catch (Exception exception) {
            throw new BadRequestAlertException(
                ExceptionConstants.DATE_TIME_INVALID_VI,
                ENTITY_NAME,
                ExceptionConstants.DATE_TIME_INVALID_CODE
            );
        }
    }
}
