package vn.softdreams.easypos.domain;

import vn.softdreams.easypos.dto.card.CardHistoryResult;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * A LoyaltyCardUsage.
 */
@Entity
@Table(name = "loyalty_card_usage")
@SuppressWarnings("common-java:DuplicatedBlocks")
@SqlResultSetMappings(
    {
        @SqlResultSetMapping(
            name = "CardHistoryResult",
            classes = {
                @ConstructorResult(
                    targetClass = CardHistoryResult.class,
                    columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "cardName", type = String.class),
                        @ColumnResult(name = "usageDate", type = ZonedDateTime.class),
                        @ColumnResult(name = "type", type = Integer.class),
                        @ColumnResult(name = "billCode", type = String.class),
                        @ColumnResult(name = "amount", type = BigDecimal.class),
                        @ColumnResult(name = "point", type = Double.class),
                        @ColumnResult(name = "description", type = String.class),
                    }
                ),
            }
        ),
    }
)
public class LoyaltyCardUsage extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "com_id")
    private Integer comId;

    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "card_id")
    private Integer cardId;

    @Column(name = "type")
    private Integer type;

    @Column(name = "usage_date")
    private ZonedDateTime usageDate;

    @Column(name = "ref_id")
    private Integer refId;

    @Column(name = "amount", precision = 21, scale = 2)
    private BigDecimal amount;

    @Column(name = "point")
    private Integer point;

    @Column(name = "description")
    private String description;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getComId() {
        return this.comId;
    }

    public LoyaltyCardUsage comId(Integer comId) {
        this.setComId(comId);
        return this;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public Integer getCardId() {
        return this.cardId;
    }

    public LoyaltyCardUsage cardId(Integer cardId) {
        this.setCardId(cardId);
        return this;
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }

    public Integer getType() {
        return this.type;
    }

    public LoyaltyCardUsage type(Integer type) {
        this.setType(type);
        return this;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public ZonedDateTime getUsageDate() {
        return this.usageDate;
    }

    public LoyaltyCardUsage usageDate(ZonedDateTime usageDate) {
        this.setUsageDate(usageDate);
        return this;
    }

    public void setUsageDate(ZonedDateTime usageDate) {
        this.usageDate = usageDate;
    }

    public Integer getRefId() {
        return this.refId;
    }

    public LoyaltyCardUsage refId(Integer refId) {
        this.setRefId(refId);
        return this;
    }

    public void setRefId(Integer refId) {
        this.refId = refId;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public LoyaltyCardUsage amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getPoint() {
        return this.point;
    }

    public LoyaltyCardUsage point(Integer point) {
        this.setPoint(point);
        return this;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LoyaltyCardUsage)) {
            return false;
        }
        return id != null && id.equals(((LoyaltyCardUsage) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LoyaltyCardUsage{" +
            "id=" + getId() +
            ", comId=" + getComId() +
            ", customerId=" + getCustomerId() +
            ", customerId=" + getCustomerId() +
            ", cardId=" + getCardId() +
            ", type=" + getType() +
            ", usageDate='" + getUsageDate() + "'" +
            ", refId=" + getRefId() +
            ", amount=" + getAmount() +
            ", point=" + getPoint() +
            "}";
    }
}
