package vn.softdreams.easypos.dto.toppingGroup;

import vn.softdreams.easypos.dto.product.ProductToppingItem;

import java.util.List;

public class ToppingGroupItemResult {

    private Integer id;
    private String name;
    private Boolean requiredOptional;
    private List<ProductToppingItem> products;
    private List<ProductToppingItem> productLinks;

    public ToppingGroupItemResult() {}

    public ToppingGroupItemResult(Integer id, String name, List<ProductToppingItem> products) {
        this.id = id;
        this.name = name;
        this.products = products;
    }

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

    public List<ProductToppingItem> getProducts() {
        return products;
    }

    public void setProducts(List<ProductToppingItem> products) {
        this.products = products;
    }

    public Boolean getRequiredOptional() {
        return requiredOptional;
    }

    public void setRequiredOptional(Boolean requiredOptional) {
        this.requiredOptional = requiredOptional;
    }

    public List<ProductToppingItem> getProductLinks() {
        return productLinks;
    }

    public void setProductLinks(List<ProductToppingItem> productLinks) {
        this.productLinks = productLinks;
    }
}
