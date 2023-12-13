package vn.softdreams.easypos.dto.companyOwner;

import vn.softdreams.easypos.dto.company.CompanyResponse;

import java.io.Serializable;
import java.util.List;

public class OwnerResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String name;

    private String taxCode;

    private String ownerName;
    private Integer userId;

    private List<CompanyResponse> companies;

    public OwnerResponse() {}

    //    public OwnerResponse(
    //        Integer id,
    //        String name,
    //        String taxCode,
    //        String ownerName
    //    ) {
    //        this.id = id;
    //        this.name = name;
    //        this.taxCode = taxCode;
    //        this.ownerName = ownerName;
    //    }

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

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public List<CompanyResponse> getCompanies() {
        return companies;
    }

    public void setCompanies(List<CompanyResponse> companies) {
        this.companies = companies;
    }
}
