package vn.softdreams.easypos.dto.product;

import java.io.Serializable;
import java.math.BigDecimal;

public class SaleProductStatsResult implements Serializable {

    private Integer rowNumber;
    private String pattern;
    private String no;
    private String arisingDate;
    private String customerName;
    private String customerTaxCode;
    private BigDecimal totalPreTax;
    private String description;
    private Integer status;
    private Integer vatRate;
    private BigDecimal vatAmount;
    private BigDecimal totalVatAmount;
    private BigDecimal totalTotalPreTax;

    public SaleProductStatsResult(
        Integer rowNumber,
        String pattern,
        String no,
        String arisingDate,
        String customerName,
        String customerTaxCode,
        BigDecimal totalPreTax,
        Integer status,
        Integer vatRate,
        BigDecimal vatAmount,
        BigDecimal totalVatAmount,
        BigDecimal totalTotalPreTax
    ) {
        this.rowNumber = rowNumber;
        this.pattern = pattern;
        this.no = no;
        this.arisingDate = arisingDate;
        this.customerName = customerName;
        this.customerTaxCode = customerTaxCode;
        this.totalPreTax = totalPreTax;
        this.status = status;
        this.vatRate = vatRate;
        this.vatAmount = vatAmount;
        this.totalVatAmount = totalVatAmount;
        this.totalTotalPreTax = totalTotalPreTax;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(Integer rowNumber) {
        this.rowNumber = rowNumber;
    }

    public BigDecimal getTotalTotalPreTax() {
        return totalTotalPreTax;
    }

    public void setTotalTotalPreTax(BigDecimal totalTotalPreTax) {
        this.totalTotalPreTax = totalTotalPreTax;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getArisingDate() {
        return arisingDate;
    }

    public void setArisingDate(String arisingDate) {
        this.arisingDate = arisingDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerTaxCode() {
        return customerTaxCode;
    }

    public void setCustomerTaxCode(String customerTaxCode) {
        this.customerTaxCode = customerTaxCode;
    }

    public BigDecimal getTotalPreTax() {
        return totalPreTax;
    }

    public void setTotalPreTax(BigDecimal totalPreTax) {
        this.totalPreTax = totalPreTax;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getVatRate() {
        return vatRate;
    }

    public void setVatRate(Integer vatRate) {
        this.vatRate = vatRate;
    }

    public BigDecimal getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(BigDecimal vatAmount) {
        this.vatAmount = vatAmount;
    }

    public BigDecimal getTotalVatAmount() {
        return totalVatAmount;
    }

    public void setTotalVatAmount(BigDecimal totalVatAmount) {
        this.totalVatAmount = totalVatAmount;
    }
}
