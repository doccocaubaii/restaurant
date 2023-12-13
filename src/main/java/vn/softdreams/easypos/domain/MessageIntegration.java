package vn.softdreams.easypos.domain;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A EpMessage.
 */
@Entity
@DynamicUpdate
@Table(name = "message_integration")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MessageIntegration extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "type")
    private Integer type;

    @Column(name = "receive")
    private String receive;

    @Column(name = "cc")
    private String cc;

    @Column(name = "subject")
    private String subject;

    @Column(name = "text_content")
    private String textContent;

    @Column(name = "html_content")
    private String htmlContent;

    @Column(name = "status")
    private Integer status = 0;

    @Column(name = "service")
    private String service;

    @Column(name = "brand_name")
    private String brandName;

    @Column(name = "error_message")
    private String errorMessage;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return this.type;
    }

    public MessageIntegration type(Integer type) {
        this.setType(type);
        return this;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getReceive() {
        return this.receive;
    }

    public void setReceive(String receive) {
        this.receive = receive;
    }

    public String getCc() {
        return this.cc;
    }

    public MessageIntegration cc(String cc) {
        this.setCc(cc);
        return this;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTextContent() {
        return this.textContent;
    }

    public MessageIntegration textContent(String textContent) {
        this.setTextContent(textContent);
        return this;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public String getHtmlContent() {
        return this.htmlContent;
    }

    public MessageIntegration htmlContent(String htmlContent) {
        this.setHtmlContent(htmlContent);
        return this;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public Integer getStatus() {
        return this.status;
    }

    public MessageIntegration status(Integer status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public MessageIntegration errorMessage(String errorMessage) {
        this.setErrorMessage(errorMessage);
        return this;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MessageIntegration)) {
            return false;
        }
        return id != null && id.equals(((MessageIntegration) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EpMessage{" +
            "id=" + getId() +
            ", type=" + getType() +
            ", to='" + getReceive() + "'" +
            ", cc='" + getCc() + "'" +
            ", textContent='" + getTextContent() + "'" +
            ", htmlContent='" + getHtmlContent() + "'" +
            ", status=" + getStatus() +
            ", service=" + getService() +
            ", errorMessage='" + getErrorMessage() + "'" +
            "}";
    }
}
