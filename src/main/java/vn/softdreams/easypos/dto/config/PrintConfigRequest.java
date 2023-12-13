package vn.softdreams.easypos.dto.config;

public class PrintConfigRequest {

    private Integer id;
    private String code;
    private String title;
    private String name;
    private Integer fontSize;
    private Integer alignText;
    private Boolean isBold;
    private Boolean isPrint;
    private Boolean isHeader;
    private Boolean isEditable;
    private Integer version;

    public PrintConfigRequest() {}

    public PrintConfigRequest(
        Integer id,
        String code,
        String title,
        String name,
        Integer fontSize,
        Integer alignText,
        Boolean isBold,
        Boolean isPrint,
        Boolean isHeader,
        Boolean isEditable,
        Integer version
    ) {
        this.id = id;
        this.code = code;
        this.title = title;
        this.name = name;
        this.fontSize = fontSize;
        this.alignText = alignText;
        this.isBold = isBold;
        this.isPrint = isPrint;
        this.isHeader = isHeader;
        this.isEditable = isEditable;
        this.version = version;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Boolean getIsBold() {
        return isBold;
    }

    public void setIsBold(Boolean bold) {
        isBold = bold;
    }

    public Boolean getIsPrint() {
        return isPrint;
    }

    public void setIsPrint(Boolean print) {
        isPrint = print;
    }

    public Boolean getIsHeader() {
        return isHeader;
    }

    public void setIsHeader(Boolean header) {
        isHeader = header;
    }

    public Boolean getIsEditable() {
        return isEditable;
    }

    public void setIsEditable(Boolean editable) {
        isEditable = editable;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
