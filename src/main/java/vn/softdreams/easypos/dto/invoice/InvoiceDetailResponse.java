package vn.softdreams.easypos.dto.invoice;

import com.fasterxml.jackson.annotation.JsonFormat;
import vn.softdreams.easypos.config.Constants;
import vn.softdreams.easypos.domain.InvoiceProduct;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

public class InvoiceDetailResponse {

    private Integer creator;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    private ZonedDateTime createTime;

    private Integer updater;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    private ZonedDateTime updateTime;

    private Integer id;
    private Integer companyId;
    private Integer billId;
    private Integer customerId;
    private String customerName;

    private String customerAddress;

    private String customerTaxCode;

    private String idNumber;

    private String customerPhone;

    private String pattern;

    private Integer no;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_INVOICE_FORMAT)
    private ZonedDateTime arisingDate;

    private ZonedDateTime publishDate;

    private String paymentMethod;

    private Integer exchangeRate;

    private String currencyUnit;

    private BigDecimal discountAmount;

    private BigDecimal totalPreTax;

    private Integer vatRate;

    private BigDecimal vatAmount;

    private BigDecimal totalAmount;

    private Integer type = 0;

    private Integer status = 0;

    private Integer taxCheckStatus;

    private String taxAuthorityCode;

    private String taxErrorMessage;

    private String ikey;

    private String refikey;

    private String extra;

    private Integer userId;

    private Integer updateUserId;

    private String customerCode;

    private BigDecimal amount;

    private String errorPublish;

    private String customerNormalizedName;

    private List<InvoiceProduct> invoiceProducts;

    private String billCode;

    private Integer typeInv;
    private Boolean haveDiscountVat;

    private Integer discountVatRate;

    private BigDecimal discountVatAmount;
    private String buyerName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getBillId() {
        return billId;
    }

    public void setBillId(Integer billId) {
        this.billId = billId;
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

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerTaxCode() {
        return customerTaxCode;
    }

    public void setCustomerTaxCode(String customerTaxCode) {
        this.customerTaxCode = customerTaxCode;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    public ZonedDateTime getArisingDate() {
        return arisingDate;
    }

    public void setArisingDate(ZonedDateTime arisingDate) {
        this.arisingDate = arisingDate;
    }

    public ZonedDateTime getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(ZonedDateTime publishDate) {
        this.publishDate = publishDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Integer getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(Integer exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getCurrencyUnit() {
        return currencyUnit;
    }

    public void setCurrencyUnit(String currencyUnit) {
        this.currencyUnit = currencyUnit;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getTaxCheckStatus() {
        return taxCheckStatus;
    }

    public void setTaxCheckStatus(Integer taxCheckStatus) {
        this.taxCheckStatus = taxCheckStatus;
    }

    public String getTaxAuthorityCode() {
        return taxAuthorityCode;
    }

    public void setTaxAuthorityCode(String taxAuthorityCode) {
        this.taxAuthorityCode = taxAuthorityCode;
    }

    public String getTaxErrorMessage() {
        return taxErrorMessage;
    }

    public void setTaxErrorMessage(String taxErrorMessage) {
        this.taxErrorMessage = taxErrorMessage;
    }

    public String getIkey() {
        return ikey;
    }

    public void setIkey(String ikey) {
        this.ikey = ikey;
    }

    public String getRefikey() {
        return refikey;
    }

    public void setRefikey(String refikey) {
        this.refikey = refikey;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Integer updateUserId) {
        this.updateUserId = updateUserId;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getErrorPublish() {
        return errorPublish;
    }

    public void setErrorPublish(String errorPublish) {
        this.errorPublish = errorPublish;
    }

    public String getCustomerNormalizedName() {
        return customerNormalizedName;
    }

    public void setCustomerNormalizedName(String customerNormalizedName) {
        this.customerNormalizedName = customerNormalizedName;
    }

    public List<InvoiceProduct> getInvoiceProducts() {
        return invoiceProducts;
    }

    public void setInvoiceProducts(List<InvoiceProduct> invoiceProducts) {
        this.invoiceProducts = invoiceProducts;
    }

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public Integer getTypeInv() {
        return typeInv;
    }

    public void setTypeInv(Integer typeInv) {
        this.typeInv = typeInv;
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

    public Integer getCreator() {
        return creator;
    }

    public void setCreator(Integer creator) {
        this.creator = creator;
    }

    public ZonedDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(ZonedDateTime createTime) {
        this.createTime = createTime;
    }

    public Integer getUpdater() {
        return updater;
    }

    public void setUpdater(Integer updater) {
        this.updater = updater;
    }

    public ZonedDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(ZonedDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }
}
