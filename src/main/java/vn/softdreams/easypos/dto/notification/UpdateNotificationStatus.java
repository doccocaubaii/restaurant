package vn.softdreams.easypos.dto.notification;

public class UpdateNotificationStatus {

    private Integer id;
    private Boolean readAll;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getReadAll() {
        return readAll;
    }

    public void setReadAll(Boolean readAll) {
        this.readAll = readAll;
    }
}
