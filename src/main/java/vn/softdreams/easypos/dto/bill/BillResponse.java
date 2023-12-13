package vn.softdreams.easypos.dto.bill;

import java.util.List;

public class BillResponse {

    private Long countAll;
    private List<BillItemResponse> bills;

    public BillResponse() {}

    public BillResponse(Long countAll, List<BillItemResponse> bills) {
        this.countAll = countAll;
        this.bills = bills;
    }

    public Long getCountAll() {
        return countAll;
    }

    public void setCountAll(Long countAll) {
        this.countAll = countAll;
    }

    public List<BillItemResponse> getBills() {
        return bills;
    }

    public void setBills(List<BillItemResponse> bills) {
        this.bills = bills;
    }
}
