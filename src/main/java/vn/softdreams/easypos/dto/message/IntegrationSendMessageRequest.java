package vn.softdreams.easypos.dto.message;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.NotBlank;

public class IntegrationSendMessageRequest {

    @NotBlank(message = ExceptionConstants.RECEIVE_INTEGRATION_NOT_NULL)
    private String receive;

    @NotBlank(message = ExceptionConstants.SUBJECT_INTEGRATION_NOT_NULL)
    private String subject;

    @NotBlank(message = ExceptionConstants.TEXT_INTEGRATION_NOT_NULL)
    private String textContent;

    @NotBlank(message = ExceptionConstants.SERVICE_INTEGRATION_NOT_NULL)
    private String service;

    @NotBlank(message = ExceptionConstants.BRANCH_NAME_INTEGRATION_NOT_NULL)
    private String brandName;

    @NotBlank(message = ExceptionConstants.HASH_NOT_EMPTY)
    private String hash;

    public String getReceive() {
        return receive;
    }

    public void setReceive(String receive) {
        this.receive = receive;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}
