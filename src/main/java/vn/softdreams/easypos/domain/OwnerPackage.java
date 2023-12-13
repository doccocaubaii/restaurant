package vn.softdreams.easypos.domain;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A OwnerPackage.
 */
@Entity
@DynamicUpdate
@Table(name = "owner_package")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OwnerPackage extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "owned_id")
    private Integer ownedId;

    @Column(name = "package_id")
    private Integer packageId;

    @Column(name = "status")
    private Integer status;

    @Column(name = "start_date")
    private ZonedDateTime startDate;

    @Column(name = "end_date")
    private ZonedDateTime endDate;

    @Column(name = "pack_count")
    private Integer packCount;

    @Column(name = "voucher_using")
    private Integer voucherUsing;

    public OwnerPackage() {}

    // constructor for register user
    public OwnerPackage(
        Integer ownedId,
        Integer packageId,
        Integer status,
        ZonedDateTime startDate,
        ZonedDateTime endDate,
        Integer packCount
    ) {
        this.ownedId = ownedId;
        this.packageId = packageId;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.packCount = packCount;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOwnedId() {
        return this.ownedId;
    }

    public OwnerPackage ownedId(Integer ownedId) {
        this.setOwnedId(ownedId);
        return this;
    }

    public void setOwnedId(Integer ownedId) {
        this.ownedId = ownedId;
    }

    public Integer getPackageId() {
        return this.packageId;
    }

    public OwnerPackage packageId(Integer packageId) {
        this.setPackageId(packageId);
        return this;
    }

    public void setPackageId(Integer packageId) {
        this.packageId = packageId;
    }

    public Integer getStatus() {
        return this.status;
    }

    public OwnerPackage status(Integer status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public ZonedDateTime getStartDate() {
        return this.startDate;
    }

    public OwnerPackage startDate(ZonedDateTime startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return this.endDate;
    }

    public OwnerPackage endDate(ZonedDateTime endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public Integer getPackCount() {
        return this.packCount;
    }

    public OwnerPackage packCount(Integer packCount) {
        this.setPackCount(packCount);
        return this;
    }

    public void setPackCount(Integer packCount) {
        this.packCount = packCount;
    }

    public Integer getVoucherUsing() {
        return this.voucherUsing;
    }

    public OwnerPackage voucherUsing(Integer voucherUsing) {
        this.setVoucherUsing(voucherUsing);
        return this;
    }

    public void setVoucherUsing(Integer voucherUsing) {
        this.voucherUsing = voucherUsing;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OwnerPackage)) {
            return false;
        }
        return id != null && id.equals(((OwnerPackage) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OwnerPackage{" +
            "id=" + getId() +
            ", ownedId=" + getOwnedId() +
            ", packageId=" + getPackageId() +
            ", status=" + getStatus() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", packCount=" + getPackCount() +
            ", voucherUsing=" + getVoucherUsing() +
            "}";
    }
}
