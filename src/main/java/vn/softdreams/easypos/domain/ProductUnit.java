package vn.softdreams.easypos.domain;

import org.hibernate.annotations.DynamicUpdate;
import vn.softdreams.easypos.dto.product.ProductUnitResponse;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A ProductUnit.
 */
@Entity
@DynamicUpdate
@Table(name = "product_unit")
@SqlResultSetMappings(
    {
        @SqlResultSetMapping(
            name = "ProductUnitResponse",
            classes = {
                @ConstructorResult(
                    targetClass = ProductUnitResponse.class,
                    columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "comId", type = Integer.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "description", type = String.class),
                    }
                ),
            }
        ),
        @SqlResultSetMapping(
            name = "ProductUnitItemResponse",
            classes = {
                @ConstructorResult(
                    targetClass = ProductUnitResponse.class,
                    columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "comId", type = Integer.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "description", type = String.class),
                        @ColumnResult(name = "active", type = Boolean.class),
                        @ColumnResult(name = "createTime", type = ZonedDateTime.class),
                    }
                ),
            }
        ),
    }
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductUnit extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "com_id")
    private Integer comId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "eb_id")
    private Integer ebId;

    @Column(name = "active")
    private Boolean active;

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

    public ProductUnit comId(Integer comId) {
        this.setComId(comId);
        return this;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public String getName() {
        return this.name;
    }

    public ProductUnit name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public ProductUnit description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getEbId() {
        return ebId;
    }

    public void setEbId(Integer ebId) {
        this.ebId = ebId;
    }

    public Boolean getActive() {
        return this.active;
    }

    public ProductUnit active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductUnit)) {
            return false;
        }
        return id != null && id.equals(((ProductUnit) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductUnit{" +
            "id=" + getId() +
            ", comId=" + getComId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", ebId='" + getEbId() + "'" +
            ", active='" + getActive() + "'" +
            "}";
    }
}
