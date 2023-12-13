package vn.softdreams.easypos.integration.easybooks88.api.dto;

import vn.softdreams.easypos.constants.CustomerConstants;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

public class CreateAccountingObjectRequest {

    @NotNull(message = "type must be not empty")
    private Integer type;

    //TODO: Hiện tại Pattern vẫn đang check theo chuẩn tự sinh code của ứng dụng, nếu hỗ trợ code2 do KH tự tạo, phải bỏ đi hoặc có pattern khác
    @NotEmpty(message = "code must be not empty")
    @Pattern(regexp = "^((EPKH|EPKHNCC|EPNCC)(\\d){1,18})|(\\d+_\\d+)$", message = "Invalid accountingObject code format")
    private String code;

    @NotEmpty(message = "name must be not empty")
    private String name;

    private String phone;
    private String address;
    private BigDecimal opnAmount;

    private final String CODE_PREFIX = "EP";

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

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

    public enum ObjectType {
        CUSTOMER(CustomerConstants.Type.CUSTOMER),
        SUPPLIER(CustomerConstants.Type.SUPPLIER),
        CUSTOMER_SUPPLIER(CustomerConstants.Type.CUSTOMER_AND_SUPPLIER),
        EMPLOYEE(0);

        private final Integer type;

        ObjectType(Integer type) {
            this.type = type;
        }

        public Integer getType() {
            return type;
        }
    }
}
