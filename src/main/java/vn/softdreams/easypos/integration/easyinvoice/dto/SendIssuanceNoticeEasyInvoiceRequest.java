package vn.softdreams.easypos.integration.easyinvoice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Map;

public class SendIssuanceNoticeEasyInvoiceRequest implements Serializable {

    @JsonProperty
    private Map<String, String> ikeyEmail;

    public Map<String, String> getIkeyEmail() {
        return ikeyEmail;
    }

    public void setIkeyEmail(Map<String, String> ikeyEmail) {
        this.ikeyEmail = ikeyEmail;
    }
}
