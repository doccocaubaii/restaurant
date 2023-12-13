package vn.softdreams.easypos.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A BillPayment.
 */
@JsonIgnoreProperties(value = { "new" })
@Entity
@DynamicUpdate
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@AuditTable(value = "bill_payment_aud", schema = "audit")
@Table(name = "bill_payment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BillPayment extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotAudited
    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "bill_id", nullable = false, referencedColumnName = "id")
    private Bill bill;

    @NotAudited
    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "amount", precision = 21, scale = 6)
    private BigDecimal amount;

    @Column(name = "refund", precision = 21, scale = 6)
    private BigDecimal refund;

    @NotAudited
    @Column(name = "debt_type")
    private Integer debtType;

    @Column(name = "debt", precision = 21, scale = 6)
    private BigDecimal debt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    //    public Integer getBillId() {
    //        return billId;
    //    }
    //
    //    public void setBillId(Integer billId) {
    //        this.billId = billId;
    //    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getRefund() {
        return refund;
    }

    public void setRefund(BigDecimal refund) {
        this.refund = refund;
    }

    public Integer getDebtType() {
        return debtType;
    }

    public void setDebtType(Integer debtType) {
        this.debtType = debtType;
    }

    public BigDecimal getDebt() {
        return debt;
    }

    public void setDebt(BigDecimal debt) {
        this.debt = debt;
    }
}
