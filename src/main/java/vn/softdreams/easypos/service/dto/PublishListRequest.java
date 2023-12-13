package vn.softdreams.easypos.service.dto;

import java.util.List;

public class PublishListRequest {

    private Integer taxCheckStatus;
    private String fromDate;
    private String toDate;
    private String pattern;
    private String customerName;
    private String no;
    private List<Integer> listID;
    private Boolean paramCheckAll;

    public PublishListRequest() {}

    public Integer getTaxCheckStatus() {
        return taxCheckStatus;
    }

    public void setTaxCheckStatus(Integer taxCheckStatus) {
        this.taxCheckStatus = taxCheckStatus;
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

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public List<Integer> getListID() {
        return listID;
    }

    public void setListID(List<Integer> listID) {
        this.listID = listID;
    }

    public Boolean getParamCheckAll() {
        return paramCheckAll;
    }

    public void setParamCheckAll(Boolean paramCheckAll) {
        this.paramCheckAll = paramCheckAll;
    }
}
