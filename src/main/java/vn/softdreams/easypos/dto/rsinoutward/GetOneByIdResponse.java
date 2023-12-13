package vn.softdreams.easypos.dto.rsinoutward;

import vn.softdreams.easypos.dto.rsInoutWardDetail.GetOneByIdDetailResponse;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class GetOneByIdResponse implements Serializable {

    private Integer id;
    private Integer billId;
    private Integer type;
    private String typeDesc;
    private String date;
    private String no;
    private String supplierName;
    private Integer supplierId;
    private String customerName;
    private Integer customerId;
    private BigDecimal quantity;
    private BigDecimal amount;
    private BigDecimal discountAmount;
    private BigDecimal costAmount;
    private BigDecimal totalAmount;
    private String description;
    private String creator;
    private List<GetOneByIdDetailResponse> detail;

    public GetOneByIdResponse(
        Integer id,
        Integer billId,
        Integer type,
        String typeDesc,
        String date,
        String no,
        String supplierName,
        Integer supplierId,
        String customerName,
        Integer customerId,
        BigDecimal quantity,
        BigDecimal amount,
        BigDecimal discountAmount,
        BigDecimal costAmount,
        BigDecimal totalAmount,
        String description,
        String creator
    ) {
        this.id = id;
        this.billId = billId;
        this.type = type;
        this.typeDesc = typeDesc;
        this.date = date;
        this.no = no;
        this.supplierName = supplierName;
        this.supplierId = supplierId;
        this.customerName = customerName;
        this.customerId = customerId;
        this.quantity = quantity;
        this.amount = amount;
        this.discountAmount = discountAmount;
        this.costAmount = costAmount;
        this.totalAmount = totalAmount;
        this.description = description;
        this.creator = creator;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBillId() {
        return billId;
    }

    public void setBillId(Integer billId) {
        this.billId = billId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getQuantity() {
        if (Objects.equals(quantity, BigDecimal.ZERO) || quantity == null) {
            return BigDecimal.valueOf(0.0);
        }
        return quantity.setScale(6);
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getAmount() {
        if (Objects.equals(amount, BigDecimal.ZERO) || amount == null) {
            return BigDecimal.valueOf(0.0);
        }
        return amount.setScale(6);
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getDiscountAmount() {
        if (Objects.equals(discountAmount, BigDecimal.ZERO) || discountAmount == null) {
            return BigDecimal.valueOf(0.0);
        }
        return discountAmount.setScale(6);
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getCostAmount() {
        if (Objects.equals(costAmount, BigDecimal.ZERO) || costAmount == null) {
            return BigDecimal.valueOf(0.0);
        }
        return costAmount.setScale(6);
    }

    public void setCostAmount(BigDecimal costAmount) {
        this.costAmount = costAmount;
    }

    public BigDecimal getTotalAmount() {
        if (Objects.equals(totalAmount, BigDecimal.ZERO) || totalAmount == null) {
            return BigDecimal.valueOf(0.0);
        }
        return totalAmount.setScale(6);
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public List<GetOneByIdDetailResponse> getDetail() {
        return detail;
    }

    public void setDetail(List<GetOneByIdDetailResponse> detail) {
        this.detail = detail;
    }
}
