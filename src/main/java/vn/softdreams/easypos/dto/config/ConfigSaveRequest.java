package vn.softdreams.easypos.dto.config;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.NotBlank;

public class ConfigSaveRequest {

    private Integer id;

    private Integer companyId;

    @NotBlank(message = ExceptionConstants.CONFIG_CODE_NOT_BLANK)
    private String code;

    private String value;
    private String description;

    public ConfigSaveRequest() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCode() {
        return code == null ? null : code.trim();
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
