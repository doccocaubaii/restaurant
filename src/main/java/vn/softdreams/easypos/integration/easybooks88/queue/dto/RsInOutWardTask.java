package vn.softdreams.easypos.integration.easybooks88.queue.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class RsInOutWardTask implements Serializable {

    @JsonProperty
    private Integer comId;

    @JsonProperty
    private Integer rsInOutWardId;

    @JsonProperty
    private String businessType;

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public Integer getRsInOutWardId() {
        return rsInOutWardId;
    }

    public void setRsInOutWardId(Integer rsInOutWardId) {
        this.rsInOutWardId = rsInOutWardId;
    }
}
