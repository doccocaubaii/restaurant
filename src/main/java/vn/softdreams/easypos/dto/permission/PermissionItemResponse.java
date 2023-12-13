package vn.softdreams.easypos.dto.permission;

public interface PermissionItemResponse {
    Integer getId();
    String getCode();
    String getParentCode();
    String getName();
    String getDescription();
}
