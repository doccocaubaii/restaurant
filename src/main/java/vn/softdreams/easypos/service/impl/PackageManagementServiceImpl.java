package vn.softdreams.easypos.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.easypos.constants.PackageConstants;
import vn.softdreams.easypos.constants.ResultConstants;
import vn.softdreams.easypos.domain.EPPackage;
import vn.softdreams.easypos.dto.epPackage.CRMPackageResponse;
import vn.softdreams.easypos.dto.epPackage.CRMPackageSaveRequest;
import vn.softdreams.easypos.dto.epPackage.EBPackageSaveRequest;
import vn.softdreams.easypos.integration.easybooks88.api.EB88ApiClient;
import vn.softdreams.easypos.integration.easybooks88.api.dto.PackageCreateResponse;
import vn.softdreams.easypos.repository.EPPackageRepository;
import vn.softdreams.easypos.service.PackageManagementService;
import vn.softdreams.easypos.util.Common;
import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import java.util.Optional;

@Service
@Transactional
public class PackageManagementServiceImpl implements PackageManagementService {

    private static final String ENTITY_NAME = "Package";
    private final Logger log = LoggerFactory.getLogger(PackageManagementServiceImpl.class);
    private final EPPackageRepository epPackageRepository;
    private final EB88ApiClient eb88ApiClient;

    public PackageManagementServiceImpl(EPPackageRepository epPackageRepository, EB88ApiClient eb88ApiClient) {
        this.epPackageRepository = epPackageRepository;
        this.eb88ApiClient = eb88ApiClient;
    }

    @Override
    public CRMPackageResponse saveFromCRM(CRMPackageSaveRequest request) {
        EPPackage epPackage;
        if (request.getExp() < -1) {
            request.setExp(PackageConstants.DefaultValueForEB88.LIMITED_TIME);
        }
        if (request.getMax_inv() == null || request.getMax_inv() < -1) {
            request.setExp(PackageConstants.DefaultValueForEB88.LIMITED_VOUCHER);
        }
        if (request.getPackage_id() == null) {
            epPackage = new EPPackage();

            CRMPackageResponse checkNameResponse = checkDuplicatePackageName(request.getName(), null);
            if (checkNameResponse != null) {
                return checkNameResponse;
            }
            epPackage.setPackageCode(request.getName());
            epPackage.setPackageName(request.getName());
            epPackage.setTime(request.getExp());
            epPackage.setLimitVoucher(request.getMax_inv());
            epPackage.setLimitCompany(PackageConstants.DefaultValueForEB88.LIMITED_COMPANY_DEFAULT);
            epPackage.setLimitUser(PackageConstants.DefaultValueForEB88.LIMITED_USER_DEFAULT);
            epPackage.setStatus(request.getStatus_package());
            epPackage.setDescription(request.getDescription());
            //            CRMPackageResponse createEBPackage = createPackageToEB(eb88ApiClient, epPackage);
            //            if (createEBPackage != null) {
            //                return createEBPackage;
            //            }
        } else {
            Optional<EPPackage> packageOptional = epPackageRepository.findByIdAndStatus(
                request.getPackage_id(),
                PackageConstants.Status.ACTIVE
            );
            if (packageOptional.isEmpty()) {
                return new CRMPackageResponse(null, PackageConstants.StatusResponse.FAIL, ExceptionConstants.PACKAGE_NOT_EXISTS_VI);
            }
            epPackage = partialUpdate(request, packageOptional.get());
        }
        epPackageRepository.save(epPackage);
        log.info(ENTITY_NAME + "_save" + ResultConstants.PACKAGE_SAVE_SUCCESS_VI);
        return new CRMPackageResponse(epPackage.getId(), PackageConstants.StatusResponse.SUCCESS, ResultConstants.PACKAGE_SAVE_SUCCESS_VI);
    }

    private CRMPackageResponse checkDuplicatePackageName(String packageNameRequest, String packageName) {
        if (!packageNameRequest.equals(packageName) && epPackageRepository.countByPackageName(packageNameRequest) > 0) {
            return new CRMPackageResponse(null, PackageConstants.StatusResponse.FAIL, ExceptionConstants.PACKAGE_NAME_DUPLICATE_VI);
        }
        return null;
    }

    private EPPackage partialUpdate(CRMPackageSaveRequest request, EPPackage existingEPPackage) {
        if (request.getName() != null) {
            checkDuplicatePackageName(request.getName(), existingEPPackage.getPackageName());
            existingEPPackage.setPackageCode(request.getName());
            existingEPPackage.setPackageName(request.getName());
        }
        if (request.getExp() != null) {
            existingEPPackage.setTime(request.getExp());
        }
        if (request.getMax_inv() != null) {
            existingEPPackage.setLimitVoucher(request.getMax_inv());
        }
        if (request.getDescription() != null) {
            existingEPPackage.setDescription(request.getDescription());
        }
        existingEPPackage.setStatus(request.getStatus_package());
        return existingEPPackage;
    }

    private CRMPackageResponse createPackageToEB(EB88ApiClient eb88ApiClient, EPPackage epPackage) {
        EBPackageSaveRequest ebPackageSaveRequest = new EBPackageSaveRequest();
        ebPackageSaveRequest.setPackageCode(epPackage.getPackageCode());
        ebPackageSaveRequest.setExpireTime(epPackage.getTime());
        ebPackageSaveRequest.setComType(PackageConstants.DefaultValueForEB88.COM_TYPE);
        ebPackageSaveRequest.setLimitedCompany(PackageConstants.DefaultValueForEB88.LIMITED_COMPANY_DEFAULT);
        ebPackageSaveRequest.setLimitedUser(PackageConstants.DefaultValueForEB88.LIMITED_USER_DEFAULT);
        ebPackageSaveRequest.setLimitedVoucher(epPackage.getLimitVoucher());
        try {
            ebPackageSaveRequest.setHash(Common.md5(ebPackageSaveRequest.getLimitedVoucher() + PackageConstants.DefaultValueForEB88.HASH));
            PackageCreateResponse response = eb88ApiClient.createPackageFromCRM(ebPackageSaveRequest);
            if (response.getSystemCode() != 1) {
                log.error("Create package to eb88 fail");
                return new CRMPackageResponse(null, PackageConstants.StatusResponse.FAIL, ExceptionConstants.PACKAGE_CREATE_EB88_ERROR_VI);
            }
        } catch (Exception exception) {
            log.error("Can not create package to eb88: {}", exception.getMessage());
            return new CRMPackageResponse(null, PackageConstants.StatusResponse.FAIL, exception.getMessage());
        }
        return null;
    }
}
