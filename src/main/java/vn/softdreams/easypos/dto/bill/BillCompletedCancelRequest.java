package vn.softdreams.easypos.dto.bill;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class BillCompletedCancelRequest implements Serializable {

    @NotNull(message = ExceptionConstants.COMPANY_ID_NOT_NULL)
    private Integer comId;

    @NotNull(message = ExceptionConstants.BILL_ID_NOT_NULL)
    private Integer billId;

    @NotNull(message = ExceptionConstants.BILL_PAYMENTS_NOT_NULL)
    private String paymentMethod;

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public Integer getBillId() {
        return billId;
    }

    public void setBillId(Integer billId) {
        this.billId = billId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
