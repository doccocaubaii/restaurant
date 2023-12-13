package vn.softdreams.easypos.dto.printTemplate;

import java.math.BigDecimal;

public class PrintDataBillPayment {

    //    {khach_Tra}
    private BigDecimal amount;
    //    {Tien_Thua}
    private BigDecimal refund;
    //    {Phuong_Thuc_Thanh_Toan}
    private String paymentMethod;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getRefund() {
        return refund;
    }

    public void setRefund(BigDecimal refund) {
        this.refund = refund;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
