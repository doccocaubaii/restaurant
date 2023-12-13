package vn.softdreams.easypos.dto.message;

public class SMSIntegration {

    private String brandName;
    private String username;
    private String password;
    private Integer unicode;

    public SMSIntegration() {}

    public SMSIntegration(String brandName, String username, String password, Integer unicode) {
        this.brandName = brandName;
        this.username = username;
        this.password = password;
        this.unicode = unicode;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
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

    public Integer getUnicode() {
        return unicode;
    }

    public void setUnicode(Integer unicode) {
        this.unicode = unicode;
    }
}
