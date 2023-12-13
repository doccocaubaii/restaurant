package vn.softdreams.easypos.dto.audit;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import vn.softdreams.easypos.config.Constants;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class ActivityHistoryResponse implements Serializable {

    private Integer rev;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer revtype;

    private Integer id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String code;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String billCode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String fullName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal totalAmount;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal vatAmount;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer rowNumber;

    private Integer type;
    private String description;
    private Integer customerId;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    private ZonedDateTime updateTime;

    public ActivityHistoryResponse() {}

    public ActivityHistoryResponse(
        Integer rev,
        Integer revtype,
        Integer id,
        Integer status,
        String code,
        String billCode,
        Integer customerId,
        String fullName,
        BigDecimal totalAmount,
        BigDecimal vatAmount,
        Integer rowNumber,
        Integer type,
        ZonedDateTime updateTime
    ) {
        this.rev = rev;
        this.revtype = revtype;
        this.id = id;
        this.status = status;
        this.code = code;
        this.billCode = billCode;
        this.customerId = customerId;
        this.fullName = fullName;
        this.totalAmount = totalAmount;
        this.rowNumber = rowNumber;
        this.type = type;
        this.vatAmount = vatAmount;
        this.updateTime = updateTime;
    }

    public ActivityHistoryResponse(
        Integer rev,
        Integer id,
        Integer type,
        String description,
        ZonedDateTime updateTime,
        Integer customerId
    ) {
        this.rev = rev;
        this.id = id;
        this.customerId = customerId;
        this.type = type;
        this.description = description;
        this.updateTime = updateTime;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public ZonedDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(ZonedDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getRev() {
        return rev;
    }

    public void setRev(Integer rev) {
        this.rev = rev;
    }

    public Integer getRevtype() {
        return revtype;
    }

    public void setRevtype(Integer revtype) {
        this.revtype = revtype;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(Integer rowNumber) {
        this.rowNumber = rowNumber;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(BigDecimal vatAmount) {
        this.vatAmount = vatAmount;
    }
}
