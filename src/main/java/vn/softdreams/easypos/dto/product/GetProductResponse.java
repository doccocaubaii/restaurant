package vn.softdreams.easypos.dto.product;

import vn.softdreams.easypos.domain.Product;
import vn.softdreams.easypos.domain.ProductGroup;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

public class GetProductResponse implements Serializable {

    private Integer id;
    private Integer comId;
    private String code;
    private String code2;
    private String name;
    private String unit;
    private Integer unitId;
    private BigDecimal inPrice;
    private BigDecimal outPrice;
    private Integer vatRate;
    private Boolean inventoryTracking;
    private BigDecimal inventoryCount;
    private String description;
    private ZonedDateTime createTime;
    private ZonedDateTime updateTime;
    private List<ProductGroup> productGroups;

    public GetProductResponse() {}

    public GetProductResponse(
        Integer id,
        Integer comId,
        String code,
        String code2,
        String name,
        String unit,
        Integer unitId,
        BigDecimal inPrice,
        BigDecimal outPrice,
        Integer vatRate,
        Boolean inventoryTracking,
        BigDecimal inventoryCount,
        String description,
        ZonedDateTime createTime,
        ZonedDateTime updateTime,
        List<ProductGroup> productGroups
    ) {
        this.id = id;
        this.comId = comId;
        this.code = code;
        this.code2 = code2;
        this.name = name;
        this.unit = unit;
        this.unitId = unitId;
        this.inPrice = inPrice;
        this.outPrice = outPrice;
        this.vatRate = vatRate;
        this.inventoryTracking = inventoryTracking;
        this.inventoryCount = inventoryCount;
        this.description = description;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.productGroups = productGroups;
    }

    public GetProductResponse(
        Integer id,
        Integer comId,
        String code,
        String code2,
        String name,
        String unit,
        Integer unitId,
        BigDecimal inPrice,
        BigDecimal outPrice,
        Integer vatRate,
        Boolean inventoryTracking,
        BigDecimal inventoryCount,
        String description,
        ZonedDateTime createTime,
        ZonedDateTime updateTime
    ) {
        this.id = id;
        this.comId = comId;
        this.code = code;
        this.code2 = code2;
        this.name = name;
        this.unit = unit;
        this.unitId = unitId;
        this.inPrice = inPrice;
        this.outPrice = outPrice;
        this.vatRate = vatRate;
        this.inventoryTracking = inventoryTracking;
        this.inventoryCount = inventoryCount;
        this.description = description;
        this.createTime = createTime;
        this.updateTime = updateTime;
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

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
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

    public Integer getVatRate() {
        return vatRate;
    }

    public void setVatRate(Integer vatRate) {
        this.vatRate = vatRate;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public List<ProductGroup> getProductGroups() {
        return productGroups;
    }

    public void setProductGroups(List<ProductGroup> productGroups) {
        this.productGroups = productGroups;
    }

    public void convertProductToGetProductResponse(Product p) {
        this.id = p.getId();
        this.comId = p.getComId();
        this.code = p.getCode();
        this.code2 = p.getCode2();
        this.name = p.getName();
        this.unit = p.getUnit();
        this.unitId = p.getUnitId();
        this.inPrice = p.getPurchasePrice();
        this.outPrice = p.getSalePrice();
        this.vatRate = p.getVatRate();
        this.inventoryTracking = p.getInventoryTracking();
        this.inventoryCount = p.getInventoryCount();
        this.description = p.getDescription();
        this.createTime = p.getCreateTime();
        this.updateTime = p.getUpdateTime();
        this.productGroups = p.getProductGroups();
    }
}
