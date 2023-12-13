package vn.softdreams.easypos.dto.report;

import java.io.Serializable;
import java.util.List;

public class ReportCommonResponse implements Serializable {

    private Integer comId;
    private String fromDate;
    private String toDate;
    private List<Object> detail;

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

    public List<Object> getDetail() {
        return detail;
    }

    public void setDetail(List<Object> detail) {
        this.detail = detail;
    }
}
