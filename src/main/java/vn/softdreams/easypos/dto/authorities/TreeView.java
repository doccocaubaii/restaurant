package vn.softdreams.easypos.dto.authorities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dungvm
 */
public class TreeView implements Serializable {

    private String text;
    private String value;
    private boolean disabled;
    private boolean checked;
    private boolean collapsed;
    private List<TreeView> children;

    public TreeView(String text, String value, boolean disabled, boolean checked, boolean collapsed) {
        this.text = text;
        this.value = value;
        this.disabled = disabled;
        this.checked = checked;
        this.collapsed = collapsed;
        children = new ArrayList<>();
    }

    public TreeView() {}

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isCollapsed() {
        return collapsed;
    }

    public void setCollapsed(boolean collapsed) {
        this.collapsed = collapsed;
    }

    public List<TreeView> getChildren() {
        return children;
    }

    public void setChildren(List<TreeView> children) {
        this.children = children;
    }

    public void addChild(TreeView child) {
        this.children.add(child);
    }
}
