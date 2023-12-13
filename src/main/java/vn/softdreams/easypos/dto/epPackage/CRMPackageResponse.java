package vn.softdreams.easypos.dto.epPackage;

public class CRMPackageResponse {

    private Integer package_id;
    private Integer status;
    private String message;

    public CRMPackageResponse(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public CRMPackageResponse(Integer package_id, Integer status, String message) {
        this.package_id = package_id;
        this.status = status;
        this.message = message;
    }

    public Integer getPackage_id() {
        return package_id;
    }

    public void setPackage_id(Integer package_id) {
        this.package_id = package_id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
