package vn.softdreams.easypos.dto.epPackage;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class EBPackageSaveRequest {

    private Integer id;

    @NotBlank(message = ExceptionConstants.PACKAGE_NAME_NOT_BLANK)
    @Size(max = 50, message = ExceptionConstants.PACKAGE_CODE_INVALID)
    private String packageCode;

    private Integer limitedVoucher;

    private Integer limitedUser;
    private Integer limitedCompany;
    private Integer expireTime;
    private Integer comType;
    private String hash;

    public EBPackageSaveRequest() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPackageCode() {
        return packageCode;
    }

    public void setPackageCode(String packageCode) {
        this.packageCode = packageCode;
    }

    public Integer getLimitedVoucher() {
        return limitedVoucher;
    }

    public void setLimitedVoucher(Integer limitedVoucher) {
        this.limitedVoucher = limitedVoucher;
    }

    public Integer getLimitedUser() {
        return limitedUser;
    }

    public void setLimitedUser(Integer limitedUser) {
        this.limitedUser = limitedUser;
    }

    public Integer getLimitedCompany() {
        return limitedCompany;
    }

    public void setLimitedCompany(Integer limitedCompany) {
        this.limitedCompany = limitedCompany;
    }

    public Integer getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Integer expireTime) {
        this.expireTime = expireTime;
    }

    public Integer getComType() {
        return comType;
    }

    public void setComType(Integer comType) {
        this.comType = comType;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
