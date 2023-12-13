package vn.softdreams.easypos.dto.report;

public class ReportRevenueCommonReq {

    private Integer comId;
    private String fromDate;
    private String toDate;
    private Integer type;

    public ReportRevenueCommonReq(Integer comId, String fromDate, String toDate, Integer type) {
        this.comId = comId;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.type = type;
    }

    public ReportRevenueCommonReq() {}

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
