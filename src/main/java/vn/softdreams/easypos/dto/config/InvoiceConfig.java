package vn.softdreams.easypos.dto.config;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.List;

public class InvoiceConfig implements Serializable {

    private Integer comId;
    private Integer invoiceType;
    private Integer typeDiscount;
    private String invoicePattern;
    private Integer invoiceMethod;

    @Min(value = 0, message = ExceptionConstants.CONFIG_OVER_STOCK_INVALID)
    @Max(value = 1, message = ExceptionConstants.CONFIG_OVER_STOCK_INVALID)
    private Integer overStock;

    private Integer voucherApply;
    private Integer combineVoucherApply;
    private Integer pushVoucherDiscountInvoice;

    private Integer discountVat;
    private Integer taxReductionType;
    private Integer isBuyer;
    private Integer invDynamicDiscountName;
    private String taxCode;
    private Integer businessType;
    private String displayConfig;

    private List<ExtraConfigItem> serviceChargeConfig;
    private List<ExtraConfigItem> totalAmount;

    public List<ExtraConfigItem> getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(List<ExtraConfigItem> totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<ExtraConfigItem> getServiceChargeConfig() {
        return serviceChargeConfig;
    }

    public void setServiceChargeConfig(List<ExtraConfigItem> serviceChargeConfig) {
        this.serviceChargeConfig = serviceChargeConfig;
    }

    public InvoiceConfig() {}

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public Integer getVoucherApply() {
        return voucherApply;
    }

    public void setVoucherApply(Integer voucherApply) {
        this.voucherApply = voucherApply;
    }

    public Integer getCombineVoucherApply() {
        return combineVoucherApply;
    }

    public void setCombineVoucherApply(Integer combineVoucherApply) {
        this.combineVoucherApply = combineVoucherApply;
    }

    public Integer getPushVoucherDiscountInvoice() {
        return pushVoucherDiscountInvoice;
    }

    public void setPushVoucherDiscountInvoice(Integer pushVoucherDiscountInvoice) {
        this.pushVoucherDiscountInvoice = pushVoucherDiscountInvoice;
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

    public Integer getOverStock() {
        return overStock;
    }

    public void setOverStock(Integer overStock) {
        this.overStock = overStock;
    }

    public Integer getDiscountVat() {
        return discountVat;
    }

    public void setDiscountVat(Integer discountVat) {
        this.discountVat = discountVat;
    }

    public Integer getTaxReductionType() {
        return taxReductionType;
    }

    public void setTaxReductionType(Integer taxReductionType) {
        this.taxReductionType = taxReductionType;
    }

    public Integer getIsBuyer() {
        return isBuyer;
    }

    public void setIsBuyer(Integer isBuyer) {
        this.isBuyer = isBuyer;
    }

    public Integer getInvDynamicDiscountName() {
        return invDynamicDiscountName;
    }

    public void setInvDynamicDiscountName(Integer invDynamicDiscountName) {
        this.invDynamicDiscountName = invDynamicDiscountName;
    }

    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public String getDisplayConfig() {
        return displayConfig;
    }

    public void setDisplayConfig(String displayConfig) {
        this.displayConfig = displayConfig;
    }
}
