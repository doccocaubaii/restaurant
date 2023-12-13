package vn.softdreams.easypos.dto;

import java.time.LocalDate;

public class ReportFileDataRequest {

    private Boolean isOnlyGetData;
    private Integer itemsPerPage;
    private Integer page;
    private LocalDate fromDate;
    private LocalDate toDate;
    private Boolean isCustomForm;
    private Integer typeReportConfig;
    private Integer type;
    private String token;
    private Integer currentBook;

    private Integer comId;
    private String pattern;
    private Integer status;
    private Integer taxCheckStatus;

    public Boolean getIsOnlyGetData() {
        return isOnlyGetData;
    }

    public void setIsOnlyGetData(Boolean onlyGetData) {
        isOnlyGetData = onlyGetData;
    }

    public Integer getItemsPerPage() {
        return itemsPerPage;
    }

    public void setItemsPerPage(Integer itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Boolean getCustomForm() {
        return isCustomForm;
    }

    public void setCustomForm(Boolean customForm) {
        isCustomForm = customForm;
    }

    public Integer getTypeReportConfig() {
        return typeReportConfig;
    }

    public void setTypeReportConfig(Integer typeReportConfig) {
        this.typeReportConfig = typeReportConfig;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getCurrentBook() {
        return currentBook;
    }

    public void setCurrentBook(Integer currentBook) {
        this.currentBook = currentBook;
    }

    public Boolean getOnlyGetData() {
        return isOnlyGetData;
    }

    public void setOnlyGetData(Boolean onlyGetData) {
        isOnlyGetData = onlyGetData;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
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
