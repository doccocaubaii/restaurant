package vn.softdreams.easypos.dto.bill;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.NotNull;

public class DeleteDishRequest {

    @NotNull(message = ExceptionConstants.BILL_ID_NOT_NULL)
    private Integer billId;

    @NotNull(message = ExceptionConstants.KITCHEN_ID_NOT_NULL)
    private Integer id;

    public Integer getBillId() {
        return billId;
    }

    public void setBillId(Integer billId) {
        this.billId = billId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
