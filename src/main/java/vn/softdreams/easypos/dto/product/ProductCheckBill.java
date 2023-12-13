package vn.softdreams.easypos.dto.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class ProductCheckBill implements Serializable {

    private Integer id;
    private Integer productProductUnitId;
    private BigDecimal onHand;
    private Boolean inventoryTracking;
    private String code;
    private String name;
    private Integer unitId;
    private String unitName;
    private Boolean isPrimary;
    private BigDecimal convertRate;
    private Boolean formula;
    private Integer overStock;

    public ProductCheckBill(BigDecimal onHand, Boolean inventoryTracking, Integer overStock, String name, String unitName) {
        this.onHand = onHand;
        this.inventoryTracking = inventoryTracking;
        this.overStock = overStock;
        this.name = name;
        this.unitName = unitName;
    }

    public ProductCheckBill(Integer id, Integer productProductUnitId, String code, BigDecimal onHand) {
        this.id = id;
        this.code = code;
        this.productProductUnitId = productProductUnitId;
        this.onHand = onHand;
    }

    public ProductCheckBill(
        Integer id,
        Integer productProductUnitId,
        String code,
        String name,
        Integer unitId,
        String unitName,
        Boolean isPrimary,
        BigDecimal convertRate,
        Boolean formula,
        BigDecimal onHand,
        Boolean inventoryTracking,
        Integer overStock
    ) {
        this.id = id;
        this.productProductUnitId = productProductUnitId;
        this.code = code;
        this.name = name;
        this.unitId = unitId;
        this.unitName = unitName;
        this.isPrimary = isPrimary;
        this.convertRate = convertRate;
        this.formula = formula;
        this.onHand = onHand;
        this.inventoryTracking = inventoryTracking;
        this.overStock = overStock;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public Boolean getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(Boolean primary) {
        isPrimary = primary;
    }

    public BigDecimal getConvertRate() {
        return convertRate;
    }

    public void setConvertRate(BigDecimal convertRate) {
        this.convertRate = convertRate;
    }

    public Integer getProductProductUnitId() {
        return productProductUnitId;
    }

    public void setProductProductUnitId(Integer productProductUnitId) {
        this.productProductUnitId = productProductUnitId;
    }

    public Boolean getFormula() {
        return formula;
    }

    public void setFormula(Boolean formula) {
        this.formula = formula;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public BigDecimal getOnHand() {
        return onHand;
    }

    public void setOnHand(BigDecimal onHand) {
        this.onHand = onHand;
    }

    public Boolean getInventoryTracking() {
        return inventoryTracking;
    }

    public void setInventoryTracking(Boolean inventoryTracking) {
        this.inventoryTracking = inventoryTracking;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getOverStock() {
        return overStock;
    }

    public void setOverStock(Integer overStock) {
        this.overStock = overStock;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ProductCheckBill)) {
            return false;
        }
        ProductCheckBill request = (ProductCheckBill) obj;
        return Objects.equals(this.id, request.id) && Objects.equals(this.code, request.code);
    }
}
