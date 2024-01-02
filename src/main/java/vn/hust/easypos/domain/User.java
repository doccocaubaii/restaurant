package vn.hust.easypos.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.DynamicUpdate;

/**
 * A user.
 */
@JsonIgnoreProperties(value = { "new" })
@Entity
@DynamicUpdate
@Table(name = "ep_user")
public class User extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Size(max = 100)
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Size(max = 100)
    @Column(name = "full_name")
    private String fullName;

    @Email
    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Size(max = 512)
    @Column(name = "address")
    private String address;

    @Column(name = "status")
    private Integer status;

    @Column(name = "normalized_name")
    private String normalizedName;

    @ManyToMany
    @JoinTable(
        name = "company_user",
        joinColumns = { @JoinColumn(name = "user_id", columnDefinition = "id") },
        inverseJoinColumns = { @JoinColumn(name = "company_id", referencedColumnName = "id") }
    )
    private List<Company> companies = new ArrayList<>();

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private Integer companyId;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private String taxCode;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private Set<String> authorities = new HashSet<>();

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public List<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(List<Company> companies) {
        this.companies = companies;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Set<String> getAuthorities() {
        return this.authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }

    public String getNormalizedName() {
        return normalizedName;
    }

    public void setNormalizedName(String normalizedName) {
        this.normalizedName = normalizedName;
    }
}
