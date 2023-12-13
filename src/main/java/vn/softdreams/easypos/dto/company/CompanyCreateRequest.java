package vn.softdreams.easypos.dto.company;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

import static vn.softdreams.easypos.web.rest.errors.ExceptionConstants.PHONE_NUMBER_IN_VALID;

public class CompanyCreateRequest implements Serializable {

    @NotNull(message = ExceptionConstants.OWNER_ID_NOT_NULL)
    private Integer comOwnerId;

    @NotBlank(message = ExceptionConstants.COMPANY_NAME_NOT_EMPTY)
    private String name;

    private Integer businessId;

    private String address;

    @Size(min = 10, max = 14, message = PHONE_NUMBER_IN_VALID)
    private String phone;

    private String description;

    public CompanyCreateRequest() {}

    public CompanyCreateRequest(Integer comOwnerId, String name, Integer businessId, String address, String phone, String description) {
        this.comOwnerId = comOwnerId;
        this.name = name;
        this.businessId = businessId;
        this.address = address;
        this.phone = phone;
        this.description = description;
    }

    public String getName() {
        return name.trim();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone == null ? null : phone.trim();
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address == null ? null : address.trim();
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

    public String getDescription() {
        return description == null ? null : description.trim();
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
