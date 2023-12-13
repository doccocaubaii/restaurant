package vn.softdreams.easypos.domain;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A EPPackage.
 */
@Entity
@DynamicUpdate
@Table(name = "package")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EPPackage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "package_code")
    private String packageCode;

    @Column(name = "package_name")
    private String packageName;

    @Column(name = "description")
    private String description;

    @Column(name = "limit_company")
    private Integer limitCompany;

    @Column(name = "limit_user")
    private Integer limitUser;

    @Column(name = "limit_voucher")
    private Integer limitVoucher;

    @Column(name = "time")
    private Integer time;

    @Column(name = "type")
    private String type;

    @Column(name = "status")
    private Integer status;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPackageCode() {
        return this.packageCode;
    }

    public EPPackage packageCode(String packageCode) {
        this.setPackageCode(packageCode);
        return this;
    }

    public void setPackageCode(String packageCode) {
        this.packageCode = packageCode;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public EPPackage packageName(String packageName) {
        this.setPackageName(packageName);
        return this;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getDescription() {
        return this.description;
    }

    public EPPackage description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getLimitCompany() {
        return this.limitCompany;
    }

    public EPPackage limitCompany(Integer limitCompany) {
        this.setLimitCompany(limitCompany);
        return this;
    }

    public void setLimitCompany(Integer limitCompany) {
        this.limitCompany = limitCompany;
    }

    public Integer getLimitUser() {
        return this.limitUser;
    }

    public EPPackage limitUser(Integer limitUser) {
        this.setLimitUser(limitUser);
        return this;
    }

    public void setLimitUser(Integer limitUser) {
        this.limitUser = limitUser;
    }

    public Integer getLimitVoucher() {
        return this.limitVoucher;
    }

    public EPPackage limitVoucher(Integer limitVoucher) {
        this.setLimitVoucher(limitVoucher);
        return this;
    }

    public void setLimitVoucher(Integer limitVoucher) {
        this.limitVoucher = limitVoucher;
    }

    public Integer getTime() {
        return this.time;
    }

    public EPPackage time(Integer time) {
        this.setTime(time);
        return this;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public String getType() {
        return this.type;
    }

    public EPPackage type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getStatus() {
        return this.status;
    }

    public EPPackage status(Integer status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EPPackage)) {
            return false;
        }
        return id != null && id.equals(((EPPackage) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EPPackage{" +
            "id=" + getId() +
            ", packageCode='" + getPackageCode() + "'" +
            ", packageName='" + getPackageName() + "'" +
            ", description='" + getDescription() + "'" +
            ", limitCompany=" + getLimitCompany() +
            ", limitUser=" + getLimitUser() +
            ", limitVoucher=" + getLimitVoucher() +
            ", time=" + getTime() +
            ", type=" + getType() +
            ", status=" + getStatus() +
            "}";
    }
}
