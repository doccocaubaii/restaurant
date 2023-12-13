package vn.softdreams.easypos.dto.voucher;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import vn.softdreams.easypos.config.Constants;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

public class VoucherResponse implements Serializable {

    private Integer id;
    private String code;
    private String name;

    @JsonFormat(pattern = Constants.ZONED_DATE_FORMAT)
    private ZonedDateTime startTime;

    @JsonFormat(pattern = Constants.ZONED_DATE_FORMAT)
    private ZonedDateTime endTime;

    private String applyType;
    private Integer status;
    private Integer type;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String discountCondition;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String extTimeCondition;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String differentExtCondition;

    private List<Object> conditions;
    private List<VoucherUsageResult> historyUsage;
    private List<ExtTimeCondition> extTimeConditions;
    private DifferentExtConditions differentExtConditions;

    // remove after push prod
    private BigDecimal discountValue;
    private Double discountPercent;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal voucherValue;

    private String desc;

    // end

    public VoucherResponse() {}

    public VoucherResponse(
        Integer id,
        String code,
        String name,
        String discountCondition,
        ZonedDateTime startTime,
        ZonedDateTime endTime,
        Integer status,
        Integer type,
        String extTimeCondition,
        String differentExtCondition
    ) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.type = type;
        this.discountCondition = discountCondition;
        this.extTimeCondition = extTimeCondition;
        this.differentExtCondition = differentExtCondition;
    }

    public String getDifferentExtCondition() {
        return differentExtCondition;
    }

    public void setDifferentExtCondition(String differentExtCondition) {
        this.differentExtCondition = differentExtCondition;
    }

    public DifferentExtConditions getDifferentExtConditions() {
        return differentExtConditions;
    }

    public void setDifferentExtConditions(DifferentExtConditions differentExtConditions) {
        this.differentExtConditions = differentExtConditions;
    }

    public List<ExtTimeCondition> getExtTimeConditions() {
        return extTimeConditions;
    }

    public void setExtTimeConditions(List<ExtTimeCondition> extTimeConditions) {
        this.extTimeConditions = extTimeConditions;
    }

    public String getExtTimeCondition() {
        return extTimeCondition;
    }

    public void setExtTimeCondition(String extTimeCondition) {
        this.extTimeCondition = extTimeCondition;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDiscountCondition() {
        return discountCondition;
    }

    public void setDiscountCondition(String discountCondition) {
        this.discountCondition = discountCondition;
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

    public List<Object> getConditions() {
        return conditions;
    }

    public void setConditions(List<Object> conditions) {
        this.conditions = conditions;
    }

    public List<VoucherUsageResult> getHistoryUsage() {
        return historyUsage;
    }

    public void setHistoryUsage(List<VoucherUsageResult> historyUsage) {
        this.historyUsage = historyUsage;
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

    public BigDecimal getVoucherValue() {
        return voucherValue;
    }

    public void setVoucherValue(BigDecimal voucherValue) {
        this.voucherValue = voucherValue;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
