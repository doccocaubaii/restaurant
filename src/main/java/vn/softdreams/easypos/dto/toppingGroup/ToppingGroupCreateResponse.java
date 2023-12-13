package vn.softdreams.easypos.dto.toppingGroup;

public class ToppingGroupCreateResponse {

    private Integer id;
    private String name;
    private Boolean requiredOptional;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getRequiredOptional() {
        return requiredOptional;
    }

    public void setRequiredOptional(Boolean requiredOptional) {
        this.requiredOptional = requiredOptional;
    }
}
