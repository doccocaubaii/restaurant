package vn.hust.restaurant.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import vn.hust.restaurant.service.dto.StaffResponse;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A user.
 */
@JsonIgnoreProperties(value = {"new"})
@Entity
@Data
@DynamicUpdate
@Table(name = "ep_user")
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "StaffResponse",
        classes = {
            @ConstructorResult(
                targetClass = StaffResponse.class,
                columns = {
                    @ColumnResult(name = "id", type = Integer.class),
                    @ColumnResult(name = "email", type = String.class),
                    @ColumnResult(name = "full_name", type = String.class),
                    @ColumnResult(name = "username", type = String.class),
                    @ColumnResult(name = "phone_number", type = String.class),
                    @ColumnResult(name = "status", type = Integer.class),
                }
            ),
        }
    ),
})
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

    @Column(name = "company_id")
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
