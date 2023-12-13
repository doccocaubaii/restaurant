package vn.softdreams.easypos.dto.processingAreaProduct;

public class ProcessingAreaProductItemResponse {

    private Integer id;
    private Integer comId;
    private Integer processingAreaId;
    private Integer productProductUnitId;
    private String code2;
    private String name;
    private String unit;

    public ProcessingAreaProductItemResponse() {}

    public ProcessingAreaProductItemResponse(
        Integer id,
        Integer comId,
        Integer processingAreaId,
        Integer productProductUnitId,
        String code2,
        String name,
        String unit
    ) {
        this.id = id;
        this.comId = comId;
        this.processingAreaId = processingAreaId;
        this.productProductUnitId = productProductUnitId;
        this.code2 = code2;
        this.name = name;
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

    public Integer getProcessingAreaId() {
        return processingAreaId;
    }

    public void setProcessingAreaId(Integer processingAreaId) {
        this.processingAreaId = processingAreaId;
    }

    public Integer getProductProductUnitId() {
        return productProductUnitId;
    }

    public void setProductProductUnitId(Integer productProductUnitId) {
        this.productProductUnitId = productProductUnitId;
    }

    public String getCode2() {
        return code2;
    }

    public void setCode2(String code2) {
        this.code2 = code2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
