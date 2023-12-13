package vn.softdreams.easypos.dto.backup;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import vn.softdreams.easypos.util.Common;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class BackupProduct implements Serializable {

    @JsonProperty("ID")
    private String item;

    @JsonProperty("code")
    private String code2;

    @JsonProperty("name")
    private String name;

    @JsonProperty("unit")
    private String unit;

    @JsonProperty("in_price")
    private BigDecimal inPrice;

    @JsonProperty("out_price")
    private BigDecimal outPrice;

    @JsonProperty("bar_code")
    private String barCode;

    @JsonProperty("vat_rate")
    private Integer vatRate;

    @JsonProperty("inventory_id")
    private String inventoryIdItem;

    @JsonProperty("inventory_count")
    private BigDecimal inventoryCount;

    @JsonProperty("description")
    private String description;

    @JsonProperty("org_id")
    private String orgId33;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @JsonProperty("create_time")
    private ZonedDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @JsonProperty("update_time")
    private ZonedDateTime updateTime;

    public BackupProduct() {}

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getInventoryIdItem() {
        return inventoryIdItem;
    }

    public void setInventoryIdItem(String inventoryIdItem) {
        this.inventoryIdItem = inventoryIdItem;
    }

    public String getOrgId33() {
        return orgId33;
    }

    public void setOrgId33(String orgId33) {
        this.orgId33 = orgId33;
    }

    public ZonedDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(ZonedDateTime createTime) {
        this.createTime = createTime;
    }

    public void setCreateTime(String createTime) {
        String path = createTime.length() > 23 ? "yyyy-MM-dd'T'HH:mm:ss.SSSSSS" : "yyyy-MM-dd'T'HH:mm:ss.SSS";
        this.createTime = Common.convertStringToZoneDateTime(createTime, path);
    }

    public ZonedDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(ZonedDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public void setUpdateTime(String updateTime) {
        String path = updateTime.length() > 23 ? "yyyy-MM-dd'T'HH:mm:ss.SSSSSS" : "yyyy-MM-dd'T'HH:mm:ss.SSS";
        this.updateTime = Common.convertStringToZoneDateTime(updateTime, path);
    }

    public String getCode2() {
        return code2;
    }

    public void setCode2(String code2) {
        this.code2 = code2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getInPrice() {
        return inPrice;
    }

    public void setInPrice(BigDecimal inPrice) {
        this.inPrice = inPrice;
    }

    public BigDecimal getOutPrice() {
        return outPrice;
    }

    public void setOutPrice(BigDecimal outPrice) {
        this.outPrice = outPrice;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public Integer getVatRate() {
        return vatRate;
    }

    public void setVatRate(Integer vatRate) {
        this.vatRate = vatRate;
    }

    //    public Integer getInventoryId() {
    //        return inventoryId;
    //    }
    //
    //    public void setInventoryId(Integer inventoryId) {
    //        this.inventoryId = inventoryId;
    //    }

    public BigDecimal getInventoryCount() {
        return inventoryCount;
    }

    public void setInventoryCount(BigDecimal inventoryCount) {
        this.inventoryCount = inventoryCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
