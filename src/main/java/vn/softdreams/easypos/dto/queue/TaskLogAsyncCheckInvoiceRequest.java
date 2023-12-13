package vn.softdreams.easypos.dto.queue;

import java.io.Serializable;
import java.util.List;

public class TaskLogAsyncCheckInvoiceRequest implements Serializable {

    private List<Integer> ids;
    private List<String> ikeys;

    public TaskLogAsyncCheckInvoiceRequest(List<Integer> ids) {
        this.ids = ids;
    }

    public TaskLogAsyncCheckInvoiceRequest() {}

    public List<String> getIkeys() {
        return ikeys;
    }

    public void setIkeys(List<String> ikeys) {
        this.ikeys = ikeys;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }
}
