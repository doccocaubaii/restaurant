package vn.softdreams.easypos.dto.companyUser;

import com.fasterxml.jackson.annotation.JsonFormat;
import vn.softdreams.easypos.config.Constants;
import vn.softdreams.easypos.integration.easyinvoice.dto.GetInvoicePatternsEasyInvoiceResponse;

import java.time.ZonedDateTime;
import java.util.List;

public class CompanyOwnerResponse {

    private Integer comId;
    private String name;
    private String taxCode;
    private String taxRegisterCode;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    private ZonedDateTime taxRegisterTime;

    private String taxRegisterMessage;
    private Integer roundScaleAmount;
    private Integer roundScaleUnitPrice;
    private Integer roundScaleQuantity;
    private String easyInvoiceUrl;
    private String easyInvoiceAccount;
    private String easyInvoicePass;
    private String discountVat;
    private String invoiceType;
    private List<GetInvoicePatternsEasyInvoiceResponse> patterns;

    public CompanyOwnerResponse() {}

    public CompanyOwnerResponse(
        Integer comId,
        String name,
        String taxCode,
        String taxRegisterCode,
        ZonedDateTime taxRegisterTime,
        String taxRegisterMessage
    ) {
        this.comId = comId;
        this.name = name;
        this.taxCode = taxCode;
        this.taxRegisterCode = taxRegisterCode;
        this.taxRegisterTime = taxRegisterTime;
        this.taxRegisterMessage = taxRegisterMessage;
    }

    public CompanyOwnerResponse(
        Integer comId,
        String name,
        String taxCode,
        String taxRegisterCode,
        ZonedDateTime taxRegisterTime,
        String taxRegisterMessage,
        List<GetInvoicePatternsEasyInvoiceResponse> patterns,
        Integer roundScaleAmount,
        Integer roundScaleUnitPrice,
        Integer roundScaleQuantity
    ) {
        this.comId = comId;
        this.name = name;
        this.taxCode = taxCode;
        this.taxRegisterCode = taxRegisterCode;
        this.taxRegisterTime = taxRegisterTime;
        this.taxRegisterMessage = taxRegisterMessage;
        this.patterns = patterns;
        this.roundScaleAmount = roundScaleAmount;
        this.roundScaleUnitPrice = roundScaleUnitPrice;
        this.roundScaleQuantity = roundScaleQuantity;
    }

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getTaxRegisterCode() {
        return taxRegisterCode;
    }

    public void setTaxRegisterCode(String taxRegisterCode) {
        this.taxRegisterCode = taxRegisterCode;
    }

    public ZonedDateTime getTaxRegisterTime() {
        return taxRegisterTime;
    }

    public void setTaxRegisterTime(ZonedDateTime taxRegisterTime) {
        this.taxRegisterTime = taxRegisterTime;
    }

    public String getTaxRegisterMessage() {
        return taxRegisterMessage;
    }

    public void setTaxRegisterMessage(String taxRegisterMessage) {
        this.taxRegisterMessage = taxRegisterMessage;
    }

    public List<GetInvoicePatternsEasyInvoiceResponse> getPatterns() {
        return patterns;
    }

    public void setPatterns(List<GetInvoicePatternsEasyInvoiceResponse> patterns) {
        this.patterns = patterns;
    }

    public Integer getRoundScaleAmount() {
        return roundScaleAmount;
    }

    public void setRoundScaleAmount(Integer roundScaleAmount) {
        this.roundScaleAmount = roundScaleAmount;
    }

    public Integer getRoundScaleUnitPrice() {
        return roundScaleUnitPrice;
    }

    public void setRoundScaleUnitPrice(Integer roundScaleUnitPrice) {
        this.roundScaleUnitPrice = roundScaleUnitPrice;
    }

    public Integer getRoundScaleQuantity() {
        return roundScaleQuantity;
    }

    public void setRoundScaleQuantity(Integer roundScaleQuantity) {
        this.roundScaleQuantity = roundScaleQuantity;
    }

    public String getEasyInvoiceUrl() {
        return easyInvoiceUrl;
    }

    public void setEasyInvoiceUrl(String easyInvoiceUrl) {
        this.easyInvoiceUrl = easyInvoiceUrl;
    }

    public String getEasyInvoiceAccount() {
        return easyInvoiceAccount;
    }

    public void setEasyInvoiceAccount(String easyInvoiceAccount) {
        this.easyInvoiceAccount = easyInvoiceAccount;
    }

    public String getEasyInvoicePass() {
        return easyInvoicePass;
    }

    public void setEasyInvoicePass(String easyInvoicePass) {
        this.easyInvoicePass = easyInvoicePass;
    }

    public String getDiscountVat() {
        return discountVat;
    }

    public void setDiscountVat(String discountVat) {
        this.discountVat = discountVat;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }
}
