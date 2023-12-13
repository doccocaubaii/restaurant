package vn.softdreams.easypos.integration.easybooks88.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class PackageCreateResponse implements Serializable {

    @JsonProperty
    private Integer systemCode;

    @JsonProperty
    private String companyTaxCode;

    @JsonProperty
    private Integer countVoucher;

    @JsonProperty
    private Integer productID;

    @JsonProperty
    private List<Object> listCompanyName;

    @JsonProperty
    private Integer countOrganizationUnit;

    @JsonProperty
    private Integer countUser;

    @JsonProperty
    private Boolean status;

    @JsonProperty
    private String message;

    public PackageCreateResponse() {}

    public PackageCreateResponse(
        Integer systemCode,
        String companyTaxCode,
        Integer countVoucher,
        Integer productID,
        List<Object> listCompanyName,
        Integer countOrganizationUnit,
        Integer countUser,
        Boolean status,
        String message
    ) {
        this.systemCode = systemCode;
        this.companyTaxCode = companyTaxCode;
        this.countVoucher = countVoucher;
        this.productID = productID;
        this.listCompanyName = listCompanyName;
        this.countOrganizationUnit = countOrganizationUnit;
        this.countUser = countUser;
        this.status = status;
        this.message = message;
    }

    public Integer getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(Integer systemCode) {
        this.systemCode = systemCode;
    }

    public String getCompanyTaxCode() {
        return companyTaxCode;
    }

    public void setCompanyTaxCode(String companyTaxCode) {
        this.companyTaxCode = companyTaxCode;
    }

    public Integer getCountVoucher() {
        return countVoucher;
    }

    public void setCountVoucher(Integer countVoucher) {
        this.countVoucher = countVoucher;
    }

    public Integer getProductID() {
        return productID;
    }

    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    public List<Object> getListCompanyName() {
        return listCompanyName;
    }

    public void setListCompanyName(List<Object> listCompanyName) {
        this.listCompanyName = listCompanyName;
    }

    public Integer getCountOrganizationUnit() {
        return countOrganizationUnit;
    }

    public void setCountOrganizationUnit(Integer countOrganizationUnit) {
        this.countOrganizationUnit = countOrganizationUnit;
    }

    public Integer getCountUser() {
        return countUser;
    }

    public void setCountUser(Integer countUser) {
        this.countUser = countUser;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
