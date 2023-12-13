package vn.softdreams.easypos.dto.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

public class ProductExcelResponse implements Serializable {

    private String name;
    private String code2;
    private String barCode;
    private String groupName;
    private BigDecimal inPrice;
    private BigDecimal outPrice;
    private String unit;
    private Integer unitId;
    private Boolean inventoryTracking = false;
    private BigDecimal inventoryCount;
    private Integer vatRate;
    private String description;

    // attribute for response validate import
    private Map<Integer, String> messageErrorMap;
    private Boolean status = false;

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Boolean getInventoryTracking() {
        return inventoryTracking;
    }

    public void setInventoryTracking(Boolean inventoryTracking) {
        this.inventoryTracking = inventoryTracking;
    }

    public BigDecimal getInventoryCount() {
        return inventoryCount;
    }

    public void setInventoryCount(BigDecimal inventoryCount) {
        this.inventoryCount = inventoryCount;
    }

    public Integer getVatRate() {
        return vatRate;
    }

    public void setVatRate(Integer vatRate) {
        this.vatRate = vatRate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getCode2() {
        return code2;
    }

    public void setCode2(String code2) {
        this.code2 = code2;
    }

    public Map<Integer, String> getMessageErrorMap() {
        return messageErrorMap;
    }

    public void setMessageErrorMap(Map<Integer, String> messageErrorMap) {
        this.messageErrorMap = messageErrorMap;
    }
}
