package vn.softdreams.easypos.dto.company;

public class CompanyResultItem {

    private Integer value;
    private String name;

    public CompanyResultItem() {}

    public CompanyResultItem(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
