package vn.softdreams.easypos.dto.product;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.NotNull;

public class DeleteConversionUnitRequest {

    @NotNull(message = ExceptionConstants.PRODUCT_UNIT_ID_MUST_NOT_EMPTY)
    private Integer id;

    @NotNull(message = ExceptionConstants.COMPANY_ID_NOT_NULL)
    private Integer comId;

    public DeleteConversionUnitRequest() {}

    public DeleteConversionUnitRequest(Integer id, Integer comId) {
        this.id = id;
        this.comId = comId;
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
}
