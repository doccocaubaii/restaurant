package vn.softdreams.easypos.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A InvoiceProduct.
 */
@JsonIgnoreProperties(value = { "new" })
@Entity
@DynamicUpdate
@Table(name = "invoice_product")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InvoiceProduct extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "invoice_id", nullable = false, referencedColumnName = "id")
    private Invoice invoice;

    @Column(name = "bill_id")
    private Integer billId;

    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "code")
    private String code;

    @Column(name = "position")
    private Integer position;

    @Column(name = "feature")
    private Integer feature;

    @Column(name = "name")
    private String name;

    @Column(name = "quantity", precision = 21, scale = 6)
    private BigDecimal quantity;

    @Column(name = "unit")
    private String unit;

    @Column(name = "unit_price", precision = 21, scale = 6)
    private BigDecimal unitPrice;

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

    @NotAudited
    @Column(name = "normalized_name")
    private String normalizedName;

    @NotAudited
    @Column(name = "extra")
    private String extra;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Integer getBillId() {
        return billId;
    }

    public void setBillId(Integer billId) {
        this.billId = billId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getFeature() {
        return feature;
    }

    public void setFeature(Integer feature) {
        this.feature = feature;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getTotalPreTax() {
        return totalPreTax;
    }

    public void setTotalPreTax(BigDecimal totalPreTax) {
        this.totalPreTax = totalPreTax;
    }

    public Integer getVatRate() {
        return vatRate;
    }

    public void setVatRate(Integer vatRate) {
        this.vatRate = vatRate;
    }

    public BigDecimal getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(BigDecimal vatAmount) {
        this.vatAmount = vatAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getNormalizedName() {
        return normalizedName;
    }

    public void setNormalizedName(String normalizedName) {
        this.normalizedName = normalizedName;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvoiceProduct{" +
            "id=" + getId() +
            ", billId='" + getBillId() + "'" +
            ", productId='" + getProductId() + "'" +
//            ", productName='" + getProductName() + "'" +
            ", quantity=" + getQuantity() +
            ", unit='" + getUnit() + "'" +
            ", unitPrice=" + getUnitPrice() +
            ", discountAmount=" + getDiscountAmount() +
            ", totalPreTax=" + getTotalPreTax() +
            ", vatRate=" + getVatRate() +
            ", vatAmount=" + getVatAmount() +
            ", totalAmount=" + getTotalAmount() +
            "}";
    }
}
