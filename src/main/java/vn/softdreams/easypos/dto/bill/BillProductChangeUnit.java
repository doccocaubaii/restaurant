package vn.softdreams.easypos.dto.bill;

public class BillProductChangeUnit {

    Integer id;
    Integer productId;
    Integer productUnitId;
    String unitName;
    String productName;

    public BillProductChangeUnit() {}

    public BillProductChangeUnit(Integer id, Integer productId, Integer productUnitId, String unitName, String productName) {
        this.id = id;
        this.productId = productId;
        this.productUnitId = productUnitId;
        this.unitName = unitName;
        this.productName = productName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getProductUnitId() {
        return productUnitId;
    }

    public void setProductUnitId(Integer productUnitId) {
        this.productUnitId = productUnitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
