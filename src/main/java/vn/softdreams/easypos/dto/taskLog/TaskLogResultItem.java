package vn.softdreams.easypos.dto.taskLog;

public class TaskLogResultItem {

    private Integer value;
    private String name;

    public TaskLogResultItem() {}

    public TaskLogResultItem(Integer value, String name) {
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
