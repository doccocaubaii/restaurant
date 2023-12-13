package vn.softdreams.easypos.integration.easybooks88.api.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

public class RegisterCompanyRequest implements Serializable {

    @NotEmpty(message = "companyName is required")
    private String companyName;

    @NotEmpty(message = "companyTaxCode is required")
    private String companyTaxCode;

    private String fullName;

    @NotEmpty(message = "userName is required")
    @Email
    private String userName;

    @NotEmpty(message = "servicePackage is required")
    private String servicePackage;

    @NotEmpty(message = "startDate is required")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "startDate is invalid, must be yyyy-MM-dd")
    private String startDate;

    @NotEmpty
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "endDate is invalid, must be yyyy-MM-dd")
    private String endDate;

    @NotEmpty(message = "packCount is required")
    private String packCount;

    @NotEmpty(message = "hash is required")
    private String hash;

    @NotEmpty(message = "password is required")
    private String password;

    private Integer type;
    private String clientId = "EasyPOSClient";
    private String clientSecret = "EasyPOS@2o23#";

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getServicePackage() {
        return servicePackage;
    }

    public void setServicePackage(String servicePackage) {
        this.servicePackage = servicePackage;
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

    public String getPackCount() {
        return packCount;
    }

    public void setPackCount(String packCount) {
        this.packCount = packCount;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
