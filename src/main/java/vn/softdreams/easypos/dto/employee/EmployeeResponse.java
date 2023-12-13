package vn.softdreams.easypos.dto.employee;

import com.fasterxml.jackson.annotation.JsonFormat;
import vn.softdreams.easypos.config.Constants;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class EmployeeResponse implements Serializable {

    private Integer id;
    private Integer comId;
    private String name;
    private Integer roleId;
    private String roleName;
    private String username;
    private String email;
    private String phoneNumber;

    private String note;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    private ZonedDateTime createTime;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    private ZonedDateTime updateTime;

    private Integer creator;

    public EmployeeResponse() {}

    public EmployeeResponse(
        Integer id,
        Integer comId,
        String name,
        Integer roleId,
        String roleName,
        String username,
        String email,
        String phoneNumber,
        ZonedDateTime createTime,
        ZonedDateTime updateTime,
        Integer creator
    ) {
        this.id = id;
        this.comId = comId;
        this.name = name;
        this.roleId = roleId;
        this.roleName = roleName;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.creator = creator;
    }

    public Integer getCreator() {
        return creator;
    }

    public void setCreator(Integer creator) {
        this.creator = creator;
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

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public ZonedDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(ZonedDateTime createTime) {
        this.createTime = createTime;
    }

    public ZonedDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(ZonedDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
