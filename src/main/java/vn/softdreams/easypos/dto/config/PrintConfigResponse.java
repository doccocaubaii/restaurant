package vn.softdreams.easypos.dto.config;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.NotNull;

public class PrintConfigResponse {

    @NotNull(message = ExceptionConstants.CODE_PRINT_CONFIG_NOT_NULL)
    private String code;

    private String content;
    private Integer fontSize;
    private Integer alignText;
    private Integer isBold;
    private Integer isPrint;
    private Integer isHeader;
    private Integer isEditable;
    private Integer isBody;
    private Integer position;

    public PrintConfigResponse() {}

    public PrintConfigResponse(
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
        this.code = code;
        this.content = content;
        this.fontSize = fontSize;
        this.alignText = alignText;
        this.isBold = isBold;
        this.isPrint = isPrint;
        this.isHeader = isHeader;
        this.isEditable = isEditable;
        this.isBody = isBody;
        this.position = position;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getFontSize() {
        return fontSize;
    }

    public void setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
    }

    public Integer getAlignText() {
        return alignText;
    }

    public void setAlignText(Integer alignText) {
        this.alignText = alignText;
    }

    public Integer getIsBold() {
        return isBold;
    }

    public void setIsBold(Integer isBold) {
        this.isBold = isBold;
    }

    public Integer getIsPrint() {
        return isPrint;
    }

    public void setIsPrint(Integer isPrint) {
        this.isPrint = isPrint;
    }

    public Integer getIsHeader() {
        return isHeader;
    }

    public void setIsHeader(Integer isHeader) {
        this.isHeader = isHeader;
    }

    public Integer getIsEditable() {
        return isEditable;
    }

    public void setIsEditable(Integer isEditable) {
        this.isEditable = isEditable;
    }

    public Integer getIsBody() {
        return isBody;
    }

    public void setIsBody(Integer isBody) {
        this.isBody = isBody;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }
}
