package vn.softdreams.easypos.integration.easybooks88.queue.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ForgotPasswordTask {

    @JsonProperty
    private Integer comId;

    @JsonProperty
    private String username;

    @JsonProperty
    private String password;

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
