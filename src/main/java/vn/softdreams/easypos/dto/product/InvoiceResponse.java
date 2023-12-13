package vn.softdreams.easypos.dto.product;

import vn.softdreams.easypos.domain.InvoiceProduct;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class InvoiceResponse implements Serializable {

    private Integer id;
    private Integer billId;
    private Integer customerId;
    private String customerName;
    private String idNumber;
    private String customerPhone;
    private String pattern;
    private Integer no;
    private String arisingDate;
    private String publishDate;
    private String paymentMethod;
    private BigDecimal discountAmount;
    private BigDecimal totalPreTax;
    private Integer vatRate;
    private BigDecimal vatAmount;
    private BigDecimal totalAmount;
    private Integer type;
    private Integer status;
    private Integer taxCheckStatus;
    private String taxAuthorityCode;
    private String taxErrorMessage;
    private String ikey;
    private String refIkey;
    private String extra;
    private Integer creator;
    private String createTime;
    private Integer companyId;
    private String customerCode;
    private String customerAddress;
    private String customerTaxCode;
    private Integer exchangeRate;
    private String currencyUnit;
    private String code;
    private String code2;
    private BigDecimal amount;
    private BigDecimal refund;
    private List<InvoiceProduct> invoiceProducts;

    public InvoiceResponse() {}

    public InvoiceResponse(
        Integer id,
        Integer billId,
        Integer customerId,
        String customerName,
        String idNumber,
        String customerPhone,
        String pattern,
        Integer no,
        String arisingDate,
        String publishDate,
        String paymentMethod,
        BigDecimal discountAmount,
        BigDecimal totalPreTax,
        Integer vatRate,
        BigDecimal vatAmount,
        BigDecimal totalAmount,
        Integer type,
        Integer status,
        Integer taxCheckStatus,
        String taxAuthorityCode,
        String taxErrorMessage,
        String ikey,
        String refIkey,
        String extra,
        Integer creator,
        String createTime,
        Integer companyId,
        String customerCode,
        String customerAddress,
        String customerTaxCode,
        Integer exchangeRate,
        String currencyUnit,
        String code,
        String code2,
        BigDecimal amount,
        BigDecimal refund
    ) {
        this.id = id;
        this.billId = billId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.idNumber = idNumber;
        this.customerPhone = customerPhone;
        this.pattern = pattern;
        this.no = no;
        this.arisingDate = arisingDate;
        this.publishDate = publishDate;
        this.paymentMethod = paymentMethod;
        this.discountAmount = discountAmount;
        this.totalPreTax = totalPreTax;
        this.vatRate = vatRate;
        this.vatAmount = vatAmount;
        this.totalAmount = totalAmount;
        this.type = type;
        this.status = status;
        this.taxCheckStatus = taxCheckStatus;
        this.taxAuthorityCode = taxAuthorityCode;
        this.taxErrorMessage = taxErrorMessage;
        this.ikey = ikey;
        this.refIkey = refIkey;
        this.extra = extra;
        this.creator = creator;
        this.createTime = createTime;
        this.companyId = companyId;
        this.customerCode = customerCode;
        this.customerAddress = customerAddress;
        this.customerTaxCode = customerTaxCode;
        this.exchangeRate = exchangeRate;
        this.currencyUnit = currencyUnit;
        this.code = code;
        this.code2 = code2;
        this.amount = amount;
        this.refund = refund;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getRefIkey() {
        return refIkey;
    }

    public void setRefIkey(String refIkey) {
        this.refIkey = refIkey;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public Integer getCreator() {
        return creator;
    }

    public void setCreator(Integer creator) {
        this.creator = creator;
    }

    public String getArisingDate() {
        return arisingDate;
    }

    public void setArisingDate(String arisingDate) {
        this.arisingDate = arisingDate;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode2() {
        return code2;
    }

    public void setCode2(String code2) {
        this.code2 = code2;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getRefund() {
        return refund;
    }

    public void setRefund(BigDecimal refund) {
        this.refund = refund;
    }

    public List<InvoiceProduct> getInvoiceProducts() {
        return invoiceProducts;
    }

    public void setInvoiceProducts(List<InvoiceProduct> invoiceProducts) {
        this.invoiceProducts = invoiceProducts;
    }
}
