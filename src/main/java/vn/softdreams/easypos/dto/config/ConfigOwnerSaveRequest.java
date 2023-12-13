package vn.softdreams.easypos.dto.config;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ConfigOwnerSaveRequest {

    @NotNull(message = ExceptionConstants.COMPANY_ID_NOT_NULL)
    private Integer ownerId;

    @NotBlank(message = ExceptionConstants.CONFIG_CODE_NOT_BLANK)
    private String code;

    private String value;
    private String description;

    public ConfigOwnerSaveRequest() {}

    public ConfigOwnerSaveRequest(Integer ownerId, String code, String value, String description) {
        this.ownerId = ownerId;
        this.code = code;
        this.value = value;
        this.description = description;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public String getCode() {
        return code.trim();
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value == null ? null : value.trim();
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
