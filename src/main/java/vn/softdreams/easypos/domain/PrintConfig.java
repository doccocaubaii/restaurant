package vn.softdreams.easypos.domain;

import org.hibernate.annotations.DynamicUpdate;
import vn.softdreams.easypos.dto.config.PrintConfigCompany;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * A PrintConfig.
 */
@Entity
@DynamicUpdate
@Table(name = "print_config")
@SuppressWarnings("common-java:DuplicatedBlocks")
@SqlResultSetMappings(
    {
        @SqlResultSetMapping(
            name = "PrintConfigCompanyResponse",
            classes = {
                @ConstructorResult(
                    //                   Sử dụng chung dto với Request vì data giống nhau
                    targetClass = PrintConfigCompany.class,
                    columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "comId", type = Integer.class),
                        @ColumnResult(name = "title", type = String.class),
                        @ColumnResult(name = "type", type = Integer.class),
                        @ColumnResult(name = "code", type = String.class),
                        @ColumnResult(name = "content", type = String.class),
                        @ColumnResult(name = "fontSize", type = Integer.class),
                        @ColumnResult(name = "alignText", type = Integer.class),
                        @ColumnResult(name = "isBold", type = Integer.class),
                        @ColumnResult(name = "isPrint", type = Integer.class),
                        @ColumnResult(name = "isHeader", type = Integer.class),
                        @ColumnResult(name = "isEditable", type = Integer.class),
                        @ColumnResult(name = "isBody", type = Integer.class),
                        @ColumnResult(name = "position", type = Integer.class),
                    }
                ),
            }
        ),
    }
)
public class PrintConfig extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "com_id")
    private Integer comId;

    @Size(max = 26)
    @Column(name = "code")
    private String code;

    @Size(max = 255)
    @Column(name = "title")
    private String title;

    @Size(max = 255)
    @Column(name = "name")
    private String name;

    @Column(name = "font_size")
    private Integer fontSize;

    @Column(name = "align_text")
    private Integer alignText;

    @Column(name = "is_bold")
    private Boolean isBold;

    @Column(name = "is_print")
    private Boolean isPrint;

    @Column(name = "is_header")
    private Boolean isHeader;

    @Column(name = "is_editable")
    private Boolean isEditable;

    @Column(name = "version")
    private Integer version;

    @Column(name = "is_body")
    private Boolean isBody;

    @Column(name = "position")
    private Integer position;

    @Column(name = "type")
    private Integer type;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Integer getId() {
        return this.id;
    }

    public PrintConfig id(Integer id) {
        this.setId(id);
        return this;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getComId() {
        return this.comId;
    }

    public PrintConfig comId(Integer comId) {
        this.setComId(comId);
        return this;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public String getCode() {
        return this.code;
    }

    public PrintConfig code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return this.title;
    }

    public PrintConfig title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return this.name;
    }

    public PrintConfig name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getFontSize() {
        return this.fontSize;
    }

    public PrintConfig fontSize(Integer fontSize) {
        this.setFontSize(fontSize);
        return this;
    }

    public void setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
    }

    public Integer getAlignText() {
        return this.alignText;
    }

    public PrintConfig alignText(Integer alignText) {
        this.setAlignText(alignText);
        return this;
    }

    public void setAlignText(Integer alignText) {
        this.alignText = alignText;
    }

    public Boolean getIsBold() {
        return this.isBold;
    }

    public PrintConfig isBold(Boolean isBold) {
        this.setIsBold(isBold);
        return this;
    }

    public void setIsBold(Boolean isBold) {
        this.isBold = isBold;
    }

    public Boolean getIsPrint() {
        return this.isPrint;
    }

    public PrintConfig isPrint(Boolean isPrint) {
        this.setIsPrint(isPrint);
        return this;
    }

    public void setIsPrint(Boolean isPrint) {
        this.isPrint = isPrint;
    }

    public Boolean getIsHeader() {
        return this.isHeader;
    }

    public PrintConfig isHeader(Boolean isHeader) {
        this.setIsHeader(isHeader);
        return this;
    }

    public void setIsHeader(Boolean isHeader) {
        this.isHeader = isHeader;
    }

    public Boolean getIsEditable() {
        return this.isEditable;
    }

    public PrintConfig isEditable(Boolean isEditable) {
        this.setIsEditable(isEditable);
        return this;
    }

    public void setIsEditable(Boolean isEditable) {
        this.isEditable = isEditable;
    }

    public Integer getVersion() {
        return this.version;
    }

    public PrintConfig version(Integer version) {
        this.setVersion(version);
        return this;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Boolean getIsBody() {
        return isBody;
    }

    public void setIsBody(Boolean body) {
        isBody = body;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PrintConfig)) {
            return false;
        }
        return id != null && id.equals(((PrintConfig) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PrintConfig{" +
            "id=" + getId() +
            ", comId=" + getComId() +
            ", code='" + getCode() + "'" +
            ", title='" + getTitle() + "'" +
            ", name='" + getName() + "'" +
            ", fontSize=" + getFontSize() +
            ", alignText=" + getAlignText() +
            ", isBold='" + getIsBold() + "'" +
            ", isPrint='" + getIsPrint() + "'" +
            ", isHeader='" + getIsHeader() + "'" +
            ", isEditable='" + getIsEditable() + "'" +
            ", version=" + getVersion() +
            "}";
    }
}
