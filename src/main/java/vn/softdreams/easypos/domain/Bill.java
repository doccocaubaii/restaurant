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
import vn.softdreams.easypos.dto.audit.ActivityHistoryResponse;
import vn.softdreams.easypos.dto.audit.ActivityHistoryResult;
import vn.softdreams.easypos.dto.bill.*;
import vn.softdreams.easypos.util.Common;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * A Bill.
 */
@JsonIgnoreProperties(value = { "new" })
@Entity
@Table(name = "bill")
@SuppressWarnings("common-java:DuplicatedBlocks")
@DynamicUpdate
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@AuditTable(value = "bill_aud", schema = "audit")
@SqlResultSetMappings(
    {
        @SqlResultSetMapping(
            name = "BillUnitResponse",
            classes = {
                @ConstructorResult(
                    targetClass = BillUnitResponse.class,
                    columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "comId", type = Integer.class),
                        @ColumnResult(name = "areaUnitId", type = Integer.class),
                        @ColumnResult(name = "status", type = Integer.class),
                        @ColumnResult(name = "totalAmount", type = BigDecimal.class),
                        @ColumnResult(name = "createTime", type = ZonedDateTime.class),
                        @ColumnResult(name = "updateTime", type = ZonedDateTime.class),
                    }
                ),
            }
        ),
        @SqlResultSetMapping(
            name = "BillResponseItemDTO",
            classes = {
                @ConstructorResult(
                    targetClass = BillItemResponse.class,
                    columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "code", type = String.class),
                        @ColumnResult(name = "code2", type = String.class),
                        @ColumnResult(name = "totalAmount", type = BigDecimal.class),
                        @ColumnResult(name = "customerId", type = Integer.class),
                        @ColumnResult(name = "customerName", type = String.class),
                        @ColumnResult(name = "status", type = Integer.class),
                        @ColumnResult(name = "paymentMethod", type = String.class),
                        @ColumnResult(name = "debt", type = BigDecimal.class),
                        @ColumnResult(name = "refund", type = BigDecimal.class),
                        @ColumnResult(name = "amount", type = BigDecimal.class),
                        @ColumnResult(name = "createTime", type = ZonedDateTime.class),
                        @ColumnResult(name = "billDate", type = ZonedDateTime.class),
                        @ColumnResult(name = "billIdReturns", type = String.class),
                    }
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
        @SqlResultSetMapping(
            name = "BillRevenueResult",
            classes = {
                @ConstructorResult(
                    targetClass = BillRevenueResult.class,
                    columns = { @ColumnResult(name = "revenue", type = BigDecimal.class) }
                ),
            }
        ),
        @SqlResultSetMapping(
            name = "BillRevenueResultItem",
            classes = {
                @ConstructorResult(
                    targetClass = RevenueByMonth.class,
                    columns = {
                        @ColumnResult(name = "month", type = String.class),
                        @ColumnResult(name = "revenue", type = BigDecimal.class),
                        @ColumnResult(name = "profit", type = BigDecimal.class),
                    }
                ),
            }
        ),
        @SqlResultSetMapping(
            name = "BillMoneyResultItem",
            classes = {
                @ConstructorResult(
                    targetClass = BillStatItem.class,
                    columns = { @ColumnResult(name = "time", type = String.class), @ColumnResult(name = "money", type = BigDecimal.class) }
                ),
            }
        ),
        @SqlResultSetMapping(
            name = "BillProductChangeUnit",
            classes = {
                @ConstructorResult(
                    targetClass = BillProductChangeUnit.class,
                    columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "productId", type = Integer.class),
                        @ColumnResult(name = "productUnitId", type = Integer.class),
                        @ColumnResult(name = "unitName", type = String.class),
                        @ColumnResult(name = "productName", type = String.class),
                    }
                ),
            }
        ),
        @SqlResultSetMapping(
            name = "ActivityHistoryResult",
            classes = {
                @ConstructorResult(
                    targetClass = ActivityHistoryResult.class,
                    columns = {
                        @ColumnResult(name = "rev", type = Integer.class),
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "code", type = String.class),
                        @ColumnResult(name = "type", type = Integer.class),
                        @ColumnResult(name = "rowNumber", type = Integer.class),
                        @ColumnResult(name = "updateTime", type = ZonedDateTime.class),
                    }
                ),
            }
        ),
        @SqlResultSetMapping(
            name = "ActivityHistoryResponse",
            classes = {
                @ConstructorResult(
                    targetClass = ActivityHistoryResponse.class,
                    columns = {
                        @ColumnResult(name = "rev", type = Integer.class),
                        @ColumnResult(name = "revtype", type = Integer.class),
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "status", type = Integer.class),
                        @ColumnResult(name = "code", type = String.class),
                        @ColumnResult(name = "billCode", type = String.class),
                        @ColumnResult(name = "customerId", type = Integer.class),
                        @ColumnResult(name = "fullName", type = String.class),
                        @ColumnResult(name = "totalAmount", type = BigDecimal.class),
                        @ColumnResult(name = "vatAmount", type = BigDecimal.class),
                        @ColumnResult(name = "rowNumber", type = Integer.class),
                        @ColumnResult(name = "type", type = Integer.class),
                        @ColumnResult(name = "updateTime", type = ZonedDateTime.class),
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

    @NotAudited
    @Column(name = "code")
    private String code;

    @NotAudited
    @Column(name = "code2")
    private String code2;

    @Column(name = "com_id")
    private Integer comId;

    @Column(name = "area_unit_id")
    private Integer areaUnitId;

    @NotAudited
    @Column(name = "customer_id")
    private Integer customerId;

    @NotAudited
    @Column(name = "customer_name")
    private String customerName;

    @NotAudited
    @Column(name = "tax_authority_code")
    private String taxAuthorityCode;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    @Column(name = "bill_date")
    private ZonedDateTime billDate;

    //    @Column(name = "payment_method")
    //    private String paymentMethod;

    @Column(name = "delivery_type")
    private Integer deliveryType;

    @Column(name = "amount", precision = 21, scale = 6)
    private BigDecimal amount;

    @Column(name = "discount_amount", precision = 21, scale = 6)
    private BigDecimal discountAmount;

    @Column(name = "total_pre_tax", precision = 21, scale = 6)
    private BigDecimal totalPreTax;

    @Column(name = "vat_rate")
    private Integer vatRate;

    @Column(name = "vat_amount", precision = 21, scale = 6)
    private BigDecimal vatAmount;

    @Column(name = "total_amount", precision = 21, scale = 6)
    private BigDecimal totalAmount;

    @Column(name = "status")
    private Integer status;

    @NotAudited
    @Column(name = "status_invoice")
    private Integer statusInvoice;

    @NotAudited
    @Column(name = "invoice_error_message")
    private String invoiceErrorMessage;

    @NotAudited
    @Column(name = "reservation_id")
    private Integer reservationId;

    @NotAudited
    @Column(name = "type_inv")
    private Integer typeInv;

    @Column(name = "quantity", precision = 21, scale = 6)
    private BigDecimal quantity;

    @Column(name = "product_discount_amount", precision = 21, scale = 2)
    private BigDecimal productDiscountAmount;

    @JsonManagedReference
    @OneToOne(mappedBy = "bill", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private BillPayment payment;

    @JsonManagedReference
    @OneToOne(mappedBy = "bill", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private BillConfig config;

    @NotAudited
    @JsonManagedReference
    @OneToMany(mappedBy = "bill", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<BillProduct> products;

    @Column(name = "area_name")
    private String areaName;

    @Column(name = "area_unit_name")
    private String areaUnitName;

    @NotAudited
    @Column(name = "customer_normalized_name")
    private String customerNormalizedName;

    @NotAudited
    @Column(name = "description")
    private String description;

    @NotAudited
    @Column(name = "bill_id_returns")
    private String billIdReturns;

    @NotAudited
    @Column(name = "discount_vat_rate")
    private Integer discountVatRate;

    @NotAudited
    @Column(name = "discount_vat_amount")
    private BigDecimal discountVatAmount;

    @NotAudited
    @Column(name = "buyer_name")
    private String buyerName;

    @NotAudited
    @Column(name = "voucher_amount")
    private BigDecimal voucherAmount;

    public BillConfig getConfig() {
        return config;
    }

    public void setConfig(BillConfig config) {
        this.config = config;
    }

    public BigDecimal getProductDiscountAmount() {
        return productDiscountAmount;
    }

    public void setProductDiscountAmount(BigDecimal productDiscountAmount) {
        this.productDiscountAmount = productDiscountAmount;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Integer getReservationId() {
        return reservationId;
    }

    public void setReservationId(Integer reservationId) {
        this.reservationId = reservationId;
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

    public String getCode2() {
        return code2;
    }

    public void setCode2(String code2) {
        this.code2 = code2;
    }

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public Integer getAreaUnitId() {
        return areaUnitId;
    }

    public void setAreaUnitId(Integer areaUnitId) {
        this.areaUnitId = areaUnitId;
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

    public String getTaxAuthorityCode() {
        return taxAuthorityCode;
    }

    public void setTaxAuthorityCode(String taxAuthorityCode) {
        this.taxAuthorityCode = taxAuthorityCode;
    }

    public ZonedDateTime getBillDate() {
        return billDate;
    }

    public void setBillDate(ZonedDateTime billDate) {
        this.billDate = billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = Common.convertStringToZoneDateTime(billDate, Constants.ZONED_DATE_TIME_FORMAT);
    }

    //    public String getPaymentMethod() {
    //        return paymentMethod;
    //    }
    //
    //    public void setPaymentMethod(String paymentMethod) {
    //        this.paymentMethod = paymentMethod;
    //    }

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

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount == null ? BigDecimal.ZERO : discountAmount;
    }

    public BigDecimal getTotalPreTax() {
        return totalPreTax;
    }

    public void setTotalPreTax(BigDecimal totalPreTax) {
        this.totalPreTax = totalPreTax;
    }

    public Integer getVatRate() {
        return vatRate;
    }

    public void setVatRate(Integer vatRate) {
        this.vatRate = vatRate;
    }

    public BigDecimal getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(BigDecimal vatAmount) {
        this.vatAmount = vatAmount;
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

    public Integer getStatusInvoice() {
        return statusInvoice;
    }

    public void setStatusInvoice(Integer statusInvoice) {
        this.statusInvoice = statusInvoice;
    }

    public String getInvoiceErrorMessage() {
        return invoiceErrorMessage;
    }

    public void setInvoiceErrorMessage(String invoiceErrorMessage) {
        this.invoiceErrorMessage = invoiceErrorMessage;
    }

    public Integer getTypeInv() {
        return typeInv;
    }

    public void setTypeInv(Integer typeInv) {
        this.typeInv = typeInv;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaUnitName() {
        return areaUnitName;
    }

    public void setAreaUnitName(String areaUnitName) {
        this.areaUnitName = areaUnitName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBillIdReturns() {
        return billIdReturns;
    }

    public void setBillIdReturns(String billIdReturns) {
        this.billIdReturns = billIdReturns;
    }

    public String getCustomerNormalizedName() {
        return customerNormalizedName;
    }

    public void setCustomerNormalizedName(String customerNormalizedName) {
        this.customerNormalizedName = customerNormalizedName;
    }

    public Integer getDiscountVatRate() {
        return discountVatRate;
    }

    public void setDiscountVatRate(Integer discountVatRate) {
        this.discountVatRate = discountVatRate;
    }

    public BigDecimal getDiscountVatAmount() {
        return discountVatAmount;
    }

    public void setDiscountVatAmount(BigDecimal discountVatAmount) {
        this.discountVatAmount = discountVatAmount;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public BigDecimal getVoucherAmount() {
        return voucherAmount;
    }

    public void setVoucherAmount(BigDecimal voucherAmount) {
        this.voucherAmount = voucherAmount;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

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

    @Override
    public String toString() {
        return (
            "Bill{" +
            "id=" +
            id +
            ", billPayment=" +
            ", billProducts=" +
            ", code='" +
            code +
            '\'' +
            ", code2='" +
            code2 +
            '\'' +
            ", comId=" +
            comId +
            ", areaUnitId=" +
            areaUnitId +
            ", customerId=" +
            customerId +
            ", customerName='" +
            customerName +
            '\'' +
            ", taxAuthorityCode='" +
            taxAuthorityCode +
            '\'' +
            ", billDate=" +
            billDate +
            ", deliveryType=" +
            deliveryType +
            ", amount=" +
            amount +
            ", discountAmount=" +
            discountAmount +
            ", totalPreTax=" +
            totalPreTax +
            ", vatRate=" +
            vatRate +
            ", vatAmount=" +
            vatAmount +
            ", totalAmount=" +
            totalAmount +
            ", status=" +
            status +
            ", statusInvoice=" +
            statusInvoice +
            ", invoiceErrorMessage='" +
            invoiceErrorMessage +
            '\'' +
            ", reservationId=" +
            reservationId +
            ", typeInv=" +
            typeInv +
            ", quantity=" +
            quantity +
            '}'
        );
    }
}
