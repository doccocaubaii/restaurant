package vn.softdreams.easypos.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A VoucherUsage.
 */
@Entity
@Table(name = "voucher_usage")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VoucherUsage extends AbstractAuditingEntity<String> implements Serializable {

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

    @Column(name = "bill_id")
    private Integer billId;

    @Column(name = "bill_code")
    private String billCode;

    @Column(name = "bill_value", precision = 21, scale = 2)
    private BigDecimal billValue;

    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "customer_name", precision = 21, scale = 2)
    private String customerName;

    @Column(name = "voucher_value", precision = 21, scale = 2)
    private BigDecimal voucherValue;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getVoucherValue() {
        return voucherValue;
    }

    public void setVoucherValue(BigDecimal voucherValue) {
        this.voucherValue = voucherValue;
    }

    public Integer getComId() {
        return this.comId;
    }

    public VoucherUsage comId(Integer comId) {
        this.setComId(comId);
        return this;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public VoucherUsage companyName(String companyName) {
        this.setCompanyName(companyName);
        return this;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getVoucherId() {
        return this.voucherId;
    }

    public VoucherUsage voucherId(Integer voucherId) {
        this.setVoucherId(voucherId);
        return this;
    }

    public void setVoucherId(Integer voucherId) {
        this.voucherId = voucherId;
    }

    public String getVoucherCode() {
        return this.voucherCode;
    }

    public VoucherUsage voucherCode(String voucherCode) {
        this.setVoucherCode(voucherCode);
        return this;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public Integer getBillId() {
        return this.billId;
    }

    public VoucherUsage billId(Integer billId) {
        this.setBillId(billId);
        return this;
    }

    public void setBillId(Integer billId) {
        this.billId = billId;
    }

    public String getBillCode() {
        return this.billCode;
    }

    public VoucherUsage billCode(String billCode) {
        this.setBillCode(billCode);
        return this;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public BigDecimal getBillValue() {
        return this.billValue;
    }

    public VoucherUsage billValue(BigDecimal billValue) {
        this.setBillValue(billValue);
        return this;
    }

    public void setBillValue(BigDecimal billValue) {
        this.billValue = billValue;
    }

    public Integer getCustomerId() {
        return this.customerId;
    }

    public VoucherUsage customerId(Integer customerId) {
        this.setCustomerId(customerId);
        return this;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VoucherUsage)) {
            return false;
        }
        return id != null && id.equals(((VoucherUsage) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VoucherUsage{" +
            "id=" + getId() +
            ", comId=" + getComId() +
            ", companyName='" + getCompanyName() + "'" +
            ", voucherId=" + getVoucherId() +
            ", voucherCode='" + getVoucherCode() + "'" +
            ", billId=" + getBillId() +
            ", billCode='" + getBillCode() + "'" +
            ", billValue=" + getBillValue() +
            ", customerId=" + getCustomerId() +
            ", customerName='" + getCustomerName() + "'" +
            ", customerName=" + getCustomerName() +
            "}";
    }
}
