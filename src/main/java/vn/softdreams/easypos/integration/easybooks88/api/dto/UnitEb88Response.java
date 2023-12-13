package vn.softdreams.easypos.integration.easybooks88.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UnitEb88Response {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("companyId")
    private Integer companyId;

    @JsonProperty("unitName")
    private String unitName;

    @JsonProperty("unitDescription")
    private String unitDescription;

    @JsonProperty("isActive")
    private boolean isActive;

    @JsonProperty("codeChange")
    private String codeChange;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getCodeChange() {
        return codeChange;
    }

    public void setCodeChange(String codeChange) {
        this.codeChange = codeChange;
    }
}
