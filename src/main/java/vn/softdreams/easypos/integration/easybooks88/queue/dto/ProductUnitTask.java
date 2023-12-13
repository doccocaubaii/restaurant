package vn.softdreams.easypos.integration.easybooks88.queue.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class ProductUnitTask implements Serializable {

    @JsonProperty
    private String comId;

    @JsonProperty
    private String productUnitId;

    public ProductUnitTask() {}

    public ProductUnitTask(String comId, String productUnitId) {
        this.comId = comId;
        this.productUnitId = productUnitId;
    }

    public String getComId() {
        return comId;
    }

    public void setComId(String comId) {
        this.comId = comId;
    }

    public String getProductUnitId() {
        return productUnitId;
    }

    public void setProductUnitId(String productUnitId) {
        this.productUnitId = productUnitId;
    }
}
