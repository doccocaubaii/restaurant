package vn.softdreams.easypos.dto.companyOwner;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CompanyOwnerRequest {

    private Integer id;

    @NotBlank(message = ExceptionConstants.OWNER_NAME_NOT_EMPTY)
    @Size(min = 5, message = ExceptionConstants.OWNER_NAME_INVALID)
    private String name;

    private String address;

    @Size(max = 14, message = ExceptionConstants.TAX_CODE_IN_VALID)
    private String taxCode;

    private Integer ownerId;

    private String ikey;

    private String taxMachineCode;

    private String taxRegisterTime;

    private String taxRegisterMessage;

    public CompanyOwnerRequest() {}

    public CompanyOwnerRequest(
        Integer id,
        String name,
        String address,
        String taxCode,
        Integer ownerId,
        String ikey,
        String taxMachineCode,
        String taxRegisterTime,
        String taxRegisterMessage
    ) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.taxCode = taxCode;
        this.ownerId = ownerId;
        this.ikey = ikey;
        this.taxMachineCode = taxMachineCode;
        this.taxRegisterTime = taxRegisterTime;
        this.taxRegisterMessage = taxRegisterMessage;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name != null ? name.trim() : null;
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

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public String getIkey() {
        return ikey;
    }

    public void setIkey(String ikey) {
        this.ikey = ikey;
    }

    public String getTaxMachineCode() {
        return taxMachineCode;
    }

    public void setTaxMachineCode(String taxMachineCode) {
        this.taxMachineCode = taxMachineCode;
    }

    public String getTaxRegisterTime() {
        return taxRegisterTime;
    }

    public void setTaxRegisterTime(String taxRegisterTime) {
        this.taxRegisterTime = taxRegisterTime;
    }

    public String getTaxRegisterMessage() {
        return taxRegisterMessage;
    }

    public void setTaxRegisterMessage(String taxRegisterMessage) {
        this.taxRegisterMessage = taxRegisterMessage;
    }
}
