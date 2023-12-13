package vn.softdreams.easypos.dto.bill;

public class ResultBillError {

    private String code;
    private String message;
    private ResultBillInfo billInfo;
    private Boolean status;

    public String getCode() {
        return code;
    }

    public ResultBillError(String code, String message, ResultBillInfo billInfo) {
        this.code = code;
        this.message = message;
        this.billInfo = billInfo;
        this.status = false;
    }

    public ResultBillError(String code, String message, ResultBillInfo billInfo, Boolean status) {
        this.code = code;
        this.message = message;
        this.billInfo = billInfo;
        this.status = status;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResultBillInfo getBillInfo() {
        return billInfo;
    }

    public void setBillInfo(ResultBillInfo billInfo) {
        this.billInfo = billInfo;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
