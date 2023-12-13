package vn.softdreams.easypos.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A CardPolicy.
 */
@Entity
@Table(name = "card_policy")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CardPolicy extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "com_id")
    private Integer comId;

    @Column(name = "name")
    private String name;

    @Column(name = "normalized_name")
    private String normalizedName;

    @Column(name = "note")
    private String note;

    @Column(name = "gen_description")
    private String genDescription;

    @Column(name = "upgrade_type")
    private Integer upgradeType;

    @Column(name = "status")
    private Integer status;

    @Column(name = "conditions")
    private String conditions;

    @Column(name = "start_time")
    private ZonedDateTime startTime;

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

    public CardPolicy comId(Integer comId) {
        this.setComId(comId);
        return this;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public String getName() {
        return this.name;
    }

    public CardPolicy name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNormalizedName() {
        return this.normalizedName;
    }

    public CardPolicy normalizedName(String normalizedName) {
        this.setNormalizedName(normalizedName);
        return this;
    }

    public void setNormalizedName(String normalizedName) {
        this.normalizedName = normalizedName;
    }

    public String getNote() {
        return this.note;
    }

    public CardPolicy note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getGenDescription() {
        return this.genDescription;
    }

    public CardPolicy genDescription(String genDescription) {
        this.setGenDescription(genDescription);
        return this;
    }

    public void setGenDescription(String genDescription) {
        this.genDescription = genDescription;
    }

    public Integer getUpgradeType() {
        return this.upgradeType;
    }

    public CardPolicy upgradeType(Integer upgradeType) {
        this.setUpgradeType(upgradeType);
        return this;
    }

    public void setUpgradeType(Integer upgradeType) {
        this.upgradeType = upgradeType;
    }

    public Integer getStatus() {
        return this.status;
    }

    public CardPolicy status(Integer status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getConditions() {
        return this.conditions;
    }

    public CardPolicy conditions(String conditions) {
        this.setConditions(conditions);
        return this;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public ZonedDateTime getStartTime() {
        return this.startTime;
    }

    public CardPolicy startTime(ZonedDateTime startTime) {
        this.setStartTime(startTime);
        return this;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CardPolicy)) {
            return false;
        }
        return id != null && id.equals(((CardPolicy) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CardPolicy{" +
            "id=" + getId() +
            ", comId=" + getComId() +
            ", name='" + getName() + "'" +
            ", normalizedName='" + getNormalizedName() + "'" +
            ", note='" + getNote() + "'" +
            ", genDescription='" + getGenDescription() + "'" +
            ", upgradeType=" + getUpgradeType() +
            ", status=" + getStatus() +
            ", conditions=" + getConditions() +
            ", startTime='" + getStartTime() + "'" +
            "}";
    }
}
