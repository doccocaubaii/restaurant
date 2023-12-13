package vn.softdreams.easypos.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A VoucherCompany.
 */
@Entity
@Table(name = "voucher_company")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VoucherCompany extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "com_id")
    private Integer comId;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "voucher_id")
    private Integer voucherId;

    @Column(name = "voucher_code")
    private String voucherCode;

    @Column(name = "auto_apply")
    private Boolean autoApply;

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

    public VoucherCompany comId(Integer comId) {
        this.setComId(comId);
        return this;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public VoucherCompany companyName(String companyName) {
        this.setCompanyName(companyName);
        return this;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getVoucherId() {
        return this.voucherId;
    }

    public VoucherCompany voucherId(Integer voucherId) {
        this.setVoucherId(voucherId);
        return this;
    }

    public void setVoucherId(Integer voucherId) {
        this.voucherId = voucherId;
    }

    public String getVoucherCode() {
        return this.voucherCode;
    }

    public VoucherCompany voucherCode(String voucherCode) {
        this.setVoucherCode(voucherCode);
        return this;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public Boolean getAutoApply() {
        return this.autoApply;
    }

    public VoucherCompany autoApply(Boolean autoApply) {
        this.setAutoApply(autoApply);
        return this;
    }

    public void setAutoApply(Boolean autoApply) {
        this.autoApply = autoApply;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VoucherCompany)) {
            return false;
        }
        return id != null && id.equals(((VoucherCompany) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VoucherCompany{" +
            "id=" + getId() +
            ", comId=" + getComId() +
            ", companyName='" + getCompanyName() + "'" +
            ", voucherId=" + getVoucherId() +
            ", voucherCode='" + getVoucherCode() + "'" +
            ", autoApply='" + getAutoApply() + "'" +
            "}";
    }
}
