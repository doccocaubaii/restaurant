package vn.hust.easypos.web.rest.vm;

import jakarta.validation.constraints.NotEmpty;
import vn.hust.easypos.web.rest.errors.ExceptionConstants;

/**
 * View Model object for storing a user's credentials.
 */
public class LoginVM {

    @NotEmpty(message = ExceptionConstants.USERNAME_NOT_NULL)
    private String username;

    @NotEmpty(message = ExceptionConstants.PASSWORD_IN_VALID)
    private String password;

    public Integer companyId;

    private boolean rememberMe;

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LoginVM{" +
            "username='" + username + '\'' +
            ", rememberMe=" + rememberMe +
            '}';
    }
}
