package vn.softdreams.easypos.dto.bill;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

public class BillVoucherRequest {

    @NotNull(message = ExceptionConstants.VOUCHER_ID_NOT_BLANK)
    private Integer id;

    @NotBlank(message = ExceptionConstants.VOUCHER_CODE_NOT_BLANK)
    @Size(max = 20, message = ExceptionConstants.VOUCHER_CODE_INVALID)
    private String code;

    @NotNull(message = ExceptionConstants.VOUCHER_DISCOUNT_NOT_NULL)
    @Min(value = 0, message = ExceptionConstants.VOUCHER_DISCOUNT_NOT_VALID)
    private BigDecimal voucherValue;

    private String desc;

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
