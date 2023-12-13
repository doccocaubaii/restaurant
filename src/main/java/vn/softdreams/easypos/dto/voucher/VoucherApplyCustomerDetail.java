package vn.softdreams.easypos.dto.voucher;

import java.io.Serializable;

public class VoucherApplyCustomerDetail implements Serializable {

    private Integer id;
    private Integer voucherId;
    private Integer customerId;
    private String customerName;
    private String taxCode;
    private String code2;

    public VoucherApplyCustomerDetail() {}

    public VoucherApplyCustomerDetail(Integer id, Integer voucherId, Integer customerId, String customerName, String taxCode) {
        this.id = id;
        this.voucherId = voucherId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.taxCode = taxCode;
    }

    public String getCode2() {
        return code2;
    }

    public void setCode2(String code2) {
        this.code2 = code2;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(Integer voucherId) {
        this.voucherId = voucherId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }
}
