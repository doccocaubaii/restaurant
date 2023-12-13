package vn.softdreams.easypos.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A BillProduct.
 */
@JsonIgnoreProperties(value = { "new" })
@Entity
@DynamicUpdate
@Table(name = "bill_product")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BillProduct extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "bill_id", nullable = false, referencedColumnName = "id")
    @JsonBackReference
    private Bill bill;

    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_code")
    private String productCode;

    //    @JsonSerialize(using = BigDecimalSerializer.class)
    @Column(name = "quantity", precision = 21, scale = 6)
    private BigDecimal quantity;

    @Column(name = "unit")
    private String unit;

    @Column(name = "unit_price", precision = 21, scale = 6)
    private BigDecimal unitPrice;

    @Column(name = "amount", precision = 21, scale = 6)
    private BigDecimal amount;

    @Column(name = "discount_amount", precision = 21, scale = 6)
    private BigDecimal discountAmount;

    @Column(name = "total_pre_tax", precision = 21, scale = 6)
    private BigDecimal totalPreTax;

    @Column(name = "vat_rate")
    private Integer vatRate;

    @Column(name = "vat_amount", precision = 21, scale = 6)
    private BigDecimal vatAmount;

    @Column(name = "total_amount", precision = 21, scale = 6)
    private BigDecimal totalAmount;

    @Column(name = "feature")
    private Integer feature;

    @Column(name = "unit_id")
    private Integer unitId;

    @NotAudited
    @Column(name = "product_normalized_name")
    private String productNormalizedName;

    @NotAudited
    @Column(name = "is_topping")
    private Boolean isTopping;

    @NotAudited
    @Column(name = "parent_id")
    private Integer parentId;

    @Transient
    @JsonSerialize
    @JsonDeserialize
    private String imageUrl;

    @NotAudited
    @Column(name = "product_product_unit_id")
    private Integer productProductUnitId;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here
    @Column(name = "position")
    private Integer position;

    @NotAudited
    @Column(name = "extra")
    private String extra;

    // jhipster-needle-

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    //    public Integer getBillId() {
    //        return billId;
    //    }
    //
    //    public void setBillId(Integer billId) {
    //        this.billId = billId;
    //    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return this.productName;
    }

    public BillProduct productName(String productName) {
        this.setProductName(productName);
        return this;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getQuantity() {
        return this.quantity;
    }

    public BillProduct quantity(BigDecimal quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return this.unit;
    }

    public BillProduct unit(String unit) {
        this.setUnit(unit);
        return this;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getUnitPrice() {
        return this.unitPrice;
    }

    public BillProduct unitPrice(BigDecimal unitPrice) {
        this.setUnitPrice(unitPrice);
        return this;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getDiscountAmount() {
        return this.discountAmount;
    }

    public BillProduct discountAmount(BigDecimal discountAmount) {
        this.setDiscountAmount(discountAmount);
        return this;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getTotalPreTax() {
        return this.totalPreTax;
    }

    public BillProduct totalPreTax(BigDecimal totalPreTax) {
        this.setTotalPreTax(totalPreTax);
        return this;
    }

    public void setTotalPreTax(BigDecimal totalPreTax) {
        this.totalPreTax = totalPreTax;
    }

    public Integer getVatRate() {
        return this.vatRate;
    }

    public BillProduct vatRate(Integer vatRate) {
        this.setVatRate(vatRate);
        return this;
    }

    public void setVatRate(Integer vatRate) {
        this.vatRate = vatRate;
    }

    public BigDecimal getVatAmount() {
        return this.vatAmount;
    }

    public BillProduct vatAmount(BigDecimal vatAmount) {
        this.setVatAmount(vatAmount);
        return this;
    }

    public void setVatAmount(BigDecimal vatAmount) {
        this.vatAmount = vatAmount;
    }

    public BigDecimal getTotalAmount() {
        return this.totalAmount;
    }

    public BillProduct totalAmount(BigDecimal totalAmount) {
        this.setTotalAmount(totalAmount);
        return this;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getFeature() {
        return this.feature;
    }

    public BillProduct feature(Integer feature) {
        this.setFeature(feature);
        return this;
    }

    public void setFeature(Integer feature) {
        this.feature = feature;
    }

    public String getProductNormalizedName() {
        return productNormalizedName;
    }

    public void setProductNormalizedName(String productNormalizedName) {
        this.productNormalizedName = productNormalizedName;
    }

    public Boolean getIsTopping() {
        return isTopping;
    }

    public void setIsTopping(Boolean topping) {
        isTopping = topping;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getProductProductUnitId() {
        return productProductUnitId;
    }

    public void setProductProductUnitId(Integer productProductUnitId) {
        this.productProductUnitId = productProductUnitId;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BillProduct)) {
            return false;
        }
        return id != null && id.equals(((BillProduct) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BillProduct{" +
            "id=" + getId() +
//            ", billId='" + getBillId() + "'" +
            ", productId='" + getProductId() + "'" +
            ", productName='" + getProductName() + "'" +
            ", quantity=" + getQuantity() +
            ", unit='" + getUnit() + "'" +
            ", unitPrice=" + getUnitPrice() +
            ", discountAmount=" + getDiscountAmount() +
            ", totalPreTax=" + getTotalPreTax() +
            ", vatRate=" + getVatRate() +
            ", vatAmount=" + getVatAmount() +
            ", totalAmount=" + getTotalAmount() +
            ", feature=" + getFeature() +
            "}";
    }
}
