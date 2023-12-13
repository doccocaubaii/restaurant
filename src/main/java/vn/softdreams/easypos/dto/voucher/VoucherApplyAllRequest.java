package vn.softdreams.easypos.dto.voucher;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

public class VoucherApplyAllRequest implements Serializable {

    @NotNull(message = ExceptionConstants.COMPANY_ID_NOT_NULL)
    private Integer comId;

    @NotNull(message = ExceptionConstants.VOUCHER_ID_NOT_EMPTY)
    private Integer voucherId;

    @NotNull(message = ExceptionConstants.VOUCHER_APPLY_TYPE_NOT_NULL)
    @Max(value = 2, message = ExceptionConstants.VOUCHER_APPLY_TYPE_INVALID)
    @Min(value = 1, message = ExceptionConstants.VOUCHER_APPLY_TYPE_INVALID)
    private Integer applyType;

    @NotNull(message = ExceptionConstants.VOUCHER_APPLY_TYPE_NOT_NULL)
    @Max(value = 2, message = ExceptionConstants.VOUCHER_APPLY_TYPE_INVALID)
    @Min(value = 1, message = ExceptionConstants.VOUCHER_APPLY_TYPE_INVALID)
    private Integer checkAllType;

    private List<VoucherApplyItemRequest> applyItem;

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public Integer getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(Integer voucherId) {
        this.voucherId = voucherId;
    }

    public Integer getApplyType() {
        return applyType;
    }

    public void setApplyType(Integer applyType) {
        this.applyType = applyType;
    }

    public Integer getCheckAllType() {
        return checkAllType;
    }

    public void setCheckAllType(Integer checkAllType) {
        this.checkAllType = checkAllType;
    }

    public List<VoucherApplyItemRequest> getApplyItem() {
        return applyItem;
    }

    public void setApplyItem(List<VoucherApplyItemRequest> applyItem) {
        this.applyItem = applyItem;
    }
}
