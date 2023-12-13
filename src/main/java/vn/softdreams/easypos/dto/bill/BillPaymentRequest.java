package vn.softdreams.easypos.dto.bill;

import java.math.BigDecimal;

public class BillPaymentRequest {

    //    private Integer id;
    private String paymentMethod;

    private BigDecimal amount;
    private BigDecimal cardAmount;

    private BigDecimal refund;

    private Integer debtType;

    private BigDecimal debt;

    public BillPaymentRequest() {}

    //    public Integer getId() {
    //        return id;
    //    }
    //
    //    public void setId(Integer id) {
    //        this.id = id;
    //    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public BigDecimal getCardAmount() {
        return cardAmount;
    }

    public void setCardAmount(BigDecimal cardAmount) {
        this.cardAmount = cardAmount;
    }

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

    public Integer getDebtType() {
        return debtType;
    }

    public void setDebtType(Integer debtType) {
        this.debtType = debtType != null ? debtType : 0;
    }

    public BigDecimal getDebt() {
        return debt;
    }

    public void setDebt(BigDecimal debt) {
        this.debt = debt;
    }
}
