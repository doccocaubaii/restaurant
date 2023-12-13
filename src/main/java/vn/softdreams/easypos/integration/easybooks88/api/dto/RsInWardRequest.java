package vn.softdreams.easypos.integration.easybooks88.api.dto;

import vn.softdreams.easypos.config.Constants;
import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class RsInWardRequest implements Serializable {

    @NotBlank
    private String code;

    @NotBlank
    private String date;

    private String customerCode;

    @NotNull
    private BigDecimal totalAmount;

    @NotNull
    private BigDecimal totalDiscountAmount;

    @NotNull
    private BigDecimal totalInwardAmount;

    private String mcPaymentCode;
    private String rsInwardOutwardCode;

    @NotNull
    @Valid
    private List<RsInWardDetailRequest> detail;

    public static class RsInWardDetailRequest {

        @NotBlank(message = "EB88Request: " + ExceptionConstants.PRODUCT_CODE_NOT_NULL)
        private String code;

        @NotNull(message = "EB88Request: " + ExceptionConstants.RS_INOUT_WARD_DETAIL_POSITION_NOT_EMPTY)
        @Min(value = 0, message = "EB88Request: " + ExceptionConstants.RS_INOUT_WARD_DETAIL_POSITION_INVALID_EMPTY)
        private Integer position;

        private Boolean isPromotion;

        private Long unitId;

        @NotNull
        private BigDecimal quantity;

        @NotNull
        private BigDecimal unitPrice;

        @NotNull
        private BigDecimal amount;

        @NotNull
        private BigDecimal discountAmount;

        public RsInWardDetailRequest() {}

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = Constants.CODE_PREFIX_EP + code;
        }

        public Integer getPosition() {
            return position;
        }

        public void setPosition(Integer position) {
            this.position = position;
        }

        public Boolean getPromotion() {
            return isPromotion;
        }

        public void setPromotion(Boolean promotion) {
            isPromotion = promotion;
        }

        public Long getUnitId() {
            return unitId;
        }

        public void setUnitId(Long unitId) {
            this.unitId = unitId;
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
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = Constants.CODE_PREFIX_EP + code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = Constants.CODE_PREFIX_EP + customerCode;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalDiscountAmount() {
        return totalDiscountAmount;
    }

    public void setTotalDiscountAmount(BigDecimal totalDiscountAmount) {
        this.totalDiscountAmount = totalDiscountAmount;
    }

    public BigDecimal getTotalInwardAmount() {
        return totalInwardAmount;
    }

    public void setTotalInwardAmount(BigDecimal totalInwardAmount) {
        this.totalInwardAmount = totalInwardAmount;
    }

    public String getMcPaymentCode() {
        return mcPaymentCode;
    }

    public void setMcPaymentCode(String mcPaymentCode) {
        this.mcPaymentCode = Constants.CODE_PREFIX_EP + mcPaymentCode;
    }

    public String getRsInwardOutwardCode() {
        return rsInwardOutwardCode;
    }

    public void setRsInwardOutwardCode(String rsInwardOutwardCode) {
        this.rsInwardOutwardCode = Constants.CODE_PREFIX_EP + rsInwardOutwardCode;
    }

    public List<RsInWardDetailRequest> getDetail() {
        return detail;
    }

    public void setDetail(List<RsInWardDetailRequest> detail) {
        this.detail = detail;
    }
}
