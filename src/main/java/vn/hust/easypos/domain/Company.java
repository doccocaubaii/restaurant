package vn.hust.easypos.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;

import jakarta.persistence.*;
import org.hibernate.annotations.DynamicUpdate;
import vn.hust.easypos.service.dto.company.CompanyResult;

/**
 * A Company.
 */
@JsonIgnoreProperties(value = { "new" })
@Entity
@DynamicUpdate
@Table(name = "company")
@SqlResultSetMappings(
    {
        @SqlResultSetMapping(
            name = "CompanyResult",
            classes = {
                @ConstructorResult(
                    targetClass = CompanyResult.class,
                    columns = { @ColumnResult(name = "id", type = Integer.class), @ColumnResult(name = "name", type = String.class) }
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

    @Column(name = "name")
    private String name;

    public Company() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
