package vn.softdreams.easypos.dto.invoice;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class DeclarationRequest implements Serializable {

    @JsonProperty("option")
    private Integer option;

    @JsonProperty("key")
    private String key;

    public DeclarationRequest() {}

    public DeclarationRequest(Integer option, String key) {
        this.option = option;
        this.key = key;
    }

    public Integer getOption() {
        return option;
    }

    public void setOption(Integer option) {
        this.option = option;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
