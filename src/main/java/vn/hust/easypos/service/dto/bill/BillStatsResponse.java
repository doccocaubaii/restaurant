package vn.hust.easypos.service.dto.bill;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.time.LocalDateTime;
import vn.hust.easypos.config.Constants;

public class BillStatsResponse implements Serializable {

    private Integer comId;
    private String fromDate;
    private String toDate;
    private Integer processingCount;
    private Integer allCount = 0;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    private LocalDateTime createTime;

    public BillStatsResponse() {}

    public BillStatsResponse(
        Integer comId,
        String fromDate,
        String toDate,
        Integer processingCount,
        Integer allCount,
        LocalDateTime createTime
    ) {
        this.comId = comId;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.processingCount = processingCount;
        this.allCount = allCount;
        this.createTime = createTime;
    }

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public Integer getProcessingCount() {
        return processingCount == null ? 0 : processingCount;
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
