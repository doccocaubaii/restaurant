package vn.softdreams.easypos.dto;

public class TaskLogSendQueue {

    private String id;
    private String type;
    private String service;

    public TaskLogSendQueue() {}

    public TaskLogSendQueue(String id, String type, String service) {
        this.id = id;
        this.type = type;
        this.service = service;
    }

    public TaskLogSendQueue(String id, String type) {
        this.id = id;
        this.type = type;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
