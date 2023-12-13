package vn.softdreams.easypos.dto.notification;

import com.fasterxml.jackson.annotation.JsonFormat;
import vn.softdreams.easypos.config.Constants;

import java.time.ZonedDateTime;

public class NotificationItemResponse {

    private Integer id;
    private Integer billId;
    private String subject;
    private String content;
    private Integer type;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    private ZonedDateTime createTime;

    private Boolean isRead;

    public NotificationItemResponse(
        Integer id,
        Integer billId,
        String subject,
        String content,
        ZonedDateTime createTime,
        Boolean isRead,
        Integer type
    ) {
        this.id = id;
        this.billId = billId;
        this.subject = subject;
        this.content = content;
        this.createTime = createTime;
        this.isRead = isRead;
        this.type = type;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean read) {
        isRead = read;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBillId() {
        return billId;
    }

    public void setBillId(Integer billId) {
        this.billId = billId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ZonedDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(ZonedDateTime createTime) {
        this.createTime = createTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
