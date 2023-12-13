package vn.softdreams.easypos.dto.authorities;

import java.io.Serializable;

public class LoginEasyInvoice implements Serializable {

    private Integer companyOwnerId;
    private String username;
    private String password;
    private String url;
    private String service;

    public LoginEasyInvoice() {}

    public LoginEasyInvoice(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public LoginEasyInvoice(Integer companyOwnerId, String username, String password) {
        this.companyOwnerId = companyOwnerId;
        this.username = username;
        this.password = password;
    }

    public Integer getCompanyOwnerId() {
        return companyOwnerId;
    }

    public void setCompanyOwnerId(Integer companyOwnerId) {
        this.companyOwnerId = companyOwnerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
