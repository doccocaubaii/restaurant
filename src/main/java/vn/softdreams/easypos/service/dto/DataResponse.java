package vn.softdreams.easypos.service.dto;

import java.util.List;

/**
 * @author hieug
 */
public class DataResponse {

    private Integer countAll;
    private Integer countSuccess;
    private Integer countFalse;
    private List<Object> dataFalse;

    public DataResponse() {}

    // status auto = false

    public DataResponse(Integer countAll, Integer countSuccess, Integer countFalse, List<Object> dataFalse) {
        this.countAll = countAll;
        this.countSuccess = countSuccess;
        this.countFalse = countFalse;
        this.dataFalse = dataFalse;
    }

    public Integer getCountAll() {
        return countAll;
    }

    public void setCountAll(Integer countAll) {
        this.countAll = countAll;
    }

    public Integer getCountSuccess() {
        return countSuccess;
    }

    public void setCountSuccess(Integer countSuccess) {
        this.countSuccess = countSuccess;
    }

    public Integer getCountFalse() {
        return countFalse;
    }

    public void setCountFalse(Integer countFalse) {
        this.countFalse = countFalse;
    }

    public List<Object> getDataFalse() {
        return dataFalse;
    }

    public void setDataFalse(List<Object> dataFalse) {
        this.dataFalse = dataFalse;
    }
}
