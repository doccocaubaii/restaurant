package vn.softdreams.easypos.dto.product;

import java.io.Serializable;
import java.math.BigDecimal;

public class HotSaleProductResult implements Serializable {

    private Integer id;
    private String name;
    private String unitName;
    private BigDecimal totalQuantity;
    private BigDecimal totalAmount;
    private String image;
    private String totalQuantityStr;
    private String totalAmountStr;
    private Integer stt;

    public String getTotalQuantityStr() {
        return totalQuantityStr;
    }

    public void setTotalQuantityStr(String totalQuantityStr) {
        this.totalQuantityStr = totalQuantityStr;
    }

    public String getTotalAmountStr() {
        return totalAmountStr;
    }

    public void setTotalAmountStr(String totalAmountStr) {
        this.totalAmountStr = totalAmountStr;
    }

    public HotSaleProductResult(Integer id, String name, String unitName, BigDecimal totalQuantity, BigDecimal totalAmount, String image) {
        this.id = id;
        this.name = name;
        this.unitName = unitName;
        this.totalQuantity = totalQuantity;
        this.totalAmount = totalAmount;
        this.image = image;
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

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public BigDecimal getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(BigDecimal totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getStt() {
        return stt;
    }

    public void setStt(Integer stt) {
        this.stt = stt;
    }
}
