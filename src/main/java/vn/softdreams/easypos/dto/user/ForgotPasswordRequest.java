package vn.softdreams.easypos.dto.user;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ForgotPasswordRequest {

    @NotBlank(message = ExceptionConstants.USERNAME_NOT_NULL)
    @Size(max = 100, message = ExceptionConstants.USER_NAME_INVALID)
    private String username;

    public ForgotPasswordRequest(String username) {
        this.username = username;
    }

    public ForgotPasswordRequest() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
