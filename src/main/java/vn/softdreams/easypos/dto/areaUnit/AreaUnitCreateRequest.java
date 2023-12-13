package vn.softdreams.easypos.dto.areaUnit;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class AreaUnitCreateRequest implements Serializable {

    @NotNull(message = ExceptionConstants.COMPANY_ID_NOT_NULL)
    private Integer comId;

    @NotNull(message = ExceptionConstants.AREA_ID_NOT_NULL)
    private Integer areaId;

    @NotBlank(message = ExceptionConstants.AREA_UNIT_NAME_NOT_EMPTY)
    @Size(max = 255, message = ExceptionConstants.AREA_UNIT_NAME_INVALID)
    private String name;

    public AreaUnitCreateRequest() {}

    public AreaUnitCreateRequest(Integer comId, Integer areaId, String name) {
        this.comId = comId;
        this.areaId = areaId;
        this.name = name;
    }

    public String getName() {
        return name.trim();
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }
}
