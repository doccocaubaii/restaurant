package vn.softdreams.easypos.domain;

import vn.softdreams.easypos.dto.customerCard.CustomerCardInformation;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * A CustomerCard.
 */
@Entity
@Table(name = "customer_card")
@SuppressWarnings("common-java:DuplicatedBlocks")
@SqlResultSetMappings(
    {
        @SqlResultSetMapping(
            name = "CustomerCardDetail",
            classes = {
                @ConstructorResult(
                    targetClass = CustomerCardInformation.class,
                    columns = {
                        @ColumnResult(name = "cardId", type = Integer.class),
                        @ColumnResult(name = "customerId", type = Integer.class),
                        @ColumnResult(name = "cardCode", type = Integer.class),
                        @ColumnResult(name = "cardName", type = String.class),
                        @ColumnResult(name = "point", type = Integer.class),
                        @ColumnResult(name = "amount", type = BigDecimal.class),
                        @ColumnResult(name = "rank", type = Integer.class),
                    }
                ),
            }
        ),
    }
)
public class CustomerCard extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "com_id")
    private Integer comId;

    @Column(name = "card_id")
    private Integer cardId;

    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "code")
    private Integer code;

    @Column(name = "amount", precision = 21, scale = 2)
    private BigDecimal amount;

    @Column(name = "point")
    private Integer point;

    @Column(name = "start_date")
    private ZonedDateTime startDate;

    @Column(name = "expired_date")
    private ZonedDateTime expiredDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getComId() {
        return this.comId;
    }

    public CustomerCard comId(Integer comId) {
        this.setComId(comId);
        return this;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public Integer getCardId() {
        return this.cardId;
    }

    public CustomerCard cardId(Integer cardId) {
        this.setCardId(cardId);
        return this;
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }

    public Integer getCustomerId() {
        return this.customerId;
    }

    public CustomerCard customerId(Integer customerId) {
        this.setCustomerId(customerId);
        return this;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getCode() {
        return this.code;
    }

    public CustomerCard code(Integer code) {
        this.setCode(code);
        return this;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public CustomerCard amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getPoint() {
        return this.point;
    }

    public CustomerCard point(Integer point) {
        this.setPoint(point);
        return this;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public ZonedDateTime getStartDate() {
        return this.startDate;
    }

    public CustomerCard startDate(ZonedDateTime startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getExpiredDate() {
        return this.expiredDate;
    }

    public CustomerCard expiredDate(ZonedDateTime expiredDate) {
        this.setExpiredDate(expiredDate);
        return this;
    }

    public void setExpiredDate(ZonedDateTime expiredDate) {
        this.expiredDate = expiredDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomerCard)) {
            return false;
        }
        return id != null && id.equals(((CustomerCard) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerCard{" +
            "id=" + getId() +
            ", comId=" + getComId() +
            ", cardId=" + getCardId() +
            ", customerId=" + getCustomerId() +
            ", code='" + getCode() + "'" +
            ", amount=" + getAmount() +
            ", point=" + getPoint() +
            ", startDate='" + getStartDate() + "'" +
            ", expiredDate='" + getExpiredDate() + "'" +
            "}";
    }
}
