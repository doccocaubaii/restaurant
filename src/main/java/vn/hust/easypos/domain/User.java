package vn.hust.easypos.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

/**
 * A user.
 */
@JsonIgnoreProperties(value = { "new" })
@Entity
@Data
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

//    @ManyToMany
//    @JoinTable(
//        name = "company_user",
//        joinColumns = { @JoinColumn(name = "user_id", columnDefinition = "id") },
//        inverseJoinColumns = { @JoinColumn(name = "company_id", referencedColumnName = "id") }
//    )
//    private List<Company> companies = new ArrayList<>();

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
}
