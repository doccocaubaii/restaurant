package vn.hust.restaurant.service.dto;

import vn.hust.restaurant.service.dto.bill.BillStatItem;

import java.util.List;

public class StaticResponse {
    private List<BillStatItem> revenue;
    private List<BillStatItem> pieChart;


    public StaticResponse() {
    }

    public StaticResponse(List<BillStatItem> revenue, List<BillStatItem> pieChart) {
        this.revenue = revenue;
        this.pieChart = pieChart;
    }

    public List<BillStatItem> getPieChart() {
        return pieChart;
    }

    public void setPieChart(List<BillStatItem> pieChart) {
        this.pieChart = pieChart;
    }

    public List<BillStatItem> getRevenue() {
        return revenue;
    }

    public void setRevenue(List<BillStatItem> revenue) {
        this.revenue = revenue;
    }
}
