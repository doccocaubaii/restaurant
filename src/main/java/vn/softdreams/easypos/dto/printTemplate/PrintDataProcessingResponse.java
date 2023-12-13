package vn.softdreams.easypos.dto.printTemplate;

import java.util.List;

public class PrintDataProcessingResponse {

    private Integer processingAreaId;
    private String processingAreaName;
    private List<String> printerInfo;
    private List<PrintTemplateResponse> printTemplate;
    private PrintDataResponse dataResponse;

    public Integer getProcessingAreaId() {
        return processingAreaId;
    }

    public void setProcessingAreaId(Integer processingAreaId) {
        this.processingAreaId = processingAreaId;
    }

    public String getProcessingAreaName() {
        return processingAreaName;
    }

    public void setProcessingAreaName(String processingAreaName) {
        this.processingAreaName = processingAreaName;
    }

    public List<String> getPrinterInfo() {
        return printerInfo;
    }

    public void setPrinterInfo(List<String> printerInfo) {
        this.printerInfo = printerInfo;
    }

    public List<PrintTemplateResponse> getPrintTemplate() {
        return printTemplate;
    }

    public void setPrintTemplate(List<PrintTemplateResponse> printTemplate) {
        this.printTemplate = printTemplate;
    }

    public PrintDataResponse getDataResponse() {
        return dataResponse;
    }

    public void setDataResponse(PrintDataResponse dataResponse) {
        this.dataResponse = dataResponse;
    }

    public static class PrintTemplateResponse {

        private Integer id;
        private String code;
        private String name;
        private String pageSize;
        private String content;
        private String contentParams;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPageSize() {
            return pageSize;
        }

        public void setPageSize(String pageSize) {
            this.pageSize = pageSize;
        }

        public String getContentParams() {
            return contentParams;
        }

        public void setContentParams(String contentParams) {
            this.contentParams = contentParams;
        }
    }
}
