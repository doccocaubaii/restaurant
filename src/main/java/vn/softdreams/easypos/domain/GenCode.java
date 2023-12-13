package vn.softdreams.easypos.domain;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * A GenCode.
 */
@Entity
@DynamicUpdate
@Table(name = "gen_code")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GenCode extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "company_id")
    private Integer companyId;

    @Size(max = 50)
    @Column(name = "type_name")
    private String typeName;

    @Column(name = "current_value")
    private Integer currentValue;

    @Column(name = "length")
    private Integer length;

    @Size(max = 50)
    @Column(name = "prefix")
    private String prefix;

    @Size(max = 50)
    @Column(name = "suffix")
    private String suffix;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCompanyId() {
        return this.companyId;
    }

    public GenCode companyId(Integer companyId) {
        this.setCompanyId(companyId);
        return this;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public GenCode typeName(String typeName) {
        this.setTypeName(typeName);
        return this;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getCurrentValue() {
        return this.currentValue;
    }

    public GenCode currentValue(Integer currentValue) {
        this.setCurrentValue(currentValue);
        return this;
    }

    public void setCurrentValue(Integer currentValue) {
        this.currentValue = currentValue;
    }

    public Integer getLength() {
        return this.length;
    }

    public GenCode length(Integer length) {
        this.setLength(length);
        return this;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public GenCode prefix(String prefix) {
        this.setPrefix(prefix);
        return this;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return this.suffix;
    }

    public GenCode suffix(String suffix) {
        this.setSuffix(suffix);
        return this;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GenCode)) {
            return false;
        }
        return id != null && id.equals(((GenCode) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GenCode{" +
            "id=" + getId() +
            ", companyId=" + getCompanyId() +
            ", typeName='" + getTypeName() + "'" +
            ", currentValue=" + getCurrentValue() +
            ", length=" + getLength() +
            ", prefix='" + getPrefix() + "'" +
            ", suffix='" + getSuffix() + "'" +
            "}";
    }
}
