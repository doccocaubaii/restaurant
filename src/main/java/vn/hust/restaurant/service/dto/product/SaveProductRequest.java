package vn.hust.restaurant.service.dto.product;

import java.io.Serializable;
import java.math.BigDecimal;


import jakarta.validation.constraints.*;
import vn.hust.restaurant.web.rest.errors.ExceptionConstants;

public class SaveProductRequest implements Serializable {

    private Integer id;

    @NotNull(message = ExceptionConstants.COM_ID_MUST_NOT_NULL)
    private Integer comId;

    @NotBlank(message = ExceptionConstants.PRODUCT_NAME_MUST_NOT_EMPTY)
    private String name;

    private String unit;

    private BigDecimal purchasePrice;

    @NotNull(message = ExceptionConstants.PRODUCT_OUT_PRICE_MUST_NOT_EMPTY)
    @Min(value = 1, message = ExceptionConstants.PRODUCT_OUT_PRICE_INVALID)
    private BigDecimal salePrice;

    private String description;

    public SaveProductRequest() {}

    public SaveProductRequest(Integer comId, String name, String unit, BigDecimal purchasePrice, BigDecimal salePrice, String description) {
        this.comId = comId;
        this.name = name;
        this.unit = unit;
        this.purchasePrice = purchasePrice;
        this.salePrice = salePrice;
        this.description = description;
    }

    public SaveProductRequest(
        Integer id,
        Integer comId,
        String name,
        String unit,
        BigDecimal purchasePrice,
        BigDecimal salePrice,
        String description
    ) {
        this(comId, name, unit, purchasePrice, salePrice, description);
        this.id = id;
    }

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
