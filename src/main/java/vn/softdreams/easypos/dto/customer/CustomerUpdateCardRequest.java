package vn.softdreams.easypos.dto.customer;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class CustomerUpdateCardRequest implements Serializable {

    private Integer comId;
    private List<Integer> customerIds;
    private Integer type;

    private BigDecimal amount;

    private Integer point;

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public List<Integer> getCustomerIds() {
        return customerIds;
    }

    public void setCustomerIds(List<Integer> customerIds) {
        this.customerIds = customerIds;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount != null ? amount : BigDecimal.ZERO;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getPoint() {
        return point != null ? point : 0;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }
}
