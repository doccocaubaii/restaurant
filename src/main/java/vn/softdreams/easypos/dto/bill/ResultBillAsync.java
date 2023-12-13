package vn.softdreams.easypos.dto.bill;

import vn.softdreams.easypos.domain.Bill;

import java.util.List;

public class ResultBillAsync {

    private List<ResultBillError> resultBillErrors;
    private List<Bill> bills;
    private boolean status;

    public ResultBillAsync() {}

    public ResultBillAsync(List<ResultBillError> resultBillErrors, List<Bill> bills) {
        this.resultBillErrors = resultBillErrors;
        this.bills = bills;
    }

    public ResultBillAsync(List<ResultBillError> resultBillErrors, List<Bill> bills, boolean status) {
        this.resultBillErrors = resultBillErrors;
        this.bills = bills;
        this.status = status;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<ResultBillError> getResultBillErrors() {
        return resultBillErrors;
    }

    public void setResultBillErrors(List<ResultBillError> resultBillErrors) {
        this.resultBillErrors = resultBillErrors;
    }

    public List<Bill> getBills() {
        return bills;
    }

    public void setBills(List<Bill> bills) {
        this.bills = bills;
    }
}
