package vn.softdreams.easypos.dto.invoice;

import java.io.Serializable;

public class InvoiceStatsResult implements Serializable {

    private Integer newCount;
    private Integer processingCount;
    private Integer doneCount;
    private Integer allCount;

    public InvoiceStatsResult() {}

    public InvoiceStatsResult(Integer newCount, Integer processingCount, Integer doneCount, Integer allCount) {
        this.newCount = newCount;
        this.processingCount = processingCount;
        this.doneCount = doneCount;
        this.allCount = allCount;
    }

    public Integer getNewCount() {
        return newCount;
    }

    public void setNewCount(Integer newCount) {
        this.newCount = newCount;
    }

    public Integer getProcessingCount() {
        return processingCount;
    }

    public void setProcessingCount(Integer processingCount) {
        this.processingCount = processingCount;
    }

    public Integer getDoneCount() {
        return doneCount;
    }

    public void setDoneCount(Integer doneCount) {
        this.doneCount = doneCount;
    }

    public Integer getAllCount() {
        return allCount;
    }

    public void setAllCount(Integer allCount) {
        this.allCount = allCount;
    }
}
