package vn.softdreams.easypos.dto.invoice;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class RegisterEasyInvoiceRequest implements Serializable {

    @JsonProperty
    private String email;

    @JsonProperty
    private String phone;

    @JsonProperty("contracPerson")
    private String contractPerson;

    @JsonProperty
    private String method;

    @JsonProperty
    private declNormalInv declNormalInv;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContractPerson() {
        return contractPerson;
    }

    public void setContractPerson(String contractPerson) {
        this.contractPerson = contractPerson;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public vn.softdreams.easypos.dto.invoice.declNormalInv getDeclNormalInv() {
        return declNormalInv;
    }

    public void setDeclNormalInv(vn.softdreams.easypos.dto.invoice.declNormalInv declNormalInv) {
        this.declNormalInv = declNormalInv;
    }
}
