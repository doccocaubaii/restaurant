package vn.softdreams.easypos.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A Transactiondetail.
 */
@JsonIgnoreProperties(value = { "new" })
@Entity
@DynamicUpdate
@Table(name = "transaction_detail")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TransactionDetail extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "transaction_id")
    private Integer transactionId;

    @Column(name = "invoice_id")
    private Integer invoiceId;

    @Column(name = "bill_id")
    private Integer billId;

    @Column(name = "status")
    private Integer status;

    @Column(name = "type")
    private Integer type;

    @Column(name = "invoice_fkey")
    private String invoiceFkey;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Integer getBillId() {
        return billId;
    }

    public void setBillId(Integer billId) {
        this.billId = billId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getInvoiceFkey() {
        return invoiceFkey;
    }

    public void setInvoiceFkey(String invoiceFkey) {
        this.invoiceFkey = invoiceFkey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransactionDetail)) {
            return false;
        }
        return id != null && id.equals(((TransactionDetail) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Transactiondetail{" +
            "id=" + getId() +
            ", transactionId='" + getTransactionId() + "'" +
            ", invoiceId='" + getInvoiceId() + "'" +
            ", billId='" + getBillId() + "'" +
            ", status=" + getStatus() +
            ", type=" + getType() +
            ", invoiceFkey='" + getInvoiceFkey() + "'" +
            "}";
    }
}
