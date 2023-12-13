package vn.softdreams.easypos.dto.employee;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

import static vn.softdreams.easypos.web.rest.errors.ExceptionConstants.*;

public class EmployeeCreateRequest implements Serializable {

    @NotNull(message = EMPLOYEE_COM_ID_MUST_NOT_NULL)
    private Integer comId;

    @Size(max = 100, message = EMPLOYEE_NAME_INVALID)
    @NotBlank(message = EMPLOYEE_NAME_MUST_NOT_EMPTY)
    private String name;

    @NotNull(message = EMPLOYEE_ROLE_ID_MUST_NOT_NULL)
    private Integer roleId;

    @NotBlank(message = EMPLOYEE_ROLE_NAME_MUST_NOT_EMPTY)
    private String roleName;

    @Size(min = 10, max = 14, message = USER_NAME_INVALID)
    private String username;

    @Size(min = 10, max = 14, message = PHONE_NUMBER_IN_VALID)
    private String phoneNumber;

    private String email;

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getUsername() {
        return username == null ? null : username.trim();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber != null ? phoneNumber.trim() : null;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getEmail() {
        return email != null ? email.trim() : null;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
