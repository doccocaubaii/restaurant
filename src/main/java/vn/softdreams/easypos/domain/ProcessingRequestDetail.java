package vn.softdreams.easypos.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Entity
@DynamicUpdate
@Table(name = "processing_request_detail")
public class ProcessingRequestDetail extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "bill_id")
    private Integer billId;

    @Column(name = "area_unit_id")
    private Integer areaUnitId;

    @Column(name = "product_product_unit_id")
    private Integer productProductUnitId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "quantity")
    private BigDecimal quantity;

    @Column(name = "position")
    private Integer position;

    @Column(name = "ref_id")
    private Integer refId;

    @Column(name = "is_topping")
    private Boolean isTopping;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "processing_request_id", nullable = false, referencedColumnName = "id")
    @JsonBackReference
    private ProcessingRequest processingRequest;

    @JsonManagedReference
    @OneToMany(mappedBy = "processingRequestDetail", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ProcessingProduct> processingProducts;

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

    public Integer getAreaUnitId() {
        return areaUnitId;
    }

    public void setAreaUnitId(Integer areaUnitId) {
        this.areaUnitId = areaUnitId;
    }

    public Integer getProductProductUnitId() {
        return productProductUnitId;
    }

    public void setProductProductUnitId(Integer productProductUnitId) {
        this.productProductUnitId = productProductUnitId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public ProcessingRequest getProcessingRequest() {
        return processingRequest;
    }

    public void setProcessingRequest(ProcessingRequest processingRequest) {
        this.processingRequest = processingRequest;
    }

    public List<ProcessingProduct> getProcessingProducts() {
        return processingProducts;
    }

    public void setProcessingProducts(List<ProcessingProduct> processingProducts) {
        this.processingProducts = processingProducts;
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
}
