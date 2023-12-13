package vn.softdreams.easypos.dto.taskLog;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class TaskLogSendQueueRequest {

    @NotNull(message = ExceptionConstants.TASK_LOG_ID_NOT_NULL)
    private Integer id;

    @NotNull(message = ExceptionConstants.TASK_LOG_TYPE_NOT_NULL)
    @NotBlank(message = ExceptionConstants.TASK_LOG_TYPE_NOT_NULL)
    private String type;

    public TaskLogSendQueueRequest() {}

    public TaskLogSendQueueRequest(Integer id, String type) {
        this.id = id;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
