package vn.softdreams.easypos.dto.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class SaleProductStatsResponse implements Serializable {

    private Integer comId;
    private String fromDate;
    private String toDate;
    private List<SaleProductStatsResult> detail;
    private BigDecimal sumTotalPreTax;
    private BigDecimal sumTotalTaxable;
    private BigDecimal sumVatAmount;

    public SaleProductStatsResponse(
        Integer comId,
        String fromDate,
        String toDate,
        List<SaleProductStatsResult> detail,
        BigDecimal sumTotalPreTax,
        BigDecimal sumTotalTaxable,
        BigDecimal sumVatAmount
    ) {
        this.comId = comId;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.detail = detail;
        this.sumTotalPreTax = sumTotalPreTax;
        this.sumTotalTaxable = sumTotalTaxable;
        this.sumVatAmount = sumVatAmount;
    }

    public BigDecimal getSumTotalPreTax() {
        return sumTotalPreTax;
    }

    public void setSumTotalPreTax(BigDecimal sumTotalPreTax) {
        this.sumTotalPreTax = sumTotalPreTax;
    }

    public BigDecimal getSumTotalTaxable() {
        return sumTotalTaxable;
    }

    public void setSumTotalTaxable(BigDecimal sumTotalTaxable) {
        this.sumTotalTaxable = sumTotalTaxable;
    }

    public BigDecimal getSumVatAmount() {
        return sumVatAmount;
    }

    public void setSumVatAmount(BigDecimal sumVatAmount) {
        this.sumVatAmount = sumVatAmount;
    }

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
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

    public List<SaleProductStatsResult> getDetail() {
        return detail;
    }

    public void setDetail(List<SaleProductStatsResult> detail) {
        this.detail = detail;
    }
}
