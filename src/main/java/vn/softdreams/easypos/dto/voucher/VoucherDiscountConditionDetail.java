package vn.softdreams.easypos.dto.voucher;

import java.io.Serializable;
import java.math.BigDecimal;

public class VoucherDiscountConditionDetail implements Serializable {

    private Integer type;
    private String desc;
    private Double discountPercent = 0.0;
    private BigDecimal discountValue = BigDecimal.ZERO;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(Double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }
}
