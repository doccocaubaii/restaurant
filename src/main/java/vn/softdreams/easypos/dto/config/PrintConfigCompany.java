package vn.softdreams.easypos.dto.config;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class PrintConfigCompany {

    private Integer id;

    @NotNull(message = ExceptionConstants.COMPANY_ID_NOT_NULL)
    private Integer comId;

    @NotNull(message = ExceptionConstants.TITLE_PRINT_CONFIG_NOT_NULL)
    private String title;

    private Integer type;

    @Valid
    private PrintConfigResponse value;

    public PrintConfigCompany() {}

    public PrintConfigCompany(Integer comId, String title, PrintConfigResponse value) {
        this.comId = comId;
        this.title = title;
        this.value = value;
    }

    public PrintConfigCompany(
        Integer id,
        Integer comId,
        String title,
        Integer type,
        String code,
        String content,
        Integer fontSize,
        Integer alignText,
        Integer isBold,
        Integer isPrint,
        Integer isHeader,
        Integer isEditable,
        Integer isBody,
        Integer position
    ) {
        this.id = id;
        this.comId = comId;
        this.title = title;
        this.type = type;
        this.value = new PrintConfigResponse(code, content, fontSize, alignText, isBold, isPrint, isHeader, isEditable, isBody, position);
    }

    public PrintConfigCompany(Integer id, Integer comId, String title, PrintConfigResponse value) {
        this.id = id;
        this.comId = comId;
        this.title = title;
        this.value = value;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public PrintConfigResponse getValue() {
        return value;
    }

    public void setValue(PrintConfigResponse value) {
        this.value = value;
    }
}
