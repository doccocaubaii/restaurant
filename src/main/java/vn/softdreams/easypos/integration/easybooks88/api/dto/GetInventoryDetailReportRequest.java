package vn.softdreams.easypos.integration.easybooks88.api.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class GetInventoryDetailReportRequest {

    @NotNull(message = "companyID must be not null")
    private Integer companyID;

    @NotEmpty(message = "fromDate must be not empty")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Invalid format fromDate")
    private String fromDate;

    @NotEmpty(message = "toDate must be not empty")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Invalid format toDate")
    private String toDate;

    private String repositoryID;
    private boolean dependent = false;
    private String excelName = "TONG_HOP_TON_KHO";
    private boolean isCheckAll = true;
    private Long[] listMaterialGoods = {};
    private String typeReport = "TONG_HOP_TON_KHO";
    private Integer unitType = 0;

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
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

    public String getRepositoryID() {
        return repositoryID;
    }

    public void setRepositoryID(String repositoryID) {
        this.repositoryID = repositoryID;
    }

    public boolean isDependent() {
        return dependent;
    }

    public void setDependent(boolean dependent) {
        this.dependent = dependent;
    }

    public String getExcelName() {
        return excelName;
    }

    public void setExcelName(String excelName) {
        this.excelName = excelName;
    }

    public boolean isCheckAll() {
        return isCheckAll;
    }

    public void setCheckAll(boolean checkAll) {
        isCheckAll = checkAll;
    }

    public Long[] getListMaterialGoods() {
        return listMaterialGoods;
    }

    public void setListMaterialGoods(Long[] listMaterialGoods) {
        this.listMaterialGoods = listMaterialGoods;
    }

    public String getTypeReport() {
        return typeReport;
    }

    public void setTypeReport(String typeReport) {
        this.typeReport = typeReport;
    }

    public Integer getUnitType() {
        return unitType;
    }

    public void setUnitType(Integer unitType) {
        this.unitType = unitType;
    }
}
