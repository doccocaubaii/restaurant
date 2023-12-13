package vn.softdreams.easypos.domain;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.NotAudited;
import vn.softdreams.easypos.dto.ownerDevice.OwnerDeviceResult;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A OwnerDevice.
 */
@Entity
@DynamicUpdate
@Table(name = "owner_device")
@SuppressWarnings("common-java:DuplicatedBlocks")
@SqlResultSetMappings(
    {
        @SqlResultSetMapping(
            name = "OwnerDeviceResult",
            classes = {
                @ConstructorResult(
                    targetClass = OwnerDeviceResult.class,
                    columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "ownerId", type = Integer.class),
                        @ColumnResult(name = "ownerName", type = String.class),
                        @ColumnResult(name = "deviceCode", type = String.class),
                        @ColumnResult(name = "createTime", type = ZonedDateTime.class),
                        @ColumnResult(name = "updateTime", type = ZonedDateTime.class),
                    }
                ),
            }
        ),
    }
)
public class OwnerDevice extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "owner_id")
    private Integer ownerId;

    @Column(name = "name")
    private String name;

    @Column(name = "device_code")
    private String deviceCode;

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

    public Integer getOwnerId() {
        return this.ownerId;
    }

    public OwnerDevice ownerId(Integer ownerId) {
        this.setOwnerId(ownerId);
        return this;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return this.name;
    }

    public OwnerDevice name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeviceCode() {
        return this.deviceCode;
    }

    public OwnerDevice deviceCode(String deviceCode) {
        this.setDeviceCode(deviceCode);
        return this;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
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
        if (!(o instanceof OwnerDevice)) {
            return false;
        }
        return id != null && id.equals(((OwnerDevice) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OwnerDevice{" +
            "id=" + getId() +
            ", ownerId=" + getOwnerId() +
            ", name='" + getName() + "'" +
            ", deviceCode='" + getDeviceCode() + "'" +
            "}";
    }
}
