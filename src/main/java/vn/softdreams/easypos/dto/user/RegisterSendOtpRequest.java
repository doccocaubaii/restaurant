package vn.softdreams.easypos.dto.user;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

public class RegisterSendOtpRequest implements Serializable {

    private String companyTaxCode;
    private String email;

    @NotBlank(message = ExceptionConstants.PHONE_NUMBER_NOT_EMPTY)
    private String phoneNumber;

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

    public String getCompanyTaxCode() {
        return companyTaxCode;
    }

    public void setCompanyTaxCode(String companyTaxCode) {
        this.companyTaxCode = companyTaxCode;
    }
}
