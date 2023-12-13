package vn.softdreams.easypos.dto.toppingGroup;

import com.fasterxml.jackson.annotation.JsonFormat;
import vn.softdreams.easypos.config.Constants;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class ProductToppingItemResponse {

    private Integer toppingGroupId;
    private Integer productProductUnitId;
    private Integer productId;
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
    private Integer discountVatRate;
    private Boolean isTopping;
    private Integer parentProductId;

    public ProductToppingItemResponse(
        Integer toppingGroupId,
        Integer productProductUnitId,
        Integer productId,
        Integer comId,
        String code,
        String code2,
        String name,
        String unit,
        Integer unitId,
        BigDecimal purchasePrice,
        BigDecimal salePrice,
        Integer vatRate,
        Integer discountVatRate,
        Boolean inventoryTracking,
        BigDecimal inventoryCount,
        String description,
        ZonedDateTime createTime,
        ZonedDateTime updateTime,
        String barcode,
        String imageUrl,
        Boolean isTopping
    ) {
        this.toppingGroupId = toppingGroupId;
        this.productProductUnitId = productProductUnitId;
        this.productId = productId;
        this.comId = comId;
        this.code = code;
        this.code2 = code2;
        this.name = name;
        this.unit = unit;
        this.unitId = unitId;
        this.purchasePrice = purchasePrice;
        this.salePrice = salePrice;
        this.vatRate = vatRate;
        this.discountVatRate = discountVatRate;
        this.inventoryTracking = inventoryTracking;
        this.inventoryCount = inventoryCount;
        this.description = description;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.barcode = barcode;
        this.imageUrl = imageUrl;
        this.isTopping = isTopping;
    }

    public ProductToppingItemResponse(
        Integer toppingGroupId,
        Integer productProductUnitId,
        Integer productId,
        Integer comId,
        String code,
        String code2,
        String name,
        String unit,
        Integer unitId,
        BigDecimal purchasePrice,
        BigDecimal salePrice,
        Integer vatRate,
        Integer discountVatRate,
        Boolean inventoryTracking,
        BigDecimal inventoryCount,
        String description,
        ZonedDateTime createTime,
        ZonedDateTime updateTime,
        String barcode,
        String imageUrl,
        Boolean isTopping,
        Integer parentProductId
    ) {
        this.toppingGroupId = toppingGroupId;
        this.productProductUnitId = productProductUnitId;
        this.productId = productId;
        this.comId = comId;
        this.code = code;
        this.code2 = code2;
        this.name = name;
        this.unit = unit;
        this.unitId = unitId;
        this.purchasePrice = purchasePrice;
        this.salePrice = salePrice;
        this.vatRate = vatRate;
        this.discountVatRate = discountVatRate;
        this.inventoryTracking = inventoryTracking;
        this.inventoryCount = inventoryCount;
        this.description = description;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.barcode = barcode;
        this.imageUrl = imageUrl;
        this.isTopping = isTopping;
        this.parentProductId = parentProductId;
    }

    public Integer getToppingGroupId() {
        return toppingGroupId;
    }

    public void setToppingGroupId(Integer toppingGroupId) {
        this.toppingGroupId = toppingGroupId;
    }

    public Integer getProductProductUnitId() {
        return productProductUnitId;
    }

    public void setProductProductUnitId(Integer productProductUnitId) {
        this.productProductUnitId = productProductUnitId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
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

    public Integer getParentProductId() {
        return parentProductId;
    }

    public void setParentProductId(Integer parentProductId) {
        this.parentProductId = parentProductId;
    }

    public Integer getDiscountVatRate() {
        return discountVatRate;
    }

    public void setDiscountVatRate(Integer discountVatRate) {
        this.discountVatRate = discountVatRate;
    }
}
