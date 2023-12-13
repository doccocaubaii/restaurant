package vn.softdreams.easypos.dto.product;

import vn.softdreams.easypos.domain.Product;
import vn.softdreams.easypos.domain.ProductUnit;

import java.io.Serializable;
import java.util.List;

public class ImportProductAsyncRequest implements Serializable {

    private Integer comId;
    private Integer count;
    private List<ProductUnit> productUnits;
    private List<Product> products;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public List<ProductUnit> getProductUnits() {
        return productUnits;
    }

    public void setProductUnits(List<ProductUnit> productUnits) {
        this.productUnits = productUnits;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
