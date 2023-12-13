package vn.softdreams.easypos.dto.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class ProductProfitResponse implements Serializable {

    private Integer comId;
    private String fromDate;
    private String toDate;
    private BigDecimal totalQuantity;
    private BigDecimal totalRevenue;
    private BigDecimal totalProfit;
    private List<ProductProfitResult> detail;

    public ProductProfitResponse() {}

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

    public BigDecimal getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(BigDecimal totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public BigDecimal getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(BigDecimal totalProfit) {
        this.totalProfit = totalProfit;
    }

    public List<ProductProfitResult> getDetail() {
        return detail;
    }

    public void setDetail(List<ProductProfitResult> detail) {
        this.detail = detail;
    }
}
