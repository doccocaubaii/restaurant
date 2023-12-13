package vn.softdreams.easypos.dto.productGroup;

import java.io.Serializable;

public class ProductCountResponse implements Serializable {

    private Integer productGroupId;
    private Integer productCount;

    public ProductCountResponse() {}

    public ProductCountResponse(Integer productGroupId, Integer productCount) {
        this.productGroupId = productGroupId;
        this.productCount = productCount;
    }

    public Integer getProductGroupId() {
        return productGroupId;
    }

    public void setProductGroupId(Integer productGroupId) {
        this.productGroupId = productGroupId;
    }

    public Integer getProductCount() {
        return productCount;
    }

    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }
}
