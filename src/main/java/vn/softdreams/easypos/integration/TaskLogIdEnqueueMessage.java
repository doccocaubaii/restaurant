package vn.softdreams.easypos.integration;

import java.io.Serializable;

public class TaskLogIdEnqueueMessage implements Serializable {

    private Integer taskLogId;

    public TaskLogIdEnqueueMessage() {}

    public TaskLogIdEnqueueMessage(Integer taskLogId) {
        this.taskLogId = taskLogId;
    }

    public Integer getTaskLogId() {
        return taskLogId;
    }

    public void setTaskLogId(Integer taskLogId) {
        this.taskLogId = taskLogId;
    }
}
