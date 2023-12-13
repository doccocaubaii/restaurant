package vn.softdreams.easypos.dto.bill;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public class BillProductRequest {

    private Integer id;

    @NotNull(message = ExceptionConstants.PRODUCT_ID_NOT_NULL)
    @Min(value = 0, message = ExceptionConstants.PRODUCT_ID_NOT_VALID)
    private Integer productProductUnitId;

    @NotBlank(message = ExceptionConstants.PRODUCT_NAME_NOT_NULL)
    private String productName;

    @NotBlank(message = ExceptionConstants.PRODUCT_CODE_NOT_NULL)
    private String productCode;

    @NotNull(message = ExceptionConstants.QUANTITY_PRODUCT_NOT_NULL)
    @Min(value = 0, message = ExceptionConstants.QUANTITY_INVALID)
    @Digits(integer = 21, fraction = 6, message = ExceptionConstants.QUANTITY_PRODUCT_INVALID)
    private BigDecimal quantity;

    private String unit;

    private Integer unitId;

    @NotNull(message = ExceptionConstants.UNIT_PRICE_NOT_NULL)
    @Digits(integer = 21, fraction = 6, message = ExceptionConstants.UNIT_PRICE_INVALID)
    private BigDecimal unitPrice;

    @NotNull(message = ExceptionConstants.AMOUNT_NOT_NULL)
    @Min(value = 0, message = ExceptionConstants.AMOUNT_GREATER_THAN_ZERO)
    @Digits(integer = 21, fraction = 6, message = ExceptionConstants.AMOUNT_INVALID)
    private BigDecimal amount;

    private BigDecimal discountAmount;

    @NotNull(message = ExceptionConstants.TOTAL_PRETAX_NOT_NULL)
    private BigDecimal totalPreTax;

    private Integer vatRate;

    private BigDecimal vatAmount;

    @NotNull(message = ExceptionConstants.TOTAL_AMOUNT_NOT_NULL)
    private BigDecimal totalAmount;

    @NotNull(message = ExceptionConstants.FEATURE_NOT_NULL)
    private Integer feature;

    @NotNull(message = ExceptionConstants.POSITION_ID_INVALID)
    @Min(value = 1, message = ExceptionConstants.POSITION_ID_INVALID)
    private Integer position;

    private Integer discountVatRate;
    private BigDecimal totalDiscount;

    @Valid
    private List<BillProductToppingRequest> toppings;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public BillProductRequest() {}

    //    public Integer getId() {
    //        return id;
    //    }
    //
    //    public void setId(Integer id) {
    //        this.id = id;
    //    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Integer getProductProductUnitId() {
        return productProductUnitId;
    }

    public void setProductProductUnitId(Integer productProductUnitId) {
        this.productProductUnitId = productProductUnitId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getTotalPreTax() {
        return totalPreTax;
    }

    public void setTotalPreTax(BigDecimal totalPreTax) {
        this.totalPreTax = totalPreTax;
    }

    public Integer getVatRate() {
        return vatRate;
    }

    public void setVatRate(Integer vatRate) {
        this.vatRate = vatRate;
    }

    public BigDecimal getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(BigDecimal vatAmount) {
        this.vatAmount = vatAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getFeature() {
        return feature;
    }

    public void setFeature(Integer feature) {
        this.feature = feature;
    }

    public List<BillProductToppingRequest> getToppings() {
        return toppings;
    }

    public void setToppings(List<BillProductToppingRequest> toppings) {
        this.toppings = toppings;
    }

    public Integer getDiscountVatRate() {
        return discountVatRate;
    }

    public void setDiscountVatRate(Integer discountVatRate) {
        this.discountVatRate = discountVatRate;
    }

    public BigDecimal getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(BigDecimal totalDiscount) {
        this.totalDiscount = totalDiscount;
    }
}
