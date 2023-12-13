package vn.softdreams.easypos.service.dto;

import java.util.Set;

public class ReportFileDataResponse {

    private Object data;
    private Object allData;
    private String headerSetting;
    private String headerOrg;
    private String headerAddInfoOrg;
    private String circulars;
    private String period;
    private String subTitle;
    private String accountNumber;
    private String reportName;
    private String unitName;
    private String repository;
    private String materialGoodsCode;
    private String materialGoodsName;
    private String director;
    private String chiefAccountant;
    private String reporter;
    private String mauSoAm;
    private Object dataAccount;
    private Object dataAccountList;
    private byte[] reportPDF;
    private Object listGroup;
    private Integer totalIteml;
    private Set<String> columnNames;
    private Boolean isEmptyData;
    private String customField1;
    private String customField2;
    private String customField3;
    private String status;
    private String message;

    public String getCustomField1() {
        return customField1;
    }

    public void setCustomField1(String customField1) {
        this.customField1 = customField1;
    }

    public String getCustomField2() {
        return customField2;
    }

    private String currencyCode;

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public ReportFileDataResponse() {}

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getHeaderSetting() {
        return headerSetting;
    }

    public void setHeaderSetting(String headerSetting) {
        this.headerSetting = headerSetting;
    }

    public String getCirculars() {
        return circulars;
    }

    public void setCirculars(String circulars) {
        this.circulars = circulars;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getChiefAccountant() {
        return chiefAccountant;
    }

    public void setChiefAccountant(String chiefAccountant) {
        this.chiefAccountant = chiefAccountant;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public String getMauSoAm() {
        return mauSoAm;
    }

    public void setMauSoAm(String mauSoAm) {
        this.mauSoAm = mauSoAm;
    }

    public byte[] getReportPDF() {
        return reportPDF;
    }

    public void setReportPDF(byte[] reportPDF) {
        this.reportPDF = reportPDF;
    }

    public Object getDataAccount() {
        return dataAccount;
    }

    public void setDataAccount(Object dataAccount) {
        this.dataAccount = dataAccount;
    }

    public Object getDataAccountList() {
        return dataAccountList;
    }

    public void setDataAccountList(Object dataAccountList) {
        this.dataAccountList = dataAccountList;
    }

    public String getHeaderOrg() {
        return headerOrg;
    }

    public void setHeaderOrg(String headerOrg) {
        this.headerOrg = headerOrg;
    }

    public String getHeaderAddInfoOrg() {
        return headerAddInfoOrg;
    }

    public void setHeaderAddInfoOrg(String headerAddInfoOrg) {
        this.headerAddInfoOrg = headerAddInfoOrg;
    }

    public Object getAllData() {
        return allData;
    }

    public void setAllData(Object allData) {
        this.allData = allData;
    }

    public Object getListGroup() {
        return listGroup;
    }

    public void setListGroup(Object listGroup) {
        this.listGroup = listGroup;
    }

    public Integer getTotalIteml() {
        return totalIteml;
    }

    public void setTotalIteml(Integer totalIteml) {
        this.totalIteml = totalIteml;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public String getMaterialGoodsCode() {
        return materialGoodsCode;
    }

    public void setMaterialGoodsCode(String materialGoodsCode) {
        this.materialGoodsCode = materialGoodsCode;
    }

    public String getMaterialGoodsName() {
        return materialGoodsName;
    }

    public void setMaterialGoodsName(String materialGoodsName) {
        this.materialGoodsName = materialGoodsName;
    }

    public Set<String> getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(Set<String> columnNames) {
        this.columnNames = columnNames;
    }

    public Boolean getEmptyData() {
        return isEmptyData;
    }

    public void setEmptyData(Boolean emptyData) {
        isEmptyData = emptyData;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
