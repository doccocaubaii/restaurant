package vn.softdreams.easypos.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.NotAudited;
import vn.softdreams.easypos.dto.reservation.ReservationResponse;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A Reservation.
 */
@JsonIgnoreProperties(value = { "new" })
@Entity
@DynamicUpdate
@Table(name = "reservation")
@SuppressWarnings("common-java:DuplicatedBlocks")
@SqlResultSetMappings(
    {
        @SqlResultSetMapping(
            name = "ReservationResponse",
            classes = {
                @ConstructorResult(
                    targetClass = ReservationResponse.class,
                    columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "comId", type = Integer.class),
                        @ColumnResult(name = "customerPhone", type = String.class),
                        @ColumnResult(name = "customerName", type = String.class),
                        @ColumnResult(name = "orderDate", type = String.class),
                        @ColumnResult(name = "orderTime", type = String.class),
                        @ColumnResult(name = "arrivalTime", type = String.class),
                        @ColumnResult(name = "peopleCount", type = Integer.class),
                        @ColumnResult(name = "status", type = Integer.class),
                        @ColumnResult(name = "note", type = String.class),
                        @ColumnResult(name = "createTime", type = String.class),
                        @ColumnResult(name = "updateTime", type = String.class),
                    }
                ),
            }
        ),
    }
)
public class Reservation extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "com_id")
    private Integer comId;

    @Column(name = "customer_phone")
    private String customerPhone;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "order_date")
    private String orderDate;

    @Column(name = "order_time")
    private String orderTime;

    @Column(name = "arrival_time")
    private String arrivalTime;

    @Column(name = "people_count")
    private Integer peopleCount;

    @Column(name = "status")
    private Integer status;

    @Column(name = "note")
    private String note;

    @NotAudited
    @Column(name = "normalized_name")
    private String normalizedName;

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

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getArrivalTime() {
        return this.arrivalTime;
    }

    public Reservation arrivalTime(String arrivalTime) {
        this.setArrivalTime(arrivalTime);
        return this;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Integer getPeopleCount() {
        return this.peopleCount;
    }

    public Reservation peopleCount(Integer peopleCount) {
        this.setPeopleCount(peopleCount);
        return this;
    }

    public void setPeopleCount(Integer peopleCount) {
        this.peopleCount = peopleCount;
    }

    public Integer getStatus() {
        return this.status;
    }

    public Reservation status(Integer status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getNote() {
        return this.note;
    }

    public Reservation note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
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
        if (!(o instanceof Reservation)) {
            return false;
        }
        return id != null && id.equals(((Reservation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Reservation{" +
            "id=" + getId() +
            ", comId='" + getComId() + "'" +
            ", customerPhone='" + getCustomerPhone() + "'" +
            ", customerName='" + getCustomerName() + "'" +
            ", orderDate='" + getOrderDate() + "'" +
            ", orderTime='" + getOrderTime() + "'" +
            ", arrivalTime='" + getArrivalTime() + "'" +
            ", peopleCount=" + getPeopleCount() +
            ", status=" + getStatus() +
            ", note='" + getNote() + "'" +

            "}";
    }
}
