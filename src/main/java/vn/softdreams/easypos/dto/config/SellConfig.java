package vn.softdreams.easypos.dto.config;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class SellConfig {

    @NotNull(message = ExceptionConstants.COMPANY_ID_NOT_NULL)
    private Integer comId;

    @Min(value = 0, message = ExceptionConstants.CONFIG_OVER_STOCK_INVALID)
    @Max(value = 1, message = ExceptionConstants.CONFIG_OVER_STOCK_INVALID)
    private Integer overStock;

    private Integer discountVat;
    private Integer taxReductionType;
    private Integer isBuyer;
    private Integer invDynamicDiscountName;
    private Integer typeDiscount;

    private Integer voucherApply;
    private Integer combineVoucherApply;
    private Integer pushVoucherDiscountInvoice;

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
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

    public Integer getTypeDiscount() {
        return typeDiscount;
    }

    public void setTypeDiscount(Integer typeDiscount) {
        this.typeDiscount = typeDiscount;
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
}
