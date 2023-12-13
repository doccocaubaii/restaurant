package vn.softdreams.easypos.dto.toppingGroup;

import java.math.BigDecimal;

public class ToppingItem {

    private Integer id;
    private String name;
    private String imageUrl;
    private BigDecimal salePrice;
    private Boolean isTopping;

    public ToppingItem() {}

    public ToppingItem(Integer id) {
        this.id = id;
    }

    public ToppingItem(Integer id, String name, Boolean isTopping) {
        this.id = id;
        this.name = name;
        this.isTopping = isTopping;
    }

    public ToppingItem(Integer id, String name, String imageUrl, BigDecimal salePrice, Boolean isTopping) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.salePrice = salePrice;
        this.isTopping = isTopping;
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
}
