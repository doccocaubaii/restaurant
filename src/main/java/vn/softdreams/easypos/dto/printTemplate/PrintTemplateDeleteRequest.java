package vn.softdreams.easypos.dto.printTemplate;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.NotEmpty;

public class PrintTemplateDeleteRequest {

    @NotEmpty(message = ExceptionConstants.PRINT_TEMPLATE_COM_ID_NOT_NULL)
    private Integer comId;

    @NotEmpty(message = ExceptionConstants.PRINT_TEMPLATE_ID_NOT_NULL)
    private Integer id;

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
