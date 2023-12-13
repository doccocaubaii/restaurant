package vn.softdreams.easypos.integration.easybooks88.api.dto;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

public class CreateSAInvoiceRequest {

    @NotEmpty(message = "code must be not empty")
    @Pattern(regexp = "^EPDH(\\d){1,18}$", message = "Invalid SAInvoice code format")
    private String code;

    @NotEmpty(message = "date must be not empty")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$", message = "Invalid SAInvoice date format")
    private String date;

    //TODO: Hiện tại Pattern vẫn đang check theo chuẩn tự sinh code của ứng dụng, nếu hỗ trợ code2 do KH tự tạo, phải bỏ đi hoặc có pattern khác
    @NotEmpty(message = "customerCode must be not empty")
    @Pattern(regexp = "^EPKH(\\d){1,18}$", message = "Invalid customer code format")
    private String customerCode;

    @NotEmpty(message = "employeeCode must be not empty")
    @Pattern(regexp = "^EP\\d+_\\d+$", message = "Invalid employee code format (must be EPcomId_userId, ex: EP14_165)")
    private String employeeCode;

    @NotNull(message = "totalAmount must be not null")
    @DecimalMin("0.0")
    private BigDecimal totalAmount;

    @NotNull(message = "totalDiscountAmount must be not null")
    @DecimalMin("0.0")
    private BigDecimal totalDiscountAmount;

    @NotNull(message = "totalAll must be not null")
    @DecimalMin("0.0")
    private BigDecimal totalAll;

    @NotNull(message = "totalVAT must be not null")
    @DecimalMin("0.0")
    private BigDecimal totalVAT;

    @Pattern(regexp = "^EPPT(\\d){1,18}$", message = "Invalid MCReceipt code format")
    private String mcReceiptCode;

    @Pattern(regexp = "^EPXK(\\d){1,18}$", message = "Invalid RSInwardOutward code format")
    private String rsInwardOutwardCode;

    private final String CODE_PREFIX = "EP";

    @Size(min = 1, message = "No SaInvoiceDetail found")
    @Valid
    private List<Detail> detail;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = CODE_PREFIX + code;
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
        this.customerCode = CODE_PREFIX + customerCode;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = CODE_PREFIX + employeeCode;
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

    public BigDecimal getTotalAll() {
        return totalAll;
    }

    public void setTotalAll(BigDecimal totalAll) {
        this.totalAll = totalAll;
    }

    public BigDecimal getTotalVAT() {
        return totalVAT;
    }

    public void setTotalVAT(BigDecimal totalVAT) {
        this.totalVAT = totalVAT;
    }

    public String getMcReceiptCode() {
        return mcReceiptCode;
    }

    public void setMcReceiptCode(String mcReceiptCode) {
        this.mcReceiptCode = CODE_PREFIX + mcReceiptCode;
    }

    public String getRsInwardOutwardCode() {
        return rsInwardOutwardCode;
    }

    public void setRsInwardOutwardCode(String rsInwardOutwardCode) {
        this.rsInwardOutwardCode = CODE_PREFIX + rsInwardOutwardCode;
    }

    public List<Detail> getDetail() {
        return detail;
    }

    public void setDetail(List<Detail> detail) {
        this.detail = detail;
    }

    public static class Detail {

        //TODO: Hiện tại Pattern vẫn đang check theo chuẩn tự sinh code của ứng dụng, nếu hỗ trợ code2 do KH tự tạo, phải bỏ đi hoặc có pattern khác
        @NotEmpty(message = "code must be not empty")
        @Pattern(regexp = "^EPSP(\\d){1,18}$", message = "Invalid product code format")
        private String code;

        @NotNull(message = "position must be not null")
        @Min(0)
        private Integer position;

        private boolean isPromotion;

        private Long unitId;

        @NotNull(message = "quantity must be not null")
        @DecimalMin("0.0")
        private BigDecimal quantity;

        @NotNull(message = "purchasePrice must be not null")
        @DecimalMin("0.0")
        private BigDecimal unitPrice;

        @DecimalMin("0.0")
        private BigDecimal oWPrice;

        @NotNull(message = "amount must be not null")
        @DecimalMin("0.0")
        private BigDecimal amount;

        @DecimalMin("0.0")
        private BigDecimal oWAmount;

        @NotNull(message = "discountAmount must be not null")
        @DecimalMin("0.0")
        private BigDecimal discountAmount;

        private final String CODE_PREFIX = "EP";

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = CODE_PREFIX + code;
        }

        public Integer getPosition() {
            return position;
        }

        public void setPosition(Integer position) {
            this.position = position;
        }

        public boolean isPromotion(boolean b) {
            return isPromotion;
        }

        public void setPromotion(boolean promotion) {
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

        public boolean isPromotion() {
            return isPromotion;
        }

        public BigDecimal getoWPrice() {
            return oWPrice;
        }

        public void setoWPrice(BigDecimal oWPrice) {
            this.oWPrice = oWPrice;
        }

        public BigDecimal getoWAmount() {
            return oWAmount;
        }

        public void setoWAmount(BigDecimal oWAmount) {
            this.oWAmount = oWAmount;
        }
    }
}
