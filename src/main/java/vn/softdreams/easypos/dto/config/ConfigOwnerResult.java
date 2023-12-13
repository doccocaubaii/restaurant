package vn.softdreams.easypos.dto.config;

public class ConfigOwnerResult {

    private String code;
    private String value;
    private String description;

    public ConfigOwnerResult(String code, String value, String description) {
        this.code = code;
        this.value = value;
        this.description = description;
    }

    public String getCode() {
        return code.trim();
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value == null ? null : value.trim();
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
