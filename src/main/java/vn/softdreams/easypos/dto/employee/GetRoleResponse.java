package vn.softdreams.easypos.dto.employee;

import com.fasterxml.jackson.annotation.JsonFormat;
import vn.softdreams.easypos.config.Constants;
import vn.softdreams.easypos.domain.Role;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class GetRoleResponse implements Serializable {

    private Integer comId;
    private Integer id;
    private String code;
    private String name;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    private ZonedDateTime createTime;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    private ZonedDateTime updateTime;

    public GetRoleResponse() {}

    public GetRoleResponse(Integer comId, Integer id, String code, String name, ZonedDateTime createTime, ZonedDateTime updateTime) {
        this.comId = comId;
        this.id = id;
        this.code = code;
        this.name = name;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public void convertRoleToGetRoleResponse(Role role, Integer comId) {
        this.comId = comId;
        this.id = role.getId();
        this.code = role.getCode();
        this.name = role.getName();
        this.createTime = role.getCreateTime();
        this.updateTime = role.getUpdateTime();
    }
}
