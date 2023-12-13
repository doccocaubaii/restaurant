package vn.softdreams.easypos.dto.bill;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class PayOffDebtRequest {

    @NotNull(message = ExceptionConstants.BILL_ID_NOT_NULL)
    @Min(value = 0, message = ExceptionConstants.BILL_ID_IN_VALID)
    private Integer id;

    @NotNull(message = ExceptionConstants.AMOUNT_DEBT_NOT_NULL)
    @Min(value = 0, message = ExceptionConstants.AMOUNT_DEBT_INVALID)
    private BigDecimal amount;

    @NotNull(message = ExceptionConstants.AMOUNT_DEBT_NOT_NULL)
    @Min(value = 0, message = ExceptionConstants.AMOUNT_DEBT_INVALID)
    private BigDecimal cardAmount;

    private String note;

    public PayOffDebtRequest() {}

    public PayOffDebtRequest(Integer id, BigDecimal amount, String note) {
        this.id = id;
        this.amount = amount;
        this.note = note;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public BigDecimal getCardAmount() {
        return cardAmount;
    }

    public void setCardAmount(BigDecimal cardAmount) {
        this.cardAmount = cardAmount;
    }
}
