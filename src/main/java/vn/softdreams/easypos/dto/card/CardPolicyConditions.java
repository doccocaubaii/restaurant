package vn.softdreams.easypos.dto.card;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

public class CardPolicyConditions implements Serializable {

    @NotNull(message = ExceptionConstants.CARD_POLICY_CARD_ID_NOT_NULL)
    private Integer cardId;

    private String cardName;

    //    @NotNull(message = ExceptionConstants.CARD_POLICY_ACCUM_VALUE_NOT_NULL)
    @Min(value = 0, message = ExceptionConstants.CARD_POLICY_ACCUM_VALUE_INVALID)
    private BigDecimal accumValue;

    //    @NotNull(message = ExceptionConstants.CARD_POLICY_REDEEM_VALUE_NOT_NULL)
    @Min(value = 0, message = ExceptionConstants.CARD_POLICY_REDEEM_VALUE_INVALID)
    private BigDecimal redeemValue;

    //    @NotNull(message = ExceptionConstants.CARD_POLICY_UPGRADE_VALUE_NOT_NULL)
    @Min(value = 0, message = ExceptionConstants.CARD_POLICY_UPGRADE_VALUE_INVALID)
    private BigDecimal upgradeValue;

    private String desc;
    private Double upgradeTime;
    private Boolean checked;

    @NotNull(message = ExceptionConstants.CARD_POLICY_CARD_IS_DEFAULT_NOT_NULL)
    private Boolean isDefault;

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public Integer getCardId() {
        return cardId;
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
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

    public BigDecimal getUpgradeValue() {
        return upgradeValue;
    }

    public void setUpgradeValue(BigDecimal upgradeValue) {
        this.upgradeValue = upgradeValue;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Double getUpgradeTime() {
        return upgradeTime;
    }

    public void setUpgradeTime(Double upgradeTime) {
        this.upgradeTime = upgradeTime;
    }
}
