package vn.softdreams.easypos.dto.voucher;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import vn.softdreams.easypos.config.Constants;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class VoucherWebResponse implements Serializable {

    private Integer id;
    private String code;
    private String name;

    @JsonFormat(pattern = Constants.ZONED_DATE_FORMAT)
    private ZonedDateTime startTime;

    @JsonFormat(pattern = Constants.ZONED_DATE_FORMAT)
    private ZonedDateTime endTime;

    private String applyType;
    private Integer status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String discountCondition;

    private BigDecimal discountValue;
    private Double discountPercent;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal voucherValue;

    private String desc;

    public VoucherWebResponse() {}

    public VoucherWebResponse(
        Integer id,
        String code,
        String name,
        String discountCondition,
        ZonedDateTime startTime,
        ZonedDateTime endTime,
        Integer status
    ) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.discountCondition = discountCondition;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDiscountCondition() {
        return discountCondition;
    }

    public void setDiscountCondition(String discountCondition) {
        this.discountCondition = discountCondition;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public Double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(Double discountPercent) {
        this.discountPercent = discountPercent;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }

    public String getApplyType() {
        return applyType;
    }

    public void setApplyType(String applyType) {
        this.applyType = applyType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public BigDecimal getVoucherValue() {
        return voucherValue;
    }

    public void setVoucherValue(BigDecimal voucherValue) {
        this.voucherValue = voucherValue;
    }
}
