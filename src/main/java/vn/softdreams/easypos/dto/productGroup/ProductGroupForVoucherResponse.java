package vn.softdreams.easypos.dto.productGroup;

import vn.softdreams.easypos.dto.product.ProductItemResponse;

import java.io.Serializable;
import java.util.Collection;

public class ProductGroupForVoucherResponse implements Serializable {

    private Integer id;
    private String name;
    private Collection<ProductItemResponse> products;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<ProductItemResponse> getProducts() {
        return products;
    }

    public void setProducts(Collection<ProductItemResponse> products) {
        this.products = products;
    }
}
