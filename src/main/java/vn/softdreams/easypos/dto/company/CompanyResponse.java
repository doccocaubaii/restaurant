package vn.softdreams.easypos.dto.company;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

public class CompanyResponse implements Serializable {

    private Boolean isCurrent = false;
    private Integer id;
    private String name;
    private String address;
    private String phone;
    private String description;
    private Integer businessId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isParent;

    public CompanyResponse() {}

    public CompanyResponse(
        Integer id,
        String name,
        String address,
        String phone,
        String description,
        Integer businessId,
        Boolean isParent
    ) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.description = description;
        this.businessId = businessId;
        this.isParent = isParent;
    }

    public CompanyResponse(
        Boolean isCurrent,
        Integer id,
        String name,
        String address,
        String phone,
        String description,
        Integer businessId
    ) {
        this.isCurrent = isCurrent;
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.description = description;
        this.businessId = businessId;
    }

    public Boolean getParent() {
        return isParent;
    }

    public void setParent(Boolean parent) {
        isParent = parent;
    }

    public Boolean getIsCurrent() {
        return isCurrent;
    }

    public void setIsCurrent(Boolean current) {
        isCurrent = current;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }
}
