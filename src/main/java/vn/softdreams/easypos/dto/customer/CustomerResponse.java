package vn.softdreams.easypos.dto.customer;

import com.fasterxml.jackson.annotation.JsonFormat;
import vn.softdreams.easypos.config.Constants;
import vn.softdreams.easypos.dto.company.CompanyRequest;
import vn.softdreams.easypos.dto.customerCard.CustomerCardInformation;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class CustomerResponse extends CompanyRequest implements Serializable {

    private Integer id;
    private String name;
    private Integer gender;
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
    private String birthday;
    private String note;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    private ZonedDateTime createTime;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    private ZonedDateTime updateTime;

    private Integer pointBalance;
    private BigDecimal moneyBalance;

    private CustomerCardInformation cardInformation;

    public CustomerResponse() {}

    public CustomerResponse(
        Integer id,
        Integer comId,
        String name,
        Integer gender,
        Integer type,
        String code,
        String code2,
        String address,
        String phoneNumber,
        String email,
        String taxCode,
        String idNumber,
        String description,
        String city,
        String district,
        String birthday,
        ZonedDateTime createTime,
        ZonedDateTime updateTime
    ) {
        this.id = id;
        this.comId = comId;
        this.type = type;
        this.name = name;
        this.gender = gender;
        this.code = code;
        this.code2 = code2;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.taxCode = taxCode;
        this.idNumber = idNumber;
        this.description = description;
        this.city = city;
        this.district = district;
        this.birthday = birthday;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public CustomerCardInformation getCardInformation() {
        return cardInformation;
    }

    public void setCardInformation(CustomerCardInformation cardInformation) {
        this.cardInformation = cardInformation;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public Integer getPointBalance() {
        return pointBalance;
    }

    public void setPointBalance(Integer pointBalance) {
        this.pointBalance = pointBalance;
    }

    public BigDecimal getMoneyBalance() {
        return moneyBalance;
    }

    public void setMoneyBalance(BigDecimal moneyBalance) {
        this.moneyBalance = moneyBalance;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
