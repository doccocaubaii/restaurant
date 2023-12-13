package vn.softdreams.easypos.dto.backup;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import vn.softdreams.easypos.util.Common;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class BillProductBackup {

    @JsonProperty("ID")
    private String item;

    @JsonProperty("bill_id")
    private String bill_id;

    @JsonProperty("product_id")
    private String product_id;

    @JsonProperty("product_code")
    private String productCode;

    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("quantity")
    private Integer quantity;

    @JsonProperty("unit")
    private String unit;

    @JsonProperty("unit_price")
    private BigDecimal unitPrice;

    @JsonProperty("discount_amount")
    private BigDecimal discountAmount;

    @JsonProperty("total_pre_tax")
    private BigDecimal totalPreTax;

    @JsonProperty("vat_rate")
    private BigDecimal vatRate;

    @JsonProperty("vat_amount")
    private BigDecimal vatAmount;

    @JsonProperty("total_amount")
    private BigDecimal totalAmount;

    @JsonProperty("feature")
    private Integer feature;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @JsonProperty("create_time")
    private ZonedDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @JsonProperty("update_time")
    private ZonedDateTime updateTime;

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getBill_id() {
        return bill_id;
    }

    public void setBill_id(String bill_id) {
        this.bill_id = bill_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getTotalPreTax() {
        return totalPreTax;
    }

    public void setTotalPreTax(BigDecimal totalPreTax) {
        this.totalPreTax = totalPreTax;
    }

    public BigDecimal getVatRate() {
        return vatRate;
    }

    public void setVatRate(BigDecimal vatRate) {
        this.vatRate = vatRate;
    }

    public BigDecimal getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(BigDecimal vatAmount) {
        this.vatAmount = vatAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getFeature() {
        return feature;
    }

    public void setFeature(Integer feature) {
        this.feature = feature;
    }

    public ZonedDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(ZonedDateTime createTime) {
        this.createTime = createTime;
    }

    public ZonedDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(ZonedDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.createTime = Common.convertStringToZoneDateTime(updateTime, "yyyy-MM-dd'T'HH:mm:ss.SSS");
    }

    public void setCreateTime(String createTime) {
        this.createTime = Common.convertStringToZoneDateTime(createTime, "yyyy-MM-dd'T'HH:mm:ss.SSS");
    }
}
