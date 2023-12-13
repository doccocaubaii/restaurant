package vn.softdreams.easypos.dto.productGroup;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.NotNull;

public class ProductGroupRequest {

    @NotNull(message = ExceptionConstants.GROUP_ID_NOT_NULL)
    private Integer id;

    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
