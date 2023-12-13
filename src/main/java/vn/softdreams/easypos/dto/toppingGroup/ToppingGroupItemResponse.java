package vn.softdreams.easypos.dto.toppingGroup;

import vn.softdreams.easypos.dto.product.ProductItemResponse;

import java.util.List;

public class ToppingGroupItemResponse {

    private Integer id;
    private String name;
    private Boolean isGroupTopping;
    private Boolean requiredOptional;
    private List<ProductItemResponse> products;

    public Boolean getIsGroupTopping() {
        return isGroupTopping;
    }

    public void setIsGroupTopping(Boolean groupTopping) {
        isGroupTopping = groupTopping;
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

    public Boolean getRequiredOptional() {
        return requiredOptional;
    }

    public void setRequiredOptional(Boolean requiredOptional) {
        this.requiredOptional = requiredOptional;
    }

    public List<ProductItemResponse> getProducts() {
        return products;
    }

    public void setProducts(List<ProductItemResponse> products) {
        this.products = products;
    }
}
