package vn.softdreams.easypos.dto.product;

import java.io.Serializable;
import java.util.List;

public class ProductExcelRequest implements Serializable {

    private Integer comId;
    private List<ProductExcelResponse> data;

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public List<ProductExcelResponse> getData() {
        return data;
    }

    public void setData(List<ProductExcelResponse> data) {
        this.data = data;
    }
}
