package vn.softdreams.easypos.dto.area;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class AreaCreateRequest implements Serializable {

    @NotNull(message = ExceptionConstants.COMPANY_ID_NOT_NULL)
    private Integer comId;

    @NotBlank(message = ExceptionConstants.AREA_NAME_NOT_EMPTY)
    @Size(max = 255, message = ExceptionConstants.AREA_NAME_INVALID)
    private String name;

    public AreaCreateRequest() {}

    public AreaCreateRequest(Integer comId, String name) {
        this.comId = comId;
        this.name = name;
    }

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public String getName() {
        return name.trim();
    }

    public void setName(String name) {
        this.name = name;
    }
}
