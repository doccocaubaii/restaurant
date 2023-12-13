package vn.softdreams.easypos.dto.processingAreaProduct;

public class ProductProcessingArea {

    private Integer productProductUnitId;
    private String processingAreaName;

    public ProductProcessingArea(Integer productProductUnitId, String processingAreaName) {
        this.productProductUnitId = productProductUnitId;
        this.processingAreaName = processingAreaName;
    }

    public Integer getProductProductUnitId() {
        return productProductUnitId;
    }

    public void setProductProductUnitId(Integer productProductUnitId) {
        this.productProductUnitId = productProductUnitId;
    }

    public String getProcessingAreaName() {
        return processingAreaName;
    }

    public void setProcessingAreaName(String processingAreaName) {
        this.processingAreaName = processingAreaName;
    }
}
