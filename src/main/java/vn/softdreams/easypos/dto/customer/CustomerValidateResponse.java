package vn.softdreams.easypos.dto.customer;

import java.io.Serializable;
import java.util.Map;

public class CustomerValidateResponse extends CustomerCreateRequest implements Serializable {

    private Map<Integer, String> messageErrorMap;
    private Boolean status = false;

    public CustomerValidateResponse() {}

    public CustomerValidateResponse(
        Integer comId,
        String name,
        String code2,
        String address,
        String phoneNumber,
        String email,
        String taxCode,
        String idNumber,
        String description,
        Map<Integer, String> messageErrorMap,
        Boolean status
    ) {
        super(comId, name, code2, address, phoneNumber, email, taxCode, idNumber, description);
        this.messageErrorMap = messageErrorMap;
        this.status = status;
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
