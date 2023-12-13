package vn.softdreams.easypos.dto.rsinoutward;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class GetAllTransactionResponse implements Serializable {

    private Integer comId;
    private String fromDate;
    private String toDate;
    private BigDecimal inWardAmount = BigDecimal.ZERO;
    private BigDecimal outWardAmount = BigDecimal.ZERO;
    private List<RsInoutWardResponse> inOutWardList;

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public BigDecimal getInWardAmount() {
        return inWardAmount != null ? inWardAmount.setScale(6) : null;
    }

    public void setInWardAmount(BigDecimal inWardAmount) {
        this.inWardAmount = inWardAmount;
    }

    public BigDecimal getOutWardAmount() {
        return outWardAmount != null ? outWardAmount.setScale(6) : null;
    }

    public void setOutWardAmount(BigDecimal outWardAmount) {
        this.outWardAmount = outWardAmount;
    }

    public List<RsInoutWardResponse> getInOutWardList() {
        return inOutWardList;
    }

    public void setInOutWardList(List<RsInoutWardResponse> inOutWardList) {
        this.inOutWardList = inOutWardList;
    }
}
