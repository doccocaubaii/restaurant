package vn.softdreams.easypos.integration.easybooks88.queue.dto;

public class EBChangePasswordRequest {

    private String currentPassword;
    private String newPassword;
    private String confirmNewPassword;
    private String logoutAll;

    public EBChangePasswordRequest() {}

    public EBChangePasswordRequest(String currentPassword, String newPassword, String confirmNewPassword, String logoutAll) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.confirmNewPassword = confirmNewPassword;
        this.logoutAll = logoutAll;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }

    public String getLogoutAll() {
        return logoutAll;
    }

    public void setLogoutAll(String logoutAll) {
        this.logoutAll = logoutAll;
    }
}
