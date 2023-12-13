package vn.softdreams.easypos.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A BillPayment.
 */
@Entity
@Table(name = "bill_payment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BillPayment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "bill_id")
    private Integer billID;

    @Column(name = "payment_method")
    private Integer paymentMethod;

    @Column(name = "amount", precision = 21, scale = 2)
    private BigDecimal amount;

    @Column(name = "refund", precision = 21, scale = 2)
    private BigDecimal refund;

    @Column(name = "debt_type")
    private Integer debtType;

    @Column(name = "debt", precision = 21, scale = 2)
    private BigDecimal debt;

    @Column(name = "creator")
    private Integer creator;

    @Column(name = "updater")
    private Integer updater;

    @Column(name = "create_time")
    private LocalDate createTime;

    @Column(name = "update_time")
    private LocalDate updateTime;

    @Column(name = "payment_time")
    private LocalDate paymentTime;

    @Column(name = "total_bill", precision = 21, scale = 2)
    private BigDecimal totalBill;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BillPayment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getBillID() {
        return this.billID;
    }

    public BillPayment billID(Integer billID) {
        this.setBillID(billID);
        return this;
    }

    public void setBillID(Integer billID) {
        this.billID = billID;
    }

    public Integer getPaymentMethod() {
        return this.paymentMethod;
    }

    public BillPayment paymentMethod(Integer paymentMethod) {
        this.setPaymentMethod(paymentMethod);
        return this;
    }

    public void setPaymentMethod(Integer paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public BillPayment amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getRefund() {
        return this.refund;
    }

    public BillPayment refund(BigDecimal refund) {
        this.setRefund(refund);
        return this;
    }

    public void setRefund(BigDecimal refund) {
        this.refund = refund;
    }

    public Integer getDebtType() {
        return this.debtType;
    }

    public BillPayment debtType(Integer debtType) {
        this.setDebtType(debtType);
        return this;
    }

    public void setDebtType(Integer debtType) {
        this.debtType = debtType;
    }

    public BigDecimal getDebt() {
        return this.debt;
    }

    public BillPayment debt(BigDecimal debt) {
        this.setDebt(debt);
        return this;
    }

    public void setDebt(BigDecimal debt) {
        this.debt = debt;
    }

    public Integer getCreator() {
        return this.creator;
    }

    public BillPayment creator(Integer creator) {
        this.setCreator(creator);
        return this;
    }

    public void setCreator(Integer creator) {
        this.creator = creator;
    }

    public Integer getUpdater() {
        return this.updater;
    }

    public BillPayment updater(Integer updater) {
        this.setUpdater(updater);
        return this;
    }

    public void setUpdater(Integer updater) {
        this.updater = updater;
    }

    public LocalDate getCreateTime() {
        return this.createTime;
    }

    public BillPayment createTime(LocalDate createTime) {
        this.setCreateTime(createTime);
        return this;
    }

    public void setCreateTime(LocalDate createTime) {
        this.createTime = createTime;
    }

    public LocalDate getUpdateTime() {
        return this.updateTime;
    }

    public BillPayment updateTime(LocalDate updateTime) {
        this.setUpdateTime(updateTime);
        return this;
    }

    public void setUpdateTime(LocalDate updateTime) {
        this.updateTime = updateTime;
    }

    public LocalDate getPaymentTime() {
        return this.paymentTime;
    }

    public BillPayment paymentTime(LocalDate paymentTime) {
        this.setPaymentTime(paymentTime);
        return this;
    }

    public void setPaymentTime(LocalDate paymentTime) {
        this.paymentTime = paymentTime;
    }

    public BigDecimal getTotalBill() {
        return this.totalBill;
    }

    public BillPayment totalBill(BigDecimal totalBill) {
        this.setTotalBill(totalBill);
        return this;
    }

    public void setTotalBill(BigDecimal totalBill) {
        this.totalBill = totalBill;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BillPayment)) {
            return false;
        }
        return id != null && id.equals(((BillPayment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BillPayment{" +
            "id=" + getId() +
            ", billID=" + getBillID() +
            ", paymentMethod=" + getPaymentMethod() +
            ", amount=" + getAmount() +
            ", refund=" + getRefund() +
            ", debtType=" + getDebtType() +
            ", debt=" + getDebt() +
            ", creator=" + getCreator() +
            ", updater=" + getUpdater() +
            ", createTime='" + getCreateTime() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            ", paymentTime='" + getPaymentTime() + "'" +
            ", totalBill=" + getTotalBill() +
            "}";
    }
}
