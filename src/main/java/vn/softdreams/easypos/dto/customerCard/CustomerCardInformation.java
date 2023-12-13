package vn.softdreams.easypos.dto.customerCard;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.math.BigDecimal;

public class CustomerCardInformation implements Serializable {

    private Integer cardId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer customerId;

    private Integer cardCode;
    private String cardName;
    private Integer point;
    private BigDecimal amount;
    private Integer rank;
    private BigDecimal accumValue;
    private BigDecimal redeemValue;

    public CustomerCardInformation() {}

    public CustomerCardInformation(
        Integer cardId,
        Integer customerId,
        Integer cardCode,
        String cardName,
        Integer point,
        BigDecimal amount,
        Integer rank
    ) {
        this.cardId = cardId;
        this.customerId = customerId;
        this.cardCode = cardCode;
        this.cardName = cardName;
        this.point = point;
        this.amount = amount;
        this.rank = rank;
    }

    public CustomerCardInformation(
        Integer cardId,
        Integer customerId,
        Integer cardCode,
        String cardName,
        Integer point,
        BigDecimal amount,
        BigDecimal accumValue,
        BigDecimal redeemValue
    ) {
        this.cardId = cardId;
        this.customerId = customerId;
        this.cardCode = cardCode;
        this.cardName = cardName;
        this.point = point;
        this.amount = amount;
        this.accumValue = accumValue;
        this.redeemValue = redeemValue;
    }

    public Integer getCardId() {
        return cardId;
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }

    public Integer getCardCode() {
        return cardCode;
    }

    public void setCardCode(Integer cardCode) {
        this.cardCode = cardCode;
    }

    public String getCardName() {
        return cardName != null ? cardName.trim() : null;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public Integer getPoint() {
        return point != null ? point : 0;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public BigDecimal getAmount() {
        return amount != null ? amount : BigDecimal.ZERO;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getAccumValue() {
        return accumValue;
    }

    public void setAccumValue(BigDecimal accumValue) {
        this.accumValue = accumValue;
    }

    public BigDecimal getRedeemValue() {
        return redeemValue;
    }

    public void setRedeemValue(BigDecimal redeemValue) {
        this.redeemValue = redeemValue;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }
}
