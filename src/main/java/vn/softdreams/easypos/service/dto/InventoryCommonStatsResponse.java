package vn.softdreams.easypos.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.math.BigDecimal;

public class InventoryCommonStatsResponse implements Serializable {

    @JsonProperty
    private int comId;

    @JsonProperty
    private String fromDate;

    @JsonProperty
    private String toDate;

    @JsonProperty
    private String productCode;

    @JsonProperty
    private String productName;

    @JsonProperty
    private BigDecimal openingQuantity;

    @JsonProperty
    private BigDecimal openingAmount;

    @JsonProperty
    private String createTime;

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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
