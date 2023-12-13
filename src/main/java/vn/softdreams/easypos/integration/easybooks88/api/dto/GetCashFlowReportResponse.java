package vn.softdreams.easypos.integration.easybooks88.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.math.BigDecimal;

public class GetCashFlowReportResponse implements Serializable {

    @JsonProperty
    private BigDecimal dongTienChi;

    @JsonProperty
    private BigDecimal dongTienThu;

    public BigDecimal getDongTienChi() {
        return dongTienChi;
    }

    public void setDongTienChi(BigDecimal dongTienChi) {
        this.dongTienChi = dongTienChi;
    }

    public BigDecimal getDongTienThu() {
        return dongTienThu;
    }

    public void setDongTienThu(BigDecimal dongTienThu) {
        this.dongTienThu = dongTienThu;
    }
}
