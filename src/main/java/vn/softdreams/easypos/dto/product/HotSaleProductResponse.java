package vn.softdreams.easypos.dto.product;

import java.io.Serializable;
import java.util.List;

public class HotSaleProductResponse implements Serializable {

    private Integer comId;
    private String fromDate;
    private String toDate;
    private List<HotSaleProductResult> detail;

    public HotSaleProductResponse() {}

    public HotSaleProductResponse(Integer comId, String fromDate, String toDate, List<HotSaleProductResult> detail) {
        this.comId = comId;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.detail = detail;
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

    public List<HotSaleProductResult> getDetail() {
        return detail;
    }

    public void setDetail(List<HotSaleProductResult> detail) {
        this.detail = detail;
    }
}
