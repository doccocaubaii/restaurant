package vn.softdreams.easypos.dto.voucher;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscountBillBonusDetail extends DiscountBillFromToDetail implements Serializable {

    private BigDecimal getQuantity;
    private List<Integer> getProductProductUnitId;
    private List<Integer> getProductGroupId;

    public List<Integer> getGetProductGroupId() {
        return getProductGroupId;
    }

    public void setGetProductGroupId(List<Integer> getProductGroupId) {
        this.getProductGroupId = getProductGroupId;
    }

    public BigDecimal getGetQuantity() {
        return getQuantity;
    }

    public void setGetQuantity(BigDecimal getQuantity) {
        this.getQuantity = getQuantity;
    }

    public List<Integer> getGetProductProductUnitId() {
        return getProductProductUnitId;
    }

    public void setGetProductProductUnitId(List<Integer> getProductProductUnitId) {
        this.getProductProductUnitId = getProductProductUnitId;
    }
}
