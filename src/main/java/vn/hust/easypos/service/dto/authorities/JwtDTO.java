package vn.hust.easypos.service.dto.authorities;

public class JwtDTO {

    private Integer companyId;
    private String auth;
    private String companyName;
    private Integer id;
    private Integer exp;

    public JwtDTO() {}

    public JwtDTO(Integer companyId, String auth, String companyName, Integer id, Integer exp) {
        this.companyId = companyId;
        this.auth = auth;
        this.companyName = companyName;
        this.id = id;
        this.exp = exp;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getExp() {
        return exp;
    }

    public void setExp(Integer exp) {
        this.exp = exp;
    }
}
