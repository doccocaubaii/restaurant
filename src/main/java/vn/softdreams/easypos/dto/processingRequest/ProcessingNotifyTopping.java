package vn.softdreams.easypos.dto.processingRequest;

import java.math.BigDecimal;

public class ProcessingNotifyTopping {

    private Integer productProductUnitId;
    private String productName;
    private String unit;
    private BigDecimal quantity;

    public ProcessingNotifyTopping() {}

    public ProcessingNotifyTopping(Integer productProductUnitId, String productName, String unit, BigDecimal quantity) {
        this.productProductUnitId = productProductUnitId;
        this.productName = productName;
        this.unit = unit;
        this.quantity = quantity;
    }

    public Integer getProductProductUnitId() {
        return productProductUnitId;
    }

    public void setProductProductUnitId(Integer productProductUnitId) {
        this.productProductUnitId = productProductUnitId;
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

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }
}
