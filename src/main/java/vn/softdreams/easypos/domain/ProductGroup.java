package vn.softdreams.easypos.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.NotAudited;
import vn.softdreams.easypos.dto.productGroup.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * A ProductGroup.
 */
@JsonIgnoreProperties(value = { "new" })
@Entity
@DynamicUpdate
@Table(name = "product_group")
@SqlResultSetMappings(
    {
        @SqlResultSetMapping(
            name = "ProductGroupResponseDTO",
            classes = {
                @ConstructorResult(
                    targetClass = ProductGroupResponse.class,
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
            name = "ProductGroupResponse",
            classes = {
                @ConstructorResult(
                    targetClass = ProductGroupOfflineResponse.class,
                    columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "comId", type = Integer.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "description", type = String.class),
                        @ColumnResult(name = "createTime", type = ZonedDateTime.class),
                        @ColumnResult(name = "updateTime", type = ZonedDateTime.class),
                    }
                ),
            }
        ),
        @SqlResultSetMapping(
            name = "ProductGroupOnlineResponse",
            classes = {
                @ConstructorResult(
                    targetClass = ProductGroupOnlineResponse.class,
                    columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "comId", type = Integer.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "description", type = String.class),
                        @ColumnResult(name = "productCount", type = Integer.class),
                        @ColumnResult(name = "createTime", type = ZonedDateTime.class),
                        @ColumnResult(name = "updateTime", type = ZonedDateTime.class),
                    }
                ),
            }
        ),
        @SqlResultSetMapping(
            name = "ProductCountResponse",
            classes = {
                @ConstructorResult(
                    targetClass = ProductCountResponse.class,
                    columns = {
                        @ColumnResult(name = "productGroupId", type = Integer.class),
                        @ColumnResult(name = "productCount", type = Integer.class),
                    }
                ),
            }
        ),
        @SqlResultSetMapping(
            name = "ProductGroupResult",
            classes = {
                @ConstructorResult(
                    targetClass = ProductGroupResult.class,
                    columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "productId", type = Integer.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "description", type = String.class),
                    }
                ),
            }
        ),
    }
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductGroup extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "com_id")
    private Integer comId;

    @NotNull
    @Size(max = 512)
    @Column(name = "name")
    private String name;

    @Size(max = 512)
    @Column(name = "description")
    private String description;

    @NotAudited
    @Column(name = "normalized_name")
    private String normalizedName;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "ProductProductGroup",
        joinColumns = { @JoinColumn(name = "product_group_id", referencedColumnName = "id") },
        inverseJoinColumns = { @JoinColumn(name = "product_id", columnDefinition = "id") }
    )
    private List<Product> products = new ArrayList<>();

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here

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

    public String getName() {
        return this.name;
    }

    public ProductGroup name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public ProductGroup description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNormalizedName() {
        return normalizedName;
    }

    public void setNormalizedName(String normalizedName) {
        this.normalizedName = normalizedName;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductGroup)) {
            return false;
        }
        return id != null && id.equals(((ProductGroup) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductGroup{" +
            "id=" + getId() +
            ", comId='" + getComId() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
