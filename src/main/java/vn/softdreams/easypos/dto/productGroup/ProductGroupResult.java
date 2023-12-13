package vn.softdreams.easypos.dto.productGroup;

import java.io.Serializable;

public class ProductGroupResult implements Serializable {

    private Integer id;
    private Integer productId;
    private String name;
    private String description;

    public ProductGroupResult() {}

    public ProductGroupResult(Integer id, Integer productId, String name, String description) {
        this.id = id;
        this.productId = productId;
        this.name = name;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
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
