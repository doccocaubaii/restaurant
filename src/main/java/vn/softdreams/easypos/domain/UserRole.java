package vn.softdreams.easypos.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A UserRole.
 */
@JsonIgnoreProperties(value = { "new" })
@Entity
@DynamicUpdate
@Table(name = "user_role")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserRole extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "com_id")
    private Integer comId;

    public UserRole() {}

    public UserRole(Integer userId, Integer roleId, Integer comId) {
        this.userId = userId;
        this.roleId = roleId;
        this.comId = comId;
    }

    public UserRole(Integer id, Integer userId, Integer roleId, Integer comId) {
        this.id = id;
        this.userId = userId;
        this.roleId = roleId;
        this.comId = comId;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserRole)) {
            return false;
        }
        return id != null && id.equals(((UserRole) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserRole{" +
            "id=" + getId() +
            ", userId='" + getUserId() + "'" +
            ", roleId='" + getRoleId() + "'" +
            ", comId='" + getComId() + "'" +
            "}";
    }
}
