package vn.softdreams.easypos.dto.company;

import java.io.Serializable;

public class CompanyResult implements Serializable {

    private Integer id;
    private String name;
    private String phone;
    private String address;
    private Integer companyOwnerId;
    private String companyOwnerName;
    private String companyOwnerAddress;
    private String companyOwnerTaxMachineCode;
    private String taxCode;
    private Integer countConfig;
    private Integer businessId;
    private String businessName;
    private String description;
    private String service;

    public CompanyResult() {}

    // get detail company by id
    public CompanyResult(
        Integer id,
        String name,
        String phone,
        String address,
        Integer businessId,
        String businessName,
        Integer companyOwnerId,
        String companyOwnerName,
        String companyOwnerAddress,
        String companyOwnerTaxMachineCode,
        String taxCode,
        String description
    ) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.businessId = businessId;
        this.businessName = businessName;
        this.companyOwnerId = companyOwnerId;
        this.companyOwnerName = companyOwnerName;
        this.companyOwnerAddress = companyOwnerAddress;
        this.companyOwnerTaxMachineCode = companyOwnerTaxMachineCode;
        this.taxCode = taxCode;
        this.description = description;
    }

    public CompanyResult(
        Integer id,
        String name,
        String phone,
        String address,
        Integer companyOwnerId,
        String companyOwnerName,
        String companyOwnerAddress,
        String companyOwnerTaxMachineCode,
        String taxCode,
        String service
    ) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.companyOwnerId = companyOwnerId;
        this.companyOwnerName = companyOwnerName;
        this.companyOwnerAddress = companyOwnerAddress;
        this.companyOwnerTaxMachineCode = companyOwnerTaxMachineCode;
        this.taxCode = taxCode;
        this.service = service;
    }

    public CompanyResult(
        Integer id,
        String name,
        String phone,
        String address,
        Integer companyOwnerId,
        String companyOwnerName,
        String companyOwnerAddress,
        String companyOwnerTaxMachineCode,
        String taxCode,
        Integer countConfig
    ) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.companyOwnerId = companyOwnerId;
        this.companyOwnerName = companyOwnerName;
        this.companyOwnerAddress = companyOwnerAddress;
        this.companyOwnerTaxMachineCode = companyOwnerTaxMachineCode;
        this.taxCode = taxCode;
        this.countConfig = countConfig;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getCompanyOwnerId() {
        return companyOwnerId;
    }

    public void setCompanyOwnerId(Integer companyOwnerId) {
        this.companyOwnerId = companyOwnerId;
    }

    public String getCompanyOwnerName() {
        return companyOwnerName;
    }

    public void setCompanyOwnerName(String companyOwnerName) {
        this.companyOwnerName = companyOwnerName;
    }

    public String getCompanyOwnerAddress() {
        return companyOwnerAddress;
    }

    public void setCompanyOwnerAddress(String companyOwnerAddress) {
        this.companyOwnerAddress = companyOwnerAddress;
    }

    public String getCompanyOwnerTaxMachineCode() {
        return companyOwnerTaxMachineCode;
    }

    public void setCompanyOwnerTaxMachineCode(String companyOwnerTaxMachineCode) {
        this.companyOwnerTaxMachineCode = companyOwnerTaxMachineCode;
    }

    public Integer getCountConfig() {
        return countConfig;
    }

    public void setCountConfig(Integer countConfig) {
        this.countConfig = countConfig;
    }

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }
}
