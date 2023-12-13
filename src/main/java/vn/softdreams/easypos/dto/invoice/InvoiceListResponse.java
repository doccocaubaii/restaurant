package vn.softdreams.easypos.dto.invoice;

import com.fasterxml.jackson.annotation.JsonFormat;
import vn.softdreams.easypos.config.Constants;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class InvoiceListResponse implements Serializable {

    private Integer id;
    private Integer billId;
    private Integer customerId;
    private String customerCode;
    private String customerName;
    private String pattern;
    private Integer no;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_INVOICE_FORMAT)
    private ZonedDateTime arisingDate;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_INVOICE_FORMAT)
    private ZonedDateTime publishDate;

    private BigDecimal totalAmount;
    private String currencyUnit;
    private Integer type;
    private Integer status;
    private Integer taxCheckStatus;
    private String taxAuthorityCode;
    private String ikey;
    private String errorPublish;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    private ZonedDateTime createTime;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    private ZonedDateTime updateTime; //

    private String note;

    public InvoiceListResponse() {}

    public InvoiceListResponse(
        Integer id,
        Integer billId,
        Integer customerId,
        String customerCode,
        String customerName,
        String pattern,
        Integer no,
        ZonedDateTime arisingDate,
        ZonedDateTime publishDate,
        BigDecimal totalAmount,
        String currencyUnit,
        Integer type,
        Integer status,
        Integer taxCheckStatus,
        String taxAuthorityCode,
        String ikey,
        String errorPublish,
        ZonedDateTime createTime,
        ZonedDateTime updateTime
    ) {
        this.id = id;
        this.billId = billId;
        this.customerId = customerId;
        this.customerCode = customerCode;
        this.customerName = customerName;
        this.pattern = pattern;
        this.no = no;
        this.arisingDate = arisingDate;
        this.publishDate = publishDate;
        this.totalAmount = totalAmount;
        this.currencyUnit = currencyUnit;
        this.type = type;
        this.status = status;
        this.taxCheckStatus = taxCheckStatus;
        this.taxAuthorityCode = taxAuthorityCode;
        this.ikey = ikey;
        this.errorPublish = errorPublish;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getErrorPublish() {
        return errorPublish;
    }

    public void setErrorPublish(String errorPublish) {
        this.errorPublish = errorPublish;
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

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCurrencyUnit() {
        return currencyUnit;
    }

    public void setCurrencyUnit(String currencyUnit) {
        this.currencyUnit = currencyUnit;
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

    public String getIkey() {
        return ikey;
    }

    public void setIkey(String ikey) {
        this.ikey = ikey;
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
}
