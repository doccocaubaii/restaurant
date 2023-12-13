package vn.softdreams.easypos.dto.invoice;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DiscountAmountInvoice {

    @JsonProperty("CheckDiscount")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer checkDiscount;

    @JsonProperty("DiscountVatRate")
    private String discountVatRate;

    @JsonProperty("TotalDiscount")
    private String totalDiscount;

    public DiscountAmountInvoice() {}

    public DiscountAmountInvoice(String discountVatRate, String totalDiscount) {
        this.discountVatRate = discountVatRate;
        this.totalDiscount = totalDiscount;
    }

    public DiscountAmountInvoice(Integer checkDiscount, String discountVatRate, String totalDiscount) {
        this.checkDiscount = checkDiscount;
        this.discountVatRate = discountVatRate;
        this.totalDiscount = totalDiscount;
    }

    public Integer getCheckDiscount() {
        return checkDiscount;
    }

    public void setCheckDiscount(Integer checkDiscount) {
        this.checkDiscount = checkDiscount;
    }

    public String getDiscountVatRate() {
        return discountVatRate;
    }

    public void setDiscountVatRate(String discountVatRate) {
        this.discountVatRate = discountVatRate;
    }

    public String getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(String totalDiscount) {
        this.totalDiscount = totalDiscount;
    }
}
