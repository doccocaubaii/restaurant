package vn.softdreams.easypos.dto.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import vn.softdreams.easypos.config.Constants;
import vn.softdreams.easypos.dto.productGroup.ProductGroupResult;
import vn.softdreams.easypos.dto.productGroup.ProductProcessingAreaResult;
import vn.softdreams.easypos.dto.productUnit.ProductProductUnitResponse;
import vn.softdreams.easypos.dto.toppingGroup.ToppingItem;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

public class ProductDetailResponse {

    private Integer id;
    private Integer comId;
    private String code;
    private String code2;
    private String name;
    private String unit;
    private Integer unitId;
    private BigDecimal purchasePrice;
    private BigDecimal salePrice;
    private Integer vatRate;
    private Boolean inventoryTracking;
    private BigDecimal inventoryCount;
    private String description;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    private ZonedDateTime createTime;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    private ZonedDateTime updateTime;

    private String barcode;

    private String imageUrl;
    private Boolean isTopping;
    private String note;
    private Integer discountVatRate;

    private List<ProductGroupResult> groups;
    private ProductProcessingAreaResult processingArea;

    private List<ProductProductUnitResponse> conversionUnits;

    private List<ToppingItem> toppings;

    public ProductDetailResponse() {}

    public ProductDetailResponse(
        Integer id,
        Integer comId,
        String code,
        String code2,
        String name,
        String unit,
        Integer unitId,
        BigDecimal purchasePrice,
        BigDecimal salePrice,
        Integer vatRate,
        Boolean inventoryTracking,
        BigDecimal inventoryCount,
        String description,
        ZonedDateTime createTime,
        ZonedDateTime updateTime,
        String barcode,
        String imageUrl,
        Boolean isTopping
    ) {
        this.id = id;
        this.comId = comId;
        this.code = code;
        this.code2 = code2;
        this.name = name;
        this.unit = unit;
        this.unitId = unitId;
        this.purchasePrice = purchasePrice;
        this.salePrice = salePrice;
        this.vatRate = vatRate;
        this.inventoryTracking = inventoryTracking;
        this.inventoryCount = inventoryCount;
        this.description = description;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.barcode = barcode;
        this.imageUrl = imageUrl;
        this.isTopping = isTopping;
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
        return unit == null ? null : unit.trim();
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

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
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

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getIsTopping() {
        return isTopping;
    }

    public void setIsTopping(Boolean topping) {
        isTopping = topping;
    }

    public List<ProductGroupResult> getGroups() {
        return groups;
    }

    public void setGroups(List<ProductGroupResult> groups) {
        this.groups = groups;
    }

    public List<ProductProductUnitResponse> getConversionUnits() {
        return conversionUnits;
    }

    public void setConversionUnits(List<ProductProductUnitResponse> conversionUnits) {
        this.conversionUnits = conversionUnits;
    }

    public List<ToppingItem> getToppings() {
        return toppings;
    }

    public void setToppings(List<ToppingItem> toppingItems) {
        this.toppings = toppingItems;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getDiscountVatRate() {
        return discountVatRate;
    }

    public void setDiscountVatRate(Integer discountVatRate) {
        this.discountVatRate = discountVatRate;
    }

    public ProductProcessingAreaResult getProcessingArea() {
        return processingArea;
    }

    public void setProcessingArea(ProductProcessingAreaResult processingArea) {
        this.processingArea = processingArea;
    }
}
