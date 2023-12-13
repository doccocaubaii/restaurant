package vn.softdreams.easypos.domain;

import org.hibernate.annotations.DynamicUpdate;
import vn.softdreams.easypos.dto.printSetting.PrintSettingItemResponse;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "print_setting")
@DynamicUpdate
@SqlResultSetMappings(
    {
        @SqlResultSetMapping(
            name = "PrintSettingItemResponse",
            classes = {
                @ConstructorResult(
                    targetClass = PrintSettingItemResponse.class,
                    columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "comId", type = Integer.class),
                        @ColumnResult(name = "printName", type = String.class),
                        @ColumnResult(name = "ipAddress", type = String.class),
                        @ColumnResult(name = "type", type = Integer.class),
                        @ColumnResult(name = "pageSize", type = String.class),
                        @ColumnResult(name = "processingAreaId", type = Integer.class),
                        @ColumnResult(name = "typeTemplate", type = Integer.class),
                    }
                ),
            }
        ),
    }
)
public class PrintSetting extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "com_id")
    private Integer comId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "print_name")
    private String printName;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "type")
    private Integer type;

    @Column(name = "page_size")
    private String pageSize;

    @Column(name = "processing_area_id")
    private Integer processingAreaId;

    @Column(name = "type_template")
    private Integer typeTemplate;

    @Column(name = "normalized_name")
    private String normalizedName;

    @Column(name = "print_template_id")
    private Integer printTemplateId;

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPrintName() {
        return printName;
    }

    public void setPrintName(String printName) {
        this.printName = printName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getProcessingAreaId() {
        return processingAreaId;
    }

    public void setProcessingAreaId(Integer processingAreaId) {
        this.processingAreaId = processingAreaId;
    }

    public Integer getTypeTemplate() {
        return typeTemplate;
    }

    public void setTypeTemplate(Integer typeTemplate) {
        this.typeTemplate = typeTemplate;
    }

    public String getNormalizedName() {
        return normalizedName;
    }

    public void setNormalizedName(String normalizedName) {
        this.normalizedName = normalizedName;
    }

    public Integer getPrintTemplateId() {
        return printTemplateId;
    }

    public void setPrintTemplateId(Integer printTemplateId) {
        this.printTemplateId = printTemplateId;
    }
}
