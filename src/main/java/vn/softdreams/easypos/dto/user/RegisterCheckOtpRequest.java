package vn.softdreams.easypos.dto.user;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

public class RegisterCheckOtpRequest implements Serializable {

    @NotBlank(message = ExceptionConstants.USERNAME_NOT_NULL)
    private String username;

    @NotBlank(message = ExceptionConstants.OTP_NOT_NULL)
    private String otp;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
