package vn.softdreams.easypos.service.impl;

import com.google.common.base.Strings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.easypos.config.Constants;
import vn.softdreams.easypos.constants.BusinessTypeConstants;
import vn.softdreams.easypos.constants.ResultConstants;
import vn.softdreams.easypos.domain.BusinessType;
import vn.softdreams.easypos.domain.User;
import vn.softdreams.easypos.dto.businessType.GetAllTransactionsResponse;
import vn.softdreams.easypos.repository.BusinessTypeRepository;
import vn.softdreams.easypos.service.BusinessTypeManagementService;
import vn.softdreams.easypos.service.UserService;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.web.rest.errors.BadRequestAlertException;
import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import java.time.ZonedDateTime;
import java.util.*;

@Service
@Transactional
public class BusinessTypeManagementServiceImpl implements BusinessTypeManagementService {

    private final BusinessTypeRepository businessTypeRepository;
    private final UserService userService;
    private final String ENTITY_NAME = "business-type";

    public BusinessTypeManagementServiceImpl(BusinessTypeRepository businessTypeRepository, UserService userService) {
        this.businessTypeRepository = businessTypeRepository;
        this.userService = userService;
    }

    @Override
    public ResultDTO getAllTransactions(Integer comId, Integer type, String keyword) {
        userService.getUserWithAuthorities(comId);
        List<GetAllTransactionsResponse> responses = businessTypeRepository.getAllTransactions(comId, type, keyword);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_GET_BUSINESS_TYPE, true, responses, responses.size());
    }

    @Override
    public ResultDTO create(Integer comId, String name, Integer type) {
        userService.getUserWithAuthorities(comId);
        BusinessType businessType = new BusinessType();
        if (Objects.equals(type, BusinessTypeConstants.Type.RECEIPT)) {
            List<BusinessType> businessTypeList = businessTypeRepository.findByBusinessTypeNameAndComIdAndType(
                name.trim(),
                comId,
                BusinessTypeConstants.TypeName.RECEIPT
            );
            if (businessTypeList.size() > 0) {
                return new ResultDTO(ResultConstants.FAIL, ExceptionConstants.BUSINESS_TYPE_NAME_EXISTED_VI);
            }
            businessType.setType(BusinessTypeConstants.TypeName.RECEIPT);
            businessType.setBusinessTypeCode(userService.genCode(comId, Constants.THU));
        } else if (Objects.equals(type, BusinessTypeConstants.Type.PAYMENT)) {
            List<BusinessType> businessTypeList = businessTypeRepository.findByBusinessTypeNameAndComIdAndType(
                name.trim(),
                comId,
                BusinessTypeConstants.TypeName.PAYMENT
            );
            if (businessTypeList.size() > 0) {
                return new ResultDTO(ResultConstants.FAIL, ExceptionConstants.BUSINESS_TYPE_NAME_EXISTED_VI);
            }
            businessType.setType(BusinessTypeConstants.TypeName.PAYMENT);
            businessType.setBusinessTypeCode(userService.genCode(comId, Constants.CHI));
        } else {
            throw new BadRequestAlertException(ExceptionConstants.TYPE_INVALID_VI, ENTITY_NAME, ExceptionConstants.TYPE_INVALID);
        }

        businessType.setComId(comId);
        if (Strings.isNullOrEmpty(name) || Strings.isNullOrEmpty(name.trim())) {
            return new ResultDTO(ResultConstants.FAIL, ExceptionConstants.BUSINESS_TYPE_NAME_INVALID_VI);
        }
        businessType.setBusinessTypeName(name.trim());
        businessTypeRepository.save(businessType);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_CREATE_BUSINESS_TYPE, true);
    }

    @Override
    public ResultDTO update(Integer comId, Integer id, String name, Integer type) {
        User user = userService.getUserWithAuthorities(comId);
        Optional<BusinessType> businessTypeOptional = businessTypeRepository.findByIdAndComId(id, user.getCompanyId());
        if (businessTypeOptional.isEmpty()) {
            throw new BadRequestAlertException(
                ExceptionConstants.BUSINESS_TYPE_NOT_FOUND_VI,
                ENTITY_NAME,
                ExceptionConstants.BUSINESS_TYPE_NOT_FOUND
            );
        }
        BusinessType businessType = businessTypeOptional.get();
        if (
            Objects.equals(type, BusinessTypeConstants.Type.RECEIPT) &&
            !businessType.getType().equals(BusinessTypeConstants.TypeName.RECEIPT)
        ) {
            throw new BadRequestAlertException(
                ExceptionConstants.BUSINESS_TYPE_NOT_FOUND_VI,
                ENTITY_NAME,
                ExceptionConstants.BUSINESS_TYPE_NOT_FOUND
            );
        }
        if (
            Objects.equals(type, BusinessTypeConstants.Type.PAYMENT) &&
            !businessType.getType().equals(BusinessTypeConstants.TypeName.PAYMENT)
        ) {
            throw new BadRequestAlertException(
                ExceptionConstants.BUSINESS_TYPE_NOT_FOUND_VI,
                ENTITY_NAME,
                ExceptionConstants.BUSINESS_TYPE_NOT_FOUND
            );
        }
        String typeCode = businessType.getBusinessTypeCode();
        List<String> codes = List.of(
            BusinessTypeConstants.RsInWard.IN_WARD,
            BusinessTypeConstants.RsOutWard.OUT_WARD,
            BusinessTypeConstants.Code.OUT_UNKNOWN,
            BusinessTypeConstants.Code.PAYMENT_UNKNOWN,
            BusinessTypeConstants.Code.RECEIPT_UNKNOWN,
            BusinessTypeConstants.Code.OUT_WARD,
            BusinessTypeConstants.Code.IN_WARD,
            BusinessTypeConstants.Code.RETURN_BILL,
            BusinessTypeConstants.Code.CANCEL_BILL,
            BusinessTypeConstants.Code.OUT_PRODUCT,
            BusinessTypeConstants.Code.DEBT_COLLECTION
        );

        if (codes.contains(typeCode)) {
            throw new BadRequestAlertException(
                ExceptionConstants.BUSINESS_TYPE_NAME_UPDATE_BANNED_VI,
                ENTITY_NAME,
                ExceptionConstants.BUSINESS_TYPE_NAME_UPDATE_BANNED
            );
        }

        if (Objects.equals(type, BusinessTypeConstants.Type.RECEIPT)) {
            List<BusinessType> businessTypeList = businessTypeRepository.findByBusinessTypeNameAndComIdAndType(
                name.trim(),
                comId,
                BusinessTypeConstants.TypeName.RECEIPT
            );
            if (businessTypeList.size() > 1 || (businessTypeList.size() == 1 && !businessTypeList.get(0).getId().equals(id))) {
                throw new BadRequestAlertException(
                    ExceptionConstants.BUSINESS_TYPE_NAME_EXISTED_VI,
                    ENTITY_NAME,
                    ExceptionConstants.BUSINESS_TYPE_NAME_EXISTED
                );
            }
        } else if (Objects.equals(type, BusinessTypeConstants.Type.PAYMENT)) {
            List<BusinessType> businessTypeList = businessTypeRepository.findByBusinessTypeNameAndComIdAndType(
                name.trim(),
                comId,
                BusinessTypeConstants.TypeName.PAYMENT
            );
            if (businessTypeList.size() > 1 || (businessTypeList.size() == 1 && !businessTypeList.get(0).getId().equals(id))) {
                throw new BadRequestAlertException(
                    ExceptionConstants.BUSINESS_TYPE_NAME_EXISTED_VI,
                    ENTITY_NAME,
                    ExceptionConstants.BUSINESS_TYPE_NAME_EXISTED
                );
            }
        } else {
            throw new BadRequestAlertException(ExceptionConstants.TYPE_INVALID_VI, ENTITY_NAME, ExceptionConstants.TYPE_INVALID);
        }
        businessType.setBusinessTypeName(name.trim());
        businessType.setUpdateTime(ZonedDateTime.now());
        businessTypeRepository.save(businessType);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_UPDATE_BUSINESS_TYPE, true);
    }

    @Override
    public ResultDTO insertDefault(List<String> codes, Integer type) {
        List<Integer> comIds = businessTypeRepository.getAllCompanyId();
        List<BusinessType> saveList = new ArrayList<>();
        for (String code : codes) {
            List<BusinessType> businessTypes = businessTypeRepository.findByBusinessTypeCodeAndComIdIn(code, comIds);
            Map<Integer, BusinessType> map = new HashMap<>();
            for (BusinessType businessType : businessTypes) {
                map.put(businessType.getComId(), businessType);
                if (
                    (
                        businessType.getBusinessTypeCode().equals(BusinessTypeConstants.Code.RETURN_BILL) &&
                        !businessType.getBusinessTypeName().equals(BusinessTypeConstants.TypeName.RETURN_BILL)
                    ) ||
                    (
                        businessType.getBusinessTypeCode().equals(BusinessTypeConstants.Code.CANCEL_BILL) &&
                        !businessType.getBusinessTypeName().equals(BusinessTypeConstants.TypeName.CANCEL_BILL)
                    )
                ) {
                    String oldName = businessType.getBusinessTypeName();
                    if (businessType.getBusinessTypeCode().equals(BusinessTypeConstants.Code.RETURN_BILL)) {
                        businessType.setBusinessTypeName(BusinessTypeConstants.TypeName.RETURN_BILL);
                    } else if (businessType.getBusinessTypeCode().equals(BusinessTypeConstants.Code.CANCEL_BILL)) {
                        businessType.setBusinessTypeName(BusinessTypeConstants.TypeName.CANCEL_BILL);
                    }
                    BusinessType business = new BusinessType();
                    business.setComId(businessType.getComId());
                    business.setBusinessTypeName(oldName);
                    if (Objects.equals(type, BusinessTypeConstants.Type.RECEIPT)) {
                        business.setType(BusinessTypeConstants.TypeName.RECEIPT);
                        business.setBusinessTypeCode(userService.genCode(businessType.getComId(), Constants.THU));
                    } else if (Objects.equals(type, BusinessTypeConstants.Type.PAYMENT)) {
                        business.setType(BusinessTypeConstants.TypeName.PAYMENT);
                        business.setBusinessTypeCode(userService.genCode(businessType.getComId(), Constants.CHI));
                    }
                    saveList.add(business);
                }
            }
            for (Integer comId : comIds) {
                if (!map.containsKey(comId)) {
                    BusinessType businessType = new BusinessType();
                    businessType.setComId(comId);
                    if (code.equals(BusinessTypeConstants.Code.RETURN_BILL)) {
                        businessType.setBusinessTypeName(BusinessTypeConstants.TypeName.RETURN_BILL);
                    } else if (code.equals(BusinessTypeConstants.Code.CANCEL_BILL)) {
                        businessType.setBusinessTypeName(BusinessTypeConstants.TypeName.CANCEL_BILL);
                    }
                    if (Objects.equals(type, BusinessTypeConstants.Type.RECEIPT)) {
                        businessType.setType(BusinessTypeConstants.TypeName.RECEIPT);
                        businessType.setBusinessTypeCode(userService.genCode(comId, Constants.THU));
                    } else if (Objects.equals(type, BusinessTypeConstants.Type.PAYMENT)) {
                        businessType.setType(BusinessTypeConstants.TypeName.PAYMENT);
                        businessType.setBusinessTypeCode(userService.genCode(comId, Constants.CHI));
                    }
                    saveList.add(businessType);
                }
            }
            businessTypeRepository.saveAll(saveList);
        }
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS, true);
    }
}
