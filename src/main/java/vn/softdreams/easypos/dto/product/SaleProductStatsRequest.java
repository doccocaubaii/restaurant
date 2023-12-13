package vn.softdreams.easypos.dto.product;

import java.io.Serializable;

public class SaleProductStatsRequest implements Serializable {

    private Integer comId;
    private String fromDate;
    private String toDate;
    private Integer status;
    private Integer taxCheckStatus;
    private String pattern;

    public SaleProductStatsRequest() {}

    public SaleProductStatsRequest(Integer comId, String fromDate, String toDate, Integer status, Integer taxCheckStatus, String pattern) {
        this.comId = comId;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.status = status;
        this.taxCheckStatus = taxCheckStatus;
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getTaxCheckStatus() {
        return taxCheckStatus;
    }

    public void setTaxCheckStatus(Integer taxCheckStatus) {
        this.taxCheckStatus = taxCheckStatus;
    }
}
