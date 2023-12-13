package vn.softdreams.easypos.domain;

import org.hibernate.annotations.DynamicUpdate;
import vn.softdreams.easypos.dto.loyaltyCard.LoyaltyCardResultItem;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A LoyaltyCard.
 */
@Entity
@Table(name = "loyalty_card")
@DynamicUpdate
@SuppressWarnings("common-java:DuplicatedBlocks")
@SqlResultSetMappings(
    {
        @SqlResultSetMapping(
            name = "LoyaltyCardItem",
            classes = {
                @ConstructorResult(
                    targetClass = LoyaltyCardResultItem.class,
                    columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "isDefault", type = Boolean.class),
                        @ColumnResult(name = "rank", type = Integer.class),
                        @ColumnResult(name = "status", type = Integer.class),
                        @ColumnResult(name = "customerId", type = Integer.class),
                        @ColumnResult(name = "customerName", type = String.class),
                        @ColumnResult(name = "code", type = String.class),
                        @ColumnResult(name = "code2", type = String.class),
                        @ColumnResult(name = "address", type = String.class),
                        @ColumnResult(name = "phoneNumber", type = String.class),
                        @ColumnResult(name = "email", type = String.class),
                        @ColumnResult(name = "taxCode", type = String.class),
                        @ColumnResult(name = "idNumber", type = String.class),
                        @ColumnResult(name = "type", type = Integer.class),
                        @ColumnResult(name = "description", type = String.class),
                        @ColumnResult(name = "city", type = String.class),
                        @ColumnResult(name = "district", type = String.class),
                        @ColumnResult(name = "createTime", type = ZonedDateTime.class),
                        @ColumnResult(name = "updateTime", type = ZonedDateTime.class),
                    }
                ),
            }
        ),
    }
)
public class LoyaltyCard extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "com_id")
    private Integer comId;

    @Column(name = "normalized_name")
    private String normalizedName;

    @Column(name = "is_default")
    private Boolean isDefault;

    @Column(name = "rank")
    private Integer rank;

    @Column(name = "status")
    private Integer status;

    // jhipster-needle-entity-add-field - JHipster will add fields here

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

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public String getName() {
        return this.name;
    }

    public LoyaltyCard name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNormalizedName() {
        return this.normalizedName;
    }

    public LoyaltyCard normalizedName(String normalizedName) {
        this.setNormalizedName(normalizedName);
        return this;
    }

    public void setNormalizedName(String normalizedName) {
        this.normalizedName = normalizedName;
    }

    public Boolean getIsDefault() {
        return this.isDefault;
    }

    public LoyaltyCard isDefault(Boolean isDefault) {
        this.setIsDefault(isDefault);
        return this;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Integer getRank() {
        return this.rank;
    }

    public LoyaltyCard rank(Integer rank) {
        this.setRank(rank);
        return this;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getStatus() {
        return this.status;
    }

    public LoyaltyCard status(Integer status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LoyaltyCard)) {
            return false;
        }
        return id != null && id.equals(((LoyaltyCard) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LoyaltyCard{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", normalizedName='" + getNormalizedName() + "'" +
            ", isDefault='" + getIsDefault() + "'" +
            ", rank=" + getRank() +
            ", status=" + getStatus() +
            "}";
    }
}
