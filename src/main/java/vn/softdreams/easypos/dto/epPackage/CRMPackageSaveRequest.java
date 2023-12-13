package vn.softdreams.easypos.dto.epPackage;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.*;
import java.io.Serializable;

public class CRMPackageSaveRequest implements Serializable {

    private Integer package_id;

    @NotBlank(message = ExceptionConstants.PACKAGE_NAME_NOT_BLANK)
    @Size(max = 50, message = ExceptionConstants.PACKAGE_NAME_NOT_BLANK)
    private String name;

    @NotNull(message = ExceptionConstants.PACKAGE_EPX_NOT_NULL)
    private Integer exp;

    private Integer max_inv;

    private String description;

    @NotNull(message = ExceptionConstants.PACKAGE_STATUS_NOT_NULL)
    @Min(value = 0, message = ExceptionConstants.PACKAGE_STATUS_INVALID)
    @Max(value = 1, message = ExceptionConstants.PACKAGE_STATUS_INVALID)
    private Integer status_package;

    public CRMPackageSaveRequest() {}

    public Integer getPackage_id() {
        return package_id;
    }

    public void setPackage_id(Integer package_id) {
        this.package_id = package_id;
    }

    public String getName() {
        return name != null ? name.trim() : null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getExp() {
        return exp;
    }

    public void setExp(Integer exp) {
        this.exp = exp;
    }

    public Integer getMax_inv() {
        return max_inv;
    }

    public void setMax_inv(Integer max_inv) {
        this.max_inv = max_inv;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus_package() {
        return status_package;
    }

    public void setStatus_package(Integer status_package) {
        this.status_package = status_package;
    }
}
