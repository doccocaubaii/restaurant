package vn.softdreams.easypos.dto.product;

import vn.softdreams.easypos.dto.productUnit.ConversionUnitRequest;
import vn.softdreams.easypos.dto.toppingGroup.ToppingRequest;
import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class UpdateProdRequest implements Serializable {

    @NotNull(message = ExceptionConstants.PRODUCT_ID_NOT_NULL)
    private Integer id;

    @NotNull(message = ExceptionConstants.COM_ID_MUST_NOT_NULL)
    private Integer comId;

    @Size(max = 100, message = ExceptionConstants.PRODUCT_CODE_2_LENGTH_INVALID)
    private String code2;

    private String name;

    private String unit;

    private Integer unitId;

    @Size(max = 50, message = ExceptionConstants.PRODUCT_BARCODE_LENGTH_INVALID)
    private String barcode;

    private BigDecimal purchasePrice;

    @Min(value = 1, message = ExceptionConstants.PRODUCT_OUT_PRICE_INVALID)
    private BigDecimal salePrice;

    @Max(value = 100, message = ExceptionConstants.PRODUCT_VAT_RATE_INVALID)
    private Integer vatRate;

    private Boolean inventoryTracking;

    private BigDecimal inventoryCount;

    private String description;
    private Integer discountVatRate;

    private Boolean isTopping;

    List<Integer> groups;
    Integer processingArea;

    @Valid
    List<ConversionUnitRequest> conversionUnits;

    @Valid
    List<ToppingRequest> toppings;

    public UpdateProdRequest() {}

    public UpdateProdRequest(
        Integer id,
        Integer comId,
        String code2,
        String name,
        String unit,
        Integer unitId,
        String barcode,
        BigDecimal purchasePrice,
        BigDecimal salePrice,
        Integer vatRate,
        Boolean inventoryTracking,
        BigDecimal inventoryCount,
        String description,
        List<Integer> groups
    ) {
        this.id = id;
        this.comId = comId;
        this.code2 = code2;
        this.name = name;
        this.unit = unit;
        this.unitId = unitId;
        this.barcode = barcode;
        this.purchasePrice = purchasePrice;
        this.salePrice = salePrice;
        this.vatRate = vatRate;
        this.inventoryTracking = inventoryTracking;
        this.inventoryCount = inventoryCount;
        this.description = description;
        this.groups = groups;
    }

    public UpdateProdRequest(
        Integer id,
        Integer comId,
        String code2,
        String name,
        String unit,
        Integer unitId,
        String barcode,
        BigDecimal purchasePrice,
        BigDecimal salePrice,
        Integer vatRate,
        Boolean inventoryTracking,
        BigDecimal inventoryCount,
        String description,
        Integer discountVatRate,
        Boolean isTopping,
        List<Integer> groups,
        List<ConversionUnitRequest> conversionUnits,
        List<ToppingRequest> toppings,
        Integer processingArea
    ) {
        this.id = id;
        this.comId = comId;
        this.code2 = code2;
        this.name = name;
        this.unit = unit;
        this.unitId = unitId;
        this.barcode = barcode;
        this.purchasePrice = purchasePrice;
        this.salePrice = salePrice;
        this.vatRate = vatRate;
        this.inventoryTracking = inventoryTracking;
        this.inventoryCount = inventoryCount;
        this.description = description;
        this.isTopping = isTopping;
        this.groups = groups;
        this.conversionUnits = conversionUnits;
        this.discountVatRate = discountVatRate;
        this.toppings = toppings;
        this.processingArea = processingArea;
    }

    public UpdateProdRequest(
        Integer id,
        Integer comId,
        String code2,
        String name,
        String unit,
        Integer unitId,
        String barcode,
        BigDecimal purchasePrice,
        BigDecimal salePrice,
        Integer vatRate,
        Boolean inventoryTracking,
        BigDecimal inventoryCount,
        String description
    ) {
        this.id = id;
        this.comId = comId;
        this.code2 = code2;
        this.name = name;
        this.unit = unit;
        this.unitId = unitId;
        this.barcode = barcode;
        this.purchasePrice = purchasePrice;
        this.salePrice = salePrice;
        this.vatRate = vatRate;
        this.inventoryTracking = inventoryTracking;
        this.inventoryCount = inventoryCount;
        this.description = description;
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

    public String getCode2() {
        return code2 != null ? code2.trim() : null;
    }

    public void setCode2(String code2) {
        this.code2 = code2;
    }

    public String getName() {
        return name != null ? name.trim() : null;
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
        return unitId != null ? unitId : 0;
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
        return description == null ? null : description.trim();
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBarcode() {
        return barcode != null ? barcode.trim() : null;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public List<Integer> getGroups() {
        return groups;
    }

    public void setGroups(List<Integer> groups) {
        this.groups = groups;
    }

    public List<ConversionUnitRequest> getConversionUnits() {
        return conversionUnits;
    }

    public void setConversionUnits(List<ConversionUnitRequest> conversionUnits) {
        this.conversionUnits = conversionUnits;
    }

    public Integer getDiscountVatRate() {
        return discountVatRate;
    }

    public void setDiscountVatRate(Integer discountVatRate) {
        this.discountVatRate = discountVatRate;
    }

    public List<ToppingRequest> getToppings() {
        return toppings;
    }

    public void setToppings(List<ToppingRequest> toppings) {
        this.toppings = toppings;
    }

    public Boolean getIsTopping() {
        return isTopping;
    }

    public void setIsTopping(Boolean topping) {
        isTopping = topping;
    }

    public Integer getProcessingArea() {
        return processingArea;
    }

    public void setProcessingArea(Integer processingArea) {
        this.processingArea = processingArea;
    }
}
