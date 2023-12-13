package vn.softdreams.easypos.dto.loyaltyCard;

import com.fasterxml.jackson.annotation.JsonFormat;
import vn.softdreams.easypos.config.Constants;

import java.time.ZonedDateTime;

public class LoyaltyCardResultItem {

    private Integer id;
    private String name;
    private Boolean isDefault;
    private Integer rank;
    private Integer status;
    private Integer customerId;
    private String customerName;
    private String code;
    private String code2;
    private String address;
    private String phoneNumber;
    private String email;
    private String taxCode;
    private String idNumber;
    private Integer type;
    private String description;
    private String city;
    private String district;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    private ZonedDateTime createTime;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    private ZonedDateTime updateTime;

    public LoyaltyCardResultItem(
        Integer id,
        String name,
        Boolean isDefault,
        Integer rank,
        Integer status,
        Integer customerId,
        String customerName,
        String code,
        String code2,
        String address,
        String phoneNumber,
        String email,
        String taxCode,
        String idNumber,
        Integer type,
        String description,
        String city,
        String district,
        ZonedDateTime createTime,
        ZonedDateTime updateTime
    ) {
        this.id = id;
        this.name = name;
        this.isDefault = isDefault;
        this.rank = rank;
        this.status = status;
        this.customerId = customerId;
        this.customerName = customerName;
        this.code = code;
        this.code2 = code2;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.taxCode = taxCode;
        this.idNumber = idNumber;
        this.type = type;
        this.description = description;
        this.city = city;
        this.district = district;
        this.createTime = createTime;
        this.updateTime = updateTime;
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

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode2() {
        return code2;
    }

    public void setCode2(String code2) {
        this.code2 = code2;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public ZonedDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(ZonedDateTime createTime) {
        this.createTime = createTime;
    }

    public ZonedDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(ZonedDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
