package vn.softdreams.easypos.integration.easybooks88.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class GetUnitsResponse implements Serializable {

    @JsonProperty
    private Integer id;

    @JsonProperty
    private String unitName;

    @JsonProperty
    private String unitDescription;

    @JsonProperty
    private Boolean isActive;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitDescription() {
        return unitDescription;
    }

    public void setUnitDescription(String unitDescription) {
        this.unitDescription = unitDescription;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
