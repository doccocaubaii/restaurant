package vn.softdreams.easypos.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.DynamicUpdate;
import vn.softdreams.easypos.dto.companyUser.CompanyUserResponse;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A CompanyUser.
 */
@JsonIgnoreProperties(value = { "new" })
@Entity
@DynamicUpdate
@Table(name = "company_user")
@SuppressWarnings("common-java:DuplicatedBlocks")
@SqlResultSetMappings(
    {
        @SqlResultSetMapping(
            name = "CompanyUserResponse",
            classes = {
                @ConstructorResult(
                    targetClass = CompanyUserResponse.class,
                    columns = {
                        @ColumnResult(name = "id", type = String.class),
                        @ColumnResult(name = "companyId", type = String.class),
                        @ColumnResult(name = "companyName", type = String.class),
                        @ColumnResult(name = "userId", type = String.class),
                        @ColumnResult(name = "username", type = String.class),
                    }
                ),
            }
        ),
    }
)
public class CompanyUser extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "company_id")
    private Integer companyId;

    @Column(name = "user_id")
    private Integer userId;

    public CompanyUser() {}

    public CompanyUser(Integer companyId, Integer userId) {
        this.companyId = companyId;
        this.userId = userId;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompanyUser)) {
            return false;
        }
        return id != null && id.equals(((CompanyUser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompanyUser{" +
            "id=" + getId() +
            ", companyId='" + getCompanyId() + "'" +
            ", userId='" + getUserId() + "'" +
            "}";
    }
}
