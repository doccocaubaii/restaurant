package vn.softdreams.easypos.dto.productGroup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductGroupResponse implements Serializable {

    private Integer id;
    private Integer comId;
    private String name;
    private String description;
    private List<ProductOfflineResponse> products;

    public ProductGroupResponse() {
        products = new ArrayList<>();
    }

    public ProductGroupResponse(Integer id, Integer comId, String name, String description) {
        this.id = id;
        this.comId = comId;
        this.name = name;
        this.description = description;
        products = new ArrayList<>();
    }

    public List<ProductOfflineResponse> getProducts() {
        return products;
    }

    public void setProducts(List<ProductOfflineResponse> products) {
        this.products = products;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
