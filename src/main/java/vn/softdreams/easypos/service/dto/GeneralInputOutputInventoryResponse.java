package vn.softdreams.easypos.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class GeneralInputOutputInventoryResponse implements Serializable {

    @JsonProperty
    private int comId;

    @JsonProperty
    private String fromDate;

    @JsonProperty
    private String toDate;

    @JsonProperty
    private String sumOpeningQuantity;

    @JsonProperty
    private String sumOpeningAmount;

    @JsonProperty
    private String sumIWQuantity;

    @JsonProperty
    private String sumIWAmount;

    @JsonProperty
    private String sumOWQuantity;

    @JsonProperty
    private String sumOWAmount;

    @JsonProperty
    private String sumClosingQuantity;

    @JsonProperty
    private String sumClosingAmount;

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

    public String getSumOpeningQuantity() {
        return sumOpeningQuantity;
    }

    public void setSumOpeningQuantity(String sumOpeningQuantity) {
        this.sumOpeningQuantity = sumOpeningQuantity;
    }

    public String getSumOpeningAmount() {
        return sumOpeningAmount;
    }

    public void setSumOpeningAmount(String sumOpeningAmount) {
        this.sumOpeningAmount = sumOpeningAmount;
    }

    public String getSumIWQuantity() {
        return sumIWQuantity;
    }

    public void setSumIWQuantity(String sumIWQuantity) {
        this.sumIWQuantity = sumIWQuantity;
    }

    public String getSumIWAmount() {
        return sumIWAmount;
    }

    public void setSumIWAmount(String sumIWAmount) {
        this.sumIWAmount = sumIWAmount;
    }

    public String getSumOWQuantity() {
        return sumOWQuantity;
    }

    public void setSumOWQuantity(String sumOWQuantity) {
        this.sumOWQuantity = sumOWQuantity;
    }

    public String getSumOWAmount() {
        return sumOWAmount;
    }

    public void setSumOWAmount(String sumOWAmount) {
        this.sumOWAmount = sumOWAmount;
    }

    public String getSumClosingQuantity() {
        return sumClosingQuantity;
    }

    public void setSumClosingQuantity(String sumClosingQuantity) {
        this.sumClosingQuantity = sumClosingQuantity;
    }

    public String getSumClosingAmount() {
        return sumClosingAmount;
    }

    public void setSumClosingAmount(String sumClosingAmount) {
        this.sumClosingAmount = sumClosingAmount;
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
        private String openingQuantity;

        @JsonProperty
        private String openingAmount;

        @JsonProperty
        private String iwQuantity;

        @JsonProperty
        private String iwAmount;

        @JsonProperty
        private String owQuantity;

        @JsonProperty
        private String owAmount;

        @JsonProperty
        private String closingQuantity;

        @JsonProperty
        private String closingAmount;

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

        public String getOpeningQuantity() {
            return openingQuantity;
        }

        public void setOpeningQuantity(String openingQuantity) {
            this.openingQuantity = openingQuantity;
        }

        public String getOpeningAmount() {
            return openingAmount;
        }

        public void setOpeningAmount(String openingAmount) {
            this.openingAmount = openingAmount;
        }

        public String getIwQuantity() {
            return iwQuantity;
        }

        public void setIwQuantity(String iwQuantity) {
            this.iwQuantity = iwQuantity;
        }

        public String getIwAmount() {
            return iwAmount;
        }

        public void setIwAmount(String iwAmount) {
            this.iwAmount = iwAmount;
        }

        public String getOwQuantity() {
            return owQuantity;
        }

        public void setOwQuantity(String owQuantity) {
            this.owQuantity = owQuantity;
        }

        public String getOwAmount() {
            return owAmount;
        }

        public void setOwAmount(String owAmount) {
            this.owAmount = owAmount;
        }

        public String getClosingQuantity() {
            return closingQuantity;
        }

        public void setClosingQuantity(String closingQuantity) {
            this.closingQuantity = closingQuantity;
        }

        public String getClosingAmount() {
            return closingAmount;
        }

        public void setClosingAmount(String closingAmount) {
            this.closingAmount = closingAmount;
        }
    }
}
