package vn.softdreams.easypos.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A SupplierSupplierGroup.
 */
@Entity
@Table(name = "supplier_supplier_group")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SupplierSupplierGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "supplier_id")
    private Integer supplierId;

    @Column(name = "supplier_group_id")
    private Integer supplierGroupId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSupplierId() {
        return this.supplierId;
    }

    public SupplierSupplierGroup supplierId(Integer supplierId) {
        this.setSupplierId(supplierId);
        return this;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public Integer getSupplierGroupId() {
        return this.supplierGroupId;
    }

    public SupplierSupplierGroup supplierGroupId(Integer supplierGroupId) {
        this.setSupplierGroupId(supplierGroupId);
        return this;
    }

    public void setSupplierGroupId(Integer supplierGroupId) {
        this.supplierGroupId = supplierGroupId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SupplierSupplierGroup)) {
            return false;
        }
        return id != null && id.equals(((SupplierSupplierGroup) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SupplierSupplierGroup{" +
            "id=" + getId() +
            ", supplierId=" + getSupplierId() +
            ", supplierGroupId=" + getSupplierGroupId() +
            "}";
    }
}
