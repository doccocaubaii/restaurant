package vn.softdreams.easypos.dto.invoice;

import java.io.Serializable;

public class TaskCancelInvoice implements Serializable {

    private String comId;
    private String id;
    private String ikey;
    private String pattern;
    private String currentStatus;

    public TaskCancelInvoice(String comId, String id, String ikey, String pattern, String currentStatus) {
        this.comId = comId;
        this.id = id;
        this.ikey = ikey;
        this.pattern = pattern;
        this.currentStatus = currentStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPattern() {
        return pattern;
    }

    public String getComId() {
        return comId;
    }

    public void setComId(String comId) {
        this.comId = comId;
    }

    public String getIkey() {
        return ikey;
    }

    public void setIkey(String ikey) {
        this.ikey = ikey;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }
}
