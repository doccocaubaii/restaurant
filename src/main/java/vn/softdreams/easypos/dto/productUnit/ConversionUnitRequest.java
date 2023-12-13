package vn.softdreams.easypos.dto.productUnit;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ConversionUnitRequest {

    private Integer id;

    @NotNull(message = ExceptionConstants.CONVERSION_UNIT_NAME_EMPTY)
    private Integer productUnitId;

    @NotNull(message = ExceptionConstants.CONVERSION_RATE_MUST_NOT_EMPTY)
    @Min(value = 0, message = ExceptionConstants.CONVERSION_RATE_MUST_NOT_EMPTY)
    private BigDecimal convertRate;

    private Integer formula;

    private BigDecimal purchasePrice;

    @NotNull(message = ExceptionConstants.CONVERSION_OUT_PRICE_INVALID)
    @Min(value = 0, message = ExceptionConstants.PRODUCT_OUT_PRICE_INVALID)
    private BigDecimal salePrice;

    @NotNull(message = ExceptionConstants.DIRECT_SALE_NOT_NULL)
    private Boolean directSale;

    private String barcode;
    private Integer processingArea;

    public ConversionUnitRequest() {}

    public ConversionUnitRequest(
        Integer id,
        Integer productUnitId,
        BigDecimal convertRate,
        Integer formula,
        BigDecimal purchasePrice,
        BigDecimal salePrice,
        Boolean directSale,
        String unitName,
        Boolean isPrimary
    ) {
        this.id = id;
        this.productUnitId = productUnitId;
        this.convertRate = convertRate;
        this.formula = formula;
        this.purchasePrice = purchasePrice;
        this.salePrice = salePrice;
        this.directSale = directSale;
        this.unitName = unitName;
        this.isPrimary = isPrimary;
    }

    private String unitName;

    private Boolean isPrimary;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getConvertRate() {
        return convertRate;
    }

    public void setConvertRate(BigDecimal converRate) {
        this.convertRate = converRate;
    }

    public Integer getFormula() {
        return formula;
    }

    public void setFormula(Integer formula) {
        this.formula = formula;
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

    public Boolean getDirectSale() {
        return directSale;
    }

    public void setDirectSale(Boolean directSale) {
        this.directSale = directSale;
    }

    public Boolean getPrimary() {
        return isPrimary;
    }

    public void setPrimary(Boolean primary) {
        isPrimary = primary;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Integer getProductUnitId() {
        return productUnitId;
    }

    public void setProductUnitId(Integer productUnitId) {
        this.productUnitId = productUnitId;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Integer getProcessingArea() {
        return processingArea;
    }

    public void setProcessingArea(Integer processingArea) {
        this.processingArea = processingArea;
    }
}
