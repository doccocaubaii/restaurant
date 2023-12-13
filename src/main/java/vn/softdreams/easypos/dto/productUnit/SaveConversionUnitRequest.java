package vn.softdreams.easypos.dto.productUnit;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class SaveConversionUnitRequest {

    @NotNull(message = ExceptionConstants.PRODUCT_ID_NOT_NULL)
    private Integer productId;

    @NotNull(message = ExceptionConstants.COMPANY_ID_NOT_NULL)
    private Integer comId;

    @NotNull(message = ExceptionConstants.PRODUCT_UNIT_MUST_NOT_EMPTY)
    private String unitName;

    @NotNull(message = ExceptionConstants.PRODUCT_UNIT_ID_MUST_NOT_EMPTY)
    private Integer productUnitId;

    @NotNull(message = ExceptionConstants.CONVERSION_RATE_MUST_NOT_EMPTY)
    @Digits(integer = 21, fraction = 2, message = ExceptionConstants.CONVERSION_RATE_INVALID)
    private BigDecimal convertRate;

    @NotNull(message = ExceptionConstants.FORMULA_MUST_NOT_EMPTY)
    @Min(value = 0, message = ExceptionConstants.FORMULA_INVALID)
    @Max(value = 1, message = ExceptionConstants.FORMULA_INVALID)
    private Integer formula;

    private String description;

    private BigDecimal purchasePrice;

    @NotNull(message = ExceptionConstants.PRODUCT_OUT_PRICE_MUST_NOT_EMPTY)
    private BigDecimal salePrice;

    @NotNull(message = ExceptionConstants.IS_PRIMARY_NOT_NULL)
    private Boolean directSale;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public BigDecimal getConvertRate() {
        return convertRate;
    }

    public void setConvertRate(BigDecimal convertRate) {
        this.convertRate = convertRate;
    }

    public Integer getFormula() {
        return formula;
    }

    public void setFormula(Integer formula) {
        this.formula = formula;
    }

    public Boolean getDirectSale() {
        return directSale;
    }

    public void setDirectSale(Boolean directSale) {
        this.directSale = directSale;
    }

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }
}
