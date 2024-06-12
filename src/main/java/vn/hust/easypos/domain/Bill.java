package vn.hust.easypos.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.hibernate.annotations.DynamicUpdate;
import vn.hust.easypos.config.Constants;
import vn.hust.easypos.service.dto.bill.BillItemResponse;
import vn.hust.easypos.service.dto.bill.BillStatItem;
import vn.hust.easypos.service.dto.bill.BillStatsResult;
import vn.hust.easypos.service.util.Common;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * A Bill. Hóa đơn bán hàng
 */
@JsonIgnoreProperties(value = {"new"})
@Entity
@Table(name = "bill")
@SuppressWarnings("common-java:DuplicatedBlocks")
@DynamicUpdate
@SqlResultSetMappings(
    {
        @SqlResultSetMapping(
            name = "BillResponseItemDTO",
            classes = {
                @ConstructorResult(
                    targetClass = BillItemResponse.class,
                    columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "code", type = String.class),
                        @ColumnResult(name = "totalAmount", type = BigDecimal.class),
                        @ColumnResult(name = "customerName", type = String.class),
                        @ColumnResult(name = "status", type = Integer.class),
                        @ColumnResult(name = "paymentMethod", type = String.class),
                        @ColumnResult(name = "createTime", type = LocalDateTime.class),
                        @ColumnResult(name = "billDate", type = LocalDateTime.class),
                    }
                ),
            }
        ),
        @SqlResultSetMapping(
            name = "BillMoneyResultItem",
            classes = {
                @ConstructorResult(
                    targetClass = BillStatItem.class,
                    columns = {@ColumnResult(name = "time", type = String.class), @ColumnResult(name = "money", type = BigDecimal.class)}
                ),
            }
        ),
        @SqlResultSetMapping(
            name = "BillStatsResult",
            classes = {
                @ConstructorResult(
                    targetClass = BillStatsResult.class,
                    columns = {
                        @ColumnResult(name = "processingCount", type = Integer.class),
                        @ColumnResult(name = "allCount", type = Integer.class),
                    }
                ),
            }
        ),
    }
)
public class Bill extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "code")
    private String code;

    @Column(name = "com_id")
    private Integer comId;

    @Column(name = "customer_name")
    private String customerName;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    @Column(name = "bill_date")
    private LocalDateTime billDate;

    @Column(name = "delivery_type")
    private Integer deliveryType;

    @Column(name = "amount", precision = 21, scale = 6)
    private BigDecimal amount;

    @Column(name = "total_amount", precision = 21, scale = 6)
    private BigDecimal totalAmount;

    @Column(name = "status")
    private Integer status;

    @Column(name = "quantity", precision = 21, scale = 6)
    private BigDecimal quantity;

    @JsonManagedReference
    @OneToOne(mappedBy = "bill", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private BillPayment payment;

    @JsonManagedReference
    @OneToMany(mappedBy = "bill", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BillProduct> products;

    @Column(name = "customer_normalized_name")
    private String customerNormalizedName;

    @Column(name = "description")
    private String description;

    @Column(name = "table_id")
    private Integer tableId;

    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerNormalizedName() {
        return customerNormalizedName;
    }

    public void setCustomerNormalizedName(String customerNormalizedName) {
        this.customerNormalizedName = customerNormalizedName;
    }

    public BillPayment getPayment() {
        return payment;
    }

    public void setPayment(BillPayment payment) {
        this.payment = payment;
    }

    public List<BillProduct> getProducts() {
        return products;
    }

    public void setProducts(List<BillProduct> products) {
        this.products = products;
    }

    public Integer getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(Integer deliveryType) {
        this.deliveryType = deliveryType;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = Common.convertStringToZoneDateTime(billDate, Constants.ZONED_DATE_TIME_FORMAT);
    }

    public void setBillDate(LocalDateTime billDate) {
        this.billDate = billDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Bill)) {
            return false;
        }
        return id != null && id.equals(((Bill) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }
}
