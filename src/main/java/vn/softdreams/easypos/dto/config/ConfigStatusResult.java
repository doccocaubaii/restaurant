package vn.softdreams.easypos.dto.config;

public class ConfigStatusResult {

    private Integer companyId;
    private Integer status;

    public ConfigStatusResult(Integer companyId, Integer status) {
        this.companyId = companyId;
        this.status = status;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
