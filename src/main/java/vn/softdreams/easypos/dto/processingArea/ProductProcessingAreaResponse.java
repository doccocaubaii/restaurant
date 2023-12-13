package vn.softdreams.easypos.dto.processingArea;

public class ProductProcessingAreaResponse {

    private Integer id;
    private Integer comId;
    private Integer productProductUnitId;
    private String name;
    private String code2;
    private String unit;

    public ProductProcessingAreaResponse() {}

    public ProductProcessingAreaResponse(Integer id, Integer comId, Integer productProductUnitId, String name, String code2, String unit) {
        this.id = id;
        this.comId = comId;
        this.productProductUnitId = productProductUnitId;
        this.name = name;
        this.code2 = code2;
        this.unit = unit;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public Integer getProductProductUnitId() {
        return productProductUnitId;
    }

    public void setProductProductUnitId(Integer productProductUnitId) {
        this.productProductUnitId = productProductUnitId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode2() {
        return code2;
    }

    public void setCode2(String code2) {
        this.code2 = code2;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
