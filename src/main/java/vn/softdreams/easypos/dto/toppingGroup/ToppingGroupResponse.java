package vn.softdreams.easypos.dto.toppingGroup;

public class ToppingGroupResponse {

    private Integer id;
    private String name;
    private Boolean requiredOptional;
    private Integer numberOption;
    private Integer numberProductLink;

    public ToppingGroupResponse() {}

    public ToppingGroupResponse(Integer id, String name, Boolean requiredOptional) {
        this.id = id;
        this.name = name;
        this.requiredOptional = requiredOptional;
    }

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

    public Integer getNumberOption() {
        return numberOption;
    }

    public void setNumberOption(Integer numberOption) {
        this.numberOption = numberOption;
    }

    public Integer getNumberProductLink() {
        return numberProductLink;
    }

    public void setNumberProductLink(Integer numberProductLink) {
        this.numberProductLink = numberProductLink;
    }
}
