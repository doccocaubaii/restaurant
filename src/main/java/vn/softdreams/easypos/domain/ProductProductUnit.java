package vn.softdreams.easypos.domain;

import org.hibernate.envers.NotAudited;
import vn.softdreams.easypos.dto.productUnit.ProductProductUnitResponse;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * A ProductProductUnit.
 */
@Entity
@Table(name = "product_product_unit")
@SuppressWarnings("common-java:DuplicatedBlocks")
@SqlResultSetMappings(
    {
        @SqlResultSetMapping(
            name = "ProductProductUnitResponseDTO",
            classes = {
                @ConstructorResult(
                    targetClass = ProductProductUnitResponse.class,
                    columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "comId", type = Integer.class),
                        @ColumnResult(name = "productId", type = Integer.class),
                        @ColumnResult(name = "productUnitId", type = Integer.class),
                        @ColumnResult(name = "unitName", type = String.class),
                        @ColumnResult(name = "isPrimary", type = Boolean.class),
                        @ColumnResult(name = "convertRate", type = BigDecimal.class),
                        @ColumnResult(name = "formula", type = Integer.class),
                        @ColumnResult(name = "salePrice", type = BigDecimal.class),
                        @ColumnResult(name = "purchasePrice", type = BigDecimal.class),
                        @ColumnResult(name = "description", type = String.class),
                        @ColumnResult(name = "directSale", type = Boolean.class),
                        @ColumnResult(name = "onHand", type = BigDecimal.class),
                        @ColumnResult(name = "barcode", type = String.class),
                    }
                ),
            }
        ),
    }
)
public class ProductProductUnit extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "com_id")
    private Integer comId;

    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "product_unit_id")
    private Integer productUnitId;

    @Column(name = "unit_name")
    private String unitName;

    @Column(name = "is_primary")
    private Boolean isPrimary;

    @Column(name = "convert_rate", precision = 21, scale = 2)
    private BigDecimal convertRate;

    @Column(name = "formula")
    private Boolean formula;

    @Column(name = "purchase_price", precision = 21, scale = 2)
    private BigDecimal purchasePrice;

    @Column(name = "sale_price", precision = 21, scale = 2)
    private BigDecimal salePrice;

    @Column(name = "direct_sale")
    private Boolean directSale;

    @Column(name = "description")
    private String description;

    @Column(name = "on_hand")
    private BigDecimal onHand;

    @NotAudited
    @Column(name = "bar_code")
    private String barcode;

    @NotAudited
    @Column(name = "unit_normalized_name")
    private String unitNormalizedName;

    @NotAudited
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "processing_area_product",
        joinColumns = { @JoinColumn(name = "product_product_unit_id", columnDefinition = "id") },
        inverseJoinColumns = { @JoinColumn(name = "processing_area_id", referencedColumnName = "id") }
    )
    private List<ProcessingArea> processingAreas = new ArrayList<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getComId() {
        return this.comId;
    }

    public ProductProductUnit comId(Integer comId) {
        this.setComId(comId);
        return this;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public Integer getProductId() {
        return this.productId;
    }

    public ProductProductUnit productId(Integer productId) {
        this.setProductId(productId);
        return this;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getProductUnitId() {
        return this.productUnitId;
    }

    public ProductProductUnit productUnitId(Integer productUnitId) {
        this.setProductUnitId(productUnitId);
        return this;
    }

    public void setProductUnitId(Integer productUnitId) {
        this.productUnitId = productUnitId;
    }

    public String getUnitName() {
        return this.unitName;
    }

    public ProductProductUnit unitName(String unitName) {
        this.setUnitName(unitName);
        return this;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Boolean getIsPrimary() {
        return this.isPrimary;
    }

    public ProductProductUnit isPrimary(Boolean isPrimary) {
        this.setIsPrimary(isPrimary);
        return this;
    }

    public void setIsPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    public BigDecimal getConvertRate() {
        return this.convertRate;
    }

    public ProductProductUnit convertRate(BigDecimal convertRate) {
        this.setConvertRate(convertRate);
        return this;
    }

    public void setConvertRate(BigDecimal convertRate) {
        this.convertRate = convertRate;
    }

    public Boolean getFormula() {
        return this.formula;
    }

    public ProductProductUnit formula(Boolean formula) {
        this.setFormula(formula);
        return this;
    }

    public void setFormula(Boolean formula) {
        this.formula = formula;
    }

    public BigDecimal getPurchasePrice() {
        return this.purchasePrice;
    }

    public ProductProductUnit purchasePrice(BigDecimal purchasePrice) {
        this.setPurchasePrice(purchasePrice);
        return this;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public BigDecimal getSalePrice() {
        return this.salePrice;
    }

    public ProductProductUnit salePrice(BigDecimal salePrice) {
        this.setSalePrice(salePrice);
        return this;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public Boolean getDirectSale() {
        return this.directSale;
    }

    public ProductProductUnit directSale(Boolean directSale) {
        this.setDirectSale(directSale);
        return this;
    }

    public void setDirectSale(Boolean directSale) {
        this.directSale = directSale;
    }

    public String getDescription() {
        return this.description;
    }

    public ProductProductUnit description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getOnHand() {
        return onHand;
    }

    public void setOnHand(BigDecimal onHand) {
        this.onHand = onHand;
    }

    public String getUnitNormalizedName() {
        return unitNormalizedName;
    }

    public void setUnitNormalizedName(String unitNormalizedName) {
        this.unitNormalizedName = unitNormalizedName;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public List<ProcessingArea> getProcessingAreas() {
        return processingAreas;
    }

    public void setProcessingAreas(List<ProcessingArea> processingAreas) {
        this.processingAreas = processingAreas;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductProductUnit)) {
            return false;
        }
        return id != null && id.equals(((ProductProductUnit) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductProductUnit{" +
            "id=" + getId() +
            ", comId=" + getComId() +
            ", productId=" + getProductId() +
            ", productUnitId=" + getProductUnitId() +
            ", unitName='" + getUnitName() + "'" +
            ", isPrimary='" + getIsPrimary() + "'" +
            ", convertRate=" + getConvertRate() +
            ", formula='" + getFormula() + "'" +
            ", purchasePrice=" + getPurchasePrice() +
            ", salePrice=" + getSalePrice() +
            ", directSale='" + getDirectSale() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
