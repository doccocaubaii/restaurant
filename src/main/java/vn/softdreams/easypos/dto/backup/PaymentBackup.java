package vn.softdreams.easypos.dto.backup;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import vn.softdreams.easypos.util.Common;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class PaymentBackup {

    @JsonProperty("ID")
    private String item;

    @JsonProperty("bill_id")
    private String bill_id;

    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("refund")
    private BigDecimal refund;

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

    public String getBill_id() {
        return bill_id;
    }

    public void setBill_id(String bill_id) {
        this.bill_id = bill_id;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
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

    public void setCreateTime(String createTime) {
        this.createTime = Common.convertStringToZoneDateTime(createTime, "yyyy-MM-dd'T'HH:mm:ss.SSS");
    }
}
