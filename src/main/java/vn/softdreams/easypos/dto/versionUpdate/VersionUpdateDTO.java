package vn.softdreams.easypos.dto.versionUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;
import vn.softdreams.easypos.config.Constants;

import java.time.ZonedDateTime;

public class VersionUpdateDTO {

    private Integer id;
    private Integer comId;
    private String version;
    private String description;
    private String link;

    @JsonFormat(pattern = Constants.ZONED_DATE_FORMAT)
    private ZonedDateTime date;

    @JsonFormat(pattern = Constants.ZONED_DATE_FORMAT)
    private ZonedDateTime startDate;

    @JsonFormat(pattern = Constants.ZONED_DATE_FORMAT)
    private ZonedDateTime endDate;

    private String image;
    private Integer system;

    public VersionUpdateDTO(
        Integer id,
        Integer comId,
        String version,
        String description,
        String link,
        ZonedDateTime date,
        ZonedDateTime startDate,
        ZonedDateTime endDate,
        String image,
        Integer system
    ) {
        this.id = id;
        this.comId = comId;
        this.version = version;
        this.description = description;
        this.link = link;
        this.date = date;
        this.startDate = startDate;
        this.endDate = endDate;
        this.image = image;
        this.system = system;
    }

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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getSystem() {
        return system;
    }

    public void setSystem(Integer system) {
        this.system = system;
    }
}
