package vn.softdreams.easypos.dto.product;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProductProfitResult implements Serializable {

    private Integer id;
    private String name;
    private Integer unitId;
    private String unitName;
    private BigDecimal quantity;
    private BigDecimal revenue;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal costPrice;

    private BigDecimal profit;

    public ProductProfitResult() {}

    //    getProductCostStats
    public ProductProfitResult(Integer id, String name, String unitName, BigDecimal quantity, BigDecimal costPrice, Integer unitId) {
        this.id = id;
        this.unitName = unitName;
        this.name = name;
        this.quantity = quantity;
        this.costPrice = costPrice;
        this.unitId = unitId;
    }

    //    getProductRevenueStats
    public ProductProfitResult(Integer id, BigDecimal revenue, BigDecimal quantity, String name, String unitName, Integer unitId) {
        this.id = id;
        this.name = name;
        this.unitName = unitName;
        this.revenue = revenue;
        this.quantity = quantity;
        this.unitId = unitId;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    public void setRevenue(BigDecimal revenue) {
        this.revenue = revenue;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }
}
