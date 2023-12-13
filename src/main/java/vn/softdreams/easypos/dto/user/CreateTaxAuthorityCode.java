package vn.softdreams.easypos.dto.user;

import java.io.Serializable;

public class CreateTaxAuthorityCode implements Serializable {

    private String deviceId;
    private String companyId;
    private String taxCode;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }
}
