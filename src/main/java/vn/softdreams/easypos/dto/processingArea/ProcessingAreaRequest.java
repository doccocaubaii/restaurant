package vn.softdreams.easypos.dto.processingArea;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class ProcessingAreaRequest {

    private Integer id;
    private Integer comId;

    @NotBlank(message = ExceptionConstants.PROCESSING_AREA_NAME_NOT_BLANK)
    private String name;

    private Integer setting;
    private Integer active;
    private List<Integer> listProduct;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSetting() {
        return setting;
    }

    public void setSetting(Integer setting) {
        this.setting = setting;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public List<Integer> getListProduct() {
        return listProduct;
    }

    public void setListProduct(List<Integer> listProduct) {
        this.listProduct = listProduct;
    }
}
