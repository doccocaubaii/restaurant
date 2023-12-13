package vn.softdreams.easypos.dto.rsinoutward;

import java.io.Serializable;
import java.math.BigDecimal;

public class RsInOutWardStatusResult implements Serializable {

    private BigDecimal totalAmountInWard;
    private BigDecimal totalAmountOutWard;

    public RsInOutWardStatusResult(BigDecimal totalAmountInWard, BigDecimal totalAmountOutWard) {
        this.totalAmountInWard = totalAmountInWard;
        this.totalAmountOutWard = totalAmountOutWard;
    }

    public BigDecimal getTotalAmountInWard() {
        return totalAmountInWard;
    }

    public void setTotalAmountInWard(BigDecimal totalAmountInWard) {
        this.totalAmountInWard = totalAmountInWard;
    }

    public BigDecimal getTotalAmountOutWard() {
        return totalAmountOutWard;
    }

    public void setTotalAmountOutWard(BigDecimal totalAmountOutWard) {
        this.totalAmountOutWard = totalAmountOutWard;
    }
}
