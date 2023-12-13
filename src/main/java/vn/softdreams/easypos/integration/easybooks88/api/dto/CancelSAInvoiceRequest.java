package vn.softdreams.easypos.integration.easybooks88.api.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class CancelSAInvoiceRequest {

    @NotEmpty(message = "code must be not empty")
    @Pattern(regexp = "^EPDH(\\d){1,18}$", message = "Invalid SAInvoice code format")
    private String code;

    //TODO: Hiện tại Pattern vẫn đang check theo chuẩn tự sinh code của ứng dụng, nếu hỗ trợ code2 do KH tự tạo, phải bỏ đi hoặc có pattern khác
    @NotEmpty(message = "customerCode must be not empty")
    @Pattern(regexp = "^EPKH(\\d){1,18}$", message = "Invalid customer code format")
    private String customerCode;

    @NotEmpty(message = "date must be not empty")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Invalid format date")
    private String date;

    private final String CODE_PREFIX = "EP";

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = CODE_PREFIX + code;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = CODE_PREFIX + customerCode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
