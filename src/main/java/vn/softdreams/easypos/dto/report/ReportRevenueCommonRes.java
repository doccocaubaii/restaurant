package vn.softdreams.easypos.dto.report;

import java.math.BigDecimal;
import java.util.List;

public class ReportRevenueCommonRes {

    private Integer comId;
    private String fromDate;
    private String toDate;
    private BigDecimal revenue;
    private BigDecimal profit;
    private String createTime;
    private List<DetailRes> detail;

    public ReportRevenueCommonRes(
        Integer comId,
        String fromDate,
        String toDate,
        BigDecimal revenue,
        BigDecimal profit,
        String createTime,
        List<DetailRes> detail
    ) {
        this.comId = comId;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.revenue = revenue;
        this.profit = profit;
        this.createTime = createTime;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public List<DetailRes> getDetail() {
        return detail;
    }

    public void setDetail(List<DetailRes> detail) {
        this.detail = detail;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    public void setRevenue(BigDecimal revenue) {
        this.revenue = revenue;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }
}
