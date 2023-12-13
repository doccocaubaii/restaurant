package vn.softdreams.easypos.dto.user;

import java.io.Serializable;

public class CompanyResponse implements Serializable {

    private Integer id;
    private String name;
    private String taxCode;
    private String ownerName;
    private String phoneNo;
    private String address;
    private String taxAuthCodePrefix;
    private String deviceCode;
    private String invoicePattern;
    private Integer invoiceType;
    private Integer discountType;

    public CompanyResponse() {}

    // get company
    public CompanyResponse(
        Integer id,
        String name,
        String taxCode,
        String ownerName,
        String phoneNo,
        String address,
        String taxAuthCodePrefix
    ) {
        this.id = id;
        this.name = name;
        this.taxCode = taxCode;
        this.ownerName = ownerName;
        this.phoneNo = phoneNo;
        this.address = address;
        this.taxAuthCodePrefix = taxAuthCodePrefix;
    }

    public CompanyResponse(
        Integer id,
        String name,
        String taxCode,
        String ownerName,
        String phoneNo,
        String address,
        String taxAuthCodePrefix,
        String deviceCode,
        String invoicePattern,
        Integer invoiceType,
        Integer discountType
    ) {
        this.id = id;
        this.name = name;
        this.taxCode = taxCode;
        this.ownerName = ownerName;
        this.phoneNo = phoneNo;
        this.address = address;
        this.taxAuthCodePrefix = taxAuthCodePrefix;
        this.deviceCode = deviceCode;
        this.invoicePattern = invoicePattern;
        this.invoiceType = invoiceType;
        this.discountType = discountType;
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

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTaxAuthCodePrefix() {
        return taxAuthCodePrefix;
    }

    public void setTaxAuthCodePrefix(String taxAuthCodePrefix) {
        this.taxAuthCodePrefix = taxAuthCodePrefix;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getInvoicePattern() {
        return invoicePattern;
    }

    public void setInvoicePattern(String invoicePattern) {
        this.invoicePattern = invoicePattern;
    }

    public Integer getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(Integer invoiceType) {
        this.invoiceType = invoiceType;
    }

    public Integer getDiscountType() {
        return discountType;
    }

    public void setDiscountType(Integer discountType) {
        this.discountType = discountType;
    }
}
