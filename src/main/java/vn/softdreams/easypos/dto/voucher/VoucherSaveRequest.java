package vn.softdreams.easypos.dto.voucher;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.List;

public class VoucherSaveRequest implements Serializable {

    private Integer id;

    @NotNull(message = ExceptionConstants.COMPANY_ID_NOT_NULL)
    private Integer comId;

    @NotBlank(message = ExceptionConstants.VOUCHER_CODE_NOT_BLANK)
    @Size(max = 20, message = ExceptionConstants.VOUCHER_CODE_INVALID)
    private String code;

    @NotBlank(message = ExceptionConstants.VOUCHER_NAME_NOT_BLANK)
    private String name;

    @NotNull(message = ExceptionConstants.VOUCHER_DISCOUNT_TYPE_NOT_NULL)
    private Integer type;

    @NotBlank(message = ExceptionConstants.VOUCHER_START_DATE_NOT_BLANK)
    private String startTime;

    @NotBlank(message = ExceptionConstants.VOUCHER_END_DATE_NOT_BLANK)
    private String endTime;

    @NotNull(message = ExceptionConstants.VOUCHER_STATUS_NOT_NULL)
    @Min(value = 0, message = ExceptionConstants.VOUCHER_STATUS_INVALID)
    @Max(value = 3, message = ExceptionConstants.VOUCHER_STATUS_INVALID)
    private Integer status;

    @Valid
    List<VoucherSaveCondition> conditions;

    private List<ExtTimeCondition> extTimeConditions;

    private DifferentExtConditions differentExtConditions;

    public DifferentExtConditions getDifferentExtConditions() {
        return differentExtConditions;
    }

    public void setDifferentExtConditions(DifferentExtConditions differentExtConditions) {
        this.differentExtConditions = differentExtConditions;
    }

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<VoucherSaveCondition> getConditions() {
        return conditions;
    }

    public void setConditions(List<VoucherSaveCondition> conditions) {
        this.conditions = conditions;
    }

    public List<ExtTimeCondition> getExtTimeConditions() {
        return extTimeConditions;
    }

    public void setExtTimeConditions(List<ExtTimeCondition> extTimeConditions) {
        this.extTimeConditions = extTimeConditions;
    }
}
