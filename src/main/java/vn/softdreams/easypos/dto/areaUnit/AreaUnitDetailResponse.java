package vn.softdreams.easypos.dto.areaUnit;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import vn.softdreams.easypos.config.Constants;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

public class AreaUnitDetailResponse implements Serializable {

    private Integer id;
    private Integer comId;
    private Integer areaId;
    private String name;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    private ZonedDateTime createTime;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    private ZonedDateTime updateTime;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Object> usingBills;

    public AreaUnitDetailResponse() {}

    public AreaUnitDetailResponse(
        Integer id,
        Integer comId,
        Integer areaId,
        String name,
        ZonedDateTime createTime,
        ZonedDateTime updateTime
    ) {
        this.id = id;
        this.comId = comId;
        this.areaId = areaId;
        this.name = name;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public AreaUnitDetailResponse(
        Integer id,
        Integer comId,
        Integer areaId,
        String name,
        ZonedDateTime createTime,
        ZonedDateTime updateTime,
        List<Object> usingBills
    ) {
        this.id = id;
        this.comId = comId;
        this.areaId = areaId;
        this.name = name;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.usingBills = usingBills;
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

    public List<Object> getUsingBills() {
        return usingBills;
    }

    public void setUsingBills(List<Object> usingBills) {
        this.usingBills = usingBills;
    }
}
