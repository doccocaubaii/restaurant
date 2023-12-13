package vn.softdreams.easypos.dto.invoice;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class InvoiceSearchResponse implements Serializable {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("ikey")
    private String ikey;

    public InvoiceSearchResponse() {}

    public InvoiceSearchResponse(Integer id, String ikey) {
        this.id = id;
        this.ikey = ikey;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIkey() {
        return ikey;
    }

    public void setIkey(String ikey) {
        this.ikey = ikey;
    }
}
