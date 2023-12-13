package vn.softdreams.easypos.service;

import vn.softdreams.easypos.dto.epPackage.CRMPackageResponse;
import vn.softdreams.easypos.dto.epPackage.CRMPackageSaveRequest;

public interface PackageManagementService {
    CRMPackageResponse saveFromCRM(CRMPackageSaveRequest request);
}
