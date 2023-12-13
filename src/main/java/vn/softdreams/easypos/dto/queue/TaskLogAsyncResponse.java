package vn.softdreams.easypos.dto.queue;

import java.io.Serializable;

public class TaskLogAsyncResponse implements Serializable {

    private Integer comId;
    private Integer count = 0;

    public TaskLogAsyncResponse() {}

    public TaskLogAsyncResponse(Integer comId, Integer count) {
        this.comId = comId;
        this.count = count;
    }

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
