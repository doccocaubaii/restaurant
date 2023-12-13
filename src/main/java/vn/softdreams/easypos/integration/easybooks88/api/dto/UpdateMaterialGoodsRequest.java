package vn.softdreams.easypos.integration.easybooks88.api.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class UpdateMaterialGoodsRequest implements Serializable {

    //TODO: Hiện tại Pattern vẫn đang check theo chuẩn tự sinh code của ứng dụng, nếu hỗ trợ code2 do KH tự tạo, phải bỏ đi hoặc có pattern khác
    @NotEmpty(message = "code must be not empty")
    @Pattern(regexp = "^EPSP(\\d){1,18}$", message = "Invalid product code format")
    private String code;

    private Long unitId;

    @NotBlank(message = "Name must be not blank")
    private String name;

    @DecimalMin(value = "0.0")
    private BigDecimal purchasePrice;

    @DecimalMin(value = "0.0")
    private BigDecimal salePrice;

    @DecimalMin(value = "0.0")
    private BigDecimal minimumStock;

    private BigDecimal opnQuantity;

    @DecimalMin(value = "0.0")
    private BigDecimal opnUnitPrice;

    private BigDecimal opnAmount;
    private boolean isActive;
    private List<ConversionUnitRequest> convertUnit;

    private final String CODE_PREFIX = "EP";

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = CODE_PREFIX + code;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public BigDecimal getMinimumStock() {
        return minimumStock;
    }

    public void setMinimumStock(BigDecimal minimumStock) {
        this.minimumStock = minimumStock;
    }

    public BigDecimal getOpnQuantity() {
        return opnQuantity;
    }

    public void setOpnQuantity(BigDecimal opnQuantity) {
        this.opnQuantity = opnQuantity;
    }

    public BigDecimal getOpnUnitPrice() {
        return opnUnitPrice;
    }

    public void setOpnUnitPrice(BigDecimal opnUnitPrice) {
        this.opnUnitPrice = opnUnitPrice;
    }

    public BigDecimal getOpnAmount() {
        return opnAmount;
    }

    public void setOpnAmount(BigDecimal opnAmount) {
        this.opnAmount = opnAmount;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<ConversionUnitRequest> getConvertUnit() {
        return convertUnit;
    }

    public void setConvertUnit(List<ConversionUnitRequest> convertUnit) {
        this.convertUnit = convertUnit;
    }
}
