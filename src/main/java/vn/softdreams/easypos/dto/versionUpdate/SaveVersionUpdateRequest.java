package vn.softdreams.easypos.dto.versionUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;
import vn.softdreams.easypos.config.Constants;

public class SaveVersionUpdateRequest {

    private Integer id;

    private Integer comId;

    private String version;

    @JsonFormat(pattern = Constants.ZONED_DATE_FORMAT)
    private String date;

    private String description;
    private String link;

    @JsonFormat(pattern = Constants.ZONED_DATE_FORMAT)
    private String startDate;

    @JsonFormat(pattern = Constants.ZONED_DATE_FORMAT)
    private String endDate;

    private Integer system;

    public SaveVersionUpdateRequest() {}

    public SaveVersionUpdateRequest(
        Integer comId,
        String version,
        String date,
        String description,
        String link,
        String startDate,
        String endDate,
        Integer system
    ) {
        this.comId = comId;
        this.version = version;
        this.date = date;
        this.description = description;
        this.link = link;
        this.startDate = startDate;
        this.endDate = endDate;
        this.system = system;
    }

    public SaveVersionUpdateRequest(
        Integer id,
        Integer comId,
        String version,
        String date,
        String description,
        String link,
        String startDate,
        String endDate,
        Integer system
    ) {
        this.id = id;
        this.comId = comId;
        this.version = version;
        this.date = date;
        this.description = description;
        this.link = link;
        this.startDate = startDate;
        this.endDate = endDate;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getSystem() {
        return system;
    }

    public void setSystem(Integer system) {
        this.system = system;
    }
}
