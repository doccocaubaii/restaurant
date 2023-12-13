package vn.softdreams.easypos.dto.toppingGroup;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

public class ToppingGroupUpdateRequest {

    //    @NotNull(message = ExceptionConstants.TOPPING_GROUP_ID_NOT_NULL)
    private Integer id;

    @NotNull(message = ExceptionConstants.COMPANY_ID_NOT_NULL)
    Integer comId;

    @NotBlank(message = ExceptionConstants.TOPPING_GROUP_NAME_NOT_NULL)
    private String name;

    @NotNull(message = ExceptionConstants.TOPPING_PRODUCT_NOT_NULL)
    @Valid
    private List<ProductToppingRequest> products;

    private Boolean requiredOptional;

    public ToppingGroupUpdateRequest() {}

    public ToppingGroupUpdateRequest(
        Integer id,
        Integer comId,
        String name,
        List<ProductToppingRequest> products,
        Boolean requiredOptional
    ) {
        this.id = id;
        this.comId = comId;
        this.name = name;
        this.products = products;
        this.requiredOptional = requiredOptional;
    }

    public static class ProductToppingRequest implements Serializable {

        @NotNull(message = ExceptionConstants.PRODUCT_ID_NOT_NULL)
        private Integer id;

        @NotBlank(message = ExceptionConstants.PRODUCT_NAME_NOT_NULL)
        private String name;

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

    public List<ProductToppingRequest> getProducts() {
        return products;
    }

    public void setProducts(List<ProductToppingRequest> products) {
        this.products = products;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getRequiredOptional() {
        return requiredOptional;
    }

    public void setRequiredOptional(Boolean requiredOptional) {
        this.requiredOptional = requiredOptional;
    }
}
