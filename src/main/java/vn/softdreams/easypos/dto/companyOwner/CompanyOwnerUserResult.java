package vn.softdreams.easypos.dto.companyOwner;

import java.io.Serializable;

public class CompanyOwnerUserResult implements Serializable {

    private Integer id;

    private String name;

    private String taxCode;
    private String address;

    public CompanyOwnerUserResult() {}

    public CompanyOwnerUserResult(Integer id, String name, String taxCode, String address) {
        this.id = id;
        this.name = name;
        this.taxCode = taxCode;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }
}
