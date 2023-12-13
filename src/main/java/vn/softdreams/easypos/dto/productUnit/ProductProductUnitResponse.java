package vn.softdreams.easypos.dto.productUnit;

import vn.softdreams.easypos.dto.productGroup.ProductProcessingAreaDetail;

import java.math.BigDecimal;

public class ProductProductUnitResponse {

    private Integer id;
    private Integer comId;
    private Integer productId;
    private Integer productUnitId;
    private String unitName;
    private Boolean isPrimary;
    private BigDecimal convertRate;
    private Integer formula;
    private BigDecimal salePrice;
    private BigDecimal purchasePrice;
    private String description;
    private Boolean directSale;
    private BigDecimal onHand;
    private String barcode;
    private ProductProcessingAreaDetail processingArea;

    public ProductProductUnitResponse() {}

    public ProductProductUnitResponse(
        Integer id,
        Integer comId,
        Integer productId,
        Integer productUnitId,
        String unitName,
        Boolean isPrimary,
        BigDecimal convertRate,
        Integer formula,
        BigDecimal salePrice,
        BigDecimal purchasePrice,
        String description,
        Boolean directSale,
        BigDecimal onHand,
        String barcode
    ) {
        this.id = id;
        this.comId = comId;
        this.productId = productId;
        this.productUnitId = productUnitId;
        this.unitName = unitName;
        this.isPrimary = isPrimary;
        this.convertRate = convertRate;
        this.formula = formula;
        this.salePrice = salePrice;
        this.purchasePrice = purchasePrice;
        this.description = description;
        this.directSale = directSale;
        this.onHand = onHand;
        this.barcode = barcode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getProductUnitId() {
        return productUnitId;
    }

    public void setProductUnitId(Integer productUnitId) {
        this.productUnitId = productUnitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public BigDecimal getConvertRate() {
        return convertRate;
    }

    public void setConvertRate(BigDecimal convertRate) {
        this.convertRate = convertRate;
    }

    public Integer getFormula() {
        return formula;
    }

    public void setFormula(Integer formula) {
        this.formula = formula;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(Boolean primary) {
        isPrimary = primary;
    }

    public Boolean getDirectSale() {
        return directSale;
    }

    public void setDirectSale(Boolean directSale) {
        this.directSale = directSale;
    }

    public BigDecimal getOnHand() {
        return onHand;
    }

    public void setOnHand(BigDecimal onHand) {
        this.onHand = onHand;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public ProductProcessingAreaDetail getProcessingArea() {
        return processingArea;
    }

    public void setProcessingArea(ProductProcessingAreaDetail processingArea) {
        this.processingArea = processingArea;
    }
}
