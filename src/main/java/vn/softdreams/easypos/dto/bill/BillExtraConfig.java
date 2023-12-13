package vn.softdreams.easypos.dto.bill;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BillExtraConfig implements Serializable {

    private BigDecimal amountVat10;
    private BigDecimal amountVat8;
    private BigDecimal svc5;
    private BigDecimal totalAmount;

    public BigDecimal getAmountVat10() {
        return amountVat10;
    }

    public void setAmountVat10(BigDecimal amountVat10) {
        this.amountVat10 = amountVat10;
    }

    public BigDecimal getAmountVat8() {
        return amountVat8;
    }

    public void setAmountVat8(BigDecimal amountVat8) {
        this.amountVat8 = amountVat8;
    }

    public BigDecimal getSvc5() {
        return svc5;
    }

    public void setSvc5(BigDecimal svc5) {
        this.svc5 = svc5;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
