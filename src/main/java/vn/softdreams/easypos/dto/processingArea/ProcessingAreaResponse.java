package vn.softdreams.easypos.dto.processingArea;

import vn.softdreams.easypos.domain.ProcessingArea;
import vn.softdreams.easypos.domain.ProcessingAreaProduct;
import vn.softdreams.easypos.dto.processingAreaProduct.ProcessingAreaProductItemResponse;

import java.util.List;

public class ProcessingAreaResponse {

    private ProcessingArea processingArea;
    private List<ProcessingAreaProduct> processingAreaProductList;
    private List<ProcessingAreaProductItemResponse> processingAreaProductItemResponses;

    public ProcessingArea getProcessingArea() {
        return processingArea;
    }

    public void setProcessingArea(ProcessingArea processingArea) {
        this.processingArea = processingArea;
    }

    public List<ProcessingAreaProduct> getProcessingAreaProductList() {
        return processingAreaProductList;
    }

    public void setProcessingAreaProductList(List<ProcessingAreaProduct> processingAreaProductList) {
        this.processingAreaProductList = processingAreaProductList;
    }

    public List<ProcessingAreaProductItemResponse> getProcessingAreaProductItemResponses() {
        return processingAreaProductItemResponses;
    }

    public void setProcessingAreaProductItemResponses(List<ProcessingAreaProductItemResponse> processingAreaProductItemResponses) {
        this.processingAreaProductItemResponses = processingAreaProductItemResponses;
    }
}
