package vn.softdreams.easypos.dto.user;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class RegisterConfigRequest {

    @NotNull(message = ExceptionConstants.COMPANY_ID_NOT_NULL)
    private Integer comId;

    @NotBlank(message = ExceptionConstants.PASSWORD_NOT_BLANK)
    private String password;

    public RegisterConfigRequest() {}

    public RegisterConfigRequest(Integer comId, String password) {
        this.comId = comId;
        this.password = password;
    }

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
