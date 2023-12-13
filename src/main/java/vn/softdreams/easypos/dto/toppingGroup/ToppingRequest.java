package vn.softdreams.easypos.dto.toppingGroup;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.NotNull;

public class ToppingRequest {

    @NotNull(message = ExceptionConstants.TOPPING_ID_NOT_NULL)
    private Integer id;

    @NotNull(message = ExceptionConstants.IS_TOPPING_NOT_NULL)
    private Boolean isTopping;

    public ToppingRequest() {}

    public ToppingRequest(Integer id, Boolean isTopping) {
        this.id = id;
        this.isTopping = isTopping;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getIsTopping() {
        return isTopping;
    }

    public void setIsTopping(Boolean topping) {
        isTopping = topping;
    }
}
