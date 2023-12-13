package vn.softdreams.easypos.dto.product;

import vn.softdreams.easypos.domain.Product;

import java.io.Serializable;
import java.util.List;

public class ProductTotalResponse implements Serializable {

    private List<Product> productList;
    private long totalValue;

    public ProductTotalResponse() {}

    public ProductTotalResponse(List<Product> productList, long totalValue) {
        this.productList = productList;
        this.totalValue = totalValue;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public long getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(long totalValue) {
        this.totalValue = totalValue;
    }
}
