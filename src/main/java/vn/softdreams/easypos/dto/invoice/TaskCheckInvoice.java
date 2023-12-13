package vn.softdreams.easypos.dto.invoice;

import java.util.List;

public class TaskCheckInvoice {

    private String comId;
    private List<String> ikeys;

    public TaskCheckInvoice() {}

    public TaskCheckInvoice(String comId, List<String> ikeys) {
        this.comId = comId;
        this.ikeys = ikeys;
    }

    public String getComId() {
        return comId;
    }

    public void setComId(String comId) {
        this.comId = comId;
    }

    public List<String> getIkeys() {
        return ikeys;
    }

    public void setIkeys(List<String> ikeys) {
        this.ikeys = ikeys;
    }
}
