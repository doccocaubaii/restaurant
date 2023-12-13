package vn.softdreams.easypos.integration.easybooks88.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class GetInventoryDetailReportResponse implements Serializable {

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
        private BigDecimal openingQuantity;

        @JsonProperty("slDauKy")
        private String openingQuantityToString;

        @JsonProperty
        private BigDecimal openingAmount;

        @JsonProperty("tienDauKy")
        private String openingAmountToString;

        @JsonProperty
        private BigDecimal iwQuantity;

        @JsonProperty("slNhap")
        private String iwQuantityToString;

        @JsonProperty
        private BigDecimal iwAmount;

        @JsonProperty("tienNhap")
        private String iwAmountToString;

        @JsonProperty
        private BigDecimal owQuantity;

        @JsonProperty("slXuat")
        private String owQuantityToString;

        @JsonProperty
        private BigDecimal owAmount;

        @JsonProperty("tienXuat")
        private String owAmountToString;

        @JsonProperty
        private BigDecimal closingQuantity;

        @JsonProperty("slTon")
        private String closingQuantityToString;

        @JsonProperty
        private BigDecimal closingAmount;

        @JsonProperty("tienTon")
        private String closingAmountToString;

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

        public BigDecimal getOpeningQuantity() {
            return openingQuantity;
        }

        public void setOpeningQuantity(BigDecimal openingQuantity) {
            this.openingQuantity = openingQuantity;
        }

        public BigDecimal getOpeningAmount() {
            return openingAmount;
        }

        public void setOpeningAmount(BigDecimal openingAmount) {
            this.openingAmount = openingAmount;
        }

        public BigDecimal getIwQuantity() {
            return iwQuantity;
        }

        public void setIwQuantity(BigDecimal iwQuantity) {
            this.iwQuantity = iwQuantity;
        }

        public BigDecimal getIwAmount() {
            return iwAmount;
        }

        public void setIwAmount(BigDecimal iwAmount) {
            this.iwAmount = iwAmount;
        }

        public BigDecimal getOwQuantity() {
            return owQuantity;
        }

        public void setOwQuantity(BigDecimal owQuantity) {
            this.owQuantity = owQuantity;
        }

        public BigDecimal getOwAmount() {
            return owAmount;
        }

        public void setOwAmount(BigDecimal owAmount) {
            this.owAmount = owAmount;
        }

        public BigDecimal getClosingQuantity() {
            return closingQuantity;
        }

        public void setClosingQuantity(BigDecimal closingQuantity) {
            this.closingQuantity = closingQuantity;
        }

        public BigDecimal getClosingAmount() {
            return closingAmount;
        }

        public void setClosingAmount(BigDecimal closingAmount) {
            this.closingAmount = closingAmount;
        }

        public String getOpeningQuantityToString() {
            return openingQuantityToString;
        }

        public void setOpeningQuantityToString(String openingQuantityToString) {
            this.openingQuantityToString = openingQuantityToString;
        }

        public String getOpeningAmountToString() {
            return openingAmountToString;
        }

        public void setOpeningAmountToString(String openingAmountToString) {
            this.openingAmountToString = openingAmountToString;
        }

        public String getIwQuantityToString() {
            return iwQuantityToString;
        }

        public void setIwQuantityToString(String iwQuantityToString) {
            this.iwQuantityToString = iwQuantityToString;
        }

        public String getIwAmountToString() {
            return iwAmountToString;
        }

        public void setIwAmountToString(String iwAmountToString) {
            this.iwAmountToString = iwAmountToString;
        }

        public String getOwQuantityToString() {
            return owQuantityToString;
        }

        public void setOwQuantityToString(String owQuantityToString) {
            this.owQuantityToString = owQuantityToString;
        }

        public String getOwAmountToString() {
            return owAmountToString;
        }

        public void setOwAmountToString(String owAmountToString) {
            this.owAmountToString = owAmountToString;
        }

        public String getClosingQuantityToString() {
            return closingQuantityToString;
        }

        public void setClosingQuantityToString(String closingQuantityToString) {
            this.closingQuantityToString = closingQuantityToString;
        }

        public String getClosingAmountToString() {
            return closingAmountToString;
        }

        public void setClosingAmountToString(String closingAmountToString) {
            this.closingAmountToString = closingAmountToString;
        }
    }
}
