package vn.softdreams.easypos.dto.config;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.NotBlank;

public class DeviceCode {

    @NotBlank(message = ExceptionConstants.TAX_CODE_IN_VALID)
    private String taxCode;

    @NotBlank(message = ExceptionConstants.DEVICE_CODE_NOT_NULL)
    private String name;

    private String deviceCode;

    public DeviceCode() {}

    public DeviceCode(String taxCode, String name) {
        this.taxCode = taxCode;
        this.name = name;
    }

    public DeviceCode(String taxCode, String name, String deviceCode) {
        this.taxCode = taxCode;
        this.name = name;
        this.deviceCode = deviceCode;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
