package vn.softdreams.easypos.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.NotAudited;
import vn.softdreams.easypos.config.Constants;
import vn.softdreams.easypos.util.Common;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * A Receivable.
 */
@JsonIgnoreProperties(value = { "new" })
@Entity
@DynamicUpdate
@Table(name = "receivable")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Receivable extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "com_id")
    private Integer comId;

    @Column(name = "bill_id")
    private Integer billId;

    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "type")
    private String type;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    @Column(name = "date")
    private ZonedDateTime date;

    @Column(name = "no")
    private String no;

    @Column(name = "amount", precision = 21, scale = 6)
    private BigDecimal amount;

    @Column(name = "description")
    private String description;

    @NotAudited
    @Column(name = "customer_normalized_name")
    private String customerNormalizedName;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public Integer getBillId() {
        return billId;
    }

    public void setBillId(Integer billId) {
        this.billId = billId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public void setDate(String date) {
        this.date = Common.convertStringToZoneDateTime(date, Constants.ZONED_DATE_TIME_FORMAT);
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCustomerNormalizedName() {
        return customerNormalizedName;
    }

    public void setCustomerNormalizedName(String customerNormalizedName) {
        this.customerNormalizedName = customerNormalizedName;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Receivable)) {
            return false;
        }
        return id != null && id.equals(((Receivable) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Receivable{" +
            "id=" + getId() +
            ", comId='" + getComId() + "'" +
            ", billId='" + getBillId() + "'" +
            ", customerId='" + getCustomerId() + "'" +
//            ", typeId=" + getTypeId() +
            ", date='" + getDate() + "'" +
            ", no='" + getNo() + "'" +
            ", amount=" + getAmount() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
