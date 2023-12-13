package vn.softdreams.easypos.dto.businessType;

import java.io.Serializable;

public class GetAllTransactionsResponse implements Serializable {

    private Integer id;
    private Integer comId;
    private String businessTypeCode;
    private String businessTypeName;
    private String type;

    public GetAllTransactionsResponse(Integer id, Integer comId, String businessTypeCode, String businessTypeName, String type) {
        this.id = id;
        this.comId = comId;
        this.businessTypeCode = businessTypeCode;
        this.businessTypeName = businessTypeName;
        this.type = type;
    }

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

    public String getBusinessTypeCode() {
        return businessTypeCode;
    }

    public void setBusinessTypeCode(String businessTypeCode) {
        this.businessTypeCode = businessTypeCode;
    }

    public String getBusinessTypeName() {
        return businessTypeName;
    }

    public void setBusinessTypeName(String businessTypeName) {
        this.businessTypeName = businessTypeName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
