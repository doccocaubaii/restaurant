package vn.softdreams.easypos.domain;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A ToppingToppingGroup.
 */
@Entity
@Table(name = "topping_topping_group")
@DynamicUpdate
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ToppingToppingGroup extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "topping_group_id")
    private Integer toppingGroupId;

    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "product_name")
    private String productName;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getToppingGroupId() {
        return this.toppingGroupId;
    }

    public ToppingToppingGroup toppingGroupId(Integer toppingGroupId) {
        this.setToppingGroupId(toppingGroupId);
        return this;
    }

    public void setToppingGroupId(Integer toppingGroupId) {
        this.toppingGroupId = toppingGroupId;
    }

    public Integer getProductId() {
        return this.productId;
    }

    public ToppingToppingGroup productId(Integer productId) {
        this.setProductId(productId);
        return this;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ToppingToppingGroup)) {
            return false;
        }
        return id != null && id.equals(((ToppingToppingGroup) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ToppingToppingGroup{" +
            "id=" + getId() +
            ", toppingGroupId=" + getToppingGroupId() +
            ", productId=" + getProductId() +
            "}";
    }
}
