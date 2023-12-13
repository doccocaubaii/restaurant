package vn.softdreams.easypos.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import org.springframework.format.annotation.DateTimeFormat;
import vn.softdreams.easypos.domain.Company;
import vn.softdreams.easypos.domain.CompanyUser;
import vn.softdreams.easypos.domain.User;
import vn.softdreams.easypos.util.DateUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * Author Hautv
 */
public class RequestReport {

    private String typeReport;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDate fromDate; // từ ngày

    private String fromDateStr; // từ ngày

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDate toDate; // đến ngày

    private String toDateStr; // đến ngày
    private String monthAndQuarter;
    private LocalDate createDate;
    private Boolean groupTheSameItem; // Cộng gộp các bút toán giống nhau
    private Boolean showAccumAmount; // Hiển thị số lũy kế kỳ trước chuyển sang
    private Integer companyID;
    private Boolean option;
    //    private List<TypeGroupNameDTO> typeGroups;
    private String debitAccount;
    private String creditAcount;
    private String typeGroupName;

    // report
    private Integer comId;
    private String pattern;
    private Integer taxCheckStatus;
    // end

    private List<String> debitAccounts;
    private List<String> creditAccounts;
    private List<String> typeGroupsCode;
    private List<String> accountList;
    private List<Object> accountListObj;
    private List<UUID> accountListSelect;
    private List<String> typeGroupList;

    private List<UUID> listCostSetID;
    private List<UUID> listExpenseItemID;
    private List<UUID> listMaterialGoods;
    private List<UUID> listMaterialGoodsNew;
    private List<UUID> listRSProductionOrderID;
    private List<UUID> listRepository;
    private List<UUID> listCostSets;
    private List<UUID> listExpenseItems;
    private List<UUID> listAccounts;
    private String accountNumber;
    private String accountName;
    private String bankAccountDetail;
    private UUID repositoryID;
    private Integer unitType;
    private String currencyID; // loại tiền
    private Boolean isSimilarSum;
    private Boolean isInvoiceNoSum;
    private Boolean isDependent;
    private Boolean onlyDisplayDifferent;

    private Boolean isOnlyDisplayAccountingObjectHasOPN;
    private Boolean getINTranferHaveDebitLikeCredit;
    private Boolean isBill;
    private UUID materialGoodsCategoryID;
    private UUID employeeID;
    private List<UUID> accountingObjects;
    private String timeLineVoucher;
    private String fileName;
    private List<UUID> tools;
    private List<UUID> departments;
    private List<Company> orgs;
    private CompanyUser department;
    private Boolean typeShowCurrency;
    private List<UUID> statisticsCodes;
    private List<UUID> expenseItems;
    private List<String> listEMContracts;
    private List<UUID> eMContractList;
    private List<UUID> listIDEMContracts;

    private Integer grade;
    private Integer optionReport; // hóa đơn điện tử
    private UUID bankAccountDetailID;
    private Boolean getAmountOriginal;
    private UUID cPPeriodID;
    private Integer typeMethod;
    private Boolean typeLedger;
    private Integer status;
    private Integer typeGroupBy;
    private Boolean isCheckAll;
    private Boolean checkALL;
    private UUID groupID;
    private String acNameFilter;
    private String acCodeFilter;
    private String acAddressFilter;
    private Integer objectType;
    private String mCodeFilter;
    private String mNameFilter;
    private String rCodeFilter;
    private String rNameFilter;
    private String empCodeFilter;
    private String empNameFilter;
    private String departmentCodeFilter;
    private String searchDate;
    private UUID departmentID;
    private UUID fixedAssetCategoryID;
    private String optionXML;
    private String mDateFilter;
    private String mNoFilter;
    private Boolean mauGop;
    private String no;
    private String date;
    private String description;
    private Integer typeReportConfig;
    private Boolean isCustomForm;
    private Integer typeStatistical;
    private Integer page;
    private Integer itemsPerPage;
    private Boolean isOnlyGetData;
    private Boolean isPrint;
    private Boolean isShared;
    private Boolean isNewDTO;
    private Boolean checkPrint;
    private List<UUID> organizationUnitID;

    private UUID accountingGroupID;
    //lưu loại đối tượng: khách hàng, nhà cung cấp , hoặc nhân viên
    //    private Integer objectType;
    private UUID objectID;
    private Integer periodType;
    private Integer currencyUnit;
    private Boolean compareBetweenPeriod;
    private Integer periodTheSame;
    private Integer yearCompare;
    private LocalDate fromDateSecond;
    private LocalDate toDateSecond;
    private Integer fromMonth;
    private Integer toMonth;
    private Integer fromQuarter;
    private Integer toQuarter;
    private Integer fromYear;
    private Integer toYear;
    private Boolean proportion;
    private Integer reportType;
    private String token;
    private Integer currentBook;
    private Integer typeAPP;
    private Integer type;
    private String typeAction;
    private String beforePeriod;
    private String afterPeriod;
    private Boolean getAccountHasData; //lấy tài khoản có phát sinh trong kỳ hay k
    private Boolean getAccountHasClosingAmount; //lấy tài khoản có số dư cuối kỳ trong kỳ hay k

    private Boolean isFirst;

    private Boolean isError;

    private Integer order;

    private User currentUserLoginAndOrg;

    private Boolean checkSynthetic;

    private boolean isApp200;
    private String dataSearch;

    private Boolean isDataFilter;
    private Integer accountingObjectType;

    public boolean getIsApp200() {
        return this.isApp200;
    }

    public void setIsApp200(boolean isapp200) {
        this.isApp200 = isapp200;
    }

    public Boolean getCheckSynthetic() {
        return checkSynthetic;
    }

    public void setCheckSynthetic(Boolean checkSynthetic) {
        this.checkSynthetic = checkSynthetic;
    }

    public List<String> getDebitAccounts() {
        return debitAccounts;
    }

    public void setDebitAccounts(List<String> debitAccounts) {
        this.debitAccounts = debitAccounts;
    }

    public List<String> getCreditAccounts() {
        return creditAccounts;
    }

    public void setCreditAccounts(List<String> creditAccounts) {
        this.creditAccounts = creditAccounts;
    }

    public List<String> getTypeGroupsCode() {
        return typeGroupsCode;
    }

    public void setTypeGroupsCode(List<String> typeGroupsCode) {
        this.typeGroupsCode = typeGroupsCode;
    }

    public String getTypeAction() {
        return typeAction;
    }

    public void setTypeAction(String typeAction) {
        this.typeAction = typeAction;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getTypeAPP() {
        return typeAPP;
    }

    public void setTypeAPP(Integer typeAPP) {
        this.typeAPP = typeAPP;
    }

    public Integer getReportType() {
        return reportType;
    }

    public void setReportType(Integer reportType) {
        this.reportType = reportType;
    }

    public Boolean getProportion() {
        return proportion;
    }

    public void setProportion(Boolean proportion) {
        this.proportion = proportion;
    }

    public Integer getFromMonth() {
        return fromMonth;
    }

    public void setFromMonth(Integer fromMonth) {
        this.fromMonth = fromMonth;
    }

    public Integer getToMonth() {
        return toMonth;
    }

    public void setToMonth(Integer toMonth) {
        this.toMonth = toMonth;
    }

    public Integer getFromQuarter() {
        return fromQuarter;
    }

    public void setFromQuarter(Integer fromQuarter) {
        this.fromQuarter = fromQuarter;
    }

    public Integer getToQuarter() {
        return toQuarter;
    }

    public void setToQuarter(Integer toQuarter) {
        this.toQuarter = toQuarter;
    }

    public Integer getFromYear() {
        return fromYear;
    }

    public void setFromYear(Integer fromYear) {
        this.fromYear = fromYear;
    }

    public Integer getToYear() {
        return toYear;
    }

    public void setToYear(Integer toYear) {
        this.toYear = toYear;
    }

    public Integer getPeriodType() {
        return periodType;
    }

    public void setPeriodType(Integer periodType) {
        this.periodType = periodType;
    }

    public Integer getCurrencyUnit() {
        return currencyUnit;
    }

    public void setCurrencyUnit(Integer currencyUnit) {
        this.currencyUnit = currencyUnit;
    }

    public Boolean getCompareBetweenPeriod() {
        return compareBetweenPeriod;
    }

    public void setCompareBetweenPeriod(Boolean compareBetweenPeriod) {
        this.compareBetweenPeriod = compareBetweenPeriod;
    }

    public Integer getPeriodTheSame() {
        return periodTheSame;
    }

    public void setPeriodTheSame(Integer periodTheSame) {
        this.periodTheSame = periodTheSame;
    }

    public Integer getYearCompare() {
        return yearCompare;
    }

    public void setYearCompare(Integer yearCompare) {
        this.yearCompare = yearCompare;
    }

    public LocalDate getFromDateSecond() {
        return fromDateSecond;
    }

    public void setFromDateSecond(LocalDate fromDateSecond) {
        this.fromDateSecond = fromDateSecond;
    }

    public LocalDate getToDateSecond() {
        return toDateSecond;
    }

    public void setToDateSecond(LocalDate toDateSecond) {
        this.toDateSecond = toDateSecond;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMonthAndQuarter() {
        return monthAndQuarter;
    }

    public void setMonthAndQuarter(String monthAndQuarter) {
        this.monthAndQuarter = monthAndQuarter;
    }

    private String repositoryName;

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public List<UUID> getListAccounts() {
        return listAccounts;
    }

    public void setListAccounts(List<UUID> listAccounts) {
        this.listAccounts = listAccounts;
    }

    public Integer getTypeGroupBy() {
        return typeGroupBy;
    }

    public void setTypeGroupBy(Integer typeGroupBy) {
        this.typeGroupBy = typeGroupBy;
    }

    public Boolean getGetINTranferHaveDebitLikeCredit() {
        return getINTranferHaveDebitLikeCredit;
    }

    public void setGetINTranferHaveDebitLikeCredit(Boolean getINTranferHaveDebitLikeCredit) {
        this.getINTranferHaveDebitLikeCredit = getINTranferHaveDebitLikeCredit;
    }

    public Boolean getOnlyDisplayDifferent() {
        return onlyDisplayDifferent;
    }

    public void setOnlyDisplayDifferent(Boolean onlyDisplayDifferent) {
        this.onlyDisplayDifferent = onlyDisplayDifferent;
    }

    private String createName;
    private String signName;

    public UUID getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(UUID departmentID) {
        this.departmentID = departmentID;
    }

    public UUID getFixedAssetCategoryID() {
        return fixedAssetCategoryID;
    }

    public void setFixedAssetCategoryID(UUID fixedAssetCategoryID) {
        this.fixedAssetCategoryID = fixedAssetCategoryID;
    }

    public Boolean getCheckAll() {
        return isCheckAll;
    }

    public void setCheckAll(Boolean checkAll) {
        isCheckAll = checkAll;
    }

    public String getAcNameFilter() {
        return acNameFilter;
    }

    public void setAcNameFilter(String acNameFilter) {
        this.acNameFilter = acNameFilter;
    }

    public String getAcCodeFilter() {
        return acCodeFilter;
    }

    public void setAcCodeFilter(String acCodeFilter) {
        this.acCodeFilter = acCodeFilter;
    }

    public String getAcAddressFilter() {
        return acAddressFilter;
    }

    public void setAcAddressFilter(String acAddressFilter) {
        this.acAddressFilter = acAddressFilter;
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

    public Integer getTaxCheckStatus() {
        return taxCheckStatus;
    }

    public void setTaxCheckStatus(Integer taxCheckStatus) {
        this.taxCheckStatus = taxCheckStatus;
    }

    public UUID getGroupID() {
        return groupID;
    }

    public void setGroupID(UUID groupID) {
        this.groupID = groupID;
    }

    public Boolean getTypeLedger() {
        return typeLedger;
    }

    public void setTypeLedger(Boolean typeLedger) {
        this.typeLedger = typeLedger;
    }

    public List<String> getListEMContracts() {
        return listEMContracts;
    }

    public void setListEMContracts(List<String> listEMContracts) {
        this.listEMContracts = listEMContracts;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<String> getTypeGroupList() {
        return typeGroupList;
    }

    public void setTypeGroupList(List<String> typeGroupList) {
        this.typeGroupList = typeGroupList;
    }

    public String getTypeGroupName() {
        return typeGroupName;
    }

    public void setTypeGroupName(String typeGroupName) {
        this.typeGroupName = typeGroupName;
    }

    public List<UUID> getListExpenseItemID() {
        return listExpenseItemID;
    }

    public void setListExpenseItemID(List<UUID> listExpenseItemID) {
        this.listExpenseItemID = listExpenseItemID;
    }

    public Boolean getDependent() {
        return isDependent;
    }

    public List<UUID> getListCostSetID() {
        return listCostSetID;
    }

    public void setListCostSetID(List<UUID> listCostSetID) {
        this.listCostSetID = listCostSetID;
    }

    public void setDependent(Boolean dependent) {
        isDependent = dependent;
    }

    public Boolean getBill() {
        return isBill;
    }

    public void setBill(Boolean bill) {
        isBill = bill;
    }

    public Boolean getSimilarSum() {
        return isSimilarSum;
    }

    public void setSimilarSum(Boolean similarSum) {
        isSimilarSum = similarSum;
    }

    public Boolean getInvoiceNoSum() {
        return isInvoiceNoSum;
    }

    public void setInvoiceNoSum(Boolean invoiceNoSum) {
        isInvoiceNoSum = invoiceNoSum;
    }

    public String getBankAccountDetail() {
        return bankAccountDetail;
    }

    public void setBankAccountDetail(String bankAccountDetail) {
        this.bankAccountDetail = bankAccountDetail;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public Boolean getOption() {
        return option;
    }

    public void setOption(Boolean option) {
        this.option = option;
    }

    public String getTypeReport() {
        return typeReport;
    }

    public void setTypeReport(String typeReport) {
        this.typeReport = typeReport;
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

    public Boolean getGroupTheSameItem() {
        return groupTheSameItem;
    }

    public void setGroupTheSameItem(Boolean groupTheSameItem) {
        this.groupTheSameItem = groupTheSameItem;
    }

    public Boolean getShowAccumAmount() {
        return showAccumAmount;
    }

    public void setShowAccumAmount(Boolean showAccumAmount) {
        this.showAccumAmount = showAccumAmount;
    }

    public UUID getRepositoryID() {
        return repositoryID;
    }

    public void setRepositoryID(UUID repositoryID) {
        this.repositoryID = repositoryID;
    }

    public Integer getUnitType() {
        return unitType;
    }

    public void setUnitType(Integer unitType) {
        this.unitType = unitType;
    }

    public String getCurrencyID() {
        return currencyID;
    }

    public void setCurrencyID(String currencyID) {
        this.currencyID = currencyID;
    }

    public List<String> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<String> accountList) {
        this.accountList = accountList;
    }

    public String getDebitAccount() {
        return debitAccount;
    }

    public void setDebitAccount(String debitAccount) {
        this.debitAccount = debitAccount;
    }

    public String getCreditAcount() {
        return creditAcount;
    }

    public void setCreditAcount(String creditAcount) {
        this.creditAcount = creditAcount;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public List<UUID> getListMaterialGoods() {
        return listMaterialGoods;
    }

    public void setListMaterialGoods(List<UUID> listMaterialGoods) {
        this.listMaterialGoods = listMaterialGoods;
    }

    public Integer getOptionReport() {
        return optionReport;
    }

    public void setOptionReport(Integer optionReport) {
        this.optionReport = optionReport;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public UUID getBankAccountDetailID() {
        return bankAccountDetailID;
    }

    public void setBankAccountDetailID(UUID bankAccountDetailID) {
        this.bankAccountDetailID = bankAccountDetailID;
    }

    public UUID getMaterialGoodsCategoryID() {
        return materialGoodsCategoryID;
    }

    public void setMaterialGoodsCategoryID(UUID materialGoodsCategoryID) {
        this.materialGoodsCategoryID = materialGoodsCategoryID;
    }

    public UUID getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(UUID employeeID) {
        this.employeeID = employeeID;
    }

    public List<UUID> getAccountingObjects() {
        return accountingObjects;
    }

    public void setAccountingObjects(List<UUID> accountingObjects) {
        this.accountingObjects = accountingObjects;
    }

    public String getTimeLineVoucher() {
        return timeLineVoucher;
    }

    public void setTimeLineVoucher(String timeLineVoucher) {
        this.timeLineVoucher = timeLineVoucher;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<UUID> getTools() {
        return tools;
    }

    public void setTools(List<UUID> tools) {
        this.tools = tools;
    }

    public Boolean getTypeShowCurrency() {
        return typeShowCurrency;
    }

    public void setTypeShowCurrency(Boolean typeShowCurrency) {
        this.typeShowCurrency = typeShowCurrency;
    }

    public Boolean getGetAmountOriginal() {
        return getAmountOriginal;
    }

    public void setGetAmountOriginal(Boolean getAmountOriginal) {
        this.getAmountOriginal = getAmountOriginal;
    }

    public List<UUID> getListExpenseItems() {
        return listExpenseItems;
    }

    public void setListExpenseItems(List<UUID> listExpenseItems) {
        this.listExpenseItems = listExpenseItems;
    }

    public List<UUID> getDepartments() {
        return departments;
    }

    public void setDepartments(List<UUID> departments) {
        this.departments = departments;
    }

    public List<UUID> getListCostSets() {
        return listCostSets;
    }

    public void setListCostSets(List<UUID> listCostSets) {
        this.listCostSets = listCostSets;
    }

    public UUID getcPPeriodID() {
        return cPPeriodID;
    }

    public void setcPPeriodID(UUID cPPeriodID) {
        this.cPPeriodID = cPPeriodID;
    }

    public Integer getTypeMethod() {
        return typeMethod;
    }

    public void setTypeMethod(Integer typeMethod) {
        this.typeMethod = typeMethod;
    }

    public List<UUID> getStatisticsCodes() {
        return statisticsCodes;
    }

    public void setStatisticsCodes(List<UUID> statisticsCodes) {
        this.statisticsCodes = statisticsCodes;
    }

    public List<UUID> getExpenseItems() {
        return expenseItems;
    }

    public void setExpenseItems(List<UUID> expenseItems) {
        this.expenseItems = expenseItems;
    }

    public List<UUID> getListRepository() {
        return listRepository;
    }

    public void setListRepository(List<UUID> listRepository) {
        this.listRepository = listRepository;
    }

    public Boolean getIsCheckAll() {
        return isCheckAll;
    }

    public void setISCheckAll(Boolean isCheckAll) {
        this.isCheckAll = isCheckAll;
    }

    public Boolean getCheckALL() {
        return checkALL;
    }

    public void setCheckALL(Boolean checkALL) {
        this.checkALL = checkALL;
    }

    public List<UUID> geteMContractList() {
        return eMContractList;
    }

    public void seteMContractList(List<UUID> eMContractList) {
        this.eMContractList = eMContractList;
    }

    public String getmCodeFilter() {
        return mCodeFilter;
    }

    public void setmCodeFilter(String mCodeFilter) {
        this.mCodeFilter = mCodeFilter;
    }

    public String getmNameFilter() {
        return mNameFilter;
    }

    public void setmNameFilter(String mNameFilter) {
        this.mNameFilter = mNameFilter;
    }

    public String getrCodeFilter() {
        return rCodeFilter;
    }

    public void setrCodeFilter(String rCodeFilter) {
        this.rCodeFilter = rCodeFilter;
    }

    public String getrNameFilter() {
        return rNameFilter;
    }

    public void setrNameFilter(String rNameFilter) {
        this.rNameFilter = rNameFilter;
    }

    public String getEmpCodeFilter() {
        return empCodeFilter;
    }

    public void setEmpCodeFilter(String empCodeFilter) {
        this.empCodeFilter = empCodeFilter;
    }

    public String getEmpNameFilter() {
        return empNameFilter;
    }

    public void setEmpNameFilter(String empNameFilter) {
        this.empNameFilter = empNameFilter;
    }

    public String getDepartmentCodeFilter() {
        return departmentCodeFilter;
    }

    public void setDepartmentCodeFilter(String departmentCodeFilter) {
        this.departmentCodeFilter = departmentCodeFilter;
    }

    public List<UUID> getListIDEMContracts() {
        return listIDEMContracts;
    }

    public void setListIDEMContracts(List<UUID> listIDEMContracts) {
        this.listIDEMContracts = listIDEMContracts;
    }

    public String getOptionXML() {
        return optionXML;
    }

    public void setOptionXML(String optionXML) {
        this.optionXML = optionXML;
    }

    public UUID getAccountingGroupID() {
        return accountingGroupID;
    }

    public void setAccountingGroupID(UUID accountingGroupID) {
        this.accountingGroupID = accountingGroupID;
    }

    public Integer getObjectType() {
        return objectType;
    }

    public void setObjectType(Integer objectType) {
        this.objectType = objectType;
    }

    public String getmDateFilter() {
        return mDateFilter;
    }

    public void setmDateFilter(String mDateFilter) {
        this.mDateFilter = mDateFilter;
    }

    public String getmNoFilter() {
        return mNoFilter;
    }

    public void setmNoFilter(String mNoFilter) {
        this.mNoFilter = mNoFilter;
    }

    public List<UUID> getListRSProductionOrderID() {
        return listRSProductionOrderID;
    }

    public void setListRSProductionOrderID(List<UUID> listRSProductionOrderID) {
        this.listRSProductionOrderID = listRSProductionOrderID;
    }

    public Boolean getMauGop() {
        return mauGop;
    }

    public void setMauGop(Boolean mauGop) {
        this.mauGop = mauGop;
    }

    public String getSearchDate() {
        return searchDate;
    }

    public void setSearchDate(String searchDate) {
        this.searchDate = searchDate;
    }

    public Integer getTypeReportConfig() {
        return typeReportConfig;
    }

    public void setTypeReportConfig(Integer typeReportConfig) {
        this.typeReportConfig = typeReportConfig;
    }

    public Boolean getIsCustomForm() {
        return isCustomForm;
    }

    public void setIsCustomForm(Boolean customForm) {
        isCustomForm = customForm;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public Integer getTypeStatistical() {
        return typeStatistical;
    }

    public void setTypeStatistical(Integer typeStatistical) {
        this.typeStatistical = typeStatistical;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getItemsPerPage() {
        return itemsPerPage;
    }

    public void setItemsPerPage(Integer itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    public Boolean getIsOnlyGetData() {
        return isOnlyGetData;
    }

    public void setIsOnlyGetData(Boolean onlyGetData) {
        isOnlyGetData = onlyGetData;
    }

    public UUID getObjectID() {
        return objectID;
    }

    public void setObjectID(UUID objectID) {
        this.objectID = objectID;
    }

    public Boolean getIsPrint() {
        return isPrint;
    }

    public void setIsPrint(Boolean print) {
        isPrint = print;
    }

    public List<UUID> getListMaterialGoodsNew() {
        return listMaterialGoodsNew;
    }

    public void setListMaterialGoodsNew(List<UUID> listMaterialGoodsNew) {
        this.listMaterialGoodsNew = listMaterialGoodsNew;
    }

    public Boolean getIsShared() {
        return isShared;
    }

    public void setIsShared(Boolean shared) {
        isShared = shared;
    }

    public Boolean getIsNewDTO() {
        return isNewDTO;
    }

    public void setIsNewDTO(Boolean newDTO) {
        isNewDTO = newDTO;
    }

    public List<Object> getAccountListObj() {
        return accountListObj;
    }

    public void setAccountListObj(List<Object> accountListObj) {
        this.accountListObj = accountListObj;
    }

    public List<UUID> getAccountListSelect() {
        return accountListSelect;
    }

    public void setAccountListSelect(List<UUID> accountListSelect) {
        this.accountListSelect = accountListSelect;
    }

    public Boolean getCheckPrint() {
        return checkPrint;
    }

    public void setCheckPrint(Boolean checkPrint) {
        this.checkPrint = checkPrint;
    }

    public List<UUID> getOrganizationUnitID() {
        return organizationUnitID;
    }

    public void setOrganizationUnitID(List<UUID> organizationUnitID) {
        this.organizationUnitID = organizationUnitID;
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

    public String getBeforePeriod() {
        return beforePeriod;
    }

    public void setBeforePeriod(String beforePeriod) {
        this.beforePeriod = beforePeriod;
    }

    public String getAfterPeriod() {
        return afterPeriod;
    }

    public void setAfterPeriod(String afterPeriod) {
        this.afterPeriod = afterPeriod;
    }

    public String getFromDateStr() {
        return fromDateStr;
    }

    public Boolean getError() {
        return isError;
    }

    public void setError(Boolean error) {
        isError = error;
    }

    public Boolean getFirst() {
        return isFirst;
    }

    public void setFirst(Boolean first) {
        isFirst = first;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Boolean getGetAccountHasData() {
        return getAccountHasData;
    }

    public void setGetAccountHasData(Boolean getAccountHasData) {
        this.getAccountHasData = getAccountHasData;
    }

    public Boolean getGetAccountHasClosingAmount() {
        return getAccountHasClosingAmount;
    }

    public void setGetAccountHasClosingAmount(Boolean getAccountHasClosingAmount) {
        this.getAccountHasClosingAmount = getAccountHasClosingAmount;
    }

    public Boolean getIsOnlyDisplayAccountingObjectHasOPN() {
        return isOnlyDisplayAccountingObjectHasOPN;
    }

    public void setIsOnlyDisplayAccountingObjectHasOPN(Boolean isOnlyDisplayAccountingObjectHasOPN) {
        this.isOnlyDisplayAccountingObjectHasOPN = isOnlyDisplayAccountingObjectHasOPN;
    }

    public void setFromDateStr(String fromDateStr) {
        if (fromDateStr != null) {
            this.fromDateStr = fromDateStr;
        } else if (this.fromDate != null) {
            this.fromDateStr = this.fromDate.format(DateTimeFormatter.ofPattern(DateUtil.C_YYYY_MM_DD));
        } else {
            this.fromDateStr = null;
        }
    }

    public String getToDateStr() {
        return toDateStr;
    }

    public void setToDateStr(String toDateStr) {
        if (toDateStr != null) {
            this.toDateStr = toDateStr;
        } else if (this.toDate != null) {
            this.toDateStr = this.toDate.format(DateTimeFormatter.ofPattern(DateUtil.C_YYYY_MM_DD));
        } else {
            this.toDateStr = null;
        }
    }

    public String getDataSearch() {
        return dataSearch;
    }

    public void setDataSearch(String dataSearch) {
        this.dataSearch = dataSearch;
    }

    public Boolean getIsDataFilter() {
        return isDataFilter;
    }

    public void setIsDataFilter(Boolean dataFilter) {
        isDataFilter = dataFilter;
    }

    public Integer getAccountingObjectType() {
        return accountingObjectType;
    }

    public void setAccountingObjectType(Integer accountingObjectType) {
        this.accountingObjectType = accountingObjectType;
    }

    @Override
    public String toString() {
        return (
            "{" +
            (typeReport != null ? "\"typeReport\":\"" + typeReport + "\"" : "") +
            (fromDate != null ? ",\"fromDate\":\"" + fromDate + "\"" : "") +
            (toDate != null ? ",\"toDate\":\"" + toDate + "\"" : "") +
            (toDateSecond != null ? ",\"toDateSecond\":\"" + toDateSecond + "\"" : "") +
            (fromDateSecond != null ? ",\"fromDateSecond\":\"" + fromDateSecond + "\"" : "") +
            (createDate != null ? ",\"createDate\":\"" + createDate + "\"" : "") +
            (groupTheSameItem != null ? ",\"groupTheSameItem\":\"" + groupTheSameItem + "\"" : "") +
            (showAccumAmount != null ? ",\"showAccumAmount\":\"" + showAccumAmount + "\"" : "") +
            (companyID != null ? ",\"companyID\":\"" + companyID + "\"" : "") +
            (option != null ? ",\"option\":\"" + option + "\"" : "") +
            (debitAccount != null ? ",\"debitAccount\":\"" + debitAccount + "\"" : "") +
            (creditAcount != null ? ",\"creditAcount\":\"" + creditAcount + "\"" : "") +
            (typeGroupName != null ? ",\"typeGroupName\":\"" + typeGroupName + "\"" : "") +
            (accountList != null ? ",\"accountList\":\"" + accountList + "\"" : "") +
            (typeGroupList != null ? ",\"typeGroupList\":\"" + typeGroupList + "\"" : "") +
            ((listCostSetID != null && listCostSetID.size() > 0) ? ",\"listCostSetID\":\"" + listCostSetID + "\"" : "") +
            ((listExpenseItemID != null && listExpenseItemID.size() > 0) ? ",\"listExpenseItemID\":\"" + listExpenseItemID + "\"" : "") +
            ((listMaterialGoods != null && listMaterialGoods.size() > 0) ? ",\"listMaterialGoods\":\"" + listMaterialGoods + "\"" : "") +
            ((listRepository != null && listRepository.size() > 0) ? ",\"listRepository\":\"" + listRepository + "\"" : "") +
            ((listCostSets != null && listCostSets.size() > 0) ? ",\"listCostSets\":\"" + listCostSets + "\"" : "") +
            ((listExpenseItems != null && listExpenseItems.size() > 0) ? ",\"listExpenseItems\":\"" + listExpenseItems + "\"" : "") +
            (accountNumber != null ? ",\"accountNumber\":\"" + accountNumber + "\"" : "") +
            (accountName != null ? ",\"accountName\":\"" + accountName + "\"" : "") +
            (bankAccountDetail != null ? ",\"bankAccountDetail\":\"" + bankAccountDetail + "\"" : "") +
            (repositoryID != null ? ",\"repositoryID\":\"" + repositoryID + "\"" : "") +
            (unitType != null ? ",\"unitType\":\"" + unitType + "\"" : "") +
            (currencyID != null ? ",\"currencyID\":\"" + currencyID + "\"" : "") +
            (isSimilarSum != null ? ",\"isSimilarSum\":\"" + isSimilarSum + "\"" : "") +
            (isDependent != null ? ",\"isDependent\":\"" + isDependent + "\"" : "") +
            (isBill != null ? ",\"isBill\":\"" + isBill + "\"" : "") +
            (isOnlyGetData != null ? ",\"isOnlyGetData\":\"" + isOnlyGetData + "\"" : "") +
            (materialGoodsCategoryID != null ? ",\"materialGoodsCategoryID\":\"" + materialGoodsCategoryID + "\"" : "") +
            (employeeID != null ? ",\"employeeID\":\"" + employeeID + "\"" : "") +
            ((accountingObjects != null && accountingObjects.size() > 0) ? ",\"accountingObjects\":\"" + accountingObjects + "\"" : "") +
            (timeLineVoucher != null ? ",\"timeLineVoucher\":\"" + timeLineVoucher + "\"" : "") +
            (fileName != null ? ",\"fileName\":\"" + fileName + "\"" : "") +
            ((tools != null && tools.size() > 0) ? ",\"tools\":\"" + tools + "\"" : "") +
            ((departments != null && departments.size() > 0) ? ",\"departments\":\"" + departments + "\"" : "") +
            (typeShowCurrency != null ? ",\"typeShowCurrency\":\"" + typeShowCurrency + "\"" : "") +
            ((statisticsCodes != null && statisticsCodes.size() > 0) ? ",\"statisticsCodes\":\"" + statisticsCodes + "\"" : "") +
            ((expenseItems != null && expenseItems.size() > 0) ? ",\"expenseItems\":\"" + expenseItems + "\"" : "") +
            (listEMContracts != null ? ",\"listEMContracts\":\"" + listEMContracts + "\"" : "") +
            ((listIDEMContracts != null && listIDEMContracts.size() > 0) ? ",\"listIDEMContracts\":\"" + listIDEMContracts + "\"" : "") +
            ((eMContractList != null && eMContractList.size() > 0) ? ",\"eMContractList\":\"" + eMContractList + "\"" : "") +
            (grade != null ? ",\"grade\":\"" + grade + "\"" : "") +
            (page != null ? ",\"page\":\"" + page + "\"" : "") +
            (itemsPerPage != null ? ",\"itemsPerPage\":\"" + itemsPerPage + "\"" : "") +
            (optionReport != null ? ",\"optionReport\":\"" + optionReport + "\"" : "") +
            (bankAccountDetailID != null ? ",\"bankAccountDetailID\":\"" + bankAccountDetailID + "\"" : "") +
            (getAmountOriginal != null ? ",\"getAmountOriginal\":\"" + getAmountOriginal + "\"" : "") +
            (cPPeriodID != null ? ",\"cPPeriodID\":\"" + cPPeriodID + "\"" : "") +
            (typeMethod != null ? ",\"typeMethod\":\"" + typeMethod + "\"" : "") +
            (typeLedger != null ? ",\"typeLedger\":\"" + typeLedger + "\"" : "") +
            (status != null ? ",\"status\":\"" + status + "\"" : "") +
            (isCheckAll != null ? ",\"isCheckAll\":\"" + isCheckAll + "\"" : "") +
            (isDataFilter != null ? ",\"isDataFilter\":\"" + isDataFilter + "\"" : "") +
            (checkALL != null ? ",\"checkALL\":\"" + checkALL + "\"" : "") +
            (isShared != null ? ",\"isShared\":\"" + isShared + "\"" : "") +
            (groupID != null ? ",\"groupID\":\"" + groupID + "\"" : "") +
            (acNameFilter != null ? ",\"acNameFilter\":\"" + acNameFilter + "\"" : "") +
            (acCodeFilter != null ? ",\"acCodeFilter\":\"" + acCodeFilter + "\"" : "") +
            (acAddressFilter != null ? ",\"acAddressFilter\":\"" + acAddressFilter + "\"" : "") +
            (objectType != null ? ",\"objectType\":\"" + objectType + "\"" : "") +
            (token != null ? ",\"token\":\"" + token + "\"" : "") +
            (type != null ? ",\"type\":\"" + type + "\"" : "") +
            (currentBook != null ? ",\"currentBook\":\"" + currentBook + "\"" : "") +
            (periodTheSame != null ? ",\"periodTheSame\":\"" + periodTheSame + "\"" : "") +
            (currencyUnit != null ? ",\"currencyUnit\":\"" + currencyUnit + "\"" : "") +
            (yearCompare != null ? ",\"yearCompare\":\"" + yearCompare + "\"" : "") +
            (periodType != null ? ",\"periodType\":\"" + periodType + "\"" : "") +
            (fromMonth != null ? ",\"fromMonth\":\"" + fromMonth + "\"" : "") +
            (toMonth != null ? ",\"toMonth\":\"" + toMonth + "\"" : "") +
            (fromYear != null ? ",\"fromYear\":\"" + fromYear + "\"" : "") +
            (toYear != null ? ",\"toYear\":\"" + toYear + "\"" : "") +
            (fromQuarter != null ? ",\"fromQuarter\":\"" + fromQuarter + "\"" : "") +
            (toQuarter != null ? ",\"toQuarter\":\"" + toQuarter + "\"" : "") +
            (proportion != null ? ",\"proportion\":\"" + proportion + "\"" : "") +
            (compareBetweenPeriod != null ? ",\"compareBetweenPeriod\":\"" + compareBetweenPeriod + "\"" : "") +
            (
                (organizationUnitID != null && organizationUnitID.size() > 0)
                    ? ",\"organizationUnitID\":\"" + organizationUnitID + "\""
                    : ""
            ) +
            (mCodeFilter != null ? ",\"mCodeFilter\":\"" + mCodeFilter + "\"" : "") +
            (mNameFilter != null ? ",\"mNameFilter\":\"" + mNameFilter + "\"" : "") +
            (rCodeFilter != null ? ",\"rCodeFilter\":\"" + rCodeFilter + "\"" : "") +
            (rNameFilter != null ? ",\"rNameFilter\":\"" + rNameFilter + "\"" : "") +
            (empCodeFilter != null ? ",\"empCodeFilter\":\"" + empCodeFilter + "\"" : "") +
            (empNameFilter != null ? ",\"empNameFilter\":\"" + empNameFilter + "\"" : "") +
            (departmentCodeFilter != null ? ",\"departmentCodeFilter\":\"" + departmentCodeFilter + "\"" : "") +
            (departmentID != null ? ",\"departmentID\":\"" + departmentID + "\"" : "") +
            (fixedAssetCategoryID != null ? ",\"fixedAssetCategoryID\":\"" + fixedAssetCategoryID + "\"" : "") +
            (typeAPP != null ? ",\"typeAPP\":\"" + typeAPP + "\"" : "") +
            (mauGop != null ? ",\"mauGop\":\"" + mauGop + "\"" : "") +
            (typeReportConfig != null ? ",\"typeReportConfig\":\"" + typeReportConfig + "\"" : "") +
            (isCustomForm != null ? ",\"isCustomForm\":\"" + isCustomForm + "\"" : "") +
            (beforePeriod != null ? ",\"beforePeriod\":\"" + beforePeriod + "\"" : "") +
            (afterPeriod != null ? ",\"afterPeriod\":\"" + afterPeriod + "\"" : "") +
            (debitAccounts != null ? ",\"debitAccounts\":\"" + debitAccounts + "\"" : "") +
            (typeGroupsCode != null ? ",\"typeGroupsCode\":\"" + typeGroupsCode + "\"" : "") +
            (creditAccounts != null ? ",\"creditAccounts\":\"" + creditAccounts + "\"" : "") +
            (objectType != null ? ",\"objectType\":\"" + objectType + "\"" : "") +
            (typeAction != null ? ",\"typeAction\":\"" + typeAction + "\"" : "") +
            (typeGroupBy != null ? ",\"typeGroupBy\":\"" + typeGroupBy + "\"" : "") +
            (monthAndQuarter != null ? ",\"monthAndQuarter\":\"" + monthAndQuarter + "\"" : "") +
            (listAccounts != null ? ",\"listAccounts\":\"" + listAccounts + "\"" : "") +
            (page != null ? ",\"page\":\"" + page + "\"" : "") +
            (itemsPerPage != null ? ",\"itemsPerPage\":\"" + itemsPerPage + "\"" : "") +
            (getAccountHasData != null ? ",\"getAccountHasData\":\"" + getAccountHasData + "\"" : "") +
            (getAccountHasClosingAmount != null ? ",\"getAccountHasClosingAmount\":\"" + getAccountHasClosingAmount + "\"" : "") +
            "}"
        );
    }
}
