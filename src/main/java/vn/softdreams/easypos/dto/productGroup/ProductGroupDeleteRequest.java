package vn.softdreams.easypos.dto.productGroup;

public class ProductGroupDeleteRequest {

    private Integer id;
    private Integer comId;

    public ProductGroupDeleteRequest() {}

    public ProductGroupDeleteRequest(Integer id, Integer comId) {
        this.id = id;
        this.comId = comId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }
}
