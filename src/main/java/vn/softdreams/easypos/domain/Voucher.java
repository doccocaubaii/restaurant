package vn.softdreams.easypos.domain;

import vn.softdreams.easypos.dto.voucher.VoucherResponse;
import vn.softdreams.easypos.dto.voucher.VoucherWebResponse;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A Voucher.
 */
@Entity
@Table(name = "voucher")
@SuppressWarnings("common-java:DuplicatedBlocks")
@SqlResultSetMappings(
    {
        @SqlResultSetMapping(
            name = "VoucherResponse",
            classes = {
                @ConstructorResult(
                    targetClass = VoucherResponse.class,
                    columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "code", type = String.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "discountConditions", type = String.class),
                        @ColumnResult(name = "startTime", type = ZonedDateTime.class),
                        @ColumnResult(name = "endTime", type = ZonedDateTime.class),
                        @ColumnResult(name = "status", type = Integer.class),
                        @ColumnResult(name = "type", type = Integer.class),
                        @ColumnResult(name = "extTimeCondition", type = String.class),
                        @ColumnResult(name = "differentExtCondition", type = String.class),
                    }
                ),
            }
        ),
        @SqlResultSetMapping(
            name = "VoucherWebResponse",
            classes = {
                @ConstructorResult(
                    targetClass = VoucherWebResponse.class,
                    columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "code", type = String.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "discountConditions", type = String.class),
                        @ColumnResult(name = "startTime", type = ZonedDateTime.class),
                        @ColumnResult(name = "endTime", type = ZonedDateTime.class),
                        @ColumnResult(name = "status", type = Integer.class),
                    }
                ),
            }
        ),
    }
)
public class Voucher extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "normalized_name")
    private String normalizedName;

    @Column(name = "code")
    private String code;

    @Column(name = "note")
    private String note;

    @Column(name = "gen_description")
    private String genDescription;

    @Column(name = "type")
    private Integer type;

    @Column(name = "status")
    private Integer status;

    @Column(name = "discount_conditions")
    private String discountConditions;

    @Column(name = "start_time")
    private ZonedDateTime startTime;

    @Column(name = "end_time")
    private ZonedDateTime endTime;

    @Column(name = "ext_time_conditions")
    private String extTimeConditions;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "different_ext_conditions")
    private String differentExtConditions;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Voucher name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNormalizedName() {
        return this.normalizedName;
    }

    public Voucher normalizedName(String normalizedName) {
        this.setNormalizedName(normalizedName);
        return this;
    }

    public void setNormalizedName(String normalizedName) {
        this.normalizedName = normalizedName;
    }

    public String getCode() {
        return this.code;
    }

    public Voucher code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNote() {
        return this.note;
    }

    public Voucher note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getGenDescription() {
        return this.genDescription;
    }

    public Voucher genDescription(String genDescription) {
        this.setGenDescription(genDescription);
        return this;
    }

    public void setGenDescription(String genDescription) {
        this.genDescription = genDescription;
    }

    public Integer getType() {
        return this.type;
    }

    public Voucher type(Integer type) {
        this.setType(type);
        return this;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return this.status;
    }

    public Voucher status(Integer status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDiscountConditions() {
        return this.discountConditions;
    }

    public Voucher discountConditions(String discountConditions) {
        this.setDiscountConditions(discountConditions);
        return this;
    }

    public void setDiscountConditions(String discountConditions) {
        this.discountConditions = discountConditions;
    }

    public ZonedDateTime getStartTime() {
        return this.startTime;
    }

    public Voucher startTime(ZonedDateTime startTime) {
        this.setStartTime(startTime);
        return this;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTime getEndTime() {
        return this.endTime;
    }

    public Voucher endTime(ZonedDateTime endTime) {
        this.setEndTime(endTime);
        return this;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }

    public String getExtTimeConditions() {
        return extTimeConditions;
    }

    public void setExtTimeConditions(String extTimeConditions) {
        this.extTimeConditions = extTimeConditions;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Voucher active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getDifferentExtConditions() {
        return differentExtConditions;
    }

    public void setDifferentExtConditions(String differentExtConditions) {
        this.differentExtConditions = differentExtConditions;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Voucher)) {
            return false;
        }
        return id != null && id.equals(((Voucher) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Voucher{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", normalizedName='" + getNormalizedName() + "'" +
            ", code='" + getCode() + "'" +
            ", note='" + getNote() + "'" +
            ", genDescription='" + getGenDescription() + "'" +
            ", type=" + getType() +
            ", status=" + getStatus() +
            ", discountConditions='" + getDiscountConditions() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", extTimeConditions='" + getExtTimeConditions() + "'" +
            ", active='" + getActive() + "'" +
            "}";
    }
}
