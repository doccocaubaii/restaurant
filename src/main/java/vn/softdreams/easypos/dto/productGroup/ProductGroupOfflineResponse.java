package vn.softdreams.easypos.dto.productGroup;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class ProductGroupOfflineResponse implements Serializable {

    private Integer id;
    private Integer comId;
    private String name;
    private String description;
    private ZonedDateTime createTime;
    private ZonedDateTime updateTime;

    public ProductGroupOfflineResponse() {}

    public ProductGroupOfflineResponse(
        Integer id,
        Integer comId,
        String name,
        String description,
        ZonedDateTime createTime,
        ZonedDateTime updateTime
    ) {
        this.id = id;
        this.comId = comId;
        this.name = name;
        this.description = description;
        this.createTime = createTime;
        this.updateTime = updateTime;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}
