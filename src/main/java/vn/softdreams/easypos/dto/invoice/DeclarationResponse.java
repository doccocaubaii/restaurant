package vn.softdreams.easypos.dto.invoice;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class DeclarationResponse implements Serializable {

    @JsonProperty("TCTCheckStatus")
    private Integer checkStatus;

    @JsonProperty("IsCashRegister")
    private Boolean isCashRegister;

    @JsonProperty("TaxAuthorityCode")
    private String taxAuthorityCode;

    public DeclarationResponse() {}

    public Integer getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(Integer checkStatus) {
        this.checkStatus = checkStatus;
    }

    public Boolean getCashRegister() {
        return isCashRegister;
    }

    public void setCashRegister(Boolean cashRegister) {
        isCashRegister = cashRegister;
    }

    public String getTaxAuthorityCode() {
        return taxAuthorityCode;
    }

    public void setTaxAuthorityCode(String taxAuthorityCode) {
        this.taxAuthorityCode = taxAuthorityCode;
    }
}
