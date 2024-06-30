package vn.hust.restaurant.service.dto.bill;

import java.math.BigDecimal;

public class RevenueByMonth implements Comparable<RevenueByMonth> {

    private String month;
    private BigDecimal revenue;
    private BigDecimal profit;

    public RevenueByMonth(String month, BigDecimal revenue, BigDecimal profit) {
        this.month = month;
        this.revenue = revenue;
        this.profit = profit;
    }

    public RevenueByMonth() {}

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
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

    @Override
    public int compareTo(RevenueByMonth other) {
        return this.getMonth().compareTo(other.getMonth());
    }
}
