package vn.softdreams.easypos.integration.easybooks88.queue.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChangePasswordTask {

    @JsonProperty
    private Integer comId;

    @JsonProperty
    private String username;

    @JsonProperty
    private String oldPassword;

    @JsonProperty
    private String newPassword;

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

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
