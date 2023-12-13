package vn.softdreams.easypos.dto.authorities;

import java.io.Serializable;

public class ConfigInvoice implements Serializable {

    private String easyInvoiceUrl;
    private String easyInvoiceLookup;
    private String easyInvoiceAccount;
    private String easyInvoicePass;

    public ConfigInvoice() {}

    public ConfigInvoice(String easyInvoiceUrl, String easyInvoiceLookup, String easyInvoiceAccount, String easyInvoicePass) {
        this.easyInvoiceUrl = easyInvoiceUrl;
        this.easyInvoiceLookup = easyInvoiceLookup;
        this.easyInvoiceAccount = easyInvoiceAccount;
        this.easyInvoicePass = easyInvoicePass;
    }

    public String getEasyInvoiceUrl() {
        return easyInvoiceUrl;
    }

    public void setEasyInvoiceUrl(String easyInvoiceUrl) {
        this.easyInvoiceUrl = easyInvoiceUrl;
    }

    public String getEasyInvoiceLookup() {
        return easyInvoiceLookup;
    }

    public void setEasyInvoiceLookup(String easyInvoiceLookup) {
        this.easyInvoiceLookup = easyInvoiceLookup;
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
}
