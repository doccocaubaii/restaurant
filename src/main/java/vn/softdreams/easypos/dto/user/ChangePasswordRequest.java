package vn.softdreams.easypos.dto.user;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ChangePasswordRequest {

    @NotNull(message = ExceptionConstants.PASSWORD_IN_VALID)
    @Size(min = 6, message = ExceptionConstants.PASSWORD_IN_VALID)
    private String oldPassword;

    @NotNull(message = ExceptionConstants.PASSWORD_IN_VALID)
    @Size(min = 6, message = ExceptionConstants.PASSWORD_IN_VALID)
    private String newPassword;

    @NotNull(message = ExceptionConstants.PASSWORD_IN_VALID)
    @Size(min = 6, message = ExceptionConstants.PASSWORD_IN_VALID)
    private String confirmPassword;

    private Boolean isLogoutAll;

    public ChangePasswordRequest() {}

    public ChangePasswordRequest(String oldPassword, String newPassword, String confirmPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
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

    public Boolean getIsLogoutAll() {
        return isLogoutAll == null ? Boolean.FALSE : isLogoutAll;
    }

    public void setIsLogoutAll(Boolean logoutAll) {
        isLogoutAll = logoutAll;
    }
}
