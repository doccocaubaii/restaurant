package vn.softdreams.easypos.dto.rsinoutward;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import vn.softdreams.easypos.config.Constants;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

public class RsInoutWardResponse implements Serializable {

    private Integer id;
    private Integer type;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    private ZonedDateTime date;

    private String no;
    private Integer productTypes;
    private BigDecimal quantity;
    private BigDecimal totalAmount;
    private String customerName;
    private Integer customerId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long countAll;

    public RsInoutWardResponse(
        Integer id,
        Integer type,
        ZonedDateTime date,
        String no,
        Integer productTypes,
        BigDecimal quantity,
        BigDecimal totalAmount,
        String customerName,
        Integer customerId,
        Long countAll
    ) {
        this.id = id;
        this.type = type;
        this.date = date;
        this.no = no;
        this.productTypes = productTypes;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.customerName = customerName;
        this.customerId = customerId;
        this.countAll = countAll;
    }

    public Long getCountAll() {
        return countAll;
    }

    public void setCountAll(Long countAll) {
        this.countAll = countAll;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public Integer getProductTypes() {
        return productTypes;
    }

    public void setProductTypes(Integer productTypes) {
        this.productTypes = productTypes;
    }

    public BigDecimal getQuantity() {
        if (Objects.equals(quantity, BigDecimal.ZERO) || quantity == null) {
            return BigDecimal.valueOf(0.0);
        }
        return quantity.setScale(6);
    }

    public BigDecimal getTotalAmount() {
        if (Objects.equals(totalAmount, BigDecimal.ZERO) || totalAmount == null) {
            return BigDecimal.valueOf(0.0);
        }
        return totalAmount.setScale(6);
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

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
