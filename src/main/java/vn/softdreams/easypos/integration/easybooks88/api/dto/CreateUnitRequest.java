package vn.softdreams.easypos.integration.easybooks88.api.dto;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

public class CreateUnitRequest implements Serializable {

    @NotEmpty(message = "UnitName is required")
    private String unitName;

    private String unitDescription;
    private boolean isActive = true;

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getUnitDescription() {
        return unitDescription;
    }

    public void setUnitDescription(String unitDescription) {
        this.unitDescription = unitDescription;
    }
}
