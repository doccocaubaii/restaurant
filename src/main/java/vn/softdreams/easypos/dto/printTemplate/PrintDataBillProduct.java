package vn.softdreams.easypos.dto.printTemplate;

import java.math.BigDecimal;
import java.util.List;

public class PrintDataBillProduct {

    private Integer id;
    //    {Ten_san_pham}
    private String productName;
    //    {Don_vi_san_pham}

    private String unit;
    //    {Gia_san_pham}
    private BigDecimal unitPrice;
    //    {Giam_Tru_Thue}
    private BigDecimal totalDiscount;
    //    {Giam_gia_san_pham}
    private BigDecimal discountAmount;
    //    {Tien_Thue}
    private BigDecimal vatAmount;
    //    {tong_Tien_Phai_Tra}
    private BigDecimal totalAmount;

    private List<PrintDataTopping> toppings;
    private Integer vatRate;
    private BigDecimal quantity;
    private BigDecimal displayTotalDiscount;

    private Integer discountVatRate;

    private BigDecimal amount;
    private BigDecimal displayAmount;
    private BigDecimal displayVatAmount;
    private BigDecimal displayDiscountAmount;
    private BigDecimal productAmount;
    private Integer position;

    public BigDecimal getDisplayAmount() {
        return displayAmount;
    }

    public void setDisplayAmount(BigDecimal displayAmount) {
        this.displayAmount = displayAmount;
    }

    public BigDecimal getDisplayVatAmount() {
        return displayVatAmount;
    }

    public void setDisplayVatAmount(BigDecimal dispalyVatAmount) {
        this.displayVatAmount = dispalyVatAmount;
    }

    public BigDecimal getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(BigDecimal productAmount) {
        this.productAmount = productAmount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getDiscountVatRate() {
        return discountVatRate;
    }

    public void setDiscountVatRate(Integer discountVatRate) {
        this.discountVatRate = discountVatRate;
    }

    public BigDecimal getDisplayTotalDiscount() {
        return displayTotalDiscount;
    }

    public void setDisplayTotalDiscount(BigDecimal displayTotalDiscount) {
        this.displayTotalDiscount = displayTotalDiscount;
    }

    public List<PrintDataTopping> getToppings() {
        return toppings;
    }

    public void setToppings(List<PrintDataTopping> toppings) {
        this.toppings = toppings;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(BigDecimal totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
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

    public Integer getVatRate() {
        return vatRate;
    }

    public void setVatRate(Integer vatRate) {
        this.vatRate = vatRate;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public BigDecimal getDisplayDiscountAmount() {
        return displayDiscountAmount;
    }

    public void setDisplayDiscountAmount(BigDecimal displayDiscountAmount) {
        this.displayDiscountAmount = displayDiscountAmount;
    }
}
