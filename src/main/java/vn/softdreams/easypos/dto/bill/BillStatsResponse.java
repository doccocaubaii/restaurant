package vn.softdreams.easypos.dto.bill;

import com.fasterxml.jackson.annotation.JsonFormat;
import vn.softdreams.easypos.config.Constants;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class BillStatsResponse implements Serializable {

    private Integer comId;
    private String fromDate;
    private String toDate;
    private Integer processingCount;
    private Integer allCount = 0;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    private ZonedDateTime createTime;

    public BillStatsResponse() {}

    public BillStatsResponse(
        Integer comId,
        String fromDate,
        String toDate,
        Integer processingCount,
        Integer allCount,
        ZonedDateTime createTime
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

    public ZonedDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(ZonedDateTime createTime) {
        this.createTime = createTime;
    }
}
