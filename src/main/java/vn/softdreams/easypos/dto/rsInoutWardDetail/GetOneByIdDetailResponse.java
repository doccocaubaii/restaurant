package vn.softdreams.easypos.dto.rsInoutWardDetail;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class GetOneByIdDetailResponse implements Serializable {

    private Integer id;
    private Integer position;
    private String productName;
    private String productCode;
    private String unitName;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal amount;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
    private String lotNo;

    public GetOneByIdDetailResponse(
        Integer id,
        Integer position,
        String productName,
        String productCode,
        String unitName,
        BigDecimal quantity,
        BigDecimal unitPrice,
        BigDecimal amount,
        BigDecimal discountAmount,
        BigDecimal totalAmount,
        String lotNo
    ) {
        this.id = id;
        this.position = position;
        this.productName = productName;
        this.productCode = productCode;
        this.unitName = unitName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.amount = amount;
        this.discountAmount = discountAmount;
        this.totalAmount = totalAmount;
        this.lotNo = lotNo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public BigDecimal getQuantity() {
        if (Objects.equals(quantity, BigDecimal.ZERO) || quantity == null) {
            return BigDecimal.valueOf(0.0);
        }
        return quantity.setScale(6);
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        if (Objects.equals(unitPrice, BigDecimal.ZERO) || unitPrice == null) {
            return BigDecimal.valueOf(0.0);
        }
        return unitPrice.setScale(6);
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getAmount() {
        if (Objects.equals(amount, BigDecimal.ZERO) || amount == null) {
            return BigDecimal.valueOf(0.0);
        }
        return amount.setScale(6);
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getDiscountAmount() {
        if (Objects.equals(discountAmount, BigDecimal.ZERO) || discountAmount == null) {
            return BigDecimal.valueOf(0.0);
        }
        return discountAmount.setScale(6);
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getTotalAmount() {
        if (Objects.equals(totalAmount, BigDecimal.ZERO) || totalAmount == null) {
            return BigDecimal.valueOf(0.0);
        }
        return totalAmount.setScale(6);
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }
}
