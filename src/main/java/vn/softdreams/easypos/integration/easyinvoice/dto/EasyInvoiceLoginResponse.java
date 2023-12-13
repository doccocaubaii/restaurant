package vn.softdreams.easypos.integration.easyinvoice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class EasyInvoiceLoginResponse implements Serializable {

    @JsonProperty("Code")
    private Integer code;

    public EasyInvoiceLoginResponse() {}

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
