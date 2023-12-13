package vn.softdreams.easypos.dto.productGroup;

import java.io.Serializable;
import java.util.Map;

public class ProductGroupValidateResponse implements Serializable {

    private String name;
    private String description;

    private Map<Integer, String> messageErrorMap;
    private Boolean status = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<Integer, String> getMessageErrorMap() {
        return messageErrorMap;
    }

    public void setMessageErrorMap(Map<Integer, String> messageErrorMap) {
        this.messageErrorMap = messageErrorMap;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
