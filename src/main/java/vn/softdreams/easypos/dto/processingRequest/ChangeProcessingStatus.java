package vn.softdreams.easypos.dto.processingRequest;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ChangeProcessingStatus {

    private Integer id;

    @NotNull(message = ExceptionConstants.KITCHEN_TYPE_NOT_NULL)
    private Integer type;

    //    @NotNull(message = ExceptionConstants.KITCHEN_CHANGE_STATUS_TYPE_NOT_NULL)
    private Integer changeStatusType;

    @NotNull(message = ExceptionConstants.KITCHEN_STATUS_NOT_NULL)
    private Integer status;

    @NotNull(message = ExceptionConstants.BILL_ID_NOT_NULL)
    private Integer billId;

    private Integer productProductUnitId;
    private Integer areaUnitId;
    private Boolean haveTopping;

    @NotNull(message = ExceptionConstants.QUANTITY_NOT_NULL)
    private BigDecimal quantity;

    private Boolean doneWithTable;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getBillId() {
        return billId;
    }

    public void setBillId(Integer billId) {
        this.billId = billId;
    }

    public Integer getAreaUnitId() {
        return areaUnitId;
    }

    public void setAreaUnitId(Integer areaUnitId) {
        this.areaUnitId = areaUnitId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Integer getChangeStatusType() {
        return changeStatusType;
    }

    public void setChangeStatusType(Integer changeStatusType) {
        this.changeStatusType = changeStatusType;
    }

    public Integer getProductProductUnitId() {
        return productProductUnitId;
    }

    public void setProductProductUnitId(Integer productProductUnitId) {
        this.productProductUnitId = productProductUnitId;
    }

    public Boolean getDoneWithTable() {
        return doneWithTable;
    }

    public void setDoneWithTable(Boolean doneWithTable) {
        this.doneWithTable = doneWithTable;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getHaveTopping() {
        return haveTopping;
    }

    public void setHaveTopping(Boolean haveTopping) {
        this.haveTopping = haveTopping;
    }
}
