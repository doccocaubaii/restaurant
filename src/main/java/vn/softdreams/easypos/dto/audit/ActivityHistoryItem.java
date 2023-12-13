package vn.softdreams.easypos.dto.audit;

import java.io.Serializable;
import java.util.Objects;

public class ActivityHistoryItem implements Serializable {

    private Integer rev;
    private Integer rowNumber;
    private Integer type;

    public ActivityHistoryItem(Integer rev, Integer rowNumber, Integer type) {
        this.rev = rev;
        this.rowNumber = rowNumber;
        this.type = type;
    }

    public Integer getRev() {
        return rev;
    }

    public void setRev(Integer rev) {
        this.rev = rev;
    }

    public Integer getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(Integer rowNumber) {
        this.rowNumber = rowNumber;
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
            ActivityHistoryItem item = (ActivityHistoryItem) object;
            if (
                Objects.equals(this.rev, item.getRev()) &&
                Objects.equals(this.rowNumber, item.getRowNumber()) &&
                this.type.equals(item.getType())
            ) {
                result = true;
            }
        }
        return result;
    }
}
