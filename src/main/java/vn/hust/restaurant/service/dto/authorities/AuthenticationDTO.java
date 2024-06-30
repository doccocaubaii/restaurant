package vn.hust.restaurant.service.dto.authorities;

import java.util.List;
import org.springframework.security.core.Authentication;
import vn.hust.restaurant.service.dto.company.CompanyResult;

public class AuthenticationDTO {

    private Authentication authentication;
    private List<CompanyResult> companies;
    private Integer id;
    private boolean isActivate = false;
    private String companyName;
    private Integer companyId;
    private String fullName;
    private String username;
    private String taxCode;

    public AuthenticationDTO() {}

    public AuthenticationDTO(Authentication authentication, Integer id) {
        this.authentication = authentication;
        this.id = id;
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
}
