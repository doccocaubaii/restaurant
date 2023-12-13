package vn.softdreams.easypos.dto.user;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AdminChangePasswordRequest {

    @NotNull(message = ExceptionConstants.USERNAME_NOT_NULL)
    @NotBlank(message = ExceptionConstants.USERNAME_NOT_NULL)
    private String username;

    @NotNull(message = ExceptionConstants.PASSWORD_IN_VALID)
    @Size(min = 6, message = ExceptionConstants.PASSWORD_IN_VALID)
    private String newPassword;

    @NotNull(message = ExceptionConstants.PASSWORD_IN_VALID)
    @Size(min = 6, message = ExceptionConstants.PASSWORD_IN_VALID)
    private String confirmPassword;

    @NotBlank(message = ExceptionConstants.OTP_NOT_NULL)
    private String otp;

    @NotBlank(message = ExceptionConstants.HASH_NOT_EMPTY)
    private String hash;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
