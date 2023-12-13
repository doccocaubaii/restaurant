package vn.softdreams.easypos.dto.product;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

public class ProductStockUpdateRequest implements Serializable {

    @NotNull(message = ExceptionConstants.PRODUCT_ID_NOT_NULL)
    private Integer productProductUnitId;

    @NotNull(message = ExceptionConstants.COM_ID_MUST_NOT_NULL)
    private Integer comId;

    @NotNull(message = ExceptionConstants.PRODUCT_IS_INVENTORY_MUST_NOT_EMPTY)
    private Boolean inventoryTracking;

    @Min(value = 0, message = ExceptionConstants.QUANTITY_INVALID)
    private BigDecimal inventoryCount;

    private BigDecimal purchasePrice;

    public Integer getProductProductUnitId() {
        return productProductUnitId;
    }

    public void setProductProductUnitId(Integer productProductUnitId) {
        this.productProductUnitId = productProductUnitId;
    }

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
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

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }
}
