package vn.softdreams.easypos.dto.invoice;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class SendIssuanceNoticeRequest {

    @NotNull(message = ExceptionConstants.BILL_ID_NOT_FOUND_VI)
    private int invoiceId;

    @NotEmpty(message = ExceptionConstants.INVOICE_IKEY_NOT_NULL)
    private String ikey;

    @NotEmpty(message = "Email không được bỏ trống")
    private String emails;

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getIkey() {
        return ikey;
    }

    public void setIkey(String ikey) {
        this.ikey = ikey;
    }

    public String getEmails() {
        return emails;
    }

    public void setEmails(String emails) {
        this.emails = emails;
    }
}
