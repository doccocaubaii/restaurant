package vn.softdreams.easypos.integration.easyinvoice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class SendIssuanceNoticeEasyInvoiceResponse implements Serializable {

    @JsonProperty("Status")
    private String status;

    @JsonProperty("Message")
    private String message;

    @JsonProperty("ErrorCode")
    private String errorCode;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
