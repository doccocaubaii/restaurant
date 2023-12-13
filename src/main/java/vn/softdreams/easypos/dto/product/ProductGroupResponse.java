package vn.softdreams.easypos.dto.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import vn.softdreams.easypos.config.Constants;
import vn.softdreams.easypos.dto.productGroup.ProductOfflineResponse;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

public class ProductGroupResponse implements Serializable {

    private Integer id;
    private Integer comId;
    private String name;
    private String code;
    private String description;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    private ZonedDateTime createTime;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    private ZonedDateTime updateTime;

    private List<ProductOfflineResponse> productResponses;

    public ProductGroupResponse() {}

    public List<ProductOfflineResponse> getProductResponses() {
        return productResponses;
    }

    public void setProductResponses(List<ProductOfflineResponse> productResponses) {
        this.productResponses = productResponses;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
