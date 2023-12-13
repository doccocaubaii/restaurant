package vn.softdreams.easypos.dto.loyaltyCard;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class SaveLoyaltyCardRequest {

    private Integer id;

    @NotBlank(message = ExceptionConstants.CARD_NAME_NOT_NULL)
    private String name;

    private Boolean isDefault;

    @Min(value = 0, message = ExceptionConstants.CARD_STATUS_INVALID)
    @Max(value = 1, message = ExceptionConstants.CARD_STATUS_INVALID)
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
