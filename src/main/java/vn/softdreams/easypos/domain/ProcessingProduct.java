package vn.softdreams.easypos.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@DynamicUpdate
@Table(name = "processing_product")
public class ProcessingProduct extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "bill_id")
    private Integer billId;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "request_detail_id", nullable = false, referencedColumnName = "id")
    @JsonBackReference
    private ProcessingRequestDetail processingRequestDetail;

    @Column(name = "product_product_unit_id")
    private Integer productProductUnitId;

    @Column(name = "ref_id")
    private Integer refId;

    @Column(name = "is_topping")
    private Boolean isTopping;

    @Column(name = "bill_product_id")
    private Integer billProductId;

    @Column(name = "notified_quantity")
    private BigDecimal notifiedQuantity;

    @Column(name = "processing_quantity")
    private BigDecimal processingQuantity;

    @Column(name = "processed_quantity")
    private BigDecimal processedQuantity;

    @Column(name = "delivered_quantity")
    private BigDecimal deliveredQuantity;

    @Column(name = "canceled_quantity")
    private BigDecimal canceledQuantity;

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

    public ProcessingRequestDetail getProcessingRequestDetail() {
        return processingRequestDetail;
    }

    public void setProcessingRequestDetail(ProcessingRequestDetail processingRequestDetail) {
        this.processingRequestDetail = processingRequestDetail;
    }

    public Integer getProductProductUnitId() {
        return productProductUnitId;
    }

    public void setProductProductUnitId(Integer productProductUnitId) {
        this.productProductUnitId = productProductUnitId;
    }

    public Integer getRefId() {
        return refId;
    }

    public void setRefId(Integer refId) {
        this.refId = refId;
    }

    public Boolean getIsTopping() {
        return isTopping;
    }

    public void setIsTopping(Boolean topping) {
        isTopping = topping;
    }

    public Integer getBillProductId() {
        return billProductId;
    }

    public void setBillProductId(Integer billProductId) {
        this.billProductId = billProductId;
    }

    public BigDecimal getProcessingQuantity() {
        return processingQuantity;
    }

    public void setProcessingQuantity(BigDecimal processingQuantity) {
        this.processingQuantity = processingQuantity;
    }

    public BigDecimal getProcessedQuantity() {
        return processedQuantity;
    }

    public void setProcessedQuantity(BigDecimal processedQuantity) {
        this.processedQuantity = processedQuantity;
    }

    public BigDecimal getDeliveredQuantity() {
        return deliveredQuantity;
    }

    public void setDeliveredQuantity(BigDecimal deliveredQuantity) {
        this.deliveredQuantity = deliveredQuantity;
    }

    public BigDecimal getCanceledQuantity() {
        return canceledQuantity;
    }

    public void setCanceledQuantity(BigDecimal canceledQuantity) {
        this.canceledQuantity = canceledQuantity;
    }

    public BigDecimal getNotifiedQuantity() {
        return notifiedQuantity;
    }

    public void setNotifiedQuantity(BigDecimal notifiedQuantity) {
        this.notifiedQuantity = notifiedQuantity;
    }
}
