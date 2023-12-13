package vn.softdreams.easypos.dto.area;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import vn.softdreams.easypos.config.Constants;
import vn.softdreams.easypos.dto.areaUnit.AreaUnitDetailResponse;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

public class AreaDetailResponse implements Serializable {

    private Integer id;
    private Integer comId;
    private String name;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    private ZonedDateTime createTime;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    private ZonedDateTime updateTime;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<AreaUnitDetailResponse> units;

    public AreaDetailResponse() {}

    public AreaDetailResponse(Integer id, Integer comId, String name, ZonedDateTime createTime, ZonedDateTime updateTime) {
        this.id = id;
        this.comId = comId;
        this.name = name;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public AreaDetailResponse(
        Integer id,
        Integer comId,
        String name,
        ZonedDateTime createTime,
        ZonedDateTime updateTime,
        List<AreaUnitDetailResponse> units
    ) {
        this.id = id;
        this.comId = comId;
        this.name = name;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.units = units;
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
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(ZonedDateTime createTime) {
        this.createTime = createTime;
    }

    public ZonedDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(ZonedDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public List<AreaUnitDetailResponse> getUnits() {
        return units;
    }

    public void setUnits(List<AreaUnitDetailResponse> units) {
        this.units = units;
    }
}
