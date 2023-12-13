package vn.softdreams.easypos.dto.inventory;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class InventoryCommonStats implements Serializable {

    private Integer comId;
    private String keyword;
    private BigDecimal totalOnHand;
    private BigDecimal totalValue;
    private String createTime;
    private List<InventoryCommonStatsDetail> detail;

    public static class InventoryCommonStatsDetail {

        private Integer id;
        private String code;
        private String name;
        private BigDecimal onHand;
        private BigDecimal purchasePrice;
        private BigDecimal value;

        public InventoryCommonStatsDetail(
            Integer id,
            String code,
            String name,
            BigDecimal onHand,
            BigDecimal purchasePrice,
            BigDecimal value
        ) {
            this.id = id;
            this.code = code;
            this.name = name;
            this.onHand = onHand;
            this.purchasePrice = purchasePrice;
            this.value = value;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public BigDecimal getOnHand() {
            return onHand;
        }

        public void setOnHand(BigDecimal onHand) {
            this.onHand = onHand;
        }

        public BigDecimal getPurchasePrice() {
            return purchasePrice;
        }

        public void setPurchasePrice(BigDecimal purchasePrice) {
            this.purchasePrice = purchasePrice;
        }

        public BigDecimal getValue() {
            return value;
        }

        public void setValue(BigDecimal value) {
            this.value = value;
        }
    }

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public BigDecimal getTotalOnHand() {
        return totalOnHand;
    }

    public void setTotalOnHand(BigDecimal totalOnHand) {
        this.totalOnHand = totalOnHand;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public List<InventoryCommonStatsDetail> getDetail() {
        return detail;
    }

    public void setDetail(List<InventoryCommonStatsDetail> detail) {
        this.detail = detail;
    }
}
