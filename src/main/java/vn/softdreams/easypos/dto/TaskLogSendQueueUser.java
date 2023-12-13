package vn.softdreams.easypos.dto;

import vn.softdreams.easypos.domain.User;

import java.io.Serializable;

public class TaskLogSendQueueUser implements Serializable {

    private TaskLogSendQueue taskLogSendQueue;
    private User user;
    private Integer comId;

    public TaskLogSendQueueUser(TaskLogSendQueue taskLogSendQueue, User user, Integer comId) {
        this.taskLogSendQueue = taskLogSendQueue;
        this.user = user;
        this.comId = comId;
    }

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public TaskLogSendQueue getTaskLogSendQueue() {
        return taskLogSendQueue;
    }

    public void setTaskLogSendQueue(TaskLogSendQueue taskLogSendQueue) {
        this.taskLogSendQueue = taskLogSendQueue;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
