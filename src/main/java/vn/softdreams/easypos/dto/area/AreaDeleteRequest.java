package vn.softdreams.easypos.dto.area;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class AreaDeleteRequest implements Serializable {

    @NotNull(message = ExceptionConstants.AREA_ID_NOT_NULL)
    private Integer id;

    @NotNull(message = ExceptionConstants.COMPANY_ID_NOT_NULL)
    private Integer comId;

    public AreaDeleteRequest() {}

    public AreaDeleteRequest(Integer id, Integer comId) {
        this.id = id;
        this.comId = comId;
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
}
