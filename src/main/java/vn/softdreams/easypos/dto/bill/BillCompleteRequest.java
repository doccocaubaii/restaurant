package vn.softdreams.easypos.dto.bill;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import vn.softdreams.easypos.config.ZonedDateTimeDeserializer;
import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class BillCompleteRequest {

    @NotNull(message = ExceptionConstants.BILL_ID_NOT_NULL)
    private Integer billId;

    @NotEmpty(message = ExceptionConstants.BILL_CODE_NOT_NULL)
    private String billCode;

    @NotEmpty(message = ExceptionConstants.PAYMENT_METHOD_NOT_NULL)
    private String paymentMethod;

    @NotNull(message = ExceptionConstants.PAYMENT_TIME_NOT_NULL)
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    private ZonedDateTime paymentTime;

    private BigDecimal amount;
    private BigDecimal cardAmount;
    private BigDecimal refund;
    private BigDecimal debt;

    public BillCompleteRequest() {}

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

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public ZonedDateTime getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(ZonedDateTime paymentTime) {
        this.paymentTime = paymentTime;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getCardAmount() {
        return cardAmount;
    }

    public void setCardAmount(BigDecimal cardAmount) {
        this.cardAmount = cardAmount;
    }

    public BigDecimal getRefund() {
        return refund;
    }

    public void setRefund(BigDecimal refund) {
        this.refund = refund;
    }

    public BigDecimal getDebt() {
        return debt;
    }

    public void setDebt(BigDecimal debt) {
        this.debt = debt;
    }
}
