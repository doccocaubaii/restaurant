package vn.softdreams.easypos.dto.backup;

public class ImportUrlRequest {

    private String url;
    private Integer comId;
    private String taxCode;

    public ImportUrlRequest() {}

    public ImportUrlRequest(String url, Integer comId) {
        this.url = url;
        this.comId = comId;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }
}
