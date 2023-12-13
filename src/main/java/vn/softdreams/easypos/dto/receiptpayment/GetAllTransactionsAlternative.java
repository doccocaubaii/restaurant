package vn.softdreams.easypos.dto.receiptpayment;

import org.springframework.data.domain.Page;

import java.math.BigDecimal;

public class GetAllTransactionsAlternative {

    private BigDecimal receiptAmount;
    private BigDecimal paymentAmount;
    private Page<ReceiptPayment> receiptPaymentList;

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

    public Page<ReceiptPayment> getReceiptPaymentList() {
        return receiptPaymentList;
    }

    public void setReceiptPaymentList(Page<ReceiptPayment> receiptPaymentList) {
        this.receiptPaymentList = receiptPaymentList;
    }
}
