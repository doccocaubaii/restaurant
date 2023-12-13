package vn.softdreams.easypos.dto.bill;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

public class BillCreateRequest {

    private Integer id;

    @NotNull(message = ExceptionConstants.COMPANY_ID_NOT_NULL)
    private Integer comId;

    private Integer areaId;

    private String code2;

    private Integer areaUnitId;

    private Integer reservationId;

    @NotNull(message = ExceptionConstants.CUSTOMER_ID_NOT_NULL)
    @Min(value = 1, message = ExceptionConstants.CUSTOMER_ID_NOT_NULL)
    private Integer customerId;

    @NotBlank(message = ExceptionConstants.CUSTOMER_NAME_NOT_NULL)
    private String customerName;

    @NotBlank(message = ExceptionConstants.TAX_AUTHORITY_NOT_NULL)
    private String taxAuthorityCode;

    @NotBlank(message = ExceptionConstants.BILL_DATE_NOT_NULL)
    //    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    //    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    private String billDate;

    @NotNull(message = ExceptionConstants.DELIVERY_TYPE_INVALID)
    @Min(value = 1, message = ExceptionConstants.DELIVERY_TYPE_INVALID)
    @Max(value = 3, message = ExceptionConstants.DELIVERY_TYPE_INVALID)
    private Integer deliveryType;

    @NotNull(message = ExceptionConstants.QUANTITY_INVALID)
    @Min(value = 0, message = ExceptionConstants.QUANTITY_INVALID)
    @Digits(integer = 21, fraction = 6, message = ExceptionConstants.QUANTITY_PRODUCT_INVALID)
    private BigDecimal quantity;

    @NotNull(message = ExceptionConstants.AMOUNT_NOT_NULL)
    @Digits(integer = 21, fraction = 6)
    private BigDecimal amount;

    private BigDecimal discountAmount;

    @NotNull(message = ExceptionConstants.TOTAL_PRETAX_NOT_NULL)
    @Digits(integer = 21, fraction = 6, message = ExceptionConstants.TOTAL_PRE_TAX_INVALID)
    private BigDecimal totalPreTax;

    private Integer vatRate;

    private BigDecimal vatAmount;

    @NotNull(message = ExceptionConstants.TOTAL_AMOUNT_NOT_NULL)
    @Digits(integer = 21, fraction = 6, message = ExceptionConstants.AMOUNT_INVALID)
    private BigDecimal totalAmount;

    @NotNull(message = ExceptionConstants.BILL_STATUS_IN_VALID)
    @Min(value = 0, message = ExceptionConstants.BILL_STATUS_IN_VALID)
    @Max(value = 1, message = ExceptionConstants.BILL_STATUS_IN_VALID)
    private Integer status;

    private Integer typeInv;
    private BigDecimal productDiscountAmount;

    @Size(max = 512, message = ExceptionConstants.DESCRIPTION_MAX_LENGTH)
    private String description;

    private Boolean haveDiscountVat;
    private Integer discountVatRate;
    private BigDecimal discountVatAmount;
    private String buyerName;
    private BigDecimal voucherAmount;
    private Boolean isProcessing;

    @Valid
    private BillPaymentRequest payment;

    @NotNull(message = ExceptionConstants.PRODUCTS_NOT_NULL)
    @Valid
    private List<BillProductRequest> products;

    @Valid
    private List<BillVoucherRequest> vouchers;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public Boolean getIsProcessing() {
        return isProcessing;
    }

    public void setIsProcessing(Boolean processing) {
        isProcessing = processing;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public BillCreateRequest() {}

    public BigDecimal getProductDiscountAmount() {
        return productDiscountAmount;
    }

    public void setProductDiscountAmount(BigDecimal productDiscountAmount) {
        this.productDiscountAmount = productDiscountAmount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    //    public Integer getId() {
    //        return id;
    //    }
    //
    //    public void setId(Integer id) {
    //        this.id = id;
    //    }
    public String messageException(String errorKey, String value) {
        return "code: " + errorKey + ", value: " + value;
    }

    public Integer getReservationId() {
        return reservationId;
    }

    public void setReservationId(Integer reservationId) {
        this.reservationId = reservationId;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BillPaymentRequest getPayment() {
        return payment;
    }

    public void setPayment(BillPaymentRequest payment) {
        this.payment = payment;
    }

    public List<BillProductRequest> getProducts() {
        return products;
    }

    public void setProducts(List<BillProductRequest> products) {
        this.products = products;
    }

    //    public String getCode() {
    //        return code;
    //    }
    //
    //    public void setCode(String code) {
    //        this.code = code;
    //    }

    public String getCode2() {
        return code2;
    }

    public void setCode2(String code2) {
        this.code2 = code2;
    }

    //    public Integer getComId() {
    //        return comId;
    //    }
    //
    //    public void setComId(Integer comId) {
    //        this.comId = comId;
    //    }

    public Integer getAreaUnitId() {
        return areaUnitId;
    }

    public void setAreaUnitId(Integer areaUnitId) {
        this.areaUnitId = areaUnitId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getTaxAuthorityCode() {
        return taxAuthorityCode;
    }

    public void setTaxAuthorityCode(String taxAuthorityCode) {
        this.taxAuthorityCode = taxAuthorityCode;
    }

    public Integer getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(Integer deliveryType) {
        this.deliveryType = deliveryType;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    //    public Integer getStatusInvoice() {
    //        return statusInvoice;
    //    }
    //
    //    public void setStatusInvoice(Integer statusInvoice) {
    //        this.statusInvoice = statusInvoice;
    //    }
    //
    //    public String getInvoiceErrorMessage() {
    //        return invoiceErrorMessage;
    //    }
    //
    //    public void setInvoiceErrorMessage(String invoiceErrorMessage) {
    //        this.invoiceErrorMessage = invoiceErrorMessage;
    //    }

    public Integer getTypeInv() {
        return typeInv;
    }

    public void setTypeInv(Integer typeInv) {
        this.typeInv = typeInv;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getHaveDiscountVat() {
        return haveDiscountVat;
    }

    public void setHaveDiscountVat(Boolean haveDiscountVat) {
        this.haveDiscountVat = haveDiscountVat;
    }

    public Integer getDiscountVatRate() {
        return discountVatRate;
    }

    public void setDiscountVatRate(Integer discountVatRate) {
        this.discountVatRate = discountVatRate;
    }

    public BigDecimal getDiscountVatAmount() {
        return discountVatAmount;
    }

    public void setDiscountVatAmount(BigDecimal discountVatAmount) {
        this.discountVatAmount = discountVatAmount;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public BigDecimal getVoucherAmount() {
        return voucherAmount;
    }

    public void setVoucherAmount(BigDecimal voucherAmount) {
        this.voucherAmount = voucherAmount;
    }

    public List<BillVoucherRequest> getVouchers() {
        return vouchers;
    }

    public void setVouchers(List<BillVoucherRequest> vouchers) {
        this.vouchers = vouchers;
    }
}
