package vn.softdreams.easypos.dto.backup;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class ProductProductGroupDb {

    @JsonProperty("ID")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("code")
    private String code;

    @JsonProperty("color")
    private String color;

    @JsonProperty("description")
    private BigDecimal description;

    @JsonProperty("org_id")
    private BigDecimal org_id;

    @JsonProperty("create_time")
    private String create_time;

    @JsonProperty("update_time")
    private Integer update_time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public BigDecimal getDescription() {
        return description;
    }

    public void setDescription(BigDecimal description) {
        this.description = description;
    }

    public BigDecimal getOrg_id() {
        return org_id;
    }

    public void setOrg_id(BigDecimal org_id) {
        this.org_id = org_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public Integer getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Integer update_time) {
        this.update_time = update_time;
    }
}
