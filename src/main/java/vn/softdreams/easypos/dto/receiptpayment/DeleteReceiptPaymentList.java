package vn.softdreams.easypos.dto.receiptpayment;

import java.util.List;

public class DeleteReceiptPaymentList {

    private Integer comId;
    private Integer type;
    private String fromDate;
    private String toDate;
    private String keyword;
    private Boolean paramCheckAll;
    private List<Integer> ids;

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Boolean getParamCheckAll() {
        return paramCheckAll;
    }

    public void setParamCheckAll(Boolean paramCheckAll) {
        this.paramCheckAll = paramCheckAll;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
