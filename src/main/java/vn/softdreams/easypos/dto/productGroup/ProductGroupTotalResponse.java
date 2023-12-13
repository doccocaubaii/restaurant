package vn.softdreams.easypos.dto.productGroup;

import java.io.Serializable;
import java.util.List;

public class ProductGroupTotalResponse implements Serializable {

    private List<ProductGroupResponse> productGroupList;
    private long totalValue;

    public ProductGroupTotalResponse() {}

    public ProductGroupTotalResponse(List<ProductGroupResponse> productGroupList, long totalValue) {
        this.productGroupList = productGroupList;
        this.totalValue = totalValue;
    }

    public List<ProductGroupResponse> getProductGroupList() {
        return productGroupList;
    }

    public void setProductGroupList(List<ProductGroupResponse> productGroupList) {
        this.productGroupList = productGroupList;
    }

    public long getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(long totalValue) {
        this.totalValue = totalValue;
    }
}
