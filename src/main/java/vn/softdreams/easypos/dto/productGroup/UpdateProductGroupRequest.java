package vn.softdreams.easypos.dto.productGroup;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class UpdateProductGroupRequest implements Serializable {

    @NotNull(message = ExceptionConstants.ID_NOT_NULL)
    private Integer id;

    @NotNull(message = ExceptionConstants.COM_ID_MUST_NOT_NULL)
    private Integer comId;

    private String name;

    @Size(max = 512, message = ExceptionConstants.DESCRIPTION_PRODUCT_GROUP_MAX_LENGTH)
    private String description;

    public UpdateProductGroupRequest() {}

    public UpdateProductGroupRequest(Integer id, Integer comId, String name, String description) {
        this.id = id;
        this.comId = comId;
        this.name = name;
        this.description = description;
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
}
