package vn.softdreams.easypos.dto.bill;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class BillCompletedReturnRequest implements Serializable {

    @NotNull(message = ExceptionConstants.COMPANY_ID_NOT_NULL)
    private Integer comId;

    @NotNull(message = ExceptionConstants.BILL_ID_NOT_NULL)
    private Integer billId;

    @NotNull(message = ExceptionConstants.AMOUNT_NOT_NULL)
    private BigDecimal amount;

    @NotBlank(message = ExceptionConstants.BILL_PAYMENTS_NOT_NULL)
    private String paymentMethod;

    @NotBlank(message = ExceptionConstants.TAX_AUTHORITY_NOT_NULL)
    private String taxAuthorityCode;

    private String description;

    @Valid
    @NotNull(message = ExceptionConstants.BILL_PRODUCTS_IS_EMPTY)
    private List<ProductReturn> products;

    public static class ProductReturn {

        @NotNull(message = ExceptionConstants.PRODUCT_ID_NOT_NULL)
        private Integer productProductUnitId;

        @NotNull(message = ExceptionConstants.PRODUCT_NAME_NOT_NULL)
        private String productName;

        @NotNull(message = ExceptionConstants.PRODUCT_CODE_NOT_NULL)
        private String productCode;

        private Integer unitId;

        private String unitName;

        @NotNull(message = ExceptionConstants.QUANTITY_NOT_NULL)
        private BigDecimal quantity;

        @NotNull(message = ExceptionConstants.UNIT_PRICE_NOT_NULL)
        private BigDecimal unitPrice;

        public Integer getProductProductUnitId() {
            return productProductUnitId;
        }

        public void setProductProductUnitId(Integer productProductUnitId) {
            this.productProductUnitId = productProductUnitId;
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

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public Integer getUnitId() {
            return unitId;
        }

        public void setUnitId(Integer unitId) {
            this.unitId = unitId;
        }

        public String getUnitName() {
            return unitName;
        }

        public void setUnitName(String unitName) {
            this.unitName = unitName;
        }

        public String getProductCode() {
            return productCode;
        }

        public void setProductCode(String productCode) {
            this.productCode = productCode;
        }
    }

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public Integer getBillId() {
        return billId;
    }

    public void setBillId(Integer billId) {
        this.billId = billId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ProductReturn> getProducts() {
        return products;
    }

    public void setProducts(List<ProductReturn> products) {
        this.products = products;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTaxAuthorityCode() {
        return taxAuthorityCode;
    }

    public void setTaxAuthorityCode(String taxAuthorityCode) {
        this.taxAuthorityCode = taxAuthorityCode;
    }
}
