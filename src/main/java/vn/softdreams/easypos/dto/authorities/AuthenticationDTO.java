package vn.softdreams.easypos.dto.authorities;

import org.springframework.security.core.Authentication;
import vn.softdreams.easypos.dto.company.CompanyResult;

import java.util.List;
import java.util.Set;

public class AuthenticationDTO {

    private Authentication authentication;
    private List<CompanyResult> companies;
    private Integer id;
    private boolean isActivate = false;
    private String companyName;
    private Integer companyId;
    private String fullName;
    private String username;
    private String role;
    private String permissions;
    private Set<String> permissionList;
    private String taxCode;
    private Integer passwordVersion;
    private String service;

    public AuthenticationDTO() {}

    public AuthenticationDTO(Authentication authentication, Integer id) {
        this.authentication = authentication;
        this.id = id;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public Set<String> getPermissionList() {
        return permissionList;
    }

    public void setPermissionList(Set<String> permissionList) {
        this.permissionList = permissionList;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public boolean isActivate() {
        return isActivate;
    }

    public void setActivate(boolean activate) {
        isActivate = activate;
    }

    public List<CompanyResult> getCompanies() {
        return companies;
    }

    public void setCompanies(List<CompanyResult> companies) {
        this.companies = companies;
    }

    public Authentication getAuthentication() {
        return authentication;
    }

    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPasswordVersion() {
        return passwordVersion;
    }

    public void setPasswordVersion(Integer passwordVersion) {
        this.passwordVersion = passwordVersion;
    }
}
