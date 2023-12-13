package vn.softdreams.easypos.service.dto;

import java.util.List;

public class RolePermissionRequestDTO {

    private String name;
    private String code;
    private List<String> rolePermissions;
    private Boolean isSample;

    public Boolean getSample() {
        return isSample;
    }

    public void setSample(Boolean sample) {
        isSample = sample;
    }

    public RolePermissionRequestDTO() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<String> getRolePermissions() {
        return rolePermissions;
    }

    public void setRolePermissions(List<String> rolePermissions) {
        this.rolePermissions = rolePermissions;
    }
}
