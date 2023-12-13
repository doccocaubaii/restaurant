package vn.softdreams.easypos.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.DynamicUpdate;
import vn.softdreams.easypos.dto.businessType.GetAllTransactionsResponse;

import javax.persistence.*;
import java.io.Serializable;

@JsonIgnoreProperties(value = { "new" })
@Entity
@DynamicUpdate
@Table(name = "business_type")
@SqlResultSetMappings(
    {
        @SqlResultSetMapping(
            name = "GetAllTransactionsResponse",
            classes = {
                @ConstructorResult(
                    targetClass = GetAllTransactionsResponse.class,
                    columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "comId", type = Integer.class),
                        @ColumnResult(name = "businessTypeCode", type = String.class),
                        @ColumnResult(name = "businessTypeName", type = String.class),
                        @ColumnResult(name = "type", type = String.class),
                    }
                ),
            }
        ),
    }
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BusinessType extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "com_id")
    private Integer comId;

    @Column(name = "business_type_code")
    private String businessTypeCode;

    @Column(name = "business_type_name")
    private String businessTypeName;

    @Column(name = "type")
    private String type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public String getBusinessTypeCode() {
        return businessTypeCode;
    }

    public void setBusinessTypeCode(String businessTypeCode) {
        this.businessTypeCode = businessTypeCode;
    }

    public String getBusinessTypeName() {
        return businessTypeName;
    }

    public void setBusinessTypeName(String businessTypeName) {
        this.businessTypeName = businessTypeName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
