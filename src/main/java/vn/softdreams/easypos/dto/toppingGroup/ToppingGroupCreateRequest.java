package vn.softdreams.easypos.dto.toppingGroup;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

public class ToppingGroupCreateRequest {

    @NotNull(message = ExceptionConstants.COMPANY_ID_NOT_NULL)
    Integer comId;

    @NotNull(message = ExceptionConstants.TOPPING_GROUP_NAME_NOT_NULL)
    @NotBlank(message = ExceptionConstants.TOPPING_GROUP_NAME_NOT_NULL)
    private String name;

    @NotNull(message = ExceptionConstants.TOPPING_PRODUCT_NOT_NULL)
    private List<Integer> products;

    public ToppingGroupCreateRequest() {}

    public ToppingGroupCreateRequest(Integer comId, String name, List<Integer> products) {
        this.comId = comId;
        this.name = name;
        this.products = products;
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

    public List<Integer> getProducts() {
        return products;
    }

    public void setProducts(List<Integer> products) {
        this.products = products;
    }
}
