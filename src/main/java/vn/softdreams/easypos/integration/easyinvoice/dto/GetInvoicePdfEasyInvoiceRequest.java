package vn.softdreams.easypos.integration.easyinvoice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class GetInvoicePdfEasyInvoiceRequest implements Serializable {

    @JsonProperty("Ikey")
    private String ikey;

    @JsonProperty("Pattern")
    private String pattern;

    @JsonProperty("Option")
    private String option;

    public String getIkey() {
        return ikey;
    }

    public void setIkey(String ikey) {
        this.ikey = ikey;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }
}
