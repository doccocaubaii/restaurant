package vn.softdreams.easypos.dto.bill;

public class ResultBillInfo {

    private Integer id;
    private String code;

    public ResultBillInfo() {}

    public ResultBillInfo(Integer id, String code) {
        this.id = id;
        this.code = code;
    }

    public ResultBillInfo(String code) {
        this.code = code;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
