package vn.softdreams.easypos.dto.receiptpayment;

import java.io.Serializable;
import java.math.BigDecimal;

public class ReceiptPaymentGetByIdResponse implements Serializable {

    private Integer id;
    private Integer comId;
    private Integer billId;
    private Integer type;
    private Integer businessType;
    private String typeDesc;
    private String date;
    private String customerName;
    private Integer customerId;
    private BigDecimal amount;
    private String note;
    private String no;
    private Integer rsInOutWardId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public Integer getRsInOutWardId() {
        return rsInOutWardId;
    }

    public void setRsInOutWardId(Integer rsInOutWardId) {
        this.rsInOutWardId = rsInOutWardId;
    }
}
