package vn.softdreams.easypos.dto.user;

import com.google.common.base.Strings;
import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class RegisterRequest implements Serializable {

    private Integer comId;

    @NotBlank(message = ExceptionConstants.COMPANY_NAME_NOT_EMPTY)
    private String companyName;

    @Size(max = 14, message = ExceptionConstants.TAX_CODE_IN_VALID)
    private String companyTaxCode;

    private String companyAddress;

    private String fullName;

    @NotBlank(message = ExceptionConstants.USERNAME_NOT_NULL)
    @Size(max = 100, message = ExceptionConstants.USER_NAME_INVALID)
    private String username;

    //    @NotBlank(message = ExceptionConstants.EMAIL_NOT_EMPTY)
    @Email(message = ExceptionConstants.EMAIL_IN_VALID)
    private String email;

    @NotBlank(message = ExceptionConstants.PHONE_NUMBER_NOT_EMPTY)
    @Size(max = 20, message = ExceptionConstants.PHONE_NUMBER_IN_VALID)
    private String phoneNumber;

    //    @NotNull(message = ExceptionConstants.COMPANY_USER_PACKAGE_ID_NOT_NULL)
    private Integer packageId;

    @NotBlank(message = ExceptionConstants.COMPANY_USER_START_DATE_NOT_EMPTY)
    private String startDate;

    @NotBlank(message = ExceptionConstants.COMPANY_USER_END_DATE_NOT_EMPTY)
    private String endDate;

    //    @NotNull(message = ExceptionConstants.COMPANY_USER_PACK_COUNT_NOT_EMPTY)
    private Integer packCount = 10;

    private String hashCode;

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public String getHashCode() {
        return hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public RegisterRequest() {}

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyTaxCode() {
        return companyTaxCode;
    }

    public void setCompanyTaxCode(String companyTaxCode) {
        this.companyTaxCode = companyTaxCode;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return Strings.isNullOrEmpty(email) ? "" : email.toLowerCase();
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber != null ? phoneNumber.trim() : null;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getPackageId() {
        return packageId;
    }

    public void setPackageId(Integer packageId) {
        this.packageId = packageId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getPackCount() {
        return packCount;
    }

    public void setPackCount(Integer packCount) {
        this.packCount = packCount;
    }
}
