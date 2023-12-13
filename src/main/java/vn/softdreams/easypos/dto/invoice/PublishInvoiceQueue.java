package vn.softdreams.easypos.dto.invoice;

import java.io.Serializable;

public class PublishInvoiceQueue implements Serializable {

    private String comId;
    private String invoiceId;

    public PublishInvoiceQueue() {}

    public PublishInvoiceQueue(String comId, String invoiceId) {
        this.comId = comId;
        this.invoiceId = invoiceId;
    }

    public String getComId() {
        return comId;
    }

    public void setComId(String comId) {
        this.comId = comId;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }
}
