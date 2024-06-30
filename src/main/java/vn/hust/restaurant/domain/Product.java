package vn.hust.restaurant.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.DynamicUpdate;
import vn.hust.restaurant.service.dto.product.ProductDetailResponse;
import vn.hust.restaurant.service.dto.product.ProductResponse;

/**
 * A Product.
 */
@JsonIgnoreProperties(value = { "new" })
@Entity
@DynamicUpdate
@Table(name = "product")
@SqlResultSetMappings(
    {
        @SqlResultSetMapping(
            name = "ProductDTO",
            classes = {
                @ConstructorResult(
                    targetClass = ProductResponse.class,
                    columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "code", type = String.class),
                        @ColumnResult(name = "name", type = String.class),
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
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "unit", type = String.class),
                        @ColumnResult(name = "inPrice", type = BigDecimal.class),
                        @ColumnResult(name = "outPrice", type = BigDecimal.class),
                        @ColumnResult(name = "description", type = String.class),
                        @ColumnResult(name = "createTime", type = LocalDateTime.class),
                        @ColumnResult(name = "updateTime", type = LocalDateTime.class),
                        @ColumnResult(name = "imageUrl", type = String.class),
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

    @Size(max = 50)
    @NotNull
    @Column(name = "code")
    private String code;

    @Size(max = 500)
    @NotNull(message = "Tên không được để trống")
    @NotEmpty(message = "Tên không được để trống")
    @Column(name = "name")
    private String name;

    @Size(max = 50)
    @Column(name = "unit")
    private String unit;

    @Column(name = "in_price", precision = 21, scale = 6)
    private BigDecimal inPrice;

    @Column(name = "out_price", precision = 21, scale = 6)
    private BigDecimal outPrice;

    @Column(name = "description")
    private String description;

    @Column(name = "active")
    private Boolean active;

    @Size(max = 500)
    @Column(name = "image")
    private String image;

    @Column(name = "normalized_name")
    private String normalizedName;

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

    public String getNormalizedName() {
        return normalizedName;
    }

    public void setNormalizedName(String normalizedName) {
        this.normalizedName = normalizedName;
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
}
