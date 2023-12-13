package vn.softdreams.easypos.dto.authorities;

import java.io.Serializable;

public class StoreConfiguration implements Serializable {

    private Integer typeDiscount = 0;
    private String invoicePattern;
    private Integer invoiceType = 0;
    private Integer invoiceMethod = 0;

    public StoreConfiguration() {}

    public StoreConfiguration(Integer typeDiscount, String invoicePattern, Integer invoiceType, Integer invoiceMethod) {
        this.typeDiscount = typeDiscount;
        this.invoicePattern = invoicePattern;
        this.invoiceType = invoiceType;
        this.invoiceMethod = invoiceMethod;
    }

    public Integer getTypeDiscount() {
        return typeDiscount;
    }

    public void setTypeDiscount(Integer typeDiscount) {
        this.typeDiscount = typeDiscount;
    }

    public String getInvoicePattern() {
        return invoicePattern;
    }

    public void setInvoicePattern(String invoicePattern) {
        this.invoicePattern = invoicePattern;
    }

    public Integer getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(Integer invoiceType) {
        this.invoiceType = invoiceType;
    }

    public Integer getInvoiceMethod() {
        return invoiceMethod;
    }

    public void setInvoiceMethod(Integer invoiceMethod) {
        this.invoiceMethod = invoiceMethod;
    }
}
