package vn.softdreams.easypos.dto.receiptpayment;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class ReceiptPaymentGetAllTransactionsResponse implements Serializable {

    private Integer comId;
    private String fromDate;
    private String toDate;
    private BigDecimal receiptAmount;
    private BigDecimal paymentAmount;
    private List<ReceiptPayment> receiptPaymentList;

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public BigDecimal getReceiptAmount() {
        return receiptAmount;
    }

    public void setReceiptAmount(BigDecimal receiptAmount) {
        this.receiptAmount = receiptAmount;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public List<ReceiptPayment> getReceiptPaymentList() {
        return receiptPaymentList;
    }

    public void setReceiptPaymentList(List<ReceiptPayment> receiptPaymentList) {
        this.receiptPaymentList = receiptPaymentList;
    }
}
