package vn.softdreams.easypos.dto.authorities;

public class JwtDTO {

    private String sub;
    private Integer companyId;
    private String auth;
    private String companyName;
    private Integer id;
    private Integer exp;
    private String taxCode;
    private String service;
    private Integer passwordVersion;

    public JwtDTO() {}

    public JwtDTO(String sub, Integer companyId, String auth, String companyName, Integer id, Integer exp, String taxCode) {
        this.sub = sub;
        this.companyId = companyId;
        this.auth = auth;
        this.companyName = companyName;
        this.id = id;
        this.exp = exp;
        this.taxCode = taxCode;
    }

    public JwtDTO(
        String sub,
        Integer companyId,
        String auth,
        String companyName,
        Integer id,
        Integer exp,
        String taxCode,
        Integer passwordVersion,
        String service
    ) {
        this.sub = sub;
        this.companyId = companyId;
        this.auth = auth;
        this.companyName = companyName;
        this.id = id;
        this.exp = exp;
        this.taxCode = taxCode;
        this.passwordVersion = passwordVersion;
        this.service = service;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
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

    public Integer getPasswordVersion() {
        return passwordVersion;
    }

    public void setPasswordVersion(Integer passwordVersion) {
        this.passwordVersion = passwordVersion;
    }
}
