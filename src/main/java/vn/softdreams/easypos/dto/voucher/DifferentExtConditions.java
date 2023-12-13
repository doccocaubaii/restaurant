package vn.softdreams.easypos.dto.voucher;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DifferentExtConditions implements Serializable {

    private Boolean autoApplyVoucher = false;
    private Boolean isFixedQuantity = true;

    public DifferentExtConditions() {}

    public DifferentExtConditions(Boolean autoApplyVoucher, Boolean isFixedQuantity) {
        this.autoApplyVoucher = autoApplyVoucher;
        this.isFixedQuantity = isFixedQuantity;
    }

    public Boolean getAutoApplyVoucher() {
        return autoApplyVoucher;
    }

    public void setAutoApplyVoucher(Boolean autoApplyVoucher) {
        this.autoApplyVoucher = autoApplyVoucher;
    }

    public Boolean getIsFixedQuantity() {
        return isFixedQuantity;
    }

    public void setIsFixedQuantity(Boolean fixedQuantity) {
        isFixedQuantity = fixedQuantity;
    }
}
