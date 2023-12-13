package vn.softdreams.easypos.dto.message;

public class SendMessageRequest {

    private String pass;
    private String tranId;
    private String brandName;
    private Integer dataEncode;
    private String phone;
    private String mess;
    private String user;

    private String telcoCode;

    public SendMessageRequest(
        String pass,
        String tranId,
        String brandName,
        Integer dataEncode,
        String phone,
        String mess,
        String user,
        String telcoCode
    ) {
        this.pass = pass;
        this.tranId = tranId;
        this.brandName = brandName;
        this.dataEncode = dataEncode;
        this.phone = phone;
        this.mess = mess;
        this.user = user;
        this.telcoCode = telcoCode;
    }

    public SendMessageRequest() {}

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getTranId() {
        return tranId;
    }

    public void setTranId(String tranId) {
        this.tranId = tranId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Integer getDataEncode() {
        return dataEncode;
    }

    public void setDataEncode(Integer dataEncode) {
        this.dataEncode = dataEncode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMess() {
        return mess;
    }

    public void setMess(String message) {
        this.mess = message;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTelcoCode() {
        return telcoCode;
    }

    public void setTelcoCode(String telcoCode) {
        this.telcoCode = telcoCode;
    }

    @Override
    public String toString() {
        return (
            "SendMessageDTO{" +
            "pass='" +
            pass +
            '\'' +
            ", tranId='" +
            tranId +
            '\'' +
            ", brandName='" +
            brandName +
            '\'' +
            ", dataEncode=" +
            dataEncode +
            ", phone='" +
            phone +
            '\'' +
            ", message='" +
            mess +
            '\'' +
            ", user='" +
            user +
            '\'' +
            '}'
        );
    }
}
