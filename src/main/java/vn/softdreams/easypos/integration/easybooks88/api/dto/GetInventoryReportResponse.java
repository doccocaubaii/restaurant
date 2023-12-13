package vn.softdreams.easypos.integration.easybooks88.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.math.BigDecimal;

public class GetInventoryReportResponse implements Serializable {

    @JsonProperty
    private String materialGoodsCode;

    @JsonProperty
    private String materialGoodsName;

    @JsonProperty
    private String unitName;

    @JsonProperty
    private BigDecimal openingQuantity;

    @JsonProperty
    private BigDecimal openingAmount;

    @JsonProperty
    private BigDecimal iwQuantity;

    @JsonProperty
    private BigDecimal iwAmount;

    @JsonProperty
    private BigDecimal owQuantity;

    @JsonProperty
    private BigDecimal owAmount;

    @JsonProperty
    private BigDecimal closingQuantity;

    @JsonProperty
    private BigDecimal closingAmount;

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
}
