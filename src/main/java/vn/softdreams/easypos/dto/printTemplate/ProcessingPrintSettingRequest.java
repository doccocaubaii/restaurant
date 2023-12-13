package vn.softdreams.easypos.dto.printTemplate;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.NotNull;
import java.util.List;

public class ProcessingPrintSettingRequest {

    @NotNull(message = ExceptionConstants.PRINT_PROCESSING_AREA_NOT_NULL)
    private Integer processingAreaId;

    private List<String> printerInfo;
    private List<Integer> printTemplateId;
    private String printTemplate;

    public Integer getProcessingAreaId() {
        return processingAreaId;
    }

    public void setProcessingAreaId(Integer processingAreaId) {
        this.processingAreaId = processingAreaId;
    }

    public List<String> getPrinterInfo() {
        return printerInfo;
    }

    public void setPrinterInfo(List<String> printerInfo) {
        this.printerInfo = printerInfo;
    }

    public List<Integer> getPrintTemplateId() {
        return printTemplateId;
    }

    public void setPrintTemplateId(List<Integer> printTemplateId) {
        this.printTemplateId = printTemplateId;
    }

    public String getPrintTemplate() {
        return printTemplate;
    }

    public void setPrintTemplate(String printTemplate) {
        this.printTemplate = printTemplate;
    }
}
