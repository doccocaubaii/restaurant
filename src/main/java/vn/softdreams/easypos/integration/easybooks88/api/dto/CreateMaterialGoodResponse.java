package vn.softdreams.easypos.integration.easybooks88.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateMaterialGoodResponse {

    @JsonProperty
    private int status;

    @JsonProperty
    private String message;

    @JsonProperty
    private Integer data;

    public CreateMaterialGoodResponse() {}

    public CreateMaterialGoodResponse(int status, String message, Integer data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getData() {
        return data;
    }

    public void setData(Integer data) {
        this.data = data;
    }
}
