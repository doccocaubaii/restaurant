package vn.softdreams.easypos.dto.voucher;

import java.io.Serializable;

public class VoucherApplyItemRequest implements Serializable {

    private Integer id;
    private Integer applyId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getApplyId() {
        return applyId;
    }

    public void setApplyId(Integer applyId) {
        this.applyId = applyId;
    }
}
