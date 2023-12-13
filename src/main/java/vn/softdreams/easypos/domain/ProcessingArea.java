package vn.softdreams.easypos.domain;

import org.hibernate.annotations.DynamicUpdate;
import vn.softdreams.easypos.dto.bill.BillProductProcessing;
import vn.softdreams.easypos.dto.processingArea.ProcessingAreaItemResponse;
import vn.softdreams.easypos.dto.processingArea.ProductProcessingAreaResponse;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * A ProcessingArea.
 */
@Entity
@DynamicUpdate
@Table(name = "processing_area")
@SuppressWarnings("common-java:DuplicatedBlocks")
@SqlResultSetMappings(
    {
        @SqlResultSetMapping(
            name = "ProcessingAreaItemResponseDTO",
            classes = {
                @ConstructorResult(
                    targetClass = ProcessingAreaItemResponse.class,
                    columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "comId", type = Integer.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "setting", type = Integer.class),
                        @ColumnResult(name = "active", type = Integer.class),
                        @ColumnResult(name = "createTime", type = String.class),
                    }
                ),
            }
        ),
        @SqlResultSetMapping(
            name = "ProductProcessingAreaResponseDTO",
            classes = {
                @ConstructorResult(
                    targetClass = ProductProcessingAreaResponse.class,
                    columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "comId", type = Integer.class),
                        @ColumnResult(name = "productProductUnitId", type = Integer.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "code2", type = String.class),
                        @ColumnResult(name = "unit", type = String.class),
                    }
                ),
            }
        ),
        @SqlResultSetMapping(
            name = "BillProcessingType",
            classes = {
                @ConstructorResult(
                    targetClass = BillProductProcessing.class,
                    columns = {
                        @ColumnResult(name = "productProductUnitId", type = Integer.class),
                        @ColumnResult(name = "productName", type = String.class),
                        @ColumnResult(name = "unitName", type = String.class),
                        @ColumnResult(name = "createTime", type = ZonedDateTime.class),
                        @ColumnResult(name = "updateTime", type = ZonedDateTime.class),
                        @ColumnResult(name = "creatorName", type = String.class),
                        @ColumnResult(name = "notifiedQuantity", type = BigDecimal.class),
                        @ColumnResult(name = "processingQuantity", type = BigDecimal.class),
                        @ColumnResult(name = "processedQuantity", type = BigDecimal.class),
                        @ColumnResult(name = "canceledQuantity", type = BigDecimal.class),
                        @ColumnResult(name = "billId", type = Integer.class),
                        @ColumnResult(name = "billCode", type = String.class),
                        @ColumnResult(name = "areaUnitId", type = Integer.class),
                        @ColumnResult(name = "areaUnitName", type = String.class),
                        @ColumnResult(name = "areaName", type = String.class),
                        @ColumnResult(name = "isTopping", type = Boolean.class),
                        @ColumnResult(name = "refId", type = Integer.class),
                        @ColumnResult(name = "id", type = Integer.class),
                    }
                ),
            }
        ),
        @SqlResultSetMapping(
            name = "BillProcessingDelete",
            classes = {
                @ConstructorResult(
                    targetClass = BillProductProcessing.class,
                    columns = {
                        @ColumnResult(name = "createTime", type = ZonedDateTime.class),
                        @ColumnResult(name = "creatorName", type = String.class),
                        @ColumnResult(name = "quantity", type = BigDecimal.class),
                        @ColumnResult(name = "id", type = Integer.class),
                    }
                ),
            }
        ),
    }
)
public class ProcessingArea extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "com_id")
    private Integer comId;

    @Column(name = "name")
    private String name;

    @Column(name = "normalized_name")
    private String normalizedName;

    @Column(name = "setting")
    private Integer setting;

    @Column(name = "active")
    private Integer active;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "processing_area_product",
        joinColumns = { @JoinColumn(name = "processing_area_id", referencedColumnName = "id") },
        inverseJoinColumns = { @JoinColumn(name = "product_product_unit_id", columnDefinition = "id") }
    )
    private List<ProductProductUnit> productProductUnits = new ArrayList<>();

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

    public ProcessingArea comId(Integer comId) {
        this.setComId(comId);
        return this;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public String getName() {
        return this.name;
    }

    public ProcessingArea name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNormalizedName() {
        return this.normalizedName;
    }

    public ProcessingArea normalizedName(String normalizedName) {
        this.setNormalizedName(normalizedName);
        return this;
    }

    public void setNormalizedName(String normalizedName) {
        this.normalizedName = normalizedName;
    }

    public Integer getSetting() {
        return this.setting;
    }

    public ProcessingArea setting(Integer setting) {
        this.setSetting(setting);
        return this;
    }

    public void setSetting(Integer setting) {
        this.setting = setting;
    }

    public Integer getActive() {
        return this.active;
    }

    public ProcessingArea active(Integer active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public List<ProductProductUnit> getProductProductUnits() {
        return productProductUnits;
    }

    public void setProductProductUnits(List<ProductProductUnit> productProductUnits) {
        this.productProductUnits = productProductUnits;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProcessingArea)) {
            return false;
        }
        return id != null && id.equals(((ProcessingArea) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProcessingArea{" +
            "id=" + getId() +
            ", comId=" + getComId() +
            ", name='" + getName() + "'" +
            ", normalizedName='" + getNormalizedName() + "'" +
            ", setting=" + getSetting() +
            ", active=" + getActive() +
            "}";
    }
}
