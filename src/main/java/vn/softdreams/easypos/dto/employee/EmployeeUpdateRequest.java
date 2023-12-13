package vn.softdreams.easypos.dto.employee;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

import static vn.softdreams.easypos.web.rest.errors.ExceptionConstants.*;

public class EmployeeUpdateRequest implements Serializable {

    @NotNull(message = EMPLOYEE_ID_MUST_NOT_NULL_VI)
    private Integer id;

    @NotNull(message = EMPLOYEE_COM_ID_MUST_NOT_NULL)
    private Integer comId;

    private String name;
    private Integer roleId;

    @Size(min = 10, max = 14, message = PHONE_NUMBER_IN_VALID)
    private String phoneNumber;

    private String email;

    public EmployeeUpdateRequest() {}

    public EmployeeUpdateRequest(Integer id, Integer comId, String name, Integer roleId, String phoneNumber, String email) {
        this.id = id;
        this.comId = comId;
        this.name = name;
        this.roleId = roleId;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public String getPhoneNumber() {
        return phoneNumber;
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
}
