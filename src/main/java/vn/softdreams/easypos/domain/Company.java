package vn.softdreams.easypos.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.NotAudited;
import vn.softdreams.easypos.dto.company.CompanyResult;
import vn.softdreams.easypos.dto.company.CompanyResultItem;
import vn.softdreams.easypos.dto.user.CompanyResponse;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A Company.
 */
@JsonIgnoreProperties(value = { "new" })
@Entity
@DynamicUpdate
@Table(name = "company")
@SuppressWarnings("common-java:DuplicatedBlocks")
@SqlResultSetMappings(
    {
        @SqlResultSetMapping(
            name = "CompanyResponseDTO",
            classes = {
                @ConstructorResult(
                    targetClass = CompanyResponse.class,
                    columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "taxCode", type = String.class),
                        @ColumnResult(name = "ownerName", type = String.class),
                        @ColumnResult(name = "phoneNo", type = String.class),
                        @ColumnResult(name = "address", type = String.class),
                        @ColumnResult(name = "taxAuthCodePrefix", type = String.class),
                    }
                ),
            }
        ),
        @SqlResultSetMapping(
            name = "CompanyForAdminResult",
            classes = {
                @ConstructorResult(
                    targetClass = CompanyResult.class,
                    columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "phone", type = String.class),
                        @ColumnResult(name = "address", type = String.class),
                        @ColumnResult(name = "companyOwnerId", type = Integer.class),
                        @ColumnResult(name = "companyOwnerName", type = String.class),
                        @ColumnResult(name = "companyOwnerAddress", type = String.class),
                        @ColumnResult(name = "companyOwnerTaxMachineCode", type = String.class),
                        @ColumnResult(name = "taxCode", type = String.class),
                        @ColumnResult(name = "countConfig", type = Integer.class),
                    }
                ),
            }
        ),
        @SqlResultSetMapping(
            name = "CompanyResult",
            classes = {
                @ConstructorResult(
                    targetClass = CompanyResult.class,
                    columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "phone", type = String.class),
                        @ColumnResult(name = "address", type = String.class),
                        @ColumnResult(name = "companyOwnerId", type = Integer.class),
                        @ColumnResult(name = "companyOwnerName", type = String.class),
                        @ColumnResult(name = "companyOwnerAddress", type = String.class),
                        @ColumnResult(name = "companyOwnerTaxMachineCode", type = String.class),
                        @ColumnResult(name = "taxCode", type = String.class),
                        @ColumnResult(name = "service", type = String.class),
                    }
                ),
            }
        ),
        @SqlResultSetMapping(
            name = "CompanyDetailResult",
            classes = {
                @ConstructorResult(
                    targetClass = CompanyResult.class,
                    columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "phone", type = String.class),
                        @ColumnResult(name = "address", type = String.class),
                        @ColumnResult(name = "businessId", type = Integer.class),
                        @ColumnResult(name = "businessName", type = String.class),
                        @ColumnResult(name = "companyOwnerId", type = Integer.class),
                        @ColumnResult(name = "companyOwnerName", type = String.class),
                        @ColumnResult(name = "companyOwnerAddress", type = String.class),
                        @ColumnResult(name = "companyOwnerTaxMachineCode", type = String.class),
                        @ColumnResult(name = "taxCode", type = String.class),
                        @ColumnResult(name = "description", type = String.class),
                    }
                ),
            }
        ),
        @SqlResultSetMapping(
            name = "CompanyResultItemDTO",
            classes = {
                @ConstructorResult(
                    targetClass = CompanyResultItem.class,
                    columns = { @ColumnResult(name = "value", type = Integer.class), @ColumnResult(name = "name", type = String.class) }
                ),
            }
        ),
    }
)
public class Company extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "com_owner_id")
    private CompanyOwner companyOwner;

    @Column(name = "business_type")
    private Integer businessType;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "description")
    private String description;

    @Column(name = "is_parent")
    private Boolean isParent;

    @Column(name = "eb_id")
    private Integer ebId;

    @Column(name = "business_id")
    private Integer businessId;

    @NotAudited
    @Column(name = "normalized_name")
    private String normalizedName;

    //    Tên viết tắt các hệ thống tích hợp
    //    NGP: Ngô gia phát
    @NotAudited
    @Column(name = "service")
    private String service;

    public Company() {}

    // constructor for register user
    public Company(CompanyOwner companyOwner, String name, String address, Boolean isParent, String normalizedName) {
        this.companyOwner = companyOwner;
        this.name = name;
        this.address = address;
        this.isParent = isParent;
        this.normalizedName = normalizedName;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CompanyOwner getCompanyOwner() {
        return companyOwner;
    }

    public void setCompanyOwner(CompanyOwner companyOwner) {
        this.companyOwner = companyOwner;
    }

    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getParent() {
        return isParent;
    }

    public void setParent(Boolean parent) {
        isParent = parent;
    }

    public Integer getEbId() {
        return ebId;
    }

    public void setEbId(Integer ebId) {
        this.ebId = ebId;
    }

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public String getNormalizedName() {
        return normalizedName;
    }

    public void setNormalizedName(String normalizedName) {
        this.normalizedName = normalizedName;
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Company{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", phone='" + getPhone() + "'" +
            ", address='" + getAddress() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
