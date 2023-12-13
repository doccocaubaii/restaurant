package vn.softdreams.easypos.integration.easybooks88.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class AuthenticateRequest implements Serializable {

    @JsonProperty("orgID")
    @NotNull(message = ExceptionConstants.CONFIG_COMPANY_NULL_EB88)
    private Long orgId;

    @JsonProperty("username")
    @NotEmpty(message = ExceptionConstants.CONFIG_USERNAME_INVALID_EB88)
    @Size(min = 4, max = 50, message = ExceptionConstants.CONFIG_USERNAME_INVALID_EB88)
    private String username;

    @JsonProperty("clientID")
    @NotEmpty
    @Size(min = 1, max = 100)
    private String clientId;

    @JsonProperty("clientSecret")
    @NotEmpty
    @Size(min = 4, max = 100)
    private String clientSecret;

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }
}
