package vn.softdreams.easypos.dto.bill;

import com.fasterxml.jackson.annotation.JsonFormat;
import vn.softdreams.easypos.config.Constants;
import vn.softdreams.easypos.util.Common;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class BillItemResponse {

    private Integer id;
    private String code;
    private String code2;
    private BigDecimal totalAmount;
    private Integer customerId;
    private String customerName;
    private Integer status;
    private String paymentMethod;
    private BigDecimal debt;
    private BigDecimal refund;
    private BigDecimal amount;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    private ZonedDateTime createTime;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    private ZonedDateTime billDate;

    private String billIdReturns;

    private BillDetailResponse.CustomerMoreInformation customerCard;

    public BillItemResponse() {}

    public BillItemResponse(
        Integer id,
        String code,
        String code2,
        BigDecimal totalAmount,
        Integer customerId,
        String customerName,
        Integer status,
        String paymentMethod,
        BigDecimal debt,
        BigDecimal refund,
        BigDecimal amount,
        ZonedDateTime createTime,
        ZonedDateTime billDate
    ) {
        this.id = id;
        this.code = code;
        this.code2 = code2;
        this.totalAmount = totalAmount;
        this.customerId = customerId;
        this.customerName = customerName;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.debt = debt;
        this.refund = refund;
        this.amount = amount;
        this.createTime = createTime;
        this.billDate = billDate;
    }

    public BillItemResponse(
        Integer id,
        String code,
        String code2,
        BigDecimal totalAmount,
        Integer customerId,
        String customerName,
        Integer status,
        String paymentMethod,
        BigDecimal debt,
        BigDecimal refund,
        BigDecimal amount,
        ZonedDateTime createTime,
        ZonedDateTime billDate,
        String billIdReturns
    ) {
        this.id = id;
        this.code = code;
        this.code2 = code2;
        this.totalAmount = totalAmount;
        this.customerId = customerId;
        this.customerName = customerName;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.debt = debt;
        this.refund = refund;
        this.amount = amount;
        this.createTime = createTime;
        this.billDate = billDate;
        this.billIdReturns = billIdReturns;
    }

    public ZonedDateTime getBillDate() {
        return billDate;
    }

    public void setBillDate(ZonedDateTime billDate) {
        this.billDate = billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = Common.convertStringToZoneDateTime(billDate, Constants.ZONED_DATE_TIME_FORMAT);
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setCreateTime(ZonedDateTime createTime) {
        this.createTime = createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = Common.convertStringToZoneDateTime(createTime, Constants.ZONED_DATE_TIME_FORMAT);
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

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
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

    public String getCode2() {
        return code2;
    }

    public void setCode2(String code2) {
        this.code2 = code2;
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

    public ZonedDateTime getCreateTime() {
        return createTime;
    }

    public BigDecimal getDebt() {
        return debt;
    }

    public void setDebt(BigDecimal debt) {
        this.debt = debt;
    }

    public BigDecimal getRefund() {
        return refund;
    }

    public void setRefund(BigDecimal refund) {
        this.refund = refund;
    }

    public String getBillIdReturns() {
        return billIdReturns;
    }

    public void setBillIdReturns(String billIdReturns) {
        this.billIdReturns = billIdReturns;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BillDetailResponse.CustomerMoreInformation getCustomerCard() {
        return customerCard;
    }

    public void setCustomerCard(BillDetailResponse.CustomerMoreInformation customerCard) {
        this.customerCard = customerCard;
    }
}
