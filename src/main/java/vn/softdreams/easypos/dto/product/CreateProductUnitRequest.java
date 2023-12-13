package vn.softdreams.easypos.dto.product;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class CreateProductUnitRequest implements Serializable {

    @NotNull(message = "comId is required")
    private Integer comId;

    @NotEmpty(message = "unitName is required")
    private String unitName;

    private String unitDescription;

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public String getUnitName() {
        return unitName == null ? null : unitName.trim();
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
}
