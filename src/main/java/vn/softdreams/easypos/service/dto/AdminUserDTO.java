package vn.softdreams.easypos.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import vn.softdreams.easypos.config.Constants;
import vn.softdreams.easypos.config.ZonedDateTimeDeserializer;
import vn.softdreams.easypos.domain.AbstractAuditingEntity;
import vn.softdreams.easypos.domain.User;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A DTO representing a user, with his authorities.
 */
public class AdminUserDTO extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    @NotBlank
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    private String userName;

    @Size(max = 50)
    private String fullName;

    @Size(min = 5, max = 512)
    private String address;

    @Email
    @NotNull
    @Size(min = 5, max = 254)
    private String email;

    @NotNull
    @Size(max = 10)
    private String phoneNumber;

    @Size(max = 14)
    private String taxCode;

    private Integer status;

    private boolean isManager;

    private String tax_machine_code;

    private String tax_register_message;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    private ZonedDateTime tax_register_time;

    private Set<String> authorities;

    public AdminUserDTO() {
        // Empty constructor needed for Jackson.
    }

    public AdminUserDTO(User user) {
        this.id = user.getId();
        this.userName = user.getUsername();
        this.fullName = user.getFullName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.status = user.getStatus();
        this.isManager = user.getManager() ? user.getManager() : true;
        Set<String> author = new HashSet<>();
        author.add("01");
        this.authorities = user.getAuthorities().size() > 0 ? user.getAuthorities() : author;
    }

    public String getTax_machine_code() {
        return tax_machine_code;
    }

    public void setTax_machine_code(String tax_machine_code) {
        this.tax_machine_code = tax_machine_code;
    }

    public String getTax_register_message() {
        return tax_register_message;
    }

    public void setTax_register_message(String tax_register_message) {
        this.tax_register_message = tax_register_message;
    }

    public ZonedDateTime getTax_register_time() {
        return tax_register_time;
    }

    public void setTax_register_time(ZonedDateTime tax_register_time) {
        this.tax_register_time = tax_register_time;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public boolean isManager() {
        return isManager;
    }

    public void setManager(boolean manager) {
        isManager = manager;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }

    @Override
    public String toString() {
        return (
            "AdminUserDTO{" +
            "id='" +
            id +
            '\'' +
            ", userName='" +
            userName +
            '\'' +
            ", fullName='" +
            fullName +
            '\'' +
            ", email='" +
            email +
            '\'' +
            ", phoneNumber='" +
            phoneNumber +
            '\'' +
            ", status=" +
            status +
            ", isManager=" +
            isManager +
            ", authorities=" +
            authorities +
            '}'
        );
    }
}
