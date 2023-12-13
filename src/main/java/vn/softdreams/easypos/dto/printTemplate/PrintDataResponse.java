package vn.softdreams.easypos.dto.printTemplate;

import com.fasterxml.jackson.annotation.JsonFormat;
import vn.softdreams.easypos.config.Constants;
import vn.softdreams.easypos.dto.bill.BillExtraConfig;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

public class PrintDataResponse {

    private String companyName;
    private String phoneNumber;
    private String address;
    //    {Ma_don}
    private String billCode;

    //    {Thoi_Gian_In}
    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    private ZonedDateTime createdDate;

    //    {Ma_Co_Quan_Thue}
    private String taxAuthorityCode;
    //    {Khu_vuc}
    private String areaUnitName;
    //    {Ten_Khach_Hang}
    private String customerName;
    //    {Tam_Tinh}
    private BigDecimal amount;
    //    {Giam_gia}
    private BigDecimal discountAmount;
    //    "{Tong_Tien_Truoc_Thue}
    private BigDecimal totalPreTax;
    //    "{Tong_Tien_Thue}
    private BigDecimal vatAmount;
    //    "{Giam_Tru_Thue}
    private BigDecimal discountVatAmount;
    //    "{Khach_Phai_Tra}
    private BigDecimal totalAmount;
    //    "{Khach_Tra}
    private PrintDataBillPayment billPayment;
    //    {Ma_QR_Tra_Cuu}
    private String qrCode;
    //    {Ma_Tra_Cuu}
    private String lookupCode;
    //    {Mo_ta}
    private String description;
    private List<PrintDataBillProduct> billProducts;
    private BillExtraConfig extraConfig;

    private Integer customerCitizenId;
    private String lookupLink;
    private String areaName;
    private Boolean haveDiscountVat;

    public BillExtraConfig getExtraConfig() {
        return extraConfig;
    }

    public void setExtraConfig(BillExtraConfig extraConfig) {
        this.extraConfig = extraConfig;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getTaxAuthorityCode() {
        return taxAuthorityCode;
    }

    public void setTaxAuthorityCode(String taxAuthorityCode) {
        this.taxAuthorityCode = taxAuthorityCode;
    }

    public String getAreaUnitName() {
        return areaUnitName;
    }

    public void setAreaUnitName(String areaUnitName) {
        this.areaUnitName = areaUnitName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getTotalPreTax() {
        return totalPreTax;
    }

    public void setTotalPreTax(BigDecimal totalPreTax) {
        this.totalPreTax = totalPreTax;
    }

    public BigDecimal getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(BigDecimal vatAmount) {
        this.vatAmount = vatAmount;
    }

    public BigDecimal getDiscountVatAmount() {
        return discountVatAmount;
    }

    public void setDiscountVatAmount(BigDecimal discountVatAmount) {
        this.discountVatAmount = discountVatAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public PrintDataBillPayment getBillPayment() {
        return billPayment;
    }

    public void setBillPayment(PrintDataBillPayment billPayment) {
        this.billPayment = billPayment;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getLookupCode() {
        return lookupCode;
    }

    public void setLookupCode(String lookupCode) {
        this.lookupCode = lookupCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<PrintDataBillProduct> getBillProducts() {
        return billProducts;
    }

    public void setBillProducts(List<PrintDataBillProduct> billProducts) {
        this.billProducts = billProducts;
    }

    public Integer getCustomerCitizenId() {
        return customerCitizenId;
    }

    public void setCustomerCitizenId(Integer customerCitizenId) {
        this.customerCitizenId = customerCitizenId;
    }

    public String getLookupLink() {
        return lookupLink;
    }

    public void setLookupLink(String lookupLink) {
        this.lookupLink = lookupLink;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Boolean getHaveDiscountVat() {
        return haveDiscountVat;
    }

    public void setHaveDiscountVat(Boolean haveDiscountVat) {
        this.haveDiscountVat = haveDiscountVat;
    }
}
