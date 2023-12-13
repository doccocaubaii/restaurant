package vn.softdreams.easypos.dto.backup;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class ProductProductGroupBackup implements Serializable {

    @JsonProperty("ID")
    private String item;

    @JsonProperty("product_id")
    private String productId;

    @JsonProperty("product_group_id")
    private String productGroupId;

    @JsonProperty("org_id")
    private String org_id;

    public String getItem() {
        return item;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductGroupId() {
        return productGroupId;
    }

    public void setProductGroupId(String productGroupId) {
        this.productGroupId = productGroupId;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getOrg_id() {
        return org_id;
    }

    public void setOrg_id(String org_id) {
        this.org_id = org_id;
    }
}
