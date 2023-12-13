package vn.softdreams.easypos.dto.voucher;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class VoucherSaveCondition implements Serializable {

    @NotNull(message = ExceptionConstants.VOUCHER_TYPE_NOT_NULL)
    @Min(value = 0, message = ExceptionConstants.VOUCHER_DISCOUNT_TYPE_INVALID_CODE)
    @Max(value = 1, message = ExceptionConstants.VOUCHER_DISCOUNT_TYPE_INVALID_CODE)
    private Integer discountType;

    private BigDecimal discountValue;

    @Min(value = 0, message = ExceptionConstants.VOUCHER_BILL_MIN_VALUE_INVALID)
    private BigDecimal minValue;

    private BigDecimal maxValue;

    @Min(value = 0, message = ExceptionConstants.VOUCHER_BILL_GET_QUANTITY_INVALID)
    private BigDecimal getQuantity;

    @Min(value = 0, message = ExceptionConstants.VOUCHER_BILL_BUY_QUANTITY_INVALID)
    private BigDecimal buyQuantity;

    private List<Integer> getProductProductUnitId;
    private List<Integer> getProductGroupId;
    private List<Integer> buyProductProductUnitId;
    private List<Integer> buyProductGroupId;

    public Integer getDiscountType() {
        return discountType;
    }

    public void setDiscountType(Integer discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public BigDecimal getMinValue() {
        return minValue;
    }

    public void setMinValue(BigDecimal minValue) {
        this.minValue = minValue;
    }

    public BigDecimal getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(BigDecimal maxValue) {
        this.maxValue = maxValue;
    }

    public BigDecimal getGetQuantity() {
        return getQuantity;
    }

    public void setGetQuantity(BigDecimal getQuantity) {
        this.getQuantity = getQuantity;
    }

    public BigDecimal getBuyQuantity() {
        return buyQuantity;
    }

    public void setBuyQuantity(BigDecimal buyQuantity) {
        this.buyQuantity = buyQuantity;
    }

    public List<Integer> getGetProductProductUnitId() {
        return getProductProductUnitId;
    }

    public void setGetProductProductUnitId(List<Integer> getProductProductUnitId) {
        this.getProductProductUnitId = getProductProductUnitId;
    }

    public List<Integer> getGetProductGroupId() {
        return getProductGroupId;
    }

    public void setGetProductGroupId(List<Integer> getProductGroupId) {
        this.getProductGroupId = getProductGroupId;
    }

    public List<Integer> getBuyProductProductUnitId() {
        return buyProductProductUnitId;
    }

    public void setBuyProductProductUnitId(List<Integer> buyProductProductUnitId) {
        this.buyProductProductUnitId = buyProductProductUnitId;
    }

    public List<Integer> getBuyProductGroupId() {
        return buyProductGroupId;
    }

    public void setBuyProductGroupId(List<Integer> buyProductGroupId) {
        this.buyProductGroupId = buyProductGroupId;
    }
}
