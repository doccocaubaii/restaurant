package vn.softdreams.easypos.dto.user;

import vn.softdreams.easypos.domain.TaxAuthorityCode;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class TaxAuthorityCodeRequest implements Serializable {

    private String id;
    private Integer companyId;
    private String deviceId;
    private String deviceCode;
    private String taxCode;
    private ZonedDateTime createTime;
    private String updateTime;

    public TaxAuthorityCodeRequest() {}

    public TaxAuthorityCodeRequest(TaxAuthorityCode taxAuthorityCode) {
        id = taxAuthorityCode.getId().toString();
        companyId = taxAuthorityCode.getComId();
        deviceId = taxAuthorityCode.getDeviceId();
        //        taxcode = taxAuthorityCode.gett();
        createTime = taxAuthorityCode.getCreateTime();
        //        updateTime = taxAuthorityCode.getUpdateTime();
        deviceCode = formatDeviceCode(taxAuthorityCode.getDeviceCode());
    }

    public String formatDeviceCode(String deviceCode) {
        String inString = "" + deviceCode;
        String pattern = "0000";
        if (inString.length() == pattern.length()) return inString;
        String prefix = pattern.substring(0, pattern.length() - inString.length());
        return prefix + inString;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public ZonedDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(ZonedDateTime createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
