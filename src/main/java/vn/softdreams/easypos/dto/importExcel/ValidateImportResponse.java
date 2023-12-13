package vn.softdreams.easypos.dto.importExcel;

import java.io.Serializable;

public class ValidateImportResponse implements Serializable {

    private Integer countValid;
    private Integer countInvalid;
    private Object data;

    public ValidateImportResponse(Integer countValid, Integer countInvalid, Object data) {
        this.countValid = countValid;
        this.countInvalid = countInvalid;
        this.data = data;
    }

    public Integer getCountValid() {
        return countValid;
    }

    public void setCountValid(Integer countValid) {
        this.countValid = countValid;
    }

    public Integer getCountInvalid() {
        return countInvalid;
    }

    public void setCountInvalid(Integer countInvalid) {
        this.countInvalid = countInvalid;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
