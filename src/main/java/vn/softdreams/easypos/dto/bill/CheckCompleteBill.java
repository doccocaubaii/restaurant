package vn.softdreams.easypos.dto.bill;

import com.fasterxml.jackson.annotation.JsonInclude;
import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class CheckCompleteBill implements Serializable {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull(message = ExceptionConstants.BILL_ID_NOT_NULL)
    private Integer billId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull(message = ExceptionConstants.COMPANY_ID_NOT_NULL)
    private Integer comId;

    private String productName;
    private String unitName;
    private String onHand;

    public CheckCompleteBill() {}

    public CheckCompleteBill(String productName, String unitName, String onHand) {
        this.productName = productName;
        this.unitName = unitName;
        this.onHand = onHand;
    }

    public Integer getBillId() {
        return billId;
    }

    public void setBillId(Integer billId) {
        this.billId = billId;
    }

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getOnHand() {
        return onHand;
    }

    public void setOnHand(String onHand) {
        this.onHand = onHand;
    }
}
