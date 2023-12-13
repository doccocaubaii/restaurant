package vn.softdreams.easypos.dto.backup;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import vn.softdreams.easypos.util.Common;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class BillBackup {

    @JsonProperty("ID")
    private String item;

    @JsonProperty("code")
    private String code2;

    @JsonProperty("customer_id")
    private String customerId;

    @JsonProperty("customer_name")
    private String customerName;

    @JsonProperty("bill_date")
    private ZonedDateTime billDate;

    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("discount_amount")
    private BigDecimal discountAmount;

    @JsonProperty("total_pre_tax")
    private BigDecimal totalPreTax;

    @JsonProperty("vat_rate")
    private Integer vatRate;

    @JsonProperty("vat_amount")
    private BigDecimal vatAmount;

    @JsonProperty("total_amount")
    private BigDecimal totalAmount;

    @JsonProperty("status")
    private Integer status;

    @JsonProperty("status_invoice")
    private Integer statusInvoice;

    @JsonProperty("invoice_error_messag")
    private String invoiceErrorMessag;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("extra")
    private String extra;

    @JsonProperty("refId")
    private String refId;

    @JsonProperty("tax_authority_code")
    private String taxAuthorityCode;

    @JsonProperty("org_id")
    private String org_id;

    @JsonProperty("type_inv")
    private Integer typeInv;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @JsonProperty("create_time")
    private ZonedDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @JsonProperty("update_time")
    private ZonedDateTime updateTime;

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getCode2() {
        return code2;
    }

    public void setCode2(String code2) {
        this.code2 = code2;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public ZonedDateTime getBillDate() {
        return billDate;
    }

    public void setBillDate(ZonedDateTime billDate) {
        this.billDate = billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = Common.convertStringToZoneDateTime(billDate, "yyyy-MM-dd'T'HH:mm:ss.SSS");
    }

    public void setCreateTime(String createTime) {
        this.createTime = Common.convertStringToZoneDateTime(createTime, "yyyy-MM-dd'T'HH:mm:ss.SSS");
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
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

    public Integer getStatusInvoice() {
        return statusInvoice;
    }

    public void setStatusInvoice(Integer statusInvoice) {
        this.statusInvoice = statusInvoice;
    }

    public String getInvoiceErrorMessag() {
        return invoiceErrorMessag;
    }

    public void setInvoiceErrorMessag(String invoiceErrorMessag) {
        this.invoiceErrorMessag = invoiceErrorMessag;
    }

    public BigDecimal getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(BigDecimal vatAmount) {
        this.vatAmount = vatAmount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getTaxAuthorityCode() {
        return taxAuthorityCode;
    }

    public void setTaxAuthorityCode(String taxAuthorityCode) {
        this.taxAuthorityCode = taxAuthorityCode;
    }

    public String getOrg_id() {
        return org_id;
    }

    public void setOrg_id(String org_id) {
        this.org_id = org_id;
    }

    public Integer getTypeInv() {
        return typeInv;
    }

    public void setTypeInv(Integer typeInv) {
        this.typeInv = typeInv;
    }

    public ZonedDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(ZonedDateTime createTime) {
        this.createTime = createTime;
    }

    public ZonedDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(ZonedDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.createTime = Common.convertStringToZoneDateTime(updateTime, "yyyy-MM-dd'T'HH:mm:ss.SSS");
    }
}
