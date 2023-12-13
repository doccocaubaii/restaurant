package vn.softdreams.easypos.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import vn.softdreams.easypos.config.Constants;
import vn.softdreams.easypos.dto.versionUpdate.VersionUpdateDTO;
import vn.softdreams.easypos.util.Common;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A VersionUpdate.
 */
@Entity
@Table(name = "version_update")
@SuppressWarnings("common-java:DuplicatedBlocks")
@SqlResultSetMappings(
    {
        @SqlResultSetMapping(
            name = "VersionUpdateDTO",
            classes = {
                @ConstructorResult(
                    targetClass = VersionUpdateDTO.class,
                    columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "com_id", type = Integer.class),
                        @ColumnResult(name = "version", type = String.class),
                        @ColumnResult(name = "description", type = String.class),
                        @ColumnResult(name = "link", type = String.class),
                        @ColumnResult(name = "date", type = ZonedDateTime.class),
                        @ColumnResult(name = "start_date", type = ZonedDateTime.class),
                        @ColumnResult(name = "end_date", type = ZonedDateTime.class),
                        @ColumnResult(name = "image", type = String.class),
                        @ColumnResult(name = "system", type = Integer.class),
                    }
                ),
            }
        ),
    }
)
public class VersionUpdate extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "com_id")
    private Integer comId;

    @Column(name = "version")
    private String version;

    @Column(name = "description")
    private String description;

    @Column(name = "link")
    private String link;

    @Column(name = "date")
    @JsonFormat(pattern = Constants.ZONED_DATE_FORMAT)
    private ZonedDateTime date;

    @Column(name = "start_date")
    @JsonFormat(pattern = Constants.ZONED_DATE_FORMAT)
    private ZonedDateTime startDate;

    @Column(name = "end_date")
    @JsonFormat(pattern = Constants.ZONED_DATE_FORMAT)
    private ZonedDateTime endDate;

    @Column(name = "image")
    private String image;

    @Column(name = "system")
    private Integer system;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getComId() {
        return this.comId;
    }

    public VersionUpdate comId(Integer comId) {
        this.setComId(comId);
        return this;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public String getVersion() {
        return this.version;
    }

    public VersionUpdate version(String version) {
        this.setVersion(version);
        return this;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return this.description;
    }

    public VersionUpdate description(String description) {
        this.setDescription(description);
        return this;
    }

    public Integer getSystem() {
        return system;
    }

    public void setSystem(Integer system) {
        this.system = system;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return this.link;
    }

    public VersionUpdate link(String link) {
        this.setLink(link);
        return this;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public ZonedDateTime getDate() {
        return this.date;
    }

    public VersionUpdate date(ZonedDateTime date) {
        this.setDate(date);
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public void setDate(String date) {
        this.date = Common.convertStringToDateTime(date, Constants.ZONED_DATE_FORMAT);
    }

    public ZonedDateTime getStartDate() {
        return this.startDate;
    }

    public VersionUpdate startDate(ZonedDateTime startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public void setStartDate(String date) {
        this.startDate = Common.convertStringToDateTime(date, Constants.ZONED_DATE_FORMAT);
    }

    public ZonedDateTime getEndDate() {
        return this.endDate;
    }

    public VersionUpdate endDate(ZonedDateTime endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public void setEndDate(String date) {
        this.endDate = Common.convertStringToDateTime(date, Constants.ZONED_DATE_FORMAT);
    }

    public String getImage() {
        return this.image;
    }

    public VersionUpdate image(String image) {
        this.setImage(image);
        return this;
    }

    public void setImage(String image) {
        this.image = image;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VersionUpdate)) {
            return false;
        }
        return id != null && id.equals(((VersionUpdate) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VersionUpdate{" +
            "id=" + getId() +
            ", comId=" + getComId() +
            ", version='" + getVersion() + "'" +
            ", description='" + getDescription() + "'" +
            ", link='" + getLink() + "'" +
            ", date='" + getDate() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", image='" + getImage() + "'" +
            "}";
    }
}
