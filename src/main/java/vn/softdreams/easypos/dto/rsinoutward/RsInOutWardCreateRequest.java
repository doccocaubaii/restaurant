package vn.softdreams.easypos.dto.rsinoutward;

import com.fasterxml.jackson.annotation.JsonProperty;
import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class RsInOutWardCreateRequest implements Serializable {

    @NotNull(message = ExceptionConstants.COMPANY_ID_NOT_NULL)
    private Integer comId;

    private Integer billId;

    @NotNull(message = ExceptionConstants.RS_INOUT_WARD_TYPE_NOT_NULL)
    @Min(value = 1, message = ExceptionConstants.RS_INOUT_WARD_TYPE_INVALID)
    @Max(value = 2, message = ExceptionConstants.RS_INOUT_WARD_TYPE_INVALID)
    private Integer type;

    private Integer businessType;

    private String typeDesc;

    private String date;
    private String customerName;
    private Integer customerId;

    @NotNull(message = ExceptionConstants.RS_INOUT_WARD_QUANTITY_NOT_NULL)
    @Min(value = 0, message = ExceptionConstants.RS_INOUT_WARD_QUANTITY_INVALID)
    private BigDecimal quantity;

    @NotNull(message = ExceptionConstants.RS_INOUT_WARD_AMOUNT_NOT_NULL)
    private BigDecimal amount;

    @NotNull(message = ExceptionConstants.RS_INOUT_WARD_DISCOUNT_AMOUNT_NOT_NULL)
    private BigDecimal discountAmount;

    @NotNull(message = ExceptionConstants.RS_INOUT_WARD_COST_AMOUNT_NOT_NULL)
    private BigDecimal costAmount;

    @NotNull(message = ExceptionConstants.RS_INOUT_WARD_TOTAL_AMOUNT_NOT_NULL)
    private BigDecimal totalAmount;

    private String description;

    @NotBlank(message = ExceptionConstants.RS_INOUT_WARD_PAYMENT_METHOD_NOT_BLANK)
    private String paymentMethod;

    @JsonProperty("detail")
    @NotNull(message = ExceptionConstants.RS_INOUT_WARD_DETAIL_NOT_EMPTY)
    @Valid
    private List<DetailsRequest> details;

    public static class DetailsRequest {

        @NotNull(message = ExceptionConstants.RS_INOUT_WARD_DETAIL_POSITION_NOT_EMPTY)
        @Min(value = 0, message = ExceptionConstants.RS_INOUT_WARD_DETAIL_POSITION_INVALID_EMPTY)
        private Integer position;

        @NotBlank(message = ExceptionConstants.PRODUCT_NAME_MUST_NOT_EMPTY)
        private String productName;

        @NotBlank(message = ExceptionConstants.PRODUCT_CODE_NOT_NULL)
        private String productCode;

        @NotNull(message = ExceptionConstants.PRODUCT_ID_NOT_NULL)
        private Integer productProductUnitId;

        private Integer unitId;

        private String unitName;

        @NotNull(message = ExceptionConstants.QUANTITY_NOT_NULL)
        @Min(value = 0, message = ExceptionConstants.QUANTITY_INVALID)
        private BigDecimal quantity;

        @NotNull(message = ExceptionConstants.UNIT_PRICE_NOT_NULL)
        private BigDecimal unitPrice;

        @NotNull(message = ExceptionConstants.RS_INOUT_WARD_DETAIL_AMOUNT_NOT_NULL)
        private BigDecimal amount;

        @NotNull(message = ExceptionConstants.RS_INOUT_WARD_DETAIL_DISCOUNT_AMOUNT_NOT_NULL)
        private BigDecimal discountAmount;

        @NotNull(message = ExceptionConstants.RS_INOUT_WARD_DETAIL_TOTAL_AMOUNT_NOT_NULL)
        private BigDecimal totalAmount;

        private String lotNo;

        public Integer getPosition() {
            return position;
        }

        public void setPosition(Integer position) {
            this.position = position;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

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

        public Integer getUnitId() {
            return unitId;
        }

        public void setUnitId(Integer unitId) {
            this.unitId = unitId;
        }

        public String getUnitName() {
            return unitName;
        }

        public void setUnitName(String unitName) {
            this.unitName = unitName;
        }

        public BigDecimal getQuantity() {
            return quantity;
        }

        public void setQuantity(BigDecimal quantity) {
            this.quantity = quantity;
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

        public BigDecimal getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(BigDecimal totalAmount) {
            this.totalAmount = totalAmount;
        }

        public String getLotNo() {
            return lotNo;
        }

        public void setLotNo(String lotNo) {
            this.lotNo = lotNo;
        }
    }

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public Integer getBillId() {
        return billId;
    }

    public void setBillId(Integer billId) {
        this.billId = billId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public String getDate() {
        return date == null ? null : date.trim();
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCustomerName() {
        return customerName == null ? null : customerName.trim();
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
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

    public BigDecimal getCostAmount() {
        return costAmount;
    }

    public void setCostAmount(BigDecimal costAmount) {
        this.costAmount = costAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public List<DetailsRequest> getDetails() {
        return details;
    }

    public void setDetails(List<DetailsRequest> details) {
        this.details = details;
    }
}
