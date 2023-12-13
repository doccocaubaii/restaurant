package vn.softdreams.easypos.dto.audit;

import java.io.Serializable;
import java.util.Objects;

public class ActivityHistoryItemKey implements Serializable {

    private Integer id;
    private Integer type;

    public ActivityHistoryItemKey(Integer id, Integer type) {
        this.id = id;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object object) {
        boolean result = false;
        if (object == null || object.getClass() != getClass()) {
            result = false;
        } else {
            ActivityHistoryItemKey key = (ActivityHistoryItemKey) object;
            if (Objects.equals(this.id, key.getId()) && this.type.equals(key.getType())) {
                result = true;
            }
        }
        return result;
    }
}
