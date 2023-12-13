package vn.softdreams.easypos.dto.company;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static vn.softdreams.easypos.web.rest.errors.ExceptionConstants.PHONE_NUMBER_IN_VALID;

public class CompanyUpdateRequest {

    @NotNull(message = ExceptionConstants.OWNER_ID_NOT_NULL)
    private Integer comOwnerId;

    @NotNull(message = ExceptionConstants.COMPANY_ID_NOT_NULL)
    private Integer comId;

    private String name;

    private Integer businessId;

    private String address;

    @Size(min = 10, max = 14, message = PHONE_NUMBER_IN_VALID)
    private String phone;

    private String description;

    public CompanyUpdateRequest() {}

    public CompanyUpdateRequest(
        Integer comOwnerId,
        Integer comId,
        String name,
        Integer businessId,
        String address,
        String phone,
        String description
    ) {
        this.comOwnerId = comOwnerId;
        this.comId = comId;
        this.name = name;
        this.businessId = businessId;
        this.address = address;
        this.phone = phone;
        this.description = description;
    }

    public String getName() {
        return name != null ? name.trim() : null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getComOwnerId() {
        return comOwnerId;
    }

    public void setComOwnerId(Integer comOwnerId) {
        this.comOwnerId = comOwnerId;
    }

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
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
