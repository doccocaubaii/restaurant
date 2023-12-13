package vn.softdreams.easypos.domain;

import org.hibernate.annotations.DynamicUpdate;
import vn.softdreams.easypos.dto.config.ConfigOwnerResult;
import vn.softdreams.easypos.dto.config.ConfigResult;
import vn.softdreams.easypos.dto.config.ConfigStatusResult;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A Config.
 */
@Entity
@DynamicUpdate
@Table(name = "config")
@SuppressWarnings("common-java:DuplicatedBlocks")
@SqlResultSetMappings(
    {
        @SqlResultSetMapping(
            name = "ConfigResult",
            classes = {
                @ConstructorResult(
                    targetClass = ConfigResult.class,
                    columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "code", type = String.class),
                        @ColumnResult(name = "value", type = String.class),
                        @ColumnResult(name = "description", type = String.class),
                        @ColumnResult(name = "companyName", type = String.class),
                        @ColumnResult(name = "createTime", type = ZonedDateTime.class),
                        @ColumnResult(name = "updateTime", type = ZonedDateTime.class),
                    }
                ),
            }
        ),
        @SqlResultSetMapping(
            name = "ConfigOwnerResult",
            classes = {
                @ConstructorResult(
                    targetClass = ConfigOwnerResult.class,
                    columns = {
                        @ColumnResult(name = "code", type = String.class),
                        @ColumnResult(name = "value", type = String.class),
                        @ColumnResult(name = "description", type = String.class),
                    }
                ),
            }
        ),
        @SqlResultSetMapping(
            name = "ConfigStatusResult",
            classes = {
                @ConstructorResult(
                    targetClass = ConfigStatusResult.class,
                    columns = {
                        @ColumnResult(name = "companyId", type = Integer.class), @ColumnResult(name = "status", type = Integer.class),
                    }
                ),
            }
        ),
    }
)
public class Config extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "company_id")
    private Integer companyId;

    @Size(max = 50)
    @Column(name = "code")
    private String code;

    @Column(name = "value")
    private String value;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Config() {}

    public Config(Integer companyId, String code, String value, String description) {
        this.companyId = companyId;
        this.code = code;
        this.value = value;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCode() {
        return this.code;
    }

    public Config code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return this.value;
    }

    public Config value(String value) {
        this.setValue(value);
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return this.description;
    }

    public Config description(String description) {
        this.setDescription(description);
        return this;
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
        if (!(o instanceof Config)) {
            return false;
        }
        return id != null && id.equals(((Config) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Config{" +
            "id=" + getId() +
            ", companyId=" + getCompanyId() +
            ", code='" + getCode() + "'" +
            ", value='" + getValue() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
