package vn.softdreams.easypos.dto.customerCard;

import java.math.BigDecimal;

public class CustomerCardRankItem {

    private Integer cardId;
    private Integer rank;
    private BigDecimal upgradeValue;
    private Double upgradeTime;

    public CustomerCardRankItem(Integer cardId, Integer rank, BigDecimal upgradeValue, Double upgradeTime) {
        this.cardId = cardId;
        this.rank = rank;
        this.upgradeValue = upgradeValue;
        this.upgradeTime = upgradeTime;
    }

    public Double getUpgradeTime() {
        return upgradeTime;
    }

    public void setUpgradeTime(Double upgradeTime) {
        this.upgradeTime = upgradeTime;
    }

    public Integer getCardId() {
        return cardId;
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public BigDecimal getUpgradeValue() {
        return upgradeValue != null ? upgradeValue : BigDecimal.ZERO;
    }

    public void setUpgradeValue(BigDecimal upgradeValue) {
        this.upgradeValue = upgradeValue;
    }
}
