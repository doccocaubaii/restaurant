package vn.softdreams.easypos.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.DynamicUpdate;
import vn.softdreams.easypos.config.Constants;
import vn.softdreams.easypos.dto.companyOwner.OwnerResult;
import vn.softdreams.easypos.dto.companyUser.CompanyOwnerResponse;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * A CompanyOwner.
 */
@JsonIgnoreProperties(value = { "new" })
@Entity
@DynamicUpdate
@Table(name = "company_owner")
@SuppressWarnings("common-java:DuplicatedBlocks")
@SqlResultSetMappings(
    {
        @SqlResultSetMapping(
            name = "CompanyOwnerResponse",
            classes = {
                @ConstructorResult(
                    targetClass = CompanyOwnerResponse.class,
                    columns = {
                        @ColumnResult(name = "comId", type = Integer.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "taxCode", type = String.class),
                        @ColumnResult(name = "taxRegisterCode", type = String.class),
                        @ColumnResult(name = "taxRegisterTime", type = ZonedDateTime.class),
                        @ColumnResult(name = "taxRegisterMessage", type = String.class),
                    }
                ),
            }
        ),
        @SqlResultSetMapping(
            name = "OwnerResult",
            classes = {
                @ConstructorResult(
                    targetClass = OwnerResult.class,
                    columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "address", type = String.class),
                        @ColumnResult(name = "taxCode", type = String.class),
                        @ColumnResult(name = "ownerName", type = String.class),
                        @ColumnResult(name = "createTime", type = ZonedDateTime.class),
                        @ColumnResult(name = "updateTime", type = ZonedDateTime.class),
                    }
                ),
            }
        ),
    }
)
public class CompanyOwner extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Size(max = 512)
    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "owner_name")
    private String ownerName;

    @Column(name = "owner_id")
    private Integer ownerId;

    @Column(name = "ikey")
    private String ikey;

    @Column(name = "tax_machine_code")
    private String taxMachineCode;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    @Column(name = "tax_register_time")
    private ZonedDateTime taxRegisterTime;

    @Column(name = "tax_register_message")
    private String taxRegisterMessage;

    @Column(name = "tax_code")
    private String taxCode;

    @JsonManagedReference
    @OneToMany(mappedBy = "companyOwner", fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Company> companies = new ArrayList<>();

    public CompanyOwner() {}

    // constructor for register user
    public CompanyOwner(String name, String address, String ownerName, Integer ownerId, String taxCode) {
        this.name = name;
        this.address = address;
        this.ownerName = ownerName;
        this.ownerId = ownerId;
        this.taxCode = taxCode;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return this.address;
    }

    public CompanyOwner address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOwnerName() {
        return this.ownerName;
    }

    public CompanyOwner ownerName(String ownerName) {
        this.setOwnerName(ownerName);
        return this;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getIkey() {
        return this.ikey;
    }

    public CompanyOwner ikey(String ikey) {
        this.setIkey(ikey);
        return this;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public void setIkey(String ikey) {
        this.ikey = ikey;
    }

    public String getTaxMachineCode() {
        return taxMachineCode;
    }

    public void setTaxMachineCode(String taxMachineCode) {
        this.taxMachineCode = taxMachineCode;
    }

    public ZonedDateTime getTaxRegisterTime() {
        return taxRegisterTime;
    }

    public void setTaxRegisterTime(ZonedDateTime taxRegisterTime) {
        this.taxRegisterTime = taxRegisterTime;
    }

    public String getTaxRegisterMessage() {
        return taxRegisterMessage;
    }

    public void setTaxRegisterMessage(String taxRegisterMessage) {
        this.taxRegisterMessage = taxRegisterMessage;
    }

    public List<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompanyOwner)) {
            return false;
        }
        return id != null && id.equals(((CompanyOwner) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompanyOwner{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", address='" + getAddress() + "'" +
            ", ownerName='" + getOwnerName() + "'" +
            ", ownerId='" + getOwnerId() + "'" +
            ", ikey='" + getIkey() + "'" +
            ", tax_machine_code='" + getTaxMachineCode() + "'" +
            ", tax_register_time='" + getTaxRegisterTime() + "'" +
            ", tax_register_message='" + getTaxRegisterMessage() + "'" +
            "}";
    }
}
