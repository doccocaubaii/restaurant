package vn.softdreams.easypos.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A CustomerCustomerGroup.
 */
@Entity
@Table(name = "customer_customer_group")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomerCustomerGroup extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "customer_group_id")
    private Integer customerGroupId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return this.customerId;
    }

    public CustomerCustomerGroup customerId(Integer customerId) {
        this.setCustomerId(customerId);
        return this;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getCustomerGroupId() {
        return this.customerGroupId;
    }

    public CustomerCustomerGroup customerGroupId(Integer customerGroupId) {
        this.setCustomerGroupId(customerGroupId);
        return this;
    }

    public void setCustomerGroupId(Integer customerGroupId) {
        this.customerGroupId = customerGroupId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomerCustomerGroup)) {
            return false;
        }
        return id != null && id.equals(((CustomerCustomerGroup) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerCustomerGroup{" +
            "id=" + getId() +
            ", customerId=" + getCustomerId() +
            ", customerGroupId=" + getCustomerGroupId() +
            "}";
    }
}
