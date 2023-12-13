package vn.softdreams.easypos.integration.easybooks88.queue.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class SAInvoiceTask implements Serializable {

    @JsonProperty
    private String comId;

    @JsonProperty
    private String saInvoiceId;

    public String getComId() {
        return comId;
    }

    public void setComId(String comId) {
        this.comId = comId;
    }

    public String getSaInvoiceId() {
        return saInvoiceId;
    }

    public void setSaInvoiceId(String saInvoiceId) {
        this.saInvoiceId = saInvoiceId;
    }
}
