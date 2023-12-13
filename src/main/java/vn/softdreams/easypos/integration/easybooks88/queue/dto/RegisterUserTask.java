package vn.softdreams.easypos.integration.easybooks88.queue.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class RegisterUserTask implements Serializable {

    @JsonProperty
    private Integer comId;

    @JsonProperty
    private Integer userId;

    @JsonProperty
    private String password;

    public RegisterUserTask() {}

    public RegisterUserTask(Integer comId, Integer userId, String password) {
        this.comId = comId;
        this.userId = userId;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
