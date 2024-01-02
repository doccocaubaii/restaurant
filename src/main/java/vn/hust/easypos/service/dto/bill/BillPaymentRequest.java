package vn.hust.easypos.service.dto.bill;

import java.math.BigDecimal;

public class BillPaymentRequest {

    private String paymentMethod;

    private BigDecimal amount;

    public BillPaymentRequest() {}

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
