package vn.softdreams.easypos.integration.easybooks88.api.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

public class UpdateAccountingObjectRequest {

    //TODO: Hiện tại Pattern vẫn đang check theo chuẩn tự sinh code của ứng dụng, nếu hỗ trợ code2 do KH tự tạo, phải bỏ đi hoặc có pattern khác
    @NotEmpty(message = "code must be not empty")
    @Pattern(regexp = "^((EPKH|EPKHNCC|EPNCC)(\\d){1,18})|(\\d+_\\d+)$", message = "Invalid accountingObject code format")
    private String code;

    @NotBlank(message = "Name must be not blank")
    private String name;

    private String phone;

    private String address;

    @DecimalMin(value = "0.0")
    private BigDecimal opnAmount;

    private boolean isActive;

    private final String CODE_PREFIX = "EP";

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = CODE_PREFIX + code;
    }

    public String getName() {
        return name;
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

    public BigDecimal getOpnAmount() {
        return opnAmount;
    }

    public void setOpnAmount(BigDecimal opnAmount) {
        this.opnAmount = opnAmount;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
