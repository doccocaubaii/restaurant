package vn.softdreams.easypos.dto.productGroup;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

import static vn.softdreams.easypos.web.rest.errors.ExceptionConstants.COM_ID_MUST_NOT_NULL;
import static vn.softdreams.easypos.web.rest.errors.ExceptionConstants.ID_MUST_NOT_NULL_VI;

public class SaveProductGroupRequest implements Serializable {

    @NotNull(message = COM_ID_MUST_NOT_NULL)
    private Integer comId;

    @NotEmpty(message = ID_MUST_NOT_NULL_VI)
    private String name;

    @Size(max = 512, message = ExceptionConstants.DESCRIPTION_PRODUCT_GROUP_MAX_LENGTH)
    private String description;

    public SaveProductGroupRequest() {}

    public SaveProductGroupRequest(Integer comId, String name, String description) {
        this.comId = comId;
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }
}
