package vn.softdreams.easypos.dto.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import vn.softdreams.easypos.config.Constants;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class ProductUnitResponse implements Serializable {

    private Integer id;
    private Integer comId;
    private String name;
    private String description;
    private Boolean active;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    private ZonedDateTime createTime;

    private String note;

    public ProductUnitResponse() {}

    public ProductUnitResponse(Integer id, Integer comId, String name, String description) {
        this.id = id;
        this.comId = comId;
        this.name = name;
        this.description = description;
    }

    public ProductUnitResponse(Integer id, Integer comId, String name, String description, Boolean active, ZonedDateTime createTime) {
        this.id = id;
        this.comId = comId;
        this.name = name;
        this.description = description;
        this.active = active;
        this.createTime = createTime;
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

    public String getName() {
        return name == null ? null : name.trim();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public ZonedDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(ZonedDateTime createTime) {
        this.createTime = createTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
