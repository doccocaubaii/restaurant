package vn.softdreams.easypos.dto.printSetting;

public class PrintSettingItemResponse {

    private Integer id;
    private Integer comId;
    private String printName;
    private String ipAddress;
    private Integer type;
    private String pageSize;
    private Integer processingAreaId;
    private Integer typeTemplate;

    public PrintSettingItemResponse() {}

    public PrintSettingItemResponse(
        Integer id,
        Integer comId,
        String printName,
        String ipAddress,
        Integer type,
        String pageSize,
        Integer processingAreaId,
        Integer typeTemplate
    ) {
        this.id = id;
        this.comId = comId;
        this.printName = printName;
        this.ipAddress = ipAddress;
        this.type = type;
        this.pageSize = pageSize;
        this.processingAreaId = processingAreaId;
        this.typeTemplate = typeTemplate;
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

    public String getPrintName() {
        return printName;
    }

    public void setPrintName(String printName) {
        this.printName = printName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getProcessingAreaId() {
        return processingAreaId;
    }

    public void setProcessingAreaId(Integer processingAreaId) {
        this.processingAreaId = processingAreaId;
    }

    public Integer getTypeTemplate() {
        return typeTemplate;
    }

    public void setTypeTemplate(Integer typeTemplate) {
        this.typeTemplate = typeTemplate;
    }
}
