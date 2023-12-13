package vn.softdreams.easypos.dto.product;

import java.util.List;

public class DeleteProductList {

    private Integer type;
    private Integer comId;
    private Boolean paramCheckAll;
    private Integer groupId;
    private String keyword;
    private List<Integer> ids;

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public Boolean getParamCheckAll() {
        return paramCheckAll;
    }

    public void setParamCheckAll(Boolean paramCheckAll) {
        this.paramCheckAll = paramCheckAll;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
