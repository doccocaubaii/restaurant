package vn.softdreams.easypos.integration.easybooks88.api.dto;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

public class ConversionUnitRequest {

    @NotEmpty(message = ExceptionConstants.PRODUCT_CODE_NOT_NULL_VI)
    @Pattern(regexp = "^EPSP(\\d){1,18}$", message = ExceptionConstants.PRODUCT_CODE_INVALID_VI)
    private String code;

    @NotNull(message = ExceptionConstants.UNIT_ID_NOT_NULL_VI)
    private Integer unitId;

    private BigDecimal convertRate;
    private String formula;
    private BigDecimal salePrice;
    private String description;
    private String position;

    private final String CODE_PREFIX = "EP";

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = CODE_PREFIX + code;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public BigDecimal getConvertRate() {
        return convertRate;
    }

    public void setConvertRate(BigDecimal convertRate) {
        this.convertRate = convertRate;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
