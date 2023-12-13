package vn.softdreams.easypos.dto.message;

public class SendMessageReponse {

    private Integer code;
    private String message;
    private String tranId;
    private String oper;
    private Integer totalSMS;

    public SendMessageReponse() {}

    public SendMessageReponse(Integer code, String message, String tranId, String oper, Integer totalSMS) {
        this.code = code;
        this.message = message;
        this.tranId = tranId;
        this.oper = oper;
        this.totalSMS = totalSMS;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTranId() {
        return tranId;
    }

    public void setTranId(String tranId) {
        this.tranId = tranId;
    }

    public String getOper() {
        return oper;
    }

    public void setOper(String oper) {
        this.oper = oper;
    }

    public Integer getTotalSMS() {
        return totalSMS;
    }

    public void setTotalSMS(int totalSMS) {
        this.totalSMS = totalSMS;
    }

    @Override
    public String toString() {
        return (
            "ThirdPartyRes{" +
            "code=" +
            code +
            ", message='" +
            message +
            '\'' +
            ", tranId='" +
            tranId +
            '\'' +
            ", oper='" +
            oper +
            '\'' +
            ", totalSMS=" +
            totalSMS +
            '}'
        );
    }
}
