package vn.softdreams.easypos.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A RolePermission.
 */
@JsonIgnoreProperties(value = { "new" })
@Entity
@DynamicUpdate
@Table(name = "role_permission")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RolePermission extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "role_code")
    private String roleCode;

    @Column(name = "permission_id")
    private Integer permissionId;

    @Column(name = "permission_code")
    private String permissionCode;

    @Column(name = "permission_parent_code")
    private String permissionParentCode;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }

    public String getRoleCode() {
        return this.roleCode;
    }

    public RolePermission roleCode(String roleCode) {
        this.setRoleCode(roleCode);
        return this;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getPermissionCode() {
        return this.permissionCode;
    }

    public RolePermission permissionCode(String permissionCode) {
        this.setPermissionCode(permissionCode);
        return this;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }

    public String getPermissionParentCode() {
        return this.permissionParentCode;
    }

    public RolePermission permissionParentCode(String permissionParentCode) {
        this.setPermissionParentCode(permissionParentCode);
        return this;
    }

    public void setPermissionParentCode(String permissionParentCode) {
        this.permissionParentCode = permissionParentCode;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RolePermission)) {
            return false;
        }
        return id != null && id.equals(((RolePermission) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RolePermission{" +
            "id=" + getId() +
            ", roleId='" + getRoleId() + "'" +
            ", roleCode='" + getRoleCode() + "'" +
            ", permissionCode='" + getPermissionCode() + "'" +
            ", permissionParentCode='" + getPermissionParentCode() + "'" +
            "}";
    }
}
