package vn.softdreams.easypos.dto.audit;

import java.io.Serializable;
import java.util.List;

public class AllActivityHistoryResponse implements Serializable {

    private Integer comId;
    private String fromDate;
    private String toDate;
    private List<ActivityHistoryResponse> detail;

    public AllActivityHistoryResponse(Integer comId, String fromDate, String toDate, List<ActivityHistoryResponse> detail) {
        this.comId = comId;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.detail = detail;
    }

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

    public List<ActivityHistoryResponse> getDetail() {
        return detail;
    }

    public void setDetail(List<ActivityHistoryResponse> detail) {
        this.detail = detail;
    }
}
