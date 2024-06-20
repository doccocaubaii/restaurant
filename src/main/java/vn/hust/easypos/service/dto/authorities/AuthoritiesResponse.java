package vn.hust.easypos.service.dto.authorities;

import java.io.Serializable;
import java.util.List;
import vn.hust.easypos.service.dto.company.CompanyResult;

public class AuthoritiesResponse implements Serializable {
    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    private String id_token;
    private boolean isActivate;
    private String message;
    private String fullName;
    private String userName;
    private String reason;
    private String companyName;
    private Integer companyId;
    private Integer id;
    private List<CompanyResult> companies;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public AuthoritiesResponse() {}

    public AuthoritiesResponse(boolean isActivate, String message) {
        this.isActivate = isActivate;
        this.message = message;
    }

    public boolean isActivate() {
        return isActivate;
    }

    public void setActivate(boolean activate) {
        isActivate = activate;
    }

    public String getId_token() {
        return id_token;
    }

    public void setId_token(String id_token) {
        this.id_token = id_token;
    }

    public List<CompanyResult> getCompanies() {
        return companies;
    }

    public void setCompanies(List<CompanyResult> companies) {
        this.companies = companies;
    }
}
