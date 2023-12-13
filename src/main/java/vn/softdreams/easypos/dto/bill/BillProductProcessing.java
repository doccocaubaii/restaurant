package vn.softdreams.easypos.dto.bill;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import vn.softdreams.easypos.config.Constants;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

public class BillProductProcessing {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer productId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer processingRequestId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer productProductUnitId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String productName;

    private String unitName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    private ZonedDateTime createTime;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    private ZonedDateTime updateTime;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String creatorName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal notifiedQuantity;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal processingQuantity;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal processedQuantity;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal deliveredQuantity;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal canceledQuantity;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String topping;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer billId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String billCode;

    private Integer areaUnitId;

    private String areaUnitName;
    private String areaName;
    private BigDecimal totalQuantity;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isTopping;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer refId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer requestDetailId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<AreaProductProcessing> areaProcessing;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<BillProductToppingProcessing> toppings;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ProcessingProductDelete> deletes;

    public static class AreaProductProcessing {

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Integer processingRequestId;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Integer productProductUnitId;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String productName;

        private String unitName;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
        private ZonedDateTime createTime;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String creatorName;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private BigDecimal processingQuantity;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private BigDecimal processedQuantity;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private BigDecimal deliveredQuantity;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private BigDecimal canceledQuantity;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Integer billId;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Integer id;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private List<BillProductToppingProcessing> toppings;

        public Integer getProcessingRequestId() {
            return processingRequestId;
        }

        public void setProcessingRequestId(Integer processingRequestId) {
            this.processingRequestId = processingRequestId;
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

        public String getUnitName() {
            return unitName;
        }

        public void setUnitName(String unitName) {
            this.unitName = unitName;
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

        public ZonedDateTime getCreateTime() {
            return createTime;
        }

        public void setCreateTime(ZonedDateTime createTime) {
            this.createTime = createTime;
        }

        public String getCreatorName() {
            return creatorName;
        }

        public void setCreatorName(String creatorName) {
            this.creatorName = creatorName;
        }

        public Integer getBillId() {
            return billId;
        }

        public void setBillId(Integer billId) {
            this.billId = billId;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public List<BillProductToppingProcessing> getToppings() {
            return toppings;
        }

        public void setToppings(List<BillProductToppingProcessing> toppings) {
            this.toppings = toppings;
        }
    }

    public static class BillProductToppingProcessing {

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Integer processingRequestId;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Integer productProductUnitId;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String productName;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
        private ZonedDateTime createTime;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String creatorName;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private BigDecimal quantity;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Integer billId;

        private Integer areaUnitId;

        private String areaUnitName;

        public Integer getProcessingRequestId() {
            return processingRequestId;
        }

        public void setProcessingRequestId(Integer processingRequestId) {
            this.processingRequestId = processingRequestId;
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

        public ZonedDateTime getCreateTime() {
            return createTime;
        }

        public void setCreateTime(ZonedDateTime createTime) {
            this.createTime = createTime;
        }

        public String getCreatorName() {
            return creatorName;
        }

        public void setCreatorName(String creatorName) {
            this.creatorName = creatorName;
        }

        public BigDecimal getQuantity() {
            return quantity;
        }

        public void setQuantity(BigDecimal quantity) {
            this.quantity = quantity;
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

        public String getAreaUnitName() {
            return areaUnitName;
        }

        public void setAreaUnitName(String areaUnitName) {
            this.areaUnitName = areaUnitName;
        }
    }

    public static class ProcessingProductDelete {

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String creatorName;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
        private ZonedDateTime createTime;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private BigDecimal canceledQuantity;

        public String getCreatorName() {
            return creatorName;
        }

        public void setCreatorName(String creatorName) {
            this.creatorName = creatorName;
        }

        public ZonedDateTime getCreateTime() {
            return createTime;
        }

        public void setCreateTime(ZonedDateTime createTime) {
            this.createTime = createTime;
        }

        public BigDecimal getCanceledQuantity() {
            return canceledQuantity;
        }

        public void setCanceledQuantity(BigDecimal canceledQuantity) {
            this.canceledQuantity = canceledQuantity;
        }
    }

    public BillProductProcessing() {}

    public BillProductProcessing(
        Integer productProductUnitId,
        String productName,
        String unitName,
        ZonedDateTime createTime,
        ZonedDateTime updateTime,
        String creatorName,
        BigDecimal notifiedQuantity,
        BigDecimal processingQuantity,
        BigDecimal processedQuantity,
        BigDecimal canceledQuantity,
        Integer billId,
        String billCode,
        Integer areaUnitId,
        String areaUnitName,
        String areaName,
        Boolean isTopping,
        Integer refId,
        Integer id
    ) {
        this.productProductUnitId = productProductUnitId;
        this.productName = productName;
        this.unitName = unitName;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.creatorName = creatorName;
        this.processingQuantity = processingQuantity;
        this.notifiedQuantity = notifiedQuantity;
        this.processedQuantity = processedQuantity;
        this.canceledQuantity = canceledQuantity;
        this.billId = billId;
        this.billCode = billCode;
        this.areaUnitId = areaUnitId;
        this.areaUnitName = areaUnitName;
        this.areaName = areaName;
        this.isTopping = isTopping;
        this.refId = refId;
        this.id = id;
    }

    public BillProductProcessing(ZonedDateTime createTime, String creatorName, BigDecimal canceledQuantity, Integer id) {
        this.createTime = createTime;
        this.creatorName = creatorName;
        this.canceledQuantity = canceledQuantity;
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
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

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public ZonedDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(ZonedDateTime createTime) {
        this.createTime = createTime;
    }

    public ZonedDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(ZonedDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getBillId() {
        return billId;
    }

    public void setBillId(Integer billId) {
        this.billId = billId;
    }

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public Integer getAreaUnitId() {
        return areaUnitId;
    }

    public void setAreaUnitId(Integer areaUnitId) {
        this.areaUnitId = areaUnitId;
    }

    public String getAreaUnitName() {
        return areaUnitName;
    }

    public void setAreaUnitName(String areaUnitName) {
        this.areaUnitName = areaUnitName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public List<AreaProductProcessing> getAreaProcessing() {
        return areaProcessing;
    }

    public void setAreaProcessing(List<AreaProductProcessing> areaProcessings) {
        this.areaProcessing = areaProcessings;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Integer getProcessingRequestId() {
        return processingRequestId;
    }

    public void setProcessingRequestId(Integer processingRequestId) {
        this.processingRequestId = processingRequestId;
    }

    public Boolean getIsTopping() {
        return isTopping;
    }

    public void setIsTopping(Boolean topping) {
        isTopping = topping;
    }

    public List<BillProductToppingProcessing> getToppings() {
        return toppings;
    }

    public void setToppings(List<BillProductToppingProcessing> toppings) {
        this.toppings = toppings;
    }

    public List<ProcessingProductDelete> getDeletes() {
        return deletes;
    }

    public void setDeletes(List<ProcessingProductDelete> deletes) {
        this.deletes = deletes;
    }

    public BigDecimal getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(BigDecimal totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public Integer getRefId() {
        return refId;
    }

    public void setRefId(Integer refId) {
        this.refId = refId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRequestDetailId() {
        return requestDetailId;
    }

    public void setRequestDetailId(Integer requestDetailId) {
        this.requestDetailId = requestDetailId;
    }

    public BigDecimal getProcessingQuantity() {
        return processingQuantity;
    }

    public void setProcessingQuantity(BigDecimal processingQuantity) {
        this.processingQuantity = processingQuantity;
    }

    public BigDecimal getNotifiedQuantity() {
        return notifiedQuantity;
    }

    public void setNotifiedQuantity(BigDecimal notifiedQuantity) {
        this.notifiedQuantity = notifiedQuantity;
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

    public String getTopping() {
        return topping;
    }

    public void setTopping(String topping) {
        this.topping = topping;
    }
}
