package vn.softdreams.easypos.dto.bill;

import java.math.BigDecimal;
import java.util.List;

public class BillRevenueResult {

    private BigDecimal revenue;

    private List<RevenueByMonth> list;

    public BillRevenueResult() {}

    public BillRevenueResult(BigDecimal revenue, List<RevenueByMonth> list) {
        this.revenue = revenue;
        this.list = list;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    public void setRevenue(BigDecimal revenue) {
        this.revenue = revenue;
    }

    public List<RevenueByMonth> getList() {
        return list;
    }

    public void setList(List<RevenueByMonth> list) {
        this.list = list;
    }
}
