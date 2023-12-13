package vn.softdreams.easypos.domain;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A ToppingGroup.
 */
@Entity
@Table(name = "topping_group")
@DynamicUpdate
public class ToppingGroup extends AbstractAuditingEntity<String> implements Serializable {

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

    @Column(name = "required_optional")
    private Boolean requiredOptional;

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

    public ToppingGroup comId(Integer comId) {
        this.setComId(comId);
        return this;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public String getName() {
        return this.name;
    }

    public ToppingGroup name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNormalizedName() {
        return this.normalizedName;
    }

    public ToppingGroup normalizedName(String normalizedName) {
        this.setNormalizedName(normalizedName);
        return this;
    }

    public void setNormalizedName(String normalizedName) {
        this.normalizedName = normalizedName;
    }

    public Boolean getRequiredOptional() {
        return this.requiredOptional;
    }

    public ToppingGroup requiredOptional(Boolean requiredOptional) {
        this.setRequiredOptional(requiredOptional);
        return this;
    }

    public void setRequiredOptional(Boolean requiredOptional) {
        this.requiredOptional = requiredOptional;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ToppingGroup)) {
            return false;
        }
        return id != null && id.equals(((ToppingGroup) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ToppingGroup{" +
            "id=" + getId() +
            ", comId=" + getComId() +
            ", name='" + getName() + "'" +
            ", normalizedName='" + getNormalizedName() + "'" +
            ", requiredOptional='" + getRequiredOptional() + "'" +
            "}";
    }
}
