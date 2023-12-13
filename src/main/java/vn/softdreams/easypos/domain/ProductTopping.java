package vn.softdreams.easypos.domain;

import org.hibernate.annotations.DynamicUpdate;
import vn.softdreams.easypos.dto.toppingGroup.ToppingGroupResponse;
import vn.softdreams.easypos.dto.toppingGroup.ToppingItem;
import vn.softdreams.easypos.dto.toppingGroup.ToppingItemOffline;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A ProductTopping.
 */
@Entity
@Table(name = "product_topping")
@DynamicUpdate
@SuppressWarnings("common-java:DuplicatedBlocks")
@SqlResultSetMappings(
    {
        @SqlResultSetMapping(
            name = "ProductToppingDTO",
            classes = {
                @ConstructorResult(
                    targetClass = ToppingGroupResponse.class,
                    columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "requiredOptional", type = Boolean.class),
                    }
                ),
            }
        ),
        @SqlResultSetMapping(
            name = "ToppingItemResponse",
            classes = {
                @ConstructorResult(
                    targetClass = ToppingItem.class,
                    columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "imageUrl", type = String.class),
                        @ColumnResult(name = "salePrice", type = BigDecimal.class),
                        @ColumnResult(name = "isTopping", type = Boolean.class),
                    }
                ),
            }
        ),
        @SqlResultSetMapping(
            name = "ToppingItemOfflineResponse",
            classes = {
                @ConstructorResult(
                    targetClass = ToppingItemOffline.class,
                    columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "imageUrl", type = String.class),
                        @ColumnResult(name = "salePrice", type = BigDecimal.class),
                        @ColumnResult(name = "isTopping", type = Boolean.class),
                        @ColumnResult(name = "productId", type = Integer.class),
                    }
                ),
            }
        ),
        @SqlResultSetMapping(
            name = "ToppingGroupId",
            classes = {
                @ConstructorResult(targetClass = ToppingItem.class, columns = { @ColumnResult(name = "id", type = Integer.class) }),
            }
        ),
    }
)
public class ProductTopping extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "topping_id")
    private Integer toppingId;

    @Column(name = "topping_group_id")
    private Integer toppingGroupId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return this.productId;
    }

    public ProductTopping productId(Integer productId) {
        this.setProductId(productId);
        return this;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getToppingId() {
        return this.toppingId;
    }

    public ProductTopping toppingId(Integer toppingId) {
        this.setToppingId(toppingId);
        return this;
    }

    public void setToppingId(Integer toppingId) {
        this.toppingId = toppingId;
    }

    public Integer getToppingGroupId() {
        return this.toppingGroupId;
    }

    public ProductTopping toppingGroupId(Integer toppingGroupId) {
        this.setToppingGroupId(toppingGroupId);
        return this;
    }

    public void setToppingGroupId(Integer toppingGroupId) {
        this.toppingGroupId = toppingGroupId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductTopping)) {
            return false;
        }
        return id != null && id.equals(((ProductTopping) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductTopping{" +
            "id=" + getId() +
            ", productId=" + getProductId() +
            ", toppingId=" + getToppingId() +
            ", toppingGroupId=" + getToppingGroupId() +
            "}";
    }
}
