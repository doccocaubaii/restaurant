package vn.hust.easypos.service.dto.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import vn.hust.easypos.config.Constants;

public class ProductItem {

    private Integer id;
    private Integer comId;
    private String code;
    private String name;
    private String unit;
    private BigDecimal purchasePrice;
    private BigDecimal salePrice;
    private String description;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    private LocalDateTime createTime;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    private LocalDateTime updateTime;

    private String imageUrl;

    public ProductItem(
        Integer id,
        Integer comId,
        String code,
        String name,
        String unit,
        BigDecimal purchasePrice,
        BigDecimal salePrice,
        String description,
        LocalDateTime createTime,
        LocalDateTime updateTime,
        String imageUrl
    ) {
        this.id = id;
        this.comId = comId;
        this.code = code;
        this.name = name;
        this.unit = unit;
        this.purchasePrice = purchasePrice;
        this.salePrice = salePrice;
        this.description = description;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.imageUrl = imageUrl;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
