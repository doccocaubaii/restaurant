package vn.softdreams.easypos.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;
import vn.softdreams.easypos.dto.inventory.InventoryCommonStats;
import vn.softdreams.easypos.dto.product.*;
import vn.softdreams.easypos.dto.productGroup.ProductOfflineResponse;
import vn.softdreams.easypos.dto.toppingGroup.ProductToppingItemResponse;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * A Product.
 */
@JsonIgnoreProperties(value = { "new" })
@Entity
@DynamicUpdate
@Table(name = "product")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@AuditTable(value = "product_aud", schema = "audit")
@SuppressWarnings("common-java:DuplicatedBlocks")
@SqlResultSetMappings(
    {
        @SqlResultSetMapping(
            name = "ProductCheckBill",
            classes = {
                @ConstructorResult(
                    targetClass = ProductCheckBill.class,
                    columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "productProductUnitId", type = Integer.class),
                        @ColumnResult(name = "code", type = String.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "unitId", type = Integer.class),
                        @ColumnResult(name = "unitName", type = String.class),
                        @ColumnResult(name = "isPrimary", type = Boolean.class),
                        @ColumnResult(name = "convertRate", type = BigDecimal.class),
                        @ColumnResult(name = "formula", type = Boolean.class),
                        @ColumnResult(name = "onHand", type = BigDecimal.class),
                        @ColumnResult(name = "inventoryTracking", type = Boolean.class),
                        @ColumnResult(name = "overStock", type = Integer.class),
                    }
                ),
            }
        ),
        @SqlResultSetMapping(
            name = "ProductResponseDTO",
            classes = {
                @ConstructorResult(
                    targetClass = ProductResponse.class,
                    columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "comId", type = Integer.class),
                        @ColumnResult(name = "code", type = String.class),
                        @ColumnResult(name = "code2", type = String.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "unit", type = String.class),
                        @ColumnResult(name = "unitId", type = Integer.class),
                        @ColumnResult(name = "inPrice", type = BigDecimal.class),
                        @ColumnResult(name = "outPrice", type = BigDecimal.class),
                        @ColumnResult(name = "vatRate", type = Integer.class),
                        @ColumnResult(name = "inventoryTracking", type = Boolean.class),
                        @ColumnResult(name = "inventoryCount", type = BigDecimal.class),
                        @ColumnResult(name = "description", type = String.class),
                        @ColumnResult(name = "createTime", type = ZonedDateTime.class),
                        @ColumnResult(name = "updateTime", type = ZonedDateTime.class),
                    }
                ),
            }
        ),
        @SqlResultSetMapping(
            name = "ProductGroupSearchResponse",
            classes = {
                @ConstructorResult(
                    targetClass = ProductOfflineResponse.class,
                    columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "comId", type = Integer.class),
                        @ColumnResult(name = "code", type = String.class),
                        @ColumnResult(name = "code2", type = String.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "unit", type = String.class),
                        @ColumnResult(name = "inPrice", type = BigDecimal.class),
                        @ColumnResult(name = "outPrice", type = BigDecimal.class),
                        @ColumnResult(name = "barCode", type = String.class),
                        @ColumnResult(name = "barCode2", type = String.class),
                        @ColumnResult(name = "vatRate", type = Integer.class),
                        @ColumnResult(name = "inventoryId", type = Integer.class),
                        @ColumnResult(name = "inventoryCount", type = BigDecimal.class),
                        @ColumnResult(name = "description", type = String.class),
                        @ColumnResult(name = "active", type = Boolean.class),
                        @ColumnResult(name = "image", type = String.class),
                        @ColumnResult(name = "isInventory", type = Boolean.class),
                        @ColumnResult(name = "createTime", type = ZonedDateTime.class),
                        @ColumnResult(name = "updateTime", type = ZonedDateTime.class),
                        @ColumnResult(name = "productGroupId", type = Integer.class),
                    }
                ),
            }
        ),
        @SqlResultSetMapping(
            name = "GetProductResponse",
            classes = {
                @ConstructorResult(
                    targetClass = GetProductResponse.class,
                    columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "comId", type = Integer.class),
                        @ColumnResult(name = "code", type = String.class),
                        @ColumnResult(name = "code2", type = String.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "unit", type = String.class),
                        @ColumnResult(name = "unitId", type = Integer.class),
                        @ColumnResult(name = "inPrice", type = BigDecimal.class),
                        @ColumnResult(name = "outPrice", type = BigDecimal.class),
                        @ColumnResult(name = "vatRate", type = Integer.class),
                        @ColumnResult(name = "inventoryTracking", type = Boolean.class),
                        @ColumnResult(name = "inventoryCount", type = BigDecimal.class),
                        @ColumnResult(name = "description", type = String.class),
                        @ColumnResult(name = "createTime", type = ZonedDateTime.class),
                        @ColumnResult(name = "updateTime", type = ZonedDateTime.class),
                    }
                ),
            }
        ),
        @SqlResultSetMapping(
            name = "ProductResponse",
            classes = {
                @ConstructorResult(
                    targetClass = ProductResponse.class,
                    columns = {
                        @ColumnResult(name = "productProductUnitId", type = Integer.class),
                        @ColumnResult(name = "comId", type = Integer.class),
                        @ColumnResult(name = "code", type = String.class),
                        @ColumnResult(name = "code2", type = String.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "unit", type = String.class),
                        @ColumnResult(name = "unitId", type = Integer.class),
                        @ColumnResult(name = "inPrice", type = BigDecimal.class),
                        @ColumnResult(name = "outPrice", type = BigDecimal.class),
                        @ColumnResult(name = "vatRate", type = Integer.class),
                        @ColumnResult(name = "inventoryTracking", type = Boolean.class),
                        @ColumnResult(name = "inventoryCount", type = BigDecimal.class),
                        @ColumnResult(name = "description", type = String.class),
                        @ColumnResult(name = "createTime", type = ZonedDateTime.class),
                        @ColumnResult(name = "updateTime", type = ZonedDateTime.class),
                        @ColumnResult(name = "barCode", type = String.class),
                        @ColumnResult(name = "imageUrl", type = String.class),
                    }
                ),
            }
        ),
        @SqlResultSetMapping(
            name = "ProductBillResponse",
            classes = {
                @ConstructorResult(
                    targetClass = ProductItemResponse.class,
                    columns = {
                        @ColumnResult(name = "productProductUnitId", type = Integer.class),
                        @ColumnResult(name = "productId", type = Integer.class),
                        @ColumnResult(name = "comId", type = Integer.class),
                        @ColumnResult(name = "code", type = String.class),
                        @ColumnResult(name = "code2", type = String.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "unit", type = String.class),
                        @ColumnResult(name = "unitId", type = Integer.class),
                        @ColumnResult(name = "inPrice", type = BigDecimal.class),
                        @ColumnResult(name = "outPrice", type = BigDecimal.class),
                        @ColumnResult(name = "vatRate", type = Integer.class),
                        @ColumnResult(name = "inventoryTracking", type = Boolean.class),
                        @ColumnResult(name = "inventoryCount", type = BigDecimal.class),
                        @ColumnResult(name = "description", type = String.class),
                        @ColumnResult(name = "createTime", type = ZonedDateTime.class),
                        @ColumnResult(name = "updateTime", type = ZonedDateTime.class),
                        @ColumnResult(name = "barCode", type = String.class),
                        @ColumnResult(name = "imageUrl", type = String.class),
                        @ColumnResult(name = "isTopping", type = Boolean.class),
                        @ColumnResult(name = "isPrimary", type = Boolean.class),
                        @ColumnResult(name = "haveOtherPrice", type = Boolean.class),
                        @ColumnResult(name = "discountVatRate", type = Integer.class),
                    }
                ),
            }
        ),
        @SqlResultSetMapping(
            name = "ProductToppingItemResponse",
            classes = {
                @ConstructorResult(
                    targetClass = ProductToppingItemResponse.class,
                    columns = {
                        @ColumnResult(name = "toppingGroupId", type = Integer.class),
                        @ColumnResult(name = "productProductUnitId", type = Integer.class),
                        @ColumnResult(name = "productId", type = Integer.class),
                        @ColumnResult(name = "comId", type = Integer.class),
                        @ColumnResult(name = "code", type = String.class),
                        @ColumnResult(name = "code2", type = String.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "unit", type = String.class),
                        @ColumnResult(name = "unitId", type = Integer.class),
                        @ColumnResult(name = "inPrice", type = BigDecimal.class),
                        @ColumnResult(name = "outPrice", type = BigDecimal.class),
                        @ColumnResult(name = "vatRate", type = Integer.class),
                        @ColumnResult(name = "discountVatRate", type = Integer.class),
                        @ColumnResult(name = "inventoryTracking", type = Boolean.class),
                        @ColumnResult(name = "inventoryCount", type = BigDecimal.class),
                        @ColumnResult(name = "description", type = String.class),
                        @ColumnResult(name = "createTime", type = ZonedDateTime.class),
                        @ColumnResult(name = "updateTime", type = ZonedDateTime.class),
                        @ColumnResult(name = "barCode", type = String.class),
                        @ColumnResult(name = "imageUrl", type = String.class),
                        @ColumnResult(name = "isTopping", type = Boolean.class),
                    }
                ),
            }
        ),
        @SqlResultSetMapping(
            name = "ProductToppingItemOfflineResponse",
            classes = {
                @ConstructorResult(
                    targetClass = ProductToppingItemResponse.class,
                    columns = {
                        @ColumnResult(name = "toppingGroupId", type = Integer.class),
                        @ColumnResult(name = "productProductUnitId", type = Integer.class),
                        @ColumnResult(name = "productId", type = Integer.class),
                        @ColumnResult(name = "comId", type = Integer.class),
                        @ColumnResult(name = "code", type = String.class),
                        @ColumnResult(name = "code2", type = String.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "unit", type = String.class),
                        @ColumnResult(name = "unitId", type = Integer.class),
                        @ColumnResult(name = "inPrice", type = BigDecimal.class),
                        @ColumnResult(name = "outPrice", type = BigDecimal.class),
                        @ColumnResult(name = "vatRate", type = Integer.class),
                        @ColumnResult(name = "discountVatRate", type = Integer.class),
                        @ColumnResult(name = "inventoryTracking", type = Boolean.class),
                        @ColumnResult(name = "inventoryCount", type = BigDecimal.class),
                        @ColumnResult(name = "description", type = String.class),
                        @ColumnResult(name = "createTime", type = ZonedDateTime.class),
                        @ColumnResult(name = "updateTime", type = ZonedDateTime.class),
                        @ColumnResult(name = "barCode", type = String.class),
                        @ColumnResult(name = "imageUrl", type = String.class),
                        @ColumnResult(name = "isTopping", type = Boolean.class),
                        @ColumnResult(name = "parentProductId", type = Integer.class),
                    }
                ),
            }
        ),
        @SqlResultSetMapping(
            name = "ProductToppingItem",
            classes = {
                @ConstructorResult(
                    targetClass = ProductToppingItem.class,
                    columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "comId", type = Integer.class),
                        @ColumnResult(name = "image", type = String.class),
                        @ColumnResult(name = "code", type = String.class),
                        @ColumnResult(name = "code2", type = String.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "unit", type = String.class),
                        @ColumnResult(name = "unitId", type = Integer.class),
                        @ColumnResult(name = "purchasePrice", type = BigDecimal.class),
                        @ColumnResult(name = "salePrice", type = BigDecimal.class),
                        @ColumnResult(name = "vatRate", type = Integer.class),
                        @ColumnResult(name = "inventoryTracking", type = Boolean.class),
                        @ColumnResult(name = "inventoryCount", type = BigDecimal.class),
                        @ColumnResult(name = "description", type = String.class),
                    }
                ),
            }
        ),
        @SqlResultSetMapping(
            name = "ProductItemResponse",
            classes = {
                @ConstructorResult(
                    targetClass = ProductItem.class,
                    columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "comId", type = Integer.class),
                        @ColumnResult(name = "code", type = String.class),
                        @ColumnResult(name = "code2", type = String.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "unit", type = String.class),
                        @ColumnResult(name = "unitId", type = Integer.class),
                        @ColumnResult(name = "inPrice", type = BigDecimal.class),
                        @ColumnResult(name = "outPrice", type = BigDecimal.class),
                        @ColumnResult(name = "vatRate", type = Integer.class),
                        @ColumnResult(name = "inventoryTracking", type = Boolean.class),
                        @ColumnResult(name = "inventoryCount", type = BigDecimal.class),
                        @ColumnResult(name = "description", type = String.class),
                        @ColumnResult(name = "createTime", type = ZonedDateTime.class),
                        @ColumnResult(name = "updateTime", type = ZonedDateTime.class),
                        @ColumnResult(name = "barCode", type = String.class),
                        @ColumnResult(name = "imageUrl", type = String.class),
                        @ColumnResult(name = "isTopping", type = Boolean.class),
                        @ColumnResult(name = "discountVatRate", type = Integer.class),
                    }
                ),
            }
        ),
        @SqlResultSetMapping(
            name = "ProductItemResult",
            classes = {
                @ConstructorResult(
                    targetClass = ProductDetailResponse.class,
                    columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "comId", type = Integer.class),
                        @ColumnResult(name = "code", type = String.class),
                        @ColumnResult(name = "code2", type = String.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "unit", type = String.class),
                        @ColumnResult(name = "unitId", type = Integer.class),
                        @ColumnResult(name = "inPrice", type = BigDecimal.class),
                        @ColumnResult(name = "outPrice", type = BigDecimal.class),
                        @ColumnResult(name = "vatRate", type = Integer.class),
                        @ColumnResult(name = "inventoryTracking", type = Boolean.class),
                        @ColumnResult(name = "inventoryCount", type = BigDecimal.class),
                        @ColumnResult(name = "description", type = String.class),
                        @ColumnResult(name = "createTime", type = ZonedDateTime.class),
                        @ColumnResult(name = "updateTime", type = ZonedDateTime.class),
                        @ColumnResult(name = "barCode", type = String.class),
                        @ColumnResult(name = "imageUrl", type = String.class),
                        @ColumnResult(name = "isTopping", type = Boolean.class),
                    }
                ),
            }
        ),
        @SqlResultSetMapping(
            name = "InventoryStatsResponses",
            classes = {
                @ConstructorResult(
                    targetClass = InventoryCommonStats.InventoryCommonStatsDetail.class,
                    columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "code", type = String.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "onHand", type = BigDecimal.class),
                        @ColumnResult(name = "purchasePrice", type = BigDecimal.class),
                        @ColumnResult(name = "value", type = BigDecimal.class),
                    }
                ),
            }
        ),
        @SqlResultSetMapping(
            name = "ProductRevenueResult",
            classes = {
                @ConstructorResult(
                    targetClass = ProductProfitResult.class,
                    columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "revenue", type = BigDecimal.class),
                        @ColumnResult(name = "quantity", type = BigDecimal.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "unitName", type = String.class),
                        @ColumnResult(name = "unitId", type = Integer.class),
                    }
                ),
            }
        ),
        @SqlResultSetMapping(
            name = "ProductCostResult",
            classes = {
                @ConstructorResult(
                    targetClass = ProductProfitResult.class,
                    columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "unitName", type = String.class),
                        @ColumnResult(name = "quantity", type = BigDecimal.class),
                        @ColumnResult(name = "costPrice", type = BigDecimal.class),
                        @ColumnResult(name = "unitId", type = Integer.class),
                    }
                ),
            }
        ),
        @SqlResultSetMapping(
            name = "HotSaleProductResult",
            classes = {
                @ConstructorResult(
                    targetClass = HotSaleProductResult.class,
                    columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "unitName", type = String.class),
                        @ColumnResult(name = "totalQuantity", type = BigDecimal.class),
                        @ColumnResult(name = "totalAmount", type = BigDecimal.class),
                        @ColumnResult(name = "image", type = String.class),
                    }
                ),
            }
        ),
        @SqlResultSetMapping(
            name = "ProductVoucherResponse",
            classes = {
                @ConstructorResult(
                    targetClass = ProductItemResponse.class,
                    columns = {
                        @ColumnResult(name = "productProductUnitId", type = Integer.class),
                        @ColumnResult(name = "productId", type = Integer.class),
                        @ColumnResult(name = "comId", type = Integer.class),
                        @ColumnResult(name = "code", type = String.class),
                        @ColumnResult(name = "code2", type = String.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "unit", type = String.class),
                        @ColumnResult(name = "unitId", type = Integer.class),
                        @ColumnResult(name = "inPrice", type = BigDecimal.class),
                        @ColumnResult(name = "outPrice", type = BigDecimal.class),
                        @ColumnResult(name = "vatRate", type = Integer.class),
                        @ColumnResult(name = "inventoryTracking", type = Boolean.class),
                        @ColumnResult(name = "inventoryCount", type = BigDecimal.class),
                        @ColumnResult(name = "description", type = String.class),
                        @ColumnResult(name = "createTime", type = ZonedDateTime.class),
                        @ColumnResult(name = "updateTime", type = ZonedDateTime.class),
                        @ColumnResult(name = "barCode", type = String.class),
                        @ColumnResult(name = "imageUrl", type = String.class),
                        @ColumnResult(name = "isTopping", type = Boolean.class),
                        @ColumnResult(name = "isPrimary", type = Boolean.class),
                        @ColumnResult(name = "haveOtherPrice", type = Boolean.class),
                        @ColumnResult(name = "discountVatRate", type = Integer.class),
                        @ColumnResult(name = "groupId", type = Integer.class),
                    }
                ),
            }
        ),
    }
)
public class Product extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "com_id")
    private Integer comId;

    @NotAudited
    @Size(max = 50)
    @NotNull
    @Column(name = "code")
    private String code;

    @Size(max = 50)
    @Column(name = "code2")
    private String code2;

    @Size(max = 500)
    @NotNull(message = "Tên không được để trống")
    @NotEmpty(message = "Tên không được để trống")
    @Column(name = "name")
    private String name;

    @Size(max = 50)
    @Column(name = "unit")
    private String unit;

    @Column(name = "feature")
    private Integer feature;

    @NotAudited
    @Column(name = "unit_id")
    private Integer unitId;

    @Column(name = "in_price", precision = 21, scale = 6)
    private BigDecimal inPrice;

    @Column(name = "out_price", precision = 21, scale = 6)
    private BigDecimal outPrice;

    @Size(max = 50)
    @Column(name = "bar_code")
    private String barCode;

    @Size(max = 20)
    @Column(name = "bar_code_2")
    private String barCode2;

    @Column(name = "vat_rate")
    private Integer vatRate;

    @Column(name = "inventory_id")
    private Integer inventoryId;

    @Column(name = "inventory_count", precision = 21, scale = 6)
    private BigDecimal inventoryCount;

    @NotAudited
    @Column(name = "description")
    private String description;

    @Column(name = "active")
    private Boolean active;

    @NotAudited
    @Size(max = 500)
    @Column(name = "image")
    private String image;

    @Column(name = "inventory_tracking")
    private Boolean inventoryTracking = false;

    @NotAudited
    @Column(name = "eb_id")
    private Integer ebId;

    @Column(name = "minimum_stock")
    private BigDecimal minimumStock;

    @NotAudited
    @Column(name = "normalized_name")
    private String normalizedName;

    @NotAudited
    @Column(name = "is_topping")
    private Boolean isTopping;

    @NotAudited
    @Column(name = "discount_vat_rate")
    private Integer discountVatRate;

    @NotAudited
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "product_product_group",
        joinColumns = { @JoinColumn(name = "product_id", columnDefinition = "id") },
        inverseJoinColumns = { @JoinColumn(name = "product_group_id", referencedColumnName = "id") }
    )
    private List<ProductGroup> productGroups = new ArrayList<>();

    public Integer getFeature() {
        return feature;
    }

    public void setFeature(Integer feature) {
        this.feature = feature;
    }

    public BigDecimal getMinimumStock() {
        return minimumStock;
    }

    public void setMinimumStock(BigDecimal minimumStock) {
        this.minimumStock = minimumStock;
    }

    public Integer getEbId() {
        return ebId;
    }

    public void setEbId(Integer ebId) {
        this.ebId = ebId;
    }

    public Boolean getInventoryTracking() {
        return inventoryTracking;
    }

    public void setInventoryTracking(Boolean inventoryTracking) {
        this.inventoryTracking = inventoryTracking;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public List<ProductGroup> getProductGroups() {
        return productGroups;
    }

    public void setProductGroups(List<ProductGroup> productGroups) {
        this.productGroups = productGroups;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getPurchasePrice() {
        return inPrice;
    }

    public void setPurchasePrice(BigDecimal inPrice) {
        this.inPrice = inPrice;
    }

    public BigDecimal getSalePrice() {
        return outPrice;
    }

    public void setSalePrice(BigDecimal outPrice) {
        this.outPrice = outPrice;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getBarCode2() {
        return barCode2;
    }

    public void setBarCode2(String barCode2) {
        this.barCode2 = barCode2;
    }

    public Integer getVatRate() {
        return vatRate;
    }

    public void setVatRate(Integer vatRate) {
        this.vatRate = vatRate;
    }

    public Integer getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Integer inventoryId) {
        this.inventoryId = inventoryId;
    }

    public BigDecimal getInventoryCount() {
        return inventoryCount;
    }

    public void setInventoryCount(BigDecimal inventoryCount) {
        this.inventoryCount = inventoryCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getCode2() {
        return code2;
    }

    public void setCode2(String code2) {
        this.code2 = code2;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public String getNormalizedName() {
        return normalizedName;
    }

    public void setNormalizedName(String normalizedName) {
        this.normalizedName = normalizedName;
    }

    public Boolean getIsTopping() {
        return isTopping;
    }

    public void setIsTopping(Boolean topping) {
        isTopping = topping;
    }

    public Integer getDiscountVatRate() {
        return discountVatRate;
    }

    public void setDiscountVatRate(Integer discountVatRate) {
        this.discountVatRate = discountVatRate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return id != null && id.equals(((Product) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", comId='" + getComId() + "'" +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", unit='" + getUnit() + "'" +
            ", inPrice=" + getPurchasePrice() +
            ", outPrice=" + getSalePrice() +
            ", barCode='" + getBarCode() + "'" +
            ", barCode2='" + getBarCode2() + "'" +
            ", vatRate=" + getVatRate() +
            ", inventoryId='" + getInventoryId() + "'" +
            ", inventoryCount=" + getInventoryCount() +
            ", description='" + getDescription() + "'" +
            ", active='" + getActive() + "'" +
            "}";
    }
}
