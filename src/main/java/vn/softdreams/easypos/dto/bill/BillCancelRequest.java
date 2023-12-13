package vn.softdreams.easypos.dto.bill;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class BillCancelRequest extends BillDetailResponse {

    @NotNull(message = ExceptionConstants.BILL_ID_NOT_NULL)
    private Integer billId;

    @NotBlank(message = ExceptionConstants.BILL_CODE_NOT_NULL)
    private String billCode;

    public BillCancelRequest() {}

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
