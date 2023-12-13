package vn.softdreams.easypos.integration.easybooks88.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class GetSalesDetailReportResponse implements Serializable {

    @JsonProperty
    private String period;

    @JsonProperty
    private List<Data> data;

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public static class Data implements Serializable {

        @JsonProperty
        private String materialGoodsCode;

        @JsonProperty
        private String materialGoodsName;

        @JsonProperty
        private String unitName;

        @JsonProperty
        private BigDecimal mainQuantity;

        @JsonProperty
        private String mainQuantityToString;

        @JsonProperty
        private BigDecimal amount;

        @JsonProperty
        private String amountToString;

        @JsonProperty
        private BigDecimal discountAmount;

        @JsonProperty
        private String discountAmountToString;

        @JsonProperty("giamgia")
        private BigDecimal salesOff;

        @JsonProperty("giamgiaToString")
        private String salesOffToString;

        @JsonProperty("tralai")
        private BigDecimal returnMoney;

        @JsonProperty("tralaiToString")
        private String returnMoneyToString;

        @JsonProperty("giavon")
        private BigDecimal costPrice;

        @JsonProperty("giavonToString")
        private String costPriceToString;

        @JsonProperty("lailo")
        private BigDecimal profitAndLoss;

        @JsonProperty("lailoToString")
        private String profitAndLossToString;

        public String getMaterialGoodsCode() {
            return materialGoodsCode;
        }

        public void setMaterialGoodsCode(String materialGoodsCode) {
            this.materialGoodsCode = materialGoodsCode;
        }

        public String getMaterialGoodsName() {
            return materialGoodsName;
        }

        public void setMaterialGoodsName(String materialGoodsName) {
            this.materialGoodsName = materialGoodsName;
        }

        public String getUnitName() {
            return unitName;
        }

        public void setUnitName(String unitName) {
            this.unitName = unitName;
        }

        public BigDecimal getMainQuantity() {
            return mainQuantity;
        }

        public void setMainQuantity(BigDecimal mainQuantity) {
            this.mainQuantity = mainQuantity;
        }

        public String getMainQuantityToString() {
            return mainQuantityToString;
        }

        public void setMainQuantityToString(String mainQuantityToString) {
            this.mainQuantityToString = mainQuantityToString;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public String getAmountToString() {
            return amountToString;
        }

        public void setAmountToString(String amountToString) {
            this.amountToString = amountToString;
        }

        public BigDecimal getDiscountAmount() {
            return discountAmount;
        }

        public void setDiscountAmount(BigDecimal discountAmount) {
            this.discountAmount = discountAmount;
        }

        public String getDiscountAmountToString() {
            return discountAmountToString;
        }

        public void setDiscountAmountToString(String discountAmountToString) {
            this.discountAmountToString = discountAmountToString;
        }

        public BigDecimal getSalesOff() {
            return salesOff;
        }

        public void setSalesOff(BigDecimal salesOff) {
            this.salesOff = salesOff;
        }

        public String getSalesOffToString() {
            return salesOffToString;
        }

        public void setSalesOffToString(String salesOffToString) {
            this.salesOffToString = salesOffToString;
        }

        public BigDecimal getReturnMoney() {
            return returnMoney;
        }

        public void setReturnMoney(BigDecimal returnMoney) {
            this.returnMoney = returnMoney;
        }

        public String getReturnMoneyToString() {
            return returnMoneyToString;
        }

        public void setReturnMoneyToString(String returnMoneyToString) {
            this.returnMoneyToString = returnMoneyToString;
        }

        public BigDecimal getCostPrice() {
            return costPrice;
        }

        public void setCostPrice(BigDecimal costPrice) {
            this.costPrice = costPrice;
        }

        public String getCostPriceToString() {
            return costPriceToString;
        }

        public void setCostPriceToString(String costPriceToString) {
            this.costPriceToString = costPriceToString;
        }

        public BigDecimal getProfitAndLoss() {
            return profitAndLoss;
        }

        public void setProfitAndLoss(BigDecimal profitAndLoss) {
            this.profitAndLoss = profitAndLoss;
        }

        public String getProfitAndLossToString() {
            return profitAndLossToString;
        }

        public void setProfitAndLossToString(String profitAndLossToString) {
            this.profitAndLossToString = profitAndLossToString;
        }
    }
}
