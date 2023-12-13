package vn.softdreams.easypos.integration.easybooks88.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountResponse {

    @JsonProperty("id")
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
