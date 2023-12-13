package vn.softdreams.easypos.domain;

import org.hibernate.annotations.DynamicUpdate;
import vn.softdreams.easypos.dto.processingAreaProduct.ProcessingAreaProductItemResponse;
import vn.softdreams.easypos.dto.processingAreaProduct.ProductProcessingArea;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A ProcessingAreaProduct.
 */
@Entity
@DynamicUpdate
@Table(name = "processing_area_product")
@SuppressWarnings("common-java:DuplicatedBlocks")
@SqlResultSetMappings(
    {
        @SqlResultSetMapping(
            name = "ProcessingAreaProductItemResponseDTO",
            classes = {
                @ConstructorResult(
                    targetClass = ProcessingAreaProductItemResponse.class,
                    columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "comId", type = Integer.class),
                        @ColumnResult(name = "processingAreaId", type = Integer.class),
                        @ColumnResult(name = "productProductUnitId", type = Integer.class),
                        @ColumnResult(name = "code2", type = String.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "unit", type = String.class),
                    }
                ),
            }
        ),
        @SqlResultSetMapping(
            name = "ProductProcessingAreaItemResponseDTO",
            classes = {
                @ConstructorResult(
                    targetClass = ProductProcessingArea.class,
                    columns = {
                        @ColumnResult(name = "productProductUnitId", type = Integer.class),
                        @ColumnResult(name = "processingAreaName", type = String.class),
                    }
                ),
            }
        ),
    }
)
public class ProcessingAreaProduct extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "com_id")
    private Integer comId;

    @Column(name = "processing_area_id")
    private Integer processingAreaId;

    @Column(name = "product_product_unit_id")
    private Integer productProductUnitId;

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

    public ProcessingAreaProduct comId(Integer comId) {
        this.setComId(comId);
        return this;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public Integer getProcessingAreaId() {
        return this.processingAreaId;
    }

    public ProcessingAreaProduct processingAreaId(Integer processingAreaId) {
        this.setProcessingAreaId(processingAreaId);
        return this;
    }

    public void setProcessingAreaId(Integer processingAreaId) {
        this.processingAreaId = processingAreaId;
    }

    public Integer getProductProductUnitId() {
        return this.productProductUnitId;
    }

    public ProcessingAreaProduct productProductUnitId(Integer productProductUnitId) {
        this.setProductProductUnitId(productProductUnitId);
        return this;
    }

    public void setProductProductUnitId(Integer productProductUnitId) {
        this.productProductUnitId = productProductUnitId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProcessingAreaProduct)) {
            return false;
        }
        return id != null && id.equals(((ProcessingAreaProduct) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProcessingAreaProduct{" +
            "id=" + getId() +
            ", comId=" + getComId() +
            ", processingAreaId=" + getProcessingAreaId() +
            ", productProductUnitId=" + getProductProductUnitId() +
            "}";
    }
}
