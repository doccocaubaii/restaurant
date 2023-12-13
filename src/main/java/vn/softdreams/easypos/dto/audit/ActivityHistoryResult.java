package vn.softdreams.easypos.dto.audit;

import com.fasterxml.jackson.annotation.JsonFormat;
import vn.softdreams.easypos.config.Constants;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class ActivityHistoryResult implements Serializable {

    private Integer rev;
    private Integer id;
    private String code;
    private Integer type;
    private Integer rowNumber;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    private ZonedDateTime updateTime;

    public ActivityHistoryResult(Integer rev, Integer id, String code, Integer type, Integer rowNumber, ZonedDateTime updateTime) {
        this.rev = rev;
        this.id = id;
        this.code = code;
        this.type = type;
        this.rowNumber = rowNumber;
        this.updateTime = updateTime;
    }

    public ZonedDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(ZonedDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(Integer rowNumber) {
        this.rowNumber = rowNumber;
    }

    public Integer getRev() {
        return rev;
    }

    public void setRev(Integer rev) {
        this.rev = rev;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
