package vn.softdreams.easypos.dto.report;

import org.apache.poi.hpsf.Decimal;

public class DetailRes {

    private String fromDate;
    private String toDate;
    private Decimal revenue;
    private Decimal profit;

    public DetailRes(String fromDate, String toDate, Decimal revenue, Decimal profit) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.revenue = revenue;
        this.profit = profit;
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

    public Decimal getRevenue() {
        return revenue;
    }

    public void setRevenue(Decimal revenue) {
        this.revenue = revenue;
    }

    public Decimal getProfit() {
        return profit;
    }

    public void setProfit(Decimal profit) {
        this.profit = profit;
    }
}
