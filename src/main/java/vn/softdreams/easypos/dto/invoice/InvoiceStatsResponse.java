package vn.softdreams.easypos.dto.invoice;

import com.fasterxml.jackson.annotation.JsonFormat;
import vn.softdreams.easypos.config.Constants;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class InvoiceStatsResponse implements Serializable {

    private Integer comId;
    private String fromDate;
    private String toDate;
    private Integer newCount;
    private Integer processingCount;
    private Integer doneCount;
    private Integer allCount;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    private ZonedDateTime createTime;

    public InvoiceStatsResponse() {}

    public InvoiceStatsResponse(
        Integer comId,
        String fromDate,
        String toDate,
        Integer newCount,
        Integer processingCount,
        Integer doneCount,
        Integer allCount,
        ZonedDateTime createTime
    ) {
        this.comId = comId;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.newCount = newCount;
        this.processingCount = processingCount;
        this.doneCount = doneCount;
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

    public Integer getNewCount() {
        return newCount == null ? 0 : newCount;
    }

    public void setNewCount(Integer newCount) {
        this.newCount = newCount;
    }

    public Integer getProcessingCount() {
        return processingCount == null ? 0 : processingCount;
    }

    public void setProcessingCount(Integer processingCount) {
        this.processingCount = processingCount;
    }

    public Integer getDoneCount() {
        return doneCount == null ? 0 : doneCount;
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

    public ZonedDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(ZonedDateTime createTime) {
        this.createTime = createTime;
    }
}
