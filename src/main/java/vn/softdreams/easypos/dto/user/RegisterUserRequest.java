package vn.softdreams.easypos.dto.user;

import java.io.Serializable;

public interface RegisterUserRequest extends Serializable {
    String getCompanyName();
    String getCompanyTaxCode();
    String getFullName();
    String getUserName();
    String getServicePackage();
    String getStartDate();
    String getEndDate();
    Integer getPackCount();
}
