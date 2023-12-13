package vn.softdreams.easypos.dto.toppingGroup;

import java.math.BigDecimal;

public class ToppingItemOffline {

    private Integer id;
    private String name;
    private String imageUrl;
    private BigDecimal salePrice;
    private Boolean isTopping;
    private Integer productId;

    public ToppingItemOffline() {}

    public ToppingItemOffline(Integer id) {
        this.id = id;
    }

    public ToppingItemOffline(Integer id, String name, String imageUrl, BigDecimal salePrice, Boolean isTopping, Integer productId) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.salePrice = salePrice;
        this.isTopping = isTopping;
        this.productId = productId;
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

    public Boolean getIsTopping() {
        return isTopping;
    }

    public void setIsTopping(Boolean topping) {
        isTopping = topping;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }
}
