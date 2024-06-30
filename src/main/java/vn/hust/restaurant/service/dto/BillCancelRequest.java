package vn.hust.restaurant.service.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import vn.hust.restaurant.web.rest.errors.ExceptionConstants;

public class BillCancelRequest {

    @NotNull(message = ExceptionConstants.BILL_ID_NOT_NULL)
    private Integer billId;

    @NotBlank(message = ExceptionConstants.BILL_CODE_NOT_NULL)
    private String billCode;

    public BillCancelRequest() {
    }

    public BillCancelRequest(Integer billId, String billCode) {
        this.billId = billId;
        this.billCode = billCode;
    }

    public BillCancelRequest(String billCode) {
        this.billCode = billCode;
    }

    public Integer getBillId() {
        return billId;
    }

    public void setBillId(Integer billId) {
        this.billId = billId;
    }

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }
}
