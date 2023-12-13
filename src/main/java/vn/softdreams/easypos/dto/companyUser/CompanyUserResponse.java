package vn.softdreams.easypos.dto.companyUser;

import java.io.Serializable;

public class CompanyUserResponse implements Serializable {

    private String id;
    private String companyId;
    private String companyName;
    private String userId;
    private String username;

    public CompanyUserResponse() {}

    // constructor for CompanyUser in CompanyUserManagement
    public CompanyUserResponse(String id, String companyId, String companyName, String userId, String username) {
        this.id = id;
        this.companyId = companyId;
        this.companyName = companyName;
        this.userId = userId;
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
