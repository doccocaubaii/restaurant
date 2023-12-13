package vn.softdreams.easypos.dto.area;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class AreaUpdateRequest implements Serializable {

    @NotNull(message = ExceptionConstants.AREA_ID_NOT_NULL)
    private Integer id;

    @NotNull(message = ExceptionConstants.COMPANY_ID_NOT_NULL)
    private Integer comId;

    @Size(max = 255, message = ExceptionConstants.AREA_NAME_INVALID)
    private String name;

    public AreaUpdateRequest() {}

    public AreaUpdateRequest(Integer id, Integer comId, String name) {
        this.id = id;
        this.comId = comId;
        this.name = name;
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

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }
}
