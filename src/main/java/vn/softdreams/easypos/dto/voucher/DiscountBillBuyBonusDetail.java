package vn.softdreams.easypos.dto.voucher;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscountBillBuyBonusDetail extends DiscountBillBonusDetail implements Serializable {

    private BigDecimal buyQuantity;
    private List<Integer> buyProductProductUnitId;
    private List<Integer> buyProductGroupId;

    public List<Integer> getBuyProductGroupId() {
        return buyProductGroupId;
    }

    public void setBuyProductGroupId(List<Integer> buyProductGroupId) {
        this.buyProductGroupId = buyProductGroupId;
    }

    public BigDecimal getBuyQuantity() {
        return buyQuantity;
    }

    public void setBuyQuantity(BigDecimal buyQuantity) {
        this.buyQuantity = buyQuantity;
    }

    public List<Integer> getBuyProductProductUnitId() {
        return buyProductProductUnitId;
    }

    public void setBuyProductProductUnitId(List<Integer> buyProductProductUnitId) {
        this.buyProductProductUnitId = buyProductProductUnitId;
    }
}
