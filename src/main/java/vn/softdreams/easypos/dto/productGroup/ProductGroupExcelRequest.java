package vn.softdreams.easypos.dto.productGroup;

import java.io.Serializable;
import java.util.List;

public class ProductGroupExcelRequest implements Serializable {

    private Integer comId;
    private List<ProductGroupExcelDetail> data;

    public static class ProductGroupExcelDetail {

        private String name;
        private String description;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public List<ProductGroupExcelDetail> getData() {
        return data;
    }

    public void setData(List<ProductGroupExcelDetail> data) {
        this.data = data;
    }
}
