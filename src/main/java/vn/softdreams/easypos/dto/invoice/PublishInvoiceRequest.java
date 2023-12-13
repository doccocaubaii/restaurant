package vn.softdreams.easypos.dto.invoice;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class PublishInvoiceRequest implements Serializable {

    @NotNull(message = ExceptionConstants.BILL_ID_NOT_FOUND_VI)
    private Integer invoiceId;

    @NotBlank(message = ExceptionConstants.INVOICE_IKEY_NOT_NULL)
    private String invoiceIKey;

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceIKey() {
        return invoiceIKey;
    }

    public void setInvoiceIKey(String invoiceIKey) {
        this.invoiceIKey = invoiceIKey;
    }
}
