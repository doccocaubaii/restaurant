package vn.softdreams.easypos.integration.easybooks88.queue.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class AccountingObjectTask implements Serializable {

    @JsonProperty
    private String comId;

    @JsonProperty
    private String accountingObjectId;

    @JsonProperty
    private String type;

    public AccountingObjectTask() {}

    public AccountingObjectTask(String comId, String accountingObjectId, String type) {
        this.comId = comId;
        this.accountingObjectId = accountingObjectId;
        this.type = type;
    }

    public String getComId() {
        return comId;
    }

    public void setComId(String comId) {
        this.comId = comId;
    }

    public String getAccountingObjectId() {
        return accountingObjectId;
    }

    public void setAccountingObjectId(String accountingObjectId) {
        this.accountingObjectId = accountingObjectId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
