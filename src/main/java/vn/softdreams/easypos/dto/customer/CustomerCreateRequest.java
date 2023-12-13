package vn.softdreams.easypos.dto.customer;

import vn.softdreams.easypos.dto.customerCard.CustomerCardInformation;
import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.*;
import java.io.Serializable;

public class CustomerCreateRequest implements Serializable {

    @NotNull(message = ExceptionConstants.COMPANY_ID_NOT_NULL)
    private Integer comId;

    @NotBlank(message = ExceptionConstants.CUSTOMER_NAME_NOT_EMPTY)
    @Size(max = 400, message = ExceptionConstants.CUSTOMER_NAME_INVALID)
    private String name;

    @Size(max = 100, message = ExceptionConstants.CUSTOMER_CODE2_INVALID)
    private String code2;

    @Size(max = 400, message = ExceptionConstants.CUSTOMER_ADDRESS_INVALID)
    private String address;

    @Size(max = 20, min = 10, message = ExceptionConstants.CUSTOMER_PHONE_INVALID)
    private String phoneNumber;

    @Email(message = ExceptionConstants.CUSTOMER_EMAIL_INVALID)
    @Size(max = 50, message = ExceptionConstants.CUSTOMER_EMAIL_INVALID)
    private String email;

    private String taxCode;

    @Size(max = 12, message = ExceptionConstants.CUSTOMER_ID_NUMBER_INVALID)
    private String idNumber;

    private String description;

    @NotNull(message = ExceptionConstants.CUSTOMER_TYPE_NOT_NULL)
    @Min(value = 1, message = ExceptionConstants.CUSTOMER_TYPE_INVALID)
    @Max(value = 3, message = ExceptionConstants.CUSTOMER_TYPE_INVALID)
    private Integer type;

    //    @NotNull(message = ExceptionConstants.CITY_NOT_NULL)
    private String city;

    private String district;
    private String birthday;

    @Min(value = 1, message = ExceptionConstants.GENDER_INVALID)
    @Max(value = 3, message = ExceptionConstants.GENDER_INVALID)
    private Integer gender;

    private CustomerCardInformation cardInformation;

    public CustomerCreateRequest() {}

    public CustomerCreateRequest(
        Integer comId,
        String name,
        String code2,
        String address,
        String phoneNumber,
        String email,
        String taxCode,
        String idNumber,
        String description,
        Integer type,
        String city,
        String district,
        Integer gender,
        CustomerCardInformation cardInformation
    ) {
        this.comId = comId;
        this.name = name;
        this.code2 = code2;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.taxCode = taxCode;
        this.idNumber = idNumber;
        this.description = description;
        this.type = type;
        this.city = city;
        this.district = district;
        this.gender = gender;
        this.cardInformation = cardInformation;
    }

    public CustomerCreateRequest(
        Integer comId,
        String name,
        String code2,
        String address,
        String phoneNumber,
        String email,
        String taxCode,
        String idNumber,
        String description
    ) {
        this.comId = comId;
        this.name = name;
        this.code2 = code2;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.taxCode = taxCode;
        this.idNumber = idNumber;
        this.description = description;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public String getName() {
        return name == null ? null : name.trim();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode2() {
        return code2 != null ? code2.trim() : null;
    }

    public void setCode2(String code2) {
        this.code2 = code2;
    }

    public String getAddress() {
        return address == null ? null : address.trim();
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber == null ? null : phoneNumber.trim();
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
}
