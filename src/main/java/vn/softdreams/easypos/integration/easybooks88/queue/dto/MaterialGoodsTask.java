package vn.softdreams.easypos.integration.easybooks88.queue.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class MaterialGoodsTask implements Serializable {

    @JsonProperty
    private String comId;

    @JsonProperty
    private String productId;

    public MaterialGoodsTask() {}

    public MaterialGoodsTask(String comId, String productId) {
        this.comId = comId;
        this.productId = productId;
    }

    public String getComId() {
        return comId;
    }

    public void setComId(String comId) {
        this.comId = comId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
