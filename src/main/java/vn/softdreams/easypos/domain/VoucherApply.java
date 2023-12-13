package vn.softdreams.easypos.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A VoucherApply.
 */
@Entity
@Table(name = "voucher_apply")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VoucherApply extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "com_id")
    private Integer comId;

    @Column(name = "voucher_id")
    private Integer voucherId;

    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "product_product_id")
    private Integer productProductId;

    @Column(name = "apply_id")
    private Integer applyId;

    @Column(name = "apply_type")
    private Integer applyType;

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

    public VoucherApply comId(Integer comId) {
        this.setComId(comId);
        return this;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public Integer getVoucherId() {
        return this.voucherId;
    }

    public VoucherApply voucherId(Integer voucherId) {
        this.setVoucherId(voucherId);
        return this;
    }

    public void setVoucherId(Integer voucherId) {
        this.voucherId = voucherId;
    }

    public Integer getCustomerId() {
        return this.customerId;
    }

    public VoucherApply customerId(Integer customerId) {
        this.setCustomerId(customerId);
        return this;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getProductProductId() {
        return this.productProductId;
    }

    public VoucherApply productProductId(Integer productProductId) {
        this.setProductProductId(productProductId);
        return this;
    }

    public void setProductProductId(Integer productProductId) {
        this.productProductId = productProductId;
    }

    public Integer getApplyId() {
        return this.applyId;
    }

    public VoucherApply applyId(Integer applyId) {
        this.setApplyId(applyId);
        return this;
    }

    public void setApplyId(Integer applyId) {
        this.applyId = applyId;
    }

    public Integer getApplyType() {
        return this.applyType;
    }

    public VoucherApply applyType(Integer applyType) {
        this.setApplyType(applyType);
        return this;
    }

    public void setApplyType(Integer applyType) {
        this.applyType = applyType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VoucherApply)) {
            return false;
        }
        return id != null && id.equals(((VoucherApply) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VoucherApply{" +
            "id=" + getId() +
            ", comId=" + getComId() +
            ", voucherId=" + getVoucherId() +
            ", customerId=" + getCustomerId() +
            ", productProductId=" + getProductProductId() +
            ", applyId=" + getApplyId() +
            ", applyType=" + getApplyType() +
            "}";
    }
}
