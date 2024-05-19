package vn.hust.easypos.service.dto.bill;

import java.math.BigDecimal;

import jakarta.validation.constraints.*;
import vn.hust.easypos.web.rest.errors.ExceptionConstants;

public class BillProductRequest {

    @NotBlank(message = ExceptionConstants.PRODUCT_ID_NOT_NULL)
    private String productId;

    @NotBlank(message = ExceptionConstants.PRODUCT_NAME_NOT_NULL)
    private String productName;

    @NotBlank(message = ExceptionConstants.PRODUCT_CODE_NOT_NULL)
    private String productCode;

    @NotNull(message = ExceptionConstants.QUANTITY_PRODUCT_NOT_NULL)
    @Min(value = 0, message = ExceptionConstants.QUANTITY_INVALID)
    @Digits(integer = 21, fraction = 6, message = ExceptionConstants.QUANTITY_PRODUCT_INVALID)
    private BigDecimal quantity;

    private String unit;

    @NotNull(message = ExceptionConstants.UNIT_PRICE_NOT_NULL)
    @Digits(integer = 21, fraction = 6, message = ExceptionConstants.UNIT_PRICE_INVALID)
    private BigDecimal unitPrice;

    @NotNull(message = ExceptionConstants.AMOUNT_NOT_NULL)
    @Min(value = 0)
    @Digits(integer = 21, fraction = 6, message = ExceptionConstants.AMOUNT_INVALID)
    private BigDecimal amount;

    @NotNull(message = ExceptionConstants.TOTAL_AMOUNT_NOT_NULL)
    private BigDecimal totalAmount;

    @NotNull(message = ExceptionConstants.POSITION_ID_INVALID)
    @Min(value = 1, message = ExceptionConstants.POSITION_ID_INVALID)
    private Integer position;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public BillProductRequest() {}

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
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

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
