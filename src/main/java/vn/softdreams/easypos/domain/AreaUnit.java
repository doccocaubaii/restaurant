package vn.softdreams.easypos.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.NotAudited;
import vn.softdreams.easypos.dto.areaUnit.AreaUnitDetailResponse;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

@JsonIgnoreProperties("new")
@Entity
@DynamicUpdate
@Table(name = "area_unit")
@SqlResultSetMappings(
    {
        @SqlResultSetMapping(
            name = "AreaUnitResponse",
            classes = {
                @ConstructorResult(
                    targetClass = AreaUnitDetailResponse.class,
                    columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "comId", type = Integer.class),
                        @ColumnResult(name = "areaId", type = Integer.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "createTime", type = ZonedDateTime.class),
                        @ColumnResult(name = "updateTime", type = ZonedDateTime.class),
                    }
                ),
            }
        ),
    }
)
public class AreaUnit extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "com_id")
    private Integer comId;

    @Column(name = "area_id")
    private Integer areaId;

    @Column(name = "name")
    private String name;

    @NotAudited
    @Column(name = "normalized_name")
    private String normalizedName;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id", insertable = false, updatable = false)
    private Area area;

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

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public String getNormalizedName() {
        return normalizedName;
    }

    public void setNormalizedName(String normalizedName) {
        this.normalizedName = normalizedName;
    }
}
