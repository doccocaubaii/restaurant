package vn.softdreams.easypos.dto.receiptpayment;

import com.fasterxml.jackson.annotation.JsonFormat;
import vn.softdreams.easypos.config.Constants;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class ReceiptPayment implements Serializable {

    private Integer id;
    private Integer type;
    private String typeDesc;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    private ZonedDateTime date;

    private String no;
    private BigDecimal amount;
    private String customerName;
    private Integer customerId;
    private String note;
    private Integer businessTypeId;
    private String businessTypeName;
    private String errorMessage;

    public ReceiptPayment(
        Integer id,
        Integer type,
        String typeDesc,
        ZonedDateTime date,
        String no,
        BigDecimal amount,
        String customerName,
        Integer customerId,
        String note,
        Integer businessTypeId,
        String businessTypeName
    ) {
        this.id = id;
        this.type = type;
        this.typeDesc = typeDesc;
        this.date = date;
        this.no = no;
        this.amount = amount;
        this.customerName = customerName;
        this.customerId = customerId;
        this.note = note;
        this.businessTypeId = businessTypeId;
        this.businessTypeName = businessTypeName;
    }

    public ReceiptPayment() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCustomerName() {
        return customerName;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getBusinessTypeId() {
        return businessTypeId;
    }

    public void setBusinessTypeId(Integer businessTypeId) {
        this.businessTypeId = businessTypeId;
    }

    public String getBusinessTypeName() {
        return businessTypeName;
    }

    public void setBusinessTypeName(String businessTypeName) {
        this.businessTypeName = businessTypeName;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
