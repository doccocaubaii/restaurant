package vn.softdreams.easypos.dto.areaUnit;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class AreaUnitDeleteRequest implements Serializable {

    @NotNull(message = ExceptionConstants.AREA_UNIT_ID_NOT_NULL)
    private Integer id;

    @NotNull(message = ExceptionConstants.COMPANY_ID_NOT_NULL)
    private Integer comId;

    @NotNull(message = ExceptionConstants.AREA_ID_NOT_NULL)
    private Integer areaId;

    public AreaUnitDeleteRequest() {}

    public AreaUnitDeleteRequest(Integer id, Integer comId, Integer areaId) {
        this.id = id;
        this.comId = comId;
        this.areaId = areaId;
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

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }
}
