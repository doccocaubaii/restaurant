package vn.softdreams.easypos.dto.bill;

import com.fasterxml.jackson.annotation.JsonFormat;
import vn.softdreams.easypos.config.Constants;
import vn.softdreams.easypos.dto.customerCard.CustomerCardInformation;
import vn.softdreams.easypos.dto.productGroup.ProductProcessingAreaResult;
import vn.softdreams.easypos.dto.voucher.VoucherResponse;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

public class BillDetailResponse {

    private Integer id;
    private String code;
    private String code2;
    private Integer comId;
    private Integer status;
    private Integer statusInvoice;
    private Integer customerId;
    private String customerName;
    private Integer creatorId;
    private String creatorName;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    private ZonedDateTime createTime;

    private Integer deliveryType;
    private BigDecimal quantity;
    private BigDecimal amount;
    private BigDecimal discountAmount;
    private BigDecimal totalPreTax;
    private Integer vatRate;
    private BigDecimal vatAmount;
    private BigDecimal totalAmount;
    private Integer areaUnitId;
    private String areaName;
    private String areaUnitName;
    private String taxAuthorityCode;
    private Integer reservationId;
    private Integer typeInv;
    private BigDecimal productDiscountAmount;
    private String description;
    private Boolean haveDiscountVat;
    private Integer discountVatRate;
    private BigDecimal discountVatAmount;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    private ZonedDateTime billDate;

    private String billIdReturns;
    private String buyerName;
    private BigDecimal voucherAmount;

    private BillPaymentResponse payment;
    private List<BillProductResponse> products;
    private List<VoucherResponse> vouchers;
    private CustomerMoreInformation customerCard;
    private BillExtraConfig extraConfig;

    public BillExtraConfig getExtraConfig() {
        return extraConfig;
    }

    public void setExtraConfig(BillExtraConfig extraConfig) {
        this.extraConfig = extraConfig;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode2() {
        return code2;
    }

    public void setCode2(String code2) {
        this.code2 = code2;
    }

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatusInvoice() {
        return statusInvoice;
    }

    public void setStatusInvoice(Integer statusInvoice) {
        this.statusInvoice = statusInvoice;
    }

    public Integer getTypeInv() {
        return typeInv;
    }

    public void setTypeInv(Integer typeInv) {
        this.typeInv = typeInv;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

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

    public Integer getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(Integer deliveryType) {
        this.deliveryType = deliveryType;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getTotalPreTax() {
        return totalPreTax;
    }

    public void setTotalPreTax(BigDecimal totalPreTax) {
        this.totalPreTax = totalPreTax;
    }

    public Integer getVatRate() {
        return vatRate;
    }

    public void setVatRate(Integer vatRate) {
        this.vatRate = vatRate;
    }

    public BigDecimal getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(BigDecimal vatAmount) {
        this.vatAmount = vatAmount;
    }

    public Integer getDiscountVatRate() {
        return discountVatRate;
    }

    public void setDiscountVatRate(Integer discountVatRate) {
        this.discountVatRate = discountVatRate;
    }

    public BigDecimal getDiscountVatAmount() {
        return discountVatAmount;
    }

    public void setDiscountVatAmount(BigDecimal discountVatAmount) {
        this.discountVatAmount = discountVatAmount;
    }

    public Boolean getHaveDiscountVat() {
        return haveDiscountVat;
    }

    public void setHaveDiscountVat(Boolean haveDiscountVat) {
        this.haveDiscountVat = haveDiscountVat;
    }

    public BigDecimal getProductDiscountAmount() {
        return productDiscountAmount;
    }

    public void setProductDiscountAmount(BigDecimal productDiscountAmount) {
        this.productDiscountAmount = productDiscountAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getAreaUnitId() {
        return areaUnitId;
    }

    public void setAreaUnitId(Integer areaUnitId) {
        this.areaUnitId = areaUnitId;
    }

    public BigDecimal getVoucherAmount() {
        return voucherAmount;
    }

    public void setVoucherAmount(BigDecimal voucherAmount) {
        this.voucherAmount = voucherAmount;
    }

    public List<VoucherResponse> getVouchers() {
        return vouchers;
    }

    public void setVouchers(List<VoucherResponse> vouchers) {
        this.vouchers = vouchers;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaUnitName() {
        return areaUnitName;
    }

    public void setAreaUnitName(String areaUnitName) {
        this.areaUnitName = areaUnitName;
    }

    public String getTaxAuthorityCode() {
        return taxAuthorityCode;
    }

    public void setTaxAuthorityCode(String taxAuthorityCode) {
        this.taxAuthorityCode = taxAuthorityCode;
    }

    public Integer getReservationId() {
        return reservationId;
    }

    public void setReservationId(Integer reservationId) {
        this.reservationId = reservationId;
    }

    public ZonedDateTime getBillDate() {
        return billDate;
    }

    public void setBillDate(ZonedDateTime billDate) {
        this.billDate = billDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBillIdReturns() {
        return billIdReturns;
    }

    public void setBillIdReturns(String billIdReturns) {
        this.billIdReturns = billIdReturns;
    }

    public BillPaymentResponse getPayment() {
        return payment;
    }

    public void setPayment(BillPaymentResponse payment) {
        this.payment = payment;
    }

    public List<BillProductResponse> getProducts() {
        return products;
    }

    public void setProducts(List<BillProductResponse> products) {
        this.products = products;
    }

    public CustomerMoreInformation getCustomerCard() {
        return customerCard;
    }

    public void setCustomerCard(CustomerMoreInformation customerCard) {
        this.customerCard = customerCard;
    }

    public static class BillPaymentResponse {

        private String paymentMethod;

        private BigDecimal amount;
        private BigDecimal cardAmount;

        private BigDecimal refund;

        private Integer debtType;

        private BigDecimal debt;

        public String getPaymentMethod() {
            return paymentMethod;
        }

        public void setPaymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public BigDecimal getCardAmount() {
            return cardAmount;
        }

        public void setCardAmount(BigDecimal cardAmount) {
            this.cardAmount = cardAmount;
        }

        public BigDecimal getRefund() {
            return refund;
        }

        public void setRefund(BigDecimal refund) {
            this.refund = refund;
        }

        public Integer getDebtType() {
            return debtType;
        }

        public void setDebtType(Integer debtType) {
            this.debtType = debtType;
        }

        public BigDecimal getDebt() {
            return debt;
        }

        public void setDebt(BigDecimal debt) {
            this.debt = debt;
        }
    }

    public static class BillProductResponse {

        private Integer id;

        private Integer productProductUnitId;
        private Integer productId;
        private String productName;
        private String productCode;
        private BigDecimal quantity;
        private Integer unitId;
        private String unit;
        private BigDecimal unitPrice;
        private BigDecimal amount;
        private BigDecimal displayAmount;
        private BigDecimal discountAmount;
        private BigDecimal totalPreTax;
        private Integer vatRate;
        private BigDecimal vatAmount;
        private BigDecimal totalAmount;
        private Integer feature;
        private Integer position;
        private String imageUrl;
        private Boolean isTopping;
        private Integer parentProductId;
        private Boolean haveTopping;
        private BigDecimal inventoryCount;
        private Boolean inventoryTracking;
        private Integer discountVatRate;
        private BigDecimal totalDiscount;
        private BigDecimal displayTotalDiscount;
        private ProductProcessingAreaResult processingArea;

        private BigDecimal notifiedQuantity;
        private BigDecimal processingQuantity;
        private BigDecimal processedQuantity;
        private BigDecimal canceledQuantity;
        private BigDecimal deliveredQuantity;
        private List<BillProductToppingResponse> toppings;

        public BigDecimal getDisplayTotalDiscount() {
            return displayTotalDiscount;
        }

        public void setDisplayTotalDiscount(BigDecimal displayTotalDiscount) {
            this.displayTotalDiscount = displayTotalDiscount;
        }

        public ProductProcessingAreaResult getProcessingArea() {
            return processingArea;
        }

        public void setProcessingArea(ProductProcessingAreaResult processingArea) {
            this.processingArea = processingArea;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getProductProductUnitId() {
            return productProductUnitId;
        }

        public void setProductProductUnitId(Integer productProductUnitId) {
            this.productProductUnitId = productProductUnitId;
        }

        public Integer getProductId() {
            return productId;
        }

        public void setProductId(Integer productId) {
            this.productId = productId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public BigDecimal getDisplayAmount() {
            return displayAmount;
        }

        public void setDisplayAmount(BigDecimal displayAmount) {
            this.displayAmount = displayAmount;
        }

        public String getProductCode() {
            return productCode;
        }

        public void setProductCode(String productCode) {
            this.productCode = productCode;
        }

        public BigDecimal getQuantity() {
            return quantity;
        }

        public void setQuantity(BigDecimal quantity) {
            this.quantity = quantity;
        }

        public BigDecimal getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(BigDecimal unitPrice) {
            this.unitPrice = unitPrice;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public BigDecimal getDiscountAmount() {
            return discountAmount;
        }

        public void setDiscountAmount(BigDecimal discountAmount) {
            this.discountAmount = discountAmount;
        }

        public BigDecimal getTotalPreTax() {
            return totalPreTax;
        }

        public void setTotalPreTax(BigDecimal totalPreTax) {
            this.totalPreTax = totalPreTax;
        }

        public Integer getVatRate() {
            return vatRate;
        }

        public void setVatRate(Integer vatRate) {
            this.vatRate = vatRate;
        }

        public BigDecimal getVatAmount() {
            return vatAmount;
        }

        public void setVatAmount(BigDecimal vatAmount) {
            this.vatAmount = vatAmount;
        }

        public BigDecimal getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(BigDecimal totalAmount) {
            this.totalAmount = totalAmount;
        }

        public Integer getFeature() {
            return feature;
        }

        public void setFeature(Integer feature) {
            this.feature = feature;
        }

        public Integer getPosition() {
            return position;
        }

        public void setPosition(Integer position) {
            this.position = position;
        }

        public Integer getUnitId() {
            return unitId;
        }

        public void setUnitId(Integer unitId) {
            this.unitId = unitId;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public Boolean getIsTopping() {
            return isTopping;
        }

        public void setIsTopping(Boolean topping) {
            isTopping = topping;
        }

        public Integer getParentProductId() {
            return parentProductId;
        }

        public void setParentProductId(Integer parentProductId) {
            this.parentProductId = parentProductId;
        }

        public Boolean getHaveTopping() {
            return haveTopping;
        }

        public void setHaveTopping(Boolean haveTopping) {
            this.haveTopping = haveTopping;
        }

        public Boolean getTopping() {
            return isTopping;
        }

        public void setTopping(Boolean topping) {
            isTopping = topping;
        }

        public BigDecimal getInventoryCount() {
            return inventoryCount;
        }

        public void setInventoryCount(BigDecimal inventoryCount) {
            this.inventoryCount = inventoryCount;
        }

        public Boolean getInventoryTracking() {
            return inventoryTracking;
        }

        public void setInventoryTracking(Boolean inventoryTracking) {
            this.inventoryTracking = inventoryTracking;
        }

        public List<BillProductToppingResponse> getToppings() {
            return toppings;
        }

        public void setToppings(List<BillProductToppingResponse> toppings) {
            this.toppings = toppings;
        }

        public Integer getDiscountVatRate() {
            return discountVatRate;
        }

        public void setDiscountVatRate(Integer discountVatRate) {
            this.discountVatRate = discountVatRate;
        }

        public BigDecimal getTotalDiscount() {
            return totalDiscount;
        }

        public void setTotalDiscount(BigDecimal totalDiscount) {
            this.totalDiscount = totalDiscount;
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

        public BigDecimal getProcessingQuantity() {
            return processingQuantity;
        }

        public void setProcessingQuantity(BigDecimal processingQuantity) {
            this.processingQuantity = processingQuantity;
        }

        public BigDecimal getCanceledQuantity() {
            return canceledQuantity;
        }

        public void setCanceledQuantity(BigDecimal canceledQuantity) {
            this.canceledQuantity = canceledQuantity;
        }

        public BigDecimal getDeliveredQuantity() {
            return deliveredQuantity;
        }

        public void setDeliveredQuantity(BigDecimal deliveredQuantity) {
            this.deliveredQuantity = deliveredQuantity;
        }
    }

    public static class BillProductToppingResponse {

        private Integer id;

        private Integer productProductUnitId;
        private Integer productId;
        private String productName;
        private String productCode;
        private BigDecimal quantity;
        private BigDecimal displayQuantity;
        private Integer unitId;
        private String unit;
        private BigDecimal unitPrice;
        private BigDecimal amount;
        private BigDecimal discountAmount;
        private BigDecimal totalPreTax;
        private Integer vatRate;
        private BigDecimal vatAmount;
        private BigDecimal totalAmount;
        private Integer feature;
        private Integer position;
        private String imageUrl;
        private Boolean isTopping;
        private Integer parentProductId;
        private Boolean haveTopping;
        private BigDecimal inventoryCount;
        private Boolean inventoryTracking;
        private Integer discountVatRate;
        private BigDecimal totalDiscount;
        private ProductProcessingAreaResult processingArea;
        private BigDecimal notifiedQuantity;
        private BigDecimal processingQuantity;
        private BigDecimal processedQuantity;
        private BigDecimal canceledQuantity;
        private BigDecimal deliveredQuantity;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getProductProductUnitId() {
            return productProductUnitId;
        }

        public void setProductProductUnitId(Integer productProductUnitId) {
            this.productProductUnitId = productProductUnitId;
        }

        public Integer getProductId() {
            return productId;
        }

        public void setProductId(Integer productId) {
            this.productId = productId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getProductCode() {
            return productCode;
        }

        public void setProductCode(String productCode) {
            this.productCode = productCode;
        }

        public BigDecimal getQuantity() {
            return quantity;
        }

        public void setQuantity(BigDecimal quantity) {
            this.quantity = quantity;
        }

        public BigDecimal getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(BigDecimal unitPrice) {
            this.unitPrice = unitPrice;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public BigDecimal getDiscountAmount() {
            return discountAmount;
        }

        public void setDiscountAmount(BigDecimal discountAmount) {
            this.discountAmount = discountAmount;
        }

        public BigDecimal getTotalPreTax() {
            return totalPreTax;
        }

        public void setTotalPreTax(BigDecimal totalPreTax) {
            this.totalPreTax = totalPreTax;
        }

        public Integer getVatRate() {
            return vatRate;
        }

        public void setVatRate(Integer vatRate) {
            this.vatRate = vatRate;
        }

        public BigDecimal getVatAmount() {
            return vatAmount;
        }

        public void setVatAmount(BigDecimal vatAmount) {
            this.vatAmount = vatAmount;
        }

        public BigDecimal getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(BigDecimal totalAmount) {
            this.totalAmount = totalAmount;
        }

        public Integer getFeature() {
            return feature;
        }

        public void setFeature(Integer feature) {
            this.feature = feature;
        }

        public Integer getPosition() {
            return position;
        }

        public void setPosition(Integer position) {
            this.position = position;
        }

        public Integer getUnitId() {
            return unitId;
        }

        public void setUnitId(Integer unitId) {
            this.unitId = unitId;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public Boolean getIsTopping() {
            return isTopping;
        }

        public void setIsTopping(Boolean topping) {
            isTopping = topping;
        }

        public Integer getParentProductId() {
            return parentProductId;
        }

        public void setParentProductId(Integer parentProductId) {
            this.parentProductId = parentProductId;
        }

        public Boolean getHaveTopping() {
            return haveTopping;
        }

        public void setHaveTopping(Boolean haveTopping) {
            this.haveTopping = haveTopping;
        }

        public Boolean getTopping() {
            return isTopping;
        }

        public void setTopping(Boolean topping) {
            isTopping = topping;
        }

        public BigDecimal getInventoryCount() {
            return inventoryCount;
        }

        public void setInventoryCount(BigDecimal inventoryCount) {
            this.inventoryCount = inventoryCount;
        }

        public Boolean getInventoryTracking() {
            return inventoryTracking;
        }

        public void setInventoryTracking(Boolean inventoryTracking) {
            this.inventoryTracking = inventoryTracking;
        }

        public BigDecimal getDisplayQuantity() {
            return displayQuantity;
        }

        public void setDisplayQuantity(BigDecimal displayQuantity) {
            this.displayQuantity = displayQuantity;
        }

        public Integer getDiscountVatRate() {
            return discountVatRate;
        }

        public void setDiscountVatRate(Integer discountVatRate) {
            this.discountVatRate = discountVatRate;
        }

        public BigDecimal getTotalDiscount() {
            return totalDiscount;
        }

        public void setTotalDiscount(BigDecimal totalDiscount) {
            this.totalDiscount = totalDiscount;
        }

        public ProductProcessingAreaResult getProcessingArea() {
            return processingArea;
        }

        public void setProcessingArea(ProductProcessingAreaResult processingArea) {
            this.processingArea = processingArea;
        }

        public BigDecimal getNotifiedQuantity() {
            return notifiedQuantity;
        }

        public void setNotifiedQuantity(BigDecimal notifiedQuantity) {
            this.notifiedQuantity = notifiedQuantity;
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

        public BigDecimal getCanceledQuantity() {
            return canceledQuantity;
        }

        public void setCanceledQuantity(BigDecimal canceledQuantity) {
            this.canceledQuantity = canceledQuantity;
        }

        public BigDecimal getDeliveredQuantity() {
            return deliveredQuantity;
        }

        public void setDeliveredQuantity(BigDecimal deliveredQuantity) {
            this.deliveredQuantity = deliveredQuantity;
        }
    }

    public static class CustomerMoreInformation {

        private Integer pointBalance;
        private BigDecimal moneyBalance;
        private CustomerCardInformation cardInformation;

        public Integer getPointBalance() {
            return pointBalance;
        }

        public void setPointBalance(Integer pointBalance) {
            this.pointBalance = pointBalance;
        }

        public BigDecimal getMoneyBalance() {
            return moneyBalance;
        }

        public void setMoneyBalance(BigDecimal moneyBalance) {
            this.moneyBalance = moneyBalance;
        }

        public CustomerCardInformation getCardInformation() {
            return cardInformation;
        }

        public void setCardInformation(CustomerCardInformation cardInformation) {
            this.cardInformation = cardInformation;
        }
    }
}
