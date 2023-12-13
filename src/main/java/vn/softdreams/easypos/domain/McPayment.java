package vn.softdreams.easypos.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;
import vn.softdreams.easypos.config.Constants;
import vn.softdreams.easypos.dto.receiptpayment.ReceiptPayment;
import vn.softdreams.easypos.util.Common;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * A McPayment.
 */
@JsonIgnoreProperties(value = { "new" })
@Entity
@DynamicUpdate
@Table(name = "mc_payment")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@AuditTable(value = "mc_payment_aud", schema = "audit")
@SuppressWarnings("common-java:DuplicatedBlocks")
@SqlResultSetMappings(
    {
        @SqlResultSetMapping(
            name = "McPayment",
            classes = {
                @ConstructorResult(
                    targetClass = ReceiptPayment.class,
                    columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "type", type = Integer.class),
                        @ColumnResult(name = "typeDesc", type = String.class),
                        @ColumnResult(name = "date", type = ZonedDateTime.class),
                        @ColumnResult(name = "no", type = String.class),
                        @ColumnResult(name = "amount", type = BigDecimal.class),
                        @ColumnResult(name = "customerName", type = String.class),
                        @ColumnResult(name = "customerId", type = Integer.class),
                        @ColumnResult(name = "note", type = String.class),
                        @ColumnResult(name = "businessTypeId", type = Integer.class),
                        @ColumnResult(name = "businessTypeName", type = String.class),
                    }
                ),
            }
        ),
    }
)
public class McPayment extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "com_id")
    private Integer comId;

    @Column(name = "rs_inoutward_id")
    private Integer rsInoutWardId;

    @NotAudited
    @Column(name = "type_desc")
    private String typeDesc;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    @Column(name = "date")
    private ZonedDateTime date;

    @Column(name = "no")
    private String no;

    @NotAudited
    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "amount", precision = 21, scale = 6)
    private BigDecimal amount;

    @NotAudited
    @Column(name = "description")
    private String description;

    @Column(name = "business_type_id")
    private Integer businessTypeId;

    @NotAudited
    @Column(name = "funds")
    private Integer funds;

    @NotAudited
    @Column(name = "payment_method")
    private String paymentMethod;

    @NotAudited
    @Column(name = "customer_normalized_name")
    private String customerNormalizedName;

    @NotAudited
    @Column(name = "bill_id")
    private Integer billId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public ZonedDateTime getDate() {
        return this.date;
    }

    public McPayment date(ZonedDateTime date) {
        this.setDate(date);
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public void setDate(String date) {
        this.date = Common.convertStringToZoneDateTime(date, Constants.ZONED_DATE_TIME_FORMAT);
    }

    public String getNo() {
        return this.no;
    }

    public McPayment no(String no) {
        this.setNo(no);
        return this;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getCustomerName() {
        return this.customerName;
    }

    public McPayment customerName(String customerName) {
        this.setCustomerName(customerName);
        return this;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public McPayment amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return this.description;
    }

    public McPayment description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getBusinessTypeId() {
        return businessTypeId;
    }

    public void setBusinessTypeId(Integer businessTypeId) {
        this.businessTypeId = businessTypeId;
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
        if (!(o instanceof McPayment)) {
            return false;
        }
        return id != null && id.equals(((McPayment) o).id);
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

    public Integer getRsInoutWardId() {
        return rsInoutWardId;
    }

    public void setRsInoutWardId(Integer rsInoutWardId) {
        this.rsInoutWardId = rsInoutWardId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getFunds() {
        return funds;
    }

    public void setFunds(Integer funds) {
        this.funds = funds;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Integer getBillId() {
        return billId;
    }

    public void setBillId(Integer billId) {
        this.billId = billId;
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "McPayment{" +
            "id=" + getId() +
            ", comId='" + getComId() + "'" +
            ", typeDesc=" + getTypeDesc() +
            ", date='" + getDate() + "'" +
            ", no='" + getNo() + "'" +
            ", customerId='" + getCustomerId() + "'" +
            ", customerName='" + getCustomerName() + "'" +
            ", amount=" + getAmount() +
            ", description='" + getDescription() + "'" +
            ", businessTypeId='" + getBusinessTypeId() + "'" +
            ", funds='" + getFunds() + "'" +
            "}";
    }
}
