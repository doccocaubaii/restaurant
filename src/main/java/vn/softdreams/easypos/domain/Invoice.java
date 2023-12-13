package vn.softdreams.easypos.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;
import vn.softdreams.easypos.config.Constants;
import vn.softdreams.easypos.dto.invoice.InvoiceListResponse;
import vn.softdreams.easypos.dto.invoice.InvoiceSearchResponse;
import vn.softdreams.easypos.dto.invoice.InvoiceStatsResult;
import vn.softdreams.easypos.dto.product.InvoiceResponse;
import vn.softdreams.easypos.dto.product.SaleProductStatsResult;
import vn.softdreams.easypos.util.Common;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * A Invoice.
 */
@JsonIgnoreProperties(value = { "new" })
@Entity
@Table(name = "invoice")
@DynamicUpdate
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@AuditTable(value = "invoice_aud", schema = "audit")
@SuppressWarnings("common-java:DuplicatedBlocks")
@SqlResultSetMappings(
    {
        @SqlResultSetMapping(
            name = "InvoiceSearchResponse",
            classes = {
                @ConstructorResult(
                    targetClass = InvoiceSearchResponse.class,
                    columns = { @ColumnResult(name = "id", type = Integer.class), @ColumnResult(name = "ikey", type = String.class) }
                ),
            }
        ),
        @SqlResultSetMapping(
            name = "InvoiceResponseListDTO",
            classes = {
                @ConstructorResult(
                    targetClass = InvoiceListResponse.class,
                    columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "billId", type = Integer.class),
                        @ColumnResult(name = "customerId", type = Integer.class),
                        @ColumnResult(name = "customerCode", type = String.class),
                        @ColumnResult(name = "customerName", type = String.class),
                        @ColumnResult(name = "pattern", type = String.class),
                        @ColumnResult(name = "no", type = Integer.class),
                        @ColumnResult(name = "arisingDate", type = ZonedDateTime.class),
                        @ColumnResult(name = "publishDate", type = ZonedDateTime.class),
                        @ColumnResult(name = "totalAmount", type = BigDecimal.class),
                        @ColumnResult(name = "currencyUnit", type = String.class),
                        @ColumnResult(name = "type", type = Integer.class),
                        @ColumnResult(name = "status", type = Integer.class),
                        @ColumnResult(name = "taxCheckStatus", type = Integer.class),
                        @ColumnResult(name = "taxAuthorityCode", type = String.class),
                        @ColumnResult(name = "ikey", type = String.class),
                        @ColumnResult(name = "errorPublish", type = String.class),
                        @ColumnResult(name = "createTime", type = ZonedDateTime.class),
                        @ColumnResult(name = "updateTime", type = ZonedDateTime.class),
                    }
                ),
            }
        ),
        @SqlResultSetMapping(
            name = "InvoiceDTO",
            classes = {
                @ConstructorResult(
                    targetClass = InvoiceResponse.class,
                    columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "billId", type = Integer.class),
                        @ColumnResult(name = "customerId", type = Integer.class),
                        @ColumnResult(name = "customerName", type = String.class),
                        @ColumnResult(name = "idNumber", type = String.class),
                        @ColumnResult(name = "customerPhone", type = String.class),
                        @ColumnResult(name = "pattern", type = String.class),
                        @ColumnResult(name = "no", type = Integer.class),
                        @ColumnResult(name = "arisingDate", type = String.class),
                        @ColumnResult(name = "publishDate", type = String.class),
                        @ColumnResult(name = "paymentMethod", type = String.class),
                        @ColumnResult(name = "discountAmount", type = BigDecimal.class),
                        @ColumnResult(name = "totalPreTax", type = BigDecimal.class),
                        @ColumnResult(name = "vatRate", type = Integer.class),
                        @ColumnResult(name = "vatAmount", type = BigDecimal.class),
                        @ColumnResult(name = "totalAmount", type = BigDecimal.class),
                        @ColumnResult(name = "type", type = Integer.class),
                        @ColumnResult(name = "status", type = Integer.class),
                        @ColumnResult(name = "taxCheckStatus", type = Integer.class),
                        @ColumnResult(name = "taxAuthorityCode", type = String.class),
                        @ColumnResult(name = "taxErrorMessage", type = String.class),
                        @ColumnResult(name = "ikey", type = String.class),
                        @ColumnResult(name = "refIkey", type = String.class),
                        @ColumnResult(name = "extra", type = String.class),
                        @ColumnResult(name = "creator", type = Integer.class),
                        @ColumnResult(name = "createTime", type = String.class),
                        @ColumnResult(name = "companyId", type = Integer.class),
                        @ColumnResult(name = "customerCode", type = String.class),
                        @ColumnResult(name = "customerAddress", type = String.class),
                        @ColumnResult(name = "customerTaxCode", type = String.class),
                        @ColumnResult(name = "exchangeRate", type = Integer.class),
                        @ColumnResult(name = "currencyUnit", type = String.class),
                        @ColumnResult(name = "code", type = String.class),
                        @ColumnResult(name = "code2", type = String.class),
                        @ColumnResult(name = "amount", type = BigDecimal.class),
                        @ColumnResult(name = "refund", type = BigDecimal.class),
                    }
                ),
            }
        ),
        @SqlResultSetMapping(
            name = "InvoiceStatsResult",
            classes = {
                @ConstructorResult(
                    targetClass = InvoiceStatsResult.class,
                    columns = {
                        @ColumnResult(name = "newCount", type = Integer.class),
                        @ColumnResult(name = "processingCount", type = Integer.class),
                        @ColumnResult(name = "doneCount", type = Integer.class),
                        @ColumnResult(name = "allCount", type = Integer.class),
                    }
                ),
            }
        ),
        @SqlResultSetMapping(
            name = "SaleProductStatsResult",
            classes = {
                @ConstructorResult(
                    targetClass = SaleProductStatsResult.class,
                    columns = {
                        @ColumnResult(name = "rowNumber", type = Integer.class),
                        @ColumnResult(name = "pattern", type = String.class),
                        @ColumnResult(name = "no", type = String.class),
                        @ColumnResult(name = "arisingDate", type = String.class),
                        @ColumnResult(name = "customerName", type = String.class),
                        @ColumnResult(name = "customerTaxCode", type = String.class),
                        @ColumnResult(name = "totalPreTax", type = BigDecimal.class),
                        @ColumnResult(name = "status", type = Integer.class),
                        @ColumnResult(name = "vatRate", type = Integer.class),
                        @ColumnResult(name = "vatAmount", type = BigDecimal.class),
                        @ColumnResult(name = "totalVatAmount", type = BigDecimal.class),
                        @ColumnResult(name = "totalTotalPreTax", type = BigDecimal.class),
                    }
                ),
            }
        ),
    }
)
public class Invoice extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "company_id")
    private Integer companyId;

    @NotAudited
    @Column(name = "bill_id")
    private Integer billId;

    @Column(name = "customer_id")
    private Integer customerId;

    @Size(max = 400)
    @Column(name = "customer_name")
    private String customerName;

    @Size(max = 400)
    @Column(name = "customer_address")
    private String customerAddress;

    @Size(max = 14)
    @Column(name = "customer_taxcode")
    private String customerTaxCode;

    //    @Column(name = "tax_code")
    //    private String taxCode;
    @NotAudited
    @Size(max = 20)
    @Column(name = "id_number")
    private String idNumber;

    @NotAudited
    @Size(max = 20)
    @Column(name = "customer_phone")
    private String customerPhone;

    @NotAudited
    @Size(max = 50)
    @Column(name = "pattern")
    private String pattern;

    @Column(name = "no")
    private Integer no;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_INVOICE_FORMAT)
    @Column(name = "arising_date")
    private ZonedDateTime arisingDate;

    @Column(name = "publish_date")
    private ZonedDateTime publishDate;

    @Size(max = 50)
    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "exchange_rate")
    private Integer exchangeRate;

    @Column(name = "currency_unit")
    private String currencyUnit;

    @NotNull
    @Column(name = "discount_amount", precision = 21, scale = 6)
    private BigDecimal discountAmount;

    @NotNull
    @Column(name = "total_pre_tax", precision = 21, scale = 6)
    private BigDecimal totalPreTax;

    @NotNull
    @Column(name = "vat_rate")
    private Integer vatRate;

    @NotNull
    @Column(name = "vat_amount", precision = 21, scale = 6)
    private BigDecimal vatAmount;

    @NotNull
    @Column(name = "total_amount", precision = 21, scale = 6)
    private BigDecimal totalAmount;

    @NotAudited
    @Column(name = "type")
    private Integer type = 0;

    @Column(name = "status")
    private Integer status = 0;

    @Column(name = "tax_check_status")
    private Integer taxCheckStatus;

    @Size(max = 23)
    @Column(name = "tax_authority_code")
    private String taxAuthorityCode;

    @Size(max = 255)
    @Column(name = "tax_error_message")
    private String taxErrorMessage;

    @NotAudited
    @Size(max = 100)
    @Column(name = "ikey")
    private String ikey;

    @NotAudited
    @Size(max = 100)
    @Column(name = "refikey")
    private String refikey;

    @NotAudited
    @Column(name = "extra")
    private String extra;

    @NotAudited
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "update_user_id")
    private Integer updateUserId;

    @Column(name = "customer_code")
    private String customerCode;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "error_publish")
    private String errorPublish;

    @NotAudited
    @Column(name = "customer_normalized_name")
    private String customerNormalizedName;

    @NotAudited
    @Column(name = "buyer_name")
    private String buyerName;

    @NotAudited
    @JsonManagedReference
    @OneToMany(mappedBy = "invoice", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<InvoiceProduct> invoiceProducts;

    //  Thêm 2 trường billcode và typeInv lấy từ bảng bill phục vụ việc trả về dữ liệu cho Client
    @Transient
    @JsonDeserialize
    @JsonSerialize
    private String billCode;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private Integer typeInv;

    @NotAudited
    @Column(name = "discount_vat_rate")
    private Integer discountVatRate;

    @NotAudited
    @Column(name = "discount_vat_amount")
    private BigDecimal discountVatAmount;

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public Integer getTypeInv() {
        return typeInv;
    }

    public void setTypeInv(Integer typeInv) {
        this.typeInv = typeInv;
    }

    public String getErrorPublish() {
        return errorPublish;
    }

    public void setErrorPublish(String errorPublish) {
        this.errorPublish = errorPublish;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Integer updateUserId) {
        this.updateUserId = updateUserId;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getCustomerName() {
        return this.customerName;
    }

    public Invoice customerName(String customerName) {
        this.setCustomerName(customerName);
        return this;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public List<InvoiceProduct> getInvoiceProducts() {
        return invoiceProducts;
    }

    public void setInvoiceProducts(List<InvoiceProduct> invoiceProducts) {
        this.invoiceProducts = invoiceProducts;
    }

    public String getCustomerNormalizedName() {
        return customerNormalizedName;
    }

    public void setCustomerNormalizedName(String customerNormalizedName) {
        this.customerNormalizedName = customerNormalizedName;
    }

    public String getIdNumber() {
        return this.idNumber;
    }

    public Invoice idNumber(String idNumber) {
        this.setIdNumber(idNumber);
        return this;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerTaxCode() {
        return customerTaxCode;
    }

    public void setCustomerTaxCode(String customerTaxCode) {
        this.customerTaxCode = customerTaxCode;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public Integer getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(Integer exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getCurrencyUnit() {
        return currencyUnit;
    }

    public void setCurrencyUnit(String currencyUnit) {
        this.currencyUnit = currencyUnit;
    }

    public Invoice customerPhone(String customerPhone) {
        this.setCustomerPhone(customerPhone);
        return this;
    }

    public String getPattern() {
        return this.pattern;
    }

    public Invoice pattern(String pattern) {
        this.setPattern(pattern);
        return this;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public Integer getNo() {
        return this.no;
    }

    public Invoice no(Integer no) {
        this.setNo(no);
        return this;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    public ZonedDateTime getArisingDate() {
        return arisingDate;
    }

    public void setArisingDate(ZonedDateTime arisingDate) {
        this.arisingDate = arisingDate;
    }

    public void setArisingDate(String arisingDate) {
        this.arisingDate = Common.convertStringToZoneDateTime(arisingDate, Constants.ZONED_DATE_TIME_FORMAT);
    }

    public ZonedDateTime getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(ZonedDateTime publishDate) {
        this.publishDate = publishDate;
    }

    public String getPaymentMethod() {
        return this.paymentMethod;
    }

    public Invoice paymentMethod(String paymentMethod) {
        this.setPaymentMethod(paymentMethod);
        return this;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public BigDecimal getDiscountAmount() {
        return this.discountAmount;
    }

    public Invoice discountAmount(BigDecimal discountAmount) {
        this.setDiscountAmount(discountAmount);
        return this;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Integer getVatRate() {
        return this.vatRate;
    }

    public Invoice vatRate(Integer vatRate) {
        this.setVatRate(vatRate);
        return this;
    }

    public void setVatRate(Integer vatRate) {
        this.vatRate = vatRate;
    }

    public BigDecimal getVatAmount() {
        return this.vatAmount;
    }

    public Invoice vatAmount(BigDecimal vatAmount) {
        this.setVatAmount(vatAmount);
        return this;
    }

    public void setVatAmount(BigDecimal vatAmount) {
        this.vatAmount = vatAmount;
    }

    public BigDecimal getTotalAmount() {
        return this.totalAmount;
    }

    public Invoice totalAmount(BigDecimal totalAmount) {
        this.setTotalAmount(totalAmount);
        return this;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getType() {
        return this.type;
    }

    public Invoice type(Integer type) {
        this.setType(type);
        return this;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return this.status;
    }

    public Invoice status(Integer status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getTaxCheckStatus() {
        return this.taxCheckStatus;
    }

    public Invoice taxCheckStatus(Integer taxCheckStatus) {
        this.setTaxCheckStatus(taxCheckStatus);
        return this;
    }

    public void setTaxCheckStatus(Integer taxCheckStatus) {
        this.taxCheckStatus = taxCheckStatus;
    }

    public String getTaxAuthorityCode() {
        return this.taxAuthorityCode;
    }

    public Invoice taxAuthorityCode(String taxAuthorityCode) {
        this.setTaxAuthorityCode(taxAuthorityCode);
        return this;
    }

    public void setTaxAuthorityCode(String taxAuthorityCode) {
        this.taxAuthorityCode = taxAuthorityCode;
    }

    public String getTaxErrorMessage() {
        return this.taxErrorMessage;
    }

    public Invoice taxErrorMessage(String taxErrorMessage) {
        this.setTaxErrorMessage(taxErrorMessage);
        return this;
    }

    public void setTaxErrorMessage(String taxErrorMessage) {
        this.taxErrorMessage = taxErrorMessage;
    }

    public String getIkey() {
        return this.ikey;
    }

    public Invoice ikey(String ikey) {
        this.setIkey(ikey);
        return this;
    }

    public void setIkey(String ikey) {
        this.ikey = ikey;
    }

    public String getRefikey() {
        return this.refikey;
    }

    public Invoice refikey(String refikey) {
        this.setRefikey(refikey);
        return this;
    }

    public void setRefikey(String refikey) {
        this.refikey = refikey;
    }

    public BigDecimal getTotalPreTax() {
        return totalPreTax;
    }

    public void setTotalPreTax(BigDecimal totalPreTax) {
        this.totalPreTax = totalPreTax;
    }

    public String getExtra() {
        return this.extra;
    }

    public Invoice extra(String extra) {
        this.setExtra(extra);
        return this;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Invoice)) {
            return false;
        }
        return id != null && id.equals(((Invoice) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Invoice{" +
            "id=" + getId() +
            ", billId='" + getBillId() + "'" +
            ", customerId='" + getCustomerId() + "'" +
            ", customerName='" + getCustomerName() + "'" +
            ", idNumber='" + getIdNumber() + "'" +
//            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", pattern='" + getPattern() + "'" +
            ", no=" + getNo() +
            ", arisingDate='" + getArisingDate() + "'" +
            ", publishDate='" + getPublishDate() + "'" +
            ", paymentMethod='" + getPaymentMethod() + "'" +
            ", discountAmount=" + getDiscountAmount() +
            ", total_pre_tax=" + getTotalPreTax() +
            ", vatRate=" + getVatRate() +
            ", vatAmount=" + getVatAmount() +
            ", totalAmount=" + getTotalAmount() +
            ", type=" + getType() +
            ", status=" + getStatus() +
            ", taxCheckStatus=" + getTaxCheckStatus() +
            ", taxAuthorityCode='" + getTaxAuthorityCode() + "'" +
            ", taxErrorMessage='" + getTaxErrorMessage() + "'" +
            ", ikey='" + getIkey() + "'" +
            ", refikey='" + getRefikey() + "'" +
            ", extra='" + getExtra() + "'" +
            "}";
    }
}
