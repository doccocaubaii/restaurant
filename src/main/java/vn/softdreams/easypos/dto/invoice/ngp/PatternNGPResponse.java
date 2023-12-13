package vn.softdreams.easypos.dto.invoice.ngp;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PatternNGPResponse {

    private String status;
    private String message;

    @JsonProperty("data")
    private DataObject response;

    public class DataObject {

        private String code;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

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

    public DataObject getResponse() {
        return response;
    }

    public void setResponse(DataObject response) {
        this.response = response;
    }
}
