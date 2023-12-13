package vn.softdreams.easypos.dto.invoice;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CompanyUrlResponse {

    @JsonProperty(value = "PublishDomain")
    private String publishDomain;

    @JsonProperty(value = "PortalLink")
    private String portalLink;

    public String getPortalLink() {
        return portalLink;
    }

    public void setPortalLink(String portalLink) {
        this.portalLink = portalLink;
    }

    public String getPublishDomain() {
        return publishDomain;
    }

    public void setPublishDomain(String publishDomain) {
        this.publishDomain = publishDomain;
    }
}
