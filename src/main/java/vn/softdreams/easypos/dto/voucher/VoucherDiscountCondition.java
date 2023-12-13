package vn.softdreams.easypos.dto.voucher;

import java.io.Serializable;

public class VoucherDiscountCondition implements Serializable {

    private Integer type;
    private String data;
    private String note;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
