package vn.softdreams.easypos.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class RevenueCostPriceByProductResponse implements Serializable {

    @JsonProperty
    private int comId;

    @JsonProperty
    private String fromDate;

    @JsonProperty
    private String toDate;

    @JsonProperty
    private String sumQuantity;

    @JsonProperty
    private String sumRevenue;

    @JsonProperty
    private String sumDiscountAmount;

    @JsonProperty
    private String sumProfit;

    @JsonProperty
    private List<Detail> details;

    public int getComId() {
        return comId;
    }

    public void setComId(int comId) {
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

    public String getSumQuantity() {
        return sumQuantity;
    }

    public void setSumQuantity(String sumQuantity) {
        this.sumQuantity = sumQuantity;
    }

    public String getSumRevenue() {
        return sumRevenue;
    }

    public void setSumRevenue(String sumRevenue) {
        this.sumRevenue = sumRevenue;
    }

    public String getSumDiscountAmount() {
        return sumDiscountAmount;
    }

    public void setSumDiscountAmount(String sumDiscountAmount) {
        this.sumDiscountAmount = sumDiscountAmount;
    }

    public String getSumProfit() {
        return sumProfit;
    }

    public void setSumProfit(String sumProfit) {
        this.sumProfit = sumProfit;
    }

    public List<Detail> getDetails() {
        return details;
    }

    public void setDetails(List<Detail> details) {
        this.details = details;
    }

    public static class Detail implements Serializable {

        @JsonProperty
        private String productCode;

        @JsonProperty
        private String productName;

        @JsonProperty
        private String unitName;

        @JsonProperty
        private String quantity;

        @JsonProperty
        private String revenue;

        @JsonProperty
        private String discountAmount;

        @JsonProperty
        private String inPrice;

        @JsonProperty
        private String profit;

        public String getProductCode() {
            return productCode;
        }

        public void setProductCode(String productCode) {
            this.productCode = productCode;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getUnitName() {
            return unitName;
        }

        public void setUnitName(String unitName) {
            this.unitName = unitName;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getRevenue() {
            return revenue;
        }

        public void setRevenue(String revenue) {
            this.revenue = revenue;
        }

        public String getDiscountAmount() {
            return discountAmount;
        }

        public void setDiscountAmount(String discountAmount) {
            this.discountAmount = discountAmount;
        }

        public String getInPrice() {
            return inPrice;
        }

        public void setInPrice(String inPrice) {
            this.inPrice = inPrice;
        }

        public String getProfit() {
            return profit;
        }

        public void setProfit(String profit) {
            this.profit = profit;
        }
    }
}
