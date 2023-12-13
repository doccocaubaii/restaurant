package vn.softdreams.easypos.dto.invoice;

public class PublishErrorResponse {

    private Integer companyId;
    private Integer invoiceId;
    private String errorMessage;

    public PublishErrorResponse() {}

    public PublishErrorResponse(Integer companyId, Integer invoiceId, String errorMessage) {
        this.companyId = companyId;
        this.invoiceId = invoiceId;
        this.errorMessage = errorMessage;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
