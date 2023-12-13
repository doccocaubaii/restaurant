package vn.softdreams.easypos.dto.processingArea;

import java.util.List;

public class ProcessingAreaItemResponse {

    private Integer id;
    private Integer comId;
    private String name;
    private Integer setting;
    private Integer active;
    private String createTime;
    private List<String> printerInfo;
    private String printTemplate;
    private List<Integer> printTemplateId;

    public ProcessingAreaItemResponse() {}

    public ProcessingAreaItemResponse(Integer id, Integer comId, String name, Integer setting, Integer active, String createTime) {
        this.id = id;
        this.comId = comId;
        this.name = name;
        this.setting = setting;
        this.active = active;
        this.createTime = createTime;
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

    public Integer getSetting() {
        return setting;
    }

    public void setSetting(Integer setting) {
        this.setting = setting;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public List<String> getPrinterInfo() {
        return printerInfo;
    }

    public void setPrinterInfo(List<String> printerInfo) {
        this.printerInfo = printerInfo;
    }

    public String getPrintTemplate() {
        return printTemplate;
    }

    public void setPrintTemplate(String printTemplate) {
        this.printTemplate = printTemplate;
    }

    public List<Integer> getPrintTemplateId() {
        return printTemplateId;
    }

    public void setPrintTemplateId(List<Integer> printTemplateId) {
        this.printTemplateId = printTemplateId;
    }
}
