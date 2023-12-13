package vn.softdreams.easypos.dto.customer;

import java.io.Serializable;

public class CustomerItemResult implements Serializable {

    private Integer customerId;
    private String customerName;
    private String taxCode;
    private String code2;

    public CustomerItemResult(Integer customerId, String customerName, String taxCode, String code2) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.taxCode = taxCode;
        this.code2 = code2;
    }

    public String getCode2() {
        return code2;
    }

    public void setCode2(String code2) {
        this.code2 = code2;
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
