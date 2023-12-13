package vn.softdreams.easypos.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@DynamicUpdate
@Table(name = "processing_request")
public class ProcessingRequest extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "bill_id")
    private Integer billId;

    @Column(name = "area_unit_id")
    private Integer areaUnitId;

    @JsonManagedReference
    @OneToMany(mappedBy = "processingRequest", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ProcessingRequestDetail> details;

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

    public List<ProcessingRequestDetail> getDetails() {
        return details;
    }

    public void setDetails(List<ProcessingRequestDetail> details) {
        this.details = details;
    }
}
