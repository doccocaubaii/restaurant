package vn.softdreams.easypos.dto.card;

import java.io.Serializable;
import java.util.List;

public class CardPolicyResponse implements Serializable {

    private Integer comId;
    private Integer id;
    private Integer upgradeType;
    private List<CardPolicyConditions> conditions;
    private String fromDate;

    public CardPolicyResponse(Integer comId, Integer id, Integer upgradeType, List<CardPolicyConditions> conditions, String fromDate) {
        this.comId = comId;
        this.id = id;
        this.upgradeType = upgradeType;
        this.conditions = conditions;
        this.fromDate = fromDate;
    }

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public CardPolicyResponse() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUpgradeType() {
        return upgradeType;
    }

    public void setUpgradeType(Integer upgradeType) {
        this.upgradeType = upgradeType;
    }

    public List<CardPolicyConditions> getConditions() {
        return conditions;
    }

    public void setConditions(List<CardPolicyConditions> conditions) {
        this.conditions = conditions;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }
}
