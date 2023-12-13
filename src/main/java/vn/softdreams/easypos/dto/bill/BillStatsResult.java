package vn.softdreams.easypos.dto.bill;

import java.io.Serializable;

public class BillStatsResult implements Serializable {

    private Integer processingCount = 0;
    private Integer allCount = 0;

    public BillStatsResult() {}

    public BillStatsResult(Integer processingCount, Integer allCount) {
        this.processingCount = processingCount;
        this.allCount = allCount;
    }

    public Integer getProcessingCount() {
        return processingCount;
    }

    public void setProcessingCount(Integer processingCount) {
        this.processingCount = processingCount;
    }

    public Integer getAllCount() {
        return allCount;
    }

    public void setAllCount(Integer allCount) {
        this.allCount = allCount;
    }
}
