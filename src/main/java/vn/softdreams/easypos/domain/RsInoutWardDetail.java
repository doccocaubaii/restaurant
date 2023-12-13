package vn.softdreams.easypos.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.NotAudited;
import vn.softdreams.easypos.dto.rsInoutWardDetail.GetOneByIdDetailResponse;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@JsonIgnoreProperties(value = { "new" })
@Entity
@DynamicUpdate
@Table(name = "rs_inoutward_detail")
@SqlResultSetMappings(
    {
        @SqlResultSetMapping(
            name = "GetOneByIdDetailResponse",
            classes = {
                @ConstructorResult(
                    targetClass = GetOneByIdDetailResponse.class,
                    columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "position", type = Integer.class),
                        @ColumnResult(name = "productName", type = String.class),
                        @ColumnResult(name = "productCode", type = String.class),
                        @ColumnResult(name = "unitName", type = String.class),
                        @ColumnResult(name = "quantity", type = BigDecimal.class),
                        @ColumnResult(name = "unitPrice", type = BigDecimal.class),
                        @ColumnResult(name = "amount", type = BigDecimal.class),
                        @ColumnResult(name = "discountAmount", type = BigDecimal.class),
                        @ColumnResult(name = "totalAmount", type = BigDecimal.class),
                        @ColumnResult(name = "lotNo", type = String.class),
                    }
                ),
            }
        ),
    }
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RsInoutWardDetail extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "rs_inoutward_id", nullable = false, referencedColumnName = "id")
    @JsonBackReference
    private RsInoutWard rsInoutWard;

    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "quantity", precision = 21, scale = 6)
    private BigDecimal quantity;

    @Column(name = "unit_price", precision = 21, scale = 6)
    private BigDecimal unitPrice;

    @NotAudited
    @Column(name = "unit_id")
    private Integer unitId;

    @Column(name = "unit_name")
    private String unitName;

    @Column(name = "amount", precision = 21, scale = 6)
    private BigDecimal amount;

    @Column(name = "discount_amount", precision = 21, scale = 6)
    private BigDecimal discountAmount;

    @Column(name = "total_amount", precision = 21, scale = 6)
    private BigDecimal totalAmount;

    @Column(name = "position")
    private Integer position;

    @Column(name = "lot_no")
    private String lotNo;

    @NotAudited
    @Column(name = "product_normalized_name")
    private String productNormalizedName;

    public RsInoutWard getRsInoutWard() {
        return rsInoutWard;
    }

    public void setRsInoutWard(RsInoutWard rsInoutWard) {
        this.rsInoutWard = rsInoutWard;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
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

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    public String getProductNormalizedName() {
        return productNormalizedName;
    }

    public void setProductNormalizedName(String productNormalizedName) {
        this.productNormalizedName = productNormalizedName;
    }
}
