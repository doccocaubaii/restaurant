package vn.softdreams.easypos.dto.printSetting;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.NotNull;

public class SavePrintSettingRequest {

    private Integer id;

    @NotNull(message = ExceptionConstants.COM_ID_MUST_NOT_NULL)
    private Integer comId;

    @NotNull(message = ExceptionConstants.PRINTER_NAME_NOT_NULL)
    private String printName;

    private String ipAddress;

    @NotNull(message = ExceptionConstants.PRINTER_TYPE_NOT_NULL)
    private Integer type;

    private String pageSize;

    //    @NotNull(message = ExceptionConstants.PROCESSING_AREA_ID_NOT_NULL)
    private Integer processingAreaId;

    private Integer typeTemplate;

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
