package vn.softdreams.easypos.dto.productGroup;

import com.fasterxml.jackson.annotation.JsonFormat;
import vn.softdreams.easypos.config.Constants;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class ProductOfflineResponse implements Serializable {

    private Integer id;
    private Integer comId;
    private String code;
    private String code2;
    private String name;
    private String unit;
    private BigDecimal inPrice;
    private BigDecimal outPrice;
    private String barCode;
    private String barCode2;
    private Integer vatRate;
    private Integer inventoryId;
    private BigDecimal inventoryCount;
    private String description;
    private Boolean active;
    private String image;
    private Boolean isInventory;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    private ZonedDateTime createTime;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    private ZonedDateTime updateTime;

    private Integer productGroupId;

    public ProductOfflineResponse() {}

    public ProductOfflineResponse(
        Integer id,
        Integer comId,
        String code,
        String code2,
        String name,
        String unit,
        BigDecimal inPrice,
        BigDecimal outPrice,
        String barCode,
        String barCode2,
        Integer vatRate,
        Integer inventoryId,
        BigDecimal inventoryCount,
        String description,
        Boolean active,
        String image,
        Boolean isInventory,
        ZonedDateTime createTime,
        ZonedDateTime updateTime,
        Integer productGroupId
    ) {
        this.id = id;
        this.comId = comId;
        this.code = code;
        this.code2 = code2;
        this.name = name;
        this.unit = unit;
        this.inPrice = inPrice;
        this.outPrice = outPrice;
        this.barCode = barCode;
        this.barCode2 = barCode2;
        this.vatRate = vatRate;
        this.inventoryId = inventoryId;
        this.inventoryCount = inventoryCount;
        this.description = description;
        this.active = active;
        this.image = image;
        this.isInventory = isInventory;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.productGroupId = productGroupId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getBarCode2() {
        return barCode2;
    }

    public void setBarCode2(String barCode2) {
        this.barCode2 = barCode2;
    }

    public Integer getVatRate() {
        return vatRate;
    }

    public void setVatRate(Integer vatRate) {
        this.vatRate = vatRate;
    }

    public Integer getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Integer inventoryId) {
        this.inventoryId = inventoryId;
    }

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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getInventory() {
        return isInventory;
    }

    public void setInventory(Boolean inventory) {
        isInventory = inventory;
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

    public Integer getProductGroupId() {
        return productGroupId;
    }

    public void setProductGroupId(Integer productGroupId) {
        this.productGroupId = productGroupId;
    }
}
