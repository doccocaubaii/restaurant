package vn.softdreams.easypos.dto.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import vn.softdreams.easypos.config.Constants;
import vn.softdreams.easypos.dto.productGroup.ProductGroupItemResult;
import vn.softdreams.easypos.dto.productGroup.ProductProcessingAreaResult;
import vn.softdreams.easypos.dto.productUnit.ProductProductUnitResponse;
import vn.softdreams.easypos.dto.toppingGroup.ToppingGroupItemResponse;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

public class ProductItemResponse {

    private Integer productProductUnitId;
    private Integer productId;
    private Integer comId;
    private String productCode;
    private String code2;
    private String productName;
    private String unit;
    private Integer unitId;
    private BigDecimal purchasePrice;
    private BigDecimal salePrice;
    private Integer vatRate;
    private Integer discountVatRate;
    private Boolean inventoryTracking;
    private BigDecimal inventoryCount;
    private String description;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    private ZonedDateTime createTime;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    private ZonedDateTime updateTime;

    private String barcode;

    private String imageUrl;
    private Boolean isPrimary;
    private Boolean haveOtherPrice;

    private Boolean isTopping;
    private Boolean isHaveTopping;

    private List<ProductGroupItemResult> groups;

    private List<ProductProductUnitResponse> conversionUnits;
    private List<ToppingGroupItemResponse> groupToppings;
    private ProductProcessingAreaResult processingArea;

    private Integer groupId;

    public ProductItemResponse() {}

    public ProductItemResponse(
        Integer productProductUnitId,
        Integer productId,
        Integer comId,
        String productCode,
        String code2,
        String productName,
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
        Boolean isTopping,
        Boolean isPrimary,
        Boolean haveOtherPrice,
        Integer discountVatRate
    ) {
        this.productProductUnitId = productProductUnitId;
        this.productId = productId;
        this.comId = comId;
        this.productCode = productCode;
        this.code2 = code2;
        this.productName = productName;
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
        this.isPrimary = isPrimary;
        this.haveOtherPrice = haveOtherPrice;
        this.discountVatRate = discountVatRate;
    }

    public ProductItemResponse(
        Integer productProductUnitId,
        Integer productId,
        Integer comId,
        String productCode,
        String code2,
        String productName,
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
        Boolean isTopping,
        Boolean isPrimary,
        Boolean haveOtherPrice,
        Integer discountVatRate,
        Integer groupId
    ) {
        this.productProductUnitId = productProductUnitId;
        this.productId = productId;
        this.comId = comId;
        this.productCode = productCode;
        this.code2 = code2;
        this.productName = productName;
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
        this.isPrimary = isPrimary;
        this.haveOtherPrice = haveOtherPrice;
        this.discountVatRate = discountVatRate;
        this.groupId = groupId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
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

    public String getCode2() {
        return code2;
    }

    public void setCode2(String code2) {
        this.code2 = code2;
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

    public List<ProductGroupItemResult> getGroups() {
        return groups;
    }

    public void setGroups(List<ProductGroupItemResult> groups) {
        this.groups = groups;
    }

    public List<ProductProductUnitResponse> getConversionUnits() {
        return conversionUnits;
    }

    public void setConversionUnits(List<ProductProductUnitResponse> conversionUnits) {
        this.conversionUnits = conversionUnits;
    }

    public Boolean getIsTopping() {
        return isTopping;
    }

    public void setIsTopping(Boolean topping) {
        isTopping = topping;
    }

    public Boolean getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(Boolean primary) {
        isPrimary = primary;
    }

    public Boolean getHaveTopping() {
        return isHaveTopping;
    }

    public void setIsHaveTopping(Boolean haveTopping) {
        isHaveTopping = haveTopping;
    }

    public List<ToppingGroupItemResponse> getGroupToppings() {
        return groupToppings;
    }

    public void setGroupToppings(List<ToppingGroupItemResponse> groupToppings) {
        this.groupToppings = groupToppings;
    }

    public Boolean getHaveOtherPrice() {
        return haveOtherPrice;
    }

    public void setHaveOtherPrice(Boolean haveOtherPrice) {
        this.haveOtherPrice = haveOtherPrice;
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
