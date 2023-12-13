package vn.softdreams.easypos.integration.easyinvoice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class GetInvoicePatternsEasyInvoiceResponse implements Serializable {

    @JsonProperty("startDate")
    private String startDate;

    @JsonProperty("pattern")
    private String pattern;

    @JsonProperty("status")
    private int status;

    public GetInvoicePatternsEasyInvoiceResponse() {}

    public GetInvoicePatternsEasyInvoiceResponse(String startDate, String pattern, int status) {
        this.startDate = startDate;
        this.pattern = pattern;
        this.status = status;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
