package vn.softdreams.easypos.dto.card;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

public class CarPolicySaveRequest implements Serializable {

    private Integer id;

    @NotNull(message = ExceptionConstants.COMPANY_ID_NOT_NULL)
    private Integer comId;

    private String note;

    @Min(value = 1, message = ExceptionConstants.CARD_POLICY_UPGRADE_TYPE_INVALID)
    @Max(value = 2, message = ExceptionConstants.CARD_POLICY_UPGRADE_TYPE_INVALID)
    private Integer upgradeType;

    @NotNull(message = ExceptionConstants.CARD_POLICY_CONDITIONS_NOT_NULL)
    @Valid
    private List<CardPolicyConditions> conditions;

    @NotBlank(message = ExceptionConstants.CARD_POLICY_START_DATE_NOT_NULL)
    private String fromDate;

    @Min(value = 0, message = ExceptionConstants.CARD_POLICY_UPGRADE_TIME_INVALID)
    private Double upgradeTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<CardPolicyConditions> getConditions() {
        return conditions;
    }

    public void setConditions(List<CardPolicyConditions> conditions) {
        this.conditions = conditions;
    }

    public Double getUpgradeTime() {
        return upgradeTime;
    }

    public void setUpgradeTime(Double upgradeTime) {
        this.upgradeTime = upgradeTime;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public Integer getUpgradeType() {
        return upgradeType;
    }

    public void setUpgradeType(Integer upgradeType) {
        this.upgradeType = upgradeType;
    }
}
