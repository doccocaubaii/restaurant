package vn.softdreams.easypos.integration.easybooks88.api.dto;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

public class CreateMaterialGoodsRequest {

    //TODO: Hiện tại Pattern vẫn đang check theo chuẩn tự sinh code của ứng dụng, nếu hỗ trợ code2 do KH tự tạo, phải bỏ đi hoặc có pattern khác
    @NotEmpty(message = "code must be not empty")
    @Pattern(regexp = "^EPSP(\\d){1,18}$", message = "Invalid product code format")
    private String code;

    @NotEmpty(message = "name must be not empty")
    private String name;

    private Long unitId;

    @DecimalMin(value = "0.0")
    private BigDecimal purchasePrice;

    @NotNull(message = "salePrice must be not null")
    @DecimalMin(value = "0.0")
    private BigDecimal salePrice;

    @NotNull(message = "repositoryId must be not null")
    @Min(1L)
    private Integer repositoryId;

    @DecimalMin(value = "0.0")
    private BigDecimal minimumStock;

    @DecimalMin(value = "0.0")
    private BigDecimal opnQuantity;

    @DecimalMin(value = "0.0")
    private BigDecimal opnUnitPrice;

    @DecimalMin(value = "0.0")
    private BigDecimal opnAmount;

    @Valid
    private List<ConversionUnitRequest> convertUnit;

    private final String CODE_PREFIX = "EP";

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = CODE_PREFIX + code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
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

    public Integer getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(Integer repositoryId) {
        this.repositoryId = repositoryId;
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

    public List<ConversionUnitRequest> getConvertUnit() {
        return convertUnit;
    }

    public void setConvertUnit(List<ConversionUnitRequest> convertUnit) {
        this.convertUnit = convertUnit;
    }
}
