package vn.softdreams.easypos.dto.voucher;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

public class VoucherWebSaveRequest implements Serializable {

    private Integer id;

    @NotNull(message = ExceptionConstants.COMPANY_ID_NOT_NULL)
    private Integer comId;

    @NotBlank(message = ExceptionConstants.VOUCHER_CODE_NOT_BLANK)
    @Size(max = 20, message = ExceptionConstants.VOUCHER_CODE_INVALID)
    private String code;

    @NotBlank(message = ExceptionConstants.VOUCHER_NAME_NOT_BLANK)
    private String name;

    @NotNull(message = ExceptionConstants.VOUCHER_TYPE_NOT_NULL)
    @Min(value = 1, message = ExceptionConstants.VOUCHER_TYPE_INVALID)
    @Max(value = 2, message = ExceptionConstants.VOUCHER_TYPE_INVALID)
    private Integer type;

    @NotNull(message = ExceptionConstants.VOUCHER_VALUE_NOT_NULL)
    private BigDecimal value;

    @NotBlank(message = ExceptionConstants.VOUCHER_START_DATE_NOT_BLANK)
    private String startTime;

    @NotBlank(message = ExceptionConstants.VOUCHER_END_DATE_NOT_BLANK)
    private String endTime;

    @NotNull(message = ExceptionConstants.VOUCHER_STATUS_NOT_NULL)
    @Min(value = 0, message = ExceptionConstants.VOUCHER_STATUS_INVALID)
    @Max(value = 3, message = ExceptionConstants.VOUCHER_STATUS_INVALID)
    private Integer status;

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

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
