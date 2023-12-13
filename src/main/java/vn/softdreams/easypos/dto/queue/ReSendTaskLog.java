package vn.softdreams.easypos.dto.queue;

import java.io.Serializable;
import java.util.List;

public class ReSendTaskLog implements Serializable {

    private List<Integer> ids;

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }
}
