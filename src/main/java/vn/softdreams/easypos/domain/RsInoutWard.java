package vn.softdreams.easypos.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;
import vn.softdreams.easypos.config.Constants;
import vn.softdreams.easypos.dto.rsinoutward.GetOneByIdResponse;
import vn.softdreams.easypos.dto.rsinoutward.RsInOutWardStatusResult;
import vn.softdreams.easypos.dto.rsinoutward.RsInoutWardResponse;
import vn.softdreams.easypos.util.Common;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * A RsInoutWard.
 */

@JsonIgnoreProperties(value = { "new" })
@Entity
@DynamicUpdate
@Table(name = "rs_inoutward")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@AuditTable(value = "rs_inoutward_aud", schema = "audit")
@SqlResultSetMappings(
    {
        @SqlResultSetMapping(
            name = "RsInoutWardResponse",
            classes = {
                @ConstructorResult(
                    targetClass = RsInoutWardResponse.class,
                    columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "type", type = Integer.class),
                        @ColumnResult(name = "date", type = ZonedDateTime.class),
                        @ColumnResult(name = "no", type = String.class),
                        @ColumnResult(name = "productTypes", type = Integer.class),
                        @ColumnResult(name = "quantity", type = BigDecimal.class),
                        @ColumnResult(name = "totalAmount", type = BigDecimal.class),
                        @ColumnResult(name = "customerName", type = String.class),
                        @ColumnResult(name = "customerId", type = Integer.class),
                        @ColumnResult(name = "countAll", type = Long.class),
                    }
                ),
            }
        ),
        @SqlResultSetMapping(
            name = "RsInoutWardAmountStatusResult",
            classes = {
                @ConstructorResult(
                    targetClass = RsInOutWardStatusResult.class,
                    columns = {
                        @ColumnResult(name = "totalAmountInWard", type = BigDecimal.class),
                        @ColumnResult(name = "totalAmountOutWard", type = BigDecimal.class),
                    }
                ),
            }
        ),
        @SqlResultSetMapping(
            name = "GetOneByIdResponse",
            classes = {
                @ConstructorResult(
                    targetClass = GetOneByIdResponse.class,
                    columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "billId", type = Integer.class),
                        @ColumnResult(name = "type", type = Integer.class),
                        @ColumnResult(name = "typeDesc", type = String.class),
                        @ColumnResult(name = "date", type = String.class),
                        @ColumnResult(name = "no", type = String.class),
                        @ColumnResult(name = "supplierName", type = String.class),
                        @ColumnResult(name = "supplierId", type = Integer.class),
                        @ColumnResult(name = "customerName", type = String.class),
                        @ColumnResult(name = "customerId", type = Integer.class),
                        @ColumnResult(name = "quantity", type = BigDecimal.class),
                        @ColumnResult(name = "amount", type = BigDecimal.class),
                        @ColumnResult(name = "discountAmount", type = BigDecimal.class),
                        @ColumnResult(name = "costAmount", type = BigDecimal.class),
                        @ColumnResult(name = "totalAmount", type = BigDecimal.class),
                        @ColumnResult(name = "description", type = String.class),
                        @ColumnResult(name = "creator", type = String.class),
                    }
                ),
            }
        ),
    }
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RsInoutWard extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "com_id")
    private Integer comId;

    @NotAudited
    @Column(name = "bill_id")
    private Integer billId;

    @Column(name = "type")
    private Integer type;

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

    @Column(name = "supplier_id")
    private Integer supplierId;

    @Column(name = "supplier_name")
    private String supplierName;

    @Column(name = "quantity", precision = 21, scale = 6)
    private BigDecimal quantity;

    @Column(name = "amount", precision = 21, scale = 6)
    private BigDecimal amount;

    @Column(name = "discount_amount", precision = 21, scale = 6)
    private BigDecimal discountAmount;

    @Column(name = "cost_amount", precision = 21, scale = 6)
    private BigDecimal costAmount;

    @Column(name = "total_amount", precision = 21, scale = 6)
    private BigDecimal totalAmount;

    @NotAudited
    @Column(name = "business_type_id")
    private Integer businessTypeId;

    @NotAudited
    @Column(name = "payment_method")
    private String paymentMethod;

    @NotAudited
    @Column(name = "description")
    private String description;

    @NotAudited
    @Column(name = "eb_id")
    private Integer ebId;

    @NotAudited
    @Column(name = "customer_normalized_name")
    private String customerNormalizedName;

    @NotAudited
    @OneToMany(mappedBy = "rsInoutWard", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Column(nullable = true)
    @JsonManagedReference
    private List<RsInoutWardDetail> rsInoutWardDetails;

    public List<RsInoutWardDetail> getRsInoutWardDetails() {
        return rsInoutWardDetails;
    }

    public void setRsInoutWardDetails(List<RsInoutWardDetail> rsInoutWardDetails) {
        this.rsInoutWardDetails = rsInoutWardDetails;
    }

    public Integer getBillId() {
        return billId;
    }

    public void setBillId(Integer billId) {
        this.billId = billId;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
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

    public Integer getCustomerId() {
        return customerId;
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

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getCostAmount() {
        return costAmount;
    }

    public void setCostAmount(BigDecimal costAmount) {
        this.costAmount = costAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getBusinessTypeId() {
        return businessTypeId;
    }

    public void setBusinessTypeId(Integer businessTypeId) {
        this.businessTypeId = businessTypeId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getEbId() {
        return ebId;
    }

    public void setEbId(Integer ebId) {
        this.ebId = ebId;
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
        if (!(o instanceof RsInoutWard)) {
            return false;
        }
        return id != null && id.equals(((RsInoutWard) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RsInoutWard{" +
            "id=" + getId() +
            ", comId='" + getComId() + "'" +
            ", type=" + getType() +
            ", date='" + getDate() + "'" +
            ", no='" + getNo() + "'" +
            ", customerId='" + getCustomerId() + "'" +
            ", customerName='" + getCustomerName() + "'" +
            ", quantity=" + getQuantity() +
            ", amount=" + getAmount() +
            ", discountAmount=" + getDiscountAmount() +
            ", costAmount=" + getCostAmount() +
            ", totalAmount=" + getTotalAmount() +
            ", businessTypeId=" + getBusinessTypeId() +
            ", paymentMethod=" + getPaymentMethod() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
