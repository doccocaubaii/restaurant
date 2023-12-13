package vn.softdreams.easypos.integration.easybooks88.api.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class GetCashFlowReportRequest {

    @NotEmpty(message = "fromDate must be not empty")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Invalid format fromDate")
    private String fromDate;

    @NotEmpty(message = "toDate must be not empty")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Invalid format toDate")
    private String toDate;

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
}
