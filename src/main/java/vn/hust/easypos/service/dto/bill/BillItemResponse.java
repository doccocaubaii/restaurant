package vn.hust.easypos.service.dto.bill;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import vn.hust.easypos.config.Constants;

public class BillItemResponse {

    private Integer id;
    private String code;
    private BigDecimal totalAmount;
    private String customerName;
    private Integer status;
    private String paymentMethod;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    private LocalDateTime createTime;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    private LocalDateTime billDate;

    public BillItemResponse() {}

    public BillItemResponse(
        Integer id,
        String code,
        BigDecimal totalAmount,
        String customerName,
        Integer status,
        String paymentMethod,
        LocalDateTime createTime,
        LocalDateTime billDate
    ) {
        this.id = id;
        this.code = code;
        this.totalAmount = totalAmount;
        this.customerName = customerName;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.createTime = createTime;
        this.billDate = billDate;
    }

    public LocalDateTime getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDateTime billDate) {
        this.billDate = billDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }
}
