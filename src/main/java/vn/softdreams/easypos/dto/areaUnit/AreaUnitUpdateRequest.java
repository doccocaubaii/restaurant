package vn.softdreams.easypos.dto.areaUnit;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class AreaUnitUpdateRequest implements Serializable {

    @NotNull(message = ExceptionConstants.AREA_UNIT_ID_NOT_NULL)
    private Integer id;

    @NotNull(message = ExceptionConstants.COMPANY_ID_NOT_NULL)
    private Integer comId;

    @NotNull(message = ExceptionConstants.AREA_ID_NOT_NULL)
    private Integer areaId;

    @Size(max = 255, message = ExceptionConstants.AREA_UNIT_NAME_INVALID)
    private String name;

    public AreaUnitUpdateRequest() {}

    public AreaUnitUpdateRequest(Integer id, Integer comId, Integer areaId, String name) {
        this.id = id;
        this.comId = comId;
        this.areaId = areaId;
        this.name = name;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        if (name != null) {
            return name.trim();
        }
        return null;
    }

    public void setName(String name) {
        this.name = name;
    }
}
